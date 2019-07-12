package org.kllbff.magic.parsers;

import org.kllbff.magic.app.Resources;
import org.kllbff.magic.exceptions.ParsingException;
import org.kllbff.magic.graphics.color.Color;

public abstract class Parser<E> {
    protected Resources resources;
    
    public Parser(Resources resources) {
        this.resources = resources;
    }
    

    protected String toString(String text) {
        if(text.startsWith("@")) {
            return resources.getString(text.replace("@string/", ""));
        }
        return text;
    }
    
    protected int toInt(String text) {
        int val;
        if(text.startsWith("@")) {
            val = (int)resources.getDimension(text.replace("@dimen/", ""));
        } else {
            val = Integer.valueOf(text);
        }
        return val;
    }
    
    protected long toColor(String text) {
        long color = -1;
        switch(text.charAt(0)) {
            case '@': color = resources.getColor(text); break;
            case '#': color = Color.hex(text); break;
            default: throw new ParsingException("Failed to parse color from string \"" + text + "\"");
        }
        return color;
    }
    
    public abstract E parseResource(String resPath) throws Exception;
}
