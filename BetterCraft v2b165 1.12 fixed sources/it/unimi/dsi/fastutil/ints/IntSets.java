// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import java.io.Serializable;

public class IntSets
{
    public static final EmptySet EMPTY_SET;
    
    private IntSets() {
    }
    
    public static IntSet singleton(final int element) {
        return new Singleton(element);
    }
    
    public static IntSet singleton(final Integer element) {
        return new Singleton(element);
    }
    
    public static IntSet synchronize(final IntSet s) {
        return new SynchronizedSet(s);
    }
    
    public static IntSet synchronize(final IntSet s, final Object sync) {
        return new SynchronizedSet(s, sync);
    }
    
    public static IntSet unmodifiable(final IntSet s) {
        return new UnmodifiableSet(s);
    }
    
    static {
        EMPTY_SET = new EmptySet();
    }
    
    public static class EmptySet extends IntCollections.EmptyCollection implements IntSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public boolean remove(final int ok) {
            throw new UnsupportedOperationException();
        }
        
        public Object clone() {
            return IntSets.EMPTY_SET;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Set && ((Set)o).isEmpty();
        }
        
        private Object readResolve() {
            return IntSets.EMPTY_SET;
        }
    }
    
    public static class Singleton extends AbstractIntSet implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final int element;
        
        protected Singleton(final int element) {
            this.element = element;
        }
        
        @Override
        public boolean add(final int k) {
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
        public boolean addAll(final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public IntListIterator iterator() {
            return IntIterators.singleton(this.element);
        }
        
        @Override
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedSet extends IntCollections.SynchronizedCollection implements IntSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected SynchronizedSet(final IntSet s, final Object sync) {
            super(s, sync);
        }
        
        protected SynchronizedSet(final IntSet s) {
            super(s);
        }
        
        @Override
        public boolean remove(final int k) {
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
    
    public static class UnmodifiableSet extends IntCollections.UnmodifiableCollection implements IntSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected UnmodifiableSet(final IntSet s) {
            super(s);
        }
        
        @Override
        public boolean remove(final int k) {
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
