// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.Function;

public interface Double2BooleanFunction extends Function<Double, Boolean>
{
    boolean put(final double p0, final boolean p1);
    
    boolean get(final double p0);
    
    boolean remove(final double p0);
    
    boolean containsKey(final double p0);
    
    void defaultReturnValue(final boolean p0);
    
    boolean defaultReturnValue();
}
