package org.kllbff.magic.graphics;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import org.kllbff.magic.geometry.Point;

public class Path {
    private Path2D awtPath;
    
    public Path() {
        awtPath = new Path2D.Double();
    }
    
    public Path moveTo(int x, int y) {
        awtPath.moveTo(x, y);
        return this;
    }
    
    public Path lineTo(int x, int y) {
        awtPath.lineTo(x, y);
        return this;
    }
    
    public Path cubicTo(int x1, int y1, int x2, int y2, int x3, int y3) {
        awtPath.curveTo(x1, y1, x2, y2, x3, y3);
        return this;
    }
    
    public Path quadTo(int x1, int y1, int x2, int y2) {
        awtPath.quadTo(x1, y1, x2, y2);
        return this;
    }
    
    public boolean contains(int x, int y) {
        return awtPath.contains(x, y);
    }
    
    public boolean intersects(int x, int y, int w, int h) {
        return awtPath.intersects(x, y, w, h);
    }
    
    public Point currentPosition() {
        Point2D awtPoint = awtPath.getCurrentPoint();
        return new Point(awtPoint.getX(), awtPoint.getY());
    }
    
    public Path reset() {
        awtPath.reset();
        return this;
    }
    
    public void draw(Canvas canvas) {
        canvas.getGraphics().draw(awtPath);
    }
}
