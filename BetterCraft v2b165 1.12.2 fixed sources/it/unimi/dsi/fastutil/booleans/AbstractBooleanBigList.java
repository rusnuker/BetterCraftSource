// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

import java.io.Serializable;
import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.BigList;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;

public abstract class AbstractBooleanBigList extends AbstractBooleanCollection implements BooleanBigList, BooleanStack
{
    protected AbstractBooleanBigList() {
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
    public void add(final long index, final boolean k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final boolean k) {
        this.add(this.size64(), k);
        return true;
    }
    
    @Override
    public boolean removeBoolean(final long i) {
        throw new UnsupportedOperationException();
    }
    
    public boolean removeBoolean(final int i) {
        return this.removeBoolean((long)i);
    }
    
    @Override
    public boolean set(final long index, final boolean k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean set(final int index, final boolean k) {
        return this.set((long)index, k);
    }
    
    @Override
    public boolean addAll(long index, final Collection<? extends Boolean> c) {
        this.ensureIndex(index);
        int n = c.size();
        if (n == 0) {
            return false;
        }
        final Iterator<? extends Boolean> i = c.iterator();
        while (n-- != 0) {
            this.add(index++, (Boolean)i.next());
        }
        return true;
    }
    
    public boolean addAll(final int index, final Collection<? extends Boolean> c) {
        return this.addAll((long)index, c);
    }
    
    @Override
    public boolean addAll(final Collection<? extends Boolean> c) {
        return this.addAll(this.size64(), c);
    }
    
    @Override
    public BooleanBigListIterator iterator() {
        return this.listIterator();
    }
    
    @Override
    public BooleanBigListIterator listIterator() {
        return this.listIterator(0L);
    }
    
    @Override
    public BooleanBigListIterator listIterator(final long index) {
        this.ensureIndex(index);
        return new AbstractBooleanBigListIterator() {
            long pos = index;
            long last = -1L;
            
            @Override
            public boolean hasNext() {
                return this.pos < AbstractBooleanBigList.this.size64();
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0L;
            }
            
            @Override
            public boolean nextBoolean() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractBooleanBigList this$0 = AbstractBooleanBigList.this;
                final long last = this.pos++;
                this.last = last;
                return this$0.getBoolean(last);
            }
            
            @Override
            public boolean previousBoolean() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractBooleanBigList this$0 = AbstractBooleanBigList.this;
                final long n = this.pos - 1L;
                this.pos = n;
                this.last = n;
                return this$0.getBoolean(n);
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
            public void add(final boolean k) {
                AbstractBooleanBigList.this.add(this.pos++, k);
                this.last = -1L;
            }
            
            @Override
            public void set(final boolean k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractBooleanBigList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractBooleanBigList.this.removeBoolean(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1L;
            }
        };
    }
    
    public BooleanBigListIterator listIterator(final int index) {
        return this.listIterator((long)index);
    }
    
    @Override
    public boolean contains(final boolean k) {
        return this.indexOf(k) >= 0L;
    }
    
    @Override
    public long indexOf(final boolean k) {
        final BooleanBigListIterator i = this.listIterator();
        while (i.hasNext()) {
            final boolean e = i.nextBoolean();
            if (k == e) {
                return i.previousIndex();
            }
        }
        return -1L;
    }
    
    @Override
    public long lastIndexOf(final boolean k) {
        final BooleanBigListIterator i = this.listIterator(this.size64());
        while (i.hasPrevious()) {
            final boolean e = i.previousBoolean();
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
                this.add(false);
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
    public BooleanBigList subList(final long from, final long to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new BooleanSubList(this, from, to);
    }
    
    @Override
    public void removeElements(final long from, final long to) {
        this.ensureIndex(to);
        final BooleanBigListIterator i = this.listIterator(from);
        long n = to - from;
        if (n < 0L) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        while (n-- != 0L) {
            i.nextBoolean();
            i.remove();
        }
    }
    
    @Override
    public void addElements(long index, final boolean[][] a, long offset, long length) {
        this.ensureIndex(index);
        BooleanBigArrays.ensureOffsetLength(a, offset, length);
        while (length-- != 0L) {
            this.add(index++, BooleanBigArrays.get(a, offset++));
        }
    }
    
    @Override
    public void addElements(final long index, final boolean[][] a) {
        this.addElements(index, a, 0L, BooleanBigArrays.length(a));
    }
    
    @Override
    public void getElements(final long from, final boolean[][] a, long offset, long length) {
        final BooleanBigListIterator i = this.listIterator(from);
        BooleanBigArrays.ensureOffsetLength(a, offset, length);
        if (from + length > this.size64()) {
            throw new IndexOutOfBoundsException("End index (" + (from + length) + ") is greater than list size (" + this.size64() + ")");
        }
        while (length-- != 0L) {
            BooleanBigArrays.set(a, offset++, i.nextBoolean());
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
        if (l instanceof BooleanBigList) {
            final BooleanBigListIterator i1 = this.listIterator();
            final BooleanBigListIterator i2 = ((BooleanBigList)l).listIterator();
            while (s-- != 0L) {
                if (i1.nextBoolean() != i2.nextBoolean()) {
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
    public int compareTo(final BigList<? extends Boolean> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof BooleanBigList) {
            final BooleanBigListIterator i1 = this.listIterator();
            final BooleanBigListIterator i2 = ((BooleanBigList)l).listIterator();
            while (i1.hasNext() && i2.hasNext()) {
                final boolean e1 = i1.nextBoolean();
                final boolean e2 = i2.nextBoolean();
                final int r;
                if ((r = Boolean.compare(e1, e2)) != 0) {
                    return r;
                }
            }
            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
        }
        final BigListIterator<? extends Boolean> i3 = this.listIterator();
        final BigListIterator<? extends Boolean> i4 = l.listIterator();
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
        final BooleanIterator i = this.iterator();
        int h = 1;
        long s = this.size64();
        while (s-- != 0L) {
            final boolean k = i.nextBoolean();
            h = 31 * h + (k ? 1231 : 1237);
        }
        return h;
    }
    
    @Override
    public void push(final boolean o) {
        this.add(o);
    }
    
    @Override
    public boolean popBoolean() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.removeBoolean(this.size64() - 1L);
    }
    
    @Override
    public boolean topBoolean() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getBoolean(this.size64() - 1L);
    }
    
    @Override
    public boolean peekBoolean(final int i) {
        return this.getBoolean(this.size64() - 1L - i);
    }
    
    public boolean getBoolean(final int index) {
        return this.getBoolean(index);
    }
    
    @Override
    public boolean rem(final boolean k) {
        final long index = this.indexOf(k);
        if (index == -1L) {
            return false;
        }
        this.removeBoolean(index);
        return true;
    }
    
    @Override
    public boolean addAll(final long index, final BooleanCollection c) {
        return this.addAll(index, (Collection<? extends Boolean>)c);
    }
    
    @Override
    public boolean addAll(final long index, final BooleanBigList l) {
        return this.addAll(index, (BooleanCollection)l);
    }
    
    @Override
    public boolean addAll(final BooleanCollection c) {
        return this.addAll(this.size64(), c);
    }
    
    @Override
    public boolean addAll(final BooleanBigList l) {
        return this.addAll(this.size64(), l);
    }
    
    @Deprecated
    @Override
    public void add(final long index, final Boolean ok) {
        this.add(index, (boolean)ok);
    }
    
    @Deprecated
    @Override
    public Boolean set(final long index, final Boolean ok) {
        return this.set(index, (boolean)ok);
    }
    
    @Deprecated
    @Override
    public Boolean get(final long index) {
        return this.getBoolean(index);
    }
    
    @Deprecated
    @Override
    public long indexOf(final Object ok) {
        return this.indexOf((boolean)ok);
    }
    
    @Deprecated
    @Override
    public long lastIndexOf(final Object ok) {
        return this.lastIndexOf((boolean)ok);
    }
    
    @Deprecated
    public Boolean remove(final int index) {
        return this.removeBoolean(index);
    }
    
    @Deprecated
    @Override
    public Boolean remove(final long index) {
        return this.removeBoolean(index);
    }
    
    @Deprecated
    @Override
    public void push(final Boolean o) {
        this.push((boolean)o);
    }
    
    @Deprecated
    @Override
    public Boolean pop() {
        return this.popBoolean();
    }
    
    @Deprecated
    @Override
    public Boolean top() {
        return this.topBoolean();
    }
    
    @Deprecated
    @Override
    public Boolean peek(final int i) {
        return this.peekBoolean(i);
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final BooleanIterator i = this.iterator();
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
            final boolean k = i.nextBoolean();
            s.append(String.valueOf(k));
        }
        s.append("]");
        return s.toString();
    }
    
    public static class BooleanSubList extends AbstractBooleanBigList implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final BooleanBigList l;
        protected final long from;
        protected long to;
        private static final boolean ASSERTS = false;
        
        public BooleanSubList(final BooleanBigList l, final long from, final long to) {
            this.l = l;
            this.from = from;
            this.to = to;
        }
        
        private void assertRange() {
        }
        
        @Override
        public boolean add(final boolean k) {
            this.l.add(this.to, k);
            ++this.to;
            return true;
        }
        
        @Override
        public void add(final long index, final boolean k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Boolean> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        @Override
        public boolean getBoolean(final long index) {
            this.ensureRestrictedIndex(index);
            return this.l.getBoolean(this.from + index);
        }
        
        @Override
        public boolean removeBoolean(final long index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeBoolean(this.from + index);
        }
        
        @Override
        public boolean set(final long index, final boolean k) {
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
        public void getElements(final long from, final boolean[][] a, final long offset, final long length) {
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
        public void addElements(final long index, final boolean[][] a, final long offset, final long length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
        }
        
        @Override
        public BooleanBigListIterator listIterator(final long index) {
            this.ensureIndex(index);
            return new AbstractBooleanBigListIterator() {
                long pos = index;
                long last = -1L;
                
                @Override
                public boolean hasNext() {
                    return this.pos < BooleanSubList.this.size64();
                }
                
                @Override
                public boolean hasPrevious() {
                    return this.pos > 0L;
                }
                
                @Override
                public boolean nextBoolean() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BooleanBigList l = BooleanSubList.this.l;
                    final long from = BooleanSubList.this.from;
                    final long last = this.pos++;
                    this.last = last;
                    return l.getBoolean(from + last);
                }
                
                @Override
                public boolean previousBoolean() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final BooleanBigList l = BooleanSubList.this.l;
                    final long from = BooleanSubList.this.from;
                    final long n = this.pos - 1L;
                    this.pos = n;
                    this.last = n;
                    return l.getBoolean(from + n);
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
                public void add(final boolean k) {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    BooleanSubList.this.add(this.pos++, k);
                    this.last = -1L;
                }
                
                @Override
                public void set(final boolean k) {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    BooleanSubList.this.set(this.last, k);
                }
                
                @Override
                public void remove() {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    BooleanSubList.this.removeBoolean(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1L;
                }
            };
        }
        
        @Override
        public BooleanBigList subList(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new BooleanSubList(this, from, to);
        }
        
        @Override
        public boolean rem(final boolean k) {
            final long index = this.indexOf(k);
            if (index == -1L) {
                return false;
            }
            --this.to;
            this.l.removeBoolean(this.from + index);
            return true;
        }
        
        @Override
        public boolean remove(final Object o) {
            return this.rem((boolean)o);
        }
        
        @Override
        public boolean addAll(final long index, final BooleanCollection c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        public boolean addAll(final long index, final BooleanList l) {
            this.ensureIndex(index);
            this.to += l.size();
            return this.l.addAll(this.from + index, l);
        }
    }
}
