// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Function;

public interface Long2CharFunction extends Function<Long, Character>
{
    char put(final long p0, final char p1);
    
    char get(final long p0);
    
    char remove(final long p0);
    
    boolean containsKey(final long p0);
    
    void defaultReturnValue(final char p0);
    
    char defaultReturnValue();
}
