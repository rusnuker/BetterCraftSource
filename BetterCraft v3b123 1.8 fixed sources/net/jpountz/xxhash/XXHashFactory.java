// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.xxhash;

import java.util.Random;
import java.lang.reflect.Field;
import net.jpountz.util.Native;
import net.jpountz.util.Utils;

public final class XXHashFactory
{
    private static XXHashFactory NATIVE_INSTANCE;
    private static XXHashFactory JAVA_UNSAFE_INSTANCE;
    private static XXHashFactory JAVA_SAFE_INSTANCE;
    private final String impl;
    private final XXHash32 hash32;
    private final XXHash64 hash64;
    private final StreamingXXHash32.Factory streamingHash32Factory;
    private final StreamingXXHash64.Factory streamingHash64Factory;
    
    private static XXHashFactory instance(final String impl) {
        try {
            return new XXHashFactory(impl);
        }
        catch (final Exception e) {
            throw new AssertionError((Object)e);
        }
    }
    
    public static synchronized XXHashFactory nativeInstance() {
        if (XXHashFactory.NATIVE_INSTANCE == null) {
            XXHashFactory.NATIVE_INSTANCE = instance("JNI");
        }
        return XXHashFactory.NATIVE_INSTANCE;
    }
    
    public static synchronized XXHashFactory safeInstance() {
        if (XXHashFactory.JAVA_SAFE_INSTANCE == null) {
            XXHashFactory.JAVA_SAFE_INSTANCE = instance("JavaSafe");
        }
        return XXHashFactory.JAVA_SAFE_INSTANCE;
    }
    
    public static synchronized XXHashFactory unsafeInstance() {
        if (XXHashFactory.JAVA_UNSAFE_INSTANCE == null) {
            XXHashFactory.JAVA_UNSAFE_INSTANCE = instance("JavaUnsafe");
        }
        return XXHashFactory.JAVA_UNSAFE_INSTANCE;
    }
    
    public static XXHashFactory fastestJavaInstance() {
        if (Utils.isUnalignedAccessAllowed()) {
            try {
                return unsafeInstance();
            }
            catch (final Throwable t) {
                return safeInstance();
            }
        }
        return safeInstance();
    }
    
    public static XXHashFactory fastestInstance() {
        if (!Native.isLoaded()) {
            if (Native.class.getClassLoader() != ClassLoader.getSystemClassLoader()) {
                return fastestJavaInstance();
            }
        }
        try {
            return nativeInstance();
        }
        catch (final Throwable t) {
            return fastestJavaInstance();
        }
        return fastestJavaInstance();
    }
    
    private static <T> T classInstance(final String cls) throws NoSuchFieldException, SecurityException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
        ClassLoader loader = XXHashFactory.class.getClassLoader();
        loader = ((loader == null) ? ClassLoader.getSystemClassLoader() : loader);
        final Class<?> c = loader.loadClass(cls);
        final Field f = c.getField("INSTANCE");
        return (T)f.get(null);
    }
    
    private XXHashFactory(final String impl) throws ClassNotFoundException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        this.impl = impl;
        this.hash32 = classInstance("net.jpountz.xxhash.XXHash32" + impl);
        this.streamingHash32Factory = classInstance("net.jpountz.xxhash.StreamingXXHash32" + impl + "$Factory");
        this.hash64 = classInstance("net.jpountz.xxhash.XXHash64" + impl);
        this.streamingHash64Factory = classInstance("net.jpountz.xxhash.StreamingXXHash64" + impl + "$Factory");
        final byte[] bytes = new byte[100];
        final Random random = new Random();
        random.nextBytes(bytes);
        final int seed = random.nextInt();
        final int h1 = this.hash32.hash(bytes, 0, bytes.length, seed);
        final StreamingXXHash32 streamingHash32 = this.newStreamingHash32(seed);
        streamingHash32.update(bytes, 0, bytes.length);
        final int h2 = streamingHash32.getValue();
        final long h3 = this.hash64.hash(bytes, 0, bytes.length, seed);
        final StreamingXXHash64 streamingHash33 = this.newStreamingHash64(seed);
        streamingHash33.update(bytes, 0, bytes.length);
        final long h4 = streamingHash33.getValue();
        if (h1 != h2) {
            throw new AssertionError();
        }
        if (h3 != h4) {
            throw new AssertionError();
        }
    }
    
    public XXHash32 hash32() {
        return this.hash32;
    }
    
    public XXHash64 hash64() {
        return this.hash64;
    }
    
    public StreamingXXHash32 newStreamingHash32(final int seed) {
        return this.streamingHash32Factory.newStreamingHash(seed);
    }
    
    public StreamingXXHash64 newStreamingHash64(final long seed) {
        return this.streamingHash64Factory.newStreamingHash(seed);
    }
    
    public static void main(final String[] args) {
        System.out.println("Fastest instance is " + fastestInstance());
        System.out.println("Fastest Java instance is " + fastestJavaInstance());
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ":" + this.impl;
    }
}
