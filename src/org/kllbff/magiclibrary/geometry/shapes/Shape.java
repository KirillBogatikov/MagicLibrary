package org.kllbff.magiclibrary.geometry.shapes;

import org.kllbff.magiclibrary.geometry.Point;
import org.kllbff.magiclibrary.geometry.PointPosition;
import org.kllbff.magiclibrary.geometry.StraightLine;

public interface Shape {
    public Point[] getVertices();
    public PointPosition getPointPosition(Point point);
    public boolean contains(Point point);
    public boolean contains(Shape other);
    public Point getIntersection(StraightLine line);
    public Shape getIntersectionArea(Shape other);
    
    public default boolean hasIntersection(StraightLine line) {
        return getIntersection(line) != null;
    }
    public default boolean hasIntersectionArea(Shape other) {
        return getIntersectionArea(other) != null;
    }
}
