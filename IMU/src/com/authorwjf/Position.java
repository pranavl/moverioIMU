
package com.authorwjf;

import android.hardware.SensorEvent;

/**
 * Calculates position and orientation of Moverio device.
 * @author Pranav
 */
public class Position {
    
    //========================================================================//
    //Flags, Fields, and Values===============================================//
    
    /**
     * Time of the last accelerometer event (in seconds).
     */
    private float lastAcc = (float) 0.0;
    
    private float startTime;
    
    /**
     * x,y,z coordinates of position.
     */
    private float[] pos;
    
    /**
     * Rotation about x,y,z axis.
     */
    private float[] orient;

    /**
     * Last x,y,z coordinates of position.
     */
    private float[] oldPos;
    
    /**
     * Last rotation about x,y,z axis.
     */
    private float[] oldOrient;
    
    /**
     * Previous acceleration values.
     */
    private float[] lastA;
    
    /**
     * Previous linear velocity values.
     */
    private float[] lastV;
    
    /**
     * Previous angular velocity values.
     */
    private float[] lastW;
    
    //========================================================================//
    //Constructors============================================================//
    
    /**
     * Default constructor.
     */
    public Position() {
        this.pos = new float[]{(float) 0.0, (float) 0.0, (float) 0.0};
        this.orient = new float[]{(float) 0.0, (float) 0.0, (float) 0.0};
        this.oldPos = new float[]{(float) 0.0, (float) 0.0, (float) 0.0};
        this.oldOrient = new float[]{(float) 0.0, (float) 0.0, (float) 0.0};
        
        this.lastA = new float[]{(float) 0.0, (float) 0.0, (float) 0.0};
        this.lastV = new float[]{(float) 0.0, (float) 0.0, (float) 0.0};
    }

    /**
     * Constructor with one field declared.
     * @param v vector for either position or orientation
     * @param w flag for which field is given
     *      true - position
     *      false - orientation
     */
    public Position(float[] v, boolean w) {
        if (w) {
            this.pos = v;
            this.orient = new float[]{(float) 0.0, (float) 0.0, (float) 0.0};
        } else {
            this.pos = new float[]{(float) 0.0, (float) 0.0, (float) 0.0};
            this.orient = v;
        }
        this.oldPos = new float[]{(float) 0.0, (float) 0.0, (float) 0.0};
        this.oldOrient = new float[]{(float) 0.0, (float) 0.0, (float) 0.0};
    }
    
    /**
     * Constructor with both fields declared.
     * @param p position vector
     * @param o orientation vector
     */
    public Position(float[] p, float[] o) {
        this.pos = p;
        this.orient = o;
        this.oldPos = new float[]{(float) 0.0, (float) 0.0, (float) 0.0};
        this.oldOrient = new float[]{(float) 0.0, (float) 0.0, (float) 0.0};
    }
    
    //========================================================================//
    //Accessors===============================================================//

    /**
     * Access position.
     * @return pos
     */
    public float[] getPos() {
        return this.pos;
    }
    
    /**
     * Access orientation.
     * @return orient 
     */
    public float[] getOrient() {
        return this.orient;
    }
    
    public void setStartTime(float t) {
        this.startTime = t;
    }
    
    //========================================================================//
    //Methods=================================================================//

    /**
     * Update position field from accelerometer event.
     * @param event accelerometer sensor event
     * @return position vector
     */
    public float[] updatePosition(SensorEvent event) {
        this.pos = this.calcPosition(event);
        return this.pos;
    }
    
    /**
     * Update position field from gyroscope event.
     * @param event gyroscope sensor event
     * @return orientation vector
     */
    public float[] updateOrient(SensorEvent event) {
        this.oldOrient = this.orient;
        this.orient = this.calcOrient(event);
        return this.orient;
    }

        
    //========================================================================//
    //Private Methods=========================================================//

    /**
     * Uses linear acceleration to calculate x,y,z position.
     * @param event accelerometer sensor event
     * @return updated position vector
     */
    private float[] calcPosition(SensorEvent event) {
        //Time of event in seconds
        float evTime = (float) (event.timestamp*Math.pow(10, -9) - this.startTime);
        
        //Calculate velocity
        //First, create line modeling acceleration
        float[] m = new float[3];
        for (int i = 0; i < 3; i++) {
            m[i] = (float) (event.values[i] - this.lastA[i]);
        }
        float[] b = new float[3];
        for (int j = 0; j < 3; j++) {
            b[j] = (float) (event.values[j] - m[j] * evTime);
        }
        
        //Integrate to calculate velocity
        float[] v = new float[3];
        for (int l = 0; l < 3; l++) {
            v[l] = (1 / 2) * m[l] * this.evalAt(evTime, this.lastAcc, 2)
                    + b[l] * this.evalAt(evTime, this.lastAcc, 1)
                    + this.lastV[l];
        }
        //Integrate to calculate position
        float[] x = new float[3];
        for (int n = 0; n < 3; n++) {
            x[n] = (1 / 6) * m[n] * this.evalAt(evTime, this.lastAcc, 3)
                    + (1 / 2) * b[n] * this.evalAt(evTime, this.lastAcc, 2)
                    + this.lastV[n] * this.evalAt(evTime, this.lastAcc, 1)
                    + this.oldPos[n];
        }
        
        //Update all last fields
        this.lastAcc = evTime;
        this.lastA = event.values;
        this.lastV = v;
        this.oldPos = this.pos;
        
        return x;
    }
    
    /**
     * Uses gyroscope to calculate x,y,z orientation.
     * @param event gyroscope sensor event
     * @return updated orientation vector
     */
    private float[] calcOrient(SensorEvent event) {
        return null;
    }
    
    /**
     * Integration helper: evaluate function at t2 and t1.
     * @param t2 later time
     * @param t1 previous time
     * @param deg degree at which to be evaluated
     * @return t2^deg - t1^deg
     */
    private float evalAt(float t2, float t1, int deg) {
        return (float) (Math.pow(t2, deg) - Math.pow(t1, deg));
    }
    
}
