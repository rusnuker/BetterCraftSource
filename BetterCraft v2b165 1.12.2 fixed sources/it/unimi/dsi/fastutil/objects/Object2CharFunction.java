// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Function;

public interface Object2CharFunction<K> extends Function<K, Character>
{
    char put(final K p0, final char p1);
    
    char getChar(final Object p0);
    
    char removeChar(final Object p0);
    
    void defaultReturnValue(final char p0);
    
    char defaultReturnValue();
}
