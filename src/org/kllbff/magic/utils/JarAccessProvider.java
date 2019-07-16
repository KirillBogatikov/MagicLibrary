package org.kllbff.magic.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class JarAccessProvider implements AutoCloseable {
    private ZipFile currentJar;
        
    public JarAccessProvider(String packageName) throws IOException {
        URL url = JarAccessProvider.class.getClassLoader().getResource(packageName.replace(".", "/"));
        if(url == null) {
            throw new IllegalStateException("Cannot find package " + packageName + " in jar");
        }
        
        String jarPath = url.toExternalForm();
                
        if(!jarPath.startsWith("jar:")) {
            throw new IllegalStateException("Can be run only from jar archive");
        }
        
        int start = "jar:file:/".length();
        int end = jarPath.indexOf("!");
        
        currentJar = new ZipFile(jarPath.substring(start, end));
    }
    
    public List<String> listFiles(String path) {
        ArrayList<String> files = new ArrayList<>();
        Enumeration<? extends ZipEntry> entries = currentJar.entries();

        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
                continue;
            }
            
            String name = entry.getName(); 
            if(name.startsWith(path)) {
                files.add(name);
            }
        }
        
        return files;
    }

    public InputStream openStream(String name) throws IOException {
        ZipEntry file = currentJar.getEntry(name);
        if(file == null) {
            throw new FileNotFoundException(name);
        }
        
        return currentJar.getInputStream(file);
    }
    
    public void close() throws IOException {
        currentJar.close();
    }
}
