// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.collection;

import java.util.Map;

public interface LongObjectMap<V> extends Map<Long, V>
{
    V get(final long p0);
    
    V put(final long p0, final V p1);
    
    V remove(final long p0);
    
    Iterable<PrimitiveEntry<V>> entries();
    
    boolean containsKey(final long p0);
    
    public interface PrimitiveEntry<V>
    {
        long key();
        
        V value();
        
        void setValue(final V p0);
    }
}
