// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

public class DoubleHeaps
{
    private DoubleHeaps() {
    }
    
    public static int downHeap(final double[] heap, final int size, int i, final DoubleComparator c) {
        assert i < size;
        final double e = heap[i];
        if (c == null) {
            int child;
            while ((child = (i << 1) + 1) < size) {
                double t = heap[child];
                final int right = child + 1;
                if (right < size && Double.compare(heap[right], t) < 0) {
                    t = heap[child = right];
                }
                if (Double.compare(e, t) <= 0) {
                    break;
                }
                heap[i] = t;
                i = child;
            }
        }
        else {
            int child;
            while ((child = (i << 1) + 1) < size) {
                double t = heap[child];
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
    
    public static int upHeap(final double[] heap, final int size, int i, final DoubleComparator c) {
        assert i < size;
        final double e = heap[i];
        if (c == null) {
            while (i != 0) {
                final int parent = i - 1 >>> 1;
                final double t = heap[parent];
                if (Double.compare(t, e) <= 0) {
                    break;
                }
                heap[i] = t;
                i = parent;
            }
        }
        else {
            while (i != 0) {
                final int parent = i - 1 >>> 1;
                final double t = heap[parent];
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
    
    public static void makeHeap(final double[] heap, final int size, final DoubleComparator c) {
        int i = size >>> 1;
        while (i-- != 0) {
            downHeap(heap, size, i, c);
        }
    }
}
