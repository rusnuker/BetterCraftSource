// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.traffic;

import java.util.concurrent.TimeUnit;
import io.netty.channel.ChannelPromise;
import java.util.Iterator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import java.util.ArrayDeque;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ConcurrentMap;
import io.netty.channel.ChannelHandler;

@ChannelHandler.Sharable
public class GlobalTrafficShapingHandler extends AbstractTrafficShapingHandler
{
    private final ConcurrentMap<Integer, PerChannel> channelQueues;
    private final AtomicLong queuesSize;
    long maxGlobalWriteSize;
    
    void createGlobalTrafficCounter(final ScheduledExecutorService executor) {
        if (executor == null) {
            throw new NullPointerException("executor");
        }
        final TrafficCounter tc = new TrafficCounter(this, executor, "GlobalTC", this.checkInterval);
        this.setTrafficCounter(tc);
        tc.start();
    }
    
    @Override
    protected int userDefinedWritabilityIndex() {
        return 2;
    }
    
    public GlobalTrafficShapingHandler(final ScheduledExecutorService executor, final long writeLimit, final long readLimit, final long checkInterval, final long maxTime) {
        super(writeLimit, readLimit, checkInterval, maxTime);
        this.channelQueues = PlatformDependent.newConcurrentHashMap();
        this.queuesSize = new AtomicLong();
        this.maxGlobalWriteSize = 419430400L;
        this.createGlobalTrafficCounter(executor);
    }
    
    public GlobalTrafficShapingHandler(final ScheduledExecutorService executor, final long writeLimit, final long readLimit, final long checkInterval) {
        super(writeLimit, readLimit, checkInterval);
        this.channelQueues = PlatformDependent.newConcurrentHashMap();
        this.queuesSize = new AtomicLong();
        this.maxGlobalWriteSize = 419430400L;
        this.createGlobalTrafficCounter(executor);
    }
    
    public GlobalTrafficShapingHandler(final ScheduledExecutorService executor, final long writeLimit, final long readLimit) {
        super(writeLimit, readLimit);
        this.channelQueues = PlatformDependent.newConcurrentHashMap();
        this.queuesSize = new AtomicLong();
        this.maxGlobalWriteSize = 419430400L;
        this.createGlobalTrafficCounter(executor);
    }
    
    public GlobalTrafficShapingHandler(final ScheduledExecutorService executor, final long checkInterval) {
        super(checkInterval);
        this.channelQueues = PlatformDependent.newConcurrentHashMap();
        this.queuesSize = new AtomicLong();
        this.maxGlobalWriteSize = 419430400L;
        this.createGlobalTrafficCounter(executor);
    }
    
    public GlobalTrafficShapingHandler(final EventExecutor executor) {
        this.channelQueues = PlatformDependent.newConcurrentHashMap();
        this.queuesSize = new AtomicLong();
        this.maxGlobalWriteSize = 419430400L;
        this.createGlobalTrafficCounter(executor);
    }
    
    public long getMaxGlobalWriteSize() {
        return this.maxGlobalWriteSize;
    }
    
    public void setMaxGlobalWriteSize(final long maxGlobalWriteSize) {
        this.maxGlobalWriteSize = maxGlobalWriteSize;
    }
    
    public long queuesSize() {
        return this.queuesSize.get();
    }
    
    public final void release() {
        this.trafficCounter.stop();
    }
    
    private PerChannel getOrSetPerChannel(final ChannelHandlerContext ctx) {
        final Channel channel = ctx.channel();
        final Integer key = channel.hashCode();
        PerChannel perChannel = this.channelQueues.get(key);
        if (perChannel == null) {
            perChannel = new PerChannel();
            perChannel.messagesQueue = new ArrayDeque<ToSend>();
            perChannel.queueSize = 0L;
            perChannel.lastReadTimestamp = TrafficCounter.milliSecondFromNano();
            perChannel.lastWriteTimestamp = perChannel.lastReadTimestamp;
            this.channelQueues.put(key, perChannel);
        }
        return perChannel;
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        this.getOrSetPerChannel(ctx);
        super.handlerAdded(ctx);
    }
    
    @Override
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        final Channel channel = ctx.channel();
        final Integer key = channel.hashCode();
        final PerChannel perChannel = this.channelQueues.remove(key);
        if (perChannel != null) {
            synchronized (perChannel) {
                if (channel.isActive()) {
                    for (final ToSend toSend : perChannel.messagesQueue) {
                        final long size = this.calculateSize(toSend.toSend);
                        this.trafficCounter.bytesRealWriteFlowControl(size);
                        final PerChannel perChannel2 = perChannel;
                        perChannel2.queueSize -= size;
                        this.queuesSize.addAndGet(-size);
                        ctx.write(toSend.toSend, toSend.promise);
                    }
                }
                else {
                    this.queuesSize.addAndGet(-perChannel.queueSize);
                    for (final ToSend toSend : perChannel.messagesQueue) {
                        if (toSend.toSend instanceof ByteBuf) {
                            ((ByteBuf)toSend.toSend).release();
                        }
                    }
                }
                perChannel.messagesQueue.clear();
            }
        }
        this.releaseWriteSuspended(ctx);
        this.releaseReadSuspended(ctx);
        super.handlerRemoved(ctx);
    }
    
    @Override
    long checkWaitReadTime(final ChannelHandlerContext ctx, long wait, final long now) {
        final Integer key = ctx.channel().hashCode();
        final PerChannel perChannel = this.channelQueues.get(key);
        if (perChannel != null && wait > this.maxTime && now + wait - perChannel.lastReadTimestamp > this.maxTime) {
            wait = this.maxTime;
        }
        return wait;
    }
    
    @Override
    void informReadOperation(final ChannelHandlerContext ctx, final long now) {
        final Integer key = ctx.channel().hashCode();
        final PerChannel perChannel = this.channelQueues.get(key);
        if (perChannel != null) {
            perChannel.lastReadTimestamp = now;
        }
    }
    
    @Override
    void submitWrite(final ChannelHandlerContext ctx, final Object msg, final long size, final long writedelay, final long now, final ChannelPromise promise) {
        final Channel channel = ctx.channel();
        final Integer key = channel.hashCode();
        PerChannel perChannel = this.channelQueues.get(key);
        if (perChannel == null) {
            perChannel = this.getOrSetPerChannel(ctx);
        }
        long delay = writedelay;
        boolean globalSizeExceeded = false;
        final ToSend newToSend;
        synchronized (perChannel) {
            if (writedelay == 0L && perChannel.messagesQueue.isEmpty()) {
                this.trafficCounter.bytesRealWriteFlowControl(size);
                ctx.write(msg, promise);
                perChannel.lastWriteTimestamp = now;
                return;
            }
            if (delay > this.maxTime && now + delay - perChannel.lastWriteTimestamp > this.maxTime) {
                delay = this.maxTime;
            }
            newToSend = new ToSend(delay + now, msg, size, promise);
            perChannel.messagesQueue.addLast(newToSend);
            final PerChannel perChannel2 = perChannel;
            perChannel2.queueSize += size;
            this.queuesSize.addAndGet(size);
            this.checkWriteSuspend(ctx, delay, perChannel.queueSize);
            if (this.queuesSize.get() > this.maxGlobalWriteSize) {
                globalSizeExceeded = true;
            }
        }
        if (globalSizeExceeded) {
            this.setUserDefinedWritability(ctx, false);
        }
        final long futureNow = newToSend.relativeTimeAction;
        final PerChannel forSchedule = perChannel;
        ctx.executor().schedule((Runnable)new Runnable() {
            @Override
            public void run() {
                GlobalTrafficShapingHandler.this.sendAllValid(ctx, forSchedule, futureNow);
            }
        }, delay, TimeUnit.MILLISECONDS);
    }
    
    private void sendAllValid(final ChannelHandlerContext ctx, final PerChannel perChannel, final long now) {
        synchronized (perChannel) {
            for (ToSend newToSend = perChannel.messagesQueue.pollFirst(); newToSend != null; newToSend = perChannel.messagesQueue.pollFirst()) {
                if (newToSend.relativeTimeAction > now) {
                    perChannel.messagesQueue.addFirst(newToSend);
                    break;
                }
                final long size = newToSend.size;
                this.trafficCounter.bytesRealWriteFlowControl(size);
                perChannel.queueSize -= size;
                this.queuesSize.addAndGet(-size);
                ctx.write(newToSend.toSend, newToSend.promise);
                perChannel.lastWriteTimestamp = now;
            }
            if (perChannel.messagesQueue.isEmpty()) {
                this.releaseWriteSuspended(ctx);
            }
        }
        ctx.flush();
    }
    
    private static final class PerChannel
    {
        ArrayDeque<ToSend> messagesQueue;
        long queueSize;
        long lastWriteTimestamp;
        long lastReadTimestamp;
    }
    
    private static final class ToSend
    {
        final long relativeTimeAction;
        final Object toSend;
        final long size;
        final ChannelPromise promise;
        
        private ToSend(final long delay, final Object toSend, final long size, final ChannelPromise promise) {
            this.relativeTimeAction = delay;
            this.toSend = toSend;
            this.size = size;
            this.promise = promise;
        }
    }
}
