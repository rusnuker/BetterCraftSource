// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Collection;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.AbstractCollection;

public abstract class AbstractReferenceCollection<K> extends AbstractCollection<K> implements ReferenceCollection<K>
{
    protected AbstractReferenceCollection() {
    }
    
    @Override
    public Object[] toArray() {
        final Object[] a = new Object[this.size()];
        ObjectIterators.unwrap(this.iterator(), a);
        return a;
    }
    
    @Override
    public <T> T[] toArray(T[] a) {
        final int size = this.size();
        if (a.length < size) {
            a = (T[])Array.newInstance(a.getClass().getComponentType(), size);
        }
        ObjectIterators.unwrap((Iterator<? extends T>)this.iterator(), a);
        if (size < a.length) {
            a[size] = null;
        }
        return a;
    }
    
    @Override
    public boolean addAll(final Collection<? extends K> c) {
        boolean retVal = false;
        final Iterator<? extends K> i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (this.add(i.next())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean add(final K k) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public ObjectIterator<K> objectIterator() {
        return this.iterator();
    }
    
    @Override
    public abstract ObjectIterator<K> iterator();
    
    @Override
    public boolean remove(final Object o) {
        return this.rem(o);
    }
    
    public boolean rem(final Object k) {
        final ObjectIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.next()) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsAll(final Collection<?> c) {
        int n = c.size();
        final Iterator<?> i = c.iterator();
        while (n-- != 0) {
            if (!this.contains(i.next())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean retainAll(final Collection<?> c) {
        boolean retVal = false;
        int n = this.size();
        final Iterator<?> i = this.iterator();
        while (n-- != 0) {
            if (!c.contains(i.next())) {
                i.remove();
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        boolean retVal = false;
        int n = c.size();
        final Iterator<?> i = c.iterator();
        while (n-- != 0) {
            if (this.remove(i.next())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<K> i = this.iterator();
        int n = this.size();
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) {
                first = false;
            }
            else {
                s.append(", ");
            }
            final Object k = i.next();
            if (this == k) {
                s.append("(this collection)");
            }
            else {
                s.append(String.valueOf(k));
            }
        }
        s.append("}");
        return s.toString();
    }
}
