// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec;

import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.Map;

public interface Headers<K, V, T extends Headers<K, V, T>> extends Iterable<Map.Entry<K, V>>
{
    V get(final K p0);
    
    V get(final K p0, final V p1);
    
    V getAndRemove(final K p0);
    
    V getAndRemove(final K p0, final V p1);
    
    List<V> getAll(final K p0);
    
    List<V> getAllAndRemove(final K p0);
    
    Boolean getBoolean(final K p0);
    
    boolean getBoolean(final K p0, final boolean p1);
    
    Byte getByte(final K p0);
    
    byte getByte(final K p0, final byte p1);
    
    Character getChar(final K p0);
    
    char getChar(final K p0, final char p1);
    
    Short getShort(final K p0);
    
    short getShort(final K p0, final short p1);
    
    Integer getInt(final K p0);
    
    int getInt(final K p0, final int p1);
    
    Long getLong(final K p0);
    
    long getLong(final K p0, final long p1);
    
    Float getFloat(final K p0);
    
    float getFloat(final K p0, final float p1);
    
    Double getDouble(final K p0);
    
    double getDouble(final K p0, final double p1);
    
    Long getTimeMillis(final K p0);
    
    long getTimeMillis(final K p0, final long p1);
    
    Boolean getBooleanAndRemove(final K p0);
    
    boolean getBooleanAndRemove(final K p0, final boolean p1);
    
    Byte getByteAndRemove(final K p0);
    
    byte getByteAndRemove(final K p0, final byte p1);
    
    Character getCharAndRemove(final K p0);
    
    char getCharAndRemove(final K p0, final char p1);
    
    Short getShortAndRemove(final K p0);
    
    short getShortAndRemove(final K p0, final short p1);
    
    Integer getIntAndRemove(final K p0);
    
    int getIntAndRemove(final K p0, final int p1);
    
    Long getLongAndRemove(final K p0);
    
    long getLongAndRemove(final K p0, final long p1);
    
    Float getFloatAndRemove(final K p0);
    
    float getFloatAndRemove(final K p0, final float p1);
    
    Double getDoubleAndRemove(final K p0);
    
    double getDoubleAndRemove(final K p0, final double p1);
    
    Long getTimeMillisAndRemove(final K p0);
    
    long getTimeMillisAndRemove(final K p0, final long p1);
    
    boolean contains(final K p0);
    
    boolean contains(final K p0, final V p1);
    
    boolean containsObject(final K p0, final Object p1);
    
    boolean containsBoolean(final K p0, final boolean p1);
    
    boolean containsByte(final K p0, final byte p1);
    
    boolean containsChar(final K p0, final char p1);
    
    boolean containsShort(final K p0, final short p1);
    
    boolean containsInt(final K p0, final int p1);
    
    boolean containsLong(final K p0, final long p1);
    
    boolean containsFloat(final K p0, final float p1);
    
    boolean containsDouble(final K p0, final double p1);
    
    boolean containsTimeMillis(final K p0, final long p1);
    
    int size();
    
    boolean isEmpty();
    
    Set<K> names();
    
    T add(final K p0, final V p1);
    
    T add(final K p0, final Iterable<? extends V> p1);
    
    T add(final K p0, final V... p1);
    
    T addObject(final K p0, final Object p1);
    
    T addObject(final K p0, final Iterable<?> p1);
    
    T addObject(final K p0, final Object... p1);
    
    T addBoolean(final K p0, final boolean p1);
    
    T addByte(final K p0, final byte p1);
    
    T addChar(final K p0, final char p1);
    
    T addShort(final K p0, final short p1);
    
    T addInt(final K p0, final int p1);
    
    T addLong(final K p0, final long p1);
    
    T addFloat(final K p0, final float p1);
    
    T addDouble(final K p0, final double p1);
    
    T addTimeMillis(final K p0, final long p1);
    
    T add(final Headers<? extends K, ? extends V, ?> p0);
    
    T set(final K p0, final V p1);
    
    T set(final K p0, final Iterable<? extends V> p1);
    
    T set(final K p0, final V... p1);
    
    T setObject(final K p0, final Object p1);
    
    T setObject(final K p0, final Iterable<?> p1);
    
    T setObject(final K p0, final Object... p1);
    
    T setBoolean(final K p0, final boolean p1);
    
    T setByte(final K p0, final byte p1);
    
    T setChar(final K p0, final char p1);
    
    T setShort(final K p0, final short p1);
    
    T setInt(final K p0, final int p1);
    
    T setLong(final K p0, final long p1);
    
    T setFloat(final K p0, final float p1);
    
    T setDouble(final K p0, final double p1);
    
    T setTimeMillis(final K p0, final long p1);
    
    T set(final Headers<? extends K, ? extends V, ?> p0);
    
    T setAll(final Headers<? extends K, ? extends V, ?> p0);
    
    boolean remove(final K p0);
    
    T clear();
    
    Iterator<Map.Entry<K, V>> iterator();
}
