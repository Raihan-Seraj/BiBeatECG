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
 *
 */


/**
 * this class is used to display the date dialogue that is being called in patientform class
 * the user selects the date from the calender that pops up when the date field is touched
 */
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    EditText Date;
    public DateDialog(View view){

        Date=(EditText)view;//
    }
    public Dialog onCreateDialog(Bundle savedInstanceState ){

        final Calendar c=Calendar.getInstance();
        int year=c.get(Calendar.YEAR);//year is displayed
        int month =c.get(Calendar.DAY_OF_MONTH);//month is displayed
        int day= c.get(Calendar.DAY_OF_MONTH);//day is displayed
        return new DatePickerDialog(getActivity(),this,year,month,day);



    }
    // This determines the format of how the date will be displayed
    //I chose to display the date in the format of yy/mm/dd
    public void onDateSet(DatePicker view,int day,int month,int year)
    {


        String date=year+"-"+(month+1)+"-"+day;// setting the format in which the date will be displayed
        Date.setText(date);// setting the text of the selected date.
    }
}
