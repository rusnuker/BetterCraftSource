// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.flush;

import io.netty.channel.ChannelPromise;
import java.util.concurrent.Future;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelDuplexHandler;

public class FlushConsolidationHandler extends ChannelDuplexHandler
{
    private final int explicitFlushAfterFlushes;
    private final boolean consolidateWhenNoReadInProgress;
    private final Runnable flushTask;
    private int flushPendingCount;
    private boolean readInProgress;
    private ChannelHandlerContext ctx;
    private Future<?> nextScheduledFlush;
    
    public FlushConsolidationHandler() {
        this(256, false);
    }
    
    public FlushConsolidationHandler(final int explicitFlushAfterFlushes) {
        this(explicitFlushAfterFlushes, false);
    }
    
    public FlushConsolidationHandler(final int explicitFlushAfterFlushes, final boolean consolidateWhenNoReadInProgress) {
        if (explicitFlushAfterFlushes <= 0) {
            throw new IllegalArgumentException("explicitFlushAfterFlushes: " + explicitFlushAfterFlushes + " (expected: > 0)");
        }
        this.explicitFlushAfterFlushes = explicitFlushAfterFlushes;
        this.consolidateWhenNoReadInProgress = consolidateWhenNoReadInProgress;
        this.flushTask = (consolidateWhenNoReadInProgress ? new Runnable() {
            @Override
            public void run() {
                if (FlushConsolidationHandler.this.flushPendingCount > 0 && !FlushConsolidationHandler.this.readInProgress) {
                    FlushConsolidationHandler.this.flushPendingCount = 0;
                    FlushConsolidationHandler.this.ctx.flush();
                    FlushConsolidationHandler.this.nextScheduledFlush = null;
                }
            }
        } : null);
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }
    
    @Override
    public void flush(final ChannelHandlerContext ctx) throws Exception {
        if (this.readInProgress) {
            if (++this.flushPendingCount == this.explicitFlushAfterFlushes) {
                this.flushNow(ctx);
            }
        }
        else if (this.consolidateWhenNoReadInProgress) {
            if (++this.flushPendingCount == this.explicitFlushAfterFlushes) {
                this.flushNow(ctx);
            }
            else {
                this.scheduleFlush(ctx);
            }
        }
        else {
            this.flushNow(ctx);
        }
    }
    
    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
        this.resetReadAndFlushIfNeeded(ctx);
        ctx.fireChannelReadComplete();
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        this.readInProgress = true;
        ctx.fireChannelRead(msg);
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        this.resetReadAndFlushIfNeeded(ctx);
        ctx.fireExceptionCaught(cause);
    }
    
    @Override
    public void disconnect(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        this.resetReadAndFlushIfNeeded(ctx);
        ctx.disconnect(promise);
    }
    
    @Override
    public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        this.resetReadAndFlushIfNeeded(ctx);
        ctx.close(promise);
    }
    
    @Override
    public void channelWritabilityChanged(final ChannelHandlerContext ctx) throws Exception {
        if (!ctx.channel().isWritable()) {
            this.flushIfNeeded(ctx);
        }
        ctx.fireChannelWritabilityChanged();
    }
    
    @Override
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        this.flushIfNeeded(ctx);
    }
    
    private void resetReadAndFlushIfNeeded(final ChannelHandlerContext ctx) {
        this.readInProgress = false;
        this.flushIfNeeded(ctx);
    }
    
    private void flushIfNeeded(final ChannelHandlerContext ctx) {
        if (this.flushPendingCount > 0) {
            this.flushNow(ctx);
        }
    }
    
    private void flushNow(final ChannelHandlerContext ctx) {
        this.cancelScheduledFlush();
        this.flushPendingCount = 0;
        ctx.flush();
    }
    
    private void scheduleFlush(final ChannelHandlerContext ctx) {
        if (this.nextScheduledFlush == null) {
            this.nextScheduledFlush = ctx.channel().eventLoop().submit(this.flushTask);
        }
    }
    
    private void cancelScheduledFlush() {
        if (this.nextScheduledFlush != null) {
            this.nextScheduledFlush.cancel(false);
            this.nextScheduledFlush = null;
        }
    }
}
