// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import java.util.Comparator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;
import java.io.Serializable;

public class DoubleHeapPriorityQueue extends AbstractDoublePriorityQueue implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected transient double[] heap;
    protected int size;
    protected DoubleComparator c;
    
    public DoubleHeapPriorityQueue(final int capacity, final DoubleComparator c) {
        this.heap = DoubleArrays.EMPTY_ARRAY;
        if (capacity > 0) {
            this.heap = new double[capacity];
        }
        this.c = c;
    }
    
    public DoubleHeapPriorityQueue(final int capacity) {
        this(capacity, null);
    }
    
    public DoubleHeapPriorityQueue(final DoubleComparator c) {
        this(0, c);
    }
    
    public DoubleHeapPriorityQueue() {
        this(0, null);
    }
    
    public DoubleHeapPriorityQueue(final double[] a, final int size, final DoubleComparator c) {
        this(c);
        DoubleHeaps.makeHeap(this.heap = a, this.size = size, c);
    }
    
    public DoubleHeapPriorityQueue(final double[] a, final DoubleComparator c) {
        this(a, a.length, c);
    }
    
    public DoubleHeapPriorityQueue(final double[] a, final int size) {
        this(a, size, null);
    }
    
    public DoubleHeapPriorityQueue(final double[] a) {
        this(a, a.length);
    }
    
    public DoubleHeapPriorityQueue(final DoubleCollection collection, final DoubleComparator c) {
        this(collection.toDoubleArray(), c);
    }
    
    public DoubleHeapPriorityQueue(final DoubleCollection collection) {
        this(collection, null);
    }
    
    public DoubleHeapPriorityQueue(final Collection<? extends Double> collection, final DoubleComparator c) {
        this(collection.size(), c);
        final Iterator<? extends Double> iterator = collection.iterator();
        for (int size = collection.size(), i = 0; i < size; ++i) {
            this.heap[i] = (double)iterator.next();
        }
    }
    
    public DoubleHeapPriorityQueue(final Collection<? extends Double> collection) {
        this(collection, null);
    }
    
    @Override
    public void enqueue(final double x) {
        if (this.size == this.heap.length) {
            this.heap = DoubleArrays.grow(this.heap, this.size + 1);
        }
        this.heap[this.size++] = x;
        DoubleHeaps.upHeap(this.heap, this.size, this.size - 1, this.c);
    }
    
    @Override
    public double dequeueDouble() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        final double result = this.heap[0];
        final double[] heap = this.heap;
        final int n = 0;
        final double[] heap2 = this.heap;
        final int size = this.size - 1;
        this.size = size;
        heap[n] = heap2[size];
        if (this.size != 0) {
            DoubleHeaps.downHeap(this.heap, this.size, 0, this.c);
        }
        return result;
    }
    
    @Override
    public double firstDouble() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.heap[0];
    }
    
    @Override
    public void changed() {
        DoubleHeaps.downHeap(this.heap, this.size, 0, this.c);
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
        this.heap = DoubleArrays.trim(this.heap, this.size);
    }
    
    @Override
    public DoubleComparator comparator() {
        return this.c;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(this.heap.length);
        for (int i = 0; i < this.size; ++i) {
            s.writeDouble(this.heap[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.heap = new double[s.readInt()];
        for (int i = 0; i < this.size; ++i) {
            this.heap[i] = s.readDouble();
        }
    }
}
