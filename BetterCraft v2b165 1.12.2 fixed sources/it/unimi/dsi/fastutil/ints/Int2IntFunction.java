// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Function;

public interface Int2IntFunction extends Function<Integer, Integer>
{
    int put(final int p0, final int p1);
    
    int get(final int p0);
    
    int remove(final int p0);
    
    boolean containsKey(final int p0);
    
    void defaultReturnValue(final int p0);
    
    int defaultReturnValue();
}
