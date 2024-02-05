package svgTester.struct;

import java.io.Serializable;

/**
 * DValue is one value of a DBlock. This DValue can be a number or coordinates x and y, both is not possible.
 */
public class DValue implements Serializable {

    private boolean isPoint;
    private double x;
    private double y;
    private double rel;

    /**
     * Constructor to insert coordinates to this DValue
     * @param pX X Cord
     * @param pY Y Cord
     */
    public DValue(double pX, double pY) {
        isPoint = true;
        this.x = pX;
        this.y = pY;
    }

    /**
     * Constructor to insert a real number to this DValue
     * @param pRel real Value
     */
    public DValue(double pRel) {
        isPoint = false;
        this.rel = pRel;
    }

    /**
     * Test if this DValue describes a point. (if it has x and y coordniates)
     * @return true if the DValue is has cords
     */
    public boolean isPoint() {
        return this.isPoint;
    }

    /**
     * Returns the X value of the DValue if its a Point. If not, it will return 0.
     * @return X value or 0 if not a point.
     */
    public double getX() {
        if(isPoint) {
            return this.x;
        }
        return 0.0;
    }

    /**
     * Returns the Y value of the DValue if its a Point. If not, it will return 0.
     * @return Y value or 0 if not a point.
     */
    public double getY() {
        if(isPoint) {
            return this.y;
        }
        return 0.0;
    }

    /**
     * Returns the value of the DValue if its not a Point. If not, it will return 0.
     * @return The value or 0 if its a point.
     */
    public double getRel() {
        if(!isPoint) {
            return this.rel;
        }
        return 0.0;
    }

    /**
     * Prepares a String of the DValue. Form: (Point) x,y (reel number) v
     * @return String of D Value
     */
    public String printValue() {
        if(isPoint) {
            return this.x+","+this.y;
        }else{
            return this.rel+"";
        }
    }
}
