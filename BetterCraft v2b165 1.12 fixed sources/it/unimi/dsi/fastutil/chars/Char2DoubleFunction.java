// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.Function;

public interface Char2DoubleFunction extends Function<Character, Double>
{
    double put(final char p0, final double p1);
    
    double get(final char p0);
    
    double remove(final char p0);
    
    boolean containsKey(final char p0);
    
    void defaultReturnValue(final double p0);
    
    double defaultReturnValue();
}
