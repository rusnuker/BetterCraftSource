// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.handler.logging.LogLevel;
import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.ChannelHandlerAdapter;

public class Http2FrameLogger extends ChannelHandlerAdapter
{
    private static final int BUFFER_LENGTH_THRESHOLD = 64;
    private final InternalLogger logger;
    private final InternalLogLevel level;
    
    public Http2FrameLogger(final LogLevel level) {
        this(level.toInternalLevel(), InternalLoggerFactory.getInstance(Http2FrameLogger.class));
    }
    
    public Http2FrameLogger(final LogLevel level, final String name) {
        this(level.toInternalLevel(), InternalLoggerFactory.getInstance(name));
    }
    
    public Http2FrameLogger(final LogLevel level, final Class<?> clazz) {
        this(level.toInternalLevel(), InternalLoggerFactory.getInstance(clazz));
    }
    
    private Http2FrameLogger(final InternalLogLevel level, final InternalLogger logger) {
        this.level = ObjectUtil.checkNotNull(level, "level");
        this.logger = ObjectUtil.checkNotNull(logger, "logger");
    }
    
    public void logData(final Direction direction, final ChannelHandlerContext ctx, final int streamId, final ByteBuf data, final int padding, final boolean endStream) {
        if (this.enabled()) {
            this.log(direction, "%s DATA: streamId=%d, padding=%d, endStream=%b, length=%d, bytes=%s", ctx.channel(), streamId, padding, endStream, data.readableBytes(), this.toString(data));
        }
    }
    
    public void logHeaders(final Direction direction, final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int padding, final boolean endStream) {
        if (this.enabled()) {
            this.log(direction, "%s HEADERS: streamId=%d, headers=%s, padding=%d, endStream=%b", ctx.channel(), streamId, headers, padding, endStream);
        }
    }
    
    public void logHeaders(final Direction direction, final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int streamDependency, final short weight, final boolean exclusive, final int padding, final boolean endStream) {
        if (this.enabled()) {
            this.log(direction, "%s HEADERS: streamId=%d, headers=%s, streamDependency=%d, weight=%d, exclusive=%b, padding=%d, endStream=%b", ctx.channel(), streamId, headers, streamDependency, weight, exclusive, padding, endStream);
        }
    }
    
    public void logPriority(final Direction direction, final ChannelHandlerContext ctx, final int streamId, final int streamDependency, final short weight, final boolean exclusive) {
        if (this.enabled()) {
            this.log(direction, "%s PRIORITY: streamId=%d, streamDependency=%d, weight=%d, exclusive=%b", ctx.channel(), streamId, streamDependency, weight, exclusive);
        }
    }
    
    public void logRstStream(final Direction direction, final ChannelHandlerContext ctx, final int streamId, final long errorCode) {
        if (this.enabled()) {
            this.log(direction, "%s RST_STREAM: streamId=%d, errorCode=%d", ctx.channel(), streamId, errorCode);
        }
    }
    
    public void logSettingsAck(final Direction direction, final ChannelHandlerContext ctx) {
        if (this.enabled()) {
            this.log(direction, "%s SETTINGS: ack=true", ctx.channel());
        }
    }
    
    public void logSettings(final Direction direction, final ChannelHandlerContext ctx, final Http2Settings settings) {
        if (this.enabled()) {
            this.log(direction, "%s SETTINGS: ack=false, settings=%s", ctx.channel(), settings);
        }
    }
    
    public void logPing(final Direction direction, final ChannelHandlerContext ctx, final ByteBuf data) {
        if (this.enabled()) {
            this.log(direction, "%s PING: ack=false, length=%d, bytes=%s", ctx.channel(), data.readableBytes(), this.toString(data));
        }
    }
    
    public void logPingAck(final Direction direction, final ChannelHandlerContext ctx, final ByteBuf data) {
        if (this.enabled()) {
            this.log(direction, "%s PING: ack=true, length=%d, bytes=%s", ctx.channel(), data.readableBytes(), this.toString(data));
        }
    }
    
    public void logPushPromise(final Direction direction, final ChannelHandlerContext ctx, final int streamId, final int promisedStreamId, final Http2Headers headers, final int padding) {
        if (this.enabled()) {
            this.log(direction, "%s PUSH_PROMISE: streamId=%d, promisedStreamId=%d, headers=%s, padding=%d", ctx.channel(), streamId, promisedStreamId, headers, padding);
        }
    }
    
    public void logGoAway(final Direction direction, final ChannelHandlerContext ctx, final int lastStreamId, final long errorCode, final ByteBuf debugData) {
        if (this.enabled()) {
            this.log(direction, "%s GO_AWAY: lastStreamId=%d, errorCode=%d, length=%d, bytes=%s", ctx.channel(), lastStreamId, errorCode, debugData.readableBytes(), this.toString(debugData));
        }
    }
    
    public void logWindowsUpdate(final Direction direction, final ChannelHandlerContext ctx, final int streamId, final int windowSizeIncrement) {
        if (this.enabled()) {
            this.log(direction, "%s WINDOW_UPDATE: streamId=%d, windowSizeIncrement=%d", ctx.channel(), streamId, windowSizeIncrement);
        }
    }
    
    public void logUnknownFrame(final Direction direction, final ChannelHandlerContext ctx, final byte frameType, final int streamId, final Http2Flags flags, final ByteBuf data) {
        if (this.enabled()) {
            this.log(direction, "%s UNKNOWN: frameType=%d, streamId=%d, flags=%d, length=%d, bytes=%s", ctx.channel(), frameType & 0xFF, streamId, flags.value(), data.readableBytes(), this.toString(data));
        }
    }
    
    private boolean enabled() {
        return this.logger.isEnabled(this.level);
    }
    
    private String toString(final ByteBuf buf) {
        if (this.level == InternalLogLevel.TRACE || buf.readableBytes() <= 64) {
            return ByteBufUtil.hexDump(buf);
        }
        final int length = Math.min(buf.readableBytes(), 64);
        return ByteBufUtil.hexDump(buf, buf.readerIndex(), length) + "...";
    }
    
    private void log(final Direction direction, final String format, final Object... args) {
        final StringBuilder b = new StringBuilder(200);
        b.append("\n----------------").append(direction.name()).append("--------------------\n").append(String.format(format, args)).append("\n------------------------------------");
        this.logger.log(this.level, b.toString());
    }
    
    public enum Direction
    {
        INBOUND, 
        OUTBOUND;
    }
}
