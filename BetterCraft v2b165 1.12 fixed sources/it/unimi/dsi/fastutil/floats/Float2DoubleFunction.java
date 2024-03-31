// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.Function;

public interface Float2DoubleFunction extends Function<Float, Double>
{
    double put(final float p0, final double p1);
    
    double get(final float p0);
    
    double remove(final float p0);
    
    boolean containsKey(final float p0);
    
    void defaultReturnValue(final double p0);
    
    double defaultReturnValue();
}
