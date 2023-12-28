// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Function;

public interface Long2FloatFunction extends Function<Long, Float>
{
    float put(final long p0, final float p1);
    
    float get(final long p0);
    
    float remove(final long p0);
    
    boolean containsKey(final long p0);
    
    void defaultReturnValue(final float p0);
    
    float defaultReturnValue();
}
