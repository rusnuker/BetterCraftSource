// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.Function;

public interface Short2FloatFunction extends Function<Short, Float>
{
    float put(final short p0, final float p1);
    
    float get(final short p0);
    
    float remove(final short p0);
    
    boolean containsKey(final short p0);
    
    void defaultReturnValue(final float p0);
    
    float defaultReturnValue();
}
