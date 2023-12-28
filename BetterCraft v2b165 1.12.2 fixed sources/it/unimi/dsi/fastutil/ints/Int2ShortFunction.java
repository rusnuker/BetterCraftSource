// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Function;

public interface Int2ShortFunction extends Function<Integer, Short>
{
    short put(final int p0, final short p1);
    
    short get(final int p0);
    
    short remove(final int p0);
    
    boolean containsKey(final int p0);
    
    void defaultReturnValue(final short p0);
    
    short defaultReturnValue();
}
