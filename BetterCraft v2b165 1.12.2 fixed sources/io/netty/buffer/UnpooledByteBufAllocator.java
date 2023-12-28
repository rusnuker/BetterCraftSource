// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import io.netty.util.internal.StringUtil;
import io.netty.util.internal.LongCounter;
import java.nio.ByteBuffer;
import io.netty.util.internal.PlatformDependent;

public final class UnpooledByteBufAllocator extends AbstractByteBufAllocator implements ByteBufAllocatorMetricProvider
{
    private final UnpooledByteBufAllocatorMetric metric;
    private final boolean disableLeakDetector;
    public static final UnpooledByteBufAllocator DEFAULT;
    
    public UnpooledByteBufAllocator(final boolean preferDirect) {
        this(preferDirect, false);
    }
    
    public UnpooledByteBufAllocator(final boolean preferDirect, final boolean disableLeakDetector) {
        super(preferDirect);
        this.metric = new UnpooledByteBufAllocatorMetric();
        this.disableLeakDetector = disableLeakDetector;
    }
    
    @Override
    protected ByteBuf newHeapBuffer(final int initialCapacity, final int maxCapacity) {
        return PlatformDependent.hasUnsafe() ? new InstrumentedUnpooledUnsafeHeapByteBuf(this, initialCapacity, maxCapacity) : new InstrumentedUnpooledHeapByteBuf(this, initialCapacity, maxCapacity);
    }
    
    @Override
    protected ByteBuf newDirectBuffer(final int initialCapacity, final int maxCapacity) {
        ByteBuf buf;
        if (PlatformDependent.hasUnsafe()) {
            buf = (PlatformDependent.useDirectBufferNoCleaner() ? new InstrumentedUnpooledUnsafeNoCleanerDirectByteBuf(this, initialCapacity, maxCapacity) : new InstrumentedUnpooledUnsafeDirectByteBuf(this, initialCapacity, maxCapacity));
        }
        else {
            buf = new InstrumentedUnpooledDirectByteBuf(this, initialCapacity, maxCapacity);
        }
        return this.disableLeakDetector ? buf : AbstractByteBufAllocator.toLeakAwareBuffer(buf);
    }
    
    @Override
    public CompositeByteBuf compositeHeapBuffer(final int maxNumComponents) {
        final CompositeByteBuf buf = new CompositeByteBuf(this, false, maxNumComponents);
        return this.disableLeakDetector ? buf : AbstractByteBufAllocator.toLeakAwareBuffer(buf);
    }
    
    @Override
    public CompositeByteBuf compositeDirectBuffer(final int maxNumComponents) {
        final CompositeByteBuf buf = new CompositeByteBuf(this, true, maxNumComponents);
        return this.disableLeakDetector ? buf : AbstractByteBufAllocator.toLeakAwareBuffer(buf);
    }
    
    @Override
    public boolean isDirectBufferPooled() {
        return false;
    }
    
    @Override
    public ByteBufAllocatorMetric metric() {
        return this.metric;
    }
    
    void incrementDirect(final int amount) {
        this.metric.directCounter.add(amount);
    }
    
    void decrementDirect(final int amount) {
        this.metric.directCounter.add(-amount);
    }
    
    void incrementHeap(final int amount) {
        this.metric.heapCounter.add(amount);
    }
    
    void decrementHeap(final int amount) {
        this.metric.heapCounter.add(-amount);
    }
    
    static {
        DEFAULT = new UnpooledByteBufAllocator(PlatformDependent.directBufferPreferred());
    }
    
    private static final class InstrumentedUnpooledUnsafeHeapByteBuf extends UnpooledUnsafeHeapByteBuf
    {
        InstrumentedUnpooledUnsafeHeapByteBuf(final UnpooledByteBufAllocator alloc, final int initialCapacity, final int maxCapacity) {
            super(alloc, initialCapacity, maxCapacity);
        }
        
        @Override
        byte[] allocateArray(final int initialCapacity) {
            final byte[] bytes = super.allocateArray(initialCapacity);
            ((UnpooledByteBufAllocator)this.alloc()).incrementHeap(bytes.length);
            return bytes;
        }
        
        @Override
        void freeArray(final byte[] array) {
            final int length = array.length;
            super.freeArray(array);
            ((UnpooledByteBufAllocator)this.alloc()).decrementHeap(length);
        }
    }
    
    private static final class InstrumentedUnpooledHeapByteBuf extends UnpooledHeapByteBuf
    {
        InstrumentedUnpooledHeapByteBuf(final UnpooledByteBufAllocator alloc, final int initialCapacity, final int maxCapacity) {
            super(alloc, initialCapacity, maxCapacity);
        }
        
        @Override
        byte[] allocateArray(final int initialCapacity) {
            final byte[] bytes = super.allocateArray(initialCapacity);
            ((UnpooledByteBufAllocator)this.alloc()).incrementHeap(bytes.length);
            return bytes;
        }
        
        @Override
        void freeArray(final byte[] array) {
            final int length = array.length;
            super.freeArray(array);
            ((UnpooledByteBufAllocator)this.alloc()).decrementHeap(length);
        }
    }
    
    private static final class InstrumentedUnpooledUnsafeNoCleanerDirectByteBuf extends UnpooledUnsafeNoCleanerDirectByteBuf
    {
        InstrumentedUnpooledUnsafeNoCleanerDirectByteBuf(final UnpooledByteBufAllocator alloc, final int initialCapacity, final int maxCapacity) {
            super(alloc, initialCapacity, maxCapacity);
        }
        
        @Override
        protected ByteBuffer allocateDirect(final int initialCapacity) {
            final ByteBuffer buffer = super.allocateDirect(initialCapacity);
            ((UnpooledByteBufAllocator)this.alloc()).incrementDirect(buffer.capacity());
            return buffer;
        }
        
        @Override
        ByteBuffer reallocateDirect(final ByteBuffer oldBuffer, final int initialCapacity) {
            final int capacity = oldBuffer.capacity();
            final ByteBuffer buffer = super.reallocateDirect(oldBuffer, initialCapacity);
            ((UnpooledByteBufAllocator)this.alloc()).incrementDirect(buffer.capacity() - capacity);
            return buffer;
        }
        
        @Override
        protected void freeDirect(final ByteBuffer buffer) {
            final int capacity = buffer.capacity();
            super.freeDirect(buffer);
            ((UnpooledByteBufAllocator)this.alloc()).decrementDirect(capacity);
        }
    }
    
    private static final class InstrumentedUnpooledUnsafeDirectByteBuf extends UnpooledUnsafeDirectByteBuf
    {
        InstrumentedUnpooledUnsafeDirectByteBuf(final UnpooledByteBufAllocator alloc, final int initialCapacity, final int maxCapacity) {
            super(alloc, initialCapacity, maxCapacity);
        }
        
        @Override
        protected ByteBuffer allocateDirect(final int initialCapacity) {
            final ByteBuffer buffer = super.allocateDirect(initialCapacity);
            ((UnpooledByteBufAllocator)this.alloc()).incrementDirect(buffer.capacity());
            return buffer;
        }
        
        @Override
        protected void freeDirect(final ByteBuffer buffer) {
            final int capacity = buffer.capacity();
            super.freeDirect(buffer);
            ((UnpooledByteBufAllocator)this.alloc()).decrementDirect(capacity);
        }
    }
    
    private static final class InstrumentedUnpooledDirectByteBuf extends UnpooledDirectByteBuf
    {
        InstrumentedUnpooledDirectByteBuf(final UnpooledByteBufAllocator alloc, final int initialCapacity, final int maxCapacity) {
            super(alloc, initialCapacity, maxCapacity);
        }
        
        @Override
        protected ByteBuffer allocateDirect(final int initialCapacity) {
            final ByteBuffer buffer = super.allocateDirect(initialCapacity);
            ((UnpooledByteBufAllocator)this.alloc()).incrementDirect(buffer.capacity());
            return buffer;
        }
        
        @Override
        protected void freeDirect(final ByteBuffer buffer) {
            final int capacity = buffer.capacity();
            super.freeDirect(buffer);
            ((UnpooledByteBufAllocator)this.alloc()).decrementDirect(capacity);
        }
    }
    
    private static final class UnpooledByteBufAllocatorMetric implements ByteBufAllocatorMetric
    {
        final LongCounter directCounter;
        final LongCounter heapCounter;
        
        private UnpooledByteBufAllocatorMetric() {
            this.directCounter = PlatformDependent.newLongCounter();
            this.heapCounter = PlatformDependent.newLongCounter();
        }
        
        @Override
        public long usedHeapMemory() {
            return this.heapCounter.value();
        }
        
        @Override
        public long usedDirectMemory() {
            return this.directCounter.value();
        }
        
        @Override
        public String toString() {
            return StringUtil.simpleClassName(this) + "(usedHeapMemory: " + this.usedHeapMemory() + "; usedDirectMemory: " + this.usedDirectMemory() + ')';
        }
    }
}
