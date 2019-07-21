package org.kllbff.magic.app;

import org.kllbff.magic.concurrency.GUIThread;
import org.kllbff.magic.concurrency.ThreadHandler;
import org.kllbff.magic.hardware.DisplayManager;
import org.kllbff.magic.hardware.WindowManager;
import org.kllbff.magic.info.ActivityInfo;
import org.kllbff.magic.res.Bundle;
import org.kllbff.magic.res.Resources;
import org.kllbff.magic.view.Window;

public abstract class Activity {
    private Application application;
    private ActivityInfo info;
    private Window window;

    public Activity(Application application, ActivityInfo info) {
        this.application = application;
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
        if(info.isLauncher()) {
            application.onStop();
        }
    }
    
    public final Window getWindow() {
        return window;
    }
}
