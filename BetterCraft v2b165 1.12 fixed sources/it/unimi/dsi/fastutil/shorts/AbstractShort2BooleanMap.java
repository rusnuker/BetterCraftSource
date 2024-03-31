// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

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

public abstract class AbstractShort2BooleanMap extends AbstractShort2BooleanFunction implements Short2BooleanMap, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractShort2BooleanMap() {
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
    public boolean containsKey(final short k) {
        return this.keySet().contains(k);
    }
    
    @Override
    public void putAll(final Map<? extends Short, ? extends Boolean> m) {
        int n = m.size();
        final Iterator<? extends Map.Entry<? extends Short, ? extends Boolean>> i = m.entrySet().iterator();
        if (m instanceof Short2BooleanMap) {
            while (n-- != 0) {
                final Entry e = (Entry)i.next();
                this.put(e.getShortKey(), e.getBooleanValue());
            }
        }
        else {
            while (n-- != 0) {
                final Map.Entry<? extends Short, ? extends Boolean> e2 = (Map.Entry<? extends Short, ? extends Boolean>)i.next();
                this.put((Short)e2.getKey(), (Boolean)e2.getValue());
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
                return AbstractShort2BooleanMap.this.containsKey(k);
            }
            
            @Override
            public int size() {
                return AbstractShort2BooleanMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractShort2BooleanMap.this.clear();
            }
            
            @Override
            public ShortIterator iterator() {
                return new AbstractShortIterator() {
                    final ObjectIterator<Map.Entry<Short, Boolean>> i = AbstractShort2BooleanMap.this.entrySet().iterator();
                    
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
    public BooleanCollection values() {
        return new AbstractBooleanCollection() {
            @Override
            public boolean contains(final boolean k) {
                return AbstractShort2BooleanMap.this.containsValue(k);
            }
            
            @Override
            public int size() {
                return AbstractShort2BooleanMap.this.size();
            }
            
            @Override
            public void clear() {
                AbstractShort2BooleanMap.this.clear();
            }
            
            @Override
            public BooleanIterator iterator() {
                return new AbstractBooleanIterator() {
                    final ObjectIterator<Map.Entry<Short, Boolean>> i = AbstractShort2BooleanMap.this.entrySet().iterator();
                    
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
    public ObjectSet<Map.Entry<Short, Boolean>> entrySet() {
        return (ObjectSet<Map.Entry<Short, Boolean>>)this.short2BooleanEntrySet();
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<? extends Map.Entry<Short, Boolean>> i = this.entrySet().iterator();
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
        final ObjectIterator<? extends Map.Entry<Short, Boolean>> i = this.entrySet().iterator();
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
            s.append(String.valueOf(e.getBooleanValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry
    {
        protected short key;
        protected boolean value;
        
        public BasicEntry(final Short key, final Boolean value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final short key, final boolean value) {
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
            return e.getKey() != null && e.getKey() instanceof Short && e.getValue() != null && e.getValue() instanceof Boolean && this.key == (short)e.getKey() && this.value == (boolean)e.getValue();
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
