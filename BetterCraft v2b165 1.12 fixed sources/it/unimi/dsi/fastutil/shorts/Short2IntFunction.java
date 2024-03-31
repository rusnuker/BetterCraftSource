// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.Function;

public interface Short2IntFunction extends Function<Short, Integer>
{
    int put(final short p0, final int p1);
    
    int get(final short p0);
    
    int remove(final short p0);
    
    boolean containsKey(final short p0);
    
    void defaultReturnValue(final int p0);
    
    int defaultReturnValue();
}
