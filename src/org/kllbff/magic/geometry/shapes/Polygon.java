package org.kllbff.magic.geometry.shapes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kllbff.magic.geometry.Point;
import org.kllbff.magic.geometry.PointPosition;
import org.kllbff.magic.geometry.lines.Line;
import org.kllbff.magic.geometry.lines.LineSegment;
import org.kllbff.magic.geometry.lines.StraightLine;

public class Polygon extends Shape {
    private Point centerPoint = new Point();
    private VerticesComparator verticesComparator = new VerticesComparator(centerPoint);
    private List<Point> verticesList = new ArrayList<>();
    private Point[] verticesArray = new Point[0];
    
    public Polygon() {
        verticesComparator = null;
    }
    
    public Polygon(Point... vertices) {
        if(vertices == null) {
            throw new NullPointerException("Vertices array is null");
        }
        if(vertices.length < 3) {
            throw new RuntimeException("Cannot create polygon with " + vertices.length + " vertices. Need at least three points");
        }
        
        for(Point vertex : vertices) {
            if(verticesList.contains(vertex)) {
                throw new RuntimeException("Cannot add vertex " + vertex + ": already contained in polygon");
            }
            verticesList.add(vertex);
        }
        updateVertices();
    }
    
    public Polygon(Collection<Point> collection) {
        this(collection.toArray(new Point[0]));
    }
    
    public Polygon(double... coordinates) {
        if(coordinates == null) {
            throw new NullPointerException("Coordinates array is null");
        }
        if(coordinates.length % 2 == 1) {
            throw new RuntimeException("Coordinates array is incorrect: it must have event number of items");
        }
        if(coordinates.length < 6) {
            throw new RuntimeException("Cannot create polygon with " + (coordinates.length / 2)+ " vertices. Need at least three points");
        }
        
        Point vertex;
        for(int i = 0; i < coordinates.length; i += 2) {
            vertex = new Point(coordinates[i], coordinates[i + 1]);
            if(verticesList.contains(vertex)) {
                throw new RuntimeException("Cannot add vertex " + vertex + ": already contained in polygon");
            }
            verticesList.add(vertex);
        }
        updateVertices();
    }
    
    public void addVertices(Point... vertices) {
        for(Point vertex : vertices) {
            this.addVertex(vertex);
        }
    }
    
    public void addVertex(Point vertex) {
        if(vertex == null) {
            throw new NullPointerException("Vertex is null");
        }
        if(verticesList.contains(vertex)) {
            throw new RuntimeException("Cannot add vertex " + vertex + ": already contained in polygon");
        }
        verticesList.add(vertex);
        updateVertices();
    }
    
    public void addVertex(double x, double y) {
        this.addVertex(new Point(x, y));
    }
    
    public void removeVertices(Point... vertices) {
        for(Point vertex : vertices) {
            this.removeVertex(vertex);
        }
    }
    
    public void removeVertex(Point vertex) {
        if(vertex == null) {
            throw new NullPointerException("Vertex is null");
        }
        if(!verticesList.contains(vertex)) {
            throw new RuntimeException("Cannot remove vertex " + vertex + ": does not contained in polygon");
        }
        verticesList.remove(vertex);
        updateVertices();
    }
    
    public void removeVertex(double x, double y) {
        this.removeVertex(new Point(x, y));
    }
    
    public void updateVertices() {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        
        for(Point vertex : verticesList) {
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
        
        centerPoint.setX(minX + width / 2);
        centerPoint.setY(minY + height / 2);
        
        if(verticesComparator != null) verticesList.sort(verticesComparator);
        verticesArray = verticesList.toArray(new Point[0]);
    }
    
    @Override
    public Point[] getVertices() {
        return verticesArray;
    }

    @Override
    public LineSegment[] getEdges() {
        LineSegment[] edges = new LineSegment[verticesList.size()];
        for(int i = 0; i < edges.length; i++) {
            edges[i] = new LineSegment(verticesArray[i], verticesList.get(i == edges.length - 1 ? 0 : i + 1));
        }
        return edges;
    }

    @Override
    public Line[] getEdges(Point vertex) {
        int index = verticesList.indexOf(vertex);
        
        if(index == -1) {
            throw new RuntimeException("Cannot get edges fot point " + vertex + ". Point does not Polygon vertex");
        }
    
        Point last = verticesArray[(index == 0 ? verticesArray.length : index) - 1]; 
        Point next = verticesArray[index == verticesArray.length - 1 ? 0 : index + 1]; 
        
        return new LineSegment[]{ new LineSegment(last, vertex), new LineSegment(next, vertex) };
    }

    @Override
    public List<Point> getIntersectionPoints(Line line) {
        List<Point> result = new ArrayList<>();
        Point intersection;
        for(LineSegment edge : getEdges()) {
            intersection = line.getIntersection(edge);
            if(intersection != null && !result.contains(intersection)) {
                result.add(intersection);
            }
        }
        return result;
    }

    @Override
    public List<Polygon> getIntersectionAreas(Shape shape) {
        ArrayList<Point> vertices = new ArrayList<>();
        for(Point vertex : verticesList) {
            if(shape.contains(vertex) && !vertices.contains(vertex)) {
                vertices.add(vertex);
            }
        }
        for(Point vertex : shape.getVertices()) {
            if(contains(vertex) && !vertices.contains(vertex)) {
                vertices.add(vertex);
            }
        }
        
        LineSegment[] myEdges = getEdges();
        Line[] itEdges = shape.getEdges();
        for(LineSegment myEdge : myEdges) {
            for(Line itEdge : itEdges) {
                Point i = itEdge.getIntersection(myEdge);
                if(i != null && !vertices.contains(i)) {
                    vertices.add(i);
                }
            }
        }
                
        ArrayList<Polygon> result = new ArrayList<>();
        result.add(new Polygon(vertices));
        
        return result;
    }
    
    public boolean isValid() {
        LineSegment[] edges = getEdges();
        LineSegment edge = edges[0];
        
        for(int i = 1; i < edges.length; i++) {
            if(edges[9].getAngle() == edge.getAngle()) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public PointPosition getPointPosition(Point point) {
        for(Point vx : verticesList) {
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
        
        for(Point vx : verticesList) {
            dist = vx.distanceTo(point);
            if(dist < nearestDist) {
                nearest = vx;
                nearestDist = dist;
            }
        }
        
        double distanceToNearest = centerPoint.distanceTo(nearest);
        double distanceToPoint = centerPoint.distanceTo(point);
        
        if(distanceToNearest < distanceToPoint) {
            return PointPosition.OUTSIDE;
        }
        return PointPosition.INSIDE;
    }

    @Override
    public void translate(double x, double y) {
        for(Point vertex : verticesList) {
            vertex.translate(x, y);
        }
    }

    @Override
    public void rotateByOrigin(double angle) {
        for(Point vertex : verticesList) {
            vertex.rotateByOrigin(angle);
        }
    }
    
    public void rotate(double angle) {
        double x0 = centerPoint.getX();
        double y0 = centerPoint.getY();
        
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        
        double x, y;
        for(Point v : verticesList) {
            x = v.getX();
            y = v.getY();
            
            v.setX(x0 + (x - x0) * cos - (y - y0) * sin);
            v.setY(y0 + (y - y0) * cos + (x - x0) * sin);   
        }
    }

    @Override
    public Polygon clone() {
        return new Polygon(verticesArray);
    }

}
