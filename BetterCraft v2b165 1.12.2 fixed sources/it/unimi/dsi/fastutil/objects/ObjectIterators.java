// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.NoSuchElementException;
import java.io.Serializable;
import java.util.ListIterator;
import java.util.Iterator;

public class ObjectIterators
{
    public static final EmptyIterator EMPTY_ITERATOR;
    
    private ObjectIterators() {
    }
    
    public static <K> ObjectListIterator<K> singleton(final K element) {
        return new SingletonIterator<K>(element);
    }
    
    public static <K> ObjectListIterator<K> wrap(final K[] array, final int offset, final int length) {
        ObjectArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator<K>(array, offset, length);
    }
    
    public static <K> ObjectListIterator<K> wrap(final K[] array) {
        return new ArrayIterator<K>(array, 0, array.length);
    }
    
    public static <K> int unwrap(final Iterator<? extends K> i, final K[] array, int offset, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            array[offset++] = (K)i.next();
        }
        return max - j - 1;
    }
    
    public static <K> int unwrap(final Iterator<? extends K> i, final K[] array) {
        return unwrap(i, array, 0, array.length);
    }
    
    public static <K> K[] unwrap(final Iterator<? extends K> i, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        K[] array = (K[])new Object[16];
        int j = 0;
        while (max-- != 0 && i.hasNext()) {
            if (j == array.length) {
                array = ObjectArrays.grow(array, j + 1);
            }
            array[j++] = (K)i.next();
        }
        return ObjectArrays.trim(array, j);
    }
    
    public static <K> K[] unwrap(final Iterator<? extends K> i) {
        return unwrap(i, Integer.MAX_VALUE);
    }
    
    public static <K> int unwrap(final Iterator<K> i, final ObjectCollection<? super K> c, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            c.add(i.next());
        }
        return max - j - 1;
    }
    
    public static <K> long unwrap(final Iterator<K> i, final ObjectCollection<? super K> c) {
        long n = 0L;
        while (i.hasNext()) {
            c.add(i.next());
            ++n;
        }
        return n;
    }
    
    public static <K> int pour(final Iterator<K> i, final ObjectCollection<? super K> s, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            s.add(i.next());
        }
        return max - j - 1;
    }
    
    public static <K> int pour(final Iterator<K> i, final ObjectCollection<? super K> s) {
        return pour(i, s, Integer.MAX_VALUE);
    }
    
    public static <K> ObjectList<K> pour(final Iterator<K> i, final int max) {
        final ObjectArrayList<K> l = new ObjectArrayList<K>();
        pour(i, l, max);
        l.trim();
        return l;
    }
    
    public static <K> ObjectList<K> pour(final Iterator<K> i) {
        return pour(i, Integer.MAX_VALUE);
    }
    
    public static <K> ObjectIterator<K> asObjectIterator(final Iterator<K> i) {
        if (i instanceof ObjectIterator) {
            return (ObjectIterator)i;
        }
        return new IteratorWrapper<K>(i);
    }
    
    public static <K> ObjectListIterator<K> asObjectIterator(final ListIterator<K> i) {
        if (i instanceof ObjectListIterator) {
            return (ObjectListIterator)i;
        }
        return new ListIteratorWrapper<K>(i);
    }
    
    public static <K> ObjectIterator<K> concat(final ObjectIterator<? extends K>[] a) {
        return concat(a, 0, a.length);
    }
    
    public static <K> ObjectIterator<K> concat(final ObjectIterator<? extends K>[] a, final int offset, final int length) {
        return new IteratorConcatenator<K>(a, offset, length);
    }
    
    public static <K> ObjectIterator<K> unmodifiable(final ObjectIterator<K> i) {
        return new UnmodifiableIterator<K>(i);
    }
    
    public static <K> ObjectBidirectionalIterator<K> unmodifiable(final ObjectBidirectionalIterator<K> i) {
        return new UnmodifiableBidirectionalIterator<K>(i);
    }
    
    public static <K> ObjectListIterator<K> unmodifiable(final ObjectListIterator<K> i) {
        return new UnmodifiableListIterator<K>(i);
    }
    
    static {
        EMPTY_ITERATOR = new EmptyIterator();
    }
    
    public static class EmptyIterator<K> extends AbstractObjectListIterator<K> implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyIterator() {
        }
        
        @Override
        public boolean hasNext() {
            return false;
        }
        
        @Override
        public boolean hasPrevious() {
            return false;
        }
        
        @Override
        public K next() {
            throw new NoSuchElementException();
        }
        
        @Override
        public K previous() {
            throw new NoSuchElementException();
        }
        
        @Override
        public int nextIndex() {
            return 0;
        }
        
        @Override
        public int previousIndex() {
            return -1;
        }
        
        @Override
        public int skip(final int n) {
            return 0;
        }
        
        @Override
        public int back(final int n) {
            return 0;
        }
        
        public Object clone() {
            return ObjectIterators.EMPTY_ITERATOR;
        }
        
        private Object readResolve() {
            return ObjectIterators.EMPTY_ITERATOR;
        }
    }
    
    private static class SingletonIterator<K> extends AbstractObjectListIterator<K>
    {
        private final K element;
        private int curr;
        
        public SingletonIterator(final K element) {
            this.element = element;
        }
        
        @Override
        public boolean hasNext() {
            return this.curr == 0;
        }
        
        @Override
        public boolean hasPrevious() {
            return this.curr == 1;
        }
        
        @Override
        public K next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = 1;
            return this.element;
        }
        
        @Override
        public K previous() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = 0;
            return this.element;
        }
        
        @Override
        public int nextIndex() {
            return this.curr;
        }
        
        @Override
        public int previousIndex() {
            return this.curr - 1;
        }
    }
    
    private static class ArrayIterator<K> extends AbstractObjectListIterator<K>
    {
        private final K[] array;
        private final int offset;
        private final int length;
        private int curr;
        
        public ArrayIterator(final K[] array, final int offset, final int length) {
            this.array = array;
            this.offset = offset;
            this.length = length;
        }
        
        @Override
        public boolean hasNext() {
            return this.curr < this.length;
        }
        
        @Override
        public boolean hasPrevious() {
            return this.curr > 0;
        }
        
        @Override
        public K next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + this.curr++];
        }
        
        @Override
        public K previous() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final K[] array = this.array;
            final int offset = this.offset;
            final int curr = this.curr - 1;
            this.curr = curr;
            return array[offset + curr];
        }
        
        @Override
        public int skip(int n) {
            if (n <= this.length - this.curr) {
                this.curr += n;
                return n;
            }
            n = this.length - this.curr;
            this.curr = this.length;
            return n;
        }
        
        @Override
        public int back(int n) {
            if (n <= this.curr) {
                this.curr -= n;
                return n;
            }
            n = this.curr;
            this.curr = 0;
            return n;
        }
        
        @Override
        public int nextIndex() {
            return this.curr;
        }
        
        @Override
        public int previousIndex() {
            return this.curr - 1;
        }
    }
    
    private static class IteratorWrapper<K> extends AbstractObjectIterator<K>
    {
        final Iterator<K> i;
        
        public IteratorWrapper(final Iterator<K> i) {
            this.i = i;
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        @Override
        public void remove() {
            this.i.remove();
        }
        
        @Override
        public K next() {
            return this.i.next();
        }
    }
    
    private static class ListIteratorWrapper<K> extends AbstractObjectListIterator<K>
    {
        final ListIterator<K> i;
        
        public ListIteratorWrapper(final ListIterator<K> i) {
            this.i = i;
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        @Override
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
        
        @Override
        public int nextIndex() {
            return this.i.nextIndex();
        }
        
        @Override
        public int previousIndex() {
            return this.i.previousIndex();
        }
        
        @Override
        public void set(final K k) {
            this.i.set(k);
        }
        
        @Override
        public void add(final K k) {
            this.i.add(k);
        }
        
        @Override
        public void remove() {
            this.i.remove();
        }
        
        @Override
        public K next() {
            return this.i.next();
        }
        
        @Override
        public K previous() {
            return this.i.previous();
        }
    }
    
    private static class IteratorConcatenator<K> extends AbstractObjectIterator<K>
    {
        final ObjectIterator<? extends K>[] a;
        int offset;
        int length;
        int lastOffset;
        
        public IteratorConcatenator(final ObjectIterator<? extends K>[] a, final int offset, final int length) {
            this.lastOffset = -1;
            this.a = a;
            this.offset = offset;
            this.length = length;
            this.advance();
        }
        
        private void advance() {
            while (this.length != 0 && !this.a[this.offset].hasNext()) {
                --this.length;
                ++this.offset;
            }
        }
        
        @Override
        public boolean hasNext() {
            return this.length > 0;
        }
        
        @Override
        public K next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final ObjectIterator<? extends K>[] a = this.a;
            final int offset = this.offset;
            this.lastOffset = offset;
            final K next = (K)a[offset].next();
            this.advance();
            return next;
        }
        
        @Override
        public void remove() {
            if (this.lastOffset == -1) {
                throw new IllegalStateException();
            }
            this.a[this.lastOffset].remove();
        }
        
        @Override
        public int skip(final int n) {
            this.lastOffset = -1;
            int skipped = 0;
            while (skipped < n && this.length != 0) {
                skipped += this.a[this.offset].skip(n - skipped);
                if (this.a[this.offset].hasNext()) {
                    break;
                }
                --this.length;
                ++this.offset;
            }
            return skipped;
        }
    }
    
    public static class UnmodifiableIterator<K> extends AbstractObjectIterator<K>
    {
        protected final ObjectIterator<K> i;
        
        public UnmodifiableIterator(final ObjectIterator<K> i) {
            this.i = i;
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        @Override
        public K next() {
            return this.i.next();
        }
    }
    
    public static class UnmodifiableBidirectionalIterator<K> extends AbstractObjectBidirectionalIterator<K>
    {
        protected final ObjectBidirectionalIterator<K> i;
        
        public UnmodifiableBidirectionalIterator(final ObjectBidirectionalIterator<K> i) {
            this.i = i;
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        @Override
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
        
        @Override
        public K next() {
            return this.i.next();
        }
        
        @Override
        public K previous() {
            return this.i.previous();
        }
    }
    
    public static class UnmodifiableListIterator<K> extends AbstractObjectListIterator<K>
    {
        protected final ObjectListIterator<K> i;
        
        public UnmodifiableListIterator(final ObjectListIterator<K> i) {
            this.i = i;
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        @Override
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
        
        @Override
        public K next() {
            return this.i.next();
        }
        
        @Override
        public K previous() {
            return this.i.previous();
        }
        
        @Override
        public int nextIndex() {
            return this.i.nextIndex();
        }
        
        @Override
        public int previousIndex() {
            return this.i.previousIndex();
        }
    }
}
