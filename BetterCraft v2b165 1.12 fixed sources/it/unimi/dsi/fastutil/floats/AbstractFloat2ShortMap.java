// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.HashCommon;
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

public abstract class AbstractFloat2ShortMap extends AbstractFloat2ShortFunction implements Float2ShortMap, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractFloat2ShortMap() {
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
    public boolean containsKey(final float k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends Float, ? extends Short> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends Float, ? extends Short>> i = m.entrySet().iterator();
        if (m instanceof Float2ShortMap) {
            while (n-- != 0) {
                final Entry e = (Entry)i.next();
                this.put(e.getFloatKey(), e.getShortValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends Float, ? extends Short> e2 = (Map.Entry<? extends Float, ? extends Short>)i.next();
                this.put((Float)e2.getKey(), (Short)e2.getValue());
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
                return AbstractFloat2ShortMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractFloat2ShortMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractFloat2ShortMap.this.clear();
            }
            
            @Override
            public FloatIterator iterator() {
                return new AbstractFloatIterator() {
                    final ObjectIterator<Map.Entry<Float, Short>> i = AbstractFloat2ShortMap.this.entrySet().iterator();
                    
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
    public ShortCollection values() {
        return new AbstractShortCollection() {
            @Override
            public boolean contains(final short k) {
                return AbstractFloat2ShortMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractFloat2ShortMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractFloat2ShortMap.this.clear();
            }
            
            @Override
            public ShortIterator iterator() {
                return new AbstractShortIterator() {
                    final ObjectIterator<Map.Entry<Float, Short>> i = AbstractFloat2ShortMap.this.entrySet().iterator();
                    
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
    public ObjectSet<Map.Entry<Float, Short>> entrySet() {
        return (ObjectSet<Map.Entry<Float, Short>>)this.float2ShortEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<Float, Short>> i = this.entrySet().iterator();
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
        final ObjectIterator<? extends Map.Entry<Float, Short>> i = this.entrySet().iterator();
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
            s.append(String.valueOf(e.getFloatKey()));
            s.append("=>");
            s.append(String.valueOf(e.getShortValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry
    {
        protected float key;
        protected short value;
        
        public BasicEntry(final Float key, final Short value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final float key, final short value) {
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
            return e.getKey() != null && e.getKey() instanceof Float && e.getValue() != null && e.getValue() instanceof Short && Float.floatToIntBits(this.key) == Float.floatToIntBits((float)e.getKey()) && this.value == (short)e.getValue();
        }
        
        @Override
        public int hashCode() {
            return HashCommon.float2int(this.key) ^ this.value;
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}
