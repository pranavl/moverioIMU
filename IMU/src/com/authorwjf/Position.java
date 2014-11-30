
package com.authorwjf;

/**
 * Calculates position and orientation of Moverio device.
 * @author Pranav
 */
public class Position {
    
    //========================================================================//
    //Flags, Fields, and Values===============================================//
    
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
    
    
    //========================================================================//
    //Methods=================================================================//

    /**
     * Update position field from accelerometer event.
     * @param values event values
     * @return position vector
     */
    public float[] updatePosition(float[] values) {
        this.oldPos = this.pos;
        this.pos = this.calcPosition(values);
        return this.pos;
    }
    
    /**
     * Update position field from gyroscope event.
     * @param values event values
     * @return orientation vector
     */
    public float[] updateOrient(float[] values) {
        this.oldOrient = this.orient;
        this.orient = this.calcOrient(values);
        return this.orient;
    }

        
    //========================================================================//
    //Private Methods=========================================================//

    /**
     * Uses linear acceleration to calculate x,y,z position.
     * @param values event values
     * @return updated position vector
     */
    private float[] calcPosition(float[] values) {
        return values;
    }
    
    /**
     * Uses gyroscope to calculate x,y,z orientation.
     * @param values event values
     * @return updated orientation vector
     */
    private float[] calcOrient(float[] values) {
        return values;
    }
    
}
