package com.authorwjf;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Main activity.
 * @author Pranav
 */
public class Main extends Activity implements SensorEventListener {

    //========================================================================//
    //Flags, Fields, and Values===============================================//
    
    /**
     * Position and orientation of IMU device.
     */
    private Position p;
    
    /**
     * Previous values for accelerometer.
     */
    private float aLastX, aLastY, aLastZ;    
    
    /**
     * Sensor manager for accelerometer.
     */
    private SensorManager mAccelManager;

    /**
     * Sensor manager for gyroscope.
     */
    private SensorManager mGyroManager;

    /**
     * Accelerometer.
     */
    private Sensor accel;
    
    /**
     * Gyroscope.
     */
    private Sensor gyro;
    
    
    //========================================================================//
    //Setup Handlers==========================================================//
    
    /**
     * Called when created.
     * @param savedInstanceState 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Initialize activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //Set up position variable
        this.p = new Position();
        
        //Set up accelerometer sensor and listener
        this.mAccelManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);
        this.accel = this.mAccelManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
        this.mAccelManager.registerListener(this, this.accel, 
                SensorManager.SENSOR_DELAY_NORMAL);
        
        //Set up gyroscope sensor and listener
        this.mGyroManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);
        this.gyro = this.mGyroManager.getDefaultSensor(
                Sensor.TYPE_GYROSCOPE);
        this.mGyroManager.registerListener(this, this.gyro, 
                SensorManager.SENSOR_DELAY_NORMAL);    

    }

    /**
     * When app is paused, stop listening.
     */
    protected void onPause() {
        super.onPause();
        this.mAccelManager.unregisterListener(this);
        this.mGyroManager.unregisterListener(this);
    }

    /**
     * When app resumes, start listening again.
     */
    protected void onResume() {
        super.onResume();
        this.mAccelManager.registerListener(this, this.accel, 
                SensorManager.SENSOR_DELAY_NORMAL);
        this.mGyroManager.registerListener(this, this.gyro, 
                SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * What to do when a sensor value changes?
     * @param event event reported by sensor
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        int eventType = event.sensor.getType();
        if (eventType == this.accel.getType()) {
            this.accelEvent(event);
        }
        if (eventType == this.gyro.getType()) {
            this.gyroEvent(event);
        }
    }
    
    //========================================================================//
    //Event Handlers==========================================================//
    
    /**
     * Handles accelerometer events.
     * @param event event reported by accelerometer
     */
    private void accelEvent(SensorEvent event) {
        //Calculate new position
        float[] ev = new float[]{event.values[0], 
            event.values[1], 
            event.values[2]};
        float[] pos = this.p.updatePosition(ev);
        
        //Set TextView elements for x, y, and z position
        TextView tvX = (TextView) findViewById(R.id.pos_x);
        TextView tvY = (TextView) findViewById(R.id.pos_y);
        TextView tvZ = (TextView) findViewById(R.id.pos_z);
        
        tvX.setText(Float.toString(pos[0]));
        tvY.setText(Float.toString(pos[1]));
        tvZ.setText(Float.toString(pos[2]));
    }
    
    /**
     * Handles gyroscope events.
     * @param event event reported by gyroscope
     */
    private void gyroEvent(SensorEvent event) {
        //Calculate new orientation
        float[] ev = new float[]{event.values[0], 
            event.values[1], 
            event.values[2]};
        float[] ori = this.p.updateOrient(ev);

        //Set TextView elements for rotation about x,y,z
        TextView tvX = (TextView) findViewById(R.id.orient_x);
        TextView tvY = (TextView) findViewById(R.id.orient_y);
        TextView tvZ = (TextView) findViewById(R.id.orient_z);
        
        tvY.setText(Float.toString(ori[0]));
        tvY.setText(Float.toString(ori[1]));
        tvZ.setText(Float.toString(ori[2]));
    }
    
    //========================================================================//
    //Private Methods=========================================================//
    
    /**
     * Method to filter noise.
     * @param val value from sensor
     * @param filt threshold value
     * @return 0 if value is less than filter threshold
     */
    private float hiPFilter(float val, float filt) {
        if (val < filt) {
            return (float) 0.0;
        } else {
            return val;
        }
    }
    
    //========================================================================//
    //Junk Area===============================================================//

//    /**
//     * Flag for accelerometer initialization.
//     */
//    private boolean accInit;
//    
//    /**
//     * Flag for gyroscope initialization.
//     */
//    private boolean gyroInit;
//    /**
//     * Filtering value for noise.
//     */
//    private final float accNOISE = (float) 1.0;
   

//    /**
//     * Handles accelerometer events.
//     * @param event event reported by accelerometer
//     */
//    private void accelEvent(SensorEvent event) {
//        //Access TextView elements for x, y, and z acceleration, and ImageView
//        TextView tvX = (TextView) findViewById(R.id.accel_x);
//        TextView tvY = (TextView) findViewById(R.id.accel_y);
//        TextView tvZ = (TextView) findViewById(R.id.accel_z);
////        ImageView iv = (ImageView) findViewById(R.id.image);
//        
//        //Store event values dx, dy, dz
//        float x = event.values[0];
//        float y = event.values[1];
//        float z = event.values[2];
//        
//        //Perform calculations for acceleration
//        if (!this.accInit) {
//            this.aLastX = x;
//            this.aLastY = y;
//            this.aLastZ = z;
//            tvX.setText("0.0");
//            tvY.setText("0.0");
//            tvZ.setText("0.0");
//            this.accInit = true;
//        } else {
//            //Calculate acceleration, with filtering and display values
//            float deltaX = this.hiPFilter(Math.abs(this.aLastX - x), accNOISE);
//            float deltaY = this.hiPFilter(Math.abs(this.aLastY - y), accNOISE);
//            float deltaZ = this.hiPFilter(Math.abs(this.aLastZ - z), accNOISE);
//            this.aLastX = x;
//            this.aLastY = y;
//            this.aLastZ = z;
//            tvX.setText(Float.toString(deltaX));
//            tvY.setText(Float.toString(deltaY));
//            tvZ.setText(Float.toString(deltaZ));
////            iv.setVisibility(View.VISIBLE);
////            if (deltaX > deltaY) {
////                iv.setImageResource(R.drawable.horizontal);
////            } else if (deltaY > deltaX) {
////                iv.setImageResource(R.drawable.vertical);
////            } else {
////                iv.setVisibility(View.INVISIBLE);
////            }
//        }
//    }
//    
//    /**
//     * Handles gyroscope events.
//     * @param event event reported by gyroscope
//     */
//    private void gyroEvent(SensorEvent event) {
//        //Access TextView elements for x, y, and z motion
//        TextView tvX = (TextView) findViewById(R.id.gyro_x);
//        TextView tvY = (TextView) findViewById(R.id.gyro_y);
//        TextView tvZ = (TextView) findViewById(R.id.gyro_z);
//        
//        //Store event values dx, dy, dz
//        float x = event.values[0];
//        float y = event.values[1];
//        float z = event.values[2];
//        
//        if (!this.gyroInit) {
//            this.gyroInit = true;
//        } else {
//            tvX.setText(Float.toString(x));
//            tvY.setText(Float.toString(x));
//            tvZ.setText(Float.toString(x));
//        
//            float omegaMag = (float) Math.sqrt(x * x + y * y + z * z);
//        }
//    }
    
}