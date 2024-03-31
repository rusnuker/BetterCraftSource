// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.io.Serializable;

public abstract class AbstractInt2FloatFunction implements Int2FloatFunction, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected float defRetValue;
    
    protected AbstractInt2FloatFunction() {
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
    public float put(final int key, final float value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public float remove(final int key) {
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
    
    @Deprecated
    @Override
    public Float get(final Object ok) {
        if (ok == null) {
            return null;
        }
        final int k = (int)ok;
        return this.containsKey(k) ? Float.valueOf(this.get(k)) : null;
    }
    
    @Deprecated
    @Override
    public Float put(final Integer ok, final Float ov) {
        final int k = ok;
        final boolean containsKey = this.containsKey(k);
        final float v = this.put(k, (float)ov);
        return containsKey ? Float.valueOf(v) : null;
    }
    
    @Deprecated
    @Override
    public Float remove(final Object ok) {
        if (ok == null) {
            return null;
        }
        final int k = (int)ok;
        final boolean containsKey = this.containsKey(k);
        final float v = this.remove(k);
        return containsKey ? Float.valueOf(v) : null;
    }
}
