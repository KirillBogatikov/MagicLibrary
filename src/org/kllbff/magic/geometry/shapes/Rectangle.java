package org.kllbff.magic.geometry.shapes;

import java.util.List;

import org.kllbff.magic.geometry.Point;
import org.kllbff.magic.geometry.PointPosition;
import org.kllbff.magic.geometry.lines.Line;
import org.kllbff.magic.geometry.lines.LineSegment;

public class Rectangle extends Shape {
    private double x, y;
    private double width, height;
    
    public Rectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    @Override
    public Point getCenterPoint() {
        return new Point(x + width / 2, y + height / 2);
    }
    
    public Rectangle(Point leftBottom, Point rightTop) {
        this(leftBottom.getX(), leftBottom.getY(), rightTop.getX() - leftBottom.getX(), rightTop.getY() - leftBottom.getY());
    }
    
    private void upd() {
        
    }

    @Override
    public Point[] getVertices() {
        return new Point[]{ 
            new Point(x, y),
            new Point(x, y + height),
            new Point(x + width, y + height),
            new Point(x + width, y)
        };
    }

    @Override
    public LineSegment[] getEdges() {
        LineSegment[] edges = new LineSegment[4];
        Point[] vertices = getVertices();
        for(int i = 0; i < 4; i++) {
            edges[i] = new LineSegment(vertices[i], vertices[i == 3 ? 0 : i + 1]);
        }
        return edges;
    }

    @Override
    public LineSegment[] getEdges(Point vertex) {
        LineSegment a = null, b = null;
        
        Point[] vertices = getVertices();
        LineSegment[] edges = getEdges();
        for(int i = 0; i < vertices.length; i++) {
            if(vertices[i].equals(vertex)) {
                a = edges[i == 0 ? 3 : i - 1];
                b = edges[i];
            }
        }
        
        if(a == null || b == null) {
            throw new RuntimeException(vertex + " does not vertex of " + this);
        }
        
        return new LineSegment[]{ a, b };
    }

    @Override
    public List<Point> getIntersectionPoints(Line line) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Polygon> getIntersectionAreas(Shape shape) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PointPosition getPointPosition(Point point) {
        Point[] vertices = getVertices();
        for(Point vx : vertices) {
            if(point.equals(vx)) {
                return PointPosition.IN_VERTEX;
            }
        }
        
        LineSegment[] edges = getEdges();
        for(LineSegment edge : edges) {
            if(edge.contains(point)) {
                return PointPosition.ON_SHAPE_EDGE;
            }
        }
        
        double x = point.getX(), y = point.getY();
        if(x > this.x && x < this.x + this.width && y > this.y && y < this.y + this.height) {
            return PointPosition.INSIDE;
        }
        
        return PointPosition.OUTSIDE;
    }

    @Override
    public void translate(double x, double y) {
        this.x += x;
        this.y += y;
    }
    
    @Override
    public void rotate(double angle) {
        Point center = new Point(x + width /2, y + height / 2);
        
        double cos = Math.cos(angle), sin = Math.sin(angle);
        double rad = center.distanceTo(x, y);
        
        double nx = center.getX() * cos, ny = center.getY() * sin;
        this.x = nx;
        this.y = y;
    }

    @Override
    public void rotateByOrigin(double angle) {
        Point start = new Point(x, y);
        start.rotateByOrigin(angle);
        this.x = start.getX();
        this.y = start.getY();
    }

    @Override
    public Rectangle clone() {
        Rectangle copy = new Rectangle(x, y, width, height);
        return copy;
    }

    @Override
    public String toString() {
        return "Rectangle at (" + x + ", " + y + "), width " + width + ", height " + height;
    }
}
