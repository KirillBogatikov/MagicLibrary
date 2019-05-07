package org.kllbff.magiclibrary.geometry;

/**
 * <h3>Describes a simple segment of 2D line</h3>
 * <p>Segment of any 2D line can be represented as two points on the plane, connected with this line segment.</p>
 * <p>This class extends a {@link StraightLine} class has some methods:
 * <ul>
 *     <li>getters and setters for two points</li>
 *     <li>finding the point of intersection of two line: this and specified</li>
 *     <li>calculating angle between line and positive x-axis</li>
 *     <li>determining the presence of an intersection point</li>
 *     <li>detection point position about this line</li>
 *     <li>determining whether a point is contained on a straight line</li>
 * </ul>
 * </p>
 * 
 * @author Kirill Bogatikov
 * @version 1.0
 * @since 1.0
 */
public class LineSegment extends StraightLine {
    /**
     * Initializes line by two given points
     * 
     * @param first any point lying on this line
     * @param second any point lying on this line
     * @throws NullPointerException if any of points is null
     * @throws RuntimeException if points are equals
     */
    public LineSegment(Point first, Point second) {
        super(first, second);
    }
    
    /**
     * Initializes line by four given coordinates (coordinates of two points)
     * 
     * @param x1 x-axis coordinate of first any point lying on this line
     * @param y1 y-axis coordinate of first any point lying on this line
     * @param x2 x-axis coordinate of second any point lying on this line
     * @param y2 y-axis coordinate of second any point lying on this line
     * @throws NullPointerException if any of points is null
     * @throws RuntimeException if points are equals
     */
    public LineSegment(double x1, double y1, double x2, double y2) {
        this(new Point(x1, y1), new Point(x2, y2));
    }    
    
    /**
     * Returns point of intersection this line with specified line or <code>null</code> if intersection does not exist
     * 
     * @param line specified line
     * @return point of intersection this line with specified line
     */
    public Point getIntersection(StraightLine other) {
        Point point = super.getIntersection(other);
        
        if(point == null) {
            return point;
        }
        
        if(getPointPosition(point) == PointPosition.INSIDE && other.getPointPosition(point) == PointPosition.INSIDE) {
            return point;
        }
        
        return null;
    }

    /**
     * Returns one item from PointPosition, representing position of specified point on this shape<br>
     * Can return:
     * <dl>
     *     <dt>INSIDE</dt>
     *     <dd>If this line contians specified point</dd>
     *     <dt>RIGHT</dt>
     *  <dd>If specified point lies on straight line, but outside this segment and point's x-coordinate bigger than x-coordinate of second point</dd>
     *     <dt>LEFT</dt>
     *  <dd>If specified point lies on straight line, but outside this segment and point's x-coordinate smaller than x-coordinate of second point</dd>
     *     <dt>ABOVE</dt>
     *     <dd>If specified point lies in the upper half-plane with respect to the line</dd>
     *  <dt>BELOW</dt>
     *  <dd>If specified point lies in the lower half-plane with respect to the line</dd>
     * </dl>  
     * 
     * @param point specified Point for checking
     * @return one of enum PointPosition
     */
    public PointPosition getPointPosition(Point point) {
        PointPosition position = super.getPointPosition(point);
        if(position == PointPosition.INSIDE) {
            double x = Math.min(Math.abs(first.getX()), Math.abs(second.getX()));
            if(Math.abs(point.getX()) < x) {
                return PointPosition.LEFT;
            }
            
            x = Math.max(Math.abs(first.getX()), Math.abs(second.getX()));
            if(Math.abs(point.getX()) > x) {
                return PointPosition.RIGHT;
            }
        }
        return position;
    }
    
    /**
     * Returns a string representation of the line segment, containing this vaertices current coordinates 
     * 
     * @return String representation of the form: <code>"Line segment from (" + {@link Point#getX()} + ", " + {@link Point#getY()} + ") to (" +  + {@link Point#getX()} + ", " + {@link Point#getY()} + ")"</code>
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("LineSegment from (");
        if(first == null) {
            builder.append("unknown, unknown");
        } else {
            builder.append(first.getX()).append(", ").append(first.getY());
        }
        builder.append(") to (");
        if(second == null) {
            builder.append("unknown, unknown");
        } else {
            builder.append(second.getX()).append(", ").append(second.getY());
        }
        builder.append(")");
        
        return builder.toString();
    }
}