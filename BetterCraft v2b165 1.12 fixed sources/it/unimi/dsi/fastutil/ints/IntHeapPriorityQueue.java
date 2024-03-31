// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.util.Comparator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;
import java.io.Serializable;

public class IntHeapPriorityQueue extends AbstractIntPriorityQueue implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected transient int[] heap;
    protected int size;
    protected IntComparator c;
    
    public IntHeapPriorityQueue(final int capacity, final IntComparator c) {
        this.heap = IntArrays.EMPTY_ARRAY;
        if (capacity > 0) {
            this.heap = new int[capacity];
        }
        this.c = c;
    }
    
    public IntHeapPriorityQueue(final int capacity) {
        this(capacity, null);
    }
    
    public IntHeapPriorityQueue(final IntComparator c) {
        this(0, c);
    }
    
    public IntHeapPriorityQueue() {
        this(0, null);
    }
    
    public IntHeapPriorityQueue(final int[] a, final int size, final IntComparator c) {
        this(c);
        IntHeaps.makeHeap(this.heap = a, this.size = size, c);
    }
    
    public IntHeapPriorityQueue(final int[] a, final IntComparator c) {
        this(a, a.length, c);
    }
    
    public IntHeapPriorityQueue(final int[] a, final int size) {
        this(a, size, null);
    }
    
    public IntHeapPriorityQueue(final int[] a) {
        this(a, a.length);
    }
    
    public IntHeapPriorityQueue(final IntCollection collection, final IntComparator c) {
        this(collection.toIntArray(), c);
    }
    
    public IntHeapPriorityQueue(final IntCollection collection) {
        this(collection, null);
    }
    
    public IntHeapPriorityQueue(final Collection<? extends Integer> collection, final IntComparator c) {
        this(collection.size(), c);
        final Iterator<? extends Integer> iterator = collection.iterator();
        for (int size = collection.size(), i = 0; i < size; ++i) {
            this.heap[i] = (int)iterator.next();
        }
    }
    
    public IntHeapPriorityQueue(final Collection<? extends Integer> collection) {
        this(collection, null);
    }
    
    @Override
    public void enqueue(final int x) {
        if (this.size == this.heap.length) {
            this.heap = IntArrays.grow(this.heap, this.size + 1);
        }
        this.heap[this.size++] = x;
        IntHeaps.upHeap(this.heap, this.size, this.size - 1, this.c);
    }
    
    @Override
    public int dequeueInt() {
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
            IntHeaps.downHeap(this.heap, this.size, 0, this.c);
        }
        return result;
    }
    
    @Override
    public int firstInt() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.heap[0];
    }
    
    @Override
    public void changed() {
        IntHeaps.downHeap(this.heap, this.size, 0, this.c);
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
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(this.heap.length);
        for (int i = 0; i < this.size; ++i) {
            s.writeInt(this.heap[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.heap = new int[s.readInt()];
        for (int i = 0; i < this.size; ++i) {
            this.heap[i] = s.readInt();
        }
    }
}
