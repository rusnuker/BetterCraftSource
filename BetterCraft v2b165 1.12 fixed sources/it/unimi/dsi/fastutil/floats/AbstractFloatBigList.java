// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.io.Serializable;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.BigList;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;

public abstract class AbstractFloatBigList extends AbstractFloatCollection implements FloatBigList, FloatStack
{
    protected AbstractFloatBigList() {
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
    public void add(final long index, final float k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final float k) {
        this.add(this.size64(), k);
        return true;
    }
    
    @Override
    public float removeFloat(final long i) {
        throw new UnsupportedOperationException();
    }
    
    public float removeFloat(final int i) {
        return this.removeFloat((long)i);
    }
    
    @Override
    public float set(final long index, final float k) {
        throw new UnsupportedOperationException();
    }
    
    public float set(final int index, final float k) {
        return this.set((long)index, k);
    }
    
    @Override
    public boolean addAll(long index, final Collection<? extends Float> c) {
        this.ensureIndex(index);
        int n = c.size();
        if (n == 0) {
            return false;
        }
        final Iterator<? extends Float> i = c.iterator();
        while (n-- != 0) {
            this.add(index++, (Float)i.next());
        }
        return true;
    }
    
    public boolean addAll(final int index, final Collection<? extends Float> c) {
        return this.addAll((long)index, c);
    }
    
    @Override
    public boolean addAll(final Collection<? extends Float> c) {
        return this.addAll(this.size64(), c);
    }
    
    @Override
    public FloatBigListIterator iterator() {
        return this.listIterator();
    }
    
    @Override
    public FloatBigListIterator listIterator() {
        return this.listIterator(0L);
    }
    
    @Override
    public FloatBigListIterator listIterator(final long index) {
        this.ensureIndex(index);
        return new AbstractFloatBigListIterator() {
            long pos = index;
            long last = -1L;
            
            @Override
            public boolean hasNext() {
                return this.pos < AbstractFloatBigList.this.size64();
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0L;
            }
            
            @Override
            public float nextFloat() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractFloatBigList this$0 = AbstractFloatBigList.this;
                final long last = this.pos++;
                this.last = last;
                return this$0.getFloat(last);
            }
            
            @Override
            public float previousFloat() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractFloatBigList this$0 = AbstractFloatBigList.this;
                final long n = this.pos - 1L;
                this.pos = n;
                this.last = n;
                return this$0.getFloat(n);
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
            public void add(final float k) {
                AbstractFloatBigList.this.add(this.pos++, k);
                this.last = -1L;
            }
            
            @Override
            public void set(final float k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractFloatBigList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractFloatBigList.this.removeFloat(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1L;
            }
        };
    }
    
    public FloatBigListIterator listIterator(final int index) {
        return this.listIterator((long)index);
    }
    
    @Override
    public boolean contains(final float k) {
        return this.indexOf(k) >= 0L;
    }
    
    @Override
    public long indexOf(final float k) {
        final FloatBigListIterator i = this.listIterator();
        while (i.hasNext()) {
            final float e = i.nextFloat();
            if (Float.floatToIntBits(k) == Float.floatToIntBits(e)) {
                return i.previousIndex();
            }
        }
        return -1L;
    }
    
    @Override
    public long lastIndexOf(final float k) {
        final FloatBigListIterator i = this.listIterator(this.size64());
        while (i.hasPrevious()) {
            final float e = i.previousFloat();
            if (Float.floatToIntBits(k) == Float.floatToIntBits(e)) {
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
                this.add(0.0f);
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
    public FloatBigList subList(final long from, final long to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new FloatSubList(this, from, to);
    }
    
    @Override
    public void removeElements(final long from, final long to) {
        this.ensureIndex(to);
        final FloatBigListIterator i = this.listIterator(from);
        long n = to - from;
        if (n < 0L) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        while (n-- != 0L) {
            i.nextFloat();
            i.remove();
        }
    }
    
    @Override
    public void addElements(long index, final float[][] a, long offset, long length) {
        this.ensureIndex(index);
        FloatBigArrays.ensureOffsetLength(a, offset, length);
        while (length-- != 0L) {
            this.add(index++, FloatBigArrays.get(a, offset++));
        }
    }
    
    @Override
    public void addElements(final long index, final float[][] a) {
        this.addElements(index, a, 0L, FloatBigArrays.length(a));
    }
    
    @Override
    public void getElements(final long from, final float[][] a, long offset, long length) {
        final FloatBigListIterator i = this.listIterator(from);
        FloatBigArrays.ensureOffsetLength(a, offset, length);
        if (from + length > this.size64()) {
            throw new IndexOutOfBoundsException("End index (" + (from + length) + ") is greater than list size (" + this.size64() + ")");
        }
        while (length-- != 0L) {
            FloatBigArrays.set(a, offset++, i.nextFloat());
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
        if (l instanceof FloatBigList) {
            final FloatBigListIterator i1 = this.listIterator();
            final FloatBigListIterator i2 = ((FloatBigList)l).listIterator();
            while (s-- != 0L) {
                if (i1.nextFloat() != i2.nextFloat()) {
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
    public int compareTo(final BigList<? extends Float> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof FloatBigList) {
            final FloatBigListIterator i1 = this.listIterator();
            final FloatBigListIterator i2 = ((FloatBigList)l).listIterator();
            while (i1.hasNext() && i2.hasNext()) {
                final float e1 = i1.nextFloat();
                final float e2 = i2.nextFloat();
                final int r;
                if ((r = Float.compare(e1, e2)) != 0) {
                    return r;
                }
            }
            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
        }
        final BigListIterator<? extends Float> i3 = this.listIterator();
        final BigListIterator<? extends Float> i4 = l.listIterator();
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
        final FloatIterator i = this.iterator();
        int h = 1;
        long s = this.size64();
        while (s-- != 0L) {
            final float k = i.nextFloat();
            h = 31 * h + HashCommon.float2int(k);
        }
        return h;
    }
    
    @Override
    public void push(final float o) {
        this.add(o);
    }
    
    @Override
    public float popFloat() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.removeFloat(this.size64() - 1L);
    }
    
    @Override
    public float topFloat() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getFloat(this.size64() - 1L);
    }
    
    @Override
    public float peekFloat(final int i) {
        return this.getFloat(this.size64() - 1L - i);
    }
    
    public float getFloat(final int index) {
        return this.getFloat(index);
    }
    
    @Override
    public boolean rem(final float k) {
        final long index = this.indexOf(k);
        if (index == -1L) {
            return false;
        }
        this.removeFloat(index);
        return true;
    }
    
    @Override
    public boolean addAll(final long index, final FloatCollection c) {
        return this.addAll(index, (Collection<? extends Float>)c);
    }
    
    @Override
    public boolean addAll(final long index, final FloatBigList l) {
        return this.addAll(index, (FloatCollection)l);
    }
    
    @Override
    public boolean addAll(final FloatCollection c) {
        return this.addAll(this.size64(), c);
    }
    
    @Override
    public boolean addAll(final FloatBigList l) {
        return this.addAll(this.size64(), l);
    }
    
    @Deprecated
    @Override
    public void add(final long index, final Float ok) {
        this.add(index, (float)ok);
    }
    
    @Deprecated
    @Override
    public Float set(final long index, final Float ok) {
        return this.set(index, (float)ok);
    }
    
    @Deprecated
    @Override
    public Float get(final long index) {
        return this.getFloat(index);
    }
    
    @Deprecated
    @Override
    public long indexOf(final Object ok) {
        return this.indexOf((float)ok);
    }
    
    @Deprecated
    @Override
    public long lastIndexOf(final Object ok) {
        return this.lastIndexOf((float)ok);
    }
    
    @Deprecated
    public Float remove(final int index) {
        return this.removeFloat(index);
    }
    
    @Deprecated
    @Override
    public Float remove(final long index) {
        return this.removeFloat(index);
    }
    
    @Deprecated
    @Override
    public void push(final Float o) {
        this.push((float)o);
    }
    
    @Deprecated
    @Override
    public Float pop() {
        return this.popFloat();
    }
    
    @Deprecated
    @Override
    public Float top() {
        return this.topFloat();
    }
    
    @Deprecated
    @Override
    public Float peek(final int i) {
        return this.peekFloat(i);
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final FloatIterator i = this.iterator();
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
            final float k = i.nextFloat();
            s.append(String.valueOf(k));
        }
        s.append("]");
        return s.toString();
    }
    
    public static class FloatSubList extends AbstractFloatBigList implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final FloatBigList l;
        protected final long from;
        protected long to;
        private static final boolean ASSERTS = false;
        
        public FloatSubList(final FloatBigList l, final long from, final long to) {
            this.l = l;
            this.from = from;
            this.to = to;
        }
        
        private void assertRange() {
        }
        
        @Override
        public boolean add(final float k) {
            this.l.add(this.to, k);
            ++this.to;
            return true;
        }
        
        @Override
        public void add(final long index, final float k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Float> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        @Override
        public float getFloat(final long index) {
            this.ensureRestrictedIndex(index);
            return this.l.getFloat(this.from + index);
        }
        
        @Override
        public float removeFloat(final long index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeFloat(this.from + index);
        }
        
        @Override
        public float set(final long index, final float k) {
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
        public void getElements(final long from, final float[][] a, final long offset, final long length) {
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
        public void addElements(final long index, final float[][] a, final long offset, final long length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
        }
        
        @Override
        public FloatBigListIterator listIterator(final long index) {
            this.ensureIndex(index);
            return new AbstractFloatBigListIterator() {
                long pos = index;
                long last = -1L;
                
                @Override
                public boolean hasNext() {
                    return this.pos < FloatSubList.this.size64();
                }
                
                @Override
                public boolean hasPrevious() {
                    return this.pos > 0L;
                }
                
                @Override
                public float nextFloat() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final FloatBigList l = FloatSubList.this.l;
                    final long from = FloatSubList.this.from;
                    final long last = this.pos++;
                    this.last = last;
                    return l.getFloat(from + last);
                }
                
                @Override
                public float previousFloat() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final FloatBigList l = FloatSubList.this.l;
                    final long from = FloatSubList.this.from;
                    final long n = this.pos - 1L;
                    this.pos = n;
                    this.last = n;
                    return l.getFloat(from + n);
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
                public void add(final float k) {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    FloatSubList.this.add(this.pos++, k);
                    this.last = -1L;
                }
                
                @Override
                public void set(final float k) {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    FloatSubList.this.set(this.last, k);
                }
                
                @Override
                public void remove() {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    FloatSubList.this.removeFloat(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1L;
                }
            };
        }
        
        @Override
        public FloatBigList subList(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new FloatSubList(this, from, to);
        }
        
        @Override
        public boolean rem(final float k) {
            final long index = this.indexOf(k);
            if (index == -1L) {
                return false;
            }
            --this.to;
            this.l.removeFloat(this.from + index);
            return true;
        }
        
        @Override
        public boolean remove(final Object o) {
            return this.rem((float)o);
        }
        
        @Override
        public boolean addAll(final long index, final FloatCollection c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        public boolean addAll(final long index, final FloatList l) {
            this.ensureIndex(index);
            this.to += l.size();
            return this.l.addAll(this.from + index, l);
        }
    }
}
