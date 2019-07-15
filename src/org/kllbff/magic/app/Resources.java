package org.kllbff.magic.app;

import static org.kllbff.magic.styling.AttributeType.DIMENSION;
import static org.kllbff.magic.styling.AttributeType.DRAWABLE;
import static org.kllbff.magic.styling.AttributeType.STRING;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.kllbff.magic.exceptions.ParsingException;
import org.kllbff.magic.exceptions.ResourceNotFoundException;
import org.kllbff.magic.graphics.drawable.BitmapDrawable;
import org.kllbff.magic.graphics.drawable.Drawable;
import org.kllbff.magic.graphics.drawable.NinePatchDrawable;
import org.kllbff.magic.images.Bitmap;
import org.kllbff.magic.images.BitmapFactory;
import org.kllbff.magic.parsers.json.JsonDrawableParser;
import org.kllbff.magic.parsers.xml.XmlDrawableParser;
import org.kllbff.magic.styling.Attribute;
import org.kllbff.magic.styling.AttributeSet;
import org.kllbff.magic.styling.AttributeType;
import org.kllbff.magic.styling.StateList;
import org.xmlpull.v1.XmlPullParserException;

public class Resources {
    private AttributeSet basicAttributes;
    private AttributeSet themeAliases;
    
    public Resources(AttributeSet basic, AttributeSet aliases) throws XmlPullParserException {
        this.basicAttributes = basic;
        this.themeAliases = aliases;
    }
    
    public void applyTheme(AttributeSet aliases) {
        this.themeAliases = aliases;
    }
    
    private void throwParsingException(String target, Attribute attr) throws ParsingException {
        throw new ParsingException("Failed to get " + target + " from attribute '" + attr.getName() + "': type '" + attr.getType() + "' is incompatible ");
    }
    
    private void checkResource(String target, String name) throws ResourceNotFoundException {
        if(!basicAttributes.has(name) && !themeAliases.has(name)) {
            throw new ResourceNotFoundException(target + " '" + name + "' not found");
        }
    }
    
    public Drawable getDrawable(String name) throws ResourceNotFoundException {
        name = name.replace("@drawable/", "");
        
        if(!basicAttributes.has(name)) {
            return getDrawable(themeAliases.getValue(name));
        }

        checkResource("Drawable", name);
        Attribute attr = basicAttributes.get(name);
        switch(attr.getType()) {
            case DRAWABLE: 
                return attr.get();
            case STRING: {
                Drawable drawable;
                String resPath = "res/drawable/" + attr.get();
                
                try(InputStream input = new BufferedInputStream(new FileInputStream(resPath))) {
                    if(resPath.endsWith(".xml")) {
                        XmlDrawableParser xmlDrawableParser = new XmlDrawableParser(this);
                        drawable = xmlDrawableParser.parseResource(new InputStreamReader(input));
                    } else if(resPath.endsWith(".json")) {
                        JsonDrawableParser jsonDrawableParser = new JsonDrawableParser(this);
                        drawable = jsonDrawableParser.parseResource(new InputStreamReader(input));
                    } else {
                        Bitmap bitmap = BitmapFactory.readBitmap(input, null);
                        if(resPath.endsWith(".9.png")) {
                            drawable = new NinePatchDrawable(bitmap);
                        }
                        drawable = new BitmapDrawable(bitmap);
                    }
                    attr.set(drawable, DRAWABLE);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ParsingException(e);
                }
                return drawable;
            }
            default:
                throwParsingException("drawable", attr);
        }
        return null;
    }
    
    public long getColor(String name) throws ResourceNotFoundException {
        name = name.replace("@color/", "");

        if(!basicAttributes.has(name)) {
            return getColor(themeAliases.getValue(name));
        }

        checkResource("Color", name);
        
        Attribute attr = basicAttributes.get(name);
        switch(attr.getType()) {
            case COLOR: 
                return attr.get();
            default:
                throwParsingException("color", attr);
        }
        return 0L;
    }
    
    public double getDimension(String name) throws ResourceNotFoundException {
        name = name.replace("@dimen/", "");

        if(!basicAttributes.has(name)) {
            return getDimension(themeAliases.getValue(name));
        }

        checkResource("Dimension", name);
        
        Attribute attr = basicAttributes.get(name);
        if(attr.is(DIMENSION)) {
            return attr.get();
        }
        throwParsingException("dimension", attr);
        return 0L;
    }
    
    public String getString(String name) throws ResourceNotFoundException {
        name = name.replace("@string/", "");

        if(!basicAttributes.has(name)) {
            return getString(themeAliases.getValue(name));
        }

        checkResource("String", name);
        
        Attribute attr = basicAttributes.get(name);
        if(attr.is(STRING)) {
            return attr.get();
        }
        throwParsingException("string", attr);
        return null;
    }
    
    public StateList<?> getStateList(String name) throws ResourceNotFoundException {
        name = name.replaceAll("@(drawable|color)/", "");

        if(!basicAttributes.has(name)) {
            return getStateList(themeAliases.getValue(name));
        }

        checkResource("StateList", name);
        
        Attribute attr = basicAttributes.get(name);
        switch(attr.getType()) {
            case STATE_LIST:
                return attr.get();
            case STRING:
                StateList<?> list = getStateList(attr.get());
                attr.set(list, AttributeType.STATE_LIST);
                return list;
            default:
                throwParsingException("state list", attr);
        }
        return null;
    } 
}
