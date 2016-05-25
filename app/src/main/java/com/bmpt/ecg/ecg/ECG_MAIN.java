package com.bmpt.ecg.ecg;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class ECG_MAIN extends Activity {
    private SurfaceHolder sh;
    private final Paint paint = new Paint();
    private final Paint paintGrid = new Paint();
    public byte[] controlOutData = new byte[]{1, 0, 0, 0, 0, 0, 0, 0};



    int topMargin = 0;
    int leftMargin = 0;
    int bottomMargin = 170;

    private int canvasWidth = 960;
    private int canvasHeight = 800;



    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patientview);
        SurfaceView surface=(SurfaceView)findViewById(R.id.ecggraph);
        paintGrid.setColor(Color.GRAY);
        paintGrid.setStyle(Paint.Style.STROKE);

surface.getHolder().addCallback(new Callback() {



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = new Canvas();
        canvas = holder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        for (int i = leftMargin; i <= canvasWidth - leftMargin; i += 20) {
            canvas.drawLine(i, topMargin, i, canvasHeight - bottomMargin,
                    paintGrid);
        }
        for (int i = topMargin; i <= canvasHeight - bottomMargin; i += 20) {
            canvas.drawLine(leftMargin, i, canvasWidth - leftMargin, i,
                    paintGrid);
        }

        holder.unlockCanvasAndPost(canvas);




    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {


    }
});


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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Toast.makeText(getBaseContext(), "Lead " + parent.getItemIdAtPosition(position + 1) + " is selected", Toast.LENGTH_SHORT).show();
                }
                if (position == 1) {
                    Toast.makeText(getBaseContext(), "Lead " + parent.getItemIdAtPosition(position + 1) + " is selected", Toast.LENGTH_SHORT).show();
                }
                if (position == 2) {
                    Toast.makeText(getBaseContext(), "Lead " + parent.getItemIdAtPosition(position + 1) + " is selected", Toast.LENGTH_SHORT).show();
                }
                if (position == 3) {
                    Toast.makeText(getBaseContext(), "Lead aVR is selected", Toast.LENGTH_SHORT).show();

                }
                if (position == 4) {
                    Toast.makeText(getBaseContext(), "Lead aVL is selected", Toast.LENGTH_SHORT).show();

                }
                if (position == 5) {
                    Toast.makeText(getBaseContext(), "Lead aVF is selected", Toast.LENGTH_SHORT).show();

                }
                if (position == 6) {
                    Toast.makeText(getBaseContext(), "Lead V1 is selected", Toast.LENGTH_SHORT).show();

                }
                if (position == 7) {
                    Toast.makeText(getBaseContext(), "Lead V2 is selected", Toast.LENGTH_SHORT).show();

                }
                if (position == 8) {
                    Toast.makeText(getBaseContext(), "Lead V3 is selected", Toast.LENGTH_SHORT).show();

                }
                if (position == 9) {
                    Toast.makeText(getBaseContext(), "Lead V4 is selected", Toast.LENGTH_SHORT).show();

                }
                if (position == 10) {
                    Toast.makeText(getBaseContext(), "Lead V5 is selected", Toast.LENGTH_SHORT).show();

                }
                if (position == 11) {
                    Toast.makeText(getBaseContext(), "Lead V6 is selected", Toast.LENGTH_SHORT).show();

                }
                if (position == 12) {
                    Toast.makeText(getBaseContext(), "Extended Lead II is selected", Toast.LENGTH_SHORT).show();

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
        //ActionBar actionBar=getActionBar();
        //actionBar.show();
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

    public void startbuttonclicked(View v) {


        final Button startbutton = (Button) findViewById(R.id.startbutton);
        final Button combinedecg = (Button) findViewById(R.id.combinedecgtrace);
        final int status = (Integer) v.getTag();


        if (status == 1) {
            combinedecg.setEnabled(false);
            startbutton.setText("Stop");
            v.setTag(0);
        } else {
            startbutton.setText("Start");
            v.setTag(1);


            combinedecg.setEnabled(true);

            // handler.
            //SystemClock.sleep(7000);

        }

    }



}







