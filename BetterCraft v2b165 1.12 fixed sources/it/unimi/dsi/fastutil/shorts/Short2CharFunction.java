// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.Function;

public interface Short2CharFunction extends Function<Short, Character>
{
    char put(final short p0, final char p1);
    
    char get(final short p0);
    
    char remove(final short p0);
    
    boolean containsKey(final short p0);
    
    void defaultReturnValue(final char p0);
    
    char defaultReturnValue();
}
