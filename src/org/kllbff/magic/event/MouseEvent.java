package org.kllbff.magic.event;

public class MouseEvent extends InputEvent {
    public static final int NO_BUTTON     = -1;
    
    private int x, y;
    private int button;
    
    public MouseEvent(int mask, int button, int x, int y) {
        super(mask);
        this.button = button;
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getButton() {
        return button;
    }
}
