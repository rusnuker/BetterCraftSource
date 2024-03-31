// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.util.NoSuchElementException;
import java.io.Serializable;
import java.util.ListIterator;
import java.util.Iterator;

public class ByteIterators
{
    public static final EmptyIterator EMPTY_ITERATOR;
    
    private ByteIterators() {
    }
    
    public static ByteListIterator singleton(final byte element) {
        return new SingletonIterator(element);
    }
    
    public static ByteListIterator wrap(final byte[] array, final int offset, final int length) {
        ByteArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator(array, offset, length);
    }
    
    public static ByteListIterator wrap(final byte[] array) {
        return new ArrayIterator(array, 0, array.length);
    }
    
    public static int unwrap(final ByteIterator i, final byte[] array, int offset, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            array[offset++] = i.nextByte();
        }
        return max - j - 1;
    }
    
    public static int unwrap(final ByteIterator i, final byte[] array) {
        return unwrap(i, array, 0, array.length);
    }
    
    public static byte[] unwrap(final ByteIterator i, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        byte[] array = new byte[16];
        int j = 0;
        while (max-- != 0 && i.hasNext()) {
            if (j == array.length) {
                array = ByteArrays.grow(array, j + 1);
            }
            array[j++] = i.nextByte();
        }
        return ByteArrays.trim(array, j);
    }
    
    public static byte[] unwrap(final ByteIterator i) {
        return unwrap(i, Integer.MAX_VALUE);
    }
    
    public static int unwrap(final ByteIterator i, final ByteCollection c, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            c.add(i.nextByte());
        }
        return max - j - 1;
    }
    
    public static long unwrap(final ByteIterator i, final ByteCollection c) {
        long n = 0L;
        while (i.hasNext()) {
            c.add(i.nextByte());
            ++n;
        }
        return n;
    }
    
    public static int pour(final ByteIterator i, final ByteCollection s, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            s.add(i.nextByte());
        }
        return max - j - 1;
    }
    
    public static int pour(final ByteIterator i, final ByteCollection s) {
        return pour(i, s, Integer.MAX_VALUE);
    }
    
    public static ByteList pour(final ByteIterator i, final int max) {
        final ByteArrayList l = new ByteArrayList();
        pour(i, l, max);
        l.trim();
        return l;
    }
    
    public static ByteList pour(final ByteIterator i) {
        return pour(i, Integer.MAX_VALUE);
    }
    
    public static ByteIterator asByteIterator(final Iterator i) {
        if (i instanceof ByteIterator) {
            return (ByteIterator)i;
        }
        return new IteratorWrapper(i);
    }
    
    public static ByteListIterator asByteIterator(final ListIterator i) {
        if (i instanceof ByteListIterator) {
            return (ByteListIterator)i;
        }
        return new ListIteratorWrapper(i);
    }
    
    public static ByteListIterator fromTo(final byte from, final byte to) {
        return new IntervalIterator(from, to);
    }
    
    public static ByteIterator concat(final ByteIterator[] a) {
        return concat(a, 0, a.length);
    }
    
    public static ByteIterator concat(final ByteIterator[] a, final int offset, final int length) {
        return new IteratorConcatenator(a, offset, length);
    }
    
    public static ByteIterator unmodifiable(final ByteIterator i) {
        return new UnmodifiableIterator(i);
    }
    
    public static ByteBidirectionalIterator unmodifiable(final ByteBidirectionalIterator i) {
        return new UnmodifiableBidirectionalIterator(i);
    }
    
    public static ByteListIterator unmodifiable(final ByteListIterator i) {
        return new UnmodifiableListIterator(i);
    }
    
    static {
        EMPTY_ITERATOR = new EmptyIterator();
    }
    
    public static class EmptyIterator extends AbstractByteListIterator implements Serializable, Cloneable
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
        public byte nextByte() {
            throw new NoSuchElementException();
        }
        
        @Override
        public byte previousByte() {
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
            return ByteIterators.EMPTY_ITERATOR;
        }
        
        private Object readResolve() {
            return ByteIterators.EMPTY_ITERATOR;
        }
    }
    
    private static class SingletonIterator extends AbstractByteListIterator
    {
        private final byte element;
        private int curr;
        
        public SingletonIterator(final byte element) {
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
        public byte nextByte() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = 1;
            return this.element;
        }
        
        @Override
        public byte previousByte() {
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
    
    private static class ArrayIterator extends AbstractByteListIterator
    {
        private final byte[] array;
        private final int offset;
        private final int length;
        private int curr;
        
        public ArrayIterator(final byte[] array, final int offset, final int length) {
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
        public byte nextByte() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + this.curr++];
        }
        
        @Override
        public byte previousByte() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final byte[] array = this.array;
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
    
    private static class IteratorWrapper extends AbstractByteIterator
    {
        final Iterator<Byte> i;
        
        public IteratorWrapper(final Iterator<Byte> i) {
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
        public byte nextByte() {
            return this.i.next();
        }
    }
    
    private static class ListIteratorWrapper extends AbstractByteListIterator
    {
        final ListIterator<Byte> i;
        
        public ListIteratorWrapper(final ListIterator<Byte> i) {
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
        public void set(final byte k) {
            this.i.set(k);
        }
        
        @Override
        public void add(final byte k) {
            this.i.add(k);
        }
        
        @Override
        public void remove() {
            this.i.remove();
        }
        
        @Override
        public byte nextByte() {
            return this.i.next();
        }
        
        @Override
        public byte previousByte() {
            return this.i.previous();
        }
    }
    
    private static class IntervalIterator extends AbstractByteListIterator
    {
        private final byte from;
        private final byte to;
        byte curr;
        
        public IntervalIterator(final byte from, final byte to) {
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
        public byte nextByte() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final byte curr = this.curr;
            this.curr = (byte)(curr + 1);
            return curr;
        }
        
        @Override
        public byte previousByte() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            return (byte)(--this.curr);
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
                this.curr += (byte)n;
                return n;
            }
            n = this.to - this.curr;
            this.curr = this.to;
            return n;
        }
        
        @Override
        public int back(int n) {
            if (this.curr - n >= this.from) {
                this.curr -= (byte)n;
                return n;
            }
            n = this.curr - this.from;
            this.curr = this.from;
            return n;
        }
    }
    
    private static class IteratorConcatenator extends AbstractByteIterator
    {
        final ByteIterator[] a;
        int offset;
        int length;
        int lastOffset;
        
        public IteratorConcatenator(final ByteIterator[] a, final int offset, final int length) {
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
        public byte nextByte() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final ByteIterator[] a = this.a;
            final int offset = this.offset;
            this.lastOffset = offset;
            final byte next = a[offset].nextByte();
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
    
    public static class UnmodifiableIterator extends AbstractByteIterator
    {
        protected final ByteIterator i;
        
        public UnmodifiableIterator(final ByteIterator i) {
            this.i = i;
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        @Override
        public byte nextByte() {
            return this.i.nextByte();
        }
        
        @Deprecated
        @Override
        public Byte next() {
            return this.i.next();
        }
    }
    
    public static class UnmodifiableBidirectionalIterator extends AbstractByteBidirectionalIterator
    {
        protected final ByteBidirectionalIterator i;
        
        public UnmodifiableBidirectionalIterator(final ByteBidirectionalIterator i) {
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
        public byte nextByte() {
            return this.i.nextByte();
        }
        
        @Override
        public byte previousByte() {
            return this.i.previousByte();
        }
        
        @Deprecated
        @Override
        public Byte next() {
            return this.i.next();
        }
        
        @Deprecated
        @Override
        public Byte previous() {
            return this.i.previous();
        }
    }
    
    public static class UnmodifiableListIterator extends AbstractByteListIterator
    {
        protected final ByteListIterator i;
        
        public UnmodifiableListIterator(final ByteListIterator i) {
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
        public byte nextByte() {
            return this.i.nextByte();
        }
        
        @Override
        public byte previousByte() {
            return this.i.previousByte();
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
        public Byte next() {
            return this.i.next();
        }
        
        @Deprecated
        @Override
        public Byte previous() {
            return this.i.previous();
        }
    }
}
