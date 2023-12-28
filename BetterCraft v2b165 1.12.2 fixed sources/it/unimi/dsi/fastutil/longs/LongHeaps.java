// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

public class LongHeaps
{
    private LongHeaps() {
    }
    
    public static int downHeap(final long[] heap, final int size, int i, final LongComparator c) {
        assert i < size;
        final long e = heap[i];
        if (c == null) {
            int child;
            while ((child = (i << 1) + 1) < size) {
                long t = heap[child];
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
                long t = heap[child];
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
    
    public static int upHeap(final long[] heap, final int size, int i, final LongComparator c) {
        assert i < size;
        final long e = heap[i];
        if (c == null) {
            while (i != 0) {
                final int parent = i - 1 >>> 1;
                final long t = heap[parent];
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
                final long t = heap[parent];
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
    
    public static void makeHeap(final long[] heap, final int size, final LongComparator c) {
        int i = size >>> 1;
        while (i-- != 0) {
            downHeap(heap, size, i, c);
        }
    }
}
