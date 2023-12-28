// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectArrays;

public class ByteCollections
{
    private ByteCollections() {
    }
    
    public static ByteCollection synchronize(final ByteCollection c) {
        return new SynchronizedCollection(c);
    }
    
    public static ByteCollection synchronize(final ByteCollection c, final Object sync) {
        return new SynchronizedCollection(c, sync);
    }
    
    public static ByteCollection unmodifiable(final ByteCollection c) {
        return new UnmodifiableCollection(c);
    }
    
    public static ByteCollection asCollection(final ByteIterable iterable) {
        if (iterable instanceof ByteCollection) {
            return (ByteCollection)iterable;
        }
        return new IterableCollection(iterable);
    }
    
    public abstract static class EmptyCollection extends AbstractByteCollection
    {
        protected EmptyCollection() {
        }
        
        @Override
        public boolean add(final byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final byte k) {
            return false;
        }
        
        @Override
        public Object[] toArray() {
            return ObjectArrays.EMPTY_ARRAY;
        }
        
        @Override
        public byte[] toByteArray(final byte[] a) {
            return a;
        }
        
        @Override
        public byte[] toByteArray() {
            return ByteArrays.EMPTY_ARRAY;
        }
        
        @Override
        public boolean rem(final byte k) {
            throw new UnsupportedOperationException();
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
        public boolean containsAll(final ByteCollection c) {
            return c.isEmpty();
        }
        
        @Override
        public ByteBidirectionalIterator iterator() {
            return ByteIterators.EMPTY_ITERATOR;
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
    
    public static class SynchronizedCollection implements ByteCollection, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ByteCollection collection;
        protected final Object sync;
        
        protected SynchronizedCollection(final ByteCollection c, final Object sync) {
            if (c == null) {
                throw new NullPointerException();
            }
            this.collection = c;
            this.sync = sync;
        }
        
        protected SynchronizedCollection(final ByteCollection c) {
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
        public boolean contains(final byte o) {
            synchronized (this.sync) {
                return this.collection.contains(o);
            }
        }
        
        @Override
        public byte[] toByteArray() {
            synchronized (this.sync) {
                return this.collection.toByteArray();
            }
        }
        
        @Override
        public Object[] toArray() {
            synchronized (this.sync) {
                return this.collection.toArray();
            }
        }
        
        @Override
        public byte[] toByteArray(final byte[] a) {
            synchronized (this.sync) {
                return this.collection.toByteArray(a);
            }
        }
        
        @Override
        public byte[] toArray(final byte[] a) {
            synchronized (this.sync) {
                return this.collection.toByteArray(a);
            }
        }
        
        @Override
        public boolean addAll(final ByteCollection c) {
            synchronized (this.sync) {
                return this.collection.addAll(c);
            }
        }
        
        @Override
        public boolean containsAll(final ByteCollection c) {
            synchronized (this.sync) {
                return this.collection.containsAll(c);
            }
        }
        
        @Override
        public boolean removeAll(final ByteCollection c) {
            synchronized (this.sync) {
                return this.collection.removeAll(c);
            }
        }
        
        @Override
        public boolean retainAll(final ByteCollection c) {
            synchronized (this.sync) {
                return this.collection.retainAll(c);
            }
        }
        
        @Override
        public boolean add(final Byte k) {
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
        public ByteIterator iterator() {
            return this.collection.iterator();
        }
        
        @Deprecated
        @Override
        public ByteIterator byteIterator() {
            return this.iterator();
        }
        
        @Override
        public boolean add(final byte k) {
            synchronized (this.sync) {
                return this.collection.add(k);
            }
        }
        
        @Override
        public boolean rem(final byte k) {
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
        public boolean addAll(final Collection<? extends Byte> c) {
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
    
    public static class UnmodifiableCollection implements ByteCollection, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ByteCollection collection;
        
        protected UnmodifiableCollection(final ByteCollection c) {
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
        public boolean contains(final byte o) {
            return this.collection.contains(o);
        }
        
        @Override
        public ByteIterator iterator() {
            return ByteIterators.unmodifiable(this.collection.iterator());
        }
        
        @Deprecated
        @Override
        public ByteIterator byteIterator() {
            return this.iterator();
        }
        
        @Override
        public boolean add(final byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final Object ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends Byte> c) {
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
        public byte[] toByteArray() {
            return this.collection.toByteArray();
        }
        
        @Override
        public byte[] toByteArray(final byte[] a) {
            return this.collection.toByteArray(a);
        }
        
        @Override
        public byte[] toArray(final byte[] a) {
            return this.collection.toArray(a);
        }
        
        @Override
        public boolean rem(final byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final ByteCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean containsAll(final ByteCollection c) {
            return this.collection.containsAll(c);
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
        public boolean add(final Byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final Object k) {
            return this.collection.contains(k);
        }
    }
    
    public static class IterableCollection extends AbstractByteCollection implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ByteIterable iterable;
        
        protected IterableCollection(final ByteIterable iterable) {
            if (iterable == null) {
                throw new NullPointerException();
            }
            this.iterable = iterable;
        }
        
        @Override
        public int size() {
            int c = 0;
            final ByteIterator iterator = this.iterator();
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
        public ByteIterator iterator() {
            return this.iterable.iterator();
        }
        
        @Deprecated
        @Override
        public ByteIterator byteIterator() {
            return this.iterator();
        }
    }
}
