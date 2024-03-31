// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.Function;

public interface Char2ByteFunction extends Function<Character, Byte>
{
    byte put(final char p0, final byte p1);
    
    byte get(final char p0);
    
    byte remove(final char p0);
    
    boolean containsKey(final char p0);
    
    void defaultReturnValue(final byte p0);
    
    byte defaultReturnValue();
}
