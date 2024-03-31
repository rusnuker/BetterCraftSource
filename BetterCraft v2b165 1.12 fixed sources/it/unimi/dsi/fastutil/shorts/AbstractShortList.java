// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.io.Serializable;
import java.util.ListIterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;

public abstract class AbstractShortList extends AbstractShortCollection implements ShortList, ShortStack
{
    protected AbstractShortList() {
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
    public void add(final int index, final short k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final short k) {
        this.add(this.size(), k);
        return true;
    }
    
    @Override
    public short removeShort(final int i) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public short set(final int index, final short k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(int index, final Collection<? extends Short> c) {
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
    
    @Override
    public boolean addAll(final Collection<? extends Short> c) {
        return this.addAll(this.size(), c);
    }
    
    @Deprecated
    @Override
    public ShortListIterator shortListIterator() {
        return this.listIterator();
    }
    
    @Deprecated
    @Override
    public ShortListIterator shortListIterator(final int index) {
        return this.listIterator(index);
    }
    
    @Override
    public ShortListIterator iterator() {
        return this.listIterator();
    }
    
    @Override
    public ShortListIterator listIterator() {
        return this.listIterator(0);
    }
    
    @Override
    public ShortListIterator listIterator(final int index) {
        this.ensureIndex(index);
        return new AbstractShortListIterator() {
            int pos = index;
            int last = -1;
            
            @Override
            public boolean hasNext() {
                return this.pos < AbstractShortList.this.size();
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0;
            }
            
            @Override
            public short nextShort() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractShortList this$0 = AbstractShortList.this;
                final int last = this.pos++;
                this.last = last;
                return this$0.getShort(last);
            }
            
            @Override
            public short previousShort() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractShortList this$0 = AbstractShortList.this;
                final int n = this.pos - 1;
                this.pos = n;
                this.last = n;
                return this$0.getShort(n);
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
            public void add(final short k) {
                AbstractShortList.this.add(this.pos++, k);
                this.last = -1;
            }
            
            @Override
            public void set(final short k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractShortList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractShortList.this.removeShort(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }
        };
    }
    
    @Override
    public boolean contains(final short k) {
        return this.indexOf(k) >= 0;
    }
    
    @Override
    public int indexOf(final short k) {
        final ShortListIterator i = this.listIterator();
        while (i.hasNext()) {
            final short e = i.nextShort();
            if (k == e) {
                return i.previousIndex();
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final short k) {
        final ShortListIterator i = this.listIterator(this.size());
        while (i.hasPrevious()) {
            final short e = i.previousShort();
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
                this.add((short)0);
            }
        }
        else {
            while (i-- != size) {
                this.remove(i);
            }
        }
    }
    
    @Override
    public ShortList subList(final int from, final int to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new ShortSubList(this, from, to);
    }
    
    @Deprecated
    @Override
    public ShortList shortSubList(final int from, final int to) {
        return this.subList(from, to);
    }
    
    @Override
    public void removeElements(final int from, final int to) {
        this.ensureIndex(to);
        final ShortListIterator i = this.listIterator(from);
        int n = to - from;
        if (n < 0) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        while (n-- != 0) {
            i.nextShort();
            i.remove();
        }
    }
    
    @Override
    public void addElements(int index, final short[] a, int offset, int length) {
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
    public void addElements(final int index, final short[] a) {
        this.addElements(index, a, 0, a.length);
    }
    
    @Override
    public void getElements(final int from, final short[] a, int offset, int length) {
        final ShortListIterator i = this.listIterator(from);
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
            a[offset++] = i.nextShort();
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
        if (l instanceof ShortList) {
            final ShortListIterator i1 = this.listIterator();
            final ShortListIterator i2 = ((ShortList)l).listIterator();
            while (s-- != 0) {
                if (i1.nextShort() != i2.nextShort()) {
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
    public int compareTo(final List<? extends Short> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof ShortList) {
            final ShortListIterator i1 = this.listIterator();
            final ShortListIterator i2 = ((ShortList)l).listIterator();
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
        final ListIterator<? extends Short> i3 = this.listIterator();
        final ListIterator<? extends Short> i4 = l.listIterator();
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
        final ShortIterator i = this.iterator();
        int h = 1;
        int s = this.size();
        while (s-- != 0) {
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
        return this.removeShort(this.size() - 1);
    }
    
    @Override
    public short topShort() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getShort(this.size() - 1);
    }
    
    @Override
    public short peekShort(final int i) {
        return this.getShort(this.size() - 1 - i);
    }
    
    @Override
    public boolean rem(final short k) {
        final int index = this.indexOf(k);
        if (index == -1) {
            return false;
        }
        this.removeShort(index);
        return true;
    }
    
    @Override
    public boolean remove(final Object o) {
        return this.rem((short)o);
    }
    
    @Override
    public boolean addAll(final int index, final ShortCollection c) {
        return this.addAll(index, (Collection<? extends Short>)c);
    }
    
    @Override
    public boolean addAll(final int index, final ShortList l) {
        return this.addAll(index, (ShortCollection)l);
    }
    
    @Override
    public boolean addAll(final ShortCollection c) {
        return this.addAll(this.size(), c);
    }
    
    @Override
    public boolean addAll(final ShortList l) {
        return this.addAll(this.size(), l);
    }
    
    @Override
    public void add(final int index, final Short ok) {
        this.add(index, (short)ok);
    }
    
    @Deprecated
    @Override
    public Short set(final int index, final Short ok) {
        return this.set(index, (short)ok);
    }
    
    @Deprecated
    @Override
    public Short get(final int index) {
        return this.getShort(index);
    }
    
    @Override
    public int indexOf(final Object ok) {
        return this.indexOf((short)ok);
    }
    
    @Override
    public int lastIndexOf(final Object ok) {
        return this.lastIndexOf((short)ok);
    }
    
    @Deprecated
    @Override
    public Short remove(final int index) {
        return this.removeShort(index);
    }
    
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
            final short k = i.nextShort();
            s.append(String.valueOf(k));
        }
        s.append("]");
        return s.toString();
    }
    
    public static class ShortSubList extends AbstractShortList implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ShortList l;
        protected final int from;
        protected int to;
        private static final boolean ASSERTS = false;
        
        public ShortSubList(final ShortList l, final int from, final int to) {
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
        public void add(final int index, final short k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Short> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        @Override
        public short getShort(final int index) {
            this.ensureRestrictedIndex(index);
            return this.l.getShort(this.from + index);
        }
        
        @Override
        public short removeShort(final int index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeShort(this.from + index);
        }
        
        @Override
        public short set(final int index, final short k) {
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
        public void getElements(final int from, final short[] a, final int offset, final int length) {
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
        public void addElements(final int index, final short[] a, final int offset, final int length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
        }
        
        @Override
        public ShortListIterator listIterator(final int index) {
            this.ensureIndex(index);
            return new AbstractShortListIterator() {
                int pos = index;
                int last = -1;
                
                @Override
                public boolean hasNext() {
                    return this.pos < ShortSubList.this.size();
                }
                
                @Override
                public boolean hasPrevious() {
                    return this.pos > 0;
                }
                
                @Override
                public short nextShort() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final ShortList l = ShortSubList.this.l;
                    final int from = ShortSubList.this.from;
                    final int last = this.pos++;
                    this.last = last;
                    return l.getShort(from + last);
                }
                
                @Override
                public short previousShort() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final ShortList l = ShortSubList.this.l;
                    final int from = ShortSubList.this.from;
                    final int n = this.pos - 1;
                    this.pos = n;
                    this.last = n;
                    return l.getShort(from + n);
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
                public void add(final short k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    ShortSubList.this.add(this.pos++, k);
                    this.last = -1;
                }
                
                @Override
                public void set(final short k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    ShortSubList.this.set(this.last, k);
                }
                
                @Override
                public void remove() {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    ShortSubList.this.removeShort(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1;
                }
            };
        }
        
        @Override
        public ShortList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new ShortSubList(this, from, to);
        }
        
        @Override
        public boolean rem(final short k) {
            final int index = this.indexOf(k);
            if (index == -1) {
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
        public boolean addAll(final int index, final ShortCollection c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        @Override
        public boolean addAll(final int index, final ShortList l) {
            this.ensureIndex(index);
            this.to += l.size();
            return this.l.addAll(this.from + index, l);
        }
    }
}
