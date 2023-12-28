// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.Function;

public interface Double2ByteFunction extends Function<Double, Byte>
{
    byte put(final double p0, final byte p1);
    
    byte get(final double p0);
    
    byte remove(final double p0);
    
    boolean containsKey(final double p0);
    
    void defaultReturnValue(final byte p0);
    
    byte defaultReturnValue();
}
