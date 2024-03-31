// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Function;

public interface Int2FloatFunction extends Function<Integer, Float>
{
    float put(final int p0, final float p1);
    
    float get(final int p0);
    
    float remove(final int p0);
    
    boolean containsKey(final int p0);
    
    void defaultReturnValue(final float p0);
    
    float defaultReturnValue();
}
