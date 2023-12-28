// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.BigListIterator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.BigList;
import java.util.Collection;
import java.io.Serializable;
import java.util.Random;

public class DoubleBigLists
{
    public static final EmptyBigList EMPTY_BIG_LIST;
    
    private DoubleBigLists() {
    }
    
    public static DoubleBigList shuffle(final DoubleBigList l, final Random random) {
        long i = l.size64();
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final double t = l.getDouble(i);
            l.set(i, l.getDouble(p));
            l.set(p, t);
        }
        return l;
    }
    
    public static DoubleBigList singleton(final double element) {
        return new Singleton(element);
    }
    
    public static DoubleBigList singleton(final Object element) {
        return new Singleton((double)element);
    }
    
    public static DoubleBigList synchronize(final DoubleBigList l) {
        return new SynchronizedBigList(l);
    }
    
    public static DoubleBigList synchronize(final DoubleBigList l, final Object sync) {
        return new SynchronizedBigList(l, sync);
    }
    
    public static DoubleBigList unmodifiable(final DoubleBigList l) {
        return new UnmodifiableBigList(l);
    }
    
    public static DoubleBigList asBigList(final DoubleList list) {
        return new ListBigList(list);
    }
    
    static {
        EMPTY_BIG_LIST = new EmptyBigList();
    }
    
    public static class EmptyBigList extends DoubleCollections.EmptyCollection implements DoubleBigList, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyBigList() {
        }
        
        @Override
        public void add(final long index, final double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double removeDouble(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double set(final long index, final double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long indexOf(final double k) {
            return -1L;
        }
        
        @Override
        public long lastIndexOf(final double k) {
            return -1L;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Double> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final Collection<? extends Double> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Double get(final long i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public boolean addAll(final DoubleCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final DoubleBigList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final DoubleCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final DoubleBigList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final long index, final Double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final Double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Double set(final long index, final Double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double getDouble(final long i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public Double remove(final long k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long indexOf(final Object k) {
            return -1L;
        }
        
        @Override
        public long lastIndexOf(final Object k) {
            return -1L;
        }
        
        @Override
        public DoubleBigListIterator listIterator() {
            return DoubleBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        @Override
        public DoubleBigListIterator iterator() {
            return DoubleBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        @Override
        public DoubleBigListIterator listIterator(final long i) {
            if (i == 0L) {
                return DoubleBigListIterators.EMPTY_BIG_LIST_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }
        
        @Override
        public DoubleBigList subList(final long from, final long to) {
            if (from == 0L && to == 0L) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public void getElements(final long from, final double[][] a, final long offset, final long length) {
            DoubleBigArrays.ensureOffsetLength(a, offset, length);
            if (from != 0L) {
                throw new IndexOutOfBoundsException();
            }
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final double[][] a, final long offset, final long length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final double[][] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final long s) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long size64() {
            return 0L;
        }
        
        @Override
        public int compareTo(final BigList<? extends Double> o) {
            if (o == this) {
                return 0;
            }
            return o.isEmpty() ? 0 : -1;
        }
        
        private Object readResolve() {
            return DoubleBigLists.EMPTY_BIG_LIST;
        }
        
        public Object clone() {
            return DoubleBigLists.EMPTY_BIG_LIST;
        }
        
        @Override
        public int hashCode() {
            return 1;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof BigList && ((BigList)o).isEmpty();
        }
        
        @Override
        public String toString() {
            return "[]";
        }
    }
    
    public static class Singleton extends AbstractDoubleBigList implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        private final double element;
        
        private Singleton(final double element) {
            this.element = element;
        }
        
        @Override
        public double getDouble(final long i) {
            if (i == 0L) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public double removeDouble(final long i) {
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
        public boolean addAll(final long i, final Collection<? extends Double> c) {
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
        public DoubleBigListIterator listIterator() {
            return DoubleBigListIterators.singleton(this.element);
        }
        
        @Override
        public DoubleBigListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public DoubleBigListIterator listIterator(final long i) {
            if (i > 1L || i < 0L) {
                throw new IndexOutOfBoundsException();
            }
            final DoubleBigListIterator l = this.listIterator();
            if (i == 1L) {
                l.next();
            }
            return l;
        }
        
        @Override
        public DoubleBigList subList(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            if (from != 0L || to != 1L) {
                return DoubleBigLists.EMPTY_BIG_LIST;
            }
            return this;
        }
        
        @Deprecated
        @Override
        public int size() {
            return 1;
        }
        
        @Override
        public long size64() {
            return 1L;
        }
        
        @Override
        public void size(final long size) {
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
        public boolean addAll(final long i, final DoubleCollection c) {
            throw new UnsupportedOperationException();
        }
    }
    
    public static class SynchronizedBigList extends DoubleCollections.SynchronizedCollection implements DoubleBigList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final DoubleBigList list;
        
        protected SynchronizedBigList(final DoubleBigList l, final Object sync) {
            super(l, sync);
            this.list = l;
        }
        
        protected SynchronizedBigList(final DoubleBigList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public double getDouble(final long i) {
            synchronized (this.sync) {
                return this.list.getDouble(i);
            }
        }
        
        @Override
        public double set(final long i, final double k) {
            synchronized (this.sync) {
                return this.list.set(i, k);
            }
        }
        
        @Override
        public void add(final long i, final double k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public double removeDouble(final long i) {
            synchronized (this.sync) {
                return this.list.removeDouble(i);
            }
        }
        
        @Override
        public long indexOf(final double k) {
            synchronized (this.sync) {
                return this.list.indexOf(k);
            }
        }
        
        @Override
        public long lastIndexOf(final double k) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(k);
            }
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Double> c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public void getElements(final long from, final double[][] a, final long offset, final long length) {
            synchronized (this.sync) {
                this.list.getElements(from, a, offset, length);
            }
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            synchronized (this.sync) {
                this.list.removeElements(from, to);
            }
        }
        
        @Override
        public void addElements(final long index, final double[][] a, final long offset, final long length) {
            synchronized (this.sync) {
                this.list.addElements(index, a, offset, length);
            }
        }
        
        @Override
        public void addElements(final long index, final double[][] a) {
            synchronized (this.sync) {
                this.list.addElements(index, a);
            }
        }
        
        @Override
        public void size(final long size) {
            synchronized (this.sync) {
                this.list.size(size);
            }
        }
        
        @Override
        public long size64() {
            synchronized (this.sync) {
                return this.list.size64();
            }
        }
        
        @Override
        public DoubleBigListIterator iterator() {
            return this.list.listIterator();
        }
        
        @Override
        public DoubleBigListIterator listIterator() {
            return this.list.listIterator();
        }
        
        @Override
        public DoubleBigListIterator listIterator(final long i) {
            return this.list.listIterator(i);
        }
        
        @Override
        public DoubleBigList subList(final long from, final long to) {
            synchronized (this.sync) {
                return DoubleBigLists.synchronize(this.list.subList(from, to), this.sync);
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            synchronized (this.sync) {
                return this.list.equals(o);
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.sync) {
                return this.list.hashCode();
            }
        }
        
        @Override
        public int compareTo(final BigList<? extends Double> o) {
            synchronized (this.sync) {
                return this.list.compareTo(o);
            }
        }
        
        @Override
        public boolean addAll(final long index, final DoubleCollection c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public boolean addAll(final long index, final DoubleBigList l) {
            synchronized (this.sync) {
                return this.list.addAll(index, l);
            }
        }
        
        @Override
        public boolean addAll(final DoubleBigList l) {
            synchronized (this.sync) {
                return this.list.addAll(l);
            }
        }
        
        @Override
        public Double get(final long i) {
            synchronized (this.sync) {
                return this.list.get(i);
            }
        }
        
        @Override
        public void add(final long i, final Double k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public Double set(final long index, final Double k) {
            synchronized (this.sync) {
                return this.list.set(index, k);
            }
        }
        
        @Override
        public Double remove(final long i) {
            synchronized (this.sync) {
                return this.list.remove(i);
            }
        }
        
        @Override
        public long indexOf(final Object o) {
            synchronized (this.sync) {
                return this.list.indexOf(o);
            }
        }
        
        @Override
        public long lastIndexOf(final Object o) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(o);
            }
        }
    }
    
    public static class UnmodifiableBigList extends DoubleCollections.UnmodifiableCollection implements DoubleBigList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final DoubleBigList list;
        
        protected UnmodifiableBigList(final DoubleBigList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public double getDouble(final long i) {
            return this.list.getDouble(i);
        }
        
        @Override
        public double set(final long i, final double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final long i, final double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double removeDouble(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long indexOf(final double k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public long lastIndexOf(final double k) {
            return this.list.lastIndexOf(k);
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Double> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void getElements(final long from, final double[][] a, final long offset, final long length) {
            this.list.getElements(from, a, offset, length);
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final double[][] a, final long offset, final long length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final double[][] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final long size) {
            this.list.size(size);
        }
        
        @Override
        public long size64() {
            return this.list.size64();
        }
        
        @Override
        public DoubleBigListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public DoubleBigListIterator listIterator() {
            return DoubleBigListIterators.unmodifiable(this.list.listIterator());
        }
        
        @Override
        public DoubleBigListIterator listIterator(final long i) {
            return DoubleBigListIterators.unmodifiable(this.list.listIterator(i));
        }
        
        @Override
        public DoubleBigList subList(final long from, final long to) {
            return DoubleBigLists.unmodifiable(this.list.subList(from, to));
        }
        
        @Override
        public boolean equals(final Object o) {
            return this.list.equals(o);
        }
        
        @Override
        public int hashCode() {
            return this.list.hashCode();
        }
        
        @Override
        public int compareTo(final BigList<? extends Double> o) {
            return this.list.compareTo(o);
        }
        
        @Override
        public boolean addAll(final long index, final DoubleCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final DoubleBigList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long index, final DoubleBigList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Double get(final long i) {
            return this.list.get(i);
        }
        
        @Override
        public void add(final long i, final Double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Double set(final long index, final Double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Double remove(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long indexOf(final Object o) {
            return this.list.indexOf(o);
        }
        
        @Override
        public long lastIndexOf(final Object o) {
            return this.list.lastIndexOf(o);
        }
    }
    
    public static class ListBigList extends AbstractDoubleBigList implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        private final DoubleList list;
        
        protected ListBigList(final DoubleList list) {
            this.list = list;
        }
        
        private int intIndex(final long index) {
            if (index >= 2147483647L) {
                throw new IndexOutOfBoundsException("This big list is restricted to 32-bit indices");
            }
            return (int)index;
        }
        
        @Override
        public long size64() {
            return this.list.size();
        }
        
        @Deprecated
        @Override
        public int size() {
            return this.list.size();
        }
        
        @Override
        public void size(final long size) {
            this.list.size(this.intIndex(size));
        }
        
        @Override
        public DoubleBigListIterator iterator() {
            return DoubleBigListIterators.asBigListIterator(this.list.iterator());
        }
        
        @Override
        public DoubleBigListIterator listIterator() {
            return DoubleBigListIterators.asBigListIterator(this.list.listIterator());
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Double> c) {
            return this.list.addAll(this.intIndex(index), c);
        }
        
        @Override
        public DoubleBigListIterator listIterator(final long index) {
            return DoubleBigListIterators.asBigListIterator(this.list.listIterator(this.intIndex(index)));
        }
        
        @Override
        public DoubleBigList subList(final long from, final long to) {
            return new ListBigList(this.list.subList(this.intIndex(from), this.intIndex(to)));
        }
        
        @Override
        public boolean contains(final double key) {
            return this.list.contains(key);
        }
        
        @Override
        public double[] toDoubleArray() {
            return this.list.toDoubleArray();
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            this.list.removeElements(this.intIndex(from), this.intIndex(to));
        }
        
        @Override
        public double[] toDoubleArray(final double[] a) {
            return this.list.toDoubleArray(a);
        }
        
        @Override
        public void add(final long index, final double key) {
            this.list.add(this.intIndex(index), key);
        }
        
        @Override
        public boolean addAll(final long index, final DoubleCollection c) {
            return this.list.addAll(this.intIndex(index), c);
        }
        
        @Override
        public boolean addAll(final long index, final DoubleBigList c) {
            return this.list.addAll(this.intIndex(index), c);
        }
        
        @Override
        public boolean add(final double key) {
            return this.list.add(key);
        }
        
        @Override
        public boolean addAll(final DoubleBigList c) {
            return this.list.addAll(c);
        }
        
        @Override
        public double getDouble(final long index) {
            return this.list.getDouble(this.intIndex(index));
        }
        
        @Override
        public long indexOf(final double k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public long lastIndexOf(final double k) {
            return this.list.lastIndexOf(k);
        }
        
        @Override
        public double removeDouble(final long index) {
            return this.list.removeDouble(this.intIndex(index));
        }
        
        @Override
        public double set(final long index, final double k) {
            return this.list.set(this.intIndex(index), k);
        }
        
        @Override
        public boolean addAll(final DoubleCollection c) {
            return this.list.addAll(c);
        }
        
        @Override
        public boolean containsAll(final DoubleCollection c) {
            return this.list.containsAll(c);
        }
        
        @Override
        public boolean removeAll(final DoubleCollection c) {
            return this.list.removeAll(c);
        }
        
        @Override
        public boolean retainAll(final DoubleCollection c) {
            return this.list.retainAll(c);
        }
        
        @Override
        public boolean isEmpty() {
            return this.list.isEmpty();
        }
        
        @Override
        public <T> T[] toArray(final T[] a) {
            return this.list.toArray(a);
        }
        
        @Override
        public boolean containsAll(final Collection<?> c) {
            return this.list.containsAll(c);
        }
        
        @Override
        public boolean addAll(final Collection<? extends Double> c) {
            return this.list.addAll(c);
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            return this.list.removeAll(c);
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            return this.list.retainAll(c);
        }
        
        @Override
        public void clear() {
            this.list.clear();
        }
        
        @Override
        public int hashCode() {
            return this.list.hashCode();
        }
    }
}
