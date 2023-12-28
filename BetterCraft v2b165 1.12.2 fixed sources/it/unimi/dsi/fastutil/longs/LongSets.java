// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import java.io.Serializable;

public class LongSets
{
    public static final EmptySet EMPTY_SET;
    
    private LongSets() {
    }
    
    public static LongSet singleton(final long element) {
        return new Singleton(element);
    }
    
    public static LongSet singleton(final Long element) {
        return new Singleton(element);
    }
    
    public static LongSet synchronize(final LongSet s) {
        return new SynchronizedSet(s);
    }
    
    public static LongSet synchronize(final LongSet s, final Object sync) {
        return new SynchronizedSet(s, sync);
    }
    
    public static LongSet unmodifiable(final LongSet s) {
        return new UnmodifiableSet(s);
    }
    
    static {
        EMPTY_SET = new EmptySet();
    }
    
    public static class EmptySet extends LongCollections.EmptyCollection implements LongSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public boolean remove(final long ok) {
            throw new UnsupportedOperationException();
        }
        
        public Object clone() {
            return LongSets.EMPTY_SET;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Set && ((Set)o).isEmpty();
        }
        
        private Object readResolve() {
            return LongSets.EMPTY_SET;
        }
    }
    
    public static class Singleton extends AbstractLongSet implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final long element;
        
        protected Singleton(final long element) {
            this.element = element;
        }
        
        @Override
        public boolean add(final long k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final long k) {
            return k == this.element;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Long> c) {
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
        public long[] toLongArray() {
            final long[] a = { this.element };
            return a;
        }
        
        @Override
        public boolean addAll(final LongCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final LongCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final LongCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public LongListIterator iterator() {
            return LongIterators.singleton(this.element);
        }
        
        @Override
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedSet extends LongCollections.SynchronizedCollection implements LongSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected SynchronizedSet(final LongSet s, final Object sync) {
            super(s, sync);
        }
        
        protected SynchronizedSet(final LongSet s) {
            super(s);
        }
        
        @Override
        public boolean remove(final long k) {
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
    
    public static class UnmodifiableSet extends LongCollections.UnmodifiableCollection implements LongSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected UnmodifiableSet(final LongSet s) {
            super(s);
        }
        
        @Override
        public boolean remove(final long k) {
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
