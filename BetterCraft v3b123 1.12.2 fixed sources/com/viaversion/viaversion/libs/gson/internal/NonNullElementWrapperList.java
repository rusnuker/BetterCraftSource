// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson.internal;

import java.util.Collection;
import java.util.Objects;
import java.util.ArrayList;
import java.util.RandomAccess;
import java.util.AbstractList;

public class NonNullElementWrapperList<E> extends AbstractList<E> implements RandomAccess
{
    private final ArrayList<E> delegate;
    
    public NonNullElementWrapperList(final ArrayList<E> delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }
    
    @Override
    public E get(final int index) {
        return this.delegate.get(index);
    }
    
    @Override
    public int size() {
        return this.delegate.size();
    }
    
    private E nonNull(final E element) {
        if (element == null) {
            throw new NullPointerException("Element must be non-null");
        }
        return element;
    }
    
    @Override
    public E set(final int index, final E element) {
        return this.delegate.set(index, this.nonNull(element));
    }
    
    @Override
    public void add(final int index, final E element) {
        this.delegate.add(index, this.nonNull(element));
    }
    
    @Override
    public E remove(final int index) {
        return this.delegate.remove(index);
    }
    
    @Override
    public void clear() {
        this.delegate.clear();
    }
    
    @Override
    public boolean remove(final Object o) {
        return this.delegate.remove(o);
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        return this.delegate.removeAll(c);
    }
    
    @Override
    public boolean retainAll(final Collection<?> c) {
        return this.delegate.retainAll(c);
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.delegate.contains(o);
    }
    
    @Override
    public int indexOf(final Object o) {
        return this.delegate.indexOf(o);
    }
    
    @Override
    public int lastIndexOf(final Object o) {
        return this.delegate.lastIndexOf(o);
    }
    
    @Override
    public Object[] toArray() {
        return this.delegate.toArray();
    }
    
    @Override
    public <T> T[] toArray(final T[] a) {
        return this.delegate.toArray(a);
    }
    
    @Override
    public boolean equals(final Object o) {
        return this.delegate.equals(o);
    }
    
    @Override
    public int hashCode() {
        return this.delegate.hashCode();
    }
}
