// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPromise;
import io.netty.util.CharsetUtil;
import io.netty.util.AsciiString;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;

public final class Http2CodecUtil
{
    public static final int CONNECTION_STREAM_ID = 0;
    public static final int HTTP_UPGRADE_STREAM_ID = 1;
    public static final CharSequence HTTP_UPGRADE_SETTINGS_HEADER;
    public static final CharSequence HTTP_UPGRADE_PROTOCOL_NAME;
    public static final CharSequence TLS_UPGRADE_PROTOCOL_NAME;
    public static final int PING_FRAME_PAYLOAD_LENGTH = 8;
    public static final short MAX_UNSIGNED_BYTE = 255;
    public static final int MAX_PADDING = 256;
    public static final long MAX_UNSIGNED_INT = 4294967295L;
    public static final int FRAME_HEADER_LENGTH = 9;
    public static final int SETTING_ENTRY_LENGTH = 6;
    public static final int PRIORITY_ENTRY_LENGTH = 5;
    public static final int INT_FIELD_LENGTH = 4;
    public static final short MAX_WEIGHT = 256;
    public static final short MIN_WEIGHT = 1;
    private static final ByteBuf CONNECTION_PREFACE;
    private static final ByteBuf EMPTY_PING;
    private static final int MAX_PADDING_LENGTH_LENGTH = 1;
    public static final int DATA_FRAME_HEADER_LENGTH = 10;
    public static final int HEADERS_FRAME_HEADER_LENGTH = 15;
    public static final int PRIORITY_FRAME_LENGTH = 14;
    public static final int RST_STREAM_FRAME_LENGTH = 13;
    public static final int PUSH_PROMISE_FRAME_HEADER_LENGTH = 14;
    public static final int GO_AWAY_FRAME_HEADER_LENGTH = 17;
    public static final int WINDOW_UPDATE_FRAME_LENGTH = 13;
    public static final int CONTINUATION_FRAME_HEADER_LENGTH = 10;
    public static final char SETTINGS_HEADER_TABLE_SIZE = '\u0001';
    public static final char SETTINGS_ENABLE_PUSH = '\u0002';
    public static final char SETTINGS_MAX_CONCURRENT_STREAMS = '\u0003';
    public static final char SETTINGS_INITIAL_WINDOW_SIZE = '\u0004';
    public static final char SETTINGS_MAX_FRAME_SIZE = '\u0005';
    public static final char SETTINGS_MAX_HEADER_LIST_SIZE = '\u0006';
    public static final int NUM_STANDARD_SETTINGS = 6;
    public static final long MAX_HEADER_TABLE_SIZE = 4294967295L;
    public static final long MAX_CONCURRENT_STREAMS = 4294967295L;
    public static final int MAX_INITIAL_WINDOW_SIZE = Integer.MAX_VALUE;
    public static final int MAX_FRAME_SIZE_LOWER_BOUND = 16384;
    public static final int MAX_FRAME_SIZE_UPPER_BOUND = 16777215;
    public static final long MAX_HEADER_LIST_SIZE = 4294967295L;
    public static final long MIN_HEADER_TABLE_SIZE = 0L;
    public static final long MIN_CONCURRENT_STREAMS = 0L;
    public static final int MIN_INITIAL_WINDOW_SIZE = 0;
    public static final long MIN_HEADER_LIST_SIZE = 0L;
    public static final int DEFAULT_WINDOW_SIZE = 65535;
    public static final short DEFAULT_PRIORITY_WEIGHT = 16;
    public static final int DEFAULT_HEADER_TABLE_SIZE = 4096;
    public static final long DEFAULT_HEADER_LIST_SIZE = 8192L;
    public static final int DEFAULT_MAX_FRAME_SIZE = 16384;
    public static final int SMALLEST_MAX_CONCURRENT_STREAMS = 100;
    static final int DEFAULT_MAX_RESERVED_STREAMS = 100;
    static final int DEFAULT_MIN_ALLOCATION_CHUNK = 1024;
    
    public static long calculateMaxHeaderListSizeGoAway(final long maxHeaderListSize) {
        return maxHeaderListSize + (maxHeaderListSize >>> 2);
    }
    
    public static boolean isOutboundStream(final boolean server, final int streamId) {
        final boolean even = (streamId & 0x1) == 0x0;
        return streamId > 0 && server == even;
    }
    
    public static boolean isStreamIdValid(final int streamId) {
        return streamId >= 0;
    }
    
    public static boolean isMaxFrameSizeValid(final int maxFrameSize) {
        return maxFrameSize >= 16384 && maxFrameSize <= 16777215;
    }
    
    public static ByteBuf connectionPrefaceBuf() {
        return Http2CodecUtil.CONNECTION_PREFACE.retainedDuplicate();
    }
    
    public static ByteBuf emptyPingBuf() {
        return Http2CodecUtil.EMPTY_PING.retainedDuplicate();
    }
    
    public static Http2Exception getEmbeddedHttp2Exception(Throwable cause) {
        while (cause != null) {
            if (cause instanceof Http2Exception) {
                return (Http2Exception)cause;
            }
            cause = cause.getCause();
        }
        return null;
    }
    
    public static ByteBuf toByteBuf(final ChannelHandlerContext ctx, final Throwable cause) {
        if (cause == null || cause.getMessage() == null) {
            return Unpooled.EMPTY_BUFFER;
        }
        return ByteBufUtil.writeUtf8(ctx.alloc(), cause.getMessage());
    }
    
    public static int readUnsignedInt(final ByteBuf buf) {
        return (buf.readByte() & 0x7F) << 24 | (buf.readByte() & 0xFF) << 16 | (buf.readByte() & 0xFF) << 8 | (buf.readByte() & 0xFF);
    }
    
    public static void writeUnsignedInt(final long value, final ByteBuf out) {
        out.writeByte((int)(value >> 24 & 0xFFL));
        out.writeByte((int)(value >> 16 & 0xFFL));
        out.writeByte((int)(value >> 8 & 0xFFL));
        out.writeByte((int)(value & 0xFFL));
    }
    
    public static void writeUnsignedShort(final int value, final ByteBuf out) {
        out.writeByte(value >> 8 & 0xFF);
        out.writeByte(value & 0xFF);
    }
    
    public static void writeFrameHeader(final ByteBuf out, final int payloadLength, final byte type, final Http2Flags flags, final int streamId) {
        out.ensureWritable(9 + payloadLength);
        writeFrameHeaderInternal(out, payloadLength, type, flags, streamId);
    }
    
    public static int streamableBytes(final StreamByteDistributor.StreamState state) {
        return Math.max(0, Math.min(state.pendingBytes(), state.windowSize()));
    }
    
    public static void headerListSizeExceeded(final int streamId, final long maxHeaderListSize, final boolean onDecode) throws Http2Exception {
        throw Http2Exception.headerListSizeError(streamId, Http2Error.PROTOCOL_ERROR, onDecode, "Header size exceeded max allowed size (%d)", maxHeaderListSize);
    }
    
    public static void headerListSizeExceeded(final long maxHeaderListSize) throws Http2Exception {
        throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Header size exceeded max allowed size (%d)", maxHeaderListSize);
    }
    
    static void writeFrameHeaderInternal(final ByteBuf out, final int payloadLength, final byte type, final Http2Flags flags, final int streamId) {
        out.writeMedium(payloadLength);
        out.writeByte(type);
        out.writeByte(flags.value());
        out.writeInt(streamId);
    }
    
    public static void verifyPadding(final int padding) {
        if (padding < 0 || padding > 256) {
            throw new IllegalArgumentException(String.format("Invalid padding '%d'. Padding must be between 0 and %d (inclusive).", padding, 256));
        }
    }
    
    private Http2CodecUtil() {
    }
    
    static {
        HTTP_UPGRADE_SETTINGS_HEADER = new AsciiString("HTTP2-Settings");
        HTTP_UPGRADE_PROTOCOL_NAME = "h2c";
        TLS_UPGRADE_PROTOCOL_NAME = "h2";
        CONNECTION_PREFACE = Unpooled.unreleasableBuffer(Unpooled.directBuffer(24).writeBytes("PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n".getBytes(CharsetUtil.UTF_8))).asReadOnly();
        EMPTY_PING = Unpooled.unreleasableBuffer(Unpooled.directBuffer(8).writeZero(8)).asReadOnly();
    }
    
    static final class SimpleChannelPromiseAggregator extends DefaultChannelPromise
    {
        private final ChannelPromise promise;
        private int expectedCount;
        private int doneCount;
        private Throwable lastFailure;
        private boolean doneAllocating;
        
        SimpleChannelPromiseAggregator(final ChannelPromise promise, final Channel c, final EventExecutor e) {
            super(c, e);
            assert promise != null && !promise.isDone();
            this.promise = promise;
        }
        
        public ChannelPromise newPromise() {
            assert !this.doneAllocating : "Done allocating. No more promises can be allocated.";
            ++this.expectedCount;
            return this;
        }
        
        public ChannelPromise doneAllocatingPromises() {
            if (!this.doneAllocating) {
                this.doneAllocating = true;
                if (this.doneCount == this.expectedCount || this.expectedCount == 0) {
                    return this.setPromise();
                }
            }
            return this;
        }
        
        @Override
        public boolean tryFailure(final Throwable cause) {
            if (this.allowFailure()) {
                ++this.doneCount;
                this.lastFailure = cause;
                return !this.allPromisesDone() || this.tryPromise();
            }
            return false;
        }
        
        @Override
        public ChannelPromise setFailure(final Throwable cause) {
            if (this.allowFailure()) {
                ++this.doneCount;
                this.lastFailure = cause;
                if (this.allPromisesDone()) {
                    return this.setPromise();
                }
            }
            return this;
        }
        
        @Override
        public ChannelPromise setSuccess(final Void result) {
            if (this.awaitingPromises()) {
                ++this.doneCount;
                if (this.allPromisesDone()) {
                    this.setPromise();
                }
            }
            return this;
        }
        
        @Override
        public boolean trySuccess(final Void result) {
            if (this.awaitingPromises()) {
                ++this.doneCount;
                return !this.allPromisesDone() || this.tryPromise();
            }
            return false;
        }
        
        private boolean allowFailure() {
            return this.awaitingPromises() || this.expectedCount == 0;
        }
        
        private boolean awaitingPromises() {
            return this.doneCount < this.expectedCount;
        }
        
        private boolean allPromisesDone() {
            return this.doneCount == this.expectedCount && this.doneAllocating;
        }
        
        private ChannelPromise setPromise() {
            if (this.lastFailure == null) {
                this.promise.setSuccess();
                return super.setSuccess(null);
            }
            this.promise.setFailure(this.lastFailure);
            return super.setFailure(this.lastFailure);
        }
        
        private boolean tryPromise() {
            if (this.lastFailure == null) {
                this.promise.trySuccess();
                return super.trySuccess(null);
            }
            this.promise.tryFailure(this.lastFailure);
            return super.tryFailure(this.lastFailure);
        }
    }
}
