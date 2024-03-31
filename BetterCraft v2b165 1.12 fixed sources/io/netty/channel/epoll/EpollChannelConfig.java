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
import io.netty.channel.ChannelOption;
import java.util.Map;
import io.netty.channel.Channel;
import io.netty.channel.DefaultChannelConfig;

public class EpollChannelConfig extends DefaultChannelConfig
{
    final AbstractEpollChannel channel;
    
    EpollChannelConfig(final AbstractEpollChannel channel) {
        super(channel);
        this.channel = channel;
    }
    
    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(super.getOptions(), EpollChannelOption.EPOLL_MODE);
    }
    
    @Override
    public <T> T getOption(final ChannelOption<T> option) {
        if (option == EpollChannelOption.EPOLL_MODE) {
            return (T)this.getEpollMode();
        }
        return super.getOption(option);
    }
    
    @Override
    public <T> boolean setOption(final ChannelOption<T> option, final T value) {
        this.validate(option, value);
        if (option == EpollChannelOption.EPOLL_MODE) {
            this.setEpollMode((EpollMode)value);
            return true;
        }
        return super.setOption(option, value);
    }
    
    @Override
    public EpollChannelConfig setConnectTimeoutMillis(final int connectTimeoutMillis) {
        super.setConnectTimeoutMillis(connectTimeoutMillis);
        return this;
    }
    
    @Deprecated
    @Override
    public EpollChannelConfig setMaxMessagesPerRead(final int maxMessagesPerRead) {
        super.setMaxMessagesPerRead(maxMessagesPerRead);
        return this;
    }
    
    @Override
    public EpollChannelConfig setWriteSpinCount(final int writeSpinCount) {
        super.setWriteSpinCount(writeSpinCount);
        return this;
    }
    
    @Override
    public EpollChannelConfig setAllocator(final ByteBufAllocator allocator) {
        super.setAllocator(allocator);
        return this;
    }
    
    @Override
    public EpollChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator allocator) {
        if (!(allocator.newHandle() instanceof RecvByteBufAllocator.ExtendedHandle)) {
            throw new IllegalArgumentException("allocator.newHandle() must return an object of type: " + RecvByteBufAllocator.ExtendedHandle.class);
        }
        super.setRecvByteBufAllocator(allocator);
        return this;
    }
    
    @Override
    public EpollChannelConfig setAutoRead(final boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }
    
    @Deprecated
    @Override
    public EpollChannelConfig setWriteBufferHighWaterMark(final int writeBufferHighWaterMark) {
        super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
        return this;
    }
    
    @Deprecated
    @Override
    public EpollChannelConfig setWriteBufferLowWaterMark(final int writeBufferLowWaterMark) {
        super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
        return this;
    }
    
    @Override
    public EpollChannelConfig setWriteBufferWaterMark(final WriteBufferWaterMark writeBufferWaterMark) {
        super.setWriteBufferWaterMark(writeBufferWaterMark);
        return this;
    }
    
    @Override
    public EpollChannelConfig setMessageSizeEstimator(final MessageSizeEstimator estimator) {
        super.setMessageSizeEstimator(estimator);
        return this;
    }
    
    public EpollMode getEpollMode() {
        return this.channel.isFlagSet(Native.EPOLLET) ? EpollMode.EDGE_TRIGGERED : EpollMode.LEVEL_TRIGGERED;
    }
    
    public EpollChannelConfig setEpollMode(final EpollMode mode) {
        if (mode == null) {
            throw new NullPointerException("mode");
        }
        try {
            switch (mode) {
                case EDGE_TRIGGERED: {
                    this.checkChannelNotRegistered();
                    this.channel.setFlag(Native.EPOLLET);
                    break;
                }
                case LEVEL_TRIGGERED: {
                    this.checkChannelNotRegistered();
                    this.channel.clearFlag(Native.EPOLLET);
                    break;
                }
                default: {
                    throw new Error();
                }
            }
        }
        catch (final IOException e) {
            throw new ChannelException(e);
        }
        return this;
    }
    
    private void checkChannelNotRegistered() {
        if (this.channel.isRegistered()) {
            throw new IllegalStateException("EpollMode can only be changed before channel is registered");
        }
    }
    
    @Override
    protected final void autoReadCleared() {
        this.channel.clearEpollIn();
    }
}
