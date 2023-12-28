// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanIterator;
import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Iterator;
import java.util.Map;
import java.io.Serializable;

public abstract class AbstractInt2BooleanMap extends AbstractInt2BooleanFunction implements Int2BooleanMap, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractInt2BooleanMap() {
    }
    
    @Override
    public boolean containsValue(final Object ov) {
        return ov != null && this.containsValue((boolean)ov);
    }
    
    @Override
    public boolean containsValue(final boolean v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final int k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends Integer, ? extends Boolean> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends Integer, ? extends Boolean>> i = m.entrySet().iterator();
        if (m instanceof Int2BooleanMap) {
            while (n-- != 0) {
                final Entry e = (Entry)i.next();
                this.put(e.getIntKey(), e.getBooleanValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends Integer, ? extends Boolean> e2 = (Map.Entry<? extends Integer, ? extends Boolean>)i.next();
                this.put((Integer)e2.getKey(), (Boolean)e2.getValue());
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
                return AbstractInt2BooleanMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractInt2BooleanMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractInt2BooleanMap.this.clear();
            }
            
            @Override
            public IntIterator iterator() {
                return new AbstractIntIterator() {
                    final ObjectIterator<Map.Entry<Integer, Boolean>> i = AbstractInt2BooleanMap.this.entrySet().iterator();
                    
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
    public BooleanCollection values() {
        return new AbstractBooleanCollection() {
            @Override
            public boolean contains(final boolean k) {
                return AbstractInt2BooleanMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractInt2BooleanMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractInt2BooleanMap.this.clear();
            }
            
            @Override
            public BooleanIterator iterator() {
                return new AbstractBooleanIterator() {
                    final ObjectIterator<Map.Entry<Integer, Boolean>> i = AbstractInt2BooleanMap.this.entrySet().iterator();
                    
                    @Deprecated
                    @Override
                    public boolean nextBoolean() {
                        return this.i.next().getBooleanValue();
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
    public ObjectSet<Map.Entry<Integer, Boolean>> entrySet() {
        return (ObjectSet<Map.Entry<Integer, Boolean>>)this.int2BooleanEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<Integer, Boolean>> i = this.entrySet().iterator();
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
        final ObjectIterator<? extends Map.Entry<Integer, Boolean>> i = this.entrySet().iterator();
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
            s.append(String.valueOf(e.getBooleanValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry
    {
        protected int key;
        protected boolean value;
        
        public BasicEntry(final Integer key, final Boolean value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final int key, final boolean value) {
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
        public Boolean getValue() {
            return this.value;
        }
        
        @Override
        public boolean getBooleanValue() {
            return this.value;
        }
        
        @Override
        public boolean setValue(final boolean value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean setValue(final Boolean value) {
            return this.setValue((boolean)value);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            return e.getKey() != null && e.getKey() instanceof Integer && e.getValue() != null && e.getValue() instanceof Boolean && this.key == (int)e.getKey() && this.value == (boolean)e.getValue();
        }
        
        @Override
        public int hashCode() {
            return this.key ^ (this.value ? 1231 : 1237);
        }
        
        @Override
        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}
