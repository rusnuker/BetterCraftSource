// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.epoll;

import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import java.io.IOException;
import io.netty.channel.ChannelException;
import java.net.InetAddress;
import io.netty.channel.ChannelOption;
import java.util.Map;
import io.netty.util.internal.PlatformDependent;
import io.netty.channel.socket.SocketChannelConfig;

public final class EpollSocketChannelConfig extends EpollChannelConfig implements SocketChannelConfig
{
    private static final long MAX_UINT32_T = 4294967295L;
    private final EpollSocketChannel channel;
    private volatile boolean allowHalfClosure;
    
    EpollSocketChannelConfig(final EpollSocketChannel channel) {
        super(channel);
        this.channel = channel;
        if (PlatformDependent.canEnableTcpNoDelayByDefault()) {
            this.setTcpNoDelay(true);
        }
    }
    
    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(super.getOptions(), ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.TCP_NODELAY, ChannelOption.SO_KEEPALIVE, ChannelOption.SO_REUSEADDR, ChannelOption.SO_LINGER, ChannelOption.IP_TOS, ChannelOption.ALLOW_HALF_CLOSURE, EpollChannelOption.TCP_CORK, EpollChannelOption.TCP_NOTSENT_LOWAT, EpollChannelOption.TCP_KEEPCNT, EpollChannelOption.TCP_KEEPIDLE, EpollChannelOption.TCP_KEEPINTVL, EpollChannelOption.TCP_MD5SIG, EpollChannelOption.TCP_QUICKACK);
    }
    
    @Override
    public <T> T getOption(final ChannelOption<T> option) {
        if (option == ChannelOption.SO_RCVBUF) {
            return (T)Integer.valueOf(this.getReceiveBufferSize());
        }
        if (option == ChannelOption.SO_SNDBUF) {
            return (T)Integer.valueOf(this.getSendBufferSize());
        }
        if (option == ChannelOption.TCP_NODELAY) {
            return (T)Boolean.valueOf(this.isTcpNoDelay());
        }
        if (option == ChannelOption.SO_KEEPALIVE) {
            return (T)Boolean.valueOf(this.isKeepAlive());
        }
        if (option == ChannelOption.SO_REUSEADDR) {
            return (T)Boolean.valueOf(this.isReuseAddress());
        }
        if (option == ChannelOption.SO_LINGER) {
            return (T)Integer.valueOf(this.getSoLinger());
        }
        if (option == ChannelOption.IP_TOS) {
            return (T)Integer.valueOf(this.getTrafficClass());
        }
        if (option == ChannelOption.ALLOW_HALF_CLOSURE) {
            return (T)Boolean.valueOf(this.isAllowHalfClosure());
        }
        if (option == EpollChannelOption.TCP_CORK) {
            return (T)Boolean.valueOf(this.isTcpCork());
        }
        if (option == EpollChannelOption.TCP_NOTSENT_LOWAT) {
            return (T)Long.valueOf(this.getTcpNotSentLowAt());
        }
        if (option == EpollChannelOption.TCP_KEEPIDLE) {
            return (T)Integer.valueOf(this.getTcpKeepIdle());
        }
        if (option == EpollChannelOption.TCP_KEEPINTVL) {
            return (T)Integer.valueOf(this.getTcpKeepIntvl());
        }
        if (option == EpollChannelOption.TCP_KEEPCNT) {
            return (T)Integer.valueOf(this.getTcpKeepCnt());
        }
        if (option == EpollChannelOption.TCP_USER_TIMEOUT) {
            return (T)Integer.valueOf(this.getTcpUserTimeout());
        }
        if (option == EpollChannelOption.TCP_QUICKACK) {
            return (T)Boolean.valueOf(this.isTcpQuickAck());
        }
        return super.getOption(option);
    }
    
    @Override
    public <T> boolean setOption(final ChannelOption<T> option, final T value) {
        this.validate(option, value);
        if (option == ChannelOption.SO_RCVBUF) {
            this.setReceiveBufferSize((int)value);
        }
        else if (option == ChannelOption.SO_SNDBUF) {
            this.setSendBufferSize((int)value);
        }
        else if (option == ChannelOption.TCP_NODELAY) {
            this.setTcpNoDelay((boolean)value);
        }
        else if (option == ChannelOption.SO_KEEPALIVE) {
            this.setKeepAlive((boolean)value);
        }
        else if (option == ChannelOption.SO_REUSEADDR) {
            this.setReuseAddress((boolean)value);
        }
        else if (option == ChannelOption.SO_LINGER) {
            this.setSoLinger((int)value);
        }
        else if (option == ChannelOption.IP_TOS) {
            this.setTrafficClass((int)value);
        }
        else if (option == ChannelOption.ALLOW_HALF_CLOSURE) {
            this.setAllowHalfClosure((boolean)value);
        }
        else if (option == EpollChannelOption.TCP_CORK) {
            this.setTcpCork((boolean)value);
        }
        else if (option == EpollChannelOption.TCP_NOTSENT_LOWAT) {
            this.setTcpNotSentLowAt((long)value);
        }
        else if (option == EpollChannelOption.TCP_KEEPIDLE) {
            this.setTcpKeepIdle((int)value);
        }
        else if (option == EpollChannelOption.TCP_KEEPCNT) {
            this.setTcpKeepCntl((int)value);
        }
        else if (option == EpollChannelOption.TCP_KEEPINTVL) {
            this.setTcpKeepIntvl((int)value);
        }
        else if (option == EpollChannelOption.TCP_USER_TIMEOUT) {
            this.setTcpUserTimeout((int)value);
        }
        else if (option == EpollChannelOption.TCP_MD5SIG) {
            final Map<InetAddress, byte[]> m = (Map<InetAddress, byte[]>)value;
            this.setTcpMd5Sig(m);
        }
        else {
            if (option != EpollChannelOption.TCP_QUICKACK) {
                return super.setOption(option, value);
            }
            this.setTcpQuickAck((boolean)value);
        }
        return true;
    }
    
    @Override
    public int getReceiveBufferSize() {
        try {
            return this.channel.fd().getReceiveBufferSize();
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public int getSendBufferSize() {
        try {
            return this.channel.fd().getSendBufferSize();
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public int getSoLinger() {
        try {
            return this.channel.fd().getSoLinger();
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public int getTrafficClass() {
        try {
            return Native.getTrafficClass(this.channel.fd().intValue());
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public boolean isKeepAlive() {
        try {
            return this.channel.fd().isKeepAlive();
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public boolean isReuseAddress() {
        try {
            return Native.isReuseAddress(this.channel.fd().intValue()) == 1;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public boolean isTcpNoDelay() {
        try {
            return this.channel.fd().isTcpNoDelay();
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    public boolean isTcpCork() {
        try {
            return this.channel.fd().isTcpCork();
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    public long getTcpNotSentLowAt() {
        try {
            return (long)Native.getTcpNotSentLowAt(this.channel.fd().intValue()) & 0xFFFFFFFFL;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    public int getTcpKeepIdle() {
        try {
            return Native.getTcpKeepIdle(this.channel.fd().intValue());
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    public int getTcpKeepIntvl() {
        try {
            return Native.getTcpKeepIntvl(this.channel.fd().intValue());
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    public int getTcpKeepCnt() {
        try {
            return Native.getTcpKeepCnt(this.channel.fd().intValue());
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    public int getTcpUserTimeout() {
        try {
            return Native.getTcpUserTimeout(this.channel.fd().intValue());
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public EpollSocketChannelConfig setKeepAlive(final boolean keepAlive) {
        try {
            this.channel.fd().setKeepAlive(keepAlive);
            return this;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public EpollSocketChannelConfig setPerformancePreferences(final int connectionTime, final int latency, final int bandwidth) {
        return this;
    }
    
    @Override
    public EpollSocketChannelConfig setReceiveBufferSize(final int receiveBufferSize) {
        try {
            this.channel.fd().setReceiveBufferSize(receiveBufferSize);
            return this;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public EpollSocketChannelConfig setReuseAddress(final boolean reuseAddress) {
        try {
            Native.setReuseAddress(this.channel.fd().intValue(), reuseAddress ? 1 : 0);
            return this;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public EpollSocketChannelConfig setSendBufferSize(final int sendBufferSize) {
        try {
            this.channel.fd().setSendBufferSize(sendBufferSize);
            return this;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public EpollSocketChannelConfig setSoLinger(final int soLinger) {
        try {
            this.channel.fd().setSoLinger(soLinger);
            return this;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public EpollSocketChannelConfig setTcpNoDelay(final boolean tcpNoDelay) {
        try {
            this.channel.fd().setTcpNoDelay(tcpNoDelay);
            return this;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    public EpollSocketChannelConfig setTcpCork(final boolean tcpCork) {
        try {
            this.channel.fd().setTcpCork(tcpCork);
            return this;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    public EpollSocketChannelConfig setTcpNotSentLowAt(final long tcpNotSentLowAt) {
        if (tcpNotSentLowAt < 0L || tcpNotSentLowAt > 4294967295L) {
            throw new IllegalArgumentException("tcpNotSentLowAt must be a uint32_t");
        }
        try {
            Native.setTcpNotSentLowAt(this.channel.fd().intValue(), (int)tcpNotSentLowAt);
            return this;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public EpollSocketChannelConfig setTrafficClass(final int trafficClass) {
        try {
            Native.setTrafficClass(this.channel.fd().intValue(), trafficClass);
            return this;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    public EpollSocketChannelConfig setTcpKeepIdle(final int seconds) {
        try {
            Native.setTcpKeepIdle(this.channel.fd().intValue(), seconds);
            return this;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    public EpollSocketChannelConfig setTcpKeepIntvl(final int seconds) {
        try {
            Native.setTcpKeepIntvl(this.channel.fd().intValue(), seconds);
            return this;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    public EpollSocketChannelConfig setTcpKeepCntl(final int probes) {
        try {
            Native.setTcpKeepCnt(this.channel.fd().intValue(), probes);
            return this;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    public EpollSocketChannelConfig setTcpUserTimeout(final int milliseconds) {
        try {
            Native.setTcpUserTimeout(this.channel.fd().intValue(), milliseconds);
            return this;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    public EpollSocketChannelConfig setTcpMd5Sig(final Map<InetAddress, byte[]> keys) {
        try {
            this.channel.setTcpMd5Sig(keys);
            return this;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    public EpollSocketChannelConfig setTcpQuickAck(final boolean quickAck) {
        try {
            this.channel.fd().setTcpQuickAck(quickAck);
            return this;
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    public boolean isTcpQuickAck() {
        try {
            return this.channel.fd().isTcpQuickAck();
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
    }
    
    @Override
    public boolean isAllowHalfClosure() {
        return this.allowHalfClosure;
    }
    
    @Override
    public EpollSocketChannelConfig setAllowHalfClosure(final boolean allowHalfClosure) {
        this.allowHalfClosure = allowHalfClosure;
        return this;
    }
    
    @Override
    public EpollSocketChannelConfig setConnectTimeoutMillis(final int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }
    
    @Deprecated
    @Override
    public EpollSocketChannelConfig setMaxMessagesPerRead(final int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }
    
    @Override
    public EpollSocketChannelConfig setWriteSpinCount(final int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }
    
    @Override
    public EpollSocketChannelConfig setAllocator(final ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }
    
    @Override
    public EpollSocketChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator allocator) {
        super.setRecvByteBufAllocator(allocator);
        return this;
    }
    
    @Override
    public EpollSocketChannelConfig setAutoRead(final boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }
    
    @Override
    public EpollSocketChannelConfig setAutoClose(final boolean autoClose) {
        super.setAutoClose(autoClose);
        return this;
    }
    
    @Deprecated
    @Override
    public EpollSocketChannelConfig setWriteBufferHighWaterMark(final int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }
    
    @Deprecated
    @Override
    public EpollSocketChannelConfig setWriteBufferLowWaterMark(final int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }
    
    @Override
    public EpollSocketChannelConfig setWriteBufferWaterMark(final WriteBufferWaterMark writeBufferWaterMark) {
        super.setWriteBufferWaterMark(writeBufferWaterMark);
        return this;
    }
    
    @Override
    public EpollSocketChannelConfig setMessageSizeEstimator(final MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }
    
    @Override
    public EpollSocketChannelConfig setEpollMode(final EpollMode mode) {
        super.setEpollMode(mode);
        return this;
    }
}
