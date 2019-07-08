package org.kllbff.magic.geometry.lines;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.kllbff.magic.geometry.Point;
import org.kllbff.magic.geometry.PointPosition;

/**
 * <h3>Describes quadratic Bezier curved line in two dimensional plain</h3>
 * <p>Any quadratic Bezier curve can be created by three points:
 *     <ul>
 *         <li>A - start point,</li>
 *         <li>B - support point,</li>
 *         <li>C - end point.</li>
 *     </ul>
 *     You can <a href="https://en.wikipedia.org/wiki/B%C3%A9zier_curve">read more about Bezier curves</a>
 * </p>
 * <p>
 *     BezierCurve extends Line class, therefore it implements all Line's methods. But Bezier curve unlike Line
 *     can have up to four intersection points. Line class provides method {@link Line#getIntersection(Line)}, which can returns only one point.
 *     To solve this problem in this class has been implemented method {@link #getIntersection(Line, int)}, which return specified point of intersection using it's index (from 0 to 4)
 * </p>
 * 
 * @author Kirill Bogatikov
 * @version 1.0
 * @since 1.0
 */
public class QuadBezierCurve extends Line {
    private static final double DEFAULT_TIME_RATIO = 100;
    private HashMap<Line, Point[]> otherIntersections;
    private Point a, b, c;
    
    /**
     * Initializes curve by given points
     * 
     * @param a start point
     * @param b support point
     * @param c end point
     */
    public QuadBezierCurve(Point a, Point b, Point c) {
        this.a = a;
        this.b = b;
        this.c = c;
        otherIntersections = new HashMap<>();
    }
    
    /**
     * Initializes curve by points at given coordinates 
     * 
     * @param ax x-axis coordinate of start point 
     * @param ay y-axis coordinate of start point 
     * @param bx x-axis coordinate of support point 
     * @param by y-axis coordinate of support point 
     * @param cx x-axis coordinate of end point 
     * @param cy y-axis coordinate of end point 
     */
    public QuadBezierCurve(double ax, double ay, double bx, double by, double cx, double cy) {
        this(new Point(ax, ay), new Point(bx, by), new Point(cx, cy));
    }
    
    /**
     * Returns point, calculated by given time parameter
     * 
     * @param t time parameter
     * @return point, calculated by given time parameter
     */
    public Point getPointAt(double t) {
        return new Point(Math.pow(1 - t, 2) * this.a.getX() + 2 * (t - t * t) * this.b.getX() + t * t * this.c.getX(),
                          Math.pow(1 - t, 2) * this.a.getY() + 2 * (t - t * t) * this.b.getY() + t * t * this.c.getY());
    }
    
    /**
     * Returns point of intersection this line with specified line or null if intersection does not exist
     * <p>
     *     This method can work with StraightLine, LineSegment and BezierCurve objects
     * </p>
     * <p>
     *     If lines has two or more intersections, they will be saved in internal private buffer and can be received by {@link #getIntersection(Line, int)} method
     * </p>
     */
    @Override
    public Point getIntersection(Line line) {
        if(line instanceof StraightLine || line instanceof LineSegment) {
            double angle = -((StraightLine)line).getAngle();
            
            Point s = line.getFirstPoint().clone();
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
            
            Point result1 = getPointAt(t1);
            Point result2 = getPointAt(t2);
            
            Point[] array = new Point[] { result1, result2 };
            otherIntersections.put(line, array);
            return array[0];
        } else if(line instanceof QuadBezierCurve) {
            QuadBezierCurve other = (QuadBezierCurve)line;
            
            Set<Point> pointsSet = new HashSet<>();
            
            for(int i = 0; i < DEFAULT_TIME_RATIO; i++) {
                Point a = getPointAt(i / DEFAULT_TIME_RATIO);
                Point b = getPointAt((i+1) / DEFAULT_TIME_RATIO);
                
                StraightLine segment = new StraightLine(a, b);
                
                Point p = other.getIntersection(segment);
                if(p != null) {
                    pointsSet.add(p);
                }
            }
            
            Point[] array = pointsSet.toArray(new Point[0]);
            otherIntersections.put(other, array);
            return array[0];
        }
        return null;
    }
    
    /**
     * Returns specified intersection point 
     * <p>
     *     This curve line can have four points, if it intersects with BezierCurve and two points if intersects with any straight line
     * </p>
     * 
     * @param otherLine specified line
     * @param which index of intersection point
     * @return intersection point with specified index
     */
    public Point getIntersection(Line otherLine, int which) {
        return otherIntersections.get(otherLine)[which];
    }

    @Override
    public PointPosition getPointPosition(Point point) {
        StraightLine test = new StraightLine(point.getX() - 0.1, point.getY() - 0.1, point.getX() + 0.1, point.getY() + 0.1);
        if(getIntersection(test).equals(point) || getIntersection(test, 1).equals(point)) {
            return PointPosition.INSIDE;
        }
        return PointPosition.OUTSIDE;
    }

    @Override
    public Point getFirstPoint() {
        return a;
    }

    @Override
    public Point getSecondPoint() {
        return c;
    }
    
    /**
     * Returns current support point
     * 
     * @return current support point
     */
    public Point getSupportPoint() {
        return b;
    }

    @Override
    public QuadBezierCurve clone() {
        return new QuadBezierCurve(a, b, c);
    }

    @Override
    public void rotateByOrigin(double angle) {
        a.rotateByOrigin(angle);
        b.rotateByOrigin(angle);
        c.rotateByOrigin(angle);
    }

    @Override
    public void translate(double x, double y) {
        a.translate(x, y);
        b.translate(x, y);
        c.translate(x, y);
    }
}
