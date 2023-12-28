// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.io.Serializable;
import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.BigList;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;

public abstract class AbstractIntBigList extends AbstractIntCollection implements IntBigList, IntStack
{
    protected AbstractIntBigList() {
    }
    
    protected void ensureIndex(final long index) {
        if (index < 0L) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index > this.size64()) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + this.size64() + ")");
        }
    }
    
    protected void ensureRestrictedIndex(final long index) {
        if (index < 0L) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index >= this.size64()) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size64() + ")");
        }
    }
    
    @Override
    public void add(final long index, final int k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final int k) {
        this.add(this.size64(), k);
        return true;
    }
    
    @Override
    public int removeInt(final long i) {
        throw new UnsupportedOperationException();
    }
    
    public int removeInt(final int i) {
        return this.removeInt((long)i);
    }
    
    @Override
    public int set(final long index, final int k) {
        throw new UnsupportedOperationException();
    }
    
    public int set(final int index, final int k) {
        return this.set((long)index, k);
    }
    
    @Override
    public boolean addAll(long index, final Collection<? extends Integer> c) {
        this.ensureIndex(index);
        int n = c.size();
        if (n == 0) {
            return false;
        }
        final Iterator<? extends Integer> i = c.iterator();
        while (n-- != 0) {
            this.add(index++, (Integer)i.next());
        }
        return true;
    }
    
    public boolean addAll(final int index, final Collection<? extends Integer> c) {
        return this.addAll((long)index, c);
    }
    
    @Override
    public boolean addAll(final Collection<? extends Integer> c) {
        return this.addAll(this.size64(), c);
    }
    
    @Override
    public IntBigListIterator iterator() {
        return this.listIterator();
    }
    
    @Override
    public IntBigListIterator listIterator() {
        return this.listIterator(0L);
    }
    
    @Override
    public IntBigListIterator listIterator(final long index) {
        this.ensureIndex(index);
        return new AbstractIntBigListIterator() {
            long pos = index;
            long last = -1L;
            
            @Override
            public boolean hasNext() {
                return this.pos < AbstractIntBigList.this.size64();
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
                final AbstractIntBigList this$0 = AbstractIntBigList.this;
                final long last = this.pos++;
                this.last = last;
                return this$0.getInt(last);
            }
            
            @Override
            public int previousInt() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractIntBigList this$0 = AbstractIntBigList.this;
                final long n = this.pos - 1L;
                this.pos = n;
                this.last = n;
                return this$0.getInt(n);
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
                AbstractIntBigList.this.add(this.pos++, k);
                this.last = -1L;
            }
            
            @Override
            public void set(final int k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractIntBigList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractIntBigList.this.removeInt(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1L;
            }
        };
    }
    
    public IntBigListIterator listIterator(final int index) {
        return this.listIterator((long)index);
    }
    
    @Override
    public boolean contains(final int k) {
        return this.indexOf(k) >= 0L;
    }
    
    @Override
    public long indexOf(final int k) {
        final IntBigListIterator i = this.listIterator();
        while (i.hasNext()) {
            final int e = i.nextInt();
            if (k == e) {
                return i.previousIndex();
            }
        }
        return -1L;
    }
    
    @Override
    public long lastIndexOf(final int k) {
        final IntBigListIterator i = this.listIterator(this.size64());
        while (i.hasPrevious()) {
            final int e = i.previousInt();
            if (k == e) {
                return i.nextIndex();
            }
        }
        return -1L;
    }
    
    @Override
    public void size(final long size) {
        long i = this.size64();
        if (size > i) {
            while (i++ < size) {
                this.add(0);
            }
        }
        else {
            while (i-- != size) {
                this.remove(i);
            }
        }
    }
    
    public void size(final int size) {
        this.size((long)size);
    }
    
    @Override
    public IntBigList subList(final long from, final long to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new IntSubList(this, from, to);
    }
    
    @Override
    public void removeElements(final long from, final long to) {
        this.ensureIndex(to);
        final IntBigListIterator i = this.listIterator(from);
        long n = to - from;
        if (n < 0L) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        while (n-- != 0L) {
            i.nextInt();
            i.remove();
        }
    }
    
    @Override
    public void addElements(long index, final int[][] a, long offset, long length) {
        this.ensureIndex(index);
        IntBigArrays.ensureOffsetLength(a, offset, length);
        while (length-- != 0L) {
            this.add(index++, IntBigArrays.get(a, offset++));
        }
    }
    
    @Override
    public void addElements(final long index, final int[][] a) {
        this.addElements(index, a, 0L, IntBigArrays.length(a));
    }
    
    @Override
    public void getElements(final long from, final int[][] a, long offset, long length) {
        final IntBigListIterator i = this.listIterator(from);
        IntBigArrays.ensureOffsetLength(a, offset, length);
        if (from + length > this.size64()) {
            throw new IndexOutOfBoundsException("End index (" + (from + length) + ") is greater than list size (" + this.size64() + ")");
        }
        while (length-- != 0L) {
            IntBigArrays.set(a, offset++, i.nextInt());
        }
    }
    
    @Deprecated
    @Override
    public int size() {
        return (int)Math.min(2147483647L, this.size64());
    }
    
    private boolean valEquals(final Object a, final Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BigList)) {
            return false;
        }
        final BigList<?> l = (BigList<?>)o;
        long s = this.size64();
        if (s != l.size64()) {
            return false;
        }
        if (l instanceof IntBigList) {
            final IntBigListIterator i1 = this.listIterator();
            final IntBigListIterator i2 = ((IntBigList)l).listIterator();
            while (s-- != 0L) {
                if (i1.nextInt() != i2.nextInt()) {
                    return false;
                }
            }
            return true;
        }
        final BigListIterator<?> i3 = this.listIterator();
        final BigListIterator<?> i4 = l.listIterator();
        while (s-- != 0L) {
            if (!this.valEquals(i3.next(), i4.next())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int compareTo(final BigList<? extends Integer> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof IntBigList) {
            final IntBigListIterator i1 = this.listIterator();
            final IntBigListIterator i2 = ((IntBigList)l).listIterator();
            while (i1.hasNext() && i2.hasNext()) {
                final int e1 = i1.nextInt();
                final int e2 = i2.nextInt();
                final int r;
                if ((r = Integer.compare(e1, e2)) != 0) {
                    return r;
                }
            }
            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
        }
        final BigListIterator<? extends Integer> i3 = this.listIterator();
        final BigListIterator<? extends Integer> i4 = l.listIterator();
        while (i3.hasNext() && i4.hasNext()) {
            final int r;
            if ((r = i3.next().compareTo(i4.next())) != 0) {
                return r;
            }
        }
        return i4.hasNext() ? -1 : (i3.hasNext() ? 1 : 0);
    }
    
    @Override
    public int hashCode() {
        final IntIterator i = this.iterator();
        int h = 1;
        long s = this.size64();
        while (s-- != 0L) {
            final int k = i.nextInt();
            h = 31 * h + k;
        }
        return h;
    }
    
    @Override
    public void push(final int o) {
        this.add(o);
    }
    
    @Override
    public int popInt() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.removeInt(this.size64() - 1L);
    }
    
    @Override
    public int topInt() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getInt(this.size64() - 1L);
    }
    
    @Override
    public int peekInt(final int i) {
        return this.getInt(this.size64() - 1L - i);
    }
    
    public int getInt(final int index) {
        return this.getInt(index);
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
    public boolean addAll(final long index, final IntCollection c) {
        return this.addAll(index, (Collection<? extends Integer>)c);
    }
    
    @Override
    public boolean addAll(final long index, final IntBigList l) {
        return this.addAll(index, (IntCollection)l);
    }
    
    @Override
    public boolean addAll(final IntCollection c) {
        return this.addAll(this.size64(), c);
    }
    
    @Override
    public boolean addAll(final IntBigList l) {
        return this.addAll(this.size64(), l);
    }
    
    @Deprecated
    @Override
    public void add(final long index, final Integer ok) {
        this.add(index, (int)ok);
    }
    
    @Deprecated
    @Override
    public Integer set(final long index, final Integer ok) {
        return this.set(index, (int)ok);
    }
    
    @Deprecated
    @Override
    public Integer get(final long index) {
        return this.getInt(index);
    }
    
    @Deprecated
    @Override
    public long indexOf(final Object ok) {
        return this.indexOf((int)ok);
    }
    
    @Deprecated
    @Override
    public long lastIndexOf(final Object ok) {
        return this.lastIndexOf((int)ok);
    }
    
    @Deprecated
    public Integer remove(final int index) {
        return this.removeInt(index);
    }
    
    @Deprecated
    @Override
    public Integer remove(final long index) {
        return this.removeInt(index);
    }
    
    @Deprecated
    @Override
    public void push(final Integer o) {
        this.push((int)o);
    }
    
    @Deprecated
    @Override
    public Integer pop() {
        return this.popInt();
    }
    
    @Deprecated
    @Override
    public Integer top() {
        return this.topInt();
    }
    
    @Deprecated
    @Override
    public Integer peek(final int i) {
        return this.peekInt(i);
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final IntIterator i = this.iterator();
        long n = this.size64();
        boolean first = true;
        s.append("[");
        while (n-- != 0L) {
            if (first) {
                first = false;
            }
            else {
                s.append(", ");
            }
            final int k = i.nextInt();
            s.append(String.valueOf(k));
        }
        s.append("]");
        return s.toString();
    }
    
    public static class IntSubList extends AbstractIntBigList implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntBigList l;
        protected final long from;
        protected long to;
        private static final boolean ASSERTS = false;
        
        public IntSubList(final IntBigList l, final long from, final long to) {
            this.l = l;
            this.from = from;
            this.to = to;
        }
        
        private void assertRange() {
        }
        
        @Override
        public boolean add(final int k) {
            this.l.add(this.to, k);
            ++this.to;
            return true;
        }
        
        @Override
        public void add(final long index, final int k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Integer> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        @Override
        public int getInt(final long index) {
            this.ensureRestrictedIndex(index);
            return this.l.getInt(this.from + index);
        }
        
        @Override
        public int removeInt(final long index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeInt(this.from + index);
        }
        
        @Override
        public int set(final long index, final int k) {
            this.ensureRestrictedIndex(index);
            return this.l.set(this.from + index, k);
        }
        
        @Override
        public void clear() {
            this.removeElements(0L, this.size64());
        }
        
        @Override
        public long size64() {
            return this.to - this.from;
        }
        
        @Override
        public void getElements(final long from, final int[][] a, final long offset, final long length) {
            this.ensureIndex(from);
            if (from + length > this.size64()) {
                throw new IndexOutOfBoundsException("End index (" + from + length + ") is greater than list size (" + this.size64() + ")");
            }
            this.l.getElements(this.from + from, a, offset, length);
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            this.l.removeElements(this.from + from, this.from + to);
            this.to -= to - from;
        }
        
        @Override
        public void addElements(final long index, final int[][] a, final long offset, final long length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
        }
        
        @Override
        public IntBigListIterator listIterator(final long index) {
            this.ensureIndex(index);
            return new AbstractIntBigListIterator() {
                long pos = index;
                long last = -1L;
                
                @Override
                public boolean hasNext() {
                    return this.pos < IntSubList.this.size64();
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
                    final IntBigList l = IntSubList.this.l;
                    final long from = IntSubList.this.from;
                    final long last = this.pos++;
                    this.last = last;
                    return l.getInt(from + last);
                }
                
                @Override
                public int previousInt() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final IntBigList l = IntSubList.this.l;
                    final long from = IntSubList.this.from;
                    final long n = this.pos - 1L;
                    this.pos = n;
                    this.last = n;
                    return l.getInt(from + n);
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
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    IntSubList.this.add(this.pos++, k);
                    this.last = -1L;
                }
                
                @Override
                public void set(final int k) {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    IntSubList.this.set(this.last, k);
                }
                
                @Override
                public void remove() {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    IntSubList.this.removeInt(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1L;
                }
            };
        }
        
        @Override
        public IntBigList subList(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new IntSubList(this, from, to);
        }
        
        @Override
        public boolean rem(final int k) {
            final long index = this.indexOf(k);
            if (index == -1L) {
                return false;
            }
            --this.to;
            this.l.removeInt(this.from + index);
            return true;
        }
        
        @Override
        public boolean remove(final Object o) {
            return this.rem((int)o);
        }
        
        @Override
        public boolean addAll(final long index, final IntCollection c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        public boolean addAll(final long index, final IntList l) {
            this.ensureIndex(index);
            this.to += l.size();
            return this.l.addAll(this.from + index, l);
        }
    }
}
