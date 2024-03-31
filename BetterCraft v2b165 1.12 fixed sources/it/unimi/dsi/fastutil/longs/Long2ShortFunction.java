// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Function;

public interface Long2ShortFunction extends Function<Long, Short>
{
    short put(final long p0, final short p1);
    
    short get(final long p0);
    
    short remove(final long p0);
    
    boolean containsKey(final long p0);
    
    void defaultReturnValue(final short p0);
    
    short defaultReturnValue();
}
