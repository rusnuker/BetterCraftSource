// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.Function;

public interface Byte2ShortFunction extends Function<Byte, Short>
{
    short put(final byte p0, final short p1);
    
    short get(final byte p0);
    
    short remove(final byte p0);
    
    boolean containsKey(final byte p0);
    
    void defaultReturnValue(final short p0);
    
    short defaultReturnValue();
}
