// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.util.internal.ObjectUtil;

public class DefaultHttp2WindowUpdateFrame extends AbstractHttp2StreamFrame implements Http2WindowUpdateFrame
{
    private final int windowUpdateIncrement;
    
    public DefaultHttp2WindowUpdateFrame(final int windowUpdateIncrement) {
        this.windowUpdateIncrement = ObjectUtil.checkPositive(windowUpdateIncrement, "windowUpdateIncrement");
    }
    
    @Override
    public DefaultHttp2WindowUpdateFrame streamId(final int streamId) {
        super.streamId(streamId);
        return this;
    }
    
    @Override
    public String name() {
        return "WINDOW_UPDATE";
    }
    
    @Override
    public int windowSizeIncrement() {
        return this.windowUpdateIncrement;
    }
}
