// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.nio.Buffer;
import java.lang.reflect.AccessibleObject;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.lang.reflect.Constructor;
import sun.misc.Unsafe;
import io.netty.util.internal.logging.InternalLogger;

final class PlatformDependent0
{
    private static final InternalLogger logger;
    private static final Unsafe UNSAFE;
    private static final long ADDRESS_FIELD_OFFSET;
    private static final long BYTE_ARRAY_BASE_OFFSET;
    private static final Constructor<?> DIRECT_BUFFER_CONSTRUCTOR;
    static final int HASH_CODE_ASCII_SEED = -1028477387;
    static final int HASH_CODE_C1 = 461845907;
    static final int HASH_CODE_C2 = 461845907;
    private static final long UNSAFE_COPY_THRESHOLD = 1048576L;
    private static final boolean UNALIGNED;
    
    static boolean isUnaligned() {
        return PlatformDependent0.UNALIGNED;
    }
    
    static boolean hasUnsafe() {
        return PlatformDependent0.UNSAFE != null;
    }
    
    static boolean unalignedAccess() {
        return PlatformDependent0.UNALIGNED;
    }
    
    static void throwException(final Throwable cause) {
        PlatformDependent0.UNSAFE.throwException(ObjectUtil.checkNotNull(cause, "cause"));
    }
    
    static boolean hasDirectBufferNoCleanerConstructor() {
        return PlatformDependent0.DIRECT_BUFFER_CONSTRUCTOR != null;
    }
    
    static ByteBuffer reallocateDirectNoCleaner(final ByteBuffer buffer, final int capacity) {
        return newDirectBuffer(PlatformDependent0.UNSAFE.reallocateMemory(directBufferAddress(buffer), capacity), capacity);
    }
    
    static ByteBuffer allocateDirectNoCleaner(final int capacity) {
        return newDirectBuffer(PlatformDependent0.UNSAFE.allocateMemory(capacity), capacity);
    }
    
    static ByteBuffer newDirectBuffer(final long address, final int capacity) {
        ObjectUtil.checkPositiveOrZero(address, "address");
        ObjectUtil.checkPositiveOrZero(capacity, "capacity");
        try {
            return (ByteBuffer)PlatformDependent0.DIRECT_BUFFER_CONSTRUCTOR.newInstance(address, capacity);
        }
        catch (final Throwable cause) {
            if (cause instanceof Error) {
                throw (Error)cause;
            }
            throw new Error(cause);
        }
    }
    
    static void freeDirectBuffer(final ByteBuffer buffer) {
        Cleaner0.freeDirectBuffer(buffer);
    }
    
    static long directBufferAddress(final ByteBuffer buffer) {
        return getLong(buffer, PlatformDependent0.ADDRESS_FIELD_OFFSET);
    }
    
    static long byteArrayBaseOffset() {
        return PlatformDependent0.BYTE_ARRAY_BASE_OFFSET;
    }
    
    static Object getObject(final Object object, final long fieldOffset) {
        return PlatformDependent0.UNSAFE.getObject(object, fieldOffset);
    }
    
    static int getInt(final Object object, final long fieldOffset) {
        return PlatformDependent0.UNSAFE.getInt(object, fieldOffset);
    }
    
    private static long getLong(final Object object, final long fieldOffset) {
        return PlatformDependent0.UNSAFE.getLong(object, fieldOffset);
    }
    
    static long objectFieldOffset(final Field field) {
        return PlatformDependent0.UNSAFE.objectFieldOffset(field);
    }
    
    static byte getByte(final long address) {
        return PlatformDependent0.UNSAFE.getByte(address);
    }
    
    static short getShort(final long address) {
        return PlatformDependent0.UNSAFE.getShort(address);
    }
    
    static int getInt(final long address) {
        return PlatformDependent0.UNSAFE.getInt(address);
    }
    
    static long getLong(final long address) {
        return PlatformDependent0.UNSAFE.getLong(address);
    }
    
    static byte getByte(final byte[] data, final int index) {
        return PlatformDependent0.UNSAFE.getByte(data, PlatformDependent0.BYTE_ARRAY_BASE_OFFSET + index);
    }
    
    static short getShort(final byte[] data, final int index) {
        return PlatformDependent0.UNSAFE.getShort(data, PlatformDependent0.BYTE_ARRAY_BASE_OFFSET + index);
    }
    
    static int getInt(final byte[] data, final int index) {
        return PlatformDependent0.UNSAFE.getInt(data, PlatformDependent0.BYTE_ARRAY_BASE_OFFSET + index);
    }
    
    static long getLong(final byte[] data, final int index) {
        return PlatformDependent0.UNSAFE.getLong(data, PlatformDependent0.BYTE_ARRAY_BASE_OFFSET + index);
    }
    
    static void putByte(final long address, final byte value) {
        PlatformDependent0.UNSAFE.putByte(address, value);
    }
    
    static void putShort(final long address, final short value) {
        PlatformDependent0.UNSAFE.putShort(address, value);
    }
    
    static void putInt(final long address, final int value) {
        PlatformDependent0.UNSAFE.putInt(address, value);
    }
    
    static void putLong(final long address, final long value) {
        PlatformDependent0.UNSAFE.putLong(address, value);
    }
    
    static void putByte(final byte[] data, final int index, final byte value) {
        PlatformDependent0.UNSAFE.putByte(data, PlatformDependent0.BYTE_ARRAY_BASE_OFFSET + index, value);
    }
    
    static void putShort(final byte[] data, final int index, final short value) {
        PlatformDependent0.UNSAFE.putShort(data, PlatformDependent0.BYTE_ARRAY_BASE_OFFSET + index, value);
    }
    
    static void putInt(final byte[] data, final int index, final int value) {
        PlatformDependent0.UNSAFE.putInt(data, PlatformDependent0.BYTE_ARRAY_BASE_OFFSET + index, value);
    }
    
    static void putLong(final byte[] data, final int index, final long value) {
        PlatformDependent0.UNSAFE.putLong(data, PlatformDependent0.BYTE_ARRAY_BASE_OFFSET + index, value);
    }
    
    static void copyMemory(long srcAddr, long dstAddr, long length) {
        while (length > 0L) {
            final long size = Math.min(length, 1048576L);
            PlatformDependent0.UNSAFE.copyMemory(srcAddr, dstAddr, size);
            length -= size;
            srcAddr += size;
            dstAddr += size;
        }
    }
    
    static void copyMemory(final Object src, long srcOffset, final Object dst, long dstOffset, long length) {
        while (length > 0L) {
            final long size = Math.min(length, 1048576L);
            PlatformDependent0.UNSAFE.copyMemory(src, srcOffset, dst, dstOffset, size);
            length -= size;
            srcOffset += size;
            dstOffset += size;
        }
    }
    
    static void setMemory(final long address, final long bytes, final byte value) {
        PlatformDependent0.UNSAFE.setMemory(address, bytes, value);
    }
    
    static void setMemory(final Object o, final long offset, final long bytes, final byte value) {
        PlatformDependent0.UNSAFE.setMemory(o, offset, bytes, value);
    }
    
    static boolean equals(final byte[] bytes1, final int startPos1, final byte[] bytes2, final int startPos2, final int length) {
        if (length == 0) {
            return true;
        }
        final long baseOffset1 = PlatformDependent0.BYTE_ARRAY_BASE_OFFSET + startPos1;
        final long baseOffset2 = PlatformDependent0.BYTE_ARRAY_BASE_OFFSET + startPos2;
        int remainingBytes = length & 0x7;
        for (long end = baseOffset1 + remainingBytes, i = baseOffset1 - 8L + length, j = baseOffset2 - 8L + length; i >= end; i -= 8L, j -= 8L) {
            if (PlatformDependent0.UNSAFE.getLong(bytes1, i) != PlatformDependent0.UNSAFE.getLong(bytes2, j)) {
                return false;
            }
        }
        if (remainingBytes >= 4) {
            remainingBytes -= 4;
            if (PlatformDependent0.UNSAFE.getInt(bytes1, baseOffset1 + remainingBytes) != PlatformDependent0.UNSAFE.getInt(bytes2, baseOffset2 + remainingBytes)) {
                return false;
            }
        }
        if (remainingBytes >= 2) {
            return PlatformDependent0.UNSAFE.getChar(bytes1, baseOffset1) == PlatformDependent0.UNSAFE.getChar(bytes2, baseOffset2) && (remainingBytes == 2 || bytes1[startPos1 + 2] == bytes2[startPos2 + 2]);
        }
        return bytes1[startPos1] == bytes2[startPos2];
    }
    
    static int equalsConstantTime(final byte[] bytes1, final int startPos1, final byte[] bytes2, final int startPos2, final int length) {
        long result = 0L;
        final long baseOffset1 = PlatformDependent0.BYTE_ARRAY_BASE_OFFSET + startPos1;
        final long baseOffset2 = PlatformDependent0.BYTE_ARRAY_BASE_OFFSET + startPos2;
        final int remainingBytes = length & 0x7;
        for (long end = baseOffset1 + remainingBytes, i = baseOffset1 - 8L + length, j = baseOffset2 - 8L + length; i >= end; i -= 8L, j -= 8L) {
            result |= (PlatformDependent0.UNSAFE.getLong(bytes1, i) ^ PlatformDependent0.UNSAFE.getLong(bytes2, j));
        }
        switch (remainingBytes) {
            case 7: {
                return ConstantTimeUtils.equalsConstantTime(result | (long)(PlatformDependent0.UNSAFE.getInt(bytes1, baseOffset1 + 3L) ^ PlatformDependent0.UNSAFE.getInt(bytes2, baseOffset2 + 3L)) | (long)(PlatformDependent0.UNSAFE.getChar(bytes1, baseOffset1 + 1L) ^ PlatformDependent0.UNSAFE.getChar(bytes2, baseOffset2 + 1L)) | (long)(PlatformDependent0.UNSAFE.getByte(bytes1, baseOffset1) ^ PlatformDependent0.UNSAFE.getByte(bytes2, baseOffset2)), 0L);
            }
            case 6: {
                return ConstantTimeUtils.equalsConstantTime(result | (long)(PlatformDependent0.UNSAFE.getInt(bytes1, baseOffset1 + 2L) ^ PlatformDependent0.UNSAFE.getInt(bytes2, baseOffset2 + 2L)) | (long)(PlatformDependent0.UNSAFE.getChar(bytes1, baseOffset1) ^ PlatformDependent0.UNSAFE.getChar(bytes2, baseOffset2)), 0L);
            }
            case 5: {
                return ConstantTimeUtils.equalsConstantTime(result | (long)(PlatformDependent0.UNSAFE.getInt(bytes1, baseOffset1 + 1L) ^ PlatformDependent0.UNSAFE.getInt(bytes2, baseOffset2 + 1L)) | (long)(PlatformDependent0.UNSAFE.getByte(bytes1, baseOffset1) ^ PlatformDependent0.UNSAFE.getByte(bytes2, baseOffset2)), 0L);
            }
            case 4: {
                return ConstantTimeUtils.equalsConstantTime(result | (long)(PlatformDependent0.UNSAFE.getInt(bytes1, baseOffset1) ^ PlatformDependent0.UNSAFE.getInt(bytes2, baseOffset2)), 0L);
            }
            case 3: {
                return ConstantTimeUtils.equalsConstantTime(result | (long)(PlatformDependent0.UNSAFE.getChar(bytes1, baseOffset1 + 1L) ^ PlatformDependent0.UNSAFE.getChar(bytes2, baseOffset2 + 1L)) | (long)(PlatformDependent0.UNSAFE.getByte(bytes1, baseOffset1) ^ PlatformDependent0.UNSAFE.getByte(bytes2, baseOffset2)), 0L);
            }
            case 2: {
                return ConstantTimeUtils.equalsConstantTime(result | (long)(PlatformDependent0.UNSAFE.getChar(bytes1, baseOffset1) ^ PlatformDependent0.UNSAFE.getChar(bytes2, baseOffset2)), 0L);
            }
            case 1: {
                return ConstantTimeUtils.equalsConstantTime(result | (long)(PlatformDependent0.UNSAFE.getByte(bytes1, baseOffset1) ^ PlatformDependent0.UNSAFE.getByte(bytes2, baseOffset2)), 0L);
            }
            default: {
                return ConstantTimeUtils.equalsConstantTime(result, 0L);
            }
        }
    }
    
    static int hashCodeAscii(final byte[] bytes, final int startPos, final int length) {
        int hash = -1028477387;
        final long baseOffset = PlatformDependent0.BYTE_ARRAY_BASE_OFFSET + startPos;
        final int remainingBytes = length & 0x7;
        for (long end = baseOffset + remainingBytes, i = baseOffset - 8L + length; i >= end; i -= 8L) {
            hash = hashCodeAsciiCompute(PlatformDependent0.UNSAFE.getLong(bytes, i), hash);
        }
        switch (remainingBytes) {
            case 7: {
                return ((hash * 461845907 + hashCodeAsciiSanitize(PlatformDependent0.UNSAFE.getByte(bytes, baseOffset))) * 461845907 + hashCodeAsciiSanitize(PlatformDependent0.UNSAFE.getShort(bytes, baseOffset + 1L))) * 461845907 + hashCodeAsciiSanitize(PlatformDependent0.UNSAFE.getInt(bytes, baseOffset + 3L));
            }
            case 6: {
                return (hash * 461845907 + hashCodeAsciiSanitize(PlatformDependent0.UNSAFE.getShort(bytes, baseOffset))) * 461845907 + hashCodeAsciiSanitize(PlatformDependent0.UNSAFE.getInt(bytes, baseOffset + 2L));
            }
            case 5: {
                return (hash * 461845907 + hashCodeAsciiSanitize(PlatformDependent0.UNSAFE.getByte(bytes, baseOffset))) * 461845907 + hashCodeAsciiSanitize(PlatformDependent0.UNSAFE.getInt(bytes, baseOffset + 1L));
            }
            case 4: {
                return hash * 461845907 + hashCodeAsciiSanitize(PlatformDependent0.UNSAFE.getInt(bytes, baseOffset));
            }
            case 3: {
                return (hash * 461845907 + hashCodeAsciiSanitize(PlatformDependent0.UNSAFE.getByte(bytes, baseOffset))) * 461845907 + hashCodeAsciiSanitize(PlatformDependent0.UNSAFE.getShort(bytes, baseOffset + 1L));
            }
            case 2: {
                return hash * 461845907 + hashCodeAsciiSanitize(PlatformDependent0.UNSAFE.getShort(bytes, baseOffset));
            }
            case 1: {
                return hash * 461845907 + hashCodeAsciiSanitize(PlatformDependent0.UNSAFE.getByte(bytes, baseOffset));
            }
            default: {
                return hash;
            }
        }
    }
    
    static int hashCodeAsciiCompute(final long value, final int hash) {
        return hash * 461845907 + hashCodeAsciiSanitize((int)value) * 461845907 + (int)((value & 0x1F1F1F1F00000000L) >>> 32);
    }
    
    static int hashCodeAsciiSanitize(final int value) {
        return value & 0x1F1F1F1F;
    }
    
    static int hashCodeAsciiSanitize(final short value) {
        return value & 0x1F1F;
    }
    
    static int hashCodeAsciiSanitize(final byte value) {
        return value & 0x1F;
    }
    
    static ClassLoader getClassLoader(final Class<?> clazz) {
        if (System.getSecurityManager() == null) {
            return clazz.getClassLoader();
        }
        return AccessController.doPrivileged((PrivilegedAction<ClassLoader>)new PrivilegedAction<ClassLoader>() {
            @Override
            public ClassLoader run() {
                return clazz.getClassLoader();
            }
        });
    }
    
    static ClassLoader getContextClassLoader() {
        if (System.getSecurityManager() == null) {
            return Thread.currentThread().getContextClassLoader();
        }
        return AccessController.doPrivileged((PrivilegedAction<ClassLoader>)new PrivilegedAction<ClassLoader>() {
            @Override
            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
    }
    
    static ClassLoader getSystemClassLoader() {
        if (System.getSecurityManager() == null) {
            return ClassLoader.getSystemClassLoader();
        }
        return AccessController.doPrivileged((PrivilegedAction<ClassLoader>)new PrivilegedAction<ClassLoader>() {
            @Override
            public ClassLoader run() {
                return ClassLoader.getSystemClassLoader();
            }
        });
    }
    
    static int addressSize() {
        return PlatformDependent0.UNSAFE.addressSize();
    }
    
    static long allocateMemory(final long size) {
        return PlatformDependent0.UNSAFE.allocateMemory(size);
    }
    
    static void freeMemory(final long address) {
        PlatformDependent0.UNSAFE.freeMemory(address);
    }
    
    private PlatformDependent0() {
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(PlatformDependent0.class);
        Field addressField = null;
        ByteBuffer direct;
        Unsafe unsafe;
        if (PlatformDependent.isExplicitNoUnsafe()) {
            direct = null;
            addressField = null;
            unsafe = null;
        }
        else {
            direct = ByteBuffer.allocateDirect(1);
            final Object maybeUnsafe = AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    try {
                        final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                        final Throwable cause = ReflectionUtil.trySetAccessible(unsafeField);
                        if (cause != null) {
                            return cause;
                        }
                        return unsafeField.get(null);
                    }
                    catch (final NoSuchFieldException e) {
                        return e;
                    }
                    catch (final SecurityException e2) {
                        return e2;
                    }
                    catch (final IllegalAccessException e3) {
                        return e3;
                    }
                }
            });
            if (maybeUnsafe instanceof Exception) {
                unsafe = null;
                PlatformDependent0.logger.debug("sun.misc.Unsafe.theUnsafe: unavailable", (Throwable)maybeUnsafe);
            }
            else {
                unsafe = (Unsafe)maybeUnsafe;
                PlatformDependent0.logger.debug("sun.misc.Unsafe.theUnsafe: available");
            }
            if (unsafe != null) {
                final Unsafe finalUnsafe = unsafe;
                final Object maybeException = AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
                    @Override
                    public Object run() {
                        try {
                            finalUnsafe.getClass().getDeclaredMethod("copyMemory", Object.class, Long.TYPE, Object.class, Long.TYPE, Long.TYPE);
                            return null;
                        }
                        catch (final NoSuchMethodException e) {
                            return e;
                        }
                        catch (final SecurityException e2) {
                            return e2;
                        }
                    }
                });
                if (maybeException == null) {
                    PlatformDependent0.logger.debug("sun.misc.Unsafe.copyMemory: available");
                }
                else {
                    unsafe = null;
                    PlatformDependent0.logger.debug("sun.misc.Unsafe.copyMemory: unavailable", (Throwable)maybeException);
                }
            }
            if (unsafe != null) {
                final Unsafe finalUnsafe = unsafe;
                final Object maybeAddressField = AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
                    @Override
                    public Object run() {
                        try {
                            final Field field = Buffer.class.getDeclaredField("address");
                            final long offset = finalUnsafe.objectFieldOffset(field);
                            final long address = finalUnsafe.getLong(direct, offset);
                            if (address == 0L) {
                                return null;
                            }
                            return field;
                        }
                        catch (final NoSuchFieldException e) {
                            return e;
                        }
                        catch (final SecurityException e2) {
                            return e2;
                        }
                    }
                });
                if (maybeAddressField instanceof Field) {
                    addressField = (Field)maybeAddressField;
                    PlatformDependent0.logger.debug("java.nio.Buffer.address: available");
                }
                else {
                    PlatformDependent0.logger.debug("java.nio.Buffer.address: unavailable", (Throwable)maybeAddressField);
                    unsafe = null;
                }
            }
            if (unsafe != null) {
                final long byteArrayIndexScale = unsafe.arrayIndexScale(byte[].class);
                if (byteArrayIndexScale != 1L) {
                    PlatformDependent0.logger.debug("unsafe.arrayIndexScale is {} (expected: 1). Not using unsafe.", (Object)byteArrayIndexScale);
                    unsafe = null;
                }
            }
        }
        if ((UNSAFE = unsafe) == null) {
            ADDRESS_FIELD_OFFSET = -1L;
            BYTE_ARRAY_BASE_OFFSET = -1L;
            UNALIGNED = false;
            DIRECT_BUFFER_CONSTRUCTOR = null;
        }
        else {
            long address = -1L;
            Constructor<?> directBufferConstructor;
            try {
                final Object maybeDirectBufferConstructor = AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
                    @Override
                    public Object run() {
                        try {
                            final Constructor<?> constructor = direct.getClass().getDeclaredConstructor(Long.TYPE, Integer.TYPE);
                            final Throwable cause = ReflectionUtil.trySetAccessible(constructor);
                            if (cause != null) {
                                return cause;
                            }
                            return constructor;
                        }
                        catch (final NoSuchMethodException e) {
                            return e;
                        }
                        catch (final SecurityException e2) {
                            return e2;
                        }
                    }
                });
                if (maybeDirectBufferConstructor instanceof Constructor) {
                    address = PlatformDependent0.UNSAFE.allocateMemory(1L);
                    try {
                        ((Constructor)maybeDirectBufferConstructor).newInstance(address, 1);
                        directBufferConstructor = (Constructor)maybeDirectBufferConstructor;
                        PlatformDependent0.logger.debug("direct buffer constructor: available");
                    }
                    catch (final InstantiationException e) {
                        directBufferConstructor = null;
                    }
                    catch (final IllegalAccessException e2) {
                        directBufferConstructor = null;
                    }
                    catch (final InvocationTargetException e3) {
                        directBufferConstructor = null;
                    }
                }
                else {
                    PlatformDependent0.logger.debug("direct buffer constructor: unavailable", (Throwable)maybeDirectBufferConstructor);
                    directBufferConstructor = null;
                }
            }
            finally {
                if (address != -1L) {
                    PlatformDependent0.UNSAFE.freeMemory(address);
                }
            }
            DIRECT_BUFFER_CONSTRUCTOR = directBufferConstructor;
            ADDRESS_FIELD_OFFSET = objectFieldOffset(addressField);
            BYTE_ARRAY_BASE_OFFSET = PlatformDependent0.UNSAFE.arrayBaseOffset(byte[].class);
            final Object maybeUnaligned = AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    try {
                        final Class<?> bitsClass = Class.forName("java.nio.Bits", false, PlatformDependent.getSystemClassLoader());
                        final Method unalignedMethod = bitsClass.getDeclaredMethod("unaligned", (Class<?>[])new Class[0]);
                        final Throwable cause = ReflectionUtil.trySetAccessible(unalignedMethod);
                        if (cause != null) {
                            return cause;
                        }
                        return unalignedMethod.invoke(null, new Object[0]);
                    }
                    catch (final NoSuchMethodException e) {
                        return e;
                    }
                    catch (final SecurityException e2) {
                        return e2;
                    }
                    catch (final IllegalAccessException e3) {
                        return e3;
                    }
                    catch (final ClassNotFoundException e4) {
                        return e4;
                    }
                    catch (final InvocationTargetException e5) {
                        return e5;
                    }
                }
            });
            boolean unaligned;
            if (maybeUnaligned instanceof Boolean) {
                unaligned = (boolean)maybeUnaligned;
                PlatformDependent0.logger.debug("java.nio.Bits.unaligned: available, {}", (Object)unaligned);
            }
            else {
                final String arch = SystemPropertyUtil.get("os.arch", "");
                unaligned = arch.matches("^(i[3-6]86|x86(_64)?|x64|amd64)$");
                final Throwable t = (Throwable)maybeUnaligned;
                PlatformDependent0.logger.debug("java.nio.Bits.unaligned: unavailable {}", (Object)unaligned, t);
            }
            UNALIGNED = unaligned;
        }
        PlatformDependent0.logger.debug("java.nio.DirectByteBuffer.<init>(long, int): {}", (PlatformDependent0.DIRECT_BUFFER_CONSTRUCTOR != null) ? "available" : "unavailable");
        if (direct != null) {
            freeDirectBuffer(direct);
        }
    }
}
