// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;

public final class ObjectCollections
{
    private ObjectCollections() {
    }
    
    public static <K> ObjectCollection<K> synchronize(final ObjectCollection<K> c) {
        return (ObjectCollection<K>)new ObjectCollections.SynchronizedCollection((ObjectCollection)c);
    }
    
    public static <K> ObjectCollection<K> synchronize(final ObjectCollection<K> c, final Object sync) {
        return (ObjectCollection<K>)new ObjectCollections.SynchronizedCollection((ObjectCollection)c, sync);
    }
    
    public static <K> ObjectCollection<K> unmodifiable(final ObjectCollection<K> c) {
        return (ObjectCollection<K>)new ObjectCollections.UnmodifiableCollection((ObjectCollection)c);
    }
    
    public static <K> ObjectCollection<K> asCollection(final ObjectIterable<K> iterable) {
        if (iterable instanceof ObjectCollection) {
            return (ObjectCollection)iterable;
        }
        return new IterableCollection<K>(iterable);
    }
    
    public abstract static class EmptyCollection<K> extends AbstractObjectCollection<K>
    {
        protected EmptyCollection() {
        }
        
        @Override
        public boolean contains(final Object k) {
            return false;
        }
        
        @Override
        public Object[] toArray() {
            return ObjectArrays.EMPTY_ARRAY;
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return ObjectIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public int size() {
            return 0;
        }
        
        @Override
        public void clear() {
        }
        
        @Override
        public int hashCode() {
            return 0;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o == this || (o instanceof Collection && ((Collection)o).isEmpty());
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
    }
    
    public static class IterableCollection<K> extends AbstractObjectCollection<K> implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ObjectIterable<K> iterable;
        
        protected IterableCollection(final ObjectIterable<K> iterable) {
            if (iterable == null) {
                throw new NullPointerException();
            }
            this.iterable = iterable;
        }
        
        @Override
        public int size() {
            int c = 0;
            final ObjectIterator<K> iterator = this.iterator();
            while (iterator.hasNext()) {
                iterator.next();
                ++c;
            }
            return c;
        }
        
        @Override
        public boolean isEmpty() {
            return !this.iterable.iterator().hasNext();
        }
        
        @Override
        public ObjectIterator<K> iterator() {
            return this.iterable.iterator();
        }
    }
}
