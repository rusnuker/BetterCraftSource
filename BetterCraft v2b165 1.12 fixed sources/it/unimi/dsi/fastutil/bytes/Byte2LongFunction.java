// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.Function;

public interface Byte2LongFunction extends Function<Byte, Long>
{
    long put(final byte p0, final long p1);
    
    long get(final byte p0);
    
    long remove(final byte p0);
    
    boolean containsKey(final byte p0);
    
    void defaultReturnValue(final long p0);
    
    long defaultReturnValue();
}
