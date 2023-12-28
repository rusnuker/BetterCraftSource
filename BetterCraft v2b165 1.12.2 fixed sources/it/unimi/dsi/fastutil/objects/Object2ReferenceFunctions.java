// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public class Object2ReferenceFunctions
{
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Object2ReferenceFunctions() {
    }
    
    public static <K, V> Object2ReferenceFunction<K, V> singleton(final K key, final V value) {
        return new Singleton<K, V>(key, value);
    }
    
    public static <K, V> Object2ReferenceFunction<K, V> synchronize(final Object2ReferenceFunction<K, V> f) {
        return new SynchronizedFunction<K, V>(f);
    }
    
    public static <K, V> Object2ReferenceFunction<K, V> synchronize(final Object2ReferenceFunction<K, V> f, final Object sync) {
        return new SynchronizedFunction<K, V>(f, sync);
    }
    
    public static <K, V> Object2ReferenceFunction<K, V> unmodifiable(final Object2ReferenceFunction<K, V> f) {
        return new UnmodifiableFunction<K, V>(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction<K, V> extends AbstractObject2ReferenceFunction<K, V> implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        @Override
        public V get(final Object k) {
            return null;
        }
        
        @Override
        public boolean containsKey(final Object k) {
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
        public int size() {
            return 0;
        }
        
        @Override
        public void clear() {
        }
        
        private Object readResolve() {
            return Object2ReferenceFunctions.EMPTY_FUNCTION;
        }
        
        public Object clone() {
            return Object2ReferenceFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton<K, V> extends AbstractObject2ReferenceFunction<K, V> implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final K key;
        protected final V value;
        
        protected Singleton(final K key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public boolean containsKey(final Object k) {
            return (this.key == null) ? (k == null) : this.key.equals(k);
        }
        
        @Override
        public V get(final Object k) {
            if (this.key == null) {
                if (k != null) {
                    return this.defRetValue;
                }
            }
            else if (!this.key.equals(k)) {
                return this.defRetValue;
            }
            return this.value;
        }
        
        @Override
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction<K, V> extends AbstractObject2ReferenceFunction<K, V> implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2ReferenceFunction<K, V> function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Object2ReferenceFunction<K, V> f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Object2ReferenceFunction<K, V> f) {
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
        public boolean containsKey(final Object k) {
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
        public V put(final K k, final V v) {
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
        
        @Override
        public V remove(final Object k) {
            synchronized (this.sync) {
                return this.function.remove(k);
            }
        }
        
        @Override
        public V get(final Object k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
    }
    
    public static class UnmodifiableFunction<K, V> extends AbstractObject2ReferenceFunction<K, V> implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2ReferenceFunction<K, V> function;
        
        protected UnmodifiableFunction(final Object2ReferenceFunction<K, V> f) {
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
        public boolean containsKey(final Object k) {
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
        public V put(final K k, final V v) {
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
        public V remove(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V get(final Object k) {
            return this.function.get(k);
        }
    }
}
