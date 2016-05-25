package com.bmpt.ecg.ecg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.HashMap;

/**
 * Created by Raihan on 24-May-16.
 */
public class DrawGraph extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder sh;
    private final Paint paint = new Paint();
    private final Paint paintGrid = new Paint();
    public byte[] controlOutData = new byte[]{1, 0, 0, 0, 0, 0, 0, 0};


    Handler handler;
    private float offset = 128;
    private final Paint paintLine = new Paint();
    Context ctx;
    ECGThread thread;

    UsbDevice Mydevice;
    boolean IntClamed = false;
    UsbInterface intf;
    UsbEndpoint endpoint;
    UsbDeviceConnection connection;
    private static final byte[] receivedDataInt = new byte[8];
    private static final byte[] receivedData = new byte[800];

    private static int[] receivedDataInte = new int[800];
    boolean DeviceAttached = false;
    int topMargin = 0;
    int leftMargin = 0;
    int bottomMargin = 170;

    private int canvasWidth = 960;
    private int canvasHeight = 800;

    private int graphMid = 350;

    int flagControlTrans = -1;
    int flagInterruptTrans = -1;
    private BufferedReader in = null;
    private BufferedWriter out = null;
    int parsedData[][];
    String info[][];
    int dataCount;
    int countFlag = 0;
    private boolean dataReceived = false;

    public DrawGraph(Context context) {
        super(context);

        handler = new Handler();

        ctx = context;
        setFocusable(true);
        sh = getHolder();
        sh.addCallback(this);

        // Signal Line Color and type
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1.5f);

        // Graph
        paintGrid.setColor(Color.GRAY);
        paintGrid.setStyle(Paint.Style.STROKE);
        paintLine.setColor(Color.MAGENTA);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setAntiAlias(true);
        paintLine.setStrokeWidth(2.0f);

        //USB enumeration and interface claiming
        UsbManager manager = (UsbManager) context
                .getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();

        for (UsbDevice device : deviceList.values()) {
            if (device.getVendorId() == 5824 && device.getProductId() == 1503) {

                Mydevice = device;
                DeviceAttached = true;

                intf = Mydevice.getInterface(0);
                endpoint = intf.getEndpoint(0);

                connection = manager.openDevice(Mydevice);
                IntClamed = connection.claimInterface(intf, true);
                break;
            }
        }
    }


    public void controlTransfer(byte[] data) {
        if (DeviceAttached) {
            controlOutData = data;
            connection.controlTransfer(0x21, 0x09, 0x00, 0x00, controlOutData, 0x08, 10000);


        }

    }

    public ECGThread getThread() {
        return thread;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = new Canvas();
        canvas = sh.lockCanvas();
        for (int i = leftMargin; i <= canvasWidth - leftMargin; i += 20) {
            canvas.drawLine(i, topMargin, i, canvasHeight - bottomMargin,
                    paintGrid);
        }
        for (int i = topMargin; i <= canvasHeight - bottomMargin; i += 20) {
            canvas.drawLine(leftMargin, i, canvasWidth - leftMargin, i,
                    paintGrid);
        }

        sh.unlockCanvasAndPost(canvas);

        thread = new ECGThread(sh, ctx, new Handler());
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        thread.setSurfaceSize(width, height);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }

    }


    public void setFlag(boolean fl) {

        Canvas c = null;
        c = sh.lockCanvas();
        thread.doDraw(c);
        if (c != null) {
            sh.unlockCanvasAndPost(c);
        }

    }


    class ECGThread extends Thread {
        private boolean run = false;

        WindowManager wm = (WindowManager) ctx
                .getSystemService(Context.WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();

        public ECGThread(SurfaceHolder surfaceHolder, Context context,
                         Handler handler) {
            sh = surfaceHolder;
            handler = handler;
            ctx = context;
        }

        public void doDrawP(Canvas c, float x, float y) {
            // TODO Auto-generated method stub
            c.drawLine(x, 0, x, canvasHeight, paint);
        }


        public void doStart() {
            synchronized (sh) {

            }
        }

        public void run() {
            while (run) {
                if (DeviceAttached) {


                    if (!connection.claimInterface(intf, true)) {
                        System.exit(0);


                    }


                    flagInterruptTrans = connection.bulkTransfer(endpoint,
                            receivedDataInt, receivedDataInt.length, 500);
                    // If Successfully Receive Byte via Interrupt Transfer then
                    // Perform a Control Transfer
                    if (flagInterruptTrans >= 0 && receivedDataInt[0] == 71) {
                        flagControlTrans = connection.controlTransfer(0xA1, 0x01, 0x00,
                                0x00, receivedData, 800, 10000);
                        flagInterruptTrans = -1;

                        // lineFlag = 0;


                        for (int i = 0; i < 800; i++) {
                            receivedDataInte[i] = receivedData[i] & 0xff;
                        }


                        if (flagControlTrans >= 0) {
                            flagControlTrans = -1;


                            Canvas c = null;
                            dataReceived = true;
                            try {
                                c = sh.lockCanvas();
                                synchronized (sh) {
                                    doDraw(c);
                                }
                            } finally {
                                if (c != null) {
                                    sh.unlockCanvasAndPost(c);
                                }
                            }

                        }


                    }


                }
            }
        }

        public void setRunning(boolean b) {
            run = b;
        }

        public void setSurfaceSize(int width, int height) {
            synchronized (sh) {
                // canvasWidth = width;
                // canvasHeight = height;
                doStart();
            }
        }

        private void drawBackground(Canvas canvas) {


            canvas.drawColor(Color.WHITE);
            for (int i = leftMargin; i <= canvasWidth - leftMargin; i += 20) {
                canvas.drawLine(i, topMargin, i, canvasHeight - bottomMargin,
                        paintGrid);
            }
            for (int i = topMargin; i <= canvasHeight - bottomMargin; i += 20) {
                canvas.drawLine(leftMargin, i, canvasWidth - leftMargin, i,
                        paintGrid);
            }


        }


        private void doDraw(Canvas canvas) {

            // canvas.drawBitmap(mBmBackground, leftMargin, topMargin, paint);

            drawBackground(canvas);

            // 80 ms
            if (dataReceived) {
                // Draw Signal
                for (int i = 0; i < receivedDataInte.length - 1; i++) {

                    canvas.drawLine(
                            (i + leftMargin),
                            (float) ((offset - (receivedDataInte[i])) * 2 + (graphMid)),
                            (i + leftMargin + 1),
                            (float) ((offset - (receivedDataInte[i + 1])) * 2 + (graphMid)),
                            paint);

                }
            }


        }


    }
}



