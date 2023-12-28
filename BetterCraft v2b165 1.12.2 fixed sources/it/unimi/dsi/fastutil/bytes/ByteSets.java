// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import java.io.Serializable;

public class ByteSets
{
    public static final EmptySet EMPTY_SET;
    
    private ByteSets() {
    }
    
    public static ByteSet singleton(final byte element) {
        return new Singleton(element);
    }
    
    public static ByteSet singleton(final Byte element) {
        return new Singleton(element);
    }
    
    public static ByteSet synchronize(final ByteSet s) {
        return new SynchronizedSet(s);
    }
    
    public static ByteSet synchronize(final ByteSet s, final Object sync) {
        return new SynchronizedSet(s, sync);
    }
    
    public static ByteSet unmodifiable(final ByteSet s) {
        return new UnmodifiableSet(s);
    }
    
    static {
        EMPTY_SET = new EmptySet();
    }
    
    public static class EmptySet extends ByteCollections.EmptyCollection implements ByteSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public boolean remove(final byte ok) {
            throw new UnsupportedOperationException();
        }
        
        public Object clone() {
            return ByteSets.EMPTY_SET;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Set && ((Set)o).isEmpty();
        }
        
        private Object readResolve() {
            return ByteSets.EMPTY_SET;
        }
    }
    
    public static class Singleton extends AbstractByteSet implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final byte element;
        
        protected Singleton(final byte element) {
            this.element = element;
        }
        
        @Override
        public boolean add(final byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final byte k) {
            return k == this.element;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Byte> c) {
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
        public byte[] toByteArray() {
            final byte[] a = { this.element };
            return a;
        }
        
        @Override
        public boolean addAll(final ByteCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final ByteCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final ByteCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ByteListIterator iterator() {
            return ByteIterators.singleton(this.element);
        }
        
        @Override
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedSet extends ByteCollections.SynchronizedCollection implements ByteSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected SynchronizedSet(final ByteSet s, final Object sync) {
            super(s, sync);
        }
        
        protected SynchronizedSet(final ByteSet s) {
            super(s);
        }
        
        @Override
        public boolean remove(final byte k) {
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
    
    public static class UnmodifiableSet extends ByteCollections.UnmodifiableCollection implements ByteSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected UnmodifiableSet(final ByteSet s) {
            super(s);
        }
        
        @Override
        public boolean remove(final byte k) {
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
