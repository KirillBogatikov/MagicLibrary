package org.kllbff.magic.geometry;

public class Rectangle extends Shape {
    private java.awt.Rectangle rect;
    
    public Rectangle(int x, int y, int width, int height) {
        rect = new java.awt.Rectangle(x, y, width, height);
    }
    
    public Rectangle(Point leftTop, Point rightBottom) {
        this(leftTop.getX(), leftTop.getY(), rightBottom.getX() - leftTop.getX(), rightBottom.getY() - leftTop.getY());
    }
    
    public Rectangle(java.awt.Shape rect) {
        this.rect = (java.awt.Rectangle)rect;
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
    public java.awt.Rectangle toAWT() {
        return rect;
    }
}
