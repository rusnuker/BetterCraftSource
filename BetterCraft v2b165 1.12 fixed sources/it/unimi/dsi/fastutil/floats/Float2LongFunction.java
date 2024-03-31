// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.Function;

public interface Float2LongFunction extends Function<Float, Long>
{
    long put(final float p0, final long p1);
    
    long get(final float p0);
    
    long remove(final float p0);
    
    boolean containsKey(final float p0);
    
    void defaultReturnValue(final long p0);
    
    long defaultReturnValue();
}
