// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Function;

public interface Object2IntFunction<K> extends Function<K, Integer>
{
    int put(final K p0, final int p1);
    
    int getInt(final Object p0);
    
    int removeInt(final Object p0);
    
    void defaultReturnValue(final int p0);
    
    int defaultReturnValue();
}
