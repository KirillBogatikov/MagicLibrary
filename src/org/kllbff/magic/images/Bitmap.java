package org.kllbff.magic.images;

import java.util.Arrays;

import org.kllbff.magic.app.Log;
import org.kllbff.magic.exceptions.IncorrectColorException;
import org.kllbff.magic.exceptions.NotEnoughMemoryException;
import org.kllbff.magic.utils.MemoryAllocator;

/**
 * <h3>Represents a bitmap</h3>
 * <p>Consisting of a set of pixels of specified colors. The color of a pixel is
 *    defined by a long integer, like all other colors in the library. The image
 *    may contain pixels in different color spaces, but when you draw or save them,
 *    they will be reduced to one, usually sRGB.</p>
 * 
 * @author Kirill Bogatikov
 * @version 1.0
 * @since 1.0
 */
public class Bitmap {
    private long[] pixels;
    private int width, height;
    
    /**
     * Initializes fields by given pixels array and size
     * 
     * @param pixels colors of image's pixels
     * @param width width of image
     * @param height height of image
     */
    protected Bitmap(long[] pixels, int width, int height) {
        if(width * height < 4194304) {
            Log.logger().i("Allocated plain bitmap image " + width + "x" + height);
        } else {
            Log.logger().w("Allocated very big bitmap image " + width + "x" + height);
        }
        
        this.pixels = pixels;
        this.width = width;
        this.height = height;
    }
    
    /**
     * Tries to allocate array for pixels of image of given size
     * <p>If allocation was successfull, initializes this instance such as {@link #Bitmap(int, int)}</p>
     * 
     * @param width width of image
     * @param height height of image
     * @throws NotEnoughMemoryException if allocation failed
     */
    public Bitmap(int width, int height) throws NotEnoughMemoryException {
        this(MemoryAllocator.allocateLongArray(width * height), width, height);
    }
    
    /**
     * Returns image width
     * 
     * @return image width
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Returns image height
     * 
     * @return iamge height
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Returns color of specified pixel
     * 
     * @param x x-axis coordinate of pixel
     * @param y y-axis coordinate of pixel
     * @return color of specified pixel
     */
    public long getPixel(int x, int y) {
        if(x < 0 || y < 0) {
            throw new IncorrectColorException("Position of pixel must be positive numbers: " + x + ", " + y);
        }
        if(x >= getWidth() || y >= getHeight()) {
            throw new IncorrectColorException("Position of pixel must be less, than image size: " + x + ", " + y);
        }
        return pixels[y * width + x];
    }
    
    /**
     * Changes color at specified pixel to new value
     * 
     * @param x x-axis coordinate of pixel
     * @param y y-axis coordinate of pixel
     * @param color new color of specified pixel
     */
    public void setPixel(int x, int y, long color) {
        pixels[y * width + x] = color;
    }
    
    /**
     * Returns array of image's pixels
     * <p>This method returns 'native' represtation of image - linear long array of pixels</p>
     * 
     * @return array of image's pixels
     */
    public long[] grabPixels() {
        return pixels;
    }
    
    /**
     * Returns new instance of this bitmap
     * <p>Copy will contains all pixels from this instance</p>
     * 
     * @return new instance of this bitmap which contains same pixels and size
     * @throws NotEnoughMemoryException if memory allocation for new bitmap failed
     */
    public Bitmap copy() throws NotEnoughMemoryException {
        return new Bitmap(Arrays.copyOf(pixels, width * height), width, height);
    }
}
