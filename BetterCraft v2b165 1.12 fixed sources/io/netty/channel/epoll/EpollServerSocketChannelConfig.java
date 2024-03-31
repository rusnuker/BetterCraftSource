// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.epoll;

import io.netty.channel.ChannelConfig;
import java.io.IOException;
import io.netty.channel.ChannelException;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import java.net.InetAddress;
import io.netty.channel.ChannelOption;
import java.util.Map;
import io.netty.channel.socket.ServerSocketChannelConfig;

public final class EpollServerSocketChannelConfig extends EpollServerChannelConfig implements ServerSocketChannelConfig
{
    EpollServerSocketChannelConfig(final EpollServerSocketChannel channel) {
        super(channel);
        this.setReuseAddress(true);
    }
    
    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(super.getOptions(), EpollChannelOption.SO_REUSEPORT, EpollChannelOption.IP_FREEBIND, EpollChannelOption.TCP_DEFER_ACCEPT);
    }
    
    @Override
    public <T> T getOption(final ChannelOption<T> option) {
        if (option == EpollChannelOption.SO_REUSEPORT) {
            return (T)Boolean.valueOf(this.isReusePort());
        }
        if (option == EpollChannelOption.IP_FREEBIND) {
            return (T)Boolean.valueOf(this.isFreeBind());
        }
        if (option == EpollChannelOption.TCP_DEFER_ACCEPT) {
            return (T)Integer.valueOf(this.getTcpDeferAccept());
        }
        return super.getOption(option);
    }
    
    @Override
    public <T> boolean setOption(final ChannelOption<T> option, final T value) {
        this.validate(option, value);
        if (option == EpollChannelOption.SO_REUSEPORT) {
            this.setReusePort((boolean)value);
        }
        else if (option == EpollChannelOption.IP_FREEBIND) {
            this.setFreeBind((boolean)value);
        }
        else if (option == EpollChannelOption.TCP_MD5SIG) {
            final Map<InetAddress, byte[]> m = (Map<InetAddress, byte[]>)value;
            this.setTcpMd5Sig(m);
        }
        else {
            if (option != EpollChannelOption.TCP_DEFER_ACCEPT) {
                return super.setOption(option, value);
            }
            this.setTcpDeferAccept((int)value);
        }
        return true;
    }
    
    @Override
    public EpollServerSocketChannelConfig setReuseAddress(final boolean reuseAddress) {
        super.setReuseAddress(reuseAddress);
        return this;
    }
    
    @Override
    public EpollServerSocketChannelConfig setReceiveBufferSize(final int receiveBufferSize) {
        super.setReceiveBufferSize(receiveBufferSize);
        return this;
    }
    
    @Override
    public EpollServerSocketChannelConfig setPerformancePreferences(final int connectionTime, final int latency, final int bandwidth) {
        return this;
    }
    
    @Override
    public EpollServerSocketChannelConfig setBacklog(final int backlog) {
        super.setBacklog(backlog);
        return this;
    }
    
    @Override
    public EpollServerSocketChannelConfig setConnectTimeoutMillis(final int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }
    
    @Deprecated
    @Override
    public EpollServerSocketChannelConfig setMaxMessagesPerRead(final int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }
    
    @Override
    public EpollServerSocketChannelConfig setWriteSpinCount(final int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }
    
    @Override
    public EpollServerSocketChannelConfig setAllocator(final ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }
    
    @Override
    public EpollServerSocketChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator allocator) {
        super.setRecvByteBufAllocator(allocator);
        return this;
    }
    
    @Override
    public EpollServerSocketChannelConfig setAutoRead(final boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }
    
    @Deprecated
    @Override
    public EpollServerSocketChannelConfig setWriteBufferHighWaterMark(final int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }
    
    @Deprecated
    @Override
    public EpollServerSocketChannelConfig setWriteBufferLowWaterMark(final int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }
    
    @Override
    public EpollServerSocketChannelConfig setWriteBufferWaterMark(final WriteBufferWaterMark writeBufferWaterMark) {
        super.setWriteBufferWaterMark(writeBufferWaterMark);
        return this;
    }
    
    @Override
    public EpollServerSocketChannelConfig setMessageSizeEstimator(final MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }
    
    public EpollServerSocketChannelConfig setTcpMd5Sig(final Map<InetAddress, byte[]> keys) {
        try {
            ((EpollServerSocketChannel)this.channel).setTcpMd5Sig(keys);
            return this;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    public boolean isReusePort() {
        try {
            return Native.isReusePort(this.channel.fd().intValue()) == 1;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    public EpollServerSocketChannelConfig setReusePort(final boolean reusePort) {
        try {
            Native.setReusePort(this.channel.fd().intValue(), reusePort ? 1 : 0);
            return this;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    public boolean isFreeBind() {
        try {
            return Native.isIpFreeBind(this.channel.fd().intValue()) != 0;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    public EpollServerSocketChannelConfig setFreeBind(final boolean freeBind) {
        try {
            Native.setIpFreeBind(this.channel.fd().intValue(), freeBind ? 1 : 0);
            return this;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    public EpollServerSocketChannelConfig setTcpDeferAccept(final int deferAccept) {
        try {
            this.channel.fd().setTcpDeferAccept(deferAccept);
            return this;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    public int getTcpDeferAccept() {
        try {
            return this.channel.fd().getTcpDeferAccept();
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
}
