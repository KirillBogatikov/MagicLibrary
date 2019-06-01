package org.kllbff.magic.geometry.lines;

import org.kllbff.magic.geometry.Point;
import org.kllbff.magic.geometry.PointPosition;
import org.kllbff.magic.math.algothms.KramerAlgorithm;

/**
 * <h3>Describes a simple 2D line</h3>
 * <p>Any 2D line can be represented as two points on the plane, but you should remember that the line infinite in both directions.</p>
 * <p>This class has some methods:
 *     <ul>
 *         <li>getters and setters for two points</li>
 *         <li>finding the point of intersection of two line: this and specified</li>
 *         <li>calculating angle between line and positive x-axis</li>
 *         <li>determining the presence of an intersection point</li>
 *         <li>detection point position about this line</li>
 *         <li>determining whether a point is contained on a straight line</li>
 *     </ul>
 * </p>
 * 
 * @author Kirill Bogatikov
 * @version 1.0
 * @since 1.0
 */
public class StraightLine extends Line {
    protected Point first, second;
    
    /**
     * Initializes line by two given points
     * 
     * @param first any point lying on this line
     * @param second any point lying on this line
     * @throws NullPointerException if any of points is null
     * @throws RuntimeException if points are equals
     */
    public StraightLine(Point first, Point second) {
        checkPoints(first, second);
        
        this.first = first;
        this.second = second;
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
    public StraightLine(double x1, double y1, double x2, double y2) {
        this(new Point(x1, y1), new Point(x2, y2));
    }
    
    /**
     * Returns one of two describing points, closest to origin point
     * 
     * @return one of two describing points, closest to origin point
     */
    @Override
    public Point getFirstPoint() {
        return first.compareTo(second) == -1 ? first : second;
    }

    /**
     * Returns one of two describing points, far from origin point
     * 
     * @return one of two describing points, far from origin point
     */
    @Override
    public Point getSecondPoint() {
        return first.compareTo(second) == 1 ? first : second;
    }
    
    /**
     * Returns an angle between this line and positive x-axis, in radians
     * <p>Calculated as <code>k = arctan(-a / b)</code>, where a and b - coefficients of the coordinates in th equation of the line</p>
     * 
     * @return an angle between this line and positive x-axis, in radians
     */
    public double getAngle() {
        double a = first.getY() - second.getY();
        double b = second.getX() - first.getX();
        
        return Math.atan(-a / b);
    }
    
    /**
     * Allows to change one of describing points
     * <p>Automatically save new point as "first" if it's closest to origin from two points, or 
     * as "second" if it's the most remote</p>
     * 
     * @param first any point lying on this line
     * @throws NullPointerException if any of points is null
     * @throws RuntimeException if points are equals
     */
    public void setFirstPoint(Point first) {
        checkPoints(second, first);
        
        this.first = first;
    }

    /**
     * Allows to change one of describing points
     * <p>Automatically save new point as "first" if it's closest to origin from two points, or 
     * as "second" if it's the most remote</p>
     * 
     * @param second any point lying on this line
     * @throws NullPointerException if any of points is null
     * @throws RuntimeException if points are equals
     */
    public void setSecondPoint(Point second) {
        checkPoints(first, second);

        this.second = second;
    }
    
    /**
     * Returns point of intersection this line with specified line or null if intersection does not exist
     * 
     * <p>
     *     This method supports only StraightLine instances
     * </p>
     */
    @Override
    public Point getIntersection(Line other) {
        double x1 = first.getX(), y1 = first.getY();
        double x2 = second.getX(), y2 = second.getY();
        double x3 = other.getFirstPoint().getX(), y3 = other.getFirstPoint().getY();
        double x4 = other.getSecondPoint().getX(), y4 = other.getSecondPoint().getY();
        
        KramerAlgorithm solver = new KramerAlgorithm(2);
        solver.add(x2 - x1, x4 - x3);
        solver.add(y2 - y1, y4 - y3);
        
        double[] t = solver.solve(x3 - x1, y3 - y1);
        if(Double.isInfinite(t[0]) || Double.isInfinite(t[1])) {
            return null;
        }
            
        Point p = new Point(x1 + (x2 - x1) * t[0], y1 + (y2 - y1) * t[1]);
        if(other.contains(p)) {
            return p;
        }
        return null;
    }

    @Override
    public PointPosition getPointPosition(Point point) {
        double a = first.getY() - second.getY();
        double b = second.getX() - first.getX();
        double c = first.getX() * second.getY() - second.getX() * first.getY();
        
        if(point.getX() * a + point.getY() * b + c == 0) {
            return PointPosition.INSIDE;
        }
        return PointPosition.OUTSIDE;
    }
    
    /**
     * Returns a StraightLine perpendicular to this line
     * 
     * @param point line through which perpendicular will be drawn
     * @return StraightLine perpendicular to this line
     */
    public StraightLine getPerpendicular(Point point) {
        double angle = getAngle() + Math.PI / 2;
        return new StraightLine(point, new Point(point.getX() + Math.cos(angle), point.getY() + Math.sin(angle)));
    }
    
    @Override
    public StraightLine clone() {
        return new StraightLine(first, second);
    }

    @Override
    public void translate(double x, double y) {
        first.translate(x, y);
        second.translate(x, y);
    }

    @Override
    public void rotateByOrigin(double angle) {
        first.rotateByOrigin(angle);
        second.rotateByOrigin(angle);
    }

    @Override
    public int hashCode() {
        return 38 * (38 + first.hashCode()) + second.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && obj instanceof StraightLine) {
            StraightLine line = (StraightLine)obj;
            return line.first.equals(first) && line.second.equals(second);
        }
        return false;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Straight line through (")
               .append(first.getX()).append(", ").append(first.getY())
               .append(") and (")
               .append(second.getX()).append(", ").append(second.getY())
               .append(")");
        return builder.toString();
    }

    private void checkPoints(Point first, Point second) {
        if(first == null || second == null) {
            throw new NullPointerException("Point is null");
        }   
        if(first.equals(second)) {
            throw new RuntimeException("Cannot create line by two same points");
        }
    }
}
