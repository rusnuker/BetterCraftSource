// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.Function;

public interface Byte2IntFunction extends Function<Byte, Integer>
{
    int put(final byte p0, final int p1);
    
    int get(final byte p0);
    
    int remove(final byte p0);
    
    boolean containsKey(final byte p0);
    
    void defaultReturnValue(final int p0);
    
    int defaultReturnValue();
}
