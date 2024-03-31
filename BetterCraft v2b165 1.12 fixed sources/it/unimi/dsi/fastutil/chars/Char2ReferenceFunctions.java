// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.io.Serializable;

public class Char2ReferenceFunctions
{
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Char2ReferenceFunctions() {
    }
    
    public static <V> Char2ReferenceFunction<V> singleton(final char key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Char2ReferenceFunction<V> singleton(final Character key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Char2ReferenceFunction<V> synchronize(final Char2ReferenceFunction<V> f) {
        return new SynchronizedFunction<V>(f);
    }
    
    public static <V> Char2ReferenceFunction<V> synchronize(final Char2ReferenceFunction<V> f, final Object sync) {
        return new SynchronizedFunction<V>(f, sync);
    }
    
    public static <V> Char2ReferenceFunction<V> unmodifiable(final Char2ReferenceFunction<V> f) {
        return new UnmodifiableFunction<V>(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction<V> extends AbstractChar2ReferenceFunction<V> implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        @Override
        public V get(final char k) {
            return null;
        }
        
        @Override
        public boolean containsKey(final char k) {
            return false;
        }
        
        @Override
        public V defaultReturnValue() {
            return null;
        }
        
        @Override
        public void defaultReturnValue(final V defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V get(final Object k) {
            return null;
        }
        
        @Override
        public int size() {
            return 0;
        }
        
        @Override
        public void clear() {
        }
        
        private Object readResolve() {
            return Char2ReferenceFunctions.EMPTY_FUNCTION;
        }
        
        public Object clone() {
            return Char2ReferenceFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton<V> extends AbstractChar2ReferenceFunction<V> implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final char key;
        protected final V value;
        
        protected Singleton(final char key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public boolean containsKey(final char k) {
            return this.key == k;
        }
        
        @Override
        public V get(final char k) {
            if (this.key == k) {
                return this.value;
            }
            return this.defRetValue;
        }
        
        @Override
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction<V> extends AbstractChar2ReferenceFunction<V> implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Char2ReferenceFunction<V> function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Char2ReferenceFunction<V> f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Char2ReferenceFunction<V> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }
        
        @Override
        public int size() {
            synchronized (this.sync) {
                return this.function.size();
            }
        }
        
        @Override
        public boolean containsKey(final char k) {
            synchronized (this.sync) {
                return this.function.containsKey(k);
            }
        }
        
        @Override
        public V defaultReturnValue() {
            synchronized (this.sync) {
                return this.function.defaultReturnValue();
            }
        }
        
        @Override
        public void defaultReturnValue(final V defRetValue) {
            synchronized (this.sync) {
                this.function.defaultReturnValue(defRetValue);
            }
        }
        
        @Override
        public V put(final char k, final V v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Override
        public void clear() {
            synchronized (this.sync) {
                this.function.clear();
            }
        }
        
        @Override
        public String toString() {
            synchronized (this.sync) {
                return this.function.toString();
            }
        }
        
        @Deprecated
        @Override
        public V put(final Character k, final V v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Override
        public V get(final Object k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        @Override
        public V remove(final Object k) {
            synchronized (this.sync) {
                return this.function.remove(k);
            }
        }
        
        @Override
        public V remove(final char k) {
            synchronized (this.sync) {
                return this.function.remove(k);
            }
        }
        
        @Override
        public V get(final char k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        @Override
        public boolean containsKey(final Object ok) {
            synchronized (this.sync) {
                return this.function.containsKey(ok);
            }
        }
    }
    
    public static class UnmodifiableFunction<V> extends AbstractChar2ReferenceFunction<V> implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Char2ReferenceFunction<V> function;
        
        protected UnmodifiableFunction(final Char2ReferenceFunction<V> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
        }
        
        @Override
        public int size() {
            return this.function.size();
        }
        
        @Override
        public boolean containsKey(final char k) {
            return this.function.containsKey(k);
        }
        
        @Override
        public V defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final V defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V put(final char k, final V v) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public String toString() {
            return this.function.toString();
        }
        
        @Override
        public V remove(final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V get(final char k) {
            return this.function.get(k);
        }
        
        @Override
        public boolean containsKey(final Object ok) {
            return this.function.containsKey(ok);
        }
        
        @Override
        public V remove(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V get(final Object k) {
            return this.function.get(k);
        }
    }
}
