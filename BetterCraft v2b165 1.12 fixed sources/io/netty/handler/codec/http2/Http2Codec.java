// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.handler.logging.LogLevel;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelDuplexHandler;

public final class Http2Codec extends ChannelDuplexHandler
{
    private static final Http2FrameLogger HTTP2_FRAME_LOGGER;
    private final Http2FrameCodec frameCodec;
    private final Http2MultiplexCodec multiplexCodec;
    
    public Http2Codec(final boolean server, final ChannelHandler streamHandler) {
        this(server, new Http2StreamChannelBootstrap().handler(streamHandler), Http2Codec.HTTP2_FRAME_LOGGER);
    }
    
    public Http2Codec(final boolean server, final ChannelHandler streamHandler, final Http2Settings initialSettings) {
        this(server, new Http2StreamChannelBootstrap().handler(streamHandler), Http2Codec.HTTP2_FRAME_LOGGER, initialSettings);
    }
    
    public Http2Codec(final boolean server, final Http2StreamChannelBootstrap bootstrap, final Http2FrameLogger frameLogger) {
        this(server, bootstrap, new DefaultHttp2FrameWriter(), frameLogger, new Http2Settings());
    }
    
    public Http2Codec(final boolean server, final Http2StreamChannelBootstrap bootstrap, final Http2FrameLogger frameLogger, final Http2Settings initialSettings) {
        this(server, bootstrap, new DefaultHttp2FrameWriter(), frameLogger, initialSettings);
    }
    
    Http2Codec(final boolean server, final Http2StreamChannelBootstrap bootstrap, final Http2FrameWriter frameWriter, final Http2FrameLogger frameLogger, final Http2Settings initialSettings) {
        this.frameCodec = new Http2FrameCodec(server, frameWriter, frameLogger, initialSettings);
        this.multiplexCodec = new Http2MultiplexCodec(server, bootstrap);
    }
    
    Http2FrameCodec frameCodec() {
        return this.frameCodec;
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        ctx.pipeline().addBefore(ctx.executor(), ctx.name(), null, this.frameCodec);
        ctx.pipeline().addBefore(ctx.executor(), ctx.name(), null, this.multiplexCodec);
        ctx.pipeline().remove(this);
    }
    
    static {
        HTTP2_FRAME_LOGGER = new Http2FrameLogger(LogLevel.INFO, Http2Codec.class);
    }
}
