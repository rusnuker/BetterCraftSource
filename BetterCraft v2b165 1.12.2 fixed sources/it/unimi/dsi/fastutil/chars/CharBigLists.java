// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.BigListIterator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.BigList;
import java.util.Collection;
import java.io.Serializable;
import java.util.Random;

public class CharBigLists
{
    public static final EmptyBigList EMPTY_BIG_LIST;
    
    private CharBigLists() {
    }
    
    public static CharBigList shuffle(final CharBigList l, final Random random) {
        long i = l.size64();
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final char t = l.getChar(i);
            l.set(i, l.getChar(p));
            l.set(p, t);
        }
        return l;
    }
    
    public static CharBigList singleton(final char element) {
        return new Singleton(element);
    }
    
    public static CharBigList singleton(final Object element) {
        return new Singleton((char)element);
    }
    
    public static CharBigList synchronize(final CharBigList l) {
        return new SynchronizedBigList(l);
    }
    
    public static CharBigList synchronize(final CharBigList l, final Object sync) {
        return new SynchronizedBigList(l, sync);
    }
    
    public static CharBigList unmodifiable(final CharBigList l) {
        return new UnmodifiableBigList(l);
    }
    
    public static CharBigList asBigList(final CharList list) {
        return new ListBigList(list);
    }
    
    static {
        EMPTY_BIG_LIST = new EmptyBigList();
    }
    
    public static class EmptyBigList extends CharCollections.EmptyCollection implements CharBigList, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyBigList() {
        }
        
        @Override
        public void add(final long index, final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char removeChar(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char set(final long index, final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long indexOf(final char k) {
            return -1L;
        }
        
        @Override
        public long lastIndexOf(final char k) {
            return -1L;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Character> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final Collection<? extends Character> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Character get(final long i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public boolean addAll(final CharCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final CharBigList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final CharCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final CharBigList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final long index, final Character k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final Character k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Character set(final long index, final Character k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char getChar(final long i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public Character remove(final long k) {
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
        public CharBigListIterator listIterator() {
            return CharBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        @Override
        public CharBigListIterator iterator() {
            return CharBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        @Override
        public CharBigListIterator listIterator(final long i) {
            if (i == 0L) {
                return CharBigListIterators.EMPTY_BIG_LIST_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }
        
        @Override
        public CharBigList subList(final long from, final long to) {
            if (from == 0L && to == 0L) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public void getElements(final long from, final char[][] a, final long offset, final long length) {
            CharBigArrays.ensureOffsetLength(a, offset, length);
            if (from != 0L) {
                throw new IndexOutOfBoundsException();
            }
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final char[][] a, final long offset, final long length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final char[][] a) {
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
        public int compareTo(final BigList<? extends Character> o) {
            if (o == this) {
                return 0;
            }
            return o.isEmpty() ? 0 : -1;
        }
        
        private Object readResolve() {
            return CharBigLists.EMPTY_BIG_LIST;
        }
        
        public Object clone() {
            return CharBigLists.EMPTY_BIG_LIST;
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
    
    public static class Singleton extends AbstractCharBigList implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        private final char element;
        
        private Singleton(final char element) {
            this.element = element;
        }
        
        @Override
        public char getChar(final long i) {
            if (i == 0L) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public char removeChar(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final char k) {
            return k == this.element;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Character> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final Collection<? extends Character> c) {
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
        public char[] toCharArray() {
            final char[] a = { this.element };
            return a;
        }
        
        @Override
        public CharBigListIterator listIterator() {
            return CharBigListIterators.singleton(this.element);
        }
        
        @Override
        public CharBigListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public CharBigListIterator listIterator(final long i) {
            if (i > 1L || i < 0L) {
                throw new IndexOutOfBoundsException();
            }
            final CharBigListIterator l = this.listIterator();
            if (i == 1L) {
                l.next();
            }
            return l;
        }
        
        @Override
        public CharBigList subList(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            if (from != 0L || to != 1L) {
                return CharBigLists.EMPTY_BIG_LIST;
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
        public boolean rem(final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final CharCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final CharCollection c) {
            throw new UnsupportedOperationException();
        }
    }
    
    public static class SynchronizedBigList extends CharCollections.SynchronizedCollection implements CharBigList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final CharBigList list;
        
        protected SynchronizedBigList(final CharBigList l, final Object sync) {
            super(l, sync);
            this.list = l;
        }
        
        protected SynchronizedBigList(final CharBigList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public char getChar(final long i) {
            synchronized (this.sync) {
                return this.list.getChar(i);
            }
        }
        
        @Override
        public char set(final long i, final char k) {
            synchronized (this.sync) {
                return this.list.set(i, k);
            }
        }
        
        @Override
        public void add(final long i, final char k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public char removeChar(final long i) {
            synchronized (this.sync) {
                return this.list.removeChar(i);
            }
        }
        
        @Override
        public long indexOf(final char k) {
            synchronized (this.sync) {
                return this.list.indexOf(k);
            }
        }
        
        @Override
        public long lastIndexOf(final char k) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(k);
            }
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Character> c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public void getElements(final long from, final char[][] a, final long offset, final long length) {
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
        public void addElements(final long index, final char[][] a, final long offset, final long length) {
            synchronized (this.sync) {
                this.list.addElements(index, a, offset, length);
            }
        }
        
        @Override
        public void addElements(final long index, final char[][] a) {
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
        public CharBigListIterator iterator() {
            return this.list.listIterator();
        }
        
        @Override
        public CharBigListIterator listIterator() {
            return this.list.listIterator();
        }
        
        @Override
        public CharBigListIterator listIterator(final long i) {
            return this.list.listIterator(i);
        }
        
        @Override
        public CharBigList subList(final long from, final long to) {
            synchronized (this.sync) {
                return CharBigLists.synchronize(this.list.subList(from, to), this.sync);
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
        public int compareTo(final BigList<? extends Character> o) {
            synchronized (this.sync) {
                return this.list.compareTo(o);
            }
        }
        
        @Override
        public boolean addAll(final long index, final CharCollection c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public boolean addAll(final long index, final CharBigList l) {
            synchronized (this.sync) {
                return this.list.addAll(index, l);
            }
        }
        
        @Override
        public boolean addAll(final CharBigList l) {
            synchronized (this.sync) {
                return this.list.addAll(l);
            }
        }
        
        @Override
        public Character get(final long i) {
            synchronized (this.sync) {
                return this.list.get(i);
            }
        }
        
        @Override
        public void add(final long i, final Character k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public Character set(final long index, final Character k) {
            synchronized (this.sync) {
                return this.list.set(index, k);
            }
        }
        
        @Override
        public Character remove(final long i) {
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
    
    public static class UnmodifiableBigList extends CharCollections.UnmodifiableCollection implements CharBigList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final CharBigList list;
        
        protected UnmodifiableBigList(final CharBigList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public char getChar(final long i) {
            return this.list.getChar(i);
        }
        
        @Override
        public char set(final long i, final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final long i, final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char removeChar(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long indexOf(final char k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public long lastIndexOf(final char k) {
            return this.list.lastIndexOf(k);
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Character> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void getElements(final long from, final char[][] a, final long offset, final long length) {
            this.list.getElements(from, a, offset, length);
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final char[][] a, final long offset, final long length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final char[][] a) {
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
        public CharBigListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public CharBigListIterator listIterator() {
            return CharBigListIterators.unmodifiable(this.list.listIterator());
        }
        
        @Override
        public CharBigListIterator listIterator(final long i) {
            return CharBigListIterators.unmodifiable(this.list.listIterator(i));
        }
        
        @Override
        public CharBigList subList(final long from, final long to) {
            return CharBigLists.unmodifiable(this.list.subList(from, to));
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
        public int compareTo(final BigList<? extends Character> o) {
            return this.list.compareTo(o);
        }
        
        @Override
        public boolean addAll(final long index, final CharCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final CharBigList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long index, final CharBigList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Character get(final long i) {
            return this.list.get(i);
        }
        
        @Override
        public void add(final long i, final Character k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Character set(final long index, final Character k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Character remove(final long i) {
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
    
    public static class ListBigList extends AbstractCharBigList implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        private final CharList list;
        
        protected ListBigList(final CharList list) {
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
        public CharBigListIterator iterator() {
            return CharBigListIterators.asBigListIterator(this.list.iterator());
        }
        
        @Override
        public CharBigListIterator listIterator() {
            return CharBigListIterators.asBigListIterator(this.list.listIterator());
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Character> c) {
            return this.list.addAll(this.intIndex(index), c);
        }
        
        @Override
        public CharBigListIterator listIterator(final long index) {
            return CharBigListIterators.asBigListIterator(this.list.listIterator(this.intIndex(index)));
        }
        
        @Override
        public CharBigList subList(final long from, final long to) {
            return new ListBigList(this.list.subList(this.intIndex(from), this.intIndex(to)));
        }
        
        @Override
        public boolean contains(final char key) {
            return this.list.contains(key);
        }
        
        @Override
        public char[] toCharArray() {
            return this.list.toCharArray();
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            this.list.removeElements(this.intIndex(from), this.intIndex(to));
        }
        
        @Override
        public char[] toCharArray(final char[] a) {
            return this.list.toCharArray(a);
        }
        
        @Override
        public void add(final long index, final char key) {
            this.list.add(this.intIndex(index), key);
        }
        
        @Override
        public boolean addAll(final long index, final CharCollection c) {
            return this.list.addAll(this.intIndex(index), c);
        }
        
        @Override
        public boolean addAll(final long index, final CharBigList c) {
            return this.list.addAll(this.intIndex(index), c);
        }
        
        @Override
        public boolean add(final char key) {
            return this.list.add(key);
        }
        
        @Override
        public boolean addAll(final CharBigList c) {
            return this.list.addAll(c);
        }
        
        @Override
        public char getChar(final long index) {
            return this.list.getChar(this.intIndex(index));
        }
        
        @Override
        public long indexOf(final char k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public long lastIndexOf(final char k) {
            return this.list.lastIndexOf(k);
        }
        
        @Override
        public char removeChar(final long index) {
            return this.list.removeChar(this.intIndex(index));
        }
        
        @Override
        public char set(final long index, final char k) {
            return this.list.set(this.intIndex(index), k);
        }
        
        @Override
        public boolean addAll(final CharCollection c) {
            return this.list.addAll(c);
        }
        
        @Override
        public boolean containsAll(final CharCollection c) {
            return this.list.containsAll(c);
        }
        
        @Override
        public boolean removeAll(final CharCollection c) {
            return this.list.removeAll(c);
        }
        
        @Override
        public boolean retainAll(final CharCollection c) {
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
        public boolean addAll(final Collection<? extends Character> c) {
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
