// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.Function;
import java.io.Serializable;

public class Long2CharFunctions
{
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Long2CharFunctions() {
    }
    
    public static Long2CharFunction singleton(final long key, final char value) {
        return new Singleton(key, value);
    }
    
    public static Long2CharFunction singleton(final Long key, final Character value) {
        return new Singleton(key, value);
    }
    
    public static Long2CharFunction synchronize(final Long2CharFunction f) {
        return new SynchronizedFunction(f);
    }
    
    public static Long2CharFunction synchronize(final Long2CharFunction f, final Object sync) {
        return new SynchronizedFunction(f, sync);
    }
    
    public static Long2CharFunction unmodifiable(final Long2CharFunction f) {
        return new UnmodifiableFunction(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction extends AbstractLong2CharFunction implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        @Override
        public char get(final long k) {
            return '\0';
        }
        
        @Override
        public boolean containsKey(final long k) {
            return false;
        }
        
        @Override
        public char defaultReturnValue() {
            return '\0';
        }
        
        @Override
        public void defaultReturnValue(final char defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character get(final Object k) {
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
            return Long2CharFunctions.EMPTY_FUNCTION;
        }
        
        public Object clone() {
            return Long2CharFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton extends AbstractLong2CharFunction implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final long key;
        protected final char value;
        
        protected Singleton(final long key, final char value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public boolean containsKey(final long k) {
            return this.key == k;
        }
        
        @Override
        public char get(final long k) {
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
    
    public static class SynchronizedFunction extends AbstractLong2CharFunction implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2CharFunction function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Long2CharFunction f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Long2CharFunction f) {
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
        public boolean containsKey(final long k) {
            synchronized (this.sync) {
                return this.function.containsKey(k);
            }
        }
        
        @Override
        public char defaultReturnValue() {
            synchronized (this.sync) {
                return this.function.defaultReturnValue();
            }
        }
        
        @Override
        public void defaultReturnValue(final char defRetValue) {
            synchronized (this.sync) {
                this.function.defaultReturnValue(defRetValue);
            }
        }
        
        @Override
        public char put(final long k, final char v) {
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
        public Character put(final Long k, final Character v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Deprecated
        @Override
        public Character get(final Object k) {
            synchronized (this.sync) {
                return ((Function<K, Character>)this.function).get(k);
            }
        }
        
        @Deprecated
        @Override
        public Character remove(final Object k) {
            synchronized (this.sync) {
                return ((Function<K, Character>)this.function).remove(k);
            }
        }
        
        @Override
        public char remove(final long k) {
            synchronized (this.sync) {
                return this.function.remove(k);
            }
        }
        
        @Override
        public char get(final long k) {
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
    
    public static class UnmodifiableFunction extends AbstractLong2CharFunction implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2CharFunction function;
        
        protected UnmodifiableFunction(final Long2CharFunction f) {
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
        public boolean containsKey(final long k) {
            return this.function.containsKey(k);
        }
        
        @Override
        public char defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final char defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char put(final long k, final char v) {
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
        public char remove(final long k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public char get(final long k) {
            return this.function.get(k);
        }
        
        @Override
        public boolean containsKey(final Object ok) {
            return this.function.containsKey(ok);
        }
    }
}
