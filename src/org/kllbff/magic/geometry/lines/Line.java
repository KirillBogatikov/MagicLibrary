package org.kllbff.magic.geometry.lines;

import org.kllbff.magic.geometry.Point;
import org.kllbff.magic.geometry.PointPosition;
import org.kllbff.magic.geometry.Primitive;

public abstract class Line implements Primitive {
    /**
     * Returns point of intersection this line with specified line or <code>null</code> if intersection does not exist
     * 
     * @param other specified line
     * @return point of intersection this line with specified line or null
     */
    public abstract Point getIntersection(Line other);
    
    /**
     * Checks if this line crosses the specified line
     * 
     * @param other specified line
     * @return true if one of specified line have an intersection points, false otherwise
     */
    public boolean hasIntersection(Line other) {
        return getIntersection(other) != null;
    }
    
    /**
     * Checks if this line contains specified point
     * 
     * @param point specified for checking point
     * @return true if the point located on this line; false otherwise
     */
    public boolean contains(Point point) {
        return getPointPosition(point) != PointPosition.OUTSIDE;
    }    

    /**
     * Returns one item from PointPosition, representing position of specified point on this line<br>
     * 
     * @param point specified Point for checking
     * @return one of enum PointPosition
     */
    public abstract PointPosition getPointPosition(Point point);
    
    /**
     * Returns the most nearest to origin point
     * 
     * @return the most nearest to origin point
     */
    public abstract Point getFirstPoint();
    
    /**
     * Returns the most remote from origin point
     *  
     * @return the most remote from origin point
     */
    public abstract Point getSecondPoint();
    
    @Override
    public abstract Line clone();
    
    @Override
    public abstract void rotateByOrigin(double angle);
    
    @Override
    public abstract void translate(double x, double y);
}
