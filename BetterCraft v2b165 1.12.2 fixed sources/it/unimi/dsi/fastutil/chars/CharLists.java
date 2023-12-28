// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.util.ListIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.io.Serializable;
import java.util.Random;

public class CharLists
{
    public static final EmptyList EMPTY_LIST;
    
    private CharLists() {
    }
    
    public static CharList shuffle(final CharList l, final Random random) {
        int i = l.size();
        while (i-- != 0) {
            final int p = random.nextInt(i + 1);
            final char t = l.getChar(i);
            l.set(i, l.getChar(p));
            l.set(p, t);
        }
        return l;
    }
    
    public static CharList singleton(final char element) {
        return new Singleton(element);
    }
    
    public static CharList singleton(final Object element) {
        return new Singleton((char)element);
    }
    
    public static CharList synchronize(final CharList l) {
        return new SynchronizedList(l);
    }
    
    public static CharList synchronize(final CharList l, final Object sync) {
        return new SynchronizedList(l, sync);
    }
    
    public static CharList unmodifiable(final CharList l) {
        return new UnmodifiableList(l);
    }
    
    static {
        EMPTY_LIST = new EmptyList();
    }
    
    public static class EmptyList extends CharCollections.EmptyCollection implements CharList, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyList() {
        }
        
        @Override
        public void add(final int index, final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char removeChar(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char set(final int index, final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int indexOf(final char k) {
            return -1;
        }
        
        @Override
        public int lastIndexOf(final char k) {
            return -1;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Character> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final Collection<? extends Character> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Character get(final int i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public boolean addAll(final CharCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final CharList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final CharCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final CharList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final int index, final Character k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final Character k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Character set(final int index, final Character k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char getChar(final int i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public Character remove(final int k) {
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
        public CharIterator charIterator() {
            return CharIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public CharListIterator listIterator() {
            return CharIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public CharListIterator iterator() {
            return CharIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public CharListIterator listIterator(final int i) {
            if (i == 0) {
                return CharIterators.EMPTY_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }
        
        @Deprecated
        @Override
        public CharListIterator charListIterator() {
            return this.listIterator();
        }
        
        @Deprecated
        @Override
        public CharListIterator charListIterator(final int i) {
            return this.listIterator(i);
        }
        
        @Override
        public CharList subList(final int from, final int to) {
            if (from == 0 && to == 0) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Deprecated
        @Override
        public CharList charSubList(final int from, final int to) {
            return this.subList(from, to);
        }
        
        @Override
        public void getElements(final int from, final char[] a, final int offset, final int length) {
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
        public void addElements(final int index, final char[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final char[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final int s) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int compareTo(final List<? extends Character> o) {
            if (o == this) {
                return 0;
            }
            return o.isEmpty() ? 0 : -1;
        }
        
        private Object readResolve() {
            return CharLists.EMPTY_LIST;
        }
        
        public Object clone() {
            return CharLists.EMPTY_LIST;
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
    
    public static class Singleton extends AbstractCharList implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        private final char element;
        
        private Singleton(final char element) {
            this.element = element;
        }
        
        @Override
        public char getChar(final int i) {
            if (i == 0) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public char removeChar(final int i) {
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
        public boolean addAll(final int i, final Collection<? extends Character> c) {
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
        public CharListIterator listIterator() {
            return CharIterators.singleton(this.element);
        }
        
        @Override
        public CharListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public CharListIterator listIterator(final int i) {
            if (i > 1 || i < 0) {
                throw new IndexOutOfBoundsException();
            }
            final CharListIterator l = this.listIterator();
            if (i == 1) {
                l.next();
            }
            return l;
        }
        
        @Override
        public CharList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            if (from != 0 || to != 1) {
                return CharLists.EMPTY_LIST;
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
        public boolean rem(final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final CharCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final CharCollection c) {
            throw new UnsupportedOperationException();
        }
    }
    
    public static class SynchronizedList extends CharCollections.SynchronizedCollection implements CharList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final CharList list;
        
        protected SynchronizedList(final CharList l, final Object sync) {
            super(l, sync);
            this.list = l;
        }
        
        protected SynchronizedList(final CharList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public char getChar(final int i) {
            synchronized (this.sync) {
                return this.list.getChar(i);
            }
        }
        
        @Override
        public char set(final int i, final char k) {
            synchronized (this.sync) {
                return this.list.set(i, k);
            }
        }
        
        @Override
        public void add(final int i, final char k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public char removeChar(final int i) {
            synchronized (this.sync) {
                return this.list.removeChar(i);
            }
        }
        
        @Override
        public int indexOf(final char k) {
            synchronized (this.sync) {
                return this.list.indexOf(k);
            }
        }
        
        @Override
        public int lastIndexOf(final char k) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(k);
            }
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Character> c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public void getElements(final int from, final char[] a, final int offset, final int length) {
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
        public void addElements(final int index, final char[] a, final int offset, final int length) {
            synchronized (this.sync) {
                this.list.addElements(index, a, offset, length);
            }
        }
        
        @Override
        public void addElements(final int index, final char[] a) {
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
        public CharListIterator iterator() {
            return this.list.listIterator();
        }
        
        @Override
        public CharListIterator listIterator() {
            return this.list.listIterator();
        }
        
        @Override
        public CharListIterator listIterator(final int i) {
            return this.list.listIterator(i);
        }
        
        @Deprecated
        @Override
        public CharListIterator charListIterator() {
            return this.listIterator();
        }
        
        @Deprecated
        @Override
        public CharListIterator charListIterator(final int i) {
            return this.listIterator(i);
        }
        
        @Override
        public CharList subList(final int from, final int to) {
            synchronized (this.sync) {
                return CharLists.synchronize(this.list.subList(from, to), this.sync);
            }
        }
        
        @Deprecated
        @Override
        public CharList charSubList(final int from, final int to) {
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
        public int compareTo(final List<? extends Character> o) {
            synchronized (this.sync) {
                return this.list.compareTo(o);
            }
        }
        
        @Override
        public boolean addAll(final int index, final CharCollection c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public boolean addAll(final int index, final CharList l) {
            synchronized (this.sync) {
                return this.list.addAll(index, l);
            }
        }
        
        @Override
        public boolean addAll(final CharList l) {
            synchronized (this.sync) {
                return this.list.addAll(l);
            }
        }
        
        @Override
        public Character get(final int i) {
            synchronized (this.sync) {
                return this.list.get(i);
            }
        }
        
        @Override
        public void add(final int i, final Character k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public Character set(final int index, final Character k) {
            synchronized (this.sync) {
                return this.list.set(index, k);
            }
        }
        
        @Override
        public Character remove(final int i) {
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
    
    public static class UnmodifiableList extends CharCollections.UnmodifiableCollection implements CharList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final CharList list;
        
        protected UnmodifiableList(final CharList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public char getChar(final int i) {
            return this.list.getChar(i);
        }
        
        @Override
        public char set(final int i, final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final int i, final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char removeChar(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int indexOf(final char k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public int lastIndexOf(final char k) {
            return this.list.lastIndexOf(k);
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Character> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void getElements(final int from, final char[] a, final int offset, final int length) {
            this.list.getElements(from, a, offset, length);
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final char[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final char[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final int size) {
            this.list.size(size);
        }
        
        @Override
        public CharListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public CharListIterator listIterator() {
            return CharIterators.unmodifiable(this.list.listIterator());
        }
        
        @Override
        public CharListIterator listIterator(final int i) {
            return CharIterators.unmodifiable(this.list.listIterator(i));
        }
        
        @Deprecated
        @Override
        public CharListIterator charListIterator() {
            return this.listIterator();
        }
        
        @Deprecated
        @Override
        public CharListIterator charListIterator(final int i) {
            return this.listIterator(i);
        }
        
        @Override
        public CharList subList(final int from, final int to) {
            return CharLists.unmodifiable(this.list.subList(from, to));
        }
        
        @Deprecated
        @Override
        public CharList charSubList(final int from, final int to) {
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
        public int compareTo(final List<? extends Character> o) {
            return this.list.compareTo(o);
        }
        
        @Override
        public boolean addAll(final int index, final CharCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final CharList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int index, final CharList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Character get(final int i) {
            return this.list.get(i);
        }
        
        @Override
        public void add(final int i, final Character k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Character set(final int index, final Character k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Character remove(final int i) {
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
