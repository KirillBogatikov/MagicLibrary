package org.kllbff.magic.app;

import java.awt.Frame;
import java.awt.Window;

import org.kllbff.magic.info.ActivityInfo;
import org.kllbff.magic.res.Bundle;
import org.kllbff.magic.res.Resources;

public abstract class Activity {
    private Application application;
    private ActivityInfo info;
    private Frame window;

    public Activity(Application application, ActivityInfo info) {
        this.application = application;
        this.info = info;
        window = new Frame();
        window.setBounds(150, 150, 150, 150);
        window.setVisible(true);
    }
    
    public Resources getResources() {
        return application.getResources();
    }
    
    public void onStart() {
        
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
