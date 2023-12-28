// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public abstract class AbstractReference2DoubleFunction<K> implements Reference2DoubleFunction<K>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected double defRetValue;
    
    protected AbstractReference2DoubleFunction() {
    }
    
    @Override
    public void defaultReturnValue(final double rv) {
        this.defRetValue = rv;
    }
    
    @Override
    public double defaultReturnValue() {
        return this.defRetValue;
    }
    
    @Override
    public double put(final K key, final double value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public double removeDouble(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public Double get(final Object ok) {
        final Object k = ok;
        return this.containsKey(k) ? Double.valueOf(this.getDouble(k)) : null;
    }
    
    @Deprecated
    @Override
    public Double put(final K ok, final Double ov) {
        final K k = ok;
        final boolean containsKey = this.containsKey(k);
        final double v = this.put(k, (double)ov);
        return containsKey ? Double.valueOf(v) : null;
    }
    
    @Deprecated
    @Override
    public Double remove(final Object ok) {
        final Object k = ok;
        final boolean containsKey = this.containsKey(k);
        final double v = this.removeDouble(k);
        return containsKey ? Double.valueOf(v) : null;
    }
}
