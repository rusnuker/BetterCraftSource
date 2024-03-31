// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.traffic;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.ChannelPromise;
import java.util.AbstractCollection;
import java.util.Collection;
import io.netty.util.Attribute;
import io.netty.channel.ChannelConfig;
import java.util.concurrent.TimeUnit;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import java.util.ArrayDeque;
import io.netty.channel.ChannelHandlerContext;
import java.util.Iterator;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ConcurrentMap;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.ChannelHandler;

@ChannelHandler.Sharable
public class GlobalChannelTrafficShapingHandler extends AbstractTrafficShapingHandler
{
    private static final InternalLogger logger;
    final ConcurrentMap<Integer, PerChannel> channelQueues;
    private final AtomicLong queuesSize;
    private final AtomicLong cumulativeWrittenBytes;
    private final AtomicLong cumulativeReadBytes;
    volatile long maxGlobalWriteSize;
    private volatile long writeChannelLimit;
    private volatile long readChannelLimit;
    private static final float DEFAULT_DEVIATION = 0.1f;
    private static final float MAX_DEVIATION = 0.4f;
    private static final float DEFAULT_SLOWDOWN = 0.4f;
    private static final float DEFAULT_ACCELERATION = -0.1f;
    private volatile float maxDeviation;
    private volatile float accelerationFactor;
    private volatile float slowDownFactor;
    private volatile boolean readDeviationActive;
    private volatile boolean writeDeviationActive;
    
    void createGlobalTrafficCounter(final ScheduledExecutorService executor) {
        this.setMaxDeviation(0.1f, 0.4f, -0.1f);
        if (executor == null) {
            throw new IllegalArgumentException("Executor must not be null");
        }
        final TrafficCounter tc = new GlobalChannelTrafficCounter(this, executor, "GlobalChannelTC", this.checkInterval);
        this.setTrafficCounter(tc);
        tc.start();
    }
    
    @Override
    protected int userDefinedWritabilityIndex() {
        return 3;
    }
    
    public GlobalChannelTrafficShapingHandler(final ScheduledExecutorService executor, final long writeGlobalLimit, final long readGlobalLimit, final long writeChannelLimit, final long readChannelLimit, final long checkInterval, final long maxTime) {
        super(writeGlobalLimit, readGlobalLimit, checkInterval, maxTime);
        this.channelQueues = PlatformDependent.newConcurrentHashMap();
        this.queuesSize = new AtomicLong();
        this.cumulativeWrittenBytes = new AtomicLong();
        this.cumulativeReadBytes = new AtomicLong();
        this.maxGlobalWriteSize = 419430400L;
        this.createGlobalTrafficCounter(executor);
        this.writeChannelLimit = writeChannelLimit;
        this.readChannelLimit = readChannelLimit;
    }
    
    public GlobalChannelTrafficShapingHandler(final ScheduledExecutorService executor, final long writeGlobalLimit, final long readGlobalLimit, final long writeChannelLimit, final long readChannelLimit, final long checkInterval) {
        super(writeGlobalLimit, readGlobalLimit, checkInterval);
        this.channelQueues = PlatformDependent.newConcurrentHashMap();
        this.queuesSize = new AtomicLong();
        this.cumulativeWrittenBytes = new AtomicLong();
        this.cumulativeReadBytes = new AtomicLong();
        this.maxGlobalWriteSize = 419430400L;
        this.writeChannelLimit = writeChannelLimit;
        this.readChannelLimit = readChannelLimit;
        this.createGlobalTrafficCounter(executor);
    }
    
    public GlobalChannelTrafficShapingHandler(final ScheduledExecutorService executor, final long writeGlobalLimit, final long readGlobalLimit, final long writeChannelLimit, final long readChannelLimit) {
        super(writeGlobalLimit, readGlobalLimit);
        this.channelQueues = PlatformDependent.newConcurrentHashMap();
        this.queuesSize = new AtomicLong();
        this.cumulativeWrittenBytes = new AtomicLong();
        this.cumulativeReadBytes = new AtomicLong();
        this.maxGlobalWriteSize = 419430400L;
        this.writeChannelLimit = writeChannelLimit;
        this.readChannelLimit = readChannelLimit;
        this.createGlobalTrafficCounter(executor);
    }
    
    public GlobalChannelTrafficShapingHandler(final ScheduledExecutorService executor, final long checkInterval) {
        super(checkInterval);
        this.channelQueues = PlatformDependent.newConcurrentHashMap();
        this.queuesSize = new AtomicLong();
        this.cumulativeWrittenBytes = new AtomicLong();
        this.cumulativeReadBytes = new AtomicLong();
        this.maxGlobalWriteSize = 419430400L;
        this.createGlobalTrafficCounter(executor);
    }
    
    public GlobalChannelTrafficShapingHandler(final ScheduledExecutorService executor) {
        this.channelQueues = PlatformDependent.newConcurrentHashMap();
        this.queuesSize = new AtomicLong();
        this.cumulativeWrittenBytes = new AtomicLong();
        this.cumulativeReadBytes = new AtomicLong();
        this.maxGlobalWriteSize = 419430400L;
        this.createGlobalTrafficCounter(executor);
    }
    
    public float maxDeviation() {
        return this.maxDeviation;
    }
    
    public float accelerationFactor() {
        return this.accelerationFactor;
    }
    
    public float slowDownFactor() {
        return this.slowDownFactor;
    }
    
    public void setMaxDeviation(final float maxDeviation, final float slowDownFactor, final float accelerationFactor) {
        if (maxDeviation > 0.4f) {
            throw new IllegalArgumentException("maxDeviation must be <= 0.4");
        }
        if (slowDownFactor < 0.0f) {
            throw new IllegalArgumentException("slowDownFactor must be >= 0");
        }
        if (accelerationFactor > 0.0f) {
            throw new IllegalArgumentException("accelerationFactor must be <= 0");
        }
        this.maxDeviation = maxDeviation;
        this.accelerationFactor = 1.0f + accelerationFactor;
        this.slowDownFactor = 1.0f + slowDownFactor;
    }
    
    private void computeDeviationCumulativeBytes() {
        long maxWrittenBytes = 0L;
        long maxReadBytes = 0L;
        long minWrittenBytes = Long.MAX_VALUE;
        long minReadBytes = Long.MAX_VALUE;
        for (final PerChannel perChannel : this.channelQueues.values()) {
            long value = perChannel.channelTrafficCounter.cumulativeWrittenBytes();
            if (maxWrittenBytes < value) {
                maxWrittenBytes = value;
            }
            if (minWrittenBytes > value) {
                minWrittenBytes = value;
            }
            value = perChannel.channelTrafficCounter.cumulativeReadBytes();
            if (maxReadBytes < value) {
                maxReadBytes = value;
            }
            if (minReadBytes > value) {
                minReadBytes = value;
            }
        }
        final boolean multiple = this.channelQueues.size() > 1;
        this.readDeviationActive = (multiple && minReadBytes < maxReadBytes / 2L);
        this.writeDeviationActive = (multiple && minWrittenBytes < maxWrittenBytes / 2L);
        this.cumulativeWrittenBytes.set(maxWrittenBytes);
        this.cumulativeReadBytes.set(maxReadBytes);
    }
    
    @Override
    protected void doAccounting(final TrafficCounter counter) {
        this.computeDeviationCumulativeBytes();
        super.doAccounting(counter);
    }
    
    private long computeBalancedWait(final float maxLocal, final float maxGlobal, long wait) {
        if (maxGlobal == 0.0f) {
            return wait;
        }
        float ratio = maxLocal / maxGlobal;
        if (ratio > this.maxDeviation) {
            if (ratio < 1.0f - this.maxDeviation) {
                return wait;
            }
            ratio = this.slowDownFactor;
            if (wait < 10L) {
                wait = 10L;
            }
        }
        else {
            ratio = this.accelerationFactor;
        }
        return (long)(wait * ratio);
    }
    
    public long getMaxGlobalWriteSize() {
        return this.maxGlobalWriteSize;
    }
    
    public void setMaxGlobalWriteSize(final long maxGlobalWriteSize) {
        if (maxGlobalWriteSize <= 0L) {
            throw new IllegalArgumentException("maxGlobalWriteSize must be positive");
        }
        this.maxGlobalWriteSize = maxGlobalWriteSize;
    }
    
    public long queuesSize() {
        return this.queuesSize.get();
    }
    
    public void configureChannel(final long newWriteLimit, final long newReadLimit) {
        this.writeChannelLimit = newWriteLimit;
        this.readChannelLimit = newReadLimit;
        final long now = TrafficCounter.milliSecondFromNano();
        for (final PerChannel perChannel : this.channelQueues.values()) {
            perChannel.channelTrafficCounter.resetAccounting(now);
        }
    }
    
    public long getWriteChannelLimit() {
        return this.writeChannelLimit;
    }
    
    public void setWriteChannelLimit(final long writeLimit) {
        this.writeChannelLimit = writeLimit;
        final long now = TrafficCounter.milliSecondFromNano();
        for (final PerChannel perChannel : this.channelQueues.values()) {
            perChannel.channelTrafficCounter.resetAccounting(now);
        }
    }
    
    public long getReadChannelLimit() {
        return this.readChannelLimit;
    }
    
    public void setReadChannelLimit(final long readLimit) {
        this.readChannelLimit = readLimit;
        final long now = TrafficCounter.milliSecondFromNano();
        for (final PerChannel perChannel : this.channelQueues.values()) {
            perChannel.channelTrafficCounter.resetAccounting(now);
        }
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
            perChannel.channelTrafficCounter = new TrafficCounter(this, null, "ChannelTC" + ctx.channel().hashCode(), this.checkInterval);
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
        this.trafficCounter.resetCumulativeTime();
        super.handlerAdded(ctx);
    }
    
    @Override
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        this.trafficCounter.resetCumulativeTime();
        final Channel channel = ctx.channel();
        final Integer key = channel.hashCode();
        final PerChannel perChannel = this.channelQueues.remove(key);
        if (perChannel != null) {
            synchronized (perChannel) {
                if (channel.isActive()) {
                    for (final ToSend toSend : perChannel.messagesQueue) {
                        final long size = this.calculateSize(toSend.toSend);
                        this.trafficCounter.bytesRealWriteFlowControl(size);
                        perChannel.channelTrafficCounter.bytesRealWriteFlowControl(size);
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
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        final long size = this.calculateSize(msg);
        final long now = TrafficCounter.milliSecondFromNano();
        if (size > 0L) {
            final long waitGlobal = this.trafficCounter.readTimeToWait(size, this.getReadLimit(), this.maxTime, now);
            final Integer key = ctx.channel().hashCode();
            final PerChannel perChannel = this.channelQueues.get(key);
            long wait = 0L;
            if (perChannel != null) {
                wait = perChannel.channelTrafficCounter.readTimeToWait(size, this.readChannelLimit, this.maxTime, now);
                if (this.readDeviationActive) {
                    long maxLocalRead = perChannel.channelTrafficCounter.cumulativeReadBytes();
                    long maxGlobalRead = this.cumulativeReadBytes.get();
                    if (maxLocalRead <= 0L) {
                        maxLocalRead = 0L;
                    }
                    if (maxGlobalRead < maxLocalRead) {
                        maxGlobalRead = maxLocalRead;
                    }
                    wait = this.computeBalancedWait((float)maxLocalRead, (float)maxGlobalRead, wait);
                }
            }
            if (wait < waitGlobal) {
                wait = waitGlobal;
            }
            wait = this.checkWaitReadTime(ctx, wait, now);
            if (wait >= 10L) {
                final ChannelConfig config = ctx.channel().config();
                if (GlobalChannelTrafficShapingHandler.logger.isDebugEnabled()) {
                    GlobalChannelTrafficShapingHandler.logger.debug("Read Suspend: " + wait + ':' + config.isAutoRead() + ':' + AbstractTrafficShapingHandler.isHandlerActive(ctx));
                }
                if (config.isAutoRead() && AbstractTrafficShapingHandler.isHandlerActive(ctx)) {
                    config.setAutoRead(false);
                    ctx.attr(GlobalChannelTrafficShapingHandler.READ_SUSPENDED).set(true);
                    final Attribute<Runnable> attr = ctx.attr(GlobalChannelTrafficShapingHandler.REOPEN_TASK);
                    Runnable reopenTask = attr.get();
                    if (reopenTask == null) {
                        reopenTask = new ReopenReadTimerTask(ctx);
                        attr.set(reopenTask);
                    }
                    ctx.executor().schedule(reopenTask, wait, TimeUnit.MILLISECONDS);
                    if (GlobalChannelTrafficShapingHandler.logger.isDebugEnabled()) {
                        GlobalChannelTrafficShapingHandler.logger.debug("Suspend final status => " + config.isAutoRead() + ':' + AbstractTrafficShapingHandler.isHandlerActive(ctx) + " will reopened at: " + wait);
                    }
                }
            }
        }
        this.informReadOperation(ctx, now);
        ctx.fireChannelRead(msg);
    }
    
    protected long checkWaitReadTime(final ChannelHandlerContext ctx, long wait, final long now) {
        final Integer key = ctx.channel().hashCode();
        final PerChannel perChannel = this.channelQueues.get(key);
        if (perChannel != null && wait > this.maxTime && now + wait - perChannel.lastReadTimestamp > this.maxTime) {
            wait = this.maxTime;
        }
        return wait;
    }
    
    protected void informReadOperation(final ChannelHandlerContext ctx, final long now) {
        final Integer key = ctx.channel().hashCode();
        final PerChannel perChannel = this.channelQueues.get(key);
        if (perChannel != null) {
            perChannel.lastReadTimestamp = now;
        }
    }
    
    protected long maximumCumulativeWrittenBytes() {
        return this.cumulativeWrittenBytes.get();
    }
    
    protected long maximumCumulativeReadBytes() {
        return this.cumulativeReadBytes.get();
    }
    
    public Collection<TrafficCounter> channelTrafficCounters() {
        return new AbstractCollection<TrafficCounter>() {
            @Override
            public Iterator<TrafficCounter> iterator() {
                return new Iterator<TrafficCounter>() {
                    final Iterator<PerChannel> iter = GlobalChannelTrafficShapingHandler.this.channelQueues.values().iterator();
                    
                    @Override
                    public boolean hasNext() {
                        return this.iter.hasNext();
                    }
                    
                    @Override
                    public TrafficCounter next() {
                        return this.iter.next().channelTrafficCounter;
                    }
                    
                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
            
            @Override
            public int size() {
                return GlobalChannelTrafficShapingHandler.this.channelQueues.size();
            }
        };
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        final long size = this.calculateSize(msg);
        final long now = TrafficCounter.milliSecondFromNano();
        if (size > 0L) {
            final long waitGlobal = this.trafficCounter.writeTimeToWait(size, this.getWriteLimit(), this.maxTime, now);
            final Integer key = ctx.channel().hashCode();
            final PerChannel perChannel = this.channelQueues.get(key);
            long wait = 0L;
            if (perChannel != null) {
                wait = perChannel.channelTrafficCounter.writeTimeToWait(size, this.writeChannelLimit, this.maxTime, now);
                if (this.writeDeviationActive) {
                    long maxLocalWrite = perChannel.channelTrafficCounter.cumulativeWrittenBytes();
                    long maxGlobalWrite = this.cumulativeWrittenBytes.get();
                    if (maxLocalWrite <= 0L) {
                        maxLocalWrite = 0L;
                    }
                    if (maxGlobalWrite < maxLocalWrite) {
                        maxGlobalWrite = maxLocalWrite;
                    }
                    wait = this.computeBalancedWait((float)maxLocalWrite, (float)maxGlobalWrite, wait);
                }
            }
            if (wait < waitGlobal) {
                wait = waitGlobal;
            }
            if (wait >= 10L) {
                if (GlobalChannelTrafficShapingHandler.logger.isDebugEnabled()) {
                    GlobalChannelTrafficShapingHandler.logger.debug("Write suspend: " + wait + ':' + ctx.channel().config().isAutoRead() + ':' + AbstractTrafficShapingHandler.isHandlerActive(ctx));
                }
                this.submitWrite(ctx, msg, size, wait, now, promise);
                return;
            }
        }
        this.submitWrite(ctx, msg, size, 0L, now, promise);
    }
    
    protected void submitWrite(final ChannelHandlerContext ctx, final Object msg, final long size, final long writedelay, final long now, final ChannelPromise promise) {
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
                perChannel.channelTrafficCounter.bytesRealWriteFlowControl(size);
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
                GlobalChannelTrafficShapingHandler.this.sendAllValid(ctx, forSchedule, futureNow);
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
                perChannel.channelTrafficCounter.bytesRealWriteFlowControl(size);
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
    
    @Override
    public String toString() {
        return new StringBuilder(340).append(super.toString()).append(" Write Channel Limit: ").append(this.writeChannelLimit).append(" Read Channel Limit: ").append(this.readChannelLimit).toString();
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(GlobalChannelTrafficShapingHandler.class);
    }
    
    static final class PerChannel
    {
        ArrayDeque<ToSend> messagesQueue;
        TrafficCounter channelTrafficCounter;
        long queueSize;
        long lastWriteTimestamp;
        long lastReadTimestamp;
    }
    
    private static final class ToSend
    {
        final long relativeTimeAction;
        final Object toSend;
        final ChannelPromise promise;
        final long size;
        
        private ToSend(final long delay, final Object toSend, final long size, final ChannelPromise promise) {
            this.relativeTimeAction = delay;
            this.toSend = toSend;
            this.size = size;
            this.promise = promise;
        }
    }
}
