// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.Function;
import java.io.Serializable;

public class Double2BooleanFunctions
{
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Double2BooleanFunctions() {
    }
    
    public static Double2BooleanFunction singleton(final double key, final boolean value) {
        return new Singleton(key, value);
    }
    
    public static Double2BooleanFunction singleton(final Double key, final Boolean value) {
        return new Singleton(key, value);
    }
    
    public static Double2BooleanFunction synchronize(final Double2BooleanFunction f) {
        return new SynchronizedFunction(f);
    }
    
    public static Double2BooleanFunction synchronize(final Double2BooleanFunction f, final Object sync) {
        return new SynchronizedFunction(f, sync);
    }
    
    public static Double2BooleanFunction unmodifiable(final Double2BooleanFunction f) {
        return new UnmodifiableFunction(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction extends AbstractDouble2BooleanFunction implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        @Override
        public boolean get(final double k) {
            return false;
        }
        
        @Override
        public boolean containsKey(final double k) {
            return false;
        }
        
        @Override
        public boolean defaultReturnValue() {
            return false;
        }
        
        @Override
        public void defaultReturnValue(final boolean defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean get(final Object k) {
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
            return Double2BooleanFunctions.EMPTY_FUNCTION;
        }
        
        public Object clone() {
            return Double2BooleanFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton extends AbstractDouble2BooleanFunction implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final double key;
        protected final boolean value;
        
        protected Singleton(final double key, final boolean value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public boolean containsKey(final double k) {
            return Double.doubleToLongBits(this.key) == Double.doubleToLongBits(k);
        }
        
        @Override
        public boolean get(final double k) {
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
    
    public static class SynchronizedFunction extends AbstractDouble2BooleanFunction implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2BooleanFunction function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Double2BooleanFunction f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Double2BooleanFunction f) {
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
        public boolean defaultReturnValue() {
            synchronized (this.sync) {
                return this.function.defaultReturnValue();
            }
        }
        
        @Override
        public void defaultReturnValue(final boolean defRetValue) {
            synchronized (this.sync) {
                this.function.defaultReturnValue(defRetValue);
            }
        }
        
        @Override
        public boolean put(final double k, final boolean v) {
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
        public Boolean put(final Double k, final Boolean v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Deprecated
        @Override
        public Boolean get(final Object k) {
            synchronized (this.sync) {
                return ((Function<K, Boolean>)this.function).get(k);
            }
        }
        
        @Deprecated
        @Override
        public Boolean remove(final Object k) {
            synchronized (this.sync) {
                return ((Function<K, Boolean>)this.function).remove(k);
            }
        }
        
        @Override
        public boolean remove(final double k) {
            synchronized (this.sync) {
                return this.function.remove(k);
            }
        }
        
        @Override
        public boolean get(final double k) {
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
    
    public static class UnmodifiableFunction extends AbstractDouble2BooleanFunction implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2BooleanFunction function;
        
        protected UnmodifiableFunction(final Double2BooleanFunction f) {
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
        public boolean defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final boolean defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean put(final double k, final boolean v) {
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
        public boolean remove(final double k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean get(final double k) {
            return this.function.get(k);
        }
        
        @Override
        public boolean containsKey(final Object ok) {
            return this.function.containsKey(ok);
        }
    }
}
