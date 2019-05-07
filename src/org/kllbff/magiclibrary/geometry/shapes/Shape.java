package org.kllbff.magiclibrary.geometry.shapes;

import java.util.List;

import org.kllbff.magiclibrary.geometry.Point;
import org.kllbff.magiclibrary.geometry.PointPosition;
import org.kllbff.magiclibrary.geometry.StraightLine;

/**
 * <h3>Represents abstract geometric shape</h3>
 * <p>Any shape can be described by a sequence of vertices.
 *  But sometimes to work with shapes requires additional methods involving complex mathematical calculations</p>
 * <p>Implementor must provides some methods for fast work with shapes intersections:
 *  <ul>
 *   <li>method for check if this shape have an intersection with other shape,</li>
 *   <li>method for getting an intersectons of this shape and other shape.</li>
 *  </ul>
 *  Also implementor should has method to check point entry into the shape.
 *    
 * @author Kirill Bogatikov
 * @version 1.0
 * @since 1.0
 */
public interface Shape {
    /**
     * Must returns array of shape's vertices
     * 
     * @return array of shape's vertices
     */
    public Point[] getVertices();
    
    /**
     * Implementor must returns one item from {@link #PointPosition}, representing position of specified point on this shape
     * 
     * @param point specified {@link Point} for checking
     * @return one of enum {@link #PointPosition}
     */
    public PointPosition getPointPosition(Point point);
    
    /**
     * Checks if this shape contains specified point
     * 
     * @param point specified Point for checking
     * @return true if point inside or on the border of shape; false otherwise
     */
    public boolean contains(Point point);
    
    /**
     * Checks if this shape contains specified shape
     * It is believed that the shape A contains a shape B if all the vertices of the figure B lie inside the shape or on its borders
     * 
     * @param other specified Shape for checking
     * @return true if point inside or on the border of shape; false otherwise
     */
    public boolean contains(Shape other);
    
    /**
     * Implementor must returns a {@link java.util.List List} of {@link Point} objects, represents the points of intersection
     * 
     * @param line specified line
     * @return intersection points list
     */
    public List<Point> getIntersections(StraightLine line);
    
    /**
     * Implementor must returns a {@link java.util.List List} of {@link Shape} objects, represents an intersection areas
     * 
     * @param other specified shape
     * @return list of {@link Shape} objects, represents an intersection areas
     */
    public List<Shape> getIntersectionAreas(Shape other);
    
    /**
     * Checks if specified line and this shape have an intersection area
     * 
     * @param line specified line
     * @return true if shape and line have an intersection area, false otherwise
     */
    public default boolean hasIntersection(StraightLine line) {
        return getIntersections(line).size() > 0;
    }
    
    /**
     * Checks if two shapes (this and specified) have an intersection area
     * 
     * @param other specified shape
     * @return true if shapes have an intersection area, false otherwise
     */
    public default boolean hasIntersectionArea(Shape other) {
        return getIntersectionAreas(other).size() > 0;
    }
}
