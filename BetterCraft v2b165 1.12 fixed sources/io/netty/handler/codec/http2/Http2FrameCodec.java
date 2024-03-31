// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.handler.logging.LogLevel;
import io.netty.util.ReferenceCountUtil;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.HttpServerUpgradeHandler;
import io.netty.channel.ChannelHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelDuplexHandler;

public class Http2FrameCodec extends ChannelDuplexHandler
{
    private static final Http2FrameLogger HTTP2_FRAME_LOGGER;
    private final Http2ConnectionHandler http2Handler;
    private final boolean server;
    private ChannelHandlerContext ctx;
    private ChannelHandlerContext http2HandlerCtx;
    
    public Http2FrameCodec(final boolean server) {
        this(server, Http2FrameCodec.HTTP2_FRAME_LOGGER);
    }
    
    public Http2FrameCodec(final boolean server, final Http2FrameLogger frameLogger) {
        this(server, new DefaultHttp2FrameWriter(), frameLogger, new Http2Settings());
    }
    
    Http2FrameCodec(final boolean server, Http2FrameWriter frameWriter, final Http2FrameLogger frameLogger, final Http2Settings initialSettings) {
        final Http2Connection connection = new DefaultHttp2Connection(server);
        frameWriter = new Http2OutboundFrameLogger(frameWriter, frameLogger);
        final Http2ConnectionEncoder encoder = new DefaultHttp2ConnectionEncoder(connection, frameWriter);
        final Long maxHeaderListSize = initialSettings.maxHeaderListSize();
        final Http2FrameReader frameReader = new DefaultHttp2FrameReader((maxHeaderListSize == null) ? new DefaultHttp2HeadersDecoder(true) : new DefaultHttp2HeadersDecoder(true, maxHeaderListSize));
        final Http2FrameReader reader = new Http2InboundFrameLogger(frameReader, frameLogger);
        final Http2ConnectionDecoder decoder = new DefaultHttp2ConnectionDecoder(connection, encoder, reader);
        decoder.frameListener(new FrameListener());
        this.http2Handler = new InternalHttp2ConnectionHandler(decoder, encoder, initialSettings);
        this.http2Handler.connection().addListener(new ConnectionListener());
        this.server = server;
    }
    
    Http2ConnectionHandler connectionHandler() {
        return this.http2Handler;
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        ctx.pipeline().addBefore(ctx.executor(), ctx.name(), null, this.http2Handler);
        this.http2HandlerCtx = ctx.pipeline().context(this.http2Handler);
    }
    
    @Override
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        ctx.pipeline().remove(this.http2Handler);
    }
    
    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
        if (!(evt instanceof HttpServerUpgradeHandler.UpgradeEvent)) {
            super.userEventTriggered(ctx, evt);
            return;
        }
        final HttpServerUpgradeHandler.UpgradeEvent upgrade = (HttpServerUpgradeHandler.UpgradeEvent)evt;
        ctx.fireUserEventTriggered((Object)upgrade.retain());
        try {
            final Http2Stream stream = this.http2Handler.connection().stream(1);
            new ConnectionListener().onStreamActive(stream);
            upgrade.upgradeRequest().headers().setInt(HttpConversionUtil.ExtensionHeaderNames.STREAM_ID.text(), 1);
            new InboundHttpToHttp2Adapter(this.http2Handler.connection(), this.http2Handler.decoder().frameListener()).channelRead(ctx, upgrade.upgradeRequest().retain());
        }
        finally {
            upgrade.release();
        }
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        ctx.fireExceptionCaught(cause);
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) {
        try {
            if (msg instanceof Http2WindowUpdateFrame) {
                final Http2WindowUpdateFrame frame = (Http2WindowUpdateFrame)msg;
                this.consumeBytes(frame.streamId(), frame.windowSizeIncrement(), promise);
            }
            else if (msg instanceof Http2StreamFrame) {
                this.writeStreamFrame((Http2StreamFrame)msg, promise);
            }
            else {
                if (!(msg instanceof Http2GoAwayFrame)) {
                    throw new UnsupportedMessageTypeException(msg, (Class<?>[])new Class[0]);
                }
                this.writeGoAwayFrame((Http2GoAwayFrame)msg, promise);
            }
        }
        finally {
            ReferenceCountUtil.release(msg);
        }
    }
    
    private void consumeBytes(final int streamId, final int bytes, final ChannelPromise promise) {
        try {
            final Http2Stream stream = this.http2Handler.connection().stream(streamId);
            this.http2Handler.connection().local().flowController().consumeBytes(stream, bytes);
            promise.setSuccess();
        }
        catch (final Throwable t) {
            promise.setFailure(t);
        }
    }
    
    private void writeGoAwayFrame(final Http2GoAwayFrame frame, final ChannelPromise promise) {
        if (frame.lastStreamId() > -1) {
            throw new IllegalArgumentException("Last stream id must not be set on GOAWAY frame");
        }
        final int lastStreamCreated = this.http2Handler.connection().remote().lastStreamCreated();
        int lastStreamId = lastStreamCreated + frame.extraStreamIds() * 2;
        if (lastStreamId < lastStreamCreated) {
            lastStreamId = Integer.MAX_VALUE;
        }
        this.http2Handler.goAway(this.http2HandlerCtx, lastStreamId, frame.errorCode(), frame.content().retain(), promise);
    }
    
    private void writeStreamFrame(final Http2StreamFrame frame, final ChannelPromise promise) {
        if (frame instanceof Http2DataFrame) {
            final Http2DataFrame dataFrame = (Http2DataFrame)frame;
            this.http2Handler.encoder().writeData(this.http2HandlerCtx, frame.streamId(), dataFrame.content().retain(), dataFrame.padding(), dataFrame.isEndStream(), promise);
        }
        else if (frame instanceof Http2HeadersFrame) {
            this.writeHeadersFrame((Http2HeadersFrame)frame, promise);
        }
        else {
            if (!(frame instanceof Http2ResetFrame)) {
                throw new UnsupportedMessageTypeException(frame, (Class<?>[])new Class[0]);
            }
            final Http2ResetFrame rstFrame = (Http2ResetFrame)frame;
            this.http2Handler.resetStream(this.http2HandlerCtx, frame.streamId(), rstFrame.errorCode(), promise);
        }
    }
    
    private void writeHeadersFrame(final Http2HeadersFrame headersFrame, final ChannelPromise promise) {
        int streamId = headersFrame.streamId();
        if (!Http2CodecUtil.isStreamIdValid(streamId)) {
            final Http2Connection.Endpoint<Http2LocalFlowController> localEndpoint = this.http2Handler.connection().local();
            streamId = localEndpoint.incrementAndGetNextStreamId();
            try {
                localEndpoint.createStream(streamId, false);
            }
            catch (final Http2Exception e) {
                promise.setFailure((Throwable)e);
                return;
            }
            this.ctx.fireUserEventTriggered((Object)new Http2StreamActiveEvent(streamId, headersFrame));
        }
        this.http2Handler.encoder().writeHeaders(this.http2HandlerCtx, streamId, headersFrame.headers(), headersFrame.padding(), headersFrame.isEndStream(), promise);
    }
    
    static {
        HTTP2_FRAME_LOGGER = new Http2FrameLogger(LogLevel.INFO, Http2FrameCodec.class);
    }
    
    private final class ConnectionListener extends Http2ConnectionAdapter
    {
        @Override
        public void onStreamActive(final Http2Stream stream) {
            if (Http2FrameCodec.this.ctx == null) {
                return;
            }
            if (Http2CodecUtil.isOutboundStream(Http2FrameCodec.this.server, stream.id())) {
                return;
            }
            Http2FrameCodec.this.ctx.fireUserEventTriggered((Object)new Http2StreamActiveEvent(stream.id()));
        }
        
        @Override
        public void onStreamClosed(final Http2Stream stream) {
            Http2FrameCodec.this.ctx.fireUserEventTriggered((Object)new Http2StreamClosedEvent(stream.id()));
        }
        
        @Override
        public void onGoAwayReceived(final int lastStreamId, final long errorCode, final ByteBuf debugData) {
            Http2FrameCodec.this.ctx.fireChannelRead((Object)new DefaultHttp2GoAwayFrame(lastStreamId, errorCode, debugData.retain()));
        }
    }
    
    private static final class InternalHttp2ConnectionHandler extends Http2ConnectionHandler
    {
        InternalHttp2ConnectionHandler(final Http2ConnectionDecoder decoder, final Http2ConnectionEncoder encoder, final Http2Settings initialSettings) {
            super(decoder, encoder, initialSettings);
        }
        
        @Override
        protected void onStreamError(final ChannelHandlerContext ctx, final Throwable cause, final Http2Exception.StreamException http2Ex) {
            try {
                final Http2Stream stream = this.connection().stream(http2Ex.streamId());
                if (stream == null) {
                    return;
                }
                ctx.fireExceptionCaught((Throwable)http2Ex);
            }
            finally {
                super.onStreamError(ctx, cause, http2Ex);
            }
        }
    }
    
    private static final class FrameListener extends Http2FrameAdapter
    {
        @Override
        public void onRstStreamRead(final ChannelHandlerContext ctx, final int streamId, final long errorCode) {
            final Http2ResetFrame rstFrame = new DefaultHttp2ResetFrame(errorCode);
            rstFrame.streamId(streamId);
            ctx.fireChannelRead((Object)rstFrame);
        }
        
        @Override
        public void onHeadersRead(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int streamDependency, final short weight, final boolean exclusive, final int padding, final boolean endStream) {
            this.onHeadersRead(ctx, streamId, headers, padding, endStream);
        }
        
        @Override
        public void onHeadersRead(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int padding, final boolean endOfStream) {
            final Http2HeadersFrame headersFrame = new DefaultHttp2HeadersFrame(headers, endOfStream, padding);
            headersFrame.streamId(streamId);
            ctx.fireChannelRead((Object)headersFrame);
        }
        
        @Override
        public int onDataRead(final ChannelHandlerContext ctx, final int streamId, final ByteBuf data, final int padding, final boolean endOfStream) {
            final Http2DataFrame dataFrame = new DefaultHttp2DataFrame(data.retain(), endOfStream, padding);
            dataFrame.streamId(streamId);
            ctx.fireChannelRead((Object)dataFrame);
            return 0;
        }
    }
}
