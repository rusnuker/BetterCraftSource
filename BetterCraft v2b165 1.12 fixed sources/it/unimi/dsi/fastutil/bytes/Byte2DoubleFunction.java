// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.Function;

public interface Byte2DoubleFunction extends Function<Byte, Double>
{
    double put(final byte p0, final double p1);
    
    double get(final byte p0);
    
    double remove(final byte p0);
    
    boolean containsKey(final byte p0);
    
    void defaultReturnValue(final double p0);
    
    double defaultReturnValue();
}
