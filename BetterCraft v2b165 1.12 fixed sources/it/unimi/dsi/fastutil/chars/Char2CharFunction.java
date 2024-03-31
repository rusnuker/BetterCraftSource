// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.Function;

public interface Char2CharFunction extends Function<Character, Character>
{
    char put(final char p0, final char p1);
    
    char get(final char p0);
    
    char remove(final char p0);
    
    boolean containsKey(final char p0);
    
    void defaultReturnValue(final char p0);
    
    char defaultReturnValue();
}
