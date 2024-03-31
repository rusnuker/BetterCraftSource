// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.io.Serializable;

public abstract class AbstractFloat2ObjectFunction<V> implements Float2ObjectFunction<V>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected V defRetValue;
    
    protected AbstractFloat2ObjectFunction() {
    }
    
    @Override
    public void defaultReturnValue(final V rv) {
        this.defRetValue = rv;
    }
    
    @Override
    public V defaultReturnValue() {
        return this.defRetValue;
    }
    
    @Override
    public V put(final float key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public V remove(final float key) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean containsKey(final Object ok) {
        return ok != null && this.containsKey((float)ok);
    }
    
    @Override
    public V get(final Object ok) {
        if (ok == null) {
            return null;
        }
        final float k = (float)ok;
        return this.containsKey(k) ? this.get(k) : null;
    }
    
    @Deprecated
    @Override
    public V put(final Float ok, final V ov) {
        final float k = ok;
        final boolean containsKey = this.containsKey(k);
        final V v = this.put(k, ov);
        return containsKey ? v : null;
    }
    
    @Override
    public V remove(final Object ok) {
        if (ok == null) {
            return null;
        }
        final float k = (float)ok;
        final boolean containsKey = this.containsKey(k);
        final V v = this.remove(k);
        return containsKey ? v : null;
    }
}
