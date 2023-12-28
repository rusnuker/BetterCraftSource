// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;

public class ObjectCollections
{
    private ObjectCollections() {
    }
    
    public static <K> ObjectCollection<K> synchronize(final ObjectCollection<K> c) {
        return new SynchronizedCollection<K>(c);
    }
    
    public static <K> ObjectCollection<K> synchronize(final ObjectCollection<K> c, final Object sync) {
        return new SynchronizedCollection<K>(c, sync);
    }
    
    public static <K> ObjectCollection<K> unmodifiable(final ObjectCollection<K> c) {
        return new UnmodifiableCollection<K>(c);
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
        public boolean add(final K k) {
            throw new UnsupportedOperationException();
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
        public boolean remove(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public <T> T[] toArray(final T[] a) {
            if (a.length != 0) {
                a[0] = null;
            }
            return a;
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
    }
    
    public static class SynchronizedCollection<K> implements ObjectCollection<K>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ObjectCollection<K> collection;
        protected final Object sync;
        
        protected SynchronizedCollection(final ObjectCollection<K> c, final Object sync) {
            if (c == null) {
                throw new NullPointerException();
            }
            this.collection = c;
            this.sync = sync;
        }
        
        protected SynchronizedCollection(final ObjectCollection<K> c) {
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
        public boolean contains(final Object o) {
            synchronized (this.sync) {
                return this.collection.contains(o);
            }
        }
        
        @Override
        public Object[] toArray() {
            synchronized (this.sync) {
                return this.collection.toArray();
            }
        }
        
        @Override
        public <T> T[] toArray(final T[] a) {
            synchronized (this.sync) {
                return this.collection.toArray(a);
            }
        }
        
        @Override
        public ObjectIterator<K> iterator() {
            return this.collection.iterator();
        }
        
        @Deprecated
        @Override
        public ObjectIterator<K> objectIterator() {
            return this.iterator();
        }
        
        @Override
        public boolean add(final K k) {
            synchronized (this.sync) {
                return this.collection.add(k);
            }
        }
        
        public boolean rem(final Object k) {
            synchronized (this.sync) {
                return this.collection.remove(k);
            }
        }
        
        @Override
        public boolean remove(final Object ok) {
            synchronized (this.sync) {
                return this.collection.remove(ok);
            }
        }
        
        @Override
        public boolean addAll(final Collection<? extends K> c) {
            synchronized (this.sync) {
                return this.collection.addAll((Collection<?>)c);
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
    
    public static class UnmodifiableCollection<K> implements ObjectCollection<K>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ObjectCollection<K> collection;
        
        protected UnmodifiableCollection(final ObjectCollection<K> c) {
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
        public boolean contains(final Object o) {
            return this.collection.contains(o);
        }
        
        @Override
        public ObjectIterator<K> iterator() {
            return ObjectIterators.unmodifiable(this.collection.iterator());
        }
        
        @Deprecated
        @Override
        public ObjectIterator<K> objectIterator() {
            return this.iterator();
        }
        
        @Override
        public boolean add(final K k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final Object ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends K> c) {
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
        
        @Deprecated
        @Override
        public ObjectIterator<K> objectIterator() {
            return this.iterator();
        }
    }
}
