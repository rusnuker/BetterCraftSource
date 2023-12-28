// 
// Decompiled by Procyon v0.6.0
// 

package joptsimple.internal;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

public class SimpleOptionNameMap<V> implements OptionNameMap<V>
{
    private final Map<String, V> map;
    
    public SimpleOptionNameMap() {
        this.map = new HashMap<String, V>();
    }
    
    @Override
    public boolean contains(final String key) {
        return this.map.containsKey(key);
    }
    
    @Override
    public V get(final String key) {
        return this.map.get(key);
    }
    
    @Override
    public void put(final String key, final V newValue) {
        this.map.put(key, newValue);
    }
    
    @Override
    public void putAll(final Iterable<String> keys, final V newValue) {
        for (final String each : keys) {
            this.map.put(each, newValue);
        }
    }
    
    @Override
    public void remove(final String key) {
        this.map.remove(key);
    }
    
    @Override
    public Map<String, V> toJavaUtilMap() {
        return new HashMap<String, V>((Map<? extends String, ? extends V>)this.map);
    }
}
