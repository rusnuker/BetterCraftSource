// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.Function;

public interface Double2DoubleFunction extends Function<Double, Double>
{
    double put(final double p0, final double p1);
    
    double get(final double p0);
    
    double remove(final double p0);
    
    boolean containsKey(final double p0);
    
    void defaultReturnValue(final double p0);
    
    double defaultReturnValue();
}
