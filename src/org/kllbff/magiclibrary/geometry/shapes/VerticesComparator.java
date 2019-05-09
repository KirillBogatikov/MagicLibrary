package org.kllbff.magiclibrary.geometry.shapes;

import java.util.Comparator;

import org.kllbff.magiclibrary.geometry.Point;

public class VerticesComparator implements Comparator<Point> {
    public static final int DEGREES_ACCURACY = 3;
    
    private Point origin;
    private int accuracy; 
    
    public VerticesComparator(Point origin) {
        this(origin, DEGREES_ACCURACY);
    }
    
    public VerticesComparator(Point origin, int accuracy) {
        this.origin = origin;
        this.accuracy = accuracy;
    }
    
    @Override
    public int compare(Point a, Point b) {
        double angleA = Math.toDegrees(Math.atan2(a.getY() - origin.getY(), a.getX() - origin.getX()));
        double angleB = Math.toDegrees(Math.atan2(b.getY() - origin.getY(), b.getX() - origin.getX()));
        
        angleA = Math.ceil(angleA);
        angleB = Math.ceil(angleB);
        
        if(angleA == angleB || Math.abs(angleA - angleB) <= accuracy) {
            return Double.compare(a.distanceTo(origin), b.distanceTo(origin));
        }
        
        return Double.compare(angleA, angleB);
    }

}