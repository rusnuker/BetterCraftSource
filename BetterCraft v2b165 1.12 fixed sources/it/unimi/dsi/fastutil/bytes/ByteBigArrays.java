// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.io.Serializable;
import java.util.Random;
import java.util.Arrays;
import it.unimi.dsi.fastutil.BigArrays;
import it.unimi.dsi.fastutil.Hash;

public class ByteBigArrays
{
    public static final byte[][] EMPTY_BIG_ARRAY;
    public static final Hash.Strategy HASH_STRATEGY;
    private static final int SMALL = 7;
    private static final int MEDIUM = 40;
    private static final int DIGIT_BITS = 8;
    private static final int DIGIT_MASK = 255;
    private static final int DIGITS_PER_ELEMENT = 1;
    
    private ByteBigArrays() {
    }
    
    public static byte get(final byte[][] array, final long index) {
        return array[BigArrays.segment(index)][BigArrays.displacement(index)];
    }
    
    public static void set(final byte[][] array, final long index, final byte value) {
        array[BigArrays.segment(index)][BigArrays.displacement(index)] = value;
    }
    
    public static void swap(final byte[][] array, final long first, final long second) {
        final byte t = array[BigArrays.segment(first)][BigArrays.displacement(first)];
        array[BigArrays.segment(first)][BigArrays.displacement(first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
        array[BigArrays.segment(second)][BigArrays.displacement(second)] = t;
    }
    
    public static void add(final byte[][] array, final long index, final byte incr) {
        final byte[] array2 = array[BigArrays.segment(index)];
        final int displacement = BigArrays.displacement(index);
        array2[displacement] += incr;
    }
    
    public static void mul(final byte[][] array, final long index, final byte factor) {
        final byte[] array2 = array[BigArrays.segment(index)];
        final int displacement = BigArrays.displacement(index);
        array2[displacement] *= factor;
    }
    
    public static void incr(final byte[][] array, final long index) {
        final byte[] array2 = array[BigArrays.segment(index)];
        final int displacement = BigArrays.displacement(index);
        ++array2[displacement];
    }
    
    public static void decr(final byte[][] array, final long index) {
        final byte[] array2 = array[BigArrays.segment(index)];
        final int displacement = BigArrays.displacement(index);
        --array2[displacement];
    }
    
    public static long length(final byte[][] array) {
        final int length = array.length;
        return (length == 0) ? 0L : (BigArrays.start(length - 1) + array[length - 1].length);
    }
    
    public static void copy(final byte[][] srcArray, final long srcPos, final byte[][] destArray, final long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = BigArrays.segment(srcPos);
            int destSegment = BigArrays.segment(destPos);
            int srcDispl = BigArrays.displacement(srcPos);
            int destDispl = BigArrays.displacement(destPos);
            while (length > 0L) {
                final int l = (int)Math.min(length, Math.min(srcArray[srcSegment].length - srcDispl, destArray[destSegment].length - destDispl));
                System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l);
                if ((srcDispl += l) == 134217728) {
                    srcDispl = 0;
                    ++srcSegment;
                }
                if ((destDispl += l) == 134217728) {
                    destDispl = 0;
                    ++destSegment;
                }
                length -= l;
            }
        }
        else {
            int srcSegment = BigArrays.segment(srcPos + length);
            int destSegment = BigArrays.segment(destPos + length);
            int srcDispl = BigArrays.displacement(srcPos + length);
            int destDispl = BigArrays.displacement(destPos + length);
            while (length > 0L) {
                if (srcDispl == 0) {
                    srcDispl = 134217728;
                    --srcSegment;
                }
                if (destDispl == 0) {
                    destDispl = 134217728;
                    --destSegment;
                }
                final int l = (int)Math.min(length, Math.min(srcDispl, destDispl));
                System.arraycopy(srcArray[srcSegment], srcDispl - l, destArray[destSegment], destDispl - l, l);
                srcDispl -= l;
                destDispl -= l;
                length -= l;
            }
        }
    }
    
    public static void copyFromBig(final byte[][] srcArray, final long srcPos, final byte[] destArray, int destPos, int length) {
        int srcSegment = BigArrays.segment(srcPos);
        int srcDispl = BigArrays.displacement(srcPos);
        while (length > 0) {
            final int l = Math.min(srcArray[srcSegment].length - srcDispl, length);
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
            if ((srcDispl += l) == 134217728) {
                srcDispl = 0;
                ++srcSegment;
            }
            destPos += l;
            length -= l;
        }
    }
    
    public static void copyToBig(final byte[] srcArray, int srcPos, final byte[][] destArray, final long destPos, long length) {
        int destSegment = BigArrays.segment(destPos);
        int destDispl = BigArrays.displacement(destPos);
        while (length > 0L) {
            final int l = (int)Math.min(destArray[destSegment].length - destDispl, length);
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
            if ((destDispl += l) == 134217728) {
                destDispl = 0;
                ++destSegment;
            }
            srcPos += l;
            length -= l;
        }
    }
    
    public static byte[][] newBigArray(final long length) {
        if (length == 0L) {
            return ByteBigArrays.EMPTY_BIG_ARRAY;
        }
        BigArrays.ensureLength(length);
        final int baseLength = (int)(length + 134217727L >>> 27);
        final byte[][] base = new byte[baseLength][];
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            for (int i = 0; i < baseLength - 1; ++i) {
                base[i] = new byte[134217728];
            }
            base[baseLength - 1] = new byte[residual];
        }
        else {
            for (int i = 0; i < baseLength; ++i) {
                base[i] = new byte[134217728];
            }
        }
        return base;
    }
    
    public static byte[][] wrap(final byte[] array) {
        if (array.length == 0) {
            return ByteBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 134217728) {
            return new byte[][] { array };
        }
        final byte[][] bigArray = newBigArray(array.length);
        for (int i = 0; i < bigArray.length; ++i) {
            System.arraycopy(array, (int)BigArrays.start(i), bigArray[i], 0, bigArray[i].length);
        }
        return bigArray;
    }
    
    public static byte[][] ensureCapacity(final byte[][] array, final long length) {
        return ensureCapacity(array, length, length(array));
    }
    
    public static byte[][] ensureCapacity(final byte[][] array, final long length, final long preserve) {
        final long oldLength = length(array);
        if (length > oldLength) {
            BigArrays.ensureLength(length);
            final int valid = array.length - ((array.length != 0 && (array.length <= 0 || array[array.length - 1].length != 134217728)) ? 1 : 0);
            final int baseLength = (int)(length + 134217727L >>> 27);
            final byte[][] base = Arrays.copyOf(array, baseLength);
            final int residual = (int)(length & 0x7FFFFFFL);
            if (residual != 0) {
                for (int i = valid; i < baseLength - 1; ++i) {
                    base[i] = new byte[134217728];
                }
                base[baseLength - 1] = new byte[residual];
            }
            else {
                for (int i = valid; i < baseLength; ++i) {
                    base[i] = new byte[134217728];
                }
            }
            if (preserve - valid * 134217728L > 0L) {
                copy(array, valid * 134217728L, base, valid * 134217728L, preserve - valid * 134217728L);
            }
            return base;
        }
        return array;
    }
    
    public static byte[][] grow(final byte[][] array, final long length) {
        final long oldLength = length(array);
        return (length > oldLength) ? grow(array, length, oldLength) : array;
    }
    
    public static byte[][] grow(final byte[][] array, final long length, final long preserve) {
        final long oldLength = length(array);
        return (length > oldLength) ? ensureCapacity(array, Math.max(2L * oldLength, length), preserve) : array;
    }
    
    public static byte[][] trim(final byte[][] array, final long length) {
        BigArrays.ensureLength(length);
        final long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        }
        final int baseLength = (int)(length + 134217727L >>> 27);
        final byte[][] base = Arrays.copyOf(array, baseLength);
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            base[baseLength - 1] = ByteArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }
    
    public static byte[][] setLength(final byte[][] array, final long length) {
        final long oldLength = length(array);
        if (length == oldLength) {
            return array;
        }
        if (length < oldLength) {
            return trim(array, length);
        }
        return ensureCapacity(array, length);
    }
    
    public static byte[][] copy(final byte[][] array, final long offset, final long length) {
        ensureOffsetLength(array, offset, length);
        final byte[][] a = newBigArray(length);
        copy(array, offset, a, 0L, length);
        return a;
    }
    
    public static byte[][] copy(final byte[][] array) {
        final byte[][] base = array.clone();
        int i = base.length;
        while (i-- != 0) {
            base[i] = array[i].clone();
        }
        return base;
    }
    
    public static void fill(final byte[][] array, final byte value) {
        int i = array.length;
        while (i-- != 0) {
            Arrays.fill(array[i], value);
        }
    }
    
    public static void fill(final byte[][] array, final long from, final long to, final byte value) {
        final long length = length(array);
        BigArrays.ensureFromTo(length, from, to);
        final int fromSegment = BigArrays.segment(from);
        int toSegment = BigArrays.segment(to);
        final int fromDispl = BigArrays.displacement(from);
        final int toDispl = BigArrays.displacement(to);
        if (fromSegment == toSegment) {
            Arrays.fill(array[fromSegment], fromDispl, toDispl, value);
            return;
        }
        if (toDispl != 0) {
            Arrays.fill(array[toSegment], 0, toDispl, value);
        }
        while (--toSegment > fromSegment) {
            Arrays.fill(array[toSegment], value);
        }
        Arrays.fill(array[fromSegment], fromDispl, 134217728, value);
    }
    
    public static boolean equals(final byte[][] a1, final byte[][] a2) {
        if (length(a1) != length(a2)) {
            return false;
        }
        int i = a1.length;
        while (i-- != 0) {
            final byte[] t = a1[i];
            final byte[] u = a2[i];
            int j = t.length;
            while (j-- != 0) {
                if (t[j] != u[j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static String toString(final byte[][] a) {
        if (a == null) {
            return "null";
        }
        final long last = length(a) - 1L;
        if (last == -1L) {
            return "[]";
        }
        final StringBuilder b = new StringBuilder();
        b.append('[');
        long i = 0L;
        while (true) {
            b.append(String.valueOf(get(a, i)));
            if (i == last) {
                break;
            }
            b.append(", ");
            ++i;
        }
        return b.append(']').toString();
    }
    
    public static void ensureFromTo(final byte[][] a, final long from, final long to) {
        BigArrays.ensureFromTo(length(a), from, to);
    }
    
    public static void ensureOffsetLength(final byte[][] a, final long offset, final long length) {
        BigArrays.ensureOffsetLength(length(a), offset, length);
    }
    
    private static void vecSwap(final byte[][] x, long a, long b, final long n) {
        for (int i = 0; i < n; ++i, ++a, ++b) {
            swap(x, a, b);
        }
    }
    
    private static long med3(final byte[][] x, final long a, final long b, final long c, final ByteComparator comp) {
        final int ab = comp.compare(get(x, a), get(x, b));
        final int ac = comp.compare(get(x, a), get(x, c));
        final int bc = comp.compare(get(x, b), get(x, c));
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static void selectionSort(final byte[][] a, final long from, final long to, final ByteComparator comp) {
        for (long i = from; i < to - 1L; ++i) {
            long m = i;
            for (long j = i + 1L; j < to; ++j) {
                if (comp.compare(get(a, j), get(a, m)) < 0) {
                    m = j;
                }
            }
            if (m != i) {
                swap(a, i, m);
            }
        }
    }
    
    public static void quickSort(final byte[][] x, final long from, final long to, final ByteComparator comp) {
        final long len = to - from;
        if (len < 7L) {
            selectionSort(x, from, to, comp);
            return;
        }
        long m = from + len / 2L;
        if (len > 7L) {
            long l = from;
            long n = to - 1L;
            if (len > 40L) {
                final long s = len / 8L;
                l = med3(x, l, l + s, l + 2L * s, comp);
                m = med3(x, m - s, m, m + s, comp);
                n = med3(x, n - 2L * s, n - s, n, comp);
            }
            m = med3(x, l, m, n, comp);
        }
        final byte v = get(x, m);
        long b;
        long a = b = from;
        long d;
        long c = d = to - 1L;
        while (true) {
            int comparison;
            if (b <= c && (comparison = comp.compare(get(x, b), v)) <= 0) {
                if (comparison == 0) {
                    swap(x, a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = comp.compare(get(x, c), v)) >= 0) {
                    if (comparison == 0) {
                        swap(x, c, d--);
                    }
                    --c;
                }
                if (b > c) {
                    break;
                }
                swap(x, b++, c--);
            }
        }
        final long n2 = to;
        long s2 = Math.min(a - from, b - a);
        vecSwap(x, from, b - s2, s2);
        s2 = Math.min(d - c, n2 - d - 1L);
        vecSwap(x, b, n2 - s2, s2);
        if ((s2 = b - a) > 1L) {
            quickSort(x, from, from + s2, comp);
        }
        if ((s2 = d - c) > 1L) {
            quickSort(x, n2 - s2, n2, comp);
        }
    }
    
    private static long med3(final byte[][] x, final long a, final long b, final long c) {
        final int ab = Byte.compare(get(x, a), get(x, b));
        final int ac = Byte.compare(get(x, a), get(x, c));
        final int bc = Byte.compare(get(x, b), get(x, c));
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static void selectionSort(final byte[][] a, final long from, final long to) {
        for (long i = from; i < to - 1L; ++i) {
            long m = i;
            for (long j = i + 1L; j < to; ++j) {
                if (get(a, j) < get(a, m)) {
                    m = j;
                }
            }
            if (m != i) {
                swap(a, i, m);
            }
        }
    }
    
    public static void quickSort(final byte[][] x, final ByteComparator comp) {
        quickSort(x, 0L, length(x), comp);
    }
    
    public static void quickSort(final byte[][] x, final long from, final long to) {
        final long len = to - from;
        if (len < 7L) {
            selectionSort(x, from, to);
            return;
        }
        long m = from + len / 2L;
        if (len > 7L) {
            long l = from;
            long n = to - 1L;
            if (len > 40L) {
                final long s = len / 8L;
                l = med3(x, l, l + s, l + 2L * s);
                m = med3(x, m - s, m, m + s);
                n = med3(x, n - 2L * s, n - s, n);
            }
            m = med3(x, l, m, n);
        }
        final byte v = get(x, m);
        long b;
        long a = b = from;
        long d;
        long c = d = to - 1L;
        while (true) {
            int comparison;
            if (b <= c && (comparison = Byte.compare(get(x, b), v)) <= 0) {
                if (comparison == 0) {
                    swap(x, a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = Byte.compare(get(x, c), v)) >= 0) {
                    if (comparison == 0) {
                        swap(x, c, d--);
                    }
                    --c;
                }
                if (b > c) {
                    break;
                }
                swap(x, b++, c--);
            }
        }
        final long n2 = to;
        long s2 = Math.min(a - from, b - a);
        vecSwap(x, from, b - s2, s2);
        s2 = Math.min(d - c, n2 - d - 1L);
        vecSwap(x, b, n2 - s2, s2);
        if ((s2 = b - a) > 1L) {
            quickSort(x, from, from + s2);
        }
        if ((s2 = d - c) > 1L) {
            quickSort(x, n2 - s2, n2);
        }
    }
    
    public static void quickSort(final byte[][] x) {
        quickSort(x, 0L, length(x));
    }
    
    public static long binarySearch(final byte[][] a, long from, long to, final byte key) {
        --to;
        while (from <= to) {
            final long mid = from + to >>> 1;
            final byte midVal = get(a, mid);
            if (midVal < key) {
                from = mid + 1L;
            }
            else {
                if (midVal <= key) {
                    return mid;
                }
                to = mid - 1L;
            }
        }
        return -(from + 1L);
    }
    
    public static long binarySearch(final byte[][] a, final byte key) {
        return binarySearch(a, 0L, length(a), key);
    }
    
    public static long binarySearch(final byte[][] a, long from, long to, final byte key, final ByteComparator c) {
        --to;
        while (from <= to) {
            final long mid = from + to >>> 1;
            final byte midVal = get(a, mid);
            final int cmp = c.compare(midVal, key);
            if (cmp < 0) {
                from = mid + 1L;
            }
            else {
                if (cmp <= 0) {
                    return mid;
                }
                to = mid - 1L;
            }
        }
        return -(from + 1L);
    }
    
    public static long binarySearch(final byte[][] a, final byte key, final ByteComparator c) {
        return binarySearch(a, 0L, length(a), key, c);
    }
    
    public static void radixSort(final byte[][] a) {
        radixSort(a, 0L, length(a));
    }
    
    public static void radixSort(final byte[][] a, final long from, final long to) {
        final int maxLevel = 0;
        final int stackSize = 1;
        final long[] offsetStack = { 0L };
        int offsetPos = 0;
        final long[] lengthStack = { 0L };
        int lengthPos = 0;
        final int[] levelStack = { 0 };
        int levelPos = 0;
        offsetStack[offsetPos++] = from;
        lengthStack[lengthPos++] = to - from;
        levelStack[levelPos++] = 0;
        final long[] count = new long[256];
        final long[] pos = new long[256];
        final byte[][] digit = newBigArray(to - from);
        while (offsetPos > 0) {
            final long first = offsetStack[--offsetPos];
            final long length = lengthStack[--lengthPos];
            final int level = levelStack[--levelPos];
            final int signMask = (level % 1 == 0) ? 128 : 0;
            if (length < 40L) {
                selectionSort(a, first, first + length);
            }
            else {
                final int shift = (0 - level % 1) * 8;
                long i = length;
                while (i-- != 0L) {
                    set(digit, i, (byte)((get(a, first + i) >>> shift & 0xFF) ^ signMask));
                }
                i = length;
                while (i-- != 0L) {
                    final long[] array = count;
                    final int n = get(digit, i) & 0xFF;
                    ++array[n];
                }
                int lastUsed = -1;
                long p = 0L;
                for (int j = 0; j < 256; ++j) {
                    if (count[j] != 0L) {
                        lastUsed = j;
                        if (level < 0 && count[j] > 1L) {
                            offsetStack[offsetPos++] = p + first;
                            lengthStack[lengthPos++] = count[j];
                            levelStack[levelPos++] = level + 1;
                        }
                    }
                    p = (pos[j] = p + count[j]);
                }
                final long end = length - count[lastUsed];
                count[lastUsed] = 0L;
                int c = -1;
                for (long k = 0L; k < end; k += count[c], count[c] = 0L) {
                    byte t = get(a, k + first);
                    c = (get(digit, k) & 0xFF);
                    while (true) {
                        final long[] array2 = pos;
                        final int n2 = c;
                        final long n3 = array2[n2] - 1L;
                        array2[n2] = n3;
                        final long d = n3;
                        if (n3 <= k) {
                            break;
                        }
                        final byte z = t;
                        final int zz = c;
                        t = get(a, d + first);
                        c = (get(digit, d) & 0xFF);
                        set(a, d + first, z);
                        set(digit, d, (byte)zz);
                    }
                    set(a, k + first, t);
                }
            }
        }
    }
    
    private static void selectionSort(final byte[][] a, final byte[][] b, final long from, final long to) {
        for (long i = from; i < to - 1L; ++i) {
            long m = i;
            for (long j = i + 1L; j < to; ++j) {
                if (get(a, j) < get(a, m) || (get(a, j) == get(a, m) && get(b, j) < get(b, m))) {
                    m = j;
                }
            }
            if (m != i) {
                byte t = get(a, i);
                set(a, i, get(a, m));
                set(a, m, t);
                t = get(b, i);
                set(b, i, get(b, m));
                set(b, m, t);
            }
        }
    }
    
    public static void radixSort(final byte[][] a, final byte[][] b) {
        radixSort(a, b, 0L, length(a));
    }
    
    public static void radixSort(final byte[][] a, final byte[][] b, final long from, final long to) {
        final int layers = 2;
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch.");
        }
        final int maxLevel = 1;
        final int stackSize = 256;
        final long[] offsetStack = new long[256];
        int offsetPos = 0;
        final long[] lengthStack = new long[256];
        int lengthPos = 0;
        final int[] levelStack = new int[256];
        int levelPos = 0;
        offsetStack[offsetPos++] = from;
        lengthStack[lengthPos++] = to - from;
        levelStack[levelPos++] = 0;
        final long[] count = new long[256];
        final long[] pos = new long[256];
        final byte[][] digit = newBigArray(to - from);
        while (offsetPos > 0) {
            final long first = offsetStack[--offsetPos];
            final long length = lengthStack[--lengthPos];
            final int level = levelStack[--levelPos];
            final int signMask = (level % 1 == 0) ? 128 : 0;
            if (length < 40L) {
                selectionSort(a, b, first, first + length);
            }
            else {
                final byte[][] k = (level < 1) ? a : b;
                final int shift = (0 - level % 1) * 8;
                long i = length;
                while (i-- != 0L) {
                    set(digit, i, (byte)((get(k, first + i) >>> shift & 0xFF) ^ signMask));
                }
                i = length;
                while (i-- != 0L) {
                    final long[] array = count;
                    final int n = get(digit, i) & 0xFF;
                    ++array[n];
                }
                int lastUsed = -1;
                long p = 0L;
                for (int j = 0; j < 256; ++j) {
                    if (count[j] != 0L) {
                        lastUsed = j;
                        if (level < 1 && count[j] > 1L) {
                            offsetStack[offsetPos++] = p + first;
                            lengthStack[lengthPos++] = count[j];
                            levelStack[levelPos++] = level + 1;
                        }
                    }
                    p = (pos[j] = p + count[j]);
                }
                final long end = length - count[lastUsed];
                count[lastUsed] = 0L;
                int c = -1;
                for (long l = 0L; l < end; l += count[c], count[c] = 0L) {
                    byte t = get(a, l + first);
                    byte u = get(b, l + first);
                    c = (get(digit, l) & 0xFF);
                    while (true) {
                        final long[] array2 = pos;
                        final int n2 = c;
                        final long n3 = array2[n2] - 1L;
                        array2[n2] = n3;
                        final long d = n3;
                        if (n3 <= l) {
                            break;
                        }
                        byte z = t;
                        final int zz = c;
                        t = get(a, d + first);
                        set(a, d + first, z);
                        z = u;
                        u = get(b, d + first);
                        set(b, d + first, z);
                        c = (get(digit, d) & 0xFF);
                        set(digit, d, (byte)zz);
                    }
                    set(a, l + first, t);
                    set(b, l + first, u);
                }
            }
        }
    }
    
    public static byte[][] shuffle(final byte[][] a, final long from, final long to, final Random random) {
        long i = to - from;
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final byte t = get(a, from + i);
            set(a, from + i, get(a, from + p));
            set(a, from + p, t);
        }
        return a;
    }
    
    public static byte[][] shuffle(final byte[][] a, final Random random) {
        long i = length(a);
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final byte t = get(a, i);
            set(a, i, get(a, p));
            set(a, p, t);
        }
        return a;
    }
    
    static {
        EMPTY_BIG_ARRAY = new byte[0][];
        HASH_STRATEGY = new BigArrayHashStrategy();
    }
    
    private static final class BigArrayHashStrategy implements Hash.Strategy<byte[][]>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        @Override
        public int hashCode(final byte[][] o) {
            return Arrays.deepHashCode(o);
        }
        
        @Override
        public boolean equals(final byte[][] a, final byte[][] b) {
            return ByteBigArrays.equals(a, b);
        }
    }
}
