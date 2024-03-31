// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.util.NoSuchElementException;
import java.io.Serializable;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import java.util.ListIterator;
import java.util.Iterator;

public class IntIterators
{
    public static final EmptyIterator EMPTY_ITERATOR;
    
    private IntIterators() {
    }
    
    public static IntListIterator singleton(final int element) {
        return new SingletonIterator(element);
    }
    
    public static IntListIterator wrap(final int[] array, final int offset, final int length) {
        IntArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator(array, offset, length);
    }
    
    public static IntListIterator wrap(final int[] array) {
        return new ArrayIterator(array, 0, array.length);
    }
    
    public static int unwrap(final IntIterator i, final int[] array, int offset, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            array[offset++] = i.nextInt();
        }
        return max - j - 1;
    }
    
    public static int unwrap(final IntIterator i, final int[] array) {
        return unwrap(i, array, 0, array.length);
    }
    
    public static int[] unwrap(final IntIterator i, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int[] array = new int[16];
        int j = 0;
        while (max-- != 0 && i.hasNext()) {
            if (j == array.length) {
                array = IntArrays.grow(array, j + 1);
            }
            array[j++] = i.nextInt();
        }
        return IntArrays.trim(array, j);
    }
    
    public static int[] unwrap(final IntIterator i) {
        return unwrap(i, Integer.MAX_VALUE);
    }
    
    public static int unwrap(final IntIterator i, final IntCollection c, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            c.add(i.nextInt());
        }
        return max - j - 1;
    }
    
    public static long unwrap(final IntIterator i, final IntCollection c) {
        long n = 0L;
        while (i.hasNext()) {
            c.add(i.nextInt());
            ++n;
        }
        return n;
    }
    
    public static int pour(final IntIterator i, final IntCollection s, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            s.add(i.nextInt());
        }
        return max - j - 1;
    }
    
    public static int pour(final IntIterator i, final IntCollection s) {
        return pour(i, s, Integer.MAX_VALUE);
    }
    
    public static IntList pour(final IntIterator i, final int max) {
        final IntArrayList l = new IntArrayList();
        pour(i, l, max);
        l.trim();
        return l;
    }
    
    public static IntList pour(final IntIterator i) {
        return pour(i, Integer.MAX_VALUE);
    }
    
    public static IntIterator asIntIterator(final Iterator i) {
        if (i instanceof IntIterator) {
            return (IntIterator)i;
        }
        return new IteratorWrapper(i);
    }
    
    public static IntListIterator asIntIterator(final ListIterator i) {
        if (i instanceof IntListIterator) {
            return (IntListIterator)i;
        }
        return new ListIteratorWrapper(i);
    }
    
    public static IntListIterator fromTo(final int from, final int to) {
        return new IntervalIterator(from, to);
    }
    
    public static IntIterator concat(final IntIterator[] a) {
        return concat(a, 0, a.length);
    }
    
    public static IntIterator concat(final IntIterator[] a, final int offset, final int length) {
        return new IteratorConcatenator(a, offset, length);
    }
    
    public static IntIterator unmodifiable(final IntIterator i) {
        return new UnmodifiableIterator(i);
    }
    
    public static IntBidirectionalIterator unmodifiable(final IntBidirectionalIterator i) {
        return new UnmodifiableBidirectionalIterator(i);
    }
    
    public static IntListIterator unmodifiable(final IntListIterator i) {
        return new UnmodifiableListIterator(i);
    }
    
    public static IntIterator wrap(final ByteIterator iterator) {
        return new ByteIteratorWrapper(iterator);
    }
    
    public static IntIterator wrap(final ShortIterator iterator) {
        return new ShortIteratorWrapper(iterator);
    }
    
    static {
        EMPTY_ITERATOR = new EmptyIterator();
    }
    
    public static class EmptyIterator extends AbstractIntListIterator implements Serializable, Cloneable
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
        public int nextInt() {
            throw new NoSuchElementException();
        }
        
        @Override
        public int previousInt() {
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
            return IntIterators.EMPTY_ITERATOR;
        }
        
        private Object readResolve() {
            return IntIterators.EMPTY_ITERATOR;
        }
    }
    
    private static class SingletonIterator extends AbstractIntListIterator
    {
        private final int element;
        private int curr;
        
        public SingletonIterator(final int element) {
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
        public int nextInt() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = 1;
            return this.element;
        }
        
        @Override
        public int previousInt() {
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
    
    private static class ArrayIterator extends AbstractIntListIterator
    {
        private final int[] array;
        private final int offset;
        private final int length;
        private int curr;
        
        public ArrayIterator(final int[] array, final int offset, final int length) {
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
        public int nextInt() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + this.curr++];
        }
        
        @Override
        public int previousInt() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final int[] array = this.array;
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
    
    private static class IteratorWrapper extends AbstractIntIterator
    {
        final Iterator<Integer> i;
        
        public IteratorWrapper(final Iterator<Integer> i) {
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
        public int nextInt() {
            return this.i.next();
        }
    }
    
    private static class ListIteratorWrapper extends AbstractIntListIterator
    {
        final ListIterator<Integer> i;
        
        public ListIteratorWrapper(final ListIterator<Integer> i) {
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
        public void set(final int k) {
            this.i.set(k);
        }
        
        @Override
        public void add(final int k) {
            this.i.add(k);
        }
        
        @Override
        public void remove() {
            this.i.remove();
        }
        
        @Override
        public int nextInt() {
            return this.i.next();
        }
        
        @Override
        public int previousInt() {
            return this.i.previous();
        }
    }
    
    private static class IntervalIterator extends AbstractIntListIterator
    {
        private final int from;
        private final int to;
        int curr;
        
        public IntervalIterator(final int from, final int to) {
            this.curr = from;
            this.from = from;
            this.to = to;
        }
        
        @Override
        public boolean hasNext() {
            return this.curr < this.to;
        }
        
        @Override
        public boolean hasPrevious() {
            return this.curr > this.from;
        }
        
        @Override
        public int nextInt() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.curr++;
        }
        
        @Override
        public int previousInt() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            return --this.curr;
        }
        
        @Override
        public int nextIndex() {
            return this.curr - this.from;
        }
        
        @Override
        public int previousIndex() {
            return this.curr - this.from - 1;
        }
        
        @Override
        public int skip(int n) {
            if (this.curr + n <= this.to) {
                this.curr += n;
                return n;
            }
            n = this.to - this.curr;
            this.curr = this.to;
            return n;
        }
        
        @Override
        public int back(int n) {
            if (this.curr - n >= this.from) {
                this.curr -= n;
                return n;
            }
            n = this.curr - this.from;
            this.curr = this.from;
            return n;
        }
    }
    
    private static class IteratorConcatenator extends AbstractIntIterator
    {
        final IntIterator[] a;
        int offset;
        int length;
        int lastOffset;
        
        public IteratorConcatenator(final IntIterator[] a, final int offset, final int length) {
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
        public int nextInt() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final IntIterator[] a = this.a;
            final int offset = this.offset;
            this.lastOffset = offset;
            final int next = a[offset].nextInt();
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
    
    public static class UnmodifiableIterator extends AbstractIntIterator
    {
        protected final IntIterator i;
        
        public UnmodifiableIterator(final IntIterator i) {
            this.i = i;
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        @Override
        public int nextInt() {
            return this.i.nextInt();
        }
        
        @Deprecated
        @Override
        public Integer next() {
            return this.i.next();
        }
    }
    
    public static class UnmodifiableBidirectionalIterator extends AbstractIntBidirectionalIterator
    {
        protected final IntBidirectionalIterator i;
        
        public UnmodifiableBidirectionalIterator(final IntBidirectionalIterator i) {
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
        public int nextInt() {
            return this.i.nextInt();
        }
        
        @Override
        public int previousInt() {
            return this.i.previousInt();
        }
        
        @Deprecated
        @Override
        public Integer next() {
            return this.i.next();
        }
        
        @Deprecated
        @Override
        public Integer previous() {
            return this.i.previous();
        }
    }
    
    public static class UnmodifiableListIterator extends AbstractIntListIterator
    {
        protected final IntListIterator i;
        
        public UnmodifiableListIterator(final IntListIterator i) {
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
        public int nextInt() {
            return this.i.nextInt();
        }
        
        @Override
        public int previousInt() {
            return this.i.previousInt();
        }
        
        @Override
        public int nextIndex() {
            return this.i.nextIndex();
        }
        
        @Override
        public int previousIndex() {
            return this.i.previousIndex();
        }
        
        @Deprecated
        @Override
        public Integer next() {
            return this.i.next();
        }
        
        @Deprecated
        @Override
        public Integer previous() {
            return this.i.previous();
        }
    }
    
    protected static class ByteIteratorWrapper implements IntIterator
    {
        final ByteIterator iterator;
        
        public ByteIteratorWrapper(final ByteIterator iterator) {
            this.iterator = iterator;
        }
        
        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }
        
        @Override
        public Integer next() {
            return (int)this.iterator.nextByte();
        }
        
        @Override
        public int nextInt() {
            return this.iterator.nextByte();
        }
        
        @Override
        public void remove() {
            this.iterator.remove();
        }
        
        @Override
        public int skip(final int n) {
            return this.iterator.skip(n);
        }
    }
    
    protected static class ShortIteratorWrapper implements IntIterator
    {
        final ShortIterator iterator;
        
        public ShortIteratorWrapper(final ShortIterator iterator) {
            this.iterator = iterator;
        }
        
        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }
        
        @Override
        public Integer next() {
            return (int)this.iterator.nextShort();
        }
        
        @Override
        public int nextInt() {
            return this.iterator.nextShort();
        }
        
        @Override
        public void remove() {
            this.iterator.remove();
        }
        
        @Override
        public int skip(final int n) {
            return this.iterator.skip(n);
        }
    }
}
