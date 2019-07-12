package org.kllbff.magic.app;

import java.io.InputStream;

import org.kllbff.magic.exceptions.ParsingException;
import org.kllbff.magic.graphics.drawable.BitmapDrawable;
import org.kllbff.magic.graphics.drawable.Drawable;
import org.kllbff.magic.graphics.drawable.NinePatchDrawable;
import org.kllbff.magic.images.Bitmap;
import org.kllbff.magic.images.BitmapFactory;
import org.kllbff.magic.parsers.JsonDrawableParser;
import org.kllbff.magic.parsers.XmlDrawableParser;
import org.kllbff.magic.styling.Attribute;
import org.kllbff.magic.styling.AttributeSet;
import org.kllbff.magic.styling.AttributeType;
import org.kllbff.magic.styling.StateList;
import org.xmlpull.v1.XmlPullParserException;

public class Resources {
    private XmlDrawableParser xmlDrawableParser;
    private JsonDrawableParser jsonDrawableParser;
    private AttributeSet attributes;
    
    public Resources() throws XmlPullParserException {
        xmlDrawableParser = new XmlDrawableParser(this);
        jsonDrawableParser = new JsonDrawableParser(this);
    }
    
    public Drawable getDrawable(String name) {
        Attribute attr = attributes.get(name);
        if(attr.is(AttributeType.DRAWABLE)) {
            return attr.get();
        } else if(attr.is(AttributeType.STRING)) {
            Drawable drawable;
            String resPath = "res/drawable/" + attr.get();
            try(InputStream input = getClass().getResourceAsStream(resPath)) {
                if(resPath.endsWith(".xml")) {
                    drawable = xmlDrawableParser.parseResource(resPath);
                } else if(resPath.endsWith(".json")) {
                    drawable = jsonDrawableParser.parseResource(resPath);
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
        } else {
            throw new ParsingException("Failed to get drawable from attribute '" + attr.getName() + "': type '" + attr.getType() + "' is incompatible ");
        }
    }
    
    public long getColor(String name) {
        return -1L;
    }
    
    public double getDimension(String name) {
        return 0.0;
    }
    
    public String getLayout(String name) {
        return "";
    }
    
    public String getString(String name) {
        return "";
    }
    
    public StateList<?> getStateList(String name) {
        return null;
    }
}
