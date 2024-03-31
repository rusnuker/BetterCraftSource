// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Function;

public interface Int2DoubleFunction extends Function<Integer, Double>
{
    double put(final int p0, final double p1);
    
    double get(final int p0);
    
    double remove(final int p0);
    
    boolean containsKey(final int p0);
    
    void defaultReturnValue(final double p0);
    
    double defaultReturnValue();
}
