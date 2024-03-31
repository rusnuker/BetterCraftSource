// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import java.io.Serializable;

public class CharSets
{
    public static final EmptySet EMPTY_SET;
    
    private CharSets() {
    }
    
    public static CharSet singleton(final char element) {
        return new Singleton(element);
    }
    
    public static CharSet singleton(final Character element) {
        return new Singleton(element);
    }
    
    public static CharSet synchronize(final CharSet s) {
        return new SynchronizedSet(s);
    }
    
    public static CharSet synchronize(final CharSet s, final Object sync) {
        return new SynchronizedSet(s, sync);
    }
    
    public static CharSet unmodifiable(final CharSet s) {
        return new UnmodifiableSet(s);
    }
    
    static {
        EMPTY_SET = new EmptySet();
    }
    
    public static class EmptySet extends CharCollections.EmptyCollection implements CharSet, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public boolean remove(final char ok) {
            throw new UnsupportedOperationException();
        }
        
        public Object clone() {
            return CharSets.EMPTY_SET;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Set && ((Set)o).isEmpty();
        }
        
        private Object readResolve() {
            return CharSets.EMPTY_SET;
        }
    }
    
    public static class Singleton extends AbstractCharSet implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final char element;
        
        protected Singleton(final char element) {
            this.element = element;
        }
        
        @Override
        public boolean add(final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final char k) {
            return k == this.element;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Character> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char[] toCharArray() {
            final char[] a = { this.element };
            return a;
        }
        
        @Override
        public boolean addAll(final CharCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final CharCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final CharCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public CharListIterator iterator() {
            return CharIterators.singleton(this.element);
        }
        
        @Override
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedSet extends CharCollections.SynchronizedCollection implements CharSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected SynchronizedSet(final CharSet s, final Object sync) {
            super(s, sync);
        }
        
        protected SynchronizedSet(final CharSet s) {
            super(s);
        }
        
        @Override
        public boolean remove(final char k) {
            synchronized (this.sync) {
                return this.collection.remove(k);
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            synchronized (this.sync) {
                return this.collection.equals(o);
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.sync) {
                return this.collection.hashCode();
            }
        }
    }
    
    public static class UnmodifiableSet extends CharCollections.UnmodifiableCollection implements CharSet, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected UnmodifiableSet(final CharSet s) {
            super(s);
        }
        
        @Override
        public boolean remove(final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean equals(final Object o) {
            return this.collection.equals(o);
        }
        
        @Override
        public int hashCode() {
            return this.collection.hashCode();
        }
    }
}
