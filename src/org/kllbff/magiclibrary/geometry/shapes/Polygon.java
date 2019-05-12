package org.kllbff.magiclibrary.geometry.shapes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.kllbff.magiclibrary.geometry.LineSegment;
import org.kllbff.magiclibrary.geometry.Point;
import org.kllbff.magiclibrary.geometry.PointPosition;
import org.kllbff.magiclibrary.geometry.StraightLine;

public class Polygon extends Shape {
    private Comparator<Point> verticesComparator;
    private List<Point> vertices;
    private Point[] cache;
    private Point origin;
    
    /**
     * Initializes an empty polygon without vertices
     */
    public Polygon() {
        this((Point[])null);
    }
    
    /**
     * Initializes polygon by given vertices
     * 
     * @param vertices array or sequence of {@link Point points}
     * @throws RuntimeException if given less than 3 vertices
     * @throws NullPointerException if at least one of given points is null
     * @throws RuntimeExcepiton if at least one of given points already added to polygon
     */
    public Polygon(Point... vertices) {
        if(vertices != null && vertices.length < 3) {
            throw new RuntimeException("Polygon can has 3 or more vertices, given " + vertices.length);
        }
        
        this.vertices = new ArrayList<>();
        origin = new Point();
        verticesComparator = new VerticesComparator(origin);
        
        if(vertices != null) {
            addVertices(vertices);
        }
    }
    
    /**
     * Adds vertex to polygon
     * 
     * @param point new vertex
     * @throws NullPointerException if given point is null
     * @throws RuntimeException if given point already added to polygon
     */
    public void addVertex(Point point) {
        if(point == null) {
            throw new NullPointerException("Vertex is null");
        }
        
        if(vertices.contains(point)) {
            throwVertexExists(point);
        }
        vertices.add(point);
        update();
    }
   
    /**
     * Adds vertex, located at given coordinates to polygon
     * @see {@link #addVertex(Point)}
     * 
     * @param x x-axis coordinate of vertex
     * @param y y-axis coordinate of vertex
     * @throws NullPointerException if given point is null
     * @throws RuntimeException if given point already added to polygon
     */
    public void addVertex(double x, double y) {
        addVertex(new Point(x, y));
    }
    
    /**
     * Adds some vertices to polygon
     * 
     * @param vertices an array or sequence of vertices
     * @throws NullPointerException if at least one of given points is null or sequence is null
     * @throws RuntimeException if at least one of given points already added to polygon
     */
    public void addVertices(Point... vertices) {
        if(vertices == null) {
            throw new NullPointerException("Vertices is null");
        }
        
        Point error = null;
        for(Point point : vertices) {
            if(point == null) {
                throw new NullPointerException("Vertex is null");
            }
            
            if(this.vertices.contains(point)) {
                error = point;
                break;
            }
            this.vertices.add(point);
        }
        update();
        
        if(error != null) {
            throwVertexExists(error);
        }
    }

    /**
     * Removes vertex from polygon
     * 
     * @param point specified vertex
     * @throws NullPointerException if given point is null
     * @throws RuntimeException if given point is not polygon's vertex
     */
    public void removeVertex(Point point) {
        if(point == null) {
            throw new NullPointerException("Vertex is null");
        }

        if(vertices.contains(point)) {
            vertices.remove(point);
            update();
        } else {
            throwVertexNotFound(point);
        }
    }
    
    /**
     * Removes vertex, located at given coordinates from polygon
     * @see {@link #removeVertex(Point)}
     * 
     * @param x x-axis coordinate of vertex
     * @param y y-axis coordinate of vertex
     * @throws NullPointerException if given point is null
     * @throws RuntimeException if given point is not polygon's vertex
     */
    public void removeVertex(double x, double y) {
        removeVertex(new Point(x, y));
    }
    
    /**
     * Removes some vertices from polygon
     * 
     * @param vertices an array or sequence of vertices
     * @throws NullPointerExceptionif at least one of given points is null or sequence is null
     * @throws RuntimeException if given point is not polygon's vertex
     */
    public void removeVertices(Point... vertices) {
        Point error = null;
        for(Point point : vertices) {
            if(point == null) {
                throw new NullPointerException("Vertex is null");
            }
            
            if(this.vertices.contains(point)) {
                this.vertices.remove(point);
            } else {
                error = point;
                break;
            }
        }
        update();
        
        if(error != null) {
            throwVertexExists(error);
        }
    }
    
    @Override
    public Point[] getVertices() {
        return cache;
    }

    @Override
    public PointPosition getPointPosition(Point point) {
        for(Point vx : vertices) {
            if(vx.equals(point)) {
                return PointPosition.IN_VERTEX;
            }
        }
        
        LineSegment[] edges = getEdges();
        for(LineSegment edge : edges) {
            if(edge.contains(point)) {
                return PointPosition.ON_SHAPE_EDGE;
            }
        }
        
        double nearestDist = Double.MAX_VALUE, dist;
        Point nearest = null;
        
        for(LineSegment edge  : edges) {
            StraightLine line = edge.getPerpendicular(point);
            if(line != null) {
                Point pt = edge.getIntersection(line);
                dist = pt.distanceTo(point);
                if(dist < nearestDist) {
                    nearest = pt;
                    nearestDist = dist;
                }
            }
        }
        
        for(Point vx : vertices) {
            dist = vx.distanceTo(point);
            if(dist < nearestDist) {
                nearest = vx;
                nearestDist = dist;
            }
        }
        
        double distanceToNearest = origin.distanceTo(nearest);
        double distanceToPoint = origin.distanceTo(point);
        
        if(distanceToNearest < distanceToPoint) {
            return PointPosition.OUTSIDE;
        }
        return PointPosition.INSIDE;
    }

    @Override
    public List<Point> getIntersections(StraightLine line) {
        if(line == null) {
            throw new NullPointerException("Cannot get intersections with " + this + ": line is null");
        }
        
        ArrayList<Point> intersections = new ArrayList<>();
        
        Point intersection;
        for(LineSegment edge : getEdges()) {
            intersection = edge.getIntersection(line);
            
            if(intersection != null && !intersections.contains(intersection)) {
                intersections.add(intersection);
            }
        }
        return intersections;
    }

    @Override
    public List<Shape> getIntersectionAreas(Shape other) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    protected int indexOfVertex(Point point) {
        return vertices.indexOf(point);
    }
    
    /**
     * Returns string representation of the object
     * <pre><ode>
     *     "Polygon with " + getVerticesCount() + " vertexes and center at (" + origin.getX() + ", " + origin.getY() + ")"
     * </code></pre>
     */
    @Override
    public String toString() {
        return "Polygon with " + vertices.size() + " vertexes and center at (" + origin.getX() + ", " + origin.getY() + ")";
    }

    /**
     * Updates bounding rectangle and calculates it's center point
     * <p>This method <b>must be called</b> manually after every changing of added vertices coordinates. <br>
     *    Example:
     *    <pre><code>
     *        Polygon polygon = new Polygon();
     *      
     *        Point a = new Point(0, 0);
     *        Point b = new Point(1, 1);
     *        Point c = new Point(-1, 1);
     *      
     *        polygon.addVertices(a, b, c);
     *      
     *        a.setY(-10);
     *        polygon.update();
     *   </code></pre>
     *   {@link #addVertex(Point)}, {@link #removeVertex(Point)} and other methods calls update method automatically. But if you change
     *   added point manually, you must call update method after changing.<br>
     *  </p>
     */
    public void update() {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        
        for(Point vertex : vertices) {
            if(vertex.getX() < minX) {
                minX = vertex.getX();
            }
            if(vertex.getY() < minY) {
                minY = vertex.getY();
            }
            
            if(vertex.getX() > maxX) {
                maxX = vertex.getX();
            }
            
            if(vertex.getY() > maxY) {
                maxY = vertex.getY();
            }
        }
        
        double width = maxX - minX;
        double height = maxY - minY;
        
        this.origin.setX(minX + width / 2);
        this.origin.setY(minY + height / 2);
        
        this.vertices.sort(verticesComparator);
        cache = vertices.toArray(new Point[0]);
    }
    
    private void throwVertexExists(Point vertex) {
        throw new RuntimeException("Vertex at (" + vertex.getX() + ", " + vertex.getY() + ") already added to " + this);
    }
    
    private void throwVertexNotFound(Point vertex) {
        throw new RuntimeException("Vertex at (" + vertex.getX() + ", " + vertex.getY() + ") does not found in " + this);
    }
}
