  package org.kllbff.magic.geometry.lines;

import java.util.HashMap;

import org.kllbff.magic.geometry.Point;
import org.kllbff.magic.geometry.PointPosition;

public class BezierCurve extends Line {
    private HashMap<Line, Point> secondIntersections;
    private Point a, b, c;
    
    public BezierCurve(Point a, Point b, Point c) {
        this.a = a;
        this.b = b;
        this.c = c;
        secondIntersections = new HashMap<>();
    }
    
    public BezierCurve(double ax, double ay, double bx, double by, double cx, double cy) {
        this(new Point(ax, ay), new Point(bx, by), new Point(cx, cy));
    }
    
    public Point calc(double t) {
        return new Point(Math.pow(1 - t, 2) * this.a.getX() + 2 * (t - t * t) * this.b.getX() + t * t * this.c.getX(),
                         Math.pow(1 - t, 2) * this.a.getY() + 2 * (t - t * t) * this.b.getY() + t * t * this.c.getY());
    }
    
    @Override
    public Point getIntersection(Line otherLine) {
        if(otherLine instanceof StraightLine || otherLine instanceof LineSegment) {
            double angle = -((StraightLine)otherLine).getAngle();
            
            Point s = otherLine.getFirstPoint().clone();
            s.rotateByOrigin(angle);
            
            Point a = this.a.clone();
            a.rotateByOrigin(angle);
            Point b = this.b.clone();
            b.rotateByOrigin(angle);
            Point c = this.c.clone();
            c.rotateByOrigin(angle);
            
            double ak = a.getY() - 2 * b.getY() + c.getY();
            double bk = 2 * (b.getY() - a.getY());
            double ck = a.getY() - s.getY();
            
            double dk = bk * bk - 4 * ak * ck;
            double t1 = (-bk + Math.sqrt(dk)) / (2 * ak);
            double t2 = (-bk - Math.sqrt(dk)) / (2 * ak);
            
            Point result1 = calc(t1);
            Point result2 = calc(t2);
            
            if(otherLine.contains(result1)) {
                if(otherLine.contains(result2)) {
                    secondIntersections.put(otherLine, result2);
                }
                return result1;
            }
        }
        return null;
    }
    
    public Point getSecondIntersection(Line otherLine) {
        return secondIntersections.get(otherLine);
    }

    @Override
    public PointPosition getPointPosition(Point point) {
        return null;
    }

    @Override
    public Point getFirstPoint() {
        return a;
    }

    @Override
    public Point getSecondPoint() {
        return c;
    }
    
    public Point getSupportPoint() {
        return b;
    }

    @Override
    public BezierCurve clone() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void rotateByOrigin(double angle) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void translate(double x, double y) {
        // TODO Auto-generated method stub
        
    }
}
