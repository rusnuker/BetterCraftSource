// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.util.internal.ObjectUtil;

public abstract class AbstractHttp2StreamStateEvent implements Http2StreamStateEvent
{
    private final int streamId;
    
    protected AbstractHttp2StreamStateEvent(final int streamId) {
        this.streamId = ObjectUtil.checkPositiveOrZero(streamId, "streamId");
    }
    
    @Override
    public int streamId() {
        return this.streamId;
    }
}
