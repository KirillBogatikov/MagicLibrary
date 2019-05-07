package org.kllbff.magiclibrary.geometry.shapes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.kllbff.magiclibrary.geometry.LineSegment;
import org.kllbff.magiclibrary.geometry.Point;
import org.kllbff.magiclibrary.geometry.PointPosition;
import org.kllbff.magiclibrary.geometry.StraightLine;

public class Polygon implements Shape {
    private Comparator<Point> verticesComparator;
    private List<Point> vertices;
    private Point origin;
    
    public Polygon() {
        vertices = new ArrayList<>();
        origin = new Point();
        verticesComparator = new VerticesComparator(origin);
    }
    
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
    
    public void addVertex(double x, double y) {
        addVertex(new Point(x, y));
    }
    
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
    
    public void removeVertex(double x, double y) {
        removeVertex(new Point(x, y));
    }
    
    public int getVerticesCount() {
        return vertices.size();
    }
    
    public void addVertices(Point... vertices) {
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
        return vertices.toArray(new Point[0]);
    }

    @Override
    public PointPosition getPointPosition(Point point) {
        return null;
    }

    @Override
    public boolean contains(Point point) {
        LineSegment line;
        List<Point> intersections;
        for(Point v : vertices) {
            if(point.equals(v)) {
                return true;
            }
            
            line = new LineSegment(point, v);
            intersections = getIntersections(line);
            
            if(intersections.size() % 2 == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean contains(Shape other) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<Point> getIntersections(StraightLine line) {
        ArrayList<Point> points = new ArrayList<>();
        
        LineSegment edge;
        Point point;
        for(int i = 1; i < vertices.size(); i++) {
            edge = new LineSegment(vertices.get(i), vertices.get(i - 1));
            point = edge.getIntersection(line);

            if(point != null && !points.contains(point)) {
                points.add(point);
            }
        }
        
        return points;
    }

    @Override
    public List<Shape> getIntersectionAreas(Shape other) {
        ArrayList<Shape> shapes = new ArrayList<>();
        
        return shapes;
    }
    
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
    public String toString() {
        return "Polygon with " + getVerticesCount() + " vertexes and center at (" + origin.getX() + ", " + origin.getY() + ")";
    }
}
