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
    private final float accNOISE = (float) 0.0;
    
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
        
        //Set up accelerometer and listener
        this.mAccelManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);
        this.accel = this.mAccelManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
        this.mAccelManager.registerListener(this, this.accel, 
                SensorManager.SENSOR_DELAY_NORMAL);
        
        //Set up gyroscope and listener
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
    }

    /**
     * When app resumes, start listening again.
     */
    protected void onResume() {
        super.onResume();
        this.mAccelManager.registerListener(this, this.accel, 
                SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * What to do when a sensor value changes.
     * @param event 
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        TextView tvX = (TextView) findViewById(R.id.x_axis);
        TextView tvY = (TextView) findViewById(R.id.y_axis);
        TextView tvZ = (TextView) findViewById(R.id.z_axis);
        ImageView iv = (ImageView) findViewById(R.id.image);
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (!this.initialized) {
            this.aLastX = x;
            this.aLastY = y;
            this.aLastZ = z;
            tvX.setText("0.0");
            tvY.setText("0.0");
            tvZ.setText("0.0");
            this.initialized = true;
        } else {
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
}