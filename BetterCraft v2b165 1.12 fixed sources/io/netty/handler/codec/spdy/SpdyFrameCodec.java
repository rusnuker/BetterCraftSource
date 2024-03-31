// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.spdy;

import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.channel.ChannelPromise;
import java.net.SocketAddress;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.handler.codec.ByteToMessageDecoder;

public class SpdyFrameCodec extends ByteToMessageDecoder implements SpdyFrameDecoderDelegate, ChannelOutboundHandler
{
    private static final SpdyProtocolException INVALID_FRAME;
    private final SpdyFrameDecoder spdyFrameDecoder;
    private final SpdyFrameEncoder spdyFrameEncoder;
    private final SpdyHeaderBlockDecoder spdyHeaderBlockDecoder;
    private final SpdyHeaderBlockEncoder spdyHeaderBlockEncoder;
    private SpdyHeadersFrame spdyHeadersFrame;
    private SpdySettingsFrame spdySettingsFrame;
    private ChannelHandlerContext ctx;
    private boolean read;
    private final boolean validateHeaders;
    
    public SpdyFrameCodec(final SpdyVersion version) {
        this(version, true);
    }
    
    public SpdyFrameCodec(final SpdyVersion version, final boolean validateHeaders) {
        this(version, 8192, 16384, 6, 15, 8, validateHeaders);
    }
    
    public SpdyFrameCodec(final SpdyVersion version, final int maxChunkSize, final int maxHeaderSize, final int compressionLevel, final int windowBits, final int memLevel) {
        this(version, maxChunkSize, maxHeaderSize, compressionLevel, windowBits, memLevel, true);
    }
    
    public SpdyFrameCodec(final SpdyVersion version, final int maxChunkSize, final int maxHeaderSize, final int compressionLevel, final int windowBits, final int memLevel, final boolean validateHeaders) {
        this(version, maxChunkSize, SpdyHeaderBlockDecoder.newInstance(version, maxHeaderSize), SpdyHeaderBlockEncoder.newInstance(version, compressionLevel, windowBits, memLevel), validateHeaders);
    }
    
    protected SpdyFrameCodec(final SpdyVersion version, final int maxChunkSize, final SpdyHeaderBlockDecoder spdyHeaderBlockDecoder, final SpdyHeaderBlockEncoder spdyHeaderBlockEncoder, final boolean validateHeaders) {
        this.spdyFrameDecoder = new SpdyFrameDecoder(version, this, maxChunkSize);
        this.spdyFrameEncoder = new SpdyFrameEncoder(version);
        this.spdyHeaderBlockDecoder = spdyHeaderBlockDecoder;
        this.spdyHeaderBlockEncoder = spdyHeaderBlockEncoder;
        this.validateHeaders = validateHeaders;
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        this.ctx = ctx;
        ctx.channel().closeFuture().addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future) throws Exception {
                SpdyFrameCodec.this.spdyHeaderBlockDecoder.end();
                SpdyFrameCodec.this.spdyHeaderBlockEncoder.end();
            }
        });
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        this.spdyFrameDecoder.decode(in);
    }
    
    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
        if (!this.read && !ctx.channel().config().isAutoRead()) {
            ctx.read();
        }
        this.read = false;
        super.channelReadComplete(ctx);
    }
    
    @Override
    public void bind(final ChannelHandlerContext ctx, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
        ctx.bind(localAddress, promise);
    }
    
    @Override
    public void connect(final ChannelHandlerContext ctx, final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
        ctx.connect(remoteAddress, localAddress, promise);
    }
    
    @Override
    public void disconnect(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        ctx.disconnect(promise);
    }
    
    @Override
    public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        ctx.close(promise);
    }
    
    @Override
    public void deregister(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }
    
    @Override
    public void read(final ChannelHandlerContext ctx) throws Exception {
        ctx.read();
    }
    
    @Override
    public void flush(final ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        if (msg instanceof SpdyDataFrame) {
            final SpdyDataFrame spdyDataFrame = (SpdyDataFrame)msg;
            final ByteBuf frame = this.spdyFrameEncoder.encodeDataFrame(ctx.alloc(), spdyDataFrame.streamId(), spdyDataFrame.isLast(), spdyDataFrame.content());
            spdyDataFrame.release();
            ctx.write(frame, promise);
        }
        else if (msg instanceof SpdySynStreamFrame) {
            final SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame)msg;
            final ByteBuf headerBlock = this.spdyHeaderBlockEncoder.encode(ctx.alloc(), spdySynStreamFrame);
            ByteBuf frame;
            try {
                frame = this.spdyFrameEncoder.encodeSynStreamFrame(ctx.alloc(), spdySynStreamFrame.streamId(), spdySynStreamFrame.associatedStreamId(), spdySynStreamFrame.priority(), spdySynStreamFrame.isLast(), spdySynStreamFrame.isUnidirectional(), headerBlock);
            }
            finally {
                headerBlock.release();
            }
            ctx.write(frame, promise);
        }
        else if (msg instanceof SpdySynReplyFrame) {
            final SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame)msg;
            final ByteBuf headerBlock = this.spdyHeaderBlockEncoder.encode(ctx.alloc(), spdySynReplyFrame);
            ByteBuf frame;
            try {
                frame = this.spdyFrameEncoder.encodeSynReplyFrame(ctx.alloc(), spdySynReplyFrame.streamId(), spdySynReplyFrame.isLast(), headerBlock);
            }
            finally {
                headerBlock.release();
            }
            ctx.write(frame, promise);
        }
        else if (msg instanceof SpdyRstStreamFrame) {
            final SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame)msg;
            final ByteBuf frame = this.spdyFrameEncoder.encodeRstStreamFrame(ctx.alloc(), spdyRstStreamFrame.streamId(), spdyRstStreamFrame.status().code());
            ctx.write(frame, promise);
        }
        else if (msg instanceof SpdySettingsFrame) {
            final SpdySettingsFrame spdySettingsFrame = (SpdySettingsFrame)msg;
            final ByteBuf frame = this.spdyFrameEncoder.encodeSettingsFrame(ctx.alloc(), spdySettingsFrame);
            ctx.write(frame, promise);
        }
        else if (msg instanceof SpdyPingFrame) {
            final SpdyPingFrame spdyPingFrame = (SpdyPingFrame)msg;
            final ByteBuf frame = this.spdyFrameEncoder.encodePingFrame(ctx.alloc(), spdyPingFrame.id());
            ctx.write(frame, promise);
        }
        else if (msg instanceof SpdyGoAwayFrame) {
            final SpdyGoAwayFrame spdyGoAwayFrame = (SpdyGoAwayFrame)msg;
            final ByteBuf frame = this.spdyFrameEncoder.encodeGoAwayFrame(ctx.alloc(), spdyGoAwayFrame.lastGoodStreamId(), spdyGoAwayFrame.status().code());
            ctx.write(frame, promise);
        }
        else if (msg instanceof SpdyHeadersFrame) {
            final SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame)msg;
            final ByteBuf headerBlock = this.spdyHeaderBlockEncoder.encode(ctx.alloc(), spdyHeadersFrame);
            ByteBuf frame;
            try {
                frame = this.spdyFrameEncoder.encodeHeadersFrame(ctx.alloc(), spdyHeadersFrame.streamId(), spdyHeadersFrame.isLast(), headerBlock);
            }
            finally {
                headerBlock.release();
            }
            ctx.write(frame, promise);
        }
        else {
            if (!(msg instanceof SpdyWindowUpdateFrame)) {
                throw new UnsupportedMessageTypeException(msg, (Class<?>[])new Class[0]);
            }
            final SpdyWindowUpdateFrame spdyWindowUpdateFrame = (SpdyWindowUpdateFrame)msg;
            final ByteBuf frame = this.spdyFrameEncoder.encodeWindowUpdateFrame(ctx.alloc(), spdyWindowUpdateFrame.streamId(), spdyWindowUpdateFrame.deltaWindowSize());
            ctx.write(frame, promise);
        }
    }
    
    @Override
    public void readDataFrame(final int streamId, final boolean last, final ByteBuf data) {
        this.read = true;
        final SpdyDataFrame spdyDataFrame = new DefaultSpdyDataFrame(streamId, data);
        spdyDataFrame.setLast(last);
        this.ctx.fireChannelRead((Object)spdyDataFrame);
    }
    
    @Override
    public void readSynStreamFrame(final int streamId, final int associatedToStreamId, final byte priority, final boolean last, final boolean unidirectional) {
        final SpdySynStreamFrame spdySynStreamFrame = new DefaultSpdySynStreamFrame(streamId, associatedToStreamId, priority, this.validateHeaders);
        spdySynStreamFrame.setLast(last);
        spdySynStreamFrame.setUnidirectional(unidirectional);
        this.spdyHeadersFrame = spdySynStreamFrame;
    }
    
    @Override
    public void readSynReplyFrame(final int streamId, final boolean last) {
        final SpdySynReplyFrame spdySynReplyFrame = new DefaultSpdySynReplyFrame(streamId, this.validateHeaders);
        spdySynReplyFrame.setLast(last);
        this.spdyHeadersFrame = spdySynReplyFrame;
    }
    
    @Override
    public void readRstStreamFrame(final int streamId, final int statusCode) {
        this.read = true;
        final SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, statusCode);
        this.ctx.fireChannelRead((Object)spdyRstStreamFrame);
    }
    
    @Override
    public void readSettingsFrame(final boolean clearPersisted) {
        this.read = true;
        (this.spdySettingsFrame = new DefaultSpdySettingsFrame()).setClearPreviouslyPersistedSettings(clearPersisted);
    }
    
    @Override
    public void readSetting(final int id, final int value, final boolean persistValue, final boolean persisted) {
        this.spdySettingsFrame.setValue(id, value, persistValue, persisted);
    }
    
    @Override
    public void readSettingsEnd() {
        this.read = true;
        final Object frame = this.spdySettingsFrame;
        this.spdySettingsFrame = null;
        this.ctx.fireChannelRead(frame);
    }
    
    @Override
    public void readPingFrame(final int id) {
        this.read = true;
        final SpdyPingFrame spdyPingFrame = new DefaultSpdyPingFrame(id);
        this.ctx.fireChannelRead((Object)spdyPingFrame);
    }
    
    @Override
    public void readGoAwayFrame(final int lastGoodStreamId, final int statusCode) {
        this.read = true;
        final SpdyGoAwayFrame spdyGoAwayFrame = new DefaultSpdyGoAwayFrame(lastGoodStreamId, statusCode);
        this.ctx.fireChannelRead((Object)spdyGoAwayFrame);
    }
    
    @Override
    public void readHeadersFrame(final int streamId, final boolean last) {
        (this.spdyHeadersFrame = new DefaultSpdyHeadersFrame(streamId, this.validateHeaders)).setLast(last);
    }
    
    @Override
    public void readWindowUpdateFrame(final int streamId, final int deltaWindowSize) {
        this.read = true;
        final SpdyWindowUpdateFrame spdyWindowUpdateFrame = new DefaultSpdyWindowUpdateFrame(streamId, deltaWindowSize);
        this.ctx.fireChannelRead((Object)spdyWindowUpdateFrame);
    }
    
    @Override
    public void readHeaderBlock(final ByteBuf headerBlock) {
        try {
            this.spdyHeaderBlockDecoder.decode(this.ctx.alloc(), headerBlock, this.spdyHeadersFrame);
        }
        catch (final Exception e) {
            this.ctx.fireExceptionCaught((Throwable)e);
        }
        finally {
            headerBlock.release();
        }
    }
    
    @Override
    public void readHeaderBlockEnd() {
        Object frame = null;
        try {
            this.spdyHeaderBlockDecoder.endHeaderBlock(this.spdyHeadersFrame);
            frame = this.spdyHeadersFrame;
            this.spdyHeadersFrame = null;
        }
        catch (final Exception e) {
            this.ctx.fireExceptionCaught((Throwable)e);
        }
        if (frame != null) {
            this.read = true;
            this.ctx.fireChannelRead(frame);
        }
    }
    
    @Override
    public void readFrameError(final String message) {
        this.ctx.fireExceptionCaught((Throwable)SpdyFrameCodec.INVALID_FRAME);
    }
    
    static {
        INVALID_FRAME = new SpdyProtocolException("Received invalid frame");
    }
}
