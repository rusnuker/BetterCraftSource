// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.timeout;

import io.netty.channel.ChannelFuture;
import java.util.concurrent.ScheduledFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandlerContext;
import java.util.concurrent.TimeUnit;
import io.netty.channel.ChannelOutboundHandlerAdapter;

public class WriteTimeoutHandler extends ChannelOutboundHandlerAdapter
{
    private static final long MIN_TIMEOUT_NANOS;
    private final long timeoutNanos;
    private WriteTimeoutTask lastTask;
    private boolean closed;
    
    public WriteTimeoutHandler(final int timeoutSeconds) {
        this(timeoutSeconds, TimeUnit.SECONDS);
    }
    
    public WriteTimeoutHandler(final long timeout, final TimeUnit unit) {
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        if (timeout <= 0L) {
            this.timeoutNanos = 0L;
        }
        else {
            this.timeoutNanos = Math.max(unit.toNanos(timeout), WriteTimeoutHandler.MIN_TIMEOUT_NANOS);
        }
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, ChannelPromise promise) throws Exception {
        if (this.timeoutNanos > 0L) {
            promise = promise.unvoid();
            this.scheduleTimeout(ctx, promise);
        }
        ctx.write(msg, promise);
    }
    
    @Override
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        WriteTimeoutTask task = this.lastTask;
        this.lastTask = null;
        while (task != null) {
            task.scheduledFuture.cancel(false);
            final WriteTimeoutTask prev = task.prev;
            task.prev = null;
            task.next = null;
            task = prev;
        }
    }
    
    private void scheduleTimeout(final ChannelHandlerContext ctx, final ChannelPromise promise) {
        final WriteTimeoutTask task = new WriteTimeoutTask(ctx, promise);
        task.scheduledFuture = ctx.executor().schedule((Runnable)task, this.timeoutNanos, TimeUnit.NANOSECONDS);
        if (!task.scheduledFuture.isDone()) {
            this.addWriteTimeoutTask(task);
            promise.addListener((GenericFutureListener<? extends Future<? super Void>>)task);
        }
    }
    
    private void addWriteTimeoutTask(final WriteTimeoutTask task) {
        if (this.lastTask == null) {
            this.lastTask = task;
        }
        else {
            this.lastTask.next = task;
            task.prev = this.lastTask;
            this.lastTask = task;
        }
    }
    
    private void removeWriteTimeoutTask(final WriteTimeoutTask task) {
        if (task == this.lastTask) {
            assert task.next == null;
            this.lastTask = this.lastTask.prev;
            if (this.lastTask != null) {
                this.lastTask.next = null;
            }
        }
        else {
            if (task.prev == null && task.next == null) {
                return;
            }
            if (task.prev == null) {
                task.next.prev = null;
            }
            else {
                task.prev.next = task.next;
                task.next.prev = task.prev;
            }
        }
        task.prev = null;
        task.next = null;
    }
    
    protected void writeTimedOut(final ChannelHandlerContext ctx) throws Exception {
        if (!this.closed) {
            ctx.fireExceptionCaught((Throwable)WriteTimeoutException.INSTANCE);
            ctx.close();
            this.closed = true;
        }
    }
    
    static {
        MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1L);
    }
    
    private final class WriteTimeoutTask implements Runnable, ChannelFutureListener
    {
        private final ChannelHandlerContext ctx;
        private final ChannelPromise promise;
        WriteTimeoutTask prev;
        WriteTimeoutTask next;
        ScheduledFuture<?> scheduledFuture;
        
        WriteTimeoutTask(final ChannelHandlerContext ctx, final ChannelPromise promise) {
            this.ctx = ctx;
            this.promise = promise;
        }
        
        @Override
        public void run() {
            if (!this.promise.isDone()) {
                try {
                    WriteTimeoutHandler.this.writeTimedOut(this.ctx);
                }
                catch (final Throwable t) {
                    this.ctx.fireExceptionCaught(t);
                }
            }
            WriteTimeoutHandler.this.removeWriteTimeoutTask(this);
        }
        
        @Override
        public void operationComplete(final ChannelFuture future) throws Exception {
            this.scheduledFuture.cancel(false);
            WriteTimeoutHandler.this.removeWriteTimeoutTask(this);
        }
    }
}
