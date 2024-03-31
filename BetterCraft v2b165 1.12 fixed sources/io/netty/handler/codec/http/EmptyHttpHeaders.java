// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import java.util.Set;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

public class EmptyHttpHeaders extends HttpHeaders
{
    static final Iterator<Map.Entry<CharSequence, CharSequence>> EMPTY_CHARS_ITERATOR;
    public static final EmptyHttpHeaders INSTANCE;
    
    @Deprecated
    static EmptyHttpHeaders instance() {
        return HttpUtil.EMPTY_HEADERS;
    }
    
    protected EmptyHttpHeaders() {
    }
    
    @Override
    public String get(final String name) {
        return null;
    }
    
    @Override
    public Integer getInt(final CharSequence name) {
        return null;
    }
    
    @Override
    public int getInt(final CharSequence name, final int defaultValue) {
        return defaultValue;
    }
    
    @Override
    public Short getShort(final CharSequence name) {
        return null;
    }
    
    @Override
    public short getShort(final CharSequence name, final short defaultValue) {
        return defaultValue;
    }
    
    @Override
    public Long getTimeMillis(final CharSequence name) {
        return null;
    }
    
    @Override
    public long getTimeMillis(final CharSequence name, final long defaultValue) {
        return defaultValue;
    }
    
    @Override
    public List<String> getAll(final String name) {
        return Collections.emptyList();
    }
    
    @Override
    public List<Map.Entry<String, String>> entries() {
        return Collections.emptyList();
    }
    
    @Override
    public boolean contains(final String name) {
        return false;
    }
    
    @Override
    public boolean isEmpty() {
        return true;
    }
    
    @Override
    public int size() {
        return 0;
    }
    
    @Override
    public Set<String> names() {
        return Collections.emptySet();
    }
    
    @Override
    public HttpHeaders add(final String name, final Object value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public HttpHeaders add(final String name, final Iterable<?> values) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public HttpHeaders addInt(final CharSequence name, final int value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public HttpHeaders addShort(final CharSequence name, final short value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public HttpHeaders set(final String name, final Object value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public HttpHeaders set(final String name, final Iterable<?> values) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public HttpHeaders setInt(final CharSequence name, final int value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public HttpHeaders setShort(final CharSequence name, final short value) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public HttpHeaders remove(final String name) {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public HttpHeaders clear() {
        throw new UnsupportedOperationException("read only");
    }
    
    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return this.entries().iterator();
    }
    
    @Override
    public Iterator<Map.Entry<CharSequence, CharSequence>> iteratorCharSequence() {
        return EmptyHttpHeaders.EMPTY_CHARS_ITERATOR;
    }
    
    static {
        EMPTY_CHARS_ITERATOR = Collections.emptyList().iterator();
        INSTANCE = instance();
    }
}
