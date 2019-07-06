package org.kllbff.magic.graphics;

import org.kllbff.magic.graphics.color.Color;

public class Paint {    
    public static final int STROKE = 1,
                            FILL   = 2;
    
    private boolean antialiased;
    private long color;
    private int type;
    
    public Paint(long color, int type) {
        this.color = color;
        this.type = type;
        this.antialiased = true;
    }
    
    public Paint(long color) {
        this(color, STROKE);
    }
    
    public Paint() {
        this(Color.WHITE);
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public boolean canFill() {
        return (type & 2) == 2;
    }
    
    public boolean hasStroke() {
        return (type & 1) == 1;
    }
    
    public long getColor() {
        return color;
    }
    
    public void setColor(long color) {
        this.color = color;
    }
    
    public void setAntialiased(boolean enabled) {
        this.antialiased = enabled;
    }
    
    public boolean isAntialiased() {
        return this.antialiased;
    }
}
