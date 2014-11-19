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
    private boolean mInitialized;
    
    /**
     * Previous values for accelerometer.
     */
    private float mLastX, mLastY, mLastZ;    
    
    /**
     * Sensor manager.
     */
    private SensorManager mSensorManager;

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
    private final float NOISE = (float) 0.0;
    
    /**
     * Called when created.
     * @param savedInstanceState 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.mInitialized = false;
        this.mSensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);
        this.accel = this.mSensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
        
        this.mSensorManager.registerListener(this, this.accel, 
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * When app is paused, stop listening.
     */
    protected void onPause() {
        super.onPause();
        this.mSensorManager.unregisterListener(this);
    }

    /**
     * When app resumes, start listening again.
     */
    protected void onResume() {
        super.onResume();
        this.mSensorManager.registerListener(this, this.accel, 
                SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * What to do when a sensor changes
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
        if (!this.mInitialized) {
            this.mLastX = x;
            this.mLastY = y;
            this.mLastZ = z;
            tvX.setText("0.0");
            tvY.setText("0.0");
            tvZ.setText("0.0");
            this.mInitialized = true;
        } else {
            float deltaX = Math.abs(this.mLastX - x);
            float deltaY = Math.abs(this.mLastY - y);
            float deltaZ = Math.abs(this.mLastZ - z);
            if (deltaX < NOISE) deltaX = (float)0.0;
            if (deltaY < NOISE) deltaY = (float)0.0;
            if (deltaZ < NOISE) deltaZ = (float)0.0;
            this.mLastX = x;
            this.mLastY = y;
            this.mLastZ = z;
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