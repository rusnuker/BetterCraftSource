// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Function;

public interface Object2ShortFunction<K> extends Function<K, Short>
{
    short put(final K p0, final short p1);
    
    short getShort(final Object p0);
    
    short removeShort(final Object p0);
    
    void defaultReturnValue(final short p0);
    
    short defaultReturnValue();
}
