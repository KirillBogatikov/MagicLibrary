package org.kllbff.magic.fonts;

import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.kllbff.magic.exceptions.ParsingException;

public class FontsManager {
    private static FontsManager instance;
    
    public static FontsManager getInstance() {
        if(instance == null) {
            return new FontsManager();
        }
        return instance;
    }
    
    private boolean cached = false;
    private GraphicsEnvironment graphicsEnvironment;
    private HashMap<String, Font> registeredFonts;
    
    private FontsManager() {
        graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        registeredFonts = new HashMap<String, Font>();
        update();
    }
    
    public void update() {
        for(java.awt.Font awtFont : graphicsEnvironment.getAllFonts()) {
            Font font = new Font(awtFont);
            if(!registeredFonts.containsKey(awtFont.getFamily())) {
                registeredFonts.put(awtFont.getFamily(), font);
            }
        }
        cached = true;
    }
    
    public Font loadFont(InputStream input) throws ParsingException {
        try {
            java.awt.Font font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, input);
            return registeredFonts.put(font.getFamily(), new Font(font));
        } catch (FontFormatException | IOException e) {
            throw new ParsingException(e);
        }
    }
    
    public Font getFont(String family) {
        if(!cached) {
            update();
        }
        Font font = registeredFonts.get(family);
        if(font != null) {
            font = font.clone();
        }
        return font;
    }
    
    public List<Font> getAvailableFonts() {
        ArrayList<Font> list = new ArrayList<Font>();
        for(Font font : registeredFonts.values()) {
            list.add(font);
        }
        return list;
    }   
}
