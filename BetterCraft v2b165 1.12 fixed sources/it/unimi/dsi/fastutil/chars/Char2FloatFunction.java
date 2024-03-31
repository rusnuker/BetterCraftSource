// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.Function;

public interface Char2FloatFunction extends Function<Character, Float>
{
    float put(final char p0, final float p1);
    
    float get(final char p0);
    
    float remove(final char p0);
    
    boolean containsKey(final char p0);
    
    void defaultReturnValue(final float p0);
    
    float defaultReturnValue();
}
