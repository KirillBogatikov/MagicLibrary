package org.kllbff.magic.parsers;

import java.io.Reader;

import org.kllbff.magic.app.Display;
import org.kllbff.magic.app.DisplayManager;
import org.kllbff.magic.app.Resources;
import org.kllbff.magic.exceptions.ParsingException;
import org.kllbff.magic.exceptions.ResourceNotFoundException;
import org.kllbff.magic.graphics.color.Color;
import org.kllbff.magic.graphics.drawable.Drawable;

public abstract class AbstractParser<E> {
    protected boolean linksAllowed = true;
    protected Resources resources;
    
    public AbstractParser(Resources resources) {
        this.resources = resources;
    }
    
    protected String toString(String text) throws ResourceNotFoundException {
        if(text.startsWith("@")) {
            if(!linksAllowed) {
                throw new ParsingException("Links does not allowed for string constants");
            }
            return resources.getString(text);
        }
        return text;
    }
    
    protected double toDimen(String text) throws ResourceNotFoundException {
        if(text.startsWith("@")){
            if(!linksAllowed) {
                throw new ParsingException("Links does not allowed for dimensions");
            }
            return resources.getDimension(text);
        }
        
        if(text.length() < 3) {
            throw new ParsingException("Cannot retrieve dimension from string shorter than 3 symbols (" + text.length() + " given)");
        }
        
        Display display = DisplayManager.getInstance().getDefaultDisplay();
        double density = display.getDensity();
        
        String type = text.substring(text.length() - 2);
        double val = Double.valueOf(text.substring(0, text.length() - 2));
        switch(type) {
            case "px": return val;
            case "dp": return (density / 160.0) * val;
            case "pt": return density * val / 72;
            case "in": return density * val;
            case "mm": return density * val / 25.4;
            default:
                throw new ParsingException("Cannot retrieve dimension from string '" + text + "'");
        }
    }
    
    protected long toColor(String text) throws ResourceNotFoundException {
        long color = -1;
        switch(text.charAt(0)) {
            case '@': 
                if(!linksAllowed) {
                    throw new ParsingException("Links does not allowed for color constants");
                }
                color = resources.getColor(text); 
            break;
            case '#': color = Color.hex(text); break;
            default: throw new ParsingException("Failed to parse color from string \"" + text + "\"");
        }
        return color;
    }
    
    protected Drawable toDrawable(String text) throws ResourceNotFoundException {
        if(!linksAllowed) {
            throw new ParsingException("Links does not allowed for drawable constants");
        }
        return resources.getDrawable(text);
    }
    
    public abstract E parseResource(Reader reader) throws Exception;
}
