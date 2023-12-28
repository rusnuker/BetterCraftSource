// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectArrays;

public class ShortCollections
{
    private ShortCollections() {
    }
    
    public static ShortCollection synchronize(final ShortCollection c) {
        return new SynchronizedCollection(c);
    }
    
    public static ShortCollection synchronize(final ShortCollection c, final Object sync) {
        return new SynchronizedCollection(c, sync);
    }
    
    public static ShortCollection unmodifiable(final ShortCollection c) {
        return new UnmodifiableCollection(c);
    }
    
    public static ShortCollection asCollection(final ShortIterable iterable) {
        if (iterable instanceof ShortCollection) {
            return (ShortCollection)iterable;
        }
        return new IterableCollection(iterable);
    }
    
    public abstract static class EmptyCollection extends AbstractShortCollection
    {
        protected EmptyCollection() {
        }
        
        @Override
        public boolean add(final short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final short k) {
            return false;
        }
        
        @Override
        public Object[] toArray() {
            return ObjectArrays.EMPTY_ARRAY;
        }
        
        @Override
        public short[] toShortArray(final short[] a) {
            return a;
        }
        
        @Override
        public short[] toShortArray() {
            return ShortArrays.EMPTY_ARRAY;
        }
        
        @Override
        public boolean rem(final short k) {
            throw new UnsupportedOperationException();
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
        public boolean containsAll(final ShortCollection c) {
            return c.isEmpty();
        }
        
        @Override
        public ShortBidirectionalIterator iterator() {
            return ShortIterators.EMPTY_ITERATOR;
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
    
    public static class SynchronizedCollection implements ShortCollection, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ShortCollection collection;
        protected final Object sync;
        
        protected SynchronizedCollection(final ShortCollection c, final Object sync) {
            if (c == null) {
                throw new NullPointerException();
            }
            this.collection = c;
            this.sync = sync;
        }
        
        protected SynchronizedCollection(final ShortCollection c) {
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
        public boolean contains(final short o) {
            synchronized (this.sync) {
                return this.collection.contains(o);
            }
        }
        
        @Override
        public short[] toShortArray() {
            synchronized (this.sync) {
                return this.collection.toShortArray();
            }
        }
        
        @Override
        public Object[] toArray() {
            synchronized (this.sync) {
                return this.collection.toArray();
            }
        }
        
        @Override
        public short[] toShortArray(final short[] a) {
            synchronized (this.sync) {
                return this.collection.toShortArray(a);
            }
        }
        
        @Override
        public short[] toArray(final short[] a) {
            synchronized (this.sync) {
                return this.collection.toShortArray(a);
            }
        }
        
        @Override
        public boolean addAll(final ShortCollection c) {
            synchronized (this.sync) {
                return this.collection.addAll(c);
            }
        }
        
        @Override
        public boolean containsAll(final ShortCollection c) {
            synchronized (this.sync) {
                return this.collection.containsAll(c);
            }
        }
        
        @Override
        public boolean removeAll(final ShortCollection c) {
            synchronized (this.sync) {
                return this.collection.removeAll(c);
            }
        }
        
        @Override
        public boolean retainAll(final ShortCollection c) {
            synchronized (this.sync) {
                return this.collection.retainAll(c);
            }
        }
        
        @Override
        public boolean add(final Short k) {
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
        public ShortIterator iterator() {
            return this.collection.iterator();
        }
        
        @Deprecated
        @Override
        public ShortIterator shortIterator() {
            return this.iterator();
        }
        
        @Override
        public boolean add(final short k) {
            synchronized (this.sync) {
                return this.collection.add(k);
            }
        }
        
        @Override
        public boolean rem(final short k) {
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
        public boolean addAll(final Collection<? extends Short> c) {
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
    
    public static class UnmodifiableCollection implements ShortCollection, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ShortCollection collection;
        
        protected UnmodifiableCollection(final ShortCollection c) {
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
        public boolean contains(final short o) {
            return this.collection.contains(o);
        }
        
        @Override
        public ShortIterator iterator() {
            return ShortIterators.unmodifiable(this.collection.iterator());
        }
        
        @Deprecated
        @Override
        public ShortIterator shortIterator() {
            return this.iterator();
        }
        
        @Override
        public boolean add(final short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final Object ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends Short> c) {
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
        public short[] toShortArray() {
            return this.collection.toShortArray();
        }
        
        @Override
        public short[] toShortArray(final short[] a) {
            return this.collection.toShortArray(a);
        }
        
        @Override
        public short[] toArray(final short[] a) {
            return this.collection.toArray(a);
        }
        
        @Override
        public boolean rem(final short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final ShortCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean containsAll(final ShortCollection c) {
            return this.collection.containsAll(c);
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
        public boolean add(final Short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final Object k) {
            return this.collection.contains(k);
        }
    }
    
    public static class IterableCollection extends AbstractShortCollection implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ShortIterable iterable;
        
        protected IterableCollection(final ShortIterable iterable) {
            if (iterable == null) {
                throw new NullPointerException();
            }
            this.iterable = iterable;
        }
        
        @Override
        public int size() {
            int c = 0;
            final ShortIterator iterator = this.iterator();
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
        public ShortIterator iterator() {
            return this.iterable.iterator();
        }
        
        @Deprecated
        @Override
        public ShortIterator shortIterator() {
            return this.iterator();
        }
    }
}
