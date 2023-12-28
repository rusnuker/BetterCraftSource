// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.oio;

import io.netty.channel.ChannelPromise;
import java.net.SocketAddress;
import io.netty.channel.ThreadPerChannelEventLoop;
import io.netty.channel.EventLoop;
import io.netty.channel.Channel;
import io.netty.channel.AbstractChannel;

public abstract class AbstractOioChannel extends AbstractChannel
{
    protected static final int SO_TIMEOUT = 1000;
    boolean readPending;
    private final Runnable readTask;
    private final Runnable clearReadPendingRunnable;
    
    protected AbstractOioChannel(final Channel parent) {
        super(parent);
        this.readTask = new Runnable() {
            @Override
            public void run() {
                AbstractOioChannel.this.doRead();
            }
        };
        this.clearReadPendingRunnable = new Runnable() {
            @Override
            public void run() {
                AbstractOioChannel.this.readPending = false;
            }
        };
    }
    
    @Override
    protected AbstractUnsafe newUnsafe() {
        return new DefaultOioUnsafe();
    }
    
    @Override
    protected boolean isCompatible(final EventLoop loop) {
        return loop instanceof ThreadPerChannelEventLoop;
    }
    
    protected abstract void doConnect(final SocketAddress p0, final SocketAddress p1) throws Exception;
    
    @Override
    protected void doBeginRead() throws Exception {
        if (this.readPending) {
            return;
        }
        this.readPending = true;
        this.eventLoop().execute(this.readTask);
    }
    
    protected abstract void doRead();
    
    @Deprecated
    protected boolean isReadPending() {
        return this.readPending;
    }
    
    @Deprecated
    protected void setReadPending(final boolean readPending) {
        if (this.isRegistered()) {
            final EventLoop eventLoop = this.eventLoop();
            if (eventLoop.inEventLoop()) {
                this.readPending = readPending;
            }
            else {
                eventLoop.execute(new Runnable() {
                    @Override
                    public void run() {
                        AbstractOioChannel.this.readPending = readPending;
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
                this.readPending = false;
            }
            else {
                eventLoop.execute(this.clearReadPendingRunnable);
            }
        }
        else {
            this.readPending = false;
        }
    }
    
    private final class DefaultOioUnsafe extends AbstractUnsafe
    {
        @Override
        public void connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
            if (!promise.setUncancellable() || !this.ensureOpen(promise)) {
                return;
            }
            try {
                final boolean wasActive = AbstractOioChannel.this.isActive();
                AbstractOioChannel.this.doConnect(remoteAddress, localAddress);
                final boolean active = AbstractOioChannel.this.isActive();
                this.safeSetSuccess(promise);
                if (!wasActive && active) {
                    AbstractOioChannel.this.pipeline().fireChannelActive();
                }
            }
            catch (final Throwable t) {
                this.safeSetFailure(promise, this.annotateConnectException(t, remoteAddress));
                this.closeIfClosed();
            }
        }
    }
}
