// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.util.Iterator;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.NoSuchElementException;
import java.io.Serializable;

public class ByteSortedSets
{
    public static final EmptySet EMPTY_SET;
    
    private ByteSortedSets() {
    }
    
    public static ByteSortedSet singleton(final byte element) {
        return new Singleton(element);
    }
    
    public static ByteSortedSet singleton(final byte element, final ByteComparator comparator) {
        return new Singleton(element, comparator);
    }
    
    public static ByteSortedSet singleton(final Object element) {
        return new Singleton((byte)element);
    }
    
    public static ByteSortedSet singleton(final Object element, final ByteComparator comparator) {
        return new Singleton((byte)element, comparator);
    }
    
    public static ByteSortedSet synchronize(final ByteSortedSet s) {
        return new SynchronizedSortedSet(s);
    }
    
    public static ByteSortedSet synchronize(final ByteSortedSet s, final Object sync) {
        return new SynchronizedSortedSet(s, sync);
    }
    
    public static ByteSortedSet unmodifiable(final ByteSortedSet s) {
        return new UnmodifiableSortedSet(s);
    }
    
    static {
        EMPTY_SET = new EmptySet();
    }
    
    public static class EmptySet extends ByteSets.EmptySet implements ByteSortedSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public boolean rem(final byte ok) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public ByteBidirectionalIterator byteIterator() {
            return this.iterator();
        }
        
        @Override
        public ByteBidirectionalIterator iterator(final byte from) {
            return ByteIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public ByteSortedSet subSet(final byte from, final byte to) {
            return ByteSortedSets.EMPTY_SET;
        }
        
        @Override
        public ByteSortedSet headSet(final byte from) {
            return ByteSortedSets.EMPTY_SET;
        }
        
        @Override
        public ByteSortedSet tailSet(final byte to) {
            return ByteSortedSets.EMPTY_SET;
        }
        
        @Override
        public byte firstByte() {
            throw new NoSuchElementException();
        }
        
        @Override
        public byte lastByte() {
            throw new NoSuchElementException();
        }
        
        @Override
        public ByteComparator comparator() {
            return null;
        }
        
        @Override
        public ByteSortedSet subSet(final Byte from, final Byte to) {
            return ByteSortedSets.EMPTY_SET;
        }
        
        @Override
        public ByteSortedSet headSet(final Byte from) {
            return ByteSortedSets.EMPTY_SET;
        }
        
        @Override
        public ByteSortedSet tailSet(final Byte to) {
            return ByteSortedSets.EMPTY_SET;
        }
        
        @Override
        public Byte first() {
            throw new NoSuchElementException();
        }
        
        @Override
        public Byte last() {
            throw new NoSuchElementException();
        }
        
        @Override
        public Object clone() {
            return ByteSortedSets.EMPTY_SET;
        }
        
        private Object readResolve() {
            return ByteSortedSets.EMPTY_SET;
        }
    }
    
    public static class Singleton extends ByteSets.Singleton implements ByteSortedSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        final ByteComparator comparator;
        
        private Singleton(final byte element, final ByteComparator comparator) {
            super(element);
            this.comparator = comparator;
        }
        
        private Singleton(final byte element) {
            this(element, (ByteComparator)null);
        }
        
        final int compare(final byte k1, final byte k2) {
            return (this.comparator == null) ? Byte.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Deprecated
        @Override
        public ByteBidirectionalIterator byteIterator() {
            return this.iterator();
        }
        
        @Override
        public ByteBidirectionalIterator iterator(final byte from) {
            final ByteBidirectionalIterator i = this.iterator();
            if (this.compare(this.element, from) <= 0) {
                i.next();
            }
            return i;
        }
        
        @Override
        public ByteComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public ByteSortedSet subSet(final byte from, final byte to) {
            if (this.compare(from, this.element) <= 0 && this.compare(this.element, to) < 0) {
                return this;
            }
            return ByteSortedSets.EMPTY_SET;
        }
        
        @Override
        public ByteSortedSet headSet(final byte to) {
            if (this.compare(this.element, to) < 0) {
                return this;
            }
            return ByteSortedSets.EMPTY_SET;
        }
        
        @Override
        public ByteSortedSet tailSet(final byte from) {
            if (this.compare(from, this.element) <= 0) {
                return this;
            }
            return ByteSortedSets.EMPTY_SET;
        }
        
        @Override
        public byte firstByte() {
            return this.element;
        }
        
        @Override
        public byte lastByte() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public Byte first() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public Byte last() {
            return this.element;
        }
        
        @Deprecated
        @Override
        public ByteSortedSet subSet(final Byte from, final Byte to) {
            return this.subSet((byte)from, (byte)to);
        }
        
        @Deprecated
        @Override
        public ByteSortedSet headSet(final Byte to) {
            return this.headSet((byte)to);
        }
        
        @Deprecated
        @Override
        public ByteSortedSet tailSet(final Byte from) {
            return this.tailSet((byte)from);
        }
    }
    
    public static class SynchronizedSortedSet extends ByteSets.SynchronizedSet implements ByteSortedSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ByteSortedSet sortedSet;
        
        protected SynchronizedSortedSet(final ByteSortedSet s, final Object sync) {
            super(s, sync);
            this.sortedSet = s;
        }
        
        protected SynchronizedSortedSet(final ByteSortedSet s) {
            super(s);
            this.sortedSet = s;
        }
        
        @Override
        public ByteComparator comparator() {
            synchronized (this.sync) {
                return this.sortedSet.comparator();
            }
        }
        
        @Override
        public ByteSortedSet subSet(final byte from, final byte to) {
            return new SynchronizedSortedSet(this.sortedSet.subSet(from, to), this.sync);
        }
        
        @Override
        public ByteSortedSet headSet(final byte to) {
            return new SynchronizedSortedSet(this.sortedSet.headSet(to), this.sync);
        }
        
        @Override
        public ByteSortedSet tailSet(final byte from) {
            return new SynchronizedSortedSet(this.sortedSet.tailSet(from), this.sync);
        }
        
        @Override
        public ByteBidirectionalIterator iterator() {
            return this.sortedSet.iterator();
        }
        
        @Override
        public ByteBidirectionalIterator iterator(final byte from) {
            return this.sortedSet.iterator(from);
        }
        
        @Deprecated
        @Override
        public ByteBidirectionalIterator byteIterator() {
            return this.sortedSet.iterator();
        }
        
        @Override
        public byte firstByte() {
            synchronized (this.sync) {
                return this.sortedSet.firstByte();
            }
        }
        
        @Override
        public byte lastByte() {
            synchronized (this.sync) {
                return this.sortedSet.lastByte();
            }
        }
        
        @Override
        public Byte first() {
            synchronized (this.sync) {
                return this.sortedSet.first();
            }
        }
        
        @Override
        public Byte last() {
            synchronized (this.sync) {
                return this.sortedSet.last();
            }
        }
        
        @Override
        public ByteSortedSet subSet(final Byte from, final Byte to) {
            return new SynchronizedSortedSet(this.sortedSet.subSet(from, to), this.sync);
        }
        
        @Override
        public ByteSortedSet headSet(final Byte to) {
            return new SynchronizedSortedSet(this.sortedSet.headSet(to), this.sync);
        }
        
        @Override
        public ByteSortedSet tailSet(final Byte from) {
            return new SynchronizedSortedSet(this.sortedSet.tailSet(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedSet extends ByteSets.UnmodifiableSet implements ByteSortedSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ByteSortedSet sortedSet;
        
        protected UnmodifiableSortedSet(final ByteSortedSet s) {
            super(s);
            this.sortedSet = s;
        }
        
        @Override
        public ByteComparator comparator() {
            return this.sortedSet.comparator();
        }
        
        @Override
        public ByteSortedSet subSet(final byte from, final byte to) {
            return new UnmodifiableSortedSet(this.sortedSet.subSet(from, to));
        }
        
        @Override
        public ByteSortedSet headSet(final byte to) {
            return new UnmodifiableSortedSet(this.sortedSet.headSet(to));
        }
        
        @Override
        public ByteSortedSet tailSet(final byte from) {
            return new UnmodifiableSortedSet(this.sortedSet.tailSet(from));
        }
        
        @Override
        public ByteBidirectionalIterator iterator() {
            return ByteIterators.unmodifiable(this.sortedSet.iterator());
        }
        
        @Override
        public ByteBidirectionalIterator iterator(final byte from) {
            return ByteIterators.unmodifiable(this.sortedSet.iterator(from));
        }
        
        @Deprecated
        @Override
        public ByteBidirectionalIterator byteIterator() {
            return this.iterator();
        }
        
        @Override
        public byte firstByte() {
            return this.sortedSet.firstByte();
        }
        
        @Override
        public byte lastByte() {
            return this.sortedSet.lastByte();
        }
        
        @Override
        public Byte first() {
            return this.sortedSet.first();
        }
        
        @Override
        public Byte last() {
            return this.sortedSet.last();
        }
        
        @Override
        public ByteSortedSet subSet(final Byte from, final Byte to) {
            return new UnmodifiableSortedSet(this.sortedSet.subSet(from, to));
        }
        
        @Override
        public ByteSortedSet headSet(final Byte to) {
            return new UnmodifiableSortedSet(this.sortedSet.headSet(to));
        }
        
        @Override
        public ByteSortedSet tailSet(final Byte from) {
            return new UnmodifiableSortedSet(this.sortedSet.tailSet(from));
        }
    }
}
