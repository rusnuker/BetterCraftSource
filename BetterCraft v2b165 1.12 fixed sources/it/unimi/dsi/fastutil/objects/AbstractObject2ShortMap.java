// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.shorts.AbstractShortIterator;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import java.util.Iterator;
import java.util.Map;
import java.io.Serializable;

public abstract class AbstractObject2ShortMap<K> extends AbstractObject2ShortFunction<K> implements Object2ShortMap<K>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractObject2ShortMap() {
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
    public boolean containsKey(final Object k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends Short> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends K, ? extends Short>> i = m.entrySet().iterator();
        if (m instanceof Object2ShortMap) {
            while (n-- != 0) {
                final Entry<? extends K> e = (Entry<? extends K>)i.next();
                this.put((K)e.getKey(), e.getShortValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends K, ? extends Short> e2 = (Map.Entry<? extends K, ? extends Short>)i.next();
                this.put((K)e2.getKey(), (Short)e2.getValue());
            }
        }
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public ObjectSet<K> keySet() {
        return new AbstractObjectSet<K>() {
            @Override
            public boolean contains(final Object k) {
                return AbstractObject2ShortMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractObject2ShortMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractObject2ShortMap.this.clear();
            }
            
            @Override
            public ObjectIterator<K> iterator() {
                return new AbstractObjectIterator<K>() {
                    final ObjectIterator<Map.Entry<K, Short>> i = AbstractObject2ShortMap.this.entrySet().iterator();
                    
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
    public ShortCollection values() {
        return new AbstractShortCollection() {
            @Override
            public boolean contains(final short k) {
                return AbstractObject2ShortMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractObject2ShortMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractObject2ShortMap.this.clear();
            }
            
            @Override
            public ShortIterator iterator() {
                return new AbstractShortIterator() {
                    final ObjectIterator<Map.Entry<K, Short>> i = AbstractObject2ShortMap.this.entrySet().iterator();
                    
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
    public ObjectSet<Map.Entry<K, Short>> entrySet() {
        return (ObjectSet<Map.Entry<K, Short>>)this.object2ShortEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<K, Short>> i = this.entrySet().iterator();
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
        final ObjectIterator<? extends Map.Entry<K, Short>> i = this.entrySet().iterator();
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
            s.append(String.valueOf(e.getShortValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry<K> implements Entry<K>
    {
        protected K key;
        protected short value;
        
        public BasicEntry(final K key, final Short value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final K key, final short value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public K getKey() {
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
            if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                return false;
            }
            if (this.key == null) {
                if (e.getKey() != null) {
                    return false;
                }
            }
            else if (!this.key.equals(e.getKey())) {
                return false;
            }
            if (this.value == (short)e.getValue()) {
                return true;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return ((this.key == null) ? 0 : this.key.hashCode()) ^ this.value;
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}
