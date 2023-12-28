// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

public class ShortHeaps
{
    private ShortHeaps() {
    }
    
    public static int downHeap(final short[] heap, final int size, int i, final ShortComparator c) {
        assert i < size;
        final short e = heap[i];
        if (c == null) {
            int child;
            while ((child = (i << 1) + 1) < size) {
                short t = heap[child];
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
                short t = heap[child];
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
    
    public static int upHeap(final short[] heap, final int size, int i, final ShortComparator c) {
        assert i < size;
        final short e = heap[i];
        if (c == null) {
            while (i != 0) {
                final int parent = i - 1 >>> 1;
                final short t = heap[parent];
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
                final short t = heap[parent];
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
    
    public static void makeHeap(final short[] heap, final int size, final ShortComparator c) {
        int i = size >>> 1;
        while (i-- != 0) {
            downHeap(heap, size, i, c);
        }
    }
}
