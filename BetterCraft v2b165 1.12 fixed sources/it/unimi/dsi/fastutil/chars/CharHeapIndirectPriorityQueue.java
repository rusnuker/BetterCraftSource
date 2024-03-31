// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.util.NoSuchElementException;
import it.unimi.dsi.fastutil.ints.IntArrays;
import java.util.Arrays;

public class CharHeapIndirectPriorityQueue extends CharHeapSemiIndirectPriorityQueue
{
    protected final int[] inv;
    
    public CharHeapIndirectPriorityQueue(final char[] refArray, final int capacity, final CharComparator c) {
        super(refArray, capacity, c);
        if (capacity > 0) {
            this.heap = new int[capacity];
        }
        this.c = c;
        Arrays.fill(this.inv = new int[refArray.length], -1);
    }
    
    public CharHeapIndirectPriorityQueue(final char[] refArray, final int capacity) {
        this(refArray, capacity, null);
    }
    
    public CharHeapIndirectPriorityQueue(final char[] refArray, final CharComparator c) {
        this(refArray, refArray.length, c);
    }
    
    public CharHeapIndirectPriorityQueue(final char[] refArray) {
        this(refArray, refArray.length, null);
    }
    
    public CharHeapIndirectPriorityQueue(final char[] refArray, final int[] a, final int size, final CharComparator c) {
        this(refArray, 0, c);
        this.heap = a;
        this.size = size;
        int i = size;
        while (i-- != 0) {
            if (this.inv[a[i]] != -1) {
                throw new IllegalArgumentException("Index " + a[i] + " appears twice in the heap");
            }
            this.inv[a[i]] = i;
        }
        CharIndirectHeaps.makeHeap(refArray, a, this.inv, size, c);
    }
    
    public CharHeapIndirectPriorityQueue(final char[] refArray, final int[] a, final CharComparator c) {
        this(refArray, a, a.length, c);
    }
    
    public CharHeapIndirectPriorityQueue(final char[] refArray, final int[] a, final int size) {
        this(refArray, a, size, null);
    }
    
    public CharHeapIndirectPriorityQueue(final char[] refArray, final int[] a) {
        this(refArray, a, a.length);
    }
    
    @Override
    public void enqueue(final int x) {
        if (this.inv[x] >= 0) {
            throw new IllegalArgumentException("Index " + x + " belongs to the queue");
        }
        if (this.size == this.heap.length) {
            this.heap = IntArrays.grow(this.heap, this.size + 1);
        }
        this.inv[this.heap[this.size] = x] = this.size++;
        CharIndirectHeaps.upHeap(this.refArray, this.heap, this.inv, this.size, this.size - 1, this.c);
    }
    
    @Override
    public boolean contains(final int index) {
        return this.inv[index] >= 0;
    }
    
    @Override
    public int dequeue() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        final int result = this.heap[0];
        if (--this.size != 0) {
            this.inv[this.heap[0] = this.heap[this.size]] = 0;
        }
        this.inv[result] = -1;
        if (this.size != 0) {
            CharIndirectHeaps.downHeap(this.refArray, this.heap, this.inv, this.size, 0, this.c);
        }
        return result;
    }
    
    @Override
    public void changed() {
        CharIndirectHeaps.downHeap(this.refArray, this.heap, this.inv, this.size, 0, this.c);
    }
    
    @Override
    public void changed(final int index) {
        final int pos = this.inv[index];
        if (pos < 0) {
            throw new IllegalArgumentException("Index " + index + " does not belong to the queue");
        }
        final int newPos = CharIndirectHeaps.upHeap(this.refArray, this.heap, this.inv, this.size, pos, this.c);
        CharIndirectHeaps.downHeap(this.refArray, this.heap, this.inv, this.size, newPos, this.c);
    }
    
    @Override
    public void allChanged() {
        CharIndirectHeaps.makeHeap(this.refArray, this.heap, this.inv, this.size, this.c);
    }
    
    @Override
    public boolean remove(final int index) {
        final int result = this.inv[index];
        if (result < 0) {
            return false;
        }
        this.inv[index] = -1;
        if (result < --this.size) {
            this.inv[this.heap[result] = this.heap[this.size]] = result;
            final int newPos = CharIndirectHeaps.upHeap(this.refArray, this.heap, this.inv, this.size, result, this.c);
            CharIndirectHeaps.downHeap(this.refArray, this.heap, this.inv, this.size, newPos, this.c);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.size = 0;
        Arrays.fill(this.inv, -1);
    }
}
