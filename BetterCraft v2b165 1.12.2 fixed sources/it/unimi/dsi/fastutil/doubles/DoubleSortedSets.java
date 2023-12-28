// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import java.util.Iterator;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.NoSuchElementException;
import java.io.Serializable;

public class DoubleSortedSets
{
    public static final EmptySet EMPTY_SET;
    
    private DoubleSortedSets() {
    }
    
    public static DoubleSortedSet singleton(final double element) {
        return new Singleton(element);
    }
    
    public static DoubleSortedSet singleton(final double element, final DoubleComparator comparator) {
        return new Singleton(element, comparator);
    }
    
    public static DoubleSortedSet singleton(final Object element) {
        return new Singleton((double)element);
    }
    
    public static DoubleSortedSet singleton(final Object element, final DoubleComparator comparator) {
        return new Singleton((double)element, comparator);
    }
    
    public static DoubleSortedSet synchronize(final DoubleSortedSet s) {
        return new SynchronizedSortedSet(s);
    }
    
    public static DoubleSortedSet synchronize(final DoubleSortedSet s, final Object sync) {
        return new SynchronizedSortedSet(s, sync);
    }
    
    public static DoubleSortedSet unmodifiable(final DoubleSortedSet s) {
        return new UnmodifiableSortedSet(s);
    }
    
    static {
        EMPTY_SET = new EmptySet();
    }
    
    public static class EmptySet extends DoubleSets.EmptySet implements DoubleSortedSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public boolean rem(final double ok) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public DoubleBidirectionalIterator doubleIterator() {
            return this.iterator();
        }
        
        @Override
        public DoubleBidirectionalIterator iterator(final double from) {
            return DoubleIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            return DoubleSortedSets.EMPTY_SET;
        }
        
        @Override
        public DoubleSortedSet headSet(final double from) {
            return DoubleSortedSets.EMPTY_SET;
        }
        
        @Override
        public DoubleSortedSet tailSet(final double to) {
            return DoubleSortedSets.EMPTY_SET;
        }
        
        @Override
        public double firstDouble() {
            throw new NoSuchElementException();
        }
        
        @Override
        public double lastDouble() {
            throw new NoSuchElementException();
        }
        
        @Override
        public DoubleComparator comparator() {
            return null;
        }
        
        @Override
        public DoubleSortedSet subSet(final Double from, final Double to) {
            return DoubleSortedSets.EMPTY_SET;
        }
        
        @Override
        public DoubleSortedSet headSet(final Double from) {
            return DoubleSortedSets.EMPTY_SET;
        }
        
        @Override
        public DoubleSortedSet tailSet(final Double to) {
            return DoubleSortedSets.EMPTY_SET;
        }
        
        @Override
        public Double first() {
            throw new NoSuchElementException();
        }
        
        @Override
        public Double last() {
            throw new NoSuchElementException();
        }
        
        @Override
        public Object clone() {
            return DoubleSortedSets.EMPTY_SET;
        }
        
        private Object readResolve() {
            return DoubleSortedSets.EMPTY_SET;
        }
    }
    
    public static class Singleton extends DoubleSets.Singleton implements DoubleSortedSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        final DoubleComparator comparator;
        
        private Singleton(final double element, final DoubleComparator comparator) {
            super(element);
            this.comparator = comparator;
        }
        
        private Singleton(final double element) {
            this(element, (DoubleComparator)null);
        }
        
        final int compare(final double k1, final double k2) {
            return (this.comparator == null) ? Double.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Deprecated
        @Override
        public DoubleBidirectionalIterator doubleIterator() {
            return this.iterator();
        }
        
        @Override
        public DoubleBidirectionalIterator iterator(final double from) {
            final DoubleBidirectionalIterator i = this.iterator();
            if (this.compare(this.element, from) <= 0) {
                i.next();
            }
            return i;
        }
        
        @Override
        public DoubleComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            if (this.compare(from, this.element) <= 0 && this.compare(this.element, to) < 0) {
                return this;
            }
            return DoubleSortedSets.EMPTY_SET;
        }
        
        @Override
        public DoubleSortedSet headSet(final double to) {
            if (this.compare(this.element, to) < 0) {
                return this;
            }
            return DoubleSortedSets.EMPTY_SET;
        }
        
        @Override
        public DoubleSortedSet tailSet(final double from) {
            if (this.compare(from, this.element) <= 0) {
                return this;
            }
            return DoubleSortedSets.EMPTY_SET;
        }
        
        @Override
        public double firstDouble() {
            return this.element;
        }
        
        @Override
        public double lastDouble() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public Double first() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public Double last() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public DoubleSortedSet subSet(final Double from, final Double to) {
            return this.subSet((double)from, (double)to);
        }
        
        @Deprecated
        @Override
        public DoubleSortedSet headSet(final Double to) {
            return this.headSet((double)to);
        }
        
        @Deprecated
        @Override
        public DoubleSortedSet tailSet(final Double from) {
            return this.tailSet((double)from);
        }
    }
    
    public static class SynchronizedSortedSet extends DoubleSets.SynchronizedSet implements DoubleSortedSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final DoubleSortedSet sortedSet;
        
        protected SynchronizedSortedSet(final DoubleSortedSet s, final Object sync) {
            super(s, sync);
            this.sortedSet = s;
        }
        
        protected SynchronizedSortedSet(final DoubleSortedSet s) {
            super(s);
            this.sortedSet = s;
        }
        
        @Override
        public DoubleComparator comparator() {
            synchronized (this.sync) {
                return this.sortedSet.comparator();
            }
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            return new SynchronizedSortedSet(this.sortedSet.subSet(from, to), this.sync);
        }
        
        @Override
        public DoubleSortedSet headSet(final double to) {
            return new SynchronizedSortedSet(this.sortedSet.headSet(to), this.sync);
        }
        
        @Override
        public DoubleSortedSet tailSet(final double from) {
            return new SynchronizedSortedSet(this.sortedSet.tailSet(from), this.sync);
        }
        
        @Override
        public DoubleBidirectionalIterator iterator() {
            return this.sortedSet.iterator();
        }
        
        @Override
        public DoubleBidirectionalIterator iterator(final double from) {
            return this.sortedSet.iterator(from);
        }
        
        @Deprecated
        @Override
        public DoubleBidirectionalIterator doubleIterator() {
            return this.sortedSet.iterator();
        }
        
        @Override
        public double firstDouble() {
            synchronized (this.sync) {
                return this.sortedSet.firstDouble();
            }
        }
        
        @Override
        public double lastDouble() {
            synchronized (this.sync) {
                return this.sortedSet.lastDouble();
            }
        }
        
        @Override
        public Double first() {
            synchronized (this.sync) {
                return this.sortedSet.first();
            }
        }
        
        @Override
        public Double last() {
            synchronized (this.sync) {
                return this.sortedSet.last();
            }
        }
        
        @Override
        public DoubleSortedSet subSet(final Double from, final Double to) {
            return new SynchronizedSortedSet(this.sortedSet.subSet(from, to), this.sync);
        }
        
        @Override
        public DoubleSortedSet headSet(final Double to) {
            return new SynchronizedSortedSet(this.sortedSet.headSet(to), this.sync);
        }
        
        @Override
        public DoubleSortedSet tailSet(final Double from) {
            return new SynchronizedSortedSet(this.sortedSet.tailSet(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedSet extends DoubleSets.UnmodifiableSet implements DoubleSortedSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final DoubleSortedSet sortedSet;
        
        protected UnmodifiableSortedSet(final DoubleSortedSet s) {
            super(s);
            this.sortedSet = s;
        }
        
        @Override
        public DoubleComparator comparator() {
            return this.sortedSet.comparator();
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            return new UnmodifiableSortedSet(this.sortedSet.subSet(from, to));
        }
        
        @Override
        public DoubleSortedSet headSet(final double to) {
            return new UnmodifiableSortedSet(this.sortedSet.headSet(to));
        }
        
        @Override
        public DoubleSortedSet tailSet(final double from) {
            return new UnmodifiableSortedSet(this.sortedSet.tailSet(from));
        }
        
        @Override
        public DoubleBidirectionalIterator iterator() {
            return DoubleIterators.unmodifiable(this.sortedSet.iterator());
        }
        
        @Override
        public DoubleBidirectionalIterator iterator(final double from) {
            return DoubleIterators.unmodifiable(this.sortedSet.iterator(from));
        }
        
        @Deprecated
        @Override
        public DoubleBidirectionalIterator doubleIterator() {
            return this.iterator();
        }
        
        @Override
        public double firstDouble() {
            return this.sortedSet.firstDouble();
        }
        
        @Override
        public double lastDouble() {
            return this.sortedSet.lastDouble();
        }
        
        @Override
        public Double first() {
            return this.sortedSet.first();
        }
        
        @Override
        public Double last() {
            return this.sortedSet.last();
        }
        
        @Override
        public DoubleSortedSet subSet(final Double from, final Double to) {
            return new UnmodifiableSortedSet(this.sortedSet.subSet(from, to));
        }
        
        @Override
        public DoubleSortedSet headSet(final Double to) {
            return new UnmodifiableSortedSet(this.sortedSet.headSet(to));
        }
        
        @Override
        public DoubleSortedSet tailSet(final Double from) {
            return new UnmodifiableSortedSet(this.sortedSet.tailSet(from));
        }
    }
}
