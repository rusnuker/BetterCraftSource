// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.Function;

public interface Short2ObjectFunction<V> extends Function<Short, V>
{
    V put(final short p0, final V p1);
    
    V get(final short p0);
    
    V remove(final short p0);
    
    boolean containsKey(final short p0);
    
    void defaultReturnValue(final V p0);
    
    V defaultReturnValue();
}
