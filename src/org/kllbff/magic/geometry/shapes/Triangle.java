package org.kllbff.magic.geometry.shapes;

import org.kllbff.magic.geometry.Point;
import org.kllbff.magic.geometry.PointPosition;
import org.kllbff.magic.geometry.lines.LineSegment;

public class Triangle extends Shape {
    private Point a, b, c;
    
    public Triangle(Point a, Point b, Point c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    public Triangle(double ax, double ay, double bx, double by, double cx, double cy) {
        this(new Point(ax, ay), new Point(bx, by), new Point(cx, cy));
    }
    
    @Override
    public Point[] getVertices() {
        return new Point[]{ a, b, c };
    }

    @Override
    public LineSegment[] getEdges() {
        return new LineSegment[] {
            new LineSegment(a, b),
            new LineSegment(b, c),
            new LineSegment(c, a)
        };
    }

    @Override
    public LineSegment[] getEdges(Point vertex) {
        LineSegment f, s;
        if(vertex.equals(a)) {
            f = new LineSegment(a, c);
            s = new LineSegment(a, b);
        } else if(vertex.equals(b)) {
            f = new LineSegment(b, a);
            s = new LineSegment(b, c);
        } else if(vertex.equals(c)) {
            f = new LineSegment(c, a);
            s = new LineSegment(c, b);
        } else {
            throw new RuntimeException("Specified point " + vertex + " does not vertex of " + this);
        }
        
        return new LineSegment[] { f, s };
    }

    @Override
    public PointPosition getPointPosition(Point point) {
        if(point.equals(a) || point.equals(b) || point.equals(c)) {
            return PointPosition.IN_VERTEX;
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
    public Point getCenterPoint() {
        return new Point((a.getX() + b.getX() + c.getX()) / 3, (a.getY() + b.getY() + c.getY()) / 3);
    }

    @Override
    public void translate(double x, double y) {
        a.translate(x, y);
        b.translate(x, y);
        c.translate(x, y);
    }

    @Override
    public void rotate(double angle) {
        Point center = getCenterPoint();
        a.rotateBy(angle, center);
        b.rotateBy(angle, center);
        c.rotateBy(angle, center);
    }

    @Override
    public void rotateByOrigin(double angle) {
        a.rotateByOrigin(angle);
        b.rotateByOrigin(angle);
        c.rotateByOrigin(angle);
    }

    @Override
    public Triangle clone() {
        return new Triangle(a, b, c);
    }

}
