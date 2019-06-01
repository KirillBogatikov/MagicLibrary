package org.kllbff.magic.geometry.lines;

import org.kllbff.magic.geometry.Point;
import org.kllbff.magic.geometry.PointPosition;

/**
 * <h3>Describes a simple 2D line</h3>
 * <p>Any 2D line can be represented as two points on the plane, between which this line is located</p>
 * <p>This class extends StraightLine class and has all it method, but overrides some methods for correct work with segments</p>
 * 
 * @author Kirill Bogatikov
 * @version 1.0
 * @since 1.0
 */
public class LineSegment extends StraightLine {

    /**
     * Initializes line by two given points
     * 
     * @param first any point lying on this line
     * @param second any point lying on this line
     * @throws NullPointerException if any of points is null
     * @throws RuntimeException if points are equals
     */
    public LineSegment(Point first, Point second) {
        super(first, second);
    }
    
    /**
     * Initializes line by four given coordinates (coordinates of two points)
     * 
     * @param x1 x-axis coordinate of first any point lying on this line
     * @param y1 y-axis coordinate of first any point lying on this line
     * @param x2 x-axis coordinate of second any point lying on this line
     * @param y2 y-axis coordinate of second any point lying on this line
     * @throws NullPointerException if any of points is null
     * @throws RuntimeException if points are equals
     */
    public LineSegment(double x1, double y1, double x2, double y2) {
        this(new Point(x1, y1), new Point(x2, y2));
    }
   
    /**
     * Returns point of intersection this line with specified line or null if intersection does not exist
     * 
     * <p>
     *     This method supports only StraightLine and LineSegment instances
     * </p>
     */
    @Override
    public Point getIntersection(Line other) {
        Point point = super.getIntersection(other);
        if(point == null || isOutside(point)) {
            return null;
        }
        return point;
    }
    
    @Override
    public PointPosition getPointPosition(Point point) {
        PointPosition pos = super.getPointPosition(point);
        if(pos == PointPosition.OUTSIDE || isOutside(point)) {
            return PointPosition.OUTSIDE;
        }
        return PointPosition.INSIDE;
    }
    
    @Override
    public LineSegment clone() {
        return new LineSegment(first, second);
    }

    @Override
    public int hashCode() {
        return 39 * (39 + first.hashCode()) + second.hashCode();
    }

    private boolean isOutside(Point point) {
        Point middle = new Point((first.getX() + second.getX()) / 2, (first.getY() + second.getY()) / 2);
        double d = point.distanceTo(middle);
        return d > first.distanceTo(middle);  
    }
}
