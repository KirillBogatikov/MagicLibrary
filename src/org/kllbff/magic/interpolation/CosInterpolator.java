package org.kllbff.magic.interpolation;

/**
 * <h3>Represents a cos interpolator</h3>
 * <p>Interpolator - an object that allows you to change the value in a given range, 
 *    depending on the value of the <i><b>t</b></i> - execution time, ranging from 0 to 1</p>
 * <p>Cos interpolator changes value not evenly: by using cos function:
 * <pre><code>
 *     t` = (1 - cos(t * 3.1415)) * corrector
 *     start * (1 - t`) + end * t`
 * </code></pre>
 * As default, corrector equals to {@value #DEFAULT_CORRECTOR} 
 * </p>
 * 
 * @author Kirill Bogatikov
 * @version 1.0
 * @since 1.0
 */
public class CosInterpolator implements Interpolator {
    public static final double DEFAULT_CORRECTOR = 0.45;
   
    @Override
    public double interpolate(double start, double end, double t1) {
        t1 = (1 - Math.cos(t1 * Math.PI)) * DEFAULT_CORRECTOR;
        
        double t2 = (1 - t1);
        return start * t2 + end * t1;
    }
}
