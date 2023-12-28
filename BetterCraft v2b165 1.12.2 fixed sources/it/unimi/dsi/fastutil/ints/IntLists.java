// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.util.ListIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.io.Serializable;
import java.util.Random;

public class IntLists
{
    public static final EmptyList EMPTY_LIST;
    
    private IntLists() {
    }
    
    public static IntList shuffle(final IntList l, final Random random) {
        int i = l.size();
        while (i-- != 0) {
            final int p = random.nextInt(i + 1);
            final int t = l.getInt(i);
            l.set(i, l.getInt(p));
            l.set(p, t);
        }
        return l;
    }
    
    public static IntList singleton(final int element) {
        return new Singleton(element);
    }
    
    public static IntList singleton(final Object element) {
        return new Singleton((int)element);
    }
    
    public static IntList synchronize(final IntList l) {
        return new SynchronizedList(l);
    }
    
    public static IntList synchronize(final IntList l, final Object sync) {
        return new SynchronizedList(l, sync);
    }
    
    public static IntList unmodifiable(final IntList l) {
        return new UnmodifiableList(l);
    }
    
    static {
        EMPTY_LIST = new EmptyList();
    }
    
    public static class EmptyList extends IntCollections.EmptyCollection implements IntList, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyList() {
        }
        
        @Override
        public void add(final int index, final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final int k) {
            throw new UnsupportedOperationException();
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
        public int indexOf(final int k) {
            return -1;
        }
        
        @Override
        public int lastIndexOf(final int k) {
            return -1;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Integer get(final int i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public boolean addAll(final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final IntList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final IntList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final int index, final Integer k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final Integer k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Integer set(final int index, final Integer k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int getInt(final int i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public Integer remove(final int k) {
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
        public IntIterator intIterator() {
            return IntIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public IntListIterator listIterator() {
            return IntIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public IntListIterator iterator() {
            return IntIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public IntListIterator listIterator(final int i) {
            if (i == 0) {
                return IntIterators.EMPTY_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }
        
        @Deprecated
        @Override
        public IntListIterator intListIterator() {
            return this.listIterator();
        }
        
        @Deprecated
        @Override
        public IntListIterator intListIterator(final int i) {
            return this.listIterator(i);
        }
        
        @Override
        public IntList subList(final int from, final int to) {
            if (from == 0 && to == 0) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Deprecated
        @Override
        public IntList intSubList(final int from, final int to) {
            return this.subList(from, to);
        }
        
        @Override
        public void getElements(final int from, final int[] a, final int offset, final int length) {
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
        public void addElements(final int index, final int[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final int[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final int s) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int compareTo(final List<? extends Integer> o) {
            if (o == this) {
                return 0;
            }
            return o.isEmpty() ? 0 : -1;
        }
        
        private Object readResolve() {
            return IntLists.EMPTY_LIST;
        }
        
        public Object clone() {
            return IntLists.EMPTY_LIST;
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
    
    public static class Singleton extends AbstractIntList implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        private final int element;
        
        private Singleton(final int element) {
            this.element = element;
        }
        
        @Override
        public int getInt(final int i) {
            if (i == 0) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public int removeInt(final int i) {
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
        public boolean addAll(final int i, final Collection<? extends Integer> c) {
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
        public IntListIterator listIterator() {
            return IntIterators.singleton(this.element);
        }
        
        @Override
        public IntListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public IntListIterator listIterator(final int i) {
            if (i > 1 || i < 0) {
                throw new IndexOutOfBoundsException();
            }
            final IntListIterator l = this.listIterator();
            if (i == 1) {
                l.next();
            }
            return l;
        }
        
        @Override
        public IntList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            if (from != 0 || to != 1) {
                return IntLists.EMPTY_LIST;
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
        public boolean rem(final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final IntCollection c) {
            throw new UnsupportedOperationException();
        }
    }
    
    public static class SynchronizedList extends IntCollections.SynchronizedCollection implements IntList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntList list;
        
        protected SynchronizedList(final IntList l, final Object sync) {
            super(l, sync);
            this.list = l;
        }
        
        protected SynchronizedList(final IntList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public int getInt(final int i) {
            synchronized (this.sync) {
                return this.list.getInt(i);
            }
        }
        
        @Override
        public int set(final int i, final int k) {
            synchronized (this.sync) {
                return this.list.set(i, k);
            }
        }
        
        @Override
        public void add(final int i, final int k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public int removeInt(final int i) {
            synchronized (this.sync) {
                return this.list.removeInt(i);
            }
        }
        
        @Override
        public int indexOf(final int k) {
            synchronized (this.sync) {
                return this.list.indexOf(k);
            }
        }
        
        @Override
        public int lastIndexOf(final int k) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(k);
            }
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Integer> c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public void getElements(final int from, final int[] a, final int offset, final int length) {
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
        public void addElements(final int index, final int[] a, final int offset, final int length) {
            synchronized (this.sync) {
                this.list.addElements(index, a, offset, length);
            }
        }
        
        @Override
        public void addElements(final int index, final int[] a) {
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
        public IntListIterator iterator() {
            return this.list.listIterator();
        }
        
        @Override
        public IntListIterator listIterator() {
            return this.list.listIterator();
        }
        
        @Override
        public IntListIterator listIterator(final int i) {
            return this.list.listIterator(i);
        }
        
        @Deprecated
        @Override
        public IntListIterator intListIterator() {
            return this.listIterator();
        }
        
        @Deprecated
        @Override
        public IntListIterator intListIterator(final int i) {
            return this.listIterator(i);
        }
        
        @Override
        public IntList subList(final int from, final int to) {
            synchronized (this.sync) {
                return IntLists.synchronize(this.list.subList(from, to), this.sync);
            }
        }
        
        @Deprecated
        @Override
        public IntList intSubList(final int from, final int to) {
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
        public int compareTo(final List<? extends Integer> o) {
            synchronized (this.sync) {
                return this.list.compareTo(o);
            }
        }
        
        @Override
        public boolean addAll(final int index, final IntCollection c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public boolean addAll(final int index, final IntList l) {
            synchronized (this.sync) {
                return this.list.addAll(index, l);
            }
        }
        
        @Override
        public boolean addAll(final IntList l) {
            synchronized (this.sync) {
                return this.list.addAll(l);
            }
        }
        
        @Override
        public Integer get(final int i) {
            synchronized (this.sync) {
                return this.list.get(i);
            }
        }
        
        @Override
        public void add(final int i, final Integer k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public Integer set(final int index, final Integer k) {
            synchronized (this.sync) {
                return this.list.set(index, k);
            }
        }
        
        @Override
        public Integer remove(final int i) {
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
    
    public static class UnmodifiableList extends IntCollections.UnmodifiableCollection implements IntList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntList list;
        
        protected UnmodifiableList(final IntList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public int getInt(final int i) {
            return this.list.getInt(i);
        }
        
        @Override
        public int set(final int i, final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final int i, final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int removeInt(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int indexOf(final int k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public int lastIndexOf(final int k) {
            return this.list.lastIndexOf(k);
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void getElements(final int from, final int[] a, final int offset, final int length) {
            this.list.getElements(from, a, offset, length);
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final int[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final int[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final int size) {
            this.list.size(size);
        }
        
        @Override
        public IntListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public IntListIterator listIterator() {
            return IntIterators.unmodifiable(this.list.listIterator());
        }
        
        @Override
        public IntListIterator listIterator(final int i) {
            return IntIterators.unmodifiable(this.list.listIterator(i));
        }
        
        @Deprecated
        @Override
        public IntListIterator intListIterator() {
            return this.listIterator();
        }
        
        @Deprecated
        @Override
        public IntListIterator intListIterator(final int i) {
            return this.listIterator(i);
        }
        
        @Override
        public IntList subList(final int from, final int to) {
            return IntLists.unmodifiable(this.list.subList(from, to));
        }
        
        @Deprecated
        @Override
        public IntList intSubList(final int from, final int to) {
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
        public int compareTo(final List<? extends Integer> o) {
            return this.list.compareTo(o);
        }
        
        @Override
        public boolean addAll(final int index, final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final IntList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int index, final IntList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Integer get(final int i) {
            return this.list.get(i);
        }
        
        @Override
        public void add(final int i, final Integer k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Integer set(final int index, final Integer k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Integer remove(final int i) {
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
