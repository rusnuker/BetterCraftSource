// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.util.Iterator;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.NoSuchElementException;
import java.io.Serializable;

public class ShortSortedSets
{
    public static final EmptySet EMPTY_SET;
    
    private ShortSortedSets() {
    }
    
    public static ShortSortedSet singleton(final short element) {
        return new Singleton(element);
    }
    
    public static ShortSortedSet singleton(final short element, final ShortComparator comparator) {
        return new Singleton(element, comparator);
    }
    
    public static ShortSortedSet singleton(final Object element) {
        return new Singleton((short)element);
    }
    
    public static ShortSortedSet singleton(final Object element, final ShortComparator comparator) {
        return new Singleton((short)element, comparator);
    }
    
    public static ShortSortedSet synchronize(final ShortSortedSet s) {
        return new SynchronizedSortedSet(s);
    }
    
    public static ShortSortedSet synchronize(final ShortSortedSet s, final Object sync) {
        return new SynchronizedSortedSet(s, sync);
    }
    
    public static ShortSortedSet unmodifiable(final ShortSortedSet s) {
        return new UnmodifiableSortedSet(s);
    }
    
    static {
        EMPTY_SET = new EmptySet();
    }
    
    public static class EmptySet extends ShortSets.EmptySet implements ShortSortedSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public boolean rem(final short ok) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public ShortBidirectionalIterator shortIterator() {
            return this.iterator();
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            return ShortIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public ShortSortedSet subSet(final short from, final short to) {
            return ShortSortedSets.EMPTY_SET;
        }
        
        @Override
        public ShortSortedSet headSet(final short from) {
            return ShortSortedSets.EMPTY_SET;
        }
        
        @Override
        public ShortSortedSet tailSet(final short to) {
            return ShortSortedSets.EMPTY_SET;
        }
        
        @Override
        public short firstShort() {
            throw new NoSuchElementException();
        }
        
        @Override
        public short lastShort() {
            throw new NoSuchElementException();
        }
        
        @Override
        public ShortComparator comparator() {
            return null;
        }
        
        @Override
        public ShortSortedSet subSet(final Short from, final Short to) {
            return ShortSortedSets.EMPTY_SET;
        }
        
        @Override
        public ShortSortedSet headSet(final Short from) {
            return ShortSortedSets.EMPTY_SET;
        }
        
        @Override
        public ShortSortedSet tailSet(final Short to) {
            return ShortSortedSets.EMPTY_SET;
        }
        
        @Override
        public Short first() {
            throw new NoSuchElementException();
        }
        
        @Override
        public Short last() {
            throw new NoSuchElementException();
        }
        
        @Override
        public Object clone() {
            return ShortSortedSets.EMPTY_SET;
        }
        
        private Object readResolve() {
            return ShortSortedSets.EMPTY_SET;
        }
    }
    
    public static class Singleton extends ShortSets.Singleton implements ShortSortedSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        final ShortComparator comparator;
        
        private Singleton(final short element, final ShortComparator comparator) {
            super(element);
            this.comparator = comparator;
        }
        
        private Singleton(final short element) {
            this(element, (ShortComparator)null);
        }
        
        final int compare(final short k1, final short k2) {
            return (this.comparator == null) ? Short.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Deprecated
        @Override
        public ShortBidirectionalIterator shortIterator() {
            return this.iterator();
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            final ShortBidirectionalIterator i = this.iterator();
            if (this.compare(this.element, from) <= 0) {
                i.next();
            }
            return i;
        }
        
        @Override
        public ShortComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public ShortSortedSet subSet(final short from, final short to) {
            if (this.compare(from, this.element) <= 0 && this.compare(this.element, to) < 0) {
                return this;
            }
            return ShortSortedSets.EMPTY_SET;
        }
        
        @Override
        public ShortSortedSet headSet(final short to) {
            if (this.compare(this.element, to) < 0) {
                return this;
            }
            return ShortSortedSets.EMPTY_SET;
        }
        
        @Override
        public ShortSortedSet tailSet(final short from) {
            if (this.compare(from, this.element) <= 0) {
                return this;
            }
            return ShortSortedSets.EMPTY_SET;
        }
        
        @Override
        public short firstShort() {
            return this.element;
        }
        
        @Override
        public short lastShort() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public Short first() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public Short last() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public ShortSortedSet subSet(final Short from, final Short to) {
            return this.subSet((short)from, (short)to);
        }
        
        @Deprecated
        @Override
        public ShortSortedSet headSet(final Short to) {
            return this.headSet((short)to);
        }
        
        @Deprecated
        @Override
        public ShortSortedSet tailSet(final Short from) {
            return this.tailSet((short)from);
        }
    }
    
    public static class SynchronizedSortedSet extends ShortSets.SynchronizedSet implements ShortSortedSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ShortSortedSet sortedSet;
        
        protected SynchronizedSortedSet(final ShortSortedSet s, final Object sync) {
            super(s, sync);
            this.sortedSet = s;
        }
        
        protected SynchronizedSortedSet(final ShortSortedSet s) {
            super(s);
            this.sortedSet = s;
        }
        
        @Override
        public ShortComparator comparator() {
            synchronized (this.sync) {
                return this.sortedSet.comparator();
            }
        }
        
        @Override
        public ShortSortedSet subSet(final short from, final short to) {
            return new SynchronizedSortedSet(this.sortedSet.subSet(from, to), this.sync);
        }
        
        @Override
        public ShortSortedSet headSet(final short to) {
            return new SynchronizedSortedSet(this.sortedSet.headSet(to), this.sync);
        }
        
        @Override
        public ShortSortedSet tailSet(final short from) {
            return new SynchronizedSortedSet(this.sortedSet.tailSet(from), this.sync);
        }
        
        @Override
        public ShortBidirectionalIterator iterator() {
            return this.sortedSet.iterator();
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            return this.sortedSet.iterator(from);
        }
        
        @Deprecated
        @Override
        public ShortBidirectionalIterator shortIterator() {
            return this.sortedSet.iterator();
        }
        
        @Override
        public short firstShort() {
            synchronized (this.sync) {
                return this.sortedSet.firstShort();
            }
        }
        
        @Override
        public short lastShort() {
            synchronized (this.sync) {
                return this.sortedSet.lastShort();
            }
        }
        
        @Override
        public Short first() {
            synchronized (this.sync) {
                return this.sortedSet.first();
            }
        }
        
        @Override
        public Short last() {
            synchronized (this.sync) {
                return this.sortedSet.last();
            }
        }
        
        @Override
        public ShortSortedSet subSet(final Short from, final Short to) {
            return new SynchronizedSortedSet(this.sortedSet.subSet(from, to), this.sync);
        }
        
        @Override
        public ShortSortedSet headSet(final Short to) {
            return new SynchronizedSortedSet(this.sortedSet.headSet(to), this.sync);
        }
        
        @Override
        public ShortSortedSet tailSet(final Short from) {
            return new SynchronizedSortedSet(this.sortedSet.tailSet(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedSet extends ShortSets.UnmodifiableSet implements ShortSortedSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ShortSortedSet sortedSet;
        
        protected UnmodifiableSortedSet(final ShortSortedSet s) {
            super(s);
            this.sortedSet = s;
        }
        
        @Override
        public ShortComparator comparator() {
            return this.sortedSet.comparator();
        }
        
        @Override
        public ShortSortedSet subSet(final short from, final short to) {
            return new UnmodifiableSortedSet(this.sortedSet.subSet(from, to));
        }
        
        @Override
        public ShortSortedSet headSet(final short to) {
            return new UnmodifiableSortedSet(this.sortedSet.headSet(to));
        }
        
        @Override
        public ShortSortedSet tailSet(final short from) {
            return new UnmodifiableSortedSet(this.sortedSet.tailSet(from));
        }
        
        @Override
        public ShortBidirectionalIterator iterator() {
            return ShortIterators.unmodifiable(this.sortedSet.iterator());
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            return ShortIterators.unmodifiable(this.sortedSet.iterator(from));
        }
        
        @Deprecated
        @Override
        public ShortBidirectionalIterator shortIterator() {
            return this.iterator();
        }
        
        @Override
        public short firstShort() {
            return this.sortedSet.firstShort();
        }
        
        @Override
        public short lastShort() {
            return this.sortedSet.lastShort();
        }
        
        @Override
        public Short first() {
            return this.sortedSet.first();
        }
        
        @Override
        public Short last() {
            return this.sortedSet.last();
        }
        
        @Override
        public ShortSortedSet subSet(final Short from, final Short to) {
            return new UnmodifiableSortedSet(this.sortedSet.subSet(from, to));
        }
        
        @Override
        public ShortSortedSet headSet(final Short to) {
            return new UnmodifiableSortedSet(this.sortedSet.headSet(to));
        }
        
        @Override
        public ShortSortedSet tailSet(final Short from) {
            return new UnmodifiableSortedSet(this.sortedSet.tailSet(from));
        }
    }
}
