// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.Function;

public interface Double2ObjectFunction<V> extends Function<Double, V>
{
    V put(final double p0, final V p1);
    
    V get(final double p0);
    
    V remove(final double p0);
    
    boolean containsKey(final double p0);
    
    void defaultReturnValue(final V p0);
    
    V defaultReturnValue();
}
