package org.kllbff.magic.graphics.drawable;

import org.kllbff.magic.geometry.Point;
import org.kllbff.magic.graphics.Canvas;
import org.kllbff.magic.images.Bitmap;
import org.kllbff.magic.interpolation.ColorInterpolator;
import org.kllbff.magic.interpolation.Interpolator;
import org.kllbff.magic.interpolation.LinearInterpolator;

public class RadialGradient implements Drawable {
    private ColorInterpolator colorInterpolator;
    private Bitmap bitmap;
    private int x, y, width, height;
    
    public RadialGradient(long start, long end, Interpolator interpolator) {
        this.colorInterpolator = new ColorInterpolator(start, end, interpolator == null ? new LinearInterpolator() : interpolator);
    }
    
    @Override
    public void setBounds(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        
        bitmap = new Bitmap(w, h);
    }

    @Override
    public void draw(Canvas canvas) {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                double t = new Point(width / 2, height / 2).distanceTo(x, y);
                long color = colorInterpolator.interpolate(t / width * 2.075);
                bitmap.setPixel(x, y, color);
            }
        }
        
        canvas.drawBitmap(x, y, bitmap);
        bitmap = null;
    }

}
