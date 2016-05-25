package com.bmpt.ecg.ecg;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
/*
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;*/

/**
 * Created by Raihan on 13-Mar-16.
 */
public class patientform extends Activity {



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patientform);

        RadioButton maleradiobutton=(RadioButton)findViewById(R.id.maleradiobutton);
        RadioButton femaleradiobutton=(RadioButton)findViewById(R.id.femaleradiobutton);
        Button savebutton=(Button)findViewById(R.id.savebutton);
         savebutton.setVisibility(View.INVISIBLE);

        if(maleradiobutton.isChecked()==false&&femaleradiobutton.isChecked()==false)
        {savebutton.setVisibility(View.INVISIBLE);

        }
        else
        {
        savebutton.setVisibility(View.VISIBLE);
        }

    }


    public void nextclicked(View v) {


        Intent i = new Intent(getBaseContext(),first_selection.class);
        startActivity(i);


    }

    public void radiobuttonclicked(View v)
    {

        Button savebutton=(Button)findViewById(R.id.savebutton);
        savebutton.setVisibility(View.VISIBLE);



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




    }







}







