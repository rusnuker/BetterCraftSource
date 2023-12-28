// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

public class Http2StreamClosedEvent extends AbstractHttp2StreamStateEvent
{
    public Http2StreamClosedEvent(final int streamId) {
        super(streamId);
    }
}
