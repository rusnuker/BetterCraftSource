// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.internal.ObjectUtil;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;

public class DefaultFullHttpResponse extends DefaultHttpResponse implements FullHttpResponse
{
    private final ByteBuf content;
    private final HttpHeaders trailingHeaders;
    private int hash;
    
    public DefaultFullHttpResponse(final HttpVersion version, final HttpResponseStatus status) {
        this(version, status, Unpooled.buffer(0));
    }
    
    public DefaultFullHttpResponse(final HttpVersion version, final HttpResponseStatus status, final ByteBuf content) {
        this(version, status, content, true);
    }
    
    public DefaultFullHttpResponse(final HttpVersion version, final HttpResponseStatus status, final boolean validateHeaders) {
        this(version, status, Unpooled.buffer(0), validateHeaders, false);
    }
    
    public DefaultFullHttpResponse(final HttpVersion version, final HttpResponseStatus status, final boolean validateHeaders, final boolean singleFieldHeaders) {
        this(version, status, Unpooled.buffer(0), validateHeaders, singleFieldHeaders);
    }
    
    public DefaultFullHttpResponse(final HttpVersion version, final HttpResponseStatus status, final ByteBuf content, final boolean validateHeaders) {
        this(version, status, content, validateHeaders, false);
    }
    
    public DefaultFullHttpResponse(final HttpVersion version, final HttpResponseStatus status, final ByteBuf content, final boolean validateHeaders, final boolean singleFieldHeaders) {
        super(version, status, validateHeaders, singleFieldHeaders);
        this.content = ObjectUtil.checkNotNull(content, "content");
        this.trailingHeaders = (singleFieldHeaders ? new CombinedHttpHeaders(validateHeaders) : new DefaultHttpHeaders(validateHeaders));
    }
    
    public DefaultFullHttpResponse(final HttpVersion version, final HttpResponseStatus status, final ByteBuf content, final HttpHeaders headers, final HttpHeaders trailingHeaders) {
        super(version, status, headers);
        this.content = ObjectUtil.checkNotNull(content, "content");
        this.trailingHeaders = ObjectUtil.checkNotNull(trailingHeaders, "trailingHeaders");
    }
    
    @Override
    public HttpHeaders trailingHeaders() {
        return this.trailingHeaders;
    }
    
    @Override
    public ByteBuf content() {
        return this.content;
    }
    
    @Override
    public int refCnt() {
        return this.content.refCnt();
    }
    
    @Override
    public FullHttpResponse retain() {
        this.content.retain();
        return this;
    }
    
    @Override
    public FullHttpResponse retain(final int increment) {
        this.content.retain(increment);
        return this;
    }
    
    @Override
    public FullHttpResponse touch() {
        this.content.touch();
        return this;
    }
    
    @Override
    public FullHttpResponse touch(final Object hint) {
        this.content.touch(hint);
        return this;
    }
    
    @Override
    public boolean release() {
        return this.content.release();
    }
    
    @Override
    public boolean release(final int decrement) {
        return this.content.release(decrement);
    }
    
    @Override
    public FullHttpResponse setProtocolVersion(final HttpVersion version) {
        super.setProtocolVersion(version);
        return this;
    }
    
    @Override
    public FullHttpResponse setStatus(final HttpResponseStatus status) {
        super.setStatus(status);
        return this;
    }
    
    @Override
    public FullHttpResponse copy() {
        return this.replace(this.content().copy());
    }
    
    @Override
    public FullHttpResponse duplicate() {
        return this.replace(this.content().duplicate());
    }
    
    @Override
    public FullHttpResponse retainedDuplicate() {
        return this.replace(this.content().retainedDuplicate());
    }
    
    @Override
    public FullHttpResponse replace(final ByteBuf content) {
        return new DefaultFullHttpResponse(this.protocolVersion(), this.status(), content, this.headers(), this.trailingHeaders());
    }
    
    @Override
    public int hashCode() {
        int hash = this.hash;
        if (hash == 0) {
            if (this.content().refCnt() != 0) {
                try {
                    hash = 31 + this.content().hashCode();
                }
                catch (final IllegalReferenceCountException ignored) {
                    hash = 31;
                }
            }
            else {
                hash = 31;
            }
            hash = 31 * hash + this.trailingHeaders().hashCode();
            hash = 31 * hash + super.hashCode();
            this.hash = hash;
        }
        return hash;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof DefaultFullHttpResponse)) {
            return false;
        }
        final DefaultFullHttpResponse other = (DefaultFullHttpResponse)o;
        return super.equals(other) && this.content().equals(other.content()) && this.trailingHeaders().equals(other.trailingHeaders());
    }
    
    @Override
    public String toString() {
        return HttpMessageUtil.appendFullResponse(new StringBuilder(256), this).toString();
    }
}
