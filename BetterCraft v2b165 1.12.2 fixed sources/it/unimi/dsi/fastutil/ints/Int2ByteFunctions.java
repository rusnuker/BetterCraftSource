// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Function;
import java.io.Serializable;

public class Int2ByteFunctions
{
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Int2ByteFunctions() {
    }
    
    public static Int2ByteFunction singleton(final int key, final byte value) {
        return new Singleton(key, value);
    }
    
    public static Int2ByteFunction singleton(final Integer key, final Byte value) {
        return new Singleton(key, value);
    }
    
    public static Int2ByteFunction synchronize(final Int2ByteFunction f) {
        return new SynchronizedFunction(f);
    }
    
    public static Int2ByteFunction synchronize(final Int2ByteFunction f, final Object sync) {
        return new SynchronizedFunction(f, sync);
    }
    
    public static Int2ByteFunction unmodifiable(final Int2ByteFunction f) {
        return new UnmodifiableFunction(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction extends AbstractInt2ByteFunction implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        @Override
        public byte get(final int k) {
            return 0;
        }
        
        @Override
        public boolean containsKey(final int k) {
            return false;
        }
        
        @Override
        public byte defaultReturnValue() {
            return 0;
        }
        
        @Override
        public void defaultReturnValue(final byte defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Byte get(final Object k) {
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
            return Int2ByteFunctions.EMPTY_FUNCTION;
        }
        
        public Object clone() {
            return Int2ByteFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton extends AbstractInt2ByteFunction implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final int key;
        protected final byte value;
        
        protected Singleton(final int key, final byte value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public boolean containsKey(final int k) {
            return this.key == k;
        }
        
        @Override
        public byte get(final int k) {
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
    
    public static class SynchronizedFunction extends AbstractInt2ByteFunction implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2ByteFunction function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Int2ByteFunction f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Int2ByteFunction f) {
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
        public boolean containsKey(final int k) {
            synchronized (this.sync) {
                return this.function.containsKey(k);
            }
        }
        
        @Override
        public byte defaultReturnValue() {
            synchronized (this.sync) {
                return this.function.defaultReturnValue();
            }
        }
        
        @Override
        public void defaultReturnValue(final byte defRetValue) {
            synchronized (this.sync) {
                this.function.defaultReturnValue(defRetValue);
            }
        }
        
        @Override
        public byte put(final int k, final byte v) {
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
        public Byte put(final Integer k, final Byte v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Deprecated
        @Override
        public Byte get(final Object k) {
            synchronized (this.sync) {
                return ((Function<K, Byte>)this.function).get(k);
            }
        }
        
        @Deprecated
        @Override
        public Byte remove(final Object k) {
            synchronized (this.sync) {
                return ((Function<K, Byte>)this.function).remove(k);
            }
        }
        
        @Override
        public byte remove(final int k) {
            synchronized (this.sync) {
                return this.function.remove(k);
            }
        }
        
        @Override
        public byte get(final int k) {
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
    
    public static class UnmodifiableFunction extends AbstractInt2ByteFunction implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2ByteFunction function;
        
        protected UnmodifiableFunction(final Int2ByteFunction f) {
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
        public boolean containsKey(final int k) {
            return this.function.containsKey(k);
        }
        
        @Override
        public byte defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final byte defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte put(final int k, final byte v) {
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
        public byte remove(final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public byte get(final int k) {
            return this.function.get(k);
        }
        
        @Override
        public boolean containsKey(final Object ok) {
            return this.function.containsKey(ok);
        }
    }
}
