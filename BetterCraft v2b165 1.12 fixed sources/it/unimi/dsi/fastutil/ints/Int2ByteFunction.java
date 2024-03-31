// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Function;

public interface Int2ByteFunction extends Function<Integer, Byte>
{
    byte put(final int p0, final byte p1);
    
    byte get(final int p0);
    
    byte remove(final int p0);
    
    boolean containsKey(final int p0);
    
    void defaultReturnValue(final byte p0);
    
    byte defaultReturnValue();
}
