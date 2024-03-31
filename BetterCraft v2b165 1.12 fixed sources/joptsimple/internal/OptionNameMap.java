// 
// Decompiled by Procyon v0.6.0
// 

package joptsimple.internal;

import java.util.Map;

public interface OptionNameMap<V>
{
    boolean contains(final String p0);
    
    V get(final String p0);
    
    void put(final String p0, final V p1);
    
    void putAll(final Iterable<String> p0, final V p1);
    
    void remove(final String p0);
    
    Map<String, V> toJavaUtilMap();
}
