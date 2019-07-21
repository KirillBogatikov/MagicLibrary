package org.kllbff.magic.info;

import org.kllbff.magic.app.Log;

public class ActivityInfo {
    private String className, title, themeName;
    private boolean launcher;
    
    public ActivityInfo(String className, String title, String themeName, boolean launcher) {
        Log.logger().i("ActivityInfo", "Parsed activity info: " + className + ".class, " + title + ", " + themeName + ", " + (launcher ? "launcher" : ""));
        
        this.className = className;
        this.title = title;
        this.themeName = themeName;
        this.launcher = launcher;
    }

    public String getClassName() {
        return className;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getThemeName() {
        return themeName;
    }
    
    public boolean isLauncher() {
        return launcher;
    }
}
