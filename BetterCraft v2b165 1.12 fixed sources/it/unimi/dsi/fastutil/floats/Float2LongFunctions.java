// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.Function;
import java.io.Serializable;

public class Float2LongFunctions
{
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Float2LongFunctions() {
    }
    
    public static Float2LongFunction singleton(final float key, final long value) {
        return new Singleton(key, value);
    }
    
    public static Float2LongFunction singleton(final Float key, final Long value) {
        return new Singleton(key, value);
    }
    
    public static Float2LongFunction synchronize(final Float2LongFunction f) {
        return new SynchronizedFunction(f);
    }
    
    public static Float2LongFunction synchronize(final Float2LongFunction f, final Object sync) {
        return new SynchronizedFunction(f, sync);
    }
    
    public static Float2LongFunction unmodifiable(final Float2LongFunction f) {
        return new UnmodifiableFunction(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction extends AbstractFloat2LongFunction implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        @Override
        public long get(final float k) {
            return 0L;
        }
        
        @Override
        public boolean containsKey(final float k) {
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
        
        @Deprecated
        @Override
        public Long get(final Object k) {
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
            return Float2LongFunctions.EMPTY_FUNCTION;
        }
        
        public Object clone() {
            return Float2LongFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton extends AbstractFloat2LongFunction implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final float key;
        protected final long value;
        
        protected Singleton(final float key, final long value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public boolean containsKey(final float k) {
            return Float.floatToIntBits(this.key) == Float.floatToIntBits(k);
        }
        
        @Override
        public long get(final float k) {
            if (Float.floatToIntBits(this.key) == Float.floatToIntBits(k)) {
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
    
    public static class SynchronizedFunction extends AbstractFloat2LongFunction implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2LongFunction function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Float2LongFunction f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Float2LongFunction f) {
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
        public boolean containsKey(final float k) {
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
        public long put(final float k, final long v) {
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
        public Long put(final Float k, final Long v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Deprecated
        @Override
        public Long get(final Object k) {
            synchronized (this.sync) {
                return ((Function<K, Long>)this.function).get(k);
            }
        }
        
        @Deprecated
        @Override
        public Long remove(final Object k) {
            synchronized (this.sync) {
                return ((Function<K, Long>)this.function).remove(k);
            }
        }
        
        @Override
        public long remove(final float k) {
            synchronized (this.sync) {
                return this.function.remove(k);
            }
        }
        
        @Override
        public long get(final float k) {
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
    
    public static class UnmodifiableFunction extends AbstractFloat2LongFunction implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2LongFunction function;
        
        protected UnmodifiableFunction(final Float2LongFunction f) {
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
        public boolean containsKey(final float k) {
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
        public long put(final float k, final long v) {
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
        
        @Deprecated
        @Override
        public long remove(final float k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public long get(final float k) {
            return this.function.get(k);
        }
        
        @Override
        public boolean containsKey(final Object ok) {
            return this.function.containsKey(ok);
        }
    }
}
