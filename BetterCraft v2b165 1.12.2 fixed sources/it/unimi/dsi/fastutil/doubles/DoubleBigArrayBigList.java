// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

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

public class DoubleBigArrayBigList extends AbstractDoubleBigList implements RandomAccess, Cloneable, Serializable
{
    private static final long serialVersionUID = -7046029254386353130L;
    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    protected transient double[][] a;
    protected long size;
    private static final boolean ASSERTS = false;
    
    protected DoubleBigArrayBigList(final double[][] a, final boolean dummy) {
        this.a = a;
    }
    
    public DoubleBigArrayBigList(final long capacity) {
        if (capacity < 0L) {
            throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
        }
        this.a = DoubleBigArrays.newBigArray(capacity);
    }
    
    public DoubleBigArrayBigList() {
        this(16L);
    }
    
    public DoubleBigArrayBigList(final DoubleCollection c) {
        this(c.size());
        final DoubleIterator i = c.iterator();
        while (i.hasNext()) {
            this.add(i.nextDouble());
        }
    }
    
    public DoubleBigArrayBigList(final DoubleBigList l) {
        this(l.size64());
        l.getElements(0L, this.a, 0L, this.size = l.size64());
    }
    
    public DoubleBigArrayBigList(final double[][] a) {
        this(a, 0L, DoubleBigArrays.length(a));
    }
    
    public DoubleBigArrayBigList(final double[][] a, final long offset, final long length) {
        this(length);
        DoubleBigArrays.copy(a, offset, this.a, 0L, length);
        this.size = length;
    }
    
    public DoubleBigArrayBigList(final Iterator<? extends Double> i) {
        this();
        while (i.hasNext()) {
            this.add((Double)i.next());
        }
    }
    
    public DoubleBigArrayBigList(final DoubleIterator i) {
        this();
        while (i.hasNext()) {
            this.add(i.nextDouble());
        }
    }
    
    public double[][] elements() {
        return this.a;
    }
    
    public static DoubleBigArrayBigList wrap(final double[][] a, final long length) {
        if (length > DoubleBigArrays.length(a)) {
            throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + DoubleBigArrays.length(a) + ")");
        }
        final DoubleBigArrayBigList l = new DoubleBigArrayBigList(a, false);
        l.size = length;
        return l;
    }
    
    public static DoubleBigArrayBigList wrap(final double[][] a) {
        return wrap(a, DoubleBigArrays.length(a));
    }
    
    public void ensureCapacity(final long capacity) {
        this.a = DoubleBigArrays.ensureCapacity(this.a, capacity, this.size);
    }
    
    private void grow(final long capacity) {
        this.a = DoubleBigArrays.grow(this.a, capacity, this.size);
    }
    
    @Override
    public void add(final long index, final double k) {
        this.ensureIndex(index);
        this.grow(this.size + 1L);
        if (index != this.size) {
            DoubleBigArrays.copy(this.a, index, this.a, index + 1L, this.size - index);
        }
        DoubleBigArrays.set(this.a, index, k);
        ++this.size;
    }
    
    @Override
    public boolean add(final double k) {
        this.grow(this.size + 1L);
        DoubleBigArrays.set(this.a, this.size++, k);
        return true;
    }
    
    @Override
    public double getDouble(final long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        return DoubleBigArrays.get(this.a, index);
    }
    
    @Override
    public long indexOf(final double k) {
        for (long i = 0L; i < this.size; ++i) {
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(DoubleBigArrays.get(this.a, i))) {
                return i;
            }
        }
        return -1L;
    }
    
    @Override
    public long lastIndexOf(final double k) {
        long i = this.size;
        while (i-- != 0L) {
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(DoubleBigArrays.get(this.a, i))) {
                return i;
            }
        }
        return -1L;
    }
    
    @Override
    public double removeDouble(final long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final double old = DoubleBigArrays.get(this.a, index);
        --this.size;
        if (index != this.size) {
            DoubleBigArrays.copy(this.a, index + 1L, this.a, index, this.size - index);
        }
        return old;
    }
    
    @Override
    public boolean rem(final double k) {
        final long index = this.indexOf(k);
        if (index == -1L) {
            return false;
        }
        this.removeDouble(index);
        return true;
    }
    
    @Override
    public double set(final long index, final double k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final double old = DoubleBigArrays.get(this.a, index);
        DoubleBigArrays.set(this.a, index, k);
        return old;
    }
    
    @Override
    public boolean removeAll(final DoubleCollection c) {
        double[] s = null;
        double[] d = null;
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
        double[] s = null;
        double[] d = null;
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
        if (size > DoubleBigArrays.length(this.a)) {
            this.ensureCapacity(size);
        }
        if (size > this.size) {
            DoubleBigArrays.fill(this.a, this.size, size, 0.0);
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
        final long arrayLength = DoubleBigArrays.length(this.a);
        if (n >= arrayLength || this.size == arrayLength) {
            return;
        }
        this.a = DoubleBigArrays.trim(this.a, Math.max(n, this.size));
    }
    
    public void getElements(final int from, final double[][] a, final long offset, final long length) {
        DoubleBigArrays.copy(this.a, from, a, offset, length);
    }
    
    public void removeElements(final int from, final int to) {
        BigArrays.ensureFromTo(this.size, from, to);
        DoubleBigArrays.copy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
    }
    
    public void addElements(final int index, final double[][] a, final long offset, final long length) {
        this.ensureIndex(index);
        DoubleBigArrays.ensureOffsetLength(a, offset, length);
        this.grow(this.size + length);
        DoubleBigArrays.copy(this.a, index, this.a, index + length, this.size - index);
        DoubleBigArrays.copy(a, offset, this.a, index, length);
        this.size += length;
    }
    
    @Override
    public DoubleBigListIterator listIterator(final long index) {
        this.ensureIndex(index);
        return new AbstractDoubleBigListIterator() {
            long pos = index;
            long last = -1L;
            
            @Override
            public boolean hasNext() {
                return this.pos < DoubleBigArrayBigList.this.size;
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0L;
            }
            
            @Override
            public double nextDouble() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final double[][] a = DoubleBigArrayBigList.this.a;
                final long n = this.pos++;
                this.last = n;
                return DoubleBigArrays.get(a, n);
            }
            
            @Override
            public double previousDouble() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final double[][] a = DoubleBigArrayBigList.this.a;
                final long index = this.pos - 1L;
                this.pos = index;
                this.last = index;
                return DoubleBigArrays.get(a, index);
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
            public void add(final double k) {
                DoubleBigArrayBigList.this.add(this.pos++, k);
                this.last = -1L;
            }
            
            @Override
            public void set(final double k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                DoubleBigArrayBigList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                DoubleBigArrayBigList.this.removeDouble(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1L;
            }
        };
    }
    
    public DoubleBigArrayBigList clone() {
        final DoubleBigArrayBigList c = new DoubleBigArrayBigList(this.size);
        DoubleBigArrays.copy(this.a, 0L, c.a, 0L, this.size);
        c.size = this.size;
        return c;
    }
    
    public boolean equals(final DoubleBigArrayBigList l) {
        if (l == this) {
            return true;
        }
        long s = this.size64();
        if (s != l.size64()) {
            return false;
        }
        final double[][] a1 = this.a;
        final double[][] a2 = l.a;
        while (s-- != 0L) {
            if (DoubleBigArrays.get(a1, s) != DoubleBigArrays.get(a2, s)) {
                return false;
            }
        }
        return true;
    }
    
    public int compareTo(final DoubleBigArrayBigList l) {
        final long s1 = this.size64();
        final long s2 = l.size64();
        final double[][] a1 = this.a;
        final double[][] a2 = l.a;
        int i;
        for (i = 0; i < s1 && i < s2; ++i) {
            final double e1 = DoubleBigArrays.get(a1, i);
            final double e2 = DoubleBigArrays.get(a2, i);
            final int r;
            if ((r = Double.compare(e1, e2)) != 0) {
                return r;
            }
        }
        return (i < s2) ? -1 : ((i < s1) ? 1 : 0);
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeDouble(DoubleBigArrays.get(this.a, i));
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = DoubleBigArrays.newBigArray(this.size);
        for (int i = 0; i < this.size; ++i) {
            DoubleBigArrays.set(this.a, i, s.readDouble());
        }
    }
}
