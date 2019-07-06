package org.kllbff.magic.interpolation;

import org.kllbff.magic.graphics.color.Color;
import org.kllbff.magic.graphics.color.ColorsConverter;

public class ColorInterpolator {
    private Interpolator basic;
    private long start, end;
    private long a1, r1, g1, b1;
    private long a2, r2, g2, b2;
    
    public ColorInterpolator(long start, long end, Interpolator basic) {
        this.basic = basic;
        
        this.start = start;
        start = ColorsConverter.toRGB(start);
        a1 = Color.alpha(start);
        r1 = Color.red(start);
        g1 = Color.green(start);
        b1 = Color.blue(start);

        this.end = end;
        end = ColorsConverter.toRGB(end);
        a2 = Color.alpha(end);
        r2 = Color.red(end);
        g2 = Color.green(end);
        b2 = Color.blue(end);
    }

    public Interpolator getBasicInterpolator() {
        return basic;
    }

    public void setBasicInterpolator(Interpolator interpolator) {
        this.basic = interpolator;
    }
    
    public long getStartValue() {
        return start;
    }

    public long getEndValue() {
        return end;
    }

    public void setStartValue(long start) {
        this.start = start;
        
        start = ColorsConverter.toRGB(start);
        a1 = Color.alpha(start);
        r1 = Color.red(start);
        g1 = Color.green(start);
        b1 = Color.blue(start);
    }

    public void setEndValue(long end) {
        this.end = end;
        
        end = ColorsConverter.toRGB(end);
        a2 = Color.alpha(end);
        r2 = Color.red(end);
        g2 = Color.green(end);
        b2 = Color.blue(end);
    }
    
    public Long interpolate(double t) {
        int newA = clip(basic.interpolate(a1, a2, t));
        int newR = clip(basic.interpolate(r1, r2, t));
        int newG = clip(basic.interpolate(g1, g2, t));
        int newB = clip(basic.interpolate(b1, b2, t));

        return Color.parseRGBA(newR, newG, newB, newA); 
    }

    private int clip(double num) {
        if(num <= 0) {
             return 0;
        }
        if(num >= 255) {
            return 255;
        }
        return (int)num;
    }
}
