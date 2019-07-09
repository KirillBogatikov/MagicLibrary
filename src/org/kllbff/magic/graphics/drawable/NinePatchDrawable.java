package org.kllbff.magic.graphics.drawable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.kllbff.magic.graphics.Canvas;
import org.kllbff.magic.graphics.color.Color;
import org.kllbff.magic.graphics.color.ColorsConverter;
import org.kllbff.magic.images.Bitmap;
import org.kllbff.magic.images.BitmapFactory;

public class NinePatchDrawable implements Drawable {
    public static class NinePatchChunk {
        Bitmap source;
        WeakReference<Bitmap> piece;
        public int x, y;
        public int originalWidth, originalHeight;
        public int calcWidth, calcHeight;
        public int addWidth, addHeight;
        public boolean hElastic, vElastic;
        
        public NinePatchChunk(Bitmap source, int x, int y, int width, int height, boolean hElastic, boolean vElastic) {
            this.source = source;
            
            this.x = x;
            this.y = y;
            
            this.originalWidth = width;
            this.originalHeight = height;
            
            this.hElastic = hElastic;
            this.vElastic = vElastic;

            this.piece = new WeakReference<>(BitmapFactory.cut(source, x, y, originalWidth, originalHeight));
        }
        
        public int[] calcSize(int newWidth, int newHeight) {
            int[] elastic = new int[] {
                (newWidth * originalWidth) / source.getWidth(),
                (newHeight * originalHeight) / source.getHeight()
            };
            this.calcWidth = hElastic ? elastic[0] : originalWidth;
            this.calcHeight = vElastic ? elastic[1] : originalHeight;
            return elastic;
        }
        
        private Bitmap getPiece() {
            Bitmap bitmap = piece.get();
            if(bitmap == null) {
                piece = new WeakReference<>(BitmapFactory.cut(source, x, y, originalWidth, originalHeight));  
                bitmap = piece.get();
            }
            return bitmap;
        }
        
        public Bitmap cutPiece(int newWidth, int newHeight) {
            Bitmap piece = getPiece();
            if(hElastic || vElastic) {
                piece = BitmapFactory.scale(piece, calcWidth + addWidth,
                                                   calcHeight + addHeight, null);
            }
            return piece;
        }
    }
    
    private List<NinePatchChunk> chunks;
    private Bitmap bmp;
    private int x, y, width, height;
    private int perLine;
    
    public NinePatchDrawable(Bitmap bmp) {
        chunks = new ArrayList<>();
        
        width = bmp.getWidth() - 2;
        height = bmp.getHeight() - 2;
        
        this.bmp = BitmapFactory.cut(bmp, 1, 1, width, height);
        
        int startX = 0, startY = 0;
        int stepX = 1, stepY = 1;
        int lastAlphaX = -1,
            lastAlphaY = -1;
        int minY = 0;
        
        for(int x = 0, y = 0; x <= width && y <= height; x += stepX, y += stepY) {
            long color = ColorsConverter.toRGB(bmp.getPixel(x + 1, 0));
            int alphaX = Color.alpha(color);
            color = ColorsConverter.toRGB(bmp.getPixel(0, y + 1));
            int alphaY = Color.alpha(color);
            
            if(lastAlphaX == -1) {
                startX = x;
                startY = y;
                lastAlphaX = alphaX;
                lastAlphaY = alphaY;
                continue;
            }

            if(stepX > 0 && alphaX != lastAlphaX || x == width) {
                stepX = 0;
                x--;
                if(stepY == 0) {
                    NinePatchChunk chunk = new NinePatchChunk(this.bmp, startX, startY, x - startX + 1, y - startY + 1, lastAlphaX == 0xFF, lastAlphaY == 0xFF);
                    chunks.add(chunk);

                    if(x + 1 == width) {
                        x = -1;
                        minY += y - startY + 1;
                    }
                    y = minY - 1;
                                        
                    stepX = stepY = 1;
                    lastAlphaX = lastAlphaY = -1;
                    continue;
                }
            }
            if(stepY > 0 && alphaY != lastAlphaY || y == height) {
                stepY = 0;
                y--;
                if(stepX == 0) {
                    NinePatchChunk chunk = new NinePatchChunk(this.bmp, startX, startY, x - startX + 1, y - startY + 1, lastAlphaX == 0xFF, lastAlphaY == 0xFF);
                    chunks.add(chunk);
                    
                    if(x + 1 == width) {
                        x = -1;
                        minY += y - startY + 1;
                    }
                    y = minY - 1;
                                        
                    stepX = stepY = 1;
                    lastAlphaX = lastAlphaY = -1;
                }
            }
        }
        
        for(int i = 1; i < chunks.size(); i++) {
            if(chunks.get(i).y != chunks.get(i - 1).y) {
                perLine = i;
                break;
            }
        }
    }
    
    @Override
    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        
        if(width != this.width || height != this.height) {
            for(NinePatchChunk chunk : chunks) {
                chunk.addWidth = chunk.addHeight = 0;
            }
        }
        
        this.width = width;
        this.height = height;
    }
    
    @Override
    public void draw(Canvas canvas) {
        NinePatchChunk last = null, chunk, next, up = null;
        
        for(int i = 0; i < chunks.size(); i++) {            
            chunk = chunks.get(i);
            
            int[] elastic = chunk.calcSize(width, height);
            
            if(chunk.calcWidth < elastic[0]) {
                next = (i < chunks.size() - 1) ? chunks.get(i + 1) : null;
                int difference = elastic[0] - chunk.calcWidth;
                
                if(next != null && (i + 1) % perLine == 0) {
                    next = null;
                }
                
                if(last != null && i % perLine == 0) {
                    last = null;
                }
                
                if(last == null) {
                    next.addWidth += difference;
                } else {
                    if(next == null) {
                        last.addWidth += difference;
                    } else {
                        int lp = last.originalWidth / chunk.originalWidth;
                        int np = next.originalWidth / chunk.originalWidth;

                        double px = (double)difference / (lp + np);

                        lp = (int)Math.round(px * lp);
                        last.addWidth += lp;
                        next.addWidth += difference - lp;                      
                    }
                }
            }
            
            if(chunk.calcHeight < elastic[1]) {
                up = (i >= perLine) ? chunks.get(i - perLine) : null;
                next = ((chunks.size() - i) > perLine) ? chunks.get(i + perLine) : null;
                int difference = elastic[1] - chunk.calcHeight;
                
                if(next == null) {
                    up.addHeight += difference;
                } else {
                    if(up == null) {
                        next.addHeight += difference;
                    } else {
                        int lp = up.originalHeight / chunk.originalHeight;
                        int np = up.originalHeight / chunk.originalHeight;

                        double px = (double)difference / (lp + np);

                        lp = (int)Math.round(px * lp);
                        up.addHeight += lp;
                        next.addHeight += difference - lp;                        
                    }
                }
            }
             
            last = chunk;
        }
        
        int x = 0, y = 0;
        
        for(int i = 0 ; i < chunks.size(); i++) {
            chunk = chunks.get(i);
            Bitmap piece = chunk.cutPiece(width, height);
            canvas.drawBitmap(this.x + x, this.y + y, piece);
            
            x += piece.getWidth();
            if(i < chunks.size() - 1 && chunks.get(i + 1).y > chunk.y) {
                x = 0;
                y += piece.getHeight();
            }
        }
    }

}
