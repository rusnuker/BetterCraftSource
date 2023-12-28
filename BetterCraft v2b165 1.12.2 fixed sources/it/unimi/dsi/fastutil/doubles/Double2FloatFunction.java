// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.Function;

public interface Double2FloatFunction extends Function<Double, Float>
{
    float put(final double p0, final float p1);
    
    float get(final double p0);
    
    float remove(final double p0);
    
    boolean containsKey(final double p0);
    
    void defaultReturnValue(final float p0);
    
    float defaultReturnValue();
}
