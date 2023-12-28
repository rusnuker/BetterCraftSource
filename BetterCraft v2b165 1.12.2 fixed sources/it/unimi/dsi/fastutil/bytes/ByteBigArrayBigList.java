// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

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

public class ByteBigArrayBigList extends AbstractByteBigList implements RandomAccess, Cloneable, Serializable
{
    private static final long serialVersionUID = -7046029254386353130L;
    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    protected transient byte[][] a;
    protected long size;
    private static final boolean ASSERTS = false;
    
    protected ByteBigArrayBigList(final byte[][] a, final boolean dummy) {
        this.a = a;
    }
    
    public ByteBigArrayBigList(final long capacity) {
        if (capacity < 0L) {
            throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
        }
        this.a = ByteBigArrays.newBigArray(capacity);
    }
    
    public ByteBigArrayBigList() {
        this(16L);
    }
    
    public ByteBigArrayBigList(final ByteCollection c) {
        this(c.size());
        final ByteIterator i = c.iterator();
        while (i.hasNext()) {
            this.add(i.nextByte());
        }
    }
    
    public ByteBigArrayBigList(final ByteBigList l) {
        this(l.size64());
        l.getElements(0L, this.a, 0L, this.size = l.size64());
    }
    
    public ByteBigArrayBigList(final byte[][] a) {
        this(a, 0L, ByteBigArrays.length(a));
    }
    
    public ByteBigArrayBigList(final byte[][] a, final long offset, final long length) {
        this(length);
        ByteBigArrays.copy(a, offset, this.a, 0L, length);
        this.size = length;
    }
    
    public ByteBigArrayBigList(final Iterator<? extends Byte> i) {
        this();
        while (i.hasNext()) {
            this.add((Byte)i.next());
        }
    }
    
    public ByteBigArrayBigList(final ByteIterator i) {
        this();
        while (i.hasNext()) {
            this.add(i.nextByte());
        }
    }
    
    public byte[][] elements() {
        return this.a;
    }
    
    public static ByteBigArrayBigList wrap(final byte[][] a, final long length) {
        if (length > ByteBigArrays.length(a)) {
            throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + ByteBigArrays.length(a) + ")");
        }
        final ByteBigArrayBigList l = new ByteBigArrayBigList(a, false);
        l.size = length;
        return l;
    }
    
    public static ByteBigArrayBigList wrap(final byte[][] a) {
        return wrap(a, ByteBigArrays.length(a));
    }
    
    public void ensureCapacity(final long capacity) {
        this.a = ByteBigArrays.ensureCapacity(this.a, capacity, this.size);
    }
    
    private void grow(final long capacity) {
        this.a = ByteBigArrays.grow(this.a, capacity, this.size);
    }
    
    @Override
    public void add(final long index, final byte k) {
        this.ensureIndex(index);
        this.grow(this.size + 1L);
        if (index != this.size) {
            ByteBigArrays.copy(this.a, index, this.a, index + 1L, this.size - index);
        }
        ByteBigArrays.set(this.a, index, k);
        ++this.size;
    }
    
    @Override
    public boolean add(final byte k) {
        this.grow(this.size + 1L);
        ByteBigArrays.set(this.a, this.size++, k);
        return true;
    }
    
    @Override
    public byte getByte(final long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        return ByteBigArrays.get(this.a, index);
    }
    
    @Override
    public long indexOf(final byte k) {
        for (long i = 0L; i < this.size; ++i) {
            if (k == ByteBigArrays.get(this.a, i)) {
                return i;
            }
        }
        return -1L;
    }
    
    @Override
    public long lastIndexOf(final byte k) {
        long i = this.size;
        while (i-- != 0L) {
            if (k == ByteBigArrays.get(this.a, i)) {
                return i;
            }
        }
        return -1L;
    }
    
    @Override
    public byte removeByte(final long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final byte old = ByteBigArrays.get(this.a, index);
        --this.size;
        if (index != this.size) {
            ByteBigArrays.copy(this.a, index + 1L, this.a, index, this.size - index);
        }
        return old;
    }
    
    @Override
    public boolean rem(final byte k) {
        final long index = this.indexOf(k);
        if (index == -1L) {
            return false;
        }
        this.removeByte(index);
        return true;
    }
    
    @Override
    public byte set(final long index, final byte k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final byte old = ByteBigArrays.get(this.a, index);
        ByteBigArrays.set(this.a, index, k);
        return old;
    }
    
    @Override
    public boolean removeAll(final ByteCollection c) {
        byte[] s = null;
        byte[] d = null;
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
        byte[] s = null;
        byte[] d = null;
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
        if (size > ByteBigArrays.length(this.a)) {
            this.ensureCapacity(size);
        }
        if (size > this.size) {
            ByteBigArrays.fill(this.a, this.size, size, (byte)0);
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
        final long arrayLength = ByteBigArrays.length(this.a);
        if (n >= arrayLength || this.size == arrayLength) {
            return;
        }
        this.a = ByteBigArrays.trim(this.a, Math.max(n, this.size));
    }
    
    public void getElements(final int from, final byte[][] a, final long offset, final long length) {
        ByteBigArrays.copy(this.a, from, a, offset, length);
    }
    
    public void removeElements(final int from, final int to) {
        BigArrays.ensureFromTo(this.size, from, to);
        ByteBigArrays.copy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
    }
    
    public void addElements(final int index, final byte[][] a, final long offset, final long length) {
        this.ensureIndex(index);
        ByteBigArrays.ensureOffsetLength(a, offset, length);
        this.grow(this.size + length);
        ByteBigArrays.copy(this.a, index, this.a, index + length, this.size - index);
        ByteBigArrays.copy(a, offset, this.a, index, length);
        this.size += length;
    }
    
    @Override
    public ByteBigListIterator listIterator(final long index) {
        this.ensureIndex(index);
        return new AbstractByteBigListIterator() {
            long pos = index;
            long last = -1L;
            
            @Override
            public boolean hasNext() {
                return this.pos < ByteBigArrayBigList.this.size;
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0L;
            }
            
            @Override
            public byte nextByte() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final byte[][] a = ByteBigArrayBigList.this.a;
                final long n = this.pos++;
                this.last = n;
                return ByteBigArrays.get(a, n);
            }
            
            @Override
            public byte previousByte() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final byte[][] a = ByteBigArrayBigList.this.a;
                final long index = this.pos - 1L;
                this.pos = index;
                this.last = index;
                return ByteBigArrays.get(a, index);
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
            public void add(final byte k) {
                ByteBigArrayBigList.this.add(this.pos++, k);
                this.last = -1L;
            }
            
            @Override
            public void set(final byte k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                ByteBigArrayBigList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                ByteBigArrayBigList.this.removeByte(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1L;
            }
        };
    }
    
    public ByteBigArrayBigList clone() {
        final ByteBigArrayBigList c = new ByteBigArrayBigList(this.size);
        ByteBigArrays.copy(this.a, 0L, c.a, 0L, this.size);
        c.size = this.size;
        return c;
    }
    
    public boolean equals(final ByteBigArrayBigList l) {
        if (l == this) {
            return true;
        }
        long s = this.size64();
        if (s != l.size64()) {
            return false;
        }
        final byte[][] a1 = this.a;
        final byte[][] a2 = l.a;
        while (s-- != 0L) {
            if (ByteBigArrays.get(a1, s) != ByteBigArrays.get(a2, s)) {
                return false;
            }
        }
        return true;
    }
    
    public int compareTo(final ByteBigArrayBigList l) {
        final long s1 = this.size64();
        final long s2 = l.size64();
        final byte[][] a1 = this.a;
        final byte[][] a2 = l.a;
        int i;
        for (i = 0; i < s1 && i < s2; ++i) {
            final byte e1 = ByteBigArrays.get(a1, i);
            final byte e2 = ByteBigArrays.get(a2, i);
            final int r;
            if ((r = Byte.compare(e1, e2)) != 0) {
                return r;
            }
        }
        return (i < s2) ? -1 : ((i < s1) ? 1 : 0);
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeByte(ByteBigArrays.get(this.a, i));
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = ByteBigArrays.newBigArray(this.size);
        for (int i = 0; i < this.size; ++i) {
            ByteBigArrays.set(this.a, i, s.readByte());
        }
    }
}
