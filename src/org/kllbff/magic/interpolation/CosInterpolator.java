package org.kllbff.magic.interpolation;

public class CosInterpolator implements Interpolator {
   
    @Override
    public double interpolate(double start, double end, double t1) {
        t1 = (1 - Math.cos(t1 * Math.PI)) * 0.45;
        
        double t2 = (1 - t1);
        return start * t2 + end * t1;
    }
}
