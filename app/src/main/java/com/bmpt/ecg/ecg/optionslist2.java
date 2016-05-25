package com.bmpt.ecg.ecg;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
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



import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raihan on 05-Apr-16.
 */


public class optionslist2 extends Activity {
    ListView list_view1;
    List<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private static final int REQUEST_CHOOSER = 1234;


    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.optionsmenu);
        list_view1 = (ListView) findViewById(R.id.listviewoptions);
        list.add("Generate combined ECG from existing xml file");

        list.add("Edit Organisation Name");
        list.add("Edit User Id and Password");
        list.add("Add/Delete user");
        list.add("Send xml by email");
        list.add("Exit");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list);
        list_view1.setAdapter(adapter);
        list_view1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int i = position;
                switch (i) {

                    case 0:


                        break;
                    case 1:
                        //handling
                        break;
                    case 2:
                        //handling
                        break;
                    case 3:
                        //handling
                        break;
                    case 4:
                        //handling
                        break;
                    case 5:
                        finish();
                        break;


                }
            }
        });


    }




}