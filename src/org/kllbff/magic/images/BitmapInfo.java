package org.kllbff.magic.images;

import java.util.List;

public final class BitmapInfo {
    private int width, height, depth;
    private List<String> comments;
    private StorageFormats format;
    
    protected BitmapInfo(int width, int height, int depth, List<String> comments, StorageFormats format) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.comments = comments;
        this.format = format;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getDepth() {
        return depth;
    }
    
    public List<String> getComments() {
        return comments;
    }
    
    public StorageFormats getFormat() {
        return format;
    }
    
    public String toString() {
        return width + "x" + height + ":" + depth + ", " + format;
    }
}
