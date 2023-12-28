// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.embedded;

import java.util.ArrayList;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.EventLoop;
import java.nio.channels.ClosedChannelException;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.channel.ChannelPromise;
import java.util.ArrayDeque;
import io.netty.channel.DefaultChannelPipeline;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelInitializer;
import io.netty.util.internal.ObjectUtil;
import io.netty.channel.DefaultChannelConfig;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import java.util.Queue;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelMetadata;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.ChannelHandler;
import java.net.SocketAddress;
import io.netty.channel.AbstractChannel;

public class EmbeddedChannel extends AbstractChannel
{
    private static final SocketAddress LOCAL_ADDRESS;
    private static final SocketAddress REMOTE_ADDRESS;
    private static final ChannelHandler[] EMPTY_HANDLERS;
    private static final InternalLogger logger;
    private static final ChannelMetadata METADATA_NO_DISCONNECT;
    private static final ChannelMetadata METADATA_DISCONNECT;
    private final EmbeddedEventLoop loop;
    private final ChannelFutureListener recordExceptionListener;
    private final ChannelMetadata metadata;
    private final ChannelConfig config;
    private Queue<Object> inboundMessages;
    private Queue<Object> outboundMessages;
    private Throwable lastException;
    private State state;
    
    public EmbeddedChannel() {
        this(EmbeddedChannel.EMPTY_HANDLERS);
    }
    
    public EmbeddedChannel(final ChannelId channelId) {
        this(channelId, EmbeddedChannel.EMPTY_HANDLERS);
    }
    
    public EmbeddedChannel(final ChannelHandler... handlers) {
        this(EmbeddedChannelId.INSTANCE, handlers);
    }
    
    public EmbeddedChannel(final boolean hasDisconnect, final ChannelHandler... handlers) {
        this(EmbeddedChannelId.INSTANCE, hasDisconnect, handlers);
    }
    
    public EmbeddedChannel(final ChannelId channelId, final ChannelHandler... handlers) {
        this(channelId, false, handlers);
    }
    
    public EmbeddedChannel(final ChannelId channelId, final boolean hasDisconnect, final ChannelHandler... handlers) {
        super(null, channelId);
        this.loop = new EmbeddedEventLoop();
        this.recordExceptionListener = new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future) throws Exception {
                EmbeddedChannel.this.recordException(future);
            }
        };
        this.metadata = metadata(hasDisconnect);
        this.config = new DefaultChannelConfig(this);
        this.setup(handlers);
    }
    
    public EmbeddedChannel(final ChannelId channelId, final boolean hasDisconnect, final ChannelConfig config, final ChannelHandler... handlers) {
        super(null, channelId);
        this.loop = new EmbeddedEventLoop();
        this.recordExceptionListener = new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future) throws Exception {
                EmbeddedChannel.this.recordException(future);
            }
        };
        this.metadata = metadata(hasDisconnect);
        this.config = ObjectUtil.checkNotNull(config, "config");
        this.setup(handlers);
    }
    
    private static ChannelMetadata metadata(final boolean hasDisconnect) {
        return hasDisconnect ? EmbeddedChannel.METADATA_DISCONNECT : EmbeddedChannel.METADATA_NO_DISCONNECT;
    }
    
    private void setup(final ChannelHandler... handlers) {
        ObjectUtil.checkNotNull(handlers, "handlers");
        final ChannelPipeline p = this.pipeline();
        p.addLast(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(final Channel ch) throws Exception {
                final ChannelPipeline pipeline = ch.pipeline();
                for (final ChannelHandler h : handlers) {
                    if (h == null) {
                        break;
                    }
                    pipeline.addLast(h);
                }
            }
        });
        final ChannelFuture future = this.loop.register(this);
        assert future.isDone();
    }
    
    @Override
    protected final DefaultChannelPipeline newChannelPipeline() {
        return new EmbeddedChannelPipeline(this);
    }
    
    @Override
    public ChannelMetadata metadata() {
        return this.metadata;
    }
    
    @Override
    public ChannelConfig config() {
        return this.config;
    }
    
    @Override
    public boolean isOpen() {
        return this.state != State.CLOSED;
    }
    
    @Override
    public boolean isActive() {
        return this.state == State.ACTIVE;
    }
    
    public Queue<Object> inboundMessages() {
        if (this.inboundMessages == null) {
            this.inboundMessages = new ArrayDeque<Object>();
        }
        return this.inboundMessages;
    }
    
    @Deprecated
    public Queue<Object> lastInboundBuffer() {
        return this.inboundMessages();
    }
    
    public Queue<Object> outboundMessages() {
        if (this.outboundMessages == null) {
            this.outboundMessages = new ArrayDeque<Object>();
        }
        return this.outboundMessages;
    }
    
    @Deprecated
    public Queue<Object> lastOutboundBuffer() {
        return this.outboundMessages();
    }
    
    public <T> T readInbound() {
        return (T)poll(this.inboundMessages);
    }
    
    public <T> T readOutbound() {
        return (T)poll(this.outboundMessages);
    }
    
    public boolean writeInbound(final Object... msgs) {
        this.ensureOpen();
        if (msgs.length == 0) {
            return isNotEmpty(this.inboundMessages);
        }
        final ChannelPipeline p = this.pipeline();
        for (final Object m : msgs) {
            p.fireChannelRead(m);
        }
        this.flushInbound(false, this.voidPromise());
        return isNotEmpty(this.inboundMessages);
    }
    
    public ChannelFuture writeOneInbound(final Object msg) {
        return this.writeOneInbound(msg, this.newPromise());
    }
    
    public ChannelFuture writeOneInbound(final Object msg, final ChannelPromise promise) {
        if (this.checkOpen(true)) {
            this.pipeline().fireChannelRead(msg);
        }
        return this.checkException(promise);
    }
    
    public EmbeddedChannel flushInbound() {
        this.flushInbound(true, this.voidPromise());
        return this;
    }
    
    private ChannelFuture flushInbound(final boolean recordException, final ChannelPromise promise) {
        if (this.checkOpen(recordException)) {
            this.pipeline().fireChannelReadComplete();
            this.runPendingTasks();
        }
        return this.checkException(promise);
    }
    
    public boolean writeOutbound(final Object... msgs) {
        this.ensureOpen();
        if (msgs.length == 0) {
            return isNotEmpty(this.outboundMessages);
        }
        final RecyclableArrayList futures = RecyclableArrayList.newInstance(msgs.length);
        try {
            for (final Object m : msgs) {
                if (m == null) {
                    break;
                }
                futures.add(this.write(m));
            }
            this.flushOutbound0();
            for (int size = futures.size(), i = 0; i < size; ++i) {
                final ChannelFuture future = ((ArrayList<ChannelFuture>)futures).get(i);
                if (future.isDone()) {
                    this.recordException(future);
                }
                else {
                    future.addListener((GenericFutureListener<? extends Future<? super Void>>)this.recordExceptionListener);
                }
            }
            this.checkException();
            return isNotEmpty(this.outboundMessages);
        }
        finally {
            futures.recycle();
        }
    }
    
    public ChannelFuture writeOneOutbound(final Object msg) {
        return this.writeOneOutbound(msg, this.newPromise());
    }
    
    public ChannelFuture writeOneOutbound(final Object msg, final ChannelPromise promise) {
        if (this.checkOpen(true)) {
            return this.write(msg, promise);
        }
        return this.checkException(promise);
    }
    
    public EmbeddedChannel flushOutbound() {
        if (this.checkOpen(true)) {
            this.flushOutbound0();
        }
        this.checkException(this.voidPromise());
        return this;
    }
    
    private void flushOutbound0() {
        this.runPendingTasks();
        this.flush();
    }
    
    public boolean finish() {
        return this.finish(false);
    }
    
    public boolean finishAndReleaseAll() {
        return this.finish(true);
    }
    
    private boolean finish(final boolean releaseAll) {
        this.close();
        try {
            this.checkException();
            return isNotEmpty(this.inboundMessages) || isNotEmpty(this.outboundMessages);
        }
        finally {
            if (releaseAll) {
                releaseAll(this.inboundMessages);
                releaseAll(this.outboundMessages);
            }
        }
    }
    
    public boolean releaseInbound() {
        return releaseAll(this.inboundMessages);
    }
    
    public boolean releaseOutbound() {
        return releaseAll(this.outboundMessages);
    }
    
    private static boolean releaseAll(final Queue<Object> queue) {
        if (isNotEmpty(queue)) {
            while (true) {
                final Object msg = queue.poll();
                if (msg == null) {
                    break;
                }
                ReferenceCountUtil.release(msg);
            }
            return true;
        }
        return false;
    }
    
    private void finishPendingTasks(final boolean cancel) {
        this.runPendingTasks();
        if (cancel) {
            this.loop.cancelScheduledTasks();
        }
    }
    
    @Override
    public final ChannelFuture close() {
        return this.close(this.newPromise());
    }
    
    @Override
    public final ChannelFuture disconnect() {
        return this.disconnect(this.newPromise());
    }
    
    @Override
    public final ChannelFuture close(final ChannelPromise promise) {
        this.runPendingTasks();
        final ChannelFuture future = super.close(promise);
        this.finishPendingTasks(true);
        return future;
    }
    
    @Override
    public final ChannelFuture disconnect(final ChannelPromise promise) {
        final ChannelFuture future = super.disconnect(promise);
        this.finishPendingTasks(!this.metadata.hasDisconnect());
        return future;
    }
    
    private static boolean isNotEmpty(final Queue<Object> queue) {
        return queue != null && !queue.isEmpty();
    }
    
    private static Object poll(final Queue<Object> queue) {
        return (queue != null) ? queue.poll() : null;
    }
    
    public void runPendingTasks() {
        try {
            this.loop.runTasks();
        }
        catch (final Exception e) {
            this.recordException(e);
        }
        try {
            this.loop.runScheduledTasks();
        }
        catch (final Exception e) {
            this.recordException(e);
        }
    }
    
    public long runScheduledPendingTasks() {
        try {
            return this.loop.runScheduledTasks();
        }
        catch (final Exception e) {
            this.recordException(e);
            return this.loop.nextScheduledTask();
        }
    }
    
    private void recordException(final ChannelFuture future) {
        if (!future.isSuccess()) {
            this.recordException(future.cause());
        }
    }
    
    private void recordException(final Throwable cause) {
        if (this.lastException == null) {
            this.lastException = cause;
        }
        else {
            EmbeddedChannel.logger.warn("More than one exception was raised. Will report only the first one and log others.", cause);
        }
    }
    
    private ChannelFuture checkException(final ChannelPromise promise) {
        final Throwable t = this.lastException;
        if (t != null) {
            this.lastException = null;
            if (promise.isVoid()) {
                PlatformDependent.throwException(t);
            }
            return promise.setFailure(t);
        }
        return promise.setSuccess();
    }
    
    public void checkException() {
        this.checkException(this.voidPromise());
    }
    
    private boolean checkOpen(final boolean recordException) {
        if (!this.isOpen()) {
            if (recordException) {
                this.recordException(new ClosedChannelException());
            }
            return false;
        }
        return true;
    }
    
    protected final void ensureOpen() {
        if (!this.checkOpen(true)) {
            this.checkException();
        }
    }
    
    @Override
    protected boolean isCompatible(final EventLoop loop) {
        return loop instanceof EmbeddedEventLoop;
    }
    
    @Override
    protected SocketAddress localAddress0() {
        return this.isActive() ? EmbeddedChannel.LOCAL_ADDRESS : null;
    }
    
    @Override
    protected SocketAddress remoteAddress0() {
        return this.isActive() ? EmbeddedChannel.REMOTE_ADDRESS : null;
    }
    
    @Override
    protected void doRegister() throws Exception {
        this.state = State.ACTIVE;
    }
    
    @Override
    protected void doBind(final SocketAddress localAddress) throws Exception {
    }
    
    @Override
    protected void doDisconnect() throws Exception {
        if (!this.metadata.hasDisconnect()) {
            this.doClose();
        }
    }
    
    @Override
    protected void doClose() throws Exception {
        this.state = State.CLOSED;
    }
    
    @Override
    protected void doBeginRead() throws Exception {
    }
    
    @Override
    protected AbstractUnsafe newUnsafe() {
        return new DefaultUnsafe();
    }
    
    @Override
    protected void doWrite(final ChannelOutboundBuffer in) throws Exception {
        while (true) {
            final Object msg = in.current();
            if (msg == null) {
                break;
            }
            ReferenceCountUtil.retain(msg);
            this.handleOutboundMessage(msg);
            in.remove();
        }
    }
    
    protected void handleOutboundMessage(final Object msg) {
        this.outboundMessages().add(msg);
    }
    
    protected void handleInboundMessage(final Object msg) {
        this.inboundMessages().add(msg);
    }
    
    static {
        LOCAL_ADDRESS = new EmbeddedSocketAddress();
        REMOTE_ADDRESS = new EmbeddedSocketAddress();
        EMPTY_HANDLERS = new ChannelHandler[0];
        logger = InternalLoggerFactory.getInstance(EmbeddedChannel.class);
        METADATA_NO_DISCONNECT = new ChannelMetadata(false);
        METADATA_DISCONNECT = new ChannelMetadata(true);
    }
    
    private enum State
    {
        OPEN, 
        ACTIVE, 
        CLOSED;
    }
    
    private class DefaultUnsafe extends AbstractUnsafe
    {
        @Override
        public void connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
            this.safeSetSuccess(promise);
        }
    }
    
    private final class EmbeddedChannelPipeline extends DefaultChannelPipeline
    {
        public EmbeddedChannelPipeline(final EmbeddedChannel channel) {
            super(channel);
        }
        
        @Override
        protected void onUnhandledInboundException(final Throwable cause) {
            EmbeddedChannel.this.recordException(cause);
        }
        
        @Override
        protected void onUnhandledInboundMessage(final Object msg) {
            EmbeddedChannel.this.handleInboundMessage(msg);
        }
    }
}
