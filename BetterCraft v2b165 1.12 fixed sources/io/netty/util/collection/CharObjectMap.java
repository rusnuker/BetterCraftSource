// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.collection;

import java.util.Map;

public interface CharObjectMap<V> extends Map<Character, V>
{
    V get(final char p0);
    
    V put(final char p0, final V p1);
    
    V remove(final char p0);
    
    Iterable<PrimitiveEntry<V>> entries();
    
    boolean containsKey(final char p0);
    
    public interface PrimitiveEntry<V>
    {
        char key();
        
        V value();
        
        void setValue(final V p0);
    }
}
