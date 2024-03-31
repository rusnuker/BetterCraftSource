// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

import io.netty.util.concurrent.Promise;
import java.net.SocketException;
import java.net.NoRouteToHostException;
import java.net.ConnectException;
import java.util.concurrent.RejectedExecutionException;
import java.io.IOException;
import io.netty.util.ReferenceCountUtil;
import java.util.concurrent.Executor;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.PlatformDependent;
import java.net.InetSocketAddress;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.net.SocketAddress;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.ClosedChannelException;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.DefaultAttributeMap;

public abstract class AbstractChannel extends DefaultAttributeMap implements Channel
{
    private static final InternalLogger logger;
    private static final ClosedChannelException FLUSH0_CLOSED_CHANNEL_EXCEPTION;
    private static final ClosedChannelException ENSURE_OPEN_CLOSED_CHANNEL_EXCEPTION;
    private static final ClosedChannelException CLOSE_CLOSED_CHANNEL_EXCEPTION;
    private static final ClosedChannelException WRITE_CLOSED_CHANNEL_EXCEPTION;
    private static final NotYetConnectedException FLUSH0_NOT_YET_CONNECTED_EXCEPTION;
    private final Channel parent;
    private final ChannelId id;
    private final Unsafe unsafe;
    private final DefaultChannelPipeline pipeline;
    private final VoidChannelPromise unsafeVoidPromise;
    private final CloseFuture closeFuture;
    private volatile SocketAddress localAddress;
    private volatile SocketAddress remoteAddress;
    private volatile EventLoop eventLoop;
    private volatile boolean registered;
    private boolean strValActive;
    private String strVal;
    
    protected AbstractChannel(final Channel parent) {
        this.unsafeVoidPromise = new VoidChannelPromise(this, false);
        this.closeFuture = new CloseFuture(this);
        this.parent = parent;
        this.id = this.newId();
        this.unsafe = this.newUnsafe();
        this.pipeline = this.newChannelPipeline();
    }
    
    protected AbstractChannel(final Channel parent, final ChannelId id) {
        this.unsafeVoidPromise = new VoidChannelPromise(this, false);
        this.closeFuture = new CloseFuture(this);
        this.parent = parent;
        this.id = id;
        this.unsafe = this.newUnsafe();
        this.pipeline = this.newChannelPipeline();
    }
    
    @Override
    public final ChannelId id() {
        return this.id;
    }
    
    protected ChannelId newId() {
        return DefaultChannelId.newInstance();
    }
    
    protected DefaultChannelPipeline newChannelPipeline() {
        return new DefaultChannelPipeline(this);
    }
    
    @Override
    public boolean isWritable() {
        final ChannelOutboundBuffer buf = this.unsafe.outboundBuffer();
        return buf != null && buf.isWritable();
    }
    
    @Override
    public long bytesBeforeUnwritable() {
        final ChannelOutboundBuffer buf = this.unsafe.outboundBuffer();
        return (buf != null) ? buf.bytesBeforeUnwritable() : 0L;
    }
    
    @Override
    public long bytesBeforeWritable() {
        final ChannelOutboundBuffer buf = this.unsafe.outboundBuffer();
        return (buf != null) ? buf.bytesBeforeWritable() : Long.MAX_VALUE;
    }
    
    @Override
    public Channel parent() {
        return this.parent;
    }
    
    @Override
    public ChannelPipeline pipeline() {
        return this.pipeline;
    }
    
    @Override
    public ByteBufAllocator alloc() {
        return this.config().getAllocator();
    }
    
    @Override
    public EventLoop eventLoop() {
        final EventLoop eventLoop = this.eventLoop;
        if (eventLoop == null) {
            throw new IllegalStateException("channel not registered to an event loop");
        }
        return eventLoop;
    }
    
    @Override
    public SocketAddress localAddress() {
        SocketAddress localAddress = this.localAddress;
        if (localAddress == null) {
            try {
                localAddress = (this.localAddress = this.unsafe().localAddress());
            }
            catch (final Throwable t) {
                return null;
            }
        }
        return localAddress;
    }
    
    @Deprecated
    protected void invalidateLocalAddress() {
        this.localAddress = null;
    }
    
    @Override
    public SocketAddress remoteAddress() {
        SocketAddress remoteAddress = this.remoteAddress;
        if (remoteAddress == null) {
            try {
                remoteAddress = (this.remoteAddress = this.unsafe().remoteAddress());
            }
            catch (final Throwable t) {
                return null;
            }
        }
        return remoteAddress;
    }
    
    @Deprecated
    protected void invalidateRemoteAddress() {
        this.remoteAddress = null;
    }
    
    @Override
    public boolean isRegistered() {
        return this.registered;
    }
    
    @Override
    public ChannelFuture bind(final SocketAddress localAddress) {
        return this.pipeline.bind(localAddress);
    }
    
    @Override
    public ChannelFuture connect(final SocketAddress remoteAddress) {
        return this.pipeline.connect(remoteAddress);
    }
    
    @Override
    public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress) {
        return this.pipeline.connect(remoteAddress, localAddress);
    }
    
    @Override
    public ChannelFuture disconnect() {
        return this.pipeline.disconnect();
    }
    
    @Override
    public ChannelFuture close() {
        return this.pipeline.close();
    }
    
    @Override
    public ChannelFuture deregister() {
        return this.pipeline.deregister();
    }
    
    @Override
    public Channel flush() {
        this.pipeline.flush();
        return this;
    }
    
    @Override
    public ChannelFuture bind(final SocketAddress localAddress, final ChannelPromise promise) {
        return this.pipeline.bind(localAddress, promise);
    }
    
    @Override
    public ChannelFuture connect(final SocketAddress remoteAddress, final ChannelPromise promise) {
        return this.pipeline.connect(remoteAddress, promise);
    }
    
    @Override
    public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
        return this.pipeline.connect(remoteAddress, localAddress, promise);
    }
    
    @Override
    public ChannelFuture disconnect(final ChannelPromise promise) {
        return this.pipeline.disconnect(promise);
    }
    
    @Override
    public ChannelFuture close(final ChannelPromise promise) {
        return this.pipeline.close(promise);
    }
    
    @Override
    public ChannelFuture deregister(final ChannelPromise promise) {
        return this.pipeline.deregister(promise);
    }
    
    @Override
    public Channel read() {
        this.pipeline.read();
        return this;
    }
    
    @Override
    public ChannelFuture write(final Object msg) {
        return this.pipeline.write(msg);
    }
    
    @Override
    public ChannelFuture write(final Object msg, final ChannelPromise promise) {
        return this.pipeline.write(msg, promise);
    }
    
    @Override
    public ChannelFuture writeAndFlush(final Object msg) {
        return this.pipeline.writeAndFlush(msg);
    }
    
    @Override
    public ChannelFuture writeAndFlush(final Object msg, final ChannelPromise promise) {
        return this.pipeline.writeAndFlush(msg, promise);
    }
    
    @Override
    public ChannelPromise newPromise() {
        return this.pipeline.newPromise();
    }
    
    @Override
    public ChannelProgressivePromise newProgressivePromise() {
        return this.pipeline.newProgressivePromise();
    }
    
    @Override
    public ChannelFuture newSucceededFuture() {
        return this.pipeline.newSucceededFuture();
    }
    
    @Override
    public ChannelFuture newFailedFuture(final Throwable cause) {
        return this.pipeline.newFailedFuture(cause);
    }
    
    @Override
    public ChannelFuture closeFuture() {
        return this.closeFuture;
    }
    
    @Override
    public Unsafe unsafe() {
        return this.unsafe;
    }
    
    protected abstract AbstractUnsafe newUnsafe();
    
    @Override
    public final int hashCode() {
        return this.id.hashCode();
    }
    
    @Override
    public final boolean equals(final Object o) {
        return this == o;
    }
    
    @Override
    public final int compareTo(final Channel o) {
        if (this == o) {
            return 0;
        }
        return this.id().compareTo(o.id());
    }
    
    @Override
    public String toString() {
        final boolean active = this.isActive();
        if (this.strValActive == active && this.strVal != null) {
            return this.strVal;
        }
        final SocketAddress remoteAddr = this.remoteAddress();
        final SocketAddress localAddr = this.localAddress();
        if (remoteAddr != null) {
            final StringBuilder buf = new StringBuilder(96).append("[id: 0x").append(this.id.asShortText()).append(", L:").append(localAddr).append(active ? " - " : " ! ").append("R:").append(remoteAddr).append(']');
            this.strVal = buf.toString();
        }
        else if (localAddr != null) {
            final StringBuilder buf = new StringBuilder(64).append("[id: 0x").append(this.id.asShortText()).append(", L:").append(localAddr).append(']');
            this.strVal = buf.toString();
        }
        else {
            final StringBuilder buf = new StringBuilder(16).append("[id: 0x").append(this.id.asShortText()).append(']');
            this.strVal = buf.toString();
        }
        this.strValActive = active;
        return this.strVal;
    }
    
    @Override
    public final ChannelPromise voidPromise() {
        return this.pipeline.voidPromise();
    }
    
    protected abstract boolean isCompatible(final EventLoop p0);
    
    protected abstract SocketAddress localAddress0();
    
    protected abstract SocketAddress remoteAddress0();
    
    protected void doRegister() throws Exception {
    }
    
    protected abstract void doBind(final SocketAddress p0) throws Exception;
    
    protected abstract void doDisconnect() throws Exception;
    
    protected abstract void doClose() throws Exception;
    
    protected void doDeregister() throws Exception {
    }
    
    protected abstract void doBeginRead() throws Exception;
    
    protected abstract void doWrite(final ChannelOutboundBuffer p0) throws Exception;
    
    protected Object filterOutboundMessage(final Object msg) throws Exception {
        return msg;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(AbstractChannel.class);
        FLUSH0_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractUnsafe.class, "flush0()");
        ENSURE_OPEN_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractUnsafe.class, "ensureOpen(...)");
        CLOSE_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractUnsafe.class, "close(...)");
        WRITE_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractUnsafe.class, "write(...)");
        FLUSH0_NOT_YET_CONNECTED_EXCEPTION = ThrowableUtil.unknownStackTrace(new NotYetConnectedException(), AbstractUnsafe.class, "flush0()");
    }
    
    protected abstract class AbstractUnsafe implements Unsafe
    {
        private volatile ChannelOutboundBuffer outboundBuffer;
        private RecvByteBufAllocator.Handle recvHandle;
        private boolean inFlush0;
        private boolean neverRegistered;
        
        protected AbstractUnsafe() {
            this.outboundBuffer = new ChannelOutboundBuffer(AbstractChannel.this);
            this.neverRegistered = true;
        }
        
        private void assertEventLoop() {
            assert !(!AbstractChannel.this.eventLoop.inEventLoop());
        }
        
        @Override
        public RecvByteBufAllocator.Handle recvBufAllocHandle() {
            if (this.recvHandle == null) {
                this.recvHandle = AbstractChannel.this.config().getRecvByteBufAllocator().newHandle();
            }
            return this.recvHandle;
        }
        
        @Override
        public final ChannelOutboundBuffer outboundBuffer() {
            return this.outboundBuffer;
        }
        
        @Override
        public final SocketAddress localAddress() {
            return AbstractChannel.this.localAddress0();
        }
        
        @Override
        public final SocketAddress remoteAddress() {
            return AbstractChannel.this.remoteAddress0();
        }
        
        @Override
        public final void register(final EventLoop eventLoop, final ChannelPromise promise) {
            if (eventLoop == null) {
                throw new NullPointerException("eventLoop");
            }
            if (AbstractChannel.this.isRegistered()) {
                promise.setFailure((Throwable)new IllegalStateException("registered to an event loop already"));
                return;
            }
            if (!AbstractChannel.this.isCompatible(eventLoop)) {
                promise.setFailure((Throwable)new IllegalStateException("incompatible event loop type: " + eventLoop.getClass().getName()));
                return;
            }
            AbstractChannel.this.eventLoop = eventLoop;
            if (eventLoop.inEventLoop()) {
                this.register0(promise);
            }
            else {
                try {
                    eventLoop.execute(new Runnable() {
                        @Override
                        public void run() {
                            AbstractUnsafe.this.register0(promise);
                        }
                    });
                }
                catch (final Throwable t) {
                    AbstractChannel.logger.warn("Force-closing a channel whose registration task was not accepted by an event loop: {}", AbstractChannel.this, t);
                    this.closeForcibly();
                    AbstractChannel.this.closeFuture.setClosed();
                    this.safeSetFailure(promise, t);
                }
            }
        }
        
        private void register0(final ChannelPromise promise) {
            try {
                if (!promise.setUncancellable() || !this.ensureOpen(promise)) {
                    return;
                }
                final boolean firstRegistration = this.neverRegistered;
                AbstractChannel.this.doRegister();
                this.neverRegistered = false;
                AbstractChannel.this.registered = true;
                AbstractChannel.this.pipeline.invokeHandlerAddedIfNeeded();
                this.safeSetSuccess(promise);
                AbstractChannel.this.pipeline.fireChannelRegistered();
                if (AbstractChannel.this.isActive()) {
                    if (firstRegistration) {
                        AbstractChannel.this.pipeline.fireChannelActive();
                    }
                    else if (AbstractChannel.this.config().isAutoRead()) {
                        this.beginRead();
                    }
                }
            }
            catch (final Throwable t) {
                this.closeForcibly();
                AbstractChannel.this.closeFuture.setClosed();
                this.safeSetFailure(promise, t);
            }
        }
        
        @Override
        public final void bind(final SocketAddress localAddress, final ChannelPromise promise) {
            this.assertEventLoop();
            if (!promise.setUncancellable() || !this.ensureOpen(promise)) {
                return;
            }
            if (Boolean.TRUE.equals(AbstractChannel.this.config().getOption(ChannelOption.SO_BROADCAST)) && localAddress instanceof InetSocketAddress && !((InetSocketAddress)localAddress).getAddress().isAnyLocalAddress() && !PlatformDependent.isWindows() && !PlatformDependent.maybeSuperUser()) {
                AbstractChannel.logger.warn("A non-root user can't receive a broadcast packet if the socket is not bound to a wildcard address; binding to a non-wildcard address (" + localAddress + ") anyway as requested.");
            }
            final boolean wasActive = AbstractChannel.this.isActive();
            try {
                AbstractChannel.this.doBind(localAddress);
            }
            catch (final Throwable t) {
                this.safeSetFailure(promise, t);
                this.closeIfClosed();
                return;
            }
            if (!wasActive && AbstractChannel.this.isActive()) {
                this.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        AbstractChannel.this.pipeline.fireChannelActive();
                    }
                });
            }
            this.safeSetSuccess(promise);
        }
        
        @Override
        public final void disconnect(final ChannelPromise promise) {
            this.assertEventLoop();
            if (!promise.setUncancellable()) {
                return;
            }
            final boolean wasActive = AbstractChannel.this.isActive();
            try {
                AbstractChannel.this.doDisconnect();
            }
            catch (final Throwable t) {
                this.safeSetFailure(promise, t);
                this.closeIfClosed();
                return;
            }
            if (wasActive && !AbstractChannel.this.isActive()) {
                this.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        AbstractChannel.this.pipeline.fireChannelInactive();
                    }
                });
            }
            this.safeSetSuccess(promise);
            this.closeIfClosed();
        }
        
        @Override
        public final void close(final ChannelPromise promise) {
            this.assertEventLoop();
            this.close(promise, AbstractChannel.CLOSE_CLOSED_CHANNEL_EXCEPTION, AbstractChannel.CLOSE_CLOSED_CHANNEL_EXCEPTION, false);
        }
        
        private void close(final ChannelPromise promise, final Throwable cause, final ClosedChannelException closeCause, final boolean notify) {
            if (!promise.setUncancellable()) {
                return;
            }
            final ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
            if (outboundBuffer == null) {
                if (!(promise instanceof VoidChannelPromise)) {
                    AbstractChannel.this.closeFuture.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(final ChannelFuture future) throws Exception {
                            promise.setSuccess();
                        }
                    });
                }
                return;
            }
            if (AbstractChannel.this.closeFuture.isDone()) {
                this.safeSetSuccess(promise);
                return;
            }
            final boolean wasActive = AbstractChannel.this.isActive();
            this.outboundBuffer = null;
            final Executor closeExecutor = this.prepareToClose();
            if (closeExecutor != null) {
                closeExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            AbstractUnsafe.this.doClose0(promise);
                        }
                        finally {
                            AbstractUnsafe.this.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    outboundBuffer.failFlushed(cause, notify);
                                    outboundBuffer.close(closeCause);
                                    AbstractUnsafe.this.fireChannelInactiveAndDeregister(wasActive);
                                }
                            });
                        }
                    }
                });
            }
            else {
                try {
                    this.doClose0(promise);
                }
                finally {
                    outboundBuffer.failFlushed(cause, notify);
                    outboundBuffer.close(closeCause);
                }
                if (this.inFlush0) {
                    this.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            AbstractUnsafe.this.fireChannelInactiveAndDeregister(wasActive);
                        }
                    });
                }
                else {
                    this.fireChannelInactiveAndDeregister(wasActive);
                }
            }
        }
        
        private void doClose0(final ChannelPromise promise) {
            try {
                AbstractChannel.this.doClose();
                AbstractChannel.this.closeFuture.setClosed();
                this.safeSetSuccess(promise);
            }
            catch (final Throwable t) {
                AbstractChannel.this.closeFuture.setClosed();
                this.safeSetFailure(promise, t);
            }
        }
        
        private void fireChannelInactiveAndDeregister(final boolean wasActive) {
            this.deregister(this.voidPromise(), wasActive && !AbstractChannel.this.isActive());
        }
        
        @Override
        public final void closeForcibly() {
            this.assertEventLoop();
            try {
                AbstractChannel.this.doClose();
            }
            catch (final Exception e) {
                AbstractChannel.logger.warn("Failed to close a channel.", e);
            }
        }
        
        @Override
        public final void deregister(final ChannelPromise promise) {
            this.assertEventLoop();
            this.deregister(promise, false);
        }
        
        private void deregister(final ChannelPromise promise, final boolean fireChannelInactive) {
            if (!promise.setUncancellable()) {
                return;
            }
            if (!AbstractChannel.this.registered) {
                this.safeSetSuccess(promise);
                return;
            }
            this.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        AbstractChannel.this.doDeregister();
                    }
                    catch (final Throwable t) {
                        AbstractChannel.logger.warn("Unexpected exception occurred while deregistering a channel.", t);
                    }
                    finally {
                        if (fireChannelInactive) {
                            AbstractChannel.this.pipeline.fireChannelInactive();
                        }
                        if (AbstractChannel.this.registered) {
                            AbstractChannel.this.registered = false;
                            AbstractChannel.this.pipeline.fireChannelUnregistered();
                        }
                        AbstractUnsafe.this.safeSetSuccess(promise);
                    }
                }
            });
        }
        
        @Override
        public final void beginRead() {
            this.assertEventLoop();
            if (!AbstractChannel.this.isActive()) {
                return;
            }
            try {
                AbstractChannel.this.doBeginRead();
            }
            catch (final Exception e) {
                this.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        AbstractChannel.this.pipeline.fireExceptionCaught((Throwable)e);
                    }
                });
                this.close(this.voidPromise());
            }
        }
        
        @Override
        public final void write(Object msg, final ChannelPromise promise) {
            this.assertEventLoop();
            final ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
            if (outboundBuffer == null) {
                this.safeSetFailure(promise, AbstractChannel.WRITE_CLOSED_CHANNEL_EXCEPTION);
                ReferenceCountUtil.release(msg);
                return;
            }
            int size;
            try {
                msg = AbstractChannel.this.filterOutboundMessage(msg);
                size = AbstractChannel.this.pipeline.estimatorHandle().size(msg);
                if (size < 0) {
                    size = 0;
                }
            }
            catch (final Throwable t) {
                this.safeSetFailure(promise, t);
                ReferenceCountUtil.release(msg);
                return;
            }
            outboundBuffer.addMessage(msg, size, promise);
        }
        
        @Override
        public final void flush() {
            this.assertEventLoop();
            final ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
            if (outboundBuffer == null) {
                return;
            }
            outboundBuffer.addFlush();
            this.flush0();
        }
        
        protected void flush0() {
            if (this.inFlush0) {
                return;
            }
            final ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
            if (outboundBuffer == null || outboundBuffer.isEmpty()) {
                return;
            }
            this.inFlush0 = true;
            if (!AbstractChannel.this.isActive()) {
                try {
                    if (AbstractChannel.this.isOpen()) {
                        outboundBuffer.failFlushed(AbstractChannel.FLUSH0_NOT_YET_CONNECTED_EXCEPTION, true);
                    }
                    else {
                        outboundBuffer.failFlushed(AbstractChannel.FLUSH0_CLOSED_CHANNEL_EXCEPTION, false);
                    }
                }
                finally {
                    this.inFlush0 = false;
                }
                return;
            }
            try {
                AbstractChannel.this.doWrite(outboundBuffer);
            }
            catch (final Throwable t) {
                if (t instanceof IOException && AbstractChannel.this.config().isAutoClose()) {
                    this.close(this.voidPromise(), t, AbstractChannel.FLUSH0_CLOSED_CHANNEL_EXCEPTION, false);
                }
                else {
                    outboundBuffer.failFlushed(t, true);
                }
            }
            finally {
                this.inFlush0 = false;
            }
        }
        
        @Override
        public final ChannelPromise voidPromise() {
            this.assertEventLoop();
            return AbstractChannel.this.unsafeVoidPromise;
        }
        
        @Deprecated
        protected final boolean ensureOpen(final ChannelPromise promise) {
            if (AbstractChannel.this.isOpen()) {
                return true;
            }
            this.safeSetFailure(promise, AbstractChannel.ENSURE_OPEN_CLOSED_CHANNEL_EXCEPTION);
            return false;
        }
        
        protected final void safeSetSuccess(final ChannelPromise promise) {
            if (!(promise instanceof VoidChannelPromise) && !promise.trySuccess()) {
                AbstractChannel.logger.warn("Failed to mark a promise as success because it is done already: {}", promise);
            }
        }
        
        protected final void safeSetFailure(final ChannelPromise promise, final Throwable cause) {
            if (!(promise instanceof VoidChannelPromise) && !promise.tryFailure(cause)) {
                AbstractChannel.logger.warn("Failed to mark a promise as failure because it's done already: {}", promise, cause);
            }
        }
        
        protected final void closeIfClosed() {
            if (AbstractChannel.this.isOpen()) {
                return;
            }
            this.close(this.voidPromise());
        }
        
        private void invokeLater(final Runnable task) {
            try {
                AbstractChannel.this.eventLoop().execute(task);
            }
            catch (final RejectedExecutionException e) {
                AbstractChannel.logger.warn("Can't invoke task later as EventLoop rejected it", e);
            }
        }
        
        protected final Throwable annotateConnectException(final Throwable cause, final SocketAddress remoteAddress) {
            if (cause instanceof ConnectException) {
                return new AnnotatedConnectException((ConnectException)cause, remoteAddress);
            }
            if (cause instanceof NoRouteToHostException) {
                return new AnnotatedNoRouteToHostException((NoRouteToHostException)cause, remoteAddress);
            }
            if (cause instanceof SocketException) {
                return new AnnotatedSocketException((SocketException)cause, remoteAddress);
            }
            return cause;
        }
        
        protected Executor prepareToClose() {
            return null;
        }
    }
    
    static final class CloseFuture extends DefaultChannelPromise
    {
        CloseFuture(final AbstractChannel ch) {
            super(ch);
        }
        
        @Override
        public ChannelPromise setSuccess() {
            throw new IllegalStateException();
        }
        
        @Override
        public ChannelPromise setFailure(final Throwable cause) {
            throw new IllegalStateException();
        }
        
        @Override
        public boolean trySuccess() {
            throw new IllegalStateException();
        }
        
        @Override
        public boolean tryFailure(final Throwable cause) {
            throw new IllegalStateException();
        }
        
        boolean setClosed() {
            return super.trySuccess();
        }
    }
    
    private static final class AnnotatedConnectException extends ConnectException
    {
        private static final long serialVersionUID = 3901958112696433556L;
        
        AnnotatedConnectException(final ConnectException exception, final SocketAddress remoteAddress) {
            super(exception.getMessage() + ": " + remoteAddress);
            this.initCause(exception);
            this.setStackTrace(exception.getStackTrace());
        }
        
        @Override
        public Throwable fillInStackTrace() {
            return this;
        }
    }
    
    private static final class AnnotatedNoRouteToHostException extends NoRouteToHostException
    {
        private static final long serialVersionUID = -6801433937592080623L;
        
        AnnotatedNoRouteToHostException(final NoRouteToHostException exception, final SocketAddress remoteAddress) {
            super(exception.getMessage() + ": " + remoteAddress);
            this.initCause(exception);
            this.setStackTrace(exception.getStackTrace());
        }
        
        @Override
        public Throwable fillInStackTrace() {
            return this;
        }
    }
    
    private static final class AnnotatedSocketException extends SocketException
    {
        private static final long serialVersionUID = 3896743275010454039L;
        
        AnnotatedSocketException(final SocketException exception, final SocketAddress remoteAddress) {
            super(exception.getMessage() + ": " + remoteAddress);
            this.initCause(exception);
            this.setStackTrace(exception.getStackTrace());
        }
        
        @Override
        public Throwable fillInStackTrace() {
            return this;
        }
    }
}
