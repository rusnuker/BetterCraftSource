// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

import java.util.NoSuchElementException;
import java.io.Serializable;

public class BooleanBigListIterators
{
    public static final EmptyBigListIterator EMPTY_BIG_LIST_ITERATOR;
    
    private BooleanBigListIterators() {
    }
    
    public static BooleanBigListIterator singleton(final boolean element) {
        return new SingletonBigListIterator(element);
    }
    
    public static BooleanBigListIterator unmodifiable(final BooleanBigListIterator i) {
        return new UnmodifiableBigListIterator(i);
    }
    
    public static BooleanBigListIterator asBigListIterator(final BooleanListIterator i) {
        return new BigListIteratorListIterator(i);
    }
    
    static {
        EMPTY_BIG_LIST_ITERATOR = new EmptyBigListIterator();
    }
    
    public static class EmptyBigListIterator extends AbstractBooleanBigListIterator implements Serializable, Cloneable
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
        public boolean nextBoolean() {
            throw new NoSuchElementException();
        }
        
        @Override
        public boolean previousBoolean() {
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
            return BooleanBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        private Object readResolve() {
            return BooleanBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
    }
    
    private static class SingletonBigListIterator extends AbstractBooleanBigListIterator
    {
        private final boolean element;
        private int curr;
        
        public SingletonBigListIterator(final boolean element) {
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
        public long nextIndex() {
            return this.curr;
        }
        
        @Override
        public long previousIndex() {
            return this.curr - 1;
        }
    }
    
    public static class UnmodifiableBigListIterator extends AbstractBooleanBigListIterator
    {
        protected final BooleanBigListIterator i;
        
        public UnmodifiableBigListIterator(final BooleanBigListIterator i) {
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
        public long nextIndex() {
            return this.i.nextIndex();
        }
        
        @Override
        public long previousIndex() {
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
    
    public static class BigListIteratorListIterator extends AbstractBooleanBigListIterator
    {
        protected final BooleanListIterator i;
        
        protected BigListIteratorListIterator(final BooleanListIterator i) {
            this.i = i;
        }
        
        private int intDisplacement(final long n) {
            if (n < -2147483648L || n > 2147483647L) {
                throw new IndexOutOfBoundsException("This big iterator is restricted to 32-bit displacements");
            }
            return (int)n;
        }
        
        @Override
        public void set(final boolean ok) {
            this.i.set(ok);
        }
        
        @Override
        public void add(final boolean ok) {
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
        public boolean nextBoolean() {
            return this.i.nextBoolean();
        }
        
        @Override
        public boolean previousBoolean() {
            return this.i.previousBoolean();
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
