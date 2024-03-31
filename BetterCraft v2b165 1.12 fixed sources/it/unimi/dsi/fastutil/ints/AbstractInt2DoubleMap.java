// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

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

public abstract class AbstractInt2DoubleMap extends AbstractInt2DoubleFunction implements Int2DoubleMap, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractInt2DoubleMap() {
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
    public boolean containsKey(final int k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends Integer, ? extends Double> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends Integer, ? extends Double>> i = m.entrySet().iterator();
        if (m instanceof Int2DoubleMap) {
            while (n-- != 0) {
                final Entry e = (Entry)i.next();
                this.put(e.getIntKey(), e.getDoubleValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends Integer, ? extends Double> e2 = (Map.Entry<? extends Integer, ? extends Double>)i.next();
                this.put((Integer)e2.getKey(), (Double)e2.getValue());
            }
        }
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public IntSet keySet() {
        return new AbstractIntSet() {
            @Override
            public boolean contains(final int k) {
                return AbstractInt2DoubleMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractInt2DoubleMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractInt2DoubleMap.this.clear();
            }
            
            @Override
            public IntIterator iterator() {
                return new AbstractIntIterator() {
                    final ObjectIterator<Map.Entry<Integer, Double>> i = AbstractInt2DoubleMap.this.entrySet().iterator();
                    
                    @Override
                    public int nextInt() {
                        return this.i.next().getIntKey();
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
                return AbstractInt2DoubleMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractInt2DoubleMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractInt2DoubleMap.this.clear();
            }
            
            @Override
            public DoubleIterator iterator() {
                return new AbstractDoubleIterator() {
                    final ObjectIterator<Map.Entry<Integer, Double>> i = AbstractInt2DoubleMap.this.entrySet().iterator();
                    
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
    public ObjectSet<Map.Entry<Integer, Double>> entrySet() {
        return (ObjectSet<Map.Entry<Integer, Double>>)this.int2DoubleEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<Integer, Double>> i = this.entrySet().iterator();
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
        final ObjectIterator<? extends Map.Entry<Integer, Double>> i = this.entrySet().iterator();
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
            s.append(String.valueOf(e.getIntKey()));
            s.append("=>");
            s.append(String.valueOf(e.getDoubleValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry
    {
        protected int key;
        protected double value;
        
        public BasicEntry(final Integer key, final Double value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final int key, final double value) {
            this.key = key;
            this.value = value;
        }
        
        @Deprecated
        @Override
        public Integer getKey() {
            return this.key;
        }
        
        @Override
        public int getIntKey() {
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
            return e.getKey() != null && e.getKey() instanceof Integer && e.getValue() != null && e.getValue() instanceof Double && this.key == (int)e.getKey() && this.value == (double)e.getValue();
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
