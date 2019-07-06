package org.kllbff.magic.graphics.strokes;

import org.kllbff.magic.graphics.color.Color;

public class DottedStroke extends Stroke {
    private int[] spaces;

    public DottedStroke(int width, long color, CapType cap, JoinType join) {
        super(width, color, cap, join);
        
        spaces = new int[]{ width };
    }
    
    public DottedStroke(int width, long color) {
        this(width, color, CapType.BUTT, JoinType.BEVEL);
    }
    
    public DottedStroke(int width) {
        this(width, Color.WHITE);
    }
    
    public void setCapType(CapType cap) {
        throw new UnsupportedOperationException("Can not change cap type of dotted stroke");
    }
    
    public void setJoinType(JoinType join) {
        throw new UnsupportedOperationException("Can not change join type of dotted stroke");
    }
    
    public void setSpaces(int... spaces) {
        if(spaces == null) {
            throw new NullPointerException("Spaces array is null");
        }
        if(spaces.length == 0) {
            throw new RuntimeException("Length of spaces array must be greater than zero");
        }
        
        this.spaces = spaces;
    }
    
    @Override
    public float[] getDrawingPattern() {
        float[] pattern = new float[spaces.length * 2];
        for(int i = 0; i < pattern.length; i += 2) {
            pattern[i] = this.width;
            pattern[i + 1] = spaces[i / 2];
        }
        
        return pattern;
    }

}
