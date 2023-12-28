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

public class DefaultFullHttpRequest extends DefaultHttpRequest implements FullHttpRequest
{
    private final ByteBuf content;
    private final HttpHeaders trailingHeader;
    private int hash;
    
    public DefaultFullHttpRequest(final HttpVersion httpVersion, final HttpMethod method, final String uri) {
        this(httpVersion, method, uri, Unpooled.buffer(0));
    }
    
    public DefaultFullHttpRequest(final HttpVersion httpVersion, final HttpMethod method, final String uri, final ByteBuf content) {
        this(httpVersion, method, uri, content, true);
    }
    
    public DefaultFullHttpRequest(final HttpVersion httpVersion, final HttpMethod method, final String uri, final boolean validateHeaders) {
        this(httpVersion, method, uri, Unpooled.buffer(0), validateHeaders);
    }
    
    public DefaultFullHttpRequest(final HttpVersion httpVersion, final HttpMethod method, final String uri, final ByteBuf content, final boolean validateHeaders) {
        super(httpVersion, method, uri, validateHeaders);
        this.content = ObjectUtil.checkNotNull(content, "content");
        this.trailingHeader = new DefaultHttpHeaders(validateHeaders);
    }
    
    public DefaultFullHttpRequest(final HttpVersion httpVersion, final HttpMethod method, final String uri, final ByteBuf content, final HttpHeaders headers, final HttpHeaders trailingHeader) {
        super(httpVersion, method, uri, headers);
        this.content = ObjectUtil.checkNotNull(content, "content");
        this.trailingHeader = ObjectUtil.checkNotNull(trailingHeader, "trailingHeader");
    }
    
    @Override
    public HttpHeaders trailingHeaders() {
        return this.trailingHeader;
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
    public FullHttpRequest retain() {
        this.content.retain();
        return this;
    }
    
    @Override
    public FullHttpRequest retain(final int increment) {
        this.content.retain(increment);
        return this;
    }
    
    @Override
    public FullHttpRequest touch() {
        this.content.touch();
        return this;
    }
    
    @Override
    public FullHttpRequest touch(final Object hint) {
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
    public FullHttpRequest setProtocolVersion(final HttpVersion version) {
        super.setProtocolVersion(version);
        return this;
    }
    
    @Override
    public FullHttpRequest setMethod(final HttpMethod method) {
        super.setMethod(method);
        return this;
    }
    
    @Override
    public FullHttpRequest setUri(final String uri) {
        super.setUri(uri);
        return this;
    }
    
    @Override
    public FullHttpRequest copy() {
        return this.replace(this.content().copy());
    }
    
    @Override
    public FullHttpRequest duplicate() {
        return this.replace(this.content().duplicate());
    }
    
    @Override
    public FullHttpRequest retainedDuplicate() {
        return this.replace(this.content().retainedDuplicate());
    }
    
    @Override
    public FullHttpRequest replace(final ByteBuf content) {
        return new DefaultFullHttpRequest(this.protocolVersion(), this.method(), this.uri(), content, this.headers(), this.trailingHeaders());
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
        if (!(o instanceof DefaultFullHttpRequest)) {
            return false;
        }
        final DefaultFullHttpRequest other = (DefaultFullHttpRequest)o;
        return super.equals(other) && this.content().equals(other.content()) && this.trailingHeaders().equals(other.trailingHeaders());
    }
    
    @Override
    public String toString() {
        return HttpMessageUtil.appendFullRequest(new StringBuilder(256), this).toString();
    }
}
