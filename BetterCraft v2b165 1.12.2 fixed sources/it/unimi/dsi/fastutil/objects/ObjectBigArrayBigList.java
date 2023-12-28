// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.BigListIterator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import it.unimi.dsi.fastutil.BigArrays;
import java.util.Collection;
import java.util.Iterator;
import java.io.Serializable;
import java.util.RandomAccess;

public class ObjectBigArrayBigList<K> extends AbstractObjectBigList<K> implements RandomAccess, Cloneable, Serializable
{
    private static final long serialVersionUID = -7046029254386353131L;
    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    protected final boolean wrapped;
    protected transient K[][] a;
    protected long size;
    private static final boolean ASSERTS = false;
    
    protected ObjectBigArrayBigList(final K[][] a, final boolean dummy) {
        this.a = a;
        this.wrapped = true;
    }
    
    public ObjectBigArrayBigList(final long capacity) {
        if (capacity < 0L) {
            throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
        }
        this.a = (K[][])ObjectBigArrays.newBigArray(capacity);
        this.wrapped = false;
    }
    
    public ObjectBigArrayBigList() {
        this(16L);
    }
    
    public ObjectBigArrayBigList(final ObjectCollection<? extends K> c) {
        this(c.size());
        final ObjectIterator<? extends K> i = c.iterator();
        while (i.hasNext()) {
            this.add(i.next());
        }
    }
    
    public ObjectBigArrayBigList(final ObjectBigList<? extends K> l) {
        this(l.size64());
        l.getElements(0L, this.a, 0L, this.size = l.size64());
    }
    
    public ObjectBigArrayBigList(final K[][] a) {
        this(a, 0L, ObjectBigArrays.length(a));
    }
    
    public ObjectBigArrayBigList(final K[][] a, final long offset, final long length) {
        this(length);
        ObjectBigArrays.copy(a, offset, this.a, 0L, length);
        this.size = length;
    }
    
    public ObjectBigArrayBigList(final Iterator<? extends K> i) {
        this();
        while (i.hasNext()) {
            this.add(i.next());
        }
    }
    
    public ObjectBigArrayBigList(final ObjectIterator<? extends K> i) {
        this();
        while (i.hasNext()) {
            this.add(i.next());
        }
    }
    
    public K[][] elements() {
        return this.a;
    }
    
    public static <K> ObjectBigArrayBigList<K> wrap(final K[][] a, final long length) {
        if (length > ObjectBigArrays.length(a)) {
            throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + ObjectBigArrays.length(a) + ")");
        }
        final ObjectBigArrayBigList<K> l = new ObjectBigArrayBigList<K>(a, false);
        l.size = length;
        return l;
    }
    
    public static <K> ObjectBigArrayBigList<K> wrap(final K[][] a) {
        return wrap(a, ObjectBigArrays.length(a));
    }
    
    public void ensureCapacity(final long capacity) {
        if (this.wrapped) {
            this.a = ObjectBigArrays.ensureCapacity(this.a, capacity, this.size);
        }
        else if (capacity > ObjectBigArrays.length(this.a)) {
            final Object[][] t = ObjectBigArrays.newBigArray(capacity);
            ObjectBigArrays.copy(this.a, 0L, t, 0L, this.size);
            this.a = (K[][])t;
        }
    }
    
    private void grow(final long capacity) {
        if (this.wrapped) {
            this.a = ObjectBigArrays.grow(this.a, capacity, this.size);
        }
        else if (capacity > ObjectBigArrays.length(this.a)) {
            final int newLength = (int)Math.max(Math.min(2L * ObjectBigArrays.length(this.a), 2147483639L), capacity);
            final Object[][] t = ObjectBigArrays.newBigArray(newLength);
            ObjectBigArrays.copy(this.a, 0L, t, 0L, this.size);
            this.a = (K[][])t;
        }
    }
    
    @Override
    public void add(final long index, final K k) {
        this.ensureIndex(index);
        this.grow(this.size + 1L);
        if (index != this.size) {
            ObjectBigArrays.copy(this.a, index, this.a, index + 1L, this.size - index);
        }
        ObjectBigArrays.set(this.a, index, k);
        ++this.size;
    }
    
    @Override
    public boolean add(final K k) {
        this.grow(this.size + 1L);
        ObjectBigArrays.set(this.a, this.size++, k);
        return true;
    }
    
    @Override
    public K get(final long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        return ObjectBigArrays.get(this.a, index);
    }
    
    @Override
    public long indexOf(final Object k) {
        for (long i = 0L; i < this.size; ++i) {
            if (k == null) {
                if (ObjectBigArrays.get(this.a, i) == null) {
                    return i;
                }
            }
            else if (k.equals(ObjectBigArrays.get(this.a, i))) {
                return i;
            }
        }
        return -1L;
    }
    
    @Override
    public long lastIndexOf(final Object k) {
        long i = this.size;
        while (i-- != 0L) {
            if (k == null) {
                if (ObjectBigArrays.get(this.a, i) != null) {
                    continue;
                }
            }
            else if (!k.equals(ObjectBigArrays.get(this.a, i))) {
                continue;
            }
            return i;
        }
        return -1L;
    }
    
    @Override
    public K remove(final long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final K old = ObjectBigArrays.get(this.a, index);
        --this.size;
        if (index != this.size) {
            ObjectBigArrays.copy(this.a, index + 1L, this.a, index, this.size - index);
        }
        ObjectBigArrays.set(this.a, this.size, (K)null);
        return old;
    }
    
    @Override
    public boolean rem(final Object k) {
        final long index = this.indexOf(k);
        if (index == -1L) {
            return false;
        }
        this.remove(index);
        return true;
    }
    
    @Override
    public boolean remove(final Object o) {
        return this.rem(o);
    }
    
    @Override
    public K set(final long index, final K k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final K old = ObjectBigArrays.get(this.a, index);
        ObjectBigArrays.set(this.a, index, k);
        return old;
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        K[] s = null;
        K[] d = null;
        int ss = -1;
        int sd = 134217728;
        int ds = -1;
        int dd = 134217728;
        for (long i = 0L; i < this.size; ++i) {
            if (sd == 134217728) {
                sd = 0;
                s = this.a[++ss];
            }
            if (!c.contains(s[sd])) {
                if (dd == 134217728) {
                    d = this.a[++ds];
                    dd = 0;
                }
                d[dd++] = s[sd];
            }
            ++sd;
        }
        final long j = BigArrays.index(ds, dd);
        final boolean modified = this.size != j;
        this.size = j;
        return modified;
    }
    
    @Override
    public void clear() {
        ObjectBigArrays.fill(this.a, 0L, this.size, (K)null);
        this.size = 0L;
    }
    
    @Override
    public long size64() {
        return this.size;
    }
    
    @Override
    public void size(final long size) {
        if (size > ObjectBigArrays.length(this.a)) {
            this.ensureCapacity(size);
        }
        if (size > this.size) {
            ObjectBigArrays.fill(this.a, this.size, size, (K)null);
        }
        else {
            ObjectBigArrays.fill(this.a, size, this.size, (K)null);
        }
        this.size = size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0L;
    }
    
    public void trim() {
        this.trim(0L);
    }
    
    public void trim(final long n) {
        final long arrayLength = ObjectBigArrays.length(this.a);
        if (n >= arrayLength || this.size == arrayLength) {
            return;
        }
        this.a = ObjectBigArrays.trim(this.a, Math.max(n, this.size));
    }
    
    public void getElements(final int from, final Object[][] a, final long offset, final long length) {
        ObjectBigArrays.copy(this.a, from, a, offset, length);
    }
    
    public void removeElements(final int from, final int to) {
        BigArrays.ensureFromTo(this.size, from, to);
        ObjectBigArrays.copy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
        ObjectBigArrays.fill(this.a, this.size, this.size + to - from, (K)null);
    }
    
    public void addElements(final int index, final K[][] a, final long offset, final long length) {
        this.ensureIndex(index);
        ObjectBigArrays.ensureOffsetLength(a, offset, length);
        this.grow(this.size + length);
        ObjectBigArrays.copy(this.a, index, this.a, index + length, this.size - index);
        ObjectBigArrays.copy(a, offset, this.a, index, length);
        this.size += length;
    }
    
    @Override
    public ObjectBigListIterator<K> listIterator(final long index) {
        this.ensureIndex(index);
        return new AbstractObjectBigListIterator<K>() {
            long pos = index;
            long last = -1L;
            
            @Override
            public boolean hasNext() {
                return this.pos < ObjectBigArrayBigList.this.size;
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0L;
            }
            
            @Override
            public K next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final K[][] a = ObjectBigArrayBigList.this.a;
                final long n = this.pos++;
                this.last = n;
                return ObjectBigArrays.get(a, n);
            }
            
            @Override
            public K previous() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final K[][] a = ObjectBigArrayBigList.this.a;
                final long index = this.pos - 1L;
                this.pos = index;
                this.last = index;
                return ObjectBigArrays.get(a, index);
            }
            
            @Override
            public long nextIndex() {
                return this.pos;
            }
            
            @Override
            public long previousIndex() {
                return this.pos - 1L;
            }
            
            @Override
            public void add(final K k) {
                ObjectBigArrayBigList.this.add(this.pos++, k);
                this.last = -1L;
            }
            
            @Override
            public void set(final K k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                ObjectBigArrayBigList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                ObjectBigArrayBigList.this.remove(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1L;
            }
        };
    }
    
    public ObjectBigArrayBigList<K> clone() {
        final ObjectBigArrayBigList<K> c = new ObjectBigArrayBigList<K>(this.size);
        ObjectBigArrays.copy(this.a, 0L, c.a, 0L, this.size);
        c.size = this.size;
        return c;
    }
    
    private boolean valEquals(final K a, final K b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
    
    public boolean equals(final ObjectBigArrayBigList<K> l) {
        if (l == this) {
            return true;
        }
        long s = this.size64();
        if (s != l.size64()) {
            return false;
        }
        final K[][] a1 = this.a;
        final K[][] a2 = l.a;
        while (s-- != 0L) {
            if (!this.valEquals(ObjectBigArrays.get(a1, s), ObjectBigArrays.get(a2, s))) {
                return false;
            }
        }
        return true;
    }
    
    public int compareTo(final ObjectBigArrayBigList<? extends K> l) {
        final long s1 = this.size64();
        final long s2 = l.size64();
        final K[][] a1 = this.a;
        final K[][] a2 = (K[][])l.a;
        int i;
        for (i = 0; i < s1 && i < s2; ++i) {
            final K e1 = ObjectBigArrays.get(a1, i);
            final K e2 = ObjectBigArrays.get(a2, i);
            final int r;
            if ((r = ((Comparable)e1).compareTo(e2)) != 0) {
                return r;
            }
        }
        return (i < s2) ? -1 : ((i < s1) ? 1 : 0);
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeObject(ObjectBigArrays.get(this.a, i));
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = (K[][])ObjectBigArrays.newBigArray(this.size);
        for (int i = 0; i < this.size; ++i) {
            ObjectBigArrays.set(this.a, i, s.readObject());
        }
    }
}
