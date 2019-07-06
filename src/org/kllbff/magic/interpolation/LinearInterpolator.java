package org.kllbff.magic.interpolation;

public class LinearInterpolator implements Interpolator {
    
    public double interpolate(double start, double end, double t) {
        double t2 = 1 - t;
        return start * t2 + end * t;
    }
}
