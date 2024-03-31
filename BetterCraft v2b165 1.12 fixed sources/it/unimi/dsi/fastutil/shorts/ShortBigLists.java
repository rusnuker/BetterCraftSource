// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.BigListIterator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.BigList;
import java.util.Collection;
import java.io.Serializable;
import java.util.Random;

public class ShortBigLists
{
    public static final EmptyBigList EMPTY_BIG_LIST;
    
    private ShortBigLists() {
    }
    
    public static ShortBigList shuffle(final ShortBigList l, final Random random) {
        long i = l.size64();
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final short t = l.getShort(i);
            l.set(i, l.getShort(p));
            l.set(p, t);
        }
        return l;
    }
    
    public static ShortBigList singleton(final short element) {
        return new Singleton(element);
    }
    
    public static ShortBigList singleton(final Object element) {
        return new Singleton((short)element);
    }
    
    public static ShortBigList synchronize(final ShortBigList l) {
        return new SynchronizedBigList(l);
    }
    
    public static ShortBigList synchronize(final ShortBigList l, final Object sync) {
        return new SynchronizedBigList(l, sync);
    }
    
    public static ShortBigList unmodifiable(final ShortBigList l) {
        return new UnmodifiableBigList(l);
    }
    
    public static ShortBigList asBigList(final ShortList list) {
        return new ListBigList(list);
    }
    
    static {
        EMPTY_BIG_LIST = new EmptyBigList();
    }
    
    public static class EmptyBigList extends ShortCollections.EmptyCollection implements ShortBigList, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyBigList() {
        }
        
        @Override
        public void add(final long index, final short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short removeShort(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short set(final long index, final short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long indexOf(final short k) {
            return -1L;
        }
        
        @Override
        public long lastIndexOf(final short k) {
            return -1L;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Short> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final Collection<? extends Short> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Short get(final long i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public boolean addAll(final ShortCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final ShortBigList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final ShortCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final ShortBigList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final long index, final Short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final Short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Short set(final long index, final Short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short getShort(final long i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public Short remove(final long k) {
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
        public ShortBigListIterator listIterator() {
            return ShortBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        @Override
        public ShortBigListIterator iterator() {
            return ShortBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        @Override
        public ShortBigListIterator listIterator(final long i) {
            if (i == 0L) {
                return ShortBigListIterators.EMPTY_BIG_LIST_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }
        
        @Override
        public ShortBigList subList(final long from, final long to) {
            if (from == 0L && to == 0L) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public void getElements(final long from, final short[][] a, final long offset, final long length) {
            ShortBigArrays.ensureOffsetLength(a, offset, length);
            if (from != 0L) {
                throw new IndexOutOfBoundsException();
            }
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final short[][] a, final long offset, final long length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final short[][] a) {
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
        public int compareTo(final BigList<? extends Short> o) {
            if (o == this) {
                return 0;
            }
            return o.isEmpty() ? 0 : -1;
        }
        
        private Object readResolve() {
            return ShortBigLists.EMPTY_BIG_LIST;
        }
        
        public Object clone() {
            return ShortBigLists.EMPTY_BIG_LIST;
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
    
    public static class Singleton extends AbstractShortBigList implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        private final short element;
        
        private Singleton(final short element) {
            this.element = element;
        }
        
        @Override
        public short getShort(final long i) {
            if (i == 0L) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public short removeShort(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final short k) {
            return k == this.element;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Short> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final Collection<? extends Short> c) {
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
        public short[] toShortArray() {
            final short[] a = { this.element };
            return a;
        }
        
        @Override
        public ShortBigListIterator listIterator() {
            return ShortBigListIterators.singleton(this.element);
        }
        
        @Override
        public ShortBigListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public ShortBigListIterator listIterator(final long i) {
            if (i > 1L || i < 0L) {
                throw new IndexOutOfBoundsException();
            }
            final ShortBigListIterator l = this.listIterator();
            if (i == 1L) {
                l.next();
            }
            return l;
        }
        
        @Override
        public ShortBigList subList(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            if (from != 0L || to != 1L) {
                return ShortBigLists.EMPTY_BIG_LIST;
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
        public boolean rem(final short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final ShortCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final ShortCollection c) {
            throw new UnsupportedOperationException();
        }
    }
    
    public static class SynchronizedBigList extends ShortCollections.SynchronizedCollection implements ShortBigList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ShortBigList list;
        
        protected SynchronizedBigList(final ShortBigList l, final Object sync) {
            super(l, sync);
            this.list = l;
        }
        
        protected SynchronizedBigList(final ShortBigList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public short getShort(final long i) {
            synchronized (this.sync) {
                return this.list.getShort(i);
            }
        }
        
        @Override
        public short set(final long i, final short k) {
            synchronized (this.sync) {
                return this.list.set(i, k);
            }
        }
        
        @Override
        public void add(final long i, final short k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public short removeShort(final long i) {
            synchronized (this.sync) {
                return this.list.removeShort(i);
            }
        }
        
        @Override
        public long indexOf(final short k) {
            synchronized (this.sync) {
                return this.list.indexOf(k);
            }
        }
        
        @Override
        public long lastIndexOf(final short k) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(k);
            }
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Short> c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public void getElements(final long from, final short[][] a, final long offset, final long length) {
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
        public void addElements(final long index, final short[][] a, final long offset, final long length) {
            synchronized (this.sync) {
                this.list.addElements(index, a, offset, length);
            }
        }
        
        @Override
        public void addElements(final long index, final short[][] a) {
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
        public ShortBigListIterator iterator() {
            return this.list.listIterator();
        }
        
        @Override
        public ShortBigListIterator listIterator() {
            return this.list.listIterator();
        }
        
        @Override
        public ShortBigListIterator listIterator(final long i) {
            return this.list.listIterator(i);
        }
        
        @Override
        public ShortBigList subList(final long from, final long to) {
            synchronized (this.sync) {
                return ShortBigLists.synchronize(this.list.subList(from, to), this.sync);
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
        public int compareTo(final BigList<? extends Short> o) {
            synchronized (this.sync) {
                return this.list.compareTo(o);
            }
        }
        
        @Override
        public boolean addAll(final long index, final ShortCollection c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public boolean addAll(final long index, final ShortBigList l) {
            synchronized (this.sync) {
                return this.list.addAll(index, l);
            }
        }
        
        @Override
        public boolean addAll(final ShortBigList l) {
            synchronized (this.sync) {
                return this.list.addAll(l);
            }
        }
        
        @Override
        public Short get(final long i) {
            synchronized (this.sync) {
                return this.list.get(i);
            }
        }
        
        @Override
        public void add(final long i, final Short k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public Short set(final long index, final Short k) {
            synchronized (this.sync) {
                return this.list.set(index, k);
            }
        }
        
        @Override
        public Short remove(final long i) {
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
    
    public static class UnmodifiableBigList extends ShortCollections.UnmodifiableCollection implements ShortBigList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ShortBigList list;
        
        protected UnmodifiableBigList(final ShortBigList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public short getShort(final long i) {
            return this.list.getShort(i);
        }
        
        @Override
        public short set(final long i, final short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final long i, final short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short removeShort(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long indexOf(final short k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public long lastIndexOf(final short k) {
            return this.list.lastIndexOf(k);
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Short> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void getElements(final long from, final short[][] a, final long offset, final long length) {
            this.list.getElements(from, a, offset, length);
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final short[][] a, final long offset, final long length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final short[][] a) {
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
        public ShortBigListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public ShortBigListIterator listIterator() {
            return ShortBigListIterators.unmodifiable(this.list.listIterator());
        }
        
        @Override
        public ShortBigListIterator listIterator(final long i) {
            return ShortBigListIterators.unmodifiable(this.list.listIterator(i));
        }
        
        @Override
        public ShortBigList subList(final long from, final long to) {
            return ShortBigLists.unmodifiable(this.list.subList(from, to));
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
        public int compareTo(final BigList<? extends Short> o) {
            return this.list.compareTo(o);
        }
        
        @Override
        public boolean addAll(final long index, final ShortCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final ShortBigList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long index, final ShortBigList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Short get(final long i) {
            return this.list.get(i);
        }
        
        @Override
        public void add(final long i, final Short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Short set(final long index, final Short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Short remove(final long i) {
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
    
    public static class ListBigList extends AbstractShortBigList implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        private final ShortList list;
        
        protected ListBigList(final ShortList list) {
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
        public ShortBigListIterator iterator() {
            return ShortBigListIterators.asBigListIterator(this.list.iterator());
        }
        
        @Override
        public ShortBigListIterator listIterator() {
            return ShortBigListIterators.asBigListIterator(this.list.listIterator());
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Short> c) {
            return this.list.addAll(this.intIndex(index), c);
        }
        
        @Override
        public ShortBigListIterator listIterator(final long index) {
            return ShortBigListIterators.asBigListIterator(this.list.listIterator(this.intIndex(index)));
        }
        
        @Override
        public ShortBigList subList(final long from, final long to) {
            return new ListBigList(this.list.subList(this.intIndex(from), this.intIndex(to)));
        }
        
        @Override
        public boolean contains(final short key) {
            return this.list.contains(key);
        }
        
        @Override
        public short[] toShortArray() {
            return this.list.toShortArray();
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            this.list.removeElements(this.intIndex(from), this.intIndex(to));
        }
        
        @Override
        public short[] toShortArray(final short[] a) {
            return this.list.toShortArray(a);
        }
        
        @Override
        public void add(final long index, final short key) {
            this.list.add(this.intIndex(index), key);
        }
        
        @Override
        public boolean addAll(final long index, final ShortCollection c) {
            return this.list.addAll(this.intIndex(index), c);
        }
        
        @Override
        public boolean addAll(final long index, final ShortBigList c) {
            return this.list.addAll(this.intIndex(index), c);
        }
        
        @Override
        public boolean add(final short key) {
            return this.list.add(key);
        }
        
        @Override
        public boolean addAll(final ShortBigList c) {
            return this.list.addAll(c);
        }
        
        @Override
        public short getShort(final long index) {
            return this.list.getShort(this.intIndex(index));
        }
        
        @Override
        public long indexOf(final short k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public long lastIndexOf(final short k) {
            return this.list.lastIndexOf(k);
        }
        
        @Override
        public short removeShort(final long index) {
            return this.list.removeShort(this.intIndex(index));
        }
        
        @Override
        public short set(final long index, final short k) {
            return this.list.set(this.intIndex(index), k);
        }
        
        @Override
        public boolean addAll(final ShortCollection c) {
            return this.list.addAll(c);
        }
        
        @Override
        public boolean containsAll(final ShortCollection c) {
            return this.list.containsAll(c);
        }
        
        @Override
        public boolean removeAll(final ShortCollection c) {
            return this.list.removeAll(c);
        }
        
        @Override
        public boolean retainAll(final ShortCollection c) {
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
        public boolean addAll(final Collection<? extends Short> c) {
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
