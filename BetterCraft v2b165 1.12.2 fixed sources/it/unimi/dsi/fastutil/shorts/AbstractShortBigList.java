// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.io.Serializable;
import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.BigList;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;

public abstract class AbstractShortBigList extends AbstractShortCollection implements ShortBigList, ShortStack
{
    protected AbstractShortBigList() {
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
    public void add(final long index, final short k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final short k) {
        this.add(this.size64(), k);
        return true;
    }
    
    @Override
    public short removeShort(final long i) {
        throw new UnsupportedOperationException();
    }
    
    public short removeShort(final int i) {
        return this.removeShort((long)i);
    }
    
    @Override
    public short set(final long index, final short k) {
        throw new UnsupportedOperationException();
    }
    
    public short set(final int index, final short k) {
        return this.set((long)index, k);
    }
    
    @Override
    public boolean addAll(long index, final Collection<? extends Short> c) {
        this.ensureIndex(index);
        int n = c.size();
        if (n == 0) {
            return false;
        }
        final Iterator<? extends Short> i = c.iterator();
        while (n-- != 0) {
            this.add(index++, (Short)i.next());
        }
        return true;
    }
    
    public boolean addAll(final int index, final Collection<? extends Short> c) {
        return this.addAll((long)index, c);
    }
    
    @Override
    public boolean addAll(final Collection<? extends Short> c) {
        return this.addAll(this.size64(), c);
    }
    
    @Override
    public ShortBigListIterator iterator() {
        return this.listIterator();
    }
    
    @Override
    public ShortBigListIterator listIterator() {
        return this.listIterator(0L);
    }
    
    @Override
    public ShortBigListIterator listIterator(final long index) {
        this.ensureIndex(index);
        return new AbstractShortBigListIterator() {
            long pos = index;
            long last = -1L;
            
            @Override
            public boolean hasNext() {
                return this.pos < AbstractShortBigList.this.size64();
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
                final AbstractShortBigList this$0 = AbstractShortBigList.this;
                final long last = this.pos++;
                this.last = last;
                return this$0.getShort(last);
            }
            
            @Override
            public short previousShort() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractShortBigList this$0 = AbstractShortBigList.this;
                final long n = this.pos - 1L;
                this.pos = n;
                this.last = n;
                return this$0.getShort(n);
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
                AbstractShortBigList.this.add(this.pos++, k);
                this.last = -1L;
            }
            
            @Override
            public void set(final short k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractShortBigList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractShortBigList.this.removeShort(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1L;
            }
        };
    }
    
    public ShortBigListIterator listIterator(final int index) {
        return this.listIterator((long)index);
    }
    
    @Override
    public boolean contains(final short k) {
        return this.indexOf(k) >= 0L;
    }
    
    @Override
    public long indexOf(final short k) {
        final ShortBigListIterator i = this.listIterator();
        while (i.hasNext()) {
            final short e = i.nextShort();
            if (k == e) {
                return i.previousIndex();
            }
        }
        return -1L;
    }
    
    @Override
    public long lastIndexOf(final short k) {
        final ShortBigListIterator i = this.listIterator(this.size64());
        while (i.hasPrevious()) {
            final short e = i.previousShort();
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
                this.add((short)0);
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
    public ShortBigList subList(final long from, final long to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new ShortSubList(this, from, to);
    }
    
    @Override
    public void removeElements(final long from, final long to) {
        this.ensureIndex(to);
        final ShortBigListIterator i = this.listIterator(from);
        long n = to - from;
        if (n < 0L) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        while (n-- != 0L) {
            i.nextShort();
            i.remove();
        }
    }
    
    @Override
    public void addElements(long index, final short[][] a, long offset, long length) {
        this.ensureIndex(index);
        ShortBigArrays.ensureOffsetLength(a, offset, length);
        while (length-- != 0L) {
            this.add(index++, ShortBigArrays.get(a, offset++));
        }
    }
    
    @Override
    public void addElements(final long index, final short[][] a) {
        this.addElements(index, a, 0L, ShortBigArrays.length(a));
    }
    
    @Override
    public void getElements(final long from, final short[][] a, long offset, long length) {
        final ShortBigListIterator i = this.listIterator(from);
        ShortBigArrays.ensureOffsetLength(a, offset, length);
        if (from + length > this.size64()) {
            throw new IndexOutOfBoundsException("End index (" + (from + length) + ") is greater than list size (" + this.size64() + ")");
        }
        while (length-- != 0L) {
            ShortBigArrays.set(a, offset++, i.nextShort());
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
        if (l instanceof ShortBigList) {
            final ShortBigListIterator i1 = this.listIterator();
            final ShortBigListIterator i2 = ((ShortBigList)l).listIterator();
            while (s-- != 0L) {
                if (i1.nextShort() != i2.nextShort()) {
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
    public int compareTo(final BigList<? extends Short> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof ShortBigList) {
            final ShortBigListIterator i1 = this.listIterator();
            final ShortBigListIterator i2 = ((ShortBigList)l).listIterator();
            while (i1.hasNext() && i2.hasNext()) {
                final short e1 = i1.nextShort();
                final short e2 = i2.nextShort();
                final int r;
                if ((r = Short.compare(e1, e2)) != 0) {
                    return r;
                }
            }
            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
        }
        final BigListIterator<? extends Short> i3 = this.listIterator();
        final BigListIterator<? extends Short> i4 = l.listIterator();
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
        final ShortIterator i = this.iterator();
        int h = 1;
        long s = this.size64();
        while (s-- != 0L) {
            final short k = i.nextShort();
            h = 31 * h + k;
        }
        return h;
    }
    
    @Override
    public void push(final short o) {
        this.add(o);
    }
    
    @Override
    public short popShort() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.removeShort(this.size64() - 1L);
    }
    
    @Override
    public short topShort() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getShort(this.size64() - 1L);
    }
    
    @Override
    public short peekShort(final int i) {
        return this.getShort(this.size64() - 1L - i);
    }
    
    public short getShort(final int index) {
        return this.getShort(index);
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
    public boolean addAll(final long index, final ShortCollection c) {
        return this.addAll(index, (Collection<? extends Short>)c);
    }
    
    @Override
    public boolean addAll(final long index, final ShortBigList l) {
        return this.addAll(index, (ShortCollection)l);
    }
    
    @Override
    public boolean addAll(final ShortCollection c) {
        return this.addAll(this.size64(), c);
    }
    
    @Override
    public boolean addAll(final ShortBigList l) {
        return this.addAll(this.size64(), l);
    }
    
    @Deprecated
    @Override
    public void add(final long index, final Short ok) {
        this.add(index, (short)ok);
    }
    
    @Deprecated
    @Override
    public Short set(final long index, final Short ok) {
        return this.set(index, (short)ok);
    }
    
    @Deprecated
    @Override
    public Short get(final long index) {
        return this.getShort(index);
    }
    
    @Deprecated
    @Override
    public long indexOf(final Object ok) {
        return this.indexOf((short)ok);
    }
    
    @Deprecated
    @Override
    public long lastIndexOf(final Object ok) {
        return this.lastIndexOf((short)ok);
    }
    
    @Deprecated
    public Short remove(final int index) {
        return this.removeShort(index);
    }
    
    @Deprecated
    @Override
    public Short remove(final long index) {
        return this.removeShort(index);
    }
    
    @Deprecated
    @Override
    public void push(final Short o) {
        this.push((short)o);
    }
    
    @Deprecated
    @Override
    public Short pop() {
        return this.popShort();
    }
    
    @Deprecated
    @Override
    public Short top() {
        return this.topShort();
    }
    
    @Deprecated
    @Override
    public Short peek(final int i) {
        return this.peekShort(i);
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ShortIterator i = this.iterator();
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
            final short k = i.nextShort();
            s.append(String.valueOf(k));
        }
        s.append("]");
        return s.toString();
    }
    
    public static class ShortSubList extends AbstractShortBigList implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ShortBigList l;
        protected final long from;
        protected long to;
        private static final boolean ASSERTS = false;
        
        public ShortSubList(final ShortBigList l, final long from, final long to) {
            this.l = l;
            this.from = from;
            this.to = to;
        }
        
        private void assertRange() {
        }
        
        @Override
        public boolean add(final short k) {
            this.l.add(this.to, k);
            ++this.to;
            return true;
        }
        
        @Override
        public void add(final long index, final short k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Short> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        @Override
        public short getShort(final long index) {
            this.ensureRestrictedIndex(index);
            return this.l.getShort(this.from + index);
        }
        
        @Override
        public short removeShort(final long index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeShort(this.from + index);
        }
        
        @Override
        public short set(final long index, final short k) {
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
        public void getElements(final long from, final short[][] a, final long offset, final long length) {
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
        public void addElements(final long index, final short[][] a, final long offset, final long length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
        }
        
        @Override
        public ShortBigListIterator listIterator(final long index) {
            this.ensureIndex(index);
            return new AbstractShortBigListIterator() {
                long pos = index;
                long last = -1L;
                
                @Override
                public boolean hasNext() {
                    return this.pos < ShortSubList.this.size64();
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
                    final ShortBigList l = ShortSubList.this.l;
                    final long from = ShortSubList.this.from;
                    final long last = this.pos++;
                    this.last = last;
                    return l.getShort(from + last);
                }
                
                @Override
                public short previousShort() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final ShortBigList l = ShortSubList.this.l;
                    final long from = ShortSubList.this.from;
                    final long n = this.pos - 1L;
                    this.pos = n;
                    this.last = n;
                    return l.getShort(from + n);
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
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    ShortSubList.this.add(this.pos++, k);
                    this.last = -1L;
                }
                
                @Override
                public void set(final short k) {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    ShortSubList.this.set(this.last, k);
                }
                
                @Override
                public void remove() {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    ShortSubList.this.removeShort(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1L;
                }
            };
        }
        
        @Override
        public ShortBigList subList(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new ShortSubList(this, from, to);
        }
        
        @Override
        public boolean rem(final short k) {
            final long index = this.indexOf(k);
            if (index == -1L) {
                return false;
            }
            --this.to;
            this.l.removeShort(this.from + index);
            return true;
        }
        
        @Override
        public boolean remove(final Object o) {
            return this.rem((short)o);
        }
        
        @Override
        public boolean addAll(final long index, final ShortCollection c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        public boolean addAll(final long index, final ShortList l) {
            this.ensureIndex(index);
            this.to += l.size();
            return this.l.addAll(this.from + index, l);
        }
    }
}
