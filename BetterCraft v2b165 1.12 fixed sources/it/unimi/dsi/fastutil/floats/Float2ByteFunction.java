// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.Function;

public interface Float2ByteFunction extends Function<Float, Byte>
{
    byte put(final float p0, final byte p1);
    
    byte get(final float p0);
    
    byte remove(final float p0);
    
    boolean containsKey(final float p0);
    
    void defaultReturnValue(final byte p0);
    
    byte defaultReturnValue();
}
