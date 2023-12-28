// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.Function;

public interface Byte2FloatFunction extends Function<Byte, Float>
{
    float put(final byte p0, final float p1);
    
    float get(final byte p0);
    
    float remove(final byte p0);
    
    boolean containsKey(final byte p0);
    
    void defaultReturnValue(final float p0);
    
    float defaultReturnValue();
}
