// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.Function;

public interface Short2ByteFunction extends Function<Short, Byte>
{
    byte put(final short p0, final byte p1);
    
    byte get(final short p0);
    
    byte remove(final short p0);
    
    boolean containsKey(final short p0);
    
    void defaultReturnValue(final byte p0);
    
    byte defaultReturnValue();
}
