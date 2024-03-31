// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.util.ListIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.io.Serializable;
import java.util.Random;

public class ByteLists
{
    public static final EmptyList EMPTY_LIST;
    
    private ByteLists() {
    }
    
    public static ByteList shuffle(final ByteList l, final Random random) {
        int i = l.size();
        while (i-- != 0) {
            final int p = random.nextInt(i + 1);
            final byte t = l.getByte(i);
            l.set(i, l.getByte(p));
            l.set(p, t);
        }
        return l;
    }
    
    public static ByteList singleton(final byte element) {
        return new Singleton(element);
    }
    
    public static ByteList singleton(final Object element) {
        return new Singleton((byte)element);
    }
    
    public static ByteList synchronize(final ByteList l) {
        return new SynchronizedList(l);
    }
    
    public static ByteList synchronize(final ByteList l, final Object sync) {
        return new SynchronizedList(l, sync);
    }
    
    public static ByteList unmodifiable(final ByteList l) {
        return new UnmodifiableList(l);
    }
    
    static {
        EMPTY_LIST = new EmptyList();
    }
    
    public static class EmptyList extends ByteCollections.EmptyCollection implements ByteList, Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyList() {
        }
        
        @Override
        public void add(final int index, final byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte removeByte(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte set(final int index, final byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int indexOf(final byte k) {
            return -1;
        }
        
        @Override
        public int lastIndexOf(final byte k) {
            return -1;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Byte> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final Collection<? extends Byte> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Byte get(final int i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public boolean addAll(final ByteCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final ByteList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final ByteCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final ByteList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final int index, final Byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(final Byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Byte set(final int index, final Byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte getByte(final int i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public Byte remove(final int k) {
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
        public ByteIterator byteIterator() {
            return ByteIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public ByteListIterator listIterator() {
            return ByteIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public ByteListIterator iterator() {
            return ByteIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public ByteListIterator listIterator(final int i) {
            if (i == 0) {
                return ByteIterators.EMPTY_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }
        
        @Deprecated
        @Override
        public ByteListIterator byteListIterator() {
            return this.listIterator();
        }
        
        @Deprecated
        @Override
        public ByteListIterator byteListIterator(final int i) {
            return this.listIterator(i);
        }
        
        @Override
        public ByteList subList(final int from, final int to) {
            if (from == 0 && to == 0) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Deprecated
        @Override
        public ByteList byteSubList(final int from, final int to) {
            return this.subList(from, to);
        }
        
        @Override
        public void getElements(final int from, final byte[] a, final int offset, final int length) {
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
        public void addElements(final int index, final byte[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final byte[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final int s) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int compareTo(final List<? extends Byte> o) {
            if (o == this) {
                return 0;
            }
            return o.isEmpty() ? 0 : -1;
        }
        
        private Object readResolve() {
            return ByteLists.EMPTY_LIST;
        }
        
        public Object clone() {
            return ByteLists.EMPTY_LIST;
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
    
    public static class Singleton extends AbstractByteList implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        private final byte element;
        
        private Singleton(final byte element) {
            this.element = element;
        }
        
        @Override
        public byte getByte(final int i) {
            if (i == 0) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public byte removeByte(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final byte k) {
            return k == this.element;
        }
        
        @Override
        public boolean addAll(final Collection<? extends Byte> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final Collection<? extends Byte> c) {
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
        public byte[] toByteArray() {
            final byte[] a = { this.element };
            return a;
        }
        
        @Override
        public ByteListIterator listIterator() {
            return ByteIterators.singleton(this.element);
        }
        
        @Override
        public ByteListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public ByteListIterator listIterator(final int i) {
            if (i > 1 || i < 0) {
                throw new IndexOutOfBoundsException();
            }
            final ByteListIterator l = this.listIterator();
            if (i == 1) {
                l.next();
            }
            return l;
        }
        
        @Override
        public ByteList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            if (from != 0 || to != 1) {
                return ByteLists.EMPTY_LIST;
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
        public boolean rem(final byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final ByteCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final ByteCollection c) {
            throw new UnsupportedOperationException();
        }
    }
    
    public static class SynchronizedList extends ByteCollections.SynchronizedCollection implements ByteList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ByteList list;
        
        protected SynchronizedList(final ByteList l, final Object sync) {
            super(l, sync);
            this.list = l;
        }
        
        protected SynchronizedList(final ByteList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public byte getByte(final int i) {
            synchronized (this.sync) {
                return this.list.getByte(i);
            }
        }
        
        @Override
        public byte set(final int i, final byte k) {
            synchronized (this.sync) {
                return this.list.set(i, k);
            }
        }
        
        @Override
        public void add(final int i, final byte k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public byte removeByte(final int i) {
            synchronized (this.sync) {
                return this.list.removeByte(i);
            }
        }
        
        @Override
        public int indexOf(final byte k) {
            synchronized (this.sync) {
                return this.list.indexOf(k);
            }
        }
        
        @Override
        public int lastIndexOf(final byte k) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(k);
            }
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Byte> c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public void getElements(final int from, final byte[] a, final int offset, final int length) {
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
        public void addElements(final int index, final byte[] a, final int offset, final int length) {
            synchronized (this.sync) {
                this.list.addElements(index, a, offset, length);
            }
        }
        
        @Override
        public void addElements(final int index, final byte[] a) {
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
        public ByteListIterator iterator() {
            return this.list.listIterator();
        }
        
        @Override
        public ByteListIterator listIterator() {
            return this.list.listIterator();
        }
        
        @Override
        public ByteListIterator listIterator(final int i) {
            return this.list.listIterator(i);
        }
        
        @Deprecated
        @Override
        public ByteListIterator byteListIterator() {
            return this.listIterator();
        }
        
        @Deprecated
        @Override
        public ByteListIterator byteListIterator(final int i) {
            return this.listIterator(i);
        }
        
        @Override
        public ByteList subList(final int from, final int to) {
            synchronized (this.sync) {
                return ByteLists.synchronize(this.list.subList(from, to), this.sync);
            }
        }
        
        @Deprecated
        @Override
        public ByteList byteSubList(final int from, final int to) {
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
        public int compareTo(final List<? extends Byte> o) {
            synchronized (this.sync) {
                return this.list.compareTo(o);
            }
        }
        
        @Override
        public boolean addAll(final int index, final ByteCollection c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public boolean addAll(final int index, final ByteList l) {
            synchronized (this.sync) {
                return this.list.addAll(index, l);
            }
        }
        
        @Override
        public boolean addAll(final ByteList l) {
            synchronized (this.sync) {
                return this.list.addAll(l);
            }
        }
        
        @Override
        public Byte get(final int i) {
            synchronized (this.sync) {
                return this.list.get(i);
            }
        }
        
        @Override
        public void add(final int i, final Byte k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public Byte set(final int index, final Byte k) {
            synchronized (this.sync) {
                return this.list.set(index, k);
            }
        }
        
        @Override
        public Byte remove(final int i) {
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
    
    public static class UnmodifiableList extends ByteCollections.UnmodifiableCollection implements ByteList, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ByteList list;
        
        protected UnmodifiableList(final ByteList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public byte getByte(final int i) {
            return this.list.getByte(i);
        }
        
        @Override
        public byte set(final int i, final byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final int i, final byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte removeByte(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int indexOf(final byte k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public int lastIndexOf(final byte k) {
            return this.list.lastIndexOf(k);
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Byte> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void getElements(final int from, final byte[] a, final int offset, final int length) {
            this.list.getElements(from, a, offset, length);
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final byte[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final byte[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final int size) {
            this.list.size(size);
        }
        
        @Override
        public ByteListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public ByteListIterator listIterator() {
            return ByteIterators.unmodifiable(this.list.listIterator());
        }
        
        @Override
        public ByteListIterator listIterator(final int i) {
            return ByteIterators.unmodifiable(this.list.listIterator(i));
        }
        
        @Deprecated
        @Override
        public ByteListIterator byteListIterator() {
            return this.listIterator();
        }
        
        @Deprecated
        @Override
        public ByteListIterator byteListIterator(final int i) {
            return this.listIterator(i);
        }
        
        @Override
        public ByteList subList(final int from, final int to) {
            return ByteLists.unmodifiable(this.list.subList(from, to));
        }
        
        @Deprecated
        @Override
        public ByteList byteSubList(final int from, final int to) {
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
        public int compareTo(final List<? extends Byte> o) {
            return this.list.compareTo(o);
        }
        
        @Override
        public boolean addAll(final int index, final ByteCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final ByteList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int index, final ByteList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Byte get(final int i) {
            return this.list.get(i);
        }
        
        @Override
        public void add(final int i, final Byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Byte set(final int index, final Byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Byte remove(final int i) {
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
