// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.util.Collection;
import java.lang.reflect.Array;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterators;
import java.util.AbstractCollection;

public abstract class AbstractFloatCollection extends AbstractCollection<Float> implements FloatCollection
{
    protected AbstractFloatCollection() {
    }
    
    @Override
    public float[] toArray(final float[] a) {
        return this.toFloatArray(a);
    }
    
    @Override
    public float[] toFloatArray() {
        return this.toFloatArray(null);
    }
    
    @Override
    public float[] toFloatArray(float[] a) {
        if (a == null || a.length < this.size()) {
            a = new float[this.size()];
        }
        FloatIterators.unwrap(this.iterator(), a);
        return a;
    }
    
    @Override
    public boolean addAll(final FloatCollection c) {
        boolean retVal = false;
        final FloatIterator i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (this.add(i.nextFloat())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean containsAll(final FloatCollection c) {
        final FloatIterator i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (!this.contains(i.nextFloat())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean retainAll(final FloatCollection c) {
        boolean retVal = false;
        int n = this.size();
        final FloatIterator i = this.iterator();
        while (n-- != 0) {
            if (!c.contains(i.nextFloat())) {
                i.remove();
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean removeAll(final FloatCollection c) {
        boolean retVal = false;
        int n = c.size();
        final FloatIterator i = c.iterator();
        while (n-- != 0) {
            if (this.rem(i.nextFloat())) {
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
    public boolean addAll(final Collection<? extends Float> c) {
        boolean retVal = false;
        final Iterator<? extends Float> i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (this.add((Float)i.next())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean add(final float k) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public FloatIterator floatIterator() {
        return this.iterator();
    }
    
    @Override
    public abstract FloatIterator iterator();
    
    @Override
    public boolean remove(final Object ok) {
        return ok != null && this.rem((float)ok);
    }
    
    @Override
    public boolean add(final Float o) {
        return this.add((float)o);
    }
    
    public boolean rem(final Object o) {
        return o != null && this.rem((float)o);
    }
    
    @Override
    public boolean contains(final Object o) {
        return o != null && this.contains((float)o);
    }
    
    @Override
    public boolean contains(final float k) {
        final FloatIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextFloat()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean rem(final float k) {
        final FloatIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextFloat()) {
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
        final FloatIterator i = this.iterator();
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
            final float k = i.nextFloat();
            s.append(String.valueOf(k));
        }
        s.append("}");
        return s.toString();
    }
}
