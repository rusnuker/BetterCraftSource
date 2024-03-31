// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.util.Collection;
import java.lang.reflect.Array;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterators;
import java.util.AbstractCollection;

public abstract class AbstractByteCollection extends AbstractCollection<Byte> implements ByteCollection
{
    protected AbstractByteCollection() {
    }
    
    @Override
    public byte[] toArray(final byte[] a) {
        return this.toByteArray(a);
    }
    
    @Override
    public byte[] toByteArray() {
        return this.toByteArray(null);
    }
    
    @Override
    public byte[] toByteArray(byte[] a) {
        if (a == null || a.length < this.size()) {
            a = new byte[this.size()];
        }
        ByteIterators.unwrap(this.iterator(), a);
        return a;
    }
    
    @Override
    public boolean addAll(final ByteCollection c) {
        boolean retVal = false;
        final ByteIterator i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (this.add(i.nextByte())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean containsAll(final ByteCollection c) {
        final ByteIterator i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (!this.contains(i.nextByte())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean retainAll(final ByteCollection c) {
        boolean retVal = false;
        int n = this.size();
        final ByteIterator i = this.iterator();
        while (n-- != 0) {
            if (!c.contains(i.nextByte())) {
                i.remove();
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean removeAll(final ByteCollection c) {
        boolean retVal = false;
        int n = c.size();
        final ByteIterator i = c.iterator();
        while (n-- != 0) {
            if (this.rem(i.nextByte())) {
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
    public boolean addAll(final Collection<? extends Byte> c) {
        boolean retVal = false;
        final Iterator<? extends Byte> i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (this.add((Byte)i.next())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean add(final byte k) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public ByteIterator byteIterator() {
        return this.iterator();
    }
    
    @Override
    public abstract ByteIterator iterator();
    
    @Override
    public boolean remove(final Object ok) {
        return ok != null && this.rem((byte)ok);
    }
    
    @Override
    public boolean add(final Byte o) {
        return this.add((byte)o);
    }
    
    public boolean rem(final Object o) {
        return o != null && this.rem((byte)o);
    }
    
    @Override
    public boolean contains(final Object o) {
        return o != null && this.contains((byte)o);
    }
    
    @Override
    public boolean contains(final byte k) {
        final ByteIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextByte()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean rem(final byte k) {
        final ByteIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextByte()) {
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
        final ByteIterator i = this.iterator();
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
            final byte k = i.nextByte();
            s.append(String.valueOf(k));
        }
        s.append("}");
        return s.toString();
    }
}
