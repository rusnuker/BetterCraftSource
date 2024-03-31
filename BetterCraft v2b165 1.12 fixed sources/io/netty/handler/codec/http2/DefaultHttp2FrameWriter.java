// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.buffer.Unpooled;
import io.netty.util.internal.PlatformDependent;
import java.util.Iterator;
import io.netty.util.collection.CharObjectMap;
import io.netty.util.internal.ObjectUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;

public class DefaultHttp2FrameWriter implements Http2FrameWriter, Http2FrameSizePolicy, Configuration
{
    private static final String STREAM_ID = "Stream ID";
    private static final String STREAM_DEPENDENCY = "Stream Dependency";
    private static final ByteBuf ZERO_BUFFER;
    private final Http2HeadersEncoder headersEncoder;
    private int maxFrameSize;
    
    public DefaultHttp2FrameWriter() {
        this(new DefaultHttp2HeadersEncoder());
    }
    
    public DefaultHttp2FrameWriter(final Http2HeadersEncoder.SensitivityDetector headersSensativityDetector) {
        this(new DefaultHttp2HeadersEncoder(headersSensativityDetector));
    }
    
    public DefaultHttp2FrameWriter(final Http2HeadersEncoder.SensitivityDetector headersSensativityDetector, final boolean ignoreMaxHeaderListSize) {
        this(new DefaultHttp2HeadersEncoder(headersSensativityDetector, ignoreMaxHeaderListSize));
    }
    
    public DefaultHttp2FrameWriter(final Http2HeadersEncoder headersEncoder) {
        this.headersEncoder = headersEncoder;
        this.maxFrameSize = 16384;
    }
    
    @Override
    public Configuration configuration() {
        return this;
    }
    
    @Override
    public Http2HeadersEncoder.Configuration headersConfiguration() {
        return this.headersEncoder.configuration();
    }
    
    @Override
    public Http2FrameSizePolicy frameSizePolicy() {
        return this;
    }
    
    @Override
    public void maxFrameSize(final int max) throws Http2Exception {
        if (!Http2CodecUtil.isMaxFrameSizeValid(max)) {
            throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Invalid MAX_FRAME_SIZE specified in sent settings: %d", max);
        }
        this.maxFrameSize = max;
    }
    
    @Override
    public int maxFrameSize() {
        return this.maxFrameSize;
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public ChannelFuture writeData(final ChannelHandlerContext ctx, final int streamId, final ByteBuf data, int padding, final boolean endStream, final ChannelPromise promise) {
        final Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
        final DataFrameHeader header = new DataFrameHeader(ctx, streamId);
        boolean needToReleaseHeaders = true;
        boolean needToReleaseData = true;
        try {
            verifyStreamId(streamId, "Stream ID");
            Http2CodecUtil.verifyPadding(padding);
            int remainingData = data.readableBytes();
            boolean lastFrame;
            do {
                final int frameDataBytes = Math.min(remainingData, this.maxFrameSize);
                final int framePaddingBytes = Math.min(padding, Math.max(0, this.maxFrameSize - 1 - frameDataBytes));
                padding -= framePaddingBytes;
                remainingData -= frameDataBytes;
                lastFrame = (remainingData == 0 && padding == 0);
                final ByteBuf frameHeader = header.slice(frameDataBytes, framePaddingBytes, lastFrame && endStream);
                needToReleaseHeaders = !lastFrame;
                ctx.write(lastFrame ? frameHeader : frameHeader.retain(), promiseAggregator.newPromise());
                final ByteBuf frameData = data.readSlice(frameDataBytes);
                needToReleaseData = !lastFrame;
                ctx.write(lastFrame ? frameData : frameData.retain(), promiseAggregator.newPromise());
                if (paddingBytes(framePaddingBytes) > 0) {
                    ctx.write(DefaultHttp2FrameWriter.ZERO_BUFFER.slice(0, paddingBytes(framePaddingBytes)), promiseAggregator.newPromise());
                }
            } while (!lastFrame);
        }
        catch (final Throwable t) {
            if (needToReleaseHeaders) {
                header.release();
            }
            if (needToReleaseData) {
                data.release();
            }
            promiseAggregator.setFailure(t);
        }
        return promiseAggregator.doneAllocatingPromises();
    }
    
    @Override
    public ChannelFuture writeHeaders(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int padding, final boolean endStream, final ChannelPromise promise) {
        return this.writeHeadersInternal(ctx, streamId, headers, padding, endStream, false, 0, (short)0, false, promise);
    }
    
    @Override
    public ChannelFuture writeHeaders(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int streamDependency, final short weight, final boolean exclusive, final int padding, final boolean endStream, final ChannelPromise promise) {
        return this.writeHeadersInternal(ctx, streamId, headers, padding, endStream, true, streamDependency, weight, exclusive, promise);
    }
    
    @Override
    public ChannelFuture writePriority(final ChannelHandlerContext ctx, final int streamId, final int streamDependency, final short weight, final boolean exclusive, final ChannelPromise promise) {
        try {
            verifyStreamId(streamId, "Stream ID");
            verifyStreamId(streamDependency, "Stream Dependency");
            verifyWeight(weight);
            final ByteBuf buf = ctx.alloc().buffer(14);
            Http2CodecUtil.writeFrameHeaderInternal(buf, 5, (byte)2, new Http2Flags(), streamId);
            final long word1 = exclusive ? (0x80000000L | (long)streamDependency) : streamDependency;
            Http2CodecUtil.writeUnsignedInt(word1, buf);
            buf.writeByte(weight - 1);
            return ctx.write(buf, promise);
        }
        catch (final Throwable t) {
            return promise.setFailure(t);
        }
    }
    
    @Override
    public ChannelFuture writeRstStream(final ChannelHandlerContext ctx, final int streamId, final long errorCode, final ChannelPromise promise) {
        try {
            verifyStreamId(streamId, "Stream ID");
            verifyErrorCode(errorCode);
            final ByteBuf buf = ctx.alloc().buffer(13);
            Http2CodecUtil.writeFrameHeaderInternal(buf, 4, (byte)3, new Http2Flags(), streamId);
            Http2CodecUtil.writeUnsignedInt(errorCode, buf);
            return ctx.write(buf, promise);
        }
        catch (final Throwable t) {
            return promise.setFailure(t);
        }
    }
    
    @Override
    public ChannelFuture writeSettings(final ChannelHandlerContext ctx, final Http2Settings settings, final ChannelPromise promise) {
        try {
            ObjectUtil.checkNotNull(settings, "settings");
            final int payloadLength = 6 * settings.size();
            final ByteBuf buf = ctx.alloc().buffer(9 + settings.size() * 6);
            Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)4, new Http2Flags(), 0);
            for (final CharObjectMap.PrimitiveEntry<Long> entry : settings.entries()) {
                Http2CodecUtil.writeUnsignedShort(entry.key(), buf);
                Http2CodecUtil.writeUnsignedInt(entry.value(), buf);
            }
            return ctx.write(buf, promise);
        }
        catch (final Throwable t) {
            return promise.setFailure(t);
        }
    }
    
    @Override
    public ChannelFuture writeSettingsAck(final ChannelHandlerContext ctx, final ChannelPromise promise) {
        try {
            final ByteBuf buf = ctx.alloc().buffer(9);
            Http2CodecUtil.writeFrameHeaderInternal(buf, 0, (byte)4, new Http2Flags().ack(true), 0);
            return ctx.write(buf, promise);
        }
        catch (final Throwable t) {
            return promise.setFailure(t);
        }
    }
    
    @Override
    public ChannelFuture writePing(final ChannelHandlerContext ctx, final boolean ack, final ByteBuf data, final ChannelPromise promise) {
        boolean releaseData = true;
        final Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
        try {
            verifyPingPayload(data);
            final Http2Flags flags = ack ? new Http2Flags().ack(true) : new Http2Flags();
            final ByteBuf buf = ctx.alloc().buffer(9);
            Http2CodecUtil.writeFrameHeaderInternal(buf, data.readableBytes(), (byte)6, flags, 0);
            ctx.write(buf, promiseAggregator.newPromise());
            releaseData = false;
            ctx.write(data, promiseAggregator.newPromise());
        }
        catch (final Throwable t) {
            if (releaseData) {
                data.release();
            }
            promiseAggregator.setFailure(t);
        }
        return promiseAggregator.doneAllocatingPromises();
    }
    
    @Override
    public ChannelFuture writePushPromise(final ChannelHandlerContext ctx, final int streamId, final int promisedStreamId, final Http2Headers headers, final int padding, final ChannelPromise promise) {
        ByteBuf headerBlock = null;
        final Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
        try {
            verifyStreamId(streamId, "Stream ID");
            verifyStreamId(promisedStreamId, "Promised Stream ID");
            Http2CodecUtil.verifyPadding(padding);
            headerBlock = ctx.alloc().buffer();
            this.headersEncoder.encodeHeaders(streamId, headers, headerBlock);
            final Http2Flags flags = new Http2Flags().paddingPresent(padding > 0);
            final int nonFragmentLength = 4 + padding;
            final int maxFragmentLength = this.maxFrameSize - nonFragmentLength;
            final ByteBuf fragment = headerBlock.readRetainedSlice(Math.min(headerBlock.readableBytes(), maxFragmentLength));
            flags.endOfHeaders(!headerBlock.isReadable());
            final int payloadLength = fragment.readableBytes() + nonFragmentLength;
            final ByteBuf buf = ctx.alloc().buffer(14);
            Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)5, flags, streamId);
            writePaddingLength(buf, padding);
            buf.writeInt(promisedStreamId);
            ctx.write(buf, promiseAggregator.newPromise());
            ctx.write(fragment, promiseAggregator.newPromise());
            if (paddingBytes(padding) > 0) {
                ctx.write(DefaultHttp2FrameWriter.ZERO_BUFFER.slice(0, paddingBytes(padding)), promiseAggregator.newPromise());
            }
            if (!flags.endOfHeaders()) {
                this.writeContinuationFrames(ctx, streamId, headerBlock, padding, promiseAggregator);
            }
        }
        catch (final Http2Exception e) {
            promiseAggregator.setFailure(e);
        }
        catch (final Throwable t) {
            promiseAggregator.setFailure(t);
            promiseAggregator.doneAllocatingPromises();
            PlatformDependent.throwException(t);
        }
        finally {
            if (headerBlock != null) {
                headerBlock.release();
            }
        }
        return promiseAggregator.doneAllocatingPromises();
    }
    
    @Override
    public ChannelFuture writeGoAway(final ChannelHandlerContext ctx, final int lastStreamId, final long errorCode, final ByteBuf debugData, final ChannelPromise promise) {
        boolean releaseData = true;
        final Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
        try {
            verifyStreamOrConnectionId(lastStreamId, "Last Stream ID");
            verifyErrorCode(errorCode);
            final int payloadLength = 8 + debugData.readableBytes();
            final ByteBuf buf = ctx.alloc().buffer(17);
            Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)7, new Http2Flags(), 0);
            buf.writeInt(lastStreamId);
            Http2CodecUtil.writeUnsignedInt(errorCode, buf);
            ctx.write(buf, promiseAggregator.newPromise());
            releaseData = false;
            ctx.write(debugData, promiseAggregator.newPromise());
        }
        catch (final Throwable t) {
            if (releaseData) {
                debugData.release();
            }
            promiseAggregator.setFailure(t);
        }
        return promiseAggregator.doneAllocatingPromises();
    }
    
    @Override
    public ChannelFuture writeWindowUpdate(final ChannelHandlerContext ctx, final int streamId, final int windowSizeIncrement, final ChannelPromise promise) {
        try {
            verifyStreamOrConnectionId(streamId, "Stream ID");
            verifyWindowSizeIncrement(windowSizeIncrement);
            final ByteBuf buf = ctx.alloc().buffer(13);
            Http2CodecUtil.writeFrameHeaderInternal(buf, 4, (byte)8, new Http2Flags(), streamId);
            buf.writeInt(windowSizeIncrement);
            return ctx.write(buf, promise);
        }
        catch (final Throwable t) {
            return promise.setFailure(t);
        }
    }
    
    @Override
    public ChannelFuture writeFrame(final ChannelHandlerContext ctx, final byte frameType, final int streamId, final Http2Flags flags, final ByteBuf payload, final ChannelPromise promise) {
        boolean releaseData = true;
        final Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
        try {
            verifyStreamOrConnectionId(streamId, "Stream ID");
            final ByteBuf buf = ctx.alloc().buffer(9);
            Http2CodecUtil.writeFrameHeaderInternal(buf, payload.readableBytes(), frameType, flags, streamId);
            ctx.write(buf, promiseAggregator.newPromise());
            releaseData = false;
            ctx.write(payload, promiseAggregator.newPromise());
        }
        catch (final Throwable t) {
            if (releaseData) {
                payload.release();
            }
            promiseAggregator.setFailure(t);
        }
        return promiseAggregator.doneAllocatingPromises();
    }
    
    private ChannelFuture writeHeadersInternal(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int padding, final boolean endStream, final boolean hasPriority, final int streamDependency, final short weight, final boolean exclusive, final ChannelPromise promise) {
        ByteBuf headerBlock = null;
        final Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
        try {
            verifyStreamId(streamId, "Stream ID");
            if (hasPriority) {
                verifyStreamOrConnectionId(streamDependency, "Stream Dependency");
                Http2CodecUtil.verifyPadding(padding);
                verifyWeight(weight);
            }
            headerBlock = ctx.alloc().buffer();
            this.headersEncoder.encodeHeaders(streamId, headers, headerBlock);
            final Http2Flags flags = new Http2Flags().endOfStream(endStream).priorityPresent(hasPriority).paddingPresent(padding > 0);
            final int nonFragmentBytes = padding + flags.getNumPriorityBytes();
            final int maxFragmentLength = this.maxFrameSize - nonFragmentBytes;
            final ByteBuf fragment = headerBlock.readRetainedSlice(Math.min(headerBlock.readableBytes(), maxFragmentLength));
            flags.endOfHeaders(!headerBlock.isReadable());
            final int payloadLength = fragment.readableBytes() + nonFragmentBytes;
            final ByteBuf buf = ctx.alloc().buffer(15);
            Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)1, flags, streamId);
            writePaddingLength(buf, padding);
            if (hasPriority) {
                final long word1 = exclusive ? (0x80000000L | (long)streamDependency) : streamDependency;
                Http2CodecUtil.writeUnsignedInt(word1, buf);
                buf.writeByte(weight - 1);
            }
            ctx.write(buf, promiseAggregator.newPromise());
            ctx.write(fragment, promiseAggregator.newPromise());
            if (paddingBytes(padding) > 0) {
                ctx.write(DefaultHttp2FrameWriter.ZERO_BUFFER.slice(0, paddingBytes(padding)), promiseAggregator.newPromise());
            }
            if (!flags.endOfHeaders()) {
                this.writeContinuationFrames(ctx, streamId, headerBlock, padding, promiseAggregator);
            }
        }
        catch (final Http2Exception e) {
            promiseAggregator.setFailure(e);
        }
        catch (final Throwable t) {
            promiseAggregator.setFailure(t);
            promiseAggregator.doneAllocatingPromises();
            PlatformDependent.throwException(t);
        }
        finally {
            if (headerBlock != null) {
                headerBlock.release();
            }
        }
        return promiseAggregator.doneAllocatingPromises();
    }
    
    private ChannelFuture writeContinuationFrames(final ChannelHandlerContext ctx, final int streamId, final ByteBuf headerBlock, final int padding, final Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator) {
        Http2Flags flags = new Http2Flags().paddingPresent(padding > 0);
        final int maxFragmentLength = this.maxFrameSize - padding;
        if (maxFragmentLength <= 0) {
            return promiseAggregator.setFailure(new IllegalArgumentException("Padding [" + padding + "] is too large for max frame size [" + this.maxFrameSize + "]"));
        }
        if (headerBlock.isReadable()) {
            int fragmentReadableBytes = Math.min(headerBlock.readableBytes(), maxFragmentLength);
            int payloadLength = fragmentReadableBytes + padding;
            ByteBuf buf = ctx.alloc().buffer(10);
            Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)9, flags, streamId);
            writePaddingLength(buf, padding);
            do {
                fragmentReadableBytes = Math.min(headerBlock.readableBytes(), maxFragmentLength);
                final ByteBuf fragment = headerBlock.readRetainedSlice(fragmentReadableBytes);
                payloadLength = fragmentReadableBytes + padding;
                if (headerBlock.isReadable()) {
                    ctx.write(buf.retain(), promiseAggregator.newPromise());
                }
                else {
                    flags = flags.endOfHeaders(true);
                    buf.release();
                    buf = ctx.alloc().buffer(10);
                    Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)9, flags, streamId);
                    writePaddingLength(buf, padding);
                    ctx.write(buf, promiseAggregator.newPromise());
                }
                ctx.write(fragment, promiseAggregator.newPromise());
                if (paddingBytes(padding) > 0) {
                    ctx.write(DefaultHttp2FrameWriter.ZERO_BUFFER.slice(0, paddingBytes(padding)), promiseAggregator.newPromise());
                }
            } while (headerBlock.isReadable());
        }
        return promiseAggregator;
    }
    
    private static int paddingBytes(final int padding) {
        return padding - 1;
    }
    
    private static void writePaddingLength(final ByteBuf buf, final int padding) {
        if (padding > 0) {
            buf.writeByte(padding - 1);
        }
    }
    
    private static void verifyStreamId(final int streamId, final String argumentName) {
        if (streamId <= 0) {
            throw new IllegalArgumentException(argumentName + " must be > 0");
        }
    }
    
    private static void verifyStreamOrConnectionId(final int streamId, final String argumentName) {
        if (streamId < 0) {
            throw new IllegalArgumentException(argumentName + " must be >= 0");
        }
    }
    
    private static void verifyWeight(final short weight) {
        if (weight < 1 || weight > 256) {
            throw new IllegalArgumentException("Invalid weight: " + weight);
        }
    }
    
    private static void verifyErrorCode(final long errorCode) {
        if (errorCode < 0L || errorCode > 4294967295L) {
            throw new IllegalArgumentException("Invalid errorCode: " + errorCode);
        }
    }
    
    private static void verifyWindowSizeIncrement(final int windowSizeIncrement) {
        if (windowSizeIncrement < 0) {
            throw new IllegalArgumentException("WindowSizeIncrement must be >= 0");
        }
    }
    
    private static void verifyPingPayload(final ByteBuf data) {
        if (data == null || data.readableBytes() != 8) {
            throw new IllegalArgumentException("Opaque data must be 8 bytes");
        }
    }
    
    static {
        ZERO_BUFFER = Unpooled.unreleasableBuffer(Unpooled.directBuffer(255).writeZero(255)).asReadOnly();
    }
    
    private static final class DataFrameHeader
    {
        private final int streamId;
        private final ByteBuf buffer;
        private final Http2Flags flags;
        private int prevData;
        private int prevPadding;
        private ByteBuf frameHeader;
        
        DataFrameHeader(final ChannelHandlerContext ctx, final int streamId) {
            this.flags = new Http2Flags();
            this.buffer = ctx.alloc().buffer(30);
            this.streamId = streamId;
        }
        
        ByteBuf slice(final int data, final int padding, final boolean endOfStream) {
            if (data != this.prevData || padding != this.prevPadding || endOfStream != this.flags.endOfStream() || this.frameHeader == null) {
                this.prevData = data;
                this.prevPadding = padding;
                this.flags.paddingPresent(padding > 0);
                this.flags.endOfStream(endOfStream);
                this.frameHeader = this.buffer.readSlice(10).writerIndex(0);
                final int payloadLength = data + padding;
                Http2CodecUtil.writeFrameHeaderInternal(this.frameHeader, payloadLength, (byte)0, this.flags, this.streamId);
                writePaddingLength(this.frameHeader, padding);
            }
            return this.frameHeader.slice();
        }
        
        void release() {
            this.buffer.release();
        }
    }
}
