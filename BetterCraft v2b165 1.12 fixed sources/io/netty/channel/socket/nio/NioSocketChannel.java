// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.socket.nio;

import io.netty.channel.socket.DefaultSocketChannelConfig;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.ChannelConfig;
import io.netty.channel.AbstractChannel;
import io.netty.channel.nio.AbstractNioChannel;
import java.nio.ByteBuffer;
import io.netty.channel.ChannelOutboundBuffer;
import java.nio.channels.WritableByteChannel;
import io.netty.channel.FileRegion;
import java.nio.channels.GatheringByteChannel;
import io.netty.channel.RecvByteBufAllocator;
import java.nio.channels.ScatteringByteChannel;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.SocketUtils;
import java.net.SocketAddress;
import io.netty.util.internal.PlatformDependent;
import io.netty.channel.EventLoop;
import java.util.concurrent.Executor;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import java.net.InetSocketAddress;
import java.net.Socket;
import io.netty.channel.socket.ServerSocketChannel;
import java.nio.channels.SelectableChannel;
import io.netty.channel.Channel;
import java.io.IOException;
import io.netty.channel.ChannelException;
import io.netty.channel.socket.SocketChannelConfig;
import java.nio.channels.spi.SelectorProvider;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.nio.AbstractNioByteChannel;

public class NioSocketChannel extends AbstractNioByteChannel implements SocketChannel
{
    private static final InternalLogger logger;
    private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER;
    private final SocketChannelConfig config;
    
    private static java.nio.channels.SocketChannel newSocket(final SelectorProvider provider) {
        try {
            return provider.openSocketChannel();
        }
        catch (final IOException e) {
            throw new ChannelException("Failed to open a socket.", e);
        }
    }
    
    public NioSocketChannel() {
        this(NioSocketChannel.DEFAULT_SELECTOR_PROVIDER);
    }
    
    public NioSocketChannel(final SelectorProvider provider) {
        this(newSocket(provider));
    }
    
    public NioSocketChannel(final java.nio.channels.SocketChannel socket) {
        this(null, socket);
    }
    
    public NioSocketChannel(final Channel parent, final java.nio.channels.SocketChannel socket) {
        super(parent, socket);
        this.config = new NioSocketChannelConfig(this, socket.socket());
    }
    
    @Override
    public ServerSocketChannel parent() {
        return (ServerSocketChannel)super.parent();
    }
    
    @Override
    public SocketChannelConfig config() {
        return this.config;
    }
    
    @Override
    protected java.nio.channels.SocketChannel javaChannel() {
        return (java.nio.channels.SocketChannel)super.javaChannel();
    }
    
    @Override
    public boolean isActive() {
        final java.nio.channels.SocketChannel ch = this.javaChannel();
        return ch.isOpen() && ch.isConnected();
    }
    
    @Override
    public boolean isOutputShutdown() {
        return this.javaChannel().socket().isOutputShutdown() || !this.isActive();
    }
    
    @Override
    public boolean isInputShutdown() {
        return this.javaChannel().socket().isInputShutdown() || !this.isActive();
    }
    
    @Override
    public boolean isShutdown() {
        final Socket socket = this.javaChannel().socket();
        return (socket.isInputShutdown() && socket.isOutputShutdown()) || !this.isActive();
    }
    
    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }
    
    @Override
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress)super.remoteAddress();
    }
    
    @Override
    public ChannelFuture shutdownOutput() {
        return this.shutdownOutput(this.newPromise());
    }
    
    @Override
    public ChannelFuture shutdownOutput(final ChannelPromise promise) {
        final Executor closeExecutor = ((NioSocketChannelUnsafe)this.unsafe()).prepareToClose();
        if (closeExecutor != null) {
            closeExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    NioSocketChannel.this.shutdownOutput0(promise);
                }
            });
        }
        else {
            final EventLoop loop = this.eventLoop();
            if (loop.inEventLoop()) {
                this.shutdownOutput0(promise);
            }
            else {
                loop.execute(new Runnable() {
                    @Override
                    public void run() {
                        NioSocketChannel.this.shutdownOutput0(promise);
                    }
                });
            }
        }
        return promise;
    }
    
    @Override
    public ChannelFuture shutdownInput() {
        return this.shutdownInput(this.newPromise());
    }
    
    @Override
    protected boolean isInputShutdown0() {
        return this.isInputShutdown();
    }
    
    @Override
    public ChannelFuture shutdownInput(final ChannelPromise promise) {
        final Executor closeExecutor = ((NioSocketChannelUnsafe)this.unsafe()).prepareToClose();
        if (closeExecutor != null) {
            closeExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    NioSocketChannel.this.shutdownInput0(promise);
                }
            });
        }
        else {
            final EventLoop loop = this.eventLoop();
            if (loop.inEventLoop()) {
                this.shutdownInput0(promise);
            }
            else {
                loop.execute(new Runnable() {
                    @Override
                    public void run() {
                        NioSocketChannel.this.shutdownInput0(promise);
                    }
                });
            }
        }
        return promise;
    }
    
    @Override
    public ChannelFuture shutdown() {
        return this.shutdown(this.newPromise());
    }
    
    @Override
    public ChannelFuture shutdown(final ChannelPromise promise) {
        final Executor closeExecutor = ((NioSocketChannelUnsafe)this.unsafe()).prepareToClose();
        if (closeExecutor != null) {
            closeExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    NioSocketChannel.this.shutdown0(promise);
                }
            });
        }
        else {
            final EventLoop loop = this.eventLoop();
            if (loop.inEventLoop()) {
                this.shutdown0(promise);
            }
            else {
                loop.execute(new Runnable() {
                    @Override
                    public void run() {
                        NioSocketChannel.this.shutdown0(promise);
                    }
                });
            }
        }
        return promise;
    }
    
    private void shutdownOutput0(final ChannelPromise promise) {
        try {
            this.shutdownOutput0();
            promise.setSuccess();
        }
        catch (final Throwable t) {
            promise.setFailure(t);
        }
    }
    
    private void shutdownOutput0() throws Exception {
        if (PlatformDependent.javaVersion() >= 7) {
            this.javaChannel().shutdownOutput();
        }
        else {
            this.javaChannel().socket().shutdownOutput();
        }
    }
    
    private void shutdownInput0(final ChannelPromise promise) {
        try {
            this.shutdownInput0();
            promise.setSuccess();
        }
        catch (final Throwable t) {
            promise.setFailure(t);
        }
    }
    
    private void shutdownInput0() throws Exception {
        if (PlatformDependent.javaVersion() >= 7) {
            this.javaChannel().shutdownInput();
        }
        else {
            this.javaChannel().socket().shutdownInput();
        }
    }
    
    private void shutdown0(final ChannelPromise promise) {
        Throwable cause = null;
        try {
            this.shutdownOutput0();
        }
        catch (final Throwable t) {
            cause = t;
        }
        try {
            this.shutdownInput0();
        }
        catch (final Throwable t) {
            if (cause == null) {
                promise.setFailure(t);
            }
            else {
                NioSocketChannel.logger.debug("Exception suppressed because a previous exception occurred.", t);
                promise.setFailure(cause);
            }
            return;
        }
        if (cause == null) {
            promise.setSuccess();
        }
        else {
            promise.setFailure(cause);
        }
    }
    
    @Override
    protected SocketAddress localAddress0() {
        return this.javaChannel().socket().getLocalSocketAddress();
    }
    
    @Override
    protected SocketAddress remoteAddress0() {
        return this.javaChannel().socket().getRemoteSocketAddress();
    }
    
    @Override
    protected void doBind(final SocketAddress localAddress) throws Exception {
        this.doBind0(localAddress);
    }
    
    private void doBind0(final SocketAddress localAddress) throws Exception {
        if (PlatformDependent.javaVersion() >= 7) {
            SocketUtils.bind(this.javaChannel(), localAddress);
        }
        else {
            SocketUtils.bind(this.javaChannel().socket(), localAddress);
        }
    }
    
    @Override
    protected boolean doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) throws Exception {
        if (localAddress != null) {
            this.doBind0(localAddress);
        }
        boolean success = false;
        try {
            final boolean connected = SocketUtils.connect(this.javaChannel(), remoteAddress);
            if (!connected) {
                this.selectionKey().interestOps(8);
            }
            success = true;
            return connected;
        }
        finally {
            if (!success) {
                this.doClose();
            }
        }
    }
    
    @Override
    protected void doFinishConnect() throws Exception {
        if (!this.javaChannel().finishConnect()) {
            throw new Error();
        }
    }
    
    @Override
    protected void doDisconnect() throws Exception {
        this.doClose();
    }
    
    @Override
    protected void doClose() throws Exception {
        super.doClose();
        this.javaChannel().close();
    }
    
    @Override
    protected int doReadBytes(final ByteBuf byteBuf) throws Exception {
        final RecvByteBufAllocator.Handle allocHandle = this.unsafe().recvBufAllocHandle();
        allocHandle.attemptedBytesRead(byteBuf.writableBytes());
        return byteBuf.writeBytes(this.javaChannel(), allocHandle.attemptedBytesRead());
    }
    
    @Override
    protected int doWriteBytes(final ByteBuf buf) throws Exception {
        final int expectedWrittenBytes = buf.readableBytes();
        return buf.readBytes(this.javaChannel(), expectedWrittenBytes);
    }
    
    @Override
    protected long doWriteFileRegion(final FileRegion region) throws Exception {
        final long position = region.transferred();
        return region.transferTo(this.javaChannel(), position);
    }
    
    @Override
    protected void doWrite(final ChannelOutboundBuffer in) throws Exception {
        while (true) {
            final int size = in.size();
            if (size == 0) {
                this.clearOpWrite();
                break;
            }
            long writtenBytes = 0L;
            boolean done = false;
            boolean setOpWrite = false;
            final ByteBuffer[] nioBuffers = in.nioBuffers();
            final int nioBufferCnt = in.nioBufferCount();
            long expectedWrittenBytes = in.nioBufferSize();
            final java.nio.channels.SocketChannel ch = this.javaChannel();
            switch (nioBufferCnt) {
                case 0: {
                    super.doWrite(in);
                    return;
                }
                case 1: {
                    final ByteBuffer nioBuffer = nioBuffers[0];
                    for (int i = this.config().getWriteSpinCount() - 1; i >= 0; --i) {
                        final int localWrittenBytes = ch.write(nioBuffer);
                        if (localWrittenBytes == 0) {
                            setOpWrite = true;
                            break;
                        }
                        expectedWrittenBytes -= localWrittenBytes;
                        writtenBytes += localWrittenBytes;
                        if (expectedWrittenBytes == 0L) {
                            done = true;
                            break;
                        }
                    }
                    break;
                }
                default: {
                    for (int i = this.config().getWriteSpinCount() - 1; i >= 0; --i) {
                        final long localWrittenBytes2 = ch.write(nioBuffers, 0, nioBufferCnt);
                        if (localWrittenBytes2 == 0L) {
                            setOpWrite = true;
                            break;
                        }
                        expectedWrittenBytes -= localWrittenBytes2;
                        writtenBytes += localWrittenBytes2;
                        if (expectedWrittenBytes == 0L) {
                            done = true;
                            break;
                        }
                    }
                    break;
                }
            }
            in.removeBytes(writtenBytes);
            if (!done) {
                this.incompleteWrite(setOpWrite);
                break;
            }
        }
    }
    
    @Override
    protected AbstractNioUnsafe newUnsafe() {
        return new NioSocketChannelUnsafe();
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(NioSocketChannel.class);
        DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();
    }
    
    private final class NioSocketChannelUnsafe extends NioByteUnsafe
    {
        @Override
        protected Executor prepareToClose() {
            try {
                if (NioSocketChannel.this.javaChannel().isOpen() && NioSocketChannel.this.config().getSoLinger() > 0) {
                    AbstractNioChannel.this.doDeregister();
                    return GlobalEventExecutor.INSTANCE;
                }
            }
            catch (final Throwable t) {}
            return null;
        }
    }
    
    private final class NioSocketChannelConfig extends DefaultSocketChannelConfig
    {
        private NioSocketChannelConfig(final NioSocketChannel channel, final Socket javaSocket) {
            super(channel, javaSocket);
        }
        
        @Override
        protected void autoReadCleared() {
            AbstractNioChannel.this.clearReadPending();
        }
    }
}
