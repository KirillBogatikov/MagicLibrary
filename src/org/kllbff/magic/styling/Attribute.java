package org.kllbff.magic.styling;

public class Attribute {
    public static final String MAGIC_ATTRIBUTES_NAMESPACE = "";
    
    private String namespace;
    private String name;
    private Object value;
    private AttributeType type;
    
    public Attribute(AttributeType type, String namespace, String name, Object value) {
        this.type = type;
        this.namespace = namespace;
        this.name = name;
        this.value = value;
    }
    
    public Attribute(AttributeType type, String name, Object value) {
        this(type, MAGIC_ATTRIBUTES_NAMESPACE, name, value);
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
        return some == null || some.equals(type);
    }
    
    public boolean is(String namespace, String name) {
        return this.namespace.equals(namespace) && this.name.equals(name);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T get() {
        return (T)value;
    }
    
    public void set(Object object, AttributeType type) {
        this.value = object;
        this.type = type;
    }
    
    public boolean equals(Object other) {
        try {
            Attribute attr = (Attribute)other;
            return (attr.type == type) && (attr.name.equals(name)) && (attr.namespace.equals(namespace));
        } catch(ClassCastException e) {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return type + " " + namespace + ":" + name + " = " + value;
    }
}
