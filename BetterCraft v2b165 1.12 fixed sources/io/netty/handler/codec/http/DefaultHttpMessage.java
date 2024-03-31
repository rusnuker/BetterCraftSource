// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.util.internal.ObjectUtil;

public abstract class DefaultHttpMessage extends DefaultHttpObject implements HttpMessage
{
    private static final int HASH_CODE_PRIME = 31;
    private HttpVersion version;
    private final HttpHeaders headers;
    
    protected DefaultHttpMessage(final HttpVersion version) {
        this(version, true, false);
    }
    
    protected DefaultHttpMessage(final HttpVersion version, final boolean validateHeaders, final boolean singleFieldHeaders) {
        this(version, singleFieldHeaders ? new CombinedHttpHeaders(validateHeaders) : new DefaultHttpHeaders(validateHeaders));
    }
    
    protected DefaultHttpMessage(final HttpVersion version, final HttpHeaders headers) {
        this.version = ObjectUtil.checkNotNull(version, "version");
        this.headers = ObjectUtil.checkNotNull(headers, "headers");
    }
    
    @Override
    public HttpHeaders headers() {
        return this.headers;
    }
    
    @Deprecated
    @Override
    public HttpVersion getProtocolVersion() {
        return this.protocolVersion();
    }
    
    @Override
    public HttpVersion protocolVersion() {
        return this.version;
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + this.headers.hashCode();
        result = 31 * result + this.version.hashCode();
        result = 31 * result + super.hashCode();
        return result;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof DefaultHttpMessage)) {
            return false;
        }
        final DefaultHttpMessage other = (DefaultHttpMessage)o;
        return this.headers().equals(other.headers()) && this.protocolVersion().equals(other.protocolVersion()) && super.equals(o);
    }
    
    @Override
    public HttpMessage setProtocolVersion(final HttpVersion version) {
        if (version == null) {
            throw new NullPointerException("version");
        }
        this.version = version;
        return this;
    }
}
