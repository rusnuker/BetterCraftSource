// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.util.NoSuchElementException;
import java.io.Serializable;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import java.util.ListIterator;
import java.util.Iterator;

public class LongIterators
{
    public static final EmptyIterator EMPTY_ITERATOR;
    
    private LongIterators() {
    }
    
    public static LongListIterator singleton(final long element) {
        return new SingletonIterator(element);
    }
    
    public static LongListIterator wrap(final long[] array, final int offset, final int length) {
        LongArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator(array, offset, length);
    }
    
    public static LongListIterator wrap(final long[] array) {
        return new ArrayIterator(array, 0, array.length);
    }
    
    public static int unwrap(final LongIterator i, final long[] array, int offset, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            array[offset++] = i.nextLong();
        }
        return max - j - 1;
    }
    
    public static int unwrap(final LongIterator i, final long[] array) {
        return unwrap(i, array, 0, array.length);
    }
    
    public static long[] unwrap(final LongIterator i, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        long[] array = new long[16];
        int j = 0;
        while (max-- != 0 && i.hasNext()) {
            if (j == array.length) {
                array = LongArrays.grow(array, j + 1);
            }
            array[j++] = i.nextLong();
        }
        return LongArrays.trim(array, j);
    }
    
    public static long[] unwrap(final LongIterator i) {
        return unwrap(i, Integer.MAX_VALUE);
    }
    
    public static int unwrap(final LongIterator i, final LongCollection c, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            c.add(i.nextLong());
        }
        return max - j - 1;
    }
    
    public static long unwrap(final LongIterator i, final LongCollection c) {
        long n = 0L;
        while (i.hasNext()) {
            c.add(i.nextLong());
            ++n;
        }
        return n;
    }
    
    public static int pour(final LongIterator i, final LongCollection s, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            s.add(i.nextLong());
        }
        return max - j - 1;
    }
    
    public static int pour(final LongIterator i, final LongCollection s) {
        return pour(i, s, Integer.MAX_VALUE);
    }
    
    public static LongList pour(final LongIterator i, final int max) {
        final LongArrayList l = new LongArrayList();
        pour(i, l, max);
        l.trim();
        return l;
    }
    
    public static LongList pour(final LongIterator i) {
        return pour(i, Integer.MAX_VALUE);
    }
    
    public static LongIterator asLongIterator(final Iterator i) {
        if (i instanceof LongIterator) {
            return (LongIterator)i;
        }
        return new IteratorWrapper(i);
    }
    
    public static LongListIterator asLongIterator(final ListIterator i) {
        if (i instanceof LongListIterator) {
            return (LongListIterator)i;
        }
        return new ListIteratorWrapper(i);
    }
    
    public static LongBidirectionalIterator fromTo(final long from, final long to) {
        return new IntervalIterator(from, to);
    }
    
    public static LongIterator concat(final LongIterator[] a) {
        return concat(a, 0, a.length);
    }
    
    public static LongIterator concat(final LongIterator[] a, final int offset, final int length) {
        return new IteratorConcatenator(a, offset, length);
    }
    
    public static LongIterator unmodifiable(final LongIterator i) {
        return new UnmodifiableIterator(i);
    }
    
    public static LongBidirectionalIterator unmodifiable(final LongBidirectionalIterator i) {
        return new UnmodifiableBidirectionalIterator(i);
    }
    
    public static LongListIterator unmodifiable(final LongListIterator i) {
        return new UnmodifiableListIterator(i);
    }
    
    public static LongIterator wrap(final ByteIterator iterator) {
        return new ByteIteratorWrapper(iterator);
    }
    
    public static LongIterator wrap(final ShortIterator iterator) {
        return new ShortIteratorWrapper(iterator);
    }
    
    public static LongIterator wrap(final IntIterator iterator) {
        return new IntIteratorWrapper(iterator);
    }
    
    static {
        EMPTY_ITERATOR = new EmptyIterator();
    }
    
    public static class EmptyIterator extends AbstractLongListIterator implements Serializable, Cloneable
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
        public long nextLong() {
            throw new NoSuchElementException();
        }
        
        @Override
        public long previousLong() {
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
            return LongIterators.EMPTY_ITERATOR;
        }
        
        private Object readResolve() {
            return LongIterators.EMPTY_ITERATOR;
        }
    }
    
    private static class SingletonIterator extends AbstractLongListIterator
    {
        private final long element;
        private int curr;
        
        public SingletonIterator(final long element) {
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
        public long nextLong() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = 1;
            return this.element;
        }
        
        @Override
        public long previousLong() {
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
    
    private static class ArrayIterator extends AbstractLongListIterator
    {
        private final long[] array;
        private final int offset;
        private final int length;
        private int curr;
        
        public ArrayIterator(final long[] array, final int offset, final int length) {
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
        public long nextLong() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + this.curr++];
        }
        
        @Override
        public long previousLong() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final long[] array = this.array;
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
    
    private static class IteratorWrapper extends AbstractLongIterator
    {
        final Iterator<Long> i;
        
        public IteratorWrapper(final Iterator<Long> i) {
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
        public long nextLong() {
            return this.i.next();
        }
    }
    
    private static class ListIteratorWrapper extends AbstractLongListIterator
    {
        final ListIterator<Long> i;
        
        public ListIteratorWrapper(final ListIterator<Long> i) {
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
        public void set(final long k) {
            this.i.set(k);
        }
        
        @Override
        public void add(final long k) {
            this.i.add(k);
        }
        
        @Override
        public void remove() {
            this.i.remove();
        }
        
        @Override
        public long nextLong() {
            return this.i.next();
        }
        
        @Override
        public long previousLong() {
            return this.i.previous();
        }
    }
    
    private static class IntervalIterator extends AbstractLongBidirectionalIterator
    {
        private final long from;
        private final long to;
        long curr;
        
        public IntervalIterator(final long from, final long to) {
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
        public long nextLong() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.curr++;
        }
        
        @Override
        public long previousLong() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            return --this.curr;
        }
        
        @Override
        public int skip(int n) {
            if (this.curr + n <= this.to) {
                this.curr += n;
                return n;
            }
            n = (int)(this.to - this.curr);
            this.curr = this.to;
            return n;
        }
        
        @Override
        public int back(int n) {
            if (this.curr - n >= this.from) {
                this.curr -= n;
                return n;
            }
            n = (int)(this.curr - this.from);
            this.curr = this.from;
            return n;
        }
    }
    
    private static class IteratorConcatenator extends AbstractLongIterator
    {
        final LongIterator[] a;
        int offset;
        int length;
        int lastOffset;
        
        public IteratorConcatenator(final LongIterator[] a, final int offset, final int length) {
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
        public long nextLong() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final LongIterator[] a = this.a;
            final int offset = this.offset;
            this.lastOffset = offset;
            final long next = a[offset].nextLong();
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
    
    public static class UnmodifiableIterator extends AbstractLongIterator
    {
        protected final LongIterator i;
        
        public UnmodifiableIterator(final LongIterator i) {
            this.i = i;
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        @Override
        public long nextLong() {
            return this.i.nextLong();
        }
        
        @Deprecated
        @Override
        public Long next() {
            return this.i.next();
        }
    }
    
    public static class UnmodifiableBidirectionalIterator extends AbstractLongBidirectionalIterator
    {
        protected final LongBidirectionalIterator i;
        
        public UnmodifiableBidirectionalIterator(final LongBidirectionalIterator i) {
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
        public long nextLong() {
            return this.i.nextLong();
        }
        
        @Override
        public long previousLong() {
            return this.i.previousLong();
        }
        
        @Deprecated
        @Override
        public Long next() {
            return this.i.next();
        }
        
        @Deprecated
        @Override
        public Long previous() {
            return this.i.previous();
        }
    }
    
    public static class UnmodifiableListIterator extends AbstractLongListIterator
    {
        protected final LongListIterator i;
        
        public UnmodifiableListIterator(final LongListIterator i) {
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
        public long nextLong() {
            return this.i.nextLong();
        }
        
        @Override
        public long previousLong() {
            return this.i.previousLong();
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
        public Long next() {
            return this.i.next();
        }
        
        @Deprecated
        @Override
        public Long previous() {
            return this.i.previous();
        }
    }
    
    protected static class ByteIteratorWrapper implements LongIterator
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
        public Long next() {
            return (long)this.iterator.nextByte();
        }
        
        @Override
        public long nextLong() {
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
    
    protected static class ShortIteratorWrapper implements LongIterator
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
        public Long next() {
            return (long)this.iterator.nextShort();
        }
        
        @Override
        public long nextLong() {
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
    
    protected static class IntIteratorWrapper implements LongIterator
    {
        final IntIterator iterator;
        
        public IntIteratorWrapper(final IntIterator iterator) {
            this.iterator = iterator;
        }
        
        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }
        
        @Override
        public Long next() {
            return (long)this.iterator.nextInt();
        }
        
        @Override
        public long nextLong() {
            return this.iterator.nextInt();
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
