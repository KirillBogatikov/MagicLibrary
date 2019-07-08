package org.kllbff.magic.geometry;

public abstract class Shape {
    public abstract boolean contains(int x, int y);
    public abstract boolean contains(Rectangle other);
    public abstract boolean intersects(int x, int y, int width, int height);
    public abstract boolean intersects(Rectangle other);
    public abstract java.awt.Shape toAWT();
}
