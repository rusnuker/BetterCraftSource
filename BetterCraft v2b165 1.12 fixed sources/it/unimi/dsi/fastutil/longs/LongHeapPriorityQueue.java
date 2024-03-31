// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.util.Comparator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;
import java.io.Serializable;

public class LongHeapPriorityQueue extends AbstractLongPriorityQueue implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected transient long[] heap;
    protected int size;
    protected LongComparator c;
    
    public LongHeapPriorityQueue(final int capacity, final LongComparator c) {
        this.heap = LongArrays.EMPTY_ARRAY;
        if (capacity > 0) {
            this.heap = new long[capacity];
        }
        this.c = c;
    }
    
    public LongHeapPriorityQueue(final int capacity) {
        this(capacity, null);
    }
    
    public LongHeapPriorityQueue(final LongComparator c) {
        this(0, c);
    }
    
    public LongHeapPriorityQueue() {
        this(0, null);
    }
    
    public LongHeapPriorityQueue(final long[] a, final int size, final LongComparator c) {
        this(c);
        LongHeaps.makeHeap(this.heap = a, this.size = size, c);
    }
    
    public LongHeapPriorityQueue(final long[] a, final LongComparator c) {
        this(a, a.length, c);
    }
    
    public LongHeapPriorityQueue(final long[] a, final int size) {
        this(a, size, null);
    }
    
    public LongHeapPriorityQueue(final long[] a) {
        this(a, a.length);
    }
    
    public LongHeapPriorityQueue(final LongCollection collection, final LongComparator c) {
        this(collection.toLongArray(), c);
    }
    
    public LongHeapPriorityQueue(final LongCollection collection) {
        this(collection, null);
    }
    
    public LongHeapPriorityQueue(final Collection<? extends Long> collection, final LongComparator c) {
        this(collection.size(), c);
        final Iterator<? extends Long> iterator = collection.iterator();
        for (int size = collection.size(), i = 0; i < size; ++i) {
            this.heap[i] = (long)iterator.next();
        }
    }
    
    public LongHeapPriorityQueue(final Collection<? extends Long> collection) {
        this(collection, null);
    }
    
    @Override
    public void enqueue(final long x) {
        if (this.size == this.heap.length) {
            this.heap = LongArrays.grow(this.heap, this.size + 1);
        }
        this.heap[this.size++] = x;
        LongHeaps.upHeap(this.heap, this.size, this.size - 1, this.c);
    }
    
    @Override
    public long dequeueLong() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        final long result = this.heap[0];
        final long[] heap = this.heap;
        final int n = 0;
        final long[] heap2 = this.heap;
        final int size = this.size - 1;
        this.size = size;
        heap[n] = heap2[size];
        if (this.size != 0) {
            LongHeaps.downHeap(this.heap, this.size, 0, this.c);
        }
        return result;
    }
    
    @Override
    public long firstLong() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.heap[0];
    }
    
    @Override
    public void changed() {
        LongHeaps.downHeap(this.heap, this.size, 0, this.c);
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
        this.heap = LongArrays.trim(this.heap, this.size);
    }
    
    @Override
    public LongComparator comparator() {
        return this.c;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(this.heap.length);
        for (int i = 0; i < this.size; ++i) {
            s.writeLong(this.heap[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.heap = new long[s.readInt()];
        for (int i = 0; i < this.size; ++i) {
            this.heap[i] = s.readLong();
        }
    }
}
