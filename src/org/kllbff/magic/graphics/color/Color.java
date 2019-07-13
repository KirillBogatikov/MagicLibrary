package org.kllbff.magic.graphics.color;

import static org.kllbff.magic.math.DigitsRounder.round;

import org.kllbff.magic.exceptions.IncorrectColorException;

/**
 * <h3>Helper class for working with colors in different color spaces</h3>
 * <p>The highest 8 bits of each color indicate its color space, the next 8
 *    contain the value of the alpha channel, from 0 to 255. The 48 least
 *    significant bits are used to store the color components and their
 *    distribution depends on the color space:</p>
 * <dl>
 *  <dt>sRGB</dt>
 *  <dd>The lowest 24 bits is distributed between the three components: <br />
 *      Blue (0-8), Green (8-16), Red (16-24), <br />
 *      where 0 is the least significant bit
 *  </dd>
 *  <dt>CMYK</dt>
 *  <dd>the lowest 32 bits are distributed among four components: <br />
 *      blacK (0-8), Yellow (8-16), Magenta (16-24), Cyan (24-32), <br />
 *      where 0 is the lowest bit
 *  </dd>
 *  <dt>HSB</dt>
 *  <dd>The lowest 24 bits is distributed between the three components: <br />
 *      Brightness (0-8), Saturation (8-16), Hue (16-24), <br />
 *      where 0 is the least significant bit
 *  </dd>
 *  <dt>CIE Lab</dt>
 *  <dd>The lowest 27 bits is distributed between the three components: <br />
 *      L (0-9), a (9-18), b (18-27), <br />
 *      where 0 is the least significant bit
 *  </dd>
 *  <dt>XYZ</dt>
 *  <dd>All 48 bits is distributed between the three components: <br />
 *      X (0-16), Y (16-32), Z (32-48), <br />
 *      where 0 is the least significant bit.<br />
 *      X, Y and Z are double numbers, and it will be stored as integer by multipling on special number. For more information, see {@link #xyz(double, double, double, long)}
 *  </dd>
 * </dl>
 * 
 * @author Kirill Bogatikov
 * @version 1.0
 * @since 1.0
 */
public final class Color {
    public static final long WHITE = hex("#FFFFFF");
    public static final long BLACK = hex("#000000");
    public static final long BLUE = hex("#0000FF");
    public static final long GREEN = hex("#00FF00");
    public static final long RED = hex("#FF0000");
    public static final long YELLOW = hex("#FFFF00");
    public static final long PURPLE = hex("#FF00FF");
    public static final long CYAN = hex("#00FFFF");
    
    /**
     * Returns one of values from {@link ColorSpace} associated with color space flag from color
     * <p>Color flag stored in 8 highest bits and can be calculated as color >>> 56</p>
     * 
     * @param color long-number color
     * @return one of values from {@link ColorSpace} associated with color space flag
     */
    public static ColorSpace colorSpace(long color) {        
        return ColorSpace.forFlag(colorSpaceFlag(color));
    }
    
    /**
     * Returns color space flag calculated from long number color
     * <p>Color flag stored in 8 highest bits and can be calculated as color >>> 56</p>
     * 
     * @param color long-number color
     * @return color space flag value
     */
    public static int colorSpaceFlag(long color) {
        return (int)(color >>> 56);
    }
    
    /**
     * Parses color described as RGBA Hex string to long-number color
     * <p>This method supports few types of hex color: #RGB, #RGBA, #RRGGBB, #RRGGBBAA</p>
     * 
     * @param hex color described as RGBA Hex string
     * @return long-number color
     * @throws IncorrectColorException string does not match any pattern
     */
    public static long hex(String hex) {
        if(hex.length() < 4) {
            throw new IncorrectColorException("Minimal length of HEX Color string is 4 symbols: #RGB");
        }
        if(hex.length() > 9) {
            throw new IncorrectColorException("Maximal length of HEX Color string is 9 symbols: #RRGGBBAA");
        }
        if(hex.charAt(0) != '#') {
            throw new IncorrectColorException("HEX Color string must start with 'sharp' symbol '#'");
        }
        
        int r = 0x00, 
            g = 0x00, 
            b = 0x00;
        long a = 0xFF;
        switch(hex.length()) {
            case 4: {
                r = Integer.parseInt(hex.substring(1, 2), 16);
                g = Integer.parseInt(hex.substring(2, 3), 16);
                b = Integer.parseInt(hex.substring(3, 4), 16);
                a = 0xFF;
            } break;
            case 5: {
                r = Integer.parseInt(hex.substring(1, 2), 16);
                g = Integer.parseInt(hex.substring(2, 3), 16);
                b = Integer.parseInt(hex.substring(3, 4), 16);
                a = Integer.parseInt(hex.substring(4, 5), 16);
            } break;
            case 7: {
                r = Integer.parseInt(hex.substring(1, 3), 16);
                g = Integer.parseInt(hex.substring(3, 5), 16);
                b = Integer.parseInt(hex.substring(5, 7), 16);
                a = 0xFF;
            } break;
            case 9: {
                r = Integer.parseInt(hex.substring(1, 3), 16);
                g = Integer.parseInt(hex.substring(3, 5), 16);
                b = Integer.parseInt(hex.substring(5, 7), 16);
                a = Integer.parseInt(hex.substring(7, 9), 16);
            } break;
        }
        
        return Color.rgba(r, g, b, a);
    }
    
    /**
     * Returns color builded from specified sRGB components
     * 
     * @param r red component, 0..255
     * @param g green component, 0..255
     * @param b blue component, 0..255
     * @return color builded from specified sRGB components
     */
    public static long rgb(int r, int g, int b) {
        return rgba(r, g, b, 0xFF);
    }
    
    /**
     * Returns color builded from specified sRGB components
     * <p>RGBA color builded as (sRGB << 56) | (a << 48) | (r << 16) | (g << 8) | b</p>
     * 
     * @param r red component, 0..255
     * @param g green component, 0..255
     * @param b blue component, 0..255
     * @param a alpha component, 0..255
     * @return color builded from specified sRGB components
     */
    public static long rgba(int r, int g, int b, long a) {
        a &= 0xFF;
        r &= 0xFF;
        g &= 0xFF;
        b &= 0xFF;
        
        return (ColorSpace.sRGB.getFlag() << 56) | (a << 48) | (r << 16) | (g << 8) | b;
    }
    
    /**
     * Returns value of alpha channel calculated from color 
     * <p>Alpha channel value can be calclulated as (color >> 48) & 0xFF</p>
     * 
     * @param color long number color
     * @return value of alpha channel
     */
    public static int alpha(long color) {
        return (int)((color >> 48) & 0xFF);
    }

    /**
     * Returns value of red channel calculated from color 
     * <p>Red channel value can be calclulated as (color >> 16) & 0xFF</p>
     * 
     * @param color long number color, in sRGB color space
     * @return value of red channel
     */
    public static int red(long color) {
        return (int)((color >> 16) & 0xFF);
    }

    /**
     * Returns value of green channel calculated from color 
     * <p>Green channel value can be calclulated as (color >> 8) & 0xFF</p>
     * 
     * @param color long number color, in sRGB color space
     * @return value of green channel
     */
    public static int green(long color) {
        return (int)((color >> 8) & 0xFF);
    }

    /**
     * Returns value of blue channel calculated from color 
     * <p>Blue channel value can be calclulated as (color & 0xFF)</p>
     * 
     * @param color long number color, in sRGB color space
     * @return value of blue channel
     */
    public static int blue(long color) {
        return (int)(color & 0xFF);
    }
    
    /**
     * Returns color builded from specified CMYK components
     * 
     * @param c cyan component, 0..100
     * @param m magenta component, 0..100
     * @param y yellow component, 0..100
     * @param k black component, 0..100
     * @return color builded from specified CMYK components
     */
    public static long cmyk(int c, int m, int y, int k) {
        return cmyk(c, m, y, k, 0xFF);
    }
    
    /**
     * Returns color builded from specified CMYK components
     * 
     * @param c cyan component, 0..100
     * @param m magenta component, 0..100
     * @param y yellow component, 0..100
     * @param k black component, 0..100
     * @param a alpha component, 0..255
     * @return color builded from specified CMYK components
     */
    public static long cmyk(int c, int m, int y, int k, long a) {
        a = a & 0xFF;
        c &= 0xFF;
        m &= 0xFF;
        y &= 0xFF;
        k &= 0xFF;
        return (ColorSpace.CMYK.getFlag() << 56) | (a << 48) | (c << 24) | (m << 16) | (y << 8) | k;
    }
    
    /**
     * Returns value of cyan component from color
     * 
     * @param color long number color, in CMYK color space
     * @return value of cyan component
     */
    public static int cyan(long color) {
        return (int)((color >>> 24) & 0xFF);
    }

    /**
     * Returns value of magenta component from color
     * 
     * @param color long number color, in CMYK color space
     * @return value of magenta component
     */
    public static int magenta(long color) {
        return (int)((color >>> 16) & 0xFF);
    }

    /**
     * Returns value of yellow component from color
     * 
     * @param color long number color, in CMYK color space
     * @return value of yellow component
     */
    public static int yellow(long color) {
        return (int)((color >>> 8) & 0xFF);
    }

    /**
     * Returns value of black component from color
     * 
     * @param color long number color, in CMYK color space
     * @return value of black component
     */
    public static int black(long color) {
        return (int)(color & 0xFF);
    }
    
    /**
     * Returns color builded from HSB color components
     * 
     * @param h hue component, 0...360
     * @param s saturation component, 0..100
     * @param b brightness component, 0..100
     * @return color builded from HSB color components
     */
    public static long hsb(int h, int s, int b) {
        return hsb(h, s, b, 0xFF);
    }
    
    /**
     * Returns color builded from HSB/HSV color components
     * 
     * @param h hue component, 0...360
     * @param s saturation component, 0..100
     * @param b brightness component, 0..100
     * @param a alpha component, 0..255
     * @return color builded from HSB color components
     */
    public static long hsb(int h, int s, int b, long a) {
        a &= 0xFF;
        h &= 0x1FF;
        s &= 0xFF;
        b &= 0xFF;
        return (ColorSpace.HSB.getFlag() << 56) | (a << 48) | (h << 16) | (s << 8) | b;
    }
    
    /**
     * Returns value of hue component from color
     * 
     * @param color long number color, in HSB/HSV color space
     * @return value of hue component from color
     */
    public static int hue(long color) {
        return (int)((color >> 16) & 0x1FF);
    }

    /**
     * Returns value of saturation component from color
     * 
     * @param color long number color, in HSB/HSV color space
     * @return value of saturation component from color
     */
    public static int saturation(long color) {
        return (int)((color >> 8) & 0xFF);
    }

    /**
     * Returns value of brightness component from color
     * 
     * @param color long number color, in HSB/HSV color space
     * @return value of brightness component from color
     */
    public static int brightness(long color) {
        return (int)(color & 0xFF);
    }
    
    /**
     * Returns color builded from CIE Lab components
     * 
     * @param l l component, 0..100
     * @param a a component, -128..128
     * @param b b component, -128..128
     * @return color builded from CIE Lab components
     */
    public static long cieLab(int l, int a, int b) {
        return cieLab(l, a, b, 0xFF);
    }

    /**
     * Returns color builded from CIE Lab components
     * 
     * @param l l component, 0..100
     * @param a a component, -128..128
     * @param b b component, -128..128
     * @param alpha alpha component, 0..255
     * @return color builded from CIE Lab components
     */
    public static long cieLab(int l, int a, int b, long alpha) {
        alpha &= 0xFF;
        l &= 0xFF;
        a = (a < 0 ? 0b100000000 : 0) | (a & 0xFF);
        b = (b < 0 ? 0b100000000 : 0) | (b & 0xFF);
        
        return (ColorSpace.CIELAB.getFlag() << 56) | (alpha << 48) | (l << 18) | (a << 9) | b;
    }
    
    /**
     * Returns l component from color
     * 
     * @param color long number color, in CIE Lab color space
     * @return l component from color
     */
    public static int l(long color) {
        return (int)((color >>> 18) & 0xFF);
    }

    /**
     * Returns a component from color
     * 
     * @param color long number color, in CIE Lab color space
     * @return a component from color
     */
    public static int a(long color) {
        long n = (color >>> 17) & 0x1;
        return (int)(((color >>> 9) & 0xFF) - (n == 1 ? 256 : 0));
    }

    /**
     * Returns b component from color
     * 
     * @param color long number color, in CIE Lab color space
     * @return b component from color
     */
    public static int b(long color) {
        long n = (color >>> 8) & 0x1;
        return (int)((color & 0xFF) - (n == 1 ? 256 : 0));
    }
    
    /**
     * Returns color builded from XYZ components
     * 
     * @param x x-axis coordinate of color, 0..95,047
     * @param y y-axis coordinate of color, 0..100
     * @param z z-axis coordinate of color, 0..108.833
     * @return color builded from XYZ components
     */
    public static long xyz(double x, double y, double z) {
        return xyz(x, y, z, 0xFF);
    }

    /**
     * Returns color builded from XYZ components
     * <p>All coordinates of color is float point numbers, therefore 
     *    even coordinate must be multipled on some number, to safe save
     *    into long number. 
     * </p>
     * <p>X coordinate will be multipled on 682.5, so will be equals 64870 (less than max 65536)</p>
     * <p>Y coordinate will be multipled on 655.3, so will be equals 65530 (less than max 65536)</p>
     * <p>Z coordinate will be multipled on 601.2, so will be equals 65430 (less than max 65536)</p>
     * 
     * @param x x-axis coordinate of color, 0..95,047
     * @param y y-axis coordinate of color, 0..100
     * @param z z-axis coordinate of color, 0..108.833
     * @param alpha alpha component of color, 0..255
     * @return color builded from XYZ components
     */
    public static long xyz(double x, double y, double z, long a) {
        a &= 0xFF;
        long xl = (long)(x * 682.5) & 0xFFFF;
        long yl = (long)(y * 655.3) & 0xFFFF;
        long zl = (long)(z * 601.2) & 0xFFFF;
        return (ColorSpace.XYZ.getFlag() << 56) | (a << 48) | (xl << 32) | (yl << 16) | zl;
    }
    
    /**
     * Returns x-axis coordinate of color
     * 
     * @param color long number color, in XYZ color space
     * @return x-axis coordinate of color
     */
    public static double x(long color) {
        return round(((color >>> 32) & 0xFFFF) / 682.5, 2);
    }

    /**
     * Returns y-axis coordinate of color
     * 
     * @param color long number color, in XYZ color space
     * @return y-axis coordinate of color
     */
    public static double y(long color) {
        return round(((color >>> 16) & 0xFFFF) / 655.3, 2);
    }

    /**
     * Returns z-axis coordinate of color
     * 
     * @param color long number color, in XYZ color space
     * @return z-axis coordinate of color
     */
    public static double z(long color) {
        return round((color & 0xFFFF) / 601.2, 2);
    }
}
