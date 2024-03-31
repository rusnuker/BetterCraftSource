// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.Function;

public interface Double2IntFunction extends Function<Double, Integer>
{
    int put(final double p0, final int p1);
    
    int get(final double p0);
    
    int remove(final double p0);
    
    boolean containsKey(final double p0);
    
    void defaultReturnValue(final int p0);
    
    int defaultReturnValue();
}
