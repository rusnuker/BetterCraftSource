// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.util.Arrays;

public class CharIndirectHeaps
{
    private CharIndirectHeaps() {
    }
    
    public static int downHeap(final char[] refArray, final int[] heap, final int[] inv, final int size, int i, final CharComparator c) {
        assert i < size;
        final int e = heap[i];
        final char E = refArray[e];
        if (c == null) {
            int child;
            while ((child = (i << 1) + 1) < size) {
                int t = heap[child];
                final int right = child + 1;
                if (right < size && refArray[heap[right]] < refArray[t]) {
                    t = heap[child = right];
                }
                if (E <= refArray[t]) {
                    break;
                }
                inv[heap[i] = t] = i;
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
                inv[heap[i] = t] = i;
                i = child;
            }
        }
        heap[i] = e;
        return inv[e] = i;
    }
    
    public static int upHeap(final char[] refArray, final int[] heap, final int[] inv, final int size, int i, final CharComparator c) {
        assert i < size;
        final int e = heap[i];
        final char E = refArray[e];
        if (c == null) {
            while (i != 0) {
                final int parent = i - 1 >>> 1;
                final int t = heap[parent];
                if (refArray[t] <= E) {
                    break;
                }
                inv[heap[i] = t] = i;
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
                inv[heap[i] = t] = i;
                i = parent;
            }
        }
        heap[i] = e;
        return inv[e] = i;
    }
    
    public static void makeHeap(final char[] refArray, final int offset, final int length, final int[] heap, final int[] inv, final CharComparator c) {
        CharArrays.ensureOffsetLength(refArray, offset, length);
        if (heap.length < length) {
            throw new IllegalArgumentException("The heap length (" + heap.length + ") is smaller than the number of elements (" + length + ")");
        }
        if (inv.length < refArray.length) {
            throw new IllegalArgumentException("The inversion array length (" + heap.length + ") is smaller than the length of the reference array (" + refArray.length + ")");
        }
        Arrays.fill(inv, 0, refArray.length, -1);
        int i = length;
        while (i-- != 0) {
            inv[heap[i] = offset + i] = i;
        }
        i = length >>> 1;
        while (i-- != 0) {
            downHeap(refArray, heap, inv, length, i, c);
        }
    }
    
    public static void makeHeap(final char[] refArray, final int[] heap, final int[] inv, final int size, final CharComparator c) {
        int i = size >>> 1;
        while (i-- != 0) {
            downHeap(refArray, heap, inv, size, i, c);
        }
    }
}
