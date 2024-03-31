// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.util.ListIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.io.Serializable;
import java.util.Random;

public class LongLists
{
    public static final EmptyList EMPTY_LIST;
    
    private LongLists() {
    }
    
    public static LongList shuffle(final LongList l, final Random random) {
        int i = l.size();
        while (i-- != 0) {
            final int p = random.nextInt(i + 1);
            final long t = l.getLong(i);
            l.set(i, l.getLong(p));
            l.set(p, t);
        }
        return l;
    }
    
    public static LongList singleton(final long element) {
        return new Singleton(element);
    }
    
    public static LongList singleton(final Object element) {
        return new Singleton((long)element);
    }
    
    public static LongList synchronize(final LongList l) {
        return new SynchronizedList(l);
    }
    
    public static LongList synchronize(final LongList l, final Object sync) {
        return new SynchronizedList(l, sync);
    }
    
    public static LongList unmodifiable(final LongList l) {
        return new UnmodifiableList(l);
    }
    
    static {
        EMPTY_LIST = new EmptyList();
    }
    
    public static class EmptyList extends LongCollections.EmptyCollection implements LongList, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyList() {
        }
        
        @Override
        public void add(final int index, final long k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final long k) {
            throw new UnsupportedOperationException();
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
        public int indexOf(final long k) {
            return -1;
        }
        
        @Override
        public int lastIndexOf(final long k) {
            return -1;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Long> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final Collection<? extends Long> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Long get(final int i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public boolean addAll(final LongCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final LongList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final LongCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final LongList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final int index, final Long k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final Long k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Long set(final int index, final Long k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long getLong(final int i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public Long remove(final int k) {
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
        public LongIterator longIterator() {
            return LongIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public LongListIterator listIterator() {
            return LongIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public LongListIterator iterator() {
            return LongIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public LongListIterator listIterator(final int i) {
            if (i == 0) {
                return LongIterators.EMPTY_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }
        
        @Deprecated
        @Override
        public LongListIterator longListIterator() {
            return this.listIterator();
        }
        
        @Deprecated
        @Override
        public LongListIterator longListIterator(final int i) {
            return this.listIterator(i);
        }
        
        @Override
        public LongList subList(final int from, final int to) {
            if (from == 0 && to == 0) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Deprecated
        @Override
        public LongList longSubList(final int from, final int to) {
            return this.subList(from, to);
        }
        
        @Override
        public void getElements(final int from, final long[] a, final int offset, final int length) {
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
        public void addElements(final int index, final long[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final long[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final int s) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int compareTo(final List<? extends Long> o) {
            if (o == this) {
                return 0;
            }
            return o.isEmpty() ? 0 : -1;
        }
        
        private Object readResolve() {
            return LongLists.EMPTY_LIST;
        }
        
        public Object clone() {
            return LongLists.EMPTY_LIST;
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
    
    public static class Singleton extends AbstractLongList implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        private final long element;
        
        private Singleton(final long element) {
            this.element = element;
        }
        
        @Override
        public long getLong(final int i) {
            if (i == 0) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public long removeLong(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final long k) {
            return k == this.element;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Long> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final Collection<? extends Long> c) {
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
        public long[] toLongArray() {
            final long[] a = { this.element };
            return a;
        }
        
        @Override
        public LongListIterator listIterator() {
            return LongIterators.singleton(this.element);
        }
        
        @Override
        public LongListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public LongListIterator listIterator(final int i) {
            if (i > 1 || i < 0) {
                throw new IndexOutOfBoundsException();
            }
            final LongListIterator l = this.listIterator();
            if (i == 1) {
                l.next();
            }
            return l;
        }
        
        @Override
        public LongList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            if (from != 0 || to != 1) {
                return LongLists.EMPTY_LIST;
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
        public boolean rem(final long k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final LongCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final LongCollection c) {
            throw new UnsupportedOperationException();
        }
    }
    
    public static class SynchronizedList extends LongCollections.SynchronizedCollection implements LongList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongList list;
        
        protected SynchronizedList(final LongList l, final Object sync) {
            super(l, sync);
            this.list = l;
        }
        
        protected SynchronizedList(final LongList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public long getLong(final int i) {
            synchronized (this.sync) {
                return this.list.getLong(i);
            }
        }
        
        @Override
        public long set(final int i, final long k) {
            synchronized (this.sync) {
                return this.list.set(i, k);
            }
        }
        
        @Override
        public void add(final int i, final long k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public long removeLong(final int i) {
            synchronized (this.sync) {
                return this.list.removeLong(i);
            }
        }
        
        @Override
        public int indexOf(final long k) {
            synchronized (this.sync) {
                return this.list.indexOf(k);
            }
        }
        
        @Override
        public int lastIndexOf(final long k) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(k);
            }
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Long> c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public void getElements(final int from, final long[] a, final int offset, final int length) {
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
        public void addElements(final int index, final long[] a, final int offset, final int length) {
            synchronized (this.sync) {
                this.list.addElements(index, a, offset, length);
            }
        }
        
        @Override
        public void addElements(final int index, final long[] a) {
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
        public LongListIterator iterator() {
            return this.list.listIterator();
        }
        
        @Override
        public LongListIterator listIterator() {
            return this.list.listIterator();
        }
        
        @Override
        public LongListIterator listIterator(final int i) {
            return this.list.listIterator(i);
        }
        
        @Deprecated
        @Override
        public LongListIterator longListIterator() {
            return this.listIterator();
        }
        
        @Deprecated
        @Override
        public LongListIterator longListIterator(final int i) {
            return this.listIterator(i);
        }
        
        @Override
        public LongList subList(final int from, final int to) {
            synchronized (this.sync) {
                return LongLists.synchronize(this.list.subList(from, to), this.sync);
            }
        }
        
        @Deprecated
        @Override
        public LongList longSubList(final int from, final int to) {
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
        public int compareTo(final List<? extends Long> o) {
            synchronized (this.sync) {
                return this.list.compareTo(o);
            }
        }
        
        @Override
        public boolean addAll(final int index, final LongCollection c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public boolean addAll(final int index, final LongList l) {
            synchronized (this.sync) {
                return this.list.addAll(index, l);
            }
        }
        
        @Override
        public boolean addAll(final LongList l) {
            synchronized (this.sync) {
                return this.list.addAll(l);
            }
        }
        
        @Override
        public Long get(final int i) {
            synchronized (this.sync) {
                return this.list.get(i);
            }
        }
        
        @Override
        public void add(final int i, final Long k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public Long set(final int index, final Long k) {
            synchronized (this.sync) {
                return this.list.set(index, k);
            }
        }
        
        @Override
        public Long remove(final int i) {
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
    
    public static class UnmodifiableList extends LongCollections.UnmodifiableCollection implements LongList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongList list;
        
        protected UnmodifiableList(final LongList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public long getLong(final int i) {
            return this.list.getLong(i);
        }
        
        @Override
        public long set(final int i, final long k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final int i, final long k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long removeLong(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int indexOf(final long k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public int lastIndexOf(final long k) {
            return this.list.lastIndexOf(k);
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Long> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void getElements(final int from, final long[] a, final int offset, final int length) {
            this.list.getElements(from, a, offset, length);
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final long[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final long[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final int size) {
            this.list.size(size);
        }
        
        @Override
        public LongListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public LongListIterator listIterator() {
            return LongIterators.unmodifiable(this.list.listIterator());
        }
        
        @Override
        public LongListIterator listIterator(final int i) {
            return LongIterators.unmodifiable(this.list.listIterator(i));
        }
        
        @Deprecated
        @Override
        public LongListIterator longListIterator() {
            return this.listIterator();
        }
        
        @Deprecated
        @Override
        public LongListIterator longListIterator(final int i) {
            return this.listIterator(i);
        }
        
        @Override
        public LongList subList(final int from, final int to) {
            return LongLists.unmodifiable(this.list.subList(from, to));
        }
        
        @Deprecated
        @Override
        public LongList longSubList(final int from, final int to) {
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
        public int compareTo(final List<? extends Long> o) {
            return this.list.compareTo(o);
        }
        
        @Override
        public boolean addAll(final int index, final LongCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final LongList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int index, final LongList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Long get(final int i) {
            return this.list.get(i);
        }
        
        @Override
        public void add(final int i, final Long k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Long set(final int index, final Long k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Long remove(final int i) {
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
