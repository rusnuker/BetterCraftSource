// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.ints.AbstractIntIterator;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Iterator;
import java.util.Map;
import java.io.Serializable;

public abstract class AbstractChar2IntMap extends AbstractChar2IntFunction implements Char2IntMap, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractChar2IntMap() {
    }
    
    @Override
    public boolean containsValue(final Object ov) {
        return ov != null && this.containsValue((int)ov);
    }
    
    @Override
    public boolean containsValue(final int v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final char k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends Character, ? extends Integer> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends Character, ? extends Integer>> i = m.entrySet().iterator();
        if (m instanceof Char2IntMap) {
            while (n-- != 0) {
                final Entry e = (Entry)i.next();
                this.put(e.getCharKey(), e.getIntValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends Character, ? extends Integer> e2 = (Map.Entry<? extends Character, ? extends Integer>)i.next();
                this.put((Character)e2.getKey(), (Integer)e2.getValue());
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
                return AbstractChar2IntMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractChar2IntMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractChar2IntMap.this.clear();
            }
            
            @Override
            public CharIterator iterator() {
                return new AbstractCharIterator() {
                    final ObjectIterator<Map.Entry<Character, Integer>> i = AbstractChar2IntMap.this.entrySet().iterator();
                    
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
    public IntCollection values() {
        return new AbstractIntCollection() {
            @Override
            public boolean contains(final int k) {
                return AbstractChar2IntMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractChar2IntMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractChar2IntMap.this.clear();
            }
            
            @Override
            public IntIterator iterator() {
                return new AbstractIntIterator() {
                    final ObjectIterator<Map.Entry<Character, Integer>> i = AbstractChar2IntMap.this.entrySet().iterator();
                    
                    @Deprecated
                    @Override
                    public int nextInt() {
                        return this.i.next().getIntValue();
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
    public ObjectSet<Map.Entry<Character, Integer>> entrySet() {
        return (ObjectSet<Map.Entry<Character, Integer>>)this.char2IntEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<Character, Integer>> i = this.entrySet().iterator();
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
        final ObjectIterator<? extends Map.Entry<Character, Integer>> i = this.entrySet().iterator();
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
            s.append(String.valueOf(e.getIntValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry
    {
        protected char key;
        protected int value;
        
        public BasicEntry(final Character key, final Integer value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final char key, final int value) {
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
        public Integer getValue() {
            return this.value;
        }
        
        @Override
        public int getIntValue() {
            return this.value;
        }
        
        @Override
        public int setValue(final int value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer setValue(final Integer value) {
            return this.setValue((int)value);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            return e.getKey() != null && e.getKey() instanceof Character && e.getValue() != null && e.getValue() instanceof Integer && this.key == (char)e.getKey() && this.value == (int)e.getValue();
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
