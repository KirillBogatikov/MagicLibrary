package org.kllbff.magic.graphics.strokes;

import org.kllbff.magic.graphics.color.Color;

public class SolidStroke extends Stroke {

    public SolidStroke(int width, long color, CapType cap, JoinType join) {
        super(width, color, cap, join);
    }
    
    public SolidStroke(int width, long color) {
        this(width, color, CapType.ROUND, JoinType.ROUND);
    }
    
    public SolidStroke(int width) {
        this(width, Color.WHITE);
    }

    @Override
    public float[] getDrawingPattern() {
        return new float[]{ };
    }

}
