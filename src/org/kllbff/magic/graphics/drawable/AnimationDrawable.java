package org.kllbff.magic.graphics.drawable;

import java.util.Arrays;
import java.util.List;

import org.kllbff.magic.graphics.Canvas;

public class AnimationDrawable extends LayerDrawable {
    protected List<Integer> durations;
    
    public AnimationDrawable(Drawable[] frames, Integer[] durations) {
        super(frames);
        this.durations = Arrays.asList(durations);
    }

    public void add(Drawable frame, int duration) {
        layers.add(frame);
        durations.add(duration);
    }
    
    public void remove(Drawable frame) {
        int index = layers.indexOf(frame);
        if(index > -1) {
            layers.remove(index);
            durations.remove(index);
        }
    }
    
    public int getCurrentDuration() {
        return durations.get(current);
    }
    
    public int getDuration(int index) {
        return durations.get(index);
    }
    
    @Override
    public void draw(Canvas canvas) {
        layers.get(current).draw(canvas);
    }

}
