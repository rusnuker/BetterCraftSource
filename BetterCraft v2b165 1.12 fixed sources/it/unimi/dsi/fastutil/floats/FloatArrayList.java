// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

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

public class FloatArrayList extends AbstractFloatList implements RandomAccess, Cloneable, Serializable
{
    private static final long serialVersionUID = -7046029254386353130L;
    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    protected transient float[] a;
    protected int size;
    private static final boolean ASSERTS = false;
    
    protected FloatArrayList(final float[] a, final boolean dummy) {
        this.a = a;
    }
    
    public FloatArrayList(final int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
        }
        this.a = new float[capacity];
    }
    
    public FloatArrayList() {
        this(16);
    }
    
    public FloatArrayList(final Collection<? extends Float> c) {
        this(c.size());
        this.size = FloatIterators.unwrap(FloatIterators.asFloatIterator(c.iterator()), this.a);
    }
    
    public FloatArrayList(final FloatCollection c) {
        this(c.size());
        this.size = FloatIterators.unwrap(c.iterator(), this.a);
    }
    
    public FloatArrayList(final FloatList l) {
        this(l.size());
        l.getElements(0, this.a, 0, this.size = l.size());
    }
    
    public FloatArrayList(final float[] a) {
        this(a, 0, a.length);
    }
    
    public FloatArrayList(final float[] a, final int offset, final int length) {
        this(length);
        System.arraycopy(a, offset, this.a, 0, length);
        this.size = length;
    }
    
    public FloatArrayList(final Iterator<? extends Float> i) {
        this();
        while (i.hasNext()) {
            this.add((Float)i.next());
        }
    }
    
    public FloatArrayList(final FloatIterator i) {
        this();
        while (i.hasNext()) {
            this.add(i.nextFloat());
        }
    }
    
    public float[] elements() {
        return this.a;
    }
    
    public static FloatArrayList wrap(final float[] a, final int length) {
        if (length > a.length) {
            throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + a.length + ")");
        }
        final FloatArrayList l = new FloatArrayList(a, false);
        l.size = length;
        return l;
    }
    
    public static FloatArrayList wrap(final float[] a) {
        return wrap(a, a.length);
    }
    
    public void ensureCapacity(final int capacity) {
        this.a = FloatArrays.ensureCapacity(this.a, capacity, this.size);
    }
    
    private void grow(final int capacity) {
        this.a = FloatArrays.grow(this.a, capacity, this.size);
    }
    
    @Override
    public void add(final int index, final float k) {
        this.ensureIndex(index);
        this.grow(this.size + 1);
        if (index != this.size) {
            System.arraycopy(this.a, index, this.a, index + 1, this.size - index);
        }
        this.a[index] = k;
        ++this.size;
    }
    
    @Override
    public boolean add(final float k) {
        this.grow(this.size + 1);
        this.a[this.size++] = k;
        return true;
    }
    
    @Override
    public float getFloat(final int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        return this.a[index];
    }
    
    @Override
    public int indexOf(final float k) {
        for (int i = 0; i < this.size; ++i) {
            if (Float.floatToIntBits(k) == Float.floatToIntBits(this.a[i])) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final float k) {
        int i = this.size;
        while (i-- != 0) {
            if (Float.floatToIntBits(k) == Float.floatToIntBits(this.a[i])) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public float removeFloat(final int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final float old = this.a[index];
        --this.size;
        if (index != this.size) {
            System.arraycopy(this.a, index + 1, this.a, index, this.size - index);
        }
        return old;
    }
    
    @Override
    public boolean rem(final float k) {
        final int index = this.indexOf(k);
        if (index == -1) {
            return false;
        }
        this.removeFloat(index);
        return true;
    }
    
    @Override
    public float set(final int index, final float k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final float old = this.a[index];
        this.a[index] = k;
        return old;
    }
    
    @Override
    public void clear() {
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
            Arrays.fill(this.a, this.size, size, 0.0f);
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
        final float[] t = new float[Math.max(n, this.size)];
        System.arraycopy(this.a, 0, t, 0, this.size);
        this.a = t;
    }
    
    @Override
    public void getElements(final int from, final float[] a, final int offset, final int length) {
        FloatArrays.ensureOffsetLength(a, offset, length);
        System.arraycopy(this.a, from, a, offset, length);
    }
    
    @Override
    public void removeElements(final int from, final int to) {
        it.unimi.dsi.fastutil.Arrays.ensureFromTo(this.size, from, to);
        System.arraycopy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
    }
    
    @Override
    public void addElements(final int index, final float[] a, final int offset, final int length) {
        this.ensureIndex(index);
        FloatArrays.ensureOffsetLength(a, offset, length);
        this.grow(this.size + length);
        System.arraycopy(this.a, index, this.a, index + length, this.size - index);
        System.arraycopy(a, offset, this.a, index, length);
        this.size += length;
    }
    
    @Override
    public float[] toFloatArray(float[] a) {
        if (a == null || a.length < this.size) {
            a = new float[this.size];
        }
        System.arraycopy(this.a, 0, a, 0, this.size);
        return a;
    }
    
    @Override
    public boolean addAll(int index, final FloatCollection c) {
        this.ensureIndex(index);
        int n = c.size();
        if (n == 0) {
            return false;
        }
        this.grow(this.size + n);
        if (index != this.size) {
            System.arraycopy(this.a, index, this.a, index + n, this.size - index);
        }
        final FloatIterator i = c.iterator();
        this.size += n;
        while (n-- != 0) {
            this.a[index++] = i.nextFloat();
        }
        return true;
    }
    
    @Override
    public boolean addAll(final int index, final FloatList l) {
        this.ensureIndex(index);
        final int n = l.size();
        if (n == 0) {
            return false;
        }
        this.grow(this.size + n);
        if (index != this.size) {
            System.arraycopy(this.a, index, this.a, index + n, this.size - index);
        }
        l.getElements(0, this.a, index, n);
        this.size += n;
        return true;
    }
    
    @Override
    public boolean removeAll(final FloatCollection c) {
        final float[] a = this.a;
        int j = 0;
        for (int i = 0; i < this.size; ++i) {
            if (!c.contains(a[i])) {
                a[j++] = a[i];
            }
        }
        final boolean modified = this.size != j;
        this.size = j;
        return modified;
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        final float[] a = this.a;
        int j = 0;
        for (int i = 0; i < this.size; ++i) {
            if (!c.contains(a[i])) {
                a[j++] = a[i];
            }
        }
        final boolean modified = this.size != j;
        this.size = j;
        return modified;
    }
    
    @Override
    public FloatListIterator listIterator(final int index) {
        this.ensureIndex(index);
        return new AbstractFloatListIterator() {
            int pos = index;
            int last = -1;
            
            @Override
            public boolean hasNext() {
                return this.pos < FloatArrayList.this.size;
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0;
            }
            
            @Override
            public float nextFloat() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final float[] a = FloatArrayList.this.a;
                final int last = this.pos++;
                this.last = last;
                return a[last];
            }
            
            @Override
            public float previousFloat() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final float[] a = FloatArrayList.this.a;
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
            public void add(final float k) {
                FloatArrayList.this.add(this.pos++, k);
                this.last = -1;
            }
            
            @Override
            public void set(final float k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                FloatArrayList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                FloatArrayList.this.removeFloat(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }
        };
    }
    
    public FloatArrayList clone() {
        final FloatArrayList c = new FloatArrayList(this.size);
        System.arraycopy(this.a, 0, c.a, 0, this.size);
        c.size = this.size;
        return c;
    }
    
    public boolean equals(final FloatArrayList l) {
        if (l == this) {
            return true;
        }
        int s = this.size();
        if (s != l.size()) {
            return false;
        }
        final float[] a1 = this.a;
        final float[] a2 = l.a;
        while (s-- != 0) {
            if (a1[s] != a2[s]) {
                return false;
            }
        }
        return true;
    }
    
    public int compareTo(final FloatArrayList l) {
        final int s1 = this.size();
        final int s2 = l.size();
        final float[] a1 = this.a;
        final float[] a2 = l.a;
        int i;
        for (i = 0; i < s1 && i < s2; ++i) {
            final float e1 = a1[i];
            final float e2 = a2[i];
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
            s.writeFloat(this.a[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = new float[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.a[i] = s.readFloat();
        }
    }
}
