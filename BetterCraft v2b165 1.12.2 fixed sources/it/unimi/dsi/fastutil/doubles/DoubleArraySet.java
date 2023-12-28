// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import java.util.Iterator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Collection;
import java.io.Serializable;

public class DoubleArraySet extends AbstractDoubleSet implements Serializable, Cloneable
{
    private static final long serialVersionUID = 1L;
    private transient double[] a;
    private int size;
    
    public DoubleArraySet(final double[] a) {
        this.a = a;
        this.size = a.length;
    }
    
    public DoubleArraySet() {
        this.a = DoubleArrays.EMPTY_ARRAY;
    }
    
    public DoubleArraySet(final int capacity) {
        this.a = new double[capacity];
    }
    
    public DoubleArraySet(final DoubleCollection c) {
        this(c.size());
        this.addAll(c);
    }
    
    public DoubleArraySet(final Collection<? extends Double> c) {
        this(c.size());
        this.addAll(c);
    }
    
    public DoubleArraySet(final double[] a, final int size) {
        this.a = a;
        this.size = size;
        if (size > a.length) {
            throw new IllegalArgumentException("The provided size (" + size + ") is larger than or equal to the array size (" + a.length + ")");
        }
    }
    
    private int findKey(final double o) {
        int i = this.size;
        while (i-- != 0) {
            if (Double.doubleToLongBits(this.a[i]) == Double.doubleToLongBits(o)) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public DoubleIterator iterator() {
        return new AbstractDoubleIterator() {
            int next = 0;
            
            @Override
            public boolean hasNext() {
                return this.next < DoubleArraySet.this.size;
            }
            
            @Override
            public double nextDouble() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return DoubleArraySet.this.a[this.next++];
            }
            
            @Override
            public void remove() {
                final int tail = DoubleArraySet.this.size-- - this.next--;
                System.arraycopy(DoubleArraySet.this.a, this.next + 1, DoubleArraySet.this.a, this.next, tail);
            }
        };
    }
    
    @Override
    public boolean contains(final double k) {
        return this.findKey(k) != -1;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean rem(final double k) {
        final int pos = this.findKey(k);
        if (pos == -1) {
            return false;
        }
        for (int tail = this.size - pos - 1, i = 0; i < tail; ++i) {
            this.a[pos + i] = this.a[pos + i + 1];
        }
        --this.size;
        return true;
    }
    
    @Override
    public boolean add(final double k) {
        final int pos = this.findKey(k);
        if (pos != -1) {
            return false;
        }
        if (this.size == this.a.length) {
            final double[] b = new double[(this.size == 0) ? 2 : (this.size * 2)];
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
        this.size = 0;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public DoubleArraySet clone() {
        DoubleArraySet c;
        try {
            c = (DoubleArraySet)super.clone();
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
            s.writeDouble(this.a[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = new double[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.a[i] = s.readDouble();
        }
    }
}
