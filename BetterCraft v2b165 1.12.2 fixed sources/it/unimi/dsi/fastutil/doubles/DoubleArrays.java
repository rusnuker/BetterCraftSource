// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

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

public class DoubleArrays
{
    public static final double[] EMPTY_ARRAY;
    private static final int QUICKSORT_NO_REC = 16;
    private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
    private static final int QUICKSORT_MEDIAN_OF_9 = 128;
    private static final int MERGESORT_NO_REC = 16;
    private static final int DIGIT_BITS = 8;
    private static final int DIGIT_MASK = 255;
    private static final int DIGITS_PER_ELEMENT = 8;
    private static final int RADIXSORT_NO_REC = 1024;
    private static final int PARALLEL_RADIXSORT_NO_FORK = 1024;
    protected static final Segment POISON_PILL;
    public static final Hash.Strategy<double[]> HASH_STRATEGY;
    
    private DoubleArrays() {
    }
    
    public static double[] ensureCapacity(final double[] array, final int length) {
        if (length > array.length) {
            final double[] t = new double[length];
            System.arraycopy(array, 0, t, 0, array.length);
            return t;
        }
        return array;
    }
    
    public static double[] ensureCapacity(final double[] array, final int length, final int preserve) {
        if (length > array.length) {
            final double[] t = new double[length];
            System.arraycopy(array, 0, t, 0, preserve);
            return t;
        }
        return array;
    }
    
    public static double[] grow(final double[] array, final int length) {
        if (length > array.length) {
            final int newLength = (int)Math.max(Math.min(2L * array.length, 2147483639L), length);
            final double[] t = new double[newLength];
            System.arraycopy(array, 0, t, 0, array.length);
            return t;
        }
        return array;
    }
    
    public static double[] grow(final double[] array, final int length, final int preserve) {
        if (length > array.length) {
            final int newLength = (int)Math.max(Math.min(2L * array.length, 2147483639L), length);
            final double[] t = new double[newLength];
            System.arraycopy(array, 0, t, 0, preserve);
            return t;
        }
        return array;
    }
    
    public static double[] trim(final double[] array, final int length) {
        if (length >= array.length) {
            return array;
        }
        final double[] t = (length == 0) ? DoubleArrays.EMPTY_ARRAY : new double[length];
        System.arraycopy(array, 0, t, 0, length);
        return t;
    }
    
    public static double[] setLength(final double[] array, final int length) {
        if (length == array.length) {
            return array;
        }
        if (length < array.length) {
            return trim(array, length);
        }
        return ensureCapacity(array, length);
    }
    
    public static double[] copy(final double[] array, final int offset, final int length) {
        ensureOffsetLength(array, offset, length);
        final double[] a = (length == 0) ? DoubleArrays.EMPTY_ARRAY : new double[length];
        System.arraycopy(array, offset, a, 0, length);
        return a;
    }
    
    public static double[] copy(final double[] array) {
        return array.clone();
    }
    
    @Deprecated
    public static void fill(final double[] array, final double value) {
        int i = array.length;
        while (i-- != 0) {
            array[i] = value;
        }
    }
    
    @Deprecated
    public static void fill(final double[] array, final int from, int to, final double value) {
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
    public static boolean equals(final double[] a1, final double[] a2) {
        int i = a1.length;
        if (i != a2.length) {
            return false;
        }
        while (i-- != 0) {
            if (Double.doubleToLongBits(a1[i]) != Double.doubleToLongBits(a2[i])) {
                return false;
            }
        }
        return true;
    }
    
    public static void ensureFromTo(final double[] a, final int from, final int to) {
        Arrays.ensureFromTo(a.length, from, to);
    }
    
    public static void ensureOffsetLength(final double[] a, final int offset, final int length) {
        Arrays.ensureOffsetLength(a.length, offset, length);
    }
    
    public static void ensureSameLength(final double[] a, final double[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Array size mismatch: " + a.length + " != " + b.length);
        }
    }
    
    public static void swap(final double[] x, final int a, final int b) {
        final double t = x[a];
        x[a] = x[b];
        x[b] = t;
    }
    
    public static void swap(final double[] x, int a, int b, final int n) {
        for (int i = 0; i < n; ++i, ++a, ++b) {
            swap(x, a, b);
        }
    }
    
    private static int med3(final double[] x, final int a, final int b, final int c, final DoubleComparator comp) {
        final int ab = comp.compare(x[a], x[b]);
        final int ac = comp.compare(x[a], x[c]);
        final int bc = comp.compare(x[b], x[c]);
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static void selectionSort(final double[] a, final int from, final int to, final DoubleComparator comp) {
        for (int i = from; i < to - 1; ++i) {
            int m = i;
            for (int j = i + 1; j < to; ++j) {
                if (comp.compare(a[j], a[m]) < 0) {
                    m = j;
                }
            }
            if (m != i) {
                final double u = a[i];
                a[i] = a[m];
                a[m] = u;
            }
        }
    }
    
    private static void insertionSort(final double[] a, final int from, final int to, final DoubleComparator comp) {
        int i = from;
        while (++i < to) {
            final double t = a[i];
            int j = i;
            for (double u = a[j - 1]; comp.compare(t, u) < 0; u = a[--j - 1]) {
                a[j] = u;
                if (from == j - 1) {
                    --j;
                    break;
                }
            }
            a[j] = t;
        }
    }
    
    public static void quickSort(final double[] x, final int from, final int to, final DoubleComparator comp) {
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
        final double v = x[m];
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
    
    public static void quickSort(final double[] x, final DoubleComparator comp) {
        quickSort(x, 0, x.length, comp);
    }
    
    public static void parallelQuickSort(final double[] x, final int from, final int to, final DoubleComparator comp) {
        if (to - from < 8192) {
            quickSort(x, from, to, comp);
        }
        else {
            final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
            pool.invoke((ForkJoinTask<Object>)new ForkJoinQuickSortComp(x, from, to, comp));
            pool.shutdown();
        }
    }
    
    public static void parallelQuickSort(final double[] x, final DoubleComparator comp) {
        parallelQuickSort(x, 0, x.length, comp);
    }
    
    private static int med3(final double[] x, final int a, final int b, final int c) {
        final int ab = Double.compare(x[a], x[b]);
        final int ac = Double.compare(x[a], x[c]);
        final int bc = Double.compare(x[b], x[c]);
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static void selectionSort(final double[] a, final int from, final int to) {
        for (int i = from; i < to - 1; ++i) {
            int m = i;
            for (int j = i + 1; j < to; ++j) {
                if (Double.compare(a[j], a[m]) < 0) {
                    m = j;
                }
            }
            if (m != i) {
                final double u = a[i];
                a[i] = a[m];
                a[m] = u;
            }
        }
    }
    
    private static void insertionSort(final double[] a, final int from, final int to) {
        int i = from;
        while (++i < to) {
            final double t = a[i];
            int j = i;
            for (double u = a[j - 1]; Double.compare(t, u) < 0; u = a[--j - 1]) {
                a[j] = u;
                if (from == j - 1) {
                    --j;
                    break;
                }
            }
            a[j] = t;
        }
    }
    
    public static void quickSort(final double[] x, final int from, final int to) {
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
        final double v = x[m];
        int b;
        int a = b = from;
        int d;
        int c = d = to - 1;
        while (true) {
            int comparison;
            if (b <= c && (comparison = Double.compare(x[b], v)) <= 0) {
                if (comparison == 0) {
                    swap(x, a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = Double.compare(x[c], v)) >= 0) {
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
    
    public static void quickSort(final double[] x) {
        quickSort(x, 0, x.length);
    }
    
    public static void parallelQuickSort(final double[] x, final int from, final int to) {
        if (to - from < 8192) {
            quickSort(x, from, to);
        }
        else {
            final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
            pool.invoke((ForkJoinTask<Object>)new ForkJoinQuickSort(x, from, to));
            pool.shutdown();
        }
    }
    
    public static void parallelQuickSort(final double[] x) {
        parallelQuickSort(x, 0, x.length);
    }
    
    private static int med3Indirect(final int[] perm, final double[] x, final int a, final int b, final int c) {
        final double aa = x[perm[a]];
        final double bb = x[perm[b]];
        final double cc = x[perm[c]];
        final int ab = Double.compare(aa, bb);
        final int ac = Double.compare(aa, cc);
        final int bc = Double.compare(bb, cc);
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static void insertionSortIndirect(final int[] perm, final double[] a, final int from, final int to) {
        int i = from;
        while (++i < to) {
            final int t = perm[i];
            int j = i;
            for (int u = perm[j - 1]; Double.compare(a[t], a[u]) < 0; u = perm[--j - 1]) {
                perm[j] = u;
                if (from == j - 1) {
                    --j;
                    break;
                }
            }
            perm[j] = t;
        }
    }
    
    public static void quickSortIndirect(final int[] perm, final double[] x, final int from, final int to) {
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
        final double v = x[perm[m]];
        int b;
        int a = b = from;
        int d;
        int c = d = to - 1;
        while (true) {
            int comparison;
            if (b <= c && (comparison = Double.compare(x[perm[b]], v)) <= 0) {
                if (comparison == 0) {
                    IntArrays.swap(perm, a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = Double.compare(x[perm[c]], v)) >= 0) {
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
    
    public static void quickSortIndirect(final int[] perm, final double[] x) {
        quickSortIndirect(perm, x, 0, x.length);
    }
    
    public static void parallelQuickSortIndirect(final int[] perm, final double[] x, final int from, final int to) {
        if (to - from < 8192) {
            quickSortIndirect(perm, x, from, to);
        }
        else {
            final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
            pool.invoke((ForkJoinTask<Object>)new ForkJoinQuickSortIndirect(perm, x, from, to));
            pool.shutdown();
        }
    }
    
    public static void parallelQuickSortIndirect(final int[] perm, final double[] x) {
        parallelQuickSortIndirect(perm, x, 0, x.length);
    }
    
    public static void stabilize(final int[] perm, final double[] x, final int from, final int to) {
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
    
    public static void stabilize(final int[] perm, final double[] x) {
        stabilize(perm, x, 0, perm.length);
    }
    
    private static int med3(final double[] x, final double[] y, final int a, final int b, final int c) {
        int t;
        final int ab = ((t = Double.compare(x[a], x[b])) == 0) ? Double.compare(y[a], y[b]) : t;
        final int ac = ((t = Double.compare(x[a], x[c])) == 0) ? Double.compare(y[a], y[c]) : t;
        final int bc = ((t = Double.compare(x[b], x[c])) == 0) ? Double.compare(y[b], y[c]) : t;
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static void swap(final double[] x, final double[] y, final int a, final int b) {
        final double t = x[a];
        final double u = y[a];
        x[a] = x[b];
        y[a] = y[b];
        x[b] = t;
        y[b] = u;
    }
    
    private static void swap(final double[] x, final double[] y, int a, int b, final int n) {
        for (int i = 0; i < n; ++i, ++a, ++b) {
            swap(x, y, a, b);
        }
    }
    
    private static void selectionSort(final double[] a, final double[] b, final int from, final int to) {
        for (int i = from; i < to - 1; ++i) {
            int m = i;
            for (int j = i + 1; j < to; ++j) {
                final int u;
                if ((u = Double.compare(a[j], a[m])) < 0 || (u == 0 && Double.compare(b[j], b[m]) < 0)) {
                    m = j;
                }
            }
            if (m != i) {
                double t = a[i];
                a[i] = a[m];
                a[m] = t;
                t = b[i];
                b[i] = b[m];
                b[m] = t;
            }
        }
    }
    
    public static void quickSort(final double[] x, final double[] y, final int from, final int to) {
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
        final double v = x[m];
        final double w = y[m];
        int b;
        int a = b = from;
        int d;
        int c = d = to - 1;
        while (true) {
            int t;
            int comparison;
            if (b <= c && (comparison = (((t = Double.compare(x[b], v)) == 0) ? Double.compare(y[b], w) : t)) <= 0) {
                if (comparison == 0) {
                    swap(x, y, a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = (((t = Double.compare(x[c], v)) == 0) ? Double.compare(y[c], w) : t)) >= 0) {
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
    
    public static void quickSort(final double[] x, final double[] y) {
        ensureSameLength(x, y);
        quickSort(x, y, 0, x.length);
    }
    
    public static void parallelQuickSort(final double[] x, final double[] y, final int from, final int to) {
        if (to - from < 8192) {
            quickSort(x, y, from, to);
        }
        final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        pool.invoke((ForkJoinTask<Object>)new ForkJoinQuickSort2(x, y, from, to));
        pool.shutdown();
    }
    
    public static void parallelQuickSort(final double[] x, final double[] y) {
        ensureSameLength(x, y);
        parallelQuickSort(x, y, 0, x.length);
    }
    
    public static void mergeSort(final double[] a, final int from, final int to, final double[] supp) {
        final int len = to - from;
        if (len < 16) {
            insertionSort(a, from, to);
            return;
        }
        final int mid = from + to >>> 1;
        mergeSort(supp, from, mid, a);
        mergeSort(supp, mid, to, a);
        if (Double.compare(supp[mid - 1], supp[mid]) <= 0) {
            System.arraycopy(supp, from, a, from, len);
            return;
        }
        int i = from;
        int p = from;
        int q = mid;
        while (i < to) {
            if (q >= to || (p < mid && Double.compare(supp[p], supp[q]) <= 0)) {
                a[i] = supp[p++];
            }
            else {
                a[i] = supp[q++];
            }
            ++i;
        }
    }
    
    public static void mergeSort(final double[] a, final int from, final int to) {
        mergeSort(a, from, to, a.clone());
    }
    
    public static void mergeSort(final double[] a) {
        mergeSort(a, 0, a.length);
    }
    
    public static void mergeSort(final double[] a, final int from, final int to, final DoubleComparator comp, final double[] supp) {
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
    
    public static void mergeSort(final double[] a, final int from, final int to, final DoubleComparator comp) {
        mergeSort(a, from, to, comp, a.clone());
    }
    
    public static void mergeSort(final double[] a, final DoubleComparator comp) {
        mergeSort(a, 0, a.length, comp);
    }
    
    public static int binarySearch(final double[] a, int from, int to, final double key) {
        --to;
        while (from <= to) {
            final int mid = from + to >>> 1;
            final double midVal = a[mid];
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
    
    public static int binarySearch(final double[] a, final double key) {
        return binarySearch(a, 0, a.length, key);
    }
    
    public static int binarySearch(final double[] a, int from, int to, final double key, final DoubleComparator c) {
        --to;
        while (from <= to) {
            final int mid = from + to >>> 1;
            final double midVal = a[mid];
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
    
    public static int binarySearch(final double[] a, final double key, final DoubleComparator c) {
        return binarySearch(a, 0, a.length, key, c);
    }
    
    private static final long fixDouble(final double d) {
        final long l = Double.doubleToLongBits(d);
        return (l >= 0L) ? l : (l ^ Long.MAX_VALUE);
    }
    
    public static void radixSort(final double[] a) {
        radixSort(a, 0, a.length);
    }
    
    public static void radixSort(final double[] a, final int from, final int to) {
        if (to - from < 1024) {
            quickSort(a, from, to);
            return;
        }
        final int maxLevel = 7;
        final int stackSize = 1786;
        int stackPos = 0;
        final int[] offsetStack = new int[1786];
        final int[] lengthStack = new int[1786];
        final int[] levelStack = new int[1786];
        lengthStack[stackPos] = to - (offsetStack[stackPos] = from);
        levelStack[stackPos++] = 0;
        final int[] count = new int[256];
        final int[] pos = new int[256];
        while (stackPos > 0) {
            final int first = offsetStack[--stackPos];
            final int length = lengthStack[stackPos];
            final int level = levelStack[stackPos];
            final int signMask = (level % 8 == 0) ? 128 : 0;
            final int shift = (7 - level % 8) * 8;
            int i = first + length;
            while (i-- != first) {
                final int[] array = count;
                final int n = (int)((fixDouble(a[i]) >>> shift & 0xFFL) ^ (long)signMask);
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
                double t = a[k];
                c = (int)((fixDouble(t) >>> shift & 0xFFL) ^ (long)signMask);
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
                        final double z = t;
                        t = a[d];
                        a[d] = z;
                        c = (int)((fixDouble(t) >>> shift & 0xFFL) ^ (long)signMask);
                    }
                    a[k] = t;
                }
                if (level < 7 && count[c] > 1) {
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
    
    public static void parallelRadixSort(final double[] a, final int from, final int to) {
        if (to - from < 1024) {
            quickSort(a, from, to);
            return;
        }
        final int maxLevel = 7;
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
                                queue.add(DoubleArrays.POISON_PILL);
                            }
                        }
                        final Segment segment = queue.take();
                        if (segment == DoubleArrays.POISON_PILL) {
                            break;
                        }
                        final int first = segment.offset;
                        final int length = segment.length;
                        final int level = segment.level;
                        final int signMask = (level % 8 == 0) ? 128 : 0;
                        final int shift = (7 - level % 8) * 8;
                        int j = first + length;
                        while (j-- != first) {
                            final int[] array = count;
                            final int n = (int)((fixDouble(a[j]) >>> shift & 0xFFL) ^ (long)signMask);
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
                            double t = a[l];
                            c = (int)((fixDouble(t) >>> shift & 0xFFL) ^ (long)signMask);
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
                                    final double z = t;
                                    t = a[d];
                                    a[d] = z;
                                    c = (int)((fixDouble(t) >>> shift & 0xFFL) ^ (long)signMask);
                                }
                                a[l] = t;
                            }
                            if (level < 7 && count[c] > 1) {
                                if (count[c] < 1024) {
                                    DoubleArrays.quickSort(a, l, l + count[c]);
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
    
    public static void parallelRadixSort(final double[] a) {
        parallelRadixSort(a, 0, a.length);
    }
    
    public static void radixSortIndirect(final int[] perm, final double[] a, final boolean stable) {
        radixSortIndirect(perm, a, 0, perm.length, stable);
    }
    
    public static void radixSortIndirect(final int[] perm, final double[] a, final int from, final int to, final boolean stable) {
        if (to - from < 1024) {
            insertionSortIndirect(perm, a, from, to);
            return;
        }
        final int maxLevel = 7;
        final int stackSize = 1786;
        int stackPos = 0;
        final int[] offsetStack = new int[1786];
        final int[] lengthStack = new int[1786];
        final int[] levelStack = new int[1786];
        lengthStack[stackPos] = to - (offsetStack[stackPos] = from);
        levelStack[stackPos++] = 0;
        final int[] count = new int[256];
        final int[] pos = new int[256];
        final int[] support = (int[])(stable ? new int[perm.length] : null);
        while (stackPos > 0) {
            final int first = offsetStack[--stackPos];
            final int length = lengthStack[stackPos];
            final int level = levelStack[stackPos];
            final int signMask = (level % 8 == 0) ? 128 : 0;
            final int shift = (7 - level % 8) * 8;
            int i = first + length;
            while (i-- != first) {
                final int[] array = count;
                final int n = (int)((fixDouble(a[perm[i]]) >>> shift & 0xFFL) ^ (long)signMask);
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
                    final int n2 = (int)((fixDouble(a[perm[j]]) >>> shift & 0xFFL) ^ (long)signMask);
                    array2[--array3[n2]] = perm[j];
                }
                System.arraycopy(support, 0, perm, first, length);
                j = 0;
                p = first;
                while (j <= lastUsed) {
                    if (level < 7 && count[j] > 1) {
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
                    c = (int)((fixDouble(a[t]) >>> shift & 0xFFL) ^ (long)signMask);
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
                            c = (int)((fixDouble(a[t]) >>> shift & 0xFFL) ^ (long)signMask);
                        }
                        perm[k] = t;
                    }
                    if (level < 7 && count[c] > 1) {
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
    
    public static void parallelRadixSortIndirect(final int[] perm, final double[] a, final int from, final int to, final boolean stable) {
        if (to - from < 1024) {
            radixSortIndirect(perm, a, from, to, stable);
            return;
        }
        final int maxLevel = 7;
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
                                queue.add(DoubleArrays.POISON_PILL);
                            }
                        }
                        final Segment segment = queue.take();
                        if (segment == DoubleArrays.POISON_PILL) {
                            break;
                        }
                        final int first = segment.offset;
                        final int length = segment.length;
                        final int level = segment.level;
                        final int signMask = (level % 8 == 0) ? 128 : 0;
                        final int shift = (7 - level % 8) * 8;
                        int j = first + length;
                        while (j-- != first) {
                            final int[] array = count;
                            final int n = (int)((fixDouble(a[perm[j]]) >>> shift & 0xFFL) ^ (long)signMask);
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
                                final int n2 = (int)((fixDouble(a[perm[k]]) >>> shift & 0xFFL) ^ (long)signMask);
                                val$support[--array2[n2]] = perm[k];
                            }
                            System.arraycopy(support, first, perm, first, length);
                            k = 0;
                            p = first;
                            while (k <= lastUsed) {
                                if (level < 7 && count[k] > 1) {
                                    if (count[k] < 1024) {
                                        DoubleArrays.radixSortIndirect(perm, a, p, p + count[k], stable);
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
                                c = (int)((fixDouble(a[t]) >>> shift & 0xFFL) ^ (long)signMask);
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
                                        c = (int)((fixDouble(a[t]) >>> shift & 0xFFL) ^ (long)signMask);
                                    }
                                    perm[l] = t;
                                }
                                if (level < 7 && count[c] > 1) {
                                    if (count[c] < 1024) {
                                        DoubleArrays.radixSortIndirect(perm, a, l, l + count[c], stable);
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
    
    public static void parallelRadixSortIndirect(final int[] perm, final double[] a, final boolean stable) {
        parallelRadixSortIndirect(perm, a, 0, a.length, stable);
    }
    
    public static void radixSort(final double[] a, final double[] b) {
        ensureSameLength(a, b);
        radixSort(a, b, 0, a.length);
    }
    
    public static void radixSort(final double[] a, final double[] b, final int from, final int to) {
        if (to - from < 1024) {
            selectionSort(a, b, from, to);
            return;
        }
        final int layers = 2;
        final int maxLevel = 15;
        final int stackSize = 3826;
        int stackPos = 0;
        final int[] offsetStack = new int[3826];
        final int[] lengthStack = new int[3826];
        final int[] levelStack = new int[3826];
        lengthStack[stackPos] = to - (offsetStack[stackPos] = from);
        levelStack[stackPos++] = 0;
        final int[] count = new int[256];
        final int[] pos = new int[256];
        while (stackPos > 0) {
            final int first = offsetStack[--stackPos];
            final int length = lengthStack[stackPos];
            final int level = levelStack[stackPos];
            final int signMask = (level % 8 == 0) ? 128 : 0;
            final double[] k = (level < 8) ? a : b;
            final int shift = (7 - level % 8) * 8;
            int i = first + length;
            while (i-- != first) {
                final int[] array = count;
                final int n = (int)((fixDouble(k[i]) >>> shift & 0xFFL) ^ (long)signMask);
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
                double t = a[l];
                double u = b[l];
                c = (int)((fixDouble(k[l]) >>> shift & 0xFFL) ^ (long)signMask);
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
                        c = (int)((fixDouble(k[d]) >>> shift & 0xFFL) ^ (long)signMask);
                        double z = t;
                        t = a[d];
                        a[d] = z;
                        z = u;
                        u = b[d];
                        b[d] = z;
                    }
                    a[l] = t;
                    b[l] = u;
                }
                if (level < 15 && count[c] > 1) {
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
    
    public static void parallelRadixSort(final double[] a, final double[] b, final int from, final int to) {
        if (to - from < 1024) {
            quickSort(a, b, from, to);
            return;
        }
        final int layers = 2;
        if (a.length != b.length) {
            throw new IllegalArgumentException("Array size mismatch.");
        }
        final int maxLevel = 15;
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
                                queue.add(DoubleArrays.POISON_PILL);
                            }
                        }
                        final Segment segment = queue.take();
                        if (segment == DoubleArrays.POISON_PILL) {
                            break;
                        }
                        final int first = segment.offset;
                        final int length = segment.length;
                        final int level = segment.level;
                        final int signMask = (level % 8 == 0) ? 128 : 0;
                        final double[] k = (level < 8) ? a : b;
                        final int shift = (7 - level % 8) * 8;
                        int j = first + length;
                        while (j-- != first) {
                            final int[] array = count;
                            final int n = (int)((fixDouble(k[j]) >>> shift & 0xFFL) ^ (long)signMask);
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
                            double t = a[m];
                            double u = b[m];
                            c = (int)((fixDouble(k[m]) >>> shift & 0xFFL) ^ (long)signMask);
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
                                    c = (int)((fixDouble(k[d]) >>> shift & 0xFFL) ^ (long)signMask);
                                    final double z = t;
                                    final double w = u;
                                    t = a[d];
                                    u = b[d];
                                    a[d] = z;
                                    b[d] = w;
                                }
                                a[m] = t;
                                b[m] = u;
                            }
                            if (level < 15 && count[c] > 1) {
                                if (count[c] < 1024) {
                                    DoubleArrays.quickSort(a, b, m, m + count[c]);
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
    
    public static void parallelRadixSort(final double[] a, final double[] b) {
        ensureSameLength(a, b);
        parallelRadixSort(a, b, 0, a.length);
    }
    
    private static void insertionSortIndirect(final int[] perm, final double[] a, final double[] b, final int from, final int to) {
        int i = from;
        while (++i < to) {
            final int t = perm[i];
            int j = i;
            for (int u = perm[j - 1]; Double.compare(a[t], a[u]) < 0 || (Double.compare(a[t], a[u]) == 0 && Double.compare(b[t], b[u]) < 0); u = perm[--j - 1]) {
                perm[j] = u;
                if (from == j - 1) {
                    --j;
                    break;
                }
            }
            perm[j] = t;
        }
    }
    
    public static void radixSortIndirect(final int[] perm, final double[] a, final double[] b, final boolean stable) {
        ensureSameLength(a, b);
        radixSortIndirect(perm, a, b, 0, a.length, stable);
    }
    
    public static void radixSortIndirect(final int[] perm, final double[] a, final double[] b, final int from, final int to, final boolean stable) {
        if (to - from < 1024) {
            insertionSortIndirect(perm, a, b, from, to);
            return;
        }
        final int layers = 2;
        final int maxLevel = 15;
        final int stackSize = 3826;
        int stackPos = 0;
        final int[] offsetStack = new int[3826];
        final int[] lengthStack = new int[3826];
        final int[] levelStack = new int[3826];
        lengthStack[stackPos] = to - (offsetStack[stackPos] = from);
        levelStack[stackPos++] = 0;
        final int[] count = new int[256];
        final int[] pos = new int[256];
        final int[] support = (int[])(stable ? new int[perm.length] : null);
        while (stackPos > 0) {
            final int first = offsetStack[--stackPos];
            final int length = lengthStack[stackPos];
            final int level = levelStack[stackPos];
            final int signMask = (level % 8 == 0) ? 128 : 0;
            final double[] k = (level < 8) ? a : b;
            final int shift = (7 - level % 8) * 8;
            int i = first + length;
            while (i-- != first) {
                final int[] array = count;
                final int n = (int)((fixDouble(k[perm[i]]) >>> shift & 0xFFL) ^ (long)signMask);
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
                    final int n2 = (int)((fixDouble(k[perm[j]]) >>> shift & 0xFFL) ^ (long)signMask);
                    array2[--array3[n2]] = perm[j];
                }
                System.arraycopy(support, 0, perm, first, length);
                j = 0;
                p = first;
                while (j < 256) {
                    if (level < 15 && count[j] > 1) {
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
                    c = (int)((fixDouble(k[t]) >>> shift & 0xFFL) ^ (long)signMask);
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
                            c = (int)((fixDouble(k[t]) >>> shift & 0xFFL) ^ (long)signMask);
                        }
                        perm[l] = t;
                    }
                    if (level < 15 && count[c] > 1) {
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
    
    private static void selectionSort(final double[][] a, final int from, final int to, final int level) {
        final int layers = a.length;
        final int firstLayer = level / 8;
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
                    final double u = a[p2][i];
                    a[p2][i] = a[p2][m];
                    a[p2][m] = u;
                }
            }
        }
    }
    
    public static void radixSort(final double[][] a) {
        radixSort(a, 0, a[0].length);
    }
    
    public static void radixSort(final double[][] a, final int from, final int to) {
        if (to - from < 1024) {
            selectionSort(a, from, to, 0);
            return;
        }
        final int layers = a.length;
        final int maxLevel = 8 * layers - 1;
        int p = layers;
        final int l = a[0].length;
        while (p-- != 0) {
            if (a[p].length != l) {
                throw new IllegalArgumentException("The array of index " + p + " has not the same length of the array of index 0.");
            }
        }
        final int stackSize = 255 * (layers * 8 - 1) + 1;
        int stackPos = 0;
        final int[] offsetStack = new int[stackSize];
        final int[] lengthStack = new int[stackSize];
        final int[] levelStack = new int[stackSize];
        lengthStack[stackPos] = to - (offsetStack[stackPos] = from);
        levelStack[stackPos++] = 0;
        final int[] count = new int[256];
        final int[] pos = new int[256];
        final double[] t = new double[layers];
        while (stackPos > 0) {
            final int first = offsetStack[--stackPos];
            final int length = lengthStack[stackPos];
            final int level = levelStack[stackPos];
            final int signMask = (level % 8 == 0) ? 128 : 0;
            final double[] k = a[level / 8];
            final int shift = (7 - level % 8) * 8;
            int i = first + length;
            while (i-- != first) {
                final int[] array = count;
                final int n = (int)((fixDouble(k[i]) >>> shift & 0xFFL) ^ (long)signMask);
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
                c = (int)((fixDouble(k[m]) >>> shift & 0xFFL) ^ (long)signMask);
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
                        c = (int)((fixDouble(k[d]) >>> shift & 0xFFL) ^ (long)signMask);
                        p3 = layers;
                        while (p3-- != 0) {
                            final double u = t[p3];
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
    
    public static double[] shuffle(final double[] a, final int from, final int to, final Random random) {
        int i = to - from;
        while (i-- != 0) {
            final int p = random.nextInt(i + 1);
            final double t = a[from + i];
            a[from + i] = a[from + p];
            a[from + p] = t;
        }
        return a;
    }
    
    public static double[] shuffle(final double[] a, final Random random) {
        int i = a.length;
        while (i-- != 0) {
            final int p = random.nextInt(i + 1);
            final double t = a[i];
            a[i] = a[p];
            a[p] = t;
        }
        return a;
    }
    
    public static double[] reverse(final double[] a) {
        final int length = a.length;
        int i = length / 2;
        while (i-- != 0) {
            final double t = a[length - i - 1];
            a[length - i - 1] = a[i];
            a[i] = t;
        }
        return a;
    }
    
    public static double[] reverse(final double[] a, final int from, final int to) {
        final int length = to - from;
        int i = length / 2;
        while (i-- != 0) {
            final double t = a[from + length - i - 1];
            a[from + length - i - 1] = a[from + i];
            a[from + i] = t;
        }
        return a;
    }
    
    static {
        EMPTY_ARRAY = new double[0];
        POISON_PILL = new Segment(-1, -1, -1);
        HASH_STRATEGY = new ArrayHashStrategy();
    }
    
    protected static class ForkJoinQuickSortComp extends RecursiveAction
    {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final double[] x;
        private final DoubleComparator comp;
        
        public ForkJoinQuickSortComp(final double[] x, final int from, final int to, final DoubleComparator comp) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.comp = comp;
        }
        
        @Override
        protected void compute() {
            final double[] x = this.x;
            final int len = this.to - this.from;
            if (len < 8192) {
                DoubleArrays.quickSort(x, this.from, this.to, this.comp);
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
            final double v = x[m];
            int b;
            int a = b = this.from;
            int d;
            int c = d = this.to - 1;
            while (true) {
                int comparison;
                if (b <= c && (comparison = this.comp.compare(x[b], v)) <= 0) {
                    if (comparison == 0) {
                        DoubleArrays.swap(x, a++, b);
                    }
                    ++b;
                }
                else {
                    while (c >= b && (comparison = this.comp.compare(x[c], v)) >= 0) {
                        if (comparison == 0) {
                            DoubleArrays.swap(x, c, d--);
                        }
                        --c;
                    }
                    if (b > c) {
                        break;
                    }
                    DoubleArrays.swap(x, b++, c--);
                }
            }
            s = Math.min(a - this.from, b - a);
            DoubleArrays.swap(x, this.from, b - s, s);
            s = Math.min(d - c, this.to - d - 1);
            DoubleArrays.swap(x, b, this.to - s, s);
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
        private final double[] x;
        
        public ForkJoinQuickSort(final double[] x, final int from, final int to) {
            this.from = from;
            this.to = to;
            this.x = x;
        }
        
        @Override
        protected void compute() {
            final double[] x = this.x;
            final int len = this.to - this.from;
            if (len < 8192) {
                DoubleArrays.quickSort(x, this.from, this.to);
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
            final double v = x[m];
            int b;
            int a = b = this.from;
            int d;
            int c = d = this.to - 1;
            while (true) {
                int comparison;
                if (b <= c && (comparison = Double.compare(x[b], v)) <= 0) {
                    if (comparison == 0) {
                        DoubleArrays.swap(x, a++, b);
                    }
                    ++b;
                }
                else {
                    while (c >= b && (comparison = Double.compare(x[c], v)) >= 0) {
                        if (comparison == 0) {
                            DoubleArrays.swap(x, c, d--);
                        }
                        --c;
                    }
                    if (b > c) {
                        break;
                    }
                    DoubleArrays.swap(x, b++, c--);
                }
            }
            s = Math.min(a - this.from, b - a);
            DoubleArrays.swap(x, this.from, b - s, s);
            s = Math.min(d - c, this.to - d - 1);
            DoubleArrays.swap(x, b, this.to - s, s);
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
        private final double[] x;
        
        public ForkJoinQuickSortIndirect(final int[] perm, final double[] x, final int from, final int to) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.perm = perm;
        }
        
        @Override
        protected void compute() {
            final double[] x = this.x;
            final int len = this.to - this.from;
            if (len < 8192) {
                DoubleArrays.quickSortIndirect(this.perm, x, this.from, this.to);
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
            final double v = x[this.perm[m]];
            int b;
            int a = b = this.from;
            int d;
            int c = d = this.to - 1;
            while (true) {
                int comparison;
                if (b <= c && (comparison = Double.compare(x[this.perm[b]], v)) <= 0) {
                    if (comparison == 0) {
                        IntArrays.swap(this.perm, a++, b);
                    }
                    ++b;
                }
                else {
                    while (c >= b && (comparison = Double.compare(x[this.perm[c]], v)) >= 0) {
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
        private final double[] x;
        private final double[] y;
        
        public ForkJoinQuickSort2(final double[] x, final double[] y, final int from, final int to) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.y = y;
        }
        
        @Override
        protected void compute() {
            final double[] x = this.x;
            final double[] y = this.y;
            final int len = this.to - this.from;
            if (len < 8192) {
                DoubleArrays.quickSort(x, y, this.from, this.to);
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
            final double v = x[m];
            final double w = y[m];
            int b;
            int a = b = this.from;
            int d;
            int c = d = this.to - 1;
            while (true) {
                int t;
                int comparison;
                if (b <= c && (comparison = (((t = Double.compare(x[b], v)) == 0) ? Double.compare(y[b], w) : t)) <= 0) {
                    if (comparison == 0) {
                        swap(x, y, a++, b);
                    }
                    ++b;
                }
                else {
                    while (c >= b && (comparison = (((t = Double.compare(x[c], v)) == 0) ? Double.compare(y[c], w) : t)) >= 0) {
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
    
    private static final class ArrayHashStrategy implements Hash.Strategy<double[]>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        @Override
        public int hashCode(final double[] o) {
            return java.util.Arrays.hashCode(o);
        }
        
        @Override
        public boolean equals(final double[] a, final double[] b) {
            return java.util.Arrays.equals(a, b);
        }
    }
}
