package org.kllbff.magic.images;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.kllbff.magic.exceptions.ParsingException;
import org.kllbff.magic.graphics.color.Color;
import org.kllbff.magic.graphics.color.ColorsConverter;

/*
 * FIXME using Commons Imaging is bad idea: not all needed formats fully supported
 * FIXME using Java AWT framework is bad idea: magic must have independency
 */
public class BitmapFactory {
    public static enum ScaleType {
        FAST, SMOOTH, REPLICATE, AREA_AVERAGE
    }
    
    private static BufferedInputStream bufferize(InputStream input) {
        if(input instanceof BufferedInputStream) {
            return (BufferedInputStream)input;
        }
        return new BufferedInputStream(input);
    }
    
    @SuppressWarnings("resource")
    public static BitmapInfo readInfo(InputStream input) throws IOException {
        ImageInfo info;
        try {
            input = bufferize(input);
            input.mark(65536);
            info = Imaging.getImageInfo(input, "unknown");
            input.reset();
        } catch(ImageReadException apacheException) {
            throw new ParsingException(apacheException.getLocalizedMessage());
        }
        
        StorageFormats myFormat;
        ImageFormat apacheFormat = info.getFormat();
        if(apacheFormat.equals(ImageFormats.BMP)) {
            myFormat = StorageFormats.BMP;
        } else if(apacheFormat.equals(ImageFormats.GIF)) {
            myFormat = StorageFormats.GIF;
        } else if(apacheFormat.equals(ImageFormats.ICO)) {
            myFormat = StorageFormats.ICO;
        } else if(apacheFormat.equals(ImageFormats.JPEG)) {
            myFormat = StorageFormats.JPEG;
        } else if(apacheFormat.equals(ImageFormats.PNG)) {
            myFormat = StorageFormats.PNG;
        } else if(apacheFormat.equals(ImageFormats.TIFF)) {
            myFormat = StorageFormats.TIFF;
        } else {
            myFormat = StorageFormats.UNKNOWN;
        }
        
        return new BitmapInfo(info.getWidth(), info.getHeight(), info.getBitsPerPixel(), info.getComments(), myFormat);
    }
    
    @SuppressWarnings("resource")
    public static Bitmap readBitmap(InputStream input, BitmapInfo info) throws IOException {
        BufferedImage image;
        
        try {
            input = bufferize(input);
            if(info == null) {
                info = readInfo(input);
            }
            image = Imaging.getBufferedImage(input);
        } catch(ImageReadException apacheException) {
            apacheException.printStackTrace();
            throw new ParsingException(apacheException.getLocalizedMessage());
        }
                
        Bitmap bitmap = new Bitmap(info.getWidth(), info.getHeight());
        for(int x = 0; x < info.getWidth(); x++) {
            for(int y = 0; y < info.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int a = rgb >>> 24;
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);
                
                bitmap.setPixel(x, y, Color.rgba(r, g, b, a));
            }
        }
        
        return bitmap;
    }
    
    public static void writeBitmap(Bitmap bitmap, OutputStream output, StorageFormats format) throws IOException {
        BufferedImage image = new BufferedImage(bitmap.getWidth(), bitmap.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int x = 0; x < bitmap.getWidth(); x++) {
            for(int y = 0; y < bitmap.getHeight(); y++) {
                image.setRGB(x, y, (int)bitmap.getPixel(x, y) | (0xFF << 24));
            }
        }
        
        ImageFormat imagingFormat;
        switch(format) {
            case BMP: imagingFormat = ImageFormats.BMP; break;
            case GIF: imagingFormat = ImageFormats.GIF; break;
            case ICO: imagingFormat = ImageFormats.ICO; break;
            case JPEG: imagingFormat = ImageFormats.JPEG; break;
            case PNG: imagingFormat = ImageFormats.PNG; break;
            case TIFF: imagingFormat = ImageFormats.TIFF; break;
            default: imagingFormat = ImageFormats.PNG; break;
        }
        
        try {
            Imaging.writeImage(image, output, imagingFormat, null);
        } catch (IOException e) {
            throw e;
        } catch(ImageWriteException e) {
            throw new ParsingException(e);
        }
    }
    
    public static Bitmap cut(Bitmap src, int ox, int oy, int width, int height) {
        if(src.getWidth() < (width + ox)) {
            throw new RuntimeException("Can not cut a piece of bitmap: width + x (" + (width + ox) + " greater than source bitmap width: " + src.getWidth());
        }
        if(src.getHeight() < (height + oy)) {
            throw new RuntimeException("Can not cut a piece of bitmap: height + y (" + (height + oy) + " greater than source bitmap height: " + src.getHeight());
        }
        
        Bitmap target = new Bitmap(width, height);
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                target.setPixel(x, y, src.getPixel(ox + x, oy + y));
            }
        }
        return target;
    }
    
    public static Bitmap scale(Bitmap src, int newWidth, int newHeight, ScaleType scaleType) {
        if(newWidth < 0 || newHeight < 0) {
            throw new RuntimeException("Cannot scale image to negative width (" + newWidth + "px) or height (" + newHeight + "px)");
        }
        if(src.getWidth() == newWidth && src.getHeight() == newHeight) {
            return src.copy();
        }
        
        int scaleFlag = BufferedImage.SCALE_DEFAULT;
        if(scaleType != null) {
            switch(scaleType) {
                case SMOOTH: scaleFlag = BufferedImage.SCALE_SMOOTH; break;
                case REPLICATE: scaleFlag = BufferedImage.SCALE_REPLICATE; break;
                case AREA_AVERAGE: scaleFlag = BufferedImage.SCALE_AREA_AVERAGING; break;
                case FAST: scaleFlag = BufferedImage.SCALE_FAST;
            }
        }
        
        Image image = BitmapFactory.toAWT(src);
        image = image.getScaledInstance(newWidth, newHeight, scaleFlag);
        BufferedImage bufImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = bufImage.getGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return fromAWT(bufImage);
    }
    
    public static Bitmap fromAWT(BufferedImage image) {
        Bitmap bmp = new Bitmap(image.getWidth(null), image.getHeight(null));
        int color, red, green, blue, alpha;
        for(int x = 0; x < bmp.getWidth(); x++) {
            for(int y = 0; y < bmp.getHeight(); y++) {
                color = image.getRGB(x, y);
                alpha = (color >>> 24) & 0xFF;
                red = (color >> 16) & 0xFF;
                green = (color >> 8) & 0xFF;
                blue = color & 0xFF;
                
                bmp.setPixel(x, y, Color.rgba(red, green, blue, alpha));
            }
        }
        return bmp;
    }
    
    public static BufferedImage toAWT(Bitmap bmp) {
        BufferedImage img = new BufferedImage(bmp.getWidth(), bmp.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int x = 0; x < bmp.getWidth(); x++) {
            for(int y = 0; y < bmp.getHeight(); y++) {
                long px = ColorsConverter.toRGB(bmp.getPixel(x, y));                
                int color = ((int)px) | (Color.alpha(px) << 24); 
                img.setRGB(x, y, color);
            }
        }
        return img;
    }
}
