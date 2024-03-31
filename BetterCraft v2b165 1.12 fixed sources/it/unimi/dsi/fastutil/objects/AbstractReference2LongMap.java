// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.longs.AbstractLongIterator;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import it.unimi.dsi.fastutil.longs.LongCollection;
import java.util.Iterator;
import java.util.Map;
import java.io.Serializable;

public abstract class AbstractReference2LongMap<K> extends AbstractReference2LongFunction<K> implements Reference2LongMap<K>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractReference2LongMap() {
    }
    
    @Override
    public boolean containsValue(final Object ov) {
        return ov != null && this.containsValue((long)ov);
    }
    
    @Override
    public boolean containsValue(final long v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final Object k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends Long> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends K, ? extends Long>> i = m.entrySet().iterator();
        if (m instanceof Reference2LongMap) {
            while (n-- != 0) {
                final Entry<? extends K> e = (Entry<? extends K>)i.next();
                this.put((K)e.getKey(), e.getLongValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends K, ? extends Long> e2 = (Map.Entry<? extends K, ? extends Long>)i.next();
                this.put((K)e2.getKey(), (Long)e2.getValue());
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
                return AbstractReference2LongMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractReference2LongMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractReference2LongMap.this.clear();
            }
            
            @Override
            public ObjectIterator<K> iterator() {
                return new AbstractObjectIterator<K>() {
                    final ObjectIterator<Map.Entry<K, Long>> i = AbstractReference2LongMap.this.entrySet().iterator();
                    
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
    public LongCollection values() {
        return new AbstractLongCollection() {
            @Override
            public boolean contains(final long k) {
                return AbstractReference2LongMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractReference2LongMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractReference2LongMap.this.clear();
            }
            
            @Override
            public LongIterator iterator() {
                return new AbstractLongIterator() {
                    final ObjectIterator<Map.Entry<K, Long>> i = AbstractReference2LongMap.this.entrySet().iterator();
                    
                    @Deprecated
                    @Override
                    public long nextLong() {
                        return this.i.next().getLongValue();
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
    public ObjectSet<Map.Entry<K, Long>> entrySet() {
        return (ObjectSet<Map.Entry<K, Long>>)this.reference2LongEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<K, Long>> i = this.entrySet().iterator();
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
        final ObjectIterator<? extends Map.Entry<K, Long>> i = this.entrySet().iterator();
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
            s.append(String.valueOf(e.getLongValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry<K> implements Entry<K>
    {
        protected K key;
        protected long value;
        
        public BasicEntry(final K key, final Long value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final K key, final long value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public K getKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Long getValue() {
            return this.value;
        }
        
        @Override
        public long getLongValue() {
            return this.value;
        }
        
        @Override
        public long setValue(final long value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Long setValue(final Long value) {
            return this.setValue((long)value);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            return e.getValue() != null && e.getValue() instanceof Long && this.key == e.getKey() && this.value == (long)e.getValue();
        }
        
        @Override
        public int hashCode() {
            return System.identityHashCode(this.key) ^ HashCommon.long2int(this.value);
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}
