// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.chars.AbstractCharIterator;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.AbstractCharCollection;
import it.unimi.dsi.fastutil.chars.CharCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Iterator;
import java.util.Map;
import java.io.Serializable;

public abstract class AbstractShort2CharMap extends AbstractShort2CharFunction implements Short2CharMap, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractShort2CharMap() {
    }
    
    @Override
    public boolean containsValue(final Object ov) {
        return ov != null && this.containsValue((char)ov);
    }
    
    @Override
    public boolean containsValue(final char v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final short k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends Short, ? extends Character> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends Short, ? extends Character>> i = m.entrySet().iterator();
        if (m instanceof Short2CharMap) {
            while (n-- != 0) {
                final Entry e = (Entry)i.next();
                this.put(e.getShortKey(), e.getCharValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends Short, ? extends Character> e2 = (Map.Entry<? extends Short, ? extends Character>)i.next();
                this.put((Short)e2.getKey(), (Character)e2.getValue());
            }
        }
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public ShortSet keySet() {
        return new AbstractShortSet() {
            @Override
            public boolean contains(final short k) {
                return AbstractShort2CharMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractShort2CharMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractShort2CharMap.this.clear();
            }
            
            @Override
            public ShortIterator iterator() {
                return new AbstractShortIterator() {
                    final ObjectIterator<Map.Entry<Short, Character>> i = AbstractShort2CharMap.this.entrySet().iterator();
                    
                    @Override
                    public short nextShort() {
                        return this.i.next().getShortKey();
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
    public CharCollection values() {
        return new AbstractCharCollection() {
            @Override
            public boolean contains(final char k) {
                return AbstractShort2CharMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractShort2CharMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractShort2CharMap.this.clear();
            }
            
            @Override
            public CharIterator iterator() {
                return new AbstractCharIterator() {
                    final ObjectIterator<Map.Entry<Short, Character>> i = AbstractShort2CharMap.this.entrySet().iterator();
                    
                    @Deprecated
                    @Override
                    public char nextChar() {
                        return this.i.next().getCharValue();
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
    public ObjectSet<Map.Entry<Short, Character>> entrySet() {
        return (ObjectSet<Map.Entry<Short, Character>>)this.short2CharEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<Short, Character>> i = this.entrySet().iterator();
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
        final ObjectIterator<? extends Map.Entry<Short, Character>> i = this.entrySet().iterator();
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
            s.append(String.valueOf(e.getShortKey()));
            s.append("=>");
            s.append(String.valueOf(e.getCharValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry
    {
        protected short key;
        protected char value;
        
        public BasicEntry(final Short key, final Character value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final short key, final char value) {
            this.key = key;
            this.value = value;
        }
        
        @Deprecated
        @Override
        public Short getKey() {
            return this.key;
        }
        
        @Override
        public short getShortKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Character getValue() {
            return this.value;
        }
        
        @Override
        public char getCharValue() {
            return this.value;
        }
        
        @Override
        public char setValue(final char value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character setValue(final Character value) {
            return this.setValue((char)value);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            return e.getKey() != null && e.getKey() instanceof Short && e.getValue() != null && e.getValue() instanceof Character && this.key == (short)e.getKey() && this.value == (char)e.getValue();
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
