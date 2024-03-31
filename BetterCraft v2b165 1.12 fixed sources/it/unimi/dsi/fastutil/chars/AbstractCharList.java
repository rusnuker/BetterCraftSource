// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.io.Serializable;
import java.util.ListIterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;

public abstract class AbstractCharList extends AbstractCharCollection implements CharList, CharStack
{
    protected AbstractCharList() {
    }
    
    protected void ensureIndex(final int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index > this.size()) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + this.size() + ")");
        }
    }
    
    protected void ensureRestrictedIndex(final int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index >= this.size()) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size() + ")");
        }
    }
    
    @Override
    public void add(final int index, final char k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final char k) {
        this.add(this.size(), k);
        return true;
    }
    
    @Override
    public char removeChar(final int i) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public char set(final int index, final char k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(int index, final Collection<? extends Character> c) {
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
    
    @Override
    public boolean addAll(final Collection<? extends Character> c) {
        return this.addAll(this.size(), c);
    }
    
    @Deprecated
    @Override
    public CharListIterator charListIterator() {
        return this.listIterator();
    }
    
    @Deprecated
    @Override
    public CharListIterator charListIterator(final int index) {
        return this.listIterator(index);
    }
    
    @Override
    public CharListIterator iterator() {
        return this.listIterator();
    }
    
    @Override
    public CharListIterator listIterator() {
        return this.listIterator(0);
    }
    
    @Override
    public CharListIterator listIterator(final int index) {
        this.ensureIndex(index);
        return new AbstractCharListIterator() {
            int pos = index;
            int last = -1;
            
            @Override
            public boolean hasNext() {
                return this.pos < AbstractCharList.this.size();
            }
            
            @Override
            public boolean hasPrevious() {
                return this.pos > 0;
            }
            
            @Override
            public char nextChar() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractCharList this$0 = AbstractCharList.this;
                final int last = this.pos++;
                this.last = last;
                return this$0.getChar(last);
            }
            
            @Override
            public char previousChar() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractCharList this$0 = AbstractCharList.this;
                final int n = this.pos - 1;
                this.pos = n;
                this.last = n;
                return this$0.getChar(n);
            }
            
            @Override
            public int nextIndex() {
                return this.pos;
            }
            
            @Override
            public int previousIndex() {
                return this.pos - 1;
            }
            
            @Override
            public void add(final char k) {
                AbstractCharList.this.add(this.pos++, k);
                this.last = -1;
            }
            
            @Override
            public void set(final char k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractCharList.this.set(this.last, k);
            }
            
            @Override
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractCharList.this.removeChar(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }
        };
    }
    
    @Override
    public boolean contains(final char k) {
        return this.indexOf(k) >= 0;
    }
    
    @Override
    public int indexOf(final char k) {
        final CharListIterator i = this.listIterator();
        while (i.hasNext()) {
            final char e = i.nextChar();
            if (k == e) {
                return i.previousIndex();
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final char k) {
        final CharListIterator i = this.listIterator(this.size());
        while (i.hasPrevious()) {
            final char e = i.previousChar();
            if (k == e) {
                return i.nextIndex();
            }
        }
        return -1;
    }
    
    @Override
    public void size(final int size) {
        int i = this.size();
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
    
    @Override
    public CharList subList(final int from, final int to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        return new CharSubList(this, from, to);
    }
    
    @Deprecated
    @Override
    public CharList charSubList(final int from, final int to) {
        return this.subList(from, to);
    }
    
    @Override
    public void removeElements(final int from, final int to) {
        this.ensureIndex(to);
        final CharListIterator i = this.listIterator(from);
        int n = to - from;
        if (n < 0) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        while (n-- != 0) {
            i.nextChar();
            i.remove();
        }
    }
    
    @Override
    public void addElements(int index, final char[] a, int offset, int length) {
        this.ensureIndex(index);
        if (offset < 0) {
            throw new ArrayIndexOutOfBoundsException("Offset (" + offset + ") is negative");
        }
        if (offset + length > a.length) {
            throw new ArrayIndexOutOfBoundsException("End index (" + (offset + length) + ") is greater than array length (" + a.length + ")");
        }
        while (length-- != 0) {
            this.add(index++, a[offset++]);
        }
    }
    
    @Override
    public void addElements(final int index, final char[] a) {
        this.addElements(index, a, 0, a.length);
    }
    
    @Override
    public void getElements(final int from, final char[] a, int offset, int length) {
        final CharListIterator i = this.listIterator(from);
        if (offset < 0) {
            throw new ArrayIndexOutOfBoundsException("Offset (" + offset + ") is negative");
        }
        if (offset + length > a.length) {
            throw new ArrayIndexOutOfBoundsException("End index (" + (offset + length) + ") is greater than array length (" + a.length + ")");
        }
        if (from + length > this.size()) {
            throw new IndexOutOfBoundsException("End index (" + (from + length) + ") is greater than list size (" + this.size() + ")");
        }
        while (length-- != 0) {
            a[offset++] = i.nextChar();
        }
    }
    
    private boolean valEquals(final Object a, final Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof List)) {
            return false;
        }
        final List<?> l = (List<?>)o;
        int s = this.size();
        if (s != l.size()) {
            return false;
        }
        if (l instanceof CharList) {
            final CharListIterator i1 = this.listIterator();
            final CharListIterator i2 = ((CharList)l).listIterator();
            while (s-- != 0) {
                if (i1.nextChar() != i2.nextChar()) {
                    return false;
                }
            }
            return true;
        }
        final ListIterator<?> i3 = this.listIterator();
        final ListIterator<?> i4 = l.listIterator();
        while (s-- != 0) {
            if (!this.valEquals(i3.next(), i4.next())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int compareTo(final List<? extends Character> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof CharList) {
            final CharListIterator i1 = this.listIterator();
            final CharListIterator i2 = ((CharList)l).listIterator();
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
        final ListIterator<? extends Character> i3 = this.listIterator();
        final ListIterator<? extends Character> i4 = l.listIterator();
        while (i3.hasNext() && i4.hasNext()) {
            final int r;
            if ((r = ((Comparable)i3.next()).compareTo(i4.next())) != 0) {
                return r;
            }
        }
        return i4.hasNext() ? -1 : (i3.hasNext() ? 1 : 0);
    }
    
    @Override
    public int hashCode() {
        final CharIterator i = this.iterator();
        int h = 1;
        int s = this.size();
        while (s-- != 0) {
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
        return this.removeChar(this.size() - 1);
    }
    
    @Override
    public char topChar() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getChar(this.size() - 1);
    }
    
    @Override
    public char peekChar(final int i) {
        return this.getChar(this.size() - 1 - i);
    }
    
    @Override
    public boolean rem(final char k) {
        final int index = this.indexOf(k);
        if (index == -1) {
            return false;
        }
        this.removeChar(index);
        return true;
    }
    
    @Override
    public boolean remove(final Object o) {
        return this.rem((char)o);
    }
    
    @Override
    public boolean addAll(final int index, final CharCollection c) {
        return this.addAll(index, (Collection<? extends Character>)c);
    }
    
    @Override
    public boolean addAll(final int index, final CharList l) {
        return this.addAll(index, (CharCollection)l);
    }
    
    @Override
    public boolean addAll(final CharCollection c) {
        return this.addAll(this.size(), c);
    }
    
    @Override
    public boolean addAll(final CharList l) {
        return this.addAll(this.size(), l);
    }
    
    @Override
    public void add(final int index, final Character ok) {
        this.add(index, (char)ok);
    }
    
    @Deprecated
    @Override
    public Character set(final int index, final Character ok) {
        return this.set(index, (char)ok);
    }
    
    @Deprecated
    @Override
    public Character get(final int index) {
        return this.getChar(index);
    }
    
    @Override
    public int indexOf(final Object ok) {
        return this.indexOf((char)ok);
    }
    
    @Override
    public int lastIndexOf(final Object ok) {
        return this.lastIndexOf((char)ok);
    }
    
    @Deprecated
    @Override
    public Character remove(final int index) {
        return this.removeChar(index);
    }
    
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
        int n = this.size();
        boolean first = true;
        s.append("[");
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
        s.append("]");
        return s.toString();
    }
    
    public static class CharSubList extends AbstractCharList implements Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final CharList l;
        protected final int from;
        protected int to;
        private static final boolean ASSERTS = false;
        
        public CharSubList(final CharList l, final int from, final int to) {
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
        public void add(final int index, final char k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Character> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        @Override
        public char getChar(final int index) {
            this.ensureRestrictedIndex(index);
            return this.l.getChar(this.from + index);
        }
        
        @Override
        public char removeChar(final int index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeChar(this.from + index);
        }
        
        @Override
        public char set(final int index, final char k) {
            this.ensureRestrictedIndex(index);
            return this.l.set(this.from + index, k);
        }
        
        @Override
        public void clear() {
            this.removeElements(0, this.size());
        }
        
        @Override
        public int size() {
            return this.to - this.from;
        }
        
        @Override
        public void getElements(final int from, final char[] a, final int offset, final int length) {
            this.ensureIndex(from);
            if (from + length > this.size()) {
                throw new IndexOutOfBoundsException("End index (" + from + length + ") is greater than list size (" + this.size() + ")");
            }
            this.l.getElements(this.from + from, a, offset, length);
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            this.l.removeElements(this.from + from, this.from + to);
            this.to -= to - from;
        }
        
        @Override
        public void addElements(final int index, final char[] a, final int offset, final int length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
        }
        
        @Override
        public CharListIterator listIterator(final int index) {
            this.ensureIndex(index);
            return new AbstractCharListIterator() {
                int pos = index;
                int last = -1;
                
                @Override
                public boolean hasNext() {
                    return this.pos < CharSubList.this.size();
                }
                
                @Override
                public boolean hasPrevious() {
                    return this.pos > 0;
                }
                
                @Override
                public char nextChar() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final CharList l = CharSubList.this.l;
                    final int from = CharSubList.this.from;
                    final int last = this.pos++;
                    this.last = last;
                    return l.getChar(from + last);
                }
                
                @Override
                public char previousChar() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final CharList l = CharSubList.this.l;
                    final int from = CharSubList.this.from;
                    final int n = this.pos - 1;
                    this.pos = n;
                    this.last = n;
                    return l.getChar(from + n);
                }
                
                @Override
                public int nextIndex() {
                    return this.pos;
                }
                
                @Override
                public int previousIndex() {
                    return this.pos - 1;
                }
                
                @Override
                public void add(final char k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    CharSubList.this.add(this.pos++, k);
                    this.last = -1;
                }
                
                @Override
                public void set(final char k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    CharSubList.this.set(this.last, k);
                }
                
                @Override
                public void remove() {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    CharSubList.this.removeChar(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1;
                }
            };
        }
        
        @Override
        public CharList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            }
            return new CharSubList(this, from, to);
        }
        
        @Override
        public boolean rem(final char k) {
            final int index = this.indexOf(k);
            if (index == -1) {
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
        public boolean addAll(final int index, final CharCollection c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        @Override
        public boolean addAll(final int index, final CharList l) {
            this.ensureIndex(index);
            this.to += l.size();
            return this.l.addAll(this.from + index, l);
        }
    }
}
