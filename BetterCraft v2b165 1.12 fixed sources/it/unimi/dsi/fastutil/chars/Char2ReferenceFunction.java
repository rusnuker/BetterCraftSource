// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.Function;

public interface Char2ReferenceFunction<V> extends Function<Character, V>
{
    V put(final char p0, final V p1);
    
    V get(final char p0);
    
    V remove(final char p0);
    
    boolean containsKey(final char p0);
    
    void defaultReturnValue(final V p0);
    
    V defaultReturnValue();
}
