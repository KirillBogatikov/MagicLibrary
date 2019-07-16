package org.kllbff.magic.parsers.json;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.kllbff.magic.exceptions.ParsingException;
import org.kllbff.magic.styling.Attribute;
import org.kllbff.magic.styling.AttributeSet;
import org.kllbff.magic.styling.AttributeType;
import org.kllbff.magic.styling.Theme;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonStylesParser extends JsonAbstractParser<List<Theme>> {
    
    public JsonStylesParser() {
        super(null);
        this.linksAllowed = false;
    }
    
    private void parseAliases(JsonArray array, AttributeSet attributes) {
        for(JsonElement element : array) {
            JsonObject alias = element.getAsJsonObject();
            
            checkProp(alias, "type", "theme alias");
            checkProp(alias, "link", "theme alias");
            checkProp(alias, "source", "theme alias");
            
            String type = getProp(alias, "type", null);
            String link = getProp(alias, "link", null);
            String source = getProp(alias, "source", null);
            
            AttributeType attrType;
            switch(type) {
                case "drawable":
                    attrType = AttributeType.DRAWABLE;
                break;
                case "color":
                    attrType = AttributeType.COLOR;
                break;
                case "string":
                    attrType = AttributeType.STRING;
                break;
                case "state-list":
                    attrType = AttributeType.STATE_LIST;
                break;
                case "dimen":
                    attrType = AttributeType.DIMENSION;
                break;
                default:
                    throw new ParsingException("Cannot parse unknown resource type '" + type + "'");
            }
            
            attributes.add(new Attribute(attrType, link, source));
        }
    }

    @Override
    public List<Theme> parseResource(Reader reader) {
        List<Theme> themes = new ArrayList<Theme>();
        JsonArray array = jp.parse(reader).getAsJsonArray();
        
        for(JsonElement element : array) {
            AttributeSet attributes = new AttributeSet();
            JsonObject item = element.getAsJsonObject();
            
            checkProp(item, "type", "theme");
            String type = getProp(item, "type", null);
            if(!type.equals("theme")) {
                throw new ParsingException("Failed to parse theme from '" + type + "' type, only 'theme' supported");
            }
            
            checkProp(item, "aliases", "theme");
            parseAliases(item.get("aliases").getAsJsonArray(), attributes);
            themes.add(new Theme(getProp(item, "name", "Theme-" + themes.size()), attributes));
        }
        return themes;
    }

}
