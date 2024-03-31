// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.Function;
import java.io.Serializable;

public class Char2IntFunctions
{
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Char2IntFunctions() {
    }
    
    public static Char2IntFunction singleton(final char key, final int value) {
        return new Singleton(key, value);
    }
    
    public static Char2IntFunction singleton(final Character key, final Integer value) {
        return new Singleton(key, value);
    }
    
    public static Char2IntFunction synchronize(final Char2IntFunction f) {
        return new SynchronizedFunction(f);
    }
    
    public static Char2IntFunction synchronize(final Char2IntFunction f, final Object sync) {
        return new SynchronizedFunction(f, sync);
    }
    
    public static Char2IntFunction unmodifiable(final Char2IntFunction f) {
        return new UnmodifiableFunction(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction extends AbstractChar2IntFunction implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        @Override
        public int get(final char k) {
            return 0;
        }
        
        @Override
        public boolean containsKey(final char k) {
            return false;
        }
        
        @Override
        public int defaultReturnValue() {
            return 0;
        }
        
        @Override
        public void defaultReturnValue(final int defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer get(final Object k) {
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
            return Char2IntFunctions.EMPTY_FUNCTION;
        }
        
        public Object clone() {
            return Char2IntFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton extends AbstractChar2IntFunction implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final char key;
        protected final int value;
        
        protected Singleton(final char key, final int value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public boolean containsKey(final char k) {
            return this.key == k;
        }
        
        @Override
        public int get(final char k) {
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
    
    public static class SynchronizedFunction extends AbstractChar2IntFunction implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Char2IntFunction function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Char2IntFunction f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Char2IntFunction f) {
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
        public int defaultReturnValue() {
            synchronized (this.sync) {
                return this.function.defaultReturnValue();
            }
        }
        
        @Override
        public void defaultReturnValue(final int defRetValue) {
            synchronized (this.sync) {
                this.function.defaultReturnValue(defRetValue);
            }
        }
        
        @Override
        public int put(final char k, final int v) {
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
        public Integer put(final Character k, final Integer v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Deprecated
        @Override
        public Integer get(final Object k) {
            synchronized (this.sync) {
                return ((Function<K, Integer>)this.function).get(k);
            }
        }
        
        @Deprecated
        @Override
        public Integer remove(final Object k) {
            synchronized (this.sync) {
                return ((Function<K, Integer>)this.function).remove(k);
            }
        }
        
        @Override
        public int remove(final char k) {
            synchronized (this.sync) {
                return this.function.remove(k);
            }
        }
        
        @Override
        public int get(final char k) {
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
    
    public static class UnmodifiableFunction extends AbstractChar2IntFunction implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Char2IntFunction function;
        
        protected UnmodifiableFunction(final Char2IntFunction f) {
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
        public int defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final int defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int put(final char k, final int v) {
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
        public int remove(final char k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public int get(final char k) {
            return this.function.get(k);
        }
        
        @Override
        public boolean containsKey(final Object ok) {
            return this.function.containsKey(ok);
        }
    }
}
