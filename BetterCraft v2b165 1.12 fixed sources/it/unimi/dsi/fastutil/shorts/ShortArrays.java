// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.io.Serializable;
import java.util.concurrent.RecursiveAction;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.LinkedBlockingQueue;
import it.unimi.dsi.fastutil.ints.IntArrays;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ForkJoinPool;
import it.unimi.dsi.fastutil.Arrays;
import it.unimi.dsi.fastutil.Hash;

public class ShortArrays
{
    public static final short[] EMPTY_ARRAY;
    private static final int QUICKSORT_NO_REC = 16;
    private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
    private static final int QUICKSORT_MEDIAN_OF_9 = 128;
    private static final int MERGESORT_NO_REC = 16;
    private static final int DIGIT_BITS = 8;
    private static final int DIGIT_MASK = 255;
    private static final int DIGITS_PER_ELEMENT = 2;
    private static final int RADIXSORT_NO_REC = 1024;
    private static final int PARALLEL_RADIXSORT_NO_FORK = 1024;
    protected static final Segment POISON_PILL;
    public static final Hash.Strategy<short[]> HASH_STRATEGY;
    
    private ShortArrays() {
    }
    
    public static short[] ensureCapacity(final short[] array, final int length) {
        if (length > array.length) {
            final short[] t = new short[length];
            System.arraycopy(array, 0, t, 0, array.length);
            return t;
        }
        return array;
    }
    
    public static short[] ensureCapacity(final short[] array, final int length, final int preserve) {
        if (length > array.length) {
            final short[] t = new short[length];
            System.arraycopy(array, 0, t, 0, preserve);
            return t;
        }
        return array;
    }
    
    public static short[] grow(final short[] array, final int length) {
        if (length > array.length) {
            final int newLength = (int)Math.max(Math.min(2L * array.length, 2147483639L), length);
            final short[] t = new short[newLength];
            System.arraycopy(array, 0, t, 0, array.length);
            return t;
        }
        return array;
    }
    
    public static short[] grow(final short[] array, final int length, final int preserve) {
        if (length > array.length) {
            final int newLength = (int)Math.max(Math.min(2L * array.length, 2147483639L), length);
            final short[] t = new short[newLength];
            System.arraycopy(array, 0, t, 0, preserve);
            return t;
        }
        return array;
    }
    
    public static short[] trim(final short[] array, final int length) {
        if (length >= array.length) {
            return array;
        }
        final short[] t = (length == 0) ? ShortArrays.EMPTY_ARRAY : new short[length];
        System.arraycopy(array, 0, t, 0, length);
        return t;
    }
    
    public static short[] setLength(final short[] array, final int length) {
        if (length == array.length) {
            return array;
        }
        if (length < array.length) {
            return trim(array, length);
        }
        return ensureCapacity(array, length);
    }
    
    public static short[] copy(final short[] array, final int offset, final int length) {
        ensureOffsetLength(array, offset, length);
        final short[] a = (length == 0) ? ShortArrays.EMPTY_ARRAY : new short[length];
        System.arraycopy(array, offset, a, 0, length);
        return a;
    }
    
    public static short[] copy(final short[] array) {
        return array.clone();
    }
    
    @Deprecated
    public static void fill(final short[] array, final short value) {
        int i = array.length;
        while (i-- != 0) {
            array[i] = value;
        }
    }
    
    @Deprecated
    public static void fill(final short[] array, final int from, int to, final short value) {
        ensureFromTo(array, from, to);
        if (from == 0) {
            while (to-- != 0) {
                array[to] = value;
            }
        }
        else {
            for (int i = from; i < to; ++i) {
                array[i] = value;
            }
        }
    }
    
    @Deprecated
    public static boolean equals(final short[] a1, final short[] a2) {
        int i = a1.length;
        if (i != a2.length) {
            return false;
        }
        while (i-- != 0) {
            if (a1[i] != a2[i]) {
                return false;
            }
        }
        return true;
    }
    
    public static void ensureFromTo(final short[] a, final int from, final int to) {
        Arrays.ensureFromTo(a.length, from, to);
    }
    
    public static void ensureOffsetLength(final short[] a, final int offset, final int length) {
        Arrays.ensureOffsetLength(a.length, offset, length);
    }
    
    public static void ensureSameLength(final short[] a, final short[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Array size mismatch: " + a.length + " != " + b.length);
        }
    }
    
    public static void swap(final short[] x, final int a, final int b) {
        final short t = x[a];
        x[a] = x[b];
        x[b] = t;
    }
    
    public static void swap(final short[] x, int a, int b, final int n) {
        for (int i = 0; i < n; ++i, ++a, ++b) {
            swap(x, a, b);
        }
    }
    
    private static int med3(final short[] x, final int a, final int b, final int c, final ShortComparator comp) {
        final int ab = comp.compare(x[a], x[b]);
        final int ac = comp.compare(x[a], x[c]);
        final int bc = comp.compare(x[b], x[c]);
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static void selectionSort(final short[] a, final int from, final int to, final ShortComparator comp) {
        for (int i = from; i < to - 1; ++i) {
            int m = i;
            for (int j = i + 1; j < to; ++j) {
                if (comp.compare(a[j], a[m]) < 0) {
                    m = j;
                }
            }
            if (m != i) {
                final short u = a[i];
                a[i] = a[m];
                a[m] = u;
            }
        }
    }
    
    private static void insertionSort(final short[] a, final int from, final int to, final ShortComparator comp) {
        int i = from;
        while (++i < to) {
            final short t = a[i];
            int j = i;
            for (short u = a[j - 1]; comp.compare(t, u) < 0; u = a[--j - 1]) {
                a[j] = u;
                if (from == j - 1) {
                    --j;
                    break;
                }
            }
            a[j] = t;
        }
    }
    
    public static void quickSort(final short[] x, final int from, final int to, final ShortComparator comp) {
        final int len = to - from;
        if (len < 16) {
            selectionSort(x, from, to, comp);
            return;
        }
        int m = from + len / 2;
        int l = from;
        int n = to - 1;
        if (len > 128) {
            final int s = len / 8;
            l = med3(x, l, l + s, l + 2 * s, comp);
            m = med3(x, m - s, m, m + s, comp);
            n = med3(x, n - 2 * s, n - s, n, comp);
        }
        m = med3(x, l, m, n, comp);
        final short v = x[m];
        int b;
        int a = b = from;
        int d;
        int c = d = to - 1;
        while (true) {
            int comparison;
            if (b <= c && (comparison = comp.compare(x[b], v)) <= 0) {
                if (comparison == 0) {
                    swap(x, a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = comp.compare(x[c], v)) >= 0) {
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
        int s2 = Math.min(a - from, b - a);
        swap(x, from, b - s2, s2);
        s2 = Math.min(d - c, to - d - 1);
        swap(x, b, to - s2, s2);
        if ((s2 = b - a) > 1) {
            quickSort(x, from, from + s2, comp);
        }
        if ((s2 = d - c) > 1) {
            quickSort(x, to - s2, to, comp);
        }
    }
    
    public static void quickSort(final short[] x, final ShortComparator comp) {
        quickSort(x, 0, x.length, comp);
    }
    
    public static void parallelQuickSort(final short[] x, final int from, final int to, final ShortComparator comp) {
        if (to - from < 8192) {
            quickSort(x, from, to, comp);
        }
        else {
            final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
            pool.invoke((ForkJoinTask<Object>)new ForkJoinQuickSortComp(x, from, to, comp));
            pool.shutdown();
        }
    }
    
    public static void parallelQuickSort(final short[] x, final ShortComparator comp) {
        parallelQuickSort(x, 0, x.length, comp);
    }
    
    private static int med3(final short[] x, final int a, final int b, final int c) {
        final int ab = Short.compare(x[a], x[b]);
        final int ac = Short.compare(x[a], x[c]);
        final int bc = Short.compare(x[b], x[c]);
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static void selectionSort(final short[] a, final int from, final int to) {
        for (int i = from; i < to - 1; ++i) {
            int m = i;
            for (int j = i + 1; j < to; ++j) {
                if (a[j] < a[m]) {
                    m = j;
                }
            }
            if (m != i) {
                final short u = a[i];
                a[i] = a[m];
                a[m] = u;
            }
        }
    }
    
    private static void insertionSort(final short[] a, final int from, final int to) {
        int i = from;
        while (++i < to) {
            final short t = a[i];
            int j = i;
            for (short u = a[j - 1]; t < u; u = a[--j - 1]) {
                a[j] = u;
                if (from == j - 1) {
                    --j;
                    break;
                }
            }
            a[j] = t;
        }
    }
    
    public static void quickSort(final short[] x, final int from, final int to) {
        final int len = to - from;
        if (len < 16) {
            selectionSort(x, from, to);
            return;
        }
        int m = from + len / 2;
        int l = from;
        int n = to - 1;
        if (len > 128) {
            final int s = len / 8;
            l = med3(x, l, l + s, l + 2 * s);
            m = med3(x, m - s, m, m + s);
            n = med3(x, n - 2 * s, n - s, n);
        }
        m = med3(x, l, m, n);
        final short v = x[m];
        int b;
        int a = b = from;
        int d;
        int c = d = to - 1;
        while (true) {
            int comparison;
            if (b <= c && (comparison = Short.compare(x[b], v)) <= 0) {
                if (comparison == 0) {
                    swap(x, a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = Short.compare(x[c], v)) >= 0) {
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
        int s2 = Math.min(a - from, b - a);
        swap(x, from, b - s2, s2);
        s2 = Math.min(d - c, to - d - 1);
        swap(x, b, to - s2, s2);
        if ((s2 = b - a) > 1) {
            quickSort(x, from, from + s2);
        }
        if ((s2 = d - c) > 1) {
            quickSort(x, to - s2, to);
        }
    }
    
    public static void quickSort(final short[] x) {
        quickSort(x, 0, x.length);
    }
    
    public static void parallelQuickSort(final short[] x, final int from, final int to) {
        if (to - from < 8192) {
            quickSort(x, from, to);
        }
        else {
            final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
            pool.invoke((ForkJoinTask<Object>)new ForkJoinQuickSort(x, from, to));
            pool.shutdown();
        }
    }
    
    public static void parallelQuickSort(final short[] x) {
        parallelQuickSort(x, 0, x.length);
    }
    
    private static int med3Indirect(final int[] perm, final short[] x, final int a, final int b, final int c) {
        final short aa = x[perm[a]];
        final short bb = x[perm[b]];
        final short cc = x[perm[c]];
        final int ab = Short.compare(aa, bb);
        final int ac = Short.compare(aa, cc);
        final int bc = Short.compare(bb, cc);
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static void insertionSortIndirect(final int[] perm, final short[] a, final int from, final int to) {
        int i = from;
        while (++i < to) {
            final int t = perm[i];
            int j = i;
            for (int u = perm[j - 1]; a[t] < a[u]; u = perm[--j - 1]) {
                perm[j] = u;
                if (from == j - 1) {
                    --j;
                    break;
                }
            }
            perm[j] = t;
        }
    }
    
    public static void quickSortIndirect(final int[] perm, final short[] x, final int from, final int to) {
        final int len = to - from;
        if (len < 16) {
            insertionSortIndirect(perm, x, from, to);
            return;
        }
        int m = from + len / 2;
        int l = from;
        int n = to - 1;
        if (len > 128) {
            final int s = len / 8;
            l = med3Indirect(perm, x, l, l + s, l + 2 * s);
            m = med3Indirect(perm, x, m - s, m, m + s);
            n = med3Indirect(perm, x, n - 2 * s, n - s, n);
        }
        m = med3Indirect(perm, x, l, m, n);
        final short v = x[perm[m]];
        int b;
        int a = b = from;
        int d;
        int c = d = to - 1;
        while (true) {
            int comparison;
            if (b <= c && (comparison = Short.compare(x[perm[b]], v)) <= 0) {
                if (comparison == 0) {
                    IntArrays.swap(perm, a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = Short.compare(x[perm[c]], v)) >= 0) {
                    if (comparison == 0) {
                        IntArrays.swap(perm, c, d--);
                    }
                    --c;
                }
                if (b > c) {
                    break;
                }
                IntArrays.swap(perm, b++, c--);
            }
        }
        int s2 = Math.min(a - from, b - a);
        IntArrays.swap(perm, from, b - s2, s2);
        s2 = Math.min(d - c, to - d - 1);
        IntArrays.swap(perm, b, to - s2, s2);
        if ((s2 = b - a) > 1) {
            quickSortIndirect(perm, x, from, from + s2);
        }
        if ((s2 = d - c) > 1) {
            quickSortIndirect(perm, x, to - s2, to);
        }
    }
    
    public static void quickSortIndirect(final int[] perm, final short[] x) {
        quickSortIndirect(perm, x, 0, x.length);
    }
    
    public static void parallelQuickSortIndirect(final int[] perm, final short[] x, final int from, final int to) {
        if (to - from < 8192) {
            quickSortIndirect(perm, x, from, to);
        }
        else {
            final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
            pool.invoke((ForkJoinTask<Object>)new ForkJoinQuickSortIndirect(perm, x, from, to));
            pool.shutdown();
        }
    }
    
    public static void parallelQuickSortIndirect(final int[] perm, final short[] x) {
        parallelQuickSortIndirect(perm, x, 0, x.length);
    }
    
    public static void stabilize(final int[] perm, final short[] x, final int from, final int to) {
        int curr = from;
        for (int i = from + 1; i < to; ++i) {
            if (x[perm[i]] != x[perm[curr]]) {
                if (i - curr > 1) {
                    IntArrays.parallelQuickSort(perm, curr, i);
                }
                curr = i;
            }
        }
        if (to - curr > 1) {
            IntArrays.parallelQuickSort(perm, curr, to);
        }
    }
    
    public static void stabilize(final int[] perm, final short[] x) {
        stabilize(perm, x, 0, perm.length);
    }
    
    private static int med3(final short[] x, final short[] y, final int a, final int b, final int c) {
        int t;
        final int ab = ((t = Short.compare(x[a], x[b])) == 0) ? Short.compare(y[a], y[b]) : t;
        final int ac = ((t = Short.compare(x[a], x[c])) == 0) ? Short.compare(y[a], y[c]) : t;
        final int bc = ((t = Short.compare(x[b], x[c])) == 0) ? Short.compare(y[b], y[c]) : t;
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static void swap(final short[] x, final short[] y, final int a, final int b) {
        final short t = x[a];
        final short u = y[a];
        x[a] = x[b];
        y[a] = y[b];
        x[b] = t;
        y[b] = u;
    }
    
    private static void swap(final short[] x, final short[] y, int a, int b, final int n) {
        for (int i = 0; i < n; ++i, ++a, ++b) {
            swap(x, y, a, b);
        }
    }
    
    private static void selectionSort(final short[] a, final short[] b, final int from, final int to) {
        for (int i = from; i < to - 1; ++i) {
            int m = i;
            for (int j = i + 1; j < to; ++j) {
                final int u;
                if ((u = Short.compare(a[j], a[m])) < 0 || (u == 0 && b[j] < b[m])) {
                    m = j;
                }
            }
            if (m != i) {
                short t = a[i];
                a[i] = a[m];
                a[m] = t;
                t = b[i];
                b[i] = b[m];
                b[m] = t;
            }
        }
    }
    
    public static void quickSort(final short[] x, final short[] y, final int from, final int to) {
        final int len = to - from;
        if (len < 16) {
            selectionSort(x, y, from, to);
            return;
        }
        int m = from + len / 2;
        int l = from;
        int n = to - 1;
        if (len > 128) {
            final int s = len / 8;
            l = med3(x, y, l, l + s, l + 2 * s);
            m = med3(x, y, m - s, m, m + s);
            n = med3(x, y, n - 2 * s, n - s, n);
        }
        m = med3(x, y, l, m, n);
        final short v = x[m];
        final short w = y[m];
        int b;
        int a = b = from;
        int d;
        int c = d = to - 1;
        while (true) {
            int t;
            int comparison;
            if (b <= c && (comparison = (((t = Short.compare(x[b], v)) == 0) ? Short.compare(y[b], w) : t)) <= 0) {
                if (comparison == 0) {
                    swap(x, y, a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = (((t = Short.compare(x[c], v)) == 0) ? Short.compare(y[c], w) : t)) >= 0) {
                    if (comparison == 0) {
                        swap(x, y, c, d--);
                    }
                    --c;
                }
                if (b > c) {
                    break;
                }
                swap(x, y, b++, c--);
            }
        }
        int s2 = Math.min(a - from, b - a);
        swap(x, y, from, b - s2, s2);
        s2 = Math.min(d - c, to - d - 1);
        swap(x, y, b, to - s2, s2);
        if ((s2 = b - a) > 1) {
            quickSort(x, y, from, from + s2);
        }
        if ((s2 = d - c) > 1) {
            quickSort(x, y, to - s2, to);
        }
    }
    
    public static void quickSort(final short[] x, final short[] y) {
        ensureSameLength(x, y);
        quickSort(x, y, 0, x.length);
    }
    
    public static void parallelQuickSort(final short[] x, final short[] y, final int from, final int to) {
        if (to - from < 8192) {
            quickSort(x, y, from, to);
        }
        final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        pool.invoke((ForkJoinTask<Object>)new ForkJoinQuickSort2(x, y, from, to));
        pool.shutdown();
    }
    
    public static void parallelQuickSort(final short[] x, final short[] y) {
        ensureSameLength(x, y);
        parallelQuickSort(x, y, 0, x.length);
    }
    
    public static void mergeSort(final short[] a, final int from, final int to, final short[] supp) {
        final int len = to - from;
        if (len < 16) {
            insertionSort(a, from, to);
            return;
        }
        final int mid = from + to >>> 1;
        mergeSort(supp, from, mid, a);
        mergeSort(supp, mid, to, a);
        if (supp[mid - 1] <= supp[mid]) {
            System.arraycopy(supp, from, a, from, len);
            return;
        }
        int i = from;
        int p = from;
        int q = mid;
        while (i < to) {
            if (q >= to || (p < mid && supp[p] <= supp[q])) {
                a[i] = supp[p++];
            }
            else {
                a[i] = supp[q++];
            }
            ++i;
        }
    }
    
    public static void mergeSort(final short[] a, final int from, final int to) {
        mergeSort(a, from, to, a.clone());
    }
    
    public static void mergeSort(final short[] a) {
        mergeSort(a, 0, a.length);
    }
    
    public static void mergeSort(final short[] a, final int from, final int to, final ShortComparator comp, final short[] supp) {
        final int len = to - from;
        if (len < 16) {
            insertionSort(a, from, to, comp);
            return;
        }
        final int mid = from + to >>> 1;
        mergeSort(supp, from, mid, comp, a);
        mergeSort(supp, mid, to, comp, a);
        if (comp.compare(supp[mid - 1], supp[mid]) <= 0) {
            System.arraycopy(supp, from, a, from, len);
            return;
        }
        int i = from;
        int p = from;
        int q = mid;
        while (i < to) {
            if (q >= to || (p < mid && comp.compare(supp[p], supp[q]) <= 0)) {
                a[i] = supp[p++];
            }
            else {
                a[i] = supp[q++];
            }
            ++i;
        }
    }
    
    public static void mergeSort(final short[] a, final int from, final int to, final ShortComparator comp) {
        mergeSort(a, from, to, comp, a.clone());
    }
    
    public static void mergeSort(final short[] a, final ShortComparator comp) {
        mergeSort(a, 0, a.length, comp);
    }
    
    public static int binarySearch(final short[] a, int from, int to, final short key) {
        --to;
        while (from <= to) {
            final int mid = from + to >>> 1;
            final short midVal = a[mid];
            if (midVal < key) {
                from = mid + 1;
            }
            else {
                if (midVal <= key) {
                    return mid;
                }
                to = mid - 1;
            }
        }
        return -(from + 1);
    }
    
    public static int binarySearch(final short[] a, final short key) {
        return binarySearch(a, 0, a.length, key);
    }
    
    public static int binarySearch(final short[] a, int from, int to, final short key, final ShortComparator c) {
        --to;
        while (from <= to) {
            final int mid = from + to >>> 1;
            final short midVal = a[mid];
            final int cmp = c.compare(midVal, key);
            if (cmp < 0) {
                from = mid + 1;
            }
            else {
                if (cmp <= 0) {
                    return mid;
                }
                to = mid - 1;
            }
        }
        return -(from + 1);
    }
    
    public static int binarySearch(final short[] a, final short key, final ShortComparator c) {
        return binarySearch(a, 0, a.length, key, c);
    }
    
    public static void radixSort(final short[] a) {
        radixSort(a, 0, a.length);
    }
    
    public static void radixSort(final short[] a, final int from, final int to) {
        if (to - from < 1024) {
            quickSort(a, from, to);
            return;
        }
        final int maxLevel = 1;
        final int stackSize = 256;
        int stackPos = 0;
        final int[] offsetStack = new int[256];
        final int[] lengthStack = new int[256];
        final int[] levelStack = new int[256];
        lengthStack[stackPos] = to - (offsetStack[stackPos] = from);
        levelStack[stackPos++] = 0;
        final int[] count = new int[256];
        final int[] pos = new int[256];
        while (stackPos > 0) {
            final int first = offsetStack[--stackPos];
            final int length = lengthStack[stackPos];
            final int level = levelStack[stackPos];
            final int signMask = (level % 2 == 0) ? 128 : 0;
            final int shift = (1 - level % 2) * 8;
            int i = first + length;
            while (i-- != first) {
                final int[] array = count;
                final int n = (a[i] >>> shift & 0xFF) ^ signMask;
                ++array[n];
            }
            int lastUsed = -1;
            int j = 0;
            int p = first;
            while (j < 256) {
                if (count[j] != 0) {
                    lastUsed = j;
                }
                p = (pos[j] = p + count[j]);
                ++j;
            }
            for (int end = first + length - count[lastUsed], k = first, c = -1; k <= end; k += count[c], count[c] = 0) {
                short t = a[k];
                c = ((t >>> shift & 0xFF) ^ signMask);
                if (k < end) {
                    while (true) {
                        final int[] array2 = pos;
                        final int n2 = c;
                        final int n3 = array2[n2] - 1;
                        array2[n2] = n3;
                        final int d;
                        if ((d = n3) <= k) {
                            break;
                        }
                        final short z = t;
                        t = a[d];
                        a[d] = z;
                        c = ((t >>> shift & 0xFF) ^ signMask);
                    }
                    a[k] = t;
                }
                if (level < 1 && count[c] > 1) {
                    if (count[c] < 1024) {
                        quickSort(a, k, k + count[c]);
                    }
                    else {
                        offsetStack[stackPos] = k;
                        lengthStack[stackPos] = count[c];
                        levelStack[stackPos++] = level + 1;
                    }
                }
            }
        }
    }
    
    public static void parallelRadixSort(final short[] a, final int from, final int to) {
        if (to - from < 1024) {
            quickSort(a, from, to);
            return;
        }
        final int maxLevel = 1;
        final LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<Segment>();
        queue.add(new Segment(from, to - from, 0));
        final AtomicInteger queueSize = new AtomicInteger(1);
        final int numberOfThreads = Runtime.getRuntime().availableProcessors();
        final ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads, Executors.defaultThreadFactory());
        final ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<Void>(executorService);
        int i = numberOfThreads;
        while (i-- != 0) {
            executorCompletionService.submit(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    final int[] count = new int[256];
                    final int[] pos = new int[256];
                    while (true) {
                        if (queueSize.get() == 0) {
                            int i = numberOfThreads;
                            while (i-- != 0) {
                                queue.add(ShortArrays.POISON_PILL);
                            }
                        }
                        final Segment segment = queue.take();
                        if (segment == ShortArrays.POISON_PILL) {
                            break;
                        }
                        final int first = segment.offset;
                        final int length = segment.length;
                        final int level = segment.level;
                        final int signMask = (level % 2 == 0) ? 128 : 0;
                        final int shift = (1 - level % 2) * 8;
                        int j = first + length;
                        while (j-- != first) {
                            final int[] array = count;
                            final int n = (a[j] >>> shift & 0xFF) ^ signMask;
                            ++array[n];
                        }
                        int lastUsed = -1;
                        int k = 0;
                        int p = first;
                        while (k < 256) {
                            if (count[k] != 0) {
                                lastUsed = k;
                            }
                            p = (pos[k] = p + count[k]);
                            ++k;
                        }
                        for (int end = first + length - count[lastUsed], l = first, c = -1; l <= end; l += count[c], count[c] = 0) {
                            short t = a[l];
                            c = ((t >>> shift & 0xFF) ^ signMask);
                            if (l < end) {
                                while (true) {
                                    final int[] array2 = pos;
                                    final int n2 = c;
                                    final int n3 = array2[n2] - 1;
                                    array2[n2] = n3;
                                    final int d;
                                    if ((d = n3) <= l) {
                                        break;
                                    }
                                    final short z = t;
                                    t = a[d];
                                    a[d] = z;
                                    c = ((t >>> shift & 0xFF) ^ signMask);
                                }
                                a[l] = t;
                            }
                            if (level < 1 && count[c] > 1) {
                                if (count[c] < 1024) {
                                    ShortArrays.quickSort(a, l, l + count[c]);
                                }
                                else {
                                    queueSize.incrementAndGet();
                                    queue.add(new Segment(l, count[c], level + 1));
                                }
                            }
                        }
                        queueSize.decrementAndGet();
                    }
                    return null;
                }
            });
        }
        Throwable problem = null;
        int j = numberOfThreads;
        while (j-- != 0) {
            try {
                executorCompletionService.take().get();
            }
            catch (final Exception e) {
                problem = e.getCause();
            }
        }
        executorService.shutdown();
        if (problem != null) {
            throw (problem instanceof RuntimeException) ? problem : new RuntimeException(problem);
        }
    }
    
    public static void parallelRadixSort(final short[] a) {
        parallelRadixSort(a, 0, a.length);
    }
    
    public static void radixSortIndirect(final int[] perm, final short[] a, final boolean stable) {
        radixSortIndirect(perm, a, 0, perm.length, stable);
    }
    
    public static void radixSortIndirect(final int[] perm, final short[] a, final int from, final int to, final boolean stable) {
        if (to - from < 1024) {
            insertionSortIndirect(perm, a, from, to);
            return;
        }
        final int maxLevel = 1;
        final int stackSize = 256;
        int stackPos = 0;
        final int[] offsetStack = new int[256];
        final int[] lengthStack = new int[256];
        final int[] levelStack = new int[256];
        lengthStack[stackPos] = to - (offsetStack[stackPos] = from);
        levelStack[stackPos++] = 0;
        final int[] count = new int[256];
        final int[] pos = new int[256];
        final int[] support = (int[])(stable ? new int[perm.length] : null);
        while (stackPos > 0) {
            final int first = offsetStack[--stackPos];
            final int length = lengthStack[stackPos];
            final int level = levelStack[stackPos];
            final int signMask = (level % 2 == 0) ? 128 : 0;
            final int shift = (1 - level % 2) * 8;
            int i = first + length;
            while (i-- != first) {
                final int[] array = count;
                final int n = (a[perm[i]] >>> shift & 0xFF) ^ signMask;
                ++array[n];
            }
            int lastUsed = -1;
            int j = 0;
            int p = stable ? 0 : first;
            while (j < 256) {
                if (count[j] != 0) {
                    lastUsed = j;
                }
                p = (pos[j] = p + count[j]);
                ++j;
            }
            if (stable) {
                j = first + length;
                while (j-- != first) {
                    final int[] array2 = support;
                    final int[] array3 = pos;
                    final int n2 = (a[perm[j]] >>> shift & 0xFF) ^ signMask;
                    array2[--array3[n2]] = perm[j];
                }
                System.arraycopy(support, 0, perm, first, length);
                j = 0;
                p = first;
                while (j <= lastUsed) {
                    if (level < 1 && count[j] > 1) {
                        if (count[j] < 1024) {
                            insertionSortIndirect(perm, a, p, p + count[j]);
                        }
                        else {
                            offsetStack[stackPos] = p;
                            lengthStack[stackPos] = count[j];
                            levelStack[stackPos++] = level + 1;
                        }
                    }
                    p += count[j];
                    ++j;
                }
                java.util.Arrays.fill(count, 0);
            }
            else {
                for (int end = first + length - count[lastUsed], k = first, c = -1; k <= end; k += count[c], count[c] = 0) {
                    int t = perm[k];
                    c = ((a[t] >>> shift & 0xFF) ^ signMask);
                    if (k < end) {
                        while (true) {
                            final int[] array4 = pos;
                            final int n3 = c;
                            final int n4 = array4[n3] - 1;
                            array4[n3] = n4;
                            final int d;
                            if ((d = n4) <= k) {
                                break;
                            }
                            final int z = t;
                            t = perm[d];
                            perm[d] = z;
                            c = ((a[t] >>> shift & 0xFF) ^ signMask);
                        }
                        perm[k] = t;
                    }
                    if (level < 1 && count[c] > 1) {
                        if (count[c] < 1024) {
                            insertionSortIndirect(perm, a, k, k + count[c]);
                        }
                        else {
                            offsetStack[stackPos] = k;
                            lengthStack[stackPos] = count[c];
                            levelStack[stackPos++] = level + 1;
                        }
                    }
                }
            }
        }
    }
    
    public static void parallelRadixSortIndirect(final int[] perm, final short[] a, final int from, final int to, final boolean stable) {
        if (to - from < 1024) {
            radixSortIndirect(perm, a, from, to, stable);
            return;
        }
        final int maxLevel = 1;
        final LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<Segment>();
        queue.add(new Segment(from, to - from, 0));
        final AtomicInteger queueSize = new AtomicInteger(1);
        final int numberOfThreads = Runtime.getRuntime().availableProcessors();
        final ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads, Executors.defaultThreadFactory());
        final ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<Void>(executorService);
        final int[] support = (int[])(stable ? new int[perm.length] : null);
        int i = numberOfThreads;
        while (i-- != 0) {
            executorCompletionService.submit(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    final int[] count = new int[256];
                    final int[] pos = new int[256];
                    while (true) {
                        if (queueSize.get() == 0) {
                            int i = numberOfThreads;
                            while (i-- != 0) {
                                queue.add(ShortArrays.POISON_PILL);
                            }
                        }
                        final Segment segment = queue.take();
                        if (segment == ShortArrays.POISON_PILL) {
                            break;
                        }
                        final int first = segment.offset;
                        final int length = segment.length;
                        final int level = segment.level;
                        final int signMask = (level % 2 == 0) ? 128 : 0;
                        final int shift = (1 - level % 2) * 8;
                        int j = first + length;
                        while (j-- != first) {
                            final int[] array = count;
                            final int n = (a[perm[j]] >>> shift & 0xFF) ^ signMask;
                            ++array[n];
                        }
                        int lastUsed = -1;
                        int k = 0;
                        int p = first;
                        while (k < 256) {
                            if (count[k] != 0) {
                                lastUsed = k;
                            }
                            p = (pos[k] = p + count[k]);
                            ++k;
                        }
                        if (stable) {
                            k = first + length;
                            while (k-- != first) {
                                final int[] val$support = support;
                                final int[] array2 = pos;
                                final int n2 = (a[perm[k]] >>> shift & 0xFF) ^ signMask;
                                val$support[--array2[n2]] = perm[k];
                            }
                            System.arraycopy(support, first, perm, first, length);
                            k = 0;
                            p = first;
                            while (k <= lastUsed) {
                                if (level < 1 && count[k] > 1) {
                                    if (count[k] < 1024) {
                                        ShortArrays.radixSortIndirect(perm, a, p, p + count[k], stable);
                                    }
                                    else {
                                        queueSize.incrementAndGet();
                                        queue.add(new Segment(p, count[k], level + 1));
                                    }
                                }
                                p += count[k];
                                ++k;
                            }
                            java.util.Arrays.fill(count, 0);
                        }
                        else {
                            for (int end = first + length - count[lastUsed], l = first, c = -1; l <= end; l += count[c], count[c] = 0) {
                                int t = perm[l];
                                c = ((a[t] >>> shift & 0xFF) ^ signMask);
                                if (l < end) {
                                    while (true) {
                                        final int[] array3 = pos;
                                        final int n3 = c;
                                        final int n4 = array3[n3] - 1;
                                        array3[n3] = n4;
                                        final int d;
                                        if ((d = n4) <= l) {
                                            break;
                                        }
                                        final int z = t;
                                        t = perm[d];
                                        perm[d] = z;
                                        c = ((a[t] >>> shift & 0xFF) ^ signMask);
                                    }
                                    perm[l] = t;
                                }
                                if (level < 1 && count[c] > 1) {
                                    if (count[c] < 1024) {
                                        ShortArrays.radixSortIndirect(perm, a, l, l + count[c], stable);
                                    }
                                    else {
                                        queueSize.incrementAndGet();
                                        queue.add(new Segment(l, count[c], level + 1));
                                    }
                                }
                            }
                        }
                        queueSize.decrementAndGet();
                    }
                    return null;
                }
            });
        }
        Throwable problem = null;
        int j = numberOfThreads;
        while (j-- != 0) {
            try {
                executorCompletionService.take().get();
            }
            catch (final Exception e) {
                problem = e.getCause();
            }
        }
        executorService.shutdown();
        if (problem != null) {
            throw (problem instanceof RuntimeException) ? problem : new RuntimeException(problem);
        }
    }
    
    public static void parallelRadixSortIndirect(final int[] perm, final short[] a, final boolean stable) {
        parallelRadixSortIndirect(perm, a, 0, a.length, stable);
    }
    
    public static void radixSort(final short[] a, final short[] b) {
        ensureSameLength(a, b);
        radixSort(a, b, 0, a.length);
    }
    
    public static void radixSort(final short[] a, final short[] b, final int from, final int to) {
        if (to - from < 1024) {
            selectionSort(a, b, from, to);
            return;
        }
        final int layers = 2;
        final int maxLevel = 3;
        final int stackSize = 766;
        int stackPos = 0;
        final int[] offsetStack = new int[766];
        final int[] lengthStack = new int[766];
        final int[] levelStack = new int[766];
        lengthStack[stackPos] = to - (offsetStack[stackPos] = from);
        levelStack[stackPos++] = 0;
        final int[] count = new int[256];
        final int[] pos = new int[256];
        while (stackPos > 0) {
            final int first = offsetStack[--stackPos];
            final int length = lengthStack[stackPos];
            final int level = levelStack[stackPos];
            final int signMask = (level % 2 == 0) ? 128 : 0;
            final short[] k = (level < 2) ? a : b;
            final int shift = (1 - level % 2) * 8;
            int i = first + length;
            while (i-- != first) {
                final int[] array = count;
                final int n = (k[i] >>> shift & 0xFF) ^ signMask;
                ++array[n];
            }
            int lastUsed = -1;
            int j = 0;
            int p = first;
            while (j < 256) {
                if (count[j] != 0) {
                    lastUsed = j;
                }
                p = (pos[j] = p + count[j]);
                ++j;
            }
            for (int end = first + length - count[lastUsed], l = first, c = -1; l <= end; l += count[c], count[c] = 0) {
                short t = a[l];
                short u = b[l];
                c = ((k[l] >>> shift & 0xFF) ^ signMask);
                if (l < end) {
                    while (true) {
                        final int[] array2 = pos;
                        final int n2 = c;
                        final int n3 = array2[n2] - 1;
                        array2[n2] = n3;
                        final int d;
                        if ((d = n3) <= l) {
                            break;
                        }
                        c = ((k[d] >>> shift & 0xFF) ^ signMask);
                        short z = t;
                        t = a[d];
                        a[d] = z;
                        z = u;
                        u = b[d];
                        b[d] = z;
                    }
                    a[l] = t;
                    b[l] = u;
                }
                if (level < 3 && count[c] > 1) {
                    if (count[c] < 1024) {
                        selectionSort(a, b, l, l + count[c]);
                    }
                    else {
                        offsetStack[stackPos] = l;
                        lengthStack[stackPos] = count[c];
                        levelStack[stackPos++] = level + 1;
                    }
                }
            }
        }
    }
    
    public static void parallelRadixSort(final short[] a, final short[] b, final int from, final int to) {
        if (to - from < 1024) {
            quickSort(a, b, from, to);
            return;
        }
        final int layers = 2;
        if (a.length != b.length) {
            throw new IllegalArgumentException("Array size mismatch.");
        }
        final int maxLevel = 3;
        final LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<Segment>();
        queue.add(new Segment(from, to - from, 0));
        final AtomicInteger queueSize = new AtomicInteger(1);
        final int numberOfThreads = Runtime.getRuntime().availableProcessors();
        final ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads, Executors.defaultThreadFactory());
        final ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<Void>(executorService);
        int i = numberOfThreads;
        while (i-- != 0) {
            executorCompletionService.submit(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    final int[] count = new int[256];
                    final int[] pos = new int[256];
                    while (true) {
                        if (queueSize.get() == 0) {
                            int i = numberOfThreads;
                            while (i-- != 0) {
                                queue.add(ShortArrays.POISON_PILL);
                            }
                        }
                        final Segment segment = queue.take();
                        if (segment == ShortArrays.POISON_PILL) {
                            break;
                        }
                        final int first = segment.offset;
                        final int length = segment.length;
                        final int level = segment.level;
                        final int signMask = (level % 2 == 0) ? 128 : 0;
                        final short[] k = (level < 2) ? a : b;
                        final int shift = (1 - level % 2) * 8;
                        int j = first + length;
                        while (j-- != first) {
                            final int[] array = count;
                            final int n = (k[j] >>> shift & 0xFF) ^ signMask;
                            ++array[n];
                        }
                        int lastUsed = -1;
                        int l = 0;
                        int p = first;
                        while (l < 256) {
                            if (count[l] != 0) {
                                lastUsed = l;
                            }
                            p = (pos[l] = p + count[l]);
                            ++l;
                        }
                        for (int end = first + length - count[lastUsed], m = first, c = -1; m <= end; m += count[c], count[c] = 0) {
                            short t = a[m];
                            short u = b[m];
                            c = ((k[m] >>> shift & 0xFF) ^ signMask);
                            if (m < end) {
                                while (true) {
                                    final int[] array2 = pos;
                                    final int n2 = c;
                                    final int n3 = array2[n2] - 1;
                                    array2[n2] = n3;
                                    final int d;
                                    if ((d = n3) <= m) {
                                        break;
                                    }
                                    c = ((k[d] >>> shift & 0xFF) ^ signMask);
                                    final short z = t;
                                    final short w = u;
                                    t = a[d];
                                    u = b[d];
                                    a[d] = z;
                                    b[d] = w;
                                }
                                a[m] = t;
                                b[m] = u;
                            }
                            if (level < 3 && count[c] > 1) {
                                if (count[c] < 1024) {
                                    ShortArrays.quickSort(a, b, m, m + count[c]);
                                }
                                else {
                                    queueSize.incrementAndGet();
                                    queue.add(new Segment(m, count[c], level + 1));
                                }
                            }
                        }
                        queueSize.decrementAndGet();
                    }
                    return null;
                }
            });
        }
        Throwable problem = null;
        int j = numberOfThreads;
        while (j-- != 0) {
            try {
                executorCompletionService.take().get();
            }
            catch (final Exception e) {
                problem = e.getCause();
            }
        }
        executorService.shutdown();
        if (problem != null) {
            throw (problem instanceof RuntimeException) ? problem : new RuntimeException(problem);
        }
    }
    
    public static void parallelRadixSort(final short[] a, final short[] b) {
        ensureSameLength(a, b);
        parallelRadixSort(a, b, 0, a.length);
    }
    
    private static void insertionSortIndirect(final int[] perm, final short[] a, final short[] b, final int from, final int to) {
        int i = from;
        while (++i < to) {
            final int t = perm[i];
            int j = i;
            for (int u = perm[j - 1]; a[t] < a[u] || (a[t] == a[u] && b[t] < b[u]); u = perm[--j - 1]) {
                perm[j] = u;
                if (from == j - 1) {
                    --j;
                    break;
                }
            }
            perm[j] = t;
        }
    }
    
    public static void radixSortIndirect(final int[] perm, final short[] a, final short[] b, final boolean stable) {
        ensureSameLength(a, b);
        radixSortIndirect(perm, a, b, 0, a.length, stable);
    }
    
    public static void radixSortIndirect(final int[] perm, final short[] a, final short[] b, final int from, final int to, final boolean stable) {
        if (to - from < 1024) {
            insertionSortIndirect(perm, a, b, from, to);
            return;
        }
        final int layers = 2;
        final int maxLevel = 3;
        final int stackSize = 766;
        int stackPos = 0;
        final int[] offsetStack = new int[766];
        final int[] lengthStack = new int[766];
        final int[] levelStack = new int[766];
        lengthStack[stackPos] = to - (offsetStack[stackPos] = from);
        levelStack[stackPos++] = 0;
        final int[] count = new int[256];
        final int[] pos = new int[256];
        final int[] support = (int[])(stable ? new int[perm.length] : null);
        while (stackPos > 0) {
            final int first = offsetStack[--stackPos];
            final int length = lengthStack[stackPos];
            final int level = levelStack[stackPos];
            final int signMask = (level % 2 == 0) ? 128 : 0;
            final short[] k = (level < 2) ? a : b;
            final int shift = (1 - level % 2) * 8;
            int i = first + length;
            while (i-- != first) {
                final int[] array = count;
                final int n = (k[perm[i]] >>> shift & 0xFF) ^ signMask;
                ++array[n];
            }
            int lastUsed = -1;
            int j = 0;
            int p = stable ? 0 : first;
            while (j < 256) {
                if (count[j] != 0) {
                    lastUsed = j;
                }
                p = (pos[j] = p + count[j]);
                ++j;
            }
            if (stable) {
                j = first + length;
                while (j-- != first) {
                    final int[] array2 = support;
                    final int[] array3 = pos;
                    final int n2 = (k[perm[j]] >>> shift & 0xFF) ^ signMask;
                    array2[--array3[n2]] = perm[j];
                }
                System.arraycopy(support, 0, perm, first, length);
                j = 0;
                p = first;
                while (j < 256) {
                    if (level < 3 && count[j] > 1) {
                        if (count[j] < 1024) {
                            insertionSortIndirect(perm, a, b, p, p + count[j]);
                        }
                        else {
                            offsetStack[stackPos] = p;
                            lengthStack[stackPos] = count[j];
                            levelStack[stackPos++] = level + 1;
                        }
                    }
                    p += count[j];
                    ++j;
                }
                java.util.Arrays.fill(count, 0);
            }
            else {
                for (int end = first + length - count[lastUsed], l = first, c = -1; l <= end; l += count[c], count[c] = 0) {
                    int t = perm[l];
                    c = ((k[t] >>> shift & 0xFF) ^ signMask);
                    if (l < end) {
                        while (true) {
                            final int[] array4 = pos;
                            final int n3 = c;
                            final int n4 = array4[n3] - 1;
                            array4[n3] = n4;
                            final int d;
                            if ((d = n4) <= l) {
                                break;
                            }
                            final int z = t;
                            t = perm[d];
                            perm[d] = z;
                            c = ((k[t] >>> shift & 0xFF) ^ signMask);
                        }
                        perm[l] = t;
                    }
                    if (level < 3 && count[c] > 1) {
                        if (count[c] < 1024) {
                            insertionSortIndirect(perm, a, b, l, l + count[c]);
                        }
                        else {
                            offsetStack[stackPos] = l;
                            lengthStack[stackPos] = count[c];
                            levelStack[stackPos++] = level + 1;
                        }
                    }
                }
            }
        }
    }
    
    private static void selectionSort(final short[][] a, final int from, final int to, final int level) {
        final int layers = a.length;
        final int firstLayer = level / 2;
        for (int i = from; i < to - 1; ++i) {
            int m = i;
            for (int j = i + 1; j < to; ++j) {
                for (int p = firstLayer; p < layers; ++p) {
                    if (a[p][j] < a[p][m]) {
                        m = j;
                        break;
                    }
                    if (a[p][j] > a[p][m]) {
                        break;
                    }
                }
            }
            if (m != i) {
                int p2 = layers;
                while (p2-- != 0) {
                    final short u = a[p2][i];
                    a[p2][i] = a[p2][m];
                    a[p2][m] = u;
                }
            }
        }
    }
    
    public static void radixSort(final short[][] a) {
        radixSort(a, 0, a[0].length);
    }
    
    public static void radixSort(final short[][] a, final int from, final int to) {
        if (to - from < 1024) {
            selectionSort(a, from, to, 0);
            return;
        }
        final int layers = a.length;
        final int maxLevel = 2 * layers - 1;
        int p = layers;
        final int l = a[0].length;
        while (p-- != 0) {
            if (a[p].length != l) {
                throw new IllegalArgumentException("The array of index " + p + " has not the same length of the array of index 0.");
            }
        }
        final int stackSize = 255 * (layers * 2 - 1) + 1;
        int stackPos = 0;
        final int[] offsetStack = new int[stackSize];
        final int[] lengthStack = new int[stackSize];
        final int[] levelStack = new int[stackSize];
        lengthStack[stackPos] = to - (offsetStack[stackPos] = from);
        levelStack[stackPos++] = 0;
        final int[] count = new int[256];
        final int[] pos = new int[256];
        final short[] t = new short[layers];
        while (stackPos > 0) {
            final int first = offsetStack[--stackPos];
            final int length = lengthStack[stackPos];
            final int level = levelStack[stackPos];
            final int signMask = (level % 2 == 0) ? 128 : 0;
            final short[] k = a[level / 2];
            final int shift = (1 - level % 2) * 8;
            int i = first + length;
            while (i-- != first) {
                final int[] array = count;
                final int n = (k[i] >>> shift & 0xFF) ^ signMask;
                ++array[n];
            }
            int lastUsed = -1;
            int j = 0;
            int p2 = first;
            while (j < 256) {
                if (count[j] != 0) {
                    lastUsed = j;
                }
                p2 = (pos[j] = p2 + count[j]);
                ++j;
            }
            for (int end = first + length - count[lastUsed], m = first, c = -1; m <= end; m += count[c], count[c] = 0) {
                int p3 = layers;
                while (p3-- != 0) {
                    t[p3] = a[p3][m];
                }
                c = ((k[m] >>> shift & 0xFF) ^ signMask);
                if (m < end) {
                    while (true) {
                        final int[] array2 = pos;
                        final int n2 = c;
                        final int n3 = array2[n2] - 1;
                        array2[n2] = n3;
                        final int d;
                        if ((d = n3) <= m) {
                            break;
                        }
                        c = ((k[d] >>> shift & 0xFF) ^ signMask);
                        p3 = layers;
                        while (p3-- != 0) {
                            final short u = t[p3];
                            t[p3] = a[p3][d];
                            a[p3][d] = u;
                        }
                    }
                    p3 = layers;
                    while (p3-- != 0) {
                        a[p3][m] = t[p3];
                    }
                }
                if (level < maxLevel && count[c] > 1) {
                    if (count[c] < 1024) {
                        selectionSort(a, m, m + count[c], level + 1);
                    }
                    else {
                        offsetStack[stackPos] = m;
                        lengthStack[stackPos] = count[c];
                        levelStack[stackPos++] = level + 1;
                    }
                }
            }
        }
    }
    
    public static short[] shuffle(final short[] a, final int from, final int to, final Random random) {
        int i = to - from;
        while (i-- != 0) {
            final int p = random.nextInt(i + 1);
            final short t = a[from + i];
            a[from + i] = a[from + p];
            a[from + p] = t;
        }
        return a;
    }
    
    public static short[] shuffle(final short[] a, final Random random) {
        int i = a.length;
        while (i-- != 0) {
            final int p = random.nextInt(i + 1);
            final short t = a[i];
            a[i] = a[p];
            a[p] = t;
        }
        return a;
    }
    
    public static short[] reverse(final short[] a) {
        final int length = a.length;
        int i = length / 2;
        while (i-- != 0) {
            final short t = a[length - i - 1];
            a[length - i - 1] = a[i];
            a[i] = t;
        }
        return a;
    }
    
    public static short[] reverse(final short[] a, final int from, final int to) {
        final int length = to - from;
        int i = length / 2;
        while (i-- != 0) {
            final short t = a[from + length - i - 1];
            a[from + length - i - 1] = a[from + i];
            a[from + i] = t;
        }
        return a;
    }
    
    static {
        EMPTY_ARRAY = new short[0];
        POISON_PILL = new Segment(-1, -1, -1);
        HASH_STRATEGY = new ArrayHashStrategy();
    }
    
    protected static class ForkJoinQuickSortComp extends RecursiveAction
    {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final short[] x;
        private final ShortComparator comp;
        
        public ForkJoinQuickSortComp(final short[] x, final int from, final int to, final ShortComparator comp) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.comp = comp;
        }
        
        @Override
        protected void compute() {
            final short[] x = this.x;
            final int len = this.to - this.from;
            if (len < 8192) {
                ShortArrays.quickSort(x, this.from, this.to, this.comp);
                return;
            }
            int m = this.from + len / 2;
            int l = this.from;
            int n = this.to - 1;
            int s = len / 8;
            l = med3(x, l, l + s, l + 2 * s, this.comp);
            m = med3(x, m - s, m, m + s, this.comp);
            n = med3(x, n - 2 * s, n - s, n, this.comp);
            m = med3(x, l, m, n, this.comp);
            final short v = x[m];
            int b;
            int a = b = this.from;
            int d;
            int c = d = this.to - 1;
            while (true) {
                int comparison;
                if (b <= c && (comparison = this.comp.compare(x[b], v)) <= 0) {
                    if (comparison == 0) {
                        ShortArrays.swap(x, a++, b);
                    }
                    ++b;
                }
                else {
                    while (c >= b && (comparison = this.comp.compare(x[c], v)) >= 0) {
                        if (comparison == 0) {
                            ShortArrays.swap(x, c, d--);
                        }
                        --c;
                    }
                    if (b > c) {
                        break;
                    }
                    ShortArrays.swap(x, b++, c--);
                }
            }
            s = Math.min(a - this.from, b - a);
            ShortArrays.swap(x, this.from, b - s, s);
            s = Math.min(d - c, this.to - d - 1);
            ShortArrays.swap(x, b, this.to - s, s);
            s = b - a;
            final int t = d - c;
            if (s > 1 && t > 1) {
                ForkJoinTask.invokeAll(new ForkJoinQuickSortComp(x, this.from, this.from + s, this.comp), new ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp));
            }
            else if (s > 1) {
                ForkJoinTask.invokeAll(new ForkJoinQuickSortComp(x, this.from, this.from + s, this.comp));
            }
            else {
                ForkJoinTask.invokeAll(new ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp));
            }
        }
    }
    
    protected static class ForkJoinQuickSort extends RecursiveAction
    {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final short[] x;
        
        public ForkJoinQuickSort(final short[] x, final int from, final int to) {
            this.from = from;
            this.to = to;
            this.x = x;
        }
        
        @Override
        protected void compute() {
            final short[] x = this.x;
            final int len = this.to - this.from;
            if (len < 8192) {
                ShortArrays.quickSort(x, this.from, this.to);
                return;
            }
            int m = this.from + len / 2;
            int l = this.from;
            int n = this.to - 1;
            int s = len / 8;
            l = med3(x, l, l + s, l + 2 * s);
            m = med3(x, m - s, m, m + s);
            n = med3(x, n - 2 * s, n - s, n);
            m = med3(x, l, m, n);
            final short v = x[m];
            int b;
            int a = b = this.from;
            int d;
            int c = d = this.to - 1;
            while (true) {
                int comparison;
                if (b <= c && (comparison = Short.compare(x[b], v)) <= 0) {
                    if (comparison == 0) {
                        ShortArrays.swap(x, a++, b);
                    }
                    ++b;
                }
                else {
                    while (c >= b && (comparison = Short.compare(x[c], v)) >= 0) {
                        if (comparison == 0) {
                            ShortArrays.swap(x, c, d--);
                        }
                        --c;
                    }
                    if (b > c) {
                        break;
                    }
                    ShortArrays.swap(x, b++, c--);
                }
            }
            s = Math.min(a - this.from, b - a);
            ShortArrays.swap(x, this.from, b - s, s);
            s = Math.min(d - c, this.to - d - 1);
            ShortArrays.swap(x, b, this.to - s, s);
            s = b - a;
            final int t = d - c;
            if (s > 1 && t > 1) {
                ForkJoinTask.invokeAll(new ForkJoinQuickSort(x, this.from, this.from + s), new ForkJoinQuickSort(x, this.to - t, this.to));
            }
            else if (s > 1) {
                ForkJoinTask.invokeAll(new ForkJoinQuickSort(x, this.from, this.from + s));
            }
            else {
                ForkJoinTask.invokeAll(new ForkJoinQuickSort(x, this.to - t, this.to));
            }
        }
    }
    
    protected static class ForkJoinQuickSortIndirect extends RecursiveAction
    {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final int[] perm;
        private final short[] x;
        
        public ForkJoinQuickSortIndirect(final int[] perm, final short[] x, final int from, final int to) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.perm = perm;
        }
        
        @Override
        protected void compute() {
            final short[] x = this.x;
            final int len = this.to - this.from;
            if (len < 8192) {
                ShortArrays.quickSortIndirect(this.perm, x, this.from, this.to);
                return;
            }
            int m = this.from + len / 2;
            int l = this.from;
            int n = this.to - 1;
            int s = len / 8;
            l = med3Indirect(this.perm, x, l, l + s, l + 2 * s);
            m = med3Indirect(this.perm, x, m - s, m, m + s);
            n = med3Indirect(this.perm, x, n - 2 * s, n - s, n);
            m = med3Indirect(this.perm, x, l, m, n);
            final short v = x[this.perm[m]];
            int b;
            int a = b = this.from;
            int d;
            int c = d = this.to - 1;
            while (true) {
                int comparison;
                if (b <= c && (comparison = Short.compare(x[this.perm[b]], v)) <= 0) {
                    if (comparison == 0) {
                        IntArrays.swap(this.perm, a++, b);
                    }
                    ++b;
                }
                else {
                    while (c >= b && (comparison = Short.compare(x[this.perm[c]], v)) >= 0) {
                        if (comparison == 0) {
                            IntArrays.swap(this.perm, c, d--);
                        }
                        --c;
                    }
                    if (b > c) {
                        break;
                    }
                    IntArrays.swap(this.perm, b++, c--);
                }
            }
            s = Math.min(a - this.from, b - a);
            IntArrays.swap(this.perm, this.from, b - s, s);
            s = Math.min(d - c, this.to - d - 1);
            IntArrays.swap(this.perm, b, this.to - s, s);
            s = b - a;
            final int t = d - c;
            if (s > 1 && t > 1) {
                ForkJoinTask.invokeAll(new ForkJoinQuickSortIndirect(this.perm, x, this.from, this.from + s), new ForkJoinQuickSortIndirect(this.perm, x, this.to - t, this.to));
            }
            else if (s > 1) {
                ForkJoinTask.invokeAll(new ForkJoinQuickSortIndirect(this.perm, x, this.from, this.from + s));
            }
            else {
                ForkJoinTask.invokeAll(new ForkJoinQuickSortIndirect(this.perm, x, this.to - t, this.to));
            }
        }
    }
    
    protected static class ForkJoinQuickSort2 extends RecursiveAction
    {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final short[] x;
        private final short[] y;
        
        public ForkJoinQuickSort2(final short[] x, final short[] y, final int from, final int to) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.y = y;
        }
        
        @Override
        protected void compute() {
            final short[] x = this.x;
            final short[] y = this.y;
            final int len = this.to - this.from;
            if (len < 8192) {
                ShortArrays.quickSort(x, y, this.from, this.to);
                return;
            }
            int m = this.from + len / 2;
            int l = this.from;
            int n = this.to - 1;
            int s = len / 8;
            l = med3(x, y, l, l + s, l + 2 * s);
            m = med3(x, y, m - s, m, m + s);
            n = med3(x, y, n - 2 * s, n - s, n);
            m = med3(x, y, l, m, n);
            final short v = x[m];
            final short w = y[m];
            int b;
            int a = b = this.from;
            int d;
            int c = d = this.to - 1;
            while (true) {
                int t;
                int comparison;
                if (b <= c && (comparison = (((t = Short.compare(x[b], v)) == 0) ? Short.compare(y[b], w) : t)) <= 0) {
                    if (comparison == 0) {
                        swap(x, y, a++, b);
                    }
                    ++b;
                }
                else {
                    while (c >= b && (comparison = (((t = Short.compare(x[c], v)) == 0) ? Short.compare(y[c], w) : t)) >= 0) {
                        if (comparison == 0) {
                            swap(x, y, c, d--);
                        }
                        --c;
                    }
                    if (b > c) {
                        break;
                    }
                    swap(x, y, b++, c--);
                }
            }
            s = Math.min(a - this.from, b - a);
            swap(x, y, this.from, b - s, s);
            s = Math.min(d - c, this.to - d - 1);
            swap(x, y, b, this.to - s, s);
            s = b - a;
            final int t2 = d - c;
            if (s > 1 && t2 > 1) {
                ForkJoinTask.invokeAll(new ForkJoinQuickSort2(x, y, this.from, this.from + s), new ForkJoinQuickSort2(x, y, this.to - t2, this.to));
            }
            else if (s > 1) {
                ForkJoinTask.invokeAll(new ForkJoinQuickSort2(x, y, this.from, this.from + s));
            }
            else {
                ForkJoinTask.invokeAll(new ForkJoinQuickSort2(x, y, this.to - t2, this.to));
            }
        }
    }
    
    protected static final class Segment
    {
        protected final int offset;
        protected final int length;
        protected final int level;
        
        protected Segment(final int offset, final int length, final int level) {
            this.offset = offset;
            this.length = length;
            this.level = level;
        }
        
        @Override
        public String toString() {
            return "Segment [offset=" + this.offset + ", length=" + this.length + ", level=" + this.level + "]";
        }
    }
    
    private static final class ArrayHashStrategy implements Hash.Strategy<short[]>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        @Override
        public int hashCode(final short[] o) {
            return java.util.Arrays.hashCode(o);
        }
        
        @Override
        public boolean equals(final short[] a, final short[] b) {
            return java.util.Arrays.equals(a, b);
        }
    }
}
