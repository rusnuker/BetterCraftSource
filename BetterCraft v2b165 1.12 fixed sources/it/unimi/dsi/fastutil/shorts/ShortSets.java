// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import java.io.Serializable;

public class ShortSets
{
    public static final EmptySet EMPTY_SET;
    
    private ShortSets() {
    }
    
    public static ShortSet singleton(final short element) {
        return new Singleton(element);
    }
    
    public static ShortSet singleton(final Short element) {
        return new Singleton(element);
    }
    
    public static ShortSet synchronize(final ShortSet s) {
        return new SynchronizedSet(s);
    }
    
    public static ShortSet synchronize(final ShortSet s, final Object sync) {
        return new SynchronizedSet(s, sync);
    }
    
    public static ShortSet unmodifiable(final ShortSet s) {
        return new UnmodifiableSet(s);
    }
    
    static {
        EMPTY_SET = new EmptySet();
    }
    
    public static class EmptySet extends ShortCollections.EmptyCollection implements ShortSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public boolean remove(final short ok) {
            throw new UnsupportedOperationException();
        }
        
        public Object clone() {
            return ShortSets.EMPTY_SET;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Set && ((Set)o).isEmpty();
        }
        
        private Object readResolve() {
            return ShortSets.EMPTY_SET;
        }
    }
    
    public static class Singleton extends AbstractShortSet implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final short element;
        
        protected Singleton(final short element) {
            this.element = element;
        }
        
        @Override
        public boolean add(final short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final short k) {
            return k == this.element;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Short> c) {
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
        public short[] toShortArray() {
            final short[] a = { this.element };
            return a;
        }
        
        @Override
        public boolean addAll(final ShortCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final ShortCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final ShortCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ShortListIterator iterator() {
            return ShortIterators.singleton(this.element);
        }
        
        @Override
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedSet extends ShortCollections.SynchronizedCollection implements ShortSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected SynchronizedSet(final ShortSet s, final Object sync) {
            super(s, sync);
        }
        
        protected SynchronizedSet(final ShortSet s) {
            super(s);
        }
        
        @Override
        public boolean remove(final short k) {
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
    
    public static class UnmodifiableSet extends ShortCollections.UnmodifiableCollection implements ShortSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected UnmodifiableSet(final ShortSet s) {
            super(s);
        }
        
        @Override
        public boolean remove(final short k) {
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
