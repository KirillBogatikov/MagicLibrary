package org.kllbff.magic.geometry.shapes;

import org.kllbff.magic.geometry.Point;
import org.kllbff.magic.geometry.PointPosition;
import org.kllbff.magic.geometry.lines.Line;
import org.kllbff.magic.math.DigitsRounder;

public class Ellipse extends Shape {
    private Point center;
    private double smallAxis, bigAxis;
    private double angle = 0;
    
    public Ellipse(Point center, double radius) {
        this.center = center;
        this.smallAxis = radius / 2;
        this.bigAxis = radius / 2;
    }
    
    public Ellipse(double x, double y, double radius) {
        this(new Point(x, y), radius);
    }
    
    public Ellipse(Point center, double smallAxis, double bigAxis) {
        this.center = center;
        this.smallAxis = smallAxis;
        this.bigAxis = bigAxis;
    }

    @Override
    public Point[] getVertices() {
        return null;
    }

    @Override
    public Line[] getEdges() {
        return null;
    }

    @Override
    public Line[] getEdges(Point vertex) {
        return null;
    }

    @Override
    public PointPosition getPointPosition(Point point) {
        if(point.equals(center)) {
            return PointPosition.IN_VERTEX;
        }
        
        point = point.clone();
        point.rotateBy(angle, center);
        
        double x = point.getX(), y = point.getY();
        double v = Math.pow(x - center.getX(), 2) / (bigAxis * bigAxis) + Math.pow(y - center.getY(),  2) / (smallAxis * smallAxis); 
        v = DigitsRounder.round(v, 5);
        
        if(v == 4.0) {
            return PointPosition.ON_SHAPE_EDGE;
        } else if(v < 4.0) {
            return PointPosition.INSIDE;
        }
        
        return PointPosition.OUTSIDE;
    }

    @Override
    public Point getCenterPoint() {
        return center;
    }

    @Override
    public void translate(double x, double y) {
        center.translate(x, y);
    }

    @Override
    public void rotate(double angle) {
        this.angle += angle;
    }

    @Override
    public void rotateByOrigin(double angle) {
        center.rotateByOrigin(angle);
    }

    @Override
    public Shape clone() {
        return null;
    }

}
