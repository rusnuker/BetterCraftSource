// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.ints.IntArrays;
import java.util.Comparator;

public class ObjectSemiIndirectHeaps
{
    private ObjectSemiIndirectHeaps() {
    }
    
    public static <K> int downHeap(final K[] refArray, final int[] heap, final int size, int i, final Comparator<K> c) {
        assert i < size;
        final int e = heap[i];
        final K E = refArray[e];
        if (c == null) {
            int child;
            while ((child = (i << 1) + 1) < size) {
                int t = heap[child];
                final int right = child + 1;
                if (right < size && ((Comparable)refArray[heap[right]]).compareTo(refArray[t]) < 0) {
                    t = heap[child = right];
                }
                if (((Comparable)E).compareTo(refArray[t]) <= 0) {
                    break;
                }
                heap[i] = t;
                i = child;
            }
        }
        else {
            int child;
            while ((child = (i << 1) + 1) < size) {
                int t = heap[child];
                final int right = child + 1;
                if (right < size && c.compare(refArray[heap[right]], refArray[t]) < 0) {
                    t = heap[child = right];
                }
                if (c.compare(E, refArray[t]) <= 0) {
                    break;
                }
                heap[i] = t;
                i = child;
            }
        }
        heap[i] = e;
        return i;
    }
    
    public static <K> int upHeap(final K[] refArray, final int[] heap, final int size, int i, final Comparator<K> c) {
        assert i < size;
        final int e = heap[i];
        final K E = refArray[e];
        if (c == null) {
            while (i != 0) {
                final int parent = i - 1 >>> 1;
                final int t = heap[parent];
                if (((Comparable)refArray[t]).compareTo(E) <= 0) {
                    break;
                }
                heap[i] = t;
                i = parent;
            }
        }
        else {
            while (i != 0) {
                final int parent = i - 1 >>> 1;
                final int t = heap[parent];
                if (c.compare(refArray[t], E) <= 0) {
                    break;
                }
                heap[i] = t;
                i = parent;
            }
        }
        heap[i] = e;
        return i;
    }
    
    public static <K> void makeHeap(final K[] refArray, final int offset, final int length, final int[] heap, final Comparator<K> c) {
        ObjectArrays.ensureOffsetLength(refArray, offset, length);
        if (heap.length < length) {
            throw new IllegalArgumentException("The heap length (" + heap.length + ") is smaller than the number of elements (" + length + ")");
        }
        int i = length;
        while (i-- != 0) {
            heap[i] = offset + i;
        }
        i = length >>> 1;
        while (i-- != 0) {
            downHeap(refArray, heap, length, i, c);
        }
    }
    
    public static <K> int[] makeHeap(final K[] refArray, final int offset, final int length, final Comparator<K> c) {
        final int[] heap = (length <= 0) ? IntArrays.EMPTY_ARRAY : new int[length];
        makeHeap(refArray, offset, length, heap, c);
        return heap;
    }
    
    public static <K> void makeHeap(final K[] refArray, final int[] heap, final int size, final Comparator<K> c) {
        int i = size >>> 1;
        while (i-- != 0) {
            downHeap(refArray, heap, size, i, c);
        }
    }
    
    public static <K> int front(final K[] refArray, final int[] heap, final int size, final int[] a) {
        final K top = refArray[heap[0]];
        int j = 0;
        int l = 0;
        int r = 1;
        int f = 0;
        for (int i = 0; i < r; ++i) {
            if (i == f) {
                if (l >= r) {
                    break;
                }
                f = (f << 1) + 1;
                i = l;
                l = -1;
            }
            if (((Comparable)top).compareTo(refArray[heap[i]]) == 0) {
                a[j++] = heap[i];
                if (l == -1) {
                    l = i * 2 + 1;
                }
                r = Math.min(size, i * 2 + 3);
            }
        }
        return j;
    }
    
    public static <K> int front(final K[] refArray, final int[] heap, final int size, final int[] a, final Comparator<K> c) {
        final K top = refArray[heap[0]];
        int j = 0;
        int l = 0;
        int r = 1;
        int f = 0;
        for (int i = 0; i < r; ++i) {
            if (i == f) {
                if (l >= r) {
                    break;
                }
                f = (f << 1) + 1;
                i = l;
                l = -1;
            }
            if (c.compare(top, refArray[heap[i]]) == 0) {
                a[j++] = heap[i];
                if (l == -1) {
                    l = i * 2 + 1;
                }
                r = Math.min(size, i * 2 + 3);
            }
        }
        return j;
    }
}
