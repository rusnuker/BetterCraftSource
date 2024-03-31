// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.channel.ChannelPromise;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.concurrent.EventExecutor;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.util.ReferenceCountUtil;
import java.net.SocketAddress;
import io.netty.channel.EventLoop;
import java.util.ArrayDeque;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.Channel;
import java.util.Queue;
import io.netty.channel.ChannelConfig;
import java.nio.channels.ClosedChannelException;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.AbstractChannel;

abstract class AbstractHttp2StreamChannel extends AbstractChannel
{
    protected static final Object CLOSE_MESSAGE;
    private static final ChannelMetadata METADATA;
    private static final ClosedChannelException CLOSED_CHANNEL_EXCEPTION;
    private static final int ARBITRARY_MESSAGE_SIZE = 9;
    private final ChannelConfig config;
    private final Queue<Object> inboundBuffer;
    private final Runnable fireChildReadCompleteTask;
    private volatile int streamId;
    private boolean closed;
    private boolean readInProgress;
    
    protected AbstractHttp2StreamChannel(final Channel parent) {
        super(parent);
        this.config = new DefaultChannelConfig(this);
        this.inboundBuffer = new ArrayDeque<Object>(4);
        this.fireChildReadCompleteTask = new Runnable() {
            @Override
            public void run() {
                if (AbstractHttp2StreamChannel.this.readInProgress) {
                    AbstractHttp2StreamChannel.this.readInProgress = false;
                    AbstractHttp2StreamChannel.this.unsafe().recvBufAllocHandle().readComplete();
                    AbstractHttp2StreamChannel.this.pipeline().fireChannelReadComplete();
                }
            }
        };
        this.streamId = -1;
    }
    
    @Override
    public ChannelMetadata metadata() {
        return AbstractHttp2StreamChannel.METADATA;
    }
    
    @Override
    public ChannelConfig config() {
        return this.config;
    }
    
    @Override
    public boolean isOpen() {
        return !this.closed;
    }
    
    @Override
    public boolean isActive() {
        return this.isOpen();
    }
    
    @Override
    protected AbstractUnsafe newUnsafe() {
        return new Unsafe();
    }
    
    @Override
    protected boolean isCompatible(final EventLoop loop) {
        return true;
    }
    
    @Override
    protected SocketAddress localAddress0() {
        return this.parent().localAddress();
    }
    
    @Override
    protected SocketAddress remoteAddress0() {
        return this.parent().remoteAddress();
    }
    
    @Override
    protected void doBind(final SocketAddress localAddress) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected void doDisconnect() throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected void doClose() throws Exception {
        this.closed = true;
        while (!this.inboundBuffer.isEmpty()) {
            ReferenceCountUtil.release(this.inboundBuffer.poll());
        }
    }
    
    @Override
    protected void doBeginRead() {
        if (this.readInProgress) {
            return;
        }
        final RecvByteBufAllocator.Handle allocHandle = this.unsafe().recvBufAllocHandle();
        allocHandle.reset(this.config());
        if (this.inboundBuffer.isEmpty()) {
            this.readInProgress = true;
            return;
        }
        do {
            final Object m = this.inboundBuffer.poll();
            if (m == null) {
                break;
            }
            if (!this.doRead0(m, allocHandle)) {
                return;
            }
        } while (allocHandle.continueReading());
        allocHandle.readComplete();
        this.pipeline().fireChannelReadComplete();
    }
    
    @Override
    protected final void doWrite(final ChannelOutboundBuffer in) throws Exception {
        if (this.closed) {
            throw AbstractHttp2StreamChannel.CLOSED_CHANNEL_EXCEPTION;
        }
        final EventExecutor preferredExecutor = this.preferredEventExecutor();
        if (preferredExecutor.inEventLoop()) {
            while (true) {
                final Object msg = in.current();
                if (msg == null) {
                    break;
                }
                try {
                    this.doWrite(ReferenceCountUtil.retain(msg));
                }
                catch (final Throwable t) {
                    this.pipeline().fireExceptionCaught(t);
                }
                in.remove();
            }
            this.doWriteComplete();
        }
        else {
            final Object[] msgsCopy = new Object[in.size()];
            for (int i = 0; i < msgsCopy.length; ++i) {
                msgsCopy[i] = ReferenceCountUtil.retain(in.current());
                in.remove();
            }
            preferredExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    for (final Object msg : msgsCopy) {
                        try {
                            AbstractHttp2StreamChannel.this.doWrite(msg);
                        }
                        catch (final Throwable t) {
                            AbstractHttp2StreamChannel.this.pipeline().fireExceptionCaught(t);
                        }
                    }
                    AbstractHttp2StreamChannel.this.doWriteComplete();
                }
            });
        }
    }
    
    protected abstract void doWrite(final Object p0) throws Exception;
    
    protected abstract void doWriteComplete();
    
    protected abstract EventExecutor preferredEventExecutor();
    
    protected abstract void bytesConsumed(final int p0);
    
    protected void fireChildRead(final Object msg) {
        if (this.eventLoop().inEventLoop()) {
            this.fireChildRead0(msg);
        }
        else {
            this.eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    AbstractHttp2StreamChannel.this.fireChildRead0(msg);
                }
            });
        }
    }
    
    private void fireChildRead0(final Object msg) {
        if (this.closed) {
            ReferenceCountUtil.release(msg);
            return;
        }
        if (this.readInProgress) {
            assert this.inboundBuffer.isEmpty();
            final RecvByteBufAllocator.Handle allocHandle = this.unsafe().recvBufAllocHandle();
            this.readInProgress = this.doRead0(ObjectUtil.checkNotNull(msg, "msg"), allocHandle);
            if (!allocHandle.continueReading()) {
                this.fireChildReadCompleteTask.run();
            }
        }
        else {
            this.inboundBuffer.add(msg);
        }
    }
    
    protected void fireChildReadComplete() {
        if (this.eventLoop().inEventLoop()) {
            this.fireChildReadCompleteTask.run();
        }
        else {
            this.eventLoop().execute(this.fireChildReadCompleteTask);
        }
    }
    
    protected void streamId(final int streamId) {
        if (this.streamId != -1) {
            throw new IllegalStateException("Stream identifier may only be set once.");
        }
        this.streamId = ObjectUtil.checkPositiveOrZero(streamId, "streamId");
    }
    
    protected int streamId() {
        return this.streamId;
    }
    
    private boolean doRead0(final Object msg, final RecvByteBufAllocator.Handle allocHandle) {
        if (msg == AbstractHttp2StreamChannel.CLOSE_MESSAGE) {
            allocHandle.readComplete();
            this.pipeline().fireChannelReadComplete();
            this.unsafe().close(this.voidPromise());
            return false;
        }
        int numBytesToBeConsumed = 0;
        if (msg instanceof Http2DataFrame) {
            final Http2DataFrame data = (Http2DataFrame)msg;
            numBytesToBeConsumed = data.content().readableBytes() + data.padding();
            allocHandle.lastBytesRead(numBytesToBeConsumed);
        }
        else {
            allocHandle.lastBytesRead(9);
        }
        allocHandle.incMessagesRead(1);
        this.pipeline().fireChannelRead(msg);
        if (numBytesToBeConsumed != 0) {
            this.bytesConsumed(numBytesToBeConsumed);
        }
        return true;
    }
    
    static {
        CLOSE_MESSAGE = new Object();
        METADATA = new ChannelMetadata(false, 16);
        CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractHttp2StreamChannel.class, "doWrite(...)");
    }
    
    private final class Unsafe extends AbstractUnsafe
    {
        @Override
        public void connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
            promise.setFailure((Throwable)new UnsupportedOperationException());
        }
    }
}
