// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Function;

public interface Long2IntFunction extends Function<Long, Integer>
{
    int put(final long p0, final int p1);
    
    int get(final long p0);
    
    int remove(final long p0);
    
    boolean containsKey(final long p0);
    
    void defaultReturnValue(final int p0);
    
    int defaultReturnValue();
}
