package org.kllbff.magic.geometry;

public class Polygon extends Shape {
    private java.awt.Polygon polygon;
    
    public Polygon() {
        this(new java.awt.Polygon());
    }
    
    public Polygon(java.awt.Shape polygon) {
        this.polygon = (java.awt.Polygon)polygon;
    }
    
    public Polygon add(Point point) {
        this.polygon.addPoint(point.getX(), point.getY());
        return this;
    }
    
    public Polygon add(int x, int y) {
        this.polygon.addPoint(x, y);
        return this;
    }
    
    @Override
    public boolean contains(int x, int y) {
        return polygon.contains(x, y);
    }

    @Override
    public boolean contains(Rectangle other) {
        return polygon.contains(other.toAWT());
    }

    @Override
    public boolean intersects(int x, int y, int width, int height) {
        return polygon.intersects(x, y, width, height);
    }

    @Override
    public boolean intersects(Rectangle other) {
        return polygon.intersects(other.toAWT());
    }

    @Override
    public java.awt.Polygon toAWT() {
        return polygon;
    }

}
