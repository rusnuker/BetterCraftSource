// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.local;

import java.net.ConnectException;
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.AlreadyConnectedException;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.ReferenceCountUtil;
import java.nio.channels.NotYetConnectedException;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import java.net.SocketAddress;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.EventLoop;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelPipeline;
import io.netty.util.internal.PlatformDependent;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import java.util.Queue;
import io.netty.channel.ChannelConfig;
import java.nio.channels.ClosedChannelException;
import io.netty.channel.ChannelMetadata;
import io.netty.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.AbstractChannel;

public class LocalChannel extends AbstractChannel
{
    private static final InternalLogger logger;
    private static final AtomicReferenceFieldUpdater<LocalChannel, Future> FINISH_READ_FUTURE_UPDATER;
    private static final ChannelMetadata METADATA;
    private static final int MAX_READER_STACK_DEPTH = 8;
    private static final ClosedChannelException DO_WRITE_CLOSED_CHANNEL_EXCEPTION;
    private static final ClosedChannelException DO_CLOSE_CLOSED_CHANNEL_EXCEPTION;
    private final ChannelConfig config;
    private final Queue<Object> inboundBuffer;
    private final Runnable readTask;
    private final Runnable shutdownHook;
    private volatile State state;
    private volatile LocalChannel peer;
    private volatile LocalAddress localAddress;
    private volatile LocalAddress remoteAddress;
    private volatile ChannelPromise connectPromise;
    private volatile boolean readInProgress;
    private volatile boolean registerInProgress;
    private volatile boolean writeInProgress;
    private volatile Future<?> finishReadFuture;
    
    public LocalChannel() {
        super(null);
        this.config = new DefaultChannelConfig(this);
        this.inboundBuffer = PlatformDependent.newSpscQueue();
        this.readTask = new Runnable() {
            @Override
            public void run() {
                final ChannelPipeline pipeline = LocalChannel.this.pipeline();
                while (true) {
                    final Object m = LocalChannel.this.inboundBuffer.poll();
                    if (m == null) {
                        break;
                    }
                    pipeline.fireChannelRead(m);
                }
                pipeline.fireChannelReadComplete();
            }
        };
        this.shutdownHook = new Runnable() {
            @Override
            public void run() {
                LocalChannel.this.unsafe().close(LocalChannel.this.unsafe().voidPromise());
            }
        };
        this.config().setAllocator(new PreferHeapByteBufAllocator(this.config.getAllocator()));
    }
    
    protected LocalChannel(final LocalServerChannel parent, final LocalChannel peer) {
        super(parent);
        this.config = new DefaultChannelConfig(this);
        this.inboundBuffer = PlatformDependent.newSpscQueue();
        this.readTask = new Runnable() {
            @Override
            public void run() {
                final ChannelPipeline pipeline = LocalChannel.this.pipeline();
                while (true) {
                    final Object m = LocalChannel.this.inboundBuffer.poll();
                    if (m == null) {
                        break;
                    }
                    pipeline.fireChannelRead(m);
                }
                pipeline.fireChannelReadComplete();
            }
        };
        this.shutdownHook = new Runnable() {
            @Override
            public void run() {
                LocalChannel.this.unsafe().close(LocalChannel.this.unsafe().voidPromise());
            }
        };
        this.config().setAllocator(new PreferHeapByteBufAllocator(this.config.getAllocator()));
        this.peer = peer;
        this.localAddress = parent.localAddress();
        this.remoteAddress = peer.localAddress();
    }
    
    @Override
    public ChannelMetadata metadata() {
        return LocalChannel.METADATA;
    }
    
    @Override
    public ChannelConfig config() {
        return this.config;
    }
    
    @Override
    public LocalServerChannel parent() {
        return (LocalServerChannel)super.parent();
    }
    
    @Override
    public LocalAddress localAddress() {
        return (LocalAddress)super.localAddress();
    }
    
    @Override
    public LocalAddress remoteAddress() {
        return (LocalAddress)super.remoteAddress();
    }
    
    @Override
    public boolean isOpen() {
        return this.state != State.CLOSED;
    }
    
    @Override
    public boolean isActive() {
        return this.state == State.CONNECTED;
    }
    
    @Override
    protected AbstractUnsafe newUnsafe() {
        return new LocalUnsafe();
    }
    
    @Override
    protected boolean isCompatible(final EventLoop loop) {
        return loop instanceof SingleThreadEventLoop;
    }
    
    @Override
    protected SocketAddress localAddress0() {
        return this.localAddress;
    }
    
    @Override
    protected SocketAddress remoteAddress0() {
        return this.remoteAddress;
    }
    
    @Override
    protected void doRegister() throws Exception {
        if (this.peer != null && this.parent() != null) {
            final LocalChannel peer = this.peer;
            this.registerInProgress = true;
            this.state = State.CONNECTED;
            peer.remoteAddress = ((this.parent() == null) ? null : this.parent().localAddress());
            peer.state = State.CONNECTED;
            peer.eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    LocalChannel.this.registerInProgress = false;
                    final ChannelPromise promise = peer.connectPromise;
                    if (promise != null && promise.trySuccess()) {
                        peer.pipeline().fireChannelActive();
                    }
                }
            });
        }
        ((SingleThreadEventExecutor)this.eventLoop()).addShutdownHook(this.shutdownHook);
    }
    
    @Override
    protected void doBind(final SocketAddress localAddress) throws Exception {
        this.localAddress = LocalChannelRegistry.register(this, this.localAddress, localAddress);
        this.state = State.BOUND;
    }
    
    @Override
    protected void doDisconnect() throws Exception {
        this.doClose();
    }
    
    @Override
    protected void doClose() throws Exception {
        final LocalChannel peer = this.peer;
        if (this.state != State.CLOSED) {
            if (this.localAddress != null) {
                if (this.parent() == null) {
                    LocalChannelRegistry.unregister(this.localAddress);
                }
                this.localAddress = null;
            }
            this.state = State.CLOSED;
            this.finishPeerRead(this);
            final ChannelPromise promise = this.connectPromise;
            if (promise != null) {
                promise.tryFailure(LocalChannel.DO_CLOSE_CLOSED_CHANNEL_EXCEPTION);
                this.connectPromise = null;
            }
        }
        if (peer != null) {
            this.peer = null;
            final EventLoop peerEventLoop = peer.eventLoop();
            final boolean peerIsActive = peer.isActive();
            if (peerEventLoop.inEventLoop() && !this.registerInProgress) {
                peer.tryClose(peerIsActive);
            }
            else {
                try {
                    peerEventLoop.execute(new Runnable() {
                        @Override
                        public void run() {
                            LocalChannel.this.tryClose(peerIsActive);
                        }
                    });
                }
                catch (final Throwable cause) {
                    LocalChannel.logger.warn("Releasing Inbound Queues for channels {}-{} because exception occurred!", this, peer, cause);
                    this.releaseInboundBuffers();
                    if (peerEventLoop.inEventLoop()) {
                        peer.releaseInboundBuffers();
                    }
                    else {
                        peer.close();
                    }
                    PlatformDependent.throwException(cause);
                }
            }
        }
    }
    
    private void tryClose(final boolean isActive) {
        if (isActive) {
            this.unsafe().close(this.unsafe().voidPromise());
        }
        else {
            this.releaseInboundBuffers();
        }
    }
    
    @Override
    protected void doDeregister() throws Exception {
        ((SingleThreadEventExecutor)this.eventLoop()).removeShutdownHook(this.shutdownHook);
    }
    
    @Override
    protected void doBeginRead() throws Exception {
        if (this.readInProgress) {
            return;
        }
        final ChannelPipeline pipeline = this.pipeline();
        final Queue<Object> inboundBuffer = this.inboundBuffer;
        if (inboundBuffer.isEmpty()) {
            this.readInProgress = true;
            return;
        }
        final InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
        final Integer stackDepth = threadLocals.localChannelReaderStackDepth();
        if (stackDepth < 8) {
            threadLocals.setLocalChannelReaderStackDepth(stackDepth + 1);
            try {
                while (true) {
                    final Object received = inboundBuffer.poll();
                    if (received == null) {
                        break;
                    }
                    pipeline.fireChannelRead(received);
                }
                pipeline.fireChannelReadComplete();
            }
            finally {
                threadLocals.setLocalChannelReaderStackDepth(stackDepth);
            }
        }
        else {
            try {
                this.eventLoop().execute(this.readTask);
            }
            catch (final Throwable cause) {
                LocalChannel.logger.warn("Closing Local channels {}-{} because exception occurred!", this, this.peer, cause);
                this.close();
                this.peer.close();
                PlatformDependent.throwException(cause);
            }
        }
    }
    
    @Override
    protected void doWrite(final ChannelOutboundBuffer in) throws Exception {
        switch (this.state) {
            case OPEN:
            case BOUND: {
                throw new NotYetConnectedException();
            }
            case CLOSED: {
                throw LocalChannel.DO_WRITE_CLOSED_CHANNEL_EXCEPTION;
            }
            default: {
                final LocalChannel peer = this.peer;
                this.writeInProgress = true;
                try {
                    while (true) {
                        final Object msg = in.current();
                        if (msg == null) {
                            break;
                        }
                        try {
                            if (peer.state == State.CONNECTED) {
                                peer.inboundBuffer.add(ReferenceCountUtil.retain(msg));
                                in.remove();
                            }
                            else {
                                in.remove(LocalChannel.DO_WRITE_CLOSED_CHANNEL_EXCEPTION);
                            }
                        }
                        catch (final Throwable cause) {
                            in.remove(cause);
                        }
                    }
                }
                finally {
                    this.writeInProgress = false;
                }
                this.finishPeerRead(peer);
            }
        }
    }
    
    private void finishPeerRead(final LocalChannel peer) {
        if (peer.eventLoop() == this.eventLoop() && !peer.writeInProgress) {
            this.finishPeerRead0(peer);
        }
        else {
            this.runFinishPeerReadTask(peer);
        }
    }
    
    private void runFinishPeerReadTask(final LocalChannel peer) {
        final Runnable finishPeerReadTask = new Runnable() {
            @Override
            public void run() {
                LocalChannel.this.finishPeerRead0(peer);
            }
        };
        try {
            if (peer.writeInProgress) {
                peer.finishReadFuture = peer.eventLoop().submit(finishPeerReadTask);
            }
            else {
                peer.eventLoop().execute(finishPeerReadTask);
            }
        }
        catch (final Throwable cause) {
            LocalChannel.logger.warn("Closing Local channels {}-{} because exception occurred!", this, peer, cause);
            this.close();
            peer.close();
            PlatformDependent.throwException(cause);
        }
    }
    
    private void releaseInboundBuffers() {
        assert !(!this.eventLoop().inEventLoop());
        this.readInProgress = false;
        final Queue<Object> inboundBuffer = this.inboundBuffer;
        Object msg;
        while ((msg = inboundBuffer.poll()) != null) {
            ReferenceCountUtil.release(msg);
        }
    }
    
    private void finishPeerRead0(final LocalChannel peer) {
        final Future<?> peerFinishReadFuture = peer.finishReadFuture;
        if (peerFinishReadFuture != null) {
            if (!peerFinishReadFuture.isDone()) {
                this.runFinishPeerReadTask(peer);
                return;
            }
            LocalChannel.FINISH_READ_FUTURE_UPDATER.compareAndSet(peer, peerFinishReadFuture, null);
        }
        final ChannelPipeline peerPipeline = peer.pipeline();
        if (peer.readInProgress) {
            peer.readInProgress = false;
            while (true) {
                final Object received = peer.inboundBuffer.poll();
                if (received == null) {
                    break;
                }
                peerPipeline.fireChannelRead(received);
            }
            peerPipeline.fireChannelReadComplete();
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(LocalChannel.class);
        FINISH_READ_FUTURE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(LocalChannel.class, Future.class, "finishReadFuture");
        METADATA = new ChannelMetadata(false);
        DO_WRITE_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.unknownStackTrace(new ClosedChannelException(), LocalChannel.class, "doWrite(...)");
        DO_CLOSE_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.unknownStackTrace(new ClosedChannelException(), LocalChannel.class, "doClose()");
    }
    
    private enum State
    {
        OPEN, 
        BOUND, 
        CONNECTED, 
        CLOSED;
    }
    
    private class LocalUnsafe extends AbstractUnsafe
    {
        @Override
        public void connect(final SocketAddress remoteAddress, SocketAddress localAddress, final ChannelPromise promise) {
            if (!promise.setUncancellable() || !this.ensureOpen(promise)) {
                return;
            }
            if (LocalChannel.this.state == State.CONNECTED) {
                final Exception cause = new AlreadyConnectedException();
                this.safeSetFailure(promise, cause);
                LocalChannel.this.pipeline().fireExceptionCaught((Throwable)cause);
                return;
            }
            if (LocalChannel.this.connectPromise != null) {
                throw new ConnectionPendingException();
            }
            LocalChannel.this.connectPromise = promise;
            if (LocalChannel.this.state != State.BOUND && localAddress == null) {
                localAddress = new LocalAddress(LocalChannel.this);
            }
            if (localAddress != null) {
                try {
                    LocalChannel.this.doBind(localAddress);
                }
                catch (final Throwable t) {
                    this.safeSetFailure(promise, t);
                    this.close(this.voidPromise());
                    return;
                }
            }
            final Channel boundChannel = LocalChannelRegistry.get(remoteAddress);
            if (!(boundChannel instanceof LocalServerChannel)) {
                final Exception cause2 = new ConnectException("connection refused: " + remoteAddress);
                this.safeSetFailure(promise, cause2);
                this.close(this.voidPromise());
                return;
            }
            final LocalServerChannel serverChannel = (LocalServerChannel)boundChannel;
            LocalChannel.this.peer = serverChannel.serve(LocalChannel.this);
        }
    }
}
