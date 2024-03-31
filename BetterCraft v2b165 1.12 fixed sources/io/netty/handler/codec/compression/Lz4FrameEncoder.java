// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.compression;

import java.util.concurrent.TimeUnit;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelPromiseNotifier;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import java.nio.ByteBuffer;
import net.jpountz.lz4.LZ4Exception;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.EncoderException;
import io.netty.util.internal.ObjectUtil;
import java.util.zip.Checksum;
import net.jpountz.xxhash.XXHashFactory;
import net.jpountz.lz4.LZ4Factory;
import io.netty.channel.ChannelHandlerContext;
import net.jpountz.lz4.LZ4Compressor;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToByteEncoder;

public class Lz4FrameEncoder extends MessageToByteEncoder<ByteBuf>
{
    static final int DEFAULT_MAX_ENCODE_SIZE = Integer.MAX_VALUE;
    private final int blockSize;
    private LZ4Compressor compressor;
    private ByteBufChecksum checksum;
    private final int compressionLevel;
    private ByteBuf buffer;
    private final int maxEncodeSize;
    private volatile boolean finished;
    private volatile ChannelHandlerContext ctx;
    
    public Lz4FrameEncoder() {
        this(false);
    }
    
    public Lz4FrameEncoder(final boolean highCompressor) {
        this(LZ4Factory.fastestInstance(), highCompressor, 65536, XXHashFactory.fastestInstance().newStreamingHash32(-1756908916).asChecksum());
    }
    
    public Lz4FrameEncoder(final LZ4Factory factory, final boolean highCompressor, final int blockSize, final Checksum checksum) {
        this(factory, highCompressor, blockSize, checksum, Integer.MAX_VALUE);
    }
    
    public Lz4FrameEncoder(final LZ4Factory factory, final boolean highCompressor, final int blockSize, final Checksum checksum, final int maxEncodeSize) {
        if (factory == null) {
            throw new NullPointerException("factory");
        }
        if (checksum == null) {
            throw new NullPointerException("checksum");
        }
        this.compressor = (highCompressor ? factory.highCompressor() : factory.fastCompressor());
        this.checksum = ByteBufChecksum.wrapChecksum(checksum);
        this.compressionLevel = compressionLevel(blockSize);
        this.blockSize = blockSize;
        this.maxEncodeSize = ObjectUtil.checkPositive(maxEncodeSize, "maxEncodeSize");
        this.finished = false;
    }
    
    private static int compressionLevel(final int blockSize) {
        if (blockSize < 64 || blockSize > 33554432) {
            throw new IllegalArgumentException(String.format("blockSize: %d (expected: %d-%d)", blockSize, 64, 33554432));
        }
        int compressionLevel = 32 - Integer.numberOfLeadingZeros(blockSize - 1);
        compressionLevel = Math.max(0, compressionLevel - 10);
        return compressionLevel;
    }
    
    @Override
    protected ByteBuf allocateBuffer(final ChannelHandlerContext ctx, final ByteBuf msg, final boolean preferDirect) {
        return this.allocateBuffer(ctx, msg, preferDirect, true);
    }
    
    private ByteBuf allocateBuffer(final ChannelHandlerContext ctx, final ByteBuf msg, final boolean preferDirect, final boolean allowEmptyReturn) {
        int targetBufSize = 0;
        int remaining = msg.readableBytes() + this.buffer.readableBytes();
        if (remaining < 0) {
            throw new EncoderException("too much data to allocate a buffer for compression");
        }
        while (remaining > 0) {
            final int curSize = Math.min(this.blockSize, remaining);
            remaining -= curSize;
            targetBufSize += this.compressor.maxCompressedLength(curSize) + 21;
        }
        if (targetBufSize > this.maxEncodeSize || 0 > targetBufSize) {
            throw new EncoderException(String.format("requested encode buffer size (%d bytes) exceeds the maximum allowable size (%d bytes)", targetBufSize, this.maxEncodeSize));
        }
        if (allowEmptyReturn && targetBufSize < this.blockSize) {
            return Unpooled.EMPTY_BUFFER;
        }
        if (preferDirect) {
            return ctx.alloc().ioBuffer(targetBufSize, targetBufSize);
        }
        return ctx.alloc().heapBuffer(targetBufSize, targetBufSize);
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf in, final ByteBuf out) throws Exception {
        if (this.finished) {
            out.writeBytes(in);
            return;
        }
        final ByteBuf buffer = this.buffer;
        int length;
        while ((length = in.readableBytes()) > 0) {
            final int nextChunkSize = Math.min(length, buffer.writableBytes());
            in.readBytes(buffer, nextChunkSize);
            if (!buffer.isWritable()) {
                this.flushBufferedData(out);
            }
        }
    }
    
    private void flushBufferedData(final ByteBuf out) {
        final int flushableBytes = this.buffer.readableBytes();
        if (flushableBytes == 0) {
            return;
        }
        this.checksum.reset();
        this.checksum.update(this.buffer, this.buffer.readerIndex(), flushableBytes);
        final int check = (int)this.checksum.getValue();
        final int bufSize = this.compressor.maxCompressedLength(flushableBytes) + 21;
        out.ensureWritable(bufSize);
        final int idx = out.writerIndex();
        int compressedLength;
        try {
            final ByteBuffer outNioBuffer = out.internalNioBuffer(idx + 21, out.writableBytes() - 21);
            final int pos = outNioBuffer.position();
            this.compressor.compress(this.buffer.internalNioBuffer(this.buffer.readerIndex(), flushableBytes), outNioBuffer);
            compressedLength = outNioBuffer.position() - pos;
        }
        catch (final LZ4Exception e) {
            throw new CompressionException((Throwable)e);
        }
        int blockType;
        if (compressedLength >= flushableBytes) {
            blockType = 16;
            compressedLength = flushableBytes;
            out.setBytes(idx + 21, this.buffer, 0, flushableBytes);
        }
        else {
            blockType = 32;
        }
        out.setLong(idx, 5501767354678207339L);
        out.setByte(idx + 8, (byte)(blockType | this.compressionLevel));
        out.setIntLE(idx + 9, compressedLength);
        out.setIntLE(idx + 13, flushableBytes);
        out.setIntLE(idx + 17, check);
        out.writerIndex(idx + 21 + compressedLength);
        this.buffer.clear();
    }
    
    @Override
    public void flush(final ChannelHandlerContext ctx) throws Exception {
        if (this.buffer != null && this.buffer.isReadable()) {
            final ByteBuf buf = this.allocateBuffer(ctx, Unpooled.EMPTY_BUFFER, this.isPreferDirect(), false);
            this.flushBufferedData(buf);
            ctx.write(buf);
        }
        ctx.flush();
    }
    
    private ChannelFuture finishEncode(final ChannelHandlerContext ctx, final ChannelPromise promise) {
        if (this.finished) {
            promise.setSuccess();
            return promise;
        }
        this.finished = true;
        try {
            final ByteBuf footer = ctx.alloc().heapBuffer(this.compressor.maxCompressedLength(this.buffer.readableBytes()) + 21);
            this.flushBufferedData(footer);
            final int idx = footer.writerIndex();
            footer.setLong(idx, 5501767354678207339L);
            footer.setByte(idx + 8, (byte)(0x10 | this.compressionLevel));
            footer.setInt(idx + 9, 0);
            footer.setInt(idx + 13, 0);
            footer.setInt(idx + 17, 0);
            footer.writerIndex(idx + 21);
            return ctx.writeAndFlush(footer, promise);
        }
        finally {
            this.cleanup();
        }
    }
    
    private void cleanup() {
        this.compressor = null;
        this.checksum = null;
        if (this.buffer != null) {
            this.buffer.release();
            this.buffer = null;
        }
    }
    
    public boolean isClosed() {
        return this.finished;
    }
    
    public ChannelFuture close() {
        return this.close(this.ctx().newPromise());
    }
    
    public ChannelFuture close(final ChannelPromise promise) {
        final ChannelHandlerContext ctx = this.ctx();
        final EventExecutor executor = ctx.executor();
        if (executor.inEventLoop()) {
            return this.finishEncode(ctx, promise);
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final ChannelFuture f = Lz4FrameEncoder.this.finishEncode(Lz4FrameEncoder.this.ctx(), promise);
                f.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelPromiseNotifier(new ChannelPromise[] { promise }));
            }
        });
        return promise;
    }
    
    @Override
    public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        final ChannelFuture f = this.finishEncode(ctx, ctx.newPromise());
        f.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture f) throws Exception {
                ctx.close(promise);
            }
        });
        if (!f.isDone()) {
            ctx.executor().schedule((Runnable)new Runnable() {
                @Override
                public void run() {
                    ctx.close(promise);
                }
            }, 10L, TimeUnit.SECONDS);
        }
    }
    
    private ChannelHandlerContext ctx() {
        final ChannelHandlerContext ctx = this.ctx;
        if (ctx == null) {
            throw new IllegalStateException("not added to a pipeline");
        }
        return ctx;
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) {
        this.ctx = ctx;
        (this.buffer = Unpooled.wrappedBuffer(new byte[this.blockSize])).clear();
    }
    
    @Override
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        this.cleanup();
    }
    
    final ByteBuf getBackingBuffer() {
        return this.buffer;
    }
}
