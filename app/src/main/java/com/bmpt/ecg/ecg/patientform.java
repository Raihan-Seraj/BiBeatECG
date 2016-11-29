package com.bmpt.ecg.ecg;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Raihan on 13-Mar-16.
 */
public class patientform extends Activity {

 int flag1=0;
    int flag2=0;
    int flag3=1;
    int flag4=1;
    int item_selection=0;
    ImageView nextButton;
    String id,name,date,age;
    RadioButton maleradiobutton;
    RadioButton femaleradiobutton;
    EditText patient_id_edit_text;
    EditText patient_name_edit_text;
    EditText patient_age_edit_text;
    EditText patient_date_edit_text;
    String sex;
    String create_patient_URL="http://192.168.154.12/android_connect/create_patient.php";
    RequestQueue requestQueue;
    ImageView nextClicked;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patientform);
        nextClicked=(ImageView)findViewById(R.id.nextbutton);
        requestQueue= Volley.newRequestQueue(getApplicationContext());

        maleradiobutton=(RadioButton)findViewById(R.id.maleradiobutton);
      femaleradiobutton=(RadioButton)findViewById(R.id.femaleradiobutton);
        nextButton=(ImageView) findViewById(R.id.nextbutton);

        patient_id_edit_text = (EditText) findViewById(R.id.patientid);
        id = patient_id_edit_text.getText().toString();
        patient_id_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
 //When the text changes each time, we are checking for the visibility of the next button
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
          check_for_next_btn_visibility();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

         patient_name_edit_text = (EditText) findViewById(R.id.patientname);
        patient_name_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                check_for_next_btn_visibility();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        name = patient_name_edit_text.getText().toString();

         patient_date_edit_text = (EditText) findViewById(R.id.Date);
        patient_date_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            check_for_next_btn_visibility();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        date = patient_date_edit_text.getText().toString();

         patient_age_edit_text = (EditText) findViewById(R.id.patientage);
        patient_age_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
             check_for_next_btn_visibility();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        age = patient_age_edit_text.getText().toString();

        nextClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //saving to shared preferences for displaying in the form later
                SharedPreferences preferences=getSharedPreferences("PatientInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("id",patient_id_edit_text.getText().toString());
                editor.putString("name",patient_name_edit_text.getText().toString());
                editor.putString("date",patient_date_edit_text.getText().toString());
                editor.putString("age",patient_age_edit_text.getText().toString());
                if(maleradiobutton.isChecked()==true)
                {
                    sex="M";
                }
                else if(femaleradiobutton.isChecked()==true)
                {
                    sex="F";
                }
                editor.putString("sex",sex);
                editor.commit();

                StringRequest request=new StringRequest(Request.Method.POST,create_patient_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_LONG).show();

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parameters=new HashMap<String, String>();
                        parameters.put("patient_id",patient_id_edit_text.getText().toString());
                        parameters.put("patient_name",patient_name_edit_text.getText().toString());
                        parameters.put("age",patient_age_edit_text.getText().toString());
                        parameters.put("sex",sex);
                        parameters.put("date",patient_date_edit_text.getText().toString());
                        return parameters;
                    }
                };
                requestQueue.add(request);


                Toast.makeText(getBaseContext(), "Patient Information Saved Successfully", Toast.LENGTH_SHORT).show();
                registerForContextMenu(v);
                openContextMenu(v);

            }
        });



    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){

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
                Intent i = new Intent(patientform.this, ECG_MAIN.class);
                startActivity(i);
                return true;



            case R.id.id_acquireandsenddata:
                Toast.makeText(getBaseContext(),"Acquire data and send data is selected",Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                item_selection=2;
                Intent j = new Intent(patientform.this, ECG_MAIN.class);
                startActivity(j);
                return true;

        }
        return super.onContextItemSelected(item);

    }


    /*public void nextclicked(View v) {
//saving to shared preferences for displaying in the form later
        SharedPreferences preferences=getSharedPreferences("PatientInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("id",patient_id_edit_text.getText().toString());
        editor.putString("name",patient_name_edit_text.getText().toString());
        editor.putString("date",patient_date_edit_text.getText().toString());
        editor.putString("age",patient_age_edit_text.getText().toString());
        if(maleradiobutton.isChecked()==true)
        {
            sex="M";
        }
        else if(femaleradiobutton.isChecked()==true)
        {
            sex="F";
        }
        editor.putString("sex",sex);
        editor.commit();

        StringRequest request=new StringRequest(Request.Method.POST,create_patient_URL,new Res)


        Toast.makeText(getBaseContext(), "Patient Information Saved Successfully", Toast.LENGTH_SHORT).show();
        registerForContextMenu(v);
        openContextMenu(v);

    }*/


    public void onStart()
    {

   super.onStart();

  EditText Date=(EditText)findViewById(R.id.Date);
        Date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {


                    DateDialog dialog=new DateDialog(v);
                    FragmentTransaction ft=getFragmentManager().beginTransaction();
                    dialog.show(ft,"DatePicker");

                }
            }

        });






check_for_next_btn_visibility();
    }

    public void radiobuttonclicked(View v){
        check_for_next_btn_visibility();





    }




    public void check_for_next_btn_visibility(){


        id = patient_id_edit_text.getText().toString();
        name = patient_name_edit_text.getText().toString();
        date = patient_date_edit_text.getText().toString();
        age = patient_age_edit_text.getText().toString();
        if (id.matches("")) {
    //        Toast.makeText(getBaseContext(), "ID check", Toast.LENGTH_SHORT).show();
            nextButton.setEnabled(false);


        }
        else if (name.matches("")){

            nextButton.setEnabled(false);
  //          Toast.makeText(getBaseContext(), "Name check", Toast.LENGTH_SHORT).show();

        }
        else if (date.matches("")){

            nextButton.setEnabled(false);
//            Toast.makeText(getBaseContext(), "Date check", Toast.LENGTH_SHORT).show();

        }

        else if (age.matches("")){

            nextButton.setEnabled(false);
          //  Toast.makeText(getBaseContext(), "age check", Toast.LENGTH_SHORT).show();

        }

        else if(maleradiobutton.isChecked()==false&&femaleradiobutton.isChecked()==false){

            nextButton.setEnabled(false);
        }
else {
            nextButton.setEnabled(true);
         //   Toast.makeText(getBaseContext(), "Should work", Toast.LENGTH_SHORT).show();

        }



    }





}







