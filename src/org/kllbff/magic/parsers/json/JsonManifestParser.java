package org.kllbff.magic.parsers.json;

import java.io.Reader;

import org.kllbff.magic.app.Log;
import org.kllbff.magic.exceptions.ParsingException;
import org.kllbff.magic.info.ActivityInfo;
import org.kllbff.magic.info.ApplicationInfo;
import org.kllbff.magic.res.Resources;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JsonManifestParser extends JsonAbstractParser<ApplicationInfo> {

    public JsonManifestParser(Resources resources) {
        super(resources);
    }

    @Override
    public ApplicationInfo parseResource(Reader reader) throws Exception {
        Log.logger().i("JsonManifest", "Parsing manifest started");
        JsonObject manifest = jp.parse(reader).getAsJsonArray().get(0).getAsJsonObject();
        
        checkProp(manifest, "name", "application info");
        String name = getProp(manifest, "name", null);
        
        String description = getProp(manifest, "description", "A some application by Magic Framework");
        String version = getProp(manifest, "version", "1.0");
        String versionCode = getProp(manifest, "versionCode", version);
        
        checkProp(manifest, "package", "application info");
        String packageName = getProp(manifest, "package", null);
        
        checkProp(manifest, "theme", "application info");
        String themeName = getProp(manifest, "theme", null);

        checkProp(manifest, "activities", "application info");
        JsonArray jsonArray = manifest.get("activities").getAsJsonArray();
        ActivityInfo[] activities = new ActivityInfo[jsonArray.size()];
        
        boolean launcherFound = false;
        
        for(int i = 0; i < jsonArray.size(); i++) {
            JsonObject activity = jsonArray.get(i).getAsJsonObject();
            
            checkProp(activity, "class", "activity for application");
            String className = getProp(activity, "class", null);
            
            checkProp(activity, "title", "activity for application");
            String title = getProp(activity, "title", null);
            
            String theme = getProp(activity, "theme", themeName);
            String launcher = getProp(activity, "launcher", "false");
            
            if(launcher.equals("true")) {
                if(launcherFound) {
                    throw new ParsingException("Failed to parse manifest: found second launcher activity, but can exists only one");
                }
                launcherFound = true;
            }
            activities[i] = new ActivityInfo(className, title, theme, launcher.equals("true"));
        }
        
        if(!launcherFound) {
            throw new ParsingException("Failed to parse manifest: none launcher was found");
        }
        
        return new ApplicationInfo(name, description, version, versionCode, packageName, themeName, activities);
    }

}
