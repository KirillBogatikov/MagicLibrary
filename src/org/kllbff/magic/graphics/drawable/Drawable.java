package org.kllbff.magic.graphics.drawable;

import org.kllbff.magic.graphics.Canvas;

public interface Drawable {
    public void setBounds(int x, int y, int w, int h);
    public void draw(Canvas canvas);
}
