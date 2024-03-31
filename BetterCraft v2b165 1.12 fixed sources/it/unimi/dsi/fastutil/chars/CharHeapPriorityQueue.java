// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.util.Comparator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;
import java.io.Serializable;

public class CharHeapPriorityQueue extends AbstractCharPriorityQueue implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected transient char[] heap;
    protected int size;
    protected CharComparator c;
    
    public CharHeapPriorityQueue(final int capacity, final CharComparator c) {
        this.heap = CharArrays.EMPTY_ARRAY;
        if (capacity > 0) {
            this.heap = new char[capacity];
        }
        this.c = c;
    }
    
    public CharHeapPriorityQueue(final int capacity) {
        this(capacity, null);
    }
    
    public CharHeapPriorityQueue(final CharComparator c) {
        this(0, c);
    }
    
    public CharHeapPriorityQueue() {
        this(0, null);
    }
    
    public CharHeapPriorityQueue(final char[] a, final int size, final CharComparator c) {
        this(c);
        CharHeaps.makeHeap(this.heap = a, this.size = size, c);
    }
    
    public CharHeapPriorityQueue(final char[] a, final CharComparator c) {
        this(a, a.length, c);
    }
    
    public CharHeapPriorityQueue(final char[] a, final int size) {
        this(a, size, null);
    }
    
    public CharHeapPriorityQueue(final char[] a) {
        this(a, a.length);
    }
    
    public CharHeapPriorityQueue(final CharCollection collection, final CharComparator c) {
        this(collection.toCharArray(), c);
    }
    
    public CharHeapPriorityQueue(final CharCollection collection) {
        this(collection, null);
    }
    
    public CharHeapPriorityQueue(final Collection<? extends Character> collection, final CharComparator c) {
        this(collection.size(), c);
        final Iterator<? extends Character> iterator = collection.iterator();
        for (int size = collection.size(), i = 0; i < size; ++i) {
            this.heap[i] = (char)iterator.next();
        }
    }
    
    public CharHeapPriorityQueue(final Collection<? extends Character> collection) {
        this(collection, null);
    }
    
    @Override
    public void enqueue(final char x) {
        if (this.size == this.heap.length) {
            this.heap = CharArrays.grow(this.heap, this.size + 1);
        }
        this.heap[this.size++] = x;
        CharHeaps.upHeap(this.heap, this.size, this.size - 1, this.c);
    }
    
    @Override
    public char dequeueChar() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        final char result = this.heap[0];
        final char[] heap = this.heap;
        final int n = 0;
        final char[] heap2 = this.heap;
        final int size = this.size - 1;
        this.size = size;
        heap[n] = heap2[size];
        if (this.size != 0) {
            CharHeaps.downHeap(this.heap, this.size, 0, this.c);
        }
        return result;
    }
    
    @Override
    public char firstChar() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.heap[0];
    }
    
    @Override
    public void changed() {
        CharHeaps.downHeap(this.heap, this.size, 0, this.c);
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
        this.heap = CharArrays.trim(this.heap, this.size);
    }
    
    @Override
    public CharComparator comparator() {
        return this.c;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(this.heap.length);
        for (int i = 0; i < this.size; ++i) {
            s.writeChar(this.heap[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.heap = new char[s.readInt()];
        for (int i = 0; i < this.size; ++i) {
            this.heap[i] = s.readChar();
        }
    }
}
