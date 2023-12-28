// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.util.NoSuchElementException;
import java.io.Serializable;
import java.util.ListIterator;
import java.util.Iterator;

public class CharIterators
{
    public static final EmptyIterator EMPTY_ITERATOR;
    
    private CharIterators() {
    }
    
    public static CharListIterator singleton(final char element) {
        return new SingletonIterator(element);
    }
    
    public static CharListIterator wrap(final char[] array, final int offset, final int length) {
        CharArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator(array, offset, length);
    }
    
    public static CharListIterator wrap(final char[] array) {
        return new ArrayIterator(array, 0, array.length);
    }
    
    public static int unwrap(final CharIterator i, final char[] array, int offset, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            array[offset++] = i.nextChar();
        }
        return max - j - 1;
    }
    
    public static int unwrap(final CharIterator i, final char[] array) {
        return unwrap(i, array, 0, array.length);
    }
    
    public static char[] unwrap(final CharIterator i, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        char[] array = new char[16];
        int j = 0;
        while (max-- != 0 && i.hasNext()) {
            if (j == array.length) {
                array = CharArrays.grow(array, j + 1);
            }
            array[j++] = i.nextChar();
        }
        return CharArrays.trim(array, j);
    }
    
    public static char[] unwrap(final CharIterator i) {
        return unwrap(i, Integer.MAX_VALUE);
    }
    
    public static int unwrap(final CharIterator i, final CharCollection c, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            c.add(i.nextChar());
        }
        return max - j - 1;
    }
    
    public static long unwrap(final CharIterator i, final CharCollection c) {
        long n = 0L;
        while (i.hasNext()) {
            c.add(i.nextChar());
            ++n;
        }
        return n;
    }
    
    public static int pour(final CharIterator i, final CharCollection s, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            s.add(i.nextChar());
        }
        return max - j - 1;
    }
    
    public static int pour(final CharIterator i, final CharCollection s) {
        return pour(i, s, Integer.MAX_VALUE);
    }
    
    public static CharList pour(final CharIterator i, final int max) {
        final CharArrayList l = new CharArrayList();
        pour(i, l, max);
        l.trim();
        return l;
    }
    
    public static CharList pour(final CharIterator i) {
        return pour(i, Integer.MAX_VALUE);
    }
    
    public static CharIterator asCharIterator(final Iterator i) {
        if (i instanceof CharIterator) {
            return (CharIterator)i;
        }
        return new IteratorWrapper(i);
    }
    
    public static CharListIterator asCharIterator(final ListIterator i) {
        if (i instanceof CharListIterator) {
            return (CharListIterator)i;
        }
        return new ListIteratorWrapper(i);
    }
    
    public static CharListIterator fromTo(final char from, final char to) {
        return new IntervalIterator(from, to);
    }
    
    public static CharIterator concat(final CharIterator[] a) {
        return concat(a, 0, a.length);
    }
    
    public static CharIterator concat(final CharIterator[] a, final int offset, final int length) {
        return new IteratorConcatenator(a, offset, length);
    }
    
    public static CharIterator unmodifiable(final CharIterator i) {
        return new UnmodifiableIterator(i);
    }
    
    public static CharBidirectionalIterator unmodifiable(final CharBidirectionalIterator i) {
        return new UnmodifiableBidirectionalIterator(i);
    }
    
    public static CharListIterator unmodifiable(final CharListIterator i) {
        return new UnmodifiableListIterator(i);
    }
    
    static {
        EMPTY_ITERATOR = new EmptyIterator();
    }
    
    public static class EmptyIterator extends AbstractCharListIterator implements Serializable, Cloneable
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
        public char nextChar() {
            throw new NoSuchElementException();
        }
        
        @Override
        public char previousChar() {
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
            return CharIterators.EMPTY_ITERATOR;
        }
        
        private Object readResolve() {
            return CharIterators.EMPTY_ITERATOR;
        }
    }
    
    private static class SingletonIterator extends AbstractCharListIterator
    {
        private final char element;
        private int curr;
        
        public SingletonIterator(final char element) {
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
        public char nextChar() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = 1;
            return this.element;
        }
        
        @Override
        public char previousChar() {
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
    
    private static class ArrayIterator extends AbstractCharListIterator
    {
        private final char[] array;
        private final int offset;
        private final int length;
        private int curr;
        
        public ArrayIterator(final char[] array, final int offset, final int length) {
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
        public char nextChar() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + this.curr++];
        }
        
        @Override
        public char previousChar() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final char[] array = this.array;
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
    
    private static class IteratorWrapper extends AbstractCharIterator
    {
        final Iterator<Character> i;
        
        public IteratorWrapper(final Iterator<Character> i) {
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
        public char nextChar() {
            return this.i.next();
        }
    }
    
    private static class ListIteratorWrapper extends AbstractCharListIterator
    {
        final ListIterator<Character> i;
        
        public ListIteratorWrapper(final ListIterator<Character> i) {
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
        public void set(final char k) {
            this.i.set(k);
        }
        
        @Override
        public void add(final char k) {
            this.i.add(k);
        }
        
        @Override
        public void remove() {
            this.i.remove();
        }
        
        @Override
        public char nextChar() {
            return this.i.next();
        }
        
        @Override
        public char previousChar() {
            return this.i.previous();
        }
    }
    
    private static class IntervalIterator extends AbstractCharListIterator
    {
        private final char from;
        private final char to;
        char curr;
        
        public IntervalIterator(final char from, final char to) {
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
        public char nextChar() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final char curr = this.curr;
            this.curr = (char)(curr + '\u0001');
            return curr;
        }
        
        @Override
        public char previousChar() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            return (char)(--this.curr);
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
                this.curr += (char)n;
                return n;
            }
            n = this.to - this.curr;
            this.curr = this.to;
            return n;
        }
        
        @Override
        public int back(int n) {
            if (this.curr - n >= this.from) {
                this.curr -= (char)n;
                return n;
            }
            n = this.curr - this.from;
            this.curr = this.from;
            return n;
        }
    }
    
    private static class IteratorConcatenator extends AbstractCharIterator
    {
        final CharIterator[] a;
        int offset;
        int length;
        int lastOffset;
        
        public IteratorConcatenator(final CharIterator[] a, final int offset, final int length) {
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
        public char nextChar() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final CharIterator[] a = this.a;
            final int offset = this.offset;
            this.lastOffset = offset;
            final char next = a[offset].nextChar();
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
    
    public static class UnmodifiableIterator extends AbstractCharIterator
    {
        protected final CharIterator i;
        
        public UnmodifiableIterator(final CharIterator i) {
            this.i = i;
        }
        
        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        @Override
        public char nextChar() {
            return this.i.nextChar();
        }
        
        @Deprecated
        @Override
        public Character next() {
            return this.i.next();
        }
    }
    
    public static class UnmodifiableBidirectionalIterator extends AbstractCharBidirectionalIterator
    {
        protected final CharBidirectionalIterator i;
        
        public UnmodifiableBidirectionalIterator(final CharBidirectionalIterator i) {
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
        public char nextChar() {
            return this.i.nextChar();
        }
        
        @Override
        public char previousChar() {
            return this.i.previousChar();
        }
        
        @Deprecated
        @Override
        public Character next() {
            return this.i.next();
        }
        
        @Deprecated
        @Override
        public Character previous() {
            return this.i.previous();
        }
    }
    
    public static class UnmodifiableListIterator extends AbstractCharListIterator
    {
        protected final CharListIterator i;
        
        public UnmodifiableListIterator(final CharListIterator i) {
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
        public char nextChar() {
            return this.i.nextChar();
        }
        
        @Override
        public char previousChar() {
            return this.i.previousChar();
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
        public Character next() {
            return this.i.next();
        }
        
        @Deprecated
        @Override
        public Character previous() {
            return this.i.previous();
        }
    }
}
