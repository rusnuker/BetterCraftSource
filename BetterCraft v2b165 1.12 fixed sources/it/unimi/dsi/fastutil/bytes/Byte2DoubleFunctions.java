// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.Function;
import java.io.Serializable;

public class Byte2DoubleFunctions
{
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Byte2DoubleFunctions() {
    }
    
    public static Byte2DoubleFunction singleton(final byte key, final double value) {
        return new Singleton(key, value);
    }
    
    public static Byte2DoubleFunction singleton(final Byte key, final Double value) {
        return new Singleton(key, value);
    }
    
    public static Byte2DoubleFunction synchronize(final Byte2DoubleFunction f) {
        return new SynchronizedFunction(f);
    }
    
    public static Byte2DoubleFunction synchronize(final Byte2DoubleFunction f, final Object sync) {
        return new SynchronizedFunction(f, sync);
    }
    
    public static Byte2DoubleFunction unmodifiable(final Byte2DoubleFunction f) {
        return new UnmodifiableFunction(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction extends AbstractByte2DoubleFunction implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        @Override
        public double get(final byte k) {
            return 0.0;
        }
        
        @Override
        public boolean containsKey(final byte k) {
            return false;
        }
        
        @Override
        public double defaultReturnValue() {
            return 0.0;
        }
        
        @Override
        public void defaultReturnValue(final double defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Double get(final Object k) {
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
            return Byte2DoubleFunctions.EMPTY_FUNCTION;
        }
        
        public Object clone() {
            return Byte2DoubleFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton extends AbstractByte2DoubleFunction implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final byte key;
        protected final double value;
        
        protected Singleton(final byte key, final double value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public boolean containsKey(final byte k) {
            return this.key == k;
        }
        
        @Override
        public double get(final byte k) {
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
    
    public static class SynchronizedFunction extends AbstractByte2DoubleFunction implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2DoubleFunction function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Byte2DoubleFunction f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Byte2DoubleFunction f) {
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
        public boolean containsKey(final byte k) {
            synchronized (this.sync) {
                return this.function.containsKey(k);
            }
        }
        
        @Override
        public double defaultReturnValue() {
            synchronized (this.sync) {
                return this.function.defaultReturnValue();
            }
        }
        
        @Override
        public void defaultReturnValue(final double defRetValue) {
            synchronized (this.sync) {
                this.function.defaultReturnValue(defRetValue);
            }
        }
        
        @Override
        public double put(final byte k, final double v) {
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
        public Double put(final Byte k, final Double v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Deprecated
        @Override
        public Double get(final Object k) {
            synchronized (this.sync) {
                return ((Function<K, Double>)this.function).get(k);
            }
        }
        
        @Deprecated
        @Override
        public Double remove(final Object k) {
            synchronized (this.sync) {
                return ((Function<K, Double>)this.function).remove(k);
            }
        }
        
        @Override
        public double remove(final byte k) {
            synchronized (this.sync) {
                return this.function.remove(k);
            }
        }
        
        @Override
        public double get(final byte k) {
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
    
    public static class UnmodifiableFunction extends AbstractByte2DoubleFunction implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2DoubleFunction function;
        
        protected UnmodifiableFunction(final Byte2DoubleFunction f) {
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
        public boolean containsKey(final byte k) {
            return this.function.containsKey(k);
        }
        
        @Override
        public double defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final double defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double put(final byte k, final double v) {
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
        public double remove(final byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public double get(final byte k) {
            return this.function.get(k);
        }
        
        @Override
        public boolean containsKey(final Object ok) {
            return this.function.containsKey(ok);
        }
    }
}
