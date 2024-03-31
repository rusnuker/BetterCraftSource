// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.io.Closeable;

public interface Http2ConnectionDecoder extends Closeable
{
    void lifecycleManager(final Http2LifecycleManager p0);
    
    Http2Connection connection();
    
    Http2LocalFlowController flowController();
    
    void frameListener(final Http2FrameListener p0);
    
    Http2FrameListener frameListener();
    
    void decodeFrame(final ChannelHandlerContext p0, final ByteBuf p1, final List<Object> p2) throws Http2Exception;
    
    Http2Settings localSettings();
    
    boolean prefaceReceived();
    
    void close();
}
