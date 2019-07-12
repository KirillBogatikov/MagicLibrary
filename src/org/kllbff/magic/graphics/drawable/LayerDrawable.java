package org.kllbff.magic.graphics.drawable;

import java.util.ArrayList;
import java.util.List;

import org.kllbff.magic.graphics.Canvas;

public class LayerDrawable implements DrawableGroup {
    protected List<Drawable> layers;
    protected List<Integer[]> offsets;
    
    public LayerDrawable() {
        this.layers = new ArrayList<>();
        this.offsets = new ArrayList<>();
    }
    
    public int getCount() {
        return layers.size();
    }
    
    public int getX(int index) {
        return offsets.get(index)[0];
    }
    
    public int getY(int index) {
        return offsets.get(index)[1];
    }
    
    @Override
    public Drawable get(int index) {
        return layers.get(index);
    }
    
    public void add(Drawable layer, int x, int y) {
        layers.add(layer);
        offsets.add(new Integer[]{ x, y });
    }

    @Override
    public void setBounds(int x, int y, int w, int h) {
        for(int i = 0; i < layers.size(); i++) {
            Drawable layer = layers.get(i);
            Integer[] offset = offsets.get(i);
            layer.setBounds(x + offset[0], y + offset[1], w, h);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        for(Drawable layer : layers) {
            layer.draw(canvas);
        }
    }

}
