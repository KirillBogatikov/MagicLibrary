package org.kllbff.magic.app;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.kllbff.magic.exceptions.UnknownResourceTypeException;
import org.kllbff.magic.info.ActivityInfo;
import org.kllbff.magic.info.ApplicationInfo;
import org.kllbff.magic.parsers.ParsersPool;
import org.kllbff.magic.parsers.json.JsonManifestParser;
import org.kllbff.magic.parsers.json.JsonStylesParser;
import org.kllbff.magic.parsers.json.JsonValuesParser;
import org.kllbff.magic.parsers.xml.XmlManifestParser;
import org.kllbff.magic.parsers.xml.XmlStylesParser;
import org.kllbff.magic.parsers.xml.XmlValuesParser;
import org.kllbff.magic.res.AccessProvider;
import org.kllbff.magic.res.Resources;
import org.kllbff.magic.styling.AttributeSet;
import org.kllbff.magic.styling.Theme;
import org.xmlpull.v1.XmlPullParserException;

public final class Application {
    public static Application currentApplication;
    
    public static Application current() {
        return currentApplication;
    }

    public static final void main(String[] args) {
        currentApplication = new Application();
        while(currentApplication.handler == null) { }
        
        currentApplication.handler.post(() -> {
            currentApplication.onStart();
        });        
    }
    
    private Thread appThread;
    private ThreadHandler<String> handler;
    private AttributeSet basicAttributes;
    private List<Theme> themesList;
    private Resources resources;
    private ApplicationInfo info;
    
    private Activity launcherActivityInstance;

    public Application() {
        appThread = new Thread() {
            public void run() {
                setName("Magic application thread");
                
                handler = new ThreadHandler<String>() {
                    @Override
                    protected void onMessageReceived(String message) {
                        if(message.equals("kill")) {
                            Thread.yield();
                            this.thread = null;
                        }
                    }
                };
                
                boolean opened = false;
                try(AccessProvider provider = AccessProvider.getInstance()) {
                    opened = true;
                            
                    basicAttributes = loadAllAttributes(provider);
                    themesList = loadAllThemes(provider);
                    resources = new Resources(basicAttributes, null);
                    info = loadManifest(provider);
                    
                    for(Theme theme : themesList) {
                        if(theme.getName().equals(info.getThemeName())) {
                            resources.setTheme(theme);
                            break;
                        }
                    }
                    
                } catch(IOException ioe) {
                    if(opened) {
                        System.err.println("Failed to open stream for resource. Caused by ");
                    } else {
                        System.err.println("Failed to open AccessProvder to resources. Caused by ");
                    }
                    ioe.printStackTrace();
                    return;
                } catch(XmlPullParserException e) {
                    System.err.println("Failed to parse XML resource. Caused by ");
                    e.printStackTrace();
                    return;
                } catch (Exception e) {
                    System.err.println("Failed to parse resource. Caused by ");
                    e.printStackTrace();
                }
                
                handler.loop();
            }
        };
        appThread.start();
    }
    
    private AttributeSet loadAllAttributes(AccessProvider provider) throws Exception {
        AttributeSet basicAttributes = new AttributeSet();
        
        ParsersPool<AttributeSet> pool = new ParsersPool<>(new JsonValuesParser(), new XmlValuesParser());
        for(String path : provider.listFiles("values/")) {
            try(Reader reader = new InputStreamReader(provider.openStream(path))) {
                basicAttributes.join(pool.parseResource(reader, path));
            } catch(UnknownResourceTypeException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
        
        return basicAttributes;
    }
    
    private List<Theme> loadAllThemes(AccessProvider provider) throws Exception {
        List<Theme> themes = new ArrayList<Theme>();
        
        ParsersPool<List<Theme>> pool = new ParsersPool<>(new JsonStylesParser(), new XmlStylesParser());
        for(String path : provider.listFiles("styles/")) {
            try(Reader reader = new InputStreamReader(provider.openStream(path))) {
                themes.addAll(pool.parseResource(reader, path));
            } catch(UnknownResourceTypeException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
        
        return themes;
    }
    
    private ApplicationInfo loadManifest(AccessProvider provider) throws Exception {
        if(provider.exists("manifest.json")) {
            try(Reader reader = new InputStreamReader(provider.openStream("manifest.json"))) {
                return new JsonManifestParser(resources).parseResource(reader);
            }
        } else if(provider.exists("manifest.xml")) {
            try(Reader reader = new InputStreamReader(provider.openStream("manifest.xml"))) {
                return new XmlManifestParser(resources).parseResource(reader);
            }
        } 
        
        throw new Exception("Application manifest not found");
    }
    
    public Resources getResources() {
        return resources;
    }
    
    public ApplicationInfo getInfo() {
        return info;
    }
    
    public List<Theme> getThemes() {
        return themesList;
    }
    
    public void onStart() {
        for(ActivityInfo activity : info.getActivities()) {
            if(activity.isLauncher()) {
                String clazzName = info.getPackageName() + "." + activity.getClassName();
                try {
                    @SuppressWarnings("unchecked")
                    Class<? extends Activity> clazz = (Class<? extends Activity>)Class.forName(clazzName);
                    Constructor<? extends Activity> constructor = clazz.getConstructor(Application.class, ActivityInfo.class);
                    launcherActivityInstance = constructor.newInstance(this, activity);
                    launcherActivityInstance.onStart();
                } catch (ReflectiveOperationException e) {
                    System.err.println("Failed to instantiate activity " + clazzName + ". Caused by: ");
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void onStop() {
        handler.postMessage("kill");
    }
}
