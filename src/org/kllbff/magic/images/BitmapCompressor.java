package org.kllbff.magic.images;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.kllbff.magic.exceptions.ParsingException;

public class BitmapCompressor {
    private CompressFormats format;
    private OutputStream output;
    
    public BitmapCompressor(OutputStream output, CompressFormats format) {
        this.format = format;
        this.output = output;
    }
    
    public void compress(Bitmap bitmap) throws IOException {
        //FIXME
        BufferedImage image = new BufferedImage(bitmap.getWidth(), bitmap.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int x = 0; x < bitmap.getWidth(); x++) {
            for(int y = 0; y < bitmap.getHeight(); y++) {
                System.out.println("set " + Integer.toHexString((int)bitmap.getPixel(x, y) | (0xFF << 24)));
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
