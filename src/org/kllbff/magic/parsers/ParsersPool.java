package org.kllbff.magic.parsers;

import java.io.Reader;

import org.kllbff.magic.exceptions.UnknownResourceTypeException;
import org.kllbff.magic.parsers.json.JsonAbstractParser;
import org.kllbff.magic.parsers.xml.XmlAbstractParser;

public class ParsersPool<E> {
    private JsonAbstractParser<E> jp;
    private XmlAbstractParser<E> xp;
        
    public ParsersPool(JsonAbstractParser<E> jp, XmlAbstractParser<E> xp) {
        this.jp = jp;
        this.xp = xp;
    }

    public E parseResource(Reader reader, String path) throws Exception {
        if(path.endsWith(".json")) {
            return this.jp.parseResource(reader);
        } else if(path.endsWith(".xml")) {
            return this.xp.parseResource(reader);
        } else {
            throw new UnknownResourceTypeException(path); 
        }
    }
}
