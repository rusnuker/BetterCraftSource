// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import java.io.Serializable;

public class Double2ObjectFunctions
{
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Double2ObjectFunctions() {
    }
    
    public static <V> Double2ObjectFunction<V> singleton(final double key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Double2ObjectFunction<V> singleton(final Double key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Double2ObjectFunction<V> synchronize(final Double2ObjectFunction<V> f) {
        return new SynchronizedFunction<V>(f);
    }
    
    public static <V> Double2ObjectFunction<V> synchronize(final Double2ObjectFunction<V> f, final Object sync) {
        return new SynchronizedFunction<V>(f, sync);
    }
    
    public static <V> Double2ObjectFunction<V> unmodifiable(final Double2ObjectFunction<V> f) {
        return new UnmodifiableFunction<V>(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction<V> extends AbstractDouble2ObjectFunction<V> implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        @Override
        public V get(final double k) {
            return null;
        }
        
        @Override
        public boolean containsKey(final double k) {
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
            return Double2ObjectFunctions.EMPTY_FUNCTION;
        }
        
        public Object clone() {
            return Double2ObjectFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton<V> extends AbstractDouble2ObjectFunction<V> implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final double key;
        protected final V value;
        
        protected Singleton(final double key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public boolean containsKey(final double k) {
            return Double.doubleToLongBits(this.key) == Double.doubleToLongBits(k);
        }
        
        @Override
        public V get(final double k) {
            if (Double.doubleToLongBits(this.key) == Double.doubleToLongBits(k)) {
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
    
    public static class SynchronizedFunction<V> extends AbstractDouble2ObjectFunction<V> implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2ObjectFunction<V> function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Double2ObjectFunction<V> f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Double2ObjectFunction<V> f) {
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
        public boolean containsKey(final double k) {
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
        public V put(final double k, final V v) {
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
        public V put(final Double k, final V v) {
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
        public V remove(final double k) {
            synchronized (this.sync) {
                return this.function.remove(k);
            }
        }
        
        @Override
        public V get(final double k) {
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
    
    public static class UnmodifiableFunction<V> extends AbstractDouble2ObjectFunction<V> implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2ObjectFunction<V> function;
        
        protected UnmodifiableFunction(final Double2ObjectFunction<V> f) {
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
        public boolean containsKey(final double k) {
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
        public V put(final double k, final V v) {
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
        public V remove(final double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V get(final double k) {
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
