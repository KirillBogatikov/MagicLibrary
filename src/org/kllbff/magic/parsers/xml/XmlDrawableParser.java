package org.kllbff.magic.parsers.xml;

import java.io.IOException;
import java.io.Reader;

import org.kllbff.magic.app.Resources;
import org.kllbff.magic.exceptions.ParsingException;
import org.kllbff.magic.exceptions.ResourceNotFoundException;
import org.kllbff.magic.graphics.drawable.AnimationDrawable;
import org.kllbff.magic.graphics.drawable.ColorDrawable;
import org.kllbff.magic.graphics.drawable.Drawable;
import org.kllbff.magic.graphics.drawable.DrawableGroup;
import org.kllbff.magic.graphics.drawable.LayerDrawable;
import org.kllbff.magic.graphics.drawable.LinearGradient;
import org.kllbff.magic.graphics.drawable.RadialGradient;
import org.kllbff.magic.interpolation.CosInterpolator;
import org.kllbff.magic.interpolation.Interpolator;
import org.kllbff.magic.interpolation.LinearInterpolator;
import org.xmlpull.v1.XmlPullParserException;

public class XmlDrawableParser extends XmlAbstractParser<Drawable> {

    public XmlDrawableParser(Resources resources) throws XmlPullParserException {
        super(resources);
    }
    
    private Drawable parseDrawable(String tag) throws XmlPullParserException, IOException, ResourceNotFoundException {
        DrawableGroup group;
        switch(tag) {
            case "animation-drawable": 
                group = new AnimationDrawable();
            break;
            case "layer-drawable": 
                group = new LayerDrawable();
            break;
            case "color-drawable": {
                checkAttr("color", "color drawable");
                return new ColorDrawable(toColor(getAttr("color", null)));
            }
            case "gradient": {
                checkAttr("start", "gradient drawable");
                checkAttr("end", "gradient drawable");
                checkAttr("type", "gradient drawable");
                
                Interpolator interpolator;
                String interpolatorName = getAttr("interpolator", "linear");
                switch(interpolatorName) {
                    case "cos":    interpolator = new CosInterpolator(); break;
                    case "linear": interpolator = new LinearInterpolator(); break;
                    default: throw new ParsingException("Cannot find interpolator for '" + interpolatorName + "'. Supported only cos and linear interpolators");
                }
                
                String gradientType = getAttr("type", null);
                switch(gradientType) {
                    case "linear": {
                        int angle = (int)toDimen(getAttr("angle", "0"));
                        return new LinearGradient(toColor(getAttr("start", null)), toColor(getAttr("end", null)), angle, interpolator);
                    }
                    case "radial":
                        return new RadialGradient(toColor(getAttr("start", null)), toColor(getAttr("end", null)), interpolator);
                }
            }   
            default:
                throw new ParsingException("Failed to parse drawable with tag \"" + tag +"\"");
        }
        
        String childTag;
        while((childTag = nextTag(tag)) != null) {
            parseChildDrawable(childTag, group);
        }
        
        return group;
    }
    
    private void parseChildDrawable(String tag, DrawableGroup root) throws XmlPullParserException, IOException, ResourceNotFoundException {
        switch(tag) {
            case "frame": {
                if(!(root instanceof AnimationDrawable)) {
                    throw new ParsingException("Cannot attach frame drawable to " + root.getClass().getName() + ". Supported only AnimationDrawable class");
                }
                
                checkAttr("duration", "frame of animation drawable");
                
                int duration = (int)toDimen(getAttr("duration", null));
                
                Drawable frame;
                String drawableAttr = getAttr("drawable", null);
                if(drawableAttr == null) {
                    frame = parseDrawable(nextTag(tag));
                } else if(drawableAttr.startsWith("@")) {
                    frame = resources.getDrawable(drawableAttr);
                } else {
                    throw new ParsingException("Cannot retrieve drawable '" + drawableAttr + "'");
                }
                
                ((AnimationDrawable)root).add(frame, duration);
            } break;
            case "layer": { 
                if(!(root instanceof LayerDrawable)) {
                    throw new ParsingException("Cannot attach layer drawable to " + root.getClass().getName() + ". Supported only LayerDrawable class");
                }
                
                checkAttr("x", "layer of layers drawable");
                checkAttr("y", "layer of layers drawable");

                int x = (int)toDimen(getAttr("x", null));
                int y = (int)toDimen(getAttr("y", null));
                
                Drawable layer;
                String drawableAttr = getAttr("drawable", null);
                if(drawableAttr == null) {
                    layer = parseDrawable(nextTag(tag));
                } else if(drawableAttr.startsWith("@")) {
                    layer = resources.getDrawable(drawableAttr);
                } else {
                    throw new ParsingException("Cannot retrieve drawable '" + drawableAttr + "'");
                }
                
                ((LayerDrawable)root).add(layer, x, y);
                
            } break;
            default:
                throw new ParsingException("Failed to parse drawable with tag \"" + tag +"\"");
        }
    }

    @Override
    public Drawable parseResource(Reader reader) throws IOException, XmlPullParserException, ResourceNotFoundException {
        xpp.setInput(reader);
        String tag = nextTag(null);
        if(tag == null) {
            throw new ParsingException("Specified resource is empty or does not contains XML tags");
        }
        
        return parseDrawable(tag);
    }

}
