package org.kllbff.magic.interpolation;

/**
 * <h3>Represents a linear interpolator</h3>
 * <p>Interpolator - an object that allows you to change the value in a given range, 
 *    depending on the value of the <i><b>t</b></i> - execution time, ranging from 0 to 1</p>
 * <p>Linear interpolator changes value evenly: by using linear function:
 * <pre><code>
 *     start * (1 - t) + end * t
 * </code></pre>
 * </p>
 * 
 * @author Kirill Bogatikov
 * @version 1.0
 * @since 1.0
 */
public class LinearInterpolator implements Interpolator {
    
    public double interpolate(double start, double end, double t) {
        double t2 = 1 - t;
        return start * t2 + end * t;
    }
}
