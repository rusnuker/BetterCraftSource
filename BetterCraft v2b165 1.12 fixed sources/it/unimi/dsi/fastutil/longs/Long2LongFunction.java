// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Function;

public interface Long2LongFunction extends Function<Long, Long>
{
    long put(final long p0, final long p1);
    
    long get(final long p0);
    
    long remove(final long p0);
    
    boolean containsKey(final long p0);
    
    void defaultReturnValue(final long p0);
    
    long defaultReturnValue();
}
