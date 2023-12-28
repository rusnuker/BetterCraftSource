// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectArrays;

public class LongCollections
{
    private LongCollections() {
    }
    
    public static LongCollection synchronize(final LongCollection c) {
        return new SynchronizedCollection(c);
    }
    
    public static LongCollection synchronize(final LongCollection c, final Object sync) {
        return new SynchronizedCollection(c, sync);
    }
    
    public static LongCollection unmodifiable(final LongCollection c) {
        return new UnmodifiableCollection(c);
    }
    
    public static LongCollection asCollection(final LongIterable iterable) {
        if (iterable instanceof LongCollection) {
            return (LongCollection)iterable;
        }
        return new IterableCollection(iterable);
    }
    
    public abstract static class EmptyCollection extends AbstractLongCollection
    {
        protected EmptyCollection() {
        }
        
        @Override
        public boolean add(final long k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final long k) {
            return false;
        }
        
        @Override
        public Object[] toArray() {
            return ObjectArrays.EMPTY_ARRAY;
        }
        
        @Override
        public long[] toLongArray(final long[] a) {
            return a;
        }
        
        @Override
        public long[] toLongArray() {
            return LongArrays.EMPTY_ARRAY;
        }
        
        @Override
        public boolean rem(final long k) {
            throw new UnsupportedOperationException();
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
        public boolean containsAll(final LongCollection c) {
            return c.isEmpty();
        }
        
        @Override
        public LongBidirectionalIterator iterator() {
            return LongIterators.EMPTY_ITERATOR;
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
    
    public static class SynchronizedCollection implements LongCollection, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongCollection collection;
        protected final Object sync;
        
        protected SynchronizedCollection(final LongCollection c, final Object sync) {
            if (c == null) {
                throw new NullPointerException();
            }
            this.collection = c;
            this.sync = sync;
        }
        
        protected SynchronizedCollection(final LongCollection c) {
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
        public boolean contains(final long o) {
            synchronized (this.sync) {
                return this.collection.contains(o);
            }
        }
        
        @Override
        public long[] toLongArray() {
            synchronized (this.sync) {
                return this.collection.toLongArray();
            }
        }
        
        @Override
        public Object[] toArray() {
            synchronized (this.sync) {
                return this.collection.toArray();
            }
        }
        
        @Override
        public long[] toLongArray(final long[] a) {
            synchronized (this.sync) {
                return this.collection.toLongArray(a);
            }
        }
        
        @Override
        public long[] toArray(final long[] a) {
            synchronized (this.sync) {
                return this.collection.toLongArray(a);
            }
        }
        
        @Override
        public boolean addAll(final LongCollection c) {
            synchronized (this.sync) {
                return this.collection.addAll(c);
            }
        }
        
        @Override
        public boolean containsAll(final LongCollection c) {
            synchronized (this.sync) {
                return this.collection.containsAll(c);
            }
        }
        
        @Override
        public boolean removeAll(final LongCollection c) {
            synchronized (this.sync) {
                return this.collection.removeAll(c);
            }
        }
        
        @Override
        public boolean retainAll(final LongCollection c) {
            synchronized (this.sync) {
                return this.collection.retainAll(c);
            }
        }
        
        @Override
        public boolean add(final Long k) {
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
        public LongIterator iterator() {
            return this.collection.iterator();
        }
        
        @Deprecated
        @Override
        public LongIterator longIterator() {
            return this.iterator();
        }
        
        @Override
        public boolean add(final long k) {
            synchronized (this.sync) {
                return this.collection.add(k);
            }
        }
        
        @Override
        public boolean rem(final long k) {
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
        public boolean addAll(final Collection<? extends Long> c) {
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
    
    public static class UnmodifiableCollection implements LongCollection, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongCollection collection;
        
        protected UnmodifiableCollection(final LongCollection c) {
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
        public boolean contains(final long o) {
            return this.collection.contains(o);
        }
        
        @Override
        public LongIterator iterator() {
            return LongIterators.unmodifiable(this.collection.iterator());
        }
        
        @Deprecated
        @Override
        public LongIterator longIterator() {
            return this.iterator();
        }
        
        @Override
        public boolean add(final long k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final Object ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends Long> c) {
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
        public long[] toLongArray() {
            return this.collection.toLongArray();
        }
        
        @Override
        public long[] toLongArray(final long[] a) {
            return this.collection.toLongArray(a);
        }
        
        @Override
        public long[] toArray(final long[] a) {
            return this.collection.toArray(a);
        }
        
        @Override
        public boolean rem(final long k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final LongCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean containsAll(final LongCollection c) {
            return this.collection.containsAll(c);
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
        public boolean add(final Long k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final Object k) {
            return this.collection.contains(k);
        }
    }
    
    public static class IterableCollection extends AbstractLongCollection implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongIterable iterable;
        
        protected IterableCollection(final LongIterable iterable) {
            if (iterable == null) {
                throw new NullPointerException();
            }
            this.iterable = iterable;
        }
        
        @Override
        public int size() {
            int c = 0;
            final LongIterator iterator = this.iterator();
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
        public LongIterator iterator() {
            return this.iterable.iterator();
        }
        
        @Deprecated
        @Override
        public LongIterator longIterator() {
            return this.iterator();
        }
    }
}
