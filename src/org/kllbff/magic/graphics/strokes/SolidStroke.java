package org.kllbff.magic.graphics.strokes;

import org.kllbff.magic.graphics.color.Color;

public class SolidStroke extends Stroke {

    public SolidStroke(long color, int width, CapType cap, JoinType join) {
        super(color, width, cap, join);
    }
    
    public SolidStroke(long color, int width) {
        this(color, width, CapType.ROUND, JoinType.ROUND);
    }
    
    public SolidStroke(int width) {
        this(Color.WHITE, width);
    }

    @Override
    public float[] getDrawingPattern() {
        return new float[]{ };
    }

}
