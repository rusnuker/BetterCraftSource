// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.BigListIterator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.BigList;
import java.util.Collection;
import java.io.Serializable;
import java.util.Random;

public class FloatBigLists
{
    public static final EmptyBigList EMPTY_BIG_LIST;
    
    private FloatBigLists() {
    }
    
    public static FloatBigList shuffle(final FloatBigList l, final Random random) {
        long i = l.size64();
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final float t = l.getFloat(i);
            l.set(i, l.getFloat(p));
            l.set(p, t);
        }
        return l;
    }
    
    public static FloatBigList singleton(final float element) {
        return new Singleton(element);
    }
    
    public static FloatBigList singleton(final Object element) {
        return new Singleton((float)element);
    }
    
    public static FloatBigList synchronize(final FloatBigList l) {
        return new SynchronizedBigList(l);
    }
    
    public static FloatBigList synchronize(final FloatBigList l, final Object sync) {
        return new SynchronizedBigList(l, sync);
    }
    
    public static FloatBigList unmodifiable(final FloatBigList l) {
        return new UnmodifiableBigList(l);
    }
    
    public static FloatBigList asBigList(final FloatList list) {
        return new ListBigList(list);
    }
    
    static {
        EMPTY_BIG_LIST = new EmptyBigList();
    }
    
    public static class EmptyBigList extends FloatCollections.EmptyCollection implements FloatBigList, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyBigList() {
        }
        
        @Override
        public void add(final long index, final float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float removeFloat(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float set(final long index, final float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long indexOf(final float k) {
            return -1L;
        }
        
        @Override
        public long lastIndexOf(final float k) {
            return -1L;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Float> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final Collection<? extends Float> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Float get(final long i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public boolean addAll(final FloatCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final FloatBigList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final FloatCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final FloatBigList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final long index, final Float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final Float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Float set(final long index, final Float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float getFloat(final long i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public Float remove(final long k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long indexOf(final Object k) {
            return -1L;
        }
        
        @Override
        public long lastIndexOf(final Object k) {
            return -1L;
        }
        
        @Override
        public FloatBigListIterator listIterator() {
            return FloatBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        @Override
        public FloatBigListIterator iterator() {
            return FloatBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        @Override
        public FloatBigListIterator listIterator(final long i) {
            if (i == 0L) {
                return FloatBigListIterators.EMPTY_BIG_LIST_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }
        
        @Override
        public FloatBigList subList(final long from, final long to) {
            if (from == 0L && to == 0L) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public void getElements(final long from, final float[][] a, final long offset, final long length) {
            FloatBigArrays.ensureOffsetLength(a, offset, length);
            if (from != 0L) {
                throw new IndexOutOfBoundsException();
            }
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final float[][] a, final long offset, final long length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final float[][] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final long s) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long size64() {
            return 0L;
        }
        
        @Override
        public int compareTo(final BigList<? extends Float> o) {
            if (o == this) {
                return 0;
            }
            return o.isEmpty() ? 0 : -1;
        }
        
        private Object readResolve() {
            return FloatBigLists.EMPTY_BIG_LIST;
        }
        
        public Object clone() {
            return FloatBigLists.EMPTY_BIG_LIST;
        }
        
        @Override
        public int hashCode() {
            return 1;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof BigList && ((BigList)o).isEmpty();
        }
        
        @Override
        public String toString() {
            return "[]";
        }
    }
    
    public static class Singleton extends AbstractFloatBigList implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        private final float element;
        
        private Singleton(final float element) {
            this.element = element;
        }
        
        @Override
        public float getFloat(final long i) {
            if (i == 0L) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public float removeFloat(final long i) {
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
        public boolean addAll(final long i, final Collection<? extends Float> c) {
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
        public FloatBigListIterator listIterator() {
            return FloatBigListIterators.singleton(this.element);
        }
        
        @Override
        public FloatBigListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public FloatBigListIterator listIterator(final long i) {
            if (i > 1L || i < 0L) {
                throw new IndexOutOfBoundsException();
            }
            final FloatBigListIterator l = this.listIterator();
            if (i == 1L) {
                l.next();
            }
            return l;
        }
        
        @Override
        public FloatBigList subList(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            if (from != 0L || to != 1L) {
                return FloatBigLists.EMPTY_BIG_LIST;
            }
            return this;
        }
        
        @Deprecated
        @Override
        public int size() {
            return 1;
        }
        
        @Override
        public long size64() {
            return 1L;
        }
        
        @Override
        public void size(final long size) {
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
        public boolean addAll(final long i, final FloatCollection c) {
            throw new UnsupportedOperationException();
        }
    }
    
    public static class SynchronizedBigList extends FloatCollections.SynchronizedCollection implements FloatBigList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final FloatBigList list;
        
        protected SynchronizedBigList(final FloatBigList l, final Object sync) {
            super(l, sync);
            this.list = l;
        }
        
        protected SynchronizedBigList(final FloatBigList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public float getFloat(final long i) {
            synchronized (this.sync) {
                return this.list.getFloat(i);
            }
        }
        
        @Override
        public float set(final long i, final float k) {
            synchronized (this.sync) {
                return this.list.set(i, k);
            }
        }
        
        @Override
        public void add(final long i, final float k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public float removeFloat(final long i) {
            synchronized (this.sync) {
                return this.list.removeFloat(i);
            }
        }
        
        @Override
        public long indexOf(final float k) {
            synchronized (this.sync) {
                return this.list.indexOf(k);
            }
        }
        
        @Override
        public long lastIndexOf(final float k) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(k);
            }
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Float> c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public void getElements(final long from, final float[][] a, final long offset, final long length) {
            synchronized (this.sync) {
                this.list.getElements(from, a, offset, length);
            }
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            synchronized (this.sync) {
                this.list.removeElements(from, to);
            }
        }
        
        @Override
        public void addElements(final long index, final float[][] a, final long offset, final long length) {
            synchronized (this.sync) {
                this.list.addElements(index, a, offset, length);
            }
        }
        
        @Override
        public void addElements(final long index, final float[][] a) {
            synchronized (this.sync) {
                this.list.addElements(index, a);
            }
        }
        
        @Override
        public void size(final long size) {
            synchronized (this.sync) {
                this.list.size(size);
            }
        }
        
        @Override
        public long size64() {
            synchronized (this.sync) {
                return this.list.size64();
            }
        }
        
        @Override
        public FloatBigListIterator iterator() {
            return this.list.listIterator();
        }
        
        @Override
        public FloatBigListIterator listIterator() {
            return this.list.listIterator();
        }
        
        @Override
        public FloatBigListIterator listIterator(final long i) {
            return this.list.listIterator(i);
        }
        
        @Override
        public FloatBigList subList(final long from, final long to) {
            synchronized (this.sync) {
                return FloatBigLists.synchronize(this.list.subList(from, to), this.sync);
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            synchronized (this.sync) {
                return this.list.equals(o);
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.sync) {
                return this.list.hashCode();
            }
        }
        
        @Override
        public int compareTo(final BigList<? extends Float> o) {
            synchronized (this.sync) {
                return this.list.compareTo(o);
            }
        }
        
        @Override
        public boolean addAll(final long index, final FloatCollection c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public boolean addAll(final long index, final FloatBigList l) {
            synchronized (this.sync) {
                return this.list.addAll(index, l);
            }
        }
        
        @Override
        public boolean addAll(final FloatBigList l) {
            synchronized (this.sync) {
                return this.list.addAll(l);
            }
        }
        
        @Override
        public Float get(final long i) {
            synchronized (this.sync) {
                return this.list.get(i);
            }
        }
        
        @Override
        public void add(final long i, final Float k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public Float set(final long index, final Float k) {
            synchronized (this.sync) {
                return this.list.set(index, k);
            }
        }
        
        @Override
        public Float remove(final long i) {
            synchronized (this.sync) {
                return this.list.remove(i);
            }
        }
        
        @Override
        public long indexOf(final Object o) {
            synchronized (this.sync) {
                return this.list.indexOf(o);
            }
        }
        
        @Override
        public long lastIndexOf(final Object o) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(o);
            }
        }
    }
    
    public static class UnmodifiableBigList extends FloatCollections.UnmodifiableCollection implements FloatBigList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final FloatBigList list;
        
        protected UnmodifiableBigList(final FloatBigList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public float getFloat(final long i) {
            return this.list.getFloat(i);
        }
        
        @Override
        public float set(final long i, final float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final long i, final float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float removeFloat(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long indexOf(final float k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public long lastIndexOf(final float k) {
            return this.list.lastIndexOf(k);
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Float> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void getElements(final long from, final float[][] a, final long offset, final long length) {
            this.list.getElements(from, a, offset, length);
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final float[][] a, final long offset, final long length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final float[][] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final long size) {
            this.list.size(size);
        }
        
        @Override
        public long size64() {
            return this.list.size64();
        }
        
        @Override
        public FloatBigListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public FloatBigListIterator listIterator() {
            return FloatBigListIterators.unmodifiable(this.list.listIterator());
        }
        
        @Override
        public FloatBigListIterator listIterator(final long i) {
            return FloatBigListIterators.unmodifiable(this.list.listIterator(i));
        }
        
        @Override
        public FloatBigList subList(final long from, final long to) {
            return FloatBigLists.unmodifiable(this.list.subList(from, to));
        }
        
        @Override
        public boolean equals(final Object o) {
            return this.list.equals(o);
        }
        
        @Override
        public int hashCode() {
            return this.list.hashCode();
        }
        
        @Override
        public int compareTo(final BigList<? extends Float> o) {
            return this.list.compareTo(o);
        }
        
        @Override
        public boolean addAll(final long index, final FloatCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final FloatBigList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long index, final FloatBigList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Float get(final long i) {
            return this.list.get(i);
        }
        
        @Override
        public void add(final long i, final Float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Float set(final long index, final Float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Float remove(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long indexOf(final Object o) {
            return this.list.indexOf(o);
        }
        
        @Override
        public long lastIndexOf(final Object o) {
            return this.list.lastIndexOf(o);
        }
    }
    
    public static class ListBigList extends AbstractFloatBigList implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        private final FloatList list;
        
        protected ListBigList(final FloatList list) {
            this.list = list;
        }
        
        private int intIndex(final long index) {
            if (index >= 2147483647L) {
                throw new IndexOutOfBoundsException("This big list is restricted to 32-bit indices");
            }
            return (int)index;
        }
        
        @Override
        public long size64() {
            return this.list.size();
        }
        
        @Deprecated
        @Override
        public int size() {
            return this.list.size();
        }
        
        @Override
        public void size(final long size) {
            this.list.size(this.intIndex(size));
        }
        
        @Override
        public FloatBigListIterator iterator() {
            return FloatBigListIterators.asBigListIterator(this.list.iterator());
        }
        
        @Override
        public FloatBigListIterator listIterator() {
            return FloatBigListIterators.asBigListIterator(this.list.listIterator());
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Float> c) {
            return this.list.addAll(this.intIndex(index), c);
        }
        
        @Override
        public FloatBigListIterator listIterator(final long index) {
            return FloatBigListIterators.asBigListIterator(this.list.listIterator(this.intIndex(index)));
        }
        
        @Override
        public FloatBigList subList(final long from, final long to) {
            return new ListBigList(this.list.subList(this.intIndex(from), this.intIndex(to)));
        }
        
        @Override
        public boolean contains(final float key) {
            return this.list.contains(key);
        }
        
        @Override
        public float[] toFloatArray() {
            return this.list.toFloatArray();
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            this.list.removeElements(this.intIndex(from), this.intIndex(to));
        }
        
        @Override
        public float[] toFloatArray(final float[] a) {
            return this.list.toFloatArray(a);
        }
        
        @Override
        public void add(final long index, final float key) {
            this.list.add(this.intIndex(index), key);
        }
        
        @Override
        public boolean addAll(final long index, final FloatCollection c) {
            return this.list.addAll(this.intIndex(index), c);
        }
        
        @Override
        public boolean addAll(final long index, final FloatBigList c) {
            return this.list.addAll(this.intIndex(index), c);
        }
        
        @Override
        public boolean add(final float key) {
            return this.list.add(key);
        }
        
        @Override
        public boolean addAll(final FloatBigList c) {
            return this.list.addAll(c);
        }
        
        @Override
        public float getFloat(final long index) {
            return this.list.getFloat(this.intIndex(index));
        }
        
        @Override
        public long indexOf(final float k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public long lastIndexOf(final float k) {
            return this.list.lastIndexOf(k);
        }
        
        @Override
        public float removeFloat(final long index) {
            return this.list.removeFloat(this.intIndex(index));
        }
        
        @Override
        public float set(final long index, final float k) {
            return this.list.set(this.intIndex(index), k);
        }
        
        @Override
        public boolean addAll(final FloatCollection c) {
            return this.list.addAll(c);
        }
        
        @Override
        public boolean containsAll(final FloatCollection c) {
            return this.list.containsAll(c);
        }
        
        @Override
        public boolean removeAll(final FloatCollection c) {
            return this.list.removeAll(c);
        }
        
        @Override
        public boolean retainAll(final FloatCollection c) {
            return this.list.retainAll(c);
        }
        
        @Override
        public boolean isEmpty() {
            return this.list.isEmpty();
        }
        
        @Override
        public <T> T[] toArray(final T[] a) {
            return this.list.toArray(a);
        }
        
        @Override
        public boolean containsAll(final Collection<?> c) {
            return this.list.containsAll(c);
        }
        
        @Override
        public boolean addAll(final Collection<? extends Float> c) {
            return this.list.addAll(c);
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            return this.list.removeAll(c);
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            return this.list.retainAll(c);
        }
        
        @Override
        public void clear() {
            this.list.clear();
        }
        
        @Override
        public int hashCode() {
            return this.list.hashCode();
        }
    }
}
