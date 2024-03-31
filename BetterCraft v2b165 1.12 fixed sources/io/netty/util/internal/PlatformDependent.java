// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal;

import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;
import io.netty.util.internal.shaded.org.jctools.queues.atomic.MpscLinkedAtomicQueue;
import io.netty.util.internal.shaded.org.jctools.queues.MpscChunkedArrayQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.nio.ByteOrder;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.regex.Matcher;
import java.lang.reflect.Method;
import java.util.List;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.Deque;
import io.netty.util.internal.shaded.org.jctools.queues.atomic.MpscAtomicArrayQueue;
import io.netty.util.internal.shaded.org.jctools.queues.MpscArrayQueue;
import io.netty.util.internal.shaded.org.jctools.queues.atomic.SpscLinkedAtomicQueue;
import io.netty.util.internal.shaded.org.jctools.queues.SpscLinkedQueue;
import java.util.Queue;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.io.File;
import java.util.regex.Pattern;
import io.netty.util.internal.logging.InternalLogger;

public final class PlatformDependent
{
    private static final InternalLogger logger;
    private static final Pattern MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN;
    private static final boolean IS_EXPLICIT_NO_UNSAFE;
    private static final boolean IS_ANDROID;
    private static final boolean IS_WINDOWS;
    private static final boolean MAYBE_SUPER_USER;
    private static final int JAVA_VERSION;
    private static final boolean CAN_ENABLE_TCP_NODELAY_BY_DEFAULT;
    private static final boolean HAS_UNSAFE;
    private static final boolean DIRECT_BUFFER_PREFERRED;
    private static final long MAX_DIRECT_MEMORY;
    private static final int MPSC_CHUNK_SIZE = 1024;
    private static final int MIN_MAX_MPSC_CAPACITY = 2048;
    private static final int DEFAULT_MAX_MPSC_CAPACITY = 1048576;
    private static final int MAX_ALLOWED_MPSC_CAPACITY = 1073741824;
    private static final long BYTE_ARRAY_BASE_OFFSET;
    private static final File TMPDIR;
    private static final int BIT_MODE;
    private static final int ADDRESS_SIZE;
    private static final boolean USE_DIRECT_BUFFER_NO_CLEANER;
    private static final AtomicLong DIRECT_MEMORY_COUNTER;
    private static final long DIRECT_MEMORY_LIMIT;
    private static final ThreadLocalRandomProvider RANDOM_PROVIDER;
    public static final boolean BIG_ENDIAN_NATIVE_ORDER;
    
    public static boolean isAndroid() {
        return PlatformDependent.IS_ANDROID;
    }
    
    public static boolean isWindows() {
        return PlatformDependent.IS_WINDOWS;
    }
    
    public static boolean maybeSuperUser() {
        return PlatformDependent.MAYBE_SUPER_USER;
    }
    
    public static int javaVersion() {
        return PlatformDependent.JAVA_VERSION;
    }
    
    public static boolean canEnableTcpNoDelayByDefault() {
        return PlatformDependent.CAN_ENABLE_TCP_NODELAY_BY_DEFAULT;
    }
    
    public static boolean hasUnsafe() {
        return PlatformDependent.HAS_UNSAFE;
    }
    
    public static boolean isUnaligned() {
        return PlatformDependent0.isUnaligned();
    }
    
    public static boolean directBufferPreferred() {
        return PlatformDependent.DIRECT_BUFFER_PREFERRED;
    }
    
    public static long maxDirectMemory() {
        return PlatformDependent.MAX_DIRECT_MEMORY;
    }
    
    public static File tmpdir() {
        return PlatformDependent.TMPDIR;
    }
    
    public static int bitMode() {
        return PlatformDependent.BIT_MODE;
    }
    
    public static int addressSize() {
        return PlatformDependent.ADDRESS_SIZE;
    }
    
    public static long allocateMemory(final long size) {
        return PlatformDependent0.allocateMemory(size);
    }
    
    public static void freeMemory(final long address) {
        PlatformDependent0.freeMemory(address);
    }
    
    public static void throwException(final Throwable t) {
        if (hasUnsafe()) {
            PlatformDependent0.throwException(t);
        }
        else {
            throwException0(t);
        }
    }
    
    private static <E extends Throwable> void throwException0(final Throwable t) throws E, Throwable {
        throw t;
    }
    
    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<K, V>();
    }
    
    public static LongCounter newLongCounter() {
        if (javaVersion() >= 8) {
            return new LongAdderCounter();
        }
        return new AtomicLongCounter();
    }
    
    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(final int initialCapacity) {
        return new ConcurrentHashMap<K, V>(initialCapacity);
    }
    
    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(final int initialCapacity, final float loadFactor) {
        return new ConcurrentHashMap<K, V>(initialCapacity, loadFactor);
    }
    
    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(final int initialCapacity, final float loadFactor, final int concurrencyLevel) {
        return new ConcurrentHashMap<K, V>(initialCapacity, loadFactor, concurrencyLevel);
    }
    
    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(final Map<? extends K, ? extends V> map) {
        return new ConcurrentHashMap<K, V>(map);
    }
    
    public static void freeDirectBuffer(final ByteBuffer buffer) {
        if (hasUnsafe() && !isAndroid()) {
            PlatformDependent0.freeDirectBuffer(buffer);
        }
    }
    
    public static long directBufferAddress(final ByteBuffer buffer) {
        return PlatformDependent0.directBufferAddress(buffer);
    }
    
    public static ByteBuffer directBuffer(final long memoryAddress, final int size) {
        if (PlatformDependent0.hasDirectBufferNoCleanerConstructor()) {
            return PlatformDependent0.newDirectBuffer(memoryAddress, size);
        }
        throw new UnsupportedOperationException("sun.misc.Unsafe or java.nio.DirectByteBuffer.<init>(long, int) not available");
    }
    
    public static int getInt(final Object object, final long fieldOffset) {
        return PlatformDependent0.getInt(object, fieldOffset);
    }
    
    public static byte getByte(final long address) {
        return PlatformDependent0.getByte(address);
    }
    
    public static short getShort(final long address) {
        return PlatformDependent0.getShort(address);
    }
    
    public static int getInt(final long address) {
        return PlatformDependent0.getInt(address);
    }
    
    public static long getLong(final long address) {
        return PlatformDependent0.getLong(address);
    }
    
    public static byte getByte(final byte[] data, final int index) {
        return PlatformDependent0.getByte(data, index);
    }
    
    public static short getShort(final byte[] data, final int index) {
        return PlatformDependent0.getShort(data, index);
    }
    
    public static int getInt(final byte[] data, final int index) {
        return PlatformDependent0.getInt(data, index);
    }
    
    public static long getLong(final byte[] data, final int index) {
        return PlatformDependent0.getLong(data, index);
    }
    
    private static long getLongSafe(final byte[] bytes, final int offset) {
        if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
            return (long)bytes[offset] << 56 | ((long)bytes[offset + 1] & 0xFFL) << 48 | ((long)bytes[offset + 2] & 0xFFL) << 40 | ((long)bytes[offset + 3] & 0xFFL) << 32 | ((long)bytes[offset + 4] & 0xFFL) << 24 | ((long)bytes[offset + 5] & 0xFFL) << 16 | ((long)bytes[offset + 6] & 0xFFL) << 8 | ((long)bytes[offset + 7] & 0xFFL);
        }
        return ((long)bytes[offset] & 0xFFL) | ((long)bytes[offset + 1] & 0xFFL) << 8 | ((long)bytes[offset + 2] & 0xFFL) << 16 | ((long)bytes[offset + 3] & 0xFFL) << 24 | ((long)bytes[offset + 4] & 0xFFL) << 32 | ((long)bytes[offset + 5] & 0xFFL) << 40 | ((long)bytes[offset + 6] & 0xFFL) << 48 | (long)bytes[offset + 7] << 56;
    }
    
    private static int getIntSafe(final byte[] bytes, final int offset) {
        if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
            return bytes[offset] << 24 | (bytes[offset + 1] & 0xFF) << 16 | (bytes[offset + 2] & 0xFF) << 8 | (bytes[offset + 3] & 0xFF);
        }
        return (bytes[offset] & 0xFF) | (bytes[offset + 1] & 0xFF) << 8 | (bytes[offset + 2] & 0xFF) << 16 | bytes[offset + 3] << 24;
    }
    
    private static short getShortSafe(final byte[] bytes, final int offset) {
        if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
            return (short)(bytes[offset] << 8 | (bytes[offset + 1] & 0xFF));
        }
        return (short)((bytes[offset] & 0xFF) | bytes[offset + 1] << 8);
    }
    
    private static int hashCodeAsciiCompute(final CharSequence value, final int offset, final int hash) {
        if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
            return hash * 461845907 + hashCodeAsciiSanitizeInt(value, offset + 4) * 461845907 + hashCodeAsciiSanitizeInt(value, offset);
        }
        return hash * 461845907 + hashCodeAsciiSanitizeInt(value, offset) * 461845907 + hashCodeAsciiSanitizeInt(value, offset + 4);
    }
    
    private static int hashCodeAsciiSanitizeInt(final CharSequence value, final int offset) {
        if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
            return (value.charAt(offset + 3) & '\u001f') | (value.charAt(offset + 2) & '\u001f') << 8 | (value.charAt(offset + 1) & '\u001f') << 16 | (value.charAt(offset) & '\u001f') << 24;
        }
        return (value.charAt(offset + 3) & '\u001f') << 24 | (value.charAt(offset + 2) & '\u001f') << 16 | (value.charAt(offset + 1) & '\u001f') << 8 | (value.charAt(offset) & '\u001f');
    }
    
    private static int hashCodeAsciiSanitizeShort(final CharSequence value, final int offset) {
        if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
            return (value.charAt(offset + 1) & '\u001f') | (value.charAt(offset) & '\u001f') << 8;
        }
        return (value.charAt(offset + 1) & '\u001f') << 8 | (value.charAt(offset) & '\u001f');
    }
    
    private static int hashCodeAsciiSanitizsByte(final char value) {
        return value & '\u001f';
    }
    
    public static void putByte(final long address, final byte value) {
        PlatformDependent0.putByte(address, value);
    }
    
    public static void putShort(final long address, final short value) {
        PlatformDependent0.putShort(address, value);
    }
    
    public static void putInt(final long address, final int value) {
        PlatformDependent0.putInt(address, value);
    }
    
    public static void putLong(final long address, final long value) {
        PlatformDependent0.putLong(address, value);
    }
    
    public static void putByte(final byte[] data, final int index, final byte value) {
        PlatformDependent0.putByte(data, index, value);
    }
    
    public static void putShort(final byte[] data, final int index, final short value) {
        PlatformDependent0.putShort(data, index, value);
    }
    
    public static void putInt(final byte[] data, final int index, final int value) {
        PlatformDependent0.putInt(data, index, value);
    }
    
    public static void putLong(final byte[] data, final int index, final long value) {
        PlatformDependent0.putLong(data, index, value);
    }
    
    public static void copyMemory(final long srcAddr, final long dstAddr, final long length) {
        PlatformDependent0.copyMemory(srcAddr, dstAddr, length);
    }
    
    public static void copyMemory(final byte[] src, final int srcIndex, final long dstAddr, final long length) {
        PlatformDependent0.copyMemory(src, PlatformDependent.BYTE_ARRAY_BASE_OFFSET + srcIndex, null, dstAddr, length);
    }
    
    public static void copyMemory(final long srcAddr, final byte[] dst, final int dstIndex, final long length) {
        PlatformDependent0.copyMemory(null, srcAddr, dst, PlatformDependent.BYTE_ARRAY_BASE_OFFSET + dstIndex, length);
    }
    
    public static void setMemory(final byte[] dst, final int dstIndex, final long bytes, final byte value) {
        PlatformDependent0.setMemory(dst, PlatformDependent.BYTE_ARRAY_BASE_OFFSET + dstIndex, bytes, value);
    }
    
    public static void setMemory(final long address, final long bytes, final byte value) {
        PlatformDependent0.setMemory(address, bytes, value);
    }
    
    public static ByteBuffer allocateDirectNoCleaner(final int capacity) {
        assert PlatformDependent.USE_DIRECT_BUFFER_NO_CLEANER;
        incrementMemoryCounter(capacity);
        try {
            return PlatformDependent0.allocateDirectNoCleaner(capacity);
        }
        catch (final Throwable e) {
            decrementMemoryCounter(capacity);
            throwException(e);
            return null;
        }
    }
    
    public static ByteBuffer reallocateDirectNoCleaner(final ByteBuffer buffer, final int capacity) {
        assert PlatformDependent.USE_DIRECT_BUFFER_NO_CLEANER;
        final int len = capacity - buffer.capacity();
        incrementMemoryCounter(len);
        try {
            return PlatformDependent0.reallocateDirectNoCleaner(buffer, capacity);
        }
        catch (final Throwable e) {
            decrementMemoryCounter(len);
            throwException(e);
            return null;
        }
    }
    
    public static void freeDirectNoCleaner(final ByteBuffer buffer) {
        assert PlatformDependent.USE_DIRECT_BUFFER_NO_CLEANER;
        final int capacity = buffer.capacity();
        PlatformDependent0.freeMemory(PlatformDependent0.directBufferAddress(buffer));
        decrementMemoryCounter(capacity);
    }
    
    private static void incrementMemoryCounter(final int capacity) {
        if (PlatformDependent.DIRECT_MEMORY_COUNTER != null) {
            long usedMemory;
            long newUsedMemory;
            do {
                usedMemory = PlatformDependent.DIRECT_MEMORY_COUNTER.get();
                newUsedMemory = usedMemory + capacity;
                if (newUsedMemory > PlatformDependent.DIRECT_MEMORY_LIMIT) {
                    throw new OutOfDirectMemoryError("failed to allocate " + capacity + " byte(s) of direct memory (used: " + usedMemory + ", max: " + PlatformDependent.DIRECT_MEMORY_LIMIT + ')');
                }
            } while (!PlatformDependent.DIRECT_MEMORY_COUNTER.compareAndSet(usedMemory, newUsedMemory));
        }
    }
    
    private static void decrementMemoryCounter(final int capacity) {
        if (PlatformDependent.DIRECT_MEMORY_COUNTER != null) {
            final long usedMemory = PlatformDependent.DIRECT_MEMORY_COUNTER.addAndGet(-capacity);
            assert usedMemory >= 0L;
        }
    }
    
    public static boolean useDirectBufferNoCleaner() {
        return PlatformDependent.USE_DIRECT_BUFFER_NO_CLEANER;
    }
    
    public static boolean equals(final byte[] bytes1, final int startPos1, final byte[] bytes2, final int startPos2, final int length) {
        return (!hasUnsafe() || !PlatformDependent0.unalignedAccess()) ? equalsSafe(bytes1, startPos1, bytes2, startPos2, length) : PlatformDependent0.equals(bytes1, startPos1, bytes2, startPos2, length);
    }
    
    public static int equalsConstantTime(final byte[] bytes1, final int startPos1, final byte[] bytes2, final int startPos2, final int length) {
        return (!hasUnsafe() || !PlatformDependent0.unalignedAccess()) ? ConstantTimeUtils.equalsConstantTime(bytes1, startPos1, bytes2, startPos2, length) : PlatformDependent0.equalsConstantTime(bytes1, startPos1, bytes2, startPos2, length);
    }
    
    public static int hashCodeAscii(final byte[] bytes, final int startPos, final int length) {
        return (!hasUnsafe() || !PlatformDependent0.unalignedAccess()) ? hashCodeAsciiSafe(bytes, startPos, length) : PlatformDependent0.hashCodeAscii(bytes, startPos, length);
    }
    
    public static int hashCodeAscii(final CharSequence bytes) {
        int hash = -1028477387;
        final int remainingBytes = bytes.length() & 0x7;
        switch (bytes.length()) {
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31: {
                hash = hashCodeAsciiCompute(bytes, bytes.length() - 24, hashCodeAsciiCompute(bytes, bytes.length() - 16, hashCodeAsciiCompute(bytes, bytes.length() - 8, hash)));
                break;
            }
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23: {
                hash = hashCodeAsciiCompute(bytes, bytes.length() - 16, hashCodeAsciiCompute(bytes, bytes.length() - 8, hash));
                break;
            }
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15: {
                hash = hashCodeAsciiCompute(bytes, bytes.length() - 8, hash);
                break;
            }
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7: {
                break;
            }
            default: {
                for (int i = bytes.length() - 8; i >= remainingBytes; i -= 8) {
                    hash = hashCodeAsciiCompute(bytes, i, hash);
                }
                break;
            }
        }
        switch (remainingBytes) {
            case 7: {
                return ((hash * 461845907 + hashCodeAsciiSanitizsByte(bytes.charAt(0))) * 461845907 + hashCodeAsciiSanitizeShort(bytes, 1)) * 461845907 + hashCodeAsciiSanitizeInt(bytes, 3);
            }
            case 6: {
                return (hash * 461845907 + hashCodeAsciiSanitizeShort(bytes, 0)) * 461845907 + hashCodeAsciiSanitizeInt(bytes, 2);
            }
            case 5: {
                return (hash * 461845907 + hashCodeAsciiSanitizsByte(bytes.charAt(0))) * 461845907 + hashCodeAsciiSanitizeInt(bytes, 1);
            }
            case 4: {
                return hash * 461845907 + hashCodeAsciiSanitizeInt(bytes, 0);
            }
            case 3: {
                return (hash * 461845907 + hashCodeAsciiSanitizsByte(bytes.charAt(0))) * 461845907 + hashCodeAsciiSanitizeShort(bytes, 1);
            }
            case 2: {
                return hash * 461845907 + hashCodeAsciiSanitizeShort(bytes, 0);
            }
            case 1: {
                return hash * 461845907 + hashCodeAsciiSanitizsByte(bytes.charAt(0));
            }
            default: {
                return hash;
            }
        }
    }
    
    public static <T> Queue<T> newMpscQueue() {
        return newMpscQueue(1048576);
    }
    
    public static <T> Queue<T> newMpscQueue(final int maxCapacity) {
        return Mpsc.newMpscQueue(maxCapacity);
    }
    
    public static <T> Queue<T> newSpscQueue() {
        return (Queue<T>)(hasUnsafe() ? new SpscLinkedQueue<T>() : new SpscLinkedAtomicQueue<T>());
    }
    
    public static <T> Queue<T> newFixedMpscQueue(final int capacity) {
        return (Queue<T>)(hasUnsafe() ? new MpscArrayQueue<Object>(capacity) : new MpscAtomicArrayQueue<Object>(capacity));
    }
    
    public static ClassLoader getClassLoader(final Class<?> clazz) {
        return PlatformDependent0.getClassLoader(clazz);
    }
    
    public static ClassLoader getContextClassLoader() {
        return PlatformDependent0.getContextClassLoader();
    }
    
    public static ClassLoader getSystemClassLoader() {
        return PlatformDependent0.getSystemClassLoader();
    }
    
    public static <C> Deque<C> newConcurrentDeque() {
        if (javaVersion() < 7) {
            return new LinkedBlockingDeque<C>();
        }
        return new ConcurrentLinkedDeque<C>();
    }
    
    public static Random threadLocalRandom() {
        return PlatformDependent.RANDOM_PROVIDER.current();
    }
    
    private static boolean isAndroid0() {
        boolean android;
        try {
            Class.forName("android.app.Application", false, getSystemClassLoader());
            android = true;
        }
        catch (final Throwable ignored) {
            android = false;
        }
        if (android) {
            PlatformDependent.logger.debug("Platform: Android");
        }
        return android;
    }
    
    private static boolean isWindows0() {
        final boolean windows = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).contains("win");
        if (windows) {
            PlatformDependent.logger.debug("Platform: Windows");
        }
        return windows;
    }
    
    private static boolean maybeSuperUser0() {
        final String username = SystemPropertyUtil.get("user.name");
        if (isWindows()) {
            return "Administrator".equals(username);
        }
        return "root".equals(username) || "toor".equals(username);
    }
    
    private static int javaVersion0() {
        int majorVersion;
        if (isAndroid()) {
            majorVersion = 6;
        }
        else {
            majorVersion = majorVersionFromJavaSpecificationVersion();
        }
        PlatformDependent.logger.debug("Java version: {}", (Object)majorVersion);
        return majorVersion;
    }
    
    static int majorVersionFromJavaSpecificationVersion() {
        try {
            final String javaSpecVersion = AccessController.doPrivileged((PrivilegedAction<String>)new PrivilegedAction<String>() {
                @Override
                public String run() {
                    return System.getProperty("java.specification.version");
                }
            });
            return majorVersion(javaSpecVersion);
        }
        catch (final SecurityException e) {
            PlatformDependent.logger.debug("security exception while reading java.specification.version", e);
            return 6;
        }
    }
    
    static int majorVersion(final String javaSpecVersion) {
        final String[] components = javaSpecVersion.split("\\.");
        final int[] version = new int[components.length];
        for (int i = 0; i < components.length; ++i) {
            version[i] = Integer.parseInt(components[i]);
        }
        if (version[0] != 1) {
            return version[0];
        }
        assert version[1] >= 6;
        return version[1];
    }
    
    static boolean isExplicitNoUnsafe() {
        return PlatformDependent.IS_EXPLICIT_NO_UNSAFE;
    }
    
    private static boolean explicitNoUnsafe0() {
        final boolean noUnsafe = SystemPropertyUtil.getBoolean("io.netty.noUnsafe", false);
        PlatformDependent.logger.debug("-Dio.netty.noUnsafe: {}", (Object)noUnsafe);
        if (noUnsafe) {
            PlatformDependent.logger.debug("sun.misc.Unsafe: unavailable (io.netty.noUnsafe)");
            return true;
        }
        boolean tryUnsafe;
        if (SystemPropertyUtil.contains("io.netty.tryUnsafe")) {
            tryUnsafe = SystemPropertyUtil.getBoolean("io.netty.tryUnsafe", true);
        }
        else {
            tryUnsafe = SystemPropertyUtil.getBoolean("org.jboss.netty.tryUnsafe", true);
        }
        if (!tryUnsafe) {
            PlatformDependent.logger.debug("sun.misc.Unsafe: unavailable (io.netty.tryUnsafe/org.jboss.netty.tryUnsafe)");
            return true;
        }
        return false;
    }
    
    private static boolean hasUnsafe0() {
        if (isAndroid()) {
            PlatformDependent.logger.debug("sun.misc.Unsafe: unavailable (Android)");
            return false;
        }
        if (PlatformDependent.IS_EXPLICIT_NO_UNSAFE) {
            return false;
        }
        try {
            final boolean hasUnsafe = PlatformDependent0.hasUnsafe();
            PlatformDependent.logger.debug("sun.misc.Unsafe: {}", hasUnsafe ? "available" : "unavailable");
            return hasUnsafe;
        }
        catch (final Throwable ignored) {
            return false;
        }
    }
    
    private static long maxDirectMemory0() {
        long maxDirectMemory = 0L;
        ClassLoader systemClassLoader = null;
        try {
            systemClassLoader = getSystemClassLoader();
            final Class<?> vmClass = Class.forName("sun.misc.VM", true, systemClassLoader);
            final Method m = vmClass.getDeclaredMethod("maxDirectMemory", (Class<?>[])new Class[0]);
            maxDirectMemory = ((Number)m.invoke(null, new Object[0])).longValue();
        }
        catch (final Throwable t) {}
        if (maxDirectMemory > 0L) {
            return maxDirectMemory;
        }
        try {
            final Class<?> mgmtFactoryClass = Class.forName("java.lang.management.ManagementFactory", true, systemClassLoader);
            final Class<?> runtimeClass = Class.forName("java.lang.management.RuntimeMXBean", true, systemClassLoader);
            final Object runtime = mgmtFactoryClass.getDeclaredMethod("getRuntimeMXBean", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
            final List<String> vmArgs = (List<String>)runtimeClass.getDeclaredMethod("getInputArguments", (Class<?>[])new Class[0]).invoke(runtime, new Object[0]);
            for (int i = vmArgs.size() - 1; i >= 0; --i) {
                final Matcher j = PlatformDependent.MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN.matcher(vmArgs.get(i));
                if (j.matches()) {
                    maxDirectMemory = Long.parseLong(j.group(1));
                    switch (j.group(2).charAt(0)) {
                        case 'K':
                        case 'k': {
                            maxDirectMemory *= 1024L;
                            break;
                        }
                        case 'M':
                        case 'm': {
                            maxDirectMemory *= 1048576L;
                            break;
                        }
                        case 'G':
                        case 'g': {
                            maxDirectMemory *= 1073741824L;
                            break;
                        }
                    }
                    break;
                }
            }
        }
        catch (final Throwable t2) {}
        if (maxDirectMemory <= 0L) {
            maxDirectMemory = Runtime.getRuntime().maxMemory();
            PlatformDependent.logger.debug("maxDirectMemory: {} bytes (maybe)", (Object)maxDirectMemory);
        }
        else {
            PlatformDependent.logger.debug("maxDirectMemory: {} bytes", (Object)maxDirectMemory);
        }
        return maxDirectMemory;
    }
    
    private static File tmpdir0() {
        try {
            File f = toDirectory(SystemPropertyUtil.get("io.netty.tmpdir"));
            if (f != null) {
                PlatformDependent.logger.debug("-Dio.netty.tmpdir: {}", f);
                return f;
            }
            f = toDirectory(SystemPropertyUtil.get("java.io.tmpdir"));
            if (f != null) {
                PlatformDependent.logger.debug("-Dio.netty.tmpdir: {} (java.io.tmpdir)", f);
                return f;
            }
            if (isWindows()) {
                f = toDirectory(System.getenv("TEMP"));
                if (f != null) {
                    PlatformDependent.logger.debug("-Dio.netty.tmpdir: {} (%TEMP%)", f);
                    return f;
                }
                final String userprofile = System.getenv("USERPROFILE");
                if (userprofile != null) {
                    f = toDirectory(userprofile + "\\AppData\\Local\\Temp");
                    if (f != null) {
                        PlatformDependent.logger.debug("-Dio.netty.tmpdir: {} (%USERPROFILE%\\AppData\\Local\\Temp)", f);
                        return f;
                    }
                    f = toDirectory(userprofile + "\\Local Settings\\Temp");
                    if (f != null) {
                        PlatformDependent.logger.debug("-Dio.netty.tmpdir: {} (%USERPROFILE%\\Local Settings\\Temp)", f);
                        return f;
                    }
                }
            }
            else {
                f = toDirectory(System.getenv("TMPDIR"));
                if (f != null) {
                    PlatformDependent.logger.debug("-Dio.netty.tmpdir: {} ($TMPDIR)", f);
                    return f;
                }
            }
        }
        catch (final Throwable t) {}
        File f;
        if (isWindows()) {
            f = new File("C:\\Windows\\Temp");
        }
        else {
            f = new File("/tmp");
        }
        PlatformDependent.logger.warn("Failed to get the temporary directory; falling back to: {}", f);
        return f;
    }
    
    private static File toDirectory(final String path) {
        if (path == null) {
            return null;
        }
        final File f = new File(path);
        f.mkdirs();
        if (!f.isDirectory()) {
            return null;
        }
        try {
            return f.getAbsoluteFile();
        }
        catch (final Exception ignored) {
            return f;
        }
    }
    
    private static int bitMode0() {
        int bitMode = SystemPropertyUtil.getInt("io.netty.bitMode", 0);
        if (bitMode > 0) {
            PlatformDependent.logger.debug("-Dio.netty.bitMode: {}", (Object)bitMode);
            return bitMode;
        }
        bitMode = SystemPropertyUtil.getInt("sun.arch.data.model", 0);
        if (bitMode > 0) {
            PlatformDependent.logger.debug("-Dio.netty.bitMode: {} (sun.arch.data.model)", (Object)bitMode);
            return bitMode;
        }
        bitMode = SystemPropertyUtil.getInt("com.ibm.vm.bitmode", 0);
        if (bitMode > 0) {
            PlatformDependent.logger.debug("-Dio.netty.bitMode: {} (com.ibm.vm.bitmode)", (Object)bitMode);
            return bitMode;
        }
        final String arch = SystemPropertyUtil.get("os.arch", "").toLowerCase(Locale.US).trim();
        if ("amd64".equals(arch) || "x86_64".equals(arch)) {
            bitMode = 64;
        }
        else if ("i386".equals(arch) || "i486".equals(arch) || "i586".equals(arch) || "i686".equals(arch)) {
            bitMode = 32;
        }
        if (bitMode > 0) {
            PlatformDependent.logger.debug("-Dio.netty.bitMode: {} (os.arch: {})", (Object)bitMode, arch);
        }
        final String vm = SystemPropertyUtil.get("java.vm.name", "").toLowerCase(Locale.US);
        final Pattern BIT_PATTERN = Pattern.compile("([1-9][0-9]+)-?bit");
        final Matcher m = BIT_PATTERN.matcher(vm);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return 64;
    }
    
    private static int addressSize0() {
        if (!hasUnsafe()) {
            return -1;
        }
        return PlatformDependent0.addressSize();
    }
    
    private static long byteArrayBaseOffset0() {
        if (!hasUnsafe()) {
            return -1L;
        }
        return PlatformDependent0.byteArrayBaseOffset();
    }
    
    private static boolean equalsSafe(final byte[] bytes1, final int startPos1, final byte[] bytes2, final int startPos2, final int length) {
        for (int end = startPos1 + length, i = startPos1, j = startPos2; i < end; ++i, ++j) {
            if (bytes1[i] != bytes2[j]) {
                return false;
            }
        }
        return true;
    }
    
    static int hashCodeAsciiSafe(final byte[] bytes, final int startPos, final int length) {
        int hash = -1028477387;
        final int remainingBytes = length & 0x7;
        for (int end = startPos + remainingBytes, i = startPos - 8 + length; i >= end; i -= 8) {
            hash = PlatformDependent0.hashCodeAsciiCompute(getLongSafe(bytes, i), hash);
        }
        switch (remainingBytes) {
            case 7: {
                return ((hash * 461845907 + PlatformDependent0.hashCodeAsciiSanitize(bytes[startPos])) * 461845907 + PlatformDependent0.hashCodeAsciiSanitize(getShortSafe(bytes, startPos + 1))) * 461845907 + PlatformDependent0.hashCodeAsciiSanitize(getIntSafe(bytes, startPos + 3));
            }
            case 6: {
                return (hash * 461845907 + PlatformDependent0.hashCodeAsciiSanitize(getShortSafe(bytes, startPos))) * 461845907 + PlatformDependent0.hashCodeAsciiSanitize(getIntSafe(bytes, startPos + 2));
            }
            case 5: {
                return (hash * 461845907 + PlatformDependent0.hashCodeAsciiSanitize(bytes[startPos])) * 461845907 + PlatformDependent0.hashCodeAsciiSanitize(getIntSafe(bytes, startPos + 1));
            }
            case 4: {
                return hash * 461845907 + PlatformDependent0.hashCodeAsciiSanitize(getIntSafe(bytes, startPos));
            }
            case 3: {
                return (hash * 461845907 + PlatformDependent0.hashCodeAsciiSanitize(bytes[startPos])) * 461845907 + PlatformDependent0.hashCodeAsciiSanitize(getShortSafe(bytes, startPos + 1));
            }
            case 2: {
                return hash * 461845907 + PlatformDependent0.hashCodeAsciiSanitize(getShortSafe(bytes, startPos));
            }
            case 1: {
                return hash * 461845907 + PlatformDependent0.hashCodeAsciiSanitize(bytes[startPos]);
            }
            default: {
                return hash;
            }
        }
    }
    
    private PlatformDependent() {
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(PlatformDependent.class);
        MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN = Pattern.compile("\\s*-XX:MaxDirectMemorySize\\s*=\\s*([0-9]+)\\s*([kKmMgG]?)\\s*$");
        IS_EXPLICIT_NO_UNSAFE = explicitNoUnsafe0();
        IS_ANDROID = isAndroid0();
        IS_WINDOWS = isWindows0();
        JAVA_VERSION = javaVersion0();
        CAN_ENABLE_TCP_NODELAY_BY_DEFAULT = !isAndroid();
        HAS_UNSAFE = hasUnsafe0();
        DIRECT_BUFFER_PREFERRED = (PlatformDependent.HAS_UNSAFE && !SystemPropertyUtil.getBoolean("io.netty.noPreferDirect", false));
        MAX_DIRECT_MEMORY = maxDirectMemory0();
        BYTE_ARRAY_BASE_OFFSET = byteArrayBaseOffset0();
        TMPDIR = tmpdir0();
        BIT_MODE = bitMode0();
        ADDRESS_SIZE = addressSize0();
        BIG_ENDIAN_NATIVE_ORDER = (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN);
        if (javaVersion() >= 7) {
            RANDOM_PROVIDER = new ThreadLocalRandomProvider() {
                @Override
                public Random current() {
                    return ThreadLocalRandom.current();
                }
            };
        }
        else {
            RANDOM_PROVIDER = new ThreadLocalRandomProvider() {
                @Override
                public Random current() {
                    return io.netty.util.internal.ThreadLocalRandom.current();
                }
            };
        }
        if (PlatformDependent.logger.isDebugEnabled()) {
            PlatformDependent.logger.debug("-Dio.netty.noPreferDirect: {}", (Object)!PlatformDependent.DIRECT_BUFFER_PREFERRED);
        }
        if (!hasUnsafe() && !isAndroid() && !PlatformDependent.IS_EXPLICIT_NO_UNSAFE) {
            PlatformDependent.logger.info("Your platform does not provide complete low-level API for accessing direct buffers reliably. Unless explicitly requested, heap buffer will always be preferred to avoid potential system unstability.");
        }
        long maxDirectMemory = SystemPropertyUtil.getLong("io.netty.maxDirectMemory", -1L);
        if (maxDirectMemory == 0L || !hasUnsafe() || !PlatformDependent0.hasDirectBufferNoCleanerConstructor()) {
            USE_DIRECT_BUFFER_NO_CLEANER = false;
            DIRECT_MEMORY_COUNTER = null;
        }
        else {
            USE_DIRECT_BUFFER_NO_CLEANER = true;
            if (maxDirectMemory < 0L) {
                maxDirectMemory = maxDirectMemory0();
                if (maxDirectMemory <= 0L) {
                    DIRECT_MEMORY_COUNTER = null;
                }
                else {
                    DIRECT_MEMORY_COUNTER = new AtomicLong();
                }
            }
            else {
                DIRECT_MEMORY_COUNTER = new AtomicLong();
            }
        }
        DIRECT_MEMORY_LIMIT = maxDirectMemory;
        PlatformDependent.logger.debug("io.netty.maxDirectMemory: {} bytes", (Object)maxDirectMemory);
        MAYBE_SUPER_USER = maybeSuperUser0();
    }
    
    private static final class Mpsc
    {
        private static final boolean USE_MPSC_CHUNKED_ARRAY_QUEUE;
        
        static <T> Queue<T> newMpscQueue(final int maxCapacity) {
            if (Mpsc.USE_MPSC_CHUNKED_ARRAY_QUEUE) {
                final int capacity = Math.max(Math.min(maxCapacity, 1073741824), 2048);
                return new MpscChunkedArrayQueue<T>(1024, capacity);
            }
            return new MpscLinkedAtomicQueue<T>();
        }
        
        static {
            Object unsafe = null;
            if (PlatformDependent.hasUnsafe()) {
                unsafe = AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
                    @Override
                    public Object run() {
                        return UnsafeAccess.UNSAFE;
                    }
                });
            }
            if (unsafe == null) {
                PlatformDependent.logger.debug("org.jctools-core.MpscChunkedArrayQueue: unavailable");
                USE_MPSC_CHUNKED_ARRAY_QUEUE = false;
            }
            else {
                PlatformDependent.logger.debug("org.jctools-core.MpscChunkedArrayQueue: available");
                USE_MPSC_CHUNKED_ARRAY_QUEUE = true;
            }
        }
    }
    
    private static final class AtomicLongCounter extends AtomicLong implements LongCounter
    {
        private static final long serialVersionUID = 4074772784610639305L;
        
        @Override
        public void add(final long delta) {
            this.addAndGet(delta);
        }
        
        @Override
        public void increment() {
            this.incrementAndGet();
        }
        
        @Override
        public void decrement() {
            this.decrementAndGet();
        }
        
        @Override
        public long value() {
            return this.get();
        }
    }
    
    private interface ThreadLocalRandomProvider
    {
        Random current();
    }
}
