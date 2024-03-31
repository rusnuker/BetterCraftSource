// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.shorts.AbstractShortIterator;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Iterator;
import java.util.Map;
import java.io.Serializable;

public abstract class AbstractByte2ShortMap extends AbstractByte2ShortFunction implements Byte2ShortMap, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractByte2ShortMap() {
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
    public boolean containsKey(final byte k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends Byte, ? extends Short> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends Byte, ? extends Short>> i = m.entrySet().iterator();
        if (m instanceof Byte2ShortMap) {
            while (n-- != 0) {
                final Entry e = (Entry)i.next();
                this.put(e.getByteKey(), e.getShortValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends Byte, ? extends Short> e2 = (Map.Entry<? extends Byte, ? extends Short>)i.next();
                this.put((Byte)e2.getKey(), (Short)e2.getValue());
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
                return AbstractByte2ShortMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractByte2ShortMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractByte2ShortMap.this.clear();
            }
            
            @Override
            public ByteIterator iterator() {
                return new AbstractByteIterator() {
                    final ObjectIterator<Map.Entry<Byte, Short>> i = AbstractByte2ShortMap.this.entrySet().iterator();
                    
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
    public ShortCollection values() {
        return new AbstractShortCollection() {
            @Override
            public boolean contains(final short k) {
                return AbstractByte2ShortMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractByte2ShortMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractByte2ShortMap.this.clear();
            }
            
            @Override
            public ShortIterator iterator() {
                return new AbstractShortIterator() {
                    final ObjectIterator<Map.Entry<Byte, Short>> i = AbstractByte2ShortMap.this.entrySet().iterator();
                    
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
    public ObjectSet<Map.Entry<Byte, Short>> entrySet() {
        return (ObjectSet<Map.Entry<Byte, Short>>)this.byte2ShortEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<Byte, Short>> i = this.entrySet().iterator();
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
        final ObjectIterator<? extends Map.Entry<Byte, Short>> i = this.entrySet().iterator();
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
            s.append(String.valueOf(e.getShortValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry
    {
        protected byte key;
        protected short value;
        
        public BasicEntry(final Byte key, final Short value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final byte key, final short value) {
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
            return e.getKey() != null && e.getKey() instanceof Byte && e.getValue() != null && e.getValue() instanceof Short && this.key == (byte)e.getKey() && this.value == (short)e.getValue();
        }
        
        @Override
        public int hashCode() {
            return this.key ^ this.value;
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}
