// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.io.Serializable;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.ListIterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;

public abstract class AbstractLongList extends AbstractLongCollection implements LongList, LongStack
{
    protected AbstractLongList() {
    }
    
    protected void ensureIndex(final int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index > this.size()) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + this.size() + ")");
        }
    }
    
    protected void ensureRestrictedIndex(final int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index >= this.size()) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size() + ")");
        }
    }
    
    @Override
    public void add(final int index, final long k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final long k) {
        this.add(this.size(), k);
        return true;
    }
    
    @Override
    public long removeLong(final int i) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public long set(final int index, final long k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(int index, final Collection<? extends Long> c) {
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
    
    @Override
    public boolean addAll(final Collection<? extends Long> c) {
        return this.addAll(this.size(), c);
    }
    
    @Deprecated
    @Override
    public LongListIterator longListIterator() {
        return this.listIterator();
    }
    
    @Deprecated
    @Override
    public LongListIterator longListIterator(final int index) {
        return this.listIterator(index);
    }
    
    @Override
    public LongListIterator iterator() {
        return this.listIterator();
    }
    
    @Override
    public LongListIterator listIterator() {
        return this.listIterator(0);
    }
    
    @Override
    public LongListIterator listIterator(final int index) {
        this.ensureIndex(index);
        return new AbstractLongListIterator() {
            int pos = index;
            int last = -1;
            
            @Override
            public boolean hasNext() {
                return this.pos < AbstractLongList.this.size();
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0;
            }
            
            @Override
            public long nextLong() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractLongList this$0 = AbstractLongList.this;
                final int last = this.pos++;
                this.last = last;
                return this$0.getLong(last);
            }
            
            @Override
            public long previousLong() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractLongList this$0 = AbstractLongList.this;
                final int n = this.pos - 1;
                this.pos = n;
                this.last = n;
                return this$0.getLong(n);
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
            public void add(final long k) {
                AbstractLongList.this.add(this.pos++, k);
                this.last = -1;
            }
            
            @Override
            public void set(final long k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractLongList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractLongList.this.removeLong(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }
        };
    }
    
    @Override
    public boolean contains(final long k) {
        return this.indexOf(k) >= 0;
    }
    
    @Override
    public int indexOf(final long k) {
        final LongListIterator i = this.listIterator();
        while (i.hasNext()) {
            final long e = i.nextLong();
            if (k == e) {
                return i.previousIndex();
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final long k) {
        final LongListIterator i = this.listIterator(this.size());
        while (i.hasPrevious()) {
            final long e = i.previousLong();
            if (k == e) {
                return i.nextIndex();
            }
        }
        return -1;
    }
    
    @Override
    public void size(final int size) {
        int i = this.size();
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
    
    @Override
    public LongList subList(final int from, final int to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new LongSubList(this, from, to);
    }
    
    @Deprecated
    @Override
    public LongList longSubList(final int from, final int to) {
        return this.subList(from, to);
    }
    
    @Override
    public void removeElements(final int from, final int to) {
        this.ensureIndex(to);
        final LongListIterator i = this.listIterator(from);
        int n = to - from;
        if (n < 0) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        while (n-- != 0) {
            i.nextLong();
            i.remove();
        }
    }
    
    @Override
    public void addElements(int index, final long[] a, int offset, int length) {
        this.ensureIndex(index);
        if (offset < 0) {
            throw new ArrayIndexOutOfBoundsException("Offset (" + offset + ") is negative");
        }
        if (offset + length > a.length) {
            throw new ArrayIndexOutOfBoundsException("End index (" + (offset + length) + ") is greater than array length (" + a.length + ")");
        }
        while (length-- != 0) {
            this.add(index++, a[offset++]);
        }
    }
    
    @Override
    public void addElements(final int index, final long[] a) {
        this.addElements(index, a, 0, a.length);
    }
    
    @Override
    public void getElements(final int from, final long[] a, int offset, int length) {
        final LongListIterator i = this.listIterator(from);
        if (offset < 0) {
            throw new ArrayIndexOutOfBoundsException("Offset (" + offset + ") is negative");
        }
        if (offset + length > a.length) {
            throw new ArrayIndexOutOfBoundsException("End index (" + (offset + length) + ") is greater than array length (" + a.length + ")");
        }
        if (from + length > this.size()) {
            throw new IndexOutOfBoundsException("End index (" + (from + length) + ") is greater than list size (" + this.size() + ")");
        }
        while (length-- != 0) {
            a[offset++] = i.nextLong();
        }
    }
    
    private boolean valEquals(final Object a, final Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof List)) {
            return false;
        }
        final List<?> l = (List<?>)o;
        int s = this.size();
        if (s != l.size()) {
            return false;
        }
        if (l instanceof LongList) {
            final LongListIterator i1 = this.listIterator();
            final LongListIterator i2 = ((LongList)l).listIterator();
            while (s-- != 0) {
                if (i1.nextLong() != i2.nextLong()) {
                    return false;
                }
            }
            return true;
        }
        final ListIterator<?> i3 = this.listIterator();
        final ListIterator<?> i4 = l.listIterator();
        while (s-- != 0) {
            if (!this.valEquals(i3.next(), i4.next())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int compareTo(final List<? extends Long> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof LongList) {
            final LongListIterator i1 = this.listIterator();
            final LongListIterator i2 = ((LongList)l).listIterator();
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
        final ListIterator<? extends Long> i3 = this.listIterator();
        final ListIterator<? extends Long> i4 = l.listIterator();
        while (i3.hasNext() && i4.hasNext()) {
            final int r;
            if ((r = ((Comparable)i3.next()).compareTo(i4.next())) != 0) {
                return r;
            }
        }
        return i4.hasNext() ? -1 : (i3.hasNext() ? 1 : 0);
    }
    
    @Override
    public int hashCode() {
        final LongIterator i = this.iterator();
        int h = 1;
        int s = this.size();
        while (s-- != 0) {
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
        return this.removeLong(this.size() - 1);
    }
    
    @Override
    public long topLong() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getLong(this.size() - 1);
    }
    
    @Override
    public long peekLong(final int i) {
        return this.getLong(this.size() - 1 - i);
    }
    
    @Override
    public boolean rem(final long k) {
        final int index = this.indexOf(k);
        if (index == -1) {
            return false;
        }
        this.removeLong(index);
        return true;
    }
    
    @Override
    public boolean remove(final Object o) {
        return this.rem((long)o);
    }
    
    @Override
    public boolean addAll(final int index, final LongCollection c) {
        return this.addAll(index, (Collection<? extends Long>)c);
    }
    
    @Override
    public boolean addAll(final int index, final LongList l) {
        return this.addAll(index, (LongCollection)l);
    }
    
    @Override
    public boolean addAll(final LongCollection c) {
        return this.addAll(this.size(), c);
    }
    
    @Override
    public boolean addAll(final LongList l) {
        return this.addAll(this.size(), l);
    }
    
    @Override
    public void add(final int index, final Long ok) {
        this.add(index, (long)ok);
    }
    
    @Deprecated
    @Override
    public Long set(final int index, final Long ok) {
        return this.set(index, (long)ok);
    }
    
    @Deprecated
    @Override
    public Long get(final int index) {
        return this.getLong(index);
    }
    
    @Override
    public int indexOf(final Object ok) {
        return this.indexOf((long)ok);
    }
    
    @Override
    public int lastIndexOf(final Object ok) {
        return this.lastIndexOf((long)ok);
    }
    
    @Deprecated
    @Override
    public Long remove(final int index) {
        return this.removeLong(index);
    }
    
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
        int n = this.size();
        boolean first = true;
        s.append("[");
        while (n-- != 0) {
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
    
    public static class LongSubList extends AbstractLongList implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongList l;
        protected final int from;
        protected int to;
        private static final boolean ASSERTS = false;
        
        public LongSubList(final LongList l, final int from, final int to) {
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
        public void add(final int index, final long k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Long> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        @Override
        public long getLong(final int index) {
            this.ensureRestrictedIndex(index);
            return this.l.getLong(this.from + index);
        }
        
        @Override
        public long removeLong(final int index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeLong(this.from + index);
        }
        
        @Override
        public long set(final int index, final long k) {
            this.ensureRestrictedIndex(index);
            return this.l.set(this.from + index, k);
        }
        
        @Override
        public void clear() {
            this.removeElements(0, this.size());
        }
        
        @Override
        public int size() {
            return this.to - this.from;
        }
        
        @Override
        public void getElements(final int from, final long[] a, final int offset, final int length) {
            this.ensureIndex(from);
            if (from + length > this.size()) {
                throw new IndexOutOfBoundsException("End index (" + from + length + ") is greater than list size (" + this.size() + ")");
            }
            this.l.getElements(this.from + from, a, offset, length);
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            this.l.removeElements(this.from + from, this.from + to);
            this.to -= to - from;
        }
        
        @Override
        public void addElements(final int index, final long[] a, final int offset, final int length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
        }
        
        @Override
        public LongListIterator listIterator(final int index) {
            this.ensureIndex(index);
            return new AbstractLongListIterator() {
                int pos = index;
                int last = -1;
                
                @Override
                public boolean hasNext() {
                    return this.pos < LongSubList.this.size();
                }
                
                @Override
                public boolean hasPrevious() {
                    return this.pos > 0;
                }
                
                @Override
                public long nextLong() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final LongList l = LongSubList.this.l;
                    final int from = LongSubList.this.from;
                    final int last = this.pos++;
                    this.last = last;
                    return l.getLong(from + last);
                }
                
                @Override
                public long previousLong() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final LongList l = LongSubList.this.l;
                    final int from = LongSubList.this.from;
                    final int n = this.pos - 1;
                    this.pos = n;
                    this.last = n;
                    return l.getLong(from + n);
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
                public void add(final long k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    LongSubList.this.add(this.pos++, k);
                    this.last = -1;
                }
                
                @Override
                public void set(final long k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    LongSubList.this.set(this.last, k);
                }
                
                @Override
                public void remove() {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    LongSubList.this.removeLong(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1;
                }
            };
        }
        
        @Override
        public LongList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new LongSubList(this, from, to);
        }
        
        @Override
        public boolean rem(final long k) {
            final int index = this.indexOf(k);
            if (index == -1) {
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
        public boolean addAll(final int index, final LongCollection c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        @Override
        public boolean addAll(final int index, final LongList l) {
            this.ensureIndex(index);
            this.to += l.size();
            return this.l.addAll(this.from + index, l);
        }
    }
}
