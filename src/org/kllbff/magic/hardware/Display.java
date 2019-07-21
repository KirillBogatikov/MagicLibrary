package org.kllbff.magic.hardware;

import org.kllbff.magic.view.Window;

public abstract class Display {
    public abstract int getWidth();
    public abstract int getHeight();
    public abstract int getRefreshRate();
    public abstract int getBitDepth();
    public abstract int getX();
    public abstract int getY();
    public abstract int getDensity();
    public abstract boolean isDefault();
    public abstract int getToolbarWidth();
    public abstract int getToolbarHeight();
    public abstract Window getFullscreenWindow();
    public abstract boolean setFullscreenWindow(Window window);
}
