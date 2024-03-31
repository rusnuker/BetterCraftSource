// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

import io.netty.util.internal.ObjectUtil;
import java.util.Iterator;
import java.util.IdentityHashMap;
import java.util.Map;
import io.netty.buffer.ByteBufAllocator;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class DefaultChannelConfig implements ChannelConfig
{
    private static final MessageSizeEstimator DEFAULT_MSG_SIZE_ESTIMATOR;
    private static final int DEFAULT_CONNECT_TIMEOUT = 30000;
    private static final AtomicIntegerFieldUpdater<DefaultChannelConfig> AUTOREAD_UPDATER;
    private static final AtomicReferenceFieldUpdater<DefaultChannelConfig, WriteBufferWaterMark> WATERMARK_UPDATER;
    protected final Channel channel;
    private volatile ByteBufAllocator allocator;
    private volatile RecvByteBufAllocator rcvBufAllocator;
    private volatile MessageSizeEstimator msgSizeEstimator;
    private volatile int connectTimeoutMillis;
    private volatile int writeSpinCount;
    private volatile int autoRead;
    private volatile boolean autoClose;
    private volatile WriteBufferWaterMark writeBufferWaterMark;
    private volatile boolean pinEventExecutor;
    
    public DefaultChannelConfig(final Channel channel) {
        this(channel, new AdaptiveRecvByteBufAllocator());
    }
    
    protected DefaultChannelConfig(final Channel channel, final RecvByteBufAllocator allocator) {
        this.allocator = ByteBufAllocator.DEFAULT;
        this.msgSizeEstimator = DefaultChannelConfig.DEFAULT_MSG_SIZE_ESTIMATOR;
        this.connectTimeoutMillis = 30000;
        this.writeSpinCount = 16;
        this.autoRead = 1;
        this.autoClose = true;
        this.writeBufferWaterMark = WriteBufferWaterMark.DEFAULT;
        this.pinEventExecutor = true;
        this.setRecvByteBufAllocator(allocator, channel.metadata());
        this.channel = channel;
    }
    
    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(null, ChannelOption.CONNECT_TIMEOUT_MILLIS, ChannelOption.MAX_MESSAGES_PER_READ, ChannelOption.WRITE_SPIN_COUNT, ChannelOption.ALLOCATOR, ChannelOption.AUTO_READ, ChannelOption.AUTO_CLOSE, ChannelOption.RCVBUF_ALLOCATOR, ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, ChannelOption.WRITE_BUFFER_WATER_MARK, ChannelOption.MESSAGE_SIZE_ESTIMATOR, ChannelOption.SINGLE_EVENTEXECUTOR_PER_GROUP);
    }
    
    protected Map<ChannelOption<?>, Object> getOptions(Map<ChannelOption<?>, Object> result, final ChannelOption<?>... options) {
        if (result == null) {
            result = new IdentityHashMap<ChannelOption<?>, Object>();
        }
        for (final ChannelOption<?> o : options) {
            result.put(o, this.getOption(o));
        }
        return result;
    }
    
    @Override
    public boolean setOptions(final Map<ChannelOption<?>, ?> options) {
        if (options == null) {
            throw new NullPointerException("options");
        }
        boolean setAllOptions = true;
        for (final Map.Entry<ChannelOption<?>, ?> e : options.entrySet()) {
            if (!this.setOption(e.getKey(), e.getValue())) {
                setAllOptions = false;
            }
        }
        return setAllOptions;
    }
    
    @Override
    public <T> T getOption(final ChannelOption<T> option) {
        if (option == null) {
            throw new NullPointerException("option");
        }
        if (option == ChannelOption.CONNECT_TIMEOUT_MILLIS) {
            return (T)Integer.valueOf(this.getConnectTimeoutMillis());
        }
        if (option == ChannelOption.MAX_MESSAGES_PER_READ) {
            return (T)Integer.valueOf(this.getMaxMessagesPerRead());
        }
        if (option == ChannelOption.WRITE_SPIN_COUNT) {
            return (T)Integer.valueOf(this.getWriteSpinCount());
        }
        if (option == ChannelOption.ALLOCATOR) {
            return (T)this.getAllocator();
        }
        if (option == ChannelOption.RCVBUF_ALLOCATOR) {
            return this.getRecvByteBufAllocator();
        }
        if (option == ChannelOption.AUTO_READ) {
            return (T)Boolean.valueOf(this.isAutoRead());
        }
        if (option == ChannelOption.AUTO_CLOSE) {
            return (T)Boolean.valueOf(this.isAutoClose());
        }
        if (option == ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK) {
            return (T)Integer.valueOf(this.getWriteBufferHighWaterMark());
        }
        if (option == ChannelOption.WRITE_BUFFER_LOW_WATER_MARK) {
            return (T)Integer.valueOf(this.getWriteBufferLowWaterMark());
        }
        if (option == ChannelOption.WRITE_BUFFER_WATER_MARK) {
            return (T)this.getWriteBufferWaterMark();
        }
        if (option == ChannelOption.MESSAGE_SIZE_ESTIMATOR) {
            return (T)this.getMessageSizeEstimator();
        }
        if (option == ChannelOption.SINGLE_EVENTEXECUTOR_PER_GROUP) {
            return (T)Boolean.valueOf(this.getPinEventExecutorPerGroup());
        }
        return null;
    }
    
    @Override
    public <T> boolean setOption(final ChannelOption<T> option, final T value) {
        this.validate(option, value);
        if (option == ChannelOption.CONNECT_TIMEOUT_MILLIS) {
            this.setConnectTimeoutMillis((int)value);
        }
        else if (option == ChannelOption.MAX_MESSAGES_PER_READ) {
            this.setMaxMessagesPerRead((int)value);
        }
        else if (option == ChannelOption.WRITE_SPIN_COUNT) {
            this.setWriteSpinCount((int)value);
        }
        else if (option == ChannelOption.ALLOCATOR) {
            this.setAllocator((ByteBufAllocator)value);
        }
        else if (option == ChannelOption.RCVBUF_ALLOCATOR) {
            this.setRecvByteBufAllocator((RecvByteBufAllocator)value);
        }
        else if (option == ChannelOption.AUTO_READ) {
            this.setAutoRead((boolean)value);
        }
        else if (option == ChannelOption.AUTO_CLOSE) {
            this.setAutoClose((boolean)value);
        }
        else if (option == ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK) {
            this.setWriteBufferHighWaterMark((int)value);
        }
        else if (option == ChannelOption.WRITE_BUFFER_LOW_WATER_MARK) {
            this.setWriteBufferLowWaterMark((int)value);
        }
        else if (option == ChannelOption.WRITE_BUFFER_WATER_MARK) {
            this.setWriteBufferWaterMark((WriteBufferWaterMark)value);
        }
        else if (option == ChannelOption.MESSAGE_SIZE_ESTIMATOR) {
            this.setMessageSizeEstimator((MessageSizeEstimator)value);
        }
        else {
            if (option != ChannelOption.SINGLE_EVENTEXECUTOR_PER_GROUP) {
                return false;
            }
            this.setPinEventExecutorPerGroup((boolean)value);
        }
        return true;
    }
    
    protected <T> void validate(final ChannelOption<T> option, final T value) {
        if (option == null) {
            throw new NullPointerException("option");
        }
        option.validate(value);
    }
    
    @Override
    public int getConnectTimeoutMillis() {
        return this.connectTimeoutMillis;
    }
    
    @Override
    public ChannelConfig setConnectTimeoutMillis(final int connectTimeoutMillis) {
        if (connectTimeoutMillis < 0) {
            throw new IllegalArgumentException(String.format("connectTimeoutMillis: %d (expected: >= 0)", connectTimeoutMillis));
        }
        this.connectTimeoutMillis = connectTimeoutMillis;
        return this;
    }
    
    @Deprecated
    @Override
    public int getMaxMessagesPerRead() {
        try {
            final MaxMessagesRecvByteBufAllocator allocator = this.getRecvByteBufAllocator();
            return allocator.maxMessagesPerRead();
        }
        catch (final ClassCastException e) {
            throw new IllegalStateException("getRecvByteBufAllocator() must return an object of type MaxMessagesRecvByteBufAllocator", e);
        }
    }
    
    @Deprecated
    @Override
    public ChannelConfig setMaxMessagesPerRead(final int maxMessagesPerRead) {
        try {
            final MaxMessagesRecvByteBufAllocator allocator = this.getRecvByteBufAllocator();
            allocator.maxMessagesPerRead(maxMessagesPerRead);
            return this;
        }
        catch (final ClassCastException e) {
            throw new IllegalStateException("getRecvByteBufAllocator() must return an object of type MaxMessagesRecvByteBufAllocator", e);
        }
    }
    
    @Override
    public int getWriteSpinCount() {
        return this.writeSpinCount;
    }
    
    @Override
    public ChannelConfig setWriteSpinCount(final int writeSpinCount) {
        if (writeSpinCount <= 0) {
            throw new IllegalArgumentException("writeSpinCount must be a positive integer.");
        }
        this.writeSpinCount = writeSpinCount;
        return this;
    }
    
    @Override
    public ByteBufAllocator getAllocator() {
        return this.allocator;
    }
    
    @Override
    public ChannelConfig setAllocator(final ByteBufAllocator allocator) {
        if (allocator == null) {
            throw new NullPointerException("allocator");
        }
        this.allocator = allocator;
        return this;
    }
    
    @Override
    public <T extends RecvByteBufAllocator> T getRecvByteBufAllocator() {
        return (T)this.rcvBufAllocator;
    }
    
    @Override
    public ChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator allocator) {
        this.rcvBufAllocator = ObjectUtil.checkNotNull(allocator, "allocator");
        return this;
    }
    
    private void setRecvByteBufAllocator(final RecvByteBufAllocator allocator, final ChannelMetadata metadata) {
        if (allocator instanceof MaxMessagesRecvByteBufAllocator) {
            ((MaxMessagesRecvByteBufAllocator)allocator).maxMessagesPerRead(metadata.defaultMaxMessagesPerRead());
        }
        else if (allocator == null) {
            throw new NullPointerException("allocator");
        }
        this.setRecvByteBufAllocator(allocator);
    }
    
    @Override
    public boolean isAutoRead() {
        return this.autoRead == 1;
    }
    
    @Override
    public ChannelConfig setAutoRead(final boolean autoRead) {
        final boolean oldAutoRead = DefaultChannelConfig.AUTOREAD_UPDATER.getAndSet(this, autoRead ? 1 : 0) == 1;
        if (autoRead && !oldAutoRead) {
            this.channel.read();
        }
        else if (!autoRead && oldAutoRead) {
            this.autoReadCleared();
        }
        return this;
    }
    
    protected void autoReadCleared() {
    }
    
    @Override
    public boolean isAutoClose() {
        return this.autoClose;
    }
    
    @Override
    public ChannelConfig setAutoClose(final boolean autoClose) {
        this.autoClose = autoClose;
        return this;
    }
    
    @Override
    public int getWriteBufferHighWaterMark() {
        return this.writeBufferWaterMark.high();
    }
    
    @Override
    public ChannelConfig setWriteBufferHighWaterMark(final int writeBufferHighWaterMark) {
        if (writeBufferHighWaterMark < 0) {
            throw new IllegalArgumentException("writeBufferHighWaterMark must be >= 0");
        }
        while (true) {
            final WriteBufferWaterMark waterMark = this.writeBufferWaterMark;
            if (writeBufferHighWaterMark < waterMark.low()) {
                throw new IllegalArgumentException("writeBufferHighWaterMark cannot be less than writeBufferLowWaterMark (" + waterMark.low() + "): " + writeBufferHighWaterMark);
            }
            if (DefaultChannelConfig.WATERMARK_UPDATER.compareAndSet(this, waterMark, new WriteBufferWaterMark(waterMark.low(), writeBufferHighWaterMark, false))) {
                return this;
            }
        }
    }
    
    @Override
    public int getWriteBufferLowWaterMark() {
        return this.writeBufferWaterMark.low();
    }
    
    @Override
    public ChannelConfig setWriteBufferLowWaterMark(final int writeBufferLowWaterMark) {
        if (writeBufferLowWaterMark < 0) {
            throw new IllegalArgumentException("writeBufferLowWaterMark must be >= 0");
        }
        while (true) {
            final WriteBufferWaterMark waterMark = this.writeBufferWaterMark;
            if (writeBufferLowWaterMark > waterMark.high()) {
                throw new IllegalArgumentException("writeBufferLowWaterMark cannot be greater than writeBufferHighWaterMark (" + waterMark.high() + "): " + writeBufferLowWaterMark);
            }
            if (DefaultChannelConfig.WATERMARK_UPDATER.compareAndSet(this, waterMark, new WriteBufferWaterMark(writeBufferLowWaterMark, waterMark.high(), false))) {
                return this;
            }
        }
    }
    
    @Override
    public ChannelConfig setWriteBufferWaterMark(final WriteBufferWaterMark writeBufferWaterMark) {
        this.writeBufferWaterMark = ObjectUtil.checkNotNull(writeBufferWaterMark, "writeBufferWaterMark");
        return this;
    }
    
    @Override
    public WriteBufferWaterMark getWriteBufferWaterMark() {
        return this.writeBufferWaterMark;
    }
    
    @Override
    public MessageSizeEstimator getMessageSizeEstimator() {
        return this.msgSizeEstimator;
    }
    
    @Override
    public ChannelConfig setMessageSizeEstimator(final MessageSizeEstimator estimator) {
        if (estimator == null) {
            throw new NullPointerException("estimator");
        }
        this.msgSizeEstimator = estimator;
        return this;
    }
    
    private ChannelConfig setPinEventExecutorPerGroup(final boolean pinEventExecutor) {
        this.pinEventExecutor = pinEventExecutor;
        return this;
    }
    
    private boolean getPinEventExecutorPerGroup() {
        return this.pinEventExecutor;
    }
    
    static {
        DEFAULT_MSG_SIZE_ESTIMATOR = DefaultMessageSizeEstimator.DEFAULT;
        AUTOREAD_UPDATER = AtomicIntegerFieldUpdater.newUpdater(DefaultChannelConfig.class, "autoRead");
        WATERMARK_UPDATER = AtomicReferenceFieldUpdater.newUpdater(DefaultChannelConfig.class, WriteBufferWaterMark.class, "writeBufferWaterMark");
    }
}
