// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.epoll;

import io.netty.util.concurrent.GlobalEventExecutor;
import java.util.concurrent.Executor;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.channel.AbstractChannel;
import io.netty.channel.ChannelConfig;
import java.util.Map;
import java.nio.channels.AlreadyConnectedException;
import java.net.UnknownHostException;
import io.netty.util.internal.PlatformDependent;
import io.netty.channel.socket.ServerSocketChannel;
import java.net.SocketAddress;
import java.io.IOException;
import io.netty.channel.ChannelException;
import io.netty.channel.unix.FileDescriptor;
import java.util.Collections;
import io.netty.channel.unix.Socket;
import io.netty.channel.Channel;
import java.net.InetAddress;
import java.util.Collection;
import java.net.InetSocketAddress;
import io.netty.channel.socket.SocketChannel;

public final class EpollSocketChannel extends AbstractEpollStreamChannel implements SocketChannel
{
    private final EpollSocketChannelConfig config;
    private volatile InetSocketAddress local;
    private volatile InetSocketAddress remote;
    private InetSocketAddress requestedRemote;
    private volatile Collection<InetAddress> tcpMd5SigAddresses;
    
    EpollSocketChannel(final Channel parent, final Socket fd, final InetSocketAddress remote) {
        super(parent, fd);
        this.tcpMd5SigAddresses = (Collection<InetAddress>)Collections.emptyList();
        this.config = new EpollSocketChannelConfig(this);
        this.remote = remote;
        this.local = fd.localAddress();
        if (parent instanceof EpollServerSocketChannel) {
            this.tcpMd5SigAddresses = ((EpollServerSocketChannel)parent).tcpMd5SigAddresses();
        }
    }
    
    public EpollSocketChannel() {
        super(Socket.newSocketStream(), false);
        this.tcpMd5SigAddresses = (Collection<InetAddress>)Collections.emptyList();
        this.config = new EpollSocketChannelConfig(this);
    }
    
    @Deprecated
    public EpollSocketChannel(final FileDescriptor fd) {
        super(fd);
        this.tcpMd5SigAddresses = (Collection<InetAddress>)Collections.emptyList();
        this.remote = this.fd().remoteAddress();
        this.local = this.fd().localAddress();
        this.config = new EpollSocketChannelConfig(this);
    }
    
    public EpollSocketChannel(final Socket fd, final boolean active) {
        super(fd, active);
        this.tcpMd5SigAddresses = (Collection<InetAddress>)Collections.emptyList();
        this.remote = fd.remoteAddress();
        this.local = fd.localAddress();
        this.config = new EpollSocketChannelConfig(this);
    }
    
    public EpollTcpInfo tcpInfo() {
        return this.tcpInfo(new EpollTcpInfo());
    }
    
    public EpollTcpInfo tcpInfo(final EpollTcpInfo info) {
        try {
            Native.tcpInfo(this.fd().intValue(), info);
            return info;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
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
    protected SocketAddress localAddress0() {
        return this.local;
    }
    
    @Override
    protected SocketAddress remoteAddress0() {
        return this.remote;
    }
    
    @Override
    protected void doBind(final SocketAddress local) throws Exception {
        final InetSocketAddress localAddress = (InetSocketAddress)local;
        this.fd().bind(localAddress);
        this.local = this.fd().localAddress();
    }
    
    @Override
    public EpollSocketChannelConfig config() {
        return this.config;
    }
    
    @Override
    public ServerSocketChannel parent() {
        return (ServerSocketChannel)super.parent();
    }
    
    @Override
    protected AbstractEpollUnsafe newUnsafe() {
        return new EpollSocketChannelUnsafe();
    }
    
    private static InetSocketAddress computeRemoteAddr(final InetSocketAddress remoteAddr, final InetSocketAddress osRemoteAddr) {
        if (osRemoteAddr != null) {
            if (PlatformDependent.javaVersion() >= 7) {
                try {
                    return new InetSocketAddress(InetAddress.getByAddress(remoteAddr.getHostString(), osRemoteAddr.getAddress().getAddress()), osRemoteAddr.getPort());
                }
                catch (final UnknownHostException ex) {}
            }
            return osRemoteAddr;
        }
        return remoteAddr;
    }
    
    @Override
    protected boolean doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) throws Exception {
        if (localAddress != null) {
            AbstractEpollChannel.checkResolvable((InetSocketAddress)localAddress);
        }
        final InetSocketAddress remoteAddr = (InetSocketAddress)remoteAddress;
        AbstractEpollChannel.checkResolvable(remoteAddr);
        if (this.remote != null) {
            throw new AlreadyConnectedException();
        }
        final boolean connected = super.doConnect(remoteAddress, localAddress);
        if (connected) {
            this.remote = computeRemoteAddr(remoteAddr, this.fd().remoteAddress());
        }
        else {
            this.requestedRemote = remoteAddr;
        }
        this.local = this.fd().localAddress();
        return connected;
    }
    
    void setTcpMd5Sig(final Map<InetAddress, byte[]> keys) throws IOException {
        this.tcpMd5SigAddresses = TcpMd5Util.newTcpMd5Sigs(this, this.tcpMd5SigAddresses, keys);
    }
    
    private final class EpollSocketChannelUnsafe extends EpollStreamUnsafe
    {
        @Override
        protected Executor prepareToClose() {
            try {
                if (EpollSocketChannel.this.isOpen() && EpollSocketChannel.this.config().getSoLinger() > 0) {
                    ((EpollEventLoop)EpollSocketChannel.this.eventLoop()).remove(EpollSocketChannel.this);
                    return GlobalEventExecutor.INSTANCE;
                }
            }
            catch (final Throwable t) {}
            return null;
        }
        
        @Override
        boolean doFinishConnect() throws Exception {
            if (super.doFinishConnect()) {
                EpollSocketChannel.this.remote = computeRemoteAddr(EpollSocketChannel.this.requestedRemote, EpollSocketChannel.this.fd().remoteAddress());
                EpollSocketChannel.this.requestedRemote = null;
                return true;
            }
            return false;
        }
    }
}
