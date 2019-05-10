package org.kllbff.magiclibrary.geometry.shapes;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.kllbff.magiclibrary.geometry.Point;
import org.kllbff.magiclibrary.geometry.StraightLine;

public class Triangle extends Shape {
    private Comparator<Point> verticesComparator;
    private Point origin;
    private Point[] vertices;

    @Override
    public Point[] getVertices() {
        return vertices;
    }
    
    @Override
    public List<Point> getIntersections(StraightLine line) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Shape> getIntersectionAreas(Shape other) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Updates bounding rectangle and calculates it's center point
     * <p>This method <b>must be called</b> manually after every changing of added vertices coordinates. <br>
     *    Example:
     *    <pre><code>
     *        Point a = new Point(0, 0);
     *        Point b = new Point(1, 1);
     *        Point c = new Point(-1, 1);
     *      
     *        Triangle triangle = new Triangle(a, b, c);
     *      
     *        a.setY(-10);
     *        triangle.update();
     *   </code></pre></p>
     */
    public void update() {
        double minX = Math.min(vertices[0].getX(), Math.min(vertices[1].getX(), vertices[2].getX()));
        double minY = Math.min(vertices[0].getY(), Math.min(vertices[1].getY(), vertices[2].getY()));
        double maxX = Math.max(vertices[0].getX(), Math.max(vertices[1].getX(), vertices[2].getX()));
        double maxY = Math.max(vertices[0].getY(), Math.max(vertices[1].getY(), vertices[2].getY()));
        
        double width = maxX - minX;
        double height = maxY - minY;
        
        this.origin.setX(minX + width / 2);
        this.origin.setY(minY + height / 2);
        
        Arrays.sort(this.vertices, verticesComparator);
    }
}
