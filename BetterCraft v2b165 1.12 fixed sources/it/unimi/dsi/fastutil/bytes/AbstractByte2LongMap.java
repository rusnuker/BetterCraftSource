// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.longs.AbstractLongIterator;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Iterator;
import java.util.Map;
import java.io.Serializable;

public abstract class AbstractByte2LongMap extends AbstractByte2LongFunction implements Byte2LongMap, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractByte2LongMap() {
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
    public boolean containsKey(final byte k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends Byte, ? extends Long> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends Byte, ? extends Long>> i = m.entrySet().iterator();
        if (m instanceof Byte2LongMap) {
            while (n-- != 0) {
                final Entry e = (Entry)i.next();
                this.put(e.getByteKey(), e.getLongValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends Byte, ? extends Long> e2 = (Map.Entry<? extends Byte, ? extends Long>)i.next();
                this.put((Byte)e2.getKey(), (Long)e2.getValue());
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
                return AbstractByte2LongMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractByte2LongMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractByte2LongMap.this.clear();
            }
            
            @Override
            public ByteIterator iterator() {
                return new AbstractByteIterator() {
                    final ObjectIterator<Map.Entry<Byte, Long>> i = AbstractByte2LongMap.this.entrySet().iterator();
                    
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
    public LongCollection values() {
        return new AbstractLongCollection() {
            @Override
            public boolean contains(final long k) {
                return AbstractByte2LongMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractByte2LongMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractByte2LongMap.this.clear();
            }
            
            @Override
            public LongIterator iterator() {
                return new AbstractLongIterator() {
                    final ObjectIterator<Map.Entry<Byte, Long>> i = AbstractByte2LongMap.this.entrySet().iterator();
                    
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
    public ObjectSet<Map.Entry<Byte, Long>> entrySet() {
        return (ObjectSet<Map.Entry<Byte, Long>>)this.byte2LongEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<Byte, Long>> i = this.entrySet().iterator();
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
        final ObjectIterator<? extends Map.Entry<Byte, Long>> i = this.entrySet().iterator();
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
            s.append(String.valueOf(e.getByteKey()));
            s.append("=>");
            s.append(String.valueOf(e.getLongValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry
    {
        protected byte key;
        protected long value;
        
        public BasicEntry(final Byte key, final Long value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final byte key, final long value) {
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
            return e.getKey() != null && e.getKey() instanceof Byte && e.getValue() != null && e.getValue() instanceof Long && this.key == (byte)e.getKey() && this.value == (long)e.getValue();
        }
        
        @Override
        public int hashCode() {
            return this.key ^ HashCommon.long2int(this.value);
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}
