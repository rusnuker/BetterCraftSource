// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.UncheckedBooleanSupplier;

public abstract class DefaultMaxMessagesRecvByteBufAllocator implements MaxMessagesRecvByteBufAllocator
{
    private volatile int maxMessagesPerRead;
    
    public DefaultMaxMessagesRecvByteBufAllocator() {
        this(1);
    }
    
    public DefaultMaxMessagesRecvByteBufAllocator(final int maxMessagesPerRead) {
        this.maxMessagesPerRead(maxMessagesPerRead);
    }
    
    @Override
    public int maxMessagesPerRead() {
        return this.maxMessagesPerRead;
    }
    
    @Override
    public MaxMessagesRecvByteBufAllocator maxMessagesPerRead(final int maxMessagesPerRead) {
        if (maxMessagesPerRead <= 0) {
            throw new IllegalArgumentException("maxMessagesPerRead: " + maxMessagesPerRead + " (expected: > 0)");
        }
        this.maxMessagesPerRead = maxMessagesPerRead;
        return this;
    }
    
    public abstract class MaxMessageHandle implements RecvByteBufAllocator.ExtendedHandle
    {
        private ChannelConfig config;
        private int maxMessagePerRead;
        private int totalMessages;
        private int totalBytesRead;
        private int attemptedBytesRead;
        private int lastBytesRead;
        private final UncheckedBooleanSupplier defaultMaybeMoreSupplier;
        
        public MaxMessageHandle() {
            this.defaultMaybeMoreSupplier = new UncheckedBooleanSupplier() {
                @Override
                public boolean get() {
                    return MaxMessageHandle.this.attemptedBytesRead == MaxMessageHandle.this.lastBytesRead;
                }
            };
        }
        
        @Override
        public void reset(final ChannelConfig config) {
            this.config = config;
            this.maxMessagePerRead = DefaultMaxMessagesRecvByteBufAllocator.this.maxMessagesPerRead();
            final int n = 0;
            this.totalBytesRead = n;
            this.totalMessages = n;
        }
        
        @Override
        public ByteBuf allocate(final ByteBufAllocator alloc) {
            return alloc.ioBuffer(this.guess());
        }
        
        @Override
        public final void incMessagesRead(final int amt) {
            this.totalMessages += amt;
        }
        
        @Override
        public final void lastBytesRead(final int bytes) {
            this.lastBytesRead = bytes;
            if (bytes > 0) {
                this.totalBytesRead += bytes;
            }
        }
        
        @Override
        public final int lastBytesRead() {
            return this.lastBytesRead;
        }
        
        @Override
        public boolean continueReading() {
            return this.continueReading(this.defaultMaybeMoreSupplier);
        }
        
        @Override
        public boolean continueReading(final UncheckedBooleanSupplier maybeMoreDataSupplier) {
            return this.config.isAutoRead() && maybeMoreDataSupplier.get() && this.totalMessages < this.maxMessagePerRead && this.totalBytesRead > 0;
        }
        
        @Override
        public void readComplete() {
        }
        
        @Override
        public int attemptedBytesRead() {
            return this.attemptedBytesRead;
        }
        
        @Override
        public void attemptedBytesRead(final int bytes) {
            this.attemptedBytesRead = bytes;
        }
        
        protected final int totalBytesRead() {
            return (this.totalBytesRead < 0) ? Integer.MAX_VALUE : this.totalBytesRead;
        }
    }
}
