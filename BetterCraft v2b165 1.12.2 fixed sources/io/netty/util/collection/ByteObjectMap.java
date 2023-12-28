// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.collection;

import java.util.Map;

public interface ByteObjectMap<V> extends Map<Byte, V>
{
    V get(final byte p0);
    
    V put(final byte p0, final V p1);
    
    V remove(final byte p0);
    
    Iterable<PrimitiveEntry<V>> entries();
    
    boolean containsKey(final byte p0);
    
    public interface PrimitiveEntry<V>
    {
        byte key();
        
        V value();
        
        void setValue(final V p0);
    }
}
