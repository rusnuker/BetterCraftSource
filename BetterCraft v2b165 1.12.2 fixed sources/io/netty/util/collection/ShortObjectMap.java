// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.collection;

import java.util.Map;

public interface ShortObjectMap<V> extends Map<Short, V>
{
    V get(final short p0);
    
    V put(final short p0, final V p1);
    
    V remove(final short p0);
    
    Iterable<PrimitiveEntry<V>> entries();
    
    boolean containsKey(final short p0);
    
    public interface PrimitiveEntry<V>
    {
        short key();
        
        V value();
        
        void setValue(final V p0);
    }
}
