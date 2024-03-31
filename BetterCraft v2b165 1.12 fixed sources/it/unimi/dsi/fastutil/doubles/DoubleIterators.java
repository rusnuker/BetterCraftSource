// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import java.util.NoSuchElementException;
import java.io.Serializable;
import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import java.util.ListIterator;
import java.util.Iterator;

public class DoubleIterators
{
    public static final EmptyIterator EMPTY_ITERATOR;
    
    private DoubleIterators() {
    }
    
    public static DoubleListIterator singleton(final double element) {
        return new SingletonIterator(element);
    }
    
    public static DoubleListIterator wrap(final double[] array, final int offset, final int length) {
        DoubleArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator(array, offset, length);
    }
    
    public static DoubleListIterator wrap(final double[] array) {
        return new ArrayIterator(array, 0, array.length);
    }
    
    public static int unwrap(final DoubleIterator i, final double[] array, int offset, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            array[offset++] = i.nextDouble();
        }
        return max - j - 1;
    }
    
    public static int unwrap(final DoubleIterator i, final double[] array) {
        return unwrap(i, array, 0, array.length);
    }
    
    public static double[] unwrap(final DoubleIterator i, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        double[] array = new double[16];
        int j = 0;
        while (max-- != 0 && i.hasNext()) {
            if (j == array.length) {
                array = DoubleArrays.grow(array, j + 1);
            }
            array[j++] = i.nextDouble();
        }
        return DoubleArrays.trim(array, j);
    }
    
    public static double[] unwrap(final DoubleIterator i) {
        return unwrap(i, Integer.MAX_VALUE);
    }
    
    public static int unwrap(final DoubleIterator i, final DoubleCollection c, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            c.add(i.nextDouble());
        }
        return max - j - 1;
    }
    
    public static long unwrap(final DoubleIterator i, final DoubleCollection c) {
        long n = 0L;
        while (i.hasNext()) {
            c.add(i.nextDouble());
            ++n;
        }
        return n;
    }
    
    public static int pour(final DoubleIterator i, final DoubleCollection s, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            s.add(i.nextDouble());
        }
        return max - j - 1;
    }
    
    public static int pour(final DoubleIterator i, final DoubleCollection s) {
        return pour(i, s, Integer.MAX_VALUE);
    }
    
    public static DoubleList pour(final DoubleIterator i, final int max) {
        final DoubleArrayList l = new DoubleArrayList();
        pour(i, l, max);
        l.trim();
        return l;
    }
    
    public static DoubleList pour(final DoubleIterator i) {
        return pour(i, Integer.MAX_VALUE);
    }
    
    public static DoubleIterator asDoubleIterator(final Iterator i) {
        if (i instanceof DoubleIterator) {
            return (DoubleIterator)i;
        }
        return new IteratorWrapper(i);
    }
    
    public static DoubleListIterator asDoubleIterator(final ListIterator i) {
        if (i instanceof DoubleListIterator) {
            return (DoubleListIterator)i;
        }
        return new ListIteratorWrapper(i);
    }
    
    public static DoubleIterator concat(final DoubleIterator[] a) {
        return concat(a, 0, a.length);
    }
    
    public static DoubleIterator concat(final DoubleIterator[] a, final int offset, final int length) {
        return new IteratorConcatenator(a, offset, length);
    }
    
    public static DoubleIterator unmodifiable(final DoubleIterator i) {
        return new UnmodifiableIterator(i);
    }
    
    public static DoubleBidirectionalIterator unmodifiable(final DoubleBidirectionalIterator i) {
        return new UnmodifiableBidirectionalIterator(i);
    }
    
    public static DoubleListIterator unmodifiable(final DoubleListIterator i) {
        return new UnmodifiableListIterator(i);
    }
    
    public static DoubleIterator wrap(final ByteIterator iterator) {
        return new ByteIteratorWrapper(iterator);
    }
    
    public static DoubleIterator wrap(final ShortIterator iterator) {
        return new ShortIteratorWrapper(iterator);
    }
    
    public static DoubleIterator wrap(final IntIterator iterator) {
        return new IntIteratorWrapper(iterator);
    }
    
    public static DoubleIterator wrap(final FloatIterator iterator) {
        return new FloatIteratorWrapper(iterator);
    }
    
    static {
        EMPTY_ITERATOR = new EmptyIterator();
    }
    
    public static class EmptyIterator extends AbstractDoubleListIterator implements Serializable, Cloneable
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
        public double nextDouble() {
            throw new NoSuchElementException();
        }
        
        @Override
        public double previousDouble() {
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
            return DoubleIterators.EMPTY_ITERATOR;
        }
        
        private Object readResolve() {
            return DoubleIterators.EMPTY_ITERATOR;
        }
    }
    
    private static class SingletonIterator extends AbstractDoubleListIterator
    {
        private final double element;
        private int curr;
        
        public SingletonIterator(final double element) {
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
        public double nextDouble() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = 1;
            return this.element;
        }
        
        @Override
        public double previousDouble() {
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
    
    private static class ArrayIterator extends AbstractDoubleListIterator
    {
        private final double[] array;
        private final int offset;
        private final int length;
        private int curr;
        
        public ArrayIterator(final double[] array, final int offset, final int length) {
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
        public double nextDouble() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + this.curr++];
        }
        
        @Override
        public double previousDouble() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final double[] array = this.array;
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
    
    private static class IteratorWrapper extends AbstractDoubleIterator
    {
        final Iterator<Double> i;
        
        public IteratorWrapper(final Iterator<Double> i) {
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
        public double nextDouble() {
            return this.i.next();
        }
    }
    
    private static class ListIteratorWrapper extends AbstractDoubleListIterator
    {
        final ListIterator<Double> i;
        
        public ListIteratorWrapper(final ListIterator<Double> i) {
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
        public void set(final double k) {
            this.i.set(k);
        }
        
        @Override
        public void add(final double k) {
            this.i.add(k);
        }
        
        @Override
        public void remove() {
            this.i.remove();
        }
        
        @Override
        public double nextDouble() {
            return this.i.next();
        }
        
        @Override
        public double previousDouble() {
            return this.i.previous();
        }
    }
    
    private static class IteratorConcatenator extends AbstractDoubleIterator
    {
        final DoubleIterator[] a;
        int offset;
        int length;
        int lastOffset;
        
        public IteratorConcatenator(final DoubleIterator[] a, final int offset, final int length) {
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
        public double nextDouble() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final DoubleIterator[] a = this.a;
            final int offset = this.offset;
            this.lastOffset = offset;
            final double next = a[offset].nextDouble();
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
    
    public static class UnmodifiableIterator extends AbstractDoubleIterator
    {
        protected final DoubleIterator i;
        
        public UnmodifiableIterator(final DoubleIterator i) {
            this.i = i;
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        @Override
        public double nextDouble() {
            return this.i.nextDouble();
        }
        
        @Deprecated
        @Override
        public Double next() {
            return this.i.next();
        }
    }
    
    public static class UnmodifiableBidirectionalIterator extends AbstractDoubleBidirectionalIterator
    {
        protected final DoubleBidirectionalIterator i;
        
        public UnmodifiableBidirectionalIterator(final DoubleBidirectionalIterator i) {
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
        public double nextDouble() {
            return this.i.nextDouble();
        }
        
        @Override
        public double previousDouble() {
            return this.i.previousDouble();
        }
        
        @Deprecated
        @Override
        public Double next() {
            return this.i.next();
        }
        
        @Deprecated
        @Override
        public Double previous() {
            return this.i.previous();
        }
    }
    
    public static class UnmodifiableListIterator extends AbstractDoubleListIterator
    {
        protected final DoubleListIterator i;
        
        public UnmodifiableListIterator(final DoubleListIterator i) {
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
        public double nextDouble() {
            return this.i.nextDouble();
        }
        
        @Override
        public double previousDouble() {
            return this.i.previousDouble();
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
        public Double next() {
            return this.i.next();
        }
        
        @Deprecated
        @Override
        public Double previous() {
            return this.i.previous();
        }
    }
    
    protected static class ByteIteratorWrapper implements DoubleIterator
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
        public Double next() {
            return (double)this.iterator.nextByte();
        }
        
        @Override
        public double nextDouble() {
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
    
    protected static class ShortIteratorWrapper implements DoubleIterator
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
        public Double next() {
            return (double)this.iterator.nextShort();
        }
        
        @Override
        public double nextDouble() {
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
    
    protected static class IntIteratorWrapper implements DoubleIterator
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
        public Double next() {
            return (double)this.iterator.nextInt();
        }
        
        @Override
        public double nextDouble() {
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
    
    protected static class FloatIteratorWrapper implements DoubleIterator
    {
        final FloatIterator iterator;
        
        public FloatIteratorWrapper(final FloatIterator iterator) {
            this.iterator = iterator;
        }
        
        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }
        
        @Override
        public Double next() {
            return (double)this.iterator.nextFloat();
        }
        
        @Override
        public double nextDouble() {
            return this.iterator.nextFloat();
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
