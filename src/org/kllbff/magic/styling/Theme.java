package org.kllbff.magic.styling;

public class Theme {
    private String name;
    private AttributeSet attributes;
    
    public Theme(String name, AttributeSet attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }
    
    public AttributeSet getAliases() {
        return attributes;
    }
}
