// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.util.Collection;
import java.lang.reflect.Array;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterators;
import java.util.AbstractCollection;

public abstract class AbstractShortCollection extends AbstractCollection<Short> implements ShortCollection
{
    protected AbstractShortCollection() {
    }
    
    @Override
    public short[] toArray(final short[] a) {
        return this.toShortArray(a);
    }
    
    @Override
    public short[] toShortArray() {
        return this.toShortArray(null);
    }
    
    @Override
    public short[] toShortArray(short[] a) {
        if (a == null || a.length < this.size()) {
            a = new short[this.size()];
        }
        ShortIterators.unwrap(this.iterator(), a);
        return a;
    }
    
    @Override
    public boolean addAll(final ShortCollection c) {
        boolean retVal = false;
        final ShortIterator i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (this.add(i.nextShort())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean containsAll(final ShortCollection c) {
        final ShortIterator i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (!this.contains(i.nextShort())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean retainAll(final ShortCollection c) {
        boolean retVal = false;
        int n = this.size();
        final ShortIterator i = this.iterator();
        while (n-- != 0) {
            if (!c.contains(i.nextShort())) {
                i.remove();
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean removeAll(final ShortCollection c) {
        boolean retVal = false;
        int n = c.size();
        final ShortIterator i = c.iterator();
        while (n-- != 0) {
            if (this.rem(i.nextShort())) {
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
    public boolean addAll(final Collection<? extends Short> c) {
        boolean retVal = false;
        final Iterator<? extends Short> i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (this.add((Short)i.next())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean add(final short k) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public ShortIterator shortIterator() {
        return this.iterator();
    }
    
    @Override
    public abstract ShortIterator iterator();
    
    @Override
    public boolean remove(final Object ok) {
        return ok != null && this.rem((short)ok);
    }
    
    @Override
    public boolean add(final Short o) {
        return this.add((short)o);
    }
    
    public boolean rem(final Object o) {
        return o != null && this.rem((short)o);
    }
    
    @Override
    public boolean contains(final Object o) {
        return o != null && this.contains((short)o);
    }
    
    @Override
    public boolean contains(final short k) {
        final ShortIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextShort()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean rem(final short k) {
        final ShortIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextShort()) {
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
        final ShortIterator i = this.iterator();
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
            final short k = i.nextShort();
            s.append(String.valueOf(k));
        }
        s.append("}");
        return s.toString();
    }
}
