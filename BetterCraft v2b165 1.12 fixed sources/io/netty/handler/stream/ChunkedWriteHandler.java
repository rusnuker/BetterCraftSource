// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.stream;

import io.netty.channel.ChannelProgressivePromise;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import java.nio.channels.ClosedChannelException;
import io.netty.channel.ChannelPromise;
import java.util.ArrayDeque;
import io.netty.channel.ChannelHandlerContext;
import java.util.Queue;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.ChannelDuplexHandler;

public class ChunkedWriteHandler extends ChannelDuplexHandler
{
    private static final InternalLogger logger;
    private final Queue<PendingWrite> queue;
    private volatile ChannelHandlerContext ctx;
    private PendingWrite currentWrite;
    
    public ChunkedWriteHandler() {
        this.queue = new ArrayDeque<PendingWrite>();
    }
    
    @Deprecated
    public ChunkedWriteHandler(final int maxPendingWrites) {
        this.queue = new ArrayDeque<PendingWrite>();
        if (maxPendingWrites <= 0) {
            throw new IllegalArgumentException("maxPendingWrites: " + maxPendingWrites + " (expected: > 0)");
        }
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }
    
    public void resumeTransfer() {
        final ChannelHandlerContext ctx = this.ctx;
        if (ctx == null) {
            return;
        }
        if (ctx.executor().inEventLoop()) {
            try {
                this.doFlush(ctx);
            }
            catch (final Exception e) {
                if (ChunkedWriteHandler.logger.isWarnEnabled()) {
                    ChunkedWriteHandler.logger.warn("Unexpected exception while sending chunks.", e);
                }
            }
        }
        else {
            ctx.executor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        ChunkedWriteHandler.this.doFlush(ctx);
                    }
                    catch (final Exception e) {
                        if (ChunkedWriteHandler.logger.isWarnEnabled()) {
                            ChunkedWriteHandler.logger.warn("Unexpected exception while sending chunks.", e);
                        }
                    }
                }
            });
        }
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        this.queue.add(new PendingWrite(msg, promise));
    }
    
    @Override
    public void flush(final ChannelHandlerContext ctx) throws Exception {
        if (!this.doFlush(ctx)) {
            ctx.flush();
        }
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        this.doFlush(ctx);
        ctx.fireChannelInactive();
    }
    
    @Override
    public void channelWritabilityChanged(final ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().isWritable()) {
            this.doFlush(ctx);
        }
        ctx.fireChannelWritabilityChanged();
    }
    
    private void discard(Throwable cause) {
        while (true) {
            PendingWrite currentWrite = this.currentWrite;
            if (this.currentWrite == null) {
                currentWrite = this.queue.poll();
            }
            else {
                this.currentWrite = null;
            }
            if (currentWrite == null) {
                break;
            }
            final Object message = currentWrite.msg;
            if (message instanceof ChunkedInput) {
                final ChunkedInput<?> in = (ChunkedInput<?>)message;
                try {
                    if (!in.isEndOfInput()) {
                        if (cause == null) {
                            cause = new ClosedChannelException();
                        }
                        currentWrite.fail(cause);
                    }
                    else {
                        currentWrite.success(in.length());
                    }
                    closeInput(in);
                }
                catch (final Exception e) {
                    currentWrite.fail(e);
                    ChunkedWriteHandler.logger.warn(ChunkedInput.class.getSimpleName() + ".isEndOfInput() failed", e);
                    closeInput(in);
                }
            }
            else {
                if (cause == null) {
                    cause = new ClosedChannelException();
                }
                currentWrite.fail(cause);
            }
        }
    }
    
    private boolean doFlush(final ChannelHandlerContext ctx) throws Exception {
        final Channel channel = ctx.channel();
        if (!channel.isActive()) {
            this.discard(null);
            return false;
        }
        boolean flushed = false;
        final ByteBufAllocator allocator = ctx.alloc();
        while (channel.isWritable()) {
            if (this.currentWrite == null) {
                this.currentWrite = this.queue.poll();
            }
            if (this.currentWrite == null) {
                break;
            }
            final PendingWrite currentWrite = this.currentWrite;
            final Object pendingMessage = currentWrite.msg;
            if (pendingMessage instanceof ChunkedInput) {
                final ChunkedInput<?> chunks = (ChunkedInput<?>)pendingMessage;
                Object message = null;
                boolean endOfInput;
                boolean suspend;
                try {
                    message = chunks.readChunk(allocator);
                    endOfInput = chunks.isEndOfInput();
                    suspend = (message == null && !endOfInput);
                }
                catch (final Throwable t) {
                    this.currentWrite = null;
                    if (message != null) {
                        ReferenceCountUtil.release(message);
                    }
                    currentWrite.fail(t);
                    closeInput(chunks);
                    break;
                }
                if (suspend) {
                    break;
                }
                if (message == null) {
                    message = Unpooled.EMPTY_BUFFER;
                }
                final ChannelFuture f = ctx.write(message);
                if (endOfInput) {
                    this.currentWrite = null;
                    f.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                        @Override
                        public void operationComplete(final ChannelFuture future) throws Exception {
                            currentWrite.progress(chunks.progress(), chunks.length());
                            currentWrite.success(chunks.length());
                            ChunkedWriteHandler.closeInput(chunks);
                        }
                    });
                }
                else if (channel.isWritable()) {
                    f.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                        @Override
                        public void operationComplete(final ChannelFuture future) throws Exception {
                            if (!future.isSuccess()) {
                                ChunkedWriteHandler.closeInput((ChunkedInput<?>)pendingMessage);
                                currentWrite.fail(future.cause());
                            }
                            else {
                                currentWrite.progress(chunks.progress(), chunks.length());
                            }
                        }
                    });
                }
                else {
                    f.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                        @Override
                        public void operationComplete(final ChannelFuture future) throws Exception {
                            if (!future.isSuccess()) {
                                ChunkedWriteHandler.closeInput((ChunkedInput<?>)pendingMessage);
                                currentWrite.fail(future.cause());
                            }
                            else {
                                currentWrite.progress(chunks.progress(), chunks.length());
                                if (channel.isWritable()) {
                                    ChunkedWriteHandler.this.resumeTransfer();
                                }
                            }
                        }
                    });
                }
            }
            else {
                ctx.write(pendingMessage, currentWrite.promise);
                this.currentWrite = null;
            }
            ctx.flush();
            flushed = true;
            if (!channel.isActive()) {
                this.discard(new ClosedChannelException());
                break;
            }
        }
        return flushed;
    }
    
    static void closeInput(final ChunkedInput<?> chunks) {
        try {
            chunks.close();
        }
        catch (final Throwable t) {
            if (ChunkedWriteHandler.logger.isWarnEnabled()) {
                ChunkedWriteHandler.logger.warn("Failed to close a chunked input.", t);
            }
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(ChunkedWriteHandler.class);
    }
    
    private static final class PendingWrite
    {
        final Object msg;
        final ChannelPromise promise;
        
        PendingWrite(final Object msg, final ChannelPromise promise) {
            this.msg = msg;
            this.promise = promise;
        }
        
        void fail(final Throwable cause) {
            ReferenceCountUtil.release(this.msg);
            this.promise.tryFailure(cause);
        }
        
        void success(final long total) {
            if (this.promise.isDone()) {
                return;
            }
            if (this.promise instanceof ChannelProgressivePromise) {
                ((ChannelProgressivePromise)this.promise).tryProgress(total, total);
            }
            this.promise.trySuccess();
        }
        
        void progress(final long progress, final long total) {
            if (this.promise instanceof ChannelProgressivePromise) {
                ((ChannelProgressivePromise)this.promise).tryProgress(progress, total);
            }
        }
    }
}
