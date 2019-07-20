package org.kllbff.magic.app;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

public class DisplayManager {
    private static DisplayManager currentInstance;
    
    public static DisplayManager getInstance() {
        if(currentInstance == null) {
            currentInstance = new DisplayManager();
        }
        return currentInstance;
    }
    
    private List<Display> cachedDisplaysList;
    private Display cachedDefaultDisplay;
    private Toolkit toolkit;
    private GraphicsEnvironment environment;
    
    private DisplayManager() {
        toolkit = Toolkit.getDefaultToolkit();
        environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    }

    public List<Display> getDisplays() {
        if(cachedDisplaysList == null) {
            cachedDisplaysList = new ArrayList<>();
        } else {
            cachedDisplaysList.clear();
        }
        List<Display> displays = new ArrayList<>();
        
        GraphicsDevice[] devices = environment.getScreenDevices();
        GraphicsDevice defScreen = environment.getDefaultScreenDevice();
        
        for(int i = 0; i < devices.length; i++) {
            if(devices[i].getType() != GraphicsDevice.TYPE_RASTER_SCREEN) {
                continue; //skip device
            }
            
            GraphicsDevice screen = devices[i];
            Display display = createDisplay(screen, screen.equals(defScreen));
            displays.add(display);
            cachedDisplaysList.add(display);
            
            if(display.isDefault()) {
                cachedDefaultDisplay = display;
            }
        }
        return displays;
    }
    
    public Display getDefaultDisplay() {
        if(cachedDefaultDisplay == null) {
            getDisplays();
        }
        return cachedDefaultDisplay;
    }
    
    public Display getDisplay(int x, int y) {
        if(cachedDisplaysList == null) {
            getDisplays();
        }
        
        for(Display display : cachedDisplaysList) {
            if(display.getX() <= x && (display.getX() + display.getWidth()) >= x) {
                if(display.getY() <= y && (display.getY() + display.getHeight()) >= y) {
                    return display;
                }
            }
        }
        
        return null;
    }
    
    private Display createDisplay(GraphicsDevice screen, boolean isDefault) {
        DisplayMode displayMode = screen.getDisplayMode();
        Rectangle bounds = screen.getDefaultConfiguration().getBounds();
        
        return new Display() {
            @Override
            public int getWidth() {
                return displayMode.getWidth();
            }

            @Override
            public int getHeight() {
                return displayMode.getHeight();
            }

            @Override
            public int getRefreshRate() {
                return displayMode.getRefreshRate();
            }

            @Override
            public int getBitDepth() {
                return displayMode.getBitDepth();
            }

            @Override
            public int getX() {
                return bounds.x;
            }

            @Override
            public int getY() {
                return bounds.y;
            }

            @Override
            public int getDensity() {
                return toolkit.getScreenResolution();
            }

            @Override
            public boolean isDefault() {
                return isDefault;
            }
        };
    }
}
