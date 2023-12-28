// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import java.nio.ByteBuffer;
import io.netty.util.internal.StringUtil;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.atomic.AtomicInteger;
import io.netty.util.internal.LongCounter;
import java.util.List;

abstract class PoolArena<T> implements PoolArenaMetric
{
    static final boolean HAS_UNSAFE;
    static final int numTinySubpagePools = 32;
    final PooledByteBufAllocator parent;
    private final int maxOrder;
    final int pageSize;
    final int pageShifts;
    final int chunkSize;
    final int subpageOverflowMask;
    final int numSmallSubpagePools;
    final int directMemoryCacheAlignment;
    final int directMemoryCacheAlignmentMask;
    private final PoolSubpage<T>[] tinySubpagePools;
    private final PoolSubpage<T>[] smallSubpagePools;
    private final PoolChunkList<T> q050;
    private final PoolChunkList<T> q025;
    private final PoolChunkList<T> q000;
    private final PoolChunkList<T> qInit;
    private final PoolChunkList<T> q075;
    private final PoolChunkList<T> q100;
    private final List<PoolChunkListMetric> chunkListMetrics;
    private long allocationsNormal;
    private final LongCounter allocationsTiny;
    private final LongCounter allocationsSmall;
    private final LongCounter allocationsHuge;
    private final LongCounter activeBytesHuge;
    private long deallocationsTiny;
    private long deallocationsSmall;
    private long deallocationsNormal;
    private final LongCounter deallocationsHuge;
    final AtomicInteger numThreadCaches;
    
    protected PoolArena(final PooledByteBufAllocator parent, final int pageSize, final int maxOrder, final int pageShifts, final int chunkSize, final int cacheAlignment) {
        this.allocationsTiny = PlatformDependent.newLongCounter();
        this.allocationsSmall = PlatformDependent.newLongCounter();
        this.allocationsHuge = PlatformDependent.newLongCounter();
        this.activeBytesHuge = PlatformDependent.newLongCounter();
        this.deallocationsHuge = PlatformDependent.newLongCounter();
        this.numThreadCaches = new AtomicInteger();
        this.parent = parent;
        this.pageSize = pageSize;
        this.maxOrder = maxOrder;
        this.pageShifts = pageShifts;
        this.chunkSize = chunkSize;
        this.directMemoryCacheAlignment = cacheAlignment;
        this.directMemoryCacheAlignmentMask = cacheAlignment - 1;
        this.subpageOverflowMask = ~(pageSize - 1);
        this.tinySubpagePools = this.newSubpagePoolArray(32);
        for (int i = 0; i < this.tinySubpagePools.length; ++i) {
            this.tinySubpagePools[i] = this.newSubpagePoolHead(pageSize);
        }
        this.numSmallSubpagePools = pageShifts - 9;
        this.smallSubpagePools = this.newSubpagePoolArray(this.numSmallSubpagePools);
        for (int i = 0; i < this.smallSubpagePools.length; ++i) {
            this.smallSubpagePools[i] = this.newSubpagePoolHead(pageSize);
        }
        this.q100 = new PoolChunkList<T>(this, null, 100, Integer.MAX_VALUE, chunkSize);
        this.q075 = new PoolChunkList<T>(this, this.q100, 75, 100, chunkSize);
        this.q050 = new PoolChunkList<T>(this, this.q075, 50, 100, chunkSize);
        this.q025 = new PoolChunkList<T>(this, this.q050, 25, 75, chunkSize);
        this.q000 = new PoolChunkList<T>(this, this.q025, 1, 50, chunkSize);
        this.qInit = new PoolChunkList<T>(this, this.q000, Integer.MIN_VALUE, 25, chunkSize);
        this.q100.prevList(this.q075);
        this.q075.prevList(this.q050);
        this.q050.prevList(this.q025);
        this.q025.prevList(this.q000);
        this.q000.prevList(null);
        this.qInit.prevList(this.qInit);
        final List<PoolChunkListMetric> metrics = new ArrayList<PoolChunkListMetric>(6);
        metrics.add(this.qInit);
        metrics.add(this.q000);
        metrics.add(this.q025);
        metrics.add(this.q050);
        metrics.add(this.q075);
        metrics.add(this.q100);
        this.chunkListMetrics = Collections.unmodifiableList((List<? extends PoolChunkListMetric>)metrics);
    }
    
    private PoolSubpage<T> newSubpagePoolHead(final int pageSize) {
        final PoolSubpage<T> head = new PoolSubpage<T>(pageSize);
        head.prev = head;
        return head.next = head;
    }
    
    private PoolSubpage<T>[] newSubpagePoolArray(final int size) {
        return new PoolSubpage[size];
    }
    
    abstract boolean isDirect();
    
    PooledByteBuf<T> allocate(final PoolThreadCache cache, final int reqCapacity, final int maxCapacity) {
        final PooledByteBuf<T> buf = this.newByteBuf(maxCapacity);
        this.allocate(cache, buf, reqCapacity);
        return buf;
    }
    
    static int tinyIdx(final int normCapacity) {
        return normCapacity >>> 4;
    }
    
    static int smallIdx(final int normCapacity) {
        int tableIdx = 0;
        for (int i = normCapacity >>> 10; i != 0; i >>>= 1, ++tableIdx) {}
        return tableIdx;
    }
    
    boolean isTinyOrSmall(final int normCapacity) {
        return (normCapacity & this.subpageOverflowMask) == 0x0;
    }
    
    static boolean isTiny(final int normCapacity) {
        return (normCapacity & 0xFFFFFE00) == 0x0;
    }
    
    private void allocate(final PoolThreadCache cache, final PooledByteBuf<T> buf, final int reqCapacity) {
        final int normCapacity = this.normalizeCapacity(reqCapacity);
        if (this.isTinyOrSmall(normCapacity)) {
            final boolean tiny = isTiny(normCapacity);
            int tableIdx;
            PoolSubpage<T>[] table;
            if (tiny) {
                if (cache.allocateTiny(this, buf, reqCapacity, normCapacity)) {
                    return;
                }
                tableIdx = tinyIdx(normCapacity);
                table = this.tinySubpagePools;
            }
            else {
                if (cache.allocateSmall(this, buf, reqCapacity, normCapacity)) {
                    return;
                }
                tableIdx = smallIdx(normCapacity);
                table = this.smallSubpagePools;
            }
            final PoolSubpage<T> head = table[tableIdx];
            synchronized (head) {
                final PoolSubpage<T> s = head.next;
                if (s != head) {
                    assert s.doNotDestroy && s.elemSize == normCapacity;
                    final long handle = s.allocate();
                    assert handle >= 0L;
                    s.chunk.initBufWithSubpage(buf, handle, reqCapacity);
                    this.incTinySmallAllocation(tiny);
                    return;
                }
            }
            synchronized (this) {
                this.allocateNormal(buf, reqCapacity, normCapacity);
            }
            this.incTinySmallAllocation(tiny);
            return;
        }
        if (normCapacity <= this.chunkSize) {
            if (cache.allocateNormal(this, buf, reqCapacity, normCapacity)) {
                return;
            }
            synchronized (this) {
                this.allocateNormal(buf, reqCapacity, normCapacity);
                ++this.allocationsNormal;
            }
        }
        else {
            this.allocateHuge(buf, reqCapacity);
        }
    }
    
    private void allocateNormal(final PooledByteBuf<T> buf, final int reqCapacity, final int normCapacity) {
        if (this.q050.allocate(buf, reqCapacity, normCapacity) || this.q025.allocate(buf, reqCapacity, normCapacity) || this.q000.allocate(buf, reqCapacity, normCapacity) || this.qInit.allocate(buf, reqCapacity, normCapacity) || this.q075.allocate(buf, reqCapacity, normCapacity)) {
            return;
        }
        final PoolChunk<T> c = this.newChunk(this.pageSize, this.maxOrder, this.pageShifts, this.chunkSize);
        final long handle = c.allocate(normCapacity);
        assert handle > 0L;
        c.initBuf(buf, handle, reqCapacity);
        this.qInit.add(c);
    }
    
    private void incTinySmallAllocation(final boolean tiny) {
        if (tiny) {
            this.allocationsTiny.increment();
        }
        else {
            this.allocationsSmall.increment();
        }
    }
    
    private void allocateHuge(final PooledByteBuf<T> buf, final int reqCapacity) {
        final PoolChunk<T> chunk = this.newUnpooledChunk(reqCapacity);
        this.activeBytesHuge.add(chunk.chunkSize());
        buf.initUnpooled(chunk, reqCapacity);
        this.allocationsHuge.increment();
    }
    
    void free(final PoolChunk<T> chunk, final long handle, final int normCapacity, final PoolThreadCache cache) {
        if (chunk.unpooled) {
            final int size = chunk.chunkSize();
            this.destroyChunk(chunk);
            this.activeBytesHuge.add(-size);
            this.deallocationsHuge.increment();
        }
        else {
            final SizeClass sizeClass = this.sizeClass(normCapacity);
            if (cache != null && cache.add(this, chunk, handle, normCapacity, sizeClass)) {
                return;
            }
            this.freeChunk(chunk, handle, sizeClass);
        }
    }
    
    private SizeClass sizeClass(final int normCapacity) {
        if (!this.isTinyOrSmall(normCapacity)) {
            return SizeClass.Normal;
        }
        return isTiny(normCapacity) ? SizeClass.Tiny : SizeClass.Small;
    }
    
    void freeChunk(final PoolChunk<T> chunk, final long handle, final SizeClass sizeClass) {
        final boolean destroyChunk;
        synchronized (this) {
            switch (sizeClass) {
                case Normal: {
                    ++this.deallocationsNormal;
                    break;
                }
                case Small: {
                    ++this.deallocationsSmall;
                    break;
                }
                case Tiny: {
                    ++this.deallocationsTiny;
                    break;
                }
                default: {
                    throw new Error();
                }
            }
            destroyChunk = !chunk.parent.free(chunk, handle);
        }
        if (destroyChunk) {
            this.destroyChunk(chunk);
        }
    }
    
    PoolSubpage<T> findSubpagePoolHead(int elemSize) {
        int tableIdx;
        PoolSubpage<T>[] table;
        if (isTiny(elemSize)) {
            tableIdx = elemSize >>> 4;
            table = this.tinySubpagePools;
        }
        else {
            for (tableIdx = 0, elemSize >>>= 10; elemSize != 0; elemSize >>>= 1, ++tableIdx) {}
            table = this.smallSubpagePools;
        }
        return table[tableIdx];
    }
    
    int normalizeCapacity(final int reqCapacity) {
        if (reqCapacity < 0) {
            throw new IllegalArgumentException("capacity: " + reqCapacity + " (expected: 0+)");
        }
        if (reqCapacity >= this.chunkSize) {
            return (this.directMemoryCacheAlignment == 0) ? reqCapacity : this.alignCapacity(reqCapacity);
        }
        if (!isTiny(reqCapacity)) {
            int normalizedCapacity = reqCapacity;
            normalizedCapacity = (--normalizedCapacity | normalizedCapacity >>> 1);
            normalizedCapacity |= normalizedCapacity >>> 2;
            normalizedCapacity |= normalizedCapacity >>> 4;
            normalizedCapacity |= normalizedCapacity >>> 8;
            normalizedCapacity |= normalizedCapacity >>> 16;
            if (++normalizedCapacity < 0) {
                normalizedCapacity >>>= 1;
            }
            assert (normalizedCapacity & this.directMemoryCacheAlignmentMask) == 0x0;
            return normalizedCapacity;
        }
        else {
            if (this.directMemoryCacheAlignment > 0) {
                return this.alignCapacity(reqCapacity);
            }
            if ((reqCapacity & 0xF) == 0x0) {
                return reqCapacity;
            }
            return (reqCapacity & 0xFFFFFFF0) + 16;
        }
    }
    
    int alignCapacity(final int reqCapacity) {
        final int delta = reqCapacity & this.directMemoryCacheAlignmentMask;
        return (delta == 0) ? reqCapacity : (reqCapacity + this.directMemoryCacheAlignment - delta);
    }
    
    void reallocate(final PooledByteBuf<T> buf, final int newCapacity, final boolean freeOldMemory) {
        if (newCapacity < 0 || newCapacity > buf.maxCapacity()) {
            throw new IllegalArgumentException("newCapacity: " + newCapacity);
        }
        final int oldCapacity = buf.length;
        if (oldCapacity == newCapacity) {
            return;
        }
        final PoolChunk<T> oldChunk = buf.chunk;
        final long oldHandle = buf.handle;
        final T oldMemory = buf.memory;
        final int oldOffset = buf.offset;
        final int oldMaxLength = buf.maxLength;
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        this.allocate(this.parent.threadCache(), buf, newCapacity);
        if (newCapacity > oldCapacity) {
            this.memoryCopy(oldMemory, oldOffset, buf.memory, buf.offset, oldCapacity);
        }
        else if (newCapacity < oldCapacity) {
            if (readerIndex < newCapacity) {
                if (writerIndex > newCapacity) {
                    writerIndex = newCapacity;
                }
                this.memoryCopy(oldMemory, oldOffset + readerIndex, buf.memory, buf.offset + readerIndex, writerIndex - readerIndex);
            }
            else {
                writerIndex = newCapacity;
                readerIndex = newCapacity;
            }
        }
        buf.setIndex(readerIndex, writerIndex);
        if (freeOldMemory) {
            this.free(oldChunk, oldHandle, oldMaxLength, buf.cache);
        }
    }
    
    @Override
    public int numThreadCaches() {
        return this.numThreadCaches.get();
    }
    
    @Override
    public int numTinySubpages() {
        return this.tinySubpagePools.length;
    }
    
    @Override
    public int numSmallSubpages() {
        return this.smallSubpagePools.length;
    }
    
    @Override
    public int numChunkLists() {
        return this.chunkListMetrics.size();
    }
    
    @Override
    public List<PoolSubpageMetric> tinySubpages() {
        return subPageMetricList(this.tinySubpagePools);
    }
    
    @Override
    public List<PoolSubpageMetric> smallSubpages() {
        return subPageMetricList(this.smallSubpagePools);
    }
    
    @Override
    public List<PoolChunkListMetric> chunkLists() {
        return this.chunkListMetrics;
    }
    
    private static List<PoolSubpageMetric> subPageMetricList(final PoolSubpage<?>[] pages) {
        final List<PoolSubpageMetric> metrics = new ArrayList<PoolSubpageMetric>();
        for (final PoolSubpage<?> head : pages) {
            if (head.next != head) {
                PoolSubpage<?> s = head.next;
                do {
                    metrics.add(s);
                    s = s.next;
                } while (s != head);
            }
        }
        return metrics;
    }
    
    @Override
    public long numAllocations() {
        final long allocsNormal;
        synchronized (this) {
            allocsNormal = this.allocationsNormal;
        }
        return this.allocationsTiny.value() + this.allocationsSmall.value() + allocsNormal + this.allocationsHuge.value();
    }
    
    @Override
    public long numTinyAllocations() {
        return this.allocationsTiny.value();
    }
    
    @Override
    public long numSmallAllocations() {
        return this.allocationsSmall.value();
    }
    
    @Override
    public synchronized long numNormalAllocations() {
        return this.allocationsNormal;
    }
    
    @Override
    public long numDeallocations() {
        final long deallocs;
        synchronized (this) {
            deallocs = this.deallocationsTiny + this.deallocationsSmall + this.deallocationsNormal;
        }
        return deallocs + this.deallocationsHuge.value();
    }
    
    @Override
    public synchronized long numTinyDeallocations() {
        return this.deallocationsTiny;
    }
    
    @Override
    public synchronized long numSmallDeallocations() {
        return this.deallocationsSmall;
    }
    
    @Override
    public synchronized long numNormalDeallocations() {
        return this.deallocationsNormal;
    }
    
    @Override
    public long numHugeAllocations() {
        return this.allocationsHuge.value();
    }
    
    @Override
    public long numHugeDeallocations() {
        return this.deallocationsHuge.value();
    }
    
    @Override
    public long numActiveAllocations() {
        long val = this.allocationsTiny.value() + this.allocationsSmall.value() + this.allocationsHuge.value() - this.deallocationsHuge.value();
        synchronized (this) {
            val += this.allocationsNormal - (this.deallocationsTiny + this.deallocationsSmall + this.deallocationsNormal);
        }
        return Math.max(val, 0L);
    }
    
    @Override
    public long numActiveTinyAllocations() {
        return Math.max(this.numTinyAllocations() - this.numTinyDeallocations(), 0L);
    }
    
    @Override
    public long numActiveSmallAllocations() {
        return Math.max(this.numSmallAllocations() - this.numSmallDeallocations(), 0L);
    }
    
    @Override
    public long numActiveNormalAllocations() {
        final long val;
        synchronized (this) {
            val = this.allocationsNormal - this.deallocationsNormal;
        }
        return Math.max(val, 0L);
    }
    
    @Override
    public long numActiveHugeAllocations() {
        return Math.max(this.numHugeAllocations() - this.numHugeDeallocations(), 0L);
    }
    
    @Override
    public long numActiveBytes() {
        long val = this.activeBytesHuge.value();
        synchronized (this) {
            for (int i = 0; i < this.chunkListMetrics.size(); ++i) {
                for (final PoolChunkMetric m : this.chunkListMetrics.get(i)) {
                    val += m.chunkSize();
                }
            }
        }
        return Math.max(0L, val);
    }
    
    protected abstract PoolChunk<T> newChunk(final int p0, final int p1, final int p2, final int p3);
    
    protected abstract PoolChunk<T> newUnpooledChunk(final int p0);
    
    protected abstract PooledByteBuf<T> newByteBuf(final int p0);
    
    protected abstract void memoryCopy(final T p0, final int p1, final T p2, final int p3, final int p4);
    
    protected abstract void destroyChunk(final PoolChunk<T> p0);
    
    @Override
    public synchronized String toString() {
        final StringBuilder buf = new StringBuilder().append("Chunk(s) at 0~25%:").append(StringUtil.NEWLINE).append(this.qInit).append(StringUtil.NEWLINE).append("Chunk(s) at 0~50%:").append(StringUtil.NEWLINE).append(this.q000).append(StringUtil.NEWLINE).append("Chunk(s) at 25~75%:").append(StringUtil.NEWLINE).append(this.q025).append(StringUtil.NEWLINE).append("Chunk(s) at 50~100%:").append(StringUtil.NEWLINE).append(this.q050).append(StringUtil.NEWLINE).append("Chunk(s) at 75~100%:").append(StringUtil.NEWLINE).append(this.q075).append(StringUtil.NEWLINE).append("Chunk(s) at 100%:").append(StringUtil.NEWLINE).append(this.q100).append(StringUtil.NEWLINE).append("tiny subpages:");
        appendPoolSubPages(buf, this.tinySubpagePools);
        buf.append(StringUtil.NEWLINE).append("small subpages:");
        appendPoolSubPages(buf, this.smallSubpagePools);
        buf.append(StringUtil.NEWLINE);
        return buf.toString();
    }
    
    private static void appendPoolSubPages(final StringBuilder buf, final PoolSubpage<?>[] subpages) {
        for (int i = 0; i < subpages.length; ++i) {
            final PoolSubpage<?> head = subpages[i];
            if (head.next != head) {
                buf.append(StringUtil.NEWLINE).append(i).append(": ");
                PoolSubpage<?> s = head.next;
                do {
                    buf.append(s);
                    s = s.next;
                } while (s != head);
            }
        }
    }
    
    @Override
    protected final void finalize() throws Throwable {
        try {
            super.finalize();
        }
        finally {
            destroyPoolSubPages(this.smallSubpagePools);
            destroyPoolSubPages(this.tinySubpagePools);
            this.destroyPoolChunkLists(this.qInit, this.q000, this.q025, this.q050, this.q075, this.q100);
        }
    }
    
    private static void destroyPoolSubPages(final PoolSubpage<?>[] pages) {
        for (final PoolSubpage<?> page : pages) {
            page.destroy();
        }
    }
    
    private void destroyPoolChunkLists(final PoolChunkList<T>... chunkLists) {
        for (final PoolChunkList<T> chunkList : chunkLists) {
            chunkList.destroy(this);
        }
    }
    
    static {
        HAS_UNSAFE = PlatformDependent.hasUnsafe();
    }
    
    enum SizeClass
    {
        Tiny, 
        Small, 
        Normal;
    }
    
    static final class HeapArena extends PoolArena<byte[]>
    {
        HeapArena(final PooledByteBufAllocator parent, final int pageSize, final int maxOrder, final int pageShifts, final int chunkSize, final int directMemoryCacheAlignment) {
            super(parent, pageSize, maxOrder, pageShifts, chunkSize, directMemoryCacheAlignment);
        }
        
        @Override
        boolean isDirect() {
            return false;
        }
        
        @Override
        protected PoolChunk<byte[]> newChunk(final int pageSize, final int maxOrder, final int pageShifts, final int chunkSize) {
            return new PoolChunk<byte[]>(this, new byte[chunkSize], pageSize, maxOrder, pageShifts, chunkSize, 0);
        }
        
        @Override
        protected PoolChunk<byte[]> newUnpooledChunk(final int capacity) {
            return new PoolChunk<byte[]>(this, new byte[capacity], capacity, 0);
        }
        
        @Override
        protected void destroyChunk(final PoolChunk<byte[]> chunk) {
        }
        
        @Override
        protected PooledByteBuf<byte[]> newByteBuf(final int maxCapacity) {
            return HeapArena.HAS_UNSAFE ? PooledUnsafeHeapByteBuf.newUnsafeInstance(maxCapacity) : PooledHeapByteBuf.newInstance(maxCapacity);
        }
        
        @Override
        protected void memoryCopy(final byte[] src, final int srcOffset, final byte[] dst, final int dstOffset, final int length) {
            if (length == 0) {
                return;
            }
            System.arraycopy(src, srcOffset, dst, dstOffset, length);
        }
    }
    
    static final class DirectArena extends PoolArena<ByteBuffer>
    {
        DirectArena(final PooledByteBufAllocator parent, final int pageSize, final int maxOrder, final int pageShifts, final int chunkSize, final int directMemoryCacheAlignment) {
            super(parent, pageSize, maxOrder, pageShifts, chunkSize, directMemoryCacheAlignment);
        }
        
        @Override
        boolean isDirect() {
            return true;
        }
        
        private int offsetCacheLine(final ByteBuffer memory) {
            return DirectArena.HAS_UNSAFE ? ((int)(PlatformDependent.directBufferAddress(memory) & (long)this.directMemoryCacheAlignmentMask)) : 0;
        }
        
        @Override
        protected PoolChunk<ByteBuffer> newChunk(final int pageSize, final int maxOrder, final int pageShifts, final int chunkSize) {
            if (this.directMemoryCacheAlignment == 0) {
                return new PoolChunk<ByteBuffer>(this, allocateDirect(chunkSize), pageSize, maxOrder, pageShifts, chunkSize, 0);
            }
            final ByteBuffer memory = allocateDirect(chunkSize + this.directMemoryCacheAlignment);
            return new PoolChunk<ByteBuffer>(this, memory, pageSize, maxOrder, pageShifts, chunkSize, this.offsetCacheLine(memory));
        }
        
        @Override
        protected PoolChunk<ByteBuffer> newUnpooledChunk(final int capacity) {
            if (this.directMemoryCacheAlignment == 0) {
                return new PoolChunk<ByteBuffer>(this, allocateDirect(capacity), capacity, 0);
            }
            final ByteBuffer memory = allocateDirect(capacity + this.directMemoryCacheAlignment);
            return new PoolChunk<ByteBuffer>(this, memory, capacity, this.offsetCacheLine(memory));
        }
        
        private static ByteBuffer allocateDirect(final int capacity) {
            return PlatformDependent.useDirectBufferNoCleaner() ? PlatformDependent.allocateDirectNoCleaner(capacity) : ByteBuffer.allocateDirect(capacity);
        }
        
        @Override
        protected void destroyChunk(final PoolChunk<ByteBuffer> chunk) {
            if (PlatformDependent.useDirectBufferNoCleaner()) {
                PlatformDependent.freeDirectNoCleaner(chunk.memory);
            }
            else {
                PlatformDependent.freeDirectBuffer(chunk.memory);
            }
        }
        
        @Override
        protected PooledByteBuf<ByteBuffer> newByteBuf(final int maxCapacity) {
            if (DirectArena.HAS_UNSAFE) {
                return PooledUnsafeDirectByteBuf.newInstance(maxCapacity);
            }
            return PooledDirectByteBuf.newInstance(maxCapacity);
        }
        
        @Override
        protected void memoryCopy(ByteBuffer src, final int srcOffset, ByteBuffer dst, final int dstOffset, final int length) {
            if (length == 0) {
                return;
            }
            if (DirectArena.HAS_UNSAFE) {
                PlatformDependent.copyMemory(PlatformDependent.directBufferAddress(src) + srcOffset, PlatformDependent.directBufferAddress(dst) + dstOffset, length);
            }
            else {
                src = src.duplicate();
                dst = dst.duplicate();
                src.position(srcOffset).limit(srcOffset + length);
                dst.position(dstOffset);
                dst.put(src);
            }
        }
    }
}
