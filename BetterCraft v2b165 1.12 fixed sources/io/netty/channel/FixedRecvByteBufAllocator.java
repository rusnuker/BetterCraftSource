// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

public class FixedRecvByteBufAllocator extends DefaultMaxMessagesRecvByteBufAllocator
{
    private final int bufferSize;
    
    public FixedRecvByteBufAllocator(final int bufferSize) {
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("bufferSize must greater than 0: " + bufferSize);
        }
        this.bufferSize = bufferSize;
    }
    
    @Override
    public RecvByteBufAllocator.Handle newHandle() {
        return new HandleImpl(this.bufferSize);
    }
    
    private final class HandleImpl extends MaxMessageHandle
    {
        private final int bufferSize;
        
        public HandleImpl(final int bufferSize) {
            this.bufferSize = bufferSize;
        }
        
        @Override
        public int guess() {
            return this.bufferSize;
        }
    }
}
