package org.kllbff.magic.app;

import java.io.Serializable;
import java.util.HashMap;

public class Bundle implements Serializable {
    private static final long serialVersionUID = -3025473724866755179L;
    
    protected HashMap<Object, Object> map;
    
    public Bundle() {
        map = new HashMap<>();
    }
    
    @SuppressWarnings("unchecked")
    public <V> V get(Object key) {
        return (V)map.get(key);
    }
    
    public boolean contains(Object key) {
        return map.containsKey(key);
    }
    
    public void set(Object key, Object value) {
        map.put(key, value);
    }
}
