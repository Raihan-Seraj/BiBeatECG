package com.bmpt.ecg.ecg;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Raihan on 11/20/2016.
 */

public class login extends Activity {
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private EditText etEmail;
    private EditText etPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //Getting the reference variable
        etEmail=(EditText)findViewById(R.id.usernamelogin);
        etPassword=(EditText)findViewById(R.id.passwordlogin);

//LoginButton();


    }



    public void onLogin(View view){

        String username= etEmail.getText().toString();
        String password=etPassword.getText().toString();
        String type= "login";
        backgroundvalidation bckvd=new backgroundvalidation(this);
        bckvd.execute(type,username,password);






    }








    }





   /* public void LoginButton() {
        final EditText username = (EditText) findViewById(R.id.usernamelogin);
        final EditText password = (EditText) findViewById(R.id.passwordlogin);

        Button login_button = (Button) findViewById(R.id.signin_button);


        login_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (username.getText().toString().equals("user")&&
                        password.getText().toString().equals("pass")){
                            Toast.makeText(login.this, "Username and password is correct",
                                    Toast.LENGTH_SHORT).show();
                            Intent j = new Intent(login.this, patientform.class);
                            startActivity(j);

                        }
                        else{
                            Toast.makeText(login.this, "Username and password is NOT correct",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        );

    }*/











