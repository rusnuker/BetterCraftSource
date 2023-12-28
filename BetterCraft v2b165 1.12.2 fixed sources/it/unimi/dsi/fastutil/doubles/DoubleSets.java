// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import java.io.Serializable;

public class DoubleSets
{
    public static final EmptySet EMPTY_SET;
    
    private DoubleSets() {
    }
    
    public static DoubleSet singleton(final double element) {
        return new Singleton(element);
    }
    
    public static DoubleSet singleton(final Double element) {
        return new Singleton(element);
    }
    
    public static DoubleSet synchronize(final DoubleSet s) {
        return new SynchronizedSet(s);
    }
    
    public static DoubleSet synchronize(final DoubleSet s, final Object sync) {
        return new SynchronizedSet(s, sync);
    }
    
    public static DoubleSet unmodifiable(final DoubleSet s) {
        return new UnmodifiableSet(s);
    }
    
    static {
        EMPTY_SET = new EmptySet();
    }
    
    public static class EmptySet extends DoubleCollections.EmptyCollection implements DoubleSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public boolean remove(final double ok) {
            throw new UnsupportedOperationException();
        }
        
        public Object clone() {
            return DoubleSets.EMPTY_SET;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Set && ((Set)o).isEmpty();
        }
        
        private Object readResolve() {
            return DoubleSets.EMPTY_SET;
        }
    }
    
    public static class Singleton extends AbstractDoubleSet implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final double element;
        
        protected Singleton(final double element) {
            this.element = element;
        }
        
        @Override
        public boolean add(final double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final double k) {
            return Double.doubleToLongBits(k) == Double.doubleToLongBits(this.element);
        }
        
        @Override
        public boolean addAll(final Collection<? extends Double> c) {
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
        public double[] toDoubleArray() {
            final double[] a = { this.element };
            return a;
        }
        
        @Override
        public boolean addAll(final DoubleCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final DoubleCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final DoubleCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public DoubleListIterator iterator() {
            return DoubleIterators.singleton(this.element);
        }
        
        @Override
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedSet extends DoubleCollections.SynchronizedCollection implements DoubleSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected SynchronizedSet(final DoubleSet s, final Object sync) {
            super(s, sync);
        }
        
        protected SynchronizedSet(final DoubleSet s) {
            super(s);
        }
        
        @Override
        public boolean remove(final double k) {
            synchronized (this.sync) {
                return this.collection.remove(k);
            }
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
    }
    
    public static class UnmodifiableSet extends DoubleCollections.UnmodifiableCollection implements DoubleSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected UnmodifiableSet(final DoubleSet s) {
            super(s);
        }
        
        @Override
        public boolean remove(final double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean equals(final Object o) {
            return this.collection.equals(o);
        }
        
        @Override
        public int hashCode() {
            return this.collection.hashCode();
        }
    }
}
