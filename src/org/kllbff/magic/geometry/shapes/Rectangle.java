package org.kllbff.magic.geometry.shapes;

import org.kllbff.magic.geometry.Point;
import org.kllbff.magic.geometry.PointPosition;
import org.kllbff.magic.geometry.lines.LineSegment;

public class Rectangle extends Shape {
    private Point a;
    private double originAngle, ownAngle;
    private double width, height;
    
    public Rectangle(double x, double y, double width, double height) {
        this.a = new Point(x, y);
        this.width = width;
        this.height = height;
    }
    
    public double getX() {
        return a.getX();
    }
    
    public double getY() {
        return a.getY();
    }
    
    public void setX(double x) {
        this.a.setX(x);
    }
    
    public void setY(double y) {
        this.a.setY(y);
    }
    
    public double getWidth() {
        return width;
    }
    
    public double getHeight() {
        return height;
    }
    
    public void setWidth(double width) {
        this.width = width;
    }
    
    public void setHeight(double height) {
        this.height = height;
    }
    
    @Override
    public Point getCenterPoint() {
        return new Point(a.getX() + width / 2, a.getY() + height / 2);
    }
    
    public Rectangle(Point leftBottom, Point rightTop) {
        this(leftBottom.getX(), leftBottom.getY(), rightTop.getX() - leftBottom.getX(), rightTop.getY() - leftBottom.getY());
    }

    @Override
    public Point[] getVertices() {
         Point center = getCenterPoint();
         
         Point a1 = new Point(a.getX(), a.getY() + height);
         a1.rotateByOrigin(originAngle);
         a1.rotateBy(ownAngle, center);
         Point a2 = new Point(a.getX() + width, a.getY());
         a2.rotateByOrigin(originAngle);
         a2.rotateBy(ownAngle, center);
         Point a3 = new Point(a.getX() + width, a.getY() + height);
         a3.rotateByOrigin(originAngle);
         a3.rotateBy(ownAngle, center);
         
         return new Point[] { 
             a.clone(),
             a1,
             a3, 
             a2     
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
        
        LineSegment line = new LineSegment(point, getCenterPoint());
        for(LineSegment edge : edges) {
            if(edge.hasIntersection(line)) {
                return PointPosition.INSIDE;
            }
        }
        
        return PointPosition.OUTSIDE;
    }

    @Override
    public void translate(double x, double y) {
        a.translate(x, y);
    }
    
    @Override
    public void rotate(double angle) {
        a.rotateBy(angle, getCenterPoint());
    }

    @Override
    public void rotateByOrigin(double angle) {
        a.rotateByOrigin(angle);
    }

    @Override
    public Rectangle clone() {
        Rectangle copy = new Rectangle(a.getX(), a.getY(), width, height);
        return copy;
    }

    @Override
    public String toString() {
        return "Rectangle at (" + a.getX() + ", " + a.getY() + "), width " + width + ", height " + height;
    }
}
