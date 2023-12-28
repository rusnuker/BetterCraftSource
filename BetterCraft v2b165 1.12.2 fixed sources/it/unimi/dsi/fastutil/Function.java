// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil;

public interface Function<K, V>
{
    V put(final K p0, final V p1);
    
    V get(final Object p0);
    
    boolean containsKey(final Object p0);
    
    V remove(final Object p0);
    
    int size();
    
    void clear();
}
