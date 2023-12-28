// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.Function;

public interface Char2ShortFunction extends Function<Character, Short>
{
    short put(final char p0, final short p1);
    
    short get(final char p0);
    
    short remove(final char p0);
    
    boolean containsKey(final char p0);
    
    void defaultReturnValue(final short p0);
    
    short defaultReturnValue();
}
