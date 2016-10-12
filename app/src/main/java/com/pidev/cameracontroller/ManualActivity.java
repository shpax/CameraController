package com.pidev.cameracontroller;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import android.widget.VideoView;

public class ManualActivity extends Activity implements SensorEventListener{

    private static final float MAX_FRONT = 5f;
    private static final float MAX_TURN = 8f;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    float xAxis,yAxis;
    ToggleButton accel;
    boolean useAccel;
    float yMark, xMark;

    private static final String streamURL = "rtsp://192.168.1.254/sjcam.mov";

    VideoView vv;
    View driveLeft;
    View driveRight;
    Button shot;
    public static boolean track = false;
    public int drLeft = 0;
    public int drRight=0;
    public Handler h;
    Runnable r;
    boolean rt = false;
    boolean lt = false;
    boolean previousState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_manual_mode);
        if (MainActivity.connection ==null) {
            this.finish();
        }
        shot = (Button) findViewById(R.id.makeShot);
//        vv = (VideoView) findViewById(R.id.videoStream);
//        MediaController mediaController = new MediaController(this);
//        mediaController.setAnchorView(vv);
//        vv.setMediaController(mediaController);
//        vv.setVideoURI(Uri.parse(streamURL));
//        vv.start();
        shot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte order[] = {1,0,0};
                MainActivity.connection.sendData(order);
            }
        });
        driveLeft = findViewById(R.id.driveLeft);
        driveRight = findViewById(R.id.driveRight);
        h = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                if (rt || lt ) {
                    byte ldrive = (byte) (Math.abs(drLeft)) ;
                    if (drLeft<0) ldrive|= (1<<7);
                    byte rdrive = (byte) (Math.abs(drRight));
                    if (drRight<0) rdrive|= (1<<7);

                    byte order[] = {4,rdrive,ldrive};
                    MainActivity.connection.sendData(order);
                    previousState = true;
                } else if (useAccel) {
                    float yFix = yAxis - yMark;
                    float xFix = xAxis - xMark;
                    float tempLeft = (Math.abs(yFix)>MAX_FRONT ? yFix/yFix*100 : yFix*100/MAX_FRONT)
                            + (Math.abs(xFix)>MAX_TURN ? xFix/xFix*100 : xFix*100/MAX_TURN);
                    float tempRight = (Math.abs(yFix)>MAX_FRONT ? yFix/yFix*100 : yFix*100/MAX_FRONT)
                            - (Math.abs(xFix)>MAX_TURN ? xFix/xFix*100 : xFix*100/MAX_TURN);
                    byte ldrive = (byte) (Math.abs(tempLeft)) ;
                    if (tempLeft<0) ldrive|= (1<<7);
                    byte rdrive = (byte) (Math.abs(tempRight));
                    if (tempRight<0) rdrive|= (1<<7);

                    byte order[] = {4,rdrive,ldrive};
                    MainActivity.connection.sendData(order);
                } else {
                    if (previousState) {
                        byte order[] = {5,0,0};
                        MainActivity.connection.sendData(order);
                        previousState = false;
                    }
                }
                h.postDelayed(this, 10);
            }
        };
        h.postDelayed(r, 10);
        driveLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                drLeft = Math.round(event.getY() / v.getHeight() * 200 - 100);
                if (event.getAction() == event.ACTION_UP) {
                    drLeft = 0;
                    lt = false;
                } else {
                    lt = true;
                }
                return true;
            }
        });
        driveRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                drRight = Math.round(event.getY() / v.getHeight() * 200 - 100);
                if (event.getAction() == event.ACTION_UP) {
                    drRight = 0;
                    rt = false;
                } else {
                    rt = true;
                }
                return true;
            }
        });
        senSensorManager = (SensorManager) getSystemService(getApplicationContext().SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        accel = (ToggleButton) findViewById(R.id.toggle_accel);
        accel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                yMark = yAxis ;
                xMark = xAxis;
                useAccel = isChecked;
                if (!isChecked) previousState = true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xAxis = sensorEvent.values[0];
            yAxis = sensorEvent.values[1];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
