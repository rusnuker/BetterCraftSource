// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.util.NoSuchElementException;
import java.io.Serializable;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import java.util.ListIterator;
import java.util.Iterator;

public class ShortIterators
{
    public static final EmptyIterator EMPTY_ITERATOR;
    
    private ShortIterators() {
    }
    
    public static ShortListIterator singleton(final short element) {
        return new SingletonIterator(element);
    }
    
    public static ShortListIterator wrap(final short[] array, final int offset, final int length) {
        ShortArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator(array, offset, length);
    }
    
    public static ShortListIterator wrap(final short[] array) {
        return new ArrayIterator(array, 0, array.length);
    }
    
    public static int unwrap(final ShortIterator i, final short[] array, int offset, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            array[offset++] = i.nextShort();
        }
        return max - j - 1;
    }
    
    public static int unwrap(final ShortIterator i, final short[] array) {
        return unwrap(i, array, 0, array.length);
    }
    
    public static short[] unwrap(final ShortIterator i, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        short[] array = new short[16];
        int j = 0;
        while (max-- != 0 && i.hasNext()) {
            if (j == array.length) {
                array = ShortArrays.grow(array, j + 1);
            }
            array[j++] = i.nextShort();
        }
        return ShortArrays.trim(array, j);
    }
    
    public static short[] unwrap(final ShortIterator i) {
        return unwrap(i, Integer.MAX_VALUE);
    }
    
    public static int unwrap(final ShortIterator i, final ShortCollection c, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            c.add(i.nextShort());
        }
        return max - j - 1;
    }
    
    public static long unwrap(final ShortIterator i, final ShortCollection c) {
        long n = 0L;
        while (i.hasNext()) {
            c.add(i.nextShort());
            ++n;
        }
        return n;
    }
    
    public static int pour(final ShortIterator i, final ShortCollection s, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            s.add(i.nextShort());
        }
        return max - j - 1;
    }
    
    public static int pour(final ShortIterator i, final ShortCollection s) {
        return pour(i, s, Integer.MAX_VALUE);
    }
    
    public static ShortList pour(final ShortIterator i, final int max) {
        final ShortArrayList l = new ShortArrayList();
        pour(i, l, max);
        l.trim();
        return l;
    }
    
    public static ShortList pour(final ShortIterator i) {
        return pour(i, Integer.MAX_VALUE);
    }
    
    public static ShortIterator asShortIterator(final Iterator i) {
        if (i instanceof ShortIterator) {
            return (ShortIterator)i;
        }
        return new IteratorWrapper(i);
    }
    
    public static ShortListIterator asShortIterator(final ListIterator i) {
        if (i instanceof ShortListIterator) {
            return (ShortListIterator)i;
        }
        return new ListIteratorWrapper(i);
    }
    
    public static ShortListIterator fromTo(final short from, final short to) {
        return new IntervalIterator(from, to);
    }
    
    public static ShortIterator concat(final ShortIterator[] a) {
        return concat(a, 0, a.length);
    }
    
    public static ShortIterator concat(final ShortIterator[] a, final int offset, final int length) {
        return new IteratorConcatenator(a, offset, length);
    }
    
    public static ShortIterator unmodifiable(final ShortIterator i) {
        return new UnmodifiableIterator(i);
    }
    
    public static ShortBidirectionalIterator unmodifiable(final ShortBidirectionalIterator i) {
        return new UnmodifiableBidirectionalIterator(i);
    }
    
    public static ShortListIterator unmodifiable(final ShortListIterator i) {
        return new UnmodifiableListIterator(i);
    }
    
    public static ShortIterator wrap(final ByteIterator iterator) {
        return new ByteIteratorWrapper(iterator);
    }
    
    static {
        EMPTY_ITERATOR = new EmptyIterator();
    }
    
    public static class EmptyIterator extends AbstractShortListIterator implements Serializable, Cloneable
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
        public short nextShort() {
            throw new NoSuchElementException();
        }
        
        @Override
        public short previousShort() {
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
            return ShortIterators.EMPTY_ITERATOR;
        }
        
        private Object readResolve() {
            return ShortIterators.EMPTY_ITERATOR;
        }
    }
    
    private static class SingletonIterator extends AbstractShortListIterator
    {
        private final short element;
        private int curr;
        
        public SingletonIterator(final short element) {
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
        public short nextShort() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = 1;
            return this.element;
        }
        
        @Override
        public short previousShort() {
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
    
    private static class ArrayIterator extends AbstractShortListIterator
    {
        private final short[] array;
        private final int offset;
        private final int length;
        private int curr;
        
        public ArrayIterator(final short[] array, final int offset, final int length) {
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
        public short nextShort() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + this.curr++];
        }
        
        @Override
        public short previousShort() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final short[] array = this.array;
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
    
    private static class IteratorWrapper extends AbstractShortIterator
    {
        final Iterator<Short> i;
        
        public IteratorWrapper(final Iterator<Short> i) {
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
        public short nextShort() {
            return this.i.next();
        }
    }
    
    private static class ListIteratorWrapper extends AbstractShortListIterator
    {
        final ListIterator<Short> i;
        
        public ListIteratorWrapper(final ListIterator<Short> i) {
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
        public void set(final short k) {
            this.i.set(k);
        }
        
        @Override
        public void add(final short k) {
            this.i.add(k);
        }
        
        @Override
        public void remove() {
            this.i.remove();
        }
        
        @Override
        public short nextShort() {
            return this.i.next();
        }
        
        @Override
        public short previousShort() {
            return this.i.previous();
        }
    }
    
    private static class IntervalIterator extends AbstractShortListIterator
    {
        private final short from;
        private final short to;
        short curr;
        
        public IntervalIterator(final short from, final short to) {
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
        public short nextShort() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final short curr = this.curr;
            this.curr = (short)(curr + 1);
            return curr;
        }
        
        @Override
        public short previousShort() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            return (short)(--this.curr);
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
                this.curr += (short)n;
                return n;
            }
            n = this.to - this.curr;
            this.curr = this.to;
            return n;
        }
        
        @Override
        public int back(int n) {
            if (this.curr - n >= this.from) {
                this.curr -= (short)n;
                return n;
            }
            n = this.curr - this.from;
            this.curr = this.from;
            return n;
        }
    }
    
    private static class IteratorConcatenator extends AbstractShortIterator
    {
        final ShortIterator[] a;
        int offset;
        int length;
        int lastOffset;
        
        public IteratorConcatenator(final ShortIterator[] a, final int offset, final int length) {
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
        public short nextShort() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final ShortIterator[] a = this.a;
            final int offset = this.offset;
            this.lastOffset = offset;
            final short next = a[offset].nextShort();
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
    
    public static class UnmodifiableIterator extends AbstractShortIterator
    {
        protected final ShortIterator i;
        
        public UnmodifiableIterator(final ShortIterator i) {
            this.i = i;
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        @Override
        public short nextShort() {
            return this.i.nextShort();
        }
        
        @Deprecated
        @Override
        public Short next() {
            return this.i.next();
        }
    }
    
    public static class UnmodifiableBidirectionalIterator extends AbstractShortBidirectionalIterator
    {
        protected final ShortBidirectionalIterator i;
        
        public UnmodifiableBidirectionalIterator(final ShortBidirectionalIterator i) {
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
        public short nextShort() {
            return this.i.nextShort();
        }
        
        @Override
        public short previousShort() {
            return this.i.previousShort();
        }
        
        @Deprecated
        @Override
        public Short next() {
            return this.i.next();
        }
        
        @Deprecated
        @Override
        public Short previous() {
            return this.i.previous();
        }
    }
    
    public static class UnmodifiableListIterator extends AbstractShortListIterator
    {
        protected final ShortListIterator i;
        
        public UnmodifiableListIterator(final ShortListIterator i) {
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
        public short nextShort() {
            return this.i.nextShort();
        }
        
        @Override
        public short previousShort() {
            return this.i.previousShort();
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
        public Short next() {
            return this.i.next();
        }
        
        @Deprecated
        @Override
        public Short previous() {
            return this.i.previous();
        }
    }
    
    protected static class ByteIteratorWrapper implements ShortIterator
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
        public Short next() {
            return (short)this.iterator.nextByte();
        }
        
        @Override
        public short nextShort() {
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
}
