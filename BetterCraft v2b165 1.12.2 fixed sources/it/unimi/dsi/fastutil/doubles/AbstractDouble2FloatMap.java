// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.floats.AbstractFloatIterator;
import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Iterator;
import java.util.Map;
import java.io.Serializable;

public abstract class AbstractDouble2FloatMap extends AbstractDouble2FloatFunction implements Double2FloatMap, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractDouble2FloatMap() {
    }
    
    @Override
    public boolean containsValue(final Object ov) {
        return ov != null && this.containsValue((float)ov);
    }
    
    @Override
    public boolean containsValue(final float v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final double k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends Double, ? extends Float> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends Double, ? extends Float>> i = m.entrySet().iterator();
        if (m instanceof Double2FloatMap) {
            while (n-- != 0) {
                final Entry e = (Entry)i.next();
                this.put(e.getDoubleKey(), e.getFloatValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends Double, ? extends Float> e2 = (Map.Entry<? extends Double, ? extends Float>)i.next();
                this.put((Double)e2.getKey(), (Float)e2.getValue());
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
                return AbstractDouble2FloatMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractDouble2FloatMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractDouble2FloatMap.this.clear();
            }
            
            @Override
            public DoubleIterator iterator() {
                return new AbstractDoubleIterator() {
                    final ObjectIterator<Map.Entry<Double, Float>> i = AbstractDouble2FloatMap.this.entrySet().iterator();
                    
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
    public FloatCollection values() {
        return new AbstractFloatCollection() {
            @Override
            public boolean contains(final float k) {
                return AbstractDouble2FloatMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractDouble2FloatMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractDouble2FloatMap.this.clear();
            }
            
            @Override
            public FloatIterator iterator() {
                return new AbstractFloatIterator() {
                    final ObjectIterator<Map.Entry<Double, Float>> i = AbstractDouble2FloatMap.this.entrySet().iterator();
                    
                    @Deprecated
                    @Override
                    public float nextFloat() {
                        return this.i.next().getFloatValue();
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
    public ObjectSet<Map.Entry<Double, Float>> entrySet() {
        return (ObjectSet<Map.Entry<Double, Float>>)this.double2FloatEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<Double, Float>> i = this.entrySet().iterator();
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
        final ObjectIterator<? extends Map.Entry<Double, Float>> i = this.entrySet().iterator();
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
            s.append(String.valueOf(e.getFloatValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry
    {
        protected double key;
        protected float value;
        
        public BasicEntry(final Double key, final Float value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final double key, final float value) {
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
        public Float getValue() {
            return this.value;
        }
        
        @Override
        public float getFloatValue() {
            return this.value;
        }
        
        @Override
        public float setValue(final float value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Float setValue(final Float value) {
            return this.setValue((float)value);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            return e.getKey() != null && e.getKey() instanceof Double && e.getValue() != null && e.getValue() instanceof Float && Double.doubleToLongBits(this.key) == Double.doubleToLongBits((double)e.getKey()) && this.value == (float)e.getValue();
        }
        
        @Override
        public int hashCode() {
            return HashCommon.double2int(this.key) ^ HashCommon.float2int(this.value);
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}
