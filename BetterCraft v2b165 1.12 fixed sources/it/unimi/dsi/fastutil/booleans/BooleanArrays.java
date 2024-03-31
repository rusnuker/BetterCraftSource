// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

import java.io.Serializable;
import java.util.concurrent.RecursiveAction;
import java.util.Random;
import it.unimi.dsi.fastutil.ints.IntArrays;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ForkJoinPool;
import it.unimi.dsi.fastutil.Arrays;
import it.unimi.dsi.fastutil.Hash;

public class BooleanArrays
{
    public static final boolean[] EMPTY_ARRAY;
    private static final int QUICKSORT_NO_REC = 16;
    private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
    private static final int QUICKSORT_MEDIAN_OF_9 = 128;
    private static final int MERGESORT_NO_REC = 16;
    public static final Hash.Strategy<boolean[]> HASH_STRATEGY;
    
    private BooleanArrays() {
    }
    
    public static boolean[] ensureCapacity(final boolean[] array, final int length) {
        if (length > array.length) {
            final boolean[] t = new boolean[length];
            System.arraycopy(array, 0, t, 0, array.length);
            return t;
        }
        return array;
    }
    
    public static boolean[] ensureCapacity(final boolean[] array, final int length, final int preserve) {
        if (length > array.length) {
            final boolean[] t = new boolean[length];
            System.arraycopy(array, 0, t, 0, preserve);
            return t;
        }
        return array;
    }
    
    public static boolean[] grow(final boolean[] array, final int length) {
        if (length > array.length) {
            final int newLength = (int)Math.max(Math.min(2L * array.length, 2147483639L), length);
            final boolean[] t = new boolean[newLength];
            System.arraycopy(array, 0, t, 0, array.length);
            return t;
        }
        return array;
    }
    
    public static boolean[] grow(final boolean[] array, final int length, final int preserve) {
        if (length > array.length) {
            final int newLength = (int)Math.max(Math.min(2L * array.length, 2147483639L), length);
            final boolean[] t = new boolean[newLength];
            System.arraycopy(array, 0, t, 0, preserve);
            return t;
        }
        return array;
    }
    
    public static boolean[] trim(final boolean[] array, final int length) {
        if (length >= array.length) {
            return array;
        }
        final boolean[] t = (length == 0) ? BooleanArrays.EMPTY_ARRAY : new boolean[length];
        System.arraycopy(array, 0, t, 0, length);
        return t;
    }
    
    public static boolean[] setLength(final boolean[] array, final int length) {
        if (length == array.length) {
            return array;
        }
        if (length < array.length) {
            return trim(array, length);
        }
        return ensureCapacity(array, length);
    }
    
    public static boolean[] copy(final boolean[] array, final int offset, final int length) {
        ensureOffsetLength(array, offset, length);
        final boolean[] a = (length == 0) ? BooleanArrays.EMPTY_ARRAY : new boolean[length];
        System.arraycopy(array, offset, a, 0, length);
        return a;
    }
    
    public static boolean[] copy(final boolean[] array) {
        return array.clone();
    }
    
    @Deprecated
    public static void fill(final boolean[] array, final boolean value) {
        int i = array.length;
        while (i-- != 0) {
            array[i] = value;
        }
    }
    
    @Deprecated
    public static void fill(final boolean[] array, final int from, int to, final boolean value) {
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
    public static boolean equals(final boolean[] a1, final boolean[] a2) {
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
    
    public static void ensureFromTo(final boolean[] a, final int from, final int to) {
        Arrays.ensureFromTo(a.length, from, to);
    }
    
    public static void ensureOffsetLength(final boolean[] a, final int offset, final int length) {
        Arrays.ensureOffsetLength(a.length, offset, length);
    }
    
    public static void ensureSameLength(final boolean[] a, final boolean[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Array size mismatch: " + a.length + " != " + b.length);
        }
    }
    
    public static void swap(final boolean[] x, final int a, final int b) {
        final boolean t = x[a];
        x[a] = x[b];
        x[b] = t;
    }
    
    public static void swap(final boolean[] x, int a, int b, final int n) {
        for (int i = 0; i < n; ++i, ++a, ++b) {
            swap(x, a, b);
        }
    }
    
    private static int med3(final boolean[] x, final int a, final int b, final int c, final BooleanComparator comp) {
        final int ab = comp.compare(x[a], x[b]);
        final int ac = comp.compare(x[a], x[c]);
        final int bc = comp.compare(x[b], x[c]);
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static void selectionSort(final boolean[] a, final int from, final int to, final BooleanComparator comp) {
        for (int i = from; i < to - 1; ++i) {
            int m = i;
            for (int j = i + 1; j < to; ++j) {
                if (comp.compare(a[j], a[m]) < 0) {
                    m = j;
                }
            }
            if (m != i) {
                final boolean u = a[i];
                a[i] = a[m];
                a[m] = u;
            }
        }
    }
    
    private static void insertionSort(final boolean[] a, final int from, final int to, final BooleanComparator comp) {
        int i = from;
        while (++i < to) {
            final boolean t = a[i];
            int j = i;
            for (boolean u = a[j - 1]; comp.compare(t, u) < 0; u = a[--j - 1]) {
                a[j] = u;
                if (from == j - 1) {
                    --j;
                    break;
                }
            }
            a[j] = t;
        }
    }
    
    public static void quickSort(final boolean[] x, final int from, final int to, final BooleanComparator comp) {
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
        final boolean v = x[m];
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
    
    public static void quickSort(final boolean[] x, final BooleanComparator comp) {
        quickSort(x, 0, x.length, comp);
    }
    
    public static void parallelQuickSort(final boolean[] x, final int from, final int to, final BooleanComparator comp) {
        if (to - from < 8192) {
            quickSort(x, from, to, comp);
        }
        else {
            final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
            pool.invoke((ForkJoinTask<Object>)new ForkJoinQuickSortComp(x, from, to, comp));
            pool.shutdown();
        }
    }
    
    public static void parallelQuickSort(final boolean[] x, final BooleanComparator comp) {
        parallelQuickSort(x, 0, x.length, comp);
    }
    
    private static int med3(final boolean[] x, final int a, final int b, final int c) {
        final int ab = Boolean.compare(x[a], x[b]);
        final int ac = Boolean.compare(x[a], x[c]);
        final int bc = Boolean.compare(x[b], x[c]);
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static void selectionSort(final boolean[] a, final int from, final int to) {
        for (int i = from; i < to - 1; ++i) {
            int m = i;
            for (int j = i + 1; j < to; ++j) {
                if (!a[j] && a[m]) {
                    m = j;
                }
            }
            if (m != i) {
                final boolean u = a[i];
                a[i] = a[m];
                a[m] = u;
            }
        }
    }
    
    private static void insertionSort(final boolean[] a, final int from, final int to) {
        int i = from;
        while (++i < to) {
            final boolean t = a[i];
            int j = i;
            for (boolean u = a[j - 1]; !t && u; u = a[--j - 1]) {
                a[j] = u;
                if (from == j - 1) {
                    --j;
                    break;
                }
            }
            a[j] = t;
        }
    }
    
    public static void quickSort(final boolean[] x, final int from, final int to) {
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
        final boolean v = x[m];
        int b;
        int a = b = from;
        int d;
        int c = d = to - 1;
        while (true) {
            int comparison;
            if (b <= c && (comparison = Boolean.compare(x[b], v)) <= 0) {
                if (comparison == 0) {
                    swap(x, a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = Boolean.compare(x[c], v)) >= 0) {
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
    
    public static void quickSort(final boolean[] x) {
        quickSort(x, 0, x.length);
    }
    
    public static void parallelQuickSort(final boolean[] x, final int from, final int to) {
        if (to - from < 8192) {
            quickSort(x, from, to);
        }
        else {
            final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
            pool.invoke((ForkJoinTask<Object>)new ForkJoinQuickSort(x, from, to));
            pool.shutdown();
        }
    }
    
    public static void parallelQuickSort(final boolean[] x) {
        parallelQuickSort(x, 0, x.length);
    }
    
    private static int med3Indirect(final int[] perm, final boolean[] x, final int a, final int b, final int c) {
        final boolean aa = x[perm[a]];
        final boolean bb = x[perm[b]];
        final boolean cc = x[perm[c]];
        final int ab = Boolean.compare(aa, bb);
        final int ac = Boolean.compare(aa, cc);
        final int bc = Boolean.compare(bb, cc);
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static void insertionSortIndirect(final int[] perm, final boolean[] a, final int from, final int to) {
        int i = from;
        while (++i < to) {
            final int t = perm[i];
            int j = i;
            for (int u = perm[j - 1]; !a[t] && a[u]; u = perm[--j - 1]) {
                perm[j] = u;
                if (from == j - 1) {
                    --j;
                    break;
                }
            }
            perm[j] = t;
        }
    }
    
    public static void quickSortIndirect(final int[] perm, final boolean[] x, final int from, final int to) {
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
        final boolean v = x[perm[m]];
        int b;
        int a = b = from;
        int d;
        int c = d = to - 1;
        while (true) {
            int comparison;
            if (b <= c && (comparison = Boolean.compare(x[perm[b]], v)) <= 0) {
                if (comparison == 0) {
                    IntArrays.swap(perm, a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = Boolean.compare(x[perm[c]], v)) >= 0) {
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
    
    public static void quickSortIndirect(final int[] perm, final boolean[] x) {
        quickSortIndirect(perm, x, 0, x.length);
    }
    
    public static void parallelQuickSortIndirect(final int[] perm, final boolean[] x, final int from, final int to) {
        if (to - from < 8192) {
            quickSortIndirect(perm, x, from, to);
        }
        else {
            final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
            pool.invoke((ForkJoinTask<Object>)new ForkJoinQuickSortIndirect(perm, x, from, to));
            pool.shutdown();
        }
    }
    
    public static void parallelQuickSortIndirect(final int[] perm, final boolean[] x) {
        parallelQuickSortIndirect(perm, x, 0, x.length);
    }
    
    public static void stabilize(final int[] perm, final boolean[] x, final int from, final int to) {
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
    
    public static void stabilize(final int[] perm, final boolean[] x) {
        stabilize(perm, x, 0, perm.length);
    }
    
    private static int med3(final boolean[] x, final boolean[] y, final int a, final int b, final int c) {
        int t;
        final int ab = ((t = Boolean.compare(x[a], x[b])) == 0) ? Boolean.compare(y[a], y[b]) : t;
        final int ac = ((t = Boolean.compare(x[a], x[c])) == 0) ? Boolean.compare(y[a], y[c]) : t;
        final int bc = ((t = Boolean.compare(x[b], x[c])) == 0) ? Boolean.compare(y[b], y[c]) : t;
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static void swap(final boolean[] x, final boolean[] y, final int a, final int b) {
        final boolean t = x[a];
        final boolean u = y[a];
        x[a] = x[b];
        y[a] = y[b];
        x[b] = t;
        y[b] = u;
    }
    
    private static void swap(final boolean[] x, final boolean[] y, int a, int b, final int n) {
        for (int i = 0; i < n; ++i, ++a, ++b) {
            swap(x, y, a, b);
        }
    }
    
    private static void selectionSort(final boolean[] a, final boolean[] b, final int from, final int to) {
        for (int i = from; i < to - 1; ++i) {
            int m = i;
            for (int j = i + 1; j < to; ++j) {
                final int u;
                if ((u = Boolean.compare(a[j], a[m])) < 0 || (u == 0 && !b[j] && b[m])) {
                    m = j;
                }
            }
            if (m != i) {
                boolean t = a[i];
                a[i] = a[m];
                a[m] = t;
                t = b[i];
                b[i] = b[m];
                b[m] = t;
            }
        }
    }
    
    public static void quickSort(final boolean[] x, final boolean[] y, final int from, final int to) {
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
        final boolean v = x[m];
        final boolean w = y[m];
        int b;
        int a = b = from;
        int d;
        int c = d = to - 1;
        while (true) {
            int t;
            int comparison;
            if (b <= c && (comparison = (((t = Boolean.compare(x[b], v)) == 0) ? Boolean.compare(y[b], w) : t)) <= 0) {
                if (comparison == 0) {
                    swap(x, y, a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = (((t = Boolean.compare(x[c], v)) == 0) ? Boolean.compare(y[c], w) : t)) >= 0) {
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
    
    public static void quickSort(final boolean[] x, final boolean[] y) {
        ensureSameLength(x, y);
        quickSort(x, y, 0, x.length);
    }
    
    public static void parallelQuickSort(final boolean[] x, final boolean[] y, final int from, final int to) {
        if (to - from < 8192) {
            quickSort(x, y, from, to);
        }
        final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        pool.invoke((ForkJoinTask<Object>)new ForkJoinQuickSort2(x, y, from, to));
        pool.shutdown();
    }
    
    public static void parallelQuickSort(final boolean[] x, final boolean[] y) {
        ensureSameLength(x, y);
        parallelQuickSort(x, y, 0, x.length);
    }
    
    public static void mergeSort(final boolean[] a, final int from, final int to, final boolean[] supp) {
        final int len = to - from;
        if (len < 16) {
            insertionSort(a, from, to);
            return;
        }
        final int mid = from + to >>> 1;
        mergeSort(supp, from, mid, a);
        mergeSort(supp, mid, to, a);
        if (!supp[mid - 1] || supp[mid]) {
            System.arraycopy(supp, from, a, from, len);
            return;
        }
        int i = from;
        int p = from;
        int q = mid;
        while (i < to) {
            if (q >= to || (p < mid && (!supp[p] || supp[q]))) {
                a[i] = supp[p++];
            }
            else {
                a[i] = supp[q++];
            }
            ++i;
        }
    }
    
    public static void mergeSort(final boolean[] a, final int from, final int to) {
        mergeSort(a, from, to, a.clone());
    }
    
    public static void mergeSort(final boolean[] a) {
        mergeSort(a, 0, a.length);
    }
    
    public static void mergeSort(final boolean[] a, final int from, final int to, final BooleanComparator comp, final boolean[] supp) {
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
    
    public static void mergeSort(final boolean[] a, final int from, final int to, final BooleanComparator comp) {
        mergeSort(a, from, to, comp, a.clone());
    }
    
    public static void mergeSort(final boolean[] a, final BooleanComparator comp) {
        mergeSort(a, 0, a.length, comp);
    }
    
    public static boolean[] shuffle(final boolean[] a, final int from, final int to, final Random random) {
        int i = to - from;
        while (i-- != 0) {
            final int p = random.nextInt(i + 1);
            final boolean t = a[from + i];
            a[from + i] = a[from + p];
            a[from + p] = t;
        }
        return a;
    }
    
    public static boolean[] shuffle(final boolean[] a, final Random random) {
        int i = a.length;
        while (i-- != 0) {
            final int p = random.nextInt(i + 1);
            final boolean t = a[i];
            a[i] = a[p];
            a[p] = t;
        }
        return a;
    }
    
    public static boolean[] reverse(final boolean[] a) {
        final int length = a.length;
        int i = length / 2;
        while (i-- != 0) {
            final boolean t = a[length - i - 1];
            a[length - i - 1] = a[i];
            a[i] = t;
        }
        return a;
    }
    
    public static boolean[] reverse(final boolean[] a, final int from, final int to) {
        final int length = to - from;
        int i = length / 2;
        while (i-- != 0) {
            final boolean t = a[from + length - i - 1];
            a[from + length - i - 1] = a[from + i];
            a[from + i] = t;
        }
        return a;
    }
    
    static {
        EMPTY_ARRAY = new boolean[0];
        HASH_STRATEGY = new ArrayHashStrategy();
    }
    
    protected static class ForkJoinQuickSortComp extends RecursiveAction
    {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final boolean[] x;
        private final BooleanComparator comp;
        
        public ForkJoinQuickSortComp(final boolean[] x, final int from, final int to, final BooleanComparator comp) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.comp = comp;
        }
        
        @Override
        protected void compute() {
            final boolean[] x = this.x;
            final int len = this.to - this.from;
            if (len < 8192) {
                BooleanArrays.quickSort(x, this.from, this.to, this.comp);
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
            final boolean v = x[m];
            int b;
            int a = b = this.from;
            int d;
            int c = d = this.to - 1;
            while (true) {
                int comparison;
                if (b <= c && (comparison = this.comp.compare(x[b], v)) <= 0) {
                    if (comparison == 0) {
                        BooleanArrays.swap(x, a++, b);
                    }
                    ++b;
                }
                else {
                    while (c >= b && (comparison = this.comp.compare(x[c], v)) >= 0) {
                        if (comparison == 0) {
                            BooleanArrays.swap(x, c, d--);
                        }
                        --c;
                    }
                    if (b > c) {
                        break;
                    }
                    BooleanArrays.swap(x, b++, c--);
                }
            }
            s = Math.min(a - this.from, b - a);
            BooleanArrays.swap(x, this.from, b - s, s);
            s = Math.min(d - c, this.to - d - 1);
            BooleanArrays.swap(x, b, this.to - s, s);
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
        private final boolean[] x;
        
        public ForkJoinQuickSort(final boolean[] x, final int from, final int to) {
            this.from = from;
            this.to = to;
            this.x = x;
        }
        
        @Override
        protected void compute() {
            final boolean[] x = this.x;
            final int len = this.to - this.from;
            if (len < 8192) {
                BooleanArrays.quickSort(x, this.from, this.to);
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
            final boolean v = x[m];
            int b;
            int a = b = this.from;
            int d;
            int c = d = this.to - 1;
            while (true) {
                int comparison;
                if (b <= c && (comparison = Boolean.compare(x[b], v)) <= 0) {
                    if (comparison == 0) {
                        BooleanArrays.swap(x, a++, b);
                    }
                    ++b;
                }
                else {
                    while (c >= b && (comparison = Boolean.compare(x[c], v)) >= 0) {
                        if (comparison == 0) {
                            BooleanArrays.swap(x, c, d--);
                        }
                        --c;
                    }
                    if (b > c) {
                        break;
                    }
                    BooleanArrays.swap(x, b++, c--);
                }
            }
            s = Math.min(a - this.from, b - a);
            BooleanArrays.swap(x, this.from, b - s, s);
            s = Math.min(d - c, this.to - d - 1);
            BooleanArrays.swap(x, b, this.to - s, s);
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
        private final boolean[] x;
        
        public ForkJoinQuickSortIndirect(final int[] perm, final boolean[] x, final int from, final int to) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.perm = perm;
        }
        
        @Override
        protected void compute() {
            final boolean[] x = this.x;
            final int len = this.to - this.from;
            if (len < 8192) {
                BooleanArrays.quickSortIndirect(this.perm, x, this.from, this.to);
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
            final boolean v = x[this.perm[m]];
            int b;
            int a = b = this.from;
            int d;
            int c = d = this.to - 1;
            while (true) {
                int comparison;
                if (b <= c && (comparison = Boolean.compare(x[this.perm[b]], v)) <= 0) {
                    if (comparison == 0) {
                        IntArrays.swap(this.perm, a++, b);
                    }
                    ++b;
                }
                else {
                    while (c >= b && (comparison = Boolean.compare(x[this.perm[c]], v)) >= 0) {
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
        private final boolean[] x;
        private final boolean[] y;
        
        public ForkJoinQuickSort2(final boolean[] x, final boolean[] y, final int from, final int to) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.y = y;
        }
        
        @Override
        protected void compute() {
            final boolean[] x = this.x;
            final boolean[] y = this.y;
            final int len = this.to - this.from;
            if (len < 8192) {
                BooleanArrays.quickSort(x, y, this.from, this.to);
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
            final boolean v = x[m];
            final boolean w = y[m];
            int b;
            int a = b = this.from;
            int d;
            int c = d = this.to - 1;
            while (true) {
                int t;
                int comparison;
                if (b <= c && (comparison = (((t = Boolean.compare(x[b], v)) == 0) ? Boolean.compare(y[b], w) : t)) <= 0) {
                    if (comparison == 0) {
                        swap(x, y, a++, b);
                    }
                    ++b;
                }
                else {
                    while (c >= b && (comparison = (((t = Boolean.compare(x[c], v)) == 0) ? Boolean.compare(y[c], w) : t)) >= 0) {
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
    
    private static final class ArrayHashStrategy implements Hash.Strategy<boolean[]>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        @Override
        public int hashCode(final boolean[] o) {
            return java.util.Arrays.hashCode(o);
        }
        
        @Override
        public boolean equals(final boolean[] a, final boolean[] b) {
            return java.util.Arrays.equals(a, b);
        }
    }
}
