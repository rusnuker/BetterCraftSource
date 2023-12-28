// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.collection;

import java.util.Map;

public interface IntObjectMap<V> extends Map<Integer, V>
{
    V get(final int p0);
    
    V put(final int p0, final V p1);
    
    V remove(final int p0);
    
    Iterable<PrimitiveEntry<V>> entries();
    
    boolean containsKey(final int p0);
    
    public interface PrimitiveEntry<V>
    {
        int key();
        
        V value();
        
        void setValue(final V p0);
    }
}
