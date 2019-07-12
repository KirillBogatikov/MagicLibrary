package org.kllbff.magic.parsers;

import java.io.FileReader;

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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonDrawableParser extends Parser<Drawable> {
    
    public JsonDrawableParser(Resources resources) {
        super(resources);
        // TODO Auto-generated constructor stub
    }
    
    private void checkProperty(JsonObject object, String property, String target) {
        if(!object.has(property)) {
            throw new ParsingException("Failed to find required property '" + property + "' for " + target);
        }
    }
        
    private Drawable parseColor(JsonObject object) {
        checkProperty(object, "color", "Failed to fing required property 'color' for color drawable");
        return new ColorDrawable(toColor(object.get("color").getAsString()));
    }
    
    private Drawable parseGradient(JsonObject object) {
        checkProperty(object, "start", "color drawable");
        checkProperty(object, "end", "color drawable");
        checkProperty(object, "type", "color drawable");
        
        long start = toColor(object.get("start").getAsString());
        long end = toColor(object.get("end").getAsString());
        String type = toString(object.get("type").getAsString());
        
        Interpolator interpolator = null;
        if(object.has("interpolator")) {
            String interProp = object.get("interpolator").getAsString();
            switch(toString(interProp)) {
                case "cos": interpolator = new CosInterpolator(); break;
                case "linear": interpolator = new LinearInterpolator(); break;
                default: throw new ParsingException("Cannot find interpolator for \"" + interProp + "\". Supported only cos and linear interpolators");
            }
        }
        
        switch(type) {
            case "linear":
                int angle;
                if(object.has("angle")) {
                    angle = toInt(object.get("angle").getAsString());
                } else {
                    angle = 0;
                }
                return new LinearGradient(start, end, angle, interpolator);
            case "radial":
                return new RadialGradient(start, end, null);
            default:
                throw new ParsingException("Cannot parse gradient of unknown type \"" + type + "\"");
        }
    }

    private Drawable parseAnimation(JsonObject object) {
        checkProperty(object, "frames", "animation drawable");
        
        AnimationDrawable drawable = new AnimationDrawable();
        
        for(JsonElement frameElement : object.get("frames").getAsJsonArray()) {
            Drawable temp;
            JsonObject frame = frameElement.getAsJsonObject();
            
            checkProperty(frame, "duration", "frame of animation drawable");
            if(frame.has("drawable")) {
                String key = frame.get("drawable").getAsString();
                if(key.startsWith("@")) {
                    temp = resources.getDrawable(key.replace("@drawable/", ""));
                } else {
                    throw new ParsingException("Failed to parse 'drawable' property of frame with value '" + key + "'");
                }
            } else {
                temp = parse(frame);
            }
            drawable.add(temp, toInt(frame.get("duration").getAsString()));
        }
        
        return drawable;
    }
    
    private Drawable parseLayers(JsonObject object) {
        checkProperty(object, "layers", "layer drawable");
        
        LayerDrawable drawable = new LayerDrawable();
        
        for(JsonElement layerElement : object.get("layers").getAsJsonArray()) {
            Drawable temp;
            JsonObject layer = layerElement.getAsJsonObject();
            
            checkProperty(layer, "x", "one of layers for drawable");
            checkProperty(layer, "y", "one of layers for drawable");
            if(layer.has("drawable")) {
                String key = layer.get("drawable").getAsString();
                if(key.startsWith("@")) {
                    temp = resources.getDrawable(key.replace("@drawable/", ""));
                } else {
                    throw new ParsingException("Failed to parse 'drawable' property of layer with value '" + key + "'");
                }
            } else {
                temp = parse(layer);
            }
            drawable.add(temp, toInt(layer.get("x").getAsString()), toInt(layer.get("y").getAsString()));
        }
        
        return drawable;
    }
    
    private Drawable parse(JsonObject object) {
        String clazz = object.get("class").getAsString();
        switch(clazz) {
            case "animation-drawable": return parseAnimation(object);
            case "layer-drawable": return parseLayers(object);
            case "color-drawable": return parseColor(object);
            case "gradient": return parseGradient(object);
            default: throw new ParsingException("Failed to parse drawable with class \"" + clazz +"\"");
        }
    }
        
    @Override
    public Drawable parseResource(String resPath) throws Exception {
        try(FileReader input = new FileReader(resPath)) {
            JsonParser json = new JsonParser();
            JsonObject rootObject = json.parse(input).getAsJsonArray().get(0).getAsJsonObject();
            return parse(rootObject);
        }
    }

}
