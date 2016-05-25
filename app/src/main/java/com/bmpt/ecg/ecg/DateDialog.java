package com.bmpt.ecg.ecg;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by Raihan on 03-Apr-16.
 package com.bmpt.ecg.ecg;

 import android.app.DatePickerDialog;
 import android.app.Dialog;
 import android.app.DialogFragment;
 import android.os.Bundle;
 import android.view.View;
 import android.widget.DatePicker;
 import android.widget.EditText;

 import java.util.Calendar;

 /**
 * Created by Raihan on 03-Apr-16.
 */
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    EditText Date;
    public DateDialog(View view){

        Date=(EditText)view;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState ){

        final Calendar c=Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month =c.get(Calendar.DAY_OF_MONTH);
        int day= c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),this,year,month,day);



    }
    public void onDateSet(DatePicker view,int day,int month,int year)
    {


        String date=year+"-"+(month+1)+"-"+day;
        Date.setText(date);
    }
}
