// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.Function;

public interface Double2LongFunction extends Function<Double, Long>
{
    long put(final double p0, final long p1);
    
    long get(final double p0);
    
    long remove(final double p0);
    
    boolean containsKey(final double p0);
    
    void defaultReturnValue(final long p0);
    
    long defaultReturnValue();
}
