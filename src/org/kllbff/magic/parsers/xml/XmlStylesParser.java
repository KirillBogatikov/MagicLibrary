package org.kllbff.magic.parsers.xml;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.kllbff.magic.exceptions.ParsingException;
import org.kllbff.magic.styling.Attribute;
import org.kllbff.magic.styling.AttributeSet;
import org.kllbff.magic.styling.AttributeType;
import org.kllbff.magic.styling.Theme;
import org.xmlpull.v1.XmlPullParserException;

public class XmlStylesParser extends XmlAbstractParser<List<Theme>> {

    public XmlStylesParser() throws XmlPullParserException {
        super(null);
        this.linksAllowed = false;
    }
    
    private Theme parseTheme() throws XmlPullParserException, IOException {
        checkAttr("name", "theme");
        String name = getAttr("name", null);
        AttributeSet attributes = new AttributeSet();
        
        String tag;
        while((tag = nextTag("theme")) != null) {
            checkAttr("link", "theme alias");
            
            String link = getAttr("link", null);
            
            AttributeType attrType;
            switch(tag) {
                case "drawable": attrType = AttributeType.DRAWABLE; break;
                case "color": attrType = AttributeType.COLOR; break;
                case "dimen": attrType = AttributeType.DIMENSION; break;
                case "state-list": attrType = AttributeType.STATE_LIST; break;
                case "string": attrType = AttributeType.STRING; break;
                default:
                    throw new RuntimeException("Cannot parse tag <" + tag + "> as theme alias"); 
            }
            
            String value = getAttr("source", null);
            if(value == null) {
                value = xpp.nextText();
            }
            
            attributes.add(new Attribute(attrType, link, value));
        }
        
        return new Theme(name, attributes);
    }

    @Override
    public List<Theme> parseResource(Reader reader) throws IOException, XmlPullParserException {
        xpp.setInput(reader);
        List<Theme> list = new ArrayList<>();
        
        if(!nextTag("").equals("resources")) {
            throw new ParsingException("Values file must be started with <resources> tag");
        }
        
        while(nextTag("resources") != null) {
            list.add(parseTheme());
        }
        
        return list;
    }

}
