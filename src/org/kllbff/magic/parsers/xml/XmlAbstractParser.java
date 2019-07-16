package org.kllbff.magic.parsers.xml;

import static org.xmlpull.v1.XmlPullParser.END_DOCUMENT;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.END_TAG;

import java.io.IOException;
import java.io.Reader;

import org.kllbff.magic.exceptions.ParsingException;
import org.kllbff.magic.exceptions.ResourceNotFoundException;
import org.kllbff.magic.parsers.AbstractParser;
import org.kllbff.magic.res.Resources;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public abstract class XmlAbstractParser<E> extends AbstractParser<E> {
    public static final XmlPullParserFactory factory;
    
    static {
        try {
            factory = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e) {
            throw new RuntimeException(e);
        }
    }
    
    protected XmlPullParser xpp;
    
    public XmlAbstractParser(Resources resources) throws XmlPullParserException {
        super(resources);
        xpp = factory.newPullParser();
    }
    
    protected void checkAttr(String attr, String target) {
        if(getAttr(attr, null) == null) {
            throw new ParsingException("Failed to find required attribute '" + attr + "' for " + target);
        }
    }
    
    protected String getAttr(String attr, String def) {
        String val = xpp.getAttributeValue("", attr);
        if(val == null) {
            return def;
        }
        return val;
    }
    
    protected String nextTag(String sourceTagName) throws XmlPullParserException, IOException {
        int e = xpp.getEventType();
        while(e != END_DOCUMENT) {
            if(e == END_TAG && xpp.getName().equals(sourceTagName)) {
                break;
            }
            e = xpp.next();
            if(e == START_TAG) {
                return xpp.getName();
            }
        }
        return null;
    }

    @Override
    public abstract E parseResource(Reader reader) throws IOException, XmlPullParserException, ResourceNotFoundException;

}
