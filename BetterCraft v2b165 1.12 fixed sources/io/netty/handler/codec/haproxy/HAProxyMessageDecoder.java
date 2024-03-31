// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.haproxy;

import io.netty.util.CharsetUtil;
import java.util.List;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.ProtocolDetectionResult;
import io.netty.handler.codec.ByteToMessageDecoder;

public class HAProxyMessageDecoder extends ByteToMessageDecoder
{
    private static final int V1_MAX_LENGTH = 108;
    private static final int V2_MAX_LENGTH = 65551;
    private static final int V2_MIN_LENGTH = 232;
    private static final int V2_MAX_TLV = 65319;
    private static final int DELIMITER_LENGTH = 2;
    private static final byte[] BINARY_PREFIX;
    private static final byte[] TEXT_PREFIX;
    private static final int BINARY_PREFIX_LENGTH;
    private static final ProtocolDetectionResult<HAProxyProtocolVersion> DETECTION_RESULT_V1;
    private static final ProtocolDetectionResult<HAProxyProtocolVersion> DETECTION_RESULT_V2;
    private boolean discarding;
    private int discardedBytes;
    private boolean finished;
    private int version;
    private final int v2MaxHeaderSize;
    
    public HAProxyMessageDecoder() {
        this.version = -1;
        this.v2MaxHeaderSize = 65551;
    }
    
    public HAProxyMessageDecoder(final int maxTlvSize) {
        this.version = -1;
        if (maxTlvSize < 1) {
            this.v2MaxHeaderSize = 232;
        }
        else if (maxTlvSize > 65319) {
            this.v2MaxHeaderSize = 65551;
        }
        else {
            final int calcMax = maxTlvSize + 232;
            if (calcMax > 65551) {
                this.v2MaxHeaderSize = 65551;
            }
            else {
                this.v2MaxHeaderSize = calcMax;
            }
        }
    }
    
    private static int findVersion(final ByteBuf buffer) {
        final int n = buffer.readableBytes();
        if (n < 13) {
            return -1;
        }
        final int idx = buffer.readerIndex();
        return match(HAProxyMessageDecoder.BINARY_PREFIX, buffer, idx) ? buffer.getByte(idx + HAProxyMessageDecoder.BINARY_PREFIX_LENGTH) : 1;
    }
    
    private static int findEndOfHeader(final ByteBuf buffer) {
        final int n = buffer.readableBytes();
        if (n < 16) {
            return -1;
        }
        final int offset = buffer.readerIndex() + 14;
        final int totalHeaderBytes = 16 + buffer.getUnsignedShort(offset);
        if (n >= totalHeaderBytes) {
            return totalHeaderBytes;
        }
        return -1;
    }
    
    private static int findEndOfLine(final ByteBuf buffer) {
        for (int n = buffer.writerIndex(), i = buffer.readerIndex(); i < n; ++i) {
            final byte b = buffer.getByte(i);
            if (b == 13 && i < n - 1 && buffer.getByte(i + 1) == 10) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public boolean isSingleDecode() {
        return true;
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        super.channelRead(ctx, msg);
        if (this.finished) {
            ctx.pipeline().remove(this);
        }
    }
    
    @Override
    protected final void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        if (this.version == -1 && (this.version = findVersion(in)) == -1) {
            return;
        }
        ByteBuf decoded;
        if (this.version == 1) {
            decoded = this.decodeLine(ctx, in);
        }
        else {
            decoded = this.decodeStruct(ctx, in);
        }
        if (decoded != null) {
            this.finished = true;
            try {
                if (this.version == 1) {
                    out.add(HAProxyMessage.decodeHeader(decoded.toString(CharsetUtil.US_ASCII)));
                }
                else {
                    out.add(HAProxyMessage.decodeHeader(decoded));
                }
            }
            catch (final HAProxyProtocolException e) {
                this.fail(ctx, null, e);
            }
        }
    }
    
    private ByteBuf decodeStruct(final ChannelHandlerContext ctx, final ByteBuf buffer) throws Exception {
        final int eoh = findEndOfHeader(buffer);
        if (this.discarding) {
            if (eoh >= 0) {
                buffer.readerIndex(eoh);
                this.discardedBytes = 0;
                this.discarding = false;
            }
            else {
                buffer.skipBytes(this.discardedBytes = buffer.readableBytes());
            }
            return null;
        }
        if (eoh < 0) {
            final int length = buffer.readableBytes();
            if (length > this.v2MaxHeaderSize) {
                buffer.skipBytes(this.discardedBytes = length);
                this.discarding = true;
                this.failOverLimit(ctx, "over " + this.discardedBytes);
            }
            return null;
        }
        final int length = eoh - buffer.readerIndex();
        if (length > this.v2MaxHeaderSize) {
            buffer.readerIndex(eoh);
            this.failOverLimit(ctx, length);
            return null;
        }
        return buffer.readSlice(length);
    }
    
    private ByteBuf decodeLine(final ChannelHandlerContext ctx, final ByteBuf buffer) throws Exception {
        final int eol = findEndOfLine(buffer);
        if (this.discarding) {
            if (eol >= 0) {
                final int delimLength = (buffer.getByte(eol) == 13) ? 2 : 1;
                buffer.readerIndex(eol + delimLength);
                this.discardedBytes = 0;
                this.discarding = false;
            }
            else {
                buffer.skipBytes(this.discardedBytes = buffer.readableBytes());
            }
            return null;
        }
        if (eol < 0) {
            final int length = buffer.readableBytes();
            if (length > 108) {
                buffer.skipBytes(this.discardedBytes = length);
                this.discarding = true;
                this.failOverLimit(ctx, "over " + this.discardedBytes);
            }
            return null;
        }
        final int length = eol - buffer.readerIndex();
        if (length > 108) {
            buffer.readerIndex(eol + 2);
            this.failOverLimit(ctx, length);
            return null;
        }
        final ByteBuf frame = buffer.readSlice(length);
        buffer.skipBytes(2);
        return frame;
    }
    
    private void failOverLimit(final ChannelHandlerContext ctx, final int length) {
        this.failOverLimit(ctx, String.valueOf(length));
    }
    
    private void failOverLimit(final ChannelHandlerContext ctx, final String length) {
        final int maxLength = (this.version == 1) ? 108 : this.v2MaxHeaderSize;
        this.fail(ctx, "header length (" + length + ") exceeds the allowed maximum (" + maxLength + ')', null);
    }
    
    private void fail(final ChannelHandlerContext ctx, final String errMsg, final Throwable t) {
        this.finished = true;
        ctx.close();
        HAProxyProtocolException ppex;
        if (errMsg != null && t != null) {
            ppex = new HAProxyProtocolException(errMsg, t);
        }
        else if (errMsg != null) {
            ppex = new HAProxyProtocolException(errMsg);
        }
        else if (t != null) {
            ppex = new HAProxyProtocolException(t);
        }
        else {
            ppex = new HAProxyProtocolException();
        }
        throw ppex;
    }
    
    public static ProtocolDetectionResult<HAProxyProtocolVersion> detectProtocol(final ByteBuf buffer) {
        if (buffer.readableBytes() < 12) {
            return ProtocolDetectionResult.needsMoreData();
        }
        final int idx = buffer.readerIndex();
        if (match(HAProxyMessageDecoder.BINARY_PREFIX, buffer, idx)) {
            return HAProxyMessageDecoder.DETECTION_RESULT_V2;
        }
        if (match(HAProxyMessageDecoder.TEXT_PREFIX, buffer, idx)) {
            return HAProxyMessageDecoder.DETECTION_RESULT_V1;
        }
        return ProtocolDetectionResult.invalid();
    }
    
    private static boolean match(final byte[] prefix, final ByteBuf buffer, final int idx) {
        for (int i = 0; i < prefix.length; ++i) {
            final byte b = buffer.getByte(idx + i);
            if (b != prefix[i]) {
                return false;
            }
        }
        return true;
    }
    
    static {
        BINARY_PREFIX = new byte[] { 13, 10, 13, 10, 0, 13, 10, 81, 85, 73, 84, 10 };
        TEXT_PREFIX = new byte[] { 80, 82, 79, 88, 89 };
        BINARY_PREFIX_LENGTH = HAProxyMessageDecoder.BINARY_PREFIX.length;
        DETECTION_RESULT_V1 = ProtocolDetectionResult.detected(HAProxyProtocolVersion.V1);
        DETECTION_RESULT_V2 = ProtocolDetectionResult.detected(HAProxyProtocolVersion.V2);
    }
}
