// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.function.ToIntFunction;
import com.viaversion.viaversion.libs.fastutil.Function;

@FunctionalInterface
public interface Object2IntFunction<K> extends Function<K, Integer>, ToIntFunction<K>
{
    default int applyAsInt(final K operand) {
        return this.getInt(operand);
    }
    
    default int put(final K key, final int value) {
        throw new UnsupportedOperationException();
    }
    
    int getInt(final Object p0);
    
    default int removeInt(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Integer put(final K key, final Integer value) {
        final K k = key;
        final boolean containsKey = this.containsKey(k);
        final int v = this.put(k, (int)value);
        return containsKey ? Integer.valueOf(v) : null;
    }
    
    @Deprecated
    default Integer get(final Object key) {
        final Object k = key;
        final int v = this.getInt(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Integer.valueOf(v) : null;
    }
    
    @Deprecated
    default Integer remove(final Object key) {
        final Object k = key;
        return this.containsKey(k) ? Integer.valueOf(this.removeInt(k)) : null;
    }
    
    default void defaultReturnValue(final int rv) {
        throw new UnsupportedOperationException();
    }
    
    default int defaultReturnValue() {
        return 0;
    }
}
