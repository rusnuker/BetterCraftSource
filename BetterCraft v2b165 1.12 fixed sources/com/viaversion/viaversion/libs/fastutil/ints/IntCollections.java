// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectArrays;

public final class IntCollections
{
    private IntCollections() {
    }
    
    public static IntCollection synchronize(final IntCollection c) {
        return (IntCollection)new IntCollections.SynchronizedCollection(c);
    }
    
    public static IntCollection synchronize(final IntCollection c, final Object sync) {
        return (IntCollection)new IntCollections.SynchronizedCollection(c, sync);
    }
    
    public static IntCollection unmodifiable(final IntCollection c) {
        return (IntCollection)new IntCollections.UnmodifiableCollection(c);
    }
    
    public static IntCollection asCollection(final IntIterable iterable) {
        if (iterable instanceof IntCollection) {
            return (IntCollection)iterable;
        }
        return new IterableCollection(iterable);
    }
    
    public abstract static class EmptyCollection extends AbstractIntCollection
    {
        protected EmptyCollection() {
        }
        
        @Override
        public boolean contains(final int k) {
            return false;
        }
        
        @Override
        public Object[] toArray() {
            return ObjectArrays.EMPTY_ARRAY;
        }
        
        @Override
        public IntBidirectionalIterator iterator() {
            return IntIterators.EMPTY_ITERATOR;
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
    }
    
    public static class IterableCollection extends AbstractIntCollection implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntIterable iterable;
        
        protected IterableCollection(final IntIterable iterable) {
            if (iterable == null) {
                throw new NullPointerException();
            }
            this.iterable = iterable;
        }
        
        @Override
        public int size() {
            int c = 0;
            final IntIterator iterator = this.iterator();
            while (iterator.hasNext()) {
                iterator.nextInt();
                ++c;
            }
            return c;
        }
        
        @Override
        public boolean isEmpty() {
            return !this.iterable.iterator().hasNext();
        }
        
        @Override
        public IntIterator iterator() {
            return this.iterable.iterator();
        }
    }
}
