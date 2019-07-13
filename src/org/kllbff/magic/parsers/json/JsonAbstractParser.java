package org.kllbff.magic.parsers.json;

import java.io.Reader;

import org.kllbff.magic.app.Resources;
import org.kllbff.magic.exceptions.ParsingException;
import org.kllbff.magic.exceptions.ResourceNotFoundException;
import org.kllbff.magic.parsers.AbstractParser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class JsonAbstractParser<E> extends AbstractParser<E> {
    protected JsonParser jp;
    
    public JsonAbstractParser(Resources resources) {
        super(resources);
        this.jp = new JsonParser();
    }
    
    protected void checkProp(JsonObject object, String property, String target) {
        if(!object.has(property)) {
            throw new ParsingException("Failed to find required property '" + property + "' for " + target);
        }
    }
    
    public String getProp(JsonObject obj, String key, String defValue) {
        if(obj.has(key)) {
            return obj.get(key).getAsString();
        }
        return defValue;
    }

    @Override
    public abstract E parseResource(Reader reader) throws ResourceNotFoundException;
}
