// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

import java.util.Collection;
import java.lang.reflect.Array;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterators;
import java.util.AbstractCollection;

public abstract class AbstractBooleanCollection extends AbstractCollection<Boolean> implements BooleanCollection
{
    protected AbstractBooleanCollection() {
    }
    
    @Override
    public boolean[] toArray(final boolean[] a) {
        return this.toBooleanArray(a);
    }
    
    @Override
    public boolean[] toBooleanArray() {
        return this.toBooleanArray(null);
    }
    
    @Override
    public boolean[] toBooleanArray(boolean[] a) {
        if (a == null || a.length < this.size()) {
            a = new boolean[this.size()];
        }
        BooleanIterators.unwrap(this.iterator(), a);
        return a;
    }
    
    @Override
    public boolean addAll(final BooleanCollection c) {
        boolean retVal = false;
        final BooleanIterator i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (this.add(i.nextBoolean())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean containsAll(final BooleanCollection c) {
        final BooleanIterator i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (!this.contains(i.nextBoolean())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean retainAll(final BooleanCollection c) {
        boolean retVal = false;
        int n = this.size();
        final BooleanIterator i = this.iterator();
        while (n-- != 0) {
            if (!c.contains(i.nextBoolean())) {
                i.remove();
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean removeAll(final BooleanCollection c) {
        boolean retVal = false;
        int n = c.size();
        final BooleanIterator i = c.iterator();
        while (n-- != 0) {
            if (this.rem(i.nextBoolean())) {
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
    public boolean addAll(final Collection<? extends Boolean> c) {
        boolean retVal = false;
        final Iterator<? extends Boolean> i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (this.add((Boolean)i.next())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean add(final boolean k) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public BooleanIterator booleanIterator() {
        return this.iterator();
    }
    
    @Override
    public abstract BooleanIterator iterator();
    
    @Override
    public boolean remove(final Object ok) {
        return ok != null && this.rem((boolean)ok);
    }
    
    @Override
    public boolean add(final Boolean o) {
        return this.add((boolean)o);
    }
    
    public boolean rem(final Object o) {
        return o != null && this.rem((boolean)o);
    }
    
    @Override
    public boolean contains(final Object o) {
        return o != null && this.contains((boolean)o);
    }
    
    @Override
    public boolean contains(final boolean k) {
        final BooleanIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextBoolean()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean rem(final boolean k) {
        final BooleanIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextBoolean()) {
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
        final BooleanIterator i = this.iterator();
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
            final boolean k = i.nextBoolean();
            s.append(String.valueOf(k));
        }
        s.append("}");
        return s.toString();
    }
}
