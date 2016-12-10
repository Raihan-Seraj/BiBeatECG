package com.bmpt.ecg.ecg;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Raihan on 11/20/2016.
 */


/**
 * this class is used to valiadat the user in order to use the app,
 * we perfrom an http request in the server with the login credential
 * given by the user and check with the data that is already
 * being stored in the database.
 */

public class login extends Activity {
    public static final int CONNECTION_TIMEOUT=10000;//declaring the time in milliseconds that we should keep on trying to establish the connection
    public static final int READ_TIMEOUT=15000;
    private EditText etEmail;
    private EditText etPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);// setting the login layout
        //Getting the reference variable
        etEmail=(EditText)findViewById(R.id.usernamelogin);
        etPassword=(EditText)findViewById(R.id.passwordlogin);

//LoginButton();


    }



    public void onLogin(View view){

        String username= etEmail.getText().toString(); //saving the user input email to username variable
        String password=etPassword.getText().toString();// saving the user input password to password variable
        String type= "login";
        // we start a background validation class, that carries out the http request and communicates with the php files stored in the server
        backgroundvalidation bckvd=new backgroundvalidation(this);
        bckvd.execute(type,username,password);






    }








    }















