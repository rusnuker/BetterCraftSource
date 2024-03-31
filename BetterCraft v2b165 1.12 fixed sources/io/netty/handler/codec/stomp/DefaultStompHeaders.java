// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.stomp;

import io.netty.util.HashingStrategy;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import io.netty.handler.codec.Headers;
import io.netty.handler.codec.HeadersUtils;
import io.netty.handler.codec.ValueConverter;
import io.netty.handler.codec.CharSequenceValueConverter;
import io.netty.util.AsciiString;
import io.netty.handler.codec.DefaultHeaders;

public class DefaultStompHeaders extends DefaultHeaders<CharSequence, CharSequence, StompHeaders> implements StompHeaders
{
    public DefaultStompHeaders() {
        super(AsciiString.CASE_SENSITIVE_HASHER, (ValueConverter<Object>)CharSequenceValueConverter.INSTANCE);
    }
    
    @Override
    public String getAsString(final CharSequence name) {
        return HeadersUtils.getAsString((Headers<CharSequence, Object, ?>)this, name);
    }
    
    @Override
    public List<String> getAllAsString(final CharSequence name) {
        return HeadersUtils.getAllAsString((Headers<CharSequence, Object, ?>)this, name);
    }
    
    @Override
    public Iterator<Map.Entry<String, String>> iteratorAsString() {
        return HeadersUtils.iteratorAsString(this);
    }
    
    @Override
    public boolean contains(final CharSequence name, final CharSequence value) {
        return this.contains(name, value, false);
    }
    
    @Override
    public boolean contains(final CharSequence name, final CharSequence value, final boolean ignoreCase) {
        return ((DefaultHeaders<CharSequence, CharSequence, T>)this).contains(name, value, ignoreCase ? AsciiString.CASE_INSENSITIVE_HASHER : AsciiString.CASE_SENSITIVE_HASHER);
    }
}
