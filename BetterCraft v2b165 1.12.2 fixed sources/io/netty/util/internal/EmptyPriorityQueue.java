// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal;

import java.util.NoSuchElementException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public final class EmptyPriorityQueue<T> implements PriorityQueue<T>
{
    private static final PriorityQueue<Object> INSTANCE;
    
    private EmptyPriorityQueue() {
    }
    
    public static <V> EmptyPriorityQueue<V> instance() {
        return (EmptyPriorityQueue)EmptyPriorityQueue.INSTANCE;
    }
    
    @Override
    public boolean removeTyped(final T node) {
        return false;
    }
    
    @Override
    public boolean containsTyped(final T node) {
        return false;
    }
    
    @Override
    public void priorityChanged(final T node) {
    }
    
    @Override
    public int size() {
        return 0;
    }
    
    @Override
    public boolean isEmpty() {
        return true;
    }
    
    @Override
    public boolean contains(final Object o) {
        return false;
    }
    
    @Override
    public Iterator<T> iterator() {
        return Collections.emptyList().iterator();
    }
    
    @Override
    public Object[] toArray() {
        return EmptyArrays.EMPTY_OBJECTS;
    }
    
    @Override
    public <T1> T1[] toArray(final T1[] a) {
        if (a.length > 0) {
            a[0] = null;
        }
        return a;
    }
    
    @Override
    public boolean add(final T t) {
        return false;
    }
    
    @Override
    public boolean remove(final Object o) {
        return false;
    }
    
    @Override
    public boolean containsAll(final Collection<?> c) {
        return false;
    }
    
    @Override
    public boolean addAll(final Collection<? extends T> c) {
        return false;
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        return false;
    }
    
    @Override
    public boolean retainAll(final Collection<?> c) {
        return false;
    }
    
    @Override
    public void clear() {
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof PriorityQueue && ((PriorityQueue)o).isEmpty();
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    @Override
    public boolean offer(final T t) {
        return false;
    }
    
    @Override
    public T remove() {
        throw new NoSuchElementException();
    }
    
    @Override
    public T poll() {
        return null;
    }
    
    @Override
    public T element() {
        throw new NoSuchElementException();
    }
    
    @Override
    public T peek() {
        return null;
    }
    
    @Override
    public String toString() {
        return EmptyPriorityQueue.class.getSimpleName();
    }
    
    static {
        INSTANCE = new EmptyPriorityQueue<Object>();
    }
}
