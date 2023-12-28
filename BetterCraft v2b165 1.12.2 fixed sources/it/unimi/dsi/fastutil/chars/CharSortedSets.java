// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.util.Iterator;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.NoSuchElementException;
import java.io.Serializable;

public class CharSortedSets
{
    public static final EmptySet EMPTY_SET;
    
    private CharSortedSets() {
    }
    
    public static CharSortedSet singleton(final char element) {
        return new Singleton(element);
    }
    
    public static CharSortedSet singleton(final char element, final CharComparator comparator) {
        return new Singleton(element, comparator);
    }
    
    public static CharSortedSet singleton(final Object element) {
        return new Singleton((char)element);
    }
    
    public static CharSortedSet singleton(final Object element, final CharComparator comparator) {
        return new Singleton((char)element, comparator);
    }
    
    public static CharSortedSet synchronize(final CharSortedSet s) {
        return new SynchronizedSortedSet(s);
    }
    
    public static CharSortedSet synchronize(final CharSortedSet s, final Object sync) {
        return new SynchronizedSortedSet(s, sync);
    }
    
    public static CharSortedSet unmodifiable(final CharSortedSet s) {
        return new UnmodifiableSortedSet(s);
    }
    
    static {
        EMPTY_SET = new EmptySet();
    }
    
    public static class EmptySet extends CharSets.EmptySet implements CharSortedSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public boolean rem(final char ok) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public CharBidirectionalIterator charIterator() {
            return this.iterator();
        }
        
        @Override
        public CharBidirectionalIterator iterator(final char from) {
            return CharIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public CharSortedSet subSet(final char from, final char to) {
            return CharSortedSets.EMPTY_SET;
        }
        
        @Override
        public CharSortedSet headSet(final char from) {
            return CharSortedSets.EMPTY_SET;
        }
        
        @Override
        public CharSortedSet tailSet(final char to) {
            return CharSortedSets.EMPTY_SET;
        }
        
        @Override
        public char firstChar() {
            throw new NoSuchElementException();
        }
        
        @Override
        public char lastChar() {
            throw new NoSuchElementException();
        }
        
        @Override
        public CharComparator comparator() {
            return null;
        }
        
        @Override
        public CharSortedSet subSet(final Character from, final Character to) {
            return CharSortedSets.EMPTY_SET;
        }
        
        @Override
        public CharSortedSet headSet(final Character from) {
            return CharSortedSets.EMPTY_SET;
        }
        
        @Override
        public CharSortedSet tailSet(final Character to) {
            return CharSortedSets.EMPTY_SET;
        }
        
        @Override
        public Character first() {
            throw new NoSuchElementException();
        }
        
        @Override
        public Character last() {
            throw new NoSuchElementException();
        }
        
        @Override
        public Object clone() {
            return CharSortedSets.EMPTY_SET;
        }
        
        private Object readResolve() {
            return CharSortedSets.EMPTY_SET;
        }
    }
    
    public static class Singleton extends CharSets.Singleton implements CharSortedSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        final CharComparator comparator;
        
        private Singleton(final char element, final CharComparator comparator) {
            super(element);
            this.comparator = comparator;
        }
        
        private Singleton(final char element) {
            this(element, (CharComparator)null);
        }
        
        final int compare(final char k1, final char k2) {
            return (this.comparator == null) ? Character.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Deprecated
        @Override
        public CharBidirectionalIterator charIterator() {
            return this.iterator();
        }
        
        @Override
        public CharBidirectionalIterator iterator(final char from) {
            final CharBidirectionalIterator i = this.iterator();
            if (this.compare(this.element, from) <= 0) {
                i.next();
            }
            return i;
        }
        
        @Override
        public CharComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public CharSortedSet subSet(final char from, final char to) {
            if (this.compare(from, this.element) <= 0 && this.compare(this.element, to) < 0) {
                return this;
            }
            return CharSortedSets.EMPTY_SET;
        }
        
        @Override
        public CharSortedSet headSet(final char to) {
            if (this.compare(this.element, to) < 0) {
                return this;
            }
            return CharSortedSets.EMPTY_SET;
        }
        
        @Override
        public CharSortedSet tailSet(final char from) {
            if (this.compare(from, this.element) <= 0) {
                return this;
            }
            return CharSortedSets.EMPTY_SET;
        }
        
        @Override
        public char firstChar() {
            return this.element;
        }
        
        @Override
        public char lastChar() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public Character first() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public Character last() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public CharSortedSet subSet(final Character from, final Character to) {
            return this.subSet((char)from, (char)to);
        }
        
        @Deprecated
        @Override
        public CharSortedSet headSet(final Character to) {
            return this.headSet((char)to);
        }
        
        @Deprecated
        @Override
        public CharSortedSet tailSet(final Character from) {
            return this.tailSet((char)from);
        }
    }
    
    public static class SynchronizedSortedSet extends CharSets.SynchronizedSet implements CharSortedSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final CharSortedSet sortedSet;
        
        protected SynchronizedSortedSet(final CharSortedSet s, final Object sync) {
            super(s, sync);
            this.sortedSet = s;
        }
        
        protected SynchronizedSortedSet(final CharSortedSet s) {
            super(s);
            this.sortedSet = s;
        }
        
        @Override
        public CharComparator comparator() {
            synchronized (this.sync) {
                return this.sortedSet.comparator();
            }
        }
        
        @Override
        public CharSortedSet subSet(final char from, final char to) {
            return new SynchronizedSortedSet(this.sortedSet.subSet(from, to), this.sync);
        }
        
        @Override
        public CharSortedSet headSet(final char to) {
            return new SynchronizedSortedSet(this.sortedSet.headSet(to), this.sync);
        }
        
        @Override
        public CharSortedSet tailSet(final char from) {
            return new SynchronizedSortedSet(this.sortedSet.tailSet(from), this.sync);
        }
        
        @Override
        public CharBidirectionalIterator iterator() {
            return this.sortedSet.iterator();
        }
        
        @Override
        public CharBidirectionalIterator iterator(final char from) {
            return this.sortedSet.iterator(from);
        }
        
        @Deprecated
        @Override
        public CharBidirectionalIterator charIterator() {
            return this.sortedSet.iterator();
        }
        
        @Override
        public char firstChar() {
            synchronized (this.sync) {
                return this.sortedSet.firstChar();
            }
        }
        
        @Override
        public char lastChar() {
            synchronized (this.sync) {
                return this.sortedSet.lastChar();
            }
        }
        
        @Override
        public Character first() {
            synchronized (this.sync) {
                return this.sortedSet.first();
            }
        }
        
        @Override
        public Character last() {
            synchronized (this.sync) {
                return this.sortedSet.last();
            }
        }
        
        @Override
        public CharSortedSet subSet(final Character from, final Character to) {
            return new SynchronizedSortedSet(this.sortedSet.subSet(from, to), this.sync);
        }
        
        @Override
        public CharSortedSet headSet(final Character to) {
            return new SynchronizedSortedSet(this.sortedSet.headSet(to), this.sync);
        }
        
        @Override
        public CharSortedSet tailSet(final Character from) {
            return new SynchronizedSortedSet(this.sortedSet.tailSet(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedSet extends CharSets.UnmodifiableSet implements CharSortedSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final CharSortedSet sortedSet;
        
        protected UnmodifiableSortedSet(final CharSortedSet s) {
            super(s);
            this.sortedSet = s;
        }
        
        @Override
        public CharComparator comparator() {
            return this.sortedSet.comparator();
        }
        
        @Override
        public CharSortedSet subSet(final char from, final char to) {
            return new UnmodifiableSortedSet(this.sortedSet.subSet(from, to));
        }
        
        @Override
        public CharSortedSet headSet(final char to) {
            return new UnmodifiableSortedSet(this.sortedSet.headSet(to));
        }
        
        @Override
        public CharSortedSet tailSet(final char from) {
            return new UnmodifiableSortedSet(this.sortedSet.tailSet(from));
        }
        
        @Override
        public CharBidirectionalIterator iterator() {
            return CharIterators.unmodifiable(this.sortedSet.iterator());
        }
        
        @Override
        public CharBidirectionalIterator iterator(final char from) {
            return CharIterators.unmodifiable(this.sortedSet.iterator(from));
        }
        
        @Deprecated
        @Override
        public CharBidirectionalIterator charIterator() {
            return this.iterator();
        }
        
        @Override
        public char firstChar() {
            return this.sortedSet.firstChar();
        }
        
        @Override
        public char lastChar() {
            return this.sortedSet.lastChar();
        }
        
        @Override
        public Character first() {
            return this.sortedSet.first();
        }
        
        @Override
        public Character last() {
            return this.sortedSet.last();
        }
        
        @Override
        public CharSortedSet subSet(final Character from, final Character to) {
            return new UnmodifiableSortedSet(this.sortedSet.subSet(from, to));
        }
        
        @Override
        public CharSortedSet headSet(final Character to) {
            return new UnmodifiableSortedSet(this.sortedSet.headSet(to));
        }
        
        @Override
        public CharSortedSet tailSet(final Character from) {
            return new UnmodifiableSortedSet(this.sortedSet.tailSet(from));
        }
    }
}
