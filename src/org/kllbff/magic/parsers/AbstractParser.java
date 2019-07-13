package org.kllbff.magic.parsers;

import java.io.Reader;

import org.kllbff.magic.app.Resources;
import org.kllbff.magic.exceptions.ParsingException;
import org.kllbff.magic.exceptions.ResourceNotFoundException;
import org.kllbff.magic.graphics.color.Color;
import org.kllbff.magic.graphics.drawable.Drawable;

public abstract class AbstractParser<E> {
    protected Resources resources;
    
    public AbstractParser(Resources resources) {
        this.resources = resources;
    }
    
    protected String toString(String text) throws ResourceNotFoundException {
        if(text.startsWith("@")) {
            return resources.getString(text);
        }
        return text;
    }
    
    protected double toDimen(String text) throws ResourceNotFoundException {
        double val;
        if(text.startsWith("@")) {
            val = resources.getDimension(text);
        } else {
            val = Integer.valueOf(text);
        }
        return val;
    }
    
    protected long toColor(String text) throws ResourceNotFoundException {
        long color = -1;
        switch(text.charAt(0)) {
            case '@': color = resources.getColor(text); break;
            case '#': color = Color.hex(text); break;
            default: throw new ParsingException("Failed to parse color from string \"" + text + "\"");
        }
        return color;
    }
    
    protected Drawable toDrawable(String text) throws ResourceNotFoundException {
        return resources.getDrawable(text);
    }
    
    public abstract E parseResource(Reader reader) throws Exception;
}
