// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.util.Collection;
import java.lang.reflect.Array;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterators;
import java.util.AbstractCollection;

public abstract class AbstractIntCollection extends AbstractCollection<Integer> implements IntCollection
{
    protected AbstractIntCollection() {
    }
    
    @Override
    public int[] toArray(final int[] a) {
        return this.toIntArray(a);
    }
    
    @Override
    public int[] toIntArray() {
        return this.toIntArray(null);
    }
    
    @Override
    public int[] toIntArray(int[] a) {
        if (a == null || a.length < this.size()) {
            a = new int[this.size()];
        }
        IntIterators.unwrap(this.iterator(), a);
        return a;
    }
    
    @Override
    public boolean addAll(final IntCollection c) {
        boolean retVal = false;
        final IntIterator i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (this.add(i.nextInt())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean containsAll(final IntCollection c) {
        final IntIterator i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (!this.contains(i.nextInt())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean retainAll(final IntCollection c) {
        boolean retVal = false;
        int n = this.size();
        final IntIterator i = this.iterator();
        while (n-- != 0) {
            if (!c.contains(i.nextInt())) {
                i.remove();
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean removeAll(final IntCollection c) {
        boolean retVal = false;
        int n = c.size();
        final IntIterator i = c.iterator();
        while (n-- != 0) {
            if (this.rem(i.nextInt())) {
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
    public boolean addAll(final Collection<? extends Integer> c) {
        boolean retVal = false;
        final Iterator<? extends Integer> i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (this.add((Integer)i.next())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean add(final int k) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public IntIterator intIterator() {
        return this.iterator();
    }
    
    @Override
    public abstract IntIterator iterator();
    
    @Override
    public boolean remove(final Object ok) {
        return ok != null && this.rem((int)ok);
    }
    
    @Override
    public boolean add(final Integer o) {
        return this.add((int)o);
    }
    
    public boolean rem(final Object o) {
        return o != null && this.rem((int)o);
    }
    
    @Override
    public boolean contains(final Object o) {
        return o != null && this.contains((int)o);
    }
    
    @Override
    public boolean contains(final int k) {
        final IntIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextInt()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean rem(final int k) {
        final IntIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextInt()) {
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
        final IntIterator i = this.iterator();
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
            final int k = i.nextInt();
            s.append(String.valueOf(k));
        }
        s.append("}");
        return s.toString();
    }
}
