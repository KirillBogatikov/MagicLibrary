package org.kllbff.magic.graphics.drawable;

import org.kllbff.magic.geometry.Point;
import org.kllbff.magic.graphics.Canvas;
import org.kllbff.magic.images.Bitmap;
import org.kllbff.magic.interpolation.ColorInterpolator;
import org.kllbff.magic.interpolation.Interpolator;
import org.kllbff.magic.interpolation.LinearInterpolator;

public class LinearGradient extends Drawable {
    private ColorInterpolator colorInterpolator;
    private Bitmap bitmap;
    private double angle;
    private int x, y, width, height;
    
    public LinearGradient(long start, long end, double angle, Interpolator interpolator) {
        this.angle = angle;
        this.colorInterpolator = new ColorInterpolator(start, end, interpolator == null ? new LinearInterpolator() : interpolator);
    }
    
    public LinearGradient(long start, long end) {
        this(start, end, 0.0, null);
    }
    
    private double AB;
    private double x1, x2, y1, y2;

    @Override
    public void setBounds(int x, int y, int width, int height) {
        bitmap = new Bitmap(width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
        double size = Math.max(width, height);
        Point center = new Point(size / 2, size / 2);
        
        Point a = new Point(0, size / 2);
        a.rotateBy(-angle, center);
        x1 = a.getX() * (width / size);
        y1 = a.getY() * (height / size);
        
        Point b = new Point(size, size / 2);
        b.rotateBy(-angle, center);
        x2 = b.getX() * (width / size);
        y2 = b.getY() * (height / size);  
        
        this.AB = ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
    
    public long getStartColor() {
        return colorInterpolator.getStartValue();
    }
    
    public long getEndColor() {
        return colorInterpolator.getEndValue(); 
    }
    
    @Override
    public void draw(Canvas canvas) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double t = ((x - x1)*(x2 - x1) + (y - y1)*(y2 - y1)) / AB;
                bitmap.setPixel(x, y, colorInterpolator.interpolate(t));
            }
        }

        canvas.drawBitmap(x, y, bitmap);
        bitmap = null;
    }
}
