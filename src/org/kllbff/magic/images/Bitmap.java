package org.kllbff.magic.images;

import java.util.Arrays;

import org.kllbff.magic.exceptions.NotEnoughMemoryException;
import org.kllbff.magic.utils.MemoryAllocator;

public class Bitmap {
    private long[] pixels;
    private int width, height;
    
    protected Bitmap(long[] pixels, int width, int height) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
    }
    
    public Bitmap(int width, int height) throws NotEnoughMemoryException {
        this(MemoryAllocator.allocateLongArray(width * height), width, height);
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public long getPixel(int x, int y) {
        return pixels[y * width + x];
    }
    
    public void setPixel(int x, int y, long color) {
        pixels[y * width + x] = color;
    }
    
    public long[] grabPixels() {
        return pixels;
    }
    
    public Bitmap copy() throws NotEnoughMemoryException {
        return new Bitmap(Arrays.copyOf(pixels, width * height), width, height);
    }
}
