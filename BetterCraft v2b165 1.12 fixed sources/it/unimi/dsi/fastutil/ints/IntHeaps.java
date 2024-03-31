// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

public class IntHeaps
{
    private IntHeaps() {
    }
    
    public static int downHeap(final int[] heap, final int size, int i, final IntComparator c) {
        assert i < size;
        final int e = heap[i];
        if (c == null) {
            int child;
            while ((child = (i << 1) + 1) < size) {
                int t = heap[child];
                final int right = child + 1;
                if (right < size && heap[right] < t) {
                    t = heap[child = right];
                }
                if (e <= t) {
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
                if (right < size && c.compare(heap[right], t) < 0) {
                    t = heap[child = right];
                }
                if (c.compare(e, t) <= 0) {
                    break;
                }
                heap[i] = t;
                i = child;
            }
        }
        heap[i] = e;
        return i;
    }
    
    public static int upHeap(final int[] heap, final int size, int i, final IntComparator c) {
        assert i < size;
        final int e = heap[i];
        if (c == null) {
            while (i != 0) {
                final int parent = i - 1 >>> 1;
                final int t = heap[parent];
                if (t <= e) {
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
                if (c.compare(t, e) <= 0) {
                    break;
                }
                heap[i] = t;
                i = parent;
            }
        }
        heap[i] = e;
        return i;
    }
    
    public static void makeHeap(final int[] heap, final int size, final IntComparator c) {
        int i = size >>> 1;
        while (i-- != 0) {
            downHeap(heap, size, i, c);
        }
    }
}
