// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

import java.io.Serializable;
import java.util.ListIterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;

public abstract class AbstractBooleanList extends AbstractBooleanCollection implements BooleanList, BooleanStack
{
    protected AbstractBooleanList() {
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
    public void add(final int index, final boolean k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final boolean k) {
        this.add(this.size(), k);
        return true;
    }
    
    @Override
    public boolean removeBoolean(final int i) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean set(final int index, final boolean k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(int index, final Collection<? extends Boolean> c) {
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
    
    @Override
    public boolean addAll(final Collection<? extends Boolean> c) {
        return this.addAll(this.size(), c);
    }
    
    @Deprecated
    @Override
    public BooleanListIterator booleanListIterator() {
        return this.listIterator();
    }
    
    @Deprecated
    @Override
    public BooleanListIterator booleanListIterator(final int index) {
        return this.listIterator(index);
    }
    
    @Override
    public BooleanListIterator iterator() {
        return this.listIterator();
    }
    
    @Override
    public BooleanListIterator listIterator() {
        return this.listIterator(0);
    }
    
    @Override
    public BooleanListIterator listIterator(final int index) {
        this.ensureIndex(index);
        return new AbstractBooleanListIterator() {
            int pos = index;
            int last = -1;
            
            @Override
            public boolean hasNext() {
                return this.pos < AbstractBooleanList.this.size();
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0;
            }
            
            @Override
            public boolean nextBoolean() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractBooleanList this$0 = AbstractBooleanList.this;
                final int last = this.pos++;
                this.last = last;
                return this$0.getBoolean(last);
            }
            
            @Override
            public boolean previousBoolean() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractBooleanList this$0 = AbstractBooleanList.this;
                final int n = this.pos - 1;
                this.pos = n;
                this.last = n;
                return this$0.getBoolean(n);
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
            public void add(final boolean k) {
                AbstractBooleanList.this.add(this.pos++, k);
                this.last = -1;
            }
            
            @Override
            public void set(final boolean k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractBooleanList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractBooleanList.this.removeBoolean(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }
        };
    }
    
    @Override
    public boolean contains(final boolean k) {
        return this.indexOf(k) >= 0;
    }
    
    @Override
    public int indexOf(final boolean k) {
        final BooleanListIterator i = this.listIterator();
        while (i.hasNext()) {
            final boolean e = i.nextBoolean();
            if (k == e) {
                return i.previousIndex();
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final boolean k) {
        final BooleanListIterator i = this.listIterator(this.size());
        while (i.hasPrevious()) {
            final boolean e = i.previousBoolean();
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
                this.add(false);
            }
        }
        else {
            while (i-- != size) {
                this.remove(i);
            }
        }
    }
    
    @Override
    public BooleanList subList(final int from, final int to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new BooleanSubList(this, from, to);
    }
    
    @Deprecated
    @Override
    public BooleanList booleanSubList(final int from, final int to) {
        return this.subList(from, to);
    }
    
    @Override
    public void removeElements(final int from, final int to) {
        this.ensureIndex(to);
        final BooleanListIterator i = this.listIterator(from);
        int n = to - from;
        if (n < 0) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        while (n-- != 0) {
            i.nextBoolean();
            i.remove();
        }
    }
    
    @Override
    public void addElements(int index, final boolean[] a, int offset, int length) {
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
    public void addElements(final int index, final boolean[] a) {
        this.addElements(index, a, 0, a.length);
    }
    
    @Override
    public void getElements(final int from, final boolean[] a, int offset, int length) {
        final BooleanListIterator i = this.listIterator(from);
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
            a[offset++] = i.nextBoolean();
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
        if (l instanceof BooleanList) {
            final BooleanListIterator i1 = this.listIterator();
            final BooleanListIterator i2 = ((BooleanList)l).listIterator();
            while (s-- != 0) {
                if (i1.nextBoolean() != i2.nextBoolean()) {
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
    public int compareTo(final List<? extends Boolean> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof BooleanList) {
            final BooleanListIterator i1 = this.listIterator();
            final BooleanListIterator i2 = ((BooleanList)l).listIterator();
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
        final ListIterator<? extends Boolean> i3 = this.listIterator();
        final ListIterator<? extends Boolean> i4 = l.listIterator();
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
        final BooleanIterator i = this.iterator();
        int h = 1;
        int s = this.size();
        while (s-- != 0) {
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
        return this.removeBoolean(this.size() - 1);
    }
    
    @Override
    public boolean topBoolean() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getBoolean(this.size() - 1);
    }
    
    @Override
    public boolean peekBoolean(final int i) {
        return this.getBoolean(this.size() - 1 - i);
    }
    
    @Override
    public boolean rem(final boolean k) {
        final int index = this.indexOf(k);
        if (index == -1) {
            return false;
        }
        this.removeBoolean(index);
        return true;
    }
    
    @Override
    public boolean remove(final Object o) {
        return this.rem((boolean)o);
    }
    
    @Override
    public boolean addAll(final int index, final BooleanCollection c) {
        return this.addAll(index, (Collection<? extends Boolean>)c);
    }
    
    @Override
    public boolean addAll(final int index, final BooleanList l) {
        return this.addAll(index, (BooleanCollection)l);
    }
    
    @Override
    public boolean addAll(final BooleanCollection c) {
        return this.addAll(this.size(), c);
    }
    
    @Override
    public boolean addAll(final BooleanList l) {
        return this.addAll(this.size(), l);
    }
    
    @Override
    public void add(final int index, final Boolean ok) {
        this.add(index, (boolean)ok);
    }
    
    @Deprecated
    @Override
    public Boolean set(final int index, final Boolean ok) {
        return this.set(index, (boolean)ok);
    }
    
    @Deprecated
    @Override
    public Boolean get(final int index) {
        return this.getBoolean(index);
    }
    
    @Override
    public int indexOf(final Object ok) {
        return this.indexOf((boolean)ok);
    }
    
    @Override
    public int lastIndexOf(final Object ok) {
        return this.lastIndexOf((boolean)ok);
    }
    
    @Deprecated
    @Override
    public Boolean remove(final int index) {
        return this.removeBoolean(index);
    }
    
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
            final boolean k = i.nextBoolean();
            s.append(String.valueOf(k));
        }
        s.append("]");
        return s.toString();
    }
    
    public static class BooleanSubList extends AbstractBooleanList implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final BooleanList l;
        protected final int from;
        protected int to;
        private static final boolean ASSERTS = false;
        
        public BooleanSubList(final BooleanList l, final int from, final int to) {
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
        public void add(final int index, final boolean k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Boolean> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        @Override
        public boolean getBoolean(final int index) {
            this.ensureRestrictedIndex(index);
            return this.l.getBoolean(this.from + index);
        }
        
        @Override
        public boolean removeBoolean(final int index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeBoolean(this.from + index);
        }
        
        @Override
        public boolean set(final int index, final boolean k) {
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
        public void getElements(final int from, final boolean[] a, final int offset, final int length) {
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
        public void addElements(final int index, final boolean[] a, final int offset, final int length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
        }
        
        @Override
        public BooleanListIterator listIterator(final int index) {
            this.ensureIndex(index);
            return new AbstractBooleanListIterator() {
                int pos = index;
                int last = -1;
                
                @Override
                public boolean hasNext() {
                    return this.pos < BooleanSubList.this.size();
                }
                
                @Override
                public boolean hasPrevious() {
                    return this.pos > 0;
                }
                
                @Override
                public boolean nextBoolean() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BooleanList l = BooleanSubList.this.l;
                    final int from = BooleanSubList.this.from;
                    final int last = this.pos++;
                    this.last = last;
                    return l.getBoolean(from + last);
                }
                
                @Override
                public boolean previousBoolean() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final BooleanList l = BooleanSubList.this.l;
                    final int from = BooleanSubList.this.from;
                    final int n = this.pos - 1;
                    this.pos = n;
                    this.last = n;
                    return l.getBoolean(from + n);
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
                public void add(final boolean k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    BooleanSubList.this.add(this.pos++, k);
                    this.last = -1;
                }
                
                @Override
                public void set(final boolean k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    BooleanSubList.this.set(this.last, k);
                }
                
                @Override
                public void remove() {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    BooleanSubList.this.removeBoolean(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1;
                }
            };
        }
        
        @Override
        public BooleanList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new BooleanSubList(this, from, to);
        }
        
        @Override
        public boolean rem(final boolean k) {
            final int index = this.indexOf(k);
            if (index == -1) {
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
        public boolean addAll(final int index, final BooleanCollection c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        @Override
        public boolean addAll(final int index, final BooleanList l) {
            this.ensureIndex(index);
            this.to += l.size();
            return this.l.addAll(this.from + index, l);
        }
    }
}
