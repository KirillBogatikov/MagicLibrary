package org.kllbff.magic.geometry.shapes;

import java.util.ArrayList;
import java.util.List;

import org.kllbff.magic.geometry.Point;
import org.kllbff.magic.geometry.PointPosition;
import org.kllbff.magic.geometry.Primitive;
import org.kllbff.magic.geometry.lines.BezierCurve;
import org.kllbff.magic.geometry.lines.Line;

/**
 * <h3>Represents abstract geometric shape</h3>
 * <p>Any shape can be described by a sequence of vertices.
 *     But sometimes to work with shapes requires additional methods involving complex mathematical calculations</p>
 *     <p>Implementor must provides some methods:
 *     <ul>
 *         <li>getter for all vertices;</li>
 *         <li>getter for all edges and edges connected with specified vertex;</li>
 *         <li>method for getting an intersection points of this shape and specified line;</li>
 *         <li>method for getting an intersection areas of this shape and specified other shape;</li>
 *         <li>method for getting position of specified point relative this shape;</li>
 *     </ul>
 *     <p>Also child class must implement all {@link Primitive} interface methods.</p>  
 *    
 * @author Kirill Bogatikov
 * @version 1.0
 * @since 1.0
 */
public abstract class Shape implements Primitive {
    /**
     * Returns an array of shape's vertices
     *  
     * @return array of shape's vertices
     */
    public abstract Point[] getVertices();
    
    /**
     * Returns an array of all shape's edges
     * 
     * @return array of shape's edges
     */
    public abstract Line[] getEdges();
    
    /**
     * Returns an array of two edges, connected to specified vertex
     * <p>This method can throw {@link RuntimeException} if specified point does not shape's vertex, but can not returns null such as result</p>
     * 
     * @param vertex specified vertex
     * @return an array of two edges, connected to specified vertex
     */
    public abstract Line[] getEdges(Point vertex);
    
    /**
     * Returns list of intersection points with specified line
     * <p>If shape has not intersections, this method returns an empty list, not null</p>
     * 
     * @param line specified line
     * @return list of intersection points with specified line, always not null
     */
    public List<Point> getIntersectionPoints(Line line) {
        List<Point> result = new ArrayList<>();
        Point intersection;
        for(Line edge : getEdges()) {
            intersection = line.getIntersection(edge);
            if(intersection != null && !result.contains(intersection)) {
                result.add(intersection);
                if(line instanceof BezierCurve) {
                    intersection = ((BezierCurve)line).getIntersection(edge, 1);
                    if(intersection != null && !result.contains(intersection)) {
                        result.add(intersection);
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * Checks if this shape has at least one intersection point with specified line
     * <p>As default, implemented as checking of {@link #getIntersectionPoints(Line)} method's result list size.</p>
     * 
     * @param line specified line
     * @return true, if this shape has at least one intersection point with specified line
     */
    public boolean hasIntersection(Line line) {
        return getIntersectionPoints(line).size() > 0;
    }
    
    /**
     * Returns position of specified point relatively to this shape as some item of {@link PointPosition} enumeration
     * 
     * @param point specified point 
     * @return position of specified point represented by {@link PointPosition} item  
     */
    public abstract PointPosition getPointPosition(Point point);
    
    /**
     * Checks if specified point locates inside this shape, or at it's edge
     * <p>As default, implemented as checking result of {@link #getPointPosition(Point)}</p>
     * 
     * @param point specified point
     * @return true if specified point locates inside this shape or it's edge
     */
    public boolean contains(Point point) {
        return getPointPosition(point) != PointPosition.OUTSIDE;
    }
    
    /**
     * Returns an point which defines center of current shape
     * <p>Returned point has only info value, changing of coordinates
     *    of this point has not effect</p>
     * 
     * @return center of this shape
     */
    public abstract Point getCenterPoint();

    @Override
    public abstract void translate(double x, double y);

    public abstract void rotate(double angle);
    
    @Override
    public abstract void rotateByOrigin(double angle);
    
    @Override
    public abstract Shape clone();
}
