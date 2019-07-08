package org.kllbff.magic.geometry;

public class Area extends Shape {
    private java.awt.geom.Area area;
    
    public Area() {
        area = new java.awt.geom.Area();
    }
    
    public Area(Shape shape) {
        area = new java.awt.geom.Area(shape.toAWT());
    }
    
    public Area(java.awt.Shape shape) {
        area = (java.awt.geom.Area)shape;
    }
    
    public void intersect(Shape shape) {
        area.intersect(new java.awt.geom.Area(shape.toAWT()));
    }
    
    public void add(Shape shape) {
        area.add(new java.awt.geom.Area(shape.toAWT()));
    }
    
    public void subtract(Shape shape) {
        area.subtract(new java.awt.geom.Area(shape.toAWT()));
    }
    
    @Override
    public boolean contains(int x, int y) {
        return area.contains(x, y);
    }

    @Override
    public boolean contains(Rectangle other) {
        return area.contains(other.toAWT());
    }

    @Override
    public boolean intersects(int x, int y, int width, int height) {
        return area.intersects(x, y, width, height);
    }

    @Override
    public boolean intersects(Rectangle other) {
        return area.intersects(other.toAWT());
    }

    @Override
    public java.awt.geom.Area toAWT() {
        return area;
    }

}
