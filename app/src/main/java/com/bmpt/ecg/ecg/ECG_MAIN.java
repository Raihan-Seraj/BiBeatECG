package com.bmpt.ecg.ecg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.io.BufferedWriter;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * This is the main class of the program. It contains the necessary USB communication, Plotting of the graph,
 * displaying the graph in realtime and displaying the data correspoding to 4 seconds in the footprint.
 * I have used an external library graphview, to plot the data. Our device uses interrupt transfer to
 * transfer 8 bytes of data every 10ms. In android we use bulk transfer method, and have obtained the interrupt
 * transfer in a similar manner. USB host communication is used, and control transfer protocal is used
 * to send the command to the ECG machine to change the lead. USB permission is being added to androidmanifest.xml
 * to provide permission for the USB.
 * for setting up the USB connection I followed the documentation from  https://developer.android.com/guide/topics/connectivity/usb/host.html
 */


public class ECG_MAIN extends Activity  {

    private static final byte[] receivedDataInt = new byte[8];// Initialising an array for storing 8 bytes of data each millisecond
    private LineGraphSeries<DataPoint> mSeries1;//initialising graphview library for plotting series line data for realtime view
    private LineGraphSeries<DataPoint> mSeries2;// initialising graphview library for plotting series line data for footprint view
    private float graph1LastXValue = 0;
    //GraphView graph;
   // private Runnable mTimer1;
    GraphView ecggraphfootprint;// declaring the graphview object
    Date dNow = new Date();// declaring the variable to store the date in dd/mm/yy format for the file name so that lead data can be saved in them
    String today = String.format("%tB_%<te_%<tY", dNow);// declaring the format of the day
    String monthFolder = String.format("%tB", dNow );// we declared the name of the folder according to the month so that a single month's file can be saved in them
    String yearFolder = String.format("%tY", dNow );// we also create a year folder.
    File root = android.os.Environment.getExternalStorageDirectory();// getting external storage permission.
int k=0;


    int id=0;

    float footprintlastXvalue=0;// this float will contain the last x value in the series
    int a=0,b=0;

    //private final Paint paint = new Paint();
   // private final Paint paintGrid = new Paint();
    public byte[] controlOutData = new byte[]{1,0,0,0,0,0,0,0};// setting the byte for control transfer method, 1, represents data is being transfered from phone to the machine
    public byte[] Data=new byte[]{1,0,0,0,0,0,0,0};// setting the byte for control transfer method
    //SurfaceHolder sh;
    private volatile boolean DeviceAttached = false;// boolean to determine whether the device is attached or not
    private boolean IntClamed = false;// boolean for using claiming interface

    //private static final byte[] buffer = new byte[4096];//
    private static final int[] receivedData = new int[4096]; //array size of the data to be received
    private int [] dataforsd=new int[4096];// array for holding the data to be saved in the external storage
    UsbDevice mDdevice;// declaring USB device
    UsbInterface intf;//declaring interface of the usb device
    UsbEndpoint mEndpointIntr;// declaring the end point of the usb device
    //UsbEndpoint epin;
    UsbDeviceConnection mConnection;// USB connection variable declared
    private UsbManager mUsbManager;// USB manager variable declared
   // private BufferedReader in = null;
    private BufferedWriter out = null; // buffer
    private BufferedWriter interout=null;


    private volatile boolean startclicked;// boolean to determine whether the start_button is clicked or not
 startrecording runner1= new startrecording();// starting startrecording thread
    Spinner spinner;// declaring the spinner, which is the combo button that will allow the user to select the lead
    ArrayAdapter<CharSequence> adapter;// array adapter is used to declare the list that the combo button will contain

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String title;
        SharedPreferences pref=getSharedPreferences("Organisation_Name",Context.MODE_PRIVATE);// shared preference is used to fetch the last saved organisation name that is being put
        title=pref.getString("OrgName","1");//fetcing the organisation name from OrgName database
        getActionBar().setTitle(title);// setting the title of the action bar with the organisation name


        super.onCreate(savedInstanceState);
        setContentView(R.layout.patientview);// setting the XML view
      final GraphView graph=(GraphView) findViewById(R.id.graphview);// pointing out the graphview
        mSeries1 = new LineGraphSeries<DataPoint>();// declaring the type of graph to be plotted that is line graph which will contain series data
        mSeries1.setThickness(1);// setting the thickness of the plot
        graph.addSeries(mSeries1);// adding the plot
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);// diabling the vertical label, change it to true to enable them
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);// disabling the horizontal label, change it to true to show them
        graph.getGridLabelRenderer().setHighlightZeroLines(true);// we specifically highlight x axis and y axis
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);// setting grid styles which is none
        graph.setBackgroundColor(Color.WHITE);// setting background color of the plot
        graph.setKeepScreenOn(true);// the screen will be kept on when the gaph is being plotted

        //graph.getGridLabelRenderer().setNumVerticalLabels(2);

       Viewport viewport = graph.getViewport();// viewport is used to determine how the plot will be viewed
        viewport.setYAxisBoundsManual(true);// we are manually setting the axis boundary values
        viewport.setMinY(-150);// minimum Y axis value
        viewport.setMaxY(150);// minimum X axis value


        ecggraphfootprint=(GraphView)findViewById(R.id.ecggraphfootprint);// declaring the graphview for plotting footprint data
        ecggraphfootprint.getGridLabelRenderer().setNumHorizontalLabels(5);// the horizontal axis will contain 5 labels
        ecggraphfootprint.getGridLabelRenderer().setNumVerticalLabels(5);// vertical axis will contain 5 labels
        ecggraphfootprint.getGridLabelRenderer().setGridColor(Color.BLACK);// grid color is being set

        Viewport ecgfootprintviewport=ecggraphfootprint.getViewport();// how the footprint plot will be viewed is handled

        ecgfootprintviewport.setYAxisBoundsManual(true);// setting manual boundary values to Y axis
        ecgfootprintviewport.setXAxisBoundsManual(true);// setting manual boundary values to X axis
        ecgfootprintviewport.setMinX(0);// minimum value of X in the x axis
        ecgfootprintviewport.setMaxX(4);// maximum value of X in the X axis
        ecgfootprintviewport.setMinY(-150);// minimum Y value
        ecgfootprintviewport.setMaxY(150);// maximum value of Y
        ecgfootprintviewport.setScalable(true);// this allows for horizontal panning when enabled
        ecgfootprintviewport.setScrollable(true);// this allows to scroll the graph horizontally
        ecggraphfootprint.getGridLabelRenderer().setHorizontalLabelsVisible(true);// horizontal axis is set visible
        ecggraphfootprint.getGridLabelRenderer().setVerticalLabelsVisible(true);//vertical axis is set to be visible
        ecggraphfootprint.getGridLabelRenderer().setHighlightZeroLines(false);// we donot highlight the zerolines





        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);// setting the USB manager service

        USB_CONNECTION(); //Calling method to setup USB connection


        final Button startbutton = (Button) findViewById(R.id.startbutton);// declaring the Start Button
        final Button combinedecg = (Button) findViewById(R.id.combinedecgtrace);// Declaring combined ECG button
        combinedecg.setEnabled(false);// Combined ECG button is disabled when the plotting is continued
        startbutton.setTag(1);// tag set to 1 when the start button is clicked
        startbutton.setText("Start");// text is set to start. We are using only 1 button for both the start and stop functionality

        int item_selection;
        SharedPreferences perfs = getSharedPreferences("optionsvalue", MODE_PRIVATE);
        item_selection = perfs.getInt("value", 0);


        spinner = (Spinner) findViewById(R.id.selectcenterspinner);//next lead combo button declared
        adapter = ArrayAdapter.createFromResource(this, R.array.lead_choices, R.layout.spinner_layout);// we defined the layout to be used
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// use the layout as dropdown format
        spinner.setAdapter(adapter);// user adapter to delare what to include in the dropdown box

        /**
         * The following code describes the function that should be carried out when each of the items
         * in the combo box is being clicked. The indexing starts from zero so each position corresponds to index+1 lead number
         * up to lead 3;
         */
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Toast.makeText(getBaseContext(), "Lead " + parent.getItemIdAtPosition(position + 1) + " is selected", Toast.LENGTH_SHORT).show();// Displaying the lead that is selected
                    Data[0]=1;// sending the data via control transfer to the ECG machine sending the data only through the first bit
                    sendCommand(Data);// using send command function to control data to the ECG machine

                }
                if (position == 1) {
                    Toast.makeText(getBaseContext(), "Lead " + parent.getItemIdAtPosition(position + 1) + " is selected", Toast.LENGTH_SHORT).show();// Displaying the lead that is selected

                    Data[0]=2;// sending the data via control transfer to the ECG machine sending the data only through the first bit
                    sendCommand(Data);// using send command function to control data to the ECG machine
                }
                if (position == 2) {
                    Toast.makeText(getBaseContext(), "Lead " + parent.getItemIdAtPosition(position + 1) + " is selected", Toast.LENGTH_SHORT).show();// Displaying the lead that is selected
                      Data[0]=3;// sending the data via control transfer to the ECG machine sending the data only through the first bit
                    sendCommand(Data);// using send command function to control data to the ECG machine


                }
                if (position == 3) {
                    Toast.makeText(getBaseContext(), "Lead aVR is selected", Toast.LENGTH_SHORT).show();// Displaying the lead that is selected

                    Data[0]=4;// sending the data via control transfer to the ECG machine sending the data only through the first bit
                    sendCommand(Data);// using send command function to control data to the ECG machine
                }
                if (position == 4) {
                    Toast.makeText(getBaseContext(), "Lead aVL is selected", Toast.LENGTH_SHORT).show();// Displaying the lead that is selected

                    Data[0]=5;// sending the data via control transfer to the ECG machine sending the data only through the first bit
                    sendCommand(Data);// using send command function to control data to the ECG machine
                }
                if (position == 5) {
                    Toast.makeText(getBaseContext(), "Lead aVF is selected", Toast.LENGTH_SHORT).show();// Displaying the lead that is selected
                    Data[0]=6;// sending the data via control transfer to the ECG machine sending the data only through the first bit
                    sendCommand(Data);// using send command function to control data to the ECG machine

                }
                if (position == 6) {
                    Toast.makeText(getBaseContext(), "Lead V1 is selected", Toast.LENGTH_SHORT).show();// Displaying the lead that is selected
                    Data[0]=7;// sending the data via control transfer to the ECG machine sending the data only through the first bit
                    sendCommand(Data);// using send command function to control data to the ECG machine

                }
                if (position == 7) {
                    Toast.makeText(getBaseContext(), "Lead V2 is selected", Toast.LENGTH_SHORT).show();// Displaying the lead that is selected
                    Data[0]=8;// sending the data via control transfer to the ECG machine sending the data only through the first bit
                    sendCommand(Data);// using send command function to control data to the ECG machine

                }
                if (position == 8) {
                    Toast.makeText(getBaseContext(), "Lead V3 is selected", Toast.LENGTH_SHORT).show();// Displaying the lead that is selected
                    Data[0]=9;// sending the data via control transfer to the ECG machine sending the data only through the first bit
                    sendCommand(Data);// using send command function to control data to the ECG machine

                }
                if (position == 9) {
                    Toast.makeText(getBaseContext(), "Lead V4 is selected", Toast.LENGTH_SHORT).show();// Displaying the lead that is selected
                    Data[0]=10;// sending the data via control transfer to the ECG machine sending the data only through the first bit
                    sendCommand(Data);// using send command function to control data to the ECG machine


                }
                if (position == 10) {
                    Toast.makeText(getBaseContext(), "Lead V5 is selected", Toast.LENGTH_SHORT).show();// Displaying the lead that is selected
                    Data[0]=11;// sending the data via control transfer to the ECG machine sending the data only through the first bit
                    sendCommand(Data);// using send command function to control data to the ECG machine

                }
                if (position == 11) {
                    Toast.makeText(getBaseContext(), "Lead V6 is selected", Toast.LENGTH_SHORT).show();// Displaying the lead that is selected
                    Data[0]=12;// sending the data via control transfer to the ECG machine sending the data only through the first bit
                    sendCommand(Data);// using send command function to control data to the ECG machine

                }
                if (position == 12) {
                    Toast.makeText(getBaseContext(), "Extended Lead II is selected", Toast.LENGTH_SHORT).show();// Displaying the lead that is selected
                    Data[0]=13;// sending the data via control transfer to the ECG machine sending the data only through the first bit
                    sendCommand(Data);// using send command function to control data to the ECG machine

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
        inflater.inflate(R.menu.menu_ecg__main, menu);// the menu bar that will contain the options and settings is declared here
        return super.onCreateOptionsMenu(menu);

    }

    /**
     * The following code contains the code that will execute when each of the items in the menu bar is selected
     * we start a different activity when each of the items in the menubar is being selected
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {// getting the item id
            case R.id.settings_id:
                Intent i = new Intent(ECG_MAIN.this, optionslist.class);// setting the intent for the options list class
                startActivity(i);// starting the optionslist class


                break;

            case R.id.options_id:
                Intent j = new Intent(ECG_MAIN.this, optionslist2.class);// setting the intent for starting the optionslist2 activity
                startActivity(j);// starting the optionslist2 activity

                break;

        }
        return super.onOptionsItemSelected(item);
    }


    /**
     *
     * The following function is executed when the next button is clicked.We change the position of the lead by 1
     */
    public void nextbuttonclicked(View v) {


        spinner.setSelection(spinner.getSelectedItemPosition() + 1);// increasing the position of lead by 1

        if (spinner.getSelectedItemPosition() > 12) {

            spinner.setSelection(0);// when the position reaches 12 we again revert back to positon 0


        }


    }
    //The following code contains the function to be executed when the combined ECG button is clicked
public void combinedECGbuttonclicked(View v){

    Intent j = new Intent(ECG_MAIN.this, reportgeneration.class);// setting the intent for starting reportgeneration class
    startActivity(j);// starting reportgeneration class



}

    /**
     * The following function is executed when the start button is clicked. We start the thread that corresponds to
     * collecting the Data from ECG machine. We also start the UI thread that updates each time with the plot.
     * We use the same start button for both start and stop operation. When the app starts collecting data from the machine
     * we label the start button text as Stop indicating then the user will press it, it will stop collecting.
     * The combined ECG button is only enabled when the method of data collection is stopped.
     *
     */
    public void startbuttonclicked(View v) {


        final Button startbutton = (Button) findViewById(R.id.startbutton);// declaring start button
        final Button combinedecg = (Button) findViewById(R.id.combinedecgtrace);//initialising combinedecg button
        final int status = (Integer) v.getTag();// getting the tag for the start button


        if (status == 1) {
            combinedecg.setEnabled(false); //disabling the combined ecg button when the ECG is collecting data
            startbutton.setText("Stop");// labelling the same button as STOP
            v.setTag(0);// setting the tag to 0
            startclicked = true;// setting the boolean to be true
          startrecording runner1= new startrecording();// declaring runner1 thread
            runner1.start(); //starting the runner 1 thread



        } else {
            startbutton.setText("Start");//setting the text for the start button as Start when it is stopped
            v.setTag(1);// setting the tag to 1
            startclicked = false;// setting the boolean for the startclicked button to false
             runner1.stopthread();// stopping the runner thread

            combinedecg.setEnabled(true);// enabling the combined ECG button



        }

    }


    /**
     * The following function deals with the initialisation of the USB service that configures the
     * USB to collect data via Interrupt transfer. Notice that android doesnot have a native method
     * of Interrupt transfer, however using bulk transfer provides the same features as that of
     * interrupt transfer is this case
     */


   public void USB_CONNECTION() {

        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);// declaring USB manager


        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();// a hashmap that contains all the device list connected to USB

        for (UsbDevice device : deviceList.values()) {// scanning through the device list

            if (device.getVendorId() == 5824 && device.getProductId() == 1503) {// for our device Vendor id is 5824 and Product id is 1503

                mDdevice = device;// setting the device name as mDevice
                DeviceAttached = true;// setting device attached to be true

                intf = device.getInterface(0);// getting the 0th interface
                mEndpointIntr = intf.getEndpoint(0);// getting the 0th endpoint

                if (mEndpointIntr.getType() != UsbConstants.USB_ENDPOINT_XFER_INT) {// checking whether the endpoint is interupt type

                    Toast.makeText(getBaseContext(), "End point is not interrupt type", Toast.LENGTH_SHORT).show();// displaying message when the endpoint is not iterrupt type
                     }

                mConnection = mUsbManager.openDevice(mDdevice);//opening the device connection
                IntClamed = mConnection.claimInterface(intf, true);// claiming the interface, these are the steps that is needed to follow as per the documentation

                Toast.makeText(getBaseContext(), "USB CONNECTED ", Toast.LENGTH_SHORT).show();// displaying the message when the USb is connected

                break;


            }


        }
    }

    /**
     * The following function is called when the lead is changed and android device
     * sends the instruction to the ecg machine to change the lead. This function only executes
     * given that the USB is already attached.
     *
     *
     *0x21 = 00100001B
     Characteristics of request:
     D7: Data transfer direction
     0 = Host-to-device
     1 = Device-to-host
     D6...5: Type
     0 = Standard
     1 = Class
     2 = Vendor
     3 = Reserved
     D4...0: Recipient
     0 = Device
     1 = Interface
     2 = Endpoint
     3 = Other
     4...31 = Reserved

     0x21 = 0 0 1 0 0 0 0 1


     public int controlTransfer (
     int requestType,
     int request,
     int value,
     int index,
     byte[] buffer,
     int length,
     int timeout )

     request type for this transaction 0x21,
     Request request ID for this transaction 0x9,
     value value field for this transaction 0x200,
     index index field for this transaction 0,
     buffer buffer for data portion of transaction, or null if no data needs to be sent or received message,
     length the length of the data to send or receive message.length,
     timeout in milliseconds 0,
     */
        private void sendCommand(byte data[]){


                if(DeviceAttached){// checking  whether the device is attached


                    controlOutData=data;//setting control out data
                    //Send command via control transfer to end point 0
                    mConnection = mUsbManager.openDevice(mDdevice);//opeing the device
                    IntClamed = mConnection.claimInterface(intf, true);//claiming the interface

                 int x=  mConnection.controlTransfer(0x20,0x09,0x00,0x00,controlOutData,0x08,10000);// sending the request

                }




    }


    /**
     * We declare the thread that will collect data from the ECG device in background
     * there is another thread with in the thread that updates the UI in order to show the realtime plot
     */
    class startrecording extends Thread{
    public synchronized void run(){
        while (DeviceAttached&&startclicked) {// checking whether the device is attached and that the start button is clicked

            if (!IntClamed) { //if interface is not claimed properly by USB service we terminate
                System.exit(0);
            }

   synchronized (this) {// synchronising the thread with the inner thread
       mConnection.bulkTransfer(mEndpointIntr, receivedDataInt, receivedDataInt.length, 10000);//using bulk transfer which serves the same purpose as interrupt transfer
   }



            runOnUiThread(new Runnable() {// inner UI thread that updates the UI with the plot

                @Override
                public void run() {

                   addEntry();//calling addEntry function for adding entry to the graph
                   dataforfootprint(receivedDataInt);// calling dataforfootprint function

                }
            });

                try {

                    Thread.sleep(150);//putting the thread to sleep

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


        }




    }

//the following code describes the criterion for terminating the thread
    public void stopthread() {

        startclicked = false;// putting the the boolean start clicked to false, since the thread initially checks whether the start button is clicked so once the boolean is set to false the thread will not execute


        plotfootprintdata();// calling plotfootprintdata function.
    }


}

    // add data to the graph
    private synchronized void addEntry() {
if(k==8)// if the 8 byte array is maxed out
{
    k=0;// setting the value of k==0
}
        if(DeviceAttached=true)
        // here, we choose to display max 10 points on the viewport and we scroll to end
        {graph1LastXValue=graph1LastXValue+0.000125f;// setting the time division
        mSeries1.appendData(new DataPoint(graph1LastXValue, receivedDataInt[k]), true,90);// adding the last x value to the data and we display maximum of 90 datapoints at a time
        k++;}
    }



    private void dataforfootprint(byte footprint[]) {
//3200 data in 4 seconds, the footprint will display data for 4 seconds
        for (int n = 0; n < 8; n++) {


            receivedData[a] = footprint[n];// transferring each byte of data to footprint array

            a++;// increasing the index
            b++;//increasing the index
            if (a == 320) { //the length of the array is set to 320
                storeinsharedpreference(receivedData);// trying to store data in shared preference
                a = 0;
                plotfootprintdata();

//writeRecordedData();
            }
/*else if(b==4096){
                b=0;
               // writeRecordedData();
            }*/

        }

    }

    //function for plotting the footprint data
public void plotfootprintdata(){
    ecggraphfootprint.removeAllSeries();// when new data is added and plotted, the previous plotted data is removed
    DataPoint[] datafoot = new DataPoint[320];// adding 320 data

    float footprintx=0;
         for (int h=0;h<320;h++){





datafoot[h]=new DataPoint(footprintx,receivedData[h]);// declaring the datapoint for plotting
             footprintx=footprintx+0.0125f;// the step of the xaxis is since 8 bytes collected in 10 ms so 1 bye is 8/10 ms


         }
    mSeries2 = new LineGraphSeries<DataPoint>(datafoot);// declaring plot type as line graph series
    ecggraphfootprint.addSeries(mSeries2);// addint the data to series

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



  /* public void writeRecordedData()

   {
       SharedPreferences x = getSharedPreferences("PatientInfo", Context.MODE_PRIVATE);
       id = x.getInt("id", 1);
       if (isExternalStorageWritable() == true) {


           try {
               Toast.makeText(getBaseContext(), "Writing to External Storage ", Toast.LENGTH_SHORT).show();



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
   }*/

    /*
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }

        return false;
    }

*/
}



















