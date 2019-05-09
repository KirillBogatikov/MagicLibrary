package org.kllbff.magiclibrary.geometry;

/**
 * <h3>Describes a simple 2D line</h3>
 * <p>Any 2D line can be represented as two points on the plane, but you chould remember that the line infinite in both directions.</p>
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
public class StraightLine {
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
        
        if(first.compareTo(second) < 0) {
            this.first = first;
            this.second = second;
        } else {
            this.first = second;
            this.second = first;
        }
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
    public Point getFirstPoint() {
        return first;
    }

    /**
     * Returns one of two describing points, far from origin point
     * 
     * @return one of two describing points, far from origin point
     */
    public Point getSecondPoint() {
        return second;
    }
    
    /**
     * Allows to change one of describing points
     * Automatically save new point as "first" if it's closest to origin from two points, or 
     * as "second" if it's the most remote
     * 
     * @param first any point lying on this line
     * @throws NullPointerException if any of points is null
     * @throws RuntimeException if points are equals
     */
    public void setFirstPoint(Point first) {
        checkPoints(second, first);
        if(first.compareTo(second) <= 0) {
            this.first = first;
        } else {
            Point temp = second;
            this.second = first;
            this.first = temp;
        }
    }

    /**
     * Allows to change one of describing points
     * Automatically save new point as "first" if it's closest to origin from two points, or 
     * as "second" if it's the most remote
     * 
     * @param second any point lying on this line
     * @throws NullPointerException if any of points is null
     * @throws RuntimeException if points are equals
     */
    public void setSecondPoint(Point second) {
        checkPoints(first, second);
        if(first.compareTo(second) >= 0) {
            this.second = second;
        } else {
            Point temp = first;
            this.first = second;
            this.second = temp;
        }
    }
    
    /**
     * Returns an angle between this line and positive x-axis, in radians
     * Calculated as <code>k = arctan(-a / b)</code>, where a and b - coefficients of the coordinates in th equation of the line
     * 
     * @return an angle between this line and positive x-axis, in radians
     */
    public double getAngle() {
        double a = first.getY() - second.getY();
        double b = second.getX() - first.getX();
        
        return Math.atan(-a / b);
    }

    /**
     * Returns point of intersection this line with specified line or <code>null</code> if intersection does not exist
     * 
     * @param other specified line
     * @return other of intersection this line with specified line
     */
    public Point getIntersection(StraightLine other) {
        double x1 = this.first.getX();
        double y1 = this.first.getY();
        double x2 = this.second.getX();
        double y2 = this.second.getY();
        
        double x3 = other.first.getX();
        double y3 = other.first.getY();
        double x4 = other.second.getX();
        double y4 = other.second.getY();
        
        double x = -((x1*y2-x2*y1)*(x4-x3)-(x3*y4-x4*y3)*(x2-x1))/((y1-y2)*(x4-x3)-(y3-y4)*(x2-x1));
        double y = ((y3-y4)*(-x)-(x3*y4-x4*y3))/(x4-x3);
        
        if(Double.isNaN(y)) {
            y = ((y1-y2)*(-x)-(x1*y2-x2*y1))/(x2-x1);
        }
                
        if(Double.isFinite(x) && Double.isFinite(y)) {
            return new Point(x, y);
        }
        return null;
    }
    
    /**
     * Checks if this line crosses the edge of the specified line
     * 
     * @param other specified line
     * @return true if one of specified line have an intersection points, false otherwise
     */
    public boolean hasIntersection(StraightLine other) {
        return getIntersection(other) != null;
    }
    
    /**
     * Checks if this shape contains specified point
     * 
     * @param point specified for checking point
     * @return true if the point located on this line; false otherwise
     */
    public boolean contains(Point point) {
        double a = first.getY() - second.getY();
        double b = second.getX() - first.getX();
        double c = first.getX() * second.getY() - second.getX() * first.getY();
        
        return point.getX() * a + point.getY() * b + c == 0;
    }    

    /**
     * Returns one item from PointPosition, representing position of specified point on this shape<br>
     * Can return:
     * <dl>
     *     <dt>INSIDE</dt>
     *     <dd>If this line contians specified point</dd>
     *     <dt>ABOVE</dt>
     *     <dd>If specified point lies in the upper half-plane with respect to the line</dd>
     *  <dt>BELOW</dt>
     *  <dd>If specified point lies in the lower half-plane with respect to the line</dd>
     * </dl>  
     * 
     * @param point specified Point for checking
     * @return one of enum PointPosition
     */
    public PointPosition getPointPosition(Point point) {
        if(contains(point)) {
            return PointPosition.INSIDE;
        }
        
        return PointPosition.OUTSIDE;
    }
    
    /**
     * Returns a string representation of the line segment, containing this vaertices current coordinates 
     * 
     * @return String representation of the form: <code>"Straight line at (" + {@link Point#getX()} + ", " + {@link Point#getY()} + ") and (" +  + {@link Point#getX()} + ", " + {@link Point#getY()} + ")"</code>
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("StraightLine at (");
        if(first == null) {
            builder.append("unknown, unknown");
        } else {
            builder.append(first.getX()).append(", ").append(first.getY());
        }
        builder.append(") and (");
        if(second == null) {
            builder.append("unknown, unknown");
        } else {
            builder.append(second.getX()).append(", ").append(second.getY());
        }
        builder.append(")");
        
        return builder.toString();
    }
    
    protected void checkPoints(Point a, Point b) {
        if(a == null || b == null) {
            throw new NullPointerException("Point is null");
        }        
        
        if(a.equals(b)) {
            throw new RuntimeException("Cannot change points of " + this + ": points are the same");
        }
    }
}
