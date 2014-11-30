
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
    
    
    //========================================================================//
    //Constructors============================================================//
    
    /**
     * Default constructor.
     */
    public Position() {
        this.pos = new float[]{(float) 0.0, (float) 0.0, (float) 0.0};
        this.orient = new float[]{(float) 0.0, (float) 0.0, (float) 0.0};
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
    }
    
    /**
     * Constructor with both fields declared.
     * @param p position vector
     * @param o orientation vector
     */
    public Position(float[] p, float[] o) {
        this.pos = p;
        this.orient = o;
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
        
        return this.pos;
    }
    
    /**
     * Update position field from gyroscope event.
     * @param values event values
     * @return orientation vector
     */
    public float[] updateOrient(float[] values) {
        
        return this.orient;
    }
    
}
