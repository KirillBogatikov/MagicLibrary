package org.kllbff.magic.event;

public class KeyEvent extends InputEvent {
    
    private int code;

    public KeyEvent(int mask, int keyCode) {
        super(mask);
        this.code = keyCode;
    }
    
    public char getChar() {
        return (char)getKeyCode();
    }
    
    public int getKeyCode() {
        return code & 0xFFFF;
    }
    
    
}
