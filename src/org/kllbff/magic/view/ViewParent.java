package org.kllbff.magic.view;

import java.util.Collection;

public interface ViewParent {
    public void addView(View child);
    
    public void addView(View child, int index);
    
    public void addViews(View... children);
    
    public void addViews(Collection<View> children);
    
    public View getViewAt(int x, int y);
    
    public View getView(int index);
    
    public int getViewsCount();
    
    public void removeView(View view);
}
