// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.Function;

public interface Byte2ReferenceFunction<V> extends Function<Byte, V>
{
    V put(final byte p0, final V p1);
    
    V get(final byte p0);
    
    V remove(final byte p0);
    
    boolean containsKey(final byte p0);
    
    void defaultReturnValue(final V p0);
    
    V defaultReturnValue();
}
