package org.kllbff.magic.event;

public class InputEvent {
    public static final int MASK_SIMPLE = 0x00,
                            MASK_CTRL   = 0x02,
                            MASK_ALT    = 0x04,
                            MASK_SHIFT  = 0x08;
    
    protected int mask;

    public InputEvent(int mask) {
        this.mask = mask;
    }
    
    public int getMask() {
        return mask;
    }
    
    public boolean isCtrlPressed() {
        return (mask & MASK_CTRL) != 0;
    }
    
    public boolean isAltPressed() {
        return (mask & MASK_ALT) != 0;
    }
    
    public boolean isShiftPressed() {
        return (mask & MASK_SHIFT) != 0;
    }
    
    public boolean isSimple() {
        return (mask & MASK_SIMPLE) != 0;
    }
}
