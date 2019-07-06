package org.kllbff.magic.graphics.strokes;

public abstract class Stroke {    
    public static enum CapType {
        BUTT,
        ROUND,
        SQUARE
    }
    
    public static enum JoinType {
        BEVEL,
        SHARP,
        ROUND
    }
    
    protected int width;
    protected long color;
    protected CapType cap;
    protected JoinType join;
    
    public Stroke(long color, int width, CapType cap, JoinType join) {
        this.width = width;
        this.color = color;
        this.cap = cap;
        this.join = join;
    }
    
    public void setCapType(CapType cap) {
        this.cap = cap;
    }
    
    public CapType getCapType() {
        return this.cap;
    }
    
    public void setJoinType(JoinType join) {
        this.join = join;
    }
    
    public JoinType getJoinType() {
        return this.join;
    }
    
    public void setWidth(int width) {
        this.width = width;
    }
    
    public int getWidth() {
        return width;
    }
    
    public void setColor(long color) {
        this.color = color;
    }
    
    public long getColor() {
        return color;
    }
    
    public abstract float[] getDrawingPattern();
}
