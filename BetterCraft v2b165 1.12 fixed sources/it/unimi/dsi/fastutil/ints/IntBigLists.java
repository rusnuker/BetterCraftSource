// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.BigListIterator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.BigList;
import java.util.Collection;
import java.io.Serializable;
import java.util.Random;

public class IntBigLists
{
    public static final EmptyBigList EMPTY_BIG_LIST;
    
    private IntBigLists() {
    }
    
    public static IntBigList shuffle(final IntBigList l, final Random random) {
        long i = l.size64();
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final int t = l.getInt(i);
            l.set(i, l.getInt(p));
            l.set(p, t);
        }
        return l;
    }
    
    public static IntBigList singleton(final int element) {
        return new Singleton(element);
    }
    
    public static IntBigList singleton(final Object element) {
        return new Singleton((int)element);
    }
    
    public static IntBigList synchronize(final IntBigList l) {
        return new SynchronizedBigList(l);
    }
    
    public static IntBigList synchronize(final IntBigList l, final Object sync) {
        return new SynchronizedBigList(l, sync);
    }
    
    public static IntBigList unmodifiable(final IntBigList l) {
        return new UnmodifiableBigList(l);
    }
    
    public static IntBigList asBigList(final IntList list) {
        return new ListBigList(list);
    }
    
    static {
        EMPTY_BIG_LIST = new EmptyBigList();
    }
    
    public static class EmptyBigList extends IntCollections.EmptyCollection implements IntBigList, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyBigList() {
        }
        
        @Override
        public void add(final long index, final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int removeInt(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int set(final long index, final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long indexOf(final int k) {
            return -1L;
        }
        
        @Override
        public long lastIndexOf(final int k) {
            return -1L;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Integer get(final long i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public boolean addAll(final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final IntBigList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final IntBigList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final long index, final Integer k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final Integer k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Integer set(final long index, final Integer k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int getInt(final long i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public Integer remove(final long k) {
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
        public IntBigListIterator listIterator() {
            return IntBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        @Override
        public IntBigListIterator iterator() {
            return IntBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        @Override
        public IntBigListIterator listIterator(final long i) {
            if (i == 0L) {
                return IntBigListIterators.EMPTY_BIG_LIST_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }
        
        @Override
        public IntBigList subList(final long from, final long to) {
            if (from == 0L && to == 0L) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public void getElements(final long from, final int[][] a, final long offset, final long length) {
            IntBigArrays.ensureOffsetLength(a, offset, length);
            if (from != 0L) {
                throw new IndexOutOfBoundsException();
            }
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final int[][] a, final long offset, final long length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final int[][] a) {
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
        public int compareTo(final BigList<? extends Integer> o) {
            if (o == this) {
                return 0;
            }
            return o.isEmpty() ? 0 : -1;
        }
        
        private Object readResolve() {
            return IntBigLists.EMPTY_BIG_LIST;
        }
        
        public Object clone() {
            return IntBigLists.EMPTY_BIG_LIST;
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
    
    public static class Singleton extends AbstractIntBigList implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        private final int element;
        
        private Singleton(final int element) {
            this.element = element;
        }
        
        @Override
        public int getInt(final long i) {
            if (i == 0L) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public int removeInt(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final int k) {
            return k == this.element;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final Collection<? extends Integer> c) {
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
        public int[] toIntArray() {
            final int[] a = { this.element };
            return a;
        }
        
        @Override
        public IntBigListIterator listIterator() {
            return IntBigListIterators.singleton(this.element);
        }
        
        @Override
        public IntBigListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public IntBigListIterator listIterator(final long i) {
            if (i > 1L || i < 0L) {
                throw new IndexOutOfBoundsException();
            }
            final IntBigListIterator l = this.listIterator();
            if (i == 1L) {
                l.next();
            }
            return l;
        }
        
        @Override
        public IntBigList subList(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            if (from != 0L || to != 1L) {
                return IntBigLists.EMPTY_BIG_LIST;
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
        public boolean rem(final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final IntCollection c) {
            throw new UnsupportedOperationException();
        }
    }
    
    public static class SynchronizedBigList extends IntCollections.SynchronizedCollection implements IntBigList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntBigList list;
        
        protected SynchronizedBigList(final IntBigList l, final Object sync) {
            super(l, sync);
            this.list = l;
        }
        
        protected SynchronizedBigList(final IntBigList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public int getInt(final long i) {
            synchronized (this.sync) {
                return this.list.getInt(i);
            }
        }
        
        @Override
        public int set(final long i, final int k) {
            synchronized (this.sync) {
                return this.list.set(i, k);
            }
        }
        
        @Override
        public void add(final long i, final int k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public int removeInt(final long i) {
            synchronized (this.sync) {
                return this.list.removeInt(i);
            }
        }
        
        @Override
        public long indexOf(final int k) {
            synchronized (this.sync) {
                return this.list.indexOf(k);
            }
        }
        
        @Override
        public long lastIndexOf(final int k) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(k);
            }
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Integer> c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public void getElements(final long from, final int[][] a, final long offset, final long length) {
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
        public void addElements(final long index, final int[][] a, final long offset, final long length) {
            synchronized (this.sync) {
                this.list.addElements(index, a, offset, length);
            }
        }
        
        @Override
        public void addElements(final long index, final int[][] a) {
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
        public IntBigListIterator iterator() {
            return this.list.listIterator();
        }
        
        @Override
        public IntBigListIterator listIterator() {
            return this.list.listIterator();
        }
        
        @Override
        public IntBigListIterator listIterator(final long i) {
            return this.list.listIterator(i);
        }
        
        @Override
        public IntBigList subList(final long from, final long to) {
            synchronized (this.sync) {
                return IntBigLists.synchronize(this.list.subList(from, to), this.sync);
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
        public int compareTo(final BigList<? extends Integer> o) {
            synchronized (this.sync) {
                return this.list.compareTo(o);
            }
        }
        
        @Override
        public boolean addAll(final long index, final IntCollection c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public boolean addAll(final long index, final IntBigList l) {
            synchronized (this.sync) {
                return this.list.addAll(index, l);
            }
        }
        
        @Override
        public boolean addAll(final IntBigList l) {
            synchronized (this.sync) {
                return this.list.addAll(l);
            }
        }
        
        @Override
        public Integer get(final long i) {
            synchronized (this.sync) {
                return this.list.get(i);
            }
        }
        
        @Override
        public void add(final long i, final Integer k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public Integer set(final long index, final Integer k) {
            synchronized (this.sync) {
                return this.list.set(index, k);
            }
        }
        
        @Override
        public Integer remove(final long i) {
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
    
    public static class UnmodifiableBigList extends IntCollections.UnmodifiableCollection implements IntBigList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntBigList list;
        
        protected UnmodifiableBigList(final IntBigList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public int getInt(final long i) {
            return this.list.getInt(i);
        }
        
        @Override
        public int set(final long i, final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final long i, final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int removeInt(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long indexOf(final int k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public long lastIndexOf(final int k) {
            return this.list.lastIndexOf(k);
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void getElements(final long from, final int[][] a, final long offset, final long length) {
            this.list.getElements(from, a, offset, length);
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final int[][] a, final long offset, final long length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final int[][] a) {
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
        public IntBigListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public IntBigListIterator listIterator() {
            return IntBigListIterators.unmodifiable(this.list.listIterator());
        }
        
        @Override
        public IntBigListIterator listIterator(final long i) {
            return IntBigListIterators.unmodifiable(this.list.listIterator(i));
        }
        
        @Override
        public IntBigList subList(final long from, final long to) {
            return IntBigLists.unmodifiable(this.list.subList(from, to));
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
        public int compareTo(final BigList<? extends Integer> o) {
            return this.list.compareTo(o);
        }
        
        @Override
        public boolean addAll(final long index, final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final IntBigList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long index, final IntBigList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Integer get(final long i) {
            return this.list.get(i);
        }
        
        @Override
        public void add(final long i, final Integer k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Integer set(final long index, final Integer k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Integer remove(final long i) {
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
    
    public static class ListBigList extends AbstractIntBigList implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        private final IntList list;
        
        protected ListBigList(final IntList list) {
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
        public IntBigListIterator iterator() {
            return IntBigListIterators.asBigListIterator(this.list.iterator());
        }
        
        @Override
        public IntBigListIterator listIterator() {
            return IntBigListIterators.asBigListIterator(this.list.listIterator());
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Integer> c) {
            return this.list.addAll(this.intIndex(index), c);
        }
        
        @Override
        public IntBigListIterator listIterator(final long index) {
            return IntBigListIterators.asBigListIterator(this.list.listIterator(this.intIndex(index)));
        }
        
        @Override
        public IntBigList subList(final long from, final long to) {
            return new ListBigList(this.list.subList(this.intIndex(from), this.intIndex(to)));
        }
        
        @Override
        public boolean contains(final int key) {
            return this.list.contains(key);
        }
        
        @Override
        public int[] toIntArray() {
            return this.list.toIntArray();
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            this.list.removeElements(this.intIndex(from), this.intIndex(to));
        }
        
        @Override
        public int[] toIntArray(final int[] a) {
            return this.list.toIntArray(a);
        }
        
        @Override
        public void add(final long index, final int key) {
            this.list.add(this.intIndex(index), key);
        }
        
        @Override
        public boolean addAll(final long index, final IntCollection c) {
            return this.list.addAll(this.intIndex(index), c);
        }
        
        @Override
        public boolean addAll(final long index, final IntBigList c) {
            return this.list.addAll(this.intIndex(index), c);
        }
        
        @Override
        public boolean add(final int key) {
            return this.list.add(key);
        }
        
        @Override
        public boolean addAll(final IntBigList c) {
            return this.list.addAll(c);
        }
        
        @Override
        public int getInt(final long index) {
            return this.list.getInt(this.intIndex(index));
        }
        
        @Override
        public long indexOf(final int k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public long lastIndexOf(final int k) {
            return this.list.lastIndexOf(k);
        }
        
        @Override
        public int removeInt(final long index) {
            return this.list.removeInt(this.intIndex(index));
        }
        
        @Override
        public int set(final long index, final int k) {
            return this.list.set(this.intIndex(index), k);
        }
        
        @Override
        public boolean addAll(final IntCollection c) {
            return this.list.addAll(c);
        }
        
        @Override
        public boolean containsAll(final IntCollection c) {
            return this.list.containsAll(c);
        }
        
        @Override
        public boolean removeAll(final IntCollection c) {
            return this.list.removeAll(c);
        }
        
        @Override
        public boolean retainAll(final IntCollection c) {
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
        public boolean addAll(final Collection<? extends Integer> c) {
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
