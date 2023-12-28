// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;

public class Http2OutboundFrameLogger implements Http2FrameWriter
{
    private final Http2FrameWriter writer;
    private final Http2FrameLogger logger;
    
    public Http2OutboundFrameLogger(final Http2FrameWriter writer, final Http2FrameLogger logger) {
        this.writer = ObjectUtil.checkNotNull(writer, "writer");
        this.logger = ObjectUtil.checkNotNull(logger, "logger");
    }
    
    @Override
    public ChannelFuture writeData(final ChannelHandlerContext ctx, final int streamId, final ByteBuf data, final int padding, final boolean endStream, final ChannelPromise promise) {
        this.logger.logData(Http2FrameLogger.Direction.OUTBOUND, ctx, streamId, data, padding, endStream);
        return this.writer.writeData(ctx, streamId, data, padding, endStream, promise);
    }
    
    @Override
    public ChannelFuture writeHeaders(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int padding, final boolean endStream, final ChannelPromise promise) {
        this.logger.logHeaders(Http2FrameLogger.Direction.OUTBOUND, ctx, streamId, headers, padding, endStream);
        return this.writer.writeHeaders(ctx, streamId, headers, padding, endStream, promise);
    }
    
    @Override
    public ChannelFuture writeHeaders(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int streamDependency, final short weight, final boolean exclusive, final int padding, final boolean endStream, final ChannelPromise promise) {
        this.logger.logHeaders(Http2FrameLogger.Direction.OUTBOUND, ctx, streamId, headers, streamDependency, weight, exclusive, padding, endStream);
        return this.writer.writeHeaders(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endStream, promise);
    }
    
    @Override
    public ChannelFuture writePriority(final ChannelHandlerContext ctx, final int streamId, final int streamDependency, final short weight, final boolean exclusive, final ChannelPromise promise) {
        this.logger.logPriority(Http2FrameLogger.Direction.OUTBOUND, ctx, streamId, streamDependency, weight, exclusive);
        return this.writer.writePriority(ctx, streamId, streamDependency, weight, exclusive, promise);
    }
    
    @Override
    public ChannelFuture writeRstStream(final ChannelHandlerContext ctx, final int streamId, final long errorCode, final ChannelPromise promise) {
        this.logger.logRstStream(Http2FrameLogger.Direction.OUTBOUND, ctx, streamId, errorCode);
        return this.writer.writeRstStream(ctx, streamId, errorCode, promise);
    }
    
    @Override
    public ChannelFuture writeSettings(final ChannelHandlerContext ctx, final Http2Settings settings, final ChannelPromise promise) {
        this.logger.logSettings(Http2FrameLogger.Direction.OUTBOUND, ctx, settings);
        return this.writer.writeSettings(ctx, settings, promise);
    }
    
    @Override
    public ChannelFuture writeSettingsAck(final ChannelHandlerContext ctx, final ChannelPromise promise) {
        this.logger.logSettingsAck(Http2FrameLogger.Direction.OUTBOUND, ctx);
        return this.writer.writeSettingsAck(ctx, promise);
    }
    
    @Override
    public ChannelFuture writePing(final ChannelHandlerContext ctx, final boolean ack, final ByteBuf data, final ChannelPromise promise) {
        if (ack) {
            this.logger.logPingAck(Http2FrameLogger.Direction.OUTBOUND, ctx, data);
        }
        else {
            this.logger.logPing(Http2FrameLogger.Direction.OUTBOUND, ctx, data);
        }
        return this.writer.writePing(ctx, ack, data, promise);
    }
    
    @Override
    public ChannelFuture writePushPromise(final ChannelHandlerContext ctx, final int streamId, final int promisedStreamId, final Http2Headers headers, final int padding, final ChannelPromise promise) {
        this.logger.logPushPromise(Http2FrameLogger.Direction.OUTBOUND, ctx, streamId, promisedStreamId, headers, padding);
        return this.writer.writePushPromise(ctx, streamId, promisedStreamId, headers, padding, promise);
    }
    
    @Override
    public ChannelFuture writeGoAway(final ChannelHandlerContext ctx, final int lastStreamId, final long errorCode, final ByteBuf debugData, final ChannelPromise promise) {
        this.logger.logGoAway(Http2FrameLogger.Direction.OUTBOUND, ctx, lastStreamId, errorCode, debugData);
        return this.writer.writeGoAway(ctx, lastStreamId, errorCode, debugData, promise);
    }
    
    @Override
    public ChannelFuture writeWindowUpdate(final ChannelHandlerContext ctx, final int streamId, final int windowSizeIncrement, final ChannelPromise promise) {
        this.logger.logWindowsUpdate(Http2FrameLogger.Direction.OUTBOUND, ctx, streamId, windowSizeIncrement);
        return this.writer.writeWindowUpdate(ctx, streamId, windowSizeIncrement, promise);
    }
    
    @Override
    public ChannelFuture writeFrame(final ChannelHandlerContext ctx, final byte frameType, final int streamId, final Http2Flags flags, final ByteBuf payload, final ChannelPromise promise) {
        this.logger.logUnknownFrame(Http2FrameLogger.Direction.OUTBOUND, ctx, frameType, streamId, flags, payload);
        return this.writer.writeFrame(ctx, frameType, streamId, flags, payload, promise);
    }
    
    @Override
    public void close() {
        this.writer.close();
    }
    
    @Override
    public Configuration configuration() {
        return this.writer.configuration();
    }
}
