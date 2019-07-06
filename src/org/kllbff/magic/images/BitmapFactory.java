package org.kllbff.magic.images;

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

/*
 * FIXME using Commons Imaging is bad idea: not all needed formats fully supported
 */
public class BitmapFactory {
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
                bitmap.setPixel(x, y, Color.parseRGBA(r, g, b, a));
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
}
