// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.Function;

public interface Float2CharFunction extends Function<Float, Character>
{
    char put(final float p0, final char p1);
    
    char get(final float p0);
    
    char remove(final float p0);
    
    boolean containsKey(final float p0);
    
    void defaultReturnValue(final char p0);
    
    char defaultReturnValue();
}
