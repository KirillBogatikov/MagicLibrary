package org.kllbff.magic.parsers.xml;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.kllbff.magic.exceptions.ParsingException;
import org.xmlpull.v1.XmlPullParserException;

public class XmlLoggingParser extends XmlAbstractParser<Map<String, PrintStream>> {

    public XmlLoggingParser() throws XmlPullParserException {
        super(null);
        this.linksAllowed = false;
    }

    @Override
    public Map<String, PrintStream> parseResource(Reader reader) throws IOException, XmlPullParserException {
        HashMap<String, PrintStream> streams = new HashMap<String, PrintStream>();
        
        if(!nextTag(null).equals("logging")) {
            throw new ParsingException("Failed to parse logging configuration: file must be start with <logging> tag");
        }
        
        String tag;
        while((tag = nextTag("logging")) != null) {
            checkAttr("target", "logging configuration");
            String target = getAttr("target", "logging configuration");
            switch(target) {
                case "stdout": streams.put(tag, System.out); break;
                case "stderr": streams.put(tag, System.err); break;
                default: 
                    streams.put(tag, new PrintStream(target)); break;
            }
        }
        
        return streams;
    }

}
