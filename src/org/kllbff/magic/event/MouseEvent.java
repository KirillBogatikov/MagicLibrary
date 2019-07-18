package org.kllbff.magic.event;

public class MouseEvent extends InputEvent {
    private int x, y;
    private int button;
    
    public MouseEvent(int mask, int button) {
        super(mask);
        this.button = button;
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
