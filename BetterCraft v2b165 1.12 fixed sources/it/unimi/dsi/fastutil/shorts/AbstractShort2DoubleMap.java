// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleIterator;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Iterator;
import java.util.Map;
import java.io.Serializable;

public abstract class AbstractShort2DoubleMap extends AbstractShort2DoubleFunction implements Short2DoubleMap, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractShort2DoubleMap() {
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
    public boolean containsKey(final short k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends Short, ? extends Double> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends Short, ? extends Double>> i = m.entrySet().iterator();
        if (m instanceof Short2DoubleMap) {
            while (n-- != 0) {
                final Entry e = (Entry)i.next();
                this.put(e.getShortKey(), e.getDoubleValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends Short, ? extends Double> e2 = (Map.Entry<? extends Short, ? extends Double>)i.next();
                this.put((Short)e2.getKey(), (Double)e2.getValue());
            }
        }
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public ShortSet keySet() {
        return new AbstractShortSet() {
            @Override
            public boolean contains(final short k) {
                return AbstractShort2DoubleMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractShort2DoubleMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractShort2DoubleMap.this.clear();
            }
            
            @Override
            public ShortIterator iterator() {
                return new AbstractShortIterator() {
                    final ObjectIterator<Map.Entry<Short, Double>> i = AbstractShort2DoubleMap.this.entrySet().iterator();
                    
                    @Override
                    public short nextShort() {
                        return this.i.next().getShortKey();
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
                return AbstractShort2DoubleMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractShort2DoubleMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractShort2DoubleMap.this.clear();
            }
            
            @Override
            public DoubleIterator iterator() {
                return new AbstractDoubleIterator() {
                    final ObjectIterator<Map.Entry<Short, Double>> i = AbstractShort2DoubleMap.this.entrySet().iterator();
                    
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
    public ObjectSet<Map.Entry<Short, Double>> entrySet() {
        return (ObjectSet<Map.Entry<Short, Double>>)this.short2DoubleEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<Short, Double>> i = this.entrySet().iterator();
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
        final ObjectIterator<? extends Map.Entry<Short, Double>> i = this.entrySet().iterator();
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
            s.append(String.valueOf(e.getShortKey()));
            s.append("=>");
            s.append(String.valueOf(e.getDoubleValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry
    {
        protected short key;
        protected double value;
        
        public BasicEntry(final Short key, final Double value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final short key, final double value) {
            this.key = key;
            this.value = value;
        }
        
        @Deprecated
        @Override
        public Short getKey() {
            return this.key;
        }
        
        @Override
        public short getShortKey() {
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
            return e.getKey() != null && e.getKey() instanceof Short && e.getValue() != null && e.getValue() instanceof Double && this.key == (short)e.getKey() && this.value == (double)e.getValue();
        }
        
        @Override
        public int hashCode() {
            return this.key ^ HashCommon.double2int(this.value);
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}
