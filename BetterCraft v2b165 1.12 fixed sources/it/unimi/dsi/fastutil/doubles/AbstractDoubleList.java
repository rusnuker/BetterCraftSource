// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import java.io.Serializable;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.ListIterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;

public abstract class AbstractDoubleList extends AbstractDoubleCollection implements DoubleList, DoubleStack
{
    protected AbstractDoubleList() {
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
    public void add(final int index, final double k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final double k) {
        this.add(this.size(), k);
        return true;
    }
    
    @Override
    public double removeDouble(final int i) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public double set(final int index, final double k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(int index, final Collection<? extends Double> c) {
        this.ensureIndex(index);
        int n = c.size();
        if (n == 0) {
            return false;
        }
        final Iterator<? extends Double> i = c.iterator();
        while (n-- != 0) {
            this.add(index++, (Double)i.next());
        }
        return true;
    }
    
    @Override
    public boolean addAll(final Collection<? extends Double> c) {
        return this.addAll(this.size(), c);
    }
    
    @Deprecated
    @Override
    public DoubleListIterator doubleListIterator() {
        return this.listIterator();
    }
    
    @Deprecated
    @Override
    public DoubleListIterator doubleListIterator(final int index) {
        return this.listIterator(index);
    }
    
    @Override
    public DoubleListIterator iterator() {
        return this.listIterator();
    }
    
    @Override
    public DoubleListIterator listIterator() {
        return this.listIterator(0);
    }
    
    @Override
    public DoubleListIterator listIterator(final int index) {
        this.ensureIndex(index);
        return new AbstractDoubleListIterator() {
            int pos = index;
            int last = -1;
            
            @Override
            public boolean hasNext() {
                return this.pos < AbstractDoubleList.this.size();
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0;
            }
            
            @Override
            public double nextDouble() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractDoubleList this$0 = AbstractDoubleList.this;
                final int last = this.pos++;
                this.last = last;
                return this$0.getDouble(last);
            }
            
            @Override
            public double previousDouble() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractDoubleList this$0 = AbstractDoubleList.this;
                final int n = this.pos - 1;
                this.pos = n;
                this.last = n;
                return this$0.getDouble(n);
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
            public void add(final double k) {
                AbstractDoubleList.this.add(this.pos++, k);
                this.last = -1;
            }
            
            @Override
            public void set(final double k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractDoubleList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractDoubleList.this.removeDouble(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }
        };
    }
    
    @Override
    public boolean contains(final double k) {
        return this.indexOf(k) >= 0;
    }
    
    @Override
    public int indexOf(final double k) {
        final DoubleListIterator i = this.listIterator();
        while (i.hasNext()) {
            final double e = i.nextDouble();
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(e)) {
                return i.previousIndex();
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final double k) {
        final DoubleListIterator i = this.listIterator(this.size());
        while (i.hasPrevious()) {
            final double e = i.previousDouble();
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(e)) {
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
                this.add(0.0);
            }
        }
        else {
            while (i-- != size) {
                this.remove(i);
            }
        }
    }
    
    @Override
    public DoubleList subList(final int from, final int to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new DoubleSubList(this, from, to);
    }
    
    @Deprecated
    @Override
    public DoubleList doubleSubList(final int from, final int to) {
        return this.subList(from, to);
    }
    
    @Override
    public void removeElements(final int from, final int to) {
        this.ensureIndex(to);
        final DoubleListIterator i = this.listIterator(from);
        int n = to - from;
        if (n < 0) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        while (n-- != 0) {
            i.nextDouble();
            i.remove();
        }
    }
    
    @Override
    public void addElements(int index, final double[] a, int offset, int length) {
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
    public void addElements(final int index, final double[] a) {
        this.addElements(index, a, 0, a.length);
    }
    
    @Override
    public void getElements(final int from, final double[] a, int offset, int length) {
        final DoubleListIterator i = this.listIterator(from);
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
            a[offset++] = i.nextDouble();
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
        if (l instanceof DoubleList) {
            final DoubleListIterator i1 = this.listIterator();
            final DoubleListIterator i2 = ((DoubleList)l).listIterator();
            while (s-- != 0) {
                if (i1.nextDouble() != i2.nextDouble()) {
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
    public int compareTo(final List<? extends Double> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof DoubleList) {
            final DoubleListIterator i1 = this.listIterator();
            final DoubleListIterator i2 = ((DoubleList)l).listIterator();
            while (i1.hasNext() && i2.hasNext()) {
                final double e1 = i1.nextDouble();
                final double e2 = i2.nextDouble();
                final int r;
                if ((r = Double.compare(e1, e2)) != 0) {
                    return r;
                }
            }
            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
        }
        final ListIterator<? extends Double> i3 = this.listIterator();
        final ListIterator<? extends Double> i4 = l.listIterator();
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
        final DoubleIterator i = this.iterator();
        int h = 1;
        int s = this.size();
        while (s-- != 0) {
            final double k = i.nextDouble();
            h = 31 * h + HashCommon.double2int(k);
        }
        return h;
    }
    
    @Override
    public void push(final double o) {
        this.add(o);
    }
    
    @Override
    public double popDouble() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.removeDouble(this.size() - 1);
    }
    
    @Override
    public double topDouble() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getDouble(this.size() - 1);
    }
    
    @Override
    public double peekDouble(final int i) {
        return this.getDouble(this.size() - 1 - i);
    }
    
    @Override
    public boolean rem(final double k) {
        final int index = this.indexOf(k);
        if (index == -1) {
            return false;
        }
        this.removeDouble(index);
        return true;
    }
    
    @Override
    public boolean remove(final Object o) {
        return this.rem((double)o);
    }
    
    @Override
    public boolean addAll(final int index, final DoubleCollection c) {
        return this.addAll(index, (Collection<? extends Double>)c);
    }
    
    @Override
    public boolean addAll(final int index, final DoubleList l) {
        return this.addAll(index, (DoubleCollection)l);
    }
    
    @Override
    public boolean addAll(final DoubleCollection c) {
        return this.addAll(this.size(), c);
    }
    
    @Override
    public boolean addAll(final DoubleList l) {
        return this.addAll(this.size(), l);
    }
    
    @Override
    public void add(final int index, final Double ok) {
        this.add(index, (double)ok);
    }
    
    @Deprecated
    @Override
    public Double set(final int index, final Double ok) {
        return this.set(index, (double)ok);
    }
    
    @Deprecated
    @Override
    public Double get(final int index) {
        return this.getDouble(index);
    }
    
    @Override
    public int indexOf(final Object ok) {
        return this.indexOf((double)ok);
    }
    
    @Override
    public int lastIndexOf(final Object ok) {
        return this.lastIndexOf((double)ok);
    }
    
    @Deprecated
    @Override
    public Double remove(final int index) {
        return this.removeDouble(index);
    }
    
    @Override
    public void push(final Double o) {
        this.push((double)o);
    }
    
    @Deprecated
    @Override
    public Double pop() {
        return this.popDouble();
    }
    
    @Deprecated
    @Override
    public Double top() {
        return this.topDouble();
    }
    
    @Deprecated
    @Override
    public Double peek(final int i) {
        return this.peekDouble(i);
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final DoubleIterator i = this.iterator();
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
            final double k = i.nextDouble();
            s.append(String.valueOf(k));
        }
        s.append("]");
        return s.toString();
    }
    
    public static class DoubleSubList extends AbstractDoubleList implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final DoubleList l;
        protected final int from;
        protected int to;
        private static final boolean ASSERTS = false;
        
        public DoubleSubList(final DoubleList l, final int from, final int to) {
            this.l = l;
            this.from = from;
            this.to = to;
        }
        
        private void assertRange() {
        }
        
        @Override
        public boolean add(final double k) {
            this.l.add(this.to, k);
            ++this.to;
            return true;
        }
        
        @Override
        public void add(final int index, final double k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Double> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        @Override
        public double getDouble(final int index) {
            this.ensureRestrictedIndex(index);
            return this.l.getDouble(this.from + index);
        }
        
        @Override
        public double removeDouble(final int index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeDouble(this.from + index);
        }
        
        @Override
        public double set(final int index, final double k) {
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
        public void getElements(final int from, final double[] a, final int offset, final int length) {
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
        public void addElements(final int index, final double[] a, final int offset, final int length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
        }
        
        @Override
        public DoubleListIterator listIterator(final int index) {
            this.ensureIndex(index);
            return new AbstractDoubleListIterator() {
                int pos = index;
                int last = -1;
                
                @Override
                public boolean hasNext() {
                    return this.pos < DoubleSubList.this.size();
                }
                
                @Override
                public boolean hasPrevious() {
                    return this.pos > 0;
                }
                
                @Override
                public double nextDouble() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final DoubleList l = DoubleSubList.this.l;
                    final int from = DoubleSubList.this.from;
                    final int last = this.pos++;
                    this.last = last;
                    return l.getDouble(from + last);
                }
                
                @Override
                public double previousDouble() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final DoubleList l = DoubleSubList.this.l;
                    final int from = DoubleSubList.this.from;
                    final int n = this.pos - 1;
                    this.pos = n;
                    this.last = n;
                    return l.getDouble(from + n);
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
                public void add(final double k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    DoubleSubList.this.add(this.pos++, k);
                    this.last = -1;
                }
                
                @Override
                public void set(final double k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    DoubleSubList.this.set(this.last, k);
                }
                
                @Override
                public void remove() {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    DoubleSubList.this.removeDouble(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1;
                }
            };
        }
        
        @Override
        public DoubleList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new DoubleSubList(this, from, to);
        }
        
        @Override
        public boolean rem(final double k) {
            final int index = this.indexOf(k);
            if (index == -1) {
                return false;
            }
            --this.to;
            this.l.removeDouble(this.from + index);
            return true;
        }
        
        @Override
        public boolean remove(final Object o) {
            return this.rem((double)o);
        }
        
        @Override
        public boolean addAll(final int index, final DoubleCollection c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        @Override
        public boolean addAll(final int index, final DoubleList l) {
            this.ensureIndex(index);
            this.to += l.size();
            return this.l.addAll(this.from + index, l);
        }
    }
}
