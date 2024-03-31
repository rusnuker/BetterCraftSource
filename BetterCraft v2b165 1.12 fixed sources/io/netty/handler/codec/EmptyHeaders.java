// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec;

import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.Collections;
import java.util.List;

public class EmptyHeaders<K, V, T extends Headers<K, V, T>> implements Headers<K, V, T>
{
    @Override
    public V get(final K name) {
        return null;
    }
    
    @Override
    public V get(final K name, final V defaultValue) {
        return null;
    }
    
    @Override
    public V getAndRemove(final K name) {
        return null;
    }
    
    @Override
    public V getAndRemove(final K name, final V defaultValue) {
        return null;
    }
    
    @Override
    public List<V> getAll(final K name) {
        return Collections.emptyList();
    }
    
    @Override
    public List<V> getAllAndRemove(final K name) {
        return Collections.emptyList();
    }
    
    @Override
    public Boolean getBoolean(final K name) {
        return null;
    }
    
    @Override
    public boolean getBoolean(final K name, final boolean defaultValue) {
        return defaultValue;
    }
    
    @Override
    public Byte getByte(final K name) {
        return null;
    }
    
    @Override
    public byte getByte(final K name, final byte defaultValue) {
        return defaultValue;
    }
    
    @Override
    public Character getChar(final K name) {
        return null;
    }
    
    @Override
    public char getChar(final K name, final char defaultValue) {
        return defaultValue;
    }
    
    @Override
    public Short getShort(final K name) {
        return null;
    }
    
    @Override
    public short getShort(final K name, final short defaultValue) {
        return defaultValue;
    }
    
    @Override
    public Integer getInt(final K name) {
        return null;
    }
    
    @Override
    public int getInt(final K name, final int defaultValue) {
        return defaultValue;
    }
    
    @Override
    public Long getLong(final K name) {
        return null;
    }
    
    @Override
    public long getLong(final K name, final long defaultValue) {
        return defaultValue;
    }
    
    @Override
    public Float getFloat(final K name) {
        return null;
    }
    
    @Override
    public float getFloat(final K name, final float defaultValue) {
        return defaultValue;
    }
    
    @Override
    public Double getDouble(final K name) {
        return null;
    }
    
    @Override
    public double getDouble(final K name, final double defaultValue) {
        return defaultValue;
    }
    
    @Override
    public Long getTimeMillis(final K name) {
        return null;
    }
    
    @Override
    public long getTimeMillis(final K name, final long defaultValue) {
        return defaultValue;
    }
    
    @Override
    public Boolean getBooleanAndRemove(final K name) {
        return null;
    }
    
    @Override
    public boolean getBooleanAndRemove(final K name, final boolean defaultValue) {
        return defaultValue;
    }
    
    @Override
    public Byte getByteAndRemove(final K name) {
        return null;
    }
    
    @Override
    public byte getByteAndRemove(final K name, final byte defaultValue) {
        return defaultValue;
    }
    
    @Override
    public Character getCharAndRemove(final K name) {
        return null;
    }
    
    @Override
    public char getCharAndRemove(final K name, final char defaultValue) {
        return defaultValue;
    }
    
    @Override
    public Short getShortAndRemove(final K name) {
        return null;
    }
    
    @Override
    public short getShortAndRemove(final K name, final short defaultValue) {
        return defaultValue;
    }
    
    @Override
    public Integer getIntAndRemove(final K name) {
        return null;
    }
    
    @Override
    public int getIntAndRemove(final K name, final int defaultValue) {
        return defaultValue;
    }
    
    @Override
    public Long getLongAndRemove(final K name) {
        return null;
    }
    
    @Override
    public long getLongAndRemove(final K name, final long defaultValue) {
        return defaultValue;
    }
    
    @Override
    public Float getFloatAndRemove(final K name) {
        return null;
    }
    
    @Override
    public float getFloatAndRemove(final K name, final float defaultValue) {
        return defaultValue;
    }
    
    @Override
    public Double getDoubleAndRemove(final K name) {
        return null;
    }
    
    @Override
    public double getDoubleAndRemove(final K name, final double defaultValue) {
        return defaultValue;
    }
    
    @Override
    public Long getTimeMillisAndRemove(final K name) {
        return null;
    }
    
    @Override
    public long getTimeMillisAndRemove(final K name, final long defaultValue) {
        return defaultValue;
    }
    
    @Override
    public boolean contains(final K name) {
        return false;
    }
    
    @Override
    public boolean contains(final K name, final V value) {
        return false;
    }
    
    @Override
    public boolean containsObject(final K name, final Object value) {
        return false;
    }
    
    @Override
    public boolean containsBoolean(final K name, final boolean value) {
        return false;
    }
    
    @Override
    public boolean containsByte(final K name, final byte value) {
        return false;
    }
    
    @Override
    public boolean containsChar(final K name, final char value) {
        return false;
    }
    
    @Override
    public boolean containsShort(final K name, final short value) {
        return false;
    }
    
    @Override
    public boolean containsInt(final K name, final int value) {
        return false;
    }
    
    @Override
    public boolean containsLong(final K name, final long value) {
        return false;
    }
    
    @Override
    public boolean containsFloat(final K name, final float value) {
        return false;
    }
    
    @Override
    public boolean containsDouble(final K name, final double value) {
        return false;
    }
    
    @Override
    public boolean containsTimeMillis(final K name, final long value) {
        return false;
    }
    
    @Override
    public int size() {
        return 0;
    }
    
    @Override
    public boolean isEmpty() {
        return true;
    }
    
    @Override
    public Set<K> names() {
        return Collections.emptySet();
    }
    
    @Override
    public T add(final K name, final V value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T add(final K name, final Iterable<? extends V> values) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T add(final K name, final V... values) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T addObject(final K name, final Object value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T addObject(final K name, final Iterable<?> values) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T addObject(final K name, final Object... values) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T addBoolean(final K name, final boolean value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T addByte(final K name, final byte value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T addChar(final K name, final char value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T addShort(final K name, final short value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T addInt(final K name, final int value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T addLong(final K name, final long value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T addFloat(final K name, final float value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T addDouble(final K name, final double value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T addTimeMillis(final K name, final long value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T add(final Headers<? extends K, ? extends V, ?> headers) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T set(final K name, final V value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T set(final K name, final Iterable<? extends V> values) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T set(final K name, final V... values) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T setObject(final K name, final Object value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T setObject(final K name, final Iterable<?> values) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T setObject(final K name, final Object... values) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T setBoolean(final K name, final boolean value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T setByte(final K name, final byte value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T setChar(final K name, final char value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T setShort(final K name, final short value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T setInt(final K name, final int value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T setLong(final K name, final long value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T setFloat(final K name, final float value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T setDouble(final K name, final double value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T setTimeMillis(final K name, final long value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T set(final Headers<? extends K, ? extends V, ?> headers) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public T setAll(final Headers<? extends K, ? extends V, ?> headers) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public boolean remove(final K name) {
        return false;
    }
    
    @Override
    public T clear() {
        return this.thisT();
    }
    
    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        final List<Map.Entry<K, V>> empty = Collections.emptyList();
        return empty.iterator();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Headers)) {
            return false;
        }
        final Headers<?, ?, ?> rhs = (Headers<?, ?, ?>)o;
        return this.isEmpty() && rhs.isEmpty();
    }
    
    @Override
    public int hashCode() {
        return -1028477387;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '[' + ']';
    }
    
    private T thisT() {
        return (T)this;
    }
}
