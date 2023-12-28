// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil;

import it.unimi.dsi.fastutil.ints.IntBigArrays;
import it.unimi.dsi.fastutil.longs.LongComparator;

public class BigArrays
{
    public static final int SEGMENT_SHIFT = 27;
    public static final int SEGMENT_SIZE = 134217728;
    public static final int SEGMENT_MASK = 134217727;
    private static final int SMALL = 7;
    private static final int MEDIUM = 40;
    
    protected BigArrays() {
    }
    
    public static int segment(final long index) {
        return (int)(index >>> 27);
    }
    
    public static int displacement(final long index) {
        return (int)(index & 0x7FFFFFFL);
    }
    
    public static long start(final int segment) {
        return (long)segment << 27;
    }
    
    public static long index(final int segment, final int displacement) {
        return start(segment) + displacement;
    }
    
    public static void ensureFromTo(final long bigArrayLength, final long from, final long to) {
        if (from < 0L) {
            throw new ArrayIndexOutOfBoundsException("Start index (" + from + ") is negative");
        }
        if (from > to) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        if (to > bigArrayLength) {
            throw new ArrayIndexOutOfBoundsException("End index (" + to + ") is greater than big-array length (" + bigArrayLength + ")");
        }
    }
    
    public static void ensureOffsetLength(final long bigArrayLength, final long offset, final long length) {
        if (offset < 0L) {
            throw new ArrayIndexOutOfBoundsException("Offset (" + offset + ") is negative");
        }
        if (length < 0L) {
            throw new IllegalArgumentException("Length (" + length + ") is negative");
        }
        if (offset + length > bigArrayLength) {
            throw new ArrayIndexOutOfBoundsException("Last index (" + (offset + length) + ") is greater than big-array length (" + bigArrayLength + ")");
        }
    }
    
    public static void ensureLength(final long bigArrayLength) {
        if (bigArrayLength < 0L) {
            throw new IllegalArgumentException("Negative big-array size: " + bigArrayLength);
        }
        if (bigArrayLength >= 288230376017494016L) {
            throw new IllegalArgumentException("Big-array size too big: " + bigArrayLength);
        }
    }
    
    private static void inPlaceMerge(final long from, long mid, final long to, final LongComparator comp, final BigSwapper swapper) {
        if (from >= mid || mid >= to) {
            return;
        }
        if (to - from == 2L) {
            if (comp.compare(mid, from) < 0) {
                swapper.swap(from, mid);
            }
            return;
        }
        long firstCut;
        long secondCut;
        if (mid - from > to - mid) {
            firstCut = from + (mid - from) / 2L;
            secondCut = lowerBound(mid, to, firstCut, comp);
        }
        else {
            secondCut = mid + (to - mid) / 2L;
            firstCut = upperBound(from, mid, secondCut, comp);
        }
        final long first2 = firstCut;
        final long middle2 = mid;
        final long last2 = secondCut;
        if (middle2 != first2 && middle2 != last2) {
            long first3 = first2;
            long last3 = middle2;
            while (first3 < --last3) {
                swapper.swap(first3++, last3);
            }
            first3 = middle2;
            last3 = last2;
            while (first3 < --last3) {
                swapper.swap(first3++, last3);
            }
            first3 = first2;
            last3 = last2;
            while (first3 < --last3) {
                swapper.swap(first3++, last3);
            }
        }
        mid = firstCut + (secondCut - mid);
        inPlaceMerge(from, firstCut, mid, comp, swapper);
        inPlaceMerge(mid, secondCut, to, comp, swapper);
    }
    
    private static long lowerBound(long mid, final long to, final long firstCut, final LongComparator comp) {
        long len = to - mid;
        while (len > 0L) {
            final long half = len / 2L;
            final long middle = mid + half;
            if (comp.compare(middle, firstCut) < 0) {
                mid = middle + 1L;
                len -= half + 1L;
            }
            else {
                len = half;
            }
        }
        return mid;
    }
    
    private static long med3(final long a, final long b, final long c, final LongComparator comp) {
        final int ab = comp.compare(a, b);
        final int ac = comp.compare(a, c);
        final int bc = comp.compare(b, c);
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    public static void mergeSort(final long from, final long to, final LongComparator comp, final BigSwapper swapper) {
        final long length = to - from;
        if (length < 7L) {
            for (long i = from; i < to; ++i) {
                for (long j = i; j > from && comp.compare(j - 1L, j) > 0; --j) {
                    swapper.swap(j, j - 1L);
                }
            }
            return;
        }
        final long mid = from + to >>> 1;
        mergeSort(from, mid, comp, swapper);
        mergeSort(mid, to, comp, swapper);
        if (comp.compare(mid - 1L, mid) <= 0) {
            return;
        }
        inPlaceMerge(from, mid, to, comp, swapper);
    }
    
    public static void quickSort(final long from, final long to, final LongComparator comp, final BigSwapper swapper) {
        final long len = to - from;
        if (len < 7L) {
            for (long i = from; i < to; ++i) {
                for (long j = i; j > from && comp.compare(j - 1L, j) > 0; --j) {
                    swapper.swap(j, j - 1L);
                }
            }
            return;
        }
        long m = from + len / 2L;
        if (len > 7L) {
            long l = from;
            long n = to - 1L;
            if (len > 40L) {
                final long s = len / 8L;
                l = med3(l, l + s, l + 2L * s, comp);
                m = med3(m - s, m, m + s, comp);
                n = med3(n - 2L * s, n - s, n, comp);
            }
            m = med3(l, m, n, comp);
        }
        long b;
        long a = b = from;
        long d;
        long c = d = to - 1L;
        while (true) {
            int comparison;
            if (b <= c && (comparison = comp.compare(b, m)) <= 0) {
                if (comparison == 0) {
                    if (a == m) {
                        m = b;
                    }
                    else if (b == m) {
                        m = a;
                    }
                    swapper.swap(a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = comp.compare(c, m)) >= 0) {
                    if (comparison == 0) {
                        if (c == m) {
                            m = d;
                        }
                        else if (d == m) {
                            m = c;
                        }
                        swapper.swap(c, d--);
                    }
                    --c;
                }
                if (b > c) {
                    break;
                }
                if (b == m) {
                    m = d;
                }
                else if (c == m) {
                    m = c;
                }
                swapper.swap(b++, c--);
            }
        }
        final long n2 = from + len;
        long s2 = Math.min(a - from, b - a);
        vecSwap(swapper, from, b - s2, s2);
        s2 = Math.min(d - c, n2 - d - 1L);
        vecSwap(swapper, b, n2 - s2, s2);
        if ((s2 = b - a) > 1L) {
            quickSort(from, from + s2, comp, swapper);
        }
        if ((s2 = d - c) > 1L) {
            quickSort(n2 - s2, n2, comp, swapper);
        }
    }
    
    private static long upperBound(long from, final long mid, final long secondCut, final LongComparator comp) {
        long len = mid - from;
        while (len > 0L) {
            final long half = len / 2L;
            final long middle = from + half;
            if (comp.compare(secondCut, middle) < 0) {
                len = half;
            }
            else {
                from = middle + 1L;
                len -= half + 1L;
            }
        }
        return from;
    }
    
    private static void vecSwap(final BigSwapper swapper, long from, long l, final long s) {
        for (int i = 0; i < s; ++i, ++from, ++l) {
            swapper.swap(from, l);
        }
    }
    
    public static void main(final String[] arg) {
        final int[][] a = IntBigArrays.newBigArray(1L << Integer.parseInt(arg[0]));
        int k = 10;
        while (k-- != 0) {
            long start = -System.currentTimeMillis();
            long x = 0L;
            long i = IntBigArrays.length(a);
            while (i-- != 0L) {
                x ^= (i ^ (long)IntBigArrays.get(a, i));
            }
            if (x == 0L) {
                System.err.println();
            }
            System.out.println("Single loop: " + (start + System.currentTimeMillis()) + "ms");
            start = -System.currentTimeMillis();
            long y = 0L;
            int j = a.length;
            while (j-- != 0) {
                final int[] t = a[j];
                int d = t.length;
                while (d-- != 0) {
                    y ^= ((long)t[d] ^ index(j, d));
                }
            }
            if (y == 0L) {
                System.err.println();
            }
            if (x != y) {
                throw new AssertionError();
            }
            System.out.println("Double loop: " + (start + System.currentTimeMillis()) + "ms");
            final long z = 0L;
            long l = IntBigArrays.length(a);
            int m = a.length;
            while (m-- != 0) {
                final int[] t2 = a[m];
                int d2 = t2.length;
                while (d2-- != 0) {
                    y ^= ((long)t2[d2] ^ --l);
                }
            }
            if (z == 0L) {
                System.err.println();
            }
            if (x != z) {
                throw new AssertionError();
            }
            System.out.println("Double loop (with additional index): " + (start + System.currentTimeMillis()) + "ms");
        }
    }
}
