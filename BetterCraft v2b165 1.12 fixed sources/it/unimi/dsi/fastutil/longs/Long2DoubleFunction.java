// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Function;

public interface Long2DoubleFunction extends Function<Long, Double>
{
    double put(final long p0, final double p1);
    
    double get(final long p0);
    
    double remove(final long p0);
    
    boolean containsKey(final long p0);
    
    void defaultReturnValue(final double p0);
    
    double defaultReturnValue();
}
