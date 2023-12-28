// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.util.Iterator;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.NoSuchElementException;
import java.io.Serializable;

public class LongSortedSets
{
    public static final EmptySet EMPTY_SET;
    
    private LongSortedSets() {
    }
    
    public static LongSortedSet singleton(final long element) {
        return new Singleton(element);
    }
    
    public static LongSortedSet singleton(final long element, final LongComparator comparator) {
        return new Singleton(element, comparator);
    }
    
    public static LongSortedSet singleton(final Object element) {
        return new Singleton((long)element);
    }
    
    public static LongSortedSet singleton(final Object element, final LongComparator comparator) {
        return new Singleton((long)element, comparator);
    }
    
    public static LongSortedSet synchronize(final LongSortedSet s) {
        return new SynchronizedSortedSet(s);
    }
    
    public static LongSortedSet synchronize(final LongSortedSet s, final Object sync) {
        return new SynchronizedSortedSet(s, sync);
    }
    
    public static LongSortedSet unmodifiable(final LongSortedSet s) {
        return new UnmodifiableSortedSet(s);
    }
    
    static {
        EMPTY_SET = new EmptySet();
    }
    
    public static class EmptySet extends LongSets.EmptySet implements LongSortedSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public boolean rem(final long ok) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public LongBidirectionalIterator longIterator() {
            return this.iterator();
        }
        
        @Override
        public LongBidirectionalIterator iterator(final long from) {
            return LongIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public LongSortedSet subSet(final long from, final long to) {
            return LongSortedSets.EMPTY_SET;
        }
        
        @Override
        public LongSortedSet headSet(final long from) {
            return LongSortedSets.EMPTY_SET;
        }
        
        @Override
        public LongSortedSet tailSet(final long to) {
            return LongSortedSets.EMPTY_SET;
        }
        
        @Override
        public long firstLong() {
            throw new NoSuchElementException();
        }
        
        @Override
        public long lastLong() {
            throw new NoSuchElementException();
        }
        
        @Override
        public LongComparator comparator() {
            return null;
        }
        
        @Override
        public LongSortedSet subSet(final Long from, final Long to) {
            return LongSortedSets.EMPTY_SET;
        }
        
        @Override
        public LongSortedSet headSet(final Long from) {
            return LongSortedSets.EMPTY_SET;
        }
        
        @Override
        public LongSortedSet tailSet(final Long to) {
            return LongSortedSets.EMPTY_SET;
        }
        
        @Override
        public Long first() {
            throw new NoSuchElementException();
        }
        
        @Override
        public Long last() {
            throw new NoSuchElementException();
        }
        
        @Override
        public Object clone() {
            return LongSortedSets.EMPTY_SET;
        }
        
        private Object readResolve() {
            return LongSortedSets.EMPTY_SET;
        }
    }
    
    public static class Singleton extends LongSets.Singleton implements LongSortedSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        final LongComparator comparator;
        
        private Singleton(final long element, final LongComparator comparator) {
            super(element);
            this.comparator = comparator;
        }
        
        private Singleton(final long element) {
            this(element, (LongComparator)null);
        }
        
        final int compare(final long k1, final long k2) {
            return (this.comparator == null) ? Long.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Deprecated
        @Override
        public LongBidirectionalIterator longIterator() {
            return this.iterator();
        }
        
        @Override
        public LongBidirectionalIterator iterator(final long from) {
            final LongBidirectionalIterator i = this.iterator();
            if (this.compare(this.element, from) <= 0) {
                i.next();
            }
            return i;
        }
        
        @Override
        public LongComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public LongSortedSet subSet(final long from, final long to) {
            if (this.compare(from, this.element) <= 0 && this.compare(this.element, to) < 0) {
                return this;
            }
            return LongSortedSets.EMPTY_SET;
        }
        
        @Override
        public LongSortedSet headSet(final long to) {
            if (this.compare(this.element, to) < 0) {
                return this;
            }
            return LongSortedSets.EMPTY_SET;
        }
        
        @Override
        public LongSortedSet tailSet(final long from) {
            if (this.compare(from, this.element) <= 0) {
                return this;
            }
            return LongSortedSets.EMPTY_SET;
        }
        
        @Override
        public long firstLong() {
            return this.element;
        }
        
        @Override
        public long lastLong() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public Long first() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public Long last() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public LongSortedSet subSet(final Long from, final Long to) {
            return this.subSet((long)from, (long)to);
        }
        
        @Deprecated
        @Override
        public LongSortedSet headSet(final Long to) {
            return this.headSet((long)to);
        }
        
        @Deprecated
        @Override
        public LongSortedSet tailSet(final Long from) {
            return this.tailSet((long)from);
        }
    }
    
    public static class SynchronizedSortedSet extends LongSets.SynchronizedSet implements LongSortedSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongSortedSet sortedSet;
        
        protected SynchronizedSortedSet(final LongSortedSet s, final Object sync) {
            super(s, sync);
            this.sortedSet = s;
        }
        
        protected SynchronizedSortedSet(final LongSortedSet s) {
            super(s);
            this.sortedSet = s;
        }
        
        @Override
        public LongComparator comparator() {
            synchronized (this.sync) {
                return this.sortedSet.comparator();
            }
        }
        
        @Override
        public LongSortedSet subSet(final long from, final long to) {
            return new SynchronizedSortedSet(this.sortedSet.subSet(from, to), this.sync);
        }
        
        @Override
        public LongSortedSet headSet(final long to) {
            return new SynchronizedSortedSet(this.sortedSet.headSet(to), this.sync);
        }
        
        @Override
        public LongSortedSet tailSet(final long from) {
            return new SynchronizedSortedSet(this.sortedSet.tailSet(from), this.sync);
        }
        
        @Override
        public LongBidirectionalIterator iterator() {
            return this.sortedSet.iterator();
        }
        
        @Override
        public LongBidirectionalIterator iterator(final long from) {
            return this.sortedSet.iterator(from);
        }
        
        @Deprecated
        @Override
        public LongBidirectionalIterator longIterator() {
            return this.sortedSet.iterator();
        }
        
        @Override
        public long firstLong() {
            synchronized (this.sync) {
                return this.sortedSet.firstLong();
            }
        }
        
        @Override
        public long lastLong() {
            synchronized (this.sync) {
                return this.sortedSet.lastLong();
            }
        }
        
        @Override
        public Long first() {
            synchronized (this.sync) {
                return this.sortedSet.first();
            }
        }
        
        @Override
        public Long last() {
            synchronized (this.sync) {
                return this.sortedSet.last();
            }
        }
        
        @Override
        public LongSortedSet subSet(final Long from, final Long to) {
            return new SynchronizedSortedSet(this.sortedSet.subSet(from, to), this.sync);
        }
        
        @Override
        public LongSortedSet headSet(final Long to) {
            return new SynchronizedSortedSet(this.sortedSet.headSet(to), this.sync);
        }
        
        @Override
        public LongSortedSet tailSet(final Long from) {
            return new SynchronizedSortedSet(this.sortedSet.tailSet(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedSet extends LongSets.UnmodifiableSet implements LongSortedSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongSortedSet sortedSet;
        
        protected UnmodifiableSortedSet(final LongSortedSet s) {
            super(s);
            this.sortedSet = s;
        }
        
        @Override
        public LongComparator comparator() {
            return this.sortedSet.comparator();
        }
        
        @Override
        public LongSortedSet subSet(final long from, final long to) {
            return new UnmodifiableSortedSet(this.sortedSet.subSet(from, to));
        }
        
        @Override
        public LongSortedSet headSet(final long to) {
            return new UnmodifiableSortedSet(this.sortedSet.headSet(to));
        }
        
        @Override
        public LongSortedSet tailSet(final long from) {
            return new UnmodifiableSortedSet(this.sortedSet.tailSet(from));
        }
        
        @Override
        public LongBidirectionalIterator iterator() {
            return LongIterators.unmodifiable(this.sortedSet.iterator());
        }
        
        @Override
        public LongBidirectionalIterator iterator(final long from) {
            return LongIterators.unmodifiable(this.sortedSet.iterator(from));
        }
        
        @Deprecated
        @Override
        public LongBidirectionalIterator longIterator() {
            return this.iterator();
        }
        
        @Override
        public long firstLong() {
            return this.sortedSet.firstLong();
        }
        
        @Override
        public long lastLong() {
            return this.sortedSet.lastLong();
        }
        
        @Override
        public Long first() {
            return this.sortedSet.first();
        }
        
        @Override
        public Long last() {
            return this.sortedSet.last();
        }
        
        @Override
        public LongSortedSet subSet(final Long from, final Long to) {
            return new UnmodifiableSortedSet(this.sortedSet.subSet(from, to));
        }
        
        @Override
        public LongSortedSet headSet(final Long to) {
            return new UnmodifiableSortedSet(this.sortedSet.headSet(to));
        }
        
        @Override
        public LongSortedSet tailSet(final Long from) {
            return new UnmodifiableSortedSet(this.sortedSet.tailSet(from));
        }
    }
}
