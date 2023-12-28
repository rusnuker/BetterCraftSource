// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

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

public abstract class AbstractDouble2ByteMap extends AbstractDouble2ByteFunction implements Double2ByteMap, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractDouble2ByteMap() {
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
    public boolean containsKey(final double k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends Double, ? extends Byte> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends Double, ? extends Byte>> i = m.entrySet().iterator();
        if (m instanceof Double2ByteMap) {
            while (n-- != 0) {
                final Entry e = (Entry)i.next();
                this.put(e.getDoubleKey(), e.getByteValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends Double, ? extends Byte> e2 = (Map.Entry<? extends Double, ? extends Byte>)i.next();
                this.put((Double)e2.getKey(), (Byte)e2.getValue());
            }
        }
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public DoubleSet keySet() {
        return new AbstractDoubleSet() {
            @Override
            public boolean contains(final double k) {
                return AbstractDouble2ByteMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractDouble2ByteMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractDouble2ByteMap.this.clear();
            }
            
            @Override
            public DoubleIterator iterator() {
                return new AbstractDoubleIterator() {
                    final ObjectIterator<Map.Entry<Double, Byte>> i = AbstractDouble2ByteMap.this.entrySet().iterator();
                    
                    @Override
                    public double nextDouble() {
                        return this.i.next().getDoubleKey();
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
                return AbstractDouble2ByteMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractDouble2ByteMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractDouble2ByteMap.this.clear();
            }
            
            @Override
            public ByteIterator iterator() {
                return new AbstractByteIterator() {
                    final ObjectIterator<Map.Entry<Double, Byte>> i = AbstractDouble2ByteMap.this.entrySet().iterator();
                    
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
    public ObjectSet<Map.Entry<Double, Byte>> entrySet() {
        return (ObjectSet<Map.Entry<Double, Byte>>)this.double2ByteEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<Double, Byte>> i = this.entrySet().iterator();
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
        final ObjectIterator<? extends Map.Entry<Double, Byte>> i = this.entrySet().iterator();
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
            s.append(String.valueOf(e.getDoubleKey()));
            s.append("=>");
            s.append(String.valueOf(e.getByteValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry
    {
        protected double key;
        protected byte value;
        
        public BasicEntry(final Double key, final Byte value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final double key, final byte value) {
            this.key = key;
            this.value = value;
        }
        
        @Deprecated
        @Override
        public Double getKey() {
            return this.key;
        }
        
        @Override
        public double getDoubleKey() {
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
            return e.getKey() != null && e.getKey() instanceof Double && e.getValue() != null && e.getValue() instanceof Byte && Double.doubleToLongBits(this.key) == Double.doubleToLongBits((double)e.getKey()) && this.value == (byte)e.getValue();
        }
        
        @Override
        public int hashCode() {
            return HashCommon.double2int(this.key) ^ this.value;
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}
