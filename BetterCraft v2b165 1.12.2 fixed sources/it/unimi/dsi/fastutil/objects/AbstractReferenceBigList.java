// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;
import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.BigList;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;
import it.unimi.dsi.fastutil.Stack;

public abstract class AbstractReferenceBigList<K> extends AbstractReferenceCollection<K> implements ReferenceBigList<K>, Stack<K>
{
    protected AbstractReferenceBigList() {
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
    public void add(final long index, final K k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final K k) {
        this.add(this.size64(), k);
        return true;
    }
    
    @Override
    public K remove(final long i) {
        throw new UnsupportedOperationException();
    }
    
    public K remove(final int i) {
        return this.remove((long)i);
    }
    
    @Override
    public K set(final long index, final K k) {
        throw new UnsupportedOperationException();
    }
    
    public K set(final int index, final K k) {
        return this.set((long)index, k);
    }
    
    @Override
    public boolean addAll(long index, final Collection<? extends K> c) {
        this.ensureIndex(index);
        int n = c.size();
        if (n == 0) {
            return false;
        }
        final Iterator<? extends K> i = c.iterator();
        while (n-- != 0) {
            this.add(index++, i.next());
        }
        return true;
    }
    
    public boolean addAll(final int index, final Collection<? extends K> c) {
        return this.addAll((long)index, c);
    }
    
    @Override
    public boolean addAll(final Collection<? extends K> c) {
        return this.addAll(this.size64(), c);
    }
    
    @Override
    public ObjectBigListIterator<K> iterator() {
        return this.listIterator();
    }
    
    @Override
    public ObjectBigListIterator<K> listIterator() {
        return this.listIterator(0L);
    }
    
    @Override
    public ObjectBigListIterator<K> listIterator(final long index) {
        this.ensureIndex(index);
        return new AbstractObjectBigListIterator<K>() {
            long pos = index;
            long last = -1L;
            
            @Override
            public boolean hasNext() {
                return this.pos < AbstractReferenceBigList.this.size64();
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0L;
            }
            
            @Override
            public K next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractReferenceBigList this$0 = AbstractReferenceBigList.this;
                final long last = this.pos++;
                this.last = last;
                return (K)this$0.get(last);
            }
            
            @Override
            public K previous() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractReferenceBigList this$0 = AbstractReferenceBigList.this;
                final long n = this.pos - 1L;
                this.pos = n;
                this.last = n;
                return (K)this$0.get(n);
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
            public void add(final K k) {
                AbstractReferenceBigList.this.add(this.pos++, k);
                this.last = -1L;
            }
            
            @Override
            public void set(final K k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractReferenceBigList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractReferenceBigList.this.remove(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1L;
            }
        };
    }
    
    public ObjectBigListIterator<K> listIterator(final int index) {
        return this.listIterator((long)index);
    }
    
    @Override
    public boolean contains(final Object k) {
        return this.indexOf(k) >= 0L;
    }
    
    @Override
    public long indexOf(final Object k) {
        final ObjectBigListIterator<K> i = this.listIterator();
        while (i.hasNext()) {
            final K e = i.next();
            if (k == e) {
                return i.previousIndex();
            }
        }
        return -1L;
    }
    
    @Override
    public long lastIndexOf(final Object k) {
        final ObjectBigListIterator<K> i = this.listIterator(this.size64());
        while (i.hasPrevious()) {
            final K e = i.previous();
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
                this.add(null);
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
    public ReferenceBigList<K> subList(final long from, final long to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new ReferenceSubList<K>(this, from, to);
    }
    
    @Override
    public void removeElements(final long from, final long to) {
        this.ensureIndex(to);
        final ObjectBigListIterator<K> i = this.listIterator(from);
        long n = to - from;
        if (n < 0L) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        while (n-- != 0L) {
            i.next();
            i.remove();
        }
    }
    
    @Override
    public void addElements(long index, final K[][] a, long offset, long length) {
        this.ensureIndex(index);
        ObjectBigArrays.ensureOffsetLength(a, offset, length);
        while (length-- != 0L) {
            this.add(index++, ObjectBigArrays.get(a, offset++));
        }
    }
    
    @Override
    public void addElements(final long index, final K[][] a) {
        this.addElements(index, a, 0L, ObjectBigArrays.length(a));
    }
    
    @Override
    public void getElements(final long from, final Object[][] a, long offset, long length) {
        final ObjectBigListIterator<K> i = this.listIterator(from);
        ObjectBigArrays.ensureOffsetLength(a, offset, length);
        if (from + length > this.size64()) {
            throw new IndexOutOfBoundsException("End index (" + (from + length) + ") is greater than list size (" + this.size64() + ")");
        }
        while (length-- != 0L) {
            ObjectBigArrays.set(a, offset++, i.next());
        }
    }
    
    @Deprecated
    @Override
    public int size() {
        return (int)Math.min(2147483647L, this.size64());
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
        final BigListIterator<?> i1 = this.listIterator();
        final BigListIterator<?> i2 = l.listIterator();
        while (s-- != 0L) {
            if (i1.next() != i2.next()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        final ObjectIterator<K> i = this.iterator();
        int h = 1;
        long s = this.size64();
        while (s-- != 0L) {
            final K k = i.next();
            h = 31 * h + System.identityHashCode(k);
        }
        return h;
    }
    
    @Override
    public void push(final K o) {
        this.add(o);
    }
    
    @Override
    public K pop() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.remove(this.size64() - 1L);
    }
    
    @Override
    public K top() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.get(this.size64() - 1L);
    }
    
    @Override
    public K peek(final int i) {
        return this.get(this.size64() - 1L - i);
    }
    
    public K get(final int index) {
        return this.get(index);
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<K> i = this.iterator();
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
            final K k = i.next();
            if (this == k) {
                s.append("(this big list)");
            }
            else {
                s.append(String.valueOf(k));
            }
        }
        s.append("]");
        return s.toString();
    }
    
    public static class ReferenceSubList<K> extends AbstractReferenceBigList<K> implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ReferenceBigList<K> l;
        protected final long from;
        protected long to;
        private static final boolean ASSERTS = false;
        
        public ReferenceSubList(final ReferenceBigList<K> l, final long from, final long to) {
            this.l = l;
            this.from = from;
            this.to = to;
        }
        
        private void assertRange() {
        }
        
        @Override
        public boolean add(final K k) {
            this.l.add(this.to, k);
            ++this.to;
            return true;
        }
        
        @Override
        public void add(final long index, final K k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends K> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, (Collection<?>)c);
        }
        
        @Override
        public K get(final long index) {
            this.ensureRestrictedIndex(index);
            return this.l.get(this.from + index);
        }
        
        @Override
        public K remove(final long index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.remove(this.from + index);
        }
        
        @Override
        public K set(final long index, final K k) {
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
        public void getElements(final long from, final Object[][] a, final long offset, final long length) {
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
        public void addElements(final long index, final K[][] a, final long offset, final long length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
        }
        
        @Override
        public ObjectBigListIterator<K> listIterator(final long index) {
            this.ensureIndex(index);
            return new AbstractObjectBigListIterator<K>() {
                long pos = index;
                long last = -1L;
                
                @Override
                public boolean hasNext() {
                    return this.pos < ReferenceSubList.this.size64();
                }
                
                @Override
                public boolean hasPrevious() {
                    return this.pos > 0L;
                }
                
                @Override
                public K next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final ReferenceBigList<K> l = ReferenceSubList.this.l;
                    final long from = ReferenceSubList.this.from;
                    final long last = this.pos++;
                    this.last = last;
                    return (K)l.get(from + last);
                }
                
                @Override
                public K previous() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final ReferenceBigList<K> l = ReferenceSubList.this.l;
                    final long from = ReferenceSubList.this.from;
                    final long n = this.pos - 1L;
                    this.pos = n;
                    this.last = n;
                    return (K)l.get(from + n);
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
                public void add(final K k) {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    ReferenceSubList.this.add(this.pos++, k);
                    this.last = -1L;
                }
                
                @Override
                public void set(final K k) {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    ReferenceSubList.this.set(this.last, k);
                }
                
                @Override
                public void remove() {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    ReferenceSubList.this.remove(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1L;
                }
            };
        }
        
        @Override
        public ReferenceBigList<K> subList(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new ReferenceSubList((ReferenceBigList<Object>)this, from, to);
        }
        
        @Override
        public boolean remove(final Object o) {
            final long index = this.indexOf(o);
            if (index == -1L) {
                return false;
            }
            this.remove(index);
            return true;
        }
    }
}
