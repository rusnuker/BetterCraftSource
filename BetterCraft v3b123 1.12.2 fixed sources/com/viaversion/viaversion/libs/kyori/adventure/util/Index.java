// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.util;

import org.jetbrains.annotations.Contract;
import java.util.NoSuchElementException;
import org.jetbrains.annotations.Nullable;
import java.util.Set;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;
import java.util.HashMap;
import java.util.EnumMap;
import org.jetbrains.annotations.NotNull;
import java.util.function.Function;
import java.util.Map;

public final class Index<K, V>
{
    private final Map<K, V> keyToValue;
    private final Map<V, K> valueToKey;
    
    private Index(final Map<K, V> keyToValue, final Map<V, K> valueToKey) {
        this.keyToValue = keyToValue;
        this.valueToKey = valueToKey;
    }
    
    @NotNull
    public static <K, V extends Enum<V>> Index<K, V> create(final Class<V> type, @NotNull final Function<? super V, ? extends K> keyFunction) {
        return create(type, keyFunction, (V[])type.getEnumConstants());
    }
    
    @SafeVarargs
    @NotNull
    public static <K, V extends Enum<V>> Index<K, V> create(final Class<V> type, @NotNull final Function<? super V, ? extends K> keyFunction, @NotNull final V... values) {
        return create(values, length -> new EnumMap(type), keyFunction);
    }
    
    @SafeVarargs
    @NotNull
    public static <K, V> Index<K, V> create(@NotNull final Function<? super V, ? extends K> keyFunction, @NotNull final V... values) {
        return create(values, (IntFunction<Map<V, K>>)HashMap::new, keyFunction);
    }
    
    @NotNull
    public static <K, V> Index<K, V> create(@NotNull final Function<? super V, ? extends K> keyFunction, @NotNull final List<V> constants) {
        return create(constants, (IntFunction<Map<V, K>>)HashMap::new, keyFunction);
    }
    
    @NotNull
    private static <K, V> Index<K, V> create(final V[] values, final IntFunction<Map<V, K>> valueToKeyFactory, @NotNull final Function<? super V, ? extends K> keyFunction) {
        return create((List<V>)Arrays.asList((V[])values), valueToKeyFactory, keyFunction);
    }
    
    @NotNull
    private static <K, V> Index<K, V> create(final List<V> values, final IntFunction<Map<V, K>> valueToKeyFactory, @NotNull final Function<? super V, ? extends K> keyFunction) {
        final int length = values.size();
        final Map<K, V> keyToValue = new HashMap<K, V>(length);
        final Map<V, K> valueToKey = valueToKeyFactory.apply(length);
        for (int i = 0; i < length; ++i) {
            final V value = values.get(i);
            final K key = (K)keyFunction.apply(value);
            if (keyToValue.putIfAbsent(key, value) != null) {
                throw new IllegalStateException(String.format("Key %s already mapped to value %s", key, keyToValue.get(key)));
            }
            if (valueToKey.putIfAbsent(value, key) != null) {
                throw new IllegalStateException(String.format("Value %s already mapped to key %s", value, valueToKey.get(value)));
            }
        }
        return new Index<K, V>(Collections.unmodifiableMap((Map<? extends K, ? extends V>)keyToValue), Collections.unmodifiableMap((Map<? extends V, ? extends K>)valueToKey));
    }
    
    @NotNull
    public Set<K> keys() {
        return Collections.unmodifiableSet((Set<? extends K>)this.keyToValue.keySet());
    }
    
    @Nullable
    public K key(@NotNull final V value) {
        return this.valueToKey.get(value);
    }
    
    @NotNull
    public K keyOrThrow(@NotNull final V value) {
        final K key = this.key(value);
        if (key == null) {
            throw new NoSuchElementException("There is no key for value " + value);
        }
        return key;
    }
    
    @Contract("_, null -> null; _, !null -> !null")
    public K keyOr(@NotNull final V value, @Nullable final K defaultKey) {
        final K key = this.key(value);
        return (key == null) ? defaultKey : key;
    }
    
    @NotNull
    public Set<V> values() {
        return Collections.unmodifiableSet((Set<? extends V>)this.valueToKey.keySet());
    }
    
    @Nullable
    public V value(@NotNull final K key) {
        return this.keyToValue.get(key);
    }
    
    @NotNull
    public V valueOrThrow(@NotNull final K key) {
        final V value = this.value(key);
        if (value == null) {
            throw new NoSuchElementException("There is no value for key " + key);
        }
        return value;
    }
    
    @Contract("_, null -> null; _, !null -> !null")
    public V valueOr(@NotNull final K key, @Nullable final V defaultValue) {
        final V value = this.value(key);
        return (value == null) ? defaultValue : value;
    }
    
    @NotNull
    public Map<K, V> keyToValue() {
        return Collections.unmodifiableMap((Map<? extends K, ? extends V>)this.keyToValue);
    }
    
    @NotNull
    public Map<V, K> valueToKey() {
        return Collections.unmodifiableMap((Map<? extends V, ? extends K>)this.valueToKey);
    }
}
