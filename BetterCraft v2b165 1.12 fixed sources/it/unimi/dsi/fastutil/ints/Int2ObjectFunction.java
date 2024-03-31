// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Function;

public interface Int2ObjectFunction<V> extends Function<Integer, V>
{
    V put(final int p0, final V p1);
    
    V get(final int p0);
    
    V remove(final int p0);
    
    boolean containsKey(final int p0);
    
    void defaultReturnValue(final V p0);
    
    V defaultReturnValue();
}
