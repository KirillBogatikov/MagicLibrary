package org.kllbff.magic.interpolation;

import org.kllbff.magic.graphics.color.Color;
import org.kllbff.magic.graphics.color.ColorsConverter;

/**
 * <h3>Represents Color interpolator</h3>
 * <p>This class does not implement the Interpolator interface because the mandatory
 *    AAA method for all interpolators cannot be overridden here. Colors are specified
 *    as long integers and consist of components, typically RGB. Therefore, this class
 *    is an add-on to one of the available interpolators and is used to calculate color.
 *    For example, such interpolators are used in Gradients</p> 
 * 
 * @author Kirill Bogatikov
 * @version 1.0
 * @since 1.0
 */
public class ColorInterpolator {
    private Interpolator basic;
    private long start, end;
    private long a1, r1, g1, b1;
    private long a2, r2, g2, b2;
    
    /**
     * Constructs interpolator by given start and end colors and one of available
     * implementions of Interpolator
     * 
     * @param start start color
     * @param end end color
     * @param basic interpolator, used to calculate color components values
     */
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

    /**
     * Returns basic interpolator associated with this instance 
     * 
     * @return basic interpolator associated with this instance 
     */
    public Interpolator getBasicInterpolator() {
        return basic;
    }

    /**
     * Changes basic interpolator associated with this instance to specified
     *  
     * @param interpolator new interpolator which will be used to calculate color components values
     */
    public void setBasicInterpolator(Interpolator interpolator) {
        this.basic = interpolator;
    }
    
    /**
     * Returns start color of this interpolator
     * 
     * @return start color
     */
    public long getStartValue() {
        return start;
    }

    /**
     * Returns end color of this interpolator
     * 
     * @return end color
     */
    public long getEndValue() {
        return end;
    }

    /**
     * Changes start color to specified
     * 
     * @param start new start color
     */
    public void setStartValue(long start) {
        this.start = start;
        
        start = ColorsConverter.toRGB(start);
        a1 = Color.alpha(start);
        r1 = Color.red(start);
        g1 = Color.green(start);
        b1 = Color.blue(start);
    }

    /**
     * Changes end color to specified
     * 
     * @param end new end color
     */
    public void setEndValue(long end) {
        this.end = end;
        
        end = ColorsConverter.toRGB(end);
        a2 = Color.alpha(end);
        r2 = Color.red(end);
        g2 = Color.green(end);
        b2 = Color.blue(end);
    }
    
    /**
     * Returns new <b>RGBA</b> color, calculated by adding interpolated components
     * <p><b>Important!</b> This method calculates new color by decomposition start and end color to RGBA components. You
     *    can use start and end color of different {@link org.kllbff.magic.graphics.color.ColorSpace color spaces} but 
     *    this method always return RGBA color. You can convert it to target color space by {@link org.kllbff.magic.graphics.color.ColorsConverter color converter}</p>
     * 
     * @param t time moment
     * @return <b>RGBA</b> color, calculated by adding interpolated components
     */
    public long interpolate(double t) {
        int newA = clip(basic.interpolate(a1, a2, t));
        int newR = clip(basic.interpolate(r1, r2, t));
        int newG = clip(basic.interpolate(g1, g2, t));
        int newB = clip(basic.interpolate(b1, b2, t));

        return Color.rgba(newR, newG, newB, newA); 
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
