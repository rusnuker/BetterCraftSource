// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.util.Comparator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.io.Serializable;

public class LongArrayPriorityQueue extends AbstractLongPriorityQueue implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected transient long[] array;
    protected int size;
    protected LongComparator c;
    protected transient int firstIndex;
    protected transient boolean firstIndexValid;
    
    public LongArrayPriorityQueue(final int capacity, final LongComparator c) {
        this.array = LongArrays.EMPTY_ARRAY;
        if (capacity > 0) {
            this.array = new long[capacity];
        }
        this.c = c;
    }
    
    public LongArrayPriorityQueue(final int capacity) {
        this(capacity, null);
    }
    
    public LongArrayPriorityQueue(final LongComparator c) {
        this(0, c);
    }
    
    public LongArrayPriorityQueue() {
        this(0, null);
    }
    
    public LongArrayPriorityQueue(final long[] a, final int size, final LongComparator c) {
        this(c);
        this.array = a;
        this.size = size;
    }
    
    public LongArrayPriorityQueue(final long[] a, final LongComparator c) {
        this(a, a.length, c);
    }
    
    public LongArrayPriorityQueue(final long[] a, final int size) {
        this(a, size, null);
    }
    
    public LongArrayPriorityQueue(final long[] a) {
        this(a, a.length);
    }
    
    private int findFirst() {
        if (this.firstIndexValid) {
            return this.firstIndex;
        }
        this.firstIndexValid = true;
        int i = this.size;
        int firstIndex = --i;
        long first = this.array[firstIndex];
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
    public void enqueue(final long x) {
        if (this.size == this.array.length) {
            this.array = LongArrays.grow(this.array, this.size + 1);
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
    public long dequeueLong() {
        this.ensureNonEmpty();
        final int first = this.findFirst();
        final long result = this.array[first];
        System.arraycopy(this.array, first + 1, this.array, first, --this.size - first);
        this.firstIndexValid = false;
        return result;
    }
    
    @Override
    public long firstLong() {
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
        this.array = LongArrays.trim(this.array, this.size);
    }
    
    @Override
    public LongComparator comparator() {
        return this.c;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(this.array.length);
        for (int i = 0; i < this.size; ++i) {
            s.writeLong(this.array[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.array = new long[s.readInt()];
        for (int i = 0; i < this.size; ++i) {
            this.array[i] = s.readLong();
        }
    }
}
