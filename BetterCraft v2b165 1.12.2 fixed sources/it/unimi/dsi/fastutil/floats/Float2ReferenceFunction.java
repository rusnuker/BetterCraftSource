// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.Function;

public interface Float2ReferenceFunction<V> extends Function<Float, V>
{
    V put(final float p0, final V p1);
    
    V get(final float p0);
    
    V remove(final float p0);
    
    boolean containsKey(final float p0);
    
    void defaultReturnValue(final V p0);
    
    V defaultReturnValue();
}
