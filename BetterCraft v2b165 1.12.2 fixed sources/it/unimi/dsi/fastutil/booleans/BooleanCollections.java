// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectArrays;

public class BooleanCollections
{
    private BooleanCollections() {
    }
    
    public static BooleanCollection synchronize(final BooleanCollection c) {
        return new SynchronizedCollection(c);
    }
    
    public static BooleanCollection synchronize(final BooleanCollection c, final Object sync) {
        return new SynchronizedCollection(c, sync);
    }
    
    public static BooleanCollection unmodifiable(final BooleanCollection c) {
        return new UnmodifiableCollection(c);
    }
    
    public static BooleanCollection asCollection(final BooleanIterable iterable) {
        if (iterable instanceof BooleanCollection) {
            return (BooleanCollection)iterable;
        }
        return new IterableCollection(iterable);
    }
    
    public abstract static class EmptyCollection extends AbstractBooleanCollection
    {
        protected EmptyCollection() {
        }
        
        @Override
        public boolean add(final boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final boolean k) {
            return false;
        }
        
        @Override
        public Object[] toArray() {
            return ObjectArrays.EMPTY_ARRAY;
        }
        
        @Override
        public boolean[] toBooleanArray(final boolean[] a) {
            return a;
        }
        
        @Override
        public boolean[] toBooleanArray() {
            return BooleanArrays.EMPTY_ARRAY;
        }
        
        @Override
        public boolean rem(final boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean containsAll(final BooleanCollection c) {
            return c.isEmpty();
        }
        
        @Override
        public BooleanBidirectionalIterator iterator() {
            return BooleanIterators.EMPTY_ITERATOR;
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
    
    public static class SynchronizedCollection implements BooleanCollection, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final BooleanCollection collection;
        protected final Object sync;
        
        protected SynchronizedCollection(final BooleanCollection c, final Object sync) {
            if (c == null) {
                throw new NullPointerException();
            }
            this.collection = c;
            this.sync = sync;
        }
        
        protected SynchronizedCollection(final BooleanCollection c) {
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
        public boolean contains(final boolean o) {
            synchronized (this.sync) {
                return this.collection.contains(o);
            }
        }
        
        @Override
        public boolean[] toBooleanArray() {
            synchronized (this.sync) {
                return this.collection.toBooleanArray();
            }
        }
        
        @Override
        public Object[] toArray() {
            synchronized (this.sync) {
                return this.collection.toArray();
            }
        }
        
        @Override
        public boolean[] toBooleanArray(final boolean[] a) {
            synchronized (this.sync) {
                return this.collection.toBooleanArray(a);
            }
        }
        
        @Override
        public boolean[] toArray(final boolean[] a) {
            synchronized (this.sync) {
                return this.collection.toBooleanArray(a);
            }
        }
        
        @Override
        public boolean addAll(final BooleanCollection c) {
            synchronized (this.sync) {
                return this.collection.addAll(c);
            }
        }
        
        @Override
        public boolean containsAll(final BooleanCollection c) {
            synchronized (this.sync) {
                return this.collection.containsAll(c);
            }
        }
        
        @Override
        public boolean removeAll(final BooleanCollection c) {
            synchronized (this.sync) {
                return this.collection.removeAll(c);
            }
        }
        
        @Override
        public boolean retainAll(final BooleanCollection c) {
            synchronized (this.sync) {
                return this.collection.retainAll(c);
            }
        }
        
        @Override
        public boolean add(final Boolean k) {
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
        public BooleanIterator iterator() {
            return this.collection.iterator();
        }
        
        @Deprecated
        @Override
        public BooleanIterator booleanIterator() {
            return this.iterator();
        }
        
        @Override
        public boolean add(final boolean k) {
            synchronized (this.sync) {
                return this.collection.add(k);
            }
        }
        
        @Override
        public boolean rem(final boolean k) {
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
        public boolean addAll(final Collection<? extends Boolean> c) {
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
    
    public static class UnmodifiableCollection implements BooleanCollection, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final BooleanCollection collection;
        
        protected UnmodifiableCollection(final BooleanCollection c) {
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
        public boolean contains(final boolean o) {
            return this.collection.contains(o);
        }
        
        @Override
        public BooleanIterator iterator() {
            return BooleanIterators.unmodifiable(this.collection.iterator());
        }
        
        @Deprecated
        @Override
        public BooleanIterator booleanIterator() {
            return this.iterator();
        }
        
        @Override
        public boolean add(final boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final Object ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends Boolean> c) {
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
        public boolean[] toBooleanArray() {
            return this.collection.toBooleanArray();
        }
        
        @Override
        public boolean[] toBooleanArray(final boolean[] a) {
            return this.collection.toBooleanArray(a);
        }
        
        @Override
        public boolean[] toArray(final boolean[] a) {
            return this.collection.toArray(a);
        }
        
        @Override
        public boolean rem(final boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean containsAll(final BooleanCollection c) {
            return this.collection.containsAll(c);
        }
        
        @Override
        public boolean removeAll(final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final Boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final Object k) {
            return this.collection.contains(k);
        }
    }
    
    public static class IterableCollection extends AbstractBooleanCollection implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final BooleanIterable iterable;
        
        protected IterableCollection(final BooleanIterable iterable) {
            if (iterable == null) {
                throw new NullPointerException();
            }
            this.iterable = iterable;
        }
        
        @Override
        public int size() {
            int c = 0;
            final BooleanIterator iterator = this.iterator();
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
        public BooleanIterator iterator() {
            return this.iterable.iterator();
        }
        
        @Deprecated
        @Override
        public BooleanIterator booleanIterator() {
            return this.iterator();
        }
    }
}
