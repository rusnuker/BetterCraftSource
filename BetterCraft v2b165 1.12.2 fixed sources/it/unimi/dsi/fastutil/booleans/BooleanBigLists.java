// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

import it.unimi.dsi.fastutil.BigListIterator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.BigList;
import java.util.Collection;
import java.io.Serializable;
import java.util.Random;

public class BooleanBigLists
{
    public static final EmptyBigList EMPTY_BIG_LIST;
    
    private BooleanBigLists() {
    }
    
    public static BooleanBigList shuffle(final BooleanBigList l, final Random random) {
        long i = l.size64();
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final boolean t = l.getBoolean(i);
            l.set(i, l.getBoolean(p));
            l.set(p, t);
        }
        return l;
    }
    
    public static BooleanBigList singleton(final boolean element) {
        return new Singleton(element);
    }
    
    public static BooleanBigList singleton(final Object element) {
        return new Singleton((boolean)element);
    }
    
    public static BooleanBigList synchronize(final BooleanBigList l) {
        return new SynchronizedBigList(l);
    }
    
    public static BooleanBigList synchronize(final BooleanBigList l, final Object sync) {
        return new SynchronizedBigList(l, sync);
    }
    
    public static BooleanBigList unmodifiable(final BooleanBigList l) {
        return new UnmodifiableBigList(l);
    }
    
    public static BooleanBigList asBigList(final BooleanList list) {
        return new ListBigList(list);
    }
    
    static {
        EMPTY_BIG_LIST = new EmptyBigList();
    }
    
    public static class EmptyBigList extends BooleanCollections.EmptyCollection implements BooleanBigList, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyBigList() {
        }
        
        @Override
        public void add(final long index, final boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeBoolean(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean set(final long index, final boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long indexOf(final boolean k) {
            return -1L;
        }
        
        @Override
        public long lastIndexOf(final boolean k) {
            return -1L;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Boolean> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final Collection<? extends Boolean> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Boolean get(final long i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public boolean addAll(final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final BooleanBigList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final BooleanBigList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final long index, final Boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final Boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Boolean set(final long index, final Boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean getBoolean(final long i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public Boolean remove(final long k) {
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
        public BooleanBigListIterator listIterator() {
            return BooleanBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        @Override
        public BooleanBigListIterator iterator() {
            return BooleanBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        @Override
        public BooleanBigListIterator listIterator(final long i) {
            if (i == 0L) {
                return BooleanBigListIterators.EMPTY_BIG_LIST_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }
        
        @Override
        public BooleanBigList subList(final long from, final long to) {
            if (from == 0L && to == 0L) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public void getElements(final long from, final boolean[][] a, final long offset, final long length) {
            BooleanBigArrays.ensureOffsetLength(a, offset, length);
            if (from != 0L) {
                throw new IndexOutOfBoundsException();
            }
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final boolean[][] a, final long offset, final long length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final boolean[][] a) {
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
        public int compareTo(final BigList<? extends Boolean> o) {
            if (o == this) {
                return 0;
            }
            return o.isEmpty() ? 0 : -1;
        }
        
        private Object readResolve() {
            return BooleanBigLists.EMPTY_BIG_LIST;
        }
        
        public Object clone() {
            return BooleanBigLists.EMPTY_BIG_LIST;
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
    
    public static class Singleton extends AbstractBooleanBigList implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        private final boolean element;
        
        private Singleton(final boolean element) {
            this.element = element;
        }
        
        @Override
        public boolean getBoolean(final long i) {
            if (i == 0L) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public boolean removeBoolean(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final boolean k) {
            return k == this.element;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Boolean> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final Collection<? extends Boolean> c) {
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
        public boolean[] toBooleanArray() {
            final boolean[] a = { this.element };
            return a;
        }
        
        @Override
        public BooleanBigListIterator listIterator() {
            return BooleanBigListIterators.singleton(this.element);
        }
        
        @Override
        public BooleanBigListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public BooleanBigListIterator listIterator(final long i) {
            if (i > 1L || i < 0L) {
                throw new IndexOutOfBoundsException();
            }
            final BooleanBigListIterator l = this.listIterator();
            if (i == 1L) {
                l.next();
            }
            return l;
        }
        
        @Override
        public BooleanBigList subList(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            if (from != 0L || to != 1L) {
                return BooleanBigLists.EMPTY_BIG_LIST;
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
        public boolean rem(final boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
    }
    
    public static class SynchronizedBigList extends BooleanCollections.SynchronizedCollection implements BooleanBigList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final BooleanBigList list;
        
        protected SynchronizedBigList(final BooleanBigList l, final Object sync) {
            super(l, sync);
            this.list = l;
        }
        
        protected SynchronizedBigList(final BooleanBigList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public boolean getBoolean(final long i) {
            synchronized (this.sync) {
                return this.list.getBoolean(i);
            }
        }
        
        @Override
        public boolean set(final long i, final boolean k) {
            synchronized (this.sync) {
                return this.list.set(i, k);
            }
        }
        
        @Override
        public void add(final long i, final boolean k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public boolean removeBoolean(final long i) {
            synchronized (this.sync) {
                return this.list.removeBoolean(i);
            }
        }
        
        @Override
        public long indexOf(final boolean k) {
            synchronized (this.sync) {
                return this.list.indexOf(k);
            }
        }
        
        @Override
        public long lastIndexOf(final boolean k) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(k);
            }
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Boolean> c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public void getElements(final long from, final boolean[][] a, final long offset, final long length) {
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
        public void addElements(final long index, final boolean[][] a, final long offset, final long length) {
            synchronized (this.sync) {
                this.list.addElements(index, a, offset, length);
            }
        }
        
        @Override
        public void addElements(final long index, final boolean[][] a) {
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
        public BooleanBigListIterator iterator() {
            return this.list.listIterator();
        }
        
        @Override
        public BooleanBigListIterator listIterator() {
            return this.list.listIterator();
        }
        
        @Override
        public BooleanBigListIterator listIterator(final long i) {
            return this.list.listIterator(i);
        }
        
        @Override
        public BooleanBigList subList(final long from, final long to) {
            synchronized (this.sync) {
                return BooleanBigLists.synchronize(this.list.subList(from, to), this.sync);
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
        public int compareTo(final BigList<? extends Boolean> o) {
            synchronized (this.sync) {
                return this.list.compareTo(o);
            }
        }
        
        @Override
        public boolean addAll(final long index, final BooleanCollection c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public boolean addAll(final long index, final BooleanBigList l) {
            synchronized (this.sync) {
                return this.list.addAll(index, l);
            }
        }
        
        @Override
        public boolean addAll(final BooleanBigList l) {
            synchronized (this.sync) {
                return this.list.addAll(l);
            }
        }
        
        @Override
        public Boolean get(final long i) {
            synchronized (this.sync) {
                return this.list.get(i);
            }
        }
        
        @Override
        public void add(final long i, final Boolean k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public Boolean set(final long index, final Boolean k) {
            synchronized (this.sync) {
                return this.list.set(index, k);
            }
        }
        
        @Override
        public Boolean remove(final long i) {
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
    
    public static class UnmodifiableBigList extends BooleanCollections.UnmodifiableCollection implements BooleanBigList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final BooleanBigList list;
        
        protected UnmodifiableBigList(final BooleanBigList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public boolean getBoolean(final long i) {
            return this.list.getBoolean(i);
        }
        
        @Override
        public boolean set(final long i, final boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final long i, final boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeBoolean(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long indexOf(final boolean k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public long lastIndexOf(final boolean k) {
            return this.list.lastIndexOf(k);
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Boolean> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void getElements(final long from, final boolean[][] a, final long offset, final long length) {
            this.list.getElements(from, a, offset, length);
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final boolean[][] a, final long offset, final long length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final boolean[][] a) {
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
        public BooleanBigListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public BooleanBigListIterator listIterator() {
            return BooleanBigListIterators.unmodifiable(this.list.listIterator());
        }
        
        @Override
        public BooleanBigListIterator listIterator(final long i) {
            return BooleanBigListIterators.unmodifiable(this.list.listIterator(i));
        }
        
        @Override
        public BooleanBigList subList(final long from, final long to) {
            return BooleanBigLists.unmodifiable(this.list.subList(from, to));
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
        public int compareTo(final BigList<? extends Boolean> o) {
            return this.list.compareTo(o);
        }
        
        @Override
        public boolean addAll(final long index, final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final BooleanBigList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long index, final BooleanBigList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Boolean get(final long i) {
            return this.list.get(i);
        }
        
        @Override
        public void add(final long i, final Boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Boolean set(final long index, final Boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Boolean remove(final long i) {
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
    
    public static class ListBigList extends AbstractBooleanBigList implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        private final BooleanList list;
        
        protected ListBigList(final BooleanList list) {
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
        public BooleanBigListIterator iterator() {
            return BooleanBigListIterators.asBigListIterator(this.list.iterator());
        }
        
        @Override
        public BooleanBigListIterator listIterator() {
            return BooleanBigListIterators.asBigListIterator(this.list.listIterator());
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Boolean> c) {
            return this.list.addAll(this.intIndex(index), c);
        }
        
        @Override
        public BooleanBigListIterator listIterator(final long index) {
            return BooleanBigListIterators.asBigListIterator(this.list.listIterator(this.intIndex(index)));
        }
        
        @Override
        public BooleanBigList subList(final long from, final long to) {
            return new ListBigList(this.list.subList(this.intIndex(from), this.intIndex(to)));
        }
        
        @Override
        public boolean contains(final boolean key) {
            return this.list.contains(key);
        }
        
        @Override
        public boolean[] toBooleanArray() {
            return this.list.toBooleanArray();
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            this.list.removeElements(this.intIndex(from), this.intIndex(to));
        }
        
        @Override
        public boolean[] toBooleanArray(final boolean[] a) {
            return this.list.toBooleanArray(a);
        }
        
        @Override
        public void add(final long index, final boolean key) {
            this.list.add(this.intIndex(index), key);
        }
        
        @Override
        public boolean addAll(final long index, final BooleanCollection c) {
            return this.list.addAll(this.intIndex(index), c);
        }
        
        @Override
        public boolean addAll(final long index, final BooleanBigList c) {
            return this.list.addAll(this.intIndex(index), c);
        }
        
        @Override
        public boolean add(final boolean key) {
            return this.list.add(key);
        }
        
        @Override
        public boolean addAll(final BooleanBigList c) {
            return this.list.addAll(c);
        }
        
        @Override
        public boolean getBoolean(final long index) {
            return this.list.getBoolean(this.intIndex(index));
        }
        
        @Override
        public long indexOf(final boolean k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public long lastIndexOf(final boolean k) {
            return this.list.lastIndexOf(k);
        }
        
        @Override
        public boolean removeBoolean(final long index) {
            return this.list.removeBoolean(this.intIndex(index));
        }
        
        @Override
        public boolean set(final long index, final boolean k) {
            return this.list.set(this.intIndex(index), k);
        }
        
        @Override
        public boolean addAll(final BooleanCollection c) {
            return this.list.addAll(c);
        }
        
        @Override
        public boolean containsAll(final BooleanCollection c) {
            return this.list.containsAll(c);
        }
        
        @Override
        public boolean removeAll(final BooleanCollection c) {
            return this.list.removeAll(c);
        }
        
        @Override
        public boolean retainAll(final BooleanCollection c) {
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
        public boolean addAll(final Collection<? extends Boolean> c) {
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
