// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Function;

public interface Object2BooleanFunction<K> extends Function<K, Boolean>
{
    boolean put(final K p0, final boolean p1);
    
    boolean getBoolean(final Object p0);
    
    boolean removeBoolean(final Object p0);
    
    void defaultReturnValue(final boolean p0);
    
    boolean defaultReturnValue();
}
