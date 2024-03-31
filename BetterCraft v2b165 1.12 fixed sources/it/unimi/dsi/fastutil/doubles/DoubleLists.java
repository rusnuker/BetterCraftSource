// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import java.util.ListIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.io.Serializable;
import java.util.Random;

public class DoubleLists
{
    public static final EmptyList EMPTY_LIST;
    
    private DoubleLists() {
    }
    
    public static DoubleList shuffle(final DoubleList l, final Random random) {
        int i = l.size();
        while (i-- != 0) {
            final int p = random.nextInt(i + 1);
            final double t = l.getDouble(i);
            l.set(i, l.getDouble(p));
            l.set(p, t);
        }
        return l;
    }
    
    public static DoubleList singleton(final double element) {
        return new Singleton(element);
    }
    
    public static DoubleList singleton(final Object element) {
        return new Singleton((double)element);
    }
    
    public static DoubleList synchronize(final DoubleList l) {
        return new SynchronizedList(l);
    }
    
    public static DoubleList synchronize(final DoubleList l, final Object sync) {
        return new SynchronizedList(l, sync);
    }
    
    public static DoubleList unmodifiable(final DoubleList l) {
        return new UnmodifiableList(l);
    }
    
    static {
        EMPTY_LIST = new EmptyList();
    }
    
    public static class EmptyList extends DoubleCollections.EmptyCollection implements DoubleList, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyList() {
        }
        
        @Override
        public void add(final int index, final double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final double k) {
            throw new UnsupportedOperationException();
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
        public int indexOf(final double k) {
            return -1;
        }
        
        @Override
        public int lastIndexOf(final double k) {
            return -1;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Double> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final Collection<? extends Double> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Double get(final int i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public boolean addAll(final DoubleCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final DoubleList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final DoubleCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final DoubleList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final int index, final Double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final Double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Double set(final int index, final Double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double getDouble(final int i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public Double remove(final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int indexOf(final Object k) {
            return -1;
        }
        
        @Override
        public int lastIndexOf(final Object k) {
            return -1;
        }
        
        @Deprecated
        @Override
        public DoubleIterator doubleIterator() {
            return DoubleIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public DoubleListIterator listIterator() {
            return DoubleIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public DoubleListIterator iterator() {
            return DoubleIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public DoubleListIterator listIterator(final int i) {
            if (i == 0) {
                return DoubleIterators.EMPTY_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }
        
        @Deprecated
        @Override
        public DoubleListIterator doubleListIterator() {
            return this.listIterator();
        }
        
        @Deprecated
        @Override
        public DoubleListIterator doubleListIterator(final int i) {
            return this.listIterator(i);
        }
        
        @Override
        public DoubleList subList(final int from, final int to) {
            if (from == 0 && to == 0) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Deprecated
        @Override
        public DoubleList doubleSubList(final int from, final int to) {
            return this.subList(from, to);
        }
        
        @Override
        public void getElements(final int from, final double[] a, final int offset, final int length) {
            if (from == 0 && length == 0 && offset >= 0 && offset <= a.length) {
                return;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final double[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final double[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final int s) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int compareTo(final List<? extends Double> o) {
            if (o == this) {
                return 0;
            }
            return o.isEmpty() ? 0 : -1;
        }
        
        private Object readResolve() {
            return DoubleLists.EMPTY_LIST;
        }
        
        public Object clone() {
            return DoubleLists.EMPTY_LIST;
        }
        
        @Override
        public int hashCode() {
            return 1;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof List && ((List)o).isEmpty();
        }
        
        @Override
        public String toString() {
            return "[]";
        }
    }
    
    public static class Singleton extends AbstractDoubleList implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        private final double element;
        
        private Singleton(final double element) {
            this.element = element;
        }
        
        @Override
        public double getDouble(final int i) {
            if (i == 0) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public double removeDouble(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final double k) {
            return Double.doubleToLongBits(k) == Double.doubleToLongBits(this.element);
        }
        
        @Override
        public boolean addAll(final Collection<? extends Double> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final Collection<? extends Double> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double[] toDoubleArray() {
            final double[] a = { this.element };
            return a;
        }
        
        @Override
        public DoubleListIterator listIterator() {
            return DoubleIterators.singleton(this.element);
        }
        
        @Override
        public DoubleListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public DoubleListIterator listIterator(final int i) {
            if (i > 1 || i < 0) {
                throw new IndexOutOfBoundsException();
            }
            final DoubleListIterator l = this.listIterator();
            if (i == 1) {
                l.next();
            }
            return l;
        }
        
        @Override
        public DoubleList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            if (from != 0 || to != 1) {
                return DoubleLists.EMPTY_LIST;
            }
            return this;
        }
        
        @Override
        public int size() {
            return 1;
        }
        
        @Override
        public void size(final int size) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        public Object clone() {
            return this;
        }
        
        @Override
        public boolean rem(final double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final DoubleCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final DoubleCollection c) {
            throw new UnsupportedOperationException();
        }
    }
    
    public static class SynchronizedList extends DoubleCollections.SynchronizedCollection implements DoubleList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final DoubleList list;
        
        protected SynchronizedList(final DoubleList l, final Object sync) {
            super(l, sync);
            this.list = l;
        }
        
        protected SynchronizedList(final DoubleList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public double getDouble(final int i) {
            synchronized (this.sync) {
                return this.list.getDouble(i);
            }
        }
        
        @Override
        public double set(final int i, final double k) {
            synchronized (this.sync) {
                return this.list.set(i, k);
            }
        }
        
        @Override
        public void add(final int i, final double k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public double removeDouble(final int i) {
            synchronized (this.sync) {
                return this.list.removeDouble(i);
            }
        }
        
        @Override
        public int indexOf(final double k) {
            synchronized (this.sync) {
                return this.list.indexOf(k);
            }
        }
        
        @Override
        public int lastIndexOf(final double k) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(k);
            }
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Double> c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public void getElements(final int from, final double[] a, final int offset, final int length) {
            synchronized (this.sync) {
                this.list.getElements(from, a, offset, length);
            }
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            synchronized (this.sync) {
                this.list.removeElements(from, to);
            }
        }
        
        @Override
        public void addElements(final int index, final double[] a, final int offset, final int length) {
            synchronized (this.sync) {
                this.list.addElements(index, a, offset, length);
            }
        }
        
        @Override
        public void addElements(final int index, final double[] a) {
            synchronized (this.sync) {
                this.list.addElements(index, a);
            }
        }
        
        @Override
        public void size(final int size) {
            synchronized (this.sync) {
                this.list.size(size);
            }
        }
        
        @Override
        public DoubleListIterator iterator() {
            return this.list.listIterator();
        }
        
        @Override
        public DoubleListIterator listIterator() {
            return this.list.listIterator();
        }
        
        @Override
        public DoubleListIterator listIterator(final int i) {
            return this.list.listIterator(i);
        }
        
        @Deprecated
        @Override
        public DoubleListIterator doubleListIterator() {
            return this.listIterator();
        }
        
        @Deprecated
        @Override
        public DoubleListIterator doubleListIterator(final int i) {
            return this.listIterator(i);
        }
        
        @Override
        public DoubleList subList(final int from, final int to) {
            synchronized (this.sync) {
                return DoubleLists.synchronize(this.list.subList(from, to), this.sync);
            }
        }
        
        @Deprecated
        @Override
        public DoubleList doubleSubList(final int from, final int to) {
            return this.subList(from, to);
        }
        
        @Override
        public boolean equals(final Object o) {
            synchronized (this.sync) {
                return this.collection.equals(o);
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.sync) {
                return this.collection.hashCode();
            }
        }
        
        @Override
        public int compareTo(final List<? extends Double> o) {
            synchronized (this.sync) {
                return this.list.compareTo(o);
            }
        }
        
        @Override
        public boolean addAll(final int index, final DoubleCollection c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public boolean addAll(final int index, final DoubleList l) {
            synchronized (this.sync) {
                return this.list.addAll(index, l);
            }
        }
        
        @Override
        public boolean addAll(final DoubleList l) {
            synchronized (this.sync) {
                return this.list.addAll(l);
            }
        }
        
        @Override
        public Double get(final int i) {
            synchronized (this.sync) {
                return this.list.get(i);
            }
        }
        
        @Override
        public void add(final int i, final Double k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public Double set(final int index, final Double k) {
            synchronized (this.sync) {
                return this.list.set(index, k);
            }
        }
        
        @Override
        public Double remove(final int i) {
            synchronized (this.sync) {
                return this.list.remove(i);
            }
        }
        
        @Override
        public int indexOf(final Object o) {
            synchronized (this.sync) {
                return this.list.indexOf(o);
            }
        }
        
        @Override
        public int lastIndexOf(final Object o) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(o);
            }
        }
    }
    
    public static class UnmodifiableList extends DoubleCollections.UnmodifiableCollection implements DoubleList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final DoubleList list;
        
        protected UnmodifiableList(final DoubleList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public double getDouble(final int i) {
            return this.list.getDouble(i);
        }
        
        @Override
        public double set(final int i, final double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final int i, final double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double removeDouble(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int indexOf(final double k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public int lastIndexOf(final double k) {
            return this.list.lastIndexOf(k);
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Double> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void getElements(final int from, final double[] a, final int offset, final int length) {
            this.list.getElements(from, a, offset, length);
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final double[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final double[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final int size) {
            this.list.size(size);
        }
        
        @Override
        public DoubleListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public DoubleListIterator listIterator() {
            return DoubleIterators.unmodifiable(this.list.listIterator());
        }
        
        @Override
        public DoubleListIterator listIterator(final int i) {
            return DoubleIterators.unmodifiable(this.list.listIterator(i));
        }
        
        @Deprecated
        @Override
        public DoubleListIterator doubleListIterator() {
            return this.listIterator();
        }
        
        @Deprecated
        @Override
        public DoubleListIterator doubleListIterator(final int i) {
            return this.listIterator(i);
        }
        
        @Override
        public DoubleList subList(final int from, final int to) {
            return DoubleLists.unmodifiable(this.list.subList(from, to));
        }
        
        @Deprecated
        @Override
        public DoubleList doubleSubList(final int from, final int to) {
            return this.subList(from, to);
        }
        
        @Override
        public boolean equals(final Object o) {
            return this.collection.equals(o);
        }
        
        @Override
        public int hashCode() {
            return this.collection.hashCode();
        }
        
        @Override
        public int compareTo(final List<? extends Double> o) {
            return this.list.compareTo(o);
        }
        
        @Override
        public boolean addAll(final int index, final DoubleCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final DoubleList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int index, final DoubleList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Double get(final int i) {
            return this.list.get(i);
        }
        
        @Override
        public void add(final int i, final Double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Double set(final int index, final Double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Double remove(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int indexOf(final Object o) {
            return this.list.indexOf(o);
        }
        
        @Override
        public int lastIndexOf(final Object o) {
            return this.list.lastIndexOf(o);
        }
    }
}
