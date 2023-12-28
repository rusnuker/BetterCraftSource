// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.timeout;

import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.Channel;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelDuplexHandler;

public class IdleStateHandler extends ChannelDuplexHandler
{
    private static final long MIN_TIMEOUT_NANOS;
    private final ChannelFutureListener writeListener;
    private final boolean observeOutput;
    private final long readerIdleTimeNanos;
    private final long writerIdleTimeNanos;
    private final long allIdleTimeNanos;
    private ScheduledFuture<?> readerIdleTimeout;
    private long lastReadTime;
    private boolean firstReaderIdleEvent;
    private ScheduledFuture<?> writerIdleTimeout;
    private long lastWriteTime;
    private boolean firstWriterIdleEvent;
    private ScheduledFuture<?> allIdleTimeout;
    private boolean firstAllIdleEvent;
    private byte state;
    private boolean reading;
    private long lastChangeCheckTimeStamp;
    private int lastMessageHashCode;
    private long lastPendingWriteBytes;
    
    public IdleStateHandler(final int readerIdleTimeSeconds, final int writerIdleTimeSeconds, final int allIdleTimeSeconds) {
        this(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds, TimeUnit.SECONDS);
    }
    
    public IdleStateHandler(final long readerIdleTime, final long writerIdleTime, final long allIdleTime, final TimeUnit unit) {
        this(false, readerIdleTime, writerIdleTime, allIdleTime, unit);
    }
    
    public IdleStateHandler(final boolean observeOutput, final long readerIdleTime, final long writerIdleTime, final long allIdleTime, final TimeUnit unit) {
        this.writeListener = new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future) throws Exception {
                IdleStateHandler.this.lastWriteTime = IdleStateHandler.this.ticksInNanos();
                IdleStateHandler.this.firstWriterIdleEvent = (IdleStateHandler.this.firstAllIdleEvent = true);
            }
        };
        this.firstReaderIdleEvent = true;
        this.firstWriterIdleEvent = true;
        this.firstAllIdleEvent = true;
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        this.observeOutput = observeOutput;
        if (readerIdleTime <= 0L) {
            this.readerIdleTimeNanos = 0L;
        }
        else {
            this.readerIdleTimeNanos = Math.max(unit.toNanos(readerIdleTime), IdleStateHandler.MIN_TIMEOUT_NANOS);
        }
        if (writerIdleTime <= 0L) {
            this.writerIdleTimeNanos = 0L;
        }
        else {
            this.writerIdleTimeNanos = Math.max(unit.toNanos(writerIdleTime), IdleStateHandler.MIN_TIMEOUT_NANOS);
        }
        if (allIdleTime <= 0L) {
            this.allIdleTimeNanos = 0L;
        }
        else {
            this.allIdleTimeNanos = Math.max(unit.toNanos(allIdleTime), IdleStateHandler.MIN_TIMEOUT_NANOS);
        }
    }
    
    public long getReaderIdleTimeInMillis() {
        return TimeUnit.NANOSECONDS.toMillis(this.readerIdleTimeNanos);
    }
    
    public long getWriterIdleTimeInMillis() {
        return TimeUnit.NANOSECONDS.toMillis(this.writerIdleTimeNanos);
    }
    
    public long getAllIdleTimeInMillis() {
        return TimeUnit.NANOSECONDS.toMillis(this.allIdleTimeNanos);
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().isActive() && ctx.channel().isRegistered()) {
            this.initialize(ctx);
        }
    }
    
    @Override
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        this.destroy();
    }
    
    @Override
    public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().isActive()) {
            this.initialize(ctx);
        }
        super.channelRegistered(ctx);
    }
    
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        this.initialize(ctx);
        super.channelActive(ctx);
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        this.destroy();
        super.channelInactive(ctx);
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        if (this.readerIdleTimeNanos > 0L || this.allIdleTimeNanos > 0L) {
            this.reading = true;
            final boolean b = true;
            this.firstAllIdleEvent = b;
            this.firstReaderIdleEvent = b;
        }
        ctx.fireChannelRead(msg);
    }
    
    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
        if ((this.readerIdleTimeNanos > 0L || this.allIdleTimeNanos > 0L) && this.reading) {
            this.lastReadTime = this.ticksInNanos();
            this.reading = false;
        }
        ctx.fireChannelReadComplete();
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        if (this.writerIdleTimeNanos > 0L || this.allIdleTimeNanos > 0L) {
            final ChannelPromise unvoid = promise.unvoid();
            unvoid.addListener((GenericFutureListener<? extends Future<? super Void>>)this.writeListener);
            ctx.write(msg, unvoid);
        }
        else {
            ctx.write(msg, promise);
        }
    }
    
    private void initialize(final ChannelHandlerContext ctx) {
        switch (this.state) {
            case 1:
            case 2: {
                return;
            }
            default: {
                this.state = 1;
                this.initOutputChanged(ctx);
                final long ticksInNanos = this.ticksInNanos();
                this.lastWriteTime = ticksInNanos;
                this.lastReadTime = ticksInNanos;
                if (this.readerIdleTimeNanos > 0L) {
                    this.readerIdleTimeout = this.schedule(ctx, new ReaderIdleTimeoutTask(ctx), this.readerIdleTimeNanos, TimeUnit.NANOSECONDS);
                }
                if (this.writerIdleTimeNanos > 0L) {
                    this.writerIdleTimeout = this.schedule(ctx, new WriterIdleTimeoutTask(ctx), this.writerIdleTimeNanos, TimeUnit.NANOSECONDS);
                }
                if (this.allIdleTimeNanos > 0L) {
                    this.allIdleTimeout = this.schedule(ctx, new AllIdleTimeoutTask(ctx), this.allIdleTimeNanos, TimeUnit.NANOSECONDS);
                }
            }
        }
    }
    
    long ticksInNanos() {
        return System.nanoTime();
    }
    
    ScheduledFuture<?> schedule(final ChannelHandlerContext ctx, final Runnable task, final long delay, final TimeUnit unit) {
        return ctx.executor().schedule(task, delay, unit);
    }
    
    private void destroy() {
        this.state = 2;
        if (this.readerIdleTimeout != null) {
            this.readerIdleTimeout.cancel(false);
            this.readerIdleTimeout = null;
        }
        if (this.writerIdleTimeout != null) {
            this.writerIdleTimeout.cancel(false);
            this.writerIdleTimeout = null;
        }
        if (this.allIdleTimeout != null) {
            this.allIdleTimeout.cancel(false);
            this.allIdleTimeout = null;
        }
    }
    
    protected void channelIdle(final ChannelHandlerContext ctx, final IdleStateEvent evt) throws Exception {
        ctx.fireUserEventTriggered((Object)evt);
    }
    
    protected IdleStateEvent newIdleStateEvent(final IdleState state, final boolean first) {
        switch (state) {
            case ALL_IDLE: {
                return first ? IdleStateEvent.FIRST_ALL_IDLE_STATE_EVENT : IdleStateEvent.ALL_IDLE_STATE_EVENT;
            }
            case READER_IDLE: {
                return first ? IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT : IdleStateEvent.READER_IDLE_STATE_EVENT;
            }
            case WRITER_IDLE: {
                return first ? IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT : IdleStateEvent.WRITER_IDLE_STATE_EVENT;
            }
            default: {
                throw new IllegalArgumentException("Unhandled: state=" + state + ", first=" + first);
            }
        }
    }
    
    private void initOutputChanged(final ChannelHandlerContext ctx) {
        if (this.observeOutput) {
            final Channel channel = ctx.channel();
            final Channel.Unsafe unsafe = channel.unsafe();
            final ChannelOutboundBuffer buf = unsafe.outboundBuffer();
            if (buf != null) {
                this.lastMessageHashCode = System.identityHashCode(buf.current());
                this.lastPendingWriteBytes = buf.totalPendingWriteBytes();
            }
        }
    }
    
    private boolean hasOutputChanged(final ChannelHandlerContext ctx, final boolean first) {
        if (this.observeOutput) {
            if (this.lastChangeCheckTimeStamp != this.lastWriteTime) {
                this.lastChangeCheckTimeStamp = this.lastWriteTime;
                if (!first) {
                    return true;
                }
            }
            final Channel channel = ctx.channel();
            final Channel.Unsafe unsafe = channel.unsafe();
            final ChannelOutboundBuffer buf = unsafe.outboundBuffer();
            if (buf != null) {
                final int messageHashCode = System.identityHashCode(buf.current());
                final long pendingWriteBytes = buf.totalPendingWriteBytes();
                if (messageHashCode != this.lastMessageHashCode || pendingWriteBytes != this.lastPendingWriteBytes) {
                    this.lastMessageHashCode = messageHashCode;
                    this.lastPendingWriteBytes = pendingWriteBytes;
                    if (!first) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    static {
        MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1L);
    }
    
    private abstract static class AbstractIdleTask implements Runnable
    {
        private final ChannelHandlerContext ctx;
        
        AbstractIdleTask(final ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }
        
        @Override
        public void run() {
            if (!this.ctx.channel().isOpen()) {
                return;
            }
            this.run(this.ctx);
        }
        
        protected abstract void run(final ChannelHandlerContext p0);
    }
    
    private final class ReaderIdleTimeoutTask extends AbstractIdleTask
    {
        ReaderIdleTimeoutTask(final ChannelHandlerContext ctx) {
            super(ctx);
        }
        
        @Override
        protected void run(final ChannelHandlerContext ctx) {
            long nextDelay = IdleStateHandler.this.readerIdleTimeNanos;
            if (!IdleStateHandler.this.reading) {
                nextDelay -= IdleStateHandler.this.ticksInNanos() - IdleStateHandler.this.lastReadTime;
            }
            if (nextDelay <= 0L) {
                IdleStateHandler.this.readerIdleTimeout = IdleStateHandler.this.schedule(ctx, this, IdleStateHandler.this.readerIdleTimeNanos, TimeUnit.NANOSECONDS);
                final boolean first = IdleStateHandler.this.firstReaderIdleEvent;
                IdleStateHandler.this.firstReaderIdleEvent = false;
                try {
                    final IdleStateEvent event = IdleStateHandler.this.newIdleStateEvent(IdleState.READER_IDLE, first);
                    IdleStateHandler.this.channelIdle(ctx, event);
                }
                catch (final Throwable t) {
                    ctx.fireExceptionCaught(t);
                }
            }
            else {
                IdleStateHandler.this.readerIdleTimeout = IdleStateHandler.this.schedule(ctx, this, nextDelay, TimeUnit.NANOSECONDS);
            }
        }
    }
    
    private final class WriterIdleTimeoutTask extends AbstractIdleTask
    {
        WriterIdleTimeoutTask(final ChannelHandlerContext ctx) {
            super(ctx);
        }
        
        @Override
        protected void run(final ChannelHandlerContext ctx) {
            final long lastWriteTime = IdleStateHandler.this.lastWriteTime;
            final long nextDelay = IdleStateHandler.this.writerIdleTimeNanos - (IdleStateHandler.this.ticksInNanos() - lastWriteTime);
            if (nextDelay <= 0L) {
                IdleStateHandler.this.writerIdleTimeout = IdleStateHandler.this.schedule(ctx, this, IdleStateHandler.this.writerIdleTimeNanos, TimeUnit.NANOSECONDS);
                final boolean first = IdleStateHandler.this.firstWriterIdleEvent;
                IdleStateHandler.this.firstWriterIdleEvent = false;
                try {
                    if (IdleStateHandler.this.hasOutputChanged(ctx, first)) {
                        return;
                    }
                    final IdleStateEvent event = IdleStateHandler.this.newIdleStateEvent(IdleState.WRITER_IDLE, first);
                    IdleStateHandler.this.channelIdle(ctx, event);
                }
                catch (final Throwable t) {
                    ctx.fireExceptionCaught(t);
                }
            }
            else {
                IdleStateHandler.this.writerIdleTimeout = IdleStateHandler.this.schedule(ctx, this, nextDelay, TimeUnit.NANOSECONDS);
            }
        }
    }
    
    private final class AllIdleTimeoutTask extends AbstractIdleTask
    {
        AllIdleTimeoutTask(final ChannelHandlerContext ctx) {
            super(ctx);
        }
        
        @Override
        protected void run(final ChannelHandlerContext ctx) {
            long nextDelay = IdleStateHandler.this.allIdleTimeNanos;
            if (!IdleStateHandler.this.reading) {
                nextDelay -= IdleStateHandler.this.ticksInNanos() - Math.max(IdleStateHandler.this.lastReadTime, IdleStateHandler.this.lastWriteTime);
            }
            if (nextDelay <= 0L) {
                IdleStateHandler.this.allIdleTimeout = IdleStateHandler.this.schedule(ctx, this, IdleStateHandler.this.allIdleTimeNanos, TimeUnit.NANOSECONDS);
                final boolean first = IdleStateHandler.this.firstAllIdleEvent;
                IdleStateHandler.this.firstAllIdleEvent = false;
                try {
                    if (IdleStateHandler.this.hasOutputChanged(ctx, first)) {
                        return;
                    }
                    final IdleStateEvent event = IdleStateHandler.this.newIdleStateEvent(IdleState.ALL_IDLE, first);
                    IdleStateHandler.this.channelIdle(ctx, event);
                }
                catch (final Throwable t) {
                    ctx.fireExceptionCaught(t);
                }
            }
            else {
                IdleStateHandler.this.allIdleTimeout = IdleStateHandler.this.schedule(ctx, this, nextDelay, TimeUnit.NANOSECONDS);
            }
        }
    }
}
