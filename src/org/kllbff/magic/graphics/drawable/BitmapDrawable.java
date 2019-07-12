package org.kllbff.magic.graphics.drawable;

import org.kllbff.magic.graphics.Canvas;
import org.kllbff.magic.images.Bitmap;
import org.kllbff.magic.images.BitmapFactory;

public class BitmapDrawable implements Drawable {
    private Bitmap bitmap;
    private int x, y, width, height;
    
    public BitmapDrawable(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(x, y, BitmapFactory.scale(bitmap, width, height, BitmapFactory.ScaleType.SMOOTH));
    }

}
