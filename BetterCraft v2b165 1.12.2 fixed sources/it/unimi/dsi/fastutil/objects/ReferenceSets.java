// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import java.io.Serializable;

public class ReferenceSets
{
    public static final EmptySet EMPTY_SET;
    
    private ReferenceSets() {
    }
    
    public static <K> ReferenceSet<K> emptySet() {
        return ReferenceSets.EMPTY_SET;
    }
    
    public static <K> ReferenceSet<K> singleton(final K element) {
        return new Singleton<K>(element);
    }
    
    public static <K> ReferenceSet<K> synchronize(final ReferenceSet<K> s) {
        return new SynchronizedSet<K>(s);
    }
    
    public static <K> ReferenceSet<K> synchronize(final ReferenceSet<K> s, final Object sync) {
        return new SynchronizedSet<K>(s, sync);
    }
    
    public static <K> ReferenceSet<K> unmodifiable(final ReferenceSet<K> s) {
        return new UnmodifiableSet<K>(s);
    }
    
    static {
        EMPTY_SET = new EmptySet();
    }
    
    public static class EmptySet<K> extends ReferenceCollections.EmptyCollection<K> implements ReferenceSet<K>, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public boolean remove(final Object ok) {
            throw new UnsupportedOperationException();
        }
        
        public Object clone() {
            return ReferenceSets.EMPTY_SET;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Set && ((Set)o).isEmpty();
        }
        
        private Object readResolve() {
            return ReferenceSets.EMPTY_SET;
        }
    }
    
    public static class Singleton<K> extends AbstractReferenceSet<K> implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final K element;
        
        protected Singleton(final K element) {
            this.element = element;
        }
        
        @Override
        public boolean add(final K k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final Object k) {
            return k == this.element;
        }
        
        @Override
        public boolean addAll(final Collection<? extends K> c) {
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
        public ObjectListIterator<K> iterator() {
            return ObjectIterators.singleton(this.element);
        }
        
        @Override
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedSet<K> extends ReferenceCollections.SynchronizedCollection<K> implements ReferenceSet<K>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected SynchronizedSet(final ReferenceSet<K> s, final Object sync) {
            super(s, sync);
        }
        
        protected SynchronizedSet(final ReferenceSet<K> s) {
            super(s);
        }
        
        @Override
        public boolean remove(final Object k) {
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
    
    public static class UnmodifiableSet<K> extends ReferenceCollections.UnmodifiableCollection<K> implements ReferenceSet<K>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected UnmodifiableSet(final ReferenceSet<K> s) {
            super(s);
        }
        
        @Override
        public boolean remove(final Object k) {
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
