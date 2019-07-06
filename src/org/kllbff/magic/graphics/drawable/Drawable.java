package org.kllbff.magic.graphics.drawable;

import org.kllbff.magic.graphics.Canvas;

public abstract class Drawable {
    public abstract void setBounds(int x, int y, int w, int h);
    public abstract void draw(Canvas canvas);
}
