// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.io.Serializable;

public abstract class AbstractInt2ReferenceFunction<V> implements Int2ReferenceFunction<V>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected V defRetValue;
    
    protected AbstractInt2ReferenceFunction() {
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
    public V put(final int key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public V remove(final int key) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean containsKey(final Object ok) {
        return ok != null && this.containsKey((int)ok);
    }
    
    @Override
    public V get(final Object ok) {
        if (ok == null) {
            return null;
        }
        final int k = (int)ok;
        return this.containsKey(k) ? this.get(k) : null;
    }
    
    @Deprecated
    @Override
    public V put(final Integer ok, final V ov) {
        final int k = ok;
        final boolean containsKey = this.containsKey(k);
        final V v = this.put(k, ov);
        return containsKey ? v : null;
    }
    
    @Override
    public V remove(final Object ok) {
        if (ok == null) {
            return null;
        }
        final int k = (int)ok;
        final boolean containsKey = this.containsKey(k);
        final V v = this.remove(k);
        return containsKey ? v : null;
    }
}
