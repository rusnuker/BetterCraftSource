// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Function;

public interface Long2ByteFunction extends Function<Long, Byte>
{
    byte put(final long p0, final byte p1);
    
    byte get(final long p0);
    
    byte remove(final long p0);
    
    boolean containsKey(final long p0);
    
    void defaultReturnValue(final byte p0);
    
    byte defaultReturnValue();
}
