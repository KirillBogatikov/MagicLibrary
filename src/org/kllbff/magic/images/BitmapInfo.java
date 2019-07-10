package org.kllbff.magic.images;

import java.util.List;

/**
 * <h3>Represents inforamtion about image file or image stream</h3>
 * 
 * @author Kirill Bogatikov
 * @version 1.0
 * @since 1.0
 */
public final class BitmapInfo {
    private int width, height, depth;
    private List<String> comments;
    private StorageFormats format;
    
    /**
     * Initialize fields of instance by given values
     * 
     * @param width width of image
     * @param height height of image
     * @param depth depth of image
     * @param comments list of text comments
     * @param format one of values from StorageFormats enum
     */
    protected BitmapInfo(int width, int height, int depth, List<String> comments, StorageFormats format) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.comments = comments;
        this.format = format;
    }
    
    /**
     * Returns width of image
     * 
     * @return image's width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns height of image
     * 
     * @return image's height
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Returns bit depth of image
     * 
     * @return image's bit depth
     */
    public int getDepth() {
        return depth;
    }
    
    /**
     * Returns list of text comments saved in specieal chunk of image file
     * 
     * @return list of image's text comments
     */
    public List<String> getComments() {
        return comments;
    }
    
    /**
     * Returns format of image
     * 
     * @return one of values from StorageFormats enum
     */
    public StorageFormats getFormat() {
        return format;
    }
    
    public String toString() {
        return width + "x" + height + ":" + depth + ", " + format;
    }
}
