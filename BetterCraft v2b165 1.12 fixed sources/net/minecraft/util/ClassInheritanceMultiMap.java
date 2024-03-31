// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util;

import java.util.Collections;
import com.google.common.collect.Iterators;
import java.util.Iterator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.AbstractSet;

public class ClassInheritanceMultiMap<T> extends AbstractSet<T>
{
    private static final Set<Class<?>> ALL_KNOWN;
    private final Map<Class<?>, List<T>> map;
    private final Set<Class<?>> knownKeys;
    private final Class<T> baseClass;
    private final List<T> values;
    
    static {
        ALL_KNOWN = Sets.newHashSet();
    }
    
    public ClassInheritanceMultiMap(final Class<T> baseClassIn) {
        this.map = (Map<Class<?>, List<T>>)Maps.newHashMap();
        this.knownKeys = Sets.newIdentityHashSet();
        this.values = (List<T>)Lists.newArrayList();
        this.baseClass = baseClassIn;
        this.knownKeys.add(baseClassIn);
        this.map.put((Class<?>)baseClassIn, (List<?>)this.values);
        for (final Class<?> oclass : ClassInheritanceMultiMap.ALL_KNOWN) {
            this.createLookup(oclass);
        }
    }
    
    protected void createLookup(final Class<?> clazz) {
        ClassInheritanceMultiMap.ALL_KNOWN.add(clazz);
        for (final T t : this.values) {
            if (clazz.isAssignableFrom(t.getClass())) {
                this.addForClass(t, clazz);
            }
        }
        this.knownKeys.add(clazz);
    }
    
    protected Class<?> initializeClassLookup(final Class<?> clazz) {
        if (this.baseClass.isAssignableFrom(clazz)) {
            if (!this.knownKeys.contains(clazz)) {
                this.createLookup(clazz);
            }
            return clazz;
        }
        throw new IllegalArgumentException("Don't know how to search for " + clazz);
    }
    
    @Override
    public boolean add(final T p_add_1_) {
        for (final Class<?> oclass : this.knownKeys) {
            if (oclass.isAssignableFrom(p_add_1_.getClass())) {
                this.addForClass(p_add_1_, oclass);
            }
        }
        return true;
    }
    
    private void addForClass(final T value, final Class<?> parentClass) {
        final List<T> list = this.map.get(parentClass);
        if (list == null) {
            this.map.put(parentClass, Lists.newArrayList(value));
        }
        else {
            list.add(value);
        }
    }
    
    @Override
    public boolean remove(final Object p_remove_1_) {
        final T t = (T)p_remove_1_;
        boolean flag = false;
        for (final Class<?> oclass : this.knownKeys) {
            if (oclass.isAssignableFrom(t.getClass())) {
                final List<T> list = this.map.get(oclass);
                if (list == null || !list.remove(t)) {
                    continue;
                }
                flag = true;
            }
        }
        return flag;
    }
    
    @Override
    public boolean contains(final Object p_contains_1_) {
        return Iterators.contains(this.getByClass(p_contains_1_.getClass()).iterator(), p_contains_1_);
    }
    
    public <S> Iterable<S> getByClass(final Class<S> clazz) {
        return new Iterable<S>() {
            @Override
            public Iterator<S> iterator() {
                final List<T> list = ClassInheritanceMultiMap.this.map.get(ClassInheritanceMultiMap.this.initializeClassLookup(clazz));
                if (list == null) {
                    return Collections.emptyIterator();
                }
                final Iterator<T> iterator = list.iterator();
                return (Iterator<S>)Iterators.filter(iterator, (Class<Object>)clazz);
            }
        };
    }
    
    @Override
    public Iterator<T> iterator() {
        return (Iterator<T>)(this.values.isEmpty() ? Collections.emptyIterator() : Iterators.unmodifiableIterator((Iterator<?>)this.values.iterator()));
    }
    
    @Override
    public int size() {
        return this.values.size();
    }
}
