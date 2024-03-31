// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Function;

public interface Int2CharFunction extends Function<Integer, Character>
{
    char put(final int p0, final char p1);
    
    char get(final int p0);
    
    char remove(final int p0);
    
    boolean containsKey(final int p0);
    
    void defaultReturnValue(final char p0);
    
    char defaultReturnValue();
}
