// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Function;

public interface Reference2ObjectFunction<K, V> extends Function<K, V>
{
    void defaultReturnValue(final V p0);
    
    V defaultReturnValue();
}
