package org.kllbff.magic.res;

import static org.kllbff.magic.styling.AttributeType.DIMENSION;
import static org.kllbff.magic.styling.AttributeType.STRING;

import java.io.BufferedInputStream;
import java.io.IOException;
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
import org.kllbff.magic.styling.Theme;
import org.xmlpull.v1.XmlPullParserException;

public class Resources {
    private AttributeSet basicAttributes;
    private Theme theme;
    private AttributeSet themeAliases;
    
    public Resources(AttributeSet basic, Theme theme) throws XmlPullParserException {
        this.basicAttributes = basic;
        setTheme(theme);
    }
    
    public Theme getTheme() {
        return theme;
    }
    
    public void setTheme(Theme theme) {
        this.theme = theme;
        this.themeAliases = theme.getAliases();
    }
    
    private void throwParsingException(String target, Attribute attr) throws ParsingException {
        throw new ParsingException("Failed to get " + target + " from attribute '" + attr.getName() + "': type '" + attr.getType() + "' is incompatible ");
    }
    
    private void checkResource(String target, String name)  {
        if(!basicAttributes.has(name) && !themeAliases.has(name)) {
            throw new ResourceNotFoundException(target + " '" + name + "' not found");
        }
    }
    
    @SuppressWarnings("resource")
    public Drawable getDrawable(String name) throws IOException {
        name = name.replace("@drawable/", "");
        
        checkResource("Drawable", name);
        
        Attribute alias = themeAliases.get(name);
        String fileName = alias.get();
        
        switch(alias.getType()) {
            case DRAWABLE: {
                Drawable drawable;
                String resPath = "drawable/" + fileName;
                
                AccessProvider provider = AccessProvider.getInstance();
                try(InputStream input = new BufferedInputStream(provider.openStream(resPath))) {
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
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ParsingException(e);
                }
                return drawable;
            }
            default:
                throwParsingException("drawable", alias);
        }
        return null;
    }
    
    public long getColor(String name) {
        name = name.replace("@color/", "");

        checkResource("Color", name);

        if(!basicAttributes.has(name)) {
            return getColor(themeAliases.getValue(name));
        }
        
        Attribute attr = basicAttributes.get(name);
        switch(attr.getType()) {
            case COLOR: 
                return attr.get();
            default:
                throwParsingException("color", attr);
        }
        return 0L;
    }
    
    public double getDimension(String name) {
        name = name.replace("@dimen/", "");

        checkResource("Dimension", name);

        if(!basicAttributes.has(name)) {
            return getDimension(themeAliases.getValue(name));
        }
        
        Attribute attr = basicAttributes.get(name);
        if(attr.is(DIMENSION)) {
            return attr.get();
        }
        throwParsingException("dimension", attr);
        return 0L;
    }
    
    public String getString(String name) {
        name = name.replace("@states/", "");

        checkResource("String", name);

        if(!basicAttributes.has(name)) {
            return getString(themeAliases.getValue(name));
        }
        
        Attribute attr = basicAttributes.get(name);
        if(attr.is(STRING)) {
            return attr.get();
        }
        throwParsingException("string", attr);
        return null;
    }
    
    /*
    public StateList<?> getStateList(String name) throws ResourceNotFoundException {
        name = name.replaceAll("@(drawable|color)/", "");

        checkResource("StateList", name);

        if(!basicAttributes.has(name)) {
            return getStateList(themeAliases.getValue(name));
        }
        
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
    } */
}
