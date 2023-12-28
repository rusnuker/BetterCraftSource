// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.util.Iterator;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.NoSuchElementException;
import java.io.Serializable;

public class FloatSortedSets
{
    public static final EmptySet EMPTY_SET;
    
    private FloatSortedSets() {
    }
    
    public static FloatSortedSet singleton(final float element) {
        return new Singleton(element);
    }
    
    public static FloatSortedSet singleton(final float element, final FloatComparator comparator) {
        return new Singleton(element, comparator);
    }
    
    public static FloatSortedSet singleton(final Object element) {
        return new Singleton((float)element);
    }
    
    public static FloatSortedSet singleton(final Object element, final FloatComparator comparator) {
        return new Singleton((float)element, comparator);
    }
    
    public static FloatSortedSet synchronize(final FloatSortedSet s) {
        return new SynchronizedSortedSet(s);
    }
    
    public static FloatSortedSet synchronize(final FloatSortedSet s, final Object sync) {
        return new SynchronizedSortedSet(s, sync);
    }
    
    public static FloatSortedSet unmodifiable(final FloatSortedSet s) {
        return new UnmodifiableSortedSet(s);
    }
    
    static {
        EMPTY_SET = new EmptySet();
    }
    
    public static class EmptySet extends FloatSets.EmptySet implements FloatSortedSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public boolean rem(final float ok) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public FloatBidirectionalIterator floatIterator() {
            return this.iterator();
        }
        
        @Override
        public FloatBidirectionalIterator iterator(final float from) {
            return FloatIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public FloatSortedSet subSet(final float from, final float to) {
            return FloatSortedSets.EMPTY_SET;
        }
        
        @Override
        public FloatSortedSet headSet(final float from) {
            return FloatSortedSets.EMPTY_SET;
        }
        
        @Override
        public FloatSortedSet tailSet(final float to) {
            return FloatSortedSets.EMPTY_SET;
        }
        
        @Override
        public float firstFloat() {
            throw new NoSuchElementException();
        }
        
        @Override
        public float lastFloat() {
            throw new NoSuchElementException();
        }
        
        @Override
        public FloatComparator comparator() {
            return null;
        }
        
        @Override
        public FloatSortedSet subSet(final Float from, final Float to) {
            return FloatSortedSets.EMPTY_SET;
        }
        
        @Override
        public FloatSortedSet headSet(final Float from) {
            return FloatSortedSets.EMPTY_SET;
        }
        
        @Override
        public FloatSortedSet tailSet(final Float to) {
            return FloatSortedSets.EMPTY_SET;
        }
        
        @Override
        public Float first() {
            throw new NoSuchElementException();
        }
        
        @Override
        public Float last() {
            throw new NoSuchElementException();
        }
        
        @Override
        public Object clone() {
            return FloatSortedSets.EMPTY_SET;
        }
        
        private Object readResolve() {
            return FloatSortedSets.EMPTY_SET;
        }
    }
    
    public static class Singleton extends FloatSets.Singleton implements FloatSortedSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        final FloatComparator comparator;
        
        private Singleton(final float element, final FloatComparator comparator) {
            super(element);
            this.comparator = comparator;
        }
        
        private Singleton(final float element) {
            this(element, (FloatComparator)null);
        }
        
        final int compare(final float k1, final float k2) {
            return (this.comparator == null) ? Float.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Deprecated
        @Override
        public FloatBidirectionalIterator floatIterator() {
            return this.iterator();
        }
        
        @Override
        public FloatBidirectionalIterator iterator(final float from) {
            final FloatBidirectionalIterator i = this.iterator();
            if (this.compare(this.element, from) <= 0) {
                i.next();
            }
            return i;
        }
        
        @Override
        public FloatComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public FloatSortedSet subSet(final float from, final float to) {
            if (this.compare(from, this.element) <= 0 && this.compare(this.element, to) < 0) {
                return this;
            }
            return FloatSortedSets.EMPTY_SET;
        }
        
        @Override
        public FloatSortedSet headSet(final float to) {
            if (this.compare(this.element, to) < 0) {
                return this;
            }
            return FloatSortedSets.EMPTY_SET;
        }
        
        @Override
        public FloatSortedSet tailSet(final float from) {
            if (this.compare(from, this.element) <= 0) {
                return this;
            }
            return FloatSortedSets.EMPTY_SET;
        }
        
        @Override
        public float firstFloat() {
            return this.element;
        }
        
        @Override
        public float lastFloat() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public Float first() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public Float last() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public FloatSortedSet subSet(final Float from, final Float to) {
            return this.subSet((float)from, (float)to);
        }
        
        @Deprecated
        @Override
        public FloatSortedSet headSet(final Float to) {
            return this.headSet((float)to);
        }
        
        @Deprecated
        @Override
        public FloatSortedSet tailSet(final Float from) {
            return this.tailSet((float)from);
        }
    }
    
    public static class SynchronizedSortedSet extends FloatSets.SynchronizedSet implements FloatSortedSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final FloatSortedSet sortedSet;
        
        protected SynchronizedSortedSet(final FloatSortedSet s, final Object sync) {
            super(s, sync);
            this.sortedSet = s;
        }
        
        protected SynchronizedSortedSet(final FloatSortedSet s) {
            super(s);
            this.sortedSet = s;
        }
        
        @Override
        public FloatComparator comparator() {
            synchronized (this.sync) {
                return this.sortedSet.comparator();
            }
        }
        
        @Override
        public FloatSortedSet subSet(final float from, final float to) {
            return new SynchronizedSortedSet(this.sortedSet.subSet(from, to), this.sync);
        }
        
        @Override
        public FloatSortedSet headSet(final float to) {
            return new SynchronizedSortedSet(this.sortedSet.headSet(to), this.sync);
        }
        
        @Override
        public FloatSortedSet tailSet(final float from) {
            return new SynchronizedSortedSet(this.sortedSet.tailSet(from), this.sync);
        }
        
        @Override
        public FloatBidirectionalIterator iterator() {
            return this.sortedSet.iterator();
        }
        
        @Override
        public FloatBidirectionalIterator iterator(final float from) {
            return this.sortedSet.iterator(from);
        }
        
        @Deprecated
        @Override
        public FloatBidirectionalIterator floatIterator() {
            return this.sortedSet.iterator();
        }
        
        @Override
        public float firstFloat() {
            synchronized (this.sync) {
                return this.sortedSet.firstFloat();
            }
        }
        
        @Override
        public float lastFloat() {
            synchronized (this.sync) {
                return this.sortedSet.lastFloat();
            }
        }
        
        @Override
        public Float first() {
            synchronized (this.sync) {
                return this.sortedSet.first();
            }
        }
        
        @Override
        public Float last() {
            synchronized (this.sync) {
                return this.sortedSet.last();
            }
        }
        
        @Override
        public FloatSortedSet subSet(final Float from, final Float to) {
            return new SynchronizedSortedSet(this.sortedSet.subSet(from, to), this.sync);
        }
        
        @Override
        public FloatSortedSet headSet(final Float to) {
            return new SynchronizedSortedSet(this.sortedSet.headSet(to), this.sync);
        }
        
        @Override
        public FloatSortedSet tailSet(final Float from) {
            return new SynchronizedSortedSet(this.sortedSet.tailSet(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedSet extends FloatSets.UnmodifiableSet implements FloatSortedSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final FloatSortedSet sortedSet;
        
        protected UnmodifiableSortedSet(final FloatSortedSet s) {
            super(s);
            this.sortedSet = s;
        }
        
        @Override
        public FloatComparator comparator() {
            return this.sortedSet.comparator();
        }
        
        @Override
        public FloatSortedSet subSet(final float from, final float to) {
            return new UnmodifiableSortedSet(this.sortedSet.subSet(from, to));
        }
        
        @Override
        public FloatSortedSet headSet(final float to) {
            return new UnmodifiableSortedSet(this.sortedSet.headSet(to));
        }
        
        @Override
        public FloatSortedSet tailSet(final float from) {
            return new UnmodifiableSortedSet(this.sortedSet.tailSet(from));
        }
        
        @Override
        public FloatBidirectionalIterator iterator() {
            return FloatIterators.unmodifiable(this.sortedSet.iterator());
        }
        
        @Override
        public FloatBidirectionalIterator iterator(final float from) {
            return FloatIterators.unmodifiable(this.sortedSet.iterator(from));
        }
        
        @Deprecated
        @Override
        public FloatBidirectionalIterator floatIterator() {
            return this.iterator();
        }
        
        @Override
        public float firstFloat() {
            return this.sortedSet.firstFloat();
        }
        
        @Override
        public float lastFloat() {
            return this.sortedSet.lastFloat();
        }
        
        @Override
        public Float first() {
            return this.sortedSet.first();
        }
        
        @Override
        public Float last() {
            return this.sortedSet.last();
        }
        
        @Override
        public FloatSortedSet subSet(final Float from, final Float to) {
            return new UnmodifiableSortedSet(this.sortedSet.subSet(from, to));
        }
        
        @Override
        public FloatSortedSet headSet(final Float to) {
            return new UnmodifiableSortedSet(this.sortedSet.headSet(to));
        }
        
        @Override
        public FloatSortedSet tailSet(final Float from) {
            return new UnmodifiableSortedSet(this.sortedSet.tailSet(from));
        }
    }
}
