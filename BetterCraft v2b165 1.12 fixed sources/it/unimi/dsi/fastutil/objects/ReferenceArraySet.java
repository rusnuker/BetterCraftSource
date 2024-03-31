// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Iterator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Collection;
import java.io.Serializable;

public class ReferenceArraySet<K> extends AbstractReferenceSet<K> implements Serializable, Cloneable
{
    private static final long serialVersionUID = 1L;
    private transient Object[] a;
    private int size;
    
    public ReferenceArraySet(final Object[] a) {
        this.a = a;
        this.size = a.length;
    }
    
    public ReferenceArraySet() {
        this.a = ObjectArrays.EMPTY_ARRAY;
    }
    
    public ReferenceArraySet(final int capacity) {
        this.a = new Object[capacity];
    }
    
    public ReferenceArraySet(final ReferenceCollection<K> c) {
        this(c.size());
        this.addAll((Collection<? extends K>)c);
    }
    
    public ReferenceArraySet(final Collection<? extends K> c) {
        this(c.size());
        this.addAll(c);
    }
    
    public ReferenceArraySet(final Object[] a, final int size) {
        this.a = a;
        this.size = size;
        if (size > a.length) {
            throw new IllegalArgumentException("The provided size (" + size + ") is larger than or equal to the array size (" + a.length + ")");
        }
    }
    
    private int findKey(final Object o) {
        int i = this.size;
        while (i-- != 0) {
            if (this.a[i] == o) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public ObjectIterator<K> iterator() {
        return new AbstractObjectIterator<K>() {
            int next = 0;
            
            @Override
            public boolean hasNext() {
                return this.next < ReferenceArraySet.this.size;
            }
            
            @Override
            public K next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return (K)ReferenceArraySet.this.a[this.next++];
            }
            
            @Override
            public void remove() {
                final int tail = ReferenceArraySet.this.size-- - this.next--;
                System.arraycopy(ReferenceArraySet.this.a, this.next + 1, ReferenceArraySet.this.a, this.next, tail);
                ReferenceArraySet.this.a[ReferenceArraySet.this.size] = null;
            }
        };
    }
    
    @Override
    public boolean contains(final Object k) {
        return this.findKey(k) != -1;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean rem(final Object k) {
        final int pos = this.findKey(k);
        if (pos == -1) {
            return false;
        }
        for (int tail = this.size - pos - 1, i = 0; i < tail; ++i) {
            this.a[pos + i] = this.a[pos + i + 1];
        }
        --this.size;
        this.a[this.size] = null;
        return true;
    }
    
    @Override
    public boolean add(final K k) {
        final int pos = this.findKey(k);
        if (pos != -1) {
            return false;
        }
        if (this.size == this.a.length) {
            final Object[] b = new Object[(this.size == 0) ? 2 : (this.size * 2)];
            int i = this.size;
            while (i-- != 0) {
                b[i] = this.a[i];
            }
            this.a = b;
        }
        this.a[this.size++] = k;
        return true;
    }
    
    @Override
    public void clear() {
        int i = this.size;
        while (i-- != 0) {
            this.a[i] = null;
        }
        this.size = 0;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public ReferenceArraySet<K> clone() {
        ReferenceArraySet<K> c;
        try {
            c = (ReferenceArraySet)super.clone();
        }
        catch (final CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.a = this.a.clone();
        return c;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeObject(this.a[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = new Object[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.a[i] = s.readObject();
        }
    }
}
