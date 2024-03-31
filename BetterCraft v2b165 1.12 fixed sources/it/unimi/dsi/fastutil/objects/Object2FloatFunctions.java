// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public class Object2FloatFunctions
{
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Object2FloatFunctions() {
    }
    
    public static <K> Object2FloatFunction<K> singleton(final K key, final float value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2FloatFunction<K> singleton(final K key, final Float value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2FloatFunction<K> synchronize(final Object2FloatFunction<K> f) {
        return new SynchronizedFunction<K>(f);
    }
    
    public static <K> Object2FloatFunction<K> synchronize(final Object2FloatFunction<K> f, final Object sync) {
        return new SynchronizedFunction<K>(f, sync);
    }
    
    public static <K> Object2FloatFunction<K> unmodifiable(final Object2FloatFunction<K> f) {
        return new UnmodifiableFunction<K>(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction<K> extends AbstractObject2FloatFunction<K> implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        @Override
        public float getFloat(final Object k) {
            return 0.0f;
        }
        
        @Override
        public boolean containsKey(final Object k) {
            return false;
        }
        
        @Override
        public float defaultReturnValue() {
            return 0.0f;
        }
        
        @Override
        public void defaultReturnValue(final float defRetValue) {
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
            return Object2FloatFunctions.EMPTY_FUNCTION;
        }
        
        public Object clone() {
            return Object2FloatFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton<K> extends AbstractObject2FloatFunction<K> implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final K key;
        protected final float value;
        
        protected Singleton(final K key, final float value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public boolean containsKey(final Object k) {
            return (this.key == null) ? (k == null) : this.key.equals(k);
        }
        
        @Override
        public float getFloat(final Object k) {
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
    
    public static class SynchronizedFunction<K> extends AbstractObject2FloatFunction<K> implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2FloatFunction<K> function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Object2FloatFunction<K> f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Object2FloatFunction<K> f) {
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
        public float defaultReturnValue() {
            synchronized (this.sync) {
                return this.function.defaultReturnValue();
            }
        }
        
        @Override
        public void defaultReturnValue(final float defRetValue) {
            synchronized (this.sync) {
                this.function.defaultReturnValue(defRetValue);
            }
        }
        
        @Override
        public float put(final K k, final float v) {
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
        public Float put(final K k, final Float v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Deprecated
        @Override
        public Float get(final Object k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        @Deprecated
        @Override
        public Float remove(final Object k) {
            synchronized (this.sync) {
                return this.function.remove(k);
            }
        }
        
        @Override
        public float removeFloat(final Object k) {
            synchronized (this.sync) {
                return this.function.removeFloat(k);
            }
        }
        
        @Override
        public float getFloat(final Object k) {
            synchronized (this.sync) {
                return this.function.getFloat(k);
            }
        }
    }
    
    public static class UnmodifiableFunction<K> extends AbstractObject2FloatFunction<K> implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2FloatFunction<K> function;
        
        protected UnmodifiableFunction(final Object2FloatFunction<K> f) {
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
        public float defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final float defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float put(final K k, final float v) {
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
        public float removeFloat(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float getFloat(final Object k) {
            return this.function.getFloat(k);
        }
    }
}
