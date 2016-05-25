package com.bmpt.ecg.ecg;

/**
 * Created by Raihan on 15-May-16.
 */
public class GetData extends Thread {

    public boolean killthread=false;
private byte [] data;
    public static boolean stop= false;

    int avgData = 0;
    double[] xv = new double[9];
    double[] yv = new double[9];
    double[] xV = new double[9];
    double[] yV = new double[9];

    int[] bufferNF = new int[8];
    int[] bufferF = new int[8];

    double[] Inv = new double[9];
    double[] Outv = new double[9];

    int[] ReportBufferNF = new int[8];
    int[] ReportBufferHP = new int[8];
    int[] ReportBufferF = new int[8];

    private int plotVal;
    //float gain = 0.7f;
    private int plotVal2;

    private static int position = 0;
    private int[] storedDataFor15 = new int[13];
    private int[] storedDataFor20 = new int[10];

}
