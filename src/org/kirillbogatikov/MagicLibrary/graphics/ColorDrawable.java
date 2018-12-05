package org.kirillbogatikov.MagicLibrary.graphics;

public class ColorDrawable extends Drawable {
    private int color;
    
    public int getWidth() {
        return 1;
    }

    public int getHeight() {
        return 1;
    }
    
    public int getColor() {
        return color;
    }

    public void draw(Canvas canvas) {
        /**
         * canvas.fillRect(color, 0, 0, canvas.getWidth(), canvas.getHeight());
         */
    }

}
