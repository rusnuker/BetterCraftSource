// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

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

public class LongBigArrayBigList extends AbstractLongBigList implements RandomAccess, Cloneable, Serializable
{
    private static final long serialVersionUID = -7046029254386353130L;
    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    protected transient long[][] a;
    protected long size;
    private static final boolean ASSERTS = false;
    
    protected LongBigArrayBigList(final long[][] a, final boolean dummy) {
        this.a = a;
    }
    
    public LongBigArrayBigList(final long capacity) {
        if (capacity < 0L) {
            throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
        }
        this.a = LongBigArrays.newBigArray(capacity);
    }
    
    public LongBigArrayBigList() {
        this(16L);
    }
    
    public LongBigArrayBigList(final LongCollection c) {
        this(c.size());
        final LongIterator i = c.iterator();
        while (i.hasNext()) {
            this.add(i.nextLong());
        }
    }
    
    public LongBigArrayBigList(final LongBigList l) {
        this(l.size64());
        l.getElements(0L, this.a, 0L, this.size = l.size64());
    }
    
    public LongBigArrayBigList(final long[][] a) {
        this(a, 0L, LongBigArrays.length(a));
    }
    
    public LongBigArrayBigList(final long[][] a, final long offset, final long length) {
        this(length);
        LongBigArrays.copy(a, offset, this.a, 0L, length);
        this.size = length;
    }
    
    public LongBigArrayBigList(final Iterator<? extends Long> i) {
        this();
        while (i.hasNext()) {
            this.add((Long)i.next());
        }
    }
    
    public LongBigArrayBigList(final LongIterator i) {
        this();
        while (i.hasNext()) {
            this.add(i.nextLong());
        }
    }
    
    public long[][] elements() {
        return this.a;
    }
    
    public static LongBigArrayBigList wrap(final long[][] a, final long length) {
        if (length > LongBigArrays.length(a)) {
            throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + LongBigArrays.length(a) + ")");
        }
        final LongBigArrayBigList l = new LongBigArrayBigList(a, false);
        l.size = length;
        return l;
    }
    
    public static LongBigArrayBigList wrap(final long[][] a) {
        return wrap(a, LongBigArrays.length(a));
    }
    
    public void ensureCapacity(final long capacity) {
        this.a = LongBigArrays.ensureCapacity(this.a, capacity, this.size);
    }
    
    private void grow(final long capacity) {
        this.a = LongBigArrays.grow(this.a, capacity, this.size);
    }
    
    @Override
    public void add(final long index, final long k) {
        this.ensureIndex(index);
        this.grow(this.size + 1L);
        if (index != this.size) {
            LongBigArrays.copy(this.a, index, this.a, index + 1L, this.size - index);
        }
        LongBigArrays.set(this.a, index, k);
        ++this.size;
    }
    
    @Override
    public boolean add(final long k) {
        this.grow(this.size + 1L);
        LongBigArrays.set(this.a, this.size++, k);
        return true;
    }
    
    @Override
    public long getLong(final long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        return LongBigArrays.get(this.a, index);
    }
    
    @Override
    public long indexOf(final long k) {
        for (long i = 0L; i < this.size; ++i) {
            if (k == LongBigArrays.get(this.a, i)) {
                return i;
            }
        }
        return -1L;
    }
    
    @Override
    public long lastIndexOf(final long k) {
        long i = this.size;
        while (i-- != 0L) {
            if (k == LongBigArrays.get(this.a, i)) {
                return i;
            }
        }
        return -1L;
    }
    
    @Override
    public long removeLong(final long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final long old = LongBigArrays.get(this.a, index);
        --this.size;
        if (index != this.size) {
            LongBigArrays.copy(this.a, index + 1L, this.a, index, this.size - index);
        }
        return old;
    }
    
    @Override
    public boolean rem(final long k) {
        final long index = this.indexOf(k);
        if (index == -1L) {
            return false;
        }
        this.removeLong(index);
        return true;
    }
    
    @Override
    public long set(final long index, final long k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final long old = LongBigArrays.get(this.a, index);
        LongBigArrays.set(this.a, index, k);
        return old;
    }
    
    @Override
    public boolean removeAll(final LongCollection c) {
        long[] s = null;
        long[] d = null;
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
        long[] s = null;
        long[] d = null;
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
        if (size > LongBigArrays.length(this.a)) {
            this.ensureCapacity(size);
        }
        if (size > this.size) {
            LongBigArrays.fill(this.a, this.size, size, 0L);
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
        final long arrayLength = LongBigArrays.length(this.a);
        if (n >= arrayLength || this.size == arrayLength) {
            return;
        }
        this.a = LongBigArrays.trim(this.a, Math.max(n, this.size));
    }
    
    public void getElements(final int from, final long[][] a, final long offset, final long length) {
        LongBigArrays.copy(this.a, from, a, offset, length);
    }
    
    public void removeElements(final int from, final int to) {
        BigArrays.ensureFromTo(this.size, from, to);
        LongBigArrays.copy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
    }
    
    public void addElements(final int index, final long[][] a, final long offset, final long length) {
        this.ensureIndex(index);
        LongBigArrays.ensureOffsetLength(a, offset, length);
        this.grow(this.size + length);
        LongBigArrays.copy(this.a, index, this.a, index + length, this.size - index);
        LongBigArrays.copy(a, offset, this.a, index, length);
        this.size += length;
    }
    
    @Override
    public LongBigListIterator listIterator(final long index) {
        this.ensureIndex(index);
        return new AbstractLongBigListIterator() {
            long pos = index;
            long last = -1L;
            
            @Override
            public boolean hasNext() {
                return this.pos < LongBigArrayBigList.this.size;
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0L;
            }
            
            @Override
            public long nextLong() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final long[][] a = LongBigArrayBigList.this.a;
                final long n = this.pos++;
                this.last = n;
                return LongBigArrays.get(a, n);
            }
            
            @Override
            public long previousLong() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final long[][] a = LongBigArrayBigList.this.a;
                final long index = this.pos - 1L;
                this.pos = index;
                this.last = index;
                return LongBigArrays.get(a, index);
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
            public void add(final long k) {
                LongBigArrayBigList.this.add(this.pos++, k);
                this.last = -1L;
            }
            
            @Override
            public void set(final long k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                LongBigArrayBigList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                LongBigArrayBigList.this.removeLong(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1L;
            }
        };
    }
    
    public LongBigArrayBigList clone() {
        final LongBigArrayBigList c = new LongBigArrayBigList(this.size);
        LongBigArrays.copy(this.a, 0L, c.a, 0L, this.size);
        c.size = this.size;
        return c;
    }
    
    public boolean equals(final LongBigArrayBigList l) {
        if (l == this) {
            return true;
        }
        long s = this.size64();
        if (s != l.size64()) {
            return false;
        }
        final long[][] a1 = this.a;
        final long[][] a2 = l.a;
        while (s-- != 0L) {
            if (LongBigArrays.get(a1, s) != LongBigArrays.get(a2, s)) {
                return false;
            }
        }
        return true;
    }
    
    public int compareTo(final LongBigArrayBigList l) {
        final long s1 = this.size64();
        final long s2 = l.size64();
        final long[][] a1 = this.a;
        final long[][] a2 = l.a;
        int i;
        for (i = 0; i < s1 && i < s2; ++i) {
            final long e1 = LongBigArrays.get(a1, i);
            final long e2 = LongBigArrays.get(a2, i);
            final int r;
            if ((r = Long.compare(e1, e2)) != 0) {
                return r;
            }
        }
        return (i < s2) ? -1 : ((i < s1) ? 1 : 0);
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeLong(LongBigArrays.get(this.a, i));
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = LongBigArrays.newBigArray(this.size);
        for (int i = 0; i < this.size; ++i) {
            LongBigArrays.set(this.a, i, s.readLong());
        }
    }
}
