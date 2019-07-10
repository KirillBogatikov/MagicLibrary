package org.kllbff.magic.interpolation;

/**
 * <h3>Represents an abstract interpolator</h3>
 * <p>Interpolator - an object that allows you to change the value in a given range, 
 *    depending on the value of the <i><b>t</b></i> - execution time, ranging from 0 to 1</p>
 * 
 * @author Kirill Bogatikov
 * @version 1.0
 * @since 1.0
 */
public interface Interpolator {
    /**
     * Returns value, calculated by interpolation <i>start</i> to <i>end</i> at specified <t><b>t</b></t> moment
     * 
     * @param start start value, any value
     * @param end end value, any value
     * @param t time moment, between 0...1
     * @return value, calculated by interpolation, must be between start and end
     */
    public double interpolate(double start, double end, double t);
}
