// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.Function;

public interface Char2BooleanFunction extends Function<Character, Boolean>
{
    boolean put(final char p0, final boolean p1);
    
    boolean get(final char p0);
    
    boolean remove(final char p0);
    
    boolean containsKey(final char p0);
    
    void defaultReturnValue(final boolean p0);
    
    boolean defaultReturnValue();
}
