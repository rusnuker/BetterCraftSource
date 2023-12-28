// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

import io.netty.util.UncheckedBooleanSupplier;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.internal.ObjectUtil;

public interface RecvByteBufAllocator
{
    Handle newHandle();
    
    public static class DelegatingHandle implements Handle
    {
        private final Handle delegate;
        
        public DelegatingHandle(final Handle delegate) {
            this.delegate = ObjectUtil.checkNotNull(delegate, "delegate");
        }
        
        protected final Handle delegate() {
            return this.delegate;
        }
        
        @Override
        public ByteBuf allocate(final ByteBufAllocator alloc) {
            return this.delegate.allocate(alloc);
        }
        
        @Override
        public int guess() {
            return this.delegate.guess();
        }
        
        @Override
        public void reset(final ChannelConfig config) {
            this.delegate.reset(config);
        }
        
        @Override
        public void incMessagesRead(final int numMessages) {
            this.delegate.incMessagesRead(numMessages);
        }
        
        @Override
        public void lastBytesRead(final int bytes) {
            this.delegate.lastBytesRead(bytes);
        }
        
        @Override
        public int lastBytesRead() {
            return this.delegate.lastBytesRead();
        }
        
        @Override
        public boolean continueReading() {
            return this.delegate.continueReading();
        }
        
        @Override
        public int attemptedBytesRead() {
            return this.delegate.attemptedBytesRead();
        }
        
        @Override
        public void attemptedBytesRead(final int bytes) {
            this.delegate.attemptedBytesRead(bytes);
        }
        
        @Override
        public void readComplete() {
            this.delegate.readComplete();
        }
    }
    
    @Deprecated
    public interface Handle
    {
        ByteBuf allocate(final ByteBufAllocator p0);
        
        int guess();
        
        void reset(final ChannelConfig p0);
        
        void incMessagesRead(final int p0);
        
        void lastBytesRead(final int p0);
        
        int lastBytesRead();
        
        void attemptedBytesRead(final int p0);
        
        int attemptedBytesRead();
        
        boolean continueReading();
        
        void readComplete();
    }
    
    public interface ExtendedHandle extends Handle
    {
        boolean continueReading(final UncheckedBooleanSupplier p0);
    }
}
