package org.kllbff.magic.graphics;

import org.kllbff.magic.graphics.color.Color;

public class Paint {
    public static enum Type {
        STROKE          (1),
        FILL            (2);
        
        protected int flag;
        private Type(int flag) {
            this.flag = flag;
        }
    }
    
    private boolean antialiased;
    private long color;
    private Type type;
    
    public Paint(long color, Type type) {
        this.color = color;
        this.type = type;
        this.antialiased = true;
    }
    
    public Paint(long color) {
        this(color, Type.STROKE);
    }
    
    public Paint() {
        this(Color.WHITE);
    }
    
    public void setType(Type type) {
        this.type = type;
    }
    
    public boolean canFill() {
        return (type.flag & 2) == 2;
    }
    
    public boolean hasStroke() {
        return (type.flag & 1) == 1;
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
