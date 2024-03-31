// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.bytes.AbstractByteIterator;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Iterator;
import java.util.Map;
import java.io.Serializable;

public abstract class AbstractLong2ByteMap extends AbstractLong2ByteFunction implements Long2ByteMap, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractLong2ByteMap() {
    }
    
    @Override
    public boolean containsValue(final Object ov) {
        return ov != null && this.containsValue((byte)ov);
    }
    
    @Override
    public boolean containsValue(final byte v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final long k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends Long, ? extends Byte> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends Long, ? extends Byte>> i = m.entrySet().iterator();
        if (m instanceof Long2ByteMap) {
            while (n-- != 0) {
                final Entry e = (Entry)i.next();
                this.put(e.getLongKey(), e.getByteValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends Long, ? extends Byte> e2 = (Map.Entry<? extends Long, ? extends Byte>)i.next();
                this.put((Long)e2.getKey(), (Byte)e2.getValue());
            }
        }
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public LongSet keySet() {
        return new AbstractLongSet() {
            @Override
            public boolean contains(final long k) {
                return AbstractLong2ByteMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractLong2ByteMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractLong2ByteMap.this.clear();
            }
            
            @Override
            public LongIterator iterator() {
                return new AbstractLongIterator() {
                    final ObjectIterator<Map.Entry<Long, Byte>> i = AbstractLong2ByteMap.this.entrySet().iterator();
                    
                    @Override
                    public long nextLong() {
                        return this.i.next().getLongKey();
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
    public ByteCollection values() {
        return new AbstractByteCollection() {
            @Override
            public boolean contains(final byte k) {
                return AbstractLong2ByteMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractLong2ByteMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractLong2ByteMap.this.clear();
            }
            
            @Override
            public ByteIterator iterator() {
                return new AbstractByteIterator() {
                    final ObjectIterator<Map.Entry<Long, Byte>> i = AbstractLong2ByteMap.this.entrySet().iterator();
                    
                    @Deprecated
                    @Override
                    public byte nextByte() {
                        return this.i.next().getByteValue();
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
    public ObjectSet<Map.Entry<Long, Byte>> entrySet() {
        return (ObjectSet<Map.Entry<Long, Byte>>)this.long2ByteEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<Long, Byte>> i = this.entrySet().iterator();
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
        final ObjectIterator<? extends Map.Entry<Long, Byte>> i = this.entrySet().iterator();
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
            s.append(String.valueOf(e.getLongKey()));
            s.append("=>");
            s.append(String.valueOf(e.getByteValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry
    {
        protected long key;
        protected byte value;
        
        public BasicEntry(final Long key, final Byte value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final long key, final byte value) {
            this.key = key;
            this.value = value;
        }
        
        @Deprecated
        @Override
        public Long getKey() {
            return this.key;
        }
        
        @Override
        public long getLongKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Byte getValue() {
            return this.value;
        }
        
        @Override
        public byte getByteValue() {
            return this.value;
        }
        
        @Override
        public byte setValue(final byte value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Byte setValue(final Byte value) {
            return this.setValue((byte)value);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            return e.getKey() != null && e.getKey() instanceof Long && e.getValue() != null && e.getValue() instanceof Byte && this.key == (long)e.getKey() && this.value == (byte)e.getValue();
        }
        
        @Override
        public int hashCode() {
            return HashCommon.long2int(this.key) ^ this.value;
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}
