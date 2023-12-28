// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.Function;

public interface Float2ShortFunction extends Function<Float, Short>
{
    short put(final float p0, final short p1);
    
    short get(final float p0);
    
    short remove(final float p0);
    
    boolean containsKey(final float p0);
    
    void defaultReturnValue(final short p0);
    
    short defaultReturnValue();
}
