package org.kllbff.magic.graphics.color;

/**
 * <h3>Represents supported by library color spaces</h3>
 * 
 * @author Kirill Bogatikov
 * @version 1.0
 * @since 1.0
 */
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
    
    /**
     * Returns long number flag associated with this color space
     * 
     * @return color space flag
     */
    public long getFlag() {
        return flag;
    }
    
    /**
     * Returns Color Space instance associated with specified color space flag
     * 
     * @param flag number color space flag
     * @return Color Space instance associated with specified color space flag
     */
    public static ColorSpace forFlag(long flag) {
        for(ColorSpace space : values()) {
            if(space.flag == flag) {
                return space;
            }
        }
        return null;
    }
}
