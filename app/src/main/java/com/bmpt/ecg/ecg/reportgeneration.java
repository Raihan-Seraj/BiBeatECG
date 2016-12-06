package com.bmpt.ecg.ecg;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by Raihan on 13-Aug-16.
 */
public class reportgeneration extends AppCompatActivity {
Toolbar toolbar;
    ViewPager pager;
    ViewPageAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Lead 1-3", "Lead aVR,aVF,aVL","Lead V1-V3","Lead V4-V6","Extended Lead II"};
    int Numboftabs=5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportgen);
        // Creating The Toolbar and setting it as the Toolbar for the activity

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
// Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPageAdapter(getSupportFragmentManager(),Titles,Numboftabs);
        // Assigning ViewPager View and setting the adapter
                pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

         /*TextView patientid=(TextView)findViewById(R.id.patientIDreport);
         TextView patientname=(TextView)findViewById(R.id.reportpatientname);
        SharedPreferences pinfo=getSharedPreferences("PatientInfo",MODE_PRIVATE);
        String Pname=(pinfo.getString("name",""));
        String pid=pinfo.getString("id","");
        patientid.setText(pid);

        final GraphView reportlead1=(GraphView)findViewById(R.id.reportlead1);
        final GraphView reportlead2=(GraphView)findViewById(R.id.reportlead2);
        final GraphView reportlead3=(GraphView)findViewById(R.id.reportlead3);
        reportlead1.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        reportlead1.getGridLabelRenderer().setVerticalLabelsVisible(false);
        reportlead1.getGridLabelRenderer().setHighlightZeroLines(false);
        reportlead1.getGridLabelRenderer().setNumVerticalLabels(15);
        reportlead1.getGridLabelRenderer().setNumHorizontalLabels(40);

        Viewport reportlead1viewport=reportlead1.getViewport();
        reportlead1viewport.setScalable(true);
        reportlead1viewport.setScrollable(true);
        reportlead1.setTitle("Lead 1");
        reportlead1viewport.setYAxisBoundsManual(true);
        reportlead1viewport.setMinY(-150);
        reportlead1viewport.setMaxY(150);

        reportlead2.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        reportlead2.getGridLabelRenderer().setVerticalLabelsVisible(false);
        reportlead2.getGridLabelRenderer().setHighlightZeroLines(false);
        reportlead2.setTitle("Lead 2");
        reportlead2.getGridLabelRenderer().setNumVerticalLabels(15);
        reportlead2.getGridLabelRenderer().setNumHorizontalLabels(40);

        Viewport reportlead2viewport=reportlead1.getViewport();
        reportlead2viewport.setScalable(true);
        reportlead2viewport.setScrollable(true);
        reportlead2viewport.setYAxisBoundsManual(true);
        reportlead2viewport.setMinY(-150);
        reportlead2viewport.setMaxY(150);

        reportlead3.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        reportlead3.getGridLabelRenderer().setVerticalLabelsVisible(false);
        reportlead3.getGridLabelRenderer().setHighlightZeroLines(false);
        reportlead3.getGridLabelRenderer().setNumVerticalLabels(15);
        reportlead3.getGridLabelRenderer().setNumHorizontalLabels(40);

        reportlead3.setTitle("Lead 3");
        Viewport reportlead3viewport=reportlead1.getViewport();
        reportlead3viewport.setScalable(true);
        reportlead3viewport.setScrollable(true);
        reportlead3viewport.setYAxisBoundsManual(true);
        reportlead3viewport.setMinY(-150);
        reportlead3viewport.setMaxY(150);
        SharedPreferences preferences=getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String data=preferences.getString("datashared","");
        final int dataint[]=new int[data.length()];
        for(int i=0;i<data.length();i++)
        {
            dataint[i]=Integer.parseInt(data);*



        }   */


    }



}
