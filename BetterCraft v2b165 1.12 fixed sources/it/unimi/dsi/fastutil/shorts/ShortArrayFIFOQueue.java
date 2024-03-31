// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.util.Comparator;
import it.unimi.dsi.fastutil.HashCommon;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.io.Serializable;

public class ShortArrayFIFOQueue extends AbstractShortPriorityQueue implements Serializable
{
    private static final long serialVersionUID = 0L;
    public static final int INITIAL_CAPACITY = 4;
    protected transient short[] array;
    protected transient int length;
    protected transient int start;
    protected transient int end;
    
    public ShortArrayFIFOQueue(final int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
        }
        this.array = new short[Math.max(1, capacity)];
        this.length = this.array.length;
    }
    
    public ShortArrayFIFOQueue() {
        this(4);
    }
    
    @Override
    public ShortComparator comparator() {
        return null;
    }
    
    @Override
    public short dequeueShort() {
        if (this.start == this.end) {
            throw new NoSuchElementException();
        }
        final short t = this.array[this.start];
        if (++this.start == this.length) {
            this.start = 0;
        }
        this.reduce();
        return t;
    }
    
    public short dequeueLastShort() {
        if (this.start == this.end) {
            throw new NoSuchElementException();
        }
        if (this.end == 0) {
            this.end = this.length;
        }
        final short[] array = this.array;
        final int end = this.end - 1;
        this.end = end;
        final short t = array[end];
        this.reduce();
        return t;
    }
    
    private final void resize(final int size, final int newLength) {
        final short[] newArray = new short[newLength];
        if (this.start >= this.end) {
            if (size != 0) {
                System.arraycopy(this.array, this.start, newArray, 0, this.length - this.start);
                System.arraycopy(this.array, 0, newArray, this.length - this.start, this.end);
            }
        }
        else {
            System.arraycopy(this.array, this.start, newArray, 0, this.end - this.start);
        }
        this.start = 0;
        this.end = size;
        this.array = newArray;
        this.length = newLength;
    }
    
    private final void expand() {
        this.resize(this.length, (int)Math.min(2147483639L, 2L * this.length));
    }
    
    private final void reduce() {
        final int size = this.size();
        if (this.length > 4 && size <= this.length / 4) {
            this.resize(size, this.length / 2);
        }
    }
    
    @Override
    public void enqueue(final short x) {
        this.array[this.end++] = x;
        if (this.end == this.length) {
            this.end = 0;
        }
        if (this.end == this.start) {
            this.expand();
        }
    }
    
    public void enqueueFirst(final short x) {
        if (this.start == 0) {
            this.start = this.length;
        }
        this.array[--this.start] = x;
        if (this.end == this.start) {
            this.expand();
        }
    }
    
    @Override
    public short firstShort() {
        if (this.start == this.end) {
            throw new NoSuchElementException();
        }
        return this.array[this.start];
    }
    
    @Override
    public short lastShort() {
        if (this.start == this.end) {
            throw new NoSuchElementException();
        }
        return this.array[((this.end == 0) ? this.length : this.end) - 1];
    }
    
    @Override
    public void clear() {
        final int n = 0;
        this.end = n;
        this.start = n;
    }
    
    public void trim() {
        final int size = this.size();
        final short[] newArray = new short[size + 1];
        if (this.start <= this.end) {
            System.arraycopy(this.array, this.start, newArray, 0, this.end - this.start);
        }
        else {
            System.arraycopy(this.array, this.start, newArray, 0, this.length - this.start);
            System.arraycopy(this.array, 0, newArray, this.length - this.start, this.end);
        }
        this.start = 0;
        final int end = size;
        this.end = end;
        this.length = end + 1;
        this.array = newArray;
    }
    
    @Override
    public int size() {
        final int apparentLength = this.end - this.start;
        return (apparentLength >= 0) ? apparentLength : (this.length + apparentLength);
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        int size = this.size();
        s.writeInt(size);
        int i = this.start;
        while (size-- != 0) {
            s.writeShort(this.array[i++]);
            if (i == this.length) {
                i = 0;
            }
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.end = s.readInt();
        final int nextPowerOfTwo = HashCommon.nextPowerOfTwo(this.end + 1);
        this.length = nextPowerOfTwo;
        this.array = new short[nextPowerOfTwo];
        for (int i = 0; i < this.end; ++i) {
            this.array[i] = s.readShort();
        }
    }
}
