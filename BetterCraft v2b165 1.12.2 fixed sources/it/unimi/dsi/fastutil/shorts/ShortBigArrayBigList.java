// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

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

public class ShortBigArrayBigList extends AbstractShortBigList implements RandomAccess, Cloneable, Serializable
{
    private static final long serialVersionUID = -7046029254386353130L;
    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    protected transient short[][] a;
    protected long size;
    private static final boolean ASSERTS = false;
    
    protected ShortBigArrayBigList(final short[][] a, final boolean dummy) {
        this.a = a;
    }
    
    public ShortBigArrayBigList(final long capacity) {
        if (capacity < 0L) {
            throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
        }
        this.a = ShortBigArrays.newBigArray(capacity);
    }
    
    public ShortBigArrayBigList() {
        this(16L);
    }
    
    public ShortBigArrayBigList(final ShortCollection c) {
        this(c.size());
        final ShortIterator i = c.iterator();
        while (i.hasNext()) {
            this.add(i.nextShort());
        }
    }
    
    public ShortBigArrayBigList(final ShortBigList l) {
        this(l.size64());
        l.getElements(0L, this.a, 0L, this.size = l.size64());
    }
    
    public ShortBigArrayBigList(final short[][] a) {
        this(a, 0L, ShortBigArrays.length(a));
    }
    
    public ShortBigArrayBigList(final short[][] a, final long offset, final long length) {
        this(length);
        ShortBigArrays.copy(a, offset, this.a, 0L, length);
        this.size = length;
    }
    
    public ShortBigArrayBigList(final Iterator<? extends Short> i) {
        this();
        while (i.hasNext()) {
            this.add((Short)i.next());
        }
    }
    
    public ShortBigArrayBigList(final ShortIterator i) {
        this();
        while (i.hasNext()) {
            this.add(i.nextShort());
        }
    }
    
    public short[][] elements() {
        return this.a;
    }
    
    public static ShortBigArrayBigList wrap(final short[][] a, final long length) {
        if (length > ShortBigArrays.length(a)) {
            throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + ShortBigArrays.length(a) + ")");
        }
        final ShortBigArrayBigList l = new ShortBigArrayBigList(a, false);
        l.size = length;
        return l;
    }
    
    public static ShortBigArrayBigList wrap(final short[][] a) {
        return wrap(a, ShortBigArrays.length(a));
    }
    
    public void ensureCapacity(final long capacity) {
        this.a = ShortBigArrays.ensureCapacity(this.a, capacity, this.size);
    }
    
    private void grow(final long capacity) {
        this.a = ShortBigArrays.grow(this.a, capacity, this.size);
    }
    
    @Override
    public void add(final long index, final short k) {
        this.ensureIndex(index);
        this.grow(this.size + 1L);
        if (index != this.size) {
            ShortBigArrays.copy(this.a, index, this.a, index + 1L, this.size - index);
        }
        ShortBigArrays.set(this.a, index, k);
        ++this.size;
    }
    
    @Override
    public boolean add(final short k) {
        this.grow(this.size + 1L);
        ShortBigArrays.set(this.a, this.size++, k);
        return true;
    }
    
    @Override
    public short getShort(final long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        return ShortBigArrays.get(this.a, index);
    }
    
    @Override
    public long indexOf(final short k) {
        for (long i = 0L; i < this.size; ++i) {
            if (k == ShortBigArrays.get(this.a, i)) {
                return i;
            }
        }
        return -1L;
    }
    
    @Override
    public long lastIndexOf(final short k) {
        long i = this.size;
        while (i-- != 0L) {
            if (k == ShortBigArrays.get(this.a, i)) {
                return i;
            }
        }
        return -1L;
    }
    
    @Override
    public short removeShort(final long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final short old = ShortBigArrays.get(this.a, index);
        --this.size;
        if (index != this.size) {
            ShortBigArrays.copy(this.a, index + 1L, this.a, index, this.size - index);
        }
        return old;
    }
    
    @Override
    public boolean rem(final short k) {
        final long index = this.indexOf(k);
        if (index == -1L) {
            return false;
        }
        this.removeShort(index);
        return true;
    }
    
    @Override
    public short set(final long index, final short k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final short old = ShortBigArrays.get(this.a, index);
        ShortBigArrays.set(this.a, index, k);
        return old;
    }
    
    @Override
    public boolean removeAll(final ShortCollection c) {
        short[] s = null;
        short[] d = null;
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
        short[] s = null;
        short[] d = null;
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
        if (size > ShortBigArrays.length(this.a)) {
            this.ensureCapacity(size);
        }
        if (size > this.size) {
            ShortBigArrays.fill(this.a, this.size, size, (short)0);
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
        final long arrayLength = ShortBigArrays.length(this.a);
        if (n >= arrayLength || this.size == arrayLength) {
            return;
        }
        this.a = ShortBigArrays.trim(this.a, Math.max(n, this.size));
    }
    
    public void getElements(final int from, final short[][] a, final long offset, final long length) {
        ShortBigArrays.copy(this.a, from, a, offset, length);
    }
    
    public void removeElements(final int from, final int to) {
        BigArrays.ensureFromTo(this.size, from, to);
        ShortBigArrays.copy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
    }
    
    public void addElements(final int index, final short[][] a, final long offset, final long length) {
        this.ensureIndex(index);
        ShortBigArrays.ensureOffsetLength(a, offset, length);
        this.grow(this.size + length);
        ShortBigArrays.copy(this.a, index, this.a, index + length, this.size - index);
        ShortBigArrays.copy(a, offset, this.a, index, length);
        this.size += length;
    }
    
    @Override
    public ShortBigListIterator listIterator(final long index) {
        this.ensureIndex(index);
        return new AbstractShortBigListIterator() {
            long pos = index;
            long last = -1L;
            
            @Override
            public boolean hasNext() {
                return this.pos < ShortBigArrayBigList.this.size;
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0L;
            }
            
            @Override
            public short nextShort() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final short[][] a = ShortBigArrayBigList.this.a;
                final long n = this.pos++;
                this.last = n;
                return ShortBigArrays.get(a, n);
            }
            
            @Override
            public short previousShort() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final short[][] a = ShortBigArrayBigList.this.a;
                final long index = this.pos - 1L;
                this.pos = index;
                this.last = index;
                return ShortBigArrays.get(a, index);
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
            public void add(final short k) {
                ShortBigArrayBigList.this.add(this.pos++, k);
                this.last = -1L;
            }
            
            @Override
            public void set(final short k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                ShortBigArrayBigList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                ShortBigArrayBigList.this.removeShort(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1L;
            }
        };
    }
    
    public ShortBigArrayBigList clone() {
        final ShortBigArrayBigList c = new ShortBigArrayBigList(this.size);
        ShortBigArrays.copy(this.a, 0L, c.a, 0L, this.size);
        c.size = this.size;
        return c;
    }
    
    public boolean equals(final ShortBigArrayBigList l) {
        if (l == this) {
            return true;
        }
        long s = this.size64();
        if (s != l.size64()) {
            return false;
        }
        final short[][] a1 = this.a;
        final short[][] a2 = l.a;
        while (s-- != 0L) {
            if (ShortBigArrays.get(a1, s) != ShortBigArrays.get(a2, s)) {
                return false;
            }
        }
        return true;
    }
    
    public int compareTo(final ShortBigArrayBigList l) {
        final long s1 = this.size64();
        final long s2 = l.size64();
        final short[][] a1 = this.a;
        final short[][] a2 = l.a;
        int i;
        for (i = 0; i < s1 && i < s2; ++i) {
            final short e1 = ShortBigArrays.get(a1, i);
            final short e2 = ShortBigArrays.get(a2, i);
            final int r;
            if ((r = Short.compare(e1, e2)) != 0) {
                return r;
            }
        }
        return (i < s2) ? -1 : ((i < s1) ? 1 : 0);
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeShort(ShortBigArrays.get(this.a, i));
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = ShortBigArrays.newBigArray(this.size);
        for (int i = 0; i < this.size; ++i) {
            ShortBigArrays.set(this.a, i, s.readShort());
        }
    }
}
