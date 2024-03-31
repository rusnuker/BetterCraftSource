// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanIterator;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import java.util.Iterator;
import java.util.Map;
import java.io.Serializable;

public abstract class AbstractObject2BooleanMap<K> extends AbstractObject2BooleanFunction<K> implements Object2BooleanMap<K>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractObject2BooleanMap() {
    }
    
    @Override
    public boolean containsValue(final Object ov) {
        return ov != null && this.containsValue((boolean)ov);
    }
    
    @Override
    public boolean containsValue(final boolean v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final Object k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends Boolean> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends K, ? extends Boolean>> i = m.entrySet().iterator();
        if (m instanceof Object2BooleanMap) {
            while (n-- != 0) {
                final Entry<? extends K> e = (Entry<? extends K>)i.next();
                this.put((K)e.getKey(), e.getBooleanValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends K, ? extends Boolean> e2 = (Map.Entry<? extends K, ? extends Boolean>)i.next();
                this.put((K)e2.getKey(), (Boolean)e2.getValue());
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
                return AbstractObject2BooleanMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractObject2BooleanMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractObject2BooleanMap.this.clear();
            }
            
            @Override
            public ObjectIterator<K> iterator() {
                return new AbstractObjectIterator<K>() {
                    final ObjectIterator<Map.Entry<K, Boolean>> i = AbstractObject2BooleanMap.this.entrySet().iterator();
                    
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
    public BooleanCollection values() {
        return new AbstractBooleanCollection() {
            @Override
            public boolean contains(final boolean k) {
                return AbstractObject2BooleanMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractObject2BooleanMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractObject2BooleanMap.this.clear();
            }
            
            @Override
            public BooleanIterator iterator() {
                return new AbstractBooleanIterator() {
                    final ObjectIterator<Map.Entry<K, Boolean>> i = AbstractObject2BooleanMap.this.entrySet().iterator();
                    
                    @Deprecated
                    @Override
                    public boolean nextBoolean() {
                        return this.i.next().getBooleanValue();
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
    public ObjectSet<Map.Entry<K, Boolean>> entrySet() {
        return (ObjectSet<Map.Entry<K, Boolean>>)this.object2BooleanEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<K, Boolean>> i = this.entrySet().iterator();
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
        final ObjectIterator<? extends Map.Entry<K, Boolean>> i = this.entrySet().iterator();
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
            s.append(String.valueOf(e.getBooleanValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry<K> implements Entry<K>
    {
        protected K key;
        protected boolean value;
        
        public BasicEntry(final K key, final Boolean value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final K key, final boolean value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public K getKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Boolean getValue() {
            return this.value;
        }
        
        @Override
        public boolean getBooleanValue() {
            return this.value;
        }
        
        @Override
        public boolean setValue(final boolean value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean setValue(final Boolean value) {
            return this.setValue((boolean)value);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            if (e.getValue() == null || !(e.getValue() instanceof Boolean)) {
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
            if (this.value == (boolean)e.getValue()) {
                return true;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return ((this.key == null) ? 0 : this.key.hashCode()) ^ (this.value ? 1231 : 1237);
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}
