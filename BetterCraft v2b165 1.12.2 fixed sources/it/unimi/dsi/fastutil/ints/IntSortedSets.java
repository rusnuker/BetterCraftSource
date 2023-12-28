// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.util.Iterator;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.NoSuchElementException;
import java.io.Serializable;

public class IntSortedSets
{
    public static final EmptySet EMPTY_SET;
    
    private IntSortedSets() {
    }
    
    public static IntSortedSet singleton(final int element) {
        return new Singleton(element);
    }
    
    public static IntSortedSet singleton(final int element, final IntComparator comparator) {
        return new Singleton(element, comparator);
    }
    
    public static IntSortedSet singleton(final Object element) {
        return new Singleton((int)element);
    }
    
    public static IntSortedSet singleton(final Object element, final IntComparator comparator) {
        return new Singleton((int)element, comparator);
    }
    
    public static IntSortedSet synchronize(final IntSortedSet s) {
        return new SynchronizedSortedSet(s);
    }
    
    public static IntSortedSet synchronize(final IntSortedSet s, final Object sync) {
        return new SynchronizedSortedSet(s, sync);
    }
    
    public static IntSortedSet unmodifiable(final IntSortedSet s) {
        return new UnmodifiableSortedSet(s);
    }
    
    static {
        EMPTY_SET = new EmptySet();
    }
    
    public static class EmptySet extends IntSets.EmptySet implements IntSortedSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public boolean rem(final int ok) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public IntBidirectionalIterator intIterator() {
            return this.iterator();
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            return IntIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            return IntSortedSets.EMPTY_SET;
        }
        
        @Override
        public IntSortedSet headSet(final int from) {
            return IntSortedSets.EMPTY_SET;
        }
        
        @Override
        public IntSortedSet tailSet(final int to) {
            return IntSortedSets.EMPTY_SET;
        }
        
        @Override
        public int firstInt() {
            throw new NoSuchElementException();
        }
        
        @Override
        public int lastInt() {
            throw new NoSuchElementException();
        }
        
        @Override
        public IntComparator comparator() {
            return null;
        }
        
        @Override
        public IntSortedSet subSet(final Integer from, final Integer to) {
            return IntSortedSets.EMPTY_SET;
        }
        
        @Override
        public IntSortedSet headSet(final Integer from) {
            return IntSortedSets.EMPTY_SET;
        }
        
        @Override
        public IntSortedSet tailSet(final Integer to) {
            return IntSortedSets.EMPTY_SET;
        }
        
        @Override
        public Integer first() {
            throw new NoSuchElementException();
        }
        
        @Override
        public Integer last() {
            throw new NoSuchElementException();
        }
        
        @Override
        public Object clone() {
            return IntSortedSets.EMPTY_SET;
        }
        
        private Object readResolve() {
            return IntSortedSets.EMPTY_SET;
        }
    }
    
    public static class Singleton extends IntSets.Singleton implements IntSortedSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        final IntComparator comparator;
        
        private Singleton(final int element, final IntComparator comparator) {
            super(element);
            this.comparator = comparator;
        }
        
        private Singleton(final int element) {
            this(element, (IntComparator)null);
        }
        
        final int compare(final int k1, final int k2) {
            return (this.comparator == null) ? Integer.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Deprecated
        @Override
        public IntBidirectionalIterator intIterator() {
            return this.iterator();
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            final IntBidirectionalIterator i = this.iterator();
            if (this.compare(this.element, from) <= 0) {
                i.next();
            }
            return i;
        }
        
        @Override
        public IntComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            if (this.compare(from, this.element) <= 0 && this.compare(this.element, to) < 0) {
                return this;
            }
            return IntSortedSets.EMPTY_SET;
        }
        
        @Override
        public IntSortedSet headSet(final int to) {
            if (this.compare(this.element, to) < 0) {
                return this;
            }
            return IntSortedSets.EMPTY_SET;
        }
        
        @Override
        public IntSortedSet tailSet(final int from) {
            if (this.compare(from, this.element) <= 0) {
                return this;
            }
            return IntSortedSets.EMPTY_SET;
        }
        
        @Override
        public int firstInt() {
            return this.element;
        }
        
        @Override
        public int lastInt() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public Integer first() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public Integer last() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public IntSortedSet subSet(final Integer from, final Integer to) {
            return this.subSet((int)from, (int)to);
        }
        
        @Deprecated
        @Override
        public IntSortedSet headSet(final Integer to) {
            return this.headSet((int)to);
        }
        
        @Deprecated
        @Override
        public IntSortedSet tailSet(final Integer from) {
            return this.tailSet((int)from);
        }
    }
    
    public static class SynchronizedSortedSet extends IntSets.SynchronizedSet implements IntSortedSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntSortedSet sortedSet;
        
        protected SynchronizedSortedSet(final IntSortedSet s, final Object sync) {
            super(s, sync);
            this.sortedSet = s;
        }
        
        protected SynchronizedSortedSet(final IntSortedSet s) {
            super(s);
            this.sortedSet = s;
        }
        
        @Override
        public IntComparator comparator() {
            synchronized (this.sync) {
                return this.sortedSet.comparator();
            }
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            return new SynchronizedSortedSet(this.sortedSet.subSet(from, to), this.sync);
        }
        
        @Override
        public IntSortedSet headSet(final int to) {
            return new SynchronizedSortedSet(this.sortedSet.headSet(to), this.sync);
        }
        
        @Override
        public IntSortedSet tailSet(final int from) {
            return new SynchronizedSortedSet(this.sortedSet.tailSet(from), this.sync);
        }
        
        @Override
        public IntBidirectionalIterator iterator() {
            return this.sortedSet.iterator();
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            return this.sortedSet.iterator(from);
        }
        
        @Deprecated
        @Override
        public IntBidirectionalIterator intIterator() {
            return this.sortedSet.iterator();
        }
        
        @Override
        public int firstInt() {
            synchronized (this.sync) {
                return this.sortedSet.firstInt();
            }
        }
        
        @Override
        public int lastInt() {
            synchronized (this.sync) {
                return this.sortedSet.lastInt();
            }
        }
        
        @Override
        public Integer first() {
            synchronized (this.sync) {
                return this.sortedSet.first();
            }
        }
        
        @Override
        public Integer last() {
            synchronized (this.sync) {
                return this.sortedSet.last();
            }
        }
        
        @Override
        public IntSortedSet subSet(final Integer from, final Integer to) {
            return new SynchronizedSortedSet(this.sortedSet.subSet(from, to), this.sync);
        }
        
        @Override
        public IntSortedSet headSet(final Integer to) {
            return new SynchronizedSortedSet(this.sortedSet.headSet(to), this.sync);
        }
        
        @Override
        public IntSortedSet tailSet(final Integer from) {
            return new SynchronizedSortedSet(this.sortedSet.tailSet(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedSet extends IntSets.UnmodifiableSet implements IntSortedSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntSortedSet sortedSet;
        
        protected UnmodifiableSortedSet(final IntSortedSet s) {
            super(s);
            this.sortedSet = s;
        }
        
        @Override
        public IntComparator comparator() {
            return this.sortedSet.comparator();
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            return new UnmodifiableSortedSet(this.sortedSet.subSet(from, to));
        }
        
        @Override
        public IntSortedSet headSet(final int to) {
            return new UnmodifiableSortedSet(this.sortedSet.headSet(to));
        }
        
        @Override
        public IntSortedSet tailSet(final int from) {
            return new UnmodifiableSortedSet(this.sortedSet.tailSet(from));
        }
        
        @Override
        public IntBidirectionalIterator iterator() {
            return IntIterators.unmodifiable(this.sortedSet.iterator());
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            return IntIterators.unmodifiable(this.sortedSet.iterator(from));
        }
        
        @Deprecated
        @Override
        public IntBidirectionalIterator intIterator() {
            return this.iterator();
        }
        
        @Override
        public int firstInt() {
            return this.sortedSet.firstInt();
        }
        
        @Override
        public int lastInt() {
            return this.sortedSet.lastInt();
        }
        
        @Override
        public Integer first() {
            return this.sortedSet.first();
        }
        
        @Override
        public Integer last() {
            return this.sortedSet.last();
        }
        
        @Override
        public IntSortedSet subSet(final Integer from, final Integer to) {
            return new UnmodifiableSortedSet(this.sortedSet.subSet(from, to));
        }
        
        @Override
        public IntSortedSet headSet(final Integer to) {
            return new UnmodifiableSortedSet(this.sortedSet.headSet(to));
        }
        
        @Override
        public IntSortedSet tailSet(final Integer from) {
            return new UnmodifiableSortedSet(this.sortedSet.tailSet(from));
        }
    }
}
