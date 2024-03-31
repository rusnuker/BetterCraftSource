// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.ListIterator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import java.io.Serializable;
import java.util.RandomAccess;

public class ObjectArrayList<K> extends AbstractObjectList<K> implements RandomAccess, Cloneable, Serializable
{
    private static final long serialVersionUID = -7046029254386353131L;
    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    protected final boolean wrapped;
    protected transient K[] a;
    protected int size;
    private static final boolean ASSERTS = false;
    
    protected ObjectArrayList(final K[] a, final boolean dummy) {
        this.a = a;
        this.wrapped = true;
    }
    
    public ObjectArrayList(final int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
        }
        this.a = (K[])new Object[capacity];
        this.wrapped = false;
    }
    
    public ObjectArrayList() {
        this(16);
    }
    
    public ObjectArrayList(final Collection<? extends K> c) {
        this(c.size());
        this.size = ObjectIterators.unwrap(c.iterator(), this.a);
    }
    
    public ObjectArrayList(final ObjectCollection<? extends K> c) {
        this(c.size());
        this.size = ObjectIterators.unwrap(c.iterator(), this.a);
    }
    
    public ObjectArrayList(final ObjectList<? extends K> l) {
        this(l.size());
        l.getElements(0, this.a, 0, this.size = l.size());
    }
    
    public ObjectArrayList(final K[] a) {
        this(a, 0, a.length);
    }
    
    public ObjectArrayList(final K[] a, final int offset, final int length) {
        this(length);
        System.arraycopy(a, offset, this.a, 0, length);
        this.size = length;
    }
    
    public ObjectArrayList(final Iterator<? extends K> i) {
        this();
        while (i.hasNext()) {
            this.add(i.next());
        }
    }
    
    public ObjectArrayList(final ObjectIterator<? extends K> i) {
        this();
        while (i.hasNext()) {
            this.add(i.next());
        }
    }
    
    public K[] elements() {
        return this.a;
    }
    
    public static <K> ObjectArrayList<K> wrap(final K[] a, final int length) {
        if (length > a.length) {
            throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + a.length + ")");
        }
        final ObjectArrayList<K> l = new ObjectArrayList<K>(a, false);
        l.size = length;
        return l;
    }
    
    public static <K> ObjectArrayList<K> wrap(final K[] a) {
        return wrap(a, a.length);
    }
    
    public void ensureCapacity(final int capacity) {
        if (this.wrapped) {
            this.a = ObjectArrays.ensureCapacity(this.a, capacity, this.size);
        }
        else if (capacity > this.a.length) {
            final Object[] t = new Object[capacity];
            System.arraycopy(this.a, 0, t, 0, this.size);
            this.a = (K[])t;
        }
    }
    
    private void grow(final int capacity) {
        if (this.wrapped) {
            this.a = ObjectArrays.grow(this.a, capacity, this.size);
        }
        else if (capacity > this.a.length) {
            final int newLength = (int)Math.max(Math.min(2L * this.a.length, 2147483639L), capacity);
            final Object[] t = new Object[newLength];
            System.arraycopy(this.a, 0, t, 0, this.size);
            this.a = (K[])t;
        }
    }
    
    @Override
    public void add(final int index, final K k) {
        this.ensureIndex(index);
        this.grow(this.size + 1);
        if (index != this.size) {
            System.arraycopy(this.a, index, this.a, index + 1, this.size - index);
        }
        this.a[index] = k;
        ++this.size;
    }
    
    @Override
    public boolean add(final K k) {
        this.grow(this.size + 1);
        this.a[this.size++] = k;
        return true;
    }
    
    @Override
    public K get(final int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        return this.a[index];
    }
    
    @Override
    public int indexOf(final Object k) {
        for (int i = 0; i < this.size; ++i) {
            if (k == null) {
                if (this.a[i] == null) {
                    return i;
                }
            }
            else if (k.equals(this.a[i])) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final Object k) {
        int i = this.size;
        while (i-- != 0) {
            if (k == null) {
                if (this.a[i] != null) {
                    continue;
                }
            }
            else if (!k.equals(this.a[i])) {
                continue;
            }
            return i;
        }
        return -1;
    }
    
    @Override
    public K remove(final int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final K old = this.a[index];
        --this.size;
        if (index != this.size) {
            System.arraycopy(this.a, index + 1, this.a, index, this.size - index);
        }
        this.a[this.size] = null;
        return old;
    }
    
    @Override
    public boolean rem(final Object k) {
        final int index = this.indexOf(k);
        if (index == -1) {
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
    public K set(final int index, final K k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final K old = this.a[index];
        this.a[index] = k;
        return old;
    }
    
    @Override
    public void clear() {
        Arrays.fill(this.a, 0, this.size, null);
        this.size = 0;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public void size(final int size) {
        if (size > this.a.length) {
            this.ensureCapacity(size);
        }
        if (size > this.size) {
            Arrays.fill(this.a, this.size, size, null);
        }
        else {
            Arrays.fill(this.a, size, this.size, null);
        }
        this.size = size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public void trim() {
        this.trim(0);
    }
    
    public void trim(final int n) {
        if (n >= this.a.length || this.size == this.a.length) {
            return;
        }
        final K[] t = (K[])new Object[Math.max(n, this.size)];
        System.arraycopy(this.a, 0, t, 0, this.size);
        this.a = t;
    }
    
    @Override
    public void getElements(final int from, final Object[] a, final int offset, final int length) {
        ObjectArrays.ensureOffsetLength(a, offset, length);
        System.arraycopy(this.a, from, a, offset, length);
    }
    
    @Override
    public void removeElements(final int from, final int to) {
        it.unimi.dsi.fastutil.Arrays.ensureFromTo(this.size, from, to);
        System.arraycopy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
        int i = to - from;
        while (i-- != 0) {
            this.a[this.size + i] = null;
        }
    }
    
    @Override
    public void addElements(final int index, final K[] a, final int offset, final int length) {
        this.ensureIndex(index);
        ObjectArrays.ensureOffsetLength(a, offset, length);
        this.grow(this.size + length);
        System.arraycopy(this.a, index, this.a, index + length, this.size - index);
        System.arraycopy(a, offset, this.a, index, length);
        this.size += length;
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        final Object[] a = this.a;
        int j = 0;
        for (int i = 0; i < this.size; ++i) {
            if (!c.contains(a[i])) {
                a[j++] = a[i];
            }
        }
        Arrays.fill(a, j, this.size, null);
        final boolean modified = this.size != j;
        this.size = j;
        return modified;
    }
    
    @Override
    public ObjectListIterator<K> listIterator(final int index) {
        this.ensureIndex(index);
        return new AbstractObjectListIterator<K>() {
            int pos = index;
            int last = -1;
            
            @Override
            public boolean hasNext() {
                return this.pos < ObjectArrayList.this.size;
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0;
            }
            
            @Override
            public K next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final K[] a = ObjectArrayList.this.a;
                final int last = this.pos++;
                this.last = last;
                return a[last];
            }
            
            @Override
            public K previous() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final K[] a = ObjectArrayList.this.a;
                final int n = this.pos - 1;
                this.pos = n;
                this.last = n;
                return a[n];
            }
            
            @Override
            public int nextIndex() {
                return this.pos;
            }
            
            @Override
            public int previousIndex() {
                return this.pos - 1;
            }
            
            @Override
            public void add(final K k) {
                ObjectArrayList.this.add(this.pos++, k);
                this.last = -1;
            }
            
            @Override
            public void set(final K k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                ObjectArrayList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                ObjectArrayList.this.remove(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }
        };
    }
    
    public ObjectArrayList<K> clone() {
        final ObjectArrayList<K> c = new ObjectArrayList<K>(this.size);
        System.arraycopy(this.a, 0, c.a, 0, this.size);
        c.size = this.size;
        return c;
    }
    
    private boolean valEquals(final K a, final K b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
    
    public boolean equals(final ObjectArrayList<K> l) {
        if (l == this) {
            return true;
        }
        int s = this.size();
        if (s != l.size()) {
            return false;
        }
        final K[] a1 = this.a;
        final K[] a2 = l.a;
        while (s-- != 0) {
            if (!this.valEquals(a1[s], a2[s])) {
                return false;
            }
        }
        return true;
    }
    
    public int compareTo(final ObjectArrayList<? extends K> l) {
        final int s1 = this.size();
        final int s2 = l.size();
        final K[] a1 = this.a;
        final K[] a2 = (K[])l.a;
        int i;
        for (i = 0; i < s1 && i < s2; ++i) {
            final K e1 = a1[i];
            final K e2 = a2[i];
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
            s.writeObject(this.a[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = (K[])new Object[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.a[i] = (K)s.readObject();
        }
    }
}
