package org.kllbff.magic.parsers;

import static org.xmlpull.v1.XmlPullParser.END_DOCUMENT;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

import java.io.FileInputStream;
import java.io.IOException;

import org.kllbff.magic.app.Resources;
import org.kllbff.magic.exceptions.ParsingException;
import org.kllbff.magic.graphics.drawable.AnimationDrawable;
import org.kllbff.magic.graphics.drawable.ColorDrawable;
import org.kllbff.magic.graphics.drawable.Drawable;
import org.kllbff.magic.graphics.drawable.LayerDrawable;
import org.kllbff.magic.graphics.drawable.LinearGradient;
import org.kllbff.magic.graphics.drawable.RadialGradient;
import org.kllbff.magic.interpolation.CosInterpolator;
import org.kllbff.magic.interpolation.Interpolator;
import org.kllbff.magic.interpolation.LinearInterpolator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class XmlDrawableParser extends Parser<Drawable> {
    private static XmlPullParserFactory parserFactory;
    
    private XmlPullParser xmlPullParser;
    
    public XmlDrawableParser(Resources res) throws XmlPullParserException {
        super(res);
        
        if(parserFactory == null) {
            parserFactory = XmlPullParserFactory.newInstance();
        }
        
        this.xmlPullParser = parserFactory.newPullParser();
    }
    
    private String checkAttr(String attribute, String target) {
        String value = xmlPullParser.getAttributeValue(null, attribute);
        if(value == null) {
            throw new ParsingException("Couldn't find attribute '" + attribute + "' for " + target);
        }
        return value;
    }
        
    private Drawable parseColor() {
        String colorAttr = checkAttr("color", "color drawable");
        return new ColorDrawable(toColor(colorAttr));
    }
    
    private Drawable parseGradient() {
        String startAttr = checkAttr("start", "gradient drawable");
        String endAttr = checkAttr("end", "gradient drawable");
        String type = checkAttr("type", "gradient drawable");
        
        String interAttr = xmlPullParser.getAttributeValue(null, "interpolator");
        Interpolator interpolator = null;
        if(interAttr != null) {
            switch(toString(interAttr)) {
                case "cos": interpolator = new CosInterpolator(); break;
                case "linear": interpolator = new LinearInterpolator(); break;
                default: throw new ParsingException("Cannot find interpolator for \"" + interAttr + "\". Supported only cos and linear interpolators");
            }
        }
        
        long start = toColor(startAttr);
        long end = toColor(endAttr);
        
        switch(toString(type)) {
            case "linear":
                String angleKey = xmlPullParser.getAttributeValue(null, "angle");
                if(angleKey == null) {
                    angleKey = "0";
                }
                int angle = toInt(angleKey);
                return new LinearGradient(start, end, angle, interpolator);
            case "radial":
                return new RadialGradient(start, end, null);
            default:
                throw new ParsingException("Cannot parse gradient of unknown type \"" + type + "\"");
        }
    }
    
    private void parseFrame(Drawable root) throws XmlPullParserException, IOException {
        String durationKey = checkAttr("duration", "frame of animation drawable");
        
        AnimationDrawable animation = (AnimationDrawable)root;
        
        int duration = toInt(durationKey);
        
        String frameKey = xmlPullParser.getAttributeValue(null, "drawable");
        Drawable frame;
        if(frameKey == null) {
            int event = xmlPullParser.next();
            while(event != END_DOCUMENT && event != START_TAG) {
                event = xmlPullParser.next();
            }
            frame = parse();
        } else if(frameKey.startsWith("@")) {
            frame = resources.getDrawable(frameKey.replaceAll("@drawable/", ""));
        } else {
            throw new ParsingException("Cannot retrieve drawable \"" + frameKey + "\"");
        }
        
        animation.add(frame, duration);
    }
    
    private void parseLayer(Drawable root) throws XmlPullParserException, IOException {
        String xAttr = checkAttr("x", "one of layers for drawable");
        String yAttr = checkAttr("y", "one of layers for drawable");
        
        LayerDrawable layerDrawable = (LayerDrawable)root;
        
        int x = toInt(xAttr);
        int y = toInt(yAttr);
        
        String layerKey = xmlPullParser.getAttributeValue(null, "drawable");
        Drawable layer;
        if(layerKey == null) {
            int event = xmlPullParser.next();
            while(event != END_DOCUMENT && event != START_TAG) {
                event = xmlPullParser.next();
            }
            layer = parse();
        } else if(layerKey.startsWith("@")) {
            layer = resources.getDrawable(layerKey.replaceAll("@drawable/", ""));
        } else {
            throw new ParsingException("Cannot retrieve drawable \"" + layerKey + "\"");
        }
        
        layerDrawable.add(layer, x, y);
    }
    
    private Drawable parse() throws XmlPullParserException, IOException {
        String tagName = xmlPullParser.getName();
        switch(tagName) {
            case "animation-drawable": return new AnimationDrawable();
            case "color-drawable":              return parseColor();
            case "layer-drawable":     return new LayerDrawable();
            case "gradient":           return parseGradient();
            default:
                throw new ParsingException("Failed to parse drawable with tag \"" + tagName +"\"");
        }
    }
    
    private void parseChild(Drawable root) throws XmlPullParserException, IOException {
        String tagName = xmlPullParser.getName();
        switch(tagName) {
            case "frame": parseFrame(root); break;
            case "layer": parseLayer(root); break;
            default:
                throw new ParsingException("Failed to parse drawable with tag \"" + tagName +"\"");
        }
    }
    
    @Override
    public Drawable parseResource(String resPath) throws IOException, XmlPullParserException {
        Drawable root;
        try(FileInputStream input = new FileInputStream(resPath)) {
            xmlPullParser.setInput(input, "UTF-8");
            int event = xmlPullParser.next();
            root = parse();
            
            while(event != END_DOCUMENT) {
                event = xmlPullParser.next();
                if(event == START_TAG) {
                    parseChild(root);
                }
            }
        }
        return root;
    }

}
