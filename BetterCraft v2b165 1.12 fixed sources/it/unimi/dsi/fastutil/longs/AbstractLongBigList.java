// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.io.Serializable;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.BigList;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;

public abstract class AbstractLongBigList extends AbstractLongCollection implements LongBigList, LongStack
{
    protected AbstractLongBigList() {
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
    public void add(final long index, final long k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final long k) {
        this.add(this.size64(), k);
        return true;
    }
    
    @Override
    public long removeLong(final long i) {
        throw new UnsupportedOperationException();
    }
    
    public long removeLong(final int i) {
        return this.removeLong((long)i);
    }
    
    @Override
    public long set(final long index, final long k) {
        throw new UnsupportedOperationException();
    }
    
    public long set(final int index, final long k) {
        return this.set((long)index, k);
    }
    
    @Override
    public boolean addAll(long index, final Collection<? extends Long> c) {
        this.ensureIndex(index);
        int n = c.size();
        if (n == 0) {
            return false;
        }
        final Iterator<? extends Long> i = c.iterator();
        while (n-- != 0) {
            this.add(index++, (Long)i.next());
        }
        return true;
    }
    
    public boolean addAll(final int index, final Collection<? extends Long> c) {
        return this.addAll((long)index, c);
    }
    
    @Override
    public boolean addAll(final Collection<? extends Long> c) {
        return this.addAll(this.size64(), c);
    }
    
    @Override
    public LongBigListIterator iterator() {
        return this.listIterator();
    }
    
    @Override
    public LongBigListIterator listIterator() {
        return this.listIterator(0L);
    }
    
    @Override
    public LongBigListIterator listIterator(final long index) {
        this.ensureIndex(index);
        return new AbstractLongBigListIterator() {
            long pos = index;
            long last = -1L;
            
            @Override
            public boolean hasNext() {
                return this.pos < AbstractLongBigList.this.size64();
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
                final AbstractLongBigList this$0 = AbstractLongBigList.this;
                final long last = this.pos++;
                this.last = last;
                return this$0.getLong(last);
            }
            
            @Override
            public long previousLong() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractLongBigList this$0 = AbstractLongBigList.this;
                final long n = this.pos - 1L;
                this.pos = n;
                this.last = n;
                return this$0.getLong(n);
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
                AbstractLongBigList.this.add(this.pos++, k);
                this.last = -1L;
            }
            
            @Override
            public void set(final long k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractLongBigList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractLongBigList.this.removeLong(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1L;
            }
        };
    }
    
    public LongBigListIterator listIterator(final int index) {
        return this.listIterator((long)index);
    }
    
    @Override
    public boolean contains(final long k) {
        return this.indexOf(k) >= 0L;
    }
    
    @Override
    public long indexOf(final long k) {
        final LongBigListIterator i = this.listIterator();
        while (i.hasNext()) {
            final long e = i.nextLong();
            if (k == e) {
                return i.previousIndex();
            }
        }
        return -1L;
    }
    
    @Override
    public long lastIndexOf(final long k) {
        final LongBigListIterator i = this.listIterator(this.size64());
        while (i.hasPrevious()) {
            final long e = i.previousLong();
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
                this.add(0L);
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
    public LongBigList subList(final long from, final long to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new LongSubList(this, from, to);
    }
    
    @Override
    public void removeElements(final long from, final long to) {
        this.ensureIndex(to);
        final LongBigListIterator i = this.listIterator(from);
        long n = to - from;
        if (n < 0L) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        while (n-- != 0L) {
            i.nextLong();
            i.remove();
        }
    }
    
    @Override
    public void addElements(long index, final long[][] a, long offset, long length) {
        this.ensureIndex(index);
        LongBigArrays.ensureOffsetLength(a, offset, length);
        while (length-- != 0L) {
            this.add(index++, LongBigArrays.get(a, offset++));
        }
    }
    
    @Override
    public void addElements(final long index, final long[][] a) {
        this.addElements(index, a, 0L, LongBigArrays.length(a));
    }
    
    @Override
    public void getElements(final long from, final long[][] a, long offset, long length) {
        final LongBigListIterator i = this.listIterator(from);
        LongBigArrays.ensureOffsetLength(a, offset, length);
        if (from + length > this.size64()) {
            throw new IndexOutOfBoundsException("End index (" + (from + length) + ") is greater than list size (" + this.size64() + ")");
        }
        while (length-- != 0L) {
            LongBigArrays.set(a, offset++, i.nextLong());
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
        if (l instanceof LongBigList) {
            final LongBigListIterator i1 = this.listIterator();
            final LongBigListIterator i2 = ((LongBigList)l).listIterator();
            while (s-- != 0L) {
                if (i1.nextLong() != i2.nextLong()) {
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
    public int compareTo(final BigList<? extends Long> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof LongBigList) {
            final LongBigListIterator i1 = this.listIterator();
            final LongBigListIterator i2 = ((LongBigList)l).listIterator();
            while (i1.hasNext() && i2.hasNext()) {
                final long e1 = i1.nextLong();
                final long e2 = i2.nextLong();
                final int r;
                if ((r = Long.compare(e1, e2)) != 0) {
                    return r;
                }
            }
            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
        }
        final BigListIterator<? extends Long> i3 = this.listIterator();
        final BigListIterator<? extends Long> i4 = l.listIterator();
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
        final LongIterator i = this.iterator();
        int h = 1;
        long s = this.size64();
        while (s-- != 0L) {
            final long k = i.nextLong();
            h = 31 * h + HashCommon.long2int(k);
        }
        return h;
    }
    
    @Override
    public void push(final long o) {
        this.add(o);
    }
    
    @Override
    public long popLong() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.removeLong(this.size64() - 1L);
    }
    
    @Override
    public long topLong() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getLong(this.size64() - 1L);
    }
    
    @Override
    public long peekLong(final int i) {
        return this.getLong(this.size64() - 1L - i);
    }
    
    public long getLong(final int index) {
        return this.getLong(index);
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
    public boolean addAll(final long index, final LongCollection c) {
        return this.addAll(index, (Collection<? extends Long>)c);
    }
    
    @Override
    public boolean addAll(final long index, final LongBigList l) {
        return this.addAll(index, (LongCollection)l);
    }
    
    @Override
    public boolean addAll(final LongCollection c) {
        return this.addAll(this.size64(), c);
    }
    
    @Override
    public boolean addAll(final LongBigList l) {
        return this.addAll(this.size64(), l);
    }
    
    @Deprecated
    @Override
    public void add(final long index, final Long ok) {
        this.add(index, (long)ok);
    }
    
    @Deprecated
    @Override
    public Long set(final long index, final Long ok) {
        return this.set(index, (long)ok);
    }
    
    @Deprecated
    @Override
    public Long get(final long index) {
        return this.getLong(index);
    }
    
    @Deprecated
    @Override
    public long indexOf(final Object ok) {
        return this.indexOf((long)ok);
    }
    
    @Deprecated
    @Override
    public long lastIndexOf(final Object ok) {
        return this.lastIndexOf((long)ok);
    }
    
    @Deprecated
    public Long remove(final int index) {
        return this.removeLong(index);
    }
    
    @Deprecated
    @Override
    public Long remove(final long index) {
        return this.removeLong(index);
    }
    
    @Deprecated
    @Override
    public void push(final Long o) {
        this.push((long)o);
    }
    
    @Deprecated
    @Override
    public Long pop() {
        return this.popLong();
    }
    
    @Deprecated
    @Override
    public Long top() {
        return this.topLong();
    }
    
    @Deprecated
    @Override
    public Long peek(final int i) {
        return this.peekLong(i);
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final LongIterator i = this.iterator();
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
            final long k = i.nextLong();
            s.append(String.valueOf(k));
        }
        s.append("]");
        return s.toString();
    }
    
    public static class LongSubList extends AbstractLongBigList implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongBigList l;
        protected final long from;
        protected long to;
        private static final boolean ASSERTS = false;
        
        public LongSubList(final LongBigList l, final long from, final long to) {
            this.l = l;
            this.from = from;
            this.to = to;
        }
        
        private void assertRange() {
        }
        
        @Override
        public boolean add(final long k) {
            this.l.add(this.to, k);
            ++this.to;
            return true;
        }
        
        @Override
        public void add(final long index, final long k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Long> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        @Override
        public long getLong(final long index) {
            this.ensureRestrictedIndex(index);
            return this.l.getLong(this.from + index);
        }
        
        @Override
        public long removeLong(final long index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeLong(this.from + index);
        }
        
        @Override
        public long set(final long index, final long k) {
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
        public void getElements(final long from, final long[][] a, final long offset, final long length) {
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
        public void addElements(final long index, final long[][] a, final long offset, final long length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
        }
        
        @Override
        public LongBigListIterator listIterator(final long index) {
            this.ensureIndex(index);
            return new AbstractLongBigListIterator() {
                long pos = index;
                long last = -1L;
                
                @Override
                public boolean hasNext() {
                    return this.pos < LongSubList.this.size64();
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
                    final LongBigList l = LongSubList.this.l;
                    final long from = LongSubList.this.from;
                    final long last = this.pos++;
                    this.last = last;
                    return l.getLong(from + last);
                }
                
                @Override
                public long previousLong() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final LongBigList l = LongSubList.this.l;
                    final long from = LongSubList.this.from;
                    final long n = this.pos - 1L;
                    this.pos = n;
                    this.last = n;
                    return l.getLong(from + n);
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
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    LongSubList.this.add(this.pos++, k);
                    this.last = -1L;
                }
                
                @Override
                public void set(final long k) {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    LongSubList.this.set(this.last, k);
                }
                
                @Override
                public void remove() {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    LongSubList.this.removeLong(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1L;
                }
            };
        }
        
        @Override
        public LongBigList subList(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new LongSubList(this, from, to);
        }
        
        @Override
        public boolean rem(final long k) {
            final long index = this.indexOf(k);
            if (index == -1L) {
                return false;
            }
            --this.to;
            this.l.removeLong(this.from + index);
            return true;
        }
        
        @Override
        public boolean remove(final Object o) {
            return this.rem((long)o);
        }
        
        @Override
        public boolean addAll(final long index, final LongCollection c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        public boolean addAll(final long index, final LongList l) {
            this.ensureIndex(index);
            this.to += l.size();
            return this.l.addAll(this.from + index, l);
        }
    }
}
