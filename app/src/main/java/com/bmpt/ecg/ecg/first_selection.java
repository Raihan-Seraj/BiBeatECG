package com.bmpt.ecg.ecg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by Raihan on 10-Mar-16.
 */
public class first_selection extends Activity {
    int item_selection=0;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstscreen);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup2);

    }



    public void next_doctor_patient_clicked(View v){

        RadioButton patientradiobutton = (RadioButton) findViewById(R.id.patientradiobutton);
        RadioButton doctorradiobutton = (RadioButton) findViewById(R.id.doctorradiobutton);
        if(patientradiobutton.isChecked())
        {
            registerForContextMenu(v);
            openContextMenu(v);
            //Intent i = new Intent(getBaseContext(), ECG_MAIN.class);
            //startActivity(i);

        }
        if(doctorradiobutton.isChecked()){
            Intent j = new Intent(getBaseContext(), Doctor.class);
            startActivity(j);



        }
        else
        {

            Toast.makeText(getBaseContext(),"Select an option",Toast.LENGTH_SHORT).show();
        }


    }


    public void dataacquisition(View v){




    }
    @Override
    public void onCreateContextMenu(ContextMenu menu,View v,ContextMenu.ContextMenuInfo menuInfo){

        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.patient_menu, menu);
        MenuItem acquiredataonly=menu.findItem(R.id.id_acquiredataonly);
        MenuItem acquireandsenddata=menu.findItem(R.id.id_acquireandsenddata);

        if(item_selection==1)
        {

            acquiredataonly.setChecked(true);
        }
        else if(item_selection==2)
        {

            acquireandsenddata.setChecked(true);
        }


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {



        switch(item.getItemId()){

            case R.id.id_acquiredataonly:
                Toast.makeText(getBaseContext(),"Acquire data only selected",Toast.LENGTH_SHORT).show();

                item.setChecked(true);
                item_selection=1;
                Intent i = new Intent(first_selection.this, ECG_MAIN.class);
                startActivity(i);
                return true;



            case R.id.id_acquireandsenddata:
                Toast.makeText(getBaseContext(),"Acquire data and send data is selected",Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                item_selection=2;
                Intent j = new Intent(first_selection.this, ECG_MAIN.class);
                startActivity(j);
                return true;

        }
        return super.onContextItemSelected(item);

    }
}