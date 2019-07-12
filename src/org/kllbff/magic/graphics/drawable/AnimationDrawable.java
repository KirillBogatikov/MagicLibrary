package org.kllbff.magic.graphics.drawable;

import java.util.ArrayList;
import java.util.List;

import org.kllbff.magic.graphics.Canvas;

public class AnimationDrawable implements DrawableGroup {
    protected int current;
    protected List<Drawable> frames;
    protected List<Integer> durations;
        
    public AnimationDrawable() {
        frames = new ArrayList<>();
        durations = new ArrayList<>();
    }
    
    public int getCurrent() {
        return current;
    }
    
    public void setCurrent(int current) {
        this.current = current;
    }

    @Override
    public Drawable get(int index) {
        return frames.get(index);
    }

    public void add(Drawable frame, int duration) {
        frames.add(frame);
        durations.add(duration);
    }
    
    public void remove(Drawable frame) {
        int index = frames.indexOf(frame);
        if(index > -1) {
            frames.remove(index);
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
    public void setBounds(int x, int y, int w, int h) {
        for(Drawable frame : frames) {
            frame.setBounds(x, y, w, h);
        }
    }
    
    @Override
    public void draw(Canvas canvas) {
        frames.get(current).draw(canvas);
    }

}
