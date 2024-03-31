// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleIterator;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import java.util.Iterator;
import java.util.Map;
import java.io.Serializable;

public abstract class AbstractObject2DoubleMap<K> extends AbstractObject2DoubleFunction<K> implements Object2DoubleMap<K>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractObject2DoubleMap() {
    }
    
    @Override
    public boolean containsValue(final Object ov) {
        return ov != null && this.containsValue((double)ov);
    }
    
    @Override
    public boolean containsValue(final double v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final Object k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends Double> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends K, ? extends Double>> i = m.entrySet().iterator();
        if (m instanceof Object2DoubleMap) {
            while (n-- != 0) {
                final Entry<? extends K> e = (Entry<? extends K>)i.next();
                this.put((K)e.getKey(), e.getDoubleValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends K, ? extends Double> e2 = (Map.Entry<? extends K, ? extends Double>)i.next();
                this.put((K)e2.getKey(), (Double)e2.getValue());
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
                return AbstractObject2DoubleMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractObject2DoubleMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractObject2DoubleMap.this.clear();
            }
            
            @Override
            public ObjectIterator<K> iterator() {
                return new AbstractObjectIterator<K>() {
                    final ObjectIterator<Map.Entry<K, Double>> i = AbstractObject2DoubleMap.this.entrySet().iterator();
                    
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
    public DoubleCollection values() {
        return new AbstractDoubleCollection() {
            @Override
            public boolean contains(final double k) {
                return AbstractObject2DoubleMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractObject2DoubleMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractObject2DoubleMap.this.clear();
            }
            
            @Override
            public DoubleIterator iterator() {
                return new AbstractDoubleIterator() {
                    final ObjectIterator<Map.Entry<K, Double>> i = AbstractObject2DoubleMap.this.entrySet().iterator();
                    
                    @Deprecated
                    @Override
                    public double nextDouble() {
                        return this.i.next().getDoubleValue();
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
    public ObjectSet<Map.Entry<K, Double>> entrySet() {
        return (ObjectSet<Map.Entry<K, Double>>)this.object2DoubleEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<K, Double>> i = this.entrySet().iterator();
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
        final ObjectIterator<? extends Map.Entry<K, Double>> i = this.entrySet().iterator();
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
            s.append(String.valueOf(e.getDoubleValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry<K> implements Entry<K>
    {
        protected K key;
        protected double value;
        
        public BasicEntry(final K key, final Double value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final K key, final double value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public K getKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Double getValue() {
            return this.value;
        }
        
        @Override
        public double getDoubleValue() {
            return this.value;
        }
        
        @Override
        public double setValue(final double value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Double setValue(final Double value) {
            return this.setValue((double)value);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            if (e.getValue() == null || !(e.getValue() instanceof Double)) {
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
            if (this.value == (double)e.getValue()) {
                return true;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return ((this.key == null) ? 0 : this.key.hashCode()) ^ HashCommon.double2int(this.value);
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}
