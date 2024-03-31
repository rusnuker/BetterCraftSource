// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.io.Serializable;
import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.BigList;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;

public abstract class AbstractByteBigList extends AbstractByteCollection implements ByteBigList, ByteStack
{
    protected AbstractByteBigList() {
    }
    
    protected void ensureIndex(final long index) {
        if (index < 0L) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index > this.size64()) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + this.size64() + ")");
        }
    }
    
    protected void ensureRestrictedIndex(final long index) {
        if (index < 0L) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index >= this.size64()) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size64() + ")");
        }
    }
    
    @Override
    public void add(final long index, final byte k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final byte k) {
        this.add(this.size64(), k);
        return true;
    }
    
    @Override
    public byte removeByte(final long i) {
        throw new UnsupportedOperationException();
    }
    
    public byte removeByte(final int i) {
        return this.removeByte((long)i);
    }
    
    @Override
    public byte set(final long index, final byte k) {
        throw new UnsupportedOperationException();
    }
    
    public byte set(final int index, final byte k) {
        return this.set((long)index, k);
    }
    
    @Override
    public boolean addAll(long index, final Collection<? extends Byte> c) {
        this.ensureIndex(index);
        int n = c.size();
        if (n == 0) {
            return false;
        }
        final Iterator<? extends Byte> i = c.iterator();
        while (n-- != 0) {
            this.add(index++, (Byte)i.next());
        }
        return true;
    }
    
    public boolean addAll(final int index, final Collection<? extends Byte> c) {
        return this.addAll((long)index, c);
    }
    
    @Override
    public boolean addAll(final Collection<? extends Byte> c) {
        return this.addAll(this.size64(), c);
    }
    
    @Override
    public ByteBigListIterator iterator() {
        return this.listIterator();
    }
    
    @Override
    public ByteBigListIterator listIterator() {
        return this.listIterator(0L);
    }
    
    @Override
    public ByteBigListIterator listIterator(final long index) {
        this.ensureIndex(index);
        return new AbstractByteBigListIterator() {
            long pos = index;
            long last = -1L;
            
            @Override
            public boolean hasNext() {
                return this.pos < AbstractByteBigList.this.size64();
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0L;
            }
            
            @Override
            public byte nextByte() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractByteBigList this$0 = AbstractByteBigList.this;
                final long last = this.pos++;
                this.last = last;
                return this$0.getByte(last);
            }
            
            @Override
            public byte previousByte() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractByteBigList this$0 = AbstractByteBigList.this;
                final long n = this.pos - 1L;
                this.pos = n;
                this.last = n;
                return this$0.getByte(n);
            }
            
            @Override
            public long nextIndex() {
                return this.pos;
            }
            
            @Override
            public long previousIndex() {
                return this.pos - 1L;
            }
            
            @Override
            public void add(final byte k) {
                AbstractByteBigList.this.add(this.pos++, k);
                this.last = -1L;
            }
            
            @Override
            public void set(final byte k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractByteBigList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractByteBigList.this.removeByte(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1L;
            }
        };
    }
    
    public ByteBigListIterator listIterator(final int index) {
        return this.listIterator((long)index);
    }
    
    @Override
    public boolean contains(final byte k) {
        return this.indexOf(k) >= 0L;
    }
    
    @Override
    public long indexOf(final byte k) {
        final ByteBigListIterator i = this.listIterator();
        while (i.hasNext()) {
            final byte e = i.nextByte();
            if (k == e) {
                return i.previousIndex();
            }
        }
        return -1L;
    }
    
    @Override
    public long lastIndexOf(final byte k) {
        final ByteBigListIterator i = this.listIterator(this.size64());
        while (i.hasPrevious()) {
            final byte e = i.previousByte();
            if (k == e) {
                return i.nextIndex();
            }
        }
        return -1L;
    }
    
    @Override
    public void size(final long size) {
        long i = this.size64();
        if (size > i) {
            while (i++ < size) {
                this.add((byte)0);
            }
        }
        else {
            while (i-- != size) {
                this.remove(i);
            }
        }
    }
    
    public void size(final int size) {
        this.size((long)size);
    }
    
    @Override
    public ByteBigList subList(final long from, final long to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new ByteSubList(this, from, to);
    }
    
    @Override
    public void removeElements(final long from, final long to) {
        this.ensureIndex(to);
        final ByteBigListIterator i = this.listIterator(from);
        long n = to - from;
        if (n < 0L) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        while (n-- != 0L) {
            i.nextByte();
            i.remove();
        }
    }
    
    @Override
    public void addElements(long index, final byte[][] a, long offset, long length) {
        this.ensureIndex(index);
        ByteBigArrays.ensureOffsetLength(a, offset, length);
        while (length-- != 0L) {
            this.add(index++, ByteBigArrays.get(a, offset++));
        }
    }
    
    @Override
    public void addElements(final long index, final byte[][] a) {
        this.addElements(index, a, 0L, ByteBigArrays.length(a));
    }
    
    @Override
    public void getElements(final long from, final byte[][] a, long offset, long length) {
        final ByteBigListIterator i = this.listIterator(from);
        ByteBigArrays.ensureOffsetLength(a, offset, length);
        if (from + length > this.size64()) {
            throw new IndexOutOfBoundsException("End index (" + (from + length) + ") is greater than list size (" + this.size64() + ")");
        }
        while (length-- != 0L) {
            ByteBigArrays.set(a, offset++, i.nextByte());
        }
    }
    
    @Deprecated
    @Override
    public int size() {
        return (int)Math.min(2147483647L, this.size64());
    }
    
    private boolean valEquals(final Object a, final Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BigList)) {
            return false;
        }
        final BigList<?> l = (BigList<?>)o;
        long s = this.size64();
        if (s != l.size64()) {
            return false;
        }
        if (l instanceof ByteBigList) {
            final ByteBigListIterator i1 = this.listIterator();
            final ByteBigListIterator i2 = ((ByteBigList)l).listIterator();
            while (s-- != 0L) {
                if (i1.nextByte() != i2.nextByte()) {
                    return false;
                }
            }
            return true;
        }
        final BigListIterator<?> i3 = this.listIterator();
        final BigListIterator<?> i4 = l.listIterator();
        while (s-- != 0L) {
            if (!this.valEquals(i3.next(), i4.next())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int compareTo(final BigList<? extends Byte> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof ByteBigList) {
            final ByteBigListIterator i1 = this.listIterator();
            final ByteBigListIterator i2 = ((ByteBigList)l).listIterator();
            while (i1.hasNext() && i2.hasNext()) {
                final byte e1 = i1.nextByte();
                final byte e2 = i2.nextByte();
                final int r;
                if ((r = Byte.compare(e1, e2)) != 0) {
                    return r;
                }
            }
            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
        }
        final BigListIterator<? extends Byte> i3 = this.listIterator();
        final BigListIterator<? extends Byte> i4 = l.listIterator();
        while (i3.hasNext() && i4.hasNext()) {
            final int r;
            if ((r = i3.next().compareTo(i4.next())) != 0) {
                return r;
            }
        }
        return i4.hasNext() ? -1 : (i3.hasNext() ? 1 : 0);
    }
    
    @Override
    public int hashCode() {
        final ByteIterator i = this.iterator();
        int h = 1;
        long s = this.size64();
        while (s-- != 0L) {
            final byte k = i.nextByte();
            h = 31 * h + k;
        }
        return h;
    }
    
    @Override
    public void push(final byte o) {
        this.add(o);
    }
    
    @Override
    public byte popByte() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.removeByte(this.size64() - 1L);
    }
    
    @Override
    public byte topByte() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getByte(this.size64() - 1L);
    }
    
    @Override
    public byte peekByte(final int i) {
        return this.getByte(this.size64() - 1L - i);
    }
    
    public byte getByte(final int index) {
        return this.getByte(index);
    }
    
    @Override
    public boolean rem(final byte k) {
        final long index = this.indexOf(k);
        if (index == -1L) {
            return false;
        }
        this.removeByte(index);
        return true;
    }
    
    @Override
    public boolean addAll(final long index, final ByteCollection c) {
        return this.addAll(index, (Collection<? extends Byte>)c);
    }
    
    @Override
    public boolean addAll(final long index, final ByteBigList l) {
        return this.addAll(index, (ByteCollection)l);
    }
    
    @Override
    public boolean addAll(final ByteCollection c) {
        return this.addAll(this.size64(), c);
    }
    
    @Override
    public boolean addAll(final ByteBigList l) {
        return this.addAll(this.size64(), l);
    }
    
    @Deprecated
    @Override
    public void add(final long index, final Byte ok) {
        this.add(index, (byte)ok);
    }
    
    @Deprecated
    @Override
    public Byte set(final long index, final Byte ok) {
        return this.set(index, (byte)ok);
    }
    
    @Deprecated
    @Override
    public Byte get(final long index) {
        return this.getByte(index);
    }
    
    @Deprecated
    @Override
    public long indexOf(final Object ok) {
        return this.indexOf((byte)ok);
    }
    
    @Deprecated
    @Override
    public long lastIndexOf(final Object ok) {
        return this.lastIndexOf((byte)ok);
    }
    
    @Deprecated
    public Byte remove(final int index) {
        return this.removeByte(index);
    }
    
    @Deprecated
    @Override
    public Byte remove(final long index) {
        return this.removeByte(index);
    }
    
    @Deprecated
    @Override
    public void push(final Byte o) {
        this.push((byte)o);
    }
    
    @Deprecated
    @Override
    public Byte pop() {
        return this.popByte();
    }
    
    @Deprecated
    @Override
    public Byte top() {
        return this.topByte();
    }
    
    @Deprecated
    @Override
    public Byte peek(final int i) {
        return this.peekByte(i);
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ByteIterator i = this.iterator();
        long n = this.size64();
        boolean first = true;
        s.append("[");
        while (n-- != 0L) {
            if (first) {
                first = false;
            }
            else {
                s.append(", ");
            }
            final byte k = i.nextByte();
            s.append(String.valueOf(k));
        }
        s.append("]");
        return s.toString();
    }
    
    public static class ByteSubList extends AbstractByteBigList implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ByteBigList l;
        protected final long from;
        protected long to;
        private static final boolean ASSERTS = false;
        
        public ByteSubList(final ByteBigList l, final long from, final long to) {
            this.l = l;
            this.from = from;
            this.to = to;
        }
        
        private void assertRange() {
        }
        
        @Override
        public boolean add(final byte k) {
            this.l.add(this.to, k);
            ++this.to;
            return true;
        }
        
        @Override
        public void add(final long index, final byte k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Byte> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        @Override
        public byte getByte(final long index) {
            this.ensureRestrictedIndex(index);
            return this.l.getByte(this.from + index);
        }
        
        @Override
        public byte removeByte(final long index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeByte(this.from + index);
        }
        
        @Override
        public byte set(final long index, final byte k) {
            this.ensureRestrictedIndex(index);
            return this.l.set(this.from + index, k);
        }
        
        @Override
        public void clear() {
            this.removeElements(0L, this.size64());
        }
        
        @Override
        public long size64() {
            return this.to - this.from;
        }
        
        @Override
        public void getElements(final long from, final byte[][] a, final long offset, final long length) {
            this.ensureIndex(from);
            if (from + length > this.size64()) {
                throw new IndexOutOfBoundsException("End index (" + from + length + ") is greater than list size (" + this.size64() + ")");
            }
            this.l.getElements(this.from + from, a, offset, length);
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            this.l.removeElements(this.from + from, this.from + to);
            this.to -= to - from;
        }
        
        @Override
        public void addElements(final long index, final byte[][] a, final long offset, final long length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
        }
        
        @Override
        public ByteBigListIterator listIterator(final long index) {
            this.ensureIndex(index);
            return new AbstractByteBigListIterator() {
                long pos = index;
                long last = -1L;
                
                @Override
                public boolean hasNext() {
                    return this.pos < ByteSubList.this.size64();
                }
                
                @Override
                public boolean hasPrevious() {
                    return this.pos > 0L;
                }
                
                @Override
                public byte nextByte() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final ByteBigList l = ByteSubList.this.l;
                    final long from = ByteSubList.this.from;
                    final long last = this.pos++;
                    this.last = last;
                    return l.getByte(from + last);
                }
                
                @Override
                public byte previousByte() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final ByteBigList l = ByteSubList.this.l;
                    final long from = ByteSubList.this.from;
                    final long n = this.pos - 1L;
                    this.pos = n;
                    this.last = n;
                    return l.getByte(from + n);
                }
                
                @Override
                public long nextIndex() {
                    return this.pos;
                }
                
                @Override
                public long previousIndex() {
                    return this.pos - 1L;
                }
                
                @Override
                public void add(final byte k) {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    ByteSubList.this.add(this.pos++, k);
                    this.last = -1L;
                }
                
                @Override
                public void set(final byte k) {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    ByteSubList.this.set(this.last, k);
                }
                
                @Override
                public void remove() {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    ByteSubList.this.removeByte(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1L;
                }
            };
        }
        
        @Override
        public ByteBigList subList(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new ByteSubList(this, from, to);
        }
        
        @Override
        public boolean rem(final byte k) {
            final long index = this.indexOf(k);
            if (index == -1L) {
                return false;
            }
            --this.to;
            this.l.removeByte(this.from + index);
            return true;
        }
        
        @Override
        public boolean remove(final Object o) {
            return this.rem((byte)o);
        }
        
        @Override
        public boolean addAll(final long index, final ByteCollection c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        public boolean addAll(final long index, final ByteList l) {
            this.ensureIndex(index);
            this.to += l.size();
            return this.l.addAll(this.from + index, l);
        }
    }
}
