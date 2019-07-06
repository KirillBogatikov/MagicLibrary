package org.kllbff.magic.graphics.drawable;

import org.kllbff.magic.graphics.Canvas;
import org.kllbff.magic.images.Bitmap;
import org.kllbff.magic.interpolation.ColorInterpolator;
import org.kllbff.magic.interpolation.CosInterpolator;
import org.kllbff.magic.math.PlanimetryValues;

public class GradientDrawable extends Drawable {
    private Bitmap bitmap;
    private long start, end;
    private double sin, cos;
    private int x, y, width, height;
    
    public GradientDrawable(long start, long end, double angle) {
        this.start = start;
        this.end = end;
        this.cos = PlanimetryValues.cos(angle);
        this.sin = PlanimetryValues.sin(angle);
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
        
        x1 = 0;
        y1 = 0;
        x2 = width;
        y2 = height;
        
        if(cos > sin) {
            y2 *= sin; 
        } else if(cos < sin) {
            x2 *= cos;
        }
        
        this.AB = ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
    
    @Override
    public void draw(Canvas canvas) {
        ColorInterpolator ci = new ColorInterpolator(start, end, new CosInterpolator());
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double t = ((x - x1)*(x2 - x1) + (y - y1)*(y2 - y1)) / AB;
                bitmap.setPixel(x, y, ci.interpolate(t));
            }
        }
        
        canvas.drawBitmap(x, y, bitmap);
    }
}
