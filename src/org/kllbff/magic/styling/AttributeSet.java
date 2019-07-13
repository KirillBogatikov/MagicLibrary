package org.kllbff.magic.styling;

import java.util.ArrayList;

public class AttributeSet {    
    private AttributeType filter;
    private ArrayList<Attribute> attributes;
    
    public AttributeSet() {
        attributes = new ArrayList<>();
    }
    
    public void add(Attribute attr) {
        this.attributes.add(attr);
    }
    
    public void filter(AttributeType filter) {
        this.filter = filter;
    }
    
    public Attribute get(String name) {
        for(Attribute attr : attributes) {
            if(attr.is(filter) && attr.is("", name)) {
                return attr;
            }
        }
        return null;
    }
    
    public boolean has(String name) {
        return get(name) != null;
    }
    
    public <T> T getValue(String name) {
        Attribute attr = get(name);
        if(attr == null) {
            return null;
        }
        return attr.get();
    }
}
