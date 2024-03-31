// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.Function;

public interface Byte2CharFunction extends Function<Byte, Character>
{
    char put(final byte p0, final char p1);
    
    char get(final byte p0);
    
    char remove(final byte p0);
    
    boolean containsKey(final byte p0);
    
    void defaultReturnValue(final char p0);
    
    char defaultReturnValue();
}
