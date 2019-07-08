package org.kllbff.magic.graphics.color;

import org.kllbff.magic.exceptions.IncorrectColorException;
import org.kllbff.magic.math.VarargsMath;

public final class ColorsConverter {
    
    public static long toRGB(long color) {
        ColorSpace colorSpace = Color.colorSpace(color);

        switch(colorSpace) {
            case sRGB: return color;
            case XYZ: return convertXYZtoRGB(color); 
            case CIELAB: return convertCIELABtoRGB(color);
            case HSB: return convertHSBtoRGB(color);
            default: throw new IncorrectColorException("Color space " + colorSpace + " does not supported");
        }
    }
    
    public static long fromRGB(long color, ColorSpace target) {
        if(Color.colorSpace(color) != ColorSpace.sRGB) {
            throw new IncorrectColorException("Initial color must be in sRGB color space");
        }
        
        switch(target) {
            case sRGB: return color;
            case XYZ: return converRGBtoXYZ(color);
            case CIELAB: return convertRGBtoCIELAB(color);
            case HSB: return convertRGBtoHSB(color);
            default: throw new IncorrectColorException("Color space " + target + " does not supported");
        }
    }
    
    public static long converRGBtoXYZ(long color) {
        if(Color.colorSpace(color) != ColorSpace.sRGB) {
            throw new IncorrectColorException("Given color does not in sRGB color space!");
        }
        
        double r = Color.red(color) / 255.0;
        double g = Color.green(color) / 255.0;
        double b = Color.blue(color) / 255.0;
        
        r = fRGBtoXYZ(r);
        g = fRGBtoXYZ(g);
        b = fRGBtoXYZ(b);
        
        /* D65 lumination correction */
        double x = r * 0.4124 + g * 0.3576 + b * 0.1805;
        double y = r * 0.2126 + g * 0.7152 + b * 0.0722;
        double z = r * 0.0193 + g * 0.1192 + b * 0.9505;
        
        return Color.xyz(x, y, z, Color.alpha(color));
    }
    
    private static double fRGBtoXYZ(double c) {
        if(c > 0.04045) {
            c = Math.pow((c + 0.055) / 1.055, 2.4);
        } else {
            c = c / 12.92;
        }
        return c * 100.0;
    }
    
    public static long convertXYZtoRGB(long color) {
        if(Color.colorSpace(color) != ColorSpace.XYZ) {
            throw new IncorrectColorException("Given color does not in XYZ color space!");
        }
        
        double x = Color.x(color) / 100;
        double y = Color.y(color) / 100;
        double z = Color.z(color) / 100;

        double r = x * 3.2406 + y * -1.5372 + z * -0.4986;
        double g = x * -0.9689 + y * 1.8758 + z * 0.0415;
        double b = x * 0.0557 + y * -0.2040 + z * 1.0570;
        
        r = fXYZtoRGB(r);
        g = fXYZtoRGB(g);
        b = fXYZtoRGB(b);
                
        return Color.rgba((int)r, (int)g, (int)b, Color.alpha(color));
    }
    
    private static double fXYZtoRGB(double c) {
        if (c > 0.0031308) {
            c = 1.055 * Math.pow(c, (1 / 2.4)) - 0.055;
        } else {
            c = 12.92 * c;
        }
        
        return Math.min(255.0, Math.max(0.0, c)) * 255.0;
    }
    
    public static long convertXYZtoCIELAB(long color) {
        if(Color.colorSpace(color) != ColorSpace.XYZ) {
            throw new IncorrectColorException("Given color does not in XYZ color space!");
        }
        
        double x = fLABtXYZ(Color.x(color) / 95.047);
        double y = fLABtXYZ(Color.y(color) / 100);
        double z = fLABtXYZ(Color.z(color) / 108.833);
        
        double l = 116 * y - 16;
        double a = 500 * (x - y);
        double b = 200 * (y - z);
                
        return Color.cieLab((int)l, (int)a, (int)b, Color.alpha(color));
    }
    
    private static double fLABtXYZ(double p) {
        if(p > 0.008856) {
            p = Math.pow(p, 1.0 / 3.0);
        } else {
            p = (7.787 * p + 16 / 116);
        }
        return p;
    }
    
    public static long convertCIELABtoXYZ(long color) {
        if(Color.colorSpace(color) != ColorSpace.CIELAB) {
            throw new IncorrectColorException("Given color does not in CIELab color space!");
        }
        
        double y = (Color.l(color) + 16.0) / 116.0;
        double x = Color.a(color) / 500.0 + y;
        double z = y - Color.b(color) / 200.0;

        x = oXYZ(x);
        y = oXYZ(y);
        z = oXYZ(z);
        
        return Color.xyz(95.047 * x, 100 * y, 108.883 * z, Color.alpha(color));
    }
    
    private static double oXYZ(double p) {
        return Math.pow(p, 3) > 0.008856 ? Math.pow(p, 3) : (p - 16.0 / 116.0) / 7.787;
    }
    
    public static long convertRGBtoCIELAB(long color) {
        return convertXYZtoCIELAB(converRGBtoXYZ(color));
    }
    
    public static long convertCIELABtoRGB(long color) {
        return convertXYZtoRGB(convertCIELABtoXYZ(color));
    }
    
    public static long convertRGBtoHSB(long color) {
        if(Color.colorSpace(color) != ColorSpace.sRGB) {
            throw new IncorrectColorException("Given color does not in sRGB color space!");
        }
        
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        
        double max = VarargsMath.max(red, green, blue),
               min = VarargsMath.min(red, green, blue);
        double delta = max - min;
        
        double hue = 0;
        double saturation = (max == 0 ? 0 : delta / max);
        double brightness = max / 255;

        if(max == red) {
            hue = (green - blue) + delta * (green < blue ? 6: 0);
        } else if(max == green) {
            hue = (blue - red) + delta * 2;
        } else if(max == blue) {
            hue = (red - green) + delta * 4;
        }
        if(delta > 0) {
            hue /= 6 * delta;
        }
        
        hue *= 360;
        saturation *= 100;
        brightness *= 100;
        
        return Color.hsb((int)hue, (int)saturation, (int)brightness, Color.alpha(color));
    }
    
    public static long convertHSBtoRGB(long color) {
        double hue = Color.hue(color) / 360.0;
        double saturation = Color.saturation(color) / 100.0;
        double brightness = Color.brightness(color) / 100.0;
        
        double red = 0, green = 0, blue = 0;

        int h = (int)(hue * 6);
        double f = hue * 6 - h;
        double p = brightness * (1 - saturation);
        double q = brightness * (1 - f * saturation);
        double t = brightness * (1 - (1 - f) * saturation);

        if (h == 0) {
            red = brightness;
            green = t;
            blue = p;
        } else if (h == 1) {
            red = q;
            green = brightness;
            blue = p;
        } else if (h == 2) {
            red = p;
            green = brightness;
            blue = t;
        } else if (h == 3) {
            red = p;
            green = q;
            blue = brightness;
        } else if (h == 4) {
            red = t;
            green = p;
            blue = brightness;
        } else if (h <= 6) {
            red = brightness;
            green = p;
            blue = q;
        }

        red *= 255;
        green *= 255;
        blue *= 255;
        
        return Color.rgba((int)red, (int)green, (int)blue, Color.alpha(color));
    }
}
