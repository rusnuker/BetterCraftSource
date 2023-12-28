// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

import java.util.NoSuchElementException;
import java.io.Serializable;
import java.util.ListIterator;
import java.util.Iterator;

public class BooleanIterators
{
    public static final EmptyIterator EMPTY_ITERATOR;
    
    private BooleanIterators() {
    }
    
    public static BooleanListIterator singleton(final boolean element) {
        return new SingletonIterator(element);
    }
    
    public static BooleanListIterator wrap(final boolean[] array, final int offset, final int length) {
        BooleanArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator(array, offset, length);
    }
    
    public static BooleanListIterator wrap(final boolean[] array) {
        return new ArrayIterator(array, 0, array.length);
    }
    
    public static int unwrap(final BooleanIterator i, final boolean[] array, int offset, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            array[offset++] = i.nextBoolean();
        }
        return max - j - 1;
    }
    
    public static int unwrap(final BooleanIterator i, final boolean[] array) {
        return unwrap(i, array, 0, array.length);
    }
    
    public static boolean[] unwrap(final BooleanIterator i, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        boolean[] array = new boolean[16];
        int j = 0;
        while (max-- != 0 && i.hasNext()) {
            if (j == array.length) {
                array = BooleanArrays.grow(array, j + 1);
            }
            array[j++] = i.nextBoolean();
        }
        return BooleanArrays.trim(array, j);
    }
    
    public static boolean[] unwrap(final BooleanIterator i) {
        return unwrap(i, Integer.MAX_VALUE);
    }
    
    public static int unwrap(final BooleanIterator i, final BooleanCollection c, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            c.add(i.nextBoolean());
        }
        return max - j - 1;
    }
    
    public static long unwrap(final BooleanIterator i, final BooleanCollection c) {
        long n = 0L;
        while (i.hasNext()) {
            c.add(i.nextBoolean());
            ++n;
        }
        return n;
    }
    
    public static int pour(final BooleanIterator i, final BooleanCollection s, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            s.add(i.nextBoolean());
        }
        return max - j - 1;
    }
    
    public static int pour(final BooleanIterator i, final BooleanCollection s) {
        return pour(i, s, Integer.MAX_VALUE);
    }
    
    public static BooleanList pour(final BooleanIterator i, final int max) {
        final BooleanArrayList l = new BooleanArrayList();
        pour(i, l, max);
        l.trim();
        return l;
    }
    
    public static BooleanList pour(final BooleanIterator i) {
        return pour(i, Integer.MAX_VALUE);
    }
    
    public static BooleanIterator asBooleanIterator(final Iterator i) {
        if (i instanceof BooleanIterator) {
            return (BooleanIterator)i;
        }
        return new IteratorWrapper(i);
    }
    
    public static BooleanListIterator asBooleanIterator(final ListIterator i) {
        if (i instanceof BooleanListIterator) {
            return (BooleanListIterator)i;
        }
        return new ListIteratorWrapper(i);
    }
    
    public static BooleanIterator concat(final BooleanIterator[] a) {
        return concat(a, 0, a.length);
    }
    
    public static BooleanIterator concat(final BooleanIterator[] a, final int offset, final int length) {
        return new IteratorConcatenator(a, offset, length);
    }
    
    public static BooleanIterator unmodifiable(final BooleanIterator i) {
        return new UnmodifiableIterator(i);
    }
    
    public static BooleanBidirectionalIterator unmodifiable(final BooleanBidirectionalIterator i) {
        return new UnmodifiableBidirectionalIterator(i);
    }
    
    public static BooleanListIterator unmodifiable(final BooleanListIterator i) {
        return new UnmodifiableListIterator(i);
    }
    
    static {
        EMPTY_ITERATOR = new EmptyIterator();
    }
    
    public static class EmptyIterator extends AbstractBooleanListIterator implements Serializable, Cloneable
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
        public boolean nextBoolean() {
            throw new NoSuchElementException();
        }
        
        @Override
        public boolean previousBoolean() {
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
            return BooleanIterators.EMPTY_ITERATOR;
        }
        
        private Object readResolve() {
            return BooleanIterators.EMPTY_ITERATOR;
        }
    }
    
    private static class SingletonIterator extends AbstractBooleanListIterator
    {
        private final boolean element;
        private int curr;
        
        public SingletonIterator(final boolean element) {
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
        public boolean nextBoolean() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = 1;
            return this.element;
        }
        
        @Override
        public boolean previousBoolean() {
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
    
    private static class ArrayIterator extends AbstractBooleanListIterator
    {
        private final boolean[] array;
        private final int offset;
        private final int length;
        private int curr;
        
        public ArrayIterator(final boolean[] array, final int offset, final int length) {
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
        public boolean nextBoolean() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + this.curr++];
        }
        
        @Override
        public boolean previousBoolean() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final boolean[] array = this.array;
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
    
    private static class IteratorWrapper extends AbstractBooleanIterator
    {
        final Iterator<Boolean> i;
        
        public IteratorWrapper(final Iterator<Boolean> i) {
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
        public boolean nextBoolean() {
            return this.i.next();
        }
    }
    
    private static class ListIteratorWrapper extends AbstractBooleanListIterator
    {
        final ListIterator<Boolean> i;
        
        public ListIteratorWrapper(final ListIterator<Boolean> i) {
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
        public void set(final boolean k) {
            this.i.set(k);
        }
        
        @Override
        public void add(final boolean k) {
            this.i.add(k);
        }
        
        @Override
        public void remove() {
            this.i.remove();
        }
        
        @Override
        public boolean nextBoolean() {
            return this.i.next();
        }
        
        @Override
        public boolean previousBoolean() {
            return this.i.previous();
        }
    }
    
    private static class IteratorConcatenator extends AbstractBooleanIterator
    {
        final BooleanIterator[] a;
        int offset;
        int length;
        int lastOffset;
        
        public IteratorConcatenator(final BooleanIterator[] a, final int offset, final int length) {
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
        public boolean nextBoolean() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final BooleanIterator[] a = this.a;
            final int offset = this.offset;
            this.lastOffset = offset;
            final boolean next = a[offset].nextBoolean();
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
    
    public static class UnmodifiableIterator extends AbstractBooleanIterator
    {
        protected final BooleanIterator i;
        
        public UnmodifiableIterator(final BooleanIterator i) {
            this.i = i;
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        @Override
        public boolean nextBoolean() {
            return this.i.nextBoolean();
        }
        
        @Deprecated
        @Override
        public Boolean next() {
            return this.i.next();
        }
    }
    
    public static class UnmodifiableBidirectionalIterator extends AbstractBooleanBidirectionalIterator
    {
        protected final BooleanBidirectionalIterator i;
        
        public UnmodifiableBidirectionalIterator(final BooleanBidirectionalIterator i) {
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
        public boolean nextBoolean() {
            return this.i.nextBoolean();
        }
        
        @Override
        public boolean previousBoolean() {
            return this.i.previousBoolean();
        }
        
        @Deprecated
        @Override
        public Boolean next() {
            return this.i.next();
        }
        
        @Deprecated
        @Override
        public Boolean previous() {
            return this.i.previous();
        }
    }
    
    public static class UnmodifiableListIterator extends AbstractBooleanListIterator
    {
        protected final BooleanListIterator i;
        
        public UnmodifiableListIterator(final BooleanListIterator i) {
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
        public boolean nextBoolean() {
            return this.i.nextBoolean();
        }
        
        @Override
        public boolean previousBoolean() {
            return this.i.previousBoolean();
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
        public Boolean next() {
            return this.i.next();
        }
        
        @Deprecated
        @Override
        public Boolean previous() {
            return this.i.previous();
        }
    }
}
