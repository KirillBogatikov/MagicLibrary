package org.kllbff.magic.geometry;

/**
 * <h3>Represents any geometry primitive</h3>
 * 
 * @author Kirill Bogatikov
 * @version 1.0
 * @since 1.0
 */
public interface Primitive {
    /**
     * Translates this point by given coordinates by adding specified numbers to origin coordinates  
     * 
     * @param x specified offset on x-axis
     * @param y specified offset on y-axis
     */
    public void translate(double x, double y);
    
    /**
     * Rotates this point by specified angle clockwise
     * <p>
     *     This method rotates point relative to the origin (0, 0)
     * </p>
     * 
     * @param angle specified angle, in radians
     */
    public void rotateByOrigin(double angle);
    
    public Primitive clone();
    
    public String toString();
}
