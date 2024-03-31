// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;

public class DecoratingHttp2FrameWriter implements Http2FrameWriter
{
    private final Http2FrameWriter delegate;
    
    public DecoratingHttp2FrameWriter(final Http2FrameWriter delegate) {
        this.delegate = ObjectUtil.checkNotNull(delegate, "delegate");
    }
    
    @Override
    public ChannelFuture writeData(final ChannelHandlerContext ctx, final int streamId, final ByteBuf data, final int padding, final boolean endStream, final ChannelPromise promise) {
        return this.delegate.writeData(ctx, streamId, data, padding, endStream, promise);
    }
    
    @Override
    public ChannelFuture writeHeaders(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int padding, final boolean endStream, final ChannelPromise promise) {
        return this.delegate.writeHeaders(ctx, streamId, headers, padding, endStream, promise);
    }
    
    @Override
    public ChannelFuture writeHeaders(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int streamDependency, final short weight, final boolean exclusive, final int padding, final boolean endStream, final ChannelPromise promise) {
        return this.delegate.writeHeaders(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endStream, promise);
    }
    
    @Override
    public ChannelFuture writePriority(final ChannelHandlerContext ctx, final int streamId, final int streamDependency, final short weight, final boolean exclusive, final ChannelPromise promise) {
        return this.delegate.writePriority(ctx, streamId, streamDependency, weight, exclusive, promise);
    }
    
    @Override
    public ChannelFuture writeRstStream(final ChannelHandlerContext ctx, final int streamId, final long errorCode, final ChannelPromise promise) {
        return this.delegate.writeRstStream(ctx, streamId, errorCode, promise);
    }
    
    @Override
    public ChannelFuture writeSettings(final ChannelHandlerContext ctx, final Http2Settings settings, final ChannelPromise promise) {
        return this.delegate.writeSettings(ctx, settings, promise);
    }
    
    @Override
    public ChannelFuture writeSettingsAck(final ChannelHandlerContext ctx, final ChannelPromise promise) {
        return this.delegate.writeSettingsAck(ctx, promise);
    }
    
    @Override
    public ChannelFuture writePing(final ChannelHandlerContext ctx, final boolean ack, final ByteBuf data, final ChannelPromise promise) {
        return this.delegate.writePing(ctx, ack, data, promise);
    }
    
    @Override
    public ChannelFuture writePushPromise(final ChannelHandlerContext ctx, final int streamId, final int promisedStreamId, final Http2Headers headers, final int padding, final ChannelPromise promise) {
        return this.delegate.writePushPromise(ctx, streamId, promisedStreamId, headers, padding, promise);
    }
    
    @Override
    public ChannelFuture writeGoAway(final ChannelHandlerContext ctx, final int lastStreamId, final long errorCode, final ByteBuf debugData, final ChannelPromise promise) {
        return this.delegate.writeGoAway(ctx, lastStreamId, errorCode, debugData, promise);
    }
    
    @Override
    public ChannelFuture writeWindowUpdate(final ChannelHandlerContext ctx, final int streamId, final int windowSizeIncrement, final ChannelPromise promise) {
        return this.delegate.writeWindowUpdate(ctx, streamId, windowSizeIncrement, promise);
    }
    
    @Override
    public ChannelFuture writeFrame(final ChannelHandlerContext ctx, final byte frameType, final int streamId, final Http2Flags flags, final ByteBuf payload, final ChannelPromise promise) {
        return this.delegate.writeFrame(ctx, frameType, streamId, flags, payload, promise);
    }
    
    @Override
    public Configuration configuration() {
        return this.delegate.configuration();
    }
    
    @Override
    public void close() {
        this.delegate.close();
    }
}
