// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public class Reference2LongFunctions
{
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Reference2LongFunctions() {
    }
    
    public static <K> Reference2LongFunction<K> singleton(final K key, final long value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2LongFunction<K> singleton(final K key, final Long value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2LongFunction<K> synchronize(final Reference2LongFunction<K> f) {
        return new SynchronizedFunction<K>(f);
    }
    
    public static <K> Reference2LongFunction<K> synchronize(final Reference2LongFunction<K> f, final Object sync) {
        return new SynchronizedFunction<K>(f, sync);
    }
    
    public static <K> Reference2LongFunction<K> unmodifiable(final Reference2LongFunction<K> f) {
        return new UnmodifiableFunction<K>(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction<K> extends AbstractReference2LongFunction<K> implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        @Override
        public long getLong(final Object k) {
            return 0L;
        }
        
        @Override
        public boolean containsKey(final Object k) {
            return false;
        }
        
        @Override
        public long defaultReturnValue() {
            return 0L;
        }
        
        @Override
        public void defaultReturnValue(final long defRetValue) {
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
            return Reference2LongFunctions.EMPTY_FUNCTION;
        }
        
        public Object clone() {
            return Reference2LongFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton<K> extends AbstractReference2LongFunction<K> implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final K key;
        protected final long value;
        
        protected Singleton(final K key, final long value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public boolean containsKey(final Object k) {
            return this.key == k;
        }
        
        @Override
        public long getLong(final Object k) {
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
    
    public static class SynchronizedFunction<K> extends AbstractReference2LongFunction<K> implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2LongFunction<K> function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Reference2LongFunction<K> f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Reference2LongFunction<K> f) {
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
        public long defaultReturnValue() {
            synchronized (this.sync) {
                return this.function.defaultReturnValue();
            }
        }
        
        @Override
        public void defaultReturnValue(final long defRetValue) {
            synchronized (this.sync) {
                this.function.defaultReturnValue(defRetValue);
            }
        }
        
        @Override
        public long put(final K k, final long v) {
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
        public Long put(final K k, final Long v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Deprecated
        @Override
        public Long get(final Object k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        @Deprecated
        @Override
        public Long remove(final Object k) {
            synchronized (this.sync) {
                return this.function.remove(k);
            }
        }
        
        @Override
        public long removeLong(final Object k) {
            synchronized (this.sync) {
                return this.function.removeLong(k);
            }
        }
        
        @Override
        public long getLong(final Object k) {
            synchronized (this.sync) {
                return this.function.getLong(k);
            }
        }
    }
    
    public static class UnmodifiableFunction<K> extends AbstractReference2LongFunction<K> implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2LongFunction<K> function;
        
        protected UnmodifiableFunction(final Reference2LongFunction<K> f) {
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
        public long defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final long defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long put(final K k, final long v) {
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
        public long removeLong(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long getLong(final Object k) {
            return this.function.getLong(k);
        }
    }
}
