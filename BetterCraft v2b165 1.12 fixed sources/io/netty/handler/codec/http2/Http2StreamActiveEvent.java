// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

public class Http2StreamActiveEvent extends AbstractHttp2StreamStateEvent
{
    private final Http2HeadersFrame headers;
    
    public Http2StreamActiveEvent(final int streamId) {
        this(streamId, null);
    }
    
    public Http2StreamActiveEvent(final int streamId, final Http2HeadersFrame headers) {
        super(streamId);
        this.headers = headers;
    }
    
    public Http2HeadersFrame headers() {
        return this.headers;
    }
}
