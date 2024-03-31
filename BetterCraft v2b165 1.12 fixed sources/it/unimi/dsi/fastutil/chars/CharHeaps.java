// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

public class CharHeaps
{
    private CharHeaps() {
    }
    
    public static int downHeap(final char[] heap, final int size, int i, final CharComparator c) {
        assert i < size;
        final char e = heap[i];
        if (c == null) {
            int child;
            while ((child = (i << 1) + 1) < size) {
                char t = heap[child];
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
                char t = heap[child];
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
    
    public static int upHeap(final char[] heap, final int size, int i, final CharComparator c) {
        assert i < size;
        final char e = heap[i];
        if (c == null) {
            while (i != 0) {
                final int parent = i - 1 >>> 1;
                final char t = heap[parent];
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
                final char t = heap[parent];
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
    
    public static void makeHeap(final char[] heap, final int size, final CharComparator c) {
        int i = size >>> 1;
        while (i-- != 0) {
            downHeap(heap, size, i, c);
        }
    }
}
