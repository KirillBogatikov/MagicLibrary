package org.kllbff.magic.hardware;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import org.kllbff.magic.app.Log;
import org.kllbff.magic.view.Window;

public class WindowManager {
    private static WindowManager instance;
    
    public static WindowManager getInstance() {
        if(instance == null) {
            Log.logger().i("WindowManager", "First call for singleton");
            instance = new WindowManager();
        }
        return instance;
    }
    
    private List<Window> registeredWindows;
    private int borderWidth, titleBarHeight;
    
    private WindowManager() {
        registeredWindows = new ArrayList<>();

        Log.logger().i("WindowManager", "Calculating system's border and title bar sizes");
        Frame frame = new Frame();
        frame.setBounds(0, 0, 100, 100);
        Button tester = new Button();
        tester.setPreferredSize(new Dimension(100, 100));
        frame.add(tester);
        frame.setLocation(-150, -150);
        frame.setVisible(true);

        Log.logger().i("WindowManager", "Wait for showing window and painting tester button");
        while(tester.getWidth() <= 0) { }
        Log.logger().i("WindowManager", "Sizes calcuated, disposing window and locked resources");
        frame.dispose();
        
        Dimension dimension = tester.getSize();
        borderWidth = Math.abs(100 - dimension.width) / 2;
        titleBarHeight = Math.abs(100 - dimension.height - borderWidth);
    }
    
    public int getTitleBarHeight() {
        return titleBarHeight;
    }
    
    public int getBorderWidth() {
        return borderWidth;
    }

    public void register(Window window) {
        registeredWindows.add(window);
        Log.logger().i("WindowManager", "Registered new window. Now app has " + registeredWindows.size() + " windows");
    }
    
    public void unregister(Window window) {
        registeredWindows.remove(window);
        Log.logger().i("WindowManager", "Unregistered exists window. Now app has " + registeredWindows.size() + " windows");
    }
    
    public Window fromAWT(java.awt.Window awt) {
        for(Window win : registeredWindows) {
            if(win.toAWT().equals(awt)) {
                return win;
            }
        }
        return null;
    }
}
