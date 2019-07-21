package org.kllbff.magic.app;

import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Log {
    public static final int PID;
    
    static {
        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        String jvmName = bean.getName();
        PID = Integer.valueOf(jvmName.split("@")[0]);
    }
    
    private static Log log;
    
    public static Log logger() {
        if(log == null) {
            log = new Log();
        }
        return log;
    }
    
    private String timestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(new Date(System.currentTimeMillis()));
    }
    
    private String defaultTag = "org.kllbff.logging";
    
    public void changeDefaultTag(String tag) {
        this.defaultTag = tag;
    }
    
    private PrintStream infoStream;
    private PrintStream debugStream;
    private PrintStream errorStream;
    private PrintStream warnStream;
    
    public void configurate(Map<String, PrintStream> configuration) {
        infoStream = configuration.get("info");
        if(infoStream == null) {
            infoStream = System.out;
        }
        
        debugStream = configuration.get("debug");
        if(debugStream == null) {
            debugStream = System.out;
        }
        
        errorStream = configuration.get("error");
        if(errorStream == null) {
            errorStream = System.out;
        }
        
        warnStream = configuration.get("warn");
        if(warnStream == null) {
            warnStream = System.out;
        }
        
        i("Logger", "logging configuration applied");
    }
    
    private Log() {
        infoStream = debugStream = errorStream = warnStream = System.out;
    }

    public void i(String message) {
        i(defaultTag, message);
    }
    
    public void i(String tag, String message) {
        i(tag, message, null);
    }
    
    public void i(String tag, String message, Throwable t) {
        infoStream.printf("[%s][%s] %s %d %s%n", "INFO", tag, timestamp(), PID, message);
        if(t != null) {
            t.printStackTrace(infoStream);
        }
    }
    
    public void d(String message) {
        d(defaultTag, message);
    }
    
    public void d(String tag, String message) {
        d(tag, message, null);
    }
    
    public void d(String tag, String message, Throwable t) {
        debugStream.printf("[%s][%s] %s %d %s%n", "DEBUG", tag, timestamp(), PID, message);
        if(t != null) {
            t.printStackTrace(debugStream);
        }
    }
    
    public void e(String message) {
        e(defaultTag, message);
    }
    
    public void e(String tag, String message) {
        e(tag, message, null);
    }
    
    public void e(String tag, String message, Throwable t) {
        errorStream.printf("[%s][%s] %s %d %s%n", "ERROR", tag, timestamp(), PID, message);
        if(t != null) {
            t.printStackTrace(errorStream);
        }
    }
    
    public void w(String message) {
        w(defaultTag, message);
    }
    
    public void w(String tag, String message) {
        w(tag, message, null);
    }
    
    public void w(String tag, String message, Throwable t) {
        warnStream.printf("[%s][%s] %s %d %s%n", "WARN", tag, timestamp(), PID, message);
        if(t != null) {
            t.printStackTrace(warnStream);
        }
    }
}
