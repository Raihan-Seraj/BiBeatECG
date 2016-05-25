package com.bmpt.ecg.ecg;

/**
 * Created by Raihan on 13-Mar-16.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Doctor extends Activity {
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctorview);
        spinner=(Spinner)findViewById(R.id.selectcenterspinner);
        adapter=ArrayAdapter.createFromResource(this,R.array.select_center,R.layout.spinner_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Toast.makeText(getBaseContext(), "Center " + parent.getItemIdAtPosition(position + 1) + " is selected", Toast.LENGTH_SHORT).show();
                }
                if (position == 1) {
                    Toast.makeText(getBaseContext(), "Center " + parent.getItemIdAtPosition(position + 1) + " is selected", Toast.LENGTH_SHORT).show();
                }
                if (position == 2) {
                    Toast.makeText(getBaseContext(), "Center " + parent.getItemIdAtPosition(position + 1) + " is selected", Toast.LENGTH_SHORT).show();
                }
                if (position == 3) {
                    Toast.makeText(getBaseContext(), "Center " + parent.getItemIdAtPosition(position + 1) + " is selected", Toast.LENGTH_SHORT).show();

                }
                if (position == 4) {
                    Toast.makeText(getBaseContext(), "Center " + parent.getItemIdAtPosition(position + 1) + " is selected", Toast.LENGTH_SHORT).show();

                }
                if (position == 5) {
                    Toast.makeText(getBaseContext(), "Center " + parent.getItemIdAtPosition(position + 1) + " is selected", Toast.LENGTH_SHORT).show();

                }
                if (position == 6) {
                    Toast.makeText(getBaseContext(), "Center " + parent.getItemIdAtPosition(position + 1) + " is selected", Toast.LENGTH_SHORT).show();

                }
                if (position == 7) {
                    Toast.makeText(getBaseContext(), "Center " + parent.getItemIdAtPosition(position + 1) + " is selected", Toast.LENGTH_SHORT).show();

                }
                if (position == 8) {
                    Toast.makeText(getBaseContext(), "Center " + parent.getItemIdAtPosition(position + 1) + " is selected", Toast.LENGTH_SHORT).show();

                }
                if (position == 9) {
                    Toast.makeText(getBaseContext(), "Center " + parent.getItemIdAtPosition(position + 1) + " is selected", Toast.LENGTH_SHORT).show();

                }
                if (position == 10) {
                    Toast.makeText(getBaseContext(), "Center " + parent.getItemIdAtPosition(position + 1) + " is selected", Toast.LENGTH_SHORT).show();

                }
                if (position == 11) {
                    Toast.makeText(getBaseContext(), "Center " + parent.getItemIdAtPosition(position + 1) + " is selected", Toast.LENGTH_SHORT).show();

                }
                if (position == 12) {
                    Toast.makeText(getBaseContext(), "Center " + parent.getItemIdAtPosition(position + 1) + " is selected", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_ecg__main, menu);
        return super.onCreateOptionsMenu(menu);
        //ActionBar actionBar=getActionBar();
        //actionBar.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.settings_id:
                Intent i = new Intent(Doctor.this, optionslist.class);
                startActivity(i);


                break;

            case R.id.options_id:
                Intent j = new Intent(Doctor.this, optionslist2.class);
                startActivity(j);

                break;

        }
        return super.onOptionsItemSelected(item);
    }
    public void nextbuttonclicked(View v){


        spinner.setSelection(spinner.getSelectedItemPosition()+1);

        if (spinner.getSelectedItemPosition()>12)
        {

            spinner.setSelection(0);


        }


    }





}
