package com.bmpt.ecg.ecg;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;


/**
 * Created by Raihan on 13-Mar-16.
 */
public class patientform extends Activity {

 int flag1=0;
    int flag2=0;
    int flag3=1;
    int flag4=1;
    ImageView nextButton;
    String id,name,date,age;
    RadioButton maleradiobutton;
    RadioButton femaleradiobutton;
    EditText patient_id_edit_text;
    EditText patient_name_edit_text;
    EditText patient_age_edit_text;
    EditText patient_date_edit_text;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patientform);

        maleradiobutton=(RadioButton)findViewById(R.id.maleradiobutton);
      femaleradiobutton=(RadioButton)findViewById(R.id.femaleradiobutton);
        nextButton=(ImageView) findViewById(R.id.nextbutton);

        patient_id_edit_text = (EditText) findViewById(R.id.patientid);
        id = patient_id_edit_text.getText().toString();
        patient_id_edit_text.addTextChangedListener(new TextWatcher() {
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



    }


    public void nextclicked(View v) {

        SharedPreferences preferences=getSharedPreferences("PatientInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("id",patient_id_edit_text.getText().toString());
        editor.putString("name",patient_name_edit_text.getText().toString());
        editor.putString("date",patient_date_edit_text.getText().toString());
        editor.putString("age",patient_age_edit_text.getText().toString());
        editor.commit();
        Toast.makeText(getBaseContext(), "Patient Information Saved Successfully", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getBaseContext(),first_selection.class);
        startActivity(i);


    }


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
            nextButton.setVisibility(View.INVISIBLE);


        }
        else if (name.matches("")){

            nextButton.setVisibility(View.INVISIBLE);
  //          Toast.makeText(getBaseContext(), "Name check", Toast.LENGTH_SHORT).show();

        }
        else if (date.matches("")){

            nextButton.setVisibility(View.INVISIBLE);
//            Toast.makeText(getBaseContext(), "Date check", Toast.LENGTH_SHORT).show();

        }

        else if (age.matches("")){

            nextButton.setVisibility(View.INVISIBLE);
          //  Toast.makeText(getBaseContext(), "age check", Toast.LENGTH_SHORT).show();

        }

        else if(maleradiobutton.isChecked()==false&&femaleradiobutton.isChecked()==false){

            nextButton.setVisibility(View.INVISIBLE);
        }
else {
            nextButton.setVisibility(View.VISIBLE);
         //   Toast.makeText(getBaseContext(), "Should work", Toast.LENGTH_SHORT).show();

        }



    }





}







