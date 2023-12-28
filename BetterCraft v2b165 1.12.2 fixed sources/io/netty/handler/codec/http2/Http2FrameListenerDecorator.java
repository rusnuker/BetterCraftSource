// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;

public class Http2FrameListenerDecorator implements Http2FrameListener
{
    protected final Http2FrameListener listener;
    
    public Http2FrameListenerDecorator(final Http2FrameListener listener) {
        this.listener = ObjectUtil.checkNotNull(listener, "listener");
    }
    
    @Override
    public int onDataRead(final ChannelHandlerContext ctx, final int streamId, final ByteBuf data, final int padding, final boolean endOfStream) throws Http2Exception {
        return this.listener.onDataRead(ctx, streamId, data, padding, endOfStream);
    }
    
    @Override
    public void onHeadersRead(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int padding, final boolean endStream) throws Http2Exception {
        this.listener.onHeadersRead(ctx, streamId, headers, padding, endStream);
    }
    
    @Override
    public void onHeadersRead(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int streamDependency, final short weight, final boolean exclusive, final int padding, final boolean endStream) throws Http2Exception {
        this.listener.onHeadersRead(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endStream);
    }
    
    @Override
    public void onPriorityRead(final ChannelHandlerContext ctx, final int streamId, final int streamDependency, final short weight, final boolean exclusive) throws Http2Exception {
        this.listener.onPriorityRead(ctx, streamId, streamDependency, weight, exclusive);
    }
    
    @Override
    public void onRstStreamRead(final ChannelHandlerContext ctx, final int streamId, final long errorCode) throws Http2Exception {
        this.listener.onRstStreamRead(ctx, streamId, errorCode);
    }
    
    @Override
    public void onSettingsAckRead(final ChannelHandlerContext ctx) throws Http2Exception {
        this.listener.onSettingsAckRead(ctx);
    }
    
    @Override
    public void onSettingsRead(final ChannelHandlerContext ctx, final Http2Settings settings) throws Http2Exception {
        this.listener.onSettingsRead(ctx, settings);
    }
    
    @Override
    public void onPingRead(final ChannelHandlerContext ctx, final ByteBuf data) throws Http2Exception {
        this.listener.onPingRead(ctx, data);
    }
    
    @Override
    public void onPingAckRead(final ChannelHandlerContext ctx, final ByteBuf data) throws Http2Exception {
        this.listener.onPingAckRead(ctx, data);
    }
    
    @Override
    public void onPushPromiseRead(final ChannelHandlerContext ctx, final int streamId, final int promisedStreamId, final Http2Headers headers, final int padding) throws Http2Exception {
        this.listener.onPushPromiseRead(ctx, streamId, promisedStreamId, headers, padding);
    }
    
    @Override
    public void onGoAwayRead(final ChannelHandlerContext ctx, final int lastStreamId, final long errorCode, final ByteBuf debugData) throws Http2Exception {
        this.listener.onGoAwayRead(ctx, lastStreamId, errorCode, debugData);
    }
    
    @Override
    public void onWindowUpdateRead(final ChannelHandlerContext ctx, final int streamId, final int windowSizeIncrement) throws Http2Exception {
        this.listener.onWindowUpdateRead(ctx, streamId, windowSizeIncrement);
    }
    
    @Override
    public void onUnknownFrame(final ChannelHandlerContext ctx, final byte frameType, final int streamId, final Http2Flags flags, final ByteBuf payload) throws Http2Exception {
        this.listener.onUnknownFrame(ctx, frameType, streamId, flags, payload);
    }
}
