// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.traffic;

import java.util.concurrent.TimeUnit;
import io.netty.channel.ChannelPromise;
import java.util.Iterator;
import io.netty.buffer.ByteBuf;
import java.util.concurrent.ScheduledExecutorService;
import io.netty.channel.ChannelHandlerContext;
import java.util.ArrayDeque;

public class ChannelTrafficShapingHandler extends AbstractTrafficShapingHandler
{
    private final ArrayDeque<ToSend> messagesQueue;
    private long queueSize;
    
    public ChannelTrafficShapingHandler(final long writeLimit, final long readLimit, final long checkInterval, final long maxTime) {
        super(writeLimit, readLimit, checkInterval, maxTime);
        this.messagesQueue = new ArrayDeque<ToSend>();
    }
    
    public ChannelTrafficShapingHandler(final long writeLimit, final long readLimit, final long checkInterval) {
        super(writeLimit, readLimit, checkInterval);
        this.messagesQueue = new ArrayDeque<ToSend>();
    }
    
    public ChannelTrafficShapingHandler(final long writeLimit, final long readLimit) {
        super(writeLimit, readLimit);
        this.messagesQueue = new ArrayDeque<ToSend>();
    }
    
    public ChannelTrafficShapingHandler(final long checkInterval) {
        super(checkInterval);
        this.messagesQueue = new ArrayDeque<ToSend>();
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        final TrafficCounter trafficCounter = new TrafficCounter(this, ctx.executor(), "ChannelTC" + ctx.channel().hashCode(), this.checkInterval);
        this.setTrafficCounter(trafficCounter);
        trafficCounter.start();
        super.handlerAdded(ctx);
    }
    
    @Override
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        this.trafficCounter.stop();
        synchronized (this) {
            if (ctx.channel().isActive()) {
                for (final ToSend toSend : this.messagesQueue) {
                    final long size = this.calculateSize(toSend.toSend);
                    this.trafficCounter.bytesRealWriteFlowControl(size);
                    this.queueSize -= size;
                    ctx.write(toSend.toSend, toSend.promise);
                }
            }
            else {
                for (final ToSend toSend : this.messagesQueue) {
                    if (toSend.toSend instanceof ByteBuf) {
                        ((ByteBuf)toSend.toSend).release();
                    }
                }
            }
            this.messagesQueue.clear();
        }
        this.releaseWriteSuspended(ctx);
        this.releaseReadSuspended(ctx);
        super.handlerRemoved(ctx);
    }
    
    @Override
    void submitWrite(final ChannelHandlerContext ctx, final Object msg, final long size, final long delay, final long now, final ChannelPromise promise) {
        final ToSend newToSend;
        synchronized (this) {
            if (delay == 0L && this.messagesQueue.isEmpty()) {
                this.trafficCounter.bytesRealWriteFlowControl(size);
                ctx.write(msg, promise);
                return;
            }
            newToSend = new ToSend(delay + now, msg, promise);
            this.messagesQueue.addLast(newToSend);
            this.checkWriteSuspend(ctx, delay, this.queueSize += size);
        }
        final long futureNow = newToSend.relativeTimeAction;
        ctx.executor().schedule((Runnable)new Runnable() {
            @Override
            public void run() {
                ChannelTrafficShapingHandler.this.sendAllValid(ctx, futureNow);
            }
        }, delay, TimeUnit.MILLISECONDS);
    }
    
    private void sendAllValid(final ChannelHandlerContext ctx, final long now) {
        synchronized (this) {
            for (ToSend newToSend = this.messagesQueue.pollFirst(); newToSend != null; newToSend = this.messagesQueue.pollFirst()) {
                if (newToSend.relativeTimeAction > now) {
                    this.messagesQueue.addFirst(newToSend);
                    break;
                }
                final long size = this.calculateSize(newToSend.toSend);
                this.trafficCounter.bytesRealWriteFlowControl(size);
                this.queueSize -= size;
                ctx.write(newToSend.toSend, newToSend.promise);
            }
            if (this.messagesQueue.isEmpty()) {
                this.releaseWriteSuspended(ctx);
            }
        }
        ctx.flush();
    }
    
    public long queueSize() {
        return this.queueSize;
    }
    
    private static final class ToSend
    {
        final long relativeTimeAction;
        final Object toSend;
        final ChannelPromise promise;
        
        private ToSend(final long delay, final Object toSend, final ChannelPromise promise) {
            this.relativeTimeAction = delay;
            this.toSend = toSend;
            this.promise = promise;
        }
    }
}
