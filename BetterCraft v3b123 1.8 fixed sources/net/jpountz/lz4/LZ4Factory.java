// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.lz4;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.lang.reflect.Field;
import net.jpountz.util.Native;
import net.jpountz.util.Utils;

public final class LZ4Factory
{
    private static LZ4Factory NATIVE_INSTANCE;
    private static LZ4Factory JAVA_UNSAFE_INSTANCE;
    private static LZ4Factory JAVA_SAFE_INSTANCE;
    private final String impl;
    private final LZ4Compressor fastCompressor;
    private final LZ4Compressor highCompressor;
    private final LZ4FastDecompressor fastDecompressor;
    private final LZ4SafeDecompressor safeDecompressor;
    private final LZ4Compressor[] highCompressors;
    
    private static LZ4Factory instance(final String impl) {
        try {
            return new LZ4Factory(impl);
        }
        catch (final Exception e) {
            throw new AssertionError((Object)e);
        }
    }
    
    public static synchronized LZ4Factory nativeInstance() {
        if (LZ4Factory.NATIVE_INSTANCE == null) {
            LZ4Factory.NATIVE_INSTANCE = instance("JNI");
        }
        return LZ4Factory.NATIVE_INSTANCE;
    }
    
    public static synchronized LZ4Factory safeInstance() {
        if (LZ4Factory.JAVA_SAFE_INSTANCE == null) {
            LZ4Factory.JAVA_SAFE_INSTANCE = instance("JavaSafe");
        }
        return LZ4Factory.JAVA_SAFE_INSTANCE;
    }
    
    public static synchronized LZ4Factory unsafeInstance() {
        if (LZ4Factory.JAVA_UNSAFE_INSTANCE == null) {
            LZ4Factory.JAVA_UNSAFE_INSTANCE = instance("JavaUnsafe");
        }
        return LZ4Factory.JAVA_UNSAFE_INSTANCE;
    }
    
    public static LZ4Factory fastestJavaInstance() {
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
    
    public static LZ4Factory fastestInstance() {
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
        ClassLoader loader = LZ4Factory.class.getClassLoader();
        loader = ((loader == null) ? ClassLoader.getSystemClassLoader() : loader);
        final Class<?> c = loader.loadClass(cls);
        final Field f = c.getField("INSTANCE");
        return (T)f.get(null);
    }
    
    private LZ4Factory(final String impl) throws ClassNotFoundException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        this.highCompressors = new LZ4Compressor[18];
        this.impl = impl;
        this.fastCompressor = classInstance("net.jpountz.lz4.LZ4" + impl + "Compressor");
        this.highCompressor = classInstance("net.jpountz.lz4.LZ4HC" + impl + "Compressor");
        this.fastDecompressor = classInstance("net.jpountz.lz4.LZ4" + impl + "FastDecompressor");
        this.safeDecompressor = classInstance("net.jpountz.lz4.LZ4" + impl + "SafeDecompressor");
        final Constructor<? extends LZ4Compressor> highConstructor = this.highCompressor.getClass().getDeclaredConstructor(Integer.TYPE);
        this.highCompressors[9] = this.highCompressor;
        for (int level = 1; level <= 17; ++level) {
            if (level != 9) {
                this.highCompressors[level] = (LZ4Compressor)highConstructor.newInstance(level);
            }
        }
        final byte[] original = { 97, 98, 99, 100, 32, 32, 32, 32, 32, 32, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106 };
        for (final LZ4Compressor compressor : Arrays.asList(this.fastCompressor, this.highCompressor)) {
            final int maxCompressedLength = compressor.maxCompressedLength(original.length);
            final byte[] compressed = new byte[maxCompressedLength];
            final int compressedLength = compressor.compress(original, 0, original.length, compressed, 0, maxCompressedLength);
            final byte[] restored = new byte[original.length];
            this.fastDecompressor.decompress(compressed, 0, restored, 0, original.length);
            if (!Arrays.equals(original, restored)) {
                throw new AssertionError();
            }
            Arrays.fill(restored, (byte)0);
            final int decompressedLength = this.safeDecompressor.decompress(compressed, 0, compressedLength, restored, 0);
            if (decompressedLength != original.length || !Arrays.equals(original, restored)) {
                throw new AssertionError();
            }
        }
    }
    
    public LZ4Compressor fastCompressor() {
        return this.fastCompressor;
    }
    
    public LZ4Compressor highCompressor() {
        return this.highCompressor;
    }
    
    public LZ4Compressor highCompressor(int compressionLevel) {
        if (compressionLevel > 17) {
            compressionLevel = 17;
        }
        else if (compressionLevel < 1) {
            compressionLevel = 9;
        }
        return this.highCompressors[compressionLevel];
    }
    
    public LZ4FastDecompressor fastDecompressor() {
        return this.fastDecompressor;
    }
    
    public LZ4SafeDecompressor safeDecompressor() {
        return this.safeDecompressor;
    }
    
    @Deprecated
    public LZ4UnknownSizeDecompressor unknownSizeDecompressor() {
        return this.safeDecompressor();
    }
    
    @Deprecated
    public LZ4Decompressor decompressor() {
        return this.fastDecompressor();
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
