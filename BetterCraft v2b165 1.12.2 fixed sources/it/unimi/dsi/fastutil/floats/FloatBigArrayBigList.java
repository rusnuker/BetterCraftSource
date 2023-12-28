// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

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

public class FloatBigArrayBigList extends AbstractFloatBigList implements RandomAccess, Cloneable, Serializable
{
    private static final long serialVersionUID = -7046029254386353130L;
    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    protected transient float[][] a;
    protected long size;
    private static final boolean ASSERTS = false;
    
    protected FloatBigArrayBigList(final float[][] a, final boolean dummy) {
        this.a = a;
    }
    
    public FloatBigArrayBigList(final long capacity) {
        if (capacity < 0L) {
            throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
        }
        this.a = FloatBigArrays.newBigArray(capacity);
    }
    
    public FloatBigArrayBigList() {
        this(16L);
    }
    
    public FloatBigArrayBigList(final FloatCollection c) {
        this(c.size());
        final FloatIterator i = c.iterator();
        while (i.hasNext()) {
            this.add(i.nextFloat());
        }
    }
    
    public FloatBigArrayBigList(final FloatBigList l) {
        this(l.size64());
        l.getElements(0L, this.a, 0L, this.size = l.size64());
    }
    
    public FloatBigArrayBigList(final float[][] a) {
        this(a, 0L, FloatBigArrays.length(a));
    }
    
    public FloatBigArrayBigList(final float[][] a, final long offset, final long length) {
        this(length);
        FloatBigArrays.copy(a, offset, this.a, 0L, length);
        this.size = length;
    }
    
    public FloatBigArrayBigList(final Iterator<? extends Float> i) {
        this();
        while (i.hasNext()) {
            this.add((Float)i.next());
        }
    }
    
    public FloatBigArrayBigList(final FloatIterator i) {
        this();
        while (i.hasNext()) {
            this.add(i.nextFloat());
        }
    }
    
    public float[][] elements() {
        return this.a;
    }
    
    public static FloatBigArrayBigList wrap(final float[][] a, final long length) {
        if (length > FloatBigArrays.length(a)) {
            throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + FloatBigArrays.length(a) + ")");
        }
        final FloatBigArrayBigList l = new FloatBigArrayBigList(a, false);
        l.size = length;
        return l;
    }
    
    public static FloatBigArrayBigList wrap(final float[][] a) {
        return wrap(a, FloatBigArrays.length(a));
    }
    
    public void ensureCapacity(final long capacity) {
        this.a = FloatBigArrays.ensureCapacity(this.a, capacity, this.size);
    }
    
    private void grow(final long capacity) {
        this.a = FloatBigArrays.grow(this.a, capacity, this.size);
    }
    
    @Override
    public void add(final long index, final float k) {
        this.ensureIndex(index);
        this.grow(this.size + 1L);
        if (index != this.size) {
            FloatBigArrays.copy(this.a, index, this.a, index + 1L, this.size - index);
        }
        FloatBigArrays.set(this.a, index, k);
        ++this.size;
    }
    
    @Override
    public boolean add(final float k) {
        this.grow(this.size + 1L);
        FloatBigArrays.set(this.a, this.size++, k);
        return true;
    }
    
    @Override
    public float getFloat(final long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        return FloatBigArrays.get(this.a, index);
    }
    
    @Override
    public long indexOf(final float k) {
        for (long i = 0L; i < this.size; ++i) {
            if (Float.floatToIntBits(k) == Float.floatToIntBits(FloatBigArrays.get(this.a, i))) {
                return i;
            }
        }
        return -1L;
    }
    
    @Override
    public long lastIndexOf(final float k) {
        long i = this.size;
        while (i-- != 0L) {
            if (Float.floatToIntBits(k) == Float.floatToIntBits(FloatBigArrays.get(this.a, i))) {
                return i;
            }
        }
        return -1L;
    }
    
    @Override
    public float removeFloat(final long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final float old = FloatBigArrays.get(this.a, index);
        --this.size;
        if (index != this.size) {
            FloatBigArrays.copy(this.a, index + 1L, this.a, index, this.size - index);
        }
        return old;
    }
    
    @Override
    public boolean rem(final float k) {
        final long index = this.indexOf(k);
        if (index == -1L) {
            return false;
        }
        this.removeFloat(index);
        return true;
    }
    
    @Override
    public float set(final long index, final float k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final float old = FloatBigArrays.get(this.a, index);
        FloatBigArrays.set(this.a, index, k);
        return old;
    }
    
    @Override
    public boolean removeAll(final FloatCollection c) {
        float[] s = null;
        float[] d = null;
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
        float[] s = null;
        float[] d = null;
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
        if (size > FloatBigArrays.length(this.a)) {
            this.ensureCapacity(size);
        }
        if (size > this.size) {
            FloatBigArrays.fill(this.a, this.size, size, 0.0f);
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
        final long arrayLength = FloatBigArrays.length(this.a);
        if (n >= arrayLength || this.size == arrayLength) {
            return;
        }
        this.a = FloatBigArrays.trim(this.a, Math.max(n, this.size));
    }
    
    public void getElements(final int from, final float[][] a, final long offset, final long length) {
        FloatBigArrays.copy(this.a, from, a, offset, length);
    }
    
    public void removeElements(final int from, final int to) {
        BigArrays.ensureFromTo(this.size, from, to);
        FloatBigArrays.copy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
    }
    
    public void addElements(final int index, final float[][] a, final long offset, final long length) {
        this.ensureIndex(index);
        FloatBigArrays.ensureOffsetLength(a, offset, length);
        this.grow(this.size + length);
        FloatBigArrays.copy(this.a, index, this.a, index + length, this.size - index);
        FloatBigArrays.copy(a, offset, this.a, index, length);
        this.size += length;
    }
    
    @Override
    public FloatBigListIterator listIterator(final long index) {
        this.ensureIndex(index);
        return new AbstractFloatBigListIterator() {
            long pos = index;
            long last = -1L;
            
            @Override
            public boolean hasNext() {
                return this.pos < FloatBigArrayBigList.this.size;
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0L;
            }
            
            @Override
            public float nextFloat() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final float[][] a = FloatBigArrayBigList.this.a;
                final long n = this.pos++;
                this.last = n;
                return FloatBigArrays.get(a, n);
            }
            
            @Override
            public float previousFloat() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final float[][] a = FloatBigArrayBigList.this.a;
                final long index = this.pos - 1L;
                this.pos = index;
                this.last = index;
                return FloatBigArrays.get(a, index);
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
            public void add(final float k) {
                FloatBigArrayBigList.this.add(this.pos++, k);
                this.last = -1L;
            }
            
            @Override
            public void set(final float k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                FloatBigArrayBigList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                FloatBigArrayBigList.this.removeFloat(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1L;
            }
        };
    }
    
    public FloatBigArrayBigList clone() {
        final FloatBigArrayBigList c = new FloatBigArrayBigList(this.size);
        FloatBigArrays.copy(this.a, 0L, c.a, 0L, this.size);
        c.size = this.size;
        return c;
    }
    
    public boolean equals(final FloatBigArrayBigList l) {
        if (l == this) {
            return true;
        }
        long s = this.size64();
        if (s != l.size64()) {
            return false;
        }
        final float[][] a1 = this.a;
        final float[][] a2 = l.a;
        while (s-- != 0L) {
            if (FloatBigArrays.get(a1, s) != FloatBigArrays.get(a2, s)) {
                return false;
            }
        }
        return true;
    }
    
    public int compareTo(final FloatBigArrayBigList l) {
        final long s1 = this.size64();
        final long s2 = l.size64();
        final float[][] a1 = this.a;
        final float[][] a2 = l.a;
        int i;
        for (i = 0; i < s1 && i < s2; ++i) {
            final float e1 = FloatBigArrays.get(a1, i);
            final float e2 = FloatBigArrays.get(a2, i);
            final int r;
            if ((r = Float.compare(e1, e2)) != 0) {
                return r;
            }
        }
        return (i < s2) ? -1 : ((i < s1) ? 1 : 0);
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeFloat(FloatBigArrays.get(this.a, i));
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = FloatBigArrays.newBigArray(this.size);
        for (int i = 0; i < this.size; ++i) {
            FloatBigArrays.set(this.a, i, s.readFloat());
        }
    }
}
