// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public abstract class AbstractObject2BooleanFunction<K> implements Object2BooleanFunction<K>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected boolean defRetValue;
    
    protected AbstractObject2BooleanFunction() {
    }
    
    @Override
    public void defaultReturnValue(final boolean rv) {
        this.defRetValue = rv;
    }
    
    @Override
    public boolean defaultReturnValue() {
        return this.defRetValue;
    }
    
    @Override
    public boolean put(final K key, final boolean value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean removeBoolean(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public Boolean get(final Object ok) {
        final Object k = ok;
        return this.containsKey(k) ? Boolean.valueOf(this.getBoolean(k)) : null;
    }
    
    @Deprecated
    @Override
    public Boolean put(final K ok, final Boolean ov) {
        final K k = ok;
        final boolean containsKey = this.containsKey(k);
        final boolean v = this.put(k, (boolean)ov);
        return containsKey ? Boolean.valueOf(v) : null;
    }
    
    @Deprecated
    @Override
    public Boolean remove(final Object ok) {
        final Object k = ok;
        final boolean containsKey = this.containsKey(k);
        final boolean v = this.removeBoolean(k);
        return containsKey ? Boolean.valueOf(v) : null;
    }
}
