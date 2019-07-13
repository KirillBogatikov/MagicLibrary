package org.kllbff.magic.app;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.kllbff.magic.exceptions.ParsingException;
import org.kllbff.magic.exceptions.ResourceNotFoundException;
import org.kllbff.magic.graphics.color.Color;
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
    private AttributeSet attributes;
    
    public Resources(AttributeSet attributes) throws XmlPullParserException {
        this.attributes = attributes;
    }
    
    private void throwParsingException(String target, Attribute attr) throws ParsingException {
        throw new ParsingException("Failed to get " + target + " from attribute '" + attr.getName() + "': type '" + attr.getType() + "' is incompatible ");
    }
    
    public Drawable getDrawable(String name) throws ResourceNotFoundException {
        name = name.replace("@drawable/", "");
        
        if(!attributes.has(name)) {
            throw new ResourceNotFoundException("Drawable " + name + " not found");
        }
        
        Attribute attr = attributes.get(name);
        if(attr.is(AttributeType.DRAWABLE)) {
            return attr.get();
        } 
        if(attr.is(AttributeType.STRING)) {
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
                attr.set(drawable, AttributeType.DRAWABLE);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ParsingException(e);
            }
            return drawable;
        }
        throwParsingException("drawable", attr);
        return null;
    }
    
    public long getColor(String name) throws ResourceNotFoundException {
        name = name.replace("@color/", "");

        if(!attributes.has(name)) {
            throw new ResourceNotFoundException("Color " + name + " not found");
        }
        
        Attribute attr = attributes.get(name);
        if(attr.is(AttributeType.COLOR)) {
            return attr.get();
        } 
        if(attr.is(AttributeType.STRING)) {
            String value = attr.get();
            long color;
            if(value.startsWith("#")) {
                color = Color.hex(value);
            } else {
                color = getColor(attr.get());
            }
            attr.set(color, AttributeType.COLOR);
            return color;
        }
        throwParsingException("color", attr);
        return 0L;
    }
    
    public double getDimension(String name) throws ResourceNotFoundException {
        name = name.replace("@dimen/", "");

        if(!attributes.has(name)) {
            throw new ResourceNotFoundException("Dimension " + name + " not found");
        }
        
        Attribute attr = attributes.get(name);
        if(attr.is(AttributeType.DIMENSION)) {
            return attr.get();
        } 
        if(attr.is(AttributeType.STRING)) {
            String value = attr.get();
            double dimen;
            if(value.startsWith("@")) {
                dimen = getColor(attr.get());
            } else {
                dimen = Double.valueOf(value);
                /**
                 *  TODO add conversions from dp/px/sp/pt
                 */
            }
            attr.set(dimen, AttributeType.DIMENSION);
            return dimen;
        }
        throwParsingException("color", attr);
        return 0L;
    }
    
    public String getLayout(String name) throws ResourceNotFoundException {
        name = name.replace("@layout/", "");

        if(!attributes.has(name)) {
            throw new ResourceNotFoundException("Layout " + name + " not found");
        }
        
        return attributes.getValue(name);
    }
    
    public String getString(String name) throws ResourceNotFoundException {
        name = name.replace("@string/", "");

        if(!attributes.has(name)) {
            throw new ResourceNotFoundException("String " + name + " not found");
        }
        
        Attribute attr = attributes.get(name);
        if(attr.is(AttributeType.STRING)) {
            String s = attr.get();
            if(s.startsWith("@")) {
                s = getString(s);
                attr.set(s, AttributeType.STRING);
            }
            return s;
        }
        throwParsingException("string", attr);
        return null;
    }
    
    public StateList<?> getStateList(String name) throws ResourceNotFoundException {
        name = name.replaceAll("@(drawable|color)/", "");

        if(!attributes.has(name)) {
            throw new ResourceNotFoundException("StateList " + name + " not found");
        }
        
        Attribute attr = attributes.get(name);
        if(attr.is(AttributeType.STATE_LIST)) {
            return attr.get();
        }
        if(attr.is(AttributeType.STRING)) {
            StateList<?> list = getStateList(attr.get());
            attr.set(list, AttributeType.STATE_LIST);
            return list;
        }
        throwParsingException("state list", attr);
        return null;
    }
}
