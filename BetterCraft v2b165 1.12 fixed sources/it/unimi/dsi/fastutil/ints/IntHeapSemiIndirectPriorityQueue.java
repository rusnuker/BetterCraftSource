// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.util.Comparator;
import java.util.NoSuchElementException;
import it.unimi.dsi.fastutil.AbstractIndirectPriorityQueue;

public class IntHeapSemiIndirectPriorityQueue extends AbstractIndirectPriorityQueue<Integer> implements IntIndirectPriorityQueue
{
    protected final int[] refArray;
    protected int[] heap;
    protected int size;
    protected IntComparator c;
    
    public IntHeapSemiIndirectPriorityQueue(final int[] refArray, final int capacity, final IntComparator c) {
        this.heap = IntArrays.EMPTY_ARRAY;
        if (capacity > 0) {
            this.heap = new int[capacity];
        }
        this.refArray = refArray;
        this.c = c;
    }
    
    public IntHeapSemiIndirectPriorityQueue(final int[] refArray, final int capacity) {
        this(refArray, capacity, null);
    }
    
    public IntHeapSemiIndirectPriorityQueue(final int[] refArray, final IntComparator c) {
        this(refArray, refArray.length, c);
    }
    
    public IntHeapSemiIndirectPriorityQueue(final int[] refArray) {
        this(refArray, refArray.length, null);
    }
    
    public IntHeapSemiIndirectPriorityQueue(final int[] refArray, final int[] a, final int size, final IntComparator c) {
        this(refArray, 0, c);
        IntSemiIndirectHeaps.makeHeap(refArray, this.heap = a, this.size = size, c);
    }
    
    public IntHeapSemiIndirectPriorityQueue(final int[] refArray, final int[] a, final IntComparator c) {
        this(refArray, a, a.length, c);
    }
    
    public IntHeapSemiIndirectPriorityQueue(final int[] refArray, final int[] a, final int size) {
        this(refArray, a, size, null);
    }
    
    public IntHeapSemiIndirectPriorityQueue(final int[] refArray, final int[] a) {
        this(refArray, a, a.length);
    }
    
    protected void ensureElement(final int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index >= this.refArray.length) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is larger than or equal to reference array size (" + this.refArray.length + ")");
        }
    }
    
    @Override
    public void enqueue(final int x) {
        this.ensureElement(x);
        if (this.size == this.heap.length) {
            this.heap = IntArrays.grow(this.heap, this.size + 1);
        }
        this.heap[this.size++] = x;
        IntSemiIndirectHeaps.upHeap(this.refArray, this.heap, this.size, this.size - 1, this.c);
    }
    
    @Override
    public int dequeue() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        final int result = this.heap[0];
        final int[] heap = this.heap;
        final int n = 0;
        final int[] heap2 = this.heap;
        final int size = this.size - 1;
        this.size = size;
        heap[n] = heap2[size];
        if (this.size != 0) {
            IntSemiIndirectHeaps.downHeap(this.refArray, this.heap, this.size, 0, this.c);
        }
        return result;
    }
    
    @Override
    public int first() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.heap[0];
    }
    
    @Override
    public void changed() {
        IntSemiIndirectHeaps.downHeap(this.refArray, this.heap, this.size, 0, this.c);
    }
    
    @Override
    public void allChanged() {
        IntSemiIndirectHeaps.makeHeap(this.refArray, this.heap, this.size, this.c);
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public void clear() {
        this.size = 0;
    }
    
    public void trim() {
        this.heap = IntArrays.trim(this.heap, this.size);
    }
    
    @Override
    public IntComparator comparator() {
        return this.c;
    }
    
    @Override
    public int front(final int[] a) {
        return (this.c == null) ? IntSemiIndirectHeaps.front(this.refArray, this.heap, this.size, a) : IntSemiIndirectHeaps.front(this.refArray, this.heap, this.size, a, this.c);
    }
    
    @Override
    public String toString() {
        final StringBuffer s = new StringBuffer();
        s.append("[");
        for (int i = 0; i < this.size; ++i) {
            if (i != 0) {
                s.append(", ");
            }
            s.append(this.refArray[this.heap[i]]);
        }
        s.append("]");
        return s.toString();
    }
}
