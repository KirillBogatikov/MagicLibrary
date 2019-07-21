package org.kllbff.magic.app;

import org.kllbff.magic.concurrency.GUIThread;
import org.kllbff.magic.concurrency.ThreadHandler;
import org.kllbff.magic.hardware.DisplayManager;
import org.kllbff.magic.hardware.WindowManager;
import org.kllbff.magic.info.ActivityInfo;
import org.kllbff.magic.res.Bundle;
import org.kllbff.magic.res.Resources;
import org.kllbff.magic.styling.Theme;
import org.kllbff.magic.view.Window;
import org.xmlpull.v1.XmlPullParserException;

public abstract class Activity {
    protected Application application;
    protected ActivityInfo info;
    protected Resources resources;
    private Window window;

    public Activity(Application application, ActivityInfo info) {
        this.application = application;
        
        Theme myTheme = null;
        for(Theme theme : application.getLoadedThemes()) {
            if(theme.getName().equals(info)) {
                myTheme = theme;
                break;
            }
        }
        
        try {
            this.resources = new Resources(application.getResources(), myTheme);
        } catch (XmlPullParserException e) {
            Log.logger().e("Activity", "Failed to apply theme " + info.getThemeName(), e);
            throw new RuntimeException(e);
        }
        this.info = info;
        window = new Window(this);
    }
    
    public Resources getResources() {
        return application.getResources();
    }
    
    public WindowManager getWindowManager() {
        return WindowManager.getInstance();
    }
    
    public DisplayManager getDisplayManager() {
        return DisplayManager.getInstance();
    }
    
    public ThreadHandler<String> getGUIThreadHandler() {
        return GUIThread.getHandlerInstance();
    }
    
    public void onStart() {
        window.show();
    }
    
    public void onResume(Bundle data) {
        
    }
    
    public void onPause(Bundle data) {
        
    }

    public void onStop() {
        window.dismiss();
        if(info.isLauncher()) {
            application.onStop();
        }
    }
    
    public final Window getWindow() {
        return window;
    }
}
