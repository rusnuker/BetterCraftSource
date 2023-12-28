// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.Function;

public interface Double2ShortFunction extends Function<Double, Short>
{
    short put(final double p0, final short p1);
    
    short get(final double p0);
    
    short remove(final double p0);
    
    boolean containsKey(final double p0);
    
    void defaultReturnValue(final short p0);
    
    short defaultReturnValue();
}
