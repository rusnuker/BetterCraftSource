// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.Function;

public interface Short2DoubleFunction extends Function<Short, Double>
{
    double put(final short p0, final double p1);
    
    double get(final short p0);
    
    double remove(final short p0);
    
    boolean containsKey(final short p0);
    
    void defaultReturnValue(final double p0);
    
    double defaultReturnValue();
}
