package org.kllbff.magic.geometry;

public class RoundRectangle extends Shape {
    private java.awt.geom.RoundRectangle2D.Double rect;
    
    public RoundRectangle(int x, int y, int width, int height, int xRadius, int yRadius) {
        rect = new java.awt.geom.RoundRectangle2D.Double(x, y, width, height, xRadius, yRadius);
    }
    
    public RoundRectangle(int x, int y, int width, int height) {
        this(x, y, width, height, 5, 5);
    }
    
    public RoundRectangle(java.awt.Shape rect) {
        this.rect = (java.awt.geom.RoundRectangle2D.Double)rect;
    }
    
    public void setX(int x) {
        rect.x = x;
    }
    
    public void setY(int y) {
        rect.y = y;
    }
    
    public void setWidth(int width) {
        rect.width = width;
    }
    
    public void setHeight(int height) {
        rect.height = height;
    }
    
    @Override
    public boolean contains(int x, int y) {
        return rect.contains(x, y);
    }

    @Override
    public boolean contains(Rectangle other) {
        return rect.contains(other.toAWT());
    }

    @Override
    public boolean intersects(int x, int y, int width, int height) {
        return rect.intersects(x, y, width, height);
    }

    @Override
    public boolean intersects(Rectangle other) {
        return rect.intersects(other.toAWT());
    }

    @Override
    public java.awt.geom.RoundRectangle2D toAWT() {
        return rect;
    }
}
