// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.BigListIterator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Collection;
import it.unimi.dsi.fastutil.BigArrays;
import java.util.Iterator;
import java.io.Serializable;
import java.util.RandomAccess;

public class IntBigArrayBigList extends AbstractIntBigList implements RandomAccess, Cloneable, Serializable
{
    private static final long serialVersionUID = -7046029254386353130L;
    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    protected transient int[][] a;
    protected long size;
    private static final boolean ASSERTS = false;
    
    protected IntBigArrayBigList(final int[][] a, final boolean dummy) {
        this.a = a;
    }
    
    public IntBigArrayBigList(final long capacity) {
        if (capacity < 0L) {
            throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
        }
        this.a = IntBigArrays.newBigArray(capacity);
    }
    
    public IntBigArrayBigList() {
        this(16L);
    }
    
    public IntBigArrayBigList(final IntCollection c) {
        this(c.size());
        final IntIterator i = c.iterator();
        while (i.hasNext()) {
            this.add(i.nextInt());
        }
    }
    
    public IntBigArrayBigList(final IntBigList l) {
        this(l.size64());
        l.getElements(0L, this.a, 0L, this.size = l.size64());
    }
    
    public IntBigArrayBigList(final int[][] a) {
        this(a, 0L, IntBigArrays.length(a));
    }
    
    public IntBigArrayBigList(final int[][] a, final long offset, final long length) {
        this(length);
        IntBigArrays.copy(a, offset, this.a, 0L, length);
        this.size = length;
    }
    
    public IntBigArrayBigList(final Iterator<? extends Integer> i) {
        this();
        while (i.hasNext()) {
            this.add((Integer)i.next());
        }
    }
    
    public IntBigArrayBigList(final IntIterator i) {
        this();
        while (i.hasNext()) {
            this.add(i.nextInt());
        }
    }
    
    public int[][] elements() {
        return this.a;
    }
    
    public static IntBigArrayBigList wrap(final int[][] a, final long length) {
        if (length > IntBigArrays.length(a)) {
            throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + IntBigArrays.length(a) + ")");
        }
        final IntBigArrayBigList l = new IntBigArrayBigList(a, false);
        l.size = length;
        return l;
    }
    
    public static IntBigArrayBigList wrap(final int[][] a) {
        return wrap(a, IntBigArrays.length(a));
    }
    
    public void ensureCapacity(final long capacity) {
        this.a = IntBigArrays.ensureCapacity(this.a, capacity, this.size);
    }
    
    private void grow(final long capacity) {
        this.a = IntBigArrays.grow(this.a, capacity, this.size);
    }
    
    @Override
    public void add(final long index, final int k) {
        this.ensureIndex(index);
        this.grow(this.size + 1L);
        if (index != this.size) {
            IntBigArrays.copy(this.a, index, this.a, index + 1L, this.size - index);
        }
        IntBigArrays.set(this.a, index, k);
        ++this.size;
    }
    
    @Override
    public boolean add(final int k) {
        this.grow(this.size + 1L);
        IntBigArrays.set(this.a, this.size++, k);
        return true;
    }
    
    @Override
    public int getInt(final long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        return IntBigArrays.get(this.a, index);
    }
    
    @Override
    public long indexOf(final int k) {
        for (long i = 0L; i < this.size; ++i) {
            if (k == IntBigArrays.get(this.a, i)) {
                return i;
            }
        }
        return -1L;
    }
    
    @Override
    public long lastIndexOf(final int k) {
        long i = this.size;
        while (i-- != 0L) {
            if (k == IntBigArrays.get(this.a, i)) {
                return i;
            }
        }
        return -1L;
    }
    
    @Override
    public int removeInt(final long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final int old = IntBigArrays.get(this.a, index);
        --this.size;
        if (index != this.size) {
            IntBigArrays.copy(this.a, index + 1L, this.a, index, this.size - index);
        }
        return old;
    }
    
    @Override
    public boolean rem(final int k) {
        final long index = this.indexOf(k);
        if (index == -1L) {
            return false;
        }
        this.removeInt(index);
        return true;
    }
    
    @Override
    public int set(final long index, final int k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final int old = IntBigArrays.get(this.a, index);
        IntBigArrays.set(this.a, index, k);
        return old;
    }
    
    @Override
    public boolean removeAll(final IntCollection c) {
        int[] s = null;
        int[] d = null;
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
    public boolean removeAll(final Collection<?> c) {
        int[] s = null;
        int[] d = null;
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
        this.size = 0L;
    }
    
    @Override
    public long size64() {
        return this.size;
    }
    
    @Override
    public void size(final long size) {
        if (size > IntBigArrays.length(this.a)) {
            this.ensureCapacity(size);
        }
        if (size > this.size) {
            IntBigArrays.fill(this.a, this.size, size, 0);
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
        final long arrayLength = IntBigArrays.length(this.a);
        if (n >= arrayLength || this.size == arrayLength) {
            return;
        }
        this.a = IntBigArrays.trim(this.a, Math.max(n, this.size));
    }
    
    public void getElements(final int from, final int[][] a, final long offset, final long length) {
        IntBigArrays.copy(this.a, from, a, offset, length);
    }
    
    public void removeElements(final int from, final int to) {
        BigArrays.ensureFromTo(this.size, from, to);
        IntBigArrays.copy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
    }
    
    public void addElements(final int index, final int[][] a, final long offset, final long length) {
        this.ensureIndex(index);
        IntBigArrays.ensureOffsetLength(a, offset, length);
        this.grow(this.size + length);
        IntBigArrays.copy(this.a, index, this.a, index + length, this.size - index);
        IntBigArrays.copy(a, offset, this.a, index, length);
        this.size += length;
    }
    
    @Override
    public IntBigListIterator listIterator(final long index) {
        this.ensureIndex(index);
        return new AbstractIntBigListIterator() {
            long pos = index;
            long last = -1L;
            
            @Override
            public boolean hasNext() {
                return this.pos < IntBigArrayBigList.this.size;
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0L;
            }
            
            @Override
            public int nextInt() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final int[][] a = IntBigArrayBigList.this.a;
                final long n = this.pos++;
                this.last = n;
                return IntBigArrays.get(a, n);
            }
            
            @Override
            public int previousInt() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final int[][] a = IntBigArrayBigList.this.a;
                final long index = this.pos - 1L;
                this.pos = index;
                this.last = index;
                return IntBigArrays.get(a, index);
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
            public void add(final int k) {
                IntBigArrayBigList.this.add(this.pos++, k);
                this.last = -1L;
            }
            
            @Override
            public void set(final int k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                IntBigArrayBigList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                IntBigArrayBigList.this.removeInt(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1L;
            }
        };
    }
    
    public IntBigArrayBigList clone() {
        final IntBigArrayBigList c = new IntBigArrayBigList(this.size);
        IntBigArrays.copy(this.a, 0L, c.a, 0L, this.size);
        c.size = this.size;
        return c;
    }
    
    public boolean equals(final IntBigArrayBigList l) {
        if (l == this) {
            return true;
        }
        long s = this.size64();
        if (s != l.size64()) {
            return false;
        }
        final int[][] a1 = this.a;
        final int[][] a2 = l.a;
        while (s-- != 0L) {
            if (IntBigArrays.get(a1, s) != IntBigArrays.get(a2, s)) {
                return false;
            }
        }
        return true;
    }
    
    public int compareTo(final IntBigArrayBigList l) {
        final long s1 = this.size64();
        final long s2 = l.size64();
        final int[][] a1 = this.a;
        final int[][] a2 = l.a;
        int i;
        for (i = 0; i < s1 && i < s2; ++i) {
            final int e1 = IntBigArrays.get(a1, i);
            final int e2 = IntBigArrays.get(a2, i);
            final int r;
            if ((r = Integer.compare(e1, e2)) != 0) {
                return r;
            }
        }
        return (i < s2) ? -1 : ((i < s1) ? 1 : 0);
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeInt(IntBigArrays.get(this.a, i));
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = IntBigArrays.newBigArray(this.size);
        for (int i = 0; i < this.size; ++i) {
            IntBigArrays.set(this.a, i, s.readInt());
        }
    }
}
