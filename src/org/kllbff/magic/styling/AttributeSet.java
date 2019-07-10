package org.kllbff.magic.styling;

import java.util.ArrayList;

public class AttributeSet {    
    private AttributeType filter;
    private ArrayList<Attribute> attributes;
    
    public AttributeSet() {
        attributes = new ArrayList<>();
    }
    
    public void filter(AttributeType filter) {
        this.filter = filter;
    }
    
    public <T> T get(String namespace, String name) {
        for(Attribute attr : attributes) {
            if(attr.is(filter) && attr.is(namespace, name)) {
                return attr.get();
            }
        }
        return null;
    }
}
