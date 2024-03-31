// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Comparator;

public class ObjectHeaps
{
    private ObjectHeaps() {
    }
    
    public static <K> int downHeap(final K[] heap, final int size, int i, final Comparator<? super K> c) {
        assert i < size;
        final K e = heap[i];
        if (c == null) {
            int child;
            while ((child = (i << 1) + 1) < size) {
                K t = heap[child];
                final int right = child + 1;
                if (right < size && ((Comparable)heap[right]).compareTo(t) < 0) {
                    t = heap[child = right];
                }
                if (((Comparable)e).compareTo(t) <= 0) {
                    break;
                }
                heap[i] = t;
                i = child;
            }
        }
        else {
            int child;
            while ((child = (i << 1) + 1) < size) {
                K t = heap[child];
                final int right = child + 1;
                if (right < size && c.compare((Object)heap[right], (Object)t) < 0) {
                    t = heap[child = right];
                }
                if (c.compare((Object)e, (Object)t) <= 0) {
                    break;
                }
                heap[i] = t;
                i = child;
            }
        }
        heap[i] = e;
        return i;
    }
    
    public static <K> int upHeap(final K[] heap, final int size, int i, final Comparator<K> c) {
        assert i < size;
        final K e = heap[i];
        if (c == null) {
            while (i != 0) {
                final int parent = i - 1 >>> 1;
                final K t = heap[parent];
                if (((Comparable)t).compareTo(e) <= 0) {
                    break;
                }
                heap[i] = t;
                i = parent;
            }
        }
        else {
            while (i != 0) {
                final int parent = i - 1 >>> 1;
                final K t = heap[parent];
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
    
    public static <K> void makeHeap(final K[] heap, final int size, final Comparator<K> c) {
        int i = size >>> 1;
        while (i-- != 0) {
            downHeap(heap, size, i, c);
        }
    }
}
