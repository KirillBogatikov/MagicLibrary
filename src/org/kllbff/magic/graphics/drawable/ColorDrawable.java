package org.kllbff.magic.graphics.drawable;

import org.kllbff.magic.graphics.Canvas;
import org.kllbff.magic.graphics.Paint;

public class ColorDrawable extends Drawable {
    private long color;
    private int x, y, w, h;
    
    public ColorDrawable(long color) {
        this.color = color;
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
    }
    
    @Override
    public void draw(Canvas canvas) {
        Paint origin = canvas.getPaint();
        Paint my = new Paint(color, Paint.Type.FILL);
        canvas.setPaint(my);
        canvas.drawRectangle(x, y, w, h);
        canvas.setPaint(origin);
    }
}
