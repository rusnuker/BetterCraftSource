// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.MathUtil;
import io.netty.util.Recycler;
import java.util.Queue;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.ThreadDeathWatcher;
import java.nio.ByteBuffer;
import io.netty.util.internal.logging.InternalLogger;

final class PoolThreadCache
{
    private static final InternalLogger logger;
    final PoolArena<byte[]> heapArena;
    final PoolArena<ByteBuffer> directArena;
    private final MemoryRegionCache<byte[]>[] tinySubPageHeapCaches;
    private final MemoryRegionCache<byte[]>[] smallSubPageHeapCaches;
    private final MemoryRegionCache<ByteBuffer>[] tinySubPageDirectCaches;
    private final MemoryRegionCache<ByteBuffer>[] smallSubPageDirectCaches;
    private final MemoryRegionCache<byte[]>[] normalHeapCaches;
    private final MemoryRegionCache<ByteBuffer>[] normalDirectCaches;
    private final int numShiftsNormalDirect;
    private final int numShiftsNormalHeap;
    private final int freeSweepAllocationThreshold;
    private final Thread deathWatchThread;
    private final Runnable freeTask;
    private int allocations;
    
    PoolThreadCache(final PoolArena<byte[]> heapArena, final PoolArena<ByteBuffer> directArena, final int tinyCacheSize, final int smallCacheSize, final int normalCacheSize, final int maxCachedBufferCapacity, final int freeSweepAllocationThreshold) {
        if (maxCachedBufferCapacity < 0) {
            throw new IllegalArgumentException("maxCachedBufferCapacity: " + maxCachedBufferCapacity + " (expected: >= 0)");
        }
        if (freeSweepAllocationThreshold < 1) {
            throw new IllegalArgumentException("freeSweepAllocationThreshold: " + freeSweepAllocationThreshold + " (expected: > 0)");
        }
        this.freeSweepAllocationThreshold = freeSweepAllocationThreshold;
        this.heapArena = heapArena;
        if ((this.directArena = directArena) != null) {
            this.tinySubPageDirectCaches = createSubPageCaches(tinyCacheSize, 32, PoolArena.SizeClass.Tiny);
            this.smallSubPageDirectCaches = createSubPageCaches(smallCacheSize, directArena.numSmallSubpagePools, PoolArena.SizeClass.Small);
            this.numShiftsNormalDirect = log2(directArena.pageSize);
            this.normalDirectCaches = createNormalCaches(normalCacheSize, maxCachedBufferCapacity, directArena);
            directArena.numThreadCaches.getAndIncrement();
        }
        else {
            this.tinySubPageDirectCaches = null;
            this.smallSubPageDirectCaches = null;
            this.normalDirectCaches = null;
            this.numShiftsNormalDirect = -1;
        }
        if (heapArena != null) {
            this.tinySubPageHeapCaches = createSubPageCaches(tinyCacheSize, 32, PoolArena.SizeClass.Tiny);
            this.smallSubPageHeapCaches = createSubPageCaches(smallCacheSize, heapArena.numSmallSubpagePools, PoolArena.SizeClass.Small);
            this.numShiftsNormalHeap = log2(heapArena.pageSize);
            this.normalHeapCaches = createNormalCaches(normalCacheSize, maxCachedBufferCapacity, heapArena);
            heapArena.numThreadCaches.getAndIncrement();
        }
        else {
            this.tinySubPageHeapCaches = null;
            this.smallSubPageHeapCaches = null;
            this.normalHeapCaches = null;
            this.numShiftsNormalHeap = -1;
        }
        if (this.tinySubPageDirectCaches != null || this.smallSubPageDirectCaches != null || this.normalDirectCaches != null || this.tinySubPageHeapCaches != null || this.smallSubPageHeapCaches != null || this.normalHeapCaches != null) {
            this.freeTask = new Runnable() {
                @Override
                public void run() {
                    PoolThreadCache.this.free0();
                }
            };
            ThreadDeathWatcher.watch(this.deathWatchThread = Thread.currentThread(), this.freeTask);
        }
        else {
            this.freeTask = null;
            this.deathWatchThread = null;
        }
    }
    
    private static <T> MemoryRegionCache<T>[] createSubPageCaches(final int cacheSize, final int numCaches, final PoolArena.SizeClass sizeClass) {
        if (cacheSize > 0) {
            final MemoryRegionCache<T>[] cache = new MemoryRegionCache[numCaches];
            for (int i = 0; i < cache.length; ++i) {
                cache[i] = new SubPageMemoryRegionCache<T>(cacheSize, sizeClass);
            }
            return cache;
        }
        return null;
    }
    
    private static <T> MemoryRegionCache<T>[] createNormalCaches(final int cacheSize, final int maxCachedBufferCapacity, final PoolArena<T> area) {
        if (cacheSize > 0) {
            final int max = Math.min(area.chunkSize, maxCachedBufferCapacity);
            final int arraySize = Math.max(1, log2(max / area.pageSize) + 1);
            final MemoryRegionCache<T>[] cache = new MemoryRegionCache[arraySize];
            for (int i = 0; i < cache.length; ++i) {
                cache[i] = new NormalMemoryRegionCache<T>(cacheSize);
            }
            return cache;
        }
        return null;
    }
    
    private static int log2(int val) {
        int res;
        for (res = 0; val > 1; val >>= 1, ++res) {}
        return res;
    }
    
    boolean allocateTiny(final PoolArena<?> area, final PooledByteBuf<?> buf, final int reqCapacity, final int normCapacity) {
        return this.allocate(this.cacheForTiny(area, normCapacity), buf, reqCapacity);
    }
    
    boolean allocateSmall(final PoolArena<?> area, final PooledByteBuf<?> buf, final int reqCapacity, final int normCapacity) {
        return this.allocate(this.cacheForSmall(area, normCapacity), buf, reqCapacity);
    }
    
    boolean allocateNormal(final PoolArena<?> area, final PooledByteBuf<?> buf, final int reqCapacity, final int normCapacity) {
        return this.allocate(this.cacheForNormal(area, normCapacity), buf, reqCapacity);
    }
    
    private boolean allocate(final MemoryRegionCache<?> cache, final PooledByteBuf buf, final int reqCapacity) {
        if (cache == null) {
            return false;
        }
        final boolean allocated = cache.allocate(buf, reqCapacity);
        if (++this.allocations >= this.freeSweepAllocationThreshold) {
            this.allocations = 0;
            this.trim();
        }
        return allocated;
    }
    
    boolean add(final PoolArena<?> area, final PoolChunk chunk, final long handle, final int normCapacity, final PoolArena.SizeClass sizeClass) {
        final MemoryRegionCache<?> cache = this.cache(area, normCapacity, sizeClass);
        return cache != null && cache.add(chunk, handle);
    }
    
    private MemoryRegionCache<?> cache(final PoolArena<?> area, final int normCapacity, final PoolArena.SizeClass sizeClass) {
        switch (sizeClass) {
            case Normal: {
                return this.cacheForNormal(area, normCapacity);
            }
            case Small: {
                return this.cacheForSmall(area, normCapacity);
            }
            case Tiny: {
                return this.cacheForTiny(area, normCapacity);
            }
            default: {
                throw new Error();
            }
        }
    }
    
    void free() {
        if (this.freeTask != null) {
            assert this.deathWatchThread != null;
            ThreadDeathWatcher.unwatch(this.deathWatchThread, this.freeTask);
        }
        this.free0();
    }
    
    private void free0() {
        final int numFreed = free(this.tinySubPageDirectCaches) + free(this.smallSubPageDirectCaches) + free(this.normalDirectCaches) + free(this.tinySubPageHeapCaches) + free(this.smallSubPageHeapCaches) + free(this.normalHeapCaches);
        if (numFreed > 0 && PoolThreadCache.logger.isDebugEnabled()) {
            PoolThreadCache.logger.debug("Freed {} thread-local buffer(s) from thread: {}", (Object)numFreed, Thread.currentThread().getName());
        }
        if (this.directArena != null) {
            this.directArena.numThreadCaches.getAndDecrement();
        }
        if (this.heapArena != null) {
            this.heapArena.numThreadCaches.getAndDecrement();
        }
    }
    
    private static int free(final MemoryRegionCache<?>[] caches) {
        if (caches == null) {
            return 0;
        }
        int numFreed = 0;
        for (final MemoryRegionCache<?> c : caches) {
            numFreed += free(c);
        }
        return numFreed;
    }
    
    private static int free(final MemoryRegionCache<?> cache) {
        if (cache == null) {
            return 0;
        }
        return cache.free();
    }
    
    void trim() {
        trim(this.tinySubPageDirectCaches);
        trim(this.smallSubPageDirectCaches);
        trim(this.normalDirectCaches);
        trim(this.tinySubPageHeapCaches);
        trim(this.smallSubPageHeapCaches);
        trim(this.normalHeapCaches);
    }
    
    private static void trim(final MemoryRegionCache<?>[] caches) {
        if (caches == null) {
            return;
        }
        for (final MemoryRegionCache<?> c : caches) {
            trim(c);
        }
    }
    
    private static void trim(final MemoryRegionCache<?> cache) {
        if (cache == null) {
            return;
        }
        cache.trim();
    }
    
    private MemoryRegionCache<?> cacheForTiny(final PoolArena<?> area, final int normCapacity) {
        final int idx = PoolArena.tinyIdx(normCapacity);
        if (area.isDirect()) {
            return cache((MemoryRegionCache<?>[])this.tinySubPageDirectCaches, idx);
        }
        return cache((MemoryRegionCache<?>[])this.tinySubPageHeapCaches, idx);
    }
    
    private MemoryRegionCache<?> cacheForSmall(final PoolArena<?> area, final int normCapacity) {
        final int idx = PoolArena.smallIdx(normCapacity);
        if (area.isDirect()) {
            return cache((MemoryRegionCache<?>[])this.smallSubPageDirectCaches, idx);
        }
        return cache((MemoryRegionCache<?>[])this.smallSubPageHeapCaches, idx);
    }
    
    private MemoryRegionCache<?> cacheForNormal(final PoolArena<?> area, final int normCapacity) {
        if (area.isDirect()) {
            final int idx = log2(normCapacity >> this.numShiftsNormalDirect);
            return cache((MemoryRegionCache<?>[])this.normalDirectCaches, idx);
        }
        final int idx = log2(normCapacity >> this.numShiftsNormalHeap);
        return cache((MemoryRegionCache<?>[])this.normalHeapCaches, idx);
    }
    
    private static <T> MemoryRegionCache<T> cache(final MemoryRegionCache<T>[] cache, final int idx) {
        if (cache == null || idx > cache.length - 1) {
            return null;
        }
        return cache[idx];
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(PoolThreadCache.class);
    }
    
    private static final class SubPageMemoryRegionCache<T> extends MemoryRegionCache<T>
    {
        SubPageMemoryRegionCache(final int size, final PoolArena.SizeClass sizeClass) {
            super(size, sizeClass);
        }
        
        @Override
        protected void initBuf(final PoolChunk<T> chunk, final long handle, final PooledByteBuf<T> buf, final int reqCapacity) {
            chunk.initBufWithSubpage(buf, handle, reqCapacity);
        }
    }
    
    private static final class NormalMemoryRegionCache<T> extends MemoryRegionCache<T>
    {
        NormalMemoryRegionCache(final int size) {
            super(size, PoolArena.SizeClass.Normal);
        }
        
        @Override
        protected void initBuf(final PoolChunk<T> chunk, final long handle, final PooledByteBuf<T> buf, final int reqCapacity) {
            chunk.initBuf(buf, handle, reqCapacity);
        }
    }
    
    private abstract static class MemoryRegionCache<T>
    {
        private final int size;
        private final Queue<Entry<T>> queue;
        private final PoolArena.SizeClass sizeClass;
        private int allocations;
        private static final Recycler<Entry> RECYCLER;
        
        MemoryRegionCache(final int size, final PoolArena.SizeClass sizeClass) {
            this.size = MathUtil.safeFindNextPositivePowerOfTwo(size);
            this.queue = PlatformDependent.newFixedMpscQueue(this.size);
            this.sizeClass = sizeClass;
        }
        
        protected abstract void initBuf(final PoolChunk<T> p0, final long p1, final PooledByteBuf<T> p2, final int p3);
        
        public final boolean add(final PoolChunk<T> chunk, final long handle) {
            final Entry<T> entry = newEntry(chunk, handle);
            final boolean queued = this.queue.offer(entry);
            if (!queued) {
                entry.recycle();
            }
            return queued;
        }
        
        public final boolean allocate(final PooledByteBuf<T> buf, final int reqCapacity) {
            final Entry<T> entry = this.queue.poll();
            if (entry == null) {
                return false;
            }
            this.initBuf(entry.chunk, entry.handle, buf, reqCapacity);
            entry.recycle();
            ++this.allocations;
            return true;
        }
        
        public final int free() {
            return this.free(Integer.MAX_VALUE);
        }
        
        private int free(final int max) {
            int numFreed;
            for (numFreed = 0; numFreed < max; ++numFreed) {
                final Entry<T> entry = this.queue.poll();
                if (entry == null) {
                    return numFreed;
                }
                this.freeEntry(entry);
            }
            return numFreed;
        }
        
        public final void trim() {
            final int free = this.size - this.allocations;
            this.allocations = 0;
            if (free > 0) {
                this.free(free);
            }
        }
        
        private void freeEntry(final Entry entry) {
            final PoolChunk chunk = entry.chunk;
            final long handle = entry.handle;
            entry.recycle();
            chunk.arena.freeChunk(chunk, handle, this.sizeClass);
        }
        
        private static Entry newEntry(final PoolChunk<?> chunk, final long handle) {
            final Entry entry = MemoryRegionCache.RECYCLER.get();
            entry.chunk = (PoolChunk<T>)chunk;
            entry.handle = handle;
            return entry;
        }
        
        static {
            RECYCLER = new Recycler<Entry>() {
                @Override
                protected Entry newObject(final Handle<Entry> handle) {
                    return new Entry((Handle<Entry<?>>)handle);
                }
            };
        }
        
        static final class Entry<T>
        {
            final Recycler.Handle<Entry<?>> recyclerHandle;
            PoolChunk<T> chunk;
            long handle;
            
            Entry(final Recycler.Handle<Entry<?>> recyclerHandle) {
                this.handle = -1L;
                this.recyclerHandle = recyclerHandle;
            }
            
            void recycle() {
                this.chunk = null;
                this.handle = -1L;
                this.recyclerHandle.recycle(this);
            }
        }
    }
}
