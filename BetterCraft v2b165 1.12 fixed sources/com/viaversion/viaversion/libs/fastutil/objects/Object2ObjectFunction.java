// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.Function;

@FunctionalInterface
public interface Object2ObjectFunction<K, V> extends Function<K, V>
{
    default V put(final K key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    V get(final Object p0);
    
    default V remove(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    default void defaultReturnValue(final V rv) {
        throw new UnsupportedOperationException();
    }
    
    default V defaultReturnValue() {
        return null;
    }
}
