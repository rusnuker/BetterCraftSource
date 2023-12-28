// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.nio;

import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import java.util.concurrent.TimeUnit;
import io.netty.channel.ConnectTimeoutException;
import java.nio.channels.ConnectionPendingException;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.netty.buffer.ByteBuf;
import java.nio.channels.CancelledKeyException;
import io.netty.channel.EventLoop;
import io.netty.channel.ChannelException;
import java.io.IOException;
import io.netty.channel.Channel;
import java.net.SocketAddress;
import java.util.concurrent.ScheduledFuture;
import io.netty.channel.ChannelPromise;
import java.nio.channels.SelectionKey;
import java.nio.channels.SelectableChannel;
import java.nio.channels.ClosedChannelException;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.AbstractChannel;

public abstract class AbstractNioChannel extends AbstractChannel
{
    private static final InternalLogger logger;
    private static final ClosedChannelException DO_CLOSE_CLOSED_CHANNEL_EXCEPTION;
    private final SelectableChannel ch;
    protected final int readInterestOp;
    volatile SelectionKey selectionKey;
    boolean readPending;
    private final Runnable clearReadPendingRunnable;
    private ChannelPromise connectPromise;
    private ScheduledFuture<?> connectTimeoutFuture;
    private SocketAddress requestedRemoteAddress;
    
    protected AbstractNioChannel(final Channel parent, final SelectableChannel ch, final int readInterestOp) {
        super(parent);
        this.clearReadPendingRunnable = new Runnable() {
            @Override
            public void run() {
                AbstractNioChannel.this.clearReadPending0();
            }
        };
        this.ch = ch;
        this.readInterestOp = readInterestOp;
        try {
            ch.configureBlocking(false);
        }
        catch (final IOException e) {
            try {
                ch.close();
            }
            catch (final IOException e2) {
                if (AbstractNioChannel.logger.isWarnEnabled()) {
                    AbstractNioChannel.logger.warn("Failed to close a partially initialized socket.", e2);
                }
            }
            throw new ChannelException("Failed to enter non-blocking mode.", e);
        }
    }
    
    @Override
    public boolean isOpen() {
        return this.ch.isOpen();
    }
    
    @Override
    public NioUnsafe unsafe() {
        return (NioUnsafe)super.unsafe();
    }
    
    protected SelectableChannel javaChannel() {
        return this.ch;
    }
    
    @Override
    public NioEventLoop eventLoop() {
        return (NioEventLoop)super.eventLoop();
    }
    
    protected SelectionKey selectionKey() {
        assert this.selectionKey != null;
        return this.selectionKey;
    }
    
    @Deprecated
    protected boolean isReadPending() {
        return this.readPending;
    }
    
    @Deprecated
    protected void setReadPending(final boolean readPending) {
        if (this.isRegistered()) {
            final EventLoop eventLoop = this.eventLoop();
            if (eventLoop.inEventLoop()) {
                this.setReadPending0(readPending);
            }
            else {
                eventLoop.execute(new Runnable() {
                    @Override
                    public void run() {
                        AbstractNioChannel.this.setReadPending0(readPending);
                    }
                });
            }
        }
        else {
            this.readPending = readPending;
        }
    }
    
    protected final void clearReadPending() {
        if (this.isRegistered()) {
            final EventLoop eventLoop = this.eventLoop();
            if (eventLoop.inEventLoop()) {
                this.clearReadPending0();
            }
            else {
                eventLoop.execute(this.clearReadPendingRunnable);
            }
        }
        else {
            this.readPending = false;
        }
    }
    
    private void setReadPending0(final boolean readPending) {
        if (!(this.readPending = readPending)) {
            ((AbstractNioUnsafe)this.unsafe()).removeReadOp();
        }
    }
    
    private void clearReadPending0() {
        this.readPending = false;
        ((AbstractNioUnsafe)this.unsafe()).removeReadOp();
    }
    
    @Override
    protected boolean isCompatible(final EventLoop loop) {
        return loop instanceof NioEventLoop;
    }
    
    @Override
    protected void doRegister() throws Exception {
        boolean selected = false;
        while (true) {
            try {
                this.selectionKey = this.javaChannel().register(this.eventLoop().unwrappedSelector(), 0, this);
            }
            catch (final CancelledKeyException e) {
                if (!selected) {
                    this.eventLoop().selectNow();
                    selected = true;
                    continue;
                }
                throw e;
            }
            break;
        }
    }
    
    @Override
    protected void doDeregister() throws Exception {
        this.eventLoop().cancel(this.selectionKey());
    }
    
    @Override
    protected void doBeginRead() throws Exception {
        final SelectionKey selectionKey = this.selectionKey;
        if (!selectionKey.isValid()) {
            return;
        }
        this.readPending = true;
        final int interestOps = selectionKey.interestOps();
        if ((interestOps & this.readInterestOp) == 0x0) {
            selectionKey.interestOps(interestOps | this.readInterestOp);
        }
    }
    
    protected abstract boolean doConnect(final SocketAddress p0, final SocketAddress p1) throws Exception;
    
    protected abstract void doFinishConnect() throws Exception;
    
    protected final ByteBuf newDirectBuffer(final ByteBuf buf) {
        final int readableBytes = buf.readableBytes();
        if (readableBytes == 0) {
            ReferenceCountUtil.safeRelease(buf);
            return Unpooled.EMPTY_BUFFER;
        }
        final ByteBufAllocator alloc = this.alloc();
        if (alloc.isDirectBufferPooled()) {
            final ByteBuf directBuf = alloc.directBuffer(readableBytes);
            directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
            ReferenceCountUtil.safeRelease(buf);
            return directBuf;
        }
        final ByteBuf directBuf = ByteBufUtil.threadLocalDirectBuffer();
        if (directBuf != null) {
            directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
            ReferenceCountUtil.safeRelease(buf);
            return directBuf;
        }
        return buf;
    }
    
    protected final ByteBuf newDirectBuffer(final ReferenceCounted holder, final ByteBuf buf) {
        final int readableBytes = buf.readableBytes();
        if (readableBytes == 0) {
            ReferenceCountUtil.safeRelease(holder);
            return Unpooled.EMPTY_BUFFER;
        }
        final ByteBufAllocator alloc = this.alloc();
        if (alloc.isDirectBufferPooled()) {
            final ByteBuf directBuf = alloc.directBuffer(readableBytes);
            directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
            ReferenceCountUtil.safeRelease(holder);
            return directBuf;
        }
        final ByteBuf directBuf = ByteBufUtil.threadLocalDirectBuffer();
        if (directBuf != null) {
            directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
            ReferenceCountUtil.safeRelease(holder);
            return directBuf;
        }
        if (holder != buf) {
            buf.retain();
            ReferenceCountUtil.safeRelease(holder);
        }
        return buf;
    }
    
    @Override
    protected void doClose() throws Exception {
        final ChannelPromise promise = this.connectPromise;
        if (promise != null) {
            promise.tryFailure(AbstractNioChannel.DO_CLOSE_CLOSED_CHANNEL_EXCEPTION);
            this.connectPromise = null;
        }
        final ScheduledFuture<?> future = this.connectTimeoutFuture;
        if (future != null) {
            future.cancel(false);
            this.connectTimeoutFuture = null;
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(AbstractNioChannel.class);
        DO_CLOSE_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractNioChannel.class, "doClose()");
    }
    
    protected abstract class AbstractNioUnsafe extends AbstractUnsafe implements NioUnsafe
    {
        protected final void removeReadOp() {
            final SelectionKey key = AbstractNioChannel.this.selectionKey();
            if (!key.isValid()) {
                return;
            }
            final int interestOps = key.interestOps();
            if ((interestOps & AbstractNioChannel.this.readInterestOp) != 0x0) {
                key.interestOps(interestOps & ~AbstractNioChannel.this.readInterestOp);
            }
        }
        
        @Override
        public final SelectableChannel ch() {
            return AbstractNioChannel.this.javaChannel();
        }
        
        @Override
        public final void connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
            if (!promise.setUncancellable() || !this.ensureOpen(promise)) {
                return;
            }
            try {
                if (AbstractNioChannel.this.connectPromise != null) {
                    throw new ConnectionPendingException();
                }
                final boolean wasActive = AbstractNioChannel.this.isActive();
                if (AbstractNioChannel.this.doConnect(remoteAddress, localAddress)) {
                    this.fulfillConnectPromise(promise, wasActive);
                }
                else {
                    AbstractNioChannel.this.connectPromise = promise;
                    AbstractNioChannel.this.requestedRemoteAddress = remoteAddress;
                    final int connectTimeoutMillis = AbstractNioChannel.this.config().getConnectTimeoutMillis();
                    if (connectTimeoutMillis > 0) {
                        AbstractNioChannel.this.connectTimeoutFuture = AbstractNioChannel.this.eventLoop().schedule(new Runnable() {
                            @Override
                            public void run() {
                                final ChannelPromise connectPromise = AbstractNioChannel.this.connectPromise;
                                final ConnectTimeoutException cause = new ConnectTimeoutException("connection timed out: " + remoteAddress);
                                if (connectPromise != null && connectPromise.tryFailure(cause)) {
                                    AbstractNioUnsafe.this.close(AbstractNioUnsafe.this.voidPromise());
                                }
                            }
                        }, connectTimeoutMillis, TimeUnit.MILLISECONDS);
                    }
                    promise.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                        @Override
                        public void operationComplete(final ChannelFuture future) throws Exception {
                            if (future.isCancelled()) {
                                if (AbstractNioChannel.this.connectTimeoutFuture != null) {
                                    AbstractNioChannel.this.connectTimeoutFuture.cancel(false);
                                }
                                AbstractNioChannel.this.connectPromise = null;
                                AbstractNioUnsafe.this.close(AbstractNioUnsafe.this.voidPromise());
                            }
                        }
                    });
                }
            }
            catch (final Throwable t) {
                promise.tryFailure(this.annotateConnectException(t, remoteAddress));
                this.closeIfClosed();
            }
        }
        
        private void fulfillConnectPromise(final ChannelPromise promise, final boolean wasActive) {
            if (promise == null) {
                return;
            }
            final boolean active = AbstractNioChannel.this.isActive();
            final boolean promiseSet = promise.trySuccess();
            if (!wasActive && active) {
                AbstractNioChannel.this.pipeline().fireChannelActive();
            }
            if (!promiseSet) {
                this.close(this.voidPromise());
            }
        }
        
        private void fulfillConnectPromise(final ChannelPromise promise, final Throwable cause) {
            if (promise == null) {
                return;
            }
            promise.tryFailure(cause);
            this.closeIfClosed();
        }
        
        @Override
        public final void finishConnect() {
            assert AbstractNioChannel.this.eventLoop().inEventLoop();
            try {
                final boolean wasActive = AbstractNioChannel.this.isActive();
                AbstractNioChannel.this.doFinishConnect();
                this.fulfillConnectPromise(AbstractNioChannel.this.connectPromise, wasActive);
            }
            catch (final Throwable t) {
                this.fulfillConnectPromise(AbstractNioChannel.this.connectPromise, this.annotateConnectException(t, AbstractNioChannel.this.requestedRemoteAddress));
            }
            finally {
                if (AbstractNioChannel.this.connectTimeoutFuture != null) {
                    AbstractNioChannel.this.connectTimeoutFuture.cancel(false);
                }
                AbstractNioChannel.this.connectPromise = null;
            }
        }
        
        @Override
        protected final void flush0() {
            if (this.isFlushPending()) {
                return;
            }
            super.flush0();
        }
        
        @Override
        public final void forceFlush() {
            super.flush0();
        }
        
        private boolean isFlushPending() {
            final SelectionKey selectionKey = AbstractNioChannel.this.selectionKey();
            return selectionKey.isValid() && (selectionKey.interestOps() & 0x4) != 0x0;
        }
    }
    
    public interface NioUnsafe extends Channel.Unsafe
    {
        SelectableChannel ch();
        
        void finishConnect();
        
        void read();
        
        void forceFlush();
    }
}
