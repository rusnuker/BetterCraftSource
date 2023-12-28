// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec;

import java.util.NoSuchElementException;
import java.util.Arrays;
import java.util.Map;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Collections;
import java.util.Set;
import java.util.LinkedList;
import java.util.List;
import io.netty.util.internal.MathUtil;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.HashingStrategy;

public class DefaultHeaders<K, V, T extends Headers<K, V, T>> implements Headers<K, V, T>
{
    static final int HASH_CODE_SEED = -1028477387;
    private final HeaderEntry<K, V>[] entries;
    protected final HeaderEntry<K, V> head;
    private final byte hashMask;
    private final ValueConverter<V> valueConverter;
    private final NameValidator<K> nameValidator;
    private final HashingStrategy<K> hashingStrategy;
    int size;
    
    public DefaultHeaders(final ValueConverter<V> valueConverter) {
        this(HashingStrategy.JAVA_HASHER, valueConverter);
    }
    
    public DefaultHeaders(final ValueConverter<V> valueConverter, final NameValidator<K> nameValidator) {
        this(HashingStrategy.JAVA_HASHER, valueConverter, nameValidator);
    }
    
    public DefaultHeaders(final HashingStrategy<K> nameHashingStrategy, final ValueConverter<V> valueConverter) {
        this(nameHashingStrategy, valueConverter, NameValidator.NOT_NULL);
    }
    
    public DefaultHeaders(final HashingStrategy<K> nameHashingStrategy, final ValueConverter<V> valueConverter, final NameValidator<K> nameValidator) {
        this(nameHashingStrategy, valueConverter, nameValidator, 16);
    }
    
    public DefaultHeaders(final HashingStrategy<K> nameHashingStrategy, final ValueConverter<V> valueConverter, final NameValidator<K> nameValidator, final int arraySizeHint) {
        this.valueConverter = ObjectUtil.checkNotNull(valueConverter, "valueConverter");
        this.nameValidator = ObjectUtil.checkNotNull(nameValidator, "nameValidator");
        this.hashingStrategy = ObjectUtil.checkNotNull(nameHashingStrategy, "nameHashingStrategy");
        this.entries = new HeaderEntry[MathUtil.findNextPositivePowerOfTwo(Math.max(2, Math.min(arraySizeHint, 128)))];
        this.hashMask = (byte)(this.entries.length - 1);
        this.head = new HeaderEntry<K, V>();
    }
    
    @Override
    public V get(final K name) {
        ObjectUtil.checkNotNull(name, "name");
        final int h = this.hashingStrategy.hashCode(name);
        final int i = this.index(h);
        HeaderEntry<K, V> e = this.entries[i];
        V value = null;
        while (e != null) {
            if (e.hash == h && this.hashingStrategy.equals(name, e.key)) {
                value = e.value;
            }
            e = e.next;
        }
        return value;
    }
    
    @Override
    public V get(final K name, final V defaultValue) {
        final V value = this.get(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
    
    @Override
    public V getAndRemove(final K name) {
        final int h = this.hashingStrategy.hashCode(name);
        return this.remove0(h, this.index(h), ObjectUtil.checkNotNull(name, "name"));
    }
    
    @Override
    public V getAndRemove(final K name, final V defaultValue) {
        final V value = this.getAndRemove(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
    
    @Override
    public List<V> getAll(final K name) {
        ObjectUtil.checkNotNull(name, "name");
        final LinkedList<V> values = new LinkedList<V>();
        final int h = this.hashingStrategy.hashCode(name);
        final int i = this.index(h);
        for (HeaderEntry<K, V> e = this.entries[i]; e != null; e = e.next) {
            if (e.hash == h && this.hashingStrategy.equals(name, e.key)) {
                values.addFirst(e.getValue());
            }
        }
        return values;
    }
    
    @Override
    public List<V> getAllAndRemove(final K name) {
        final List<V> all = this.getAll(name);
        this.remove(name);
        return all;
    }
    
    @Override
    public boolean contains(final K name) {
        return this.get(name) != null;
    }
    
    @Override
    public boolean containsObject(final K name, final Object value) {
        return this.contains(name, this.valueConverter.convertObject(ObjectUtil.checkNotNull(value, "value")));
    }
    
    @Override
    public boolean containsBoolean(final K name, final boolean value) {
        return this.contains(name, this.valueConverter.convertBoolean(value));
    }
    
    @Override
    public boolean containsByte(final K name, final byte value) {
        return this.contains(name, this.valueConverter.convertByte(value));
    }
    
    @Override
    public boolean containsChar(final K name, final char value) {
        return this.contains(name, this.valueConverter.convertChar(value));
    }
    
    @Override
    public boolean containsShort(final K name, final short value) {
        return this.contains(name, this.valueConverter.convertShort(value));
    }
    
    @Override
    public boolean containsInt(final K name, final int value) {
        return this.contains(name, this.valueConverter.convertInt(value));
    }
    
    @Override
    public boolean containsLong(final K name, final long value) {
        return this.contains(name, this.valueConverter.convertLong(value));
    }
    
    @Override
    public boolean containsFloat(final K name, final float value) {
        return this.contains(name, this.valueConverter.convertFloat(value));
    }
    
    @Override
    public boolean containsDouble(final K name, final double value) {
        return this.contains(name, this.valueConverter.convertDouble(value));
    }
    
    @Override
    public boolean containsTimeMillis(final K name, final long value) {
        return this.contains(name, this.valueConverter.convertTimeMillis(value));
    }
    
    @Override
    public boolean contains(final K name, final V value) {
        return this.contains(name, value, HashingStrategy.JAVA_HASHER);
    }
    
    public final boolean contains(final K name, final V value, final HashingStrategy<? super V> valueHashingStrategy) {
        ObjectUtil.checkNotNull(name, "name");
        final int h = this.hashingStrategy.hashCode(name);
        final int i = this.index(h);
        for (HeaderEntry<K, V> e = this.entries[i]; e != null; e = e.next) {
            if (e.hash == h && this.hashingStrategy.equals(name, e.key) && valueHashingStrategy.equals((Object)value, (Object)e.value)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.head == this.head.after;
    }
    
    @Override
    public Set<K> names() {
        if (this.isEmpty()) {
            return Collections.emptySet();
        }
        final Set<K> names = new LinkedHashSet<K>(this.size());
        for (HeaderEntry<K, V> e = this.head.after; e != this.head; e = e.after) {
            names.add(e.getKey());
        }
        return names;
    }
    
    @Override
    public T add(final K name, final V value) {
        this.nameValidator.validateName(name);
        ObjectUtil.checkNotNull(value, "value");
        final int h = this.hashingStrategy.hashCode(name);
        final int i = this.index(h);
        this.add0(h, i, name, value);
        return this.thisT();
    }
    
    @Override
    public T add(final K name, final Iterable<? extends V> values) {
        this.nameValidator.validateName(name);
        final int h = this.hashingStrategy.hashCode(name);
        final int i = this.index(h);
        for (final V v : values) {
            this.add0(h, i, name, v);
        }
        return this.thisT();
    }
    
    @Override
    public T add(final K name, final V... values) {
        this.nameValidator.validateName(name);
        final int h = this.hashingStrategy.hashCode(name);
        final int i = this.index(h);
        for (final V v : values) {
            this.add0(h, i, name, v);
        }
        return this.thisT();
    }
    
    @Override
    public T addObject(final K name, final Object value) {
        return this.add(name, this.valueConverter.convertObject(ObjectUtil.checkNotNull(value, "value")));
    }
    
    @Override
    public T addObject(final K name, final Iterable<?> values) {
        for (final Object value : values) {
            this.addObject(name, value);
        }
        return this.thisT();
    }
    
    @Override
    public T addObject(final K name, final Object... values) {
        for (final Object value : values) {
            this.addObject(name, value);
        }
        return this.thisT();
    }
    
    @Override
    public T addInt(final K name, final int value) {
        return this.add(name, this.valueConverter.convertInt(value));
    }
    
    @Override
    public T addLong(final K name, final long value) {
        return this.add(name, this.valueConverter.convertLong(value));
    }
    
    @Override
    public T addDouble(final K name, final double value) {
        return this.add(name, this.valueConverter.convertDouble(value));
    }
    
    @Override
    public T addTimeMillis(final K name, final long value) {
        return this.add(name, this.valueConverter.convertTimeMillis(value));
    }
    
    @Override
    public T addChar(final K name, final char value) {
        return this.add(name, this.valueConverter.convertChar(value));
    }
    
    @Override
    public T addBoolean(final K name, final boolean value) {
        return this.add(name, this.valueConverter.convertBoolean(value));
    }
    
    @Override
    public T addFloat(final K name, final float value) {
        return this.add(name, this.valueConverter.convertFloat(value));
    }
    
    @Override
    public T addByte(final K name, final byte value) {
        return this.add(name, this.valueConverter.convertByte(value));
    }
    
    @Override
    public T addShort(final K name, final short value) {
        return this.add(name, this.valueConverter.convertShort(value));
    }
    
    @Override
    public T add(final Headers<? extends K, ? extends V, ?> headers) {
        if (headers == this) {
            throw new IllegalArgumentException("can't add to itself.");
        }
        this.addImpl(headers);
        return this.thisT();
    }
    
    protected void addImpl(final Headers<? extends K, ? extends V, ?> headers) {
        if (headers instanceof DefaultHeaders) {
            final DefaultHeaders<? extends K, ? extends V, T> defaultHeaders = (DefaultHeaders)headers;
            HeaderEntry<? extends K, ? extends V> e = defaultHeaders.head.after;
            if (defaultHeaders.hashingStrategy == this.hashingStrategy && defaultHeaders.nameValidator == this.nameValidator) {
                while (e != defaultHeaders.head) {
                    this.add0(e.hash, this.index(e.hash), e.key, e.value);
                    e = e.after;
                }
            }
            else {
                while (e != defaultHeaders.head) {
                    this.add(e.key, e.value);
                    e = e.after;
                }
            }
        }
        else {
            for (final Map.Entry<? extends K, ? extends V> header : headers) {
                this.add(header.getKey(), header.getValue());
            }
        }
    }
    
    @Override
    public T set(final K name, final V value) {
        this.nameValidator.validateName(name);
        ObjectUtil.checkNotNull(value, "value");
        final int h = this.hashingStrategy.hashCode(name);
        final int i = this.index(h);
        this.remove0(h, i, name);
        this.add0(h, i, name, value);
        return this.thisT();
    }
    
    @Override
    public T set(final K name, final Iterable<? extends V> values) {
        this.nameValidator.validateName(name);
        ObjectUtil.checkNotNull(values, "values");
        final int h = this.hashingStrategy.hashCode(name);
        final int i = this.index(h);
        this.remove0(h, i, name);
        for (final V v : values) {
            if (v == null) {
                break;
            }
            this.add0(h, i, name, v);
        }
        return this.thisT();
    }
    
    @Override
    public T set(final K name, final V... values) {
        this.nameValidator.validateName(name);
        ObjectUtil.checkNotNull(values, "values");
        final int h = this.hashingStrategy.hashCode(name);
        final int i = this.index(h);
        this.remove0(h, i, name);
        for (final V v : values) {
            if (v == null) {
                break;
            }
            this.add0(h, i, name, v);
        }
        return this.thisT();
    }
    
    @Override
    public T setObject(final K name, final Object value) {
        ObjectUtil.checkNotNull(value, "value");
        final V convertedValue = ObjectUtil.checkNotNull(this.valueConverter.convertObject(value), "convertedValue");
        return this.set(name, convertedValue);
    }
    
    @Override
    public T setObject(final K name, final Iterable<?> values) {
        this.nameValidator.validateName(name);
        final int h = this.hashingStrategy.hashCode(name);
        final int i = this.index(h);
        this.remove0(h, i, name);
        for (final Object v : values) {
            if (v == null) {
                break;
            }
            this.add0(h, i, name, this.valueConverter.convertObject(v));
        }
        return this.thisT();
    }
    
    @Override
    public T setObject(final K name, final Object... values) {
        this.nameValidator.validateName(name);
        final int h = this.hashingStrategy.hashCode(name);
        final int i = this.index(h);
        this.remove0(h, i, name);
        for (final Object v : values) {
            if (v == null) {
                break;
            }
            this.add0(h, i, name, this.valueConverter.convertObject(v));
        }
        return this.thisT();
    }
    
    @Override
    public T setInt(final K name, final int value) {
        return this.set(name, this.valueConverter.convertInt(value));
    }
    
    @Override
    public T setLong(final K name, final long value) {
        return this.set(name, this.valueConverter.convertLong(value));
    }
    
    @Override
    public T setDouble(final K name, final double value) {
        return this.set(name, this.valueConverter.convertDouble(value));
    }
    
    @Override
    public T setTimeMillis(final K name, final long value) {
        return this.set(name, this.valueConverter.convertTimeMillis(value));
    }
    
    @Override
    public T setFloat(final K name, final float value) {
        return this.set(name, this.valueConverter.convertFloat(value));
    }
    
    @Override
    public T setChar(final K name, final char value) {
        return this.set(name, this.valueConverter.convertChar(value));
    }
    
    @Override
    public T setBoolean(final K name, final boolean value) {
        return this.set(name, this.valueConverter.convertBoolean(value));
    }
    
    @Override
    public T setByte(final K name, final byte value) {
        return this.set(name, this.valueConverter.convertByte(value));
    }
    
    @Override
    public T setShort(final K name, final short value) {
        return this.set(name, this.valueConverter.convertShort(value));
    }
    
    @Override
    public T set(final Headers<? extends K, ? extends V, ?> headers) {
        if (headers != this) {
            this.clear();
            this.addImpl(headers);
        }
        return this.thisT();
    }
    
    @Override
    public T setAll(final Headers<? extends K, ? extends V, ?> headers) {
        if (headers != this) {
            for (final K key : headers.names()) {
                this.remove(key);
            }
            this.addImpl(headers);
        }
        return this.thisT();
    }
    
    @Override
    public boolean remove(final K name) {
        return this.getAndRemove(name) != null;
    }
    
    @Override
    public T clear() {
        Arrays.fill(this.entries, null);
        final HeaderEntry<K, V> head = this.head;
        final HeaderEntry<K, V> head2 = this.head;
        final HeaderEntry<K, V> head3 = this.head;
        head2.after = head3;
        head.before = head3;
        this.size = 0;
        return this.thisT();
    }
    
    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new HeaderIterator();
    }
    
    @Override
    public Boolean getBoolean(final K name) {
        final V v = this.get(name);
        return (v != null) ? Boolean.valueOf(this.valueConverter.convertToBoolean(v)) : null;
    }
    
    @Override
    public boolean getBoolean(final K name, final boolean defaultValue) {
        final Boolean v = this.getBoolean(name);
        return (v != null) ? v : defaultValue;
    }
    
    @Override
    public Byte getByte(final K name) {
        final V v = this.get(name);
        return (v != null) ? Byte.valueOf(this.valueConverter.convertToByte(v)) : null;
    }
    
    @Override
    public byte getByte(final K name, final byte defaultValue) {
        final Byte v = this.getByte(name);
        return (v != null) ? v : defaultValue;
    }
    
    @Override
    public Character getChar(final K name) {
        final V v = this.get(name);
        return (v != null) ? Character.valueOf(this.valueConverter.convertToChar(v)) : null;
    }
    
    @Override
    public char getChar(final K name, final char defaultValue) {
        final Character v = this.getChar(name);
        return (v != null) ? v : defaultValue;
    }
    
    @Override
    public Short getShort(final K name) {
        final V v = this.get(name);
        return (v != null) ? Short.valueOf(this.valueConverter.convertToShort(v)) : null;
    }
    
    @Override
    public short getShort(final K name, final short defaultValue) {
        final Short v = this.getShort(name);
        return (v != null) ? v : defaultValue;
    }
    
    @Override
    public Integer getInt(final K name) {
        final V v = this.get(name);
        return (v != null) ? Integer.valueOf(this.valueConverter.convertToInt(v)) : null;
    }
    
    @Override
    public int getInt(final K name, final int defaultValue) {
        final Integer v = this.getInt(name);
        return (v != null) ? v : defaultValue;
    }
    
    @Override
    public Long getLong(final K name) {
        final V v = this.get(name);
        return (v != null) ? Long.valueOf(this.valueConverter.convertToLong(v)) : null;
    }
    
    @Override
    public long getLong(final K name, final long defaultValue) {
        final Long v = this.getLong(name);
        return (v != null) ? v : defaultValue;
    }
    
    @Override
    public Float getFloat(final K name) {
        final V v = this.get(name);
        return (v != null) ? Float.valueOf(this.valueConverter.convertToFloat(v)) : null;
    }
    
    @Override
    public float getFloat(final K name, final float defaultValue) {
        final Float v = this.getFloat(name);
        return (v != null) ? v : defaultValue;
    }
    
    @Override
    public Double getDouble(final K name) {
        final V v = this.get(name);
        return (v != null) ? Double.valueOf(this.valueConverter.convertToDouble(v)) : null;
    }
    
    @Override
    public double getDouble(final K name, final double defaultValue) {
        final Double v = this.getDouble(name);
        return (v != null) ? v : defaultValue;
    }
    
    @Override
    public Long getTimeMillis(final K name) {
        final V v = this.get(name);
        return (v != null) ? Long.valueOf(this.valueConverter.convertToTimeMillis(v)) : null;
    }
    
    @Override
    public long getTimeMillis(final K name, final long defaultValue) {
        final Long v = this.getTimeMillis(name);
        return (v != null) ? v : defaultValue;
    }
    
    @Override
    public Boolean getBooleanAndRemove(final K name) {
        final V v = this.getAndRemove(name);
        return (v != null) ? Boolean.valueOf(this.valueConverter.convertToBoolean(v)) : null;
    }
    
    @Override
    public boolean getBooleanAndRemove(final K name, final boolean defaultValue) {
        final Boolean v = this.getBooleanAndRemove(name);
        return (v != null) ? v : defaultValue;
    }
    
    @Override
    public Byte getByteAndRemove(final K name) {
        final V v = this.getAndRemove(name);
        return (v != null) ? Byte.valueOf(this.valueConverter.convertToByte(v)) : null;
    }
    
    @Override
    public byte getByteAndRemove(final K name, final byte defaultValue) {
        final Byte v = this.getByteAndRemove(name);
        return (v != null) ? v : defaultValue;
    }
    
    @Override
    public Character getCharAndRemove(final K name) {
        final V v = this.getAndRemove(name);
        if (v == null) {
            return null;
        }
        try {
            return this.valueConverter.convertToChar(v);
        }
        catch (final Throwable ignored) {
            return null;
        }
    }
    
    @Override
    public char getCharAndRemove(final K name, final char defaultValue) {
        final Character v = this.getCharAndRemove(name);
        return (v != null) ? v : defaultValue;
    }
    
    @Override
    public Short getShortAndRemove(final K name) {
        final V v = this.getAndRemove(name);
        return (v != null) ? Short.valueOf(this.valueConverter.convertToShort(v)) : null;
    }
    
    @Override
    public short getShortAndRemove(final K name, final short defaultValue) {
        final Short v = this.getShortAndRemove(name);
        return (v != null) ? v : defaultValue;
    }
    
    @Override
    public Integer getIntAndRemove(final K name) {
        final V v = this.getAndRemove(name);
        return (v != null) ? Integer.valueOf(this.valueConverter.convertToInt(v)) : null;
    }
    
    @Override
    public int getIntAndRemove(final K name, final int defaultValue) {
        final Integer v = this.getIntAndRemove(name);
        return (v != null) ? v : defaultValue;
    }
    
    @Override
    public Long getLongAndRemove(final K name) {
        final V v = this.getAndRemove(name);
        return (v != null) ? Long.valueOf(this.valueConverter.convertToLong(v)) : null;
    }
    
    @Override
    public long getLongAndRemove(final K name, final long defaultValue) {
        final Long v = this.getLongAndRemove(name);
        return (v != null) ? v : defaultValue;
    }
    
    @Override
    public Float getFloatAndRemove(final K name) {
        final V v = this.getAndRemove(name);
        return (v != null) ? Float.valueOf(this.valueConverter.convertToFloat(v)) : null;
    }
    
    @Override
    public float getFloatAndRemove(final K name, final float defaultValue) {
        final Float v = this.getFloatAndRemove(name);
        return (v != null) ? v : defaultValue;
    }
    
    @Override
    public Double getDoubleAndRemove(final K name) {
        final V v = this.getAndRemove(name);
        return (v != null) ? Double.valueOf(this.valueConverter.convertToDouble(v)) : null;
    }
    
    @Override
    public double getDoubleAndRemove(final K name, final double defaultValue) {
        final Double v = this.getDoubleAndRemove(name);
        return (v != null) ? v : defaultValue;
    }
    
    @Override
    public Long getTimeMillisAndRemove(final K name) {
        final V v = this.getAndRemove(name);
        return (v != null) ? Long.valueOf(this.valueConverter.convertToTimeMillis(v)) : null;
    }
    
    @Override
    public long getTimeMillisAndRemove(final K name, final long defaultValue) {
        final Long v = this.getTimeMillisAndRemove(name);
        return (v != null) ? v : defaultValue;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof Headers && this.equals((Headers<K, V, ?>)o, HashingStrategy.JAVA_HASHER);
    }
    
    @Override
    public int hashCode() {
        return this.hashCode(HashingStrategy.JAVA_HASHER);
    }
    
    public final boolean equals(final Headers<K, V, ?> h2, final HashingStrategy<V> valueHashingStrategy) {
        if (h2.size() != this.size()) {
            return false;
        }
        if (this == h2) {
            return true;
        }
        for (final K name : this.names()) {
            final List<V> otherValues = h2.getAll(name);
            final List<V> values = this.getAll(name);
            if (otherValues.size() != values.size()) {
                return false;
            }
            for (int i = 0; i < otherValues.size(); ++i) {
                if (!valueHashingStrategy.equals(otherValues.get(i), values.get(i))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public final int hashCode(final HashingStrategy<V> valueHashingStrategy) {
        int result = -1028477387;
        for (final K name : this.names()) {
            result = 31 * result + this.hashingStrategy.hashCode(name);
            final List<V> values = this.getAll(name);
            for (int i = 0; i < values.size(); ++i) {
                result = 31 * result + valueHashingStrategy.hashCode(values.get(i));
            }
        }
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(this.getClass().getSimpleName()).append('[');
        String separator = "";
        for (final K name : this.names()) {
            final List<V> values = this.getAll(name);
            for (int i = 0; i < values.size(); ++i) {
                builder.append(separator);
                builder.append(name).append(": ").append(values.get(i));
                separator = ", ";
            }
        }
        return builder.append(']').toString();
    }
    
    protected HeaderEntry<K, V> newHeaderEntry(final int h, final K name, final V value, final HeaderEntry<K, V> next) {
        return new HeaderEntry<K, V>(h, name, value, next, this.head);
    }
    
    protected ValueConverter<V> valueConverter() {
        return this.valueConverter;
    }
    
    private int index(final int hash) {
        return hash & this.hashMask;
    }
    
    private void add0(final int h, final int i, final K name, final V value) {
        this.entries[i] = this.newHeaderEntry(h, name, value, this.entries[i]);
        ++this.size;
    }
    
    private V remove0(final int h, final int i, final K name) {
        HeaderEntry<K, V> e = this.entries[i];
        if (e == null) {
            return null;
        }
        V value = null;
        for (HeaderEntry<K, V> next = e.next; next != null; next = e.next) {
            if (next.hash == h && this.hashingStrategy.equals(name, next.key)) {
                value = next.value;
                e.next = next.next;
                next.remove();
                --this.size;
            }
            else {
                e = next;
            }
        }
        e = this.entries[i];
        if (e.hash == h && this.hashingStrategy.equals(name, e.key)) {
            if (value == null) {
                value = e.value;
            }
            this.entries[i] = e.next;
            e.remove();
            --this.size;
        }
        return value;
    }
    
    private T thisT() {
        return (T)this;
    }
    
    public interface NameValidator<K>
    {
        public static final NameValidator NOT_NULL = new NameValidator() {
            @Override
            public void validateName(final Object name) {
                ObjectUtil.checkNotNull(name, "name");
            }
        };
        
        void validateName(final K p0);
    }
    
    private final class HeaderIterator implements Iterator<Map.Entry<K, V>>
    {
        private HeaderEntry<K, V> current;
        
        private HeaderIterator() {
            this.current = DefaultHeaders.this.head;
        }
        
        @Override
        public boolean hasNext() {
            return this.current.after != DefaultHeaders.this.head;
        }
        
        @Override
        public Map.Entry<K, V> next() {
            this.current = this.current.after;
            if (this.current == DefaultHeaders.this.head) {
                throw new NoSuchElementException();
            }
            return this.current;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException("read only");
        }
    }
    
    protected static class HeaderEntry<K, V> implements Map.Entry<K, V>
    {
        protected final int hash;
        protected final K key;
        protected V value;
        protected HeaderEntry<K, V> next;
        protected HeaderEntry<K, V> before;
        protected HeaderEntry<K, V> after;
        
        protected HeaderEntry(final int hash, final K key) {
            this.hash = hash;
            this.key = key;
        }
        
        HeaderEntry(final int hash, final K key, final V value, final HeaderEntry<K, V> next, final HeaderEntry<K, V> head) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
            this.after = head;
            this.before = head.before;
            this.pointNeighborsToThis();
        }
        
        HeaderEntry() {
            this.hash = -1;
            this.key = null;
            this.after = this;
            this.before = this;
        }
        
        protected final void pointNeighborsToThis() {
            this.before.after = this;
            this.after.before = this;
        }
        
        public final HeaderEntry<K, V> before() {
            return this.before;
        }
        
        public final HeaderEntry<K, V> after() {
            return this.after;
        }
        
        protected void remove() {
            this.before.after = this.after;
            this.after.before = this.before;
        }
        
        @Override
        public final K getKey() {
            return this.key;
        }
        
        @Override
        public final V getValue() {
            return this.value;
        }
        
        @Override
        public final V setValue(final V value) {
            ObjectUtil.checkNotNull(value, "value");
            final V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
        
        @Override
        public final String toString() {
            return this.key.toString() + '=' + this.value.toString();
        }
    }
}
