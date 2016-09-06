package com.bmpt.ecg.ecg;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;

/**
 * Created by Raihan on 13-Aug-16.
 */
public class reportgeneration extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportgen);
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
            dataint[i]=Integer.parseInt(data);



        }


    }



}
