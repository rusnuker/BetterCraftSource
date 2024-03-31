// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.AbstractObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Iterator;
import java.util.Map;
import java.io.Serializable;

public abstract class AbstractChar2ObjectMap<V> extends AbstractChar2ObjectFunction<V> implements Char2ObjectMap<V>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractChar2ObjectMap() {
    }
    
    @Override
    public boolean containsValue(final Object v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final char k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends Character, ? extends V> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends Character, ? extends V>> i = m.entrySet().iterator();
        if (m instanceof Char2ObjectMap) {
            while (n-- != 0) {
                final Entry<? extends V> e = (Entry<? extends V>)i.next();
                this.put(e.getCharKey(), (V)e.getValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends Character, ? extends V> e2 = (Map.Entry<? extends Character, ? extends V>)i.next();
                this.put((Character)e2.getKey(), (V)e2.getValue());
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
                return AbstractChar2ObjectMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractChar2ObjectMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractChar2ObjectMap.this.clear();
            }
            
            @Override
            public CharIterator iterator() {
                return new AbstractCharIterator() {
                    final ObjectIterator<Map.Entry<Character, V>> i = AbstractChar2ObjectMap.this.entrySet().iterator();
                    
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
    public ObjectCollection<V> values() {
        return new AbstractObjectCollection<V>() {
            @Override
            public boolean contains(final Object k) {
                return AbstractChar2ObjectMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractChar2ObjectMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractChar2ObjectMap.this.clear();
            }
            
            @Override
            public ObjectIterator<V> iterator() {
                return new AbstractObjectIterator<V>() {
                    final ObjectIterator<Map.Entry<Character, V>> i = AbstractChar2ObjectMap.this.entrySet().iterator();
                    
                    @Deprecated
                    @Override
                    public V next() {
                        return (V)this.i.next().getValue();
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
    public ObjectSet<Map.Entry<Character, V>> entrySet() {
        return (ObjectSet<Map.Entry<Character, V>>)this.char2ObjectEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<Character, V>> i = this.entrySet().iterator();
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
        final ObjectIterator<? extends Map.Entry<Character, V>> i = this.entrySet().iterator();
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
            final Entry<V> e = i.next();
            s.append(String.valueOf(e.getCharKey()));
            s.append("=>");
            if (this == e.getValue()) {
                s.append("(this map)");
            }
            else {
                s.append(String.valueOf(e.getValue()));
            }
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry<V> implements Entry<V>
    {
        protected char key;
        protected V value;
        
        public BasicEntry(final Character key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final char key, final V value) {
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
        
        @Override
        public V getValue() {
            return this.value;
        }
        
        @Override
        public V setValue(final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            return e.getKey() != null && e.getKey() instanceof Character && this.key == (char)e.getKey() && ((this.value != null) ? this.value.equals(e.getValue()) : (e.getValue() == null));
        }
        
        @Override
        public int hashCode() {
            return this.key ^ ((this.value == null) ? 0 : this.value.hashCode());
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}
