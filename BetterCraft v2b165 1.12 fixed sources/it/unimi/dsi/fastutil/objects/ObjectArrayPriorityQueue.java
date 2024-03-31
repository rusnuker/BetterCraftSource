// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Comparator;
import java.io.Serializable;
import it.unimi.dsi.fastutil.AbstractPriorityQueue;

public class ObjectArrayPriorityQueue<K> extends AbstractPriorityQueue<K> implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected transient K[] array;
    protected int size;
    protected Comparator<? super K> c;
    protected transient int firstIndex;
    protected transient boolean firstIndexValid;
    
    public ObjectArrayPriorityQueue(final int capacity, final Comparator<? super K> c) {
        this.array = (K[])ObjectArrays.EMPTY_ARRAY;
        if (capacity > 0) {
            this.array = (K[])new Object[capacity];
        }
        this.c = c;
    }
    
    public ObjectArrayPriorityQueue(final int capacity) {
        this(capacity, null);
    }
    
    public ObjectArrayPriorityQueue(final Comparator<? super K> c) {
        this(0, c);
    }
    
    public ObjectArrayPriorityQueue() {
        this(0, null);
    }
    
    public ObjectArrayPriorityQueue(final K[] a, final int size, final Comparator<? super K> c) {
        this(c);
        this.array = a;
        this.size = size;
    }
    
    public ObjectArrayPriorityQueue(final K[] a, final Comparator<? super K> c) {
        this(a, a.length, (Comparator<? super Object>)c);
    }
    
    public ObjectArrayPriorityQueue(final K[] a, final int size) {
        this(a, size, null);
    }
    
    public ObjectArrayPriorityQueue(final K[] a) {
        this(a, a.length);
    }
    
    private int findFirst() {
        if (this.firstIndexValid) {
            return this.firstIndex;
        }
        this.firstIndexValid = true;
        int i = this.size;
        int firstIndex = --i;
        K first = this.array[firstIndex];
        if (this.c == null) {
            while (i-- != 0) {
                if (((Comparable)this.array[i]).compareTo(first) < 0) {
                    first = this.array[firstIndex = i];
                }
            }
        }
        else {
            while (i-- != 0) {
                if (this.c.compare((Object)this.array[i], (Object)first) < 0) {
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
    public void enqueue(final K x) {
        if (this.size == this.array.length) {
            this.array = ObjectArrays.grow(this.array, this.size + 1);
        }
        if (this.firstIndexValid) {
            if (this.c == null) {
                if (((Comparable)x).compareTo(this.array[this.firstIndex]) < 0) {
                    this.firstIndex = this.size;
                }
            }
            else if (this.c.compare((Object)x, (Object)this.array[this.firstIndex]) < 0) {
                this.firstIndex = this.size;
            }
        }
        else {
            this.firstIndexValid = false;
        }
        this.array[this.size++] = x;
    }
    
    @Override
    public K dequeue() {
        this.ensureNonEmpty();
        final int first = this.findFirst();
        final K result = this.array[first];
        System.arraycopy(this.array, first + 1, this.array, first, --this.size - first);
        this.array[this.size] = null;
        this.firstIndexValid = false;
        return result;
    }
    
    @Override
    public K first() {
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
        Arrays.fill(this.array, 0, this.size, null);
        this.size = 0;
        this.firstIndexValid = false;
    }
    
    public void trim() {
        this.array = ObjectArrays.trim(this.array, this.size);
    }
    
    @Override
    public Comparator<? super K> comparator() {
        return this.c;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(this.array.length);
        for (int i = 0; i < this.size; ++i) {
            s.writeObject(this.array[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.array = (K[])new Object[s.readInt()];
        for (int i = 0; i < this.size; ++i) {
            this.array[i] = (K)s.readObject();
        }
    }
}
