// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public abstract class AbstractReference2FloatFunction<K> implements Reference2FloatFunction<K>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected float defRetValue;
    
    protected AbstractReference2FloatFunction() {
    }
    
    @Override
    public void defaultReturnValue(final float rv) {
        this.defRetValue = rv;
    }
    
    @Override
    public float defaultReturnValue() {
        return this.defRetValue;
    }
    
    @Override
    public float put(final K key, final float value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public float removeFloat(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public Float get(final Object ok) {
        final Object k = ok;
        return this.containsKey(k) ? Float.valueOf(this.getFloat(k)) : null;
    }
    
    @Deprecated
    @Override
    public Float put(final K ok, final Float ov) {
        final K k = ok;
        final boolean containsKey = this.containsKey(k);
        final float v = this.put(k, (float)ov);
        return containsKey ? Float.valueOf(v) : null;
    }
    
    @Deprecated
    @Override
    public Float remove(final Object ok) {
        final Object k = ok;
        final boolean containsKey = this.containsKey(k);
        final float v = this.removeFloat(k);
        return containsKey ? Float.valueOf(v) : null;
    }
}
