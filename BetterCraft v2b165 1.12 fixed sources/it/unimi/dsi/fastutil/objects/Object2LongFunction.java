// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Function;

public interface Object2LongFunction<K> extends Function<K, Long>
{
    long put(final K p0, final long p1);
    
    long getLong(final Object p0);
    
    long removeLong(final Object p0);
    
    void defaultReturnValue(final long p0);
    
    long defaultReturnValue();
}
