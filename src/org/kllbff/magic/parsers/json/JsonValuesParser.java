package org.kllbff.magic.parsers.json;

import java.io.Reader;

import org.kllbff.magic.exceptions.ResourceNotFoundException;
import org.kllbff.magic.styling.Attribute;
import org.kllbff.magic.styling.AttributeSet;
import org.kllbff.magic.styling.AttributeType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonValuesParser extends JsonAbstractParser<AttributeSet> {

    public JsonValuesParser() {
        super(null);
        this.linksAllowed = false;
    }

    @Override
    public AttributeSet parseResource(Reader reader) throws ResourceNotFoundException {
        AttributeSet attributes = new AttributeSet();
        JsonArray array = jp.parse(reader).getAsJsonArray();
        
        for(JsonElement element : array) {
            JsonObject item = element.getAsJsonObject();
            
            checkProp(item, "type", "attribute set");
            checkProp(item, "name", "attribute set");
            checkProp(item, "value", "attribute set");
            
            String type = getProp(item, "type", null);
            String name = getProp(item, "name", null);
            String value = getProp(item, "value", null);
            
            Attribute attr;
            switch(type) {
                case "dimen": attr = new Attribute(AttributeType.DIMENSION, name, toDimen(value)); break;
                case "color": attr = new Attribute(AttributeType.COLOR, name, toColor(value)); break;
                case "string": attr = new Attribute(AttributeType.STRING, name, value); break;
                default:
                    throw new RuntimeException("Failed to parse unknown resource type '" + type + "'");
            }
            attributes.add(attr);
        }
        
        return attributes;
    }

}
