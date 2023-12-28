// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import io.netty.util.concurrent.FastThreadLocalThread;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.PlatformDependent;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.nio.ByteBuffer;
import io.netty.util.internal.logging.InternalLogger;

public class PooledByteBufAllocator extends AbstractByteBufAllocator implements ByteBufAllocatorMetricProvider
{
    private static final InternalLogger logger;
    private static final int DEFAULT_NUM_HEAP_ARENA;
    private static final int DEFAULT_NUM_DIRECT_ARENA;
    private static final int DEFAULT_PAGE_SIZE;
    private static final int DEFAULT_MAX_ORDER;
    private static final int DEFAULT_TINY_CACHE_SIZE;
    private static final int DEFAULT_SMALL_CACHE_SIZE;
    private static final int DEFAULT_NORMAL_CACHE_SIZE;
    private static final int DEFAULT_MAX_CACHED_BUFFER_CAPACITY;
    private static final int DEFAULT_CACHE_TRIM_INTERVAL;
    private static final boolean DEFAULT_USE_CACHE_FOR_ALL_THREADS;
    private static final int DEFAULT_DIRECT_MEMORY_CACHE_ALIGNMENT;
    private static final int MIN_PAGE_SIZE = 4096;
    private static final int MAX_CHUNK_SIZE = 1073741824;
    public static final PooledByteBufAllocator DEFAULT;
    private final PoolArena<byte[]>[] heapArenas;
    private final PoolArena<ByteBuffer>[] directArenas;
    private final int tinyCacheSize;
    private final int smallCacheSize;
    private final int normalCacheSize;
    private final List<PoolArenaMetric> heapArenaMetrics;
    private final List<PoolArenaMetric> directArenaMetrics;
    private final PoolThreadLocalCache threadCache;
    private final int chunkSize;
    private final PooledByteBufAllocatorMetric metric;
    
    public PooledByteBufAllocator() {
        this(false);
    }
    
    public PooledByteBufAllocator(final boolean preferDirect) {
        this(preferDirect, PooledByteBufAllocator.DEFAULT_NUM_HEAP_ARENA, PooledByteBufAllocator.DEFAULT_NUM_DIRECT_ARENA, PooledByteBufAllocator.DEFAULT_PAGE_SIZE, PooledByteBufAllocator.DEFAULT_MAX_ORDER);
    }
    
    public PooledByteBufAllocator(final int nHeapArena, final int nDirectArena, final int pageSize, final int maxOrder) {
        this(false, nHeapArena, nDirectArena, pageSize, maxOrder);
    }
    
    @Deprecated
    public PooledByteBufAllocator(final boolean preferDirect, final int nHeapArena, final int nDirectArena, final int pageSize, final int maxOrder) {
        this(preferDirect, nHeapArena, nDirectArena, pageSize, maxOrder, PooledByteBufAllocator.DEFAULT_TINY_CACHE_SIZE, PooledByteBufAllocator.DEFAULT_SMALL_CACHE_SIZE, PooledByteBufAllocator.DEFAULT_NORMAL_CACHE_SIZE);
    }
    
    @Deprecated
    public PooledByteBufAllocator(final boolean preferDirect, final int nHeapArena, final int nDirectArena, final int pageSize, final int maxOrder, final int tinyCacheSize, final int smallCacheSize, final int normalCacheSize) {
        this(preferDirect, nHeapArena, nDirectArena, pageSize, maxOrder, tinyCacheSize, smallCacheSize, normalCacheSize, PooledByteBufAllocator.DEFAULT_USE_CACHE_FOR_ALL_THREADS, PooledByteBufAllocator.DEFAULT_DIRECT_MEMORY_CACHE_ALIGNMENT);
    }
    
    public PooledByteBufAllocator(final boolean preferDirect, final int nHeapArena, final int nDirectArena, final int pageSize, final int maxOrder, final int tinyCacheSize, final int smallCacheSize, final int normalCacheSize, final boolean useCacheForAllThreads) {
        this(preferDirect, nHeapArena, nDirectArena, pageSize, maxOrder, tinyCacheSize, smallCacheSize, normalCacheSize, useCacheForAllThreads, PooledByteBufAllocator.DEFAULT_DIRECT_MEMORY_CACHE_ALIGNMENT);
    }
    
    public PooledByteBufAllocator(final boolean preferDirect, final int nHeapArena, final int nDirectArena, final int pageSize, final int maxOrder, final int tinyCacheSize, final int smallCacheSize, final int normalCacheSize, final boolean useCacheForAllThreads, final int directMemoryCacheAlignment) {
        super(preferDirect);
        this.threadCache = new PoolThreadLocalCache(useCacheForAllThreads);
        this.tinyCacheSize = tinyCacheSize;
        this.smallCacheSize = smallCacheSize;
        this.normalCacheSize = normalCacheSize;
        this.chunkSize = validateAndCalculateChunkSize(pageSize, maxOrder);
        if (nHeapArena < 0) {
            throw new IllegalArgumentException("nHeapArena: " + nHeapArena + " (expected: >= 0)");
        }
        if (nDirectArena < 0) {
            throw new IllegalArgumentException("nDirectArea: " + nDirectArena + " (expected: >= 0)");
        }
        if (directMemoryCacheAlignment < 0) {
            throw new IllegalArgumentException("directMemoryCacheAlignment: " + directMemoryCacheAlignment + " (expected: >= 0)");
        }
        if (directMemoryCacheAlignment > 0 && !isDirectMemoryCacheAlignmentSupported()) {
            throw new IllegalArgumentException("directMemoryCacheAlignment is not supported");
        }
        if ((directMemoryCacheAlignment & -directMemoryCacheAlignment) != directMemoryCacheAlignment) {
            throw new IllegalArgumentException("directMemoryCacheAlignment: " + directMemoryCacheAlignment + " (expected: power of two)");
        }
        final int pageShifts = validateAndCalculatePageShifts(pageSize);
        if (nHeapArena > 0) {
            this.heapArenas = newArenaArray(nHeapArena);
            final List<PoolArenaMetric> metrics = new ArrayList<PoolArenaMetric>(this.heapArenas.length);
            for (int i = 0; i < this.heapArenas.length; ++i) {
                final PoolArena.HeapArena arena = new PoolArena.HeapArena(this, pageSize, maxOrder, pageShifts, this.chunkSize, directMemoryCacheAlignment);
                metrics.add(this.heapArenas[i] = arena);
            }
            this.heapArenaMetrics = Collections.unmodifiableList((List<? extends PoolArenaMetric>)metrics);
        }
        else {
            this.heapArenas = null;
            this.heapArenaMetrics = Collections.emptyList();
        }
        if (nDirectArena > 0) {
            this.directArenas = newArenaArray(nDirectArena);
            final List<PoolArenaMetric> metrics = new ArrayList<PoolArenaMetric>(this.directArenas.length);
            for (int i = 0; i < this.directArenas.length; ++i) {
                final PoolArena.DirectArena arena2 = new PoolArena.DirectArena(this, pageSize, maxOrder, pageShifts, this.chunkSize, directMemoryCacheAlignment);
                metrics.add(this.directArenas[i] = arena2);
            }
            this.directArenaMetrics = Collections.unmodifiableList((List<? extends PoolArenaMetric>)metrics);
        }
        else {
            this.directArenas = null;
            this.directArenaMetrics = Collections.emptyList();
        }
        this.metric = new PooledByteBufAllocatorMetric(this);
    }
    
    private static <T> PoolArena<T>[] newArenaArray(final int size) {
        return new PoolArena[size];
    }
    
    private static int validateAndCalculatePageShifts(final int pageSize) {
        if (pageSize < 4096) {
            throw new IllegalArgumentException("pageSize: " + pageSize + " (expected: " + 4096 + ")");
        }
        if ((pageSize & pageSize - 1) != 0x0) {
            throw new IllegalArgumentException("pageSize: " + pageSize + " (expected: power of 2)");
        }
        return 31 - Integer.numberOfLeadingZeros(pageSize);
    }
    
    private static int validateAndCalculateChunkSize(final int pageSize, final int maxOrder) {
        if (maxOrder > 14) {
            throw new IllegalArgumentException("maxOrder: " + maxOrder + " (expected: 0-14)");
        }
        int chunkSize = pageSize;
        for (int i = maxOrder; i > 0; --i) {
            if (chunkSize > 536870912) {
                throw new IllegalArgumentException(String.format("pageSize (%d) << maxOrder (%d) must not exceed %d", pageSize, maxOrder, 1073741824));
            }
            chunkSize <<= 1;
        }
        return chunkSize;
    }
    
    @Override
    protected ByteBuf newHeapBuffer(final int initialCapacity, final int maxCapacity) {
        final PoolThreadCache cache = this.threadCache.get();
        final PoolArena<byte[]> heapArena = cache.heapArena;
        ByteBuf buf;
        if (heapArena != null) {
            buf = heapArena.allocate(cache, initialCapacity, maxCapacity);
        }
        else {
            buf = (PlatformDependent.hasUnsafe() ? new UnpooledUnsafeHeapByteBuf(this, initialCapacity, maxCapacity) : new UnpooledHeapByteBuf(this, initialCapacity, maxCapacity));
        }
        return AbstractByteBufAllocator.toLeakAwareBuffer(buf);
    }
    
    @Override
    protected ByteBuf newDirectBuffer(final int initialCapacity, final int maxCapacity) {
        final PoolThreadCache cache = this.threadCache.get();
        final PoolArena<ByteBuffer> directArena = cache.directArena;
        ByteBuf buf;
        if (directArena != null) {
            buf = directArena.allocate(cache, initialCapacity, maxCapacity);
        }
        else {
            buf = (PlatformDependent.hasUnsafe() ? UnsafeByteBufUtil.newUnsafeDirectByteBuf(this, initialCapacity, maxCapacity) : new UnpooledDirectByteBuf(this, initialCapacity, maxCapacity));
        }
        return AbstractByteBufAllocator.toLeakAwareBuffer(buf);
    }
    
    public static int defaultNumHeapArena() {
        return PooledByteBufAllocator.DEFAULT_NUM_HEAP_ARENA;
    }
    
    public static int defaultNumDirectArena() {
        return PooledByteBufAllocator.DEFAULT_NUM_DIRECT_ARENA;
    }
    
    public static int defaultPageSize() {
        return PooledByteBufAllocator.DEFAULT_PAGE_SIZE;
    }
    
    public static int defaultMaxOrder() {
        return PooledByteBufAllocator.DEFAULT_MAX_ORDER;
    }
    
    public static int defaultTinyCacheSize() {
        return PooledByteBufAllocator.DEFAULT_TINY_CACHE_SIZE;
    }
    
    public static int defaultSmallCacheSize() {
        return PooledByteBufAllocator.DEFAULT_SMALL_CACHE_SIZE;
    }
    
    public static int defaultNormalCacheSize() {
        return PooledByteBufAllocator.DEFAULT_NORMAL_CACHE_SIZE;
    }
    
    public static boolean isDirectMemoryCacheAlignmentSupported() {
        return PlatformDependent.hasUnsafe();
    }
    
    @Override
    public boolean isDirectBufferPooled() {
        return this.directArenas != null;
    }
    
    @Deprecated
    public boolean hasThreadLocalCache() {
        return this.threadCache.isSet();
    }
    
    @Deprecated
    public void freeThreadLocalCache() {
        this.threadCache.remove();
    }
    
    @Override
    public PooledByteBufAllocatorMetric metric() {
        return this.metric;
    }
    
    @Deprecated
    public int numHeapArenas() {
        return this.heapArenaMetrics.size();
    }
    
    @Deprecated
    public int numDirectArenas() {
        return this.directArenaMetrics.size();
    }
    
    @Deprecated
    public List<PoolArenaMetric> heapArenas() {
        return this.heapArenaMetrics;
    }
    
    @Deprecated
    public List<PoolArenaMetric> directArenas() {
        return this.directArenaMetrics;
    }
    
    @Deprecated
    public int numThreadLocalCaches() {
        final PoolArena<?>[] arenas = (PoolArena<?>[])((this.heapArenas != null) ? this.heapArenas : this.directArenas);
        if (arenas == null) {
            return 0;
        }
        int total = 0;
        for (final PoolArena<?> arena : arenas) {
            total += arena.numThreadCaches.get();
        }
        return total;
    }
    
    @Deprecated
    public int tinyCacheSize() {
        return this.tinyCacheSize;
    }
    
    @Deprecated
    public int smallCacheSize() {
        return this.smallCacheSize;
    }
    
    @Deprecated
    public int normalCacheSize() {
        return this.normalCacheSize;
    }
    
    @Deprecated
    public final int chunkSize() {
        return this.chunkSize;
    }
    
    final long usedHeapMemory() {
        return usedMemory((PoolArena<?>[])this.heapArenas);
    }
    
    final long usedDirectMemory() {
        return usedMemory((PoolArena<?>[])this.directArenas);
    }
    
    private static long usedMemory(final PoolArena<?>... arenas) {
        if (arenas == null) {
            return -1L;
        }
        long used = 0L;
        for (final PoolArena<?> arena : arenas) {
            used += arena.numActiveBytes();
            if (used < 0L) {
                return Long.MAX_VALUE;
            }
        }
        return used;
    }
    
    final PoolThreadCache threadCache() {
        return this.threadCache.get();
    }
    
    public String dumpStats() {
        final int heapArenasLen = (this.heapArenas == null) ? 0 : this.heapArenas.length;
        final StringBuilder buf = new StringBuilder(512).append(heapArenasLen).append(" heap arena(s):").append(StringUtil.NEWLINE);
        if (heapArenasLen > 0) {
            for (final PoolArena<byte[]> a : this.heapArenas) {
                buf.append(a);
            }
        }
        final int directArenasLen = (this.directArenas == null) ? 0 : this.directArenas.length;
        buf.append(directArenasLen).append(" direct arena(s):").append(StringUtil.NEWLINE);
        if (directArenasLen > 0) {
            for (final PoolArena<ByteBuffer> a2 : this.directArenas) {
                buf.append(a2);
            }
        }
        return buf.toString();
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(PooledByteBufAllocator.class);
        int defaultPageSize = SystemPropertyUtil.getInt("io.netty.allocator.pageSize", 8192);
        Throwable pageSizeFallbackCause = null;
        try {
            validateAndCalculatePageShifts(defaultPageSize);
        }
        catch (final Throwable t) {
            pageSizeFallbackCause = t;
            defaultPageSize = 8192;
        }
        DEFAULT_PAGE_SIZE = defaultPageSize;
        int defaultMaxOrder = SystemPropertyUtil.getInt("io.netty.allocator.maxOrder", 11);
        Throwable maxOrderFallbackCause = null;
        try {
            validateAndCalculateChunkSize(PooledByteBufAllocator.DEFAULT_PAGE_SIZE, defaultMaxOrder);
        }
        catch (final Throwable t2) {
            maxOrderFallbackCause = t2;
            defaultMaxOrder = 11;
        }
        DEFAULT_MAX_ORDER = defaultMaxOrder;
        final Runtime runtime = Runtime.getRuntime();
        final int defaultMinNumArena = runtime.availableProcessors() * 2;
        final int defaultChunkSize = PooledByteBufAllocator.DEFAULT_PAGE_SIZE << PooledByteBufAllocator.DEFAULT_MAX_ORDER;
        DEFAULT_NUM_HEAP_ARENA = Math.max(0, SystemPropertyUtil.getInt("io.netty.allocator.numHeapArenas", (int)Math.min(defaultMinNumArena, runtime.maxMemory() / defaultChunkSize / 2L / 3L)));
        DEFAULT_NUM_DIRECT_ARENA = Math.max(0, SystemPropertyUtil.getInt("io.netty.allocator.numDirectArenas", (int)Math.min(defaultMinNumArena, PlatformDependent.maxDirectMemory() / defaultChunkSize / 2L / 3L)));
        DEFAULT_TINY_CACHE_SIZE = SystemPropertyUtil.getInt("io.netty.allocator.tinyCacheSize", 512);
        DEFAULT_SMALL_CACHE_SIZE = SystemPropertyUtil.getInt("io.netty.allocator.smallCacheSize", 256);
        DEFAULT_NORMAL_CACHE_SIZE = SystemPropertyUtil.getInt("io.netty.allocator.normalCacheSize", 64);
        DEFAULT_MAX_CACHED_BUFFER_CAPACITY = SystemPropertyUtil.getInt("io.netty.allocator.maxCachedBufferCapacity", 32768);
        DEFAULT_CACHE_TRIM_INTERVAL = SystemPropertyUtil.getInt("io.netty.allocator.cacheTrimInterval", 8192);
        DEFAULT_USE_CACHE_FOR_ALL_THREADS = SystemPropertyUtil.getBoolean("io.netty.allocator.useCacheForAllThreads", true);
        DEFAULT_DIRECT_MEMORY_CACHE_ALIGNMENT = SystemPropertyUtil.getInt("io.netty.allocator.directMemoryCacheAlignment", 0);
        if (PooledByteBufAllocator.logger.isDebugEnabled()) {
            PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.numHeapArenas: {}", (Object)PooledByteBufAllocator.DEFAULT_NUM_HEAP_ARENA);
            PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.numDirectArenas: {}", (Object)PooledByteBufAllocator.DEFAULT_NUM_DIRECT_ARENA);
            if (pageSizeFallbackCause == null) {
                PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.pageSize: {}", (Object)PooledByteBufAllocator.DEFAULT_PAGE_SIZE);
            }
            else {
                PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.pageSize: {}", (Object)PooledByteBufAllocator.DEFAULT_PAGE_SIZE, pageSizeFallbackCause);
            }
            if (maxOrderFallbackCause == null) {
                PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.maxOrder: {}", (Object)PooledByteBufAllocator.DEFAULT_MAX_ORDER);
            }
            else {
                PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.maxOrder: {}", (Object)PooledByteBufAllocator.DEFAULT_MAX_ORDER, maxOrderFallbackCause);
            }
            PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.chunkSize: {}", (Object)(PooledByteBufAllocator.DEFAULT_PAGE_SIZE << PooledByteBufAllocator.DEFAULT_MAX_ORDER));
            PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.tinyCacheSize: {}", (Object)PooledByteBufAllocator.DEFAULT_TINY_CACHE_SIZE);
            PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.smallCacheSize: {}", (Object)PooledByteBufAllocator.DEFAULT_SMALL_CACHE_SIZE);
            PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.normalCacheSize: {}", (Object)PooledByteBufAllocator.DEFAULT_NORMAL_CACHE_SIZE);
            PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.maxCachedBufferCapacity: {}", (Object)PooledByteBufAllocator.DEFAULT_MAX_CACHED_BUFFER_CAPACITY);
            PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.cacheTrimInterval: {}", (Object)PooledByteBufAllocator.DEFAULT_CACHE_TRIM_INTERVAL);
            PooledByteBufAllocator.logger.debug("-Dio.netty.allocator.useCacheForAllThreads: {}", (Object)PooledByteBufAllocator.DEFAULT_USE_CACHE_FOR_ALL_THREADS);
        }
        DEFAULT = new PooledByteBufAllocator(PlatformDependent.directBufferPreferred());
    }
    
    final class PoolThreadLocalCache extends FastThreadLocal<PoolThreadCache>
    {
        private final boolean useCacheForAllThreads;
        
        PoolThreadLocalCache(final boolean useCacheForAllThreads) {
            this.useCacheForAllThreads = useCacheForAllThreads;
        }
        
        @Override
        protected synchronized PoolThreadCache initialValue() {
            final PoolArena<byte[]> heapArena = this.leastUsedArena(PooledByteBufAllocator.this.heapArenas);
            final PoolArena<ByteBuffer> directArena = this.leastUsedArena(PooledByteBufAllocator.this.directArenas);
            if (this.useCacheForAllThreads || Thread.currentThread() instanceof FastThreadLocalThread) {
                return new PoolThreadCache(heapArena, directArena, PooledByteBufAllocator.this.tinyCacheSize, PooledByteBufAllocator.this.smallCacheSize, PooledByteBufAllocator.this.normalCacheSize, PooledByteBufAllocator.DEFAULT_MAX_CACHED_BUFFER_CAPACITY, PooledByteBufAllocator.DEFAULT_CACHE_TRIM_INTERVAL);
            }
            return new PoolThreadCache(heapArena, directArena, 0, 0, 0, 0, 0);
        }
        
        @Override
        protected void onRemoval(final PoolThreadCache threadCache) {
            threadCache.free();
        }
        
        private <T> PoolArena<T> leastUsedArena(final PoolArena<T>[] arenas) {
            if (arenas == null || arenas.length == 0) {
                return null;
            }
            PoolArena<T> minArena = arenas[0];
            for (int i = 1; i < arenas.length; ++i) {
                final PoolArena<T> arena = arenas[i];
                if (arena.numThreadCaches.get() < minArena.numThreadCaches.get()) {
                    minArena = arena;
                }
            }
            return minArena;
        }
    }
}
