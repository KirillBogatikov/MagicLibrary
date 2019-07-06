package org.kllbff.magic.graphics.color;

import static org.kllbff.magic.math.DigitsRounder.round;
import org.kllbff.magic.exceptions.IncorrectColorException;

public final class Color {
    public static final long WHITE = parseHex("#FFFFFF");
    public static final long BLACK = parseHex("#000000");
    public static final long BLUE = parseHex("#0000FF");
    public static final long GREEN = parseHex("#00FF00");
    public static final long RED = parseHex("#FF0000");
    public static final long YELLOW = parseHex("#FFFF00");
    public static final long PURPLE = parseHex("#FF00FF");
    public static final long CYAN = parseHex("#00FFFF");
    
    public static ColorSpace colorSpace(long color) {        
        return ColorSpace.forFlag(colorSpaceFlag(color));
    }
    
    public static long colorSpaceFlag(long color) {
        return color >> 56;
    }
    
    public static long parseHex(String hex) {
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
        
        return Color.parseRGBA(r, g, b, a);
    }
    
    public static long parseRGB(int r, int g, int b) {
        return parseRGBA(r, g, b, 0xFF);
    }
    
    public static long parseRGBA(int r, int g, int b, long a) {
        a &= 0xFF;
        r &= 0xFF;
        g &= 0xFF;
        b &= 0xFF;
        
        return (ColorSpace.sRGB.getFlag() << 56) | (a << 48) | (r << 16) | (g << 8) | b;
    }
    
    public static int alpha(long color) {
        return (int)((color >>> 48) & 0xFF);
    }
    
    public static int red(long color) {
        return (int)((color >>> 16) & 0xFF);
    }
    
    public static int green(long color) {
        return (int)((color >>> 8) & 0xFF);
    }
    
    public static int blue(long color) {
        return (int)(color & 0xFF);
    }
    
    public static long parseCMYK(int c, int m, int y, int k) {
        return parseCMYK(c, m, y, k, 0xFF);
    }
    
    public static long parseCMYK(int c, int m, int y, int k, long a) {
        a = a & 0xFF;
        c &= 0xFF;
        m &= 0xFF;
        y &= 0xFF;
        k &= 0xFF;
        return (ColorSpace.CMYK.getFlag() << 56) | (a << 48) | (c << 24) | (m << 16) | (y << 8) | k;
    }
    
    public static int cyan(long color) {
        return (int)((color >>> 24) & 0xFF);
    }
    
    public static int magenta(long color) {
        return (int)((color >>> 16) & 0xFF);
    }
    
    public static int yellow(long color) {
        return (int)((color >>> 8) & 0xFF);
    }
    
    public static int black(long color) {
        return (int)(color & 0xFF);
    }
    
    public static long parseHSB(int h, int s, int b) {
        return parseHSB(h, s, b, 0xFF);
    }
    
    public static long parseHSB(int h, int s, int b, long a) {
        a &= 0xFF;
        h &= 0xFF;
        s &= 0xFF;
        b &= 0xFF;
        return (ColorSpace.HSB.getFlag() << 56) | (a << 48) | (h << 16) | (s << 8) | b;
    }
    
    public static int hue(long color) {
        return (int)((color >> 16) & 0xFF);
    }
    
    public static int saturation(long color) {
        return (int)((color >> 8) & 0xFF);
    }
    
    public static int brightness(long color) {
        return (int)(color & 0xFF);
    }
    
    public static long parseCIELab(int l, int a, int b) {
        return parseCIELab(l, a, b, 0xFF);
    }
    
    public static long parseCIELab(int l, int a, int b, long alpha) {
        alpha &= 0xFF;
        l &= 0x1FF;
        a = (a < 0 ? 0b100000000 : 0) | (a & 0xFF);
        b = (b < 0 ? 0b100000000 : 0) | (b & 0xFF);
        
        return (ColorSpace.CIELAB.getFlag() << 56) | (alpha << 48) | (l << 18) | (a << 9) | b;
    }
    
    public static int l(long color) {
        return (int)((color >>> 18) & 0x1FF);
    }
    
    public static int a(long color) {
        long n = (color >>> 17) & 0x1;
        return (int)(((color >>> 9) & 0xFF) - (n == 1 ? 256 : 0));
    }
    
    public static int b(long color) {
        long n = (color >>> 8) & 0x1;
        return (int)((color & 0xFF) - (n == 1 ? 256 : 0));
    }
    
    public static long parseXYZ(double x, double y, double z) {
        return parseXYZ(x, y, z, 0xFF);
    }
    
    public static long parseXYZ(double x, double y, double z, long a) {
        a &= 0xFF;
        long xl = (long)(x * 682.5) & 0xFFFF;
        long yl = (long)(y * 655.3) & 0xFFFF;
        long zl = (long)(z * 601.2) & 0xFFFF;
        return (ColorSpace.XYZ.getFlag() << 56) | (a << 48) | (xl << 32) | (yl << 16) | zl;
    }
    
    public static double x(long color) {
        return round(((color >>> 32) & 0xFFFF) / 682.5, 2);
    }
    
    public static double y(long color) {
        return round(((color >>> 16) & 0xFFFF) / 655.3, 2);
    }
    
    public static double z(long color) {
        return round((color & 0xFFFF) / 601.2, 2);
    }
}
