// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.io.Serializable;
import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.BigList;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;

public abstract class AbstractCharBigList extends AbstractCharCollection implements CharBigList, CharStack
{
    protected AbstractCharBigList() {
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
    public void add(final long index, final char k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final char k) {
        this.add(this.size64(), k);
        return true;
    }
    
    @Override
    public char removeChar(final long i) {
        throw new UnsupportedOperationException();
    }
    
    public char removeChar(final int i) {
        return this.removeChar((long)i);
    }
    
    @Override
    public char set(final long index, final char k) {
        throw new UnsupportedOperationException();
    }
    
    public char set(final int index, final char k) {
        return this.set((long)index, k);
    }
    
    @Override
    public boolean addAll(long index, final Collection<? extends Character> c) {
        this.ensureIndex(index);
        int n = c.size();
        if (n == 0) {
            return false;
        }
        final Iterator<? extends Character> i = c.iterator();
        while (n-- != 0) {
            this.add(index++, (Character)i.next());
        }
        return true;
    }
    
    public boolean addAll(final int index, final Collection<? extends Character> c) {
        return this.addAll((long)index, c);
    }
    
    @Override
    public boolean addAll(final Collection<? extends Character> c) {
        return this.addAll(this.size64(), c);
    }
    
    @Override
    public CharBigListIterator iterator() {
        return this.listIterator();
    }
    
    @Override
    public CharBigListIterator listIterator() {
        return this.listIterator(0L);
    }
    
    @Override
    public CharBigListIterator listIterator(final long index) {
        this.ensureIndex(index);
        return new AbstractCharBigListIterator() {
            long pos = index;
            long last = -1L;
            
            @Override
            public boolean hasNext() {
                return this.pos < AbstractCharBigList.this.size64();
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0L;
            }
            
            @Override
            public char nextChar() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractCharBigList this$0 = AbstractCharBigList.this;
                final long last = this.pos++;
                this.last = last;
                return this$0.getChar(last);
            }
            
            @Override
            public char previousChar() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractCharBigList this$0 = AbstractCharBigList.this;
                final long n = this.pos - 1L;
                this.pos = n;
                this.last = n;
                return this$0.getChar(n);
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
            public void add(final char k) {
                AbstractCharBigList.this.add(this.pos++, k);
                this.last = -1L;
            }
            
            @Override
            public void set(final char k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractCharBigList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractCharBigList.this.removeChar(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1L;
            }
        };
    }
    
    public CharBigListIterator listIterator(final int index) {
        return this.listIterator((long)index);
    }
    
    @Override
    public boolean contains(final char k) {
        return this.indexOf(k) >= 0L;
    }
    
    @Override
    public long indexOf(final char k) {
        final CharBigListIterator i = this.listIterator();
        while (i.hasNext()) {
            final char e = i.nextChar();
            if (k == e) {
                return i.previousIndex();
            }
        }
        return -1L;
    }
    
    @Override
    public long lastIndexOf(final char k) {
        final CharBigListIterator i = this.listIterator(this.size64());
        while (i.hasPrevious()) {
            final char e = i.previousChar();
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
                this.add('\0');
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
    public CharBigList subList(final long from, final long to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new CharSubList(this, from, to);
    }
    
    @Override
    public void removeElements(final long from, final long to) {
        this.ensureIndex(to);
        final CharBigListIterator i = this.listIterator(from);
        long n = to - from;
        if (n < 0L) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        while (n-- != 0L) {
            i.nextChar();
            i.remove();
        }
    }
    
    @Override
    public void addElements(long index, final char[][] a, long offset, long length) {
        this.ensureIndex(index);
        CharBigArrays.ensureOffsetLength(a, offset, length);
        while (length-- != 0L) {
            this.add(index++, CharBigArrays.get(a, offset++));
        }
    }
    
    @Override
    public void addElements(final long index, final char[][] a) {
        this.addElements(index, a, 0L, CharBigArrays.length(a));
    }
    
    @Override
    public void getElements(final long from, final char[][] a, long offset, long length) {
        final CharBigListIterator i = this.listIterator(from);
        CharBigArrays.ensureOffsetLength(a, offset, length);
        if (from + length > this.size64()) {
            throw new IndexOutOfBoundsException("End index (" + (from + length) + ") is greater than list size (" + this.size64() + ")");
        }
        while (length-- != 0L) {
            CharBigArrays.set(a, offset++, i.nextChar());
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
        if (l instanceof CharBigList) {
            final CharBigListIterator i1 = this.listIterator();
            final CharBigListIterator i2 = ((CharBigList)l).listIterator();
            while (s-- != 0L) {
                if (i1.nextChar() != i2.nextChar()) {
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
    public int compareTo(final BigList<? extends Character> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof CharBigList) {
            final CharBigListIterator i1 = this.listIterator();
            final CharBigListIterator i2 = ((CharBigList)l).listIterator();
            while (i1.hasNext() && i2.hasNext()) {
                final char e1 = i1.nextChar();
                final char e2 = i2.nextChar();
                final int r;
                if ((r = Character.compare(e1, e2)) != 0) {
                    return r;
                }
            }
            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
        }
        final BigListIterator<? extends Character> i3 = this.listIterator();
        final BigListIterator<? extends Character> i4 = l.listIterator();
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
        final CharIterator i = this.iterator();
        int h = 1;
        long s = this.size64();
        while (s-- != 0L) {
            final char k = i.nextChar();
            h = 31 * h + k;
        }
        return h;
    }
    
    @Override
    public void push(final char o) {
        this.add(o);
    }
    
    @Override
    public char popChar() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.removeChar(this.size64() - 1L);
    }
    
    @Override
    public char topChar() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getChar(this.size64() - 1L);
    }
    
    @Override
    public char peekChar(final int i) {
        return this.getChar(this.size64() - 1L - i);
    }
    
    public char getChar(final int index) {
        return this.getChar(index);
    }
    
    @Override
    public boolean rem(final char k) {
        final long index = this.indexOf(k);
        if (index == -1L) {
            return false;
        }
        this.removeChar(index);
        return true;
    }
    
    @Override
    public boolean addAll(final long index, final CharCollection c) {
        return this.addAll(index, (Collection<? extends Character>)c);
    }
    
    @Override
    public boolean addAll(final long index, final CharBigList l) {
        return this.addAll(index, (CharCollection)l);
    }
    
    @Override
    public boolean addAll(final CharCollection c) {
        return this.addAll(this.size64(), c);
    }
    
    @Override
    public boolean addAll(final CharBigList l) {
        return this.addAll(this.size64(), l);
    }
    
    @Deprecated
    @Override
    public void add(final long index, final Character ok) {
        this.add(index, (char)ok);
    }
    
    @Deprecated
    @Override
    public Character set(final long index, final Character ok) {
        return this.set(index, (char)ok);
    }
    
    @Deprecated
    @Override
    public Character get(final long index) {
        return this.getChar(index);
    }
    
    @Deprecated
    @Override
    public long indexOf(final Object ok) {
        return this.indexOf((char)ok);
    }
    
    @Deprecated
    @Override
    public long lastIndexOf(final Object ok) {
        return this.lastIndexOf((char)ok);
    }
    
    @Deprecated
    public Character remove(final int index) {
        return this.removeChar(index);
    }
    
    @Deprecated
    @Override
    public Character remove(final long index) {
        return this.removeChar(index);
    }
    
    @Deprecated
    @Override
    public void push(final Character o) {
        this.push((char)o);
    }
    
    @Deprecated
    @Override
    public Character pop() {
        return this.popChar();
    }
    
    @Deprecated
    @Override
    public Character top() {
        return this.topChar();
    }
    
    @Deprecated
    @Override
    public Character peek(final int i) {
        return this.peekChar(i);
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final CharIterator i = this.iterator();
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
            final char k = i.nextChar();
            s.append(String.valueOf(k));
        }
        s.append("]");
        return s.toString();
    }
    
    public static class CharSubList extends AbstractCharBigList implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final CharBigList l;
        protected final long from;
        protected long to;
        private static final boolean ASSERTS = false;
        
        public CharSubList(final CharBigList l, final long from, final long to) {
            this.l = l;
            this.from = from;
            this.to = to;
        }
        
        private void assertRange() {
        }
        
        @Override
        public boolean add(final char k) {
            this.l.add(this.to, k);
            ++this.to;
            return true;
        }
        
        @Override
        public void add(final long index, final char k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Character> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        @Override
        public char getChar(final long index) {
            this.ensureRestrictedIndex(index);
            return this.l.getChar(this.from + index);
        }
        
        @Override
        public char removeChar(final long index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeChar(this.from + index);
        }
        
        @Override
        public char set(final long index, final char k) {
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
        public void getElements(final long from, final char[][] a, final long offset, final long length) {
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
        public void addElements(final long index, final char[][] a, final long offset, final long length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
        }
        
        @Override
        public CharBigListIterator listIterator(final long index) {
            this.ensureIndex(index);
            return new AbstractCharBigListIterator() {
                long pos = index;
                long last = -1L;
                
                @Override
                public boolean hasNext() {
                    return this.pos < CharSubList.this.size64();
                }
                
                @Override
                public boolean hasPrevious() {
                    return this.pos > 0L;
                }
                
                @Override
                public char nextChar() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final CharBigList l = CharSubList.this.l;
                    final long from = CharSubList.this.from;
                    final long last = this.pos++;
                    this.last = last;
                    return l.getChar(from + last);
                }
                
                @Override
                public char previousChar() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final CharBigList l = CharSubList.this.l;
                    final long from = CharSubList.this.from;
                    final long n = this.pos - 1L;
                    this.pos = n;
                    this.last = n;
                    return l.getChar(from + n);
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
                public void add(final char k) {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    CharSubList.this.add(this.pos++, k);
                    this.last = -1L;
                }
                
                @Override
                public void set(final char k) {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    CharSubList.this.set(this.last, k);
                }
                
                @Override
                public void remove() {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    CharSubList.this.removeChar(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1L;
                }
            };
        }
        
        @Override
        public CharBigList subList(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new CharSubList(this, from, to);
        }
        
        @Override
        public boolean rem(final char k) {
            final long index = this.indexOf(k);
            if (index == -1L) {
                return false;
            }
            --this.to;
            this.l.removeChar(this.from + index);
            return true;
        }
        
        @Override
        public boolean remove(final Object o) {
            return this.rem((char)o);
        }
        
        @Override
        public boolean addAll(final long index, final CharCollection c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        public boolean addAll(final long index, final CharList l) {
            this.ensureIndex(index);
            this.to += l.size();
            return this.l.addAll(this.from + index, l);
        }
    }
}
