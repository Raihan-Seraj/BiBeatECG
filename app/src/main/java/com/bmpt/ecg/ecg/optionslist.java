package com.bmpt.ecg.ecg;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raihan on 03-Apr-16.
 */
public class optionslist extends Activity {
ListView list_view;
   int d1,d2,d3;

    int item_selection;
    int item_selection1;
    int item_selection2;

            //int item_selection3=0;
    int i;
    List<String>list=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    protected void onCreate(final Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_menu);
        list_view=(ListView)findViewById(R.id.listview);
        list.add("Gain");
        list.add("Horizontal Scaling");
        list.add("50Hz Filter");
        list.add("Exit");
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,list);
        list_view.setAdapter(adapter);
        registerForContextMenu(list_view);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
             //   Log.w("value of x is:"+x, "Value of y is:"+y);
                i = position;
                switch (i) {
                    case 0:
                        openContextMenu(view);

                        break;
                    case 1:
                        //registerForContextMenu(view);
                        openContextMenu(view);
                        break;
                    case 2:
                        openContextMenu(view);
                        //registerForContextMenu(view);
                        break;
                    case 3:

                        finish();

                       // Log.w("value of x is:"+x, "Value of y is:"+y);
                }

            }

        });


    }


    public void onCreateContextMenu(final ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        SharedPreferences perfs=getSharedPreferences("optionsvalue",MODE_PRIVATE);

        item_selection=perfs.getInt("value",0);
        item_selection1=perfs.getInt("value1",0);
        item_selection2=perfs.getInt("value2",1);

        switch (i) {



            case 0:
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.gainmarginmenu, menu);
                MenuItem fivemmgain=menu.findItem(R.id.id_5mmgain);
                MenuItem tenmmgain=menu.findItem(R.id.id_10mmgain);

                if(item_selection==1)
                {

                   // Log.d(" item selection is:,"+item_selection,"creakyaaaa");
                    fivemmgain.setChecked(true);

                }
                else if(item_selection==2)
                {

                    tenmmgain.setChecked(true);
                }
                registerForContextMenu(v);

                break;
            case 1:

                MenuInflater inflater1 = getMenuInflater();
                inflater1.inflate(R.menu.horizontalscallingmenu, menu);
                MenuItem fifteenmmpers=menu.findItem(R.id.id_15mmsec);
                MenuItem twentymmpers=menu.findItem(R.id.id_20mmsec);
                MenuItem twentyfivemmpers=menu.findItem(R.id.id_25mmsec);
                MenuItem fiftymmpers=menu.findItem(R.id.id_50mmsec);
                if(item_selection1==1)
                {
                    fifteenmmpers.setChecked(true);
                }
                else if(item_selection1==2)
                {

                    twentymmpers.setChecked(true);
                }

                else if(item_selection1==3)
                {

                    twentyfivemmpers.setChecked(true);
                }
                else if(item_selection1==4)
                {
                    fiftymmpers.setChecked(true);
                }

                registerForContextMenu(v);
                break;
            case 2:

                MenuInflater inflater2 = getMenuInflater();
                inflater2.inflate(R.menu.filter50hzonoffmenu, menu);
                MenuItem fiftyhzon=menu.findItem(R.id.id_on);
                MenuItem fiftyhzoff=menu.findItem(R.id.id_off);
                if(item_selection2==1)
                {
                    fiftyhzon.setChecked(true);
                }
                else if(item_selection2==2)
                {

                    fiftyhzoff.setChecked(true);
                }


                registerForContextMenu(v);
                break;
        }








        }



//Log.d("ADebugTag", "Value: " + Float.toString(i));

    @Override
    public boolean onContextItemSelected(MenuItem item) {



        switch(item.getItemId()){

            case R.id.id_5mmgain:
                Toast.makeText(getBaseContext(), "Gain Set to 5mm/mV", Toast.LENGTH_SHORT).show();

                item.setChecked(true);
                item_selection=1;
                SharedPreferences prefs=getSharedPreferences("optionsvalue",MODE_PRIVATE);
                SharedPreferences.Editor editor=prefs.edit();
                editor.putInt("value",item_selection);
                editor.commit();
               //handling code here
                return true;



            case R.id.id_10mmgain:
                Toast.makeText(getBaseContext(),"Gain set to 10mm/mV ",Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                item_selection=2;
                d1=2;
                SharedPreferences prefs1=getSharedPreferences("optionsvalue",MODE_PRIVATE);
                SharedPreferences.Editor editor1=prefs1.edit();
                editor1.putInt("value",item_selection);
                editor1.commit();
                return true;
            case R.id.id_15mmsec:
                Toast.makeText(getBaseContext(),"Horizontal Scaling set to 15mm/sec ",Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                item_selection1=1;
                d2=1;
                SharedPreferences prefs2=getSharedPreferences("optionsvalue",MODE_PRIVATE);
                SharedPreferences.Editor editor2=prefs2.edit();
                editor2.putInt("value1",item_selection1);
                editor2.commit();
                return true;
            case R.id.id_20mmsec:
                Toast.makeText(getBaseContext(),"Horizontal Scaling set to 20mm/sec ",Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                item_selection1=2;
                d2=2;
                SharedPreferences prefs3=getSharedPreferences("optionsvalue",MODE_PRIVATE);
                SharedPreferences.Editor editor3=prefs3.edit();
                editor3.putInt("value1",item_selection1);
                editor3.commit();
                //Handeling code here
                return true;
            case R.id.id_25mmsec:
                Toast.makeText(getBaseContext(),"Horizontal Scaling set to 25mm/sec ",Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                item_selection1=3;
                d2=3;
                SharedPreferences prefs4=getSharedPreferences("optionsvalue",MODE_PRIVATE);
                SharedPreferences.Editor editor4=prefs4.edit();
                editor4.putInt("value1",item_selection1);
                editor4.commit();
                return true;
            case R.id.id_50mmsec:
                Toast.makeText(getBaseContext(),"Horizontal Scaling set to 50mm/sec ",Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                item_selection1=4;
                d2=4;
               SharedPreferences prefs5=getSharedPreferences("optionsvalue",MODE_PRIVATE);
                SharedPreferences.Editor editor5=prefs5.edit();
                editor5.putInt("value1",item_selection1);
                editor5.commit();
                return true;
            case R.id.id_on:
                Toast.makeText(getBaseContext(),"50Hz filter on ",Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                item_selection2=1;
                //Handeling code here
                SharedPreferences prefs6=getSharedPreferences("optionsvalue",MODE_PRIVATE);
                SharedPreferences.Editor editor6=prefs6.edit();
                editor6.putInt("value2",item_selection2);
                editor6.commit();
                return true;
            case R.id.id_off:
                Toast.makeText(getBaseContext(),"50Hz filter on ",Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                item_selection2=2;

                SharedPreferences prefs7=getSharedPreferences("optionsvalue",MODE_PRIVATE);
                SharedPreferences.Editor editor7=prefs7.edit();
                editor7.putInt("value2",item_selection2);
                editor7.commit();
                return true;



        }
        return super.onContextItemSelected(item);

    }





}
