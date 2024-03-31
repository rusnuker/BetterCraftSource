// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectArrays;

public class CharCollections
{
    private CharCollections() {
    }
    
    public static CharCollection synchronize(final CharCollection c) {
        return new SynchronizedCollection(c);
    }
    
    public static CharCollection synchronize(final CharCollection c, final Object sync) {
        return new SynchronizedCollection(c, sync);
    }
    
    public static CharCollection unmodifiable(final CharCollection c) {
        return new UnmodifiableCollection(c);
    }
    
    public static CharCollection asCollection(final CharIterable iterable) {
        if (iterable instanceof CharCollection) {
            return (CharCollection)iterable;
        }
        return new IterableCollection(iterable);
    }
    
    public abstract static class EmptyCollection extends AbstractCharCollection
    {
        protected EmptyCollection() {
        }
        
        @Override
        public boolean add(final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final char k) {
            return false;
        }
        
        @Override
        public Object[] toArray() {
            return ObjectArrays.EMPTY_ARRAY;
        }
        
        @Override
        public char[] toCharArray(final char[] a) {
            return a;
        }
        
        @Override
        public char[] toCharArray() {
            return CharArrays.EMPTY_ARRAY;
        }
        
        @Override
        public boolean rem(final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final CharCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final CharCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final CharCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean containsAll(final CharCollection c) {
            return c.isEmpty();
        }
        
        @Override
        public CharBidirectionalIterator iterator() {
            return CharIterators.EMPTY_ITERATOR;
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
    
    public static class SynchronizedCollection implements CharCollection, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final CharCollection collection;
        protected final Object sync;
        
        protected SynchronizedCollection(final CharCollection c, final Object sync) {
            if (c == null) {
                throw new NullPointerException();
            }
            this.collection = c;
            this.sync = sync;
        }
        
        protected SynchronizedCollection(final CharCollection c) {
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
        public boolean contains(final char o) {
            synchronized (this.sync) {
                return this.collection.contains(o);
            }
        }
        
        @Override
        public char[] toCharArray() {
            synchronized (this.sync) {
                return this.collection.toCharArray();
            }
        }
        
        @Override
        public Object[] toArray() {
            synchronized (this.sync) {
                return this.collection.toArray();
            }
        }
        
        @Override
        public char[] toCharArray(final char[] a) {
            synchronized (this.sync) {
                return this.collection.toCharArray(a);
            }
        }
        
        @Override
        public char[] toArray(final char[] a) {
            synchronized (this.sync) {
                return this.collection.toCharArray(a);
            }
        }
        
        @Override
        public boolean addAll(final CharCollection c) {
            synchronized (this.sync) {
                return this.collection.addAll(c);
            }
        }
        
        @Override
        public boolean containsAll(final CharCollection c) {
            synchronized (this.sync) {
                return this.collection.containsAll(c);
            }
        }
        
        @Override
        public boolean removeAll(final CharCollection c) {
            synchronized (this.sync) {
                return this.collection.removeAll(c);
            }
        }
        
        @Override
        public boolean retainAll(final CharCollection c) {
            synchronized (this.sync) {
                return this.collection.retainAll(c);
            }
        }
        
        @Override
        public boolean add(final Character k) {
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
        public CharIterator iterator() {
            return this.collection.iterator();
        }
        
        @Deprecated
        @Override
        public CharIterator charIterator() {
            return this.iterator();
        }
        
        @Override
        public boolean add(final char k) {
            synchronized (this.sync) {
                return this.collection.add(k);
            }
        }
        
        @Override
        public boolean rem(final char k) {
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
        public boolean addAll(final Collection<? extends Character> c) {
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
    
    public static class UnmodifiableCollection implements CharCollection, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final CharCollection collection;
        
        protected UnmodifiableCollection(final CharCollection c) {
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
        public boolean contains(final char o) {
            return this.collection.contains(o);
        }
        
        @Override
        public CharIterator iterator() {
            return CharIterators.unmodifiable(this.collection.iterator());
        }
        
        @Deprecated
        @Override
        public CharIterator charIterator() {
            return this.iterator();
        }
        
        @Override
        public boolean add(final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final Object ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends Character> c) {
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
        public char[] toCharArray() {
            return this.collection.toCharArray();
        }
        
        @Override
        public char[] toCharArray(final char[] a) {
            return this.collection.toCharArray(a);
        }
        
        @Override
        public char[] toArray(final char[] a) {
            return this.collection.toArray(a);
        }
        
        @Override
        public boolean rem(final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final CharCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean containsAll(final CharCollection c) {
            return this.collection.containsAll(c);
        }
        
        @Override
        public boolean removeAll(final CharCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final CharCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final Character k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final Object k) {
            return this.collection.contains(k);
        }
    }
    
    public static class IterableCollection extends AbstractCharCollection implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final CharIterable iterable;
        
        protected IterableCollection(final CharIterable iterable) {
            if (iterable == null) {
                throw new NullPointerException();
            }
            this.iterable = iterable;
        }
        
        @Override
        public int size() {
            int c = 0;
            final CharIterator iterator = this.iterator();
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
        public CharIterator iterator() {
            return this.iterable.iterator();
        }
        
        @Deprecated
        @Override
        public CharIterator charIterator() {
            return this.iterator();
        }
    }
}
