// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.util.Comparator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;
import java.io.Serializable;

public class FloatHeapPriorityQueue extends AbstractFloatPriorityQueue implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected transient float[] heap;
    protected int size;
    protected FloatComparator c;
    
    public FloatHeapPriorityQueue(final int capacity, final FloatComparator c) {
        this.heap = FloatArrays.EMPTY_ARRAY;
        if (capacity > 0) {
            this.heap = new float[capacity];
        }
        this.c = c;
    }
    
    public FloatHeapPriorityQueue(final int capacity) {
        this(capacity, null);
    }
    
    public FloatHeapPriorityQueue(final FloatComparator c) {
        this(0, c);
    }
    
    public FloatHeapPriorityQueue() {
        this(0, null);
    }
    
    public FloatHeapPriorityQueue(final float[] a, final int size, final FloatComparator c) {
        this(c);
        FloatHeaps.makeHeap(this.heap = a, this.size = size, c);
    }
    
    public FloatHeapPriorityQueue(final float[] a, final FloatComparator c) {
        this(a, a.length, c);
    }
    
    public FloatHeapPriorityQueue(final float[] a, final int size) {
        this(a, size, null);
    }
    
    public FloatHeapPriorityQueue(final float[] a) {
        this(a, a.length);
    }
    
    public FloatHeapPriorityQueue(final FloatCollection collection, final FloatComparator c) {
        this(collection.toFloatArray(), c);
    }
    
    public FloatHeapPriorityQueue(final FloatCollection collection) {
        this(collection, null);
    }
    
    public FloatHeapPriorityQueue(final Collection<? extends Float> collection, final FloatComparator c) {
        this(collection.size(), c);
        final Iterator<? extends Float> iterator = collection.iterator();
        for (int size = collection.size(), i = 0; i < size; ++i) {
            this.heap[i] = (float)iterator.next();
        }
    }
    
    public FloatHeapPriorityQueue(final Collection<? extends Float> collection) {
        this(collection, null);
    }
    
    @Override
    public void enqueue(final float x) {
        if (this.size == this.heap.length) {
            this.heap = FloatArrays.grow(this.heap, this.size + 1);
        }
        this.heap[this.size++] = x;
        FloatHeaps.upHeap(this.heap, this.size, this.size - 1, this.c);
    }
    
    @Override
    public float dequeueFloat() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        final float result = this.heap[0];
        final float[] heap = this.heap;
        final int n = 0;
        final float[] heap2 = this.heap;
        final int size = this.size - 1;
        this.size = size;
        heap[n] = heap2[size];
        if (this.size != 0) {
            FloatHeaps.downHeap(this.heap, this.size, 0, this.c);
        }
        return result;
    }
    
    @Override
    public float firstFloat() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.heap[0];
    }
    
    @Override
    public void changed() {
        FloatHeaps.downHeap(this.heap, this.size, 0, this.c);
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
        this.heap = FloatArrays.trim(this.heap, this.size);
    }
    
    @Override
    public FloatComparator comparator() {
        return this.c;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(this.heap.length);
        for (int i = 0; i < this.size; ++i) {
            s.writeFloat(this.heap[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.heap = new float[s.readInt()];
        for (int i = 0; i < this.size; ++i) {
            this.heap[i] = s.readFloat();
        }
    }
}
