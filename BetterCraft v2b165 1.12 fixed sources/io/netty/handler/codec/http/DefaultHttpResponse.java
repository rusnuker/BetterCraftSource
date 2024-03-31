// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.util.internal.ObjectUtil;

public class DefaultHttpResponse extends DefaultHttpMessage implements HttpResponse
{
    private HttpResponseStatus status;
    
    public DefaultHttpResponse(final HttpVersion version, final HttpResponseStatus status) {
        this(version, status, true, false);
    }
    
    public DefaultHttpResponse(final HttpVersion version, final HttpResponseStatus status, final boolean validateHeaders) {
        this(version, status, validateHeaders, false);
    }
    
    public DefaultHttpResponse(final HttpVersion version, final HttpResponseStatus status, final boolean validateHeaders, final boolean singleFieldHeaders) {
        super(version, validateHeaders, singleFieldHeaders);
        this.status = ObjectUtil.checkNotNull(status, "status");
    }
    
    public DefaultHttpResponse(final HttpVersion version, final HttpResponseStatus status, final HttpHeaders headers) {
        super(version, headers);
        this.status = ObjectUtil.checkNotNull(status, "status");
    }
    
    @Deprecated
    @Override
    public HttpResponseStatus getStatus() {
        return this.status();
    }
    
    @Override
    public HttpResponseStatus status() {
        return this.status;
    }
    
    @Override
    public HttpResponse setStatus(final HttpResponseStatus status) {
        if (status == null) {
            throw new NullPointerException("status");
        }
        this.status = status;
        return this;
    }
    
    @Override
    public HttpResponse setProtocolVersion(final HttpVersion version) {
        super.setProtocolVersion(version);
        return this;
    }
    
    @Override
    public String toString() {
        return HttpMessageUtil.appendResponse(new StringBuilder(256), this).toString();
    }
}
