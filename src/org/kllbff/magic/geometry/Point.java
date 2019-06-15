package org.kllbff.magic.geometry;

import org.kllbff.magic.math.PlanimetryValues;

/**
 * <h3>Represents a point in two-dimensional space (plane)</h3>
 * <p>A simple point has two characteristics: 
 *     <ul>
 *      <li>position at x-axis,</li>
 *      <li>position at y-axis.</li>
 *  </ul>
 *  But this point has some auxiliary methods for fast and simple work with points:
 *  <ul>
 *      <li>getters for x-axis and y-axis</li>
 *      <li>setters for x-axis and y-axis</li>
 *      <li>method for computing distance between two points</li>
 *      <li>method for computing distance between point and two coordinates</li>
 *      <li>method for comparing point and some other object (usually other point)</li>
 *  </ul></p>
 *  <p>Class implements {@link java.lang.Comparable} interface and allows using simple
 *     sorting methods on sequence of points</p> 
 * 
 * @author Kirill Bogatikov
 * @version 1.0
 * @since 1.0
 */
public class Point implements Comparable<Point>, Primitive {
    private double distanceToStart;
    private double x, y;
    
    /**
     * Default constructor
     * <p>Initializes point as Origin point with coordinates (0, 0)</p>
     */
    public Point() {
        this(0, 0);
    }
    
    /**
     * Initializes point by given coordinates
     * 
     * @param x point's coordinate on the x-axis
     * @param y point's coordinate on the y-axis
     */
    public Point(double x, double y) {
        setX(x);
        setY(y);
    }
    
    /**
     * Returns point's current coordinate on the x-axis 
     * 
     * @return point's current coordinate on the x-axis 
     */
    public double getX() {
        return x;
    }
    
    /**
     * Returns point's current coordinate on the y-axis 
     * 
     * @return point's current coordinate on the y-axis 
     */
    public double getY() {
        return y;
    }
    
    /**
     * Change point's current coordinate on the x-axis to given value
     * 
     * @param x point's x-axis position
     * @return reference to this object
     */
    public Point setX(double x) {
        this.x = x;
        this.distanceToStart = distanceTo(0, 0);
        return this;
    }
    
    /**
     * Change point's current coordinate on the y-axis to given value
     * 
     * @param y point's y-axis position
     * @return reference to this object
     */
    public Point setY(double y) {
        this.y = y;
        this.distanceToStart = distanceTo(0, 0);
        return this;
    }
    
    /**
     * Compute distance between two points - this and given
     * 
     * @param other point, distance to which will be computed 
     * @return distance between two points - this and given
     */
    public double distanceTo(Point other) {
        return this.distanceTo(other.x, other.y);
    }
    
    /**
     * Compute distance between two points - this and point at given coordinates
     * 
     * @param x x-axis coordinate of point, distance to which will be computed 
     * @param y y-axis coordinate of point, distance to which will be computed
     * @return distance between two points - this and given
     */
    public double distanceTo(double x, double y) {
        return Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));
    }
    
    /**
     * Indicates whether some other object is "equal to" this one. 
     * 
     * @return true if other point's coordinates equals to this one's coordinates  
     */
    @Override
    public boolean equals(Object other) {
        try {
            Point p = (Point)other;
            if(Double.doubleToLongBits(x) == Double.doubleToLongBits(p.x) &&
               Double.doubleToLongBits(y) == Double.doubleToLongBits(p.y)) {
                return true;
            }
        } catch(ClassCastException | NullPointerException ex){}
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = 37 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 37 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
    
    /**
     * Compares two points according using distance between every point and Origin point by the following rule:
     * <p><ul>
     *  <li>if distance between this point and Origin greater than distance between specified point and Origin, returns positive integer</li>
     *  <li>if distance between this point and Origin less than distance between specified point and Origin, returns negative integer</li>
     *  <li>if distance between this point and Origin equals distance between specified point and Origin, returns zero</li>   
     * </ul></p>
     * 
     * @param other the point to be compared
     * @return a negative integer, zero, or a positive integer as this point is less than, equal to, or greater than the specified point
     */
    @Override
    public int compareTo(Point other) {
        /* less */
        if(other.distanceToStart < this.distanceToStart) {
            return 1;
        }
        
        /* more */
        if(other.distanceToStart > this.distanceToStart) {
            return -1;
        }
        
        /* equals */
        return 0;
    }
    
    /**
     * Returns new instance of Point class, equals to this by content, but difference by objects in memory
     */
    @Override
    public Point clone() {
        return new Point(x, y);
    }

    /**
     * Translates this point by given coordinates
     * <p>
     * This method only increases coordinates of point by specified number of unit segments. 
     * Example:
     * <pre>
     * <code>
     *     Point p = new Point(0, 0);
     *     
     *     p.translate(5, 3);
     *     //now p in 5, 3
     *     
     *     p.translate(1, 2);
     *     //now p in 6 (5 + 1), 5 (3 + 2)
     * </code>
     * </pre>
     * </p>
     */
    @Override
    public void translate(double x, double y) {
        this.x += x;
        this.y += y;
    }
    
    @Override
    public void rotateByOrigin(double angle) {
        angle = angle % (Math.PI * 2);
        if(angle == 0 || angle == Math.PI * 2) {
            return;
        }
        
        double cos = PlanimetryValues.cos(angle), sin = PlanimetryValues.sin(angle);
        double nx = x * cos - y * sin;
        double ny = x * sin + y * cos;
        
        this.x = nx;
        this.y = ny;
    }
    
    /**
     * Rotates this point around specified point
     * <p>New coordinates of point can be calculated as:<br>
     *    <i>x = x<sub>0</sub> + (x - x<sub>0</sub>) * cos(&#945;) - (y - y<sub>0</sub>) * sin(&#945;)</i><br>
     *    <i>y = y<sub>0</sub> + (y - y<sub>0</sub>) * sin(&#945;) - (x - x<sub>0</sub>) * cos(&#945;)</i><br> 
     * </p>
     *  
     * @param angle specified angle, in radians
     * @param point specified point, center of rotation
     */
    public void rotateBy(double angle, Point point) {
        angle = angle % (Math.PI * 2);
        if(angle == 0 || angle == Math.PI * 2) {
            return;
        }
        
        double x0 = point.getX();
        double y0 = point.getY();
        
        double cos = PlanimetryValues.cos(angle), sin = PlanimetryValues.sin(angle);
                
        double nx = x0 + (x - x0) * cos - (y - y0) * sin;
        double ny = y0 + (y - y0) * sin + (x - x0) * cos;   
        
        this.x = nx;
        this.y = ny;
    }
    
    /**
     * Returns a string representation of the point, containing this point's current coordinates 
     * 
     * @return String representation of the form: <code>"Point at (" + {@link #getX()} + ", " + {@link #getY()} + ")"</code>
     */
    @Override
    public String toString() {
        return "Point at (" + getX() + ", " + getY() + ")";
    }
}