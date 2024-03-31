// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentMap;

public abstract class ConstantPool<T extends Constant<T>>
{
    private final ConcurrentMap<String, T> constants;
    private final AtomicInteger nextId;
    
    public ConstantPool() {
        this.constants = PlatformDependent.newConcurrentHashMap();
        this.nextId = new AtomicInteger(1);
    }
    
    public T valueOf(final Class<?> firstNameComponent, final String secondNameComponent) {
        if (firstNameComponent == null) {
            throw new NullPointerException("firstNameComponent");
        }
        if (secondNameComponent == null) {
            throw new NullPointerException("secondNameComponent");
        }
        return this.valueOf(firstNameComponent.getName() + '#' + secondNameComponent);
    }
    
    public T valueOf(final String name) {
        checkNotNullAndNotEmpty(name);
        return this.getOrCreate(name);
    }
    
    private T getOrCreate(final String name) {
        T constant = this.constants.get(name);
        if (constant == null) {
            final T tempConstant = this.newConstant(this.nextId(), name);
            constant = this.constants.putIfAbsent(name, tempConstant);
            if (constant == null) {
                return tempConstant;
            }
        }
        return constant;
    }
    
    public boolean exists(final String name) {
        checkNotNullAndNotEmpty(name);
        return this.constants.containsKey(name);
    }
    
    public T newInstance(final String name) {
        checkNotNullAndNotEmpty(name);
        return this.createOrThrow(name);
    }
    
    private T createOrThrow(final String name) {
        T constant = this.constants.get(name);
        if (constant == null) {
            final T tempConstant = this.newConstant(this.nextId(), name);
            constant = this.constants.putIfAbsent(name, tempConstant);
            if (constant == null) {
                return tempConstant;
            }
        }
        throw new IllegalArgumentException(String.format("'%s' is already in use", name));
    }
    
    private static String checkNotNullAndNotEmpty(final String name) {
        ObjectUtil.checkNotNull(name, "name");
        if (name.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        return name;
    }
    
    protected abstract T newConstant(final int p0, final String p1);
    
    @Deprecated
    public final int nextId() {
        return this.nextId.getAndIncrement();
    }
}
