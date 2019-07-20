package org.kllbff.magic.res;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class AccessProvider implements AutoCloseable {
    private static final String PROVIDER_CLASS = AccessProvider.class.getPackage().getName().replace(".", "/");
    private static AccessProvider instance;
    
    public static AccessProvider getInstance() throws IOException {
        if(instance == null) {
            instance = new AccessProvider();
        }
        return instance;
    }
    
    private ZipFile currentJar;
    
    private AccessProvider() throws IOException {
        URL url = AccessProvider.class.getClassLoader().getResource(PROVIDER_CLASS);
        String jarPath = url.toExternalForm();
        
        if(jarPath.startsWith("jar:")) {
            int start = "jar:file:/".length();
            int end = jarPath.indexOf("!");
            
            currentJar = new ZipFile(jarPath.substring(start, end));
        } else {
            currentJar = null;
        }
    }

    public List<String> listFiles(String path) throws FileNotFoundException {
        ArrayList<String> files = new ArrayList<>();
        
        if(currentJar == null) {
            File file = new File("res/" + path);
            
            if(!file.exists()) {
                throw new FileNotFoundException("Resource " + path + " not found");
            }
            
            File[] list = file.listFiles();
            for(File child : list) {
                files.add(child.getPath().replace("res[/\\\\]", ""));
            }
        } else {
            boolean notFound = true;
            Enumeration<? extends ZipEntry> entries = currentJar.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName(); 
                
                if(name.startsWith(path)) {
                    if(entry.isDirectory()) {
                        notFound = false;
                        continue;
                    }
                    
                    files.add(name);
                }
            }
            
            if(files.isEmpty() && notFound) {
                throw new FileNotFoundException("Resource " + path + " not found");
            }
        }
        
        return files;
    }
    
    public boolean exists(String name) {
        if(currentJar == null) {
            return new File("res/" + name).exists();
        }
        ZipEntry file = currentJar.getEntry(name);
        return file != null;
    }

    public InputStream openStream(String name) throws IOException {
        if(currentJar == null) {
            if(!name.matches("^res[/\\\\](.*)")) {
                name = "res/" + name;
            }
            return new FileInputStream(name); 
        }
        
        ZipEntry file = currentJar.getEntry(name);
        if(file == null) {
            throw new FileNotFoundException(name);
        }
        
        return currentJar.getInputStream(file);
    }
    
    @Override
    public void close() throws IOException {
        if(currentJar != null) {
            currentJar.close();
        }
    }
}
