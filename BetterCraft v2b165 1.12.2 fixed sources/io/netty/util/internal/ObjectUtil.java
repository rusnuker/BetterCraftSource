// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal;

public final class ObjectUtil
{
    private ObjectUtil() {
    }
    
    public static <T> T checkNotNull(final T arg, final String text) {
        if (arg == null) {
            throw new NullPointerException(text);
        }
        return arg;
    }
    
    public static int checkPositive(final int i, final String name) {
        if (i <= 0) {
            throw new IllegalArgumentException(name + ": " + i + " (expected: > 0)");
        }
        return i;
    }
    
    public static long checkPositive(final long i, final String name) {
        if (i <= 0L) {
            throw new IllegalArgumentException(name + ": " + i + " (expected: > 0)");
        }
        return i;
    }
    
    public static int checkPositiveOrZero(final int i, final String name) {
        if (i < 0) {
            throw new IllegalArgumentException(name + ": " + i + " (expected: >= 0)");
        }
        return i;
    }
    
    public static long checkPositiveOrZero(final long i, final String name) {
        if (i < 0L) {
            throw new IllegalArgumentException(name + ": " + i + " (expected: >= 0)");
        }
        return i;
    }
    
    public static <T> T[] checkNonEmpty(final T[] array, final String name) {
        checkNotNull(array, name);
        checkPositive(array.length, name + ".length");
        return array;
    }
    
    public static int intValue(final Integer wrapper, final int defaultValue) {
        return (wrapper != null) ? wrapper : defaultValue;
    }
    
    public static long longValue(final Long wrapper, final long defaultValue) {
        return (wrapper != null) ? wrapper : defaultValue;
    }
}
