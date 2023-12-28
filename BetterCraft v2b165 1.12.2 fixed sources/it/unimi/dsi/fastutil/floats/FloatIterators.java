// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.util.NoSuchElementException;
import java.io.Serializable;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import java.util.ListIterator;
import java.util.Iterator;

public class FloatIterators
{
    public static final EmptyIterator EMPTY_ITERATOR;
    
    private FloatIterators() {
    }
    
    public static FloatListIterator singleton(final float element) {
        return new SingletonIterator(element);
    }
    
    public static FloatListIterator wrap(final float[] array, final int offset, final int length) {
        FloatArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator(array, offset, length);
    }
    
    public static FloatListIterator wrap(final float[] array) {
        return new ArrayIterator(array, 0, array.length);
    }
    
    public static int unwrap(final FloatIterator i, final float[] array, int offset, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            array[offset++] = i.nextFloat();
        }
        return max - j - 1;
    }
    
    public static int unwrap(final FloatIterator i, final float[] array) {
        return unwrap(i, array, 0, array.length);
    }
    
    public static float[] unwrap(final FloatIterator i, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        float[] array = new float[16];
        int j = 0;
        while (max-- != 0 && i.hasNext()) {
            if (j == array.length) {
                array = FloatArrays.grow(array, j + 1);
            }
            array[j++] = i.nextFloat();
        }
        return FloatArrays.trim(array, j);
    }
    
    public static float[] unwrap(final FloatIterator i) {
        return unwrap(i, Integer.MAX_VALUE);
    }
    
    public static int unwrap(final FloatIterator i, final FloatCollection c, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            c.add(i.nextFloat());
        }
        return max - j - 1;
    }
    
    public static long unwrap(final FloatIterator i, final FloatCollection c) {
        long n = 0L;
        while (i.hasNext()) {
            c.add(i.nextFloat());
            ++n;
        }
        return n;
    }
    
    public static int pour(final FloatIterator i, final FloatCollection s, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            s.add(i.nextFloat());
        }
        return max - j - 1;
    }
    
    public static int pour(final FloatIterator i, final FloatCollection s) {
        return pour(i, s, Integer.MAX_VALUE);
    }
    
    public static FloatList pour(final FloatIterator i, final int max) {
        final FloatArrayList l = new FloatArrayList();
        pour(i, l, max);
        l.trim();
        return l;
    }
    
    public static FloatList pour(final FloatIterator i) {
        return pour(i, Integer.MAX_VALUE);
    }
    
    public static FloatIterator asFloatIterator(final Iterator i) {
        if (i instanceof FloatIterator) {
            return (FloatIterator)i;
        }
        return new IteratorWrapper(i);
    }
    
    public static FloatListIterator asFloatIterator(final ListIterator i) {
        if (i instanceof FloatListIterator) {
            return (FloatListIterator)i;
        }
        return new ListIteratorWrapper(i);
    }
    
    public static FloatIterator concat(final FloatIterator[] a) {
        return concat(a, 0, a.length);
    }
    
    public static FloatIterator concat(final FloatIterator[] a, final int offset, final int length) {
        return new IteratorConcatenator(a, offset, length);
    }
    
    public static FloatIterator unmodifiable(final FloatIterator i) {
        return new UnmodifiableIterator(i);
    }
    
    public static FloatBidirectionalIterator unmodifiable(final FloatBidirectionalIterator i) {
        return new UnmodifiableBidirectionalIterator(i);
    }
    
    public static FloatListIterator unmodifiable(final FloatListIterator i) {
        return new UnmodifiableListIterator(i);
    }
    
    public static FloatIterator wrap(final ByteIterator iterator) {
        return new ByteIteratorWrapper(iterator);
    }
    
    public static FloatIterator wrap(final ShortIterator iterator) {
        return new ShortIteratorWrapper(iterator);
    }
    
    static {
        EMPTY_ITERATOR = new EmptyIterator();
    }
    
    public static class EmptyIterator extends AbstractFloatListIterator implements Serializable, Cloneable
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
        public float nextFloat() {
            throw new NoSuchElementException();
        }
        
        @Override
        public float previousFloat() {
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
            return FloatIterators.EMPTY_ITERATOR;
        }
        
        private Object readResolve() {
            return FloatIterators.EMPTY_ITERATOR;
        }
    }
    
    private static class SingletonIterator extends AbstractFloatListIterator
    {
        private final float element;
        private int curr;
        
        public SingletonIterator(final float element) {
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
        public float nextFloat() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = 1;
            return this.element;
        }
        
        @Override
        public float previousFloat() {
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
    
    private static class ArrayIterator extends AbstractFloatListIterator
    {
        private final float[] array;
        private final int offset;
        private final int length;
        private int curr;
        
        public ArrayIterator(final float[] array, final int offset, final int length) {
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
        public float nextFloat() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + this.curr++];
        }
        
        @Override
        public float previousFloat() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final float[] array = this.array;
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
    
    private static class IteratorWrapper extends AbstractFloatIterator
    {
        final Iterator<Float> i;
        
        public IteratorWrapper(final Iterator<Float> i) {
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
        public float nextFloat() {
            return this.i.next();
        }
    }
    
    private static class ListIteratorWrapper extends AbstractFloatListIterator
    {
        final ListIterator<Float> i;
        
        public ListIteratorWrapper(final ListIterator<Float> i) {
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
        public void set(final float k) {
            this.i.set(k);
        }
        
        @Override
        public void add(final float k) {
            this.i.add(k);
        }
        
        @Override
        public void remove() {
            this.i.remove();
        }
        
        @Override
        public float nextFloat() {
            return this.i.next();
        }
        
        @Override
        public float previousFloat() {
            return this.i.previous();
        }
    }
    
    private static class IteratorConcatenator extends AbstractFloatIterator
    {
        final FloatIterator[] a;
        int offset;
        int length;
        int lastOffset;
        
        public IteratorConcatenator(final FloatIterator[] a, final int offset, final int length) {
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
        public float nextFloat() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final FloatIterator[] a = this.a;
            final int offset = this.offset;
            this.lastOffset = offset;
            final float next = a[offset].nextFloat();
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
    
    public static class UnmodifiableIterator extends AbstractFloatIterator
    {
        protected final FloatIterator i;
        
        public UnmodifiableIterator(final FloatIterator i) {
            this.i = i;
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        @Override
        public float nextFloat() {
            return this.i.nextFloat();
        }
        
        @Deprecated
        @Override
        public Float next() {
            return this.i.next();
        }
    }
    
    public static class UnmodifiableBidirectionalIterator extends AbstractFloatBidirectionalIterator
    {
        protected final FloatBidirectionalIterator i;
        
        public UnmodifiableBidirectionalIterator(final FloatBidirectionalIterator i) {
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
        public float nextFloat() {
            return this.i.nextFloat();
        }
        
        @Override
        public float previousFloat() {
            return this.i.previousFloat();
        }
        
        @Deprecated
        @Override
        public Float next() {
            return this.i.next();
        }
        
        @Deprecated
        @Override
        public Float previous() {
            return this.i.previous();
        }
    }
    
    public static class UnmodifiableListIterator extends AbstractFloatListIterator
    {
        protected final FloatListIterator i;
        
        public UnmodifiableListIterator(final FloatListIterator i) {
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
        public float nextFloat() {
            return this.i.nextFloat();
        }
        
        @Override
        public float previousFloat() {
            return this.i.previousFloat();
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
        public Float next() {
            return this.i.next();
        }
        
        @Deprecated
        @Override
        public Float previous() {
            return this.i.previous();
        }
    }
    
    protected static class ByteIteratorWrapper implements FloatIterator
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
        public Float next() {
            return (float)this.iterator.nextByte();
        }
        
        @Override
        public float nextFloat() {
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
    
    protected static class ShortIteratorWrapper implements FloatIterator
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
        public Float next() {
            return (float)this.iterator.nextShort();
        }
        
        @Override
        public float nextFloat() {
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
