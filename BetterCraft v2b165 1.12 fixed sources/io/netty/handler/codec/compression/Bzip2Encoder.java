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
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToByteEncoder;

public class Bzip2Encoder extends MessageToByteEncoder<ByteBuf>
{
    private State currentState;
    private final Bzip2BitWriter writer;
    private final int streamBlockSize;
    private int streamCRC;
    private Bzip2BlockCompressor blockCompressor;
    private volatile boolean finished;
    private volatile ChannelHandlerContext ctx;
    
    public Bzip2Encoder() {
        this(9);
    }
    
    public Bzip2Encoder(final int blockSizeMultiplier) {
        this.currentState = State.INIT;
        this.writer = new Bzip2BitWriter();
        if (blockSizeMultiplier < 1 || blockSizeMultiplier > 9) {
            throw new IllegalArgumentException("blockSizeMultiplier: " + blockSizeMultiplier + " (expected: 1-9)");
        }
        this.streamBlockSize = blockSizeMultiplier * 100000;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf in, final ByteBuf out) throws Exception {
        if (this.finished) {
            out.writeBytes(in);
            return;
        }
        while (true) {
            switch (this.currentState) {
                case INIT: {
                    out.ensureWritable(4);
                    out.writeMedium(4348520);
                    out.writeByte(48 + this.streamBlockSize / 100000);
                    this.currentState = State.INIT_BLOCK;
                }
                case INIT_BLOCK: {
                    this.blockCompressor = new Bzip2BlockCompressor(this.writer, this.streamBlockSize);
                    this.currentState = State.WRITE_DATA;
                }
                case WRITE_DATA: {
                    if (!in.isReadable()) {
                        return;
                    }
                    final Bzip2BlockCompressor blockCompressor = this.blockCompressor;
                    final int length = Math.min(in.readableBytes(), blockCompressor.availableSize());
                    final int bytesWritten = blockCompressor.write(in, in.readerIndex(), length);
                    in.skipBytes(bytesWritten);
                    if (blockCompressor.isFull()) {
                        this.currentState = State.CLOSE_BLOCK;
                    }
                    if (in.isReadable()) {
                        continue;
                    }
                    return;
                }
                case CLOSE_BLOCK: {
                    this.closeBlock(out);
                    this.currentState = State.INIT_BLOCK;
                    continue;
                }
                default: {
                    throw new IllegalStateException();
                }
            }
        }
    }
    
    private void closeBlock(final ByteBuf out) {
        final Bzip2BlockCompressor blockCompressor = this.blockCompressor;
        if (!blockCompressor.isEmpty()) {
            blockCompressor.close(out);
            final int blockCRC = blockCompressor.crc();
            this.streamCRC = ((this.streamCRC << 1 | this.streamCRC >>> 31) ^ blockCRC);
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
                final ChannelFuture f = Bzip2Encoder.this.finishEncode(Bzip2Encoder.this.ctx(), promise);
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
    
    private ChannelFuture finishEncode(final ChannelHandlerContext ctx, final ChannelPromise promise) {
        if (this.finished) {
            promise.setSuccess();
            return promise;
        }
        this.finished = true;
        final ByteBuf footer = ctx.alloc().buffer();
        this.closeBlock(footer);
        final int streamCRC = this.streamCRC;
        final Bzip2BitWriter writer = this.writer;
        try {
            writer.writeBits(footer, 24, 1536581L);
            writer.writeBits(footer, 24, 3690640L);
            writer.writeInt(footer, streamCRC);
            writer.flush(footer);
        }
        finally {
            this.blockCompressor = null;
        }
        return ctx.writeAndFlush(footer, promise);
    }
    
    private ChannelHandlerContext ctx() {
        final ChannelHandlerContext ctx = this.ctx;
        if (ctx == null) {
            throw new IllegalStateException("not added to a pipeline");
        }
        return ctx;
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }
    
    private enum State
    {
        INIT, 
        INIT_BLOCK, 
        WRITE_DATA, 
        CLOSE_BLOCK;
    }
}
