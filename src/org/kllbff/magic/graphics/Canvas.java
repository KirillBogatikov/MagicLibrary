package org.kllbff.magic.graphics;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import org.kllbff.magic.graphics.color.Color;
import org.kllbff.magic.graphics.color.ColorsConverter;
import org.kllbff.magic.graphics.strokes.SolidStroke;
import org.kllbff.magic.graphics.strokes.Stroke;
import org.kllbff.magic.images.Bitmap;

public class Canvas {
    private BufferedImage image = null;
    private Graphics2D graphics = null;
    private java.awt.Color fillColor, strokeColor;
    private Paint paint;
    private Stroke stroke;
    
    public Canvas(Bitmap bitmap) {
        this(bitmap.getWidth(), bitmap.getHeight());
        copy(bitmap, image);
    }
    
    public Canvas(Graphics2D awtGraphics, int width, int height) {
        this.graphics = awtGraphics;
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        setPaint(new Paint());
        setStroke(new SolidStroke(5));
    }
    
    public Canvas(int width, int height) {
        this((new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)).createGraphics(), width, height);
    }
    
    public void setPaint(Paint paint) {
        this.paint = paint;
        
        Object antialias, textAntialias;
        if(paint.isAntialiased()) {
            antialias = RenderingHints.VALUE_ANTIALIAS_ON;
            textAntialias = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
        } else {
            antialias = RenderingHints.VALUE_ANTIALIAS_OFF;
            textAntialias = RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
        }
        this.graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialias);
        this.graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, textAntialias);
        
        fillColor = toAWTColor(paint.getColor());
    }
    
    public Paint getPaint() {
        return paint;
    }
    
    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
        
        int cap = 0, join = 0;
        switch(stroke.getCapType()) {
            case BUTT:   cap = BasicStroke.CAP_BUTT; break;
            case ROUND:  cap = BasicStroke.CAP_ROUND; break;
            case SQUARE: cap = BasicStroke.CAP_SQUARE; break;
        }
        
        switch(stroke.getJoinType()) {
            case BEVEL: join = BasicStroke.JOIN_BEVEL; break;
            case SHARP: join = BasicStroke.JOIN_MITER; break;
            case ROUND: join = BasicStroke.JOIN_ROUND; break;
        }

        float[] pattern = stroke.getDrawingPattern();
        if(pattern.length == 0 || pattern[0] == 0.0f) {
           pattern = new float[]{ 0.33f };
        }
        this.graphics.setStroke(new BasicStroke(stroke.getWidth(), cap, join, 10.0f, pattern, 0.0f));
        strokeColor = toAWTColor(stroke.getColor());
    }
    
    public Stroke getStroke() {
        return this.stroke;
    }
    
    public void drawText(int x, int y, String text) {
        graphics.drawString(text, x, y);
    }
    
    public void drawTriangle(int x, int y, int x1, int y1, int x2, int y2) {
        Polygon polygon = new Polygon();
        polygon.addPoint(x, y);
        polygon.addPoint(x1, y1);
        polygon.addPoint(x2, y2);
        
        if(paint.canFill()) {
            graphics.setColor(fillColor);
            graphics.fill(polygon);
        }
        if(paint.hasStroke()) {
            graphics.setColor(strokeColor);
            graphics.drawPolygon(polygon);
        }
    }
    
    public void drawRectangle(int x, int y, int width, int height) {
        if(paint.canFill()) {
            graphics.setColor(fillColor);
            graphics.fillRect(x, y, width, height);
        }
        if(paint.hasStroke()) {
            graphics.setColor(strokeColor);
            graphics.drawRect(x, y, width, height);
        }
    }
    
    public void drawRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        if(paint.canFill()) {
            graphics.setColor(fillColor);
            graphics.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
        }
        if(paint.hasStroke()) {
            graphics.setColor(strokeColor);
            graphics.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
        }
    }
    
    public void drawOval(int x, int y, int width, int height) {
        if(paint.canFill()) {
            graphics.setColor(fillColor);
            graphics.fillOval(x, y, width, height);
        }
        if(paint.hasStroke()) {
            graphics.setColor(strokeColor);
            graphics.drawOval(x, y, width, height);
        }
    }
    
    public void drawPolygon(int... coordinates) {
        Polygon polygon = new Polygon();
        for(int i = 0; i < coordinates.length; i += 2) {
            polygon.addPoint(coordinates[i], coordinates[i + 1]);
        }
        
        if(paint.canFill()) {
            graphics.setColor(fillColor);
            graphics.fillPolygon(polygon);
        }
        if(paint.hasStroke()) {
            graphics.setColor(strokeColor);
            graphics.drawPolygon(polygon);
        }
    }
    
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        if(paint.canFill()) {
            graphics.setColor(fillColor);
            graphics.fillArc(x, y, width, height, startAngle, arcAngle);
        }
        if(paint.hasStroke()) {
            graphics.setColor(strokeColor);
            graphics.drawArc(x, y, width, height, startAngle, arcAngle);
        }
    }
    
    public void drawBitmap(int x, int y, Bitmap bitmap) {
        BufferedImage image = new BufferedImage(bitmap.getWidth(), bitmap.getHeight(), BufferedImage.TYPE_INT_ARGB);
        copy(bitmap, image);
        graphics.drawImage(image, x, y, bitmap.getWidth(), bitmap.getHeight(), null);
    }
    
    public Graphics2D getGraphics() {
        return graphics;
    }
    
    private void copy(Bitmap bmp, BufferedImage img) {
        for(int x = 0; x < bmp.getWidth(); x++) {
            for(int y = 0; y < bmp.getHeight(); y++) {
                long px = ColorsConverter.toRGB(bmp.getPixel(x, y));
                int color = ((int)px) | (Color.alpha(px) << 24); 
                img.setRGB(x, y, color);
            }
        }
    }
    
    private java.awt.Color toAWTColor(long color) {
        color = ColorsConverter.toRGB(color);
        return new java.awt.Color(Color.red(color), Color.green(color), Color.blue(color), Color.alpha(color));
    }
}
