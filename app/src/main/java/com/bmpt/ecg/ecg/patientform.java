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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;




/**
 * Created by Raihan on 13-Mar-16.
 */

/**
 * In this activity I performed the basic handling of the patient form
 * this activity checks whether all the necessary fields are filled
 * and saves the patient information both in the application preference storage
 * and the external server.
 * I used Volley library which is an external library and followed the
 * tutorial of connecting to the server using this link:
 *
 * https://www.simplifiedcoding.net/android-volley-post-request-tutorial/
 *
 *
 *
 */
public class patientform extends Activity {


    int item_selection=0;// variable that determines whether the acuiredata or aquireonly data should be checked
    ImageView nextButton;//Declaring the image for the next button
    String id,name,date,age;// Initialising id, name, date and age string for holding the data
    RadioButton maleradiobutton;//  male radio button declared
    RadioButton femaleradiobutton;// female radio button declared
    EditText patient_id_edit_text;// textbox where the user will input the patient id
    EditText patient_name_edit_text;//textbox where the user will input patient name
    EditText patient_age_edit_text;//textbox where the user will input age
    EditText patient_date_edit_text;// textbox where the user will input date
    String sex;// string variable for holding sex
    String create_patient_URL="http://192.168.0.104/android_connect/patient.php";//local host URL, change to the server ip.
    RequestQueue requestQueue;// initialising
    ImageView nextClicked;// same button declared twice
    /*
     Here I am declaring the key that will connect to the server
     */
    public static final String KEY_PATIENTID="patient_id";
    public static final String KEY_PATIENTNAME="patient_name";
    public static final String KEY_SEX="sex";
    public static final String KEY_AGE="age";
    public static final String KEY_Date="date";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patientform);// Initialising the form
        nextClicked=(ImageView)findViewById(R.id.nextbutton);//initialising button
        requestQueue= Volley.newRequestQueue(getApplicationContext());// volley library is an external library

        maleradiobutton=(RadioButton)findViewById(R.id.maleradiobutton);//tagging the id of the male radio button
      femaleradiobutton=(RadioButton)findViewById(R.id.femaleradiobutton); //tagging the id of female radio button
        nextButton=(ImageView) findViewById(R.id.nextbutton); // identifying the next button

        patient_id_edit_text = (EditText) findViewById(R.id.patientid); //identifying patient id edittext
        id = patient_id_edit_text.getText().toString();// converting the user input text to string and storing in id variable
        //Textwatcher function executes each time the user provide the input
        patient_id_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
 //When the text changes each time in the edit text, we are checking for the visibility of the next button
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
          check_for_next_btn_visibility();// function for checking whether the next button should be enabled
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

         patient_name_edit_text = (EditText) findViewById(R.id.patientname);
        //adding textwatcher to the edit text that takes the user input for the name of the patient
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

        name = patient_name_edit_text.getText().toString();// taking the user input value and storing in the name variable

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
        date = patient_date_edit_text.getText().toString();//taking user input date and storing in the date variable

         patient_age_edit_text = (EditText) findViewById(R.id.patientage);
        patient_age_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            //we will check whether the next button should be enabled in onText changed option.
            public void onTextChanged(CharSequence s, int start, int before, int count) {
             check_for_next_btn_visibility();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        age = patient_age_edit_text.getText().toString();// storing the age inserted by the user in age variable


        /**
         * We define what method should be executed when the next button is clicked
         * we have stored the date ,patient name , age , sex and patient id variable in
         * the server as well as the app's temporary storage known as the shared preferencce.
         * Using shared preference allows us to use the stored value to be used in other activities
         * I am storing these values so that they can be fetched and displayed in the activity
         * where the combined report will be generated and that will include patient's name and
         * patient id
         */

        nextClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //saving to shared preferences for displaying in the form later
                SharedPreferences preferences=getSharedPreferences("PatientInfo", Context.MODE_PRIVATE);//Storing the values in Patientinfo database
                SharedPreferences.Editor editor=preferences.edit();// declaring shared preference editor to put the values
                editor.putString("id",patient_id_edit_text.getText().toString());// converting id to string and putting into the id field of Patientinfo database
                editor.putString("name",patient_name_edit_text.getText().toString());// converting name to string and putting into the name field of Patientinfo database
                editor.putString("date",patient_date_edit_text.getText().toString());// converting date to string and putting into the date field of Patientinfo database
                editor.putString("age",patient_age_edit_text.getText().toString());// converting age to string and putting into the age field of Patientinfo database


                if(maleradiobutton.isChecked()==true) //checking whether the radio button corresponding to male has been clicked
                {
                    sex="M";// if male radio button is checked we store M as the string in sex variable

                }
                else if(femaleradiobutton.isChecked()==true) //checking whether the radio button corresponding to female has been checked
                {
                    sex="F";  //if female radio button is clicked we store F string in the sex variable
                }
                editor.putString("sex",sex);//we are storing sex variable is the sharedpreference which is the apps temporary storage
                editor.commit();
                try {
                    registerpatient(); //we are going to the register patient_method to start the patient registration in the server.
                } catch (JSONException e) { //exception needs to be declared
                    e.printStackTrace();
                }


                Toast.makeText(getBaseContext(), "Patient Information Saved Successfully", Toast.LENGTH_SHORT).show(); //displaying that the information saved successfully
                registerForContextMenu(v);//  registering for the popup menu that appears when the next button is clicked that allows the user to select 2 options
                openContextMenu(v);// opening the menu

            }
        });



    }


    /**
     * In this method the code for storing the data in the server is written
     * we get the values from the edit texts and convert them to string
     * the data is then send via json format to the server.
     *
     *
     */


    private void registerpatient() throws JSONException {
 final String patientid=patient_id_edit_text.getText().toString().trim(); //getting the values
        final String patientname=patient_name_edit_text.getText().toString().trim();//getting the values

        final String age=patient_age_edit_text.getText().toString().trim();// storing the values to age
        final String date=patient_date_edit_text.getText().toString().trim();// storing the value to the date variable
        //string request is created where the server URL and the method of sending the data to the server is defined, we used POST method
        StringRequest stringRequest = new StringRequest(Request.Method.POST, create_patient_URL,

                /*setting the response that will be displayed when successfully connected to server
                or when there is an error
                */
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(patientform.this,response,Toast.LENGTH_LONG).show();//displaying success response
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(patientform.this,error.toString(),Toast.LENGTH_LONG).show();// error response
                    }
                }) {

             /*
             declaring hashmap (data structure) to store the parameters
             using the KEY of PatientID, Name, Sex, Age,Date
              */
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_PATIENTID, patientid);
                params.put(KEY_PATIENTNAME, patientname);
                params.put(KEY_SEX, sex);
                params.put(KEY_AGE, age);
                params.put(KEY_Date, date);
                return params;


            }
        };

        //using the volley library to initiate the request to the server using POST method
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }

    /**
     *
     * Setting up the context menu, that appeares when the next button is clicked
     *
     *
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){

        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.patient_menu, menu);//identifying and initiating the menu
        MenuItem acquiredataonly=menu.findItem(R.id.id_acquiredataonly);// finding the id of the menu item
        MenuItem acquireandsenddata=menu.findItem(R.id.id_acquireandsenddata);// finding the id of the menu item

        /**
         * here we are checking for the last checked value of the radio button and
         * keeping them checked based on the last checked value of the radio button/
         */
        if(item_selection==1)
        {

            acquiredataonly.setChecked(true);
        }
        else if(item_selection==2)
        {

            acquireandsenddata.setChecked(true);
        }


    }

    /**
     *
     * Here we are setting up the item in the context menu, and what actions should be performed
     * when the items acquire data or acquire and send data are selected
     *
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {



        switch(item.getItemId()){

            case R.id.id_acquiredataonly:
                Toast.makeText(getBaseContext(),"Acquire data only selected",Toast.LENGTH_SHORT).show();

                item.setChecked(true);
                item_selection=1;// setting the value of item_selection to 1 so that the radio button keeps as checked
                Intent i = new Intent(patientform.this, ECG_MAIN.class);//registering in order to start the activity of ECE main
                startActivity(i); //starting the activity of ECE_Main class when acquire data is selected
                return true;



            case R.id.id_acquireandsenddata:
                Toast.makeText(getBaseContext(),"Acquire data and send data is selected",Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                item_selection=2;// setting item_selection to 2 so that this value keep on being checked when acquireand send data is selected
                Intent j = new Intent(patientform.this, ECG_MAIN.class);// registering for the activity ECE main
                startActivity(j);// starting the activity ece_main, we can start another activity or start the Ecg main activity and look for the method that determines wheich one of the radio button is checked
                return true;

        }
        return super.onContextItemSelected(item);

    }


    /**
     * The following method actually displays the date pop up calender when the date is selected
     *when the user touches the date field a calender popup is made where the user can appropriately
     * select the date
     */

    public void onStart()
    {

   super.onStart();

  EditText Date=(EditText)findViewById(R.id.Date);
        Date.setOnFocusChangeListener(new View.OnFocusChangeListener() { ///method to execute when the user touches the date field
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {


                    DateDialog dialog=new DateDialog(v);// registering the datepopup in a fragment
                    FragmentTransaction ft=getFragmentManager().beginTransaction();
                    dialog.show(ft,"DatePicker");// displaying the date picker

                }
            }

        });






check_for_next_btn_visibility();// checking whether the next button should be enabled or not
    }

    public void radiobuttonclicked(View v){
        check_for_next_btn_visibility(); // when any one of the radio button is clicked, we check whether all criterion is being met before we activate the next button





    }


    /**
     * This is the function that checks whether the next button should be enabled or not
     * we enable the next button when the user has filled all the necessary information in the
     * patient form
     */
    public void check_for_next_btn_visibility(){


        id = patient_id_edit_text.getText().toString();// storing the patient_id from the user input to the id variable
        name = patient_name_edit_text.getText().toString();// storing the patient_name from the user input to the name variable
        date = patient_date_edit_text.getText().toString();// storing the date from the user input to the date variable
        age = patient_age_edit_text.getText().toString();// storing the age from the user input to the age variable
        if (id.matches("")) {// checking whether the id variable is empty

            nextButton.setBackgroundResource(R.drawable.arrowdisable);// setting the image of the button that should be displayed when the button is disabled
            nextButton.setEnabled(false);// disabling the next button


        }
        else if (name.matches("")){// checking whether name variable is empty
            nextButton.setBackgroundResource(R.drawable.arrowdisable);// image that should be displayed when the button is disabled

            nextButton.setEnabled(false);// diabling the button


        }
        else if (date.matches("")){//checking whether the date variable is empty
            nextButton.setBackgroundResource(R.drawable.arrowdisable);// setting the image to be displayed when the button is disabled

            nextButton.setEnabled(false);//disabling the next button


        }

        else if (age.matches("")){//checking whether the age variable is empty
            nextButton.setBackgroundResource(R.drawable.arrowdisable);//setting up the disable image

            nextButton.setEnabled(false);//disabling the button


        }

        else if(maleradiobutton.isChecked()==false&&femaleradiobutton.isChecked()==false){ //checking whether any of the radio button is checked

            nextButton.setEnabled(false);// if none of the radio button is checked, we disable the next button
        }
else {
            nextButton.setEnabled(true); //finally if all the above criterion is met we enable the next button


        }



    }





}







