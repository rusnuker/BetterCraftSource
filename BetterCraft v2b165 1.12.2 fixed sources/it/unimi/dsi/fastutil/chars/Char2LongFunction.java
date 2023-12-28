// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.Function;

public interface Char2LongFunction extends Function<Character, Long>
{
    long put(final char p0, final long p1);
    
    long get(final char p0);
    
    long remove(final char p0);
    
    boolean containsKey(final char p0);
    
    void defaultReturnValue(final long p0);
    
    long defaultReturnValue();
}
