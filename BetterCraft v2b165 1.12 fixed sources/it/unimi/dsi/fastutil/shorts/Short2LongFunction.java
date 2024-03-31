// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.Function;

public interface Short2LongFunction extends Function<Short, Long>
{
    long put(final short p0, final long p1);
    
    long get(final short p0);
    
    long remove(final short p0);
    
    boolean containsKey(final short p0);
    
    void defaultReturnValue(final long p0);
    
    long defaultReturnValue();
}
