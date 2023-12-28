// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal;

import java.util.WeakHashMap;
import java.util.ArrayList;
import java.nio.charset.CharsetDecoder;
import java.util.IdentityHashMap;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Arrays;
import io.netty.util.concurrent.FastThreadLocalThread;

public final class InternalThreadLocalMap extends UnpaddedInternalThreadLocalMap
{
    private static final int DEFAULT_ARRAY_LIST_INITIAL_CAPACITY = 8;
    public static final Object UNSET;
    public long rp1;
    public long rp2;
    public long rp3;
    public long rp4;
    public long rp5;
    public long rp6;
    public long rp7;
    public long rp8;
    public long rp9;
    
    public static InternalThreadLocalMap getIfSet() {
        final Thread thread = Thread.currentThread();
        if (thread instanceof FastThreadLocalThread) {
            return ((FastThreadLocalThread)thread).threadLocalMap();
        }
        return InternalThreadLocalMap.slowThreadLocalMap.get();
    }
    
    public static InternalThreadLocalMap get() {
        final Thread thread = Thread.currentThread();
        if (thread instanceof FastThreadLocalThread) {
            return fastGet((FastThreadLocalThread)thread);
        }
        return slowGet();
    }
    
    private static InternalThreadLocalMap fastGet(final FastThreadLocalThread thread) {
        InternalThreadLocalMap threadLocalMap = thread.threadLocalMap();
        if (threadLocalMap == null) {
            thread.setThreadLocalMap(threadLocalMap = new InternalThreadLocalMap());
        }
        return threadLocalMap;
    }
    
    private static InternalThreadLocalMap slowGet() {
        final ThreadLocal<InternalThreadLocalMap> slowThreadLocalMap = UnpaddedInternalThreadLocalMap.slowThreadLocalMap;
        InternalThreadLocalMap ret = slowThreadLocalMap.get();
        if (ret == null) {
            ret = new InternalThreadLocalMap();
            slowThreadLocalMap.set(ret);
        }
        return ret;
    }
    
    public static void remove() {
        final Thread thread = Thread.currentThread();
        if (thread instanceof FastThreadLocalThread) {
            ((FastThreadLocalThread)thread).setThreadLocalMap(null);
        }
        else {
            InternalThreadLocalMap.slowThreadLocalMap.remove();
        }
    }
    
    public static void destroy() {
        InternalThreadLocalMap.slowThreadLocalMap.remove();
    }
    
    public static int nextVariableIndex() {
        final int index = InternalThreadLocalMap.nextIndex.getAndIncrement();
        if (index < 0) {
            InternalThreadLocalMap.nextIndex.decrementAndGet();
            throw new IllegalStateException("too many thread-local indexed variables");
        }
        return index;
    }
    
    public static int lastVariableIndex() {
        return InternalThreadLocalMap.nextIndex.get() - 1;
    }
    
    private InternalThreadLocalMap() {
        super(newIndexedVariableTable());
    }
    
    private static Object[] newIndexedVariableTable() {
        final Object[] array = new Object[32];
        Arrays.fill(array, InternalThreadLocalMap.UNSET);
        return array;
    }
    
    public int size() {
        int count = 0;
        if (this.futureListenerStackDepth != 0) {
            ++count;
        }
        if (this.localChannelReaderStackDepth != 0) {
            ++count;
        }
        if (this.handlerSharableCache != null) {
            ++count;
        }
        if (this.counterHashCode != null) {
            ++count;
        }
        if (this.random != null) {
            ++count;
        }
        if (this.typeParameterMatcherGetCache != null) {
            ++count;
        }
        if (this.typeParameterMatcherFindCache != null) {
            ++count;
        }
        if (this.stringBuilder != null) {
            ++count;
        }
        if (this.charsetEncoderCache != null) {
            ++count;
        }
        if (this.charsetDecoderCache != null) {
            ++count;
        }
        if (this.arrayList != null) {
            ++count;
        }
        for (final Object o : this.indexedVariables) {
            if (o != InternalThreadLocalMap.UNSET) {
                ++count;
            }
        }
        return count - 1;
    }
    
    public StringBuilder stringBuilder() {
        StringBuilder builder = this.stringBuilder;
        if (builder == null) {
            builder = (this.stringBuilder = new StringBuilder(512));
        }
        else {
            builder.setLength(0);
        }
        return builder;
    }
    
    public Map<Charset, CharsetEncoder> charsetEncoderCache() {
        Map<Charset, CharsetEncoder> cache = this.charsetEncoderCache;
        if (cache == null) {
            cache = (this.charsetEncoderCache = new IdentityHashMap<Charset, CharsetEncoder>());
        }
        return cache;
    }
    
    public Map<Charset, CharsetDecoder> charsetDecoderCache() {
        Map<Charset, CharsetDecoder> cache = this.charsetDecoderCache;
        if (cache == null) {
            cache = (this.charsetDecoderCache = new IdentityHashMap<Charset, CharsetDecoder>());
        }
        return cache;
    }
    
    public <E> ArrayList<E> arrayList() {
        return this.arrayList(8);
    }
    
    public <E> ArrayList<E> arrayList(final int minCapacity) {
        final ArrayList<E> list = (ArrayList<E>)this.arrayList;
        if (list == null) {
            return (ArrayList<E>)(this.arrayList = new ArrayList<Object>(minCapacity));
        }
        list.clear();
        list.ensureCapacity(minCapacity);
        return list;
    }
    
    public int futureListenerStackDepth() {
        return this.futureListenerStackDepth;
    }
    
    public void setFutureListenerStackDepth(final int futureListenerStackDepth) {
        this.futureListenerStackDepth = futureListenerStackDepth;
    }
    
    public ThreadLocalRandom random() {
        ThreadLocalRandom r = this.random;
        if (r == null) {
            r = (this.random = new ThreadLocalRandom());
        }
        return r;
    }
    
    public Map<Class<?>, TypeParameterMatcher> typeParameterMatcherGetCache() {
        Map<Class<?>, TypeParameterMatcher> cache = this.typeParameterMatcherGetCache;
        if (cache == null) {
            cache = (this.typeParameterMatcherGetCache = new IdentityHashMap<Class<?>, TypeParameterMatcher>());
        }
        return cache;
    }
    
    public Map<Class<?>, Map<String, TypeParameterMatcher>> typeParameterMatcherFindCache() {
        Map<Class<?>, Map<String, TypeParameterMatcher>> cache = this.typeParameterMatcherFindCache;
        if (cache == null) {
            cache = (this.typeParameterMatcherFindCache = new IdentityHashMap<Class<?>, Map<String, TypeParameterMatcher>>());
        }
        return cache;
    }
    
    public IntegerHolder counterHashCode() {
        return this.counterHashCode;
    }
    
    public void setCounterHashCode(final IntegerHolder counterHashCode) {
        this.counterHashCode = counterHashCode;
    }
    
    public Map<Class<?>, Boolean> handlerSharableCache() {
        Map<Class<?>, Boolean> cache = this.handlerSharableCache;
        if (cache == null) {
            cache = (this.handlerSharableCache = new WeakHashMap<Class<?>, Boolean>(4));
        }
        return cache;
    }
    
    public int localChannelReaderStackDepth() {
        return this.localChannelReaderStackDepth;
    }
    
    public void setLocalChannelReaderStackDepth(final int localChannelReaderStackDepth) {
        this.localChannelReaderStackDepth = localChannelReaderStackDepth;
    }
    
    public Object indexedVariable(final int index) {
        final Object[] lookup = this.indexedVariables;
        return (index < lookup.length) ? lookup[index] : InternalThreadLocalMap.UNSET;
    }
    
    public boolean setIndexedVariable(final int index, final Object value) {
        final Object[] lookup = this.indexedVariables;
        if (index < lookup.length) {
            final Object oldValue = lookup[index];
            lookup[index] = value;
            return oldValue == InternalThreadLocalMap.UNSET;
        }
        this.expandIndexedVariableTableAndSet(index, value);
        return true;
    }
    
    private void expandIndexedVariableTableAndSet(final int index, final Object value) {
        final Object[] oldArray = this.indexedVariables;
        final int oldCapacity = oldArray.length;
        int newCapacity = index;
        newCapacity |= newCapacity >>> 1;
        newCapacity |= newCapacity >>> 2;
        newCapacity |= newCapacity >>> 4;
        newCapacity |= newCapacity >>> 8;
        newCapacity |= newCapacity >>> 16;
        ++newCapacity;
        final Object[] newArray = Arrays.copyOf(oldArray, newCapacity);
        Arrays.fill(newArray, oldCapacity, newArray.length, InternalThreadLocalMap.UNSET);
        newArray[index] = value;
        this.indexedVariables = newArray;
    }
    
    public Object removeIndexedVariable(final int index) {
        final Object[] lookup = this.indexedVariables;
        if (index < lookup.length) {
            final Object v = lookup[index];
            lookup[index] = InternalThreadLocalMap.UNSET;
            return v;
        }
        return InternalThreadLocalMap.UNSET;
    }
    
    public boolean isIndexedVariableSet(final int index) {
        final Object[] lookup = this.indexedVariables;
        return index < lookup.length && lookup[index] != InternalThreadLocalMap.UNSET;
    }
    
    static {
        UNSET = new Object();
    }
}
