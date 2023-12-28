// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.Function;

public interface Float2FloatFunction extends Function<Float, Float>
{
    float put(final float p0, final float p1);
    
    float get(final float p0);
    
    float remove(final float p0);
    
    boolean containsKey(final float p0);
    
    void defaultReturnValue(final float p0);
    
    float defaultReturnValue();
}
