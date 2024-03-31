// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectArrays;

public class IntCollections
{
    private IntCollections() {
    }
    
    public static IntCollection synchronize(final IntCollection c) {
        return new SynchronizedCollection(c);
    }
    
    public static IntCollection synchronize(final IntCollection c, final Object sync) {
        return new SynchronizedCollection(c, sync);
    }
    
    public static IntCollection unmodifiable(final IntCollection c) {
        return new UnmodifiableCollection(c);
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
        public boolean add(final int k) {
            throw new UnsupportedOperationException();
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
        public int[] toIntArray(final int[] a) {
            return a;
        }
        
        @Override
        public int[] toIntArray() {
            return IntArrays.EMPTY_ARRAY;
        }
        
        @Override
        public boolean rem(final int k) {
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
        
        @Override
        public boolean containsAll(final IntCollection c) {
            return c.isEmpty();
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
    }
    
    public static class SynchronizedCollection implements IntCollection, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntCollection collection;
        protected final Object sync;
        
        protected SynchronizedCollection(final IntCollection c, final Object sync) {
            if (c == null) {
                throw new NullPointerException();
            }
            this.collection = c;
            this.sync = sync;
        }
        
        protected SynchronizedCollection(final IntCollection c) {
            if (c == null) {
                throw new NullPointerException();
            }
            this.collection = c;
            this.sync = this;
        }
        
        @Override
        public int size() {
            synchronized (this.sync) {
                return this.collection.size();
            }
        }
        
        @Override
        public boolean isEmpty() {
            synchronized (this.sync) {
                return this.collection.isEmpty();
            }
        }
        
        @Override
        public boolean contains(final int o) {
            synchronized (this.sync) {
                return this.collection.contains(o);
            }
        }
        
        @Override
        public int[] toIntArray() {
            synchronized (this.sync) {
                return this.collection.toIntArray();
            }
        }
        
        @Override
        public Object[] toArray() {
            synchronized (this.sync) {
                return this.collection.toArray();
            }
        }
        
        @Override
        public int[] toIntArray(final int[] a) {
            synchronized (this.sync) {
                return this.collection.toIntArray(a);
            }
        }
        
        @Override
        public int[] toArray(final int[] a) {
            synchronized (this.sync) {
                return this.collection.toIntArray(a);
            }
        }
        
        @Override
        public boolean addAll(final IntCollection c) {
            synchronized (this.sync) {
                return this.collection.addAll(c);
            }
        }
        
        @Override
        public boolean containsAll(final IntCollection c) {
            synchronized (this.sync) {
                return this.collection.containsAll(c);
            }
        }
        
        @Override
        public boolean removeAll(final IntCollection c) {
            synchronized (this.sync) {
                return this.collection.removeAll(c);
            }
        }
        
        @Override
        public boolean retainAll(final IntCollection c) {
            synchronized (this.sync) {
                return this.collection.retainAll(c);
            }
        }
        
        @Override
        public boolean add(final Integer k) {
            synchronized (this.sync) {
                return this.collection.add(k);
            }
        }
        
        @Override
        public boolean contains(final Object k) {
            synchronized (this.sync) {
                return this.collection.contains(k);
            }
        }
        
        @Override
        public <T> T[] toArray(final T[] a) {
            synchronized (this.sync) {
                return this.collection.toArray(a);
            }
        }
        
        @Override
        public IntIterator iterator() {
            return this.collection.iterator();
        }
        
        @Deprecated
        @Override
        public IntIterator intIterator() {
            return this.iterator();
        }
        
        @Override
        public boolean add(final int k) {
            synchronized (this.sync) {
                return this.collection.add(k);
            }
        }
        
        @Override
        public boolean rem(final int k) {
            synchronized (this.sync) {
                return this.collection.rem(k);
            }
        }
        
        @Override
        public boolean remove(final Object ok) {
            synchronized (this.sync) {
                return this.collection.remove(ok);
            }
        }
        
        @Override
        public boolean addAll(final Collection<? extends Integer> c) {
            synchronized (this.sync) {
                return this.collection.addAll(c);
            }
        }
        
        @Override
        public boolean containsAll(final Collection<?> c) {
            synchronized (this.sync) {
                return this.collection.containsAll(c);
            }
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            synchronized (this.sync) {
                return this.collection.removeAll(c);
            }
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            synchronized (this.sync) {
                return this.collection.retainAll(c);
            }
        }
        
        @Override
        public void clear() {
            synchronized (this.sync) {
                this.collection.clear();
            }
        }
        
        @Override
        public String toString() {
            synchronized (this.sync) {
                return this.collection.toString();
            }
        }
    }
    
    public static class UnmodifiableCollection implements IntCollection, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntCollection collection;
        
        protected UnmodifiableCollection(final IntCollection c) {
            if (c == null) {
                throw new NullPointerException();
            }
            this.collection = c;
        }
        
        @Override
        public int size() {
            return this.collection.size();
        }
        
        @Override
        public boolean isEmpty() {
            return this.collection.isEmpty();
        }
        
        @Override
        public boolean contains(final int o) {
            return this.collection.contains(o);
        }
        
        @Override
        public IntIterator iterator() {
            return IntIterators.unmodifiable(this.collection.iterator());
        }
        
        @Deprecated
        @Override
        public IntIterator intIterator() {
            return this.iterator();
        }
        
        @Override
        public boolean add(final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final Object ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean containsAll(final Collection<?> c) {
            return this.collection.containsAll(c);
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
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public String toString() {
            return this.collection.toString();
        }
        
        @Override
        public <T> T[] toArray(final T[] a) {
            return this.collection.toArray(a);
        }
        
        @Override
        public Object[] toArray() {
            return this.collection.toArray();
        }
        
        @Override
        public int[] toIntArray() {
            return this.collection.toIntArray();
        }
        
        @Override
        public int[] toIntArray(final int[] a) {
            return this.collection.toIntArray(a);
        }
        
        @Override
        public int[] toArray(final int[] a) {
            return this.collection.toArray(a);
        }
        
        @Override
        public boolean rem(final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean containsAll(final IntCollection c) {
            return this.collection.containsAll(c);
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
        public boolean add(final Integer k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final Object k) {
            return this.collection.contains(k);
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
        public IntIterator iterator() {
            return this.iterable.iterator();
        }
        
        @Deprecated
        @Override
        public IntIterator intIterator() {
            return this.iterator();
        }
    }
}
