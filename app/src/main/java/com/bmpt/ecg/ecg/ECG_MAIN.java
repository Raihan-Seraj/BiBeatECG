package com.bmpt.ecg.ecg;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

//import android.content.Context;


public class ECG_MAIN extends Activity  {
    private static final String ACTION_USB_PERMISSION = "com.bmpt.ecg.ecg.USB_PERMISSION";
    PendingIntent mPermissionIntent;
    Context ctx;
    Canvas canvas;
    private static final byte[] receivedDataInt = new byte[8];
    private LineGraphSeries<DataPoint> mSeries1;
    private LineGraphSeries<DataPoint> mSeries2;
    private float graph1LastXValue = 0;
    GraphView graph;
    private Runnable mTimer1;
    GraphView ecggraphfootprint;
    Date dNow = new Date();
    String today = String.format("%tB_%<te_%<tY", dNow);
    String monthFolder = String.format("%tB", dNow );
    String yearFolder = String.format("%tY", dNow );
    File root = android.os.Environment.getExternalStorageDirectory();
int k=0;


    int id=0;

    float footprintlastXvalue=0;
    int a=0,b=0;
    int flagInterruptTrans;
    private final Paint paint = new Paint();
    private final Paint paintGrid = new Paint();
    public byte[] controlOutData = new byte[]{1,0,0,0,0,0,0,0};
    public byte[] Data=new byte[]{1,0,0,0,0,0,0,0};
    SurfaceHolder sh;
    private volatile boolean DeviceAttached = false;
    private boolean IntClamed = false;

    private static final byte[] buffer = new byte[4096];
    private static final int[] receivedData = new int[4096];
    private int [] dataforsd=new int[4096];
    UsbDevice mDdevice;
    UsbInterface intf;
    UsbEndpoint mEndpointIntr;
    UsbEndpoint epin;
    UsbDeviceConnection mConnection;
    private UsbManager mUsbManager;
    int topMargin = 0;
    int leftMargin = 0;
    int bottomMargin = 170;
    int intflagcontroltransfer;
    private int canvasWidth = 960;
    private int canvasHeight = 800;private BufferedReader in = null;
    private BufferedWriter out = null;
    private BufferedWriter interout=null;

    //recorddata recorddata = new recorddata();
    private volatile boolean startclicked;
 startrecording runner1= new startrecording();
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String title;
        SharedPreferences pref=getSharedPreferences("Organisation_Name",Context.MODE_PRIVATE);
        title=pref.getString("OrgName","1");
        getActionBar().setTitle(title);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.patientview);
      final GraphView graph=(GraphView) findViewById(R.id.graphview);
        mSeries1 = new LineGraphSeries<DataPoint>();
        mSeries1.setThickness(1);
        graph.addSeries(mSeries1);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setHighlightZeroLines(true);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph.setBackgroundColor(Color.WHITE);
        graph.setKeepScreenOn(true);
        //graph.getGridLabelRenderer().setNumHorizontalLabels(20);
        graph.getGridLabelRenderer().setNumVerticalLabels(2);

       Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(-150);
        viewport.setMaxY(150);
        ecggraphfootprint=(GraphView)findViewById(R.id.ecggraphfootprint);
        ecggraphfootprint.getGridLabelRenderer().setNumHorizontalLabels(5);
        ecggraphfootprint.getGridLabelRenderer().setNumVerticalLabels(5);
        ecggraphfootprint.getGridLabelRenderer().setGridColor(Color.BLACK);
        //ecggraphfootprint.getGridLabelRenderer().setGridStyle(Gr);

        Viewport ecgfootprintviewport=ecggraphfootprint.getViewport();
        //ecgfootprintviewport.color
        ecgfootprintviewport.setYAxisBoundsManual(true);
        ecgfootprintviewport.setXAxisBoundsManual(true);
        ecgfootprintviewport.setMinX(0);
        ecgfootprintviewport.setMaxX(4);
        ecgfootprintviewport.setMinY(-150);
        ecgfootprintviewport.setMaxY(150);
        ecgfootprintviewport.setScalable(true);
        ecgfootprintviewport.setScrollable(true);
        ecggraphfootprint.getGridLabelRenderer().setHorizontalLabelsVisible(true);
        ecggraphfootprint.getGridLabelRenderer().setVerticalLabelsVisible(true);
        ecggraphfootprint.getGridLabelRenderer().setHighlightZeroLines(false);
       // NumberFormat nf = NumberFormat.getInstance();
        //nf.setMinimumFractionDigits(9);
        //nf.setMinimumIntegerDigits(9);

      //  ecggraphfootprint.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(nf, nf));


        paintGrid.setColor(Color.GRAY);
        paintGrid.setStyle(Paint.Style.STROKE);

        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
USB_CONNECTION();


        final Button startbutton = (Button) findViewById(R.id.startbutton);
        final Button combinedecg = (Button) findViewById(R.id.combinedecgtrace);
        combinedecg.setEnabled(false);
        startbutton.setTag(1);
        startbutton.setText("Start");

        int item_selection;
        SharedPreferences perfs = getSharedPreferences("optionsvalue", MODE_PRIVATE);
        item_selection = perfs.getInt("value", 0);

        spinner = (Spinner) findViewById(R.id.selectcenterspinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.lead_choices, R.layout.spinner_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //setdevice();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Toast.makeText(getBaseContext(), "Lead " + parent.getItemIdAtPosition(position + 1) + " is selected", Toast.LENGTH_SHORT).show();
                    Data[0]=1;
                    sendCommand(Data);

                }
                if (position == 1) {
                    Toast.makeText(getBaseContext(), "Lead " + parent.getItemIdAtPosition(position + 1) + " is selected", Toast.LENGTH_SHORT).show();

                    Data[0]=2;
                    sendCommand(Data);
                }
                if (position == 2) {
                    Toast.makeText(getBaseContext(), "Lead " + parent.getItemIdAtPosition(position + 1) + " is selected", Toast.LENGTH_SHORT).show();
                      Data[0]=3;
                    sendCommand(Data);


                }
                if (position == 3) {
                    Toast.makeText(getBaseContext(), "Lead aVR is selected", Toast.LENGTH_SHORT).show();

                    Data[0]=4;
                    sendCommand(Data);
                }
                if (position == 4) {
                    Toast.makeText(getBaseContext(), "Lead aVL is selected", Toast.LENGTH_SHORT).show();

                    Data[0]=5;
                    sendCommand(Data);
                }
                if (position == 5) {
                    Toast.makeText(getBaseContext(), "Lead aVF is selected", Toast.LENGTH_SHORT).show();
                    Data[0]=6;
                    sendCommand(Data);

                }
                if (position == 6) {
                    Toast.makeText(getBaseContext(), "Lead V1 is selected", Toast.LENGTH_SHORT).show();
                    Data[0]=7;
                    sendCommand(Data);

                }
                if (position == 7) {
                    Toast.makeText(getBaseContext(), "Lead V2 is selected", Toast.LENGTH_SHORT).show();
                    Data[0]=8;
                    sendCommand(Data);

                }
                if (position == 8) {
                    Toast.makeText(getBaseContext(), "Lead V3 is selected", Toast.LENGTH_SHORT).show();
                    Data[0]=9;
                    sendCommand(Data);

                }
                if (position == 9) {
                    Toast.makeText(getBaseContext(), "Lead V4 is selected", Toast.LENGTH_SHORT).show();
                    Data[0]=10;
                    sendCommand(Data);


                }
                if (position == 10) {
                    Toast.makeText(getBaseContext(), "Lead V5 is selected", Toast.LENGTH_SHORT).show();
                    Data[0]=11;
                    sendCommand(Data);

                }
                if (position == 11) {
                    Toast.makeText(getBaseContext(), "Lead V6 is selected", Toast.LENGTH_SHORT).show();
                    Data[0]=12;
                    sendCommand(Data);

                }
                if (position == 12) {
                    Toast.makeText(getBaseContext(), "Extended Lead II is selected", Toast.LENGTH_SHORT).show();
                    Data[0]=13;
                    sendCommand(Data);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ecg__main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings_id:
                Intent i = new Intent(ECG_MAIN.this, optionslist.class);
                startActivity(i);


                break;

            case R.id.options_id:
                Intent j = new Intent(ECG_MAIN.this, optionslist2.class);
                startActivity(j);

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void nextbuttonclicked(View v) {


        spinner.setSelection(spinner.getSelectedItemPosition() + 1);

        if (spinner.getSelectedItemPosition() > 12) {

            spinner.setSelection(0);


        }


    }
public void combinedECGbuttonclicked(View v){

    Intent j = new Intent(ECG_MAIN.this, reportgeneration.class);
    startActivity(j);



}
    public void startbuttonclicked(View v) {


        final Button startbutton = (Button) findViewById(R.id.startbutton);
        final Button combinedecg = (Button) findViewById(R.id.combinedecgtrace);
        final int status = (Integer) v.getTag();


        if (status == 1) {
            combinedecg.setEnabled(false);
            startbutton.setText("Stop");
            v.setTag(0);
            startclicked = true;
          startrecording runner1= new startrecording();
            runner1.start();



        } else {
            startbutton.setText("Start");
            v.setTag(1);
            startclicked = false;
             runner1.stopthread();

            combinedecg.setEnabled(true);

            // handler.
            //SystemClock.sleep(7000);

        }

    }
















   public void USB_CONNECTION() {

        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);


        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();

        for (UsbDevice device : deviceList.values()) {

            if (device.getVendorId() == 5824 && device.getProductId() == 1503) {

                mDdevice = device;
                DeviceAttached = true;

                intf = device.getInterface(0);
                mEndpointIntr = intf.getEndpoint(0);
                //epin=intf.getEndpoint(1);
                if (mEndpointIntr.getType() != UsbConstants.USB_ENDPOINT_XFER_INT) {

                    Toast.makeText(getBaseContext(), "End point is not interrupt type", Toast.LENGTH_SHORT).show();
                     }

                mConnection = mUsbManager.openDevice(mDdevice);
                IntClamed = mConnection.claimInterface(intf, true);

                Toast.makeText(getBaseContext(), "USB CONNECTED ", Toast.LENGTH_SHORT).show();

                break;


            }


        }
    }


        private void sendCommand(byte data[]){


                if(DeviceAttached){

                   //int a=1;
                    controlOutData=data;
                    //Send command via control transfer to end point 0
                    mConnection = mUsbManager.openDevice(mDdevice);
                    IntClamed = mConnection.claimInterface(intf, true);
                    //int x = mConnection.bulkTransfer(mEndpointIntr,receivedDataInt, receivedDataInt.length, 500);
                 int x=  mConnection.controlTransfer(0x20,0x09,0x00,0x00,controlOutData,0x08,10000);
                   // Toast.makeText(getBaseContext(), "I The transfer is "+x/*+receivedDataInt[0]+receivedDataInt[1]+receivedDataInt[2]+receivedDataInt[3]+receivedDataInt[4]+receivedDataInt[5]+receivedDataInt[6]+receivedDataInt[7]*/, Toast.LENGTH_SHORT).show();

                }




    }


class startrecording extends Thread{
    public synchronized void run(){
        while (DeviceAttached&&startclicked) {

            if (!IntClamed) {
                System.exit(0);
            }

   synchronized (this) {
       mConnection.bulkTransfer(mEndpointIntr, receivedDataInt, receivedDataInt.length, 10000);
   }

                //if (receivedDataInt.length != 0)
                   // for (int i = 0; i < 8; i++) {
                     //   receivedData[i] = receivedDataInt[i]; //& 0xff;


                        //receivedData[4]=7;

                    //}

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                   addEntry();
                   dataforfootprint(receivedDataInt);

                }
            });

                try {

                    Thread.sleep(150);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


        }




    }


    public void stopthread() {

        startclicked = false;
        Toast.makeText(getBaseContext(),"The received data is "+receivedDataInt[0] +receivedDataInt[1]+receivedDataInt[2]+receivedDataInt[3]+receivedDataInt[4]+receivedDataInt[5]+receivedDataInt[6]+receivedDataInt[7],Toast.LENGTH_SHORT).show();
        Toast.makeText(getBaseContext(),"The status is ",Toast.LENGTH_SHORT).show();
        plotfootprintdata();
    }


}

    // add data to the graph
    private synchronized void addEntry() {
if(k==8)
{
    k=0;
}
        if(DeviceAttached=true)
        // here, we choose to display max 10 points on the viewport and we scroll to end
        {graph1LastXValue=graph1LastXValue+0.000125f;
        mSeries1.appendData(new DataPoint(graph1LastXValue, receivedDataInt[k]), true,90);
        k++;}
    }



    private void dataforfootprint(byte footprint[]) {
//3200 data in 4 seconds, the footprint will display data for 4 seconds
        for (int n = 0; n < 8; n++) {


            receivedData[a] = footprint[n];
            dataforsd[b]=footprint[n];
            a++;
            b++;
            if (a == 320) {
                storeinsharedpreference(receivedData);
                a = 0;
                plotfootprintdata();

//writeRecordedData();
            }
else if(b==4096){
                b=0;
                writeRecordedData();
            }

        }

    }

public void plotfootprintdata(){
    ecggraphfootprint.removeAllSeries();
    DataPoint[] datafoot = new DataPoint[320];

    float footprintx=0;
         for (int h=0;h<320;h++){





datafoot[h]=new DataPoint(footprintx,receivedData[h]);
             footprintx=footprintx+0.0125f;


         }
    mSeries2 = new LineGraphSeries<DataPoint>(datafoot);
    ecggraphfootprint.addSeries(mSeries2);

    }


    public void storeinsharedpreference(int shared[]){


        SharedPreferences sharedPreferences=getSharedPreferences("MyData",Context.MODE_PRIVATE);

      SharedPreferences.Editor dataeditor= sharedPreferences.edit();
      String conv2string=  Arrays.toString(shared);
       dataeditor.putString("datashared",conv2string.toString());

    }




  /*  public void writeRecordedData(){

isExternalStorageWritable();








    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.ge), albumName);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }*/



   public void writeRecordedData()

   {
       SharedPreferences x = getSharedPreferences("PatientInfo", Context.MODE_PRIVATE);
       id = x.getInt("id", 1);
       if (isExternalStorageWritable() == true) {


           try {


               File dir = new File(
                       root.getAbsolutePath()
                               + "/BiBeatECG/Data/"
                               + yearFolder + "/"
                               + monthFolder);
               dir.mkdirs();
               out = new BufferedWriter(
                       new FileWriter(new File(dir, id
                               + "_" + today + ".txt")));


               for (int i = 0; i < dataforsd.length; i++) {
                   out.append(dataforsd[i] + " ");
               }
               out.append(";");
               out.flush();

               Toast.makeText(getBaseContext(), "File saved ", Toast.LENGTH_SHORT).show();

           } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           }
           // out.close();


       } else {
           Toast.makeText(getBaseContext(), "External Storage Not Mounted. Writing to Internal Storage instead ", Toast.LENGTH_SHORT).show();



//If external storage is not there then the same procedure will be followed for writing files in the internal storage
           try {
               File myDir = new File(getCacheDir()
                       + "/BiBeatECG/Data/"
                       + yearFolder + "/"
                       + monthFolder);
               myDir.mkdir();
               interout = new BufferedWriter(
                       new FileWriter(new File(myDir, id + "_" + today + ".txt")));

               for (int i = 0; i < dataforsd.length; i++) {
                   interout.append(dataforsd[i] + " ");
               }
               interout.append(";");
               interout.flush();

               Toast.makeText(getBaseContext(), "File saved to internal storage ", Toast.LENGTH_SHORT).show();

           } catch (IOException e) {
               e.printStackTrace();
           }
       }
   }
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }

        return false;
    }


}



















