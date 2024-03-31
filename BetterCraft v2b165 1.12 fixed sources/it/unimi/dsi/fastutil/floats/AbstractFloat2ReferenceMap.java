// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.AbstractObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Iterator;
import java.util.Map;
import java.io.Serializable;

public abstract class AbstractFloat2ReferenceMap<V> extends AbstractFloat2ReferenceFunction<V> implements Float2ReferenceMap<V>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractFloat2ReferenceMap() {
    }
    
    @Override
    public boolean containsValue(final Object v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final float k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends Float, ? extends V> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends Float, ? extends V>> i = m.entrySet().iterator();
        if (m instanceof Float2ReferenceMap) {
            while (n-- != 0) {
                final Entry<? extends V> e = (Entry<? extends V>)i.next();
                this.put(e.getFloatKey(), (V)e.getValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends Float, ? extends V> e2 = (Map.Entry<? extends Float, ? extends V>)i.next();
                this.put((Float)e2.getKey(), (V)e2.getValue());
            }
        }
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public FloatSet keySet() {
        return new AbstractFloatSet() {
            @Override
            public boolean contains(final float k) {
                return AbstractFloat2ReferenceMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractFloat2ReferenceMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractFloat2ReferenceMap.this.clear();
            }
            
            @Override
            public FloatIterator iterator() {
                return new AbstractFloatIterator() {
                    final ObjectIterator<Map.Entry<Float, V>> i = AbstractFloat2ReferenceMap.this.entrySet().iterator();
                    
                    @Override
                    public float nextFloat() {
                        return this.i.next().getFloatKey();
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
    public ReferenceCollection<V> values() {
        return new AbstractReferenceCollection<V>() {
            @Override
            public boolean contains(final Object k) {
                return AbstractFloat2ReferenceMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractFloat2ReferenceMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractFloat2ReferenceMap.this.clear();
            }
            
            @Override
            public ObjectIterator<V> iterator() {
                return new AbstractObjectIterator<V>() {
                    final ObjectIterator<Map.Entry<Float, V>> i = AbstractFloat2ReferenceMap.this.entrySet().iterator();
                    
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
    public ObjectSet<Map.Entry<Float, V>> entrySet() {
        return (ObjectSet<Map.Entry<Float, V>>)this.float2ReferenceEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<Float, V>> i = this.entrySet().iterator();
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
        final ObjectIterator<? extends Map.Entry<Float, V>> i = this.entrySet().iterator();
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
            s.append(String.valueOf(e.getFloatKey()));
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
        protected float key;
        protected V value;
        
        public BasicEntry(final Float key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final float key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        @Deprecated
        @Override
        public Float getKey() {
            return this.key;
        }
        
        @Override
        public float getFloatKey() {
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
            return e.getKey() != null && e.getKey() instanceof Float && Float.floatToIntBits(this.key) == Float.floatToIntBits((float)e.getKey()) && this.value == e.getValue();
        }
        
        @Override
        public int hashCode() {
            return HashCommon.float2int(this.key) ^ ((this.value == null) ? 0 : System.identityHashCode(this.value));
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}
