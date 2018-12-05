package org.kirillbogatikov.MagicLibrary.graphics;

import java.util.ArrayList;
import java.util.List;

public class DrawableList extends Drawable {
    private ArrayList<Drawable> levels;
    private int width, height;
    
    public DrawableList(Drawable... levels) {
        this.levels = new ArrayList<Drawable>();
        
        for(int i = 0; i < levels.length; i++) {
            this.levels.add(levels[i]);
        }
    }
    
    public DrawableList(List<Drawable> levels) {
        this.levels = new ArrayList<Drawable>();
        
        for(int i = 0; i < levels.size(); i++) {
            this.levels.add(levels.get(i));
        }
    }
    
    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
    
    public int indexOf(Drawable d) {
        return levels.indexOf(d);
    }
    
    public void add(Drawable d) {
        levels.add(d);
    }
    
    public void shift(Drawable d) {
        add(0, d);
    }
    
    public void add(int i, Drawable d) {
        levels.add(i, d);
    }
    
    public void remove(Drawable d) {
        remove(indexOf(d));
    }
    
    public void remove(int i) {
        levels.remove(i);
    }
    
    public void swap(int a, int b) {
        Drawable d = levels.get(a);
        levels.set(a, levels.get(b));
        levels.set(b, d);
    }
    
    public void swap(Drawable a, Drawable b) {
        swap(indexOf(a), indexOf(b));
    }

    @Override
    public void draw(Canvas canvas) {
        //width = canvas.getWidth();
        //height = canvas.getHeight();
        
        for(Drawable drawable : levels) {
            drawable.draw(canvas);
        }
    }
}
