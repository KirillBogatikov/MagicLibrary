package org.kllbff.magic.geometry.shapes;

import java.util.List;

import org.kllbff.magic.geometry.LineSegment;
import org.kllbff.magic.geometry.Point;
import org.kllbff.magic.geometry.PointPosition;
import org.kllbff.magic.geometry.StraightLine;

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
public abstract class Shape {
    /**
     * Must returns array of shape's vertices
     * <p>Array must be sorted in ascending angles between the line connecting
     *    the top and the center of the shape starting from the leftmost vertex</p>
     * 
     * @return array of shape's vertices
     */
    public abstract Point[] getVertices();
    
    /**
     * Must returns one item from {@link PointPosition}, representing position of specified point on this shape
     * 
     * @param point specified {@link Point} for checking
     * @return one of enum {@link PointPosition}
     */
    public abstract PointPosition getPointPosition(Point point);
    
    /**
     * Returns index of specified vertex or -1 if point does not found
     * 
     * @param point specified vertex
     * @return index of specified vertex or -1 if point does not found
     */
    protected int indexOfVertex(Point point) {
        Point[] vertices = getVertices();
        for(int i = 0; i < vertices.length; i++) {
            if(vertices[i].equals(point)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Returns true if point located inside the polygon, or at vertex or on edge
     * 
     * <p>This method uses {@link #getPointPosition(Point)}</p>
     * 
     * @return true if point inside or on the border of polygon; false otherwise
     */
    public boolean contains(Point point) {
        return getPointPosition(point) != PointPosition.OUTSIDE;
    }
    
    /**
     * Checks whether a specified shape lies within a polygon
     * 
     * <p>Checking divided into two stages:
     * <dl>
     *      <dt>Checking shape's vertices</dt>
     *      <dd>Checks if this shape contains each vertex. If all vertices inside the shape, ran second stage</dd>
     *      <dt>Checking edges intersections</dt>
     *      <dd>Checks if this shape edges has intersections with edges of specified shape. Intersections at shape's vertices ignored. If intersections not found, it is believed that the specified shape is inside</dd>
     * </dl>
     * 
     * @param other specified shape
     */
    public boolean contains(Shape other) {
        Point[] vertices = other.getVertices();
        
        for(int i = 1; i < vertices.length; i++) {
            if(!contains(vertices[i])) {
                return false;
            }
        }
        
        LineSegment[] myEdges = getEdges();
        LineSegment[] otherEdges = other.getEdges();
        
        for(LineSegment otherEdge : otherEdges) {
            for(LineSegment myEdge : myEdges) {
                Point point = otherEdge.getIntersection(myEdge);
                if(point != null && indexOfVertex(point) == -1) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Implementor must returns a {@link java.util.List List} of {@link Point} objects, represents the points of intersection
     * 
     * @param line specified line
     * @return intersection points list
     */
    public abstract List<Point> getIntersections(StraightLine line);
    
    /**
     * Implementor must returns a {@link java.util.List List} of {@link Shape} objects, represents an intersection areas
     * 
     * @param other specified shape
     * @return list of {@link Shape} objects, represents an intersection areas
     */
    public abstract List<Shape> getIntersectionAreas(Shape other);
    
    /**
     * Checks if specified line and this shape have an intersection area
     * 
     * @param line specified line
     * @return true if shape and line have an intersection area, false otherwise
     */
    public boolean hasIntersection(StraightLine line) {
        return getIntersections(line).size() > 0;
    }
    
    /**
     * Checks if two shapes (this and specified) have an intersection area
     * 
     * @param other specified shape
     * @return true if shapes have an intersection area, false otherwise
     */
    public boolean hasIntersectionArea(Shape other) {
        return getIntersectionAreas(other).size() > 0;
    }
    
    /**
     * Returns an array of shape's edges, represented by LineSegment
     * 
     * @return an array of shape's edges
     */
    public LineSegment[] getEdges() {
        Point[] vertices = getVertices();
        LineSegment[] edges = new LineSegment[vertices.length];
        for(int i = 1; i < vertices.length; i++) {
            edges[i - 1] = new LineSegment(vertices[i - 1], vertices[i]);
        }
        edges[vertices.length - 1] = new LineSegment(vertices[vertices.length - 1], vertices[0]);
        return edges;
    }

    /**
     * Returns two edges that share a vertex in the shape <i>vertex</i> or null if specified point does not shape's vertex
     * 
     * @param vertex specified vertex contained in this shape
     * @return array of two edges that share a vertex in the shape <i>vertex</i> or null
     */
    public LineSegment[] getVertexEdges(Point vertex) {
        int i = indexOfVertex(vertex);
        if(i == -1) {
            return null;
        }
        
        Point[] vertices = getVertices();
        LineSegment[] edges = new LineSegment[2];
        
        int j = i == 0 ? vertices.length - 1 : i - 1;
        int k = i == vertices.length - 1 ? 0 : i + 1; 
        edges[0] = new LineSegment(vertices[i], vertices[j]);
        edges[1] = new LineSegment(vertices[i], vertices[k]);
        return edges;
    }
}
