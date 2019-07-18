package org.kllbff.magic.parsers.xml;

import java.io.IOException;
import java.io.Reader;

import org.kllbff.magic.exceptions.ParsingException;
import org.kllbff.magic.styling.Attribute;
import org.kllbff.magic.styling.AttributeSet;
import org.kllbff.magic.styling.AttributeType;
import org.xmlpull.v1.XmlPullParserException;

public class XmlValuesParser extends XmlAbstractParser<AttributeSet> {

    public XmlValuesParser() throws XmlPullParserException {
        super(null);
        this.linksAllowed = false;
    }

    @Override
    public AttributeSet parseResource(Reader reader) throws IOException, XmlPullParserException {
        xpp.setInput(reader);
        
        String tag = nextTag("");
        if(!tag.equals("resources")) {
            throw new ParsingException("Values file must be started with <resources> tag");
        }
        
        AttributeSet attributes = new AttributeSet();
        while((tag = nextTag("resources")) != null) {
            checkAttr("name", "resources item");
            
            String name = getAttr("name", null);
            String value = getAttr("value", null);
            if(value == null) {
                value = xpp.nextText();
            }
            
            switch(tag) {
                case "dimen": attributes.add(new Attribute(AttributeType.DIMENSION, name, toDimen(value))); break;
                case "color": attributes.add(new Attribute(AttributeType.COLOR, name, toColor(value))); break;
                case "string": attributes.add(new Attribute(AttributeType.STRING, name, value)); break;
                default:
                    throw new RuntimeException("Failed to parse unknown resource tag <" + tag + ">");
            }
        }
        
        return attributes;
    }

}
