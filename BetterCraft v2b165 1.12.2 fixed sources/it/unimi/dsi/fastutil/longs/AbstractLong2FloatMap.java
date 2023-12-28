// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

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

public abstract class AbstractLong2FloatMap extends AbstractLong2FloatFunction implements Long2FloatMap, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractLong2FloatMap() {
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
    public boolean containsKey(final long k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends Long, ? extends Float> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends Long, ? extends Float>> i = m.entrySet().iterator();
        if (m instanceof Long2FloatMap) {
            while (n-- != 0) {
                final Entry e = (Entry)i.next();
                this.put(e.getLongKey(), e.getFloatValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends Long, ? extends Float> e2 = (Map.Entry<? extends Long, ? extends Float>)i.next();
                this.put((Long)e2.getKey(), (Float)e2.getValue());
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
                return AbstractLong2FloatMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractLong2FloatMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractLong2FloatMap.this.clear();
            }
            
            @Override
            public LongIterator iterator() {
                return new AbstractLongIterator() {
                    final ObjectIterator<Map.Entry<Long, Float>> i = AbstractLong2FloatMap.this.entrySet().iterator();
                    
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
    public FloatCollection values() {
        return new AbstractFloatCollection() {
            @Override
            public boolean contains(final float k) {
                return AbstractLong2FloatMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractLong2FloatMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractLong2FloatMap.this.clear();
            }
            
            @Override
            public FloatIterator iterator() {
                return new AbstractFloatIterator() {
                    final ObjectIterator<Map.Entry<Long, Float>> i = AbstractLong2FloatMap.this.entrySet().iterator();
                    
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
    public ObjectSet<Map.Entry<Long, Float>> entrySet() {
        return (ObjectSet<Map.Entry<Long, Float>>)this.long2FloatEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<Long, Float>> i = this.entrySet().iterator();
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
        final ObjectIterator<? extends Map.Entry<Long, Float>> i = this.entrySet().iterator();
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
            s.append(String.valueOf(e.getFloatValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry
    {
        protected long key;
        protected float value;
        
        public BasicEntry(final Long key, final Float value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final long key, final float value) {
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
            return e.getKey() != null && e.getKey() instanceof Long && e.getValue() != null && e.getValue() instanceof Float && this.key == (long)e.getKey() && this.value == (float)e.getValue();
        }
        
        @Override
        public int hashCode() {
            return HashCommon.long2int(this.key) ^ HashCommon.float2int(this.value);
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}
