// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.util.internal.ObjectUtil;

public abstract class AbstractHttp2StreamFrame implements Http2StreamFrame
{
    private volatile int streamId;
    
    public AbstractHttp2StreamFrame() {
        this.streamId = -1;
    }
    
    @Override
    public AbstractHttp2StreamFrame streamId(final int streamId) {
        if (this.streamId != -1) {
            throw new IllegalStateException("Stream identifier may only be set once.");
        }
        this.streamId = ObjectUtil.checkPositiveOrZero(streamId, "streamId");
        return this;
    }
    
    @Override
    public int streamId() {
        return this.streamId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Http2StreamFrame)) {
            return false;
        }
        final Http2StreamFrame other = (Http2StreamFrame)o;
        return this.streamId == other.streamId();
    }
    
    @Override
    public int hashCode() {
        return this.streamId;
    }
}
