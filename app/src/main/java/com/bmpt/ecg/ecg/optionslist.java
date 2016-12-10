package com.bmpt.ecg.ecg;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
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


/**
 * This class handles the settings menu that the user can select to tune in certain parameters
 * We uses shared preferences so that the app remembers its last setting and resumes from there
 *
 *
 *
 *
 */
public class optionslist extends Activity {
ListView list_view;// declaring list view
   int d1,d2,d3;

    int item_selection;// setting up of integer
    int item_selection1;
    int item_selection2;


    int i;
    List<String>list=new ArrayList<String>();
    ArrayAdapter<String> adapter;// we are using array adapter
    protected void onCreate(final Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_menu);// we are using the settings_menu
        list_view=(ListView)findViewById(R.id.listview);
        list.add("Gain"); // adding gain title to the list menu
        list.add("Horizontal Scaling");// adding horizontal scaling to the settings menu
        list.add("50Hz Filter");// adding 50Hz filter to the menu
        list.add("Exit");// adding Exit to the menu
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,list);
        list_view.setAdapter(adapter);// setting the adapter
        registerForContextMenu(list_view);//rwe need to register the menu before we use it
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {// setting the functionality that should be carried out when each item of the list is clicked

                i = position;
                switch (i) {
                    case 0:
                        openContextMenu(view);// we are opening a popup menu that contains radio button for setting up the gain

                        break;
                    case 1:

                        openContextMenu(view);// opening up of the popup menu for setting up the horizontal scale

                        //code handling for of what to perform when horizontal scale is set
                        break;
                    case 2:
                        openContextMenu(view);//opening of context menu for switching on the 50Hz filter

                        //do the code handling for 50Hz filter

                        break;
                    case 3:

                        finish();// exit the settings


                }

            }

        });


    }

    /**
     *
     * Since the context menu has radio button, we use shared preference to remember the last checked
     * radio button
     */
    public void onCreateContextMenu(final ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        SharedPreferences perfs=getSharedPreferences("optionsvalue",MODE_PRIVATE);// stored in optionsvalue database

        item_selection=perfs.getInt("value",0);//stored in value field
        item_selection1=perfs.getInt("value1",0);//stored in value1 field
        item_selection2=perfs.getInt("value2",1);// stored in value2 field


        /**
         * here we configure the contextmenu that will contain the radio button of gain, horizonal scaling, 50hz filtering
         */
        switch (i) {



            case 0:
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.gainmarginmenu, menu);
                MenuItem fivemmgain=menu.findItem(R.id.id_5mmgain);
                MenuItem tenmmgain=menu.findItem(R.id.id_10mmgain);

                if(item_selection==1)
                {


                    fivemmgain.setChecked(true);// checking the 5mm gain radio_button
                    // insert the code that will execute when the radio button is checked

                }
                else if(item_selection==2)
                {

                    tenmmgain.setChecked(true);//checking the radio button for 10mm gain
                    // insert the code that will execute when the radio button is checked

                }
                registerForContextMenu(v);// registering the popup menu for gain
                break;
            case 1:

                MenuInflater inflater1 = getMenuInflater();
                inflater1.inflate(R.menu.horizontalscallingmenu, menu);// adding the horizontal scaling menu from R.menu
                MenuItem fifteenmmpers=menu.findItem(R.id.id_15mmsec);//adding 15mm/s horizontal scaling item
                MenuItem twentymmpers=menu.findItem(R.id.id_20mmsec);// adding 20mm/s horizontal scaling item
                MenuItem twentyfivemmpers=menu.findItem(R.id.id_25mmsec);//adding 25mm/s horizontal scaling item
                MenuItem fiftymmpers=menu.findItem(R.id.id_50mmsec);// addint 50mm/s horizontal scaling item

                if(item_selection1==1)
                {
                    fifteenmmpers.setChecked(true);// checking the radio button corresponding to 15mm/sec

                }
                else if(item_selection1==2)
                {

                    twentymmpers.setChecked(true);// checking the radio button corresponding to 20 mm/sec

                }

                else if(item_selection1==3)
                {

                    twentyfivemmpers.setChecked(true);      // checking the radio button corresponding to 25mm/sec


                }
                else if(item_selection1==4)
                {
                    fiftymmpers.setChecked(true);  // checking the radio button corresponding to 50mm/sec

                }

                registerForContextMenu(v);// registering for the context menu of horizontal scaling, context menu is registered each time when a new menu items set up
                break;
            case 2:

                MenuInflater inflater2 = getMenuInflater();
                inflater2.inflate(R.menu.filter50hzonoffmenu, menu);
                MenuItem fiftyhzon=menu.findItem(R.id.id_on);// setting up of radio on button
                MenuItem fiftyhzoff=menu.findItem(R.id.id_off);// setting up of radio off button
                if(item_selection2==1)
                {
                    fiftyhzon.setChecked(true);//checking the 50Hz On radio button

                }
                else if(item_selection2==2)
                {

                    fiftyhzoff.setChecked(true);// checking the 50Hz filter off button

                }


                registerForContextMenu(v);// registering of the menu for 50hz filter
                break;
        }








        }


    /**
     * we use the value stored in the shared preference to keep the radio button of the options menu checked
     * to the last configuration when we exit the app
     * we create 3 field in options value database they are value, value1,value2
     * the popup menu corresponding to 3 settings is stored in the 3 fields value,value1,value2
     */

    @Override
    public boolean onContextItemSelected(MenuItem item) {



        switch(item.getItemId()){

            case R.id.id_5mmgain:
                Toast.makeText(getBaseContext(), "Gain Set to 5mm/mV", Toast.LENGTH_SHORT).show();

                item.setChecked(true);
                item_selection=1;
                SharedPreferences prefs=getSharedPreferences("optionsvalue",MODE_PRIVATE);// stored in options value database in value field.
                SharedPreferences.Editor editor=prefs.edit();//configuring shared preference to store the value
                editor.putInt("value",item_selection);//store in value field
                editor.commit();

                //insert the code here when this button is clicked





                return true;



            case R.id.id_10mmgain:
                Toast.makeText(getBaseContext(),"Gain set to 10mm/mV ",Toast.LENGTH_SHORT).show();// displaying the selected button message
                item.setChecked(true);// checking the 10mm gain radio button
                item_selection=2;
                d1=2;
                SharedPreferences prefs1=getSharedPreferences("optionsvalue",MODE_PRIVATE);// storing in the options value database
                SharedPreferences.Editor editor1=prefs1.edit();//configuring the shared preference
                editor1.putInt("value",item_selection);// storin in the value field
                editor1.commit();
                //insert the code here when this button is clicked




                return true;
            case R.id.id_15mmsec:
                Toast.makeText(getBaseContext(),"Horizontal Scaling set to 15mm/sec ",Toast.LENGTH_SHORT).show();// displaying the selected button message
                item.setChecked(true);// checking the 10mm gain radio button
                item_selection1=1;//setting the value of item_selection to 1 to be stored in the shared preference
                d2=1;
                SharedPreferences prefs2=getSharedPreferences("optionsvalue",MODE_PRIVATE);// storing in the optionsvalue database
                SharedPreferences.Editor editor2=prefs2.edit();// configuring
                editor2.putInt("value1",item_selection1);// storing in value1 field of the database
                editor2.commit();
                //insert the code here when this button is clicked
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
                editor5.putInt("value1",item_selection1);//putting item_selection1 value in value1 field
                editor5.commit();

                //do the code handling when this button is clicked


                return true;
            case R.id.id_on:
                Toast.makeText(getBaseContext(),"50Hz filter on ",Toast.LENGTH_SHORT).show();//displaying the selected button message
                item.setChecked(true);//checking the button
                item_selection2=1;//putting the value
                //Handeling code here
                SharedPreferences prefs6=getSharedPreferences("optionsvalue",MODE_PRIVATE);
                SharedPreferences.Editor editor6=prefs6.edit();
                editor6.putInt("value2",item_selection2);
                editor6.commit();
                return true;
            case R.id.id_off:
                Toast.makeText(getBaseContext(),"50Hz filter on ",Toast.LENGTH_SHORT).show();// displaying the message
                item.setChecked(true);//checked
                item_selection2=2;// storing the value 2

                SharedPreferences prefs7=getSharedPreferences("optionsvalue",MODE_PRIVATE);//saving in optionsvalue database
                SharedPreferences.Editor editor7=prefs7.edit();// configuring shared preference
                editor7.putInt("value2",item_selection2);// puting the value of item_selection2 in value2 field
                editor7.commit();
                return true;



        }
        return super.onContextItemSelected(item);

    }





}
