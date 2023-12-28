// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Function;

public interface Int2LongFunction extends Function<Integer, Long>
{
    long put(final int p0, final long p1);
    
    long get(final int p0);
    
    long remove(final int p0);
    
    boolean containsKey(final int p0);
    
    void defaultReturnValue(final long p0);
    
    long defaultReturnValue();
}
