package org.kllbff.magic.graphics;

public enum ColorSpace {
    sRGB   (0),
    CMYK   (1),
    HSB    (2),
    CIELAB (3),
    XYZ    (4);
    
    private long flag;
    
    private ColorSpace(long flag) {
        this.flag = flag;
    }
    
    public long getFlag() {
        return flag;
    }
    
    public static ColorSpace forFlag(long flag) {
        for(ColorSpace space : values()) {
            if(space.flag == flag) {
                return space;
            }
        }
        return null;
    }
}
