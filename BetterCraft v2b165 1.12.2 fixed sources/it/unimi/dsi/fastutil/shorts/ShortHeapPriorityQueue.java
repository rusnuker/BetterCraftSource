// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.util.Comparator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;
import java.io.Serializable;

public class ShortHeapPriorityQueue extends AbstractShortPriorityQueue implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected transient short[] heap;
    protected int size;
    protected ShortComparator c;
    
    public ShortHeapPriorityQueue(final int capacity, final ShortComparator c) {
        this.heap = ShortArrays.EMPTY_ARRAY;
        if (capacity > 0) {
            this.heap = new short[capacity];
        }
        this.c = c;
    }
    
    public ShortHeapPriorityQueue(final int capacity) {
        this(capacity, null);
    }
    
    public ShortHeapPriorityQueue(final ShortComparator c) {
        this(0, c);
    }
    
    public ShortHeapPriorityQueue() {
        this(0, null);
    }
    
    public ShortHeapPriorityQueue(final short[] a, final int size, final ShortComparator c) {
        this(c);
        ShortHeaps.makeHeap(this.heap = a, this.size = size, c);
    }
    
    public ShortHeapPriorityQueue(final short[] a, final ShortComparator c) {
        this(a, a.length, c);
    }
    
    public ShortHeapPriorityQueue(final short[] a, final int size) {
        this(a, size, null);
    }
    
    public ShortHeapPriorityQueue(final short[] a) {
        this(a, a.length);
    }
    
    public ShortHeapPriorityQueue(final ShortCollection collection, final ShortComparator c) {
        this(collection.toShortArray(), c);
    }
    
    public ShortHeapPriorityQueue(final ShortCollection collection) {
        this(collection, null);
    }
    
    public ShortHeapPriorityQueue(final Collection<? extends Short> collection, final ShortComparator c) {
        this(collection.size(), c);
        final Iterator<? extends Short> iterator = collection.iterator();
        for (int size = collection.size(), i = 0; i < size; ++i) {
            this.heap[i] = (short)iterator.next();
        }
    }
    
    public ShortHeapPriorityQueue(final Collection<? extends Short> collection) {
        this(collection, null);
    }
    
    @Override
    public void enqueue(final short x) {
        if (this.size == this.heap.length) {
            this.heap = ShortArrays.grow(this.heap, this.size + 1);
        }
        this.heap[this.size++] = x;
        ShortHeaps.upHeap(this.heap, this.size, this.size - 1, this.c);
    }
    
    @Override
    public short dequeueShort() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        final short result = this.heap[0];
        final short[] heap = this.heap;
        final int n = 0;
        final short[] heap2 = this.heap;
        final int size = this.size - 1;
        this.size = size;
        heap[n] = heap2[size];
        if (this.size != 0) {
            ShortHeaps.downHeap(this.heap, this.size, 0, this.c);
        }
        return result;
    }
    
    @Override
    public short firstShort() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.heap[0];
    }
    
    @Override
    public void changed() {
        ShortHeaps.downHeap(this.heap, this.size, 0, this.c);
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
        this.heap = ShortArrays.trim(this.heap, this.size);
    }
    
    @Override
    public ShortComparator comparator() {
        return this.c;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(this.heap.length);
        for (int i = 0; i < this.size; ++i) {
            s.writeShort(this.heap[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.heap = new short[s.readInt()];
        for (int i = 0; i < this.size; ++i) {
            this.heap[i] = s.readShort();
        }
    }
}
