// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.handler.codec.DefaultHeaders;
import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import java.util.Iterator;
import java.util.Map;
import io.netty.util.internal.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class DefaultLastHttpContent extends DefaultHttpContent implements LastHttpContent
{
    private final HttpHeaders trailingHeaders;
    private final boolean validateHeaders;
    
    public DefaultLastHttpContent() {
        this(Unpooled.buffer(0));
    }
    
    public DefaultLastHttpContent(final ByteBuf content) {
        this(content, true);
    }
    
    public DefaultLastHttpContent(final ByteBuf content, final boolean validateHeaders) {
        super(content);
        this.trailingHeaders = new TrailingHttpHeaders(validateHeaders);
        this.validateHeaders = validateHeaders;
    }
    
    @Override
    public LastHttpContent copy() {
        return this.replace(this.content().copy());
    }
    
    @Override
    public LastHttpContent duplicate() {
        return this.replace(this.content().duplicate());
    }
    
    @Override
    public LastHttpContent retainedDuplicate() {
        return this.replace(this.content().retainedDuplicate());
    }
    
    @Override
    public LastHttpContent replace(final ByteBuf content) {
        final DefaultLastHttpContent dup = new DefaultLastHttpContent(content, this.validateHeaders);
        dup.trailingHeaders().set(this.trailingHeaders());
        return dup;
    }
    
    @Override
    public LastHttpContent retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public LastHttpContent retain() {
        super.retain();
        return this;
    }
    
    @Override
    public LastHttpContent touch() {
        super.touch();
        return this;
    }
    
    @Override
    public LastHttpContent touch(final Object hint) {
        super.touch(hint);
        return this;
    }
    
    @Override
    public HttpHeaders trailingHeaders() {
        return this.trailingHeaders;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder(super.toString());
        buf.append(StringUtil.NEWLINE);
        this.appendHeaders(buf);
        buf.setLength(buf.length() - StringUtil.NEWLINE.length());
        return buf.toString();
    }
    
    private void appendHeaders(final StringBuilder buf) {
        for (final Map.Entry<String, String> e : this.trailingHeaders()) {
            buf.append(e.getKey());
            buf.append(": ");
            buf.append(e.getValue());
            buf.append(StringUtil.NEWLINE);
        }
    }
    
    private static final class TrailingHttpHeaders extends DefaultHttpHeaders
    {
        private static final DefaultHeaders.NameValidator<CharSequence> TrailerNameValidator;
        
        TrailingHttpHeaders(final boolean validate) {
            super(validate, validate ? TrailingHttpHeaders.TrailerNameValidator : DefaultHeaders.NameValidator.NOT_NULL);
        }
        
        static {
            TrailerNameValidator = new DefaultHeaders.NameValidator<CharSequence>() {
                @Override
                public void validateName(final CharSequence name) {
                    DefaultHttpHeaders.HttpNameValidator.validateName(name);
                    if (HttpHeaderNames.CONTENT_LENGTH.contentEqualsIgnoreCase(name) || HttpHeaderNames.TRANSFER_ENCODING.contentEqualsIgnoreCase(name) || HttpHeaderNames.TRAILER.contentEqualsIgnoreCase(name)) {
                        throw new IllegalArgumentException("prohibited trailing header: " + (Object)name);
                    }
                }
            };
        }
    }
}
