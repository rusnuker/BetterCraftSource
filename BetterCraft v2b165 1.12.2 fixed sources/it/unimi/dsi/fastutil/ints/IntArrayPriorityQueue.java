// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.util.Comparator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.io.Serializable;

public class IntArrayPriorityQueue extends AbstractIntPriorityQueue implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected transient int[] array;
    protected int size;
    protected IntComparator c;
    protected transient int firstIndex;
    protected transient boolean firstIndexValid;
    
    public IntArrayPriorityQueue(final int capacity, final IntComparator c) {
        this.array = IntArrays.EMPTY_ARRAY;
        if (capacity > 0) {
            this.array = new int[capacity];
        }
        this.c = c;
    }
    
    public IntArrayPriorityQueue(final int capacity) {
        this(capacity, null);
    }
    
    public IntArrayPriorityQueue(final IntComparator c) {
        this(0, c);
    }
    
    public IntArrayPriorityQueue() {
        this(0, null);
    }
    
    public IntArrayPriorityQueue(final int[] a, final int size, final IntComparator c) {
        this(c);
        this.array = a;
        this.size = size;
    }
    
    public IntArrayPriorityQueue(final int[] a, final IntComparator c) {
        this(a, a.length, c);
    }
    
    public IntArrayPriorityQueue(final int[] a, final int size) {
        this(a, size, null);
    }
    
    public IntArrayPriorityQueue(final int[] a) {
        this(a, a.length);
    }
    
    private int findFirst() {
        if (this.firstIndexValid) {
            return this.firstIndex;
        }
        this.firstIndexValid = true;
        int i = this.size;
        int firstIndex = --i;
        int first = this.array[firstIndex];
        if (this.c == null) {
            while (i-- != 0) {
                if (this.array[i] < first) {
                    first = this.array[firstIndex = i];
                }
            }
        }
        else {
            while (i-- != 0) {
                if (this.c.compare(this.array[i], first) < 0) {
                    first = this.array[firstIndex = i];
                }
            }
        }
        return this.firstIndex = firstIndex;
    }
    
    private void ensureNonEmpty() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
    }
    
    @Override
    public void enqueue(final int x) {
        if (this.size == this.array.length) {
            this.array = IntArrays.grow(this.array, this.size + 1);
        }
        if (this.firstIndexValid) {
            if (this.c == null) {
                if (x < this.array[this.firstIndex]) {
                    this.firstIndex = this.size;
                }
            }
            else if (this.c.compare(x, this.array[this.firstIndex]) < 0) {
                this.firstIndex = this.size;
            }
        }
        else {
            this.firstIndexValid = false;
        }
        this.array[this.size++] = x;
    }
    
    @Override
    public int dequeueInt() {
        this.ensureNonEmpty();
        final int first = this.findFirst();
        final int result = this.array[first];
        System.arraycopy(this.array, first + 1, this.array, first, --this.size - first);
        this.firstIndexValid = false;
        return result;
    }
    
    @Override
    public int firstInt() {
        this.ensureNonEmpty();
        return this.array[this.findFirst()];
    }
    
    @Override
    public void changed() {
        this.ensureNonEmpty();
        this.firstIndexValid = false;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public void clear() {
        this.size = 0;
        this.firstIndexValid = false;
    }
    
    public void trim() {
        this.array = IntArrays.trim(this.array, this.size);
    }
    
    @Override
    public IntComparator comparator() {
        return this.c;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(this.array.length);
        for (int i = 0; i < this.size; ++i) {
            s.writeInt(this.array[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.array = new int[s.readInt()];
        for (int i = 0; i < this.size; ++i) {
            this.array[i] = s.readInt();
        }
    }
}
