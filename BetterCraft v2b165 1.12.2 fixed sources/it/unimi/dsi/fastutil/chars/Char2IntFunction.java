// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.Function;

public interface Char2IntFunction extends Function<Character, Integer>
{
    int put(final char p0, final int p1);
    
    int get(final char p0);
    
    int remove(final char p0);
    
    boolean containsKey(final char p0);
    
    void defaultReturnValue(final int p0);
    
    int defaultReturnValue();
}
