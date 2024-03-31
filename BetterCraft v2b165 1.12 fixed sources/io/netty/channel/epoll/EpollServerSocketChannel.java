// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.epoll;

import io.netty.channel.socket.ServerSocketChannelConfig;
import io.netty.channel.ChannelConfig;
import java.io.IOException;
import java.util.Map;
import io.netty.channel.unix.NativeInetAddress;
import io.netty.channel.Channel;
import java.net.SocketAddress;
import io.netty.channel.EventLoop;
import io.netty.channel.unix.FileDescriptor;
import java.util.Collections;
import io.netty.channel.unix.Socket;
import java.net.InetAddress;
import java.util.Collection;
import java.net.InetSocketAddress;
import io.netty.channel.socket.ServerSocketChannel;

public final class EpollServerSocketChannel extends AbstractEpollServerChannel implements ServerSocketChannel
{
    private final EpollServerSocketChannelConfig config;
    private volatile InetSocketAddress local;
    private volatile Collection<InetAddress> tcpMd5SigAddresses;
    
    public EpollServerSocketChannel() {
        super(Socket.newSocketStream(), false);
        this.tcpMd5SigAddresses = (Collection<InetAddress>)Collections.emptyList();
        this.config = new EpollServerSocketChannelConfig(this);
    }
    
    @Deprecated
    public EpollServerSocketChannel(final FileDescriptor fd) {
        this(new Socket(fd.intValue()));
    }
    
    @Deprecated
    public EpollServerSocketChannel(final Socket fd) {
        super(fd);
        this.tcpMd5SigAddresses = (Collection<InetAddress>)Collections.emptyList();
        this.local = fd.localAddress();
        this.config = new EpollServerSocketChannelConfig(this);
    }
    
    public EpollServerSocketChannel(final Socket fd, final boolean active) {
        super(fd, active);
        this.tcpMd5SigAddresses = (Collection<InetAddress>)Collections.emptyList();
        this.local = fd.localAddress();
        this.config = new EpollServerSocketChannelConfig(this);
    }
    
    @Override
    protected boolean isCompatible(final EventLoop loop) {
        return loop instanceof EpollEventLoop;
    }
    
    @Override
    protected void doBind(final SocketAddress localAddress) throws Exception {
        final InetSocketAddress addr = (InetSocketAddress)localAddress;
        AbstractEpollChannel.checkResolvable(addr);
        this.fd().bind(addr);
        this.local = this.fd().localAddress();
        if (Native.IS_SUPPORTING_TCP_FASTOPEN && this.config.getTcpFastopen() > 0) {
            Native.setTcpFastopen(this.fd().intValue(), this.config.getTcpFastopen());
        }
        this.fd().listen(this.config.getBacklog());
        this.active = true;
    }
    
    @Override
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress)super.remoteAddress();
    }
    
    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }
    
    @Override
    public EpollServerSocketChannelConfig config() {
        return this.config;
    }
    
    @Override
    protected InetSocketAddress localAddress0() {
        return this.local;
    }
    
    protected Channel newChildChannel(final int fd, final byte[] address, final int offset, final int len) throws Exception {
        return new EpollSocketChannel(this, new Socket(fd), NativeInetAddress.address(address, offset, len));
    }
    
    Collection<InetAddress> tcpMd5SigAddresses() {
        return this.tcpMd5SigAddresses;
    }
    
    void setTcpMd5Sig(final Map<InetAddress, byte[]> keys) throws IOException {
        this.tcpMd5SigAddresses = TcpMd5Util.newTcpMd5Sigs(this, this.tcpMd5SigAddresses, keys);
    }
}
