package org.kirillbogatikov.MagicLibrary.graphics;

import java.io.File;

public class BitmapDrawable extends Drawable {
    public static final int MODE_SCALE  = 0x2, 
                            MODE_REPEAT = 0x4, 
                            MODE_FILL_X = 0x8,
                            MODE_FILL_Y = 0x10;
    
    public Bitmap bitmap;
    public int drawingMode;
    
    public BitmapDrawable(Bitmap bmp) {
        bitmap = bmp;
        drawingMode = MODE_SCALE;
    }
    
    public BitmapDrawable(File file) {
        //this(new Bitmap(file));
    }
    
    public boolean isScalable() {
        return drawingMode == MODE_SCALE;
    }
    
    public boolean isRepeatable() {
        return (drawingMode & MODE_REPEAT) != 0;
    }
    
    public boolean setDrawingMode(int mode) {
        drawingMode = mode;
        return true;
    }
    
    public Bitmap getBitmap() {
        return bitmap;
    }
    
    public int getWidth() {
        return /*bitmap.getWidth();*/1;
    }

    public int getHeight() {
        return /*bitmap.getWidth();*/1;
    }

    public void draw(Canvas canvas) {
        /*if(drawingMode == MODE_SCALE) {
            int k = Math.max(canvas.getWidth(), canvas.getHeight()) - Math.min(bitmap.getWidth(), bitmap.getHeight());
            canvas.drawBitmap(bitmap, bitmap.getWidth() + k, bitmap.getHeight() + k);
            return;
        }
        
        if(drawingMode & MODE_FILL_X) {
            if((drawingMode & MODE_FILL_Y) == 0) {
                canvas.drawBitmap(bitmap, canvas.getWidth(), canvas.getHeight());
                return;
            }
            if((drawingMode & MODE_REPEAT) != 0) {
                canvas.drawBitmap(bitmap, canvas.getWidth(), bitmap.getHeight());
                return;
            }
                
            for(int y = 0; y < canvas.getHeight(); y+= bitmap.getHeight()) {
                canvas.drawBitmap(bitmap, 0, y, canvas.getWidth(), bitmap.getHeight());
            }
        }
        
        if(drawingMode & MODE_FILL_Y) {
            if((drawingMode & MODE_REPEAT) != 0) {
                canvas.drawBitmap(bitmap, bitmap.getWidth(), canvas.getHeight());
                return;
            }
                
            for(int x = 0; x < canvas.getWidth(); x+= bitmap.getWidth()) {
                canvas.drawBitmap(bitmap, x, 0, bitmap.getWidth(), canvas.getHeight());
            }
        }*/
    }
}
