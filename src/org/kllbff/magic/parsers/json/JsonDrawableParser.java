package org.kllbff.magic.parsers.json;

import java.io.Reader;

import org.kllbff.magic.app.Resources;
import org.kllbff.magic.exceptions.ParsingException;
import org.kllbff.magic.exceptions.ResourceNotFoundException;
import org.kllbff.magic.graphics.drawable.AnimationDrawable;
import org.kllbff.magic.graphics.drawable.ColorDrawable;
import org.kllbff.magic.graphics.drawable.Drawable;
import org.kllbff.magic.graphics.drawable.LayerDrawable;
import org.kllbff.magic.graphics.drawable.LinearGradient;
import org.kllbff.magic.graphics.drawable.RadialGradient;
import org.kllbff.magic.interpolation.CosInterpolator;
import org.kllbff.magic.interpolation.Interpolator;
import org.kllbff.magic.interpolation.LinearInterpolator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonDrawableParser extends JsonAbstractParser<Drawable> {

    public JsonDrawableParser(Resources resources) {
        super(resources);
    }
    
    private Drawable parseDrawable(JsonObject object) throws ResourceNotFoundException {
        String clazz = object.get("class").getAsString();
        switch(clazz) {
            case "animation-drawable": {
                AnimationDrawable group = new AnimationDrawable(); 
                
                JsonArray children = object.get("frames").getAsJsonArray();
                for(JsonElement child : children) {
                    JsonObject frame = child.getAsJsonObject();
                    checkProp(frame, "duration", "frame of animation drawable");
                    group.add(parseChild(frame), (int)toDimen(getProp(frame, "duration", "0")));
                }
                
                return group;
            }
            case "layer-drawable": {
                LayerDrawable group = new LayerDrawable();
                JsonArray children = object.get("layers").getAsJsonArray();
                for(JsonElement child : children) {
                    JsonObject layer = child.getAsJsonObject();
                    checkProp(layer, "duration", "layer of animation drawable");
                    group.add(parseChild(layer), (int)toDimen(getProp(layer, "x", "0")), (int)toDimen(getProp(layer, "y", "0")));
                }
                
                return group;
            }
            case "color-drawable": { 
                checkProp(object, "color", "color drawable");
                return new ColorDrawable(toColor(getProp(object, "color", null)));
            }
            case "gradient": {
                checkProp(object, "start", "gradient drawable");
                checkProp(object, "end", "gradient drawable");
                checkProp(object, "type", "gradient drawable");
                
                Interpolator interpolator = null;
                if(object.has("interpolator")) {
                    String interpoaltionProp = object.get("interpolator").getAsString();
                    switch(interpoaltionProp) {
                        case "cos": interpolator = new CosInterpolator(); break;
                        case "linear": interpolator = new LinearInterpolator(); break;
                        default: throw new ParsingException("Cannot find interpolator for \"" + interpoaltionProp + "\". Supported only cos and linear interpolators");
                    }
                }
                
                long start = toColor(getProp(object, "start", null));
                long end = toColor(getProp(object, "end", null));
                
                String type = getProp(object, "type", null);
                switch(type) {
                    case "linear": 
                        int angle = (int)toDimen(getProp(object, "angle", "0"));
                        return new LinearGradient(start, end, angle, interpolator);
                    case "radial":
                        return new RadialGradient(start, end, interpolator);
                    default:
                        throw new ParsingException("Cannot parse gradient of unknown type \"" + type + "\"");
                }
            }
            default: throw new ParsingException("Failed to parse drawable with class \"" + clazz +"\"");
        }
    }
    
    private Drawable parseChild(JsonObject object) throws ResourceNotFoundException {
        checkProp(object, "duration", "frame of animation drawable");

        String key = getProp(object, "drawable", null);
        if(key != null) {
            if(key.startsWith("@")) {
                return resources.getDrawable(key);
            }
            throw new ParsingException("Failed to parse 'drawable' property of frame with value '" + key + "'");
        } else {
            return parseDrawable(object);
        }
    }

    @Override
    public Drawable parseResource(Reader reader) throws ResourceNotFoundException {
        JsonArray root = jp.parse(reader).getAsJsonArray();
        
        if(root.size() != 1) {
            throw new ParsingException("Cannot parse json drawable: it contains " + root.size() + " elements, but supported only 1");
        }
        
        JsonObject drawable = root.get(0).getAsJsonObject();
        return parseDrawable(drawable);
    }

}
