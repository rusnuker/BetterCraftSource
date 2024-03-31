// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.util.NoSuchElementException;
import java.io.Serializable;

public class FloatBigListIterators
{
    public static final EmptyBigListIterator EMPTY_BIG_LIST_ITERATOR;
    
    private FloatBigListIterators() {
    }
    
    public static FloatBigListIterator singleton(final float element) {
        return new SingletonBigListIterator(element);
    }
    
    public static FloatBigListIterator unmodifiable(final FloatBigListIterator i) {
        return new UnmodifiableBigListIterator(i);
    }
    
    public static FloatBigListIterator asBigListIterator(final FloatListIterator i) {
        return new BigListIteratorListIterator(i);
    }
    
    static {
        EMPTY_BIG_LIST_ITERATOR = new EmptyBigListIterator();
    }
    
    public static class EmptyBigListIterator extends AbstractFloatBigListIterator implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyBigListIterator() {
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
        public long nextIndex() {
            return 0L;
        }
        
        @Override
        public long previousIndex() {
            return -1L;
        }
        
        @Override
        public long skip(final long n) {
            return 0L;
        }
        
        @Override
        public long back(final long n) {
            return 0L;
        }
        
        public Object clone() {
            return FloatBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        private Object readResolve() {
            return FloatBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
    }
    
    private static class SingletonBigListIterator extends AbstractFloatBigListIterator
    {
        private final float element;
        private int curr;
        
        public SingletonBigListIterator(final float element) {
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
        public long nextIndex() {
            return this.curr;
        }
        
        @Override
        public long previousIndex() {
            return this.curr - 1;
        }
    }
    
    public static class UnmodifiableBigListIterator extends AbstractFloatBigListIterator
    {
        protected final FloatBigListIterator i;
        
        public UnmodifiableBigListIterator(final FloatBigListIterator i) {
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
        public long nextIndex() {
            return this.i.nextIndex();
        }
        
        @Override
        public long previousIndex() {
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
    
    public static class BigListIteratorListIterator extends AbstractFloatBigListIterator
    {
        protected final FloatListIterator i;
        
        protected BigListIteratorListIterator(final FloatListIterator i) {
            this.i = i;
        }
        
        private int intDisplacement(final long n) {
            if (n < -2147483648L || n > 2147483647L) {
                throw new IndexOutOfBoundsException("This big iterator is restricted to 32-bit displacements");
            }
            return (int)n;
        }
        
        @Override
        public void set(final float ok) {
            this.i.set(ok);
        }
        
        @Override
        public void add(final float ok) {
            this.i.add(ok);
        }
        
        @Override
        public int back(final int n) {
            return this.i.back(n);
        }
        
        @Override
        public long back(final long n) {
            return this.i.back(this.intDisplacement(n));
        }
        
        @Override
        public void remove() {
            this.i.remove();
        }
        
        @Override
        public int skip(final int n) {
            return this.i.skip(n);
        }
        
        @Override
        public long skip(final long n) {
            return this.i.skip(this.intDisplacement(n));
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
        public long nextIndex() {
            return this.i.nextIndex();
        }
        
        @Override
        public long previousIndex() {
            return this.i.previousIndex();
        }
    }
}
