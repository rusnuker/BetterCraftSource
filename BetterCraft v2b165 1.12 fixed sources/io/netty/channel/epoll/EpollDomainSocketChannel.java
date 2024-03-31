// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.epoll;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.unix.DomainSocketChannelConfig;
import io.netty.channel.AbstractChannel;
import io.netty.channel.ChannelConfig;
import java.io.IOException;
import io.netty.channel.unix.PeerCredentials;
import io.netty.channel.ChannelOutboundBuffer;
import java.net.SocketAddress;
import io.netty.channel.unix.FileDescriptor;
import io.netty.channel.Channel;
import io.netty.channel.unix.Socket;
import io.netty.channel.unix.DomainSocketAddress;
import io.netty.channel.unix.DomainSocketChannel;

public final class EpollDomainSocketChannel extends AbstractEpollStreamChannel implements DomainSocketChannel
{
    private final EpollDomainSocketChannelConfig config;
    private volatile DomainSocketAddress local;
    private volatile DomainSocketAddress remote;
    
    public EpollDomainSocketChannel() {
        super(Socket.newSocketDomain(), false);
        this.config = new EpollDomainSocketChannelConfig(this);
    }
    
    @Deprecated
    public EpollDomainSocketChannel(final Channel parent, final FileDescriptor fd) {
        super(parent, new Socket(fd.intValue()));
        this.config = new EpollDomainSocketChannelConfig(this);
    }
    
    @Deprecated
    public EpollDomainSocketChannel(final FileDescriptor fd) {
        super(fd);
        this.config = new EpollDomainSocketChannelConfig(this);
    }
    
    public EpollDomainSocketChannel(final Channel parent, final Socket fd) {
        super(parent, fd);
        this.config = new EpollDomainSocketChannelConfig(this);
    }
    
    public EpollDomainSocketChannel(final Socket fd, final boolean active) {
        super(fd, active);
        this.config = new EpollDomainSocketChannelConfig(this);
    }
    
    @Override
    protected AbstractEpollUnsafe newUnsafe() {
        return new EpollDomainUnsafe();
    }
    
    @Override
    protected DomainSocketAddress localAddress0() {
        return this.local;
    }
    
    @Override
    protected DomainSocketAddress remoteAddress0() {
        return this.remote;
    }
    
    @Override
    protected void doBind(final SocketAddress localAddress) throws Exception {
        this.fd().bind(localAddress);
        this.local = (DomainSocketAddress)localAddress;
    }
    
    @Override
    public EpollDomainSocketChannelConfig config() {
        return this.config;
    }
    
    @Override
    protected boolean doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) throws Exception {
        if (super.doConnect(remoteAddress, localAddress)) {
            this.local = (DomainSocketAddress)localAddress;
            this.remote = (DomainSocketAddress)remoteAddress;
            return true;
        }
        return false;
    }
    
    @Override
    public DomainSocketAddress remoteAddress() {
        return (DomainSocketAddress)super.remoteAddress();
    }
    
    @Override
    public DomainSocketAddress localAddress() {
        return (DomainSocketAddress)super.localAddress();
    }
    
    @Override
    protected boolean doWriteSingle(final ChannelOutboundBuffer in, final int writeSpinCount) throws Exception {
        final Object msg = in.current();
        if (msg instanceof FileDescriptor && Native.sendFd(this.fd().intValue(), ((FileDescriptor)msg).intValue()) > 0) {
            in.remove();
            return true;
        }
        return super.doWriteSingle(in, writeSpinCount);
    }
    
    @Override
    protected Object filterOutboundMessage(final Object msg) {
        if (msg instanceof FileDescriptor) {
            return msg;
        }
        return super.filterOutboundMessage(msg);
    }
    
    public PeerCredentials peerCredentials() throws IOException {
        return this.fd().getPeerCredentials();
    }
    
    private final class EpollDomainUnsafe extends EpollStreamUnsafe
    {
        @Override
        void epollInReady() {
            switch (EpollDomainSocketChannel.this.config().getReadMode()) {
                case BYTES: {
                    super.epollInReady();
                    break;
                }
                case FILE_DESCRIPTORS: {
                    this.epollInReadFd();
                    break;
                }
                default: {
                    throw new Error();
                }
            }
        }
        
        private void epollInReadFd() {
            if (EpollDomainSocketChannel.this.fd().isInputShutdown()) {
                this.clearEpollIn0();
                return;
            }
            final ChannelConfig config = EpollDomainSocketChannel.this.config();
            final EpollRecvByteAllocatorHandle allocHandle = this.recvBufAllocHandle();
            allocHandle.edgeTriggered(EpollDomainSocketChannel.this.isFlagSet(Native.EPOLLET));
            final ChannelPipeline pipeline = EpollDomainSocketChannel.this.pipeline();
            allocHandle.reset(config);
            this.epollInBefore();
            try {
            Label_0160:
                do {
                    allocHandle.lastBytesRead(Native.recvFd(EpollDomainSocketChannel.this.fd().intValue()));
                    switch (allocHandle.lastBytesRead()) {
                        case 0: {
                            break Label_0160;
                        }
                        case -1: {
                            this.close(this.voidPromise());
                            return;
                        }
                        default: {
                            allocHandle.incMessagesRead(1);
                            this.readPending = false;
                            pipeline.fireChannelRead((Object)new FileDescriptor(allocHandle.lastBytesRead()));
                            continue;
                        }
                    }
                } while (allocHandle.continueReading());
                allocHandle.readComplete();
                pipeline.fireChannelReadComplete();
            }
            catch (final Throwable t) {
                allocHandle.readComplete();
                pipeline.fireChannelReadComplete();
                pipeline.fireExceptionCaught(t);
            }
            finally {
                this.epollInFinally(config);
            }
        }
    }
}
