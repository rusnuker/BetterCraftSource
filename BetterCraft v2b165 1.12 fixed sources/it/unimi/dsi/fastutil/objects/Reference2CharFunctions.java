// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public class Reference2CharFunctions
{
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Reference2CharFunctions() {
    }
    
    public static <K> Reference2CharFunction<K> singleton(final K key, final char value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2CharFunction<K> singleton(final K key, final Character value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2CharFunction<K> synchronize(final Reference2CharFunction<K> f) {
        return new SynchronizedFunction<K>(f);
    }
    
    public static <K> Reference2CharFunction<K> synchronize(final Reference2CharFunction<K> f, final Object sync) {
        return new SynchronizedFunction<K>(f, sync);
    }
    
    public static <K> Reference2CharFunction<K> unmodifiable(final Reference2CharFunction<K> f) {
        return new UnmodifiableFunction<K>(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction<K> extends AbstractReference2CharFunction<K> implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        @Override
        public char getChar(final Object k) {
            return '\0';
        }
        
        @Override
        public boolean containsKey(final Object k) {
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
        
        @Override
        public int size() {
            return 0;
        }
        
        @Override
        public void clear() {
        }
        
        private Object readResolve() {
            return Reference2CharFunctions.EMPTY_FUNCTION;
        }
        
        public Object clone() {
            return Reference2CharFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton<K> extends AbstractReference2CharFunction<K> implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final K key;
        protected final char value;
        
        protected Singleton(final K key, final char value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public boolean containsKey(final Object k) {
            return this.key == k;
        }
        
        @Override
        public char getChar(final Object k) {
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
    
    public static class SynchronizedFunction<K> extends AbstractReference2CharFunction<K> implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2CharFunction<K> function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Reference2CharFunction<K> f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Reference2CharFunction<K> f) {
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
        public char put(final K k, final char v) {
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
        public Character put(final K k, final Character v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Deprecated
        @Override
        public Character get(final Object k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        @Deprecated
        @Override
        public Character remove(final Object k) {
            synchronized (this.sync) {
                return this.function.remove(k);
            }
        }
        
        @Override
        public char removeChar(final Object k) {
            synchronized (this.sync) {
                return this.function.removeChar(k);
            }
        }
        
        @Override
        public char getChar(final Object k) {
            synchronized (this.sync) {
                return this.function.getChar(k);
            }
        }
    }
    
    public static class UnmodifiableFunction<K> extends AbstractReference2CharFunction<K> implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2CharFunction<K> function;
        
        protected UnmodifiableFunction(final Reference2CharFunction<K> f) {
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
        public char defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final char defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char put(final K k, final char v) {
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
        public char removeChar(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char getChar(final Object k) {
            return this.function.getChar(k);
        }
    }
}
