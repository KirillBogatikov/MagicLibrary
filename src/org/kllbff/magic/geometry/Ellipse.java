package org.kllbff.magic.geometry;

public class Ellipse extends Shape {
    private java.awt.geom.Ellipse2D ellipse;
    
    public Ellipse() {
        ellipse = new java.awt.geom.Ellipse2D.Double();
    }
    
    public Ellipse(int x, int y, int width, int height) {
        ellipse = new java.awt.geom.Ellipse2D.Double(x, y, width, height);
    }
    
    public Ellipse(java.awt.Shape shape) {
        ellipse = (java.awt.geom.Ellipse2D.Double)shape;
    }
    
    @Override
    public boolean contains(int x, int y) {
        return ellipse.contains(x, y);
    }

    @Override
    public boolean contains(Rectangle other) {
        return ellipse.contains(other.toAWT());
    }

    @Override
    public boolean intersects(int x, int y, int width, int height) {
        return ellipse.intersects(x, y, width, height);
    }

    @Override
    public boolean intersects(Rectangle other) {
        return ellipse.intersects(other.toAWT());
    }

    @Override
    public java.awt.geom.Ellipse2D toAWT() {
        return ellipse;
    }

}
