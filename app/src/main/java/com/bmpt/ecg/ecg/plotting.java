package com.bmpt.ecg.ecg;

/**
 * Created by Raihan on 23-Apr-16.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package display;

/**
 *
 * @author Aumi
 */
//import LeadDisplay.*;
//import Timer.StopWatch;
//import ecgbmptversion15.*;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.androidplot.xy.SimpleXYSeries;

//import Timer.StopWatch;

/*
 * This PApplet (Processing applet) gets data from GetData.java and draws graph
 */


public class plotting extends Activity //implements Runnable{
{

    private static final int HISTORY_SIZE = 30;            // number of points to plot in history
    private SensorManager sensorMgr = null;
    private Sensor orSensor = null;


    private SimpleXYSeries ecgHistorySeries = null;


}
