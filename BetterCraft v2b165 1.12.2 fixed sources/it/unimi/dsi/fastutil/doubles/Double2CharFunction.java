// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.Function;

public interface Double2CharFunction extends Function<Double, Character>
{
    char put(final double p0, final char p1);
    
    char get(final double p0);
    
    char remove(final double p0);
    
    boolean containsKey(final double p0);
    
    void defaultReturnValue(final char p0);
    
    char defaultReturnValue();
}
