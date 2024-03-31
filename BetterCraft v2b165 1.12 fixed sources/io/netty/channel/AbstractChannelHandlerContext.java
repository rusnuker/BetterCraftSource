// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.Recycler;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.internal.StringUtil;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.PromiseNotificationUtil;
import io.netty.util.ReferenceCountUtil;
import java.net.SocketAddress;
import io.netty.util.internal.ThrowableUtil;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.concurrent.OrderedEventExecutor;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.concurrent.EventExecutor;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.ResourceLeakHint;
import io.netty.util.DefaultAttributeMap;

abstract class AbstractChannelHandlerContext extends DefaultAttributeMap implements ChannelHandlerContext, ResourceLeakHint
{
    private static final InternalLogger logger;
    volatile AbstractChannelHandlerContext next;
    volatile AbstractChannelHandlerContext prev;
    private static final AtomicIntegerFieldUpdater<AbstractChannelHandlerContext> HANDLER_STATE_UPDATER;
    private static final int ADD_PENDING = 1;
    private static final int ADD_COMPLETE = 2;
    private static final int REMOVE_COMPLETE = 3;
    private static final int INIT = 0;
    private final boolean inbound;
    private final boolean outbound;
    private final DefaultChannelPipeline pipeline;
    private final String name;
    private final boolean ordered;
    final EventExecutor executor;
    private ChannelFuture succeededFuture;
    private Runnable invokeChannelReadCompleteTask;
    private Runnable invokeReadTask;
    private Runnable invokeChannelWritableStateChangedTask;
    private Runnable invokeFlushTask;
    private volatile int handlerState;
    
    AbstractChannelHandlerContext(final DefaultChannelPipeline pipeline, final EventExecutor executor, final String name, final boolean inbound, final boolean outbound) {
        this.handlerState = 0;
        this.name = ObjectUtil.checkNotNull(name, "name");
        this.pipeline = pipeline;
        this.executor = executor;
        this.inbound = inbound;
        this.outbound = outbound;
        this.ordered = (executor == null || executor instanceof OrderedEventExecutor);
    }
    
    @Override
    public Channel channel() {
        return this.pipeline.channel();
    }
    
    @Override
    public ChannelPipeline pipeline() {
        return this.pipeline;
    }
    
    @Override
    public ByteBufAllocator alloc() {
        return this.channel().config().getAllocator();
    }
    
    @Override
    public EventExecutor executor() {
        if (this.executor == null) {
            return this.channel().eventLoop();
        }
        return this.executor;
    }
    
    @Override
    public String name() {
        return this.name;
    }
    
    @Override
    public ChannelHandlerContext fireChannelRegistered() {
        invokeChannelRegistered(this.findContextInbound());
        return this;
    }
    
    static void invokeChannelRegistered(final AbstractChannelHandlerContext next) {
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelRegistered();
        }
        else {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    AbstractChannelHandlerContext.this.invokeChannelRegistered();
                }
            });
        }
    }
    
    private void invokeChannelRegistered() {
        if (this.invokeHandler()) {
            try {
                ((ChannelInboundHandler)this.handler()).channelRegistered(this);
            }
            catch (final Throwable t) {
                this.notifyHandlerException(t);
            }
        }
        else {
            this.fireChannelRegistered();
        }
    }
    
    @Override
    public ChannelHandlerContext fireChannelUnregistered() {
        invokeChannelUnregistered(this.findContextInbound());
        return this;
    }
    
    static void invokeChannelUnregistered(final AbstractChannelHandlerContext next) {
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelUnregistered();
        }
        else {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    AbstractChannelHandlerContext.this.invokeChannelUnregistered();
                }
            });
        }
    }
    
    private void invokeChannelUnregistered() {
        if (this.invokeHandler()) {
            try {
                ((ChannelInboundHandler)this.handler()).channelUnregistered(this);
            }
            catch (final Throwable t) {
                this.notifyHandlerException(t);
            }
        }
        else {
            this.fireChannelUnregistered();
        }
    }
    
    @Override
    public ChannelHandlerContext fireChannelActive() {
        invokeChannelActive(this.findContextInbound());
        return this;
    }
    
    static void invokeChannelActive(final AbstractChannelHandlerContext next) {
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelActive();
        }
        else {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    AbstractChannelHandlerContext.this.invokeChannelActive();
                }
            });
        }
    }
    
    private void invokeChannelActive() {
        if (this.invokeHandler()) {
            try {
                ((ChannelInboundHandler)this.handler()).channelActive(this);
            }
            catch (final Throwable t) {
                this.notifyHandlerException(t);
            }
        }
        else {
            this.fireChannelActive();
        }
    }
    
    @Override
    public ChannelHandlerContext fireChannelInactive() {
        invokeChannelInactive(this.findContextInbound());
        return this;
    }
    
    static void invokeChannelInactive(final AbstractChannelHandlerContext next) {
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelInactive();
        }
        else {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    AbstractChannelHandlerContext.this.invokeChannelInactive();
                }
            });
        }
    }
    
    private void invokeChannelInactive() {
        if (this.invokeHandler()) {
            try {
                ((ChannelInboundHandler)this.handler()).channelInactive(this);
            }
            catch (final Throwable t) {
                this.notifyHandlerException(t);
            }
        }
        else {
            this.fireChannelInactive();
        }
    }
    
    @Override
    public ChannelHandlerContext fireExceptionCaught(final Throwable cause) {
        invokeExceptionCaught(this.next, cause);
        return this;
    }
    
    static void invokeExceptionCaught(final AbstractChannelHandlerContext next, final Throwable cause) {
        ObjectUtil.checkNotNull(cause, "cause");
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeExceptionCaught(cause);
        }
        else {
            try {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        AbstractChannelHandlerContext.this.invokeExceptionCaught(cause);
                    }
                });
            }
            catch (final Throwable t) {
                if (AbstractChannelHandlerContext.logger.isWarnEnabled()) {
                    AbstractChannelHandlerContext.logger.warn("Failed to submit an exceptionCaught() event.", t);
                    AbstractChannelHandlerContext.logger.warn("The exceptionCaught() event that was failed to submit was:", cause);
                }
            }
        }
    }
    
    private void invokeExceptionCaught(final Throwable cause) {
        if (this.invokeHandler()) {
            try {
                this.handler().exceptionCaught(this, cause);
            }
            catch (final Throwable error) {
                if (AbstractChannelHandlerContext.logger.isDebugEnabled()) {
                    AbstractChannelHandlerContext.logger.debug("An exception {}was thrown by a user handler's exceptionCaught() method while handling the following exception:", ThrowableUtil.stackTraceToString(error), cause);
                }
                else if (AbstractChannelHandlerContext.logger.isWarnEnabled()) {
                    AbstractChannelHandlerContext.logger.warn("An exception '{}' [enable DEBUG level for full stacktrace] was thrown by a user handler's exceptionCaught() method while handling the following exception:", error, cause);
                }
            }
        }
        else {
            this.fireExceptionCaught(cause);
        }
    }
    
    @Override
    public ChannelHandlerContext fireUserEventTriggered(final Object event) {
        invokeUserEventTriggered(this.findContextInbound(), event);
        return this;
    }
    
    static void invokeUserEventTriggered(final AbstractChannelHandlerContext next, final Object event) {
        ObjectUtil.checkNotNull(event, "event");
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeUserEventTriggered(event);
        }
        else {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    AbstractChannelHandlerContext.this.invokeUserEventTriggered(event);
                }
            });
        }
    }
    
    private void invokeUserEventTriggered(final Object event) {
        if (this.invokeHandler()) {
            try {
                ((ChannelInboundHandler)this.handler()).userEventTriggered(this, event);
            }
            catch (final Throwable t) {
                this.notifyHandlerException(t);
            }
        }
        else {
            this.fireUserEventTriggered(event);
        }
    }
    
    @Override
    public ChannelHandlerContext fireChannelRead(final Object msg) {
        invokeChannelRead(this.findContextInbound(), msg);
        return this;
    }
    
    static void invokeChannelRead(final AbstractChannelHandlerContext next, final Object msg) {
        final Object m = next.pipeline.touch(ObjectUtil.checkNotNull(msg, "msg"), next);
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelRead(m);
        }
        else {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    AbstractChannelHandlerContext.this.invokeChannelRead(m);
                }
            });
        }
    }
    
    private void invokeChannelRead(final Object msg) {
        if (this.invokeHandler()) {
            try {
                ((ChannelInboundHandler)this.handler()).channelRead(this, msg);
            }
            catch (final Throwable t) {
                this.notifyHandlerException(t);
            }
        }
        else {
            this.fireChannelRead(msg);
        }
    }
    
    @Override
    public ChannelHandlerContext fireChannelReadComplete() {
        invokeChannelReadComplete(this.findContextInbound());
        return this;
    }
    
    static void invokeChannelReadComplete(final AbstractChannelHandlerContext next) {
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelReadComplete();
        }
        else {
            Runnable task = next.invokeChannelReadCompleteTask;
            if (task == null) {
                task = (next.invokeChannelReadCompleteTask = new Runnable() {
                    @Override
                    public void run() {
                        AbstractChannelHandlerContext.this.invokeChannelReadComplete();
                    }
                });
            }
            executor.execute(task);
        }
    }
    
    private void invokeChannelReadComplete() {
        if (this.invokeHandler()) {
            try {
                ((ChannelInboundHandler)this.handler()).channelReadComplete(this);
            }
            catch (final Throwable t) {
                this.notifyHandlerException(t);
            }
        }
        else {
            this.fireChannelReadComplete();
        }
    }
    
    @Override
    public ChannelHandlerContext fireChannelWritabilityChanged() {
        invokeChannelWritabilityChanged(this.findContextInbound());
        return this;
    }
    
    static void invokeChannelWritabilityChanged(final AbstractChannelHandlerContext next) {
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelWritabilityChanged();
        }
        else {
            Runnable task = next.invokeChannelWritableStateChangedTask;
            if (task == null) {
                task = (next.invokeChannelWritableStateChangedTask = new Runnable() {
                    @Override
                    public void run() {
                        AbstractChannelHandlerContext.this.invokeChannelWritabilityChanged();
                    }
                });
            }
            executor.execute(task);
        }
    }
    
    private void invokeChannelWritabilityChanged() {
        if (this.invokeHandler()) {
            try {
                ((ChannelInboundHandler)this.handler()).channelWritabilityChanged(this);
            }
            catch (final Throwable t) {
                this.notifyHandlerException(t);
            }
        }
        else {
            this.fireChannelWritabilityChanged();
        }
    }
    
    @Override
    public ChannelFuture bind(final SocketAddress localAddress) {
        return this.bind(localAddress, this.newPromise());
    }
    
    @Override
    public ChannelFuture connect(final SocketAddress remoteAddress) {
        return this.connect(remoteAddress, this.newPromise());
    }
    
    @Override
    public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress) {
        return this.connect(remoteAddress, localAddress, this.newPromise());
    }
    
    @Override
    public ChannelFuture disconnect() {
        return this.disconnect(this.newPromise());
    }
    
    @Override
    public ChannelFuture close() {
        return this.close(this.newPromise());
    }
    
    @Override
    public ChannelFuture deregister() {
        return this.deregister(this.newPromise());
    }
    
    @Override
    public ChannelFuture bind(final SocketAddress localAddress, final ChannelPromise promise) {
        if (localAddress == null) {
            throw new NullPointerException("localAddress");
        }
        if (this.isNotValidPromise(promise, false)) {
            return promise;
        }
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeBind(localAddress, promise);
        }
        else {
            safeExecute(executor, new Runnable() {
                @Override
                public void run() {
                    AbstractChannelHandlerContext.this.invokeBind(localAddress, promise);
                }
            }, promise, null);
        }
        return promise;
    }
    
    private void invokeBind(final SocketAddress localAddress, final ChannelPromise promise) {
        if (this.invokeHandler()) {
            try {
                ((ChannelOutboundHandler)this.handler()).bind(this, localAddress, promise);
            }
            catch (final Throwable t) {
                notifyOutboundHandlerException(t, promise);
            }
        }
        else {
            this.bind(localAddress, promise);
        }
    }
    
    @Override
    public ChannelFuture connect(final SocketAddress remoteAddress, final ChannelPromise promise) {
        return this.connect(remoteAddress, null, promise);
    }
    
    @Override
    public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
        if (remoteAddress == null) {
            throw new NullPointerException("remoteAddress");
        }
        if (this.isNotValidPromise(promise, false)) {
            return promise;
        }
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeConnect(remoteAddress, localAddress, promise);
        }
        else {
            safeExecute(executor, new Runnable() {
                @Override
                public void run() {
                    AbstractChannelHandlerContext.this.invokeConnect(remoteAddress, localAddress, promise);
                }
            }, promise, null);
        }
        return promise;
    }
    
    private void invokeConnect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
        if (this.invokeHandler()) {
            try {
                ((ChannelOutboundHandler)this.handler()).connect(this, remoteAddress, localAddress, promise);
            }
            catch (final Throwable t) {
                notifyOutboundHandlerException(t, promise);
            }
        }
        else {
            this.connect(remoteAddress, localAddress, promise);
        }
    }
    
    @Override
    public ChannelFuture disconnect(final ChannelPromise promise) {
        if (this.isNotValidPromise(promise, false)) {
            return promise;
        }
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            if (!this.channel().metadata().hasDisconnect()) {
                next.invokeClose(promise);
            }
            else {
                next.invokeDisconnect(promise);
            }
        }
        else {
            safeExecute(executor, new Runnable() {
                @Override
                public void run() {
                    if (!AbstractChannelHandlerContext.this.channel().metadata().hasDisconnect()) {
                        AbstractChannelHandlerContext.this.invokeClose(promise);
                    }
                    else {
                        AbstractChannelHandlerContext.this.invokeDisconnect(promise);
                    }
                }
            }, promise, null);
        }
        return promise;
    }
    
    private void invokeDisconnect(final ChannelPromise promise) {
        if (this.invokeHandler()) {
            try {
                ((ChannelOutboundHandler)this.handler()).disconnect(this, promise);
            }
            catch (final Throwable t) {
                notifyOutboundHandlerException(t, promise);
            }
        }
        else {
            this.disconnect(promise);
        }
    }
    
    @Override
    public ChannelFuture close(final ChannelPromise promise) {
        if (this.isNotValidPromise(promise, false)) {
            return promise;
        }
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeClose(promise);
        }
        else {
            safeExecute(executor, new Runnable() {
                @Override
                public void run() {
                    AbstractChannelHandlerContext.this.invokeClose(promise);
                }
            }, promise, null);
        }
        return promise;
    }
    
    private void invokeClose(final ChannelPromise promise) {
        if (this.invokeHandler()) {
            try {
                ((ChannelOutboundHandler)this.handler()).close(this, promise);
            }
            catch (final Throwable t) {
                notifyOutboundHandlerException(t, promise);
            }
        }
        else {
            this.close(promise);
        }
    }
    
    @Override
    public ChannelFuture deregister(final ChannelPromise promise) {
        if (this.isNotValidPromise(promise, false)) {
            return promise;
        }
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeDeregister(promise);
        }
        else {
            safeExecute(executor, new Runnable() {
                @Override
                public void run() {
                    AbstractChannelHandlerContext.this.invokeDeregister(promise);
                }
            }, promise, null);
        }
        return promise;
    }
    
    private void invokeDeregister(final ChannelPromise promise) {
        if (this.invokeHandler()) {
            try {
                ((ChannelOutboundHandler)this.handler()).deregister(this, promise);
            }
            catch (final Throwable t) {
                notifyOutboundHandlerException(t, promise);
            }
        }
        else {
            this.deregister(promise);
        }
    }
    
    @Override
    public ChannelHandlerContext read() {
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeRead();
        }
        else {
            Runnable task = next.invokeReadTask;
            if (task == null) {
                task = (next.invokeReadTask = new Runnable() {
                    @Override
                    public void run() {
                        AbstractChannelHandlerContext.this.invokeRead();
                    }
                });
            }
            executor.execute(task);
        }
        return this;
    }
    
    private void invokeRead() {
        if (this.invokeHandler()) {
            try {
                ((ChannelOutboundHandler)this.handler()).read(this);
            }
            catch (final Throwable t) {
                this.notifyHandlerException(t);
            }
        }
        else {
            this.read();
        }
    }
    
    @Override
    public ChannelFuture write(final Object msg) {
        return this.write(msg, this.newPromise());
    }
    
    @Override
    public ChannelFuture write(final Object msg, final ChannelPromise promise) {
        if (msg == null) {
            throw new NullPointerException("msg");
        }
        try {
            if (this.isNotValidPromise(promise, true)) {
                ReferenceCountUtil.release(msg);
                return promise;
            }
        }
        catch (final RuntimeException e) {
            ReferenceCountUtil.release(msg);
            throw e;
        }
        this.write(msg, false, promise);
        return promise;
    }
    
    private void invokeWrite(final Object msg, final ChannelPromise promise) {
        if (this.invokeHandler()) {
            this.invokeWrite0(msg, promise);
        }
        else {
            this.write(msg, promise);
        }
    }
    
    private void invokeWrite0(final Object msg, final ChannelPromise promise) {
        try {
            ((ChannelOutboundHandler)this.handler()).write(this, msg, promise);
        }
        catch (final Throwable t) {
            notifyOutboundHandlerException(t, promise);
        }
    }
    
    @Override
    public ChannelHandlerContext flush() {
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeFlush();
        }
        else {
            Runnable task = next.invokeFlushTask;
            if (task == null) {
                task = (next.invokeFlushTask = new Runnable() {
                    @Override
                    public void run() {
                        AbstractChannelHandlerContext.this.invokeFlush();
                    }
                });
            }
            safeExecute(executor, task, this.channel().voidPromise(), null);
        }
        return this;
    }
    
    private void invokeFlush() {
        if (this.invokeHandler()) {
            this.invokeFlush0();
        }
        else {
            this.flush();
        }
    }
    
    private void invokeFlush0() {
        try {
            ((ChannelOutboundHandler)this.handler()).flush(this);
        }
        catch (final Throwable t) {
            this.notifyHandlerException(t);
        }
    }
    
    @Override
    public ChannelFuture writeAndFlush(final Object msg, final ChannelPromise promise) {
        if (msg == null) {
            throw new NullPointerException("msg");
        }
        if (this.isNotValidPromise(promise, true)) {
            ReferenceCountUtil.release(msg);
            return promise;
        }
        this.write(msg, true, promise);
        return promise;
    }
    
    private void invokeWriteAndFlush(final Object msg, final ChannelPromise promise) {
        if (this.invokeHandler()) {
            this.invokeWrite0(msg, promise);
            this.invokeFlush0();
        }
        else {
            this.writeAndFlush(msg, promise);
        }
    }
    
    private void write(final Object msg, final boolean flush, final ChannelPromise promise) {
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final Object m = this.pipeline.touch(msg, next);
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            if (flush) {
                next.invokeWriteAndFlush(m, promise);
            }
            else {
                next.invokeWrite(m, promise);
            }
        }
        else {
            AbstractWriteTask task;
            if (flush) {
                task = newInstance(next, m, promise);
            }
            else {
                task = newInstance(next, m, promise);
            }
            safeExecute(executor, task, promise, m);
        }
    }
    
    @Override
    public ChannelFuture writeAndFlush(final Object msg) {
        return this.writeAndFlush(msg, this.newPromise());
    }
    
    private static void notifyOutboundHandlerException(final Throwable cause, final ChannelPromise promise) {
        if (!(promise instanceof VoidChannelPromise)) {
            PromiseNotificationUtil.tryFailure(promise, cause, AbstractChannelHandlerContext.logger);
        }
    }
    
    private void notifyHandlerException(final Throwable cause) {
        if (inExceptionCaught(cause)) {
            if (AbstractChannelHandlerContext.logger.isWarnEnabled()) {
                AbstractChannelHandlerContext.logger.warn("An exception was thrown by a user handler while handling an exceptionCaught event", cause);
            }
            return;
        }
        this.invokeExceptionCaught(cause);
    }
    
    private static boolean inExceptionCaught(Throwable cause) {
        do {
            final StackTraceElement[] trace = cause.getStackTrace();
            if (trace != null) {
                for (final StackTraceElement t : trace) {
                    if (t == null) {
                        break;
                    }
                    if ("exceptionCaught".equals(t.getMethodName())) {
                        return true;
                    }
                }
            }
            cause = cause.getCause();
        } while (cause != null);
        return false;
    }
    
    @Override
    public ChannelPromise newPromise() {
        return new DefaultChannelPromise(this.channel(), this.executor());
    }
    
    @Override
    public ChannelProgressivePromise newProgressivePromise() {
        return new DefaultChannelProgressivePromise(this.channel(), this.executor());
    }
    
    @Override
    public ChannelFuture newSucceededFuture() {
        ChannelFuture succeededFuture = this.succeededFuture;
        if (succeededFuture == null) {
            succeededFuture = (this.succeededFuture = new SucceededChannelFuture(this.channel(), this.executor()));
        }
        return succeededFuture;
    }
    
    @Override
    public ChannelFuture newFailedFuture(final Throwable cause) {
        return new FailedChannelFuture(this.channel(), this.executor(), cause);
    }
    
    private boolean isNotValidPromise(final ChannelPromise promise, final boolean allowVoidPromise) {
        if (promise == null) {
            throw new NullPointerException("promise");
        }
        if (promise.isDone()) {
            if (promise.isCancelled()) {
                return true;
            }
            throw new IllegalArgumentException("promise already done: " + promise);
        }
        else {
            if (promise.channel() != this.channel()) {
                throw new IllegalArgumentException(String.format("promise.channel does not match: %s (expected: %s)", promise.channel(), this.channel()));
            }
            if (promise.getClass() == DefaultChannelPromise.class) {
                return false;
            }
            if (!allowVoidPromise && promise instanceof VoidChannelPromise) {
                throw new IllegalArgumentException(StringUtil.simpleClassName(VoidChannelPromise.class) + " not allowed for this operation");
            }
            if (promise instanceof AbstractChannel.CloseFuture) {
                throw new IllegalArgumentException(StringUtil.simpleClassName(AbstractChannel.CloseFuture.class) + " not allowed in a pipeline");
            }
            return false;
        }
    }
    
    private AbstractChannelHandlerContext findContextInbound() {
        AbstractChannelHandlerContext ctx = this;
        do {
            ctx = ctx.next;
        } while (!ctx.inbound);
        return ctx;
    }
    
    private AbstractChannelHandlerContext findContextOutbound() {
        AbstractChannelHandlerContext ctx = this;
        do {
            ctx = ctx.prev;
        } while (!ctx.outbound);
        return ctx;
    }
    
    @Override
    public ChannelPromise voidPromise() {
        return this.channel().voidPromise();
    }
    
    final void setRemoved() {
        this.handlerState = 3;
    }
    
    final void setAddComplete() {
        int oldState;
        do {
            oldState = this.handlerState;
        } while (oldState != 3 && !AbstractChannelHandlerContext.HANDLER_STATE_UPDATER.compareAndSet(this, oldState, 2));
    }
    
    final void setAddPending() {
        final boolean updated = AbstractChannelHandlerContext.HANDLER_STATE_UPDATER.compareAndSet(this, 0, 1);
        assert updated;
    }
    
    private boolean invokeHandler() {
        final int handlerState = this.handlerState;
        return handlerState == 2 || (!this.ordered && handlerState == 1);
    }
    
    @Override
    public boolean isRemoved() {
        return this.handlerState == 3;
    }
    
    @Override
    public <T> Attribute<T> attr(final AttributeKey<T> key) {
        return this.channel().attr(key);
    }
    
    @Override
    public <T> boolean hasAttr(final AttributeKey<T> key) {
        return this.channel().hasAttr(key);
    }
    
    private static void safeExecute(final EventExecutor executor, final Runnable runnable, final ChannelPromise promise, final Object msg) {
        try {
            executor.execute(runnable);
        }
        catch (final Throwable cause) {
            try {
                promise.setFailure(cause);
            }
            finally {
                if (msg != null) {
                    ReferenceCountUtil.release(msg);
                }
            }
        }
    }
    
    @Override
    public String toHintString() {
        return '\'' + this.name + "' will handle the message from this point.";
    }
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(ChannelHandlerContext.class) + '(' + this.name + ", " + this.channel() + ')';
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(AbstractChannelHandlerContext.class);
        HANDLER_STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(AbstractChannelHandlerContext.class, "handlerState");
    }
    
    abstract static class AbstractWriteTask implements Runnable
    {
        private static final boolean ESTIMATE_TASK_SIZE_ON_SUBMIT;
        private static final int WRITE_TASK_OVERHEAD;
        private final Recycler.Handle<AbstractWriteTask> handle;
        private AbstractChannelHandlerContext ctx;
        private Object msg;
        private ChannelPromise promise;
        private int size;
        
        private AbstractWriteTask(final Recycler.Handle<? extends AbstractWriteTask> handle) {
            this.handle = (Recycler.Handle<AbstractWriteTask>)handle;
        }
        
        protected static void init(final AbstractWriteTask task, final AbstractChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) {
            task.ctx = ctx;
            task.msg = msg;
            task.promise = promise;
            if (AbstractWriteTask.ESTIMATE_TASK_SIZE_ON_SUBMIT) {
                final ChannelOutboundBuffer buffer = ctx.channel().unsafe().outboundBuffer();
                if (buffer != null) {
                    task.size = ctx.pipeline.estimatorHandle().size(msg) + AbstractWriteTask.WRITE_TASK_OVERHEAD;
                    buffer.incrementPendingOutboundBytes(task.size);
                }
                else {
                    task.size = 0;
                }
            }
            else {
                task.size = 0;
            }
        }
        
        @Override
        public final void run() {
            try {
                final ChannelOutboundBuffer buffer = this.ctx.channel().unsafe().outboundBuffer();
                if (AbstractWriteTask.ESTIMATE_TASK_SIZE_ON_SUBMIT && buffer != null) {
                    buffer.decrementPendingOutboundBytes(this.size);
                }
                this.write(this.ctx, this.msg, this.promise);
            }
            finally {
                this.ctx = null;
                this.msg = null;
                this.promise = null;
                this.handle.recycle(this);
            }
        }
        
        protected void write(final AbstractChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) {
            ctx.invokeWrite(msg, promise);
        }
        
        static {
            ESTIMATE_TASK_SIZE_ON_SUBMIT = SystemPropertyUtil.getBoolean("io.netty.transport.estimateSizeOnSubmit", true);
            WRITE_TASK_OVERHEAD = SystemPropertyUtil.getInt("io.netty.transport.writeTaskSizeOverhead", 48);
        }
    }
    
    static final class WriteTask extends AbstractWriteTask implements SingleThreadEventLoop.NonWakeupRunnable
    {
        private static final Recycler<WriteTask> RECYCLER;
        
        private static WriteTask newInstance(final AbstractChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) {
            final WriteTask task = WriteTask.RECYCLER.get();
            AbstractWriteTask.init(task, ctx, msg, promise);
            return task;
        }
        
        private WriteTask(final Recycler.Handle<WriteTask> handle) {
            super((Recycler.Handle)handle);
        }
        
        static {
            RECYCLER = new Recycler<WriteTask>() {
                @Override
                protected WriteTask newObject(final Handle<WriteTask> handle) {
                    return new WriteTask((Handle)handle);
                }
            };
        }
    }
    
    static final class WriteAndFlushTask extends AbstractWriteTask
    {
        private static final Recycler<WriteAndFlushTask> RECYCLER;
        
        private static WriteAndFlushTask newInstance(final AbstractChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) {
            final WriteAndFlushTask task = WriteAndFlushTask.RECYCLER.get();
            AbstractWriteTask.init(task, ctx, msg, promise);
            return task;
        }
        
        private WriteAndFlushTask(final Recycler.Handle<WriteAndFlushTask> handle) {
            super((Recycler.Handle)handle);
        }
        
        public void write(final AbstractChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) {
            super.write(ctx, msg, promise);
            ctx.invokeFlush();
        }
        
        static {
            RECYCLER = new Recycler<WriteAndFlushTask>() {
                @Override
                protected WriteAndFlushTask newObject(final Handle<WriteAndFlushTask> handle) {
                    return new WriteAndFlushTask((Handle)handle);
                }
            };
        }
    }
}
