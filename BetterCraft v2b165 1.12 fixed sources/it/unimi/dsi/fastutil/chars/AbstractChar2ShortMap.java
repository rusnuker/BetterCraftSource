// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.shorts.AbstractShortIterator;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Iterator;
import java.util.Map;
import java.io.Serializable;

public abstract class AbstractChar2ShortMap extends AbstractChar2ShortFunction implements Char2ShortMap, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractChar2ShortMap() {
    }
    
    @Override
    public boolean containsValue(final Object ov) {
        return ov != null && this.containsValue((short)ov);
    }
    
    @Override
    public boolean containsValue(final short v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final char k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends Character, ? extends Short> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends Character, ? extends Short>> i = m.entrySet().iterator();
        if (m instanceof Char2ShortMap) {
            while (n-- != 0) {
                final Entry e = (Entry)i.next();
                this.put(e.getCharKey(), e.getShortValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends Character, ? extends Short> e2 = (Map.Entry<? extends Character, ? extends Short>)i.next();
                this.put((Character)e2.getKey(), (Short)e2.getValue());
            }
        }
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public CharSet keySet() {
        return new AbstractCharSet() {
            @Override
            public boolean contains(final char k) {
                return AbstractChar2ShortMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractChar2ShortMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractChar2ShortMap.this.clear();
            }
            
            @Override
            public CharIterator iterator() {
                return new AbstractCharIterator() {
                    final ObjectIterator<Map.Entry<Character, Short>> i = AbstractChar2ShortMap.this.entrySet().iterator();
                    
                    @Override
                    public char nextChar() {
                        return this.i.next().getCharKey();
                    }
                    
                    @Override
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }
                    
                    @Override
                    public void remove() {
                        this.i.remove();
                    }
                };
            }
        };
    }
    
    @Override
    public ShortCollection values() {
        return new AbstractShortCollection() {
            @Override
            public boolean contains(final short k) {
                return AbstractChar2ShortMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractChar2ShortMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractChar2ShortMap.this.clear();
            }
            
            @Override
            public ShortIterator iterator() {
                return new AbstractShortIterator() {
                    final ObjectIterator<Map.Entry<Character, Short>> i = AbstractChar2ShortMap.this.entrySet().iterator();
                    
                    @Deprecated
                    @Override
                    public short nextShort() {
                        return this.i.next().getShortValue();
                    }
                    
                    @Override
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }
                };
            }
        };
    }
    
    @Override
    public ObjectSet<Map.Entry<Character, Short>> entrySet() {
        return (ObjectSet<Map.Entry<Character, Short>>)this.char2ShortEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<Character, Short>> i = this.entrySet().iterator();
        while (n-- != 0) {
            h += i.next().hashCode();
        }
        return h;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Map)) {
            return false;
        }
        final Map<?, ?> m = (Map<?, ?>)o;
        return m.size() == this.size() && this.entrySet().containsAll(m.entrySet());
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<? extends Map.Entry<Character, Short>> i = this.entrySet().iterator();
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
            final Entry e = i.next();
            s.append(String.valueOf(e.getCharKey()));
            s.append("=>");
            s.append(String.valueOf(e.getShortValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry
    {
        protected char key;
        protected short value;
        
        public BasicEntry(final Character key, final Short value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final char key, final short value) {
            this.key = key;
            this.value = value;
        }
        
        @Deprecated
        @Override
        public Character getKey() {
            return this.key;
        }
        
        @Override
        public char getCharKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Short getValue() {
            return this.value;
        }
        
        @Override
        public short getShortValue() {
            return this.value;
        }
        
        @Override
        public short setValue(final short value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Short setValue(final Short value) {
            return this.setValue((short)value);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            return e.getKey() != null && e.getKey() instanceof Character && e.getValue() != null && e.getValue() instanceof Short && this.key == (char)e.getKey() && this.value == (short)e.getValue();
        }
        
        @Override
        public int hashCode() {
            return this.key ^ this.value;
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}
