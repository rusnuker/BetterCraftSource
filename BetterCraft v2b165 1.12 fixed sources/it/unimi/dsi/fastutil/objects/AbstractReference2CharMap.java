// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.chars.AbstractCharIterator;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.AbstractCharCollection;
import it.unimi.dsi.fastutil.chars.CharCollection;
import java.util.Iterator;
import java.util.Map;
import java.io.Serializable;

public abstract class AbstractReference2CharMap<K> extends AbstractReference2CharFunction<K> implements Reference2CharMap<K>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractReference2CharMap() {
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
    public boolean containsKey(final Object k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends Character> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends K, ? extends Character>> i = m.entrySet().iterator();
        if (m instanceof Reference2CharMap) {
            while (n-- != 0) {
                final Entry<? extends K> e = (Entry<? extends K>)i.next();
                this.put((K)e.getKey(), e.getCharValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends K, ? extends Character> e2 = (Map.Entry<? extends K, ? extends Character>)i.next();
                this.put((K)e2.getKey(), (Character)e2.getValue());
            }
        }
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public ReferenceSet<K> keySet() {
        return new AbstractReferenceSet<K>() {
            @Override
            public boolean contains(final Object k) {
                return AbstractReference2CharMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractReference2CharMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractReference2CharMap.this.clear();
            }
            
            @Override
            public ObjectIterator<K> iterator() {
                return new AbstractObjectIterator<K>() {
                    final ObjectIterator<Map.Entry<K, Character>> i = AbstractReference2CharMap.this.entrySet().iterator();
                    
                    @Override
                    public K next() {
                        return (K)this.i.next().getKey();
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
                return AbstractReference2CharMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractReference2CharMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractReference2CharMap.this.clear();
            }
            
            @Override
            public CharIterator iterator() {
                return new AbstractCharIterator() {
                    final ObjectIterator<Map.Entry<K, Character>> i = AbstractReference2CharMap.this.entrySet().iterator();
                    
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
    public ObjectSet<Map.Entry<K, Character>> entrySet() {
        return (ObjectSet<Map.Entry<K, Character>>)this.reference2CharEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<K, Character>> i = this.entrySet().iterator();
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
        final ObjectIterator<? extends Map.Entry<K, Character>> i = this.entrySet().iterator();
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
            final Entry<K> e = i.next();
            if (this == e.getKey()) {
                s.append("(this map)");
            }
            else {
                s.append(String.valueOf(e.getKey()));
            }
            s.append("=>");
            s.append(String.valueOf(e.getCharValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry<K> implements Entry<K>
    {
        protected K key;
        protected char value;
        
        public BasicEntry(final K key, final Character value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final K key, final char value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public K getKey() {
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
            return e.getValue() != null && e.getValue() instanceof Character && this.key == e.getKey() && this.value == (char)e.getValue();
        }
        
        @Override
        public int hashCode() {
            return System.identityHashCode(this.key) ^ this.value;
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}
