package org.kllbff.magiclibrary.geometry.shapes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.kllbff.magiclibrary.geometry.LineSegment;
import org.kllbff.magiclibrary.geometry.Point;
import org.kllbff.magiclibrary.geometry.PointPosition;
import org.kllbff.magiclibrary.geometry.StraightLine;

/**
 * <h3>Represents Polygon shape</h3>
 * <p>Polygon is a shape, containing a tree or more vertices. This polygon is extensible: class has some methods for adding and removing vertices.</p>
 * <p>This class implements all methods from {@link Shape} interface, but has some own:
 *     <ul>
 *          <li>{@link #addVertex(Point)}, {@link #addVertex(double, double)} for adding one vertex to polygon;</li>
 *          <li>{@link #addVertices(Point...)} for adding sequence or array of vertices;</li>
 *          <li>{@link #removeVertex(Point)}, {@link #removeVertex(double, double)} for removing vertex from polygon;</li>
 *          <li>{@link #removeVertices(Point...)} for removing sequence or array of vertices;</li>
 *          <li>{@link #getVerticesCount()} for getting count of polygon's vertices</li>
 *          <li>{@link #update()} for updating polygons's bounding rectangle after manual changing of vertices coordinates</li>
 *     </ul></p>
 *    
 * @author Kirill Bogatikov
 * @version 1.0
 * @since 1.0
 */
public class Polygon extends Shape {
    private Comparator<Point> verticesComparator;
    private List<Point> vertices;
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
    
    /**
     * Returns number of current added to polygon vertices
     * 
     * @return vertices count
     */
    public int getVerticesCount() {
        return vertices.size();
    }
    
    /**
     * Returns a copy of origin point - current center of polygon's bounding rectangle
     * <p>This methods returns only copy - changes of returned point have not affect of origin bouding center point object</p> 
     *  
     * @return a copy of origin point - current center of polygon's bounding rectangle
     */
    public Point getBoundsCenter() {
        return new Point(origin.getX(), origin.getY());
    }
    
    /**
     * Returns array of polygon's vertices
     * 
     * @return array of polygon's vertices
     */
    @Override
    public Point[] getVertices() {
        return vertices.toArray(new Point[0]);
    }

    /**
     * Returns a List of Point objects, represents the points of intersection
     */
    @Override
    public List<Point> getIntersections(StraightLine line) {
        ArrayList<Point> points = new ArrayList<>();
        
        LineSegment edge;
        Point point;
        for(int i = 1; i <= vertices.size(); i++) {
            edge = new LineSegment(vertices.get(i == vertices.size() ? 0 : i), vertices.get(i - 1));
            point = edge.getIntersection(line);
            
            if(point != null && !points.contains(point)) {
                points.add(point);
            }
        }
        
        return points;
    }

    /**
     * Returns a List of Shape objects, represents intersection areas with specified shape
     */
    @Override
    public List<Shape> getIntersectionAreas(Shape other) {
        ArrayList<Shape> shapes = new ArrayList<>();
        
        Polygon area = new Polygon();
        Point[] vertices = other.getVertices();
        for(Point vertex : vertices) {
            if(getPointPosition(vertex) == PointPosition.INSIDE) {
                area.addVertex(vertex);
            } else {
                
            }
            //LineSegment[] edges = other.getEdges(vertex);
            //edges[0].hasIntersection(other)
        }
        
        return shapes;
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
    }
    
    private void throwVertexExists(Point vertex) {
        throw new RuntimeException("Vertex at (" + vertex.getX() + ", " + vertex.getY() + ") already added to " + this);
    }
    
    private void throwVertexNotFound(Point vertex) {
        throw new RuntimeException("Vertex at (" + vertex.getX() + ", " + vertex.getY() + ") does not found in " + this);
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
        return "Polygon with " + getVerticesCount() + " vertexes and center at (" + origin.getX() + ", " + origin.getY() + ")";
    }
}
