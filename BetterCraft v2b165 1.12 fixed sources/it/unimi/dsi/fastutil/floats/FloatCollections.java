// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectArrays;

public class FloatCollections
{
    private FloatCollections() {
    }
    
    public static FloatCollection synchronize(final FloatCollection c) {
        return new SynchronizedCollection(c);
    }
    
    public static FloatCollection synchronize(final FloatCollection c, final Object sync) {
        return new SynchronizedCollection(c, sync);
    }
    
    public static FloatCollection unmodifiable(final FloatCollection c) {
        return new UnmodifiableCollection(c);
    }
    
    public static FloatCollection asCollection(final FloatIterable iterable) {
        if (iterable instanceof FloatCollection) {
            return (FloatCollection)iterable;
        }
        return new IterableCollection(iterable);
    }
    
    public abstract static class EmptyCollection extends AbstractFloatCollection
    {
        protected EmptyCollection() {
        }
        
        @Override
        public boolean add(final float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final float k) {
            return false;
        }
        
        @Override
        public Object[] toArray() {
            return ObjectArrays.EMPTY_ARRAY;
        }
        
        @Override
        public float[] toFloatArray(final float[] a) {
            return a;
        }
        
        @Override
        public float[] toFloatArray() {
            return FloatArrays.EMPTY_ARRAY;
        }
        
        @Override
        public boolean rem(final float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final FloatCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final FloatCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final FloatCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean containsAll(final FloatCollection c) {
            return c.isEmpty();
        }
        
        @Override
        public FloatBidirectionalIterator iterator() {
            return FloatIterators.EMPTY_ITERATOR;
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
    
    public static class SynchronizedCollection implements FloatCollection, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final FloatCollection collection;
        protected final Object sync;
        
        protected SynchronizedCollection(final FloatCollection c, final Object sync) {
            if (c == null) {
                throw new NullPointerException();
            }
            this.collection = c;
            this.sync = sync;
        }
        
        protected SynchronizedCollection(final FloatCollection c) {
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
        public boolean contains(final float o) {
            synchronized (this.sync) {
                return this.collection.contains(o);
            }
        }
        
        @Override
        public float[] toFloatArray() {
            synchronized (this.sync) {
                return this.collection.toFloatArray();
            }
        }
        
        @Override
        public Object[] toArray() {
            synchronized (this.sync) {
                return this.collection.toArray();
            }
        }
        
        @Override
        public float[] toFloatArray(final float[] a) {
            synchronized (this.sync) {
                return this.collection.toFloatArray(a);
            }
        }
        
        @Override
        public float[] toArray(final float[] a) {
            synchronized (this.sync) {
                return this.collection.toFloatArray(a);
            }
        }
        
        @Override
        public boolean addAll(final FloatCollection c) {
            synchronized (this.sync) {
                return this.collection.addAll(c);
            }
        }
        
        @Override
        public boolean containsAll(final FloatCollection c) {
            synchronized (this.sync) {
                return this.collection.containsAll(c);
            }
        }
        
        @Override
        public boolean removeAll(final FloatCollection c) {
            synchronized (this.sync) {
                return this.collection.removeAll(c);
            }
        }
        
        @Override
        public boolean retainAll(final FloatCollection c) {
            synchronized (this.sync) {
                return this.collection.retainAll(c);
            }
        }
        
        @Override
        public boolean add(final Float k) {
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
        public FloatIterator iterator() {
            return this.collection.iterator();
        }
        
        @Deprecated
        @Override
        public FloatIterator floatIterator() {
            return this.iterator();
        }
        
        @Override
        public boolean add(final float k) {
            synchronized (this.sync) {
                return this.collection.add(k);
            }
        }
        
        @Override
        public boolean rem(final float k) {
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
        public boolean addAll(final Collection<? extends Float> c) {
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
    
    public static class UnmodifiableCollection implements FloatCollection, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final FloatCollection collection;
        
        protected UnmodifiableCollection(final FloatCollection c) {
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
        public boolean contains(final float o) {
            return this.collection.contains(o);
        }
        
        @Override
        public FloatIterator iterator() {
            return FloatIterators.unmodifiable(this.collection.iterator());
        }
        
        @Deprecated
        @Override
        public FloatIterator floatIterator() {
            return this.iterator();
        }
        
        @Override
        public boolean add(final float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final Object ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends Float> c) {
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
        public float[] toFloatArray() {
            return this.collection.toFloatArray();
        }
        
        @Override
        public float[] toFloatArray(final float[] a) {
            return this.collection.toFloatArray(a);
        }
        
        @Override
        public float[] toArray(final float[] a) {
            return this.collection.toArray(a);
        }
        
        @Override
        public boolean rem(final float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final FloatCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean containsAll(final FloatCollection c) {
            return this.collection.containsAll(c);
        }
        
        @Override
        public boolean removeAll(final FloatCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final FloatCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final Float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final Object k) {
            return this.collection.contains(k);
        }
    }
    
    public static class IterableCollection extends AbstractFloatCollection implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final FloatIterable iterable;
        
        protected IterableCollection(final FloatIterable iterable) {
            if (iterable == null) {
                throw new NullPointerException();
            }
            this.iterable = iterable;
        }
        
        @Override
        public int size() {
            int c = 0;
            final FloatIterator iterator = this.iterator();
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
        public FloatIterator iterator() {
            return this.iterable.iterator();
        }
        
        @Deprecated
        @Override
        public FloatIterator floatIterator() {
            return this.iterator();
        }
    }
}
