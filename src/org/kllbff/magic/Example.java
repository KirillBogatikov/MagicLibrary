package org.kllbff.magic;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.io.IOException;

import org.kllbff.magic.fonts.Font;
import org.kllbff.magic.fonts.FontsManager;
import org.kllbff.magic.geometry.Ellipse;
import org.kllbff.magic.graphics.Canvas;
import org.kllbff.magic.graphics.Paint;
import org.kllbff.magic.graphics.color.Color;
import org.kllbff.magic.graphics.drawable.LinearGradient;
import org.kllbff.magic.graphics.drawable.RadialGradient;

public class Example {

    public static void main(String[] args) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        
        Frame frame = new Frame() {
            private static final long serialVersionUID = 263085134421852621L;

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                long memory = runtime.totalMemory();
                time((Graphics2D)g);
                System.out.printf("%d B\n", (runtime.totalMemory() - memory));
            }
        };
        frame.setBounds(0, 0, 900, 900);
        frame.setUndecorated(false);
        frame.setVisible(true);  
            
    }
    
    public static void time(Graphics2D g2d) {
        long time = System.currentTimeMillis();
        
        g2d.translate(50, 50);
        
        Canvas canvas = new Canvas(g2d, 800, 800);
        LinearGradient gd = new LinearGradient(Color.rgba(0, 0, 255, 64), Color.rgba(0, 0, 255, 255), Math.toRadians(90), null);
        gd.setBounds(0, 0, 400, 800);
        gd.draw(canvas);
        
        canvas.setClip(new Ellipse(400, 0, 400, 400));
        RadialGradient rd = new RadialGradient(Color.rgba(255, 0, 255, 0), Color.rgba(255, 0, 255, 255), null);
        rd.setBounds(400, 0, 400, 400);
        rd.draw(canvas);

        canvas.setClip(new Ellipse(400, 400, 400, 400));
        RadialGradient rd1 = new RadialGradient(Color.rgba(255, 216, 0, 255), Color.rgba(178, 0, 255, 255), null);
        rd1.setBounds(400, 400, 400, 400);
        rd1.draw(canvas);
        canvas.clearClip();
        
        Paint myPaint = new Paint(Color.rgba(255, 0, 0, 255), Paint.STROKE | Paint.FILL);
        canvas.setPaint(myPaint);
        canvas.setTextColor(Color.WHITE);
        
        FontsManager man = FontsManager.getInstance();
        Font font = man.getFont("Comic Sans MS");
        font.setSize(75);
        
        canvas.setFont(font);
        canvas.clearClip();
        canvas.drawText(15, 300, "My canvas!");
               
        System.out.printf("%d ms\n", System.currentTimeMillis() - time);
    }
}
