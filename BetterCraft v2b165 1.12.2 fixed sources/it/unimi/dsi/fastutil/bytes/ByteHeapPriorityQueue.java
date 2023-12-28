// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.util.Comparator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;
import java.io.Serializable;

public class ByteHeapPriorityQueue extends AbstractBytePriorityQueue implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected transient byte[] heap;
    protected int size;
    protected ByteComparator c;
    
    public ByteHeapPriorityQueue(final int capacity, final ByteComparator c) {
        this.heap = ByteArrays.EMPTY_ARRAY;
        if (capacity > 0) {
            this.heap = new byte[capacity];
        }
        this.c = c;
    }
    
    public ByteHeapPriorityQueue(final int capacity) {
        this(capacity, null);
    }
    
    public ByteHeapPriorityQueue(final ByteComparator c) {
        this(0, c);
    }
    
    public ByteHeapPriorityQueue() {
        this(0, null);
    }
    
    public ByteHeapPriorityQueue(final byte[] a, final int size, final ByteComparator c) {
        this(c);
        ByteHeaps.makeHeap(this.heap = a, this.size = size, c);
    }
    
    public ByteHeapPriorityQueue(final byte[] a, final ByteComparator c) {
        this(a, a.length, c);
    }
    
    public ByteHeapPriorityQueue(final byte[] a, final int size) {
        this(a, size, null);
    }
    
    public ByteHeapPriorityQueue(final byte[] a) {
        this(a, a.length);
    }
    
    public ByteHeapPriorityQueue(final ByteCollection collection, final ByteComparator c) {
        this(collection.toByteArray(), c);
    }
    
    public ByteHeapPriorityQueue(final ByteCollection collection) {
        this(collection, null);
    }
    
    public ByteHeapPriorityQueue(final Collection<? extends Byte> collection, final ByteComparator c) {
        this(collection.size(), c);
        final Iterator<? extends Byte> iterator = collection.iterator();
        for (int size = collection.size(), i = 0; i < size; ++i) {
            this.heap[i] = (byte)iterator.next();
        }
    }
    
    public ByteHeapPriorityQueue(final Collection<? extends Byte> collection) {
        this(collection, null);
    }
    
    @Override
    public void enqueue(final byte x) {
        if (this.size == this.heap.length) {
            this.heap = ByteArrays.grow(this.heap, this.size + 1);
        }
        this.heap[this.size++] = x;
        ByteHeaps.upHeap(this.heap, this.size, this.size - 1, this.c);
    }
    
    @Override
    public byte dequeueByte() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        final byte result = this.heap[0];
        final byte[] heap = this.heap;
        final int n = 0;
        final byte[] heap2 = this.heap;
        final int size = this.size - 1;
        this.size = size;
        heap[n] = heap2[size];
        if (this.size != 0) {
            ByteHeaps.downHeap(this.heap, this.size, 0, this.c);
        }
        return result;
    }
    
    @Override
    public byte firstByte() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.heap[0];
    }
    
    @Override
    public void changed() {
        ByteHeaps.downHeap(this.heap, this.size, 0, this.c);
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
        this.heap = ByteArrays.trim(this.heap, this.size);
    }
    
    @Override
    public ByteComparator comparator() {
        return this.c;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(this.heap.length);
        for (int i = 0; i < this.size; ++i) {
            s.writeByte(this.heap[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.heap = new byte[s.readInt()];
        for (int i = 0; i < this.size; ++i) {
            this.heap[i] = s.readByte();
        }
    }
}
