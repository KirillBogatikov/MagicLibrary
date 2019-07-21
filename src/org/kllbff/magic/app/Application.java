package org.kllbff.magic.app;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.kllbff.magic.concurrency.GUIThread;
import org.kllbff.magic.concurrency.ThreadHandler;
import org.kllbff.magic.exceptions.UnknownResourceTypeException;
import org.kllbff.magic.hardware.WindowManager;
import org.kllbff.magic.info.ActivityInfo;
import org.kllbff.magic.info.ApplicationInfo;
import org.kllbff.magic.parsers.ParsersPool;
import org.kllbff.magic.parsers.json.JsonLoggingParser;
import org.kllbff.magic.parsers.json.JsonManifestParser;
import org.kllbff.magic.parsers.json.JsonStylesParser;
import org.kllbff.magic.parsers.json.JsonValuesParser;
import org.kllbff.magic.parsers.xml.XmlLoggingParser;
import org.kllbff.magic.parsers.xml.XmlManifestParser;
import org.kllbff.magic.parsers.xml.XmlStylesParser;
import org.kllbff.magic.parsers.xml.XmlValuesParser;
import org.kllbff.magic.res.AccessProvider;
import org.kllbff.magic.res.Resources;
import org.kllbff.magic.styling.Attribute;
import org.kllbff.magic.styling.AttributeSet;
import org.kllbff.magic.styling.AttributeType;
import org.kllbff.magic.styling.Theme;
import org.xmlpull.v1.XmlPullParserException;

public final class Application {
    public static Application currentApplication;
    
    public static final void main(String[] args) {
        Log.logger().i("Application", "Starting application...");
        currentApplication = new Application();   
    }
    
    private ApplicationInfo applicationInfo;
    private List<Theme> loadedThemes;
    private Resources resources;
    private WindowManager windowManager;
    private ThreadHandler<String> threadHandler;
    private Activity launcherActivityInstance;

    public Application() {
        Log.logger().i("Application", "Changing Thread settings...");
        Thread.currentThread().setName("Magic application main thread");
        
        windowManager = WindowManager.getInstance();
        Log.logger().i("Application", "WindowManager created");
        
        boolean opened = false;
        Log.logger().i("Application", "Loading resources");
        try(AccessProvider provider = AccessProvider.getInstance()) {
            Log.logger().i("Application", "AccessProvider instantiated");
            opened = true;
            
            Map<String, PrintStream> config = Collections.emptyMap();
            if(provider.exists("logging.json")) {
                JsonLoggingParser jlp = new JsonLoggingParser();
                config = jlp.parseResource(new InputStreamReader(provider.openStream("logging.json")));
            } else if(provider.exists("logging.xml")) {
                XmlLoggingParser xlp = new XmlLoggingParser();
                config = xlp.parseResource(new InputStreamReader(provider.openStream("logging.xml")));
            } 
            Log.logger().i("Application", "Logging configuration loaded");
            Log.logger().configurate(config);
                    
            Log.logger().i("Application", "Loading all defined values");
            AttributeSet basicAttributes = loadAllAttributes(provider);
            basicAttributes.add(new Attribute(AttributeType.DIMENSION, "window.border.width", windowManager.getBorderWidth()));
            basicAttributes.add(new Attribute(AttributeType.DIMENSION, "window.titlebar.height", windowManager.getTitleBarHeight()));

            Log.logger().i("Application", "Loading all defined themes");
            loadedThemes = loadAllThemes(provider);
            Log.logger().i("Application", "Instantiating Resources class");
            resources = new Resources(basicAttributes, null);
            Log.logger().i("Application", "Loading application manifest");
            applicationInfo = loadManifest(provider);
                        
            for(Theme theme : loadedThemes) {
                if(theme.getName().equals(applicationInfo.getThemeName())) {
                    Log.logger().i("Application", "App theme found. Applying aliases...");
                    resources.setTheme(theme);
                    break;
                }
            }
        } catch(IOException ioe) {
            if(opened) {
                Log.logger().e("Application", "Failed to open stream for resource. Caused by ", ioe);
            } else {
                Log.logger().e("Application", "Failed to open AccessProvder to resources. Caused by ", ioe);
            }
            onStop();
            return;
        } catch(XmlPullParserException e) {
            Log.logger().e("Application", "Failed to parse XML resource. Caused by ", e);
            onStop();
            return;
        } catch (Exception e) {
            Log.logger().e("Application", "Failed to parse resource. Caused by ", e);
            onStop();
            return;
        }
        
        onStart();
    }

    private AttributeSet loadAllAttributes(AccessProvider provider) throws Exception {
        AttributeSet basicAttributes = new AttributeSet();
        
        ParsersPool<AttributeSet> pool = new ParsersPool<>(new JsonValuesParser(), new XmlValuesParser());
        for(String path : provider.listFiles("values/")) {
            Log.logger().i("Application", "Opening next resource: " + path);
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
            Log.logger().i("Application", "Opening next resource: " + path);
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
            Log.logger().i("Application", "Found JSON manifest");
            try(Reader reader = new InputStreamReader(provider.openStream("manifest.json"))) {
                return new JsonManifestParser(resources).parseResource(reader);
            }
        } else if(provider.exists("manifest.xml")) {
            Log.logger().i("Application", "Found XML manifest");
            try(Reader reader = new InputStreamReader(provider.openStream("manifest.xml"))) {
                return new XmlManifestParser(resources).parseResource(reader);
            }
        } 
        
        throw new Exception("Application manifest not found");
    }

    public ApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }

    public List<Theme> getLoadedThemes() {
        return loadedThemes;
    }

    public Resources getResources() {
        return resources;
    }

    public WindowManager getWindowManager() {
        return windowManager;
    }

    public ThreadHandler<String> getThreadHandler() {
        return threadHandler;
    }
    
    public void onStart() {
        Log.logger().i("Application", "Application is ready, search for launcher activity");
        for(ActivityInfo activity : applicationInfo.getActivities()) {
            if(activity.isLauncher()) {
                Log.logger().i("Application", "Launcher found, instantiating...");
                String clazzName = applicationInfo.getPackageName() + "." + activity.getClassName();
                try {
                    @SuppressWarnings("unchecked")
                    Class<? extends Activity> clazz = (Class<? extends Activity>)Class.forName(clazzName);
                    Constructor<? extends Activity> constructor = clazz.getConstructor(Application.class, ActivityInfo.class);
                    launcherActivityInstance = constructor.newInstance(this, activity);
                    launcherActivityInstance.onStart();
                } catch (ReflectiveOperationException e) {
                    Log.logger().e("Application", "Failed to instantiate activity " + clazzName + ". Caused by: ", e);
                }
            }
        }
    }
    
    public void onStop() {
        Log.logger().i("Application", "Stopping application, killing GUI thread");
        GUIThread.getHandlerInstance().postMessage("kill");
    }
}
