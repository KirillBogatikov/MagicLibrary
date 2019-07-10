package org.kllbff.magic.styling;

public class Attribute {
    private String namespace;
    private String name;
    private Object value;
    private AttributeType type;
    
    public Attribute(AttributeType type, String namespace, String name) {
        this.type = type;
        this.namespace = namespace;
        this.name = name;
    }
    
    public String getNamespace() {
        return namespace;
    }
    
    public String getName() {
        return name;
    }
    
    public AttributeType getType() {
        return type;
    }
    
    public boolean is(AttributeType some) {
        return some.equals(type);
    }
    
    public boolean is(String namespace, String name) {
        return this.namespace.equals(namespace) && this.name.equals(name);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T get() {
        return (T)value;
    }
}
