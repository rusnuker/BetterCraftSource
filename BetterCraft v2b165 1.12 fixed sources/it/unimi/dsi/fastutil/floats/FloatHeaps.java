// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

public class FloatHeaps
{
    private FloatHeaps() {
    }
    
    public static int downHeap(final float[] heap, final int size, int i, final FloatComparator c) {
        assert i < size;
        final float e = heap[i];
        if (c == null) {
            int child;
            while ((child = (i << 1) + 1) < size) {
                float t = heap[child];
                final int right = child + 1;
                if (right < size && Float.compare(heap[right], t) < 0) {
                    t = heap[child = right];
                }
                if (Float.compare(e, t) <= 0) {
                    break;
                }
                heap[i] = t;
                i = child;
            }
        }
        else {
            int child;
            while ((child = (i << 1) + 1) < size) {
                float t = heap[child];
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
    
    public static int upHeap(final float[] heap, final int size, int i, final FloatComparator c) {
        assert i < size;
        final float e = heap[i];
        if (c == null) {
            while (i != 0) {
                final int parent = i - 1 >>> 1;
                final float t = heap[parent];
                if (Float.compare(t, e) <= 0) {
                    break;
                }
                heap[i] = t;
                i = parent;
            }
        }
        else {
            while (i != 0) {
                final int parent = i - 1 >>> 1;
                final float t = heap[parent];
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
    
    public static void makeHeap(final float[] heap, final int size, final FloatComparator c) {
        int i = size >>> 1;
        while (i-- != 0) {
            downHeap(heap, size, i, c);
        }
    }
}
