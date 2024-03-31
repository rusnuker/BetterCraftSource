// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.proxy;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.concurrent.TimeUnit;
import java.nio.channels.ConnectionPendingException;
import io.netty.channel.ChannelPromise;
import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.channel.PendingWriteQueue;
import io.netty.channel.ChannelHandlerContext;
import java.net.SocketAddress;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.ChannelDuplexHandler;

public abstract class ProxyHandler extends ChannelDuplexHandler
{
    private static final InternalLogger logger;
    private static final long DEFAULT_CONNECT_TIMEOUT_MILLIS = 10000L;
    static final String AUTH_NONE = "none";
    private final SocketAddress proxyAddress;
    private volatile SocketAddress destinationAddress;
    private volatile long connectTimeoutMillis;
    private volatile ChannelHandlerContext ctx;
    private PendingWriteQueue pendingWrites;
    private boolean finished;
    private boolean suppressChannelReadComplete;
    private boolean flushedPrematurely;
    private final LazyChannelPromise connectPromise;
    private ScheduledFuture<?> connectTimeoutFuture;
    private final ChannelFutureListener writeListener;
    
    protected ProxyHandler(final SocketAddress proxyAddress) {
        this.connectTimeoutMillis = 10000L;
        this.connectPromise = new LazyChannelPromise();
        this.writeListener = new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    ProxyHandler.this.setConnectFailure(future.cause());
                }
            }
        };
        if (proxyAddress == null) {
            throw new NullPointerException("proxyAddress");
        }
        this.proxyAddress = proxyAddress;
    }
    
    public abstract String protocol();
    
    public abstract String authScheme();
    
    public final <T extends SocketAddress> T proxyAddress() {
        return (T)this.proxyAddress;
    }
    
    public final <T extends SocketAddress> T destinationAddress() {
        return (T)this.destinationAddress;
    }
    
    public final boolean isConnected() {
        return this.connectPromise.isSuccess();
    }
    
    public final Future<Channel> connectFuture() {
        return this.connectPromise;
    }
    
    public final long connectTimeoutMillis() {
        return this.connectTimeoutMillis;
    }
    
    public final void setConnectTimeoutMillis(long connectTimeoutMillis) {
        if (connectTimeoutMillis <= 0L) {
            connectTimeoutMillis = 0L;
        }
        this.connectTimeoutMillis = connectTimeoutMillis;
    }
    
    @Override
    public final void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        this.addCodec(this.ctx = ctx);
        if (ctx.channel().isActive()) {
            this.sendInitialMessage(ctx);
        }
    }
    
    protected abstract void addCodec(final ChannelHandlerContext p0) throws Exception;
    
    protected abstract void removeEncoder(final ChannelHandlerContext p0) throws Exception;
    
    protected abstract void removeDecoder(final ChannelHandlerContext p0) throws Exception;
    
    @Override
    public final void connect(final ChannelHandlerContext ctx, final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
        if (this.destinationAddress != null) {
            promise.setFailure((Throwable)new ConnectionPendingException());
            return;
        }
        this.destinationAddress = remoteAddress;
        ctx.connect(this.proxyAddress, localAddress, promise);
    }
    
    @Override
    public final void channelActive(final ChannelHandlerContext ctx) throws Exception {
        this.sendInitialMessage(ctx);
        ctx.fireChannelActive();
    }
    
    private void sendInitialMessage(final ChannelHandlerContext ctx) throws Exception {
        final long connectTimeoutMillis = this.connectTimeoutMillis;
        if (connectTimeoutMillis > 0L) {
            this.connectTimeoutFuture = ctx.executor().schedule((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (!ProxyHandler.this.connectPromise.isDone()) {
                        ProxyHandler.this.setConnectFailure(new ProxyConnectException(ProxyHandler.this.exceptionMessage("timeout")));
                    }
                }
            }, connectTimeoutMillis, TimeUnit.MILLISECONDS);
        }
        final Object initialMessage = this.newInitialMessage(ctx);
        if (initialMessage != null) {
            this.sendToProxyServer(initialMessage);
        }
        readIfNeeded(ctx);
    }
    
    protected abstract Object newInitialMessage(final ChannelHandlerContext p0) throws Exception;
    
    protected final void sendToProxyServer(final Object msg) {
        this.ctx.writeAndFlush(msg).addListener((GenericFutureListener<? extends Future<? super Void>>)this.writeListener);
    }
    
    @Override
    public final void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        if (this.finished) {
            ctx.fireChannelInactive();
        }
        else {
            this.setConnectFailure(new ProxyConnectException(this.exceptionMessage("disconnected")));
        }
    }
    
    @Override
    public final void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        if (this.finished) {
            ctx.fireExceptionCaught(cause);
        }
        else {
            this.setConnectFailure(cause);
        }
    }
    
    @Override
    public final void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        if (this.finished) {
            this.suppressChannelReadComplete = false;
            ctx.fireChannelRead(msg);
        }
        else {
            this.suppressChannelReadComplete = true;
            Throwable cause = null;
            try {
                final boolean done = this.handleResponse(ctx, msg);
                if (done) {
                    this.setConnectSuccess();
                }
            }
            catch (final Throwable t) {
                cause = t;
            }
            finally {
                ReferenceCountUtil.release(msg);
                if (cause != null) {
                    this.setConnectFailure(cause);
                }
            }
        }
    }
    
    protected abstract boolean handleResponse(final ChannelHandlerContext p0, final Object p1) throws Exception;
    
    private void setConnectSuccess() {
        this.finished = true;
        if (this.connectTimeoutFuture != null) {
            this.connectTimeoutFuture.cancel(false);
        }
        if (this.connectPromise.trySuccess(this.ctx.channel())) {
            boolean removedCodec = true;
            removedCodec &= this.safeRemoveEncoder();
            this.ctx.fireUserEventTriggered((Object)new ProxyConnectionEvent(this.protocol(), this.authScheme(), this.proxyAddress, this.destinationAddress));
            removedCodec &= this.safeRemoveDecoder();
            if (removedCodec) {
                this.writePendingWrites();
                if (this.flushedPrematurely) {
                    this.ctx.flush();
                }
            }
            else {
                final Exception cause = new ProxyConnectException("failed to remove all codec handlers added by the proxy handler; bug?");
                this.failPendingWrites(cause);
                this.ctx.fireExceptionCaught((Throwable)cause);
                this.ctx.close();
            }
        }
    }
    
    private boolean safeRemoveDecoder() {
        try {
            this.removeDecoder(this.ctx);
            return true;
        }
        catch (final Exception e) {
            ProxyHandler.logger.warn("Failed to remove proxy decoders:", e);
            return false;
        }
    }
    
    private boolean safeRemoveEncoder() {
        try {
            this.removeEncoder(this.ctx);
            return true;
        }
        catch (final Exception e) {
            ProxyHandler.logger.warn("Failed to remove proxy encoders:", e);
            return false;
        }
    }
    
    private void setConnectFailure(Throwable cause) {
        this.finished = true;
        if (this.connectTimeoutFuture != null) {
            this.connectTimeoutFuture.cancel(false);
        }
        if (!(cause instanceof ProxyConnectException)) {
            cause = new ProxyConnectException(this.exceptionMessage(cause.toString()), cause);
        }
        if (this.connectPromise.tryFailure(cause)) {
            this.safeRemoveDecoder();
            this.safeRemoveEncoder();
            this.failPendingWrites(cause);
            this.ctx.fireExceptionCaught(cause);
            this.ctx.close();
        }
    }
    
    protected final String exceptionMessage(String msg) {
        if (msg == null) {
            msg = "";
        }
        final StringBuilder buf = new StringBuilder(128 + msg.length()).append(this.protocol()).append(", ").append(this.authScheme()).append(", ").append(this.proxyAddress).append(" => ").append(this.destinationAddress);
        if (!msg.isEmpty()) {
            buf.append(", ").append(msg);
        }
        return buf.toString();
    }
    
    @Override
    public final void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
        if (this.suppressChannelReadComplete) {
            this.suppressChannelReadComplete = false;
            readIfNeeded(ctx);
        }
        else {
            ctx.fireChannelReadComplete();
        }
    }
    
    @Override
    public final void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        if (this.finished) {
            this.writePendingWrites();
            ctx.write(msg, promise);
        }
        else {
            this.addPendingWrite(ctx, msg, promise);
        }
    }
    
    @Override
    public final void flush(final ChannelHandlerContext ctx) throws Exception {
        if (this.finished) {
            this.writePendingWrites();
            ctx.flush();
        }
        else {
            this.flushedPrematurely = true;
        }
    }
    
    private static void readIfNeeded(final ChannelHandlerContext ctx) {
        if (!ctx.channel().config().isAutoRead()) {
            ctx.read();
        }
    }
    
    private void writePendingWrites() {
        if (this.pendingWrites != null) {
            this.pendingWrites.removeAndWriteAll();
            this.pendingWrites = null;
        }
    }
    
    private void failPendingWrites(final Throwable cause) {
        if (this.pendingWrites != null) {
            this.pendingWrites.removeAndFailAll(cause);
            this.pendingWrites = null;
        }
    }
    
    private void addPendingWrite(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) {
        PendingWriteQueue pendingWrites = this.pendingWrites;
        if (pendingWrites == null) {
            pendingWrites = (this.pendingWrites = new PendingWriteQueue(ctx));
        }
        pendingWrites.add(msg, promise);
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(ProxyHandler.class);
    }
    
    private final class LazyChannelPromise extends DefaultPromise<Channel>
    {
        @Override
        protected EventExecutor executor() {
            if (ProxyHandler.this.ctx == null) {
                throw new IllegalStateException();
            }
            return ProxyHandler.this.ctx.executor();
        }
    }
}
