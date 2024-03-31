// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Function;

public interface Long2ObjectFunction<V> extends Function<Long, V>
{
    V put(final long p0, final V p1);
    
    V get(final long p0);
    
    V remove(final long p0);
    
    boolean containsKey(final long p0);
    
    void defaultReturnValue(final V p0);
    
    V defaultReturnValue();
}
