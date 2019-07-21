package org.kllbff.magic.view;

import java.util.Collection;

import org.kllbff.magic.app.Activity;
import org.kllbff.magic.graphics.Canvas;

public abstract class ViewGroup extends View implements ViewParent {

    public ViewGroup(Activity activity) {
        super(activity);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T extends View> T findViewById(long id) {
        View view = super.findViewById(id);
        if(view == null) {
            for(int i = 0; i < getViewsCount(); i++) {
                view = getView(i);
                if(view.getId() == id) {
                    break;
                }
            }
        }
        return (T)view;
    }
    
    public abstract void addView(View child);
    
    public abstract void addView(View child, int index);
    
    public abstract void addViews(View... children);
    
    public abstract void addViews(Collection<View> children);
    
    public abstract View getViewAt(int x, int y);
    
    public abstract View getView(int index);
    
    public abstract int getViewsCount();
    
    public abstract void removeView(View view);
    
    public abstract void removeView(int index);

    @Override
    public abstract boolean isCheckable();
    
    @Override
    public abstract boolean isClickable();
    
    @Override
    public abstract View getTooltipView();

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
    
    @Override
    public abstract void redraw();
}
