package org.kllbff.magic.graphics.drawable;

import java.util.Arrays;
import java.util.List;

import org.kllbff.magic.graphics.Canvas;

public class LayerDrawable implements Drawable {
    protected int current;
    protected List<Drawable> layers;
    
    public LayerDrawable(Drawable... layers) {
        this.layers = Arrays.asList(layers);
        current = 0;
    }
    
    public int getCount() {
        return layers.size();
    }
    
    public int getCurrent() {
        return current;
    }
    
    public void setCurrent(int current) {
        this.current = current;
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Drawable> T get(int index) {
        return (T)layers.get(index);
    }

    @Override
    public void setBounds(int x, int y, int w, int h) {
        for(Drawable layer : layers) {
            layer.setBounds(x, y, w, h);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        layers.get(current).draw(canvas);
    }

}
