package org.kllbff.magic.parsers.xml;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.kllbff.magic.exceptions.ParsingException;
import org.kllbff.magic.info.ActivityInfo;
import org.kllbff.magic.info.ApplicationInfo;
import org.kllbff.magic.res.Resources;
import org.xmlpull.v1.XmlPullParserException;

public class XmlManifestParser extends XmlAbstractParser<ApplicationInfo> {

    public XmlManifestParser(Resources resources) throws XmlPullParserException {
        super(resources);
    }

    @Override
    public ApplicationInfo parseResource(Reader reader) throws IOException, XmlPullParserException {
        xpp.setInput(reader);
        if(!nextTag(null).equals("application")) {
            throw new ParsingException("Application manifest must be started with <application> tag");
        }
        
        checkAttr("name", "application info");
        String name = getAttr("name", null);
        
        String description = getAttr("description", "A some application by Magic Framework");
        String version = getAttr("version", "1.0");
        String versionCode = getAttr("versionCode", version);
        
        checkAttr("package", "application info");
        String packageName = getAttr("package", null);
        
        checkAttr("theme", "application info");
        String themeName = getAttr("theme", null);
        
        boolean launcherFound = false;
        
        List<ActivityInfo> activities = new ArrayList<>();
        String tag;
        while((tag = nextTag("application")) != null) {
            if(!tag.equals("activity")) {
                throw new ParsingException("<application> tag can contains only <activity> tags");
            }
            
            checkAttr("class", "activity for application");
            String className = getAttr("class", null);
            
            checkAttr("title", "activity for application");
            String title = getAttr("title", null);
            
            String theme = getAttr("theme", themeName);
            String launcher = getAttr("launcher", "false");
            
            if(launcher.equals("true")) {
                if(launcherFound) {
                    throw new ParsingException("Failed to parse manifest: found second launcher activity, but can exists only one");
                }
                launcherFound = true;
            }
            
            activities.add(new ActivityInfo(className, title, theme, launcher.equals("true")));
        }
        
        if(!launcherFound) {
            throw new ParsingException("Failed to parse manifest: none launcher was found");
        }
        
        if(activities.size() == 0) {
            throw new ParsingException("None activity was found!");
        }
        
        return new ApplicationInfo(name, description, version, versionCode, packageName, themeName, activities.toArray(new ActivityInfo[0]));
    }

}
