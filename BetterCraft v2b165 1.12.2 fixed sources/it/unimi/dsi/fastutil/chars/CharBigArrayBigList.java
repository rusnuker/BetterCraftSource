// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

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

public class CharBigArrayBigList extends AbstractCharBigList implements RandomAccess, Cloneable, Serializable
{
    private static final long serialVersionUID = -7046029254386353130L;
    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    protected transient char[][] a;
    protected long size;
    private static final boolean ASSERTS = false;
    
    protected CharBigArrayBigList(final char[][] a, final boolean dummy) {
        this.a = a;
    }
    
    public CharBigArrayBigList(final long capacity) {
        if (capacity < 0L) {
            throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
        }
        this.a = CharBigArrays.newBigArray(capacity);
    }
    
    public CharBigArrayBigList() {
        this(16L);
    }
    
    public CharBigArrayBigList(final CharCollection c) {
        this(c.size());
        final CharIterator i = c.iterator();
        while (i.hasNext()) {
            this.add(i.nextChar());
        }
    }
    
    public CharBigArrayBigList(final CharBigList l) {
        this(l.size64());
        l.getElements(0L, this.a, 0L, this.size = l.size64());
    }
    
    public CharBigArrayBigList(final char[][] a) {
        this(a, 0L, CharBigArrays.length(a));
    }
    
    public CharBigArrayBigList(final char[][] a, final long offset, final long length) {
        this(length);
        CharBigArrays.copy(a, offset, this.a, 0L, length);
        this.size = length;
    }
    
    public CharBigArrayBigList(final Iterator<? extends Character> i) {
        this();
        while (i.hasNext()) {
            this.add((Character)i.next());
        }
    }
    
    public CharBigArrayBigList(final CharIterator i) {
        this();
        while (i.hasNext()) {
            this.add(i.nextChar());
        }
    }
    
    public char[][] elements() {
        return this.a;
    }
    
    public static CharBigArrayBigList wrap(final char[][] a, final long length) {
        if (length > CharBigArrays.length(a)) {
            throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + CharBigArrays.length(a) + ")");
        }
        final CharBigArrayBigList l = new CharBigArrayBigList(a, false);
        l.size = length;
        return l;
    }
    
    public static CharBigArrayBigList wrap(final char[][] a) {
        return wrap(a, CharBigArrays.length(a));
    }
    
    public void ensureCapacity(final long capacity) {
        this.a = CharBigArrays.ensureCapacity(this.a, capacity, this.size);
    }
    
    private void grow(final long capacity) {
        this.a = CharBigArrays.grow(this.a, capacity, this.size);
    }
    
    @Override
    public void add(final long index, final char k) {
        this.ensureIndex(index);
        this.grow(this.size + 1L);
        if (index != this.size) {
            CharBigArrays.copy(this.a, index, this.a, index + 1L, this.size - index);
        }
        CharBigArrays.set(this.a, index, k);
        ++this.size;
    }
    
    @Override
    public boolean add(final char k) {
        this.grow(this.size + 1L);
        CharBigArrays.set(this.a, this.size++, k);
        return true;
    }
    
    @Override
    public char getChar(final long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        return CharBigArrays.get(this.a, index);
    }
    
    @Override
    public long indexOf(final char k) {
        for (long i = 0L; i < this.size; ++i) {
            if (k == CharBigArrays.get(this.a, i)) {
                return i;
            }
        }
        return -1L;
    }
    
    @Override
    public long lastIndexOf(final char k) {
        long i = this.size;
        while (i-- != 0L) {
            if (k == CharBigArrays.get(this.a, i)) {
                return i;
            }
        }
        return -1L;
    }
    
    @Override
    public char removeChar(final long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final char old = CharBigArrays.get(this.a, index);
        --this.size;
        if (index != this.size) {
            CharBigArrays.copy(this.a, index + 1L, this.a, index, this.size - index);
        }
        return old;
    }
    
    @Override
    public boolean rem(final char k) {
        final long index = this.indexOf(k);
        if (index == -1L) {
            return false;
        }
        this.removeChar(index);
        return true;
    }
    
    @Override
    public char set(final long index, final char k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        final char old = CharBigArrays.get(this.a, index);
        CharBigArrays.set(this.a, index, k);
        return old;
    }
    
    @Override
    public boolean removeAll(final CharCollection c) {
        char[] s = null;
        char[] d = null;
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
        char[] s = null;
        char[] d = null;
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
        if (size > CharBigArrays.length(this.a)) {
            this.ensureCapacity(size);
        }
        if (size > this.size) {
            CharBigArrays.fill(this.a, this.size, size, '\0');
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
        final long arrayLength = CharBigArrays.length(this.a);
        if (n >= arrayLength || this.size == arrayLength) {
            return;
        }
        this.a = CharBigArrays.trim(this.a, Math.max(n, this.size));
    }
    
    public void getElements(final int from, final char[][] a, final long offset, final long length) {
        CharBigArrays.copy(this.a, from, a, offset, length);
    }
    
    public void removeElements(final int from, final int to) {
        BigArrays.ensureFromTo(this.size, from, to);
        CharBigArrays.copy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
    }
    
    public void addElements(final int index, final char[][] a, final long offset, final long length) {
        this.ensureIndex(index);
        CharBigArrays.ensureOffsetLength(a, offset, length);
        this.grow(this.size + length);
        CharBigArrays.copy(this.a, index, this.a, index + length, this.size - index);
        CharBigArrays.copy(a, offset, this.a, index, length);
        this.size += length;
    }
    
    @Override
    public CharBigListIterator listIterator(final long index) {
        this.ensureIndex(index);
        return new AbstractCharBigListIterator() {
            long pos = index;
            long last = -1L;
            
            @Override
            public boolean hasNext() {
                return this.pos < CharBigArrayBigList.this.size;
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0L;
            }
            
            @Override
            public char nextChar() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final char[][] a = CharBigArrayBigList.this.a;
                final long n = this.pos++;
                this.last = n;
                return CharBigArrays.get(a, n);
            }
            
            @Override
            public char previousChar() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final char[][] a = CharBigArrayBigList.this.a;
                final long index = this.pos - 1L;
                this.pos = index;
                this.last = index;
                return CharBigArrays.get(a, index);
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
            public void add(final char k) {
                CharBigArrayBigList.this.add(this.pos++, k);
                this.last = -1L;
            }
            
            @Override
            public void set(final char k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                CharBigArrayBigList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                CharBigArrayBigList.this.removeChar(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1L;
            }
        };
    }
    
    public CharBigArrayBigList clone() {
        final CharBigArrayBigList c = new CharBigArrayBigList(this.size);
        CharBigArrays.copy(this.a, 0L, c.a, 0L, this.size);
        c.size = this.size;
        return c;
    }
    
    public boolean equals(final CharBigArrayBigList l) {
        if (l == this) {
            return true;
        }
        long s = this.size64();
        if (s != l.size64()) {
            return false;
        }
        final char[][] a1 = this.a;
        final char[][] a2 = l.a;
        while (s-- != 0L) {
            if (CharBigArrays.get(a1, s) != CharBigArrays.get(a2, s)) {
                return false;
            }
        }
        return true;
    }
    
    public int compareTo(final CharBigArrayBigList l) {
        final long s1 = this.size64();
        final long s2 = l.size64();
        final char[][] a1 = this.a;
        final char[][] a2 = l.a;
        int i;
        for (i = 0; i < s1 && i < s2; ++i) {
            final char e1 = CharBigArrays.get(a1, i);
            final char e2 = CharBigArrays.get(a2, i);
            final int r;
            if ((r = Character.compare(e1, e2)) != 0) {
                return r;
            }
        }
        return (i < s2) ? -1 : ((i < s1) ? 1 : 0);
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeChar(CharBigArrays.get(this.a, i));
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = CharBigArrays.newBigArray(this.size);
        for (int i = 0; i < this.size; ++i) {
            CharBigArrays.set(this.a, i, s.readChar());
        }
    }
}
