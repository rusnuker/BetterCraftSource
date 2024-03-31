// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.util.ListIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.io.Serializable;
import java.util.Random;

public class FloatLists
{
    public static final EmptyList EMPTY_LIST;
    
    private FloatLists() {
    }
    
    public static FloatList shuffle(final FloatList l, final Random random) {
        int i = l.size();
        while (i-- != 0) {
            final int p = random.nextInt(i + 1);
            final float t = l.getFloat(i);
            l.set(i, l.getFloat(p));
            l.set(p, t);
        }
        return l;
    }
    
    public static FloatList singleton(final float element) {
        return new Singleton(element);
    }
    
    public static FloatList singleton(final Object element) {
        return new Singleton((float)element);
    }
    
    public static FloatList synchronize(final FloatList l) {
        return new SynchronizedList(l);
    }
    
    public static FloatList synchronize(final FloatList l, final Object sync) {
        return new SynchronizedList(l, sync);
    }
    
    public static FloatList unmodifiable(final FloatList l) {
        return new UnmodifiableList(l);
    }
    
    static {
        EMPTY_LIST = new EmptyList();
    }
    
    public static class EmptyList extends FloatCollections.EmptyCollection implements FloatList, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyList() {
        }
        
        @Override
        public void add(final int index, final float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float removeFloat(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float set(final int index, final float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int indexOf(final float k) {
            return -1;
        }
        
        @Override
        public int lastIndexOf(final float k) {
            return -1;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Float> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final Collection<? extends Float> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Float get(final int i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public boolean addAll(final FloatCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final FloatList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final FloatCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final FloatList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final int index, final Float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final Float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Float set(final int index, final Float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float getFloat(final int i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public Float remove(final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int indexOf(final Object k) {
            return -1;
        }
        
        @Override
        public int lastIndexOf(final Object k) {
            return -1;
        }
        
        @Deprecated
        @Override
        public FloatIterator floatIterator() {
            return FloatIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public FloatListIterator listIterator() {
            return FloatIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public FloatListIterator iterator() {
            return FloatIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public FloatListIterator listIterator(final int i) {
            if (i == 0) {
                return FloatIterators.EMPTY_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }
        
        @Deprecated
        @Override
        public FloatListIterator floatListIterator() {
            return this.listIterator();
        }
        
        @Deprecated
        @Override
        public FloatListIterator floatListIterator(final int i) {
            return this.listIterator(i);
        }
        
        @Override
        public FloatList subList(final int from, final int to) {
            if (from == 0 && to == 0) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Deprecated
        @Override
        public FloatList floatSubList(final int from, final int to) {
            return this.subList(from, to);
        }
        
        @Override
        public void getElements(final int from, final float[] a, final int offset, final int length) {
            if (from == 0 && length == 0 && offset >= 0 && offset <= a.length) {
                return;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final float[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final float[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final int s) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int compareTo(final List<? extends Float> o) {
            if (o == this) {
                return 0;
            }
            return o.isEmpty() ? 0 : -1;
        }
        
        private Object readResolve() {
            return FloatLists.EMPTY_LIST;
        }
        
        public Object clone() {
            return FloatLists.EMPTY_LIST;
        }
        
        @Override
        public int hashCode() {
            return 1;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof List && ((List)o).isEmpty();
        }
        
        @Override
        public String toString() {
            return "[]";
        }
    }
    
    public static class Singleton extends AbstractFloatList implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        private final float element;
        
        private Singleton(final float element) {
            this.element = element;
        }
        
        @Override
        public float getFloat(final int i) {
            if (i == 0) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public float removeFloat(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final float k) {
            return Float.floatToIntBits(k) == Float.floatToIntBits(this.element);
        }
        
        @Override
        public boolean addAll(final Collection<? extends Float> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final Collection<? extends Float> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float[] toFloatArray() {
            final float[] a = { this.element };
            return a;
        }
        
        @Override
        public FloatListIterator listIterator() {
            return FloatIterators.singleton(this.element);
        }
        
        @Override
        public FloatListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public FloatListIterator listIterator(final int i) {
            if (i > 1 || i < 0) {
                throw new IndexOutOfBoundsException();
            }
            final FloatListIterator l = this.listIterator();
            if (i == 1) {
                l.next();
            }
            return l;
        }
        
        @Override
        public FloatList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            if (from != 0 || to != 1) {
                return FloatLists.EMPTY_LIST;
            }
            return this;
        }
        
        @Override
        public int size() {
            return 1;
        }
        
        @Override
        public void size(final int size) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        public Object clone() {
            return this;
        }
        
        @Override
        public boolean rem(final float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final FloatCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final FloatCollection c) {
            throw new UnsupportedOperationException();
        }
    }
    
    public static class SynchronizedList extends FloatCollections.SynchronizedCollection implements FloatList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final FloatList list;
        
        protected SynchronizedList(final FloatList l, final Object sync) {
            super(l, sync);
            this.list = l;
        }
        
        protected SynchronizedList(final FloatList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public float getFloat(final int i) {
            synchronized (this.sync) {
                return this.list.getFloat(i);
            }
        }
        
        @Override
        public float set(final int i, final float k) {
            synchronized (this.sync) {
                return this.list.set(i, k);
            }
        }
        
        @Override
        public void add(final int i, final float k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public float removeFloat(final int i) {
            synchronized (this.sync) {
                return this.list.removeFloat(i);
            }
        }
        
        @Override
        public int indexOf(final float k) {
            synchronized (this.sync) {
                return this.list.indexOf(k);
            }
        }
        
        @Override
        public int lastIndexOf(final float k) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(k);
            }
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Float> c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public void getElements(final int from, final float[] a, final int offset, final int length) {
            synchronized (this.sync) {
                this.list.getElements(from, a, offset, length);
            }
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            synchronized (this.sync) {
                this.list.removeElements(from, to);
            }
        }
        
        @Override
        public void addElements(final int index, final float[] a, final int offset, final int length) {
            synchronized (this.sync) {
                this.list.addElements(index, a, offset, length);
            }
        }
        
        @Override
        public void addElements(final int index, final float[] a) {
            synchronized (this.sync) {
                this.list.addElements(index, a);
            }
        }
        
        @Override
        public void size(final int size) {
            synchronized (this.sync) {
                this.list.size(size);
            }
        }
        
        @Override
        public FloatListIterator iterator() {
            return this.list.listIterator();
        }
        
        @Override
        public FloatListIterator listIterator() {
            return this.list.listIterator();
        }
        
        @Override
        public FloatListIterator listIterator(final int i) {
            return this.list.listIterator(i);
        }
        
        @Deprecated
        @Override
        public FloatListIterator floatListIterator() {
            return this.listIterator();
        }
        
        @Deprecated
        @Override
        public FloatListIterator floatListIterator(final int i) {
            return this.listIterator(i);
        }
        
        @Override
        public FloatList subList(final int from, final int to) {
            synchronized (this.sync) {
                return FloatLists.synchronize(this.list.subList(from, to), this.sync);
            }
        }
        
        @Deprecated
        @Override
        public FloatList floatSubList(final int from, final int to) {
            return this.subList(from, to);
        }
        
        @Override
        public boolean equals(final Object o) {
            synchronized (this.sync) {
                return this.collection.equals(o);
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.sync) {
                return this.collection.hashCode();
            }
        }
        
        @Override
        public int compareTo(final List<? extends Float> o) {
            synchronized (this.sync) {
                return this.list.compareTo(o);
            }
        }
        
        @Override
        public boolean addAll(final int index, final FloatCollection c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public boolean addAll(final int index, final FloatList l) {
            synchronized (this.sync) {
                return this.list.addAll(index, l);
            }
        }
        
        @Override
        public boolean addAll(final FloatList l) {
            synchronized (this.sync) {
                return this.list.addAll(l);
            }
        }
        
        @Override
        public Float get(final int i) {
            synchronized (this.sync) {
                return this.list.get(i);
            }
        }
        
        @Override
        public void add(final int i, final Float k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public Float set(final int index, final Float k) {
            synchronized (this.sync) {
                return this.list.set(index, k);
            }
        }
        
        @Override
        public Float remove(final int i) {
            synchronized (this.sync) {
                return this.list.remove(i);
            }
        }
        
        @Override
        public int indexOf(final Object o) {
            synchronized (this.sync) {
                return this.list.indexOf(o);
            }
        }
        
        @Override
        public int lastIndexOf(final Object o) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(o);
            }
        }
    }
    
    public static class UnmodifiableList extends FloatCollections.UnmodifiableCollection implements FloatList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final FloatList list;
        
        protected UnmodifiableList(final FloatList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public float getFloat(final int i) {
            return this.list.getFloat(i);
        }
        
        @Override
        public float set(final int i, final float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final int i, final float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float removeFloat(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int indexOf(final float k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public int lastIndexOf(final float k) {
            return this.list.lastIndexOf(k);
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Float> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void getElements(final int from, final float[] a, final int offset, final int length) {
            this.list.getElements(from, a, offset, length);
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final float[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final float[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final int size) {
            this.list.size(size);
        }
        
        @Override
        public FloatListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public FloatListIterator listIterator() {
            return FloatIterators.unmodifiable(this.list.listIterator());
        }
        
        @Override
        public FloatListIterator listIterator(final int i) {
            return FloatIterators.unmodifiable(this.list.listIterator(i));
        }
        
        @Deprecated
        @Override
        public FloatListIterator floatListIterator() {
            return this.listIterator();
        }
        
        @Deprecated
        @Override
        public FloatListIterator floatListIterator(final int i) {
            return this.listIterator(i);
        }
        
        @Override
        public FloatList subList(final int from, final int to) {
            return FloatLists.unmodifiable(this.list.subList(from, to));
        }
        
        @Deprecated
        @Override
        public FloatList floatSubList(final int from, final int to) {
            return this.subList(from, to);
        }
        
        @Override
        public boolean equals(final Object o) {
            return this.collection.equals(o);
        }
        
        @Override
        public int hashCode() {
            return this.collection.hashCode();
        }
        
        @Override
        public int compareTo(final List<? extends Float> o) {
            return this.list.compareTo(o);
        }
        
        @Override
        public boolean addAll(final int index, final FloatCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final FloatList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int index, final FloatList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Float get(final int i) {
            return this.list.get(i);
        }
        
        @Override
        public void add(final int i, final Float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Float set(final int index, final Float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Float remove(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int indexOf(final Object o) {
            return this.list.indexOf(o);
        }
        
        @Override
        public int lastIndexOf(final Object o) {
            return this.list.lastIndexOf(o);
        }
    }
}
