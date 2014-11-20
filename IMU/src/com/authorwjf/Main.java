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
    //Fields and Values=======================================================//
    
    /**
     * Flag for initialization.
     */
    private boolean initialized;
    
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
    
    /**
     * Filtering value for noise.
     */
    private final float accNOISE = (float) 1.0;
    
    
    //========================================================================//
    //Event Handlers==========================================================//
    
    /**
     * Called when created.
     * @param savedInstanceState 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Initialize activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.initialized = false;
        
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
        this.accel = this.mGyroManager.getDefaultSensor(
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
     * @param event 
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
    //Private Methods=========================================================//
    
    /**
     * Handles accelerometer events.
     * @param event event reported by accelerometer
     */
    private void accelEvent(SensorEvent event) {
        //Access TextView elements for x, y, and z acceleration, and ImageView
        TextView tvX = (TextView) findViewById(R.id.accel_x);
        TextView tvY = (TextView) findViewById(R.id.accel_y);
        TextView tvZ = (TextView) findViewById(R.id.accel_z);
        ImageView iv = (ImageView) findViewById(R.id.image);
        
        //Store event values dx, dy, dz
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        
        //Perform calculations for acceleration
        if (!this.initialized) {
            this.aLastX = x;
            this.aLastY = y;
            this.aLastZ = z;
            tvX.setText("0.0");
            tvY.setText("0.0");
            tvZ.setText("0.0");
            this.initialized = true;
        } else {
            //Calculate acceleration, with filtering and display values
            float deltaX = Math.abs(this.aLastX - x);
            float deltaY = Math.abs(this.aLastY - y);
            float deltaZ = Math.abs(this.aLastZ - z);
            if (deltaX < accNOISE) deltaX = (float)0.0;
            if (deltaY < accNOISE) deltaY = (float)0.0;
            if (deltaZ < accNOISE) deltaZ = (float)0.0;
            this.aLastX = x;
            this.aLastY = y;
            this.aLastZ = z;
            tvX.setText(Float.toString(deltaX));
            tvY.setText(Float.toString(deltaY));
            tvZ.setText(Float.toString(deltaZ));
            iv.setVisibility(View.VISIBLE);
            if (deltaX > deltaY) {
                iv.setImageResource(R.drawable.horizontal);
            } else if (deltaY > deltaX) {
                iv.setImageResource(R.drawable.vertical);
            } else {
                iv.setVisibility(View.INVISIBLE);
            }
        }
    }
    
    /**
     * Handles gyroscope events.
     * @param event event reported by gyroscope
     */
    private void gyroEvent(SensorEvent event) {
        //Access TextView elements for x, y, and z motion
        TextView tvX = (TextView) findViewById(R.id.accel_x);
        TextView tvY = (TextView) findViewById(R.id.accel_y);
        TextView tvZ = (TextView) findViewById(R.id.accel_z);
        
        //Store event values dx, dy, dz
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        
        tvX.setText(Float.toString(x));
        tvY.setText(Float.toString(x));
        tvZ.setText(Float.toString(x));
        
        //float omegaMag = (float) Math.sqrt(x * x + y * y + z * z);
    }
    
    
    
}