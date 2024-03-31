// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public class Object2ShortFunctions
{
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Object2ShortFunctions() {
    }
    
    public static <K> Object2ShortFunction<K> singleton(final K key, final short value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2ShortFunction<K> singleton(final K key, final Short value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2ShortFunction<K> synchronize(final Object2ShortFunction<K> f) {
        return new SynchronizedFunction<K>(f);
    }
    
    public static <K> Object2ShortFunction<K> synchronize(final Object2ShortFunction<K> f, final Object sync) {
        return new SynchronizedFunction<K>(f, sync);
    }
    
    public static <K> Object2ShortFunction<K> unmodifiable(final Object2ShortFunction<K> f) {
        return new UnmodifiableFunction<K>(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction<K> extends AbstractObject2ShortFunction<K> implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        @Override
        public short getShort(final Object k) {
            return 0;
        }
        
        @Override
        public boolean containsKey(final Object k) {
            return false;
        }
        
        @Override
        public short defaultReturnValue() {
            return 0;
        }
        
        @Override
        public void defaultReturnValue(final short defRetValue) {
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
            return Object2ShortFunctions.EMPTY_FUNCTION;
        }
        
        public Object clone() {
            return Object2ShortFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton<K> extends AbstractObject2ShortFunction<K> implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final K key;
        protected final short value;
        
        protected Singleton(final K key, final short value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public boolean containsKey(final Object k) {
            return (this.key == null) ? (k == null) : this.key.equals(k);
        }
        
        @Override
        public short getShort(final Object k) {
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
    
    public static class SynchronizedFunction<K> extends AbstractObject2ShortFunction<K> implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2ShortFunction<K> function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Object2ShortFunction<K> f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Object2ShortFunction<K> f) {
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
        public short defaultReturnValue() {
            synchronized (this.sync) {
                return this.function.defaultReturnValue();
            }
        }
        
        @Override
        public void defaultReturnValue(final short defRetValue) {
            synchronized (this.sync) {
                this.function.defaultReturnValue(defRetValue);
            }
        }
        
        @Override
        public short put(final K k, final short v) {
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
        public Short put(final K k, final Short v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Deprecated
        @Override
        public Short get(final Object k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        @Deprecated
        @Override
        public Short remove(final Object k) {
            synchronized (this.sync) {
                return this.function.remove(k);
            }
        }
        
        @Override
        public short removeShort(final Object k) {
            synchronized (this.sync) {
                return this.function.removeShort(k);
            }
        }
        
        @Override
        public short getShort(final Object k) {
            synchronized (this.sync) {
                return this.function.getShort(k);
            }
        }
    }
    
    public static class UnmodifiableFunction<K> extends AbstractObject2ShortFunction<K> implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2ShortFunction<K> function;
        
        protected UnmodifiableFunction(final Object2ShortFunction<K> f) {
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
        public short defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final short defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short put(final K k, final short v) {
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
        public short removeShort(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short getShort(final Object k) {
            return this.function.getShort(k);
        }
    }
}
