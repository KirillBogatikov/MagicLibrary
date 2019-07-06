package org.kllbff.magic.graphics.strokes;

import org.kllbff.magic.graphics.color.Color;

public class DashedStroke extends Stroke {
    private int[] dashWidth, dashSpace;
    
    public DashedStroke(int width, long color, CapType cap, JoinType join) {
        super(width, color, cap, join);
        
        dashWidth = new int[]{ width * 2 };
        dashSpace = new int[]{ width * 2 };
    }
    
    public DashedStroke(int width, long color) {
        this(width, color, CapType.ROUND, JoinType.ROUND);
    }
    
    public DashedStroke(int width) {
        this(width, Color.WHITE);
    }
    
    public void setDashWidth(int... dashWidth) {
        if(dashWidth == null) {
            throw new NullPointerException("Dash width array is null");
        }
        if(dashWidth.length == 0) {
            throw new RuntimeException("Length of dash width array must be greater than zero");
        }
        
        this.dashWidth = dashWidth;
    }
    
    public int getDashWidth(int index) {
        return dashWidth[index];
    }
    
    public void setDashSpace(int... dashSpace) {
        if(dashSpace == null) {
            throw new NullPointerException("Given dash space is null");
        }
        if(dashSpace.length == 0) {
            throw new RuntimeException("Length of dash space array must be greater than zero");
        }
        
        this.dashSpace = dashSpace;
    }
    
    public int getDashSpace(int index) {
        return dashSpace[index];
    }

    @Override
    public float[] getDrawingPattern() {
        int length = Math.max(dashWidth.length, dashSpace.length) * 2;
        float[] pattern = new float[length];
        
        int i = 0, w = 0, s = 0;
        while(i < length) {
            pattern[i++] = dashWidth[w];
            pattern[i++] = dashSpace[s];
            
            if(++w == dashWidth.length) {
                w = 0;
            }
            if(++s == dashSpace.length) {
                s = 0;
            }
        }
        return pattern;
    }
}
