// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.spdy;

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

public class DefaultSpdyHeaders extends DefaultHeaders<CharSequence, CharSequence, SpdyHeaders> implements SpdyHeaders
{
    private static final NameValidator<CharSequence> SpydNameValidator;
    
    public DefaultSpdyHeaders() {
        this(true);
    }
    
    public DefaultSpdyHeaders(final boolean validate) {
        super(AsciiString.CASE_INSENSITIVE_HASHER, (ValueConverter<Object>)(validate ? HeaderValueConverterAndValidator.INSTANCE : CharSequenceValueConverter.INSTANCE), validate ? DefaultSpdyHeaders.SpydNameValidator : NameValidator.NOT_NULL);
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
    
    static {
        SpydNameValidator = new NameValidator<CharSequence>() {
            @Override
            public void validateName(final CharSequence name) {
                SpdyCodecUtil.validateHeaderName(name);
            }
        };
    }
    
    private static final class HeaderValueConverterAndValidator extends CharSequenceValueConverter
    {
        public static final HeaderValueConverterAndValidator INSTANCE;
        
        @Override
        public CharSequence convertObject(final Object value) {
            final CharSequence seq = super.convertObject(value);
            SpdyCodecUtil.validateHeaderValue(seq);
            return seq;
        }
        
        static {
            INSTANCE = new HeaderValueConverterAndValidator();
        }
    }
}
