// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.lang.ref.PhantomReference;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.ConcurrentMap;
import io.netty.util.internal.logging.InternalLogger;

public class ResourceLeakDetector<T>
{
    private static final String PROP_LEVEL_OLD = "io.netty.leakDetectionLevel";
    private static final String PROP_LEVEL = "io.netty.leakDetection.level";
    private static final Level DEFAULT_LEVEL;
    private static final String PROP_MAX_RECORDS = "io.netty.leakDetection.maxRecords";
    private static final int DEFAULT_MAX_RECORDS = 4;
    private static final int MAX_RECORDS;
    private static Level level;
    private static final InternalLogger logger;
    static final int DEFAULT_SAMPLING_INTERVAL = 128;
    private final ConcurrentMap<DefaultResourceLeak, LeakEntry> allLeaks;
    private final ReferenceQueue<Object> refQueue;
    private final ConcurrentMap<String, Boolean> reportedLeaks;
    private final String resourceType;
    private final int samplingInterval;
    private static final String[] STACK_TRACE_ELEMENT_EXCLUSIONS;
    
    @Deprecated
    public static void setEnabled(final boolean enabled) {
        setLevel(enabled ? Level.SIMPLE : Level.DISABLED);
    }
    
    public static boolean isEnabled() {
        return getLevel().ordinal() > Level.DISABLED.ordinal();
    }
    
    public static void setLevel(final Level level) {
        if (level == null) {
            throw new NullPointerException("level");
        }
        ResourceLeakDetector.level = level;
    }
    
    public static Level getLevel() {
        return ResourceLeakDetector.level;
    }
    
    @Deprecated
    public ResourceLeakDetector(final Class<?> resourceType) {
        this(StringUtil.simpleClassName(resourceType));
    }
    
    @Deprecated
    public ResourceLeakDetector(final String resourceType) {
        this(resourceType, 128, Long.MAX_VALUE);
    }
    
    @Deprecated
    public ResourceLeakDetector(final Class<?> resourceType, final int samplingInterval, final long maxActive) {
        this(resourceType, samplingInterval);
    }
    
    public ResourceLeakDetector(final Class<?> resourceType, final int samplingInterval) {
        this(StringUtil.simpleClassName(resourceType), samplingInterval, Long.MAX_VALUE);
    }
    
    @Deprecated
    public ResourceLeakDetector(final String resourceType, final int samplingInterval, final long maxActive) {
        this.allLeaks = PlatformDependent.newConcurrentHashMap();
        this.refQueue = new ReferenceQueue<Object>();
        this.reportedLeaks = PlatformDependent.newConcurrentHashMap();
        if (resourceType == null) {
            throw new NullPointerException("resourceType");
        }
        this.resourceType = resourceType;
        this.samplingInterval = samplingInterval;
    }
    
    @Deprecated
    public final ResourceLeak open(final T obj) {
        return this.track0(obj);
    }
    
    public final ResourceLeakTracker<T> track(final T obj) {
        return this.track0(obj);
    }
    
    private DefaultResourceLeak track0(final T obj) {
        final Level level = ResourceLeakDetector.level;
        if (level == Level.DISABLED) {
            return null;
        }
        if (level.ordinal() >= Level.PARANOID.ordinal()) {
            this.reportLeak(level);
            return new DefaultResourceLeak(obj);
        }
        if (PlatformDependent.threadLocalRandom().nextInt(this.samplingInterval) == 0) {
            this.reportLeak(level);
            return new DefaultResourceLeak(obj);
        }
        return null;
    }
    
    private void reportLeak(final Level level) {
        if (!ResourceLeakDetector.logger.isErrorEnabled()) {
            while (true) {
                final DefaultResourceLeak ref = (DefaultResourceLeak)this.refQueue.poll();
                if (ref == null) {
                    break;
                }
                ref.close();
            }
            return;
        }
        while (true) {
            final DefaultResourceLeak ref = (DefaultResourceLeak)this.refQueue.poll();
            if (ref == null) {
                break;
            }
            ref.clear();
            if (!ref.close()) {
                continue;
            }
            final String records = ref.toString();
            if (this.reportedLeaks.putIfAbsent(records, Boolean.TRUE) != null) {
                continue;
            }
            if (records.isEmpty()) {
                this.reportUntracedLeak(this.resourceType);
            }
            else {
                this.reportTracedLeak(this.resourceType, records);
            }
        }
    }
    
    protected void reportTracedLeak(final String resourceType, final String records) {
        ResourceLeakDetector.logger.error("LEAK: {}.release() was not called before it's garbage-collected. See http://netty.io/wiki/reference-counted-objects.html for more information.{}", resourceType, records);
    }
    
    protected void reportUntracedLeak(final String resourceType) {
        ResourceLeakDetector.logger.error("LEAK: {}.release() was not called before it's garbage-collected. Enable advanced leak reporting to find out where the leak occurred. To enable advanced leak reporting, specify the JVM option '-D{}={}' or call {}.setLevel() See http://netty.io/wiki/reference-counted-objects.html for more information.", resourceType, "io.netty.leakDetection.level", Level.ADVANCED.name().toLowerCase(), StringUtil.simpleClassName(this));
    }
    
    @Deprecated
    protected void reportInstancesLeak(final String resourceType) {
    }
    
    static String newRecord(final Object hint, int recordsToSkip) {
        final StringBuilder buf = new StringBuilder(4096);
        if (hint != null) {
            buf.append("\tHint: ");
            if (hint instanceof ResourceLeakHint) {
                buf.append(((ResourceLeakHint)hint).toHintString());
            }
            else {
                buf.append(hint);
            }
            buf.append(StringUtil.NEWLINE);
        }
        final StackTraceElement[] stackTrace;
        final StackTraceElement[] array = stackTrace = new Throwable().getStackTrace();
        for (final StackTraceElement e : stackTrace) {
            if (recordsToSkip > 0) {
                --recordsToSkip;
            }
            else {
                final String estr = e.toString();
                boolean excluded = false;
                for (final String exclusion : ResourceLeakDetector.STACK_TRACE_ELEMENT_EXCLUSIONS) {
                    if (estr.startsWith(exclusion)) {
                        excluded = true;
                        break;
                    }
                }
                if (!excluded) {
                    buf.append('\t');
                    buf.append(estr);
                    buf.append(StringUtil.NEWLINE);
                }
            }
        }
        return buf.toString();
    }
    
    static {
        DEFAULT_LEVEL = Level.SIMPLE;
        logger = InternalLoggerFactory.getInstance(ResourceLeakDetector.class);
        boolean disabled;
        if (SystemPropertyUtil.get("io.netty.noResourceLeakDetection") != null) {
            disabled = SystemPropertyUtil.getBoolean("io.netty.noResourceLeakDetection", false);
            ResourceLeakDetector.logger.debug("-Dio.netty.noResourceLeakDetection: {}", (Object)disabled);
            ResourceLeakDetector.logger.warn("-Dio.netty.noResourceLeakDetection is deprecated. Use '-D{}={}' instead.", "io.netty.leakDetection.level", ResourceLeakDetector.DEFAULT_LEVEL.name().toLowerCase());
        }
        else {
            disabled = false;
        }
        final Level defaultLevel = disabled ? Level.DISABLED : ResourceLeakDetector.DEFAULT_LEVEL;
        String levelStr = SystemPropertyUtil.get("io.netty.leakDetectionLevel", defaultLevel.name());
        levelStr = SystemPropertyUtil.get("io.netty.leakDetection.level", levelStr);
        final Level level = Level.parseLevel(levelStr);
        MAX_RECORDS = SystemPropertyUtil.getInt("io.netty.leakDetection.maxRecords", 4);
        ResourceLeakDetector.level = level;
        if (ResourceLeakDetector.logger.isDebugEnabled()) {
            ResourceLeakDetector.logger.debug("-D{}: {}", "io.netty.leakDetection.level", level.name().toLowerCase());
            ResourceLeakDetector.logger.debug("-D{}: {}", "io.netty.leakDetection.maxRecords", ResourceLeakDetector.MAX_RECORDS);
        }
        STACK_TRACE_ELEMENT_EXCLUSIONS = new String[] { "io.netty.util.ReferenceCountUtil.touch(", "io.netty.buffer.AdvancedLeakAwareByteBuf.touch(", "io.netty.buffer.AbstractByteBufAllocator.toLeakAwareBuffer(", "io.netty.buffer.AdvancedLeakAwareByteBuf.recordLeakNonRefCountingOperation(" };
    }
    
    public enum Level
    {
        DISABLED, 
        SIMPLE, 
        ADVANCED, 
        PARANOID;
        
        static Level parseLevel(final String levelStr) {
            final String trimmedLevelStr = levelStr.trim();
            for (final Level l : values()) {
                if (trimmedLevelStr.equalsIgnoreCase(l.name()) || trimmedLevelStr.equals(String.valueOf(l.ordinal()))) {
                    return l;
                }
            }
            return ResourceLeakDetector.DEFAULT_LEVEL;
        }
    }
    
    private final class DefaultResourceLeak extends PhantomReference<Object> implements ResourceLeakTracker<T>, ResourceLeak
    {
        private final String creationRecord;
        private final Deque<String> lastRecords;
        private final int trackedHash;
        private int removedRecords;
        
        DefaultResourceLeak(final Object referent) {
            super(referent, ResourceLeakDetector.this.refQueue);
            this.lastRecords = new ArrayDeque<String>();
            assert referent != null;
            this.trackedHash = System.identityHashCode(referent);
            final Level level = ResourceLeakDetector.getLevel();
            if (level.ordinal() >= Level.ADVANCED.ordinal()) {
                this.creationRecord = ResourceLeakDetector.newRecord(null, 3);
            }
            else {
                this.creationRecord = null;
            }
            ResourceLeakDetector.this.allLeaks.put(this, LeakEntry.INSTANCE);
        }
        
        @Override
        public void record() {
            this.record0(null, 3);
        }
        
        @Override
        public void record(final Object hint) {
            this.record0(hint, 3);
        }
        
        private void record0(final Object hint, final int recordsToSkip) {
            if (this.creationRecord != null) {
                final String value = ResourceLeakDetector.newRecord(hint, recordsToSkip);
                synchronized (this.lastRecords) {
                    final int size = this.lastRecords.size();
                    if (size == 0 || !this.lastRecords.getLast().equals(value)) {
                        this.lastRecords.add(value);
                    }
                    if (size > ResourceLeakDetector.MAX_RECORDS) {
                        this.lastRecords.removeFirst();
                        ++this.removedRecords;
                    }
                }
            }
        }
        
        @Override
        public boolean close() {
            return ResourceLeakDetector.this.allLeaks.remove(this, LeakEntry.INSTANCE);
        }
        
        @Override
        public boolean close(final T trackedObject) {
            assert this.trackedHash == System.identityHashCode(trackedObject);
            return this.close() && trackedObject != null;
        }
        
        @Override
        public String toString() {
            if (this.creationRecord == null) {
                return "";
            }
            final Object[] array;
            final int removedRecords;
            synchronized (this.lastRecords) {
                array = this.lastRecords.toArray();
                removedRecords = this.removedRecords;
            }
            final StringBuilder buf = new StringBuilder(16384).append(StringUtil.NEWLINE);
            if (removedRecords > 0) {
                buf.append("WARNING: ").append(removedRecords).append(" leak records were discarded because the leak record count is limited to ").append(ResourceLeakDetector.MAX_RECORDS).append(". Use system property ").append("io.netty.leakDetection.maxRecords").append(" to increase the limit.").append(StringUtil.NEWLINE);
            }
            buf.append("Recent access records: ").append(array.length).append(StringUtil.NEWLINE);
            if (array.length > 0) {
                for (int i = array.length - 1; i >= 0; --i) {
                    buf.append('#').append(i + 1).append(':').append(StringUtil.NEWLINE).append(array[i]);
                }
            }
            buf.append("Created at:").append(StringUtil.NEWLINE).append(this.creationRecord);
            buf.setLength(buf.length() - StringUtil.NEWLINE.length());
            return buf.toString();
        }
    }
    
    private static final class LeakEntry
    {
        static final LeakEntry INSTANCE;
        private static final int HASH;
        
        @Override
        public int hashCode() {
            return LeakEntry.HASH;
        }
        
        @Override
        public boolean equals(final Object obj) {
            return obj == this;
        }
        
        static {
            INSTANCE = new LeakEntry();
            HASH = System.identityHashCode(LeakEntry.INSTANCE);
        }
    }
}
