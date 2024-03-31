// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;

public class DecoratingHttp2ConnectionDecoder implements Http2ConnectionDecoder
{
    private final Http2ConnectionDecoder delegate;
    
    public DecoratingHttp2ConnectionDecoder(final Http2ConnectionDecoder delegate) {
        this.delegate = ObjectUtil.checkNotNull(delegate, "delegate");
    }
    
    @Override
    public void lifecycleManager(final Http2LifecycleManager lifecycleManager) {
        this.delegate.lifecycleManager(lifecycleManager);
    }
    
    @Override
    public Http2Connection connection() {
        return this.delegate.connection();
    }
    
    @Override
    public Http2LocalFlowController flowController() {
        return this.delegate.flowController();
    }
    
    @Override
    public void frameListener(final Http2FrameListener listener) {
        this.delegate.frameListener(listener);
    }
    
    @Override
    public Http2FrameListener frameListener() {
        return this.delegate.frameListener();
    }
    
    @Override
    public void decodeFrame(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Http2Exception {
        this.delegate.decodeFrame(ctx, in, out);
    }
    
    @Override
    public Http2Settings localSettings() {
        return this.delegate.localSettings();
    }
    
    @Override
    public boolean prefaceReceived() {
        return this.delegate.prefaceReceived();
    }
    
    @Override
    public void close() {
        this.delegate.close();
    }
}
