// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import java.util.Collection;
import java.lang.reflect.Array;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterators;
import java.util.AbstractCollection;

public abstract class AbstractDoubleCollection extends AbstractCollection<Double> implements DoubleCollection
{
    protected AbstractDoubleCollection() {
    }
    
    @Override
    public double[] toArray(final double[] a) {
        return this.toDoubleArray(a);
    }
    
    @Override
    public double[] toDoubleArray() {
        return this.toDoubleArray(null);
    }
    
    @Override
    public double[] toDoubleArray(double[] a) {
        if (a == null || a.length < this.size()) {
            a = new double[this.size()];
        }
        DoubleIterators.unwrap(this.iterator(), a);
        return a;
    }
    
    @Override
    public boolean addAll(final DoubleCollection c) {
        boolean retVal = false;
        final DoubleIterator i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (this.add(i.nextDouble())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean containsAll(final DoubleCollection c) {
        final DoubleIterator i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (!this.contains(i.nextDouble())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean retainAll(final DoubleCollection c) {
        boolean retVal = false;
        int n = this.size();
        final DoubleIterator i = this.iterator();
        while (n-- != 0) {
            if (!c.contains(i.nextDouble())) {
                i.remove();
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean removeAll(final DoubleCollection c) {
        boolean retVal = false;
        int n = c.size();
        final DoubleIterator i = c.iterator();
        while (n-- != 0) {
            if (this.rem(i.nextDouble())) {
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
    public boolean addAll(final Collection<? extends Double> c) {
        boolean retVal = false;
        final Iterator<? extends Double> i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (this.add((Double)i.next())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean add(final double k) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public DoubleIterator doubleIterator() {
        return this.iterator();
    }
    
    @Override
    public abstract DoubleIterator iterator();
    
    @Override
    public boolean remove(final Object ok) {
        return ok != null && this.rem((double)ok);
    }
    
    @Override
    public boolean add(final Double o) {
        return this.add((double)o);
    }
    
    public boolean rem(final Object o) {
        return o != null && this.rem((double)o);
    }
    
    @Override
    public boolean contains(final Object o) {
        return o != null && this.contains((double)o);
    }
    
    @Override
    public boolean contains(final double k) {
        final DoubleIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextDouble()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean rem(final double k) {
        final DoubleIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextDouble()) {
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
        final DoubleIterator i = this.iterator();
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
            final double k = i.nextDouble();
            s.append(String.valueOf(k));
        }
        s.append("}");
        return s.toString();
    }
}
