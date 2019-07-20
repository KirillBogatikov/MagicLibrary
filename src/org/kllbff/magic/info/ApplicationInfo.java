package org.kllbff.magic.info;

public class ApplicationInfo {
    private String name;
    private String description;
    private String version;
    private String versionCode;
    private String packageName;
    private String themeName;
    private ActivityInfo[] activities;
    
    public ApplicationInfo(String name, String description, String version, 
                           String versionCode, String packageName, String themeName,
                           ActivityInfo[] activities) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.versionCode = versionCode;
        this.packageName = packageName;
        this.themeName = themeName;
        this.activities = activities;
    }

    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getVersion() {
        return version;
    }
    
    public String getVersionCode() {
        return versionCode;
    }
    
    public String getPackageName() {
        return packageName;
    }
    
    public String getThemeName() {
        return themeName;
    }
    
    public ActivityInfo[] getActivities() {
        return activities;
    }
}
