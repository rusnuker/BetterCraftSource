// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.util.Collection;
import java.lang.reflect.Array;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterators;
import java.util.AbstractCollection;

public abstract class AbstractCharCollection extends AbstractCollection<Character> implements CharCollection
{
    protected AbstractCharCollection() {
    }
    
    @Override
    public char[] toArray(final char[] a) {
        return this.toCharArray(a);
    }
    
    @Override
    public char[] toCharArray() {
        return this.toCharArray(null);
    }
    
    @Override
    public char[] toCharArray(char[] a) {
        if (a == null || a.length < this.size()) {
            a = new char[this.size()];
        }
        CharIterators.unwrap(this.iterator(), a);
        return a;
    }
    
    @Override
    public boolean addAll(final CharCollection c) {
        boolean retVal = false;
        final CharIterator i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (this.add(i.nextChar())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean containsAll(final CharCollection c) {
        final CharIterator i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (!this.contains(i.nextChar())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean retainAll(final CharCollection c) {
        boolean retVal = false;
        int n = this.size();
        final CharIterator i = this.iterator();
        while (n-- != 0) {
            if (!c.contains(i.nextChar())) {
                i.remove();
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean removeAll(final CharCollection c) {
        boolean retVal = false;
        int n = c.size();
        final CharIterator i = c.iterator();
        while (n-- != 0) {
            if (this.rem(i.nextChar())) {
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
    public boolean addAll(final Collection<? extends Character> c) {
        boolean retVal = false;
        final Iterator<? extends Character> i = c.iterator();
        int n = c.size();
        while (n-- != 0) {
            if (this.add((Character)i.next())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    @Override
    public boolean add(final char k) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public CharIterator charIterator() {
        return this.iterator();
    }
    
    @Override
    public abstract CharIterator iterator();
    
    @Override
    public boolean remove(final Object ok) {
        return ok != null && this.rem((char)ok);
    }
    
    @Override
    public boolean add(final Character o) {
        return this.add((char)o);
    }
    
    public boolean rem(final Object o) {
        return o != null && this.rem((char)o);
    }
    
    @Override
    public boolean contains(final Object o) {
        return o != null && this.contains((char)o);
    }
    
    @Override
    public boolean contains(final char k) {
        final CharIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextChar()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean rem(final char k) {
        final CharIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextChar()) {
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
        final CharIterator i = this.iterator();
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
            final char k = i.nextChar();
            s.append(String.valueOf(k));
        }
        s.append("}");
        return s.toString();
    }
}
