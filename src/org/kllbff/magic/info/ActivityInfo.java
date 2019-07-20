package org.kllbff.magic.info;

public class ActivityInfo {
    private String className, title, themeName;
    private boolean launcher;
    
    public ActivityInfo(String className, String title, String themeName, boolean launcher) {
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
