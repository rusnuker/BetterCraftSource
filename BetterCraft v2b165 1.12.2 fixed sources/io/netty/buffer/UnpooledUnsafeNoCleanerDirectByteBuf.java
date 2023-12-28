// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import io.netty.util.internal.PlatformDependent;
import java.nio.ByteBuffer;

class UnpooledUnsafeNoCleanerDirectByteBuf extends UnpooledUnsafeDirectByteBuf
{
    UnpooledUnsafeNoCleanerDirectByteBuf(final ByteBufAllocator alloc, final int initialCapacity, final int maxCapacity) {
        super(alloc, initialCapacity, maxCapacity);
    }
    
    @Override
    protected ByteBuffer allocateDirect(final int initialCapacity) {
        return PlatformDependent.allocateDirectNoCleaner(initialCapacity);
    }
    
    ByteBuffer reallocateDirect(final ByteBuffer oldBuffer, final int initialCapacity) {
        return PlatformDependent.reallocateDirectNoCleaner(oldBuffer, initialCapacity);
    }
    
    @Override
    protected void freeDirect(final ByteBuffer buffer) {
        PlatformDependent.freeDirectNoCleaner(buffer);
    }
    
    @Override
    public ByteBuf capacity(final int newCapacity) {
        this.checkNewCapacity(newCapacity);
        final int readerIndex = this.readerIndex();
        int writerIndex = this.writerIndex();
        final int oldCapacity = this.capacity();
        if (newCapacity > oldCapacity) {
            final ByteBuffer oldBuffer = this.buffer;
            final ByteBuffer newBuffer = this.reallocateDirect(oldBuffer, newCapacity);
            this.setByteBuffer(newBuffer, false);
        }
        else if (newCapacity < oldCapacity) {
            final ByteBuffer oldBuffer = this.buffer;
            final ByteBuffer newBuffer = this.allocateDirect(newCapacity);
            if (readerIndex < newCapacity) {
                if (writerIndex > newCapacity) {
                    writerIndex = newCapacity;
                    this.writerIndex(writerIndex);
                }
                oldBuffer.position(readerIndex).limit(writerIndex);
                newBuffer.position(readerIndex).limit(writerIndex);
                newBuffer.put(oldBuffer);
                newBuffer.clear();
            }
            else {
                this.setIndex(newCapacity, newCapacity);
            }
            this.setByteBuffer(newBuffer, true);
        }
        return this;
    }
}
