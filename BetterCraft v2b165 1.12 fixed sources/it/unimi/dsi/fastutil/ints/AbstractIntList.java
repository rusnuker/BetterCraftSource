// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.io.Serializable;
import java.util.ListIterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;

public abstract class AbstractIntList extends AbstractIntCollection implements IntList, IntStack
{
    protected AbstractIntList() {
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
    public void add(final int index, final int k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final int k) {
        this.add(this.size(), k);
        return true;
    }
    
    @Override
    public int removeInt(final int i) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int set(final int index, final int k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(int index, final Collection<? extends Integer> c) {
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
    
    @Override
    public boolean addAll(final Collection<? extends Integer> c) {
        return this.addAll(this.size(), c);
    }
    
    @Deprecated
    @Override
    public IntListIterator intListIterator() {
        return this.listIterator();
    }
    
    @Deprecated
    @Override
    public IntListIterator intListIterator(final int index) {
        return this.listIterator(index);
    }
    
    @Override
    public IntListIterator iterator() {
        return this.listIterator();
    }
    
    @Override
    public IntListIterator listIterator() {
        return this.listIterator(0);
    }
    
    @Override
    public IntListIterator listIterator(final int index) {
        this.ensureIndex(index);
        return new AbstractIntListIterator() {
            int pos = index;
            int last = -1;
            
            @Override
            public boolean hasNext() {
                return this.pos < AbstractIntList.this.size();
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0;
            }
            
            @Override
            public int nextInt() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractIntList this$0 = AbstractIntList.this;
                final int last = this.pos++;
                this.last = last;
                return this$0.getInt(last);
            }
            
            @Override
            public int previousInt() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractIntList this$0 = AbstractIntList.this;
                final int n = this.pos - 1;
                this.pos = n;
                this.last = n;
                return this$0.getInt(n);
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
            public void add(final int k) {
                AbstractIntList.this.add(this.pos++, k);
                this.last = -1;
            }
            
            @Override
            public void set(final int k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractIntList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractIntList.this.removeInt(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }
        };
    }
    
    @Override
    public boolean contains(final int k) {
        return this.indexOf(k) >= 0;
    }
    
    @Override
    public int indexOf(final int k) {
        final IntListIterator i = this.listIterator();
        while (i.hasNext()) {
            final int e = i.nextInt();
            if (k == e) {
                return i.previousIndex();
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final int k) {
        final IntListIterator i = this.listIterator(this.size());
        while (i.hasPrevious()) {
            final int e = i.previousInt();
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
                this.add(0);
            }
        }
        else {
            while (i-- != size) {
                this.remove(i);
            }
        }
    }
    
    @Override
    public IntList subList(final int from, final int to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new IntSubList(this, from, to);
    }
    
    @Deprecated
    @Override
    public IntList intSubList(final int from, final int to) {
        return this.subList(from, to);
    }
    
    @Override
    public void removeElements(final int from, final int to) {
        this.ensureIndex(to);
        final IntListIterator i = this.listIterator(from);
        int n = to - from;
        if (n < 0) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        while (n-- != 0) {
            i.nextInt();
            i.remove();
        }
    }
    
    @Override
    public void addElements(int index, final int[] a, int offset, int length) {
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
    public void addElements(final int index, final int[] a) {
        this.addElements(index, a, 0, a.length);
    }
    
    @Override
    public void getElements(final int from, final int[] a, int offset, int length) {
        final IntListIterator i = this.listIterator(from);
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
            a[offset++] = i.nextInt();
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
        if (l instanceof IntList) {
            final IntListIterator i1 = this.listIterator();
            final IntListIterator i2 = ((IntList)l).listIterator();
            while (s-- != 0) {
                if (i1.nextInt() != i2.nextInt()) {
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
    public int compareTo(final List<? extends Integer> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof IntList) {
            final IntListIterator i1 = this.listIterator();
            final IntListIterator i2 = ((IntList)l).listIterator();
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
        final ListIterator<? extends Integer> i3 = this.listIterator();
        final ListIterator<? extends Integer> i4 = l.listIterator();
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
        final IntIterator i = this.iterator();
        int h = 1;
        int s = this.size();
        while (s-- != 0) {
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
        return this.removeInt(this.size() - 1);
    }
    
    @Override
    public int topInt() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getInt(this.size() - 1);
    }
    
    @Override
    public int peekInt(final int i) {
        return this.getInt(this.size() - 1 - i);
    }
    
    @Override
    public boolean rem(final int k) {
        final int index = this.indexOf(k);
        if (index == -1) {
            return false;
        }
        this.removeInt(index);
        return true;
    }
    
    @Override
    public boolean remove(final Object o) {
        return this.rem((int)o);
    }
    
    @Override
    public boolean addAll(final int index, final IntCollection c) {
        return this.addAll(index, (Collection<? extends Integer>)c);
    }
    
    @Override
    public boolean addAll(final int index, final IntList l) {
        return this.addAll(index, (IntCollection)l);
    }
    
    @Override
    public boolean addAll(final IntCollection c) {
        return this.addAll(this.size(), c);
    }
    
    @Override
    public boolean addAll(final IntList l) {
        return this.addAll(this.size(), l);
    }
    
    @Override
    public void add(final int index, final Integer ok) {
        this.add(index, (int)ok);
    }
    
    @Deprecated
    @Override
    public Integer set(final int index, final Integer ok) {
        return this.set(index, (int)ok);
    }
    
    @Deprecated
    @Override
    public Integer get(final int index) {
        return this.getInt(index);
    }
    
    @Override
    public int indexOf(final Object ok) {
        return this.indexOf((int)ok);
    }
    
    @Override
    public int lastIndexOf(final Object ok) {
        return this.lastIndexOf((int)ok);
    }
    
    @Deprecated
    @Override
    public Integer remove(final int index) {
        return this.removeInt(index);
    }
    
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
            final int k = i.nextInt();
            s.append(String.valueOf(k));
        }
        s.append("]");
        return s.toString();
    }
    
    public static class IntSubList extends AbstractIntList implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntList l;
        protected final int from;
        protected int to;
        private static final boolean ASSERTS = false;
        
        public IntSubList(final IntList l, final int from, final int to) {
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
        public void add(final int index, final int k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Integer> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        @Override
        public int getInt(final int index) {
            this.ensureRestrictedIndex(index);
            return this.l.getInt(this.from + index);
        }
        
        @Override
        public int removeInt(final int index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeInt(this.from + index);
        }
        
        @Override
        public int set(final int index, final int k) {
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
        public void getElements(final int from, final int[] a, final int offset, final int length) {
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
        public void addElements(final int index, final int[] a, final int offset, final int length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
        }
        
        @Override
        public IntListIterator listIterator(final int index) {
            this.ensureIndex(index);
            return new AbstractIntListIterator() {
                int pos = index;
                int last = -1;
                
                @Override
                public boolean hasNext() {
                    return this.pos < IntSubList.this.size();
                }
                
                @Override
                public boolean hasPrevious() {
                    return this.pos > 0;
                }
                
                @Override
                public int nextInt() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final IntList l = IntSubList.this.l;
                    final int from = IntSubList.this.from;
                    final int last = this.pos++;
                    this.last = last;
                    return l.getInt(from + last);
                }
                
                @Override
                public int previousInt() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final IntList l = IntSubList.this.l;
                    final int from = IntSubList.this.from;
                    final int n = this.pos - 1;
                    this.pos = n;
                    this.last = n;
                    return l.getInt(from + n);
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
                public void add(final int k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    IntSubList.this.add(this.pos++, k);
                    this.last = -1;
                }
                
                @Override
                public void set(final int k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    IntSubList.this.set(this.last, k);
                }
                
                @Override
                public void remove() {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    IntSubList.this.removeInt(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1;
                }
            };
        }
        
        @Override
        public IntList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new IntSubList(this, from, to);
        }
        
        @Override
        public boolean rem(final int k) {
            final int index = this.indexOf(k);
            if (index == -1) {
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
        public boolean addAll(final int index, final IntCollection c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        @Override
        public boolean addAll(final int index, final IntList l) {
            this.ensureIndex(index);
            this.to += l.size();
            return this.l.addAll(this.from + index, l);
        }
    }
}
