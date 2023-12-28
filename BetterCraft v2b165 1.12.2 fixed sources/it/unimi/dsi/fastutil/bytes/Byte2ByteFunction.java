// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.Function;

public interface Byte2ByteFunction extends Function<Byte, Byte>
{
    byte put(final byte p0, final byte p1);
    
    byte get(final byte p0);
    
    byte remove(final byte p0);
    
    boolean containsKey(final byte p0);
    
    void defaultReturnValue(final byte p0);
    
    byte defaultReturnValue();
}
