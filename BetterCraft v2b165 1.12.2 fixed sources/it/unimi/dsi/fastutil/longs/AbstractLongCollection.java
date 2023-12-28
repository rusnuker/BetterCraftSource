// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.util.Collection;
import java.lang.reflect.Array;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterators;
import java.util.AbstractCollection;

public abstract class AbstractLongCollection extends AbstractCollection<Long> implements LongCollection
{
    protected AbstractLongCollection() {
    }
    
    @Override
    public long[] toArray(final long[] a) {
        return this.toLongArray(a);
    }
    
    @Override
    public long[] toLongArray() {
        return this.toLongArray(null);
    }
    
    @Override
    public long[] toLongArray(long[] a) {
        if (a == null || a.length < this.size()) {
            a = new long[this.size()];
        }
        LongIterators.unwrap(this.iterator(), a);
        return a;
    }
    
    @Override
    public boolean addAll(final LongCollection c) {
        boolean retVal = false;
        final LongIterator i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (this.add(i.nextLong())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean containsAll(final LongCollection c) {
        final LongIterator i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (!this.contains(i.nextLong())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean retainAll(final LongCollection c) {
        boolean retVal = false;
        int n = this.size();
        final LongIterator i = this.iterator();
        while (n-- != 0) {
            if (!c.contains(i.nextLong())) {
                i.remove();
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean removeAll(final LongCollection c) {
        boolean retVal = false;
        int n = c.size();
        final LongIterator i = c.iterator();
        while (n-- != 0) {
            if (this.rem(i.nextLong())) {
                retVal = true;
            }
        }
        return retVal;
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
    public boolean addAll(final Collection<? extends Long> c) {
        boolean retVal = false;
        final Iterator<? extends Long> i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (this.add((Long)i.next())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean add(final long k) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public LongIterator longIterator() {
        return this.iterator();
    }
    
    @Override
    public abstract LongIterator iterator();
    
    @Override
    public boolean remove(final Object ok) {
        return ok != null && this.rem((long)ok);
    }
    
    @Override
    public boolean add(final Long o) {
        return this.add((long)o);
    }
    
    public boolean rem(final Object o) {
        return o != null && this.rem((long)o);
    }
    
    @Override
    public boolean contains(final Object o) {
        return o != null && this.contains((long)o);
    }
    
    @Override
    public boolean contains(final long k) {
        final LongIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextLong()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean rem(final long k) {
        final LongIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextLong()) {
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
        final LongIterator i = this.iterator();
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
            final long k = i.nextLong();
            s.append(String.valueOf(k));
        }
        s.append("}");
        return s.toString();
    }
}
