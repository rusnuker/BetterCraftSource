// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

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

public abstract class AbstractByte2ReferenceMap<V> extends AbstractByte2ReferenceFunction<V> implements Byte2ReferenceMap<V>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractByte2ReferenceMap() {
    }
    
    @Override
    public boolean containsValue(final Object v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final byte k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends Byte, ? extends V> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends Byte, ? extends V>> i = m.entrySet().iterator();
        if (m instanceof Byte2ReferenceMap) {
            while (n-- != 0) {
                final Entry<? extends V> e = (Entry<? extends V>)i.next();
                this.put(e.getByteKey(), (V)e.getValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends Byte, ? extends V> e2 = (Map.Entry<? extends Byte, ? extends V>)i.next();
                this.put((Byte)e2.getKey(), (V)e2.getValue());
            }
        }
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public ByteSet keySet() {
        return new AbstractByteSet() {
            @Override
            public boolean contains(final byte k) {
                return AbstractByte2ReferenceMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractByte2ReferenceMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractByte2ReferenceMap.this.clear();
            }
            
            @Override
            public ByteIterator iterator() {
                return new AbstractByteIterator() {
                    final ObjectIterator<Map.Entry<Byte, V>> i = AbstractByte2ReferenceMap.this.entrySet().iterator();
                    
                    @Override
                    public byte nextByte() {
                        return this.i.next().getByteKey();
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
                return AbstractByte2ReferenceMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractByte2ReferenceMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractByte2ReferenceMap.this.clear();
            }
            
            @Override
            public ObjectIterator<V> iterator() {
                return new AbstractObjectIterator<V>() {
                    final ObjectIterator<Map.Entry<Byte, V>> i = AbstractByte2ReferenceMap.this.entrySet().iterator();
                    
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
    public ObjectSet<Map.Entry<Byte, V>> entrySet() {
        return (ObjectSet<Map.Entry<Byte, V>>)this.byte2ReferenceEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<Byte, V>> i = this.entrySet().iterator();
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
        final ObjectIterator<? extends Map.Entry<Byte, V>> i = this.entrySet().iterator();
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
            s.append(String.valueOf(e.getByteKey()));
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
        protected byte key;
        protected V value;
        
        public BasicEntry(final Byte key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final byte key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        @Deprecated
        @Override
        public Byte getKey() {
            return this.key;
        }
        
        @Override
        public byte getByteKey() {
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
            return e.getKey() != null && e.getKey() instanceof Byte && this.key == (byte)e.getKey() && this.value == e.getValue();
        }
        
        @Override
        public int hashCode() {
            return this.key ^ ((this.value == null) ? 0 : System.identityHashCode(this.value));
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}
