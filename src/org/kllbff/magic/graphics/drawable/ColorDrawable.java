package org.kllbff.magic.graphics.drawable;

import org.kllbff.magic.graphics.Canvas;
import org.kllbff.magic.graphics.Paint;

public class ColorDrawable extends Drawable {
    private Paint myPaint;
    private int x, y, w, h;
    
    public ColorDrawable(long color) {
        this.myPaint = new Paint(color, Paint.FILL);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
    }
    
    public void setColor(long color) {
        this.myPaint.setColor(color);
    }
    
    public long getColor() {
        return myPaint.getColor();
    }
    
    @Override
    public void draw(Canvas canvas) {
        Paint origin = canvas.getPaint();
        canvas.setPaint(myPaint);
        canvas.drawRectangle(x, y, w, h);
        canvas.setPaint(origin);
    }
}
