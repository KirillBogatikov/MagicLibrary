package org.kllbff.magic.geometry.utils;

import java.util.Comparator;

import org.kllbff.magic.geometry.Point;

/**
 * <h3>Represents tool for sorting shape's vertices counter-clockwise</h3>
 * 
 * @author Kirill Bogatikov
 * @version 1.0
 * @since 0.0
 */
public class VerticesComparator implements Comparator<Point> {
    /**
     * Represents accuracy of angles comparing in sorting, in degrees
     */
    public static final int DEGREES_ACCURACY = 3;
    
    private Point origin;
    private int accuracy; 
    
    /**
     * Constructs new VerticesComparator for shape with center at <i>origin</i> point 
     * 
     * @param origin center of shape, which vertices will be sorted by this comparator
     */
    public VerticesComparator(Point origin) {
        this(origin, DEGREES_ACCURACY);
    }
    
    /**
     * Constructs new VerticesComparator for shape with center at <i>origin</i> point
     * with specified comparing accuracy
     * 
     * @param origin center of shape, which vertices will be sorted by this comparator
     * @param accuracy accuracy of comparing, in degrees
     */
    public VerticesComparator(Point origin, int accuracy) {
        this.origin = origin;
        this.accuracy = accuracy;
    }
    
    @Override
    public int compare(Point a, Point b) {
        double angleA = (Math.toDegrees(Math.atan2(a.getY() - origin.getY(), a.getX() - origin.getX())) + 360) % 360;
        double angleB = (Math.toDegrees(Math.atan2(b.getY() - origin.getY(), b.getX() - origin.getX())) + 360) % 360;
        
        angleA = Math.ceil(angleA);
        angleB = Math.ceil(angleB);
                
        if(angleA == angleB || Math.abs(angleA - angleB) <= accuracy) {
            return Double.compare(a.distanceTo(origin), b.distanceTo(origin));
        }
        
        return Double.compare(angleA, angleB);
    }

}