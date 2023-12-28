// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.io.Serializable;

public abstract class AbstractFloat2LongFunction implements Float2LongFunction, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected long defRetValue;
    
    protected AbstractFloat2LongFunction() {
    }
    
    @Override
    public void defaultReturnValue(final long rv) {
        this.defRetValue = rv;
    }
    
    @Override
    public long defaultReturnValue() {
        return this.defRetValue;
    }
    
    @Override
    public long put(final float key, final long value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public long remove(final float key) {
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
    
    @Deprecated
    @Override
    public Long get(final Object ok) {
        if (ok == null) {
            return null;
        }
        final float k = (float)ok;
        return this.containsKey(k) ? Long.valueOf(this.get(k)) : null;
    }
    
    @Deprecated
    @Override
    public Long put(final Float ok, final Long ov) {
        final float k = ok;
        final boolean containsKey = this.containsKey(k);
        final long v = this.put(k, (long)ov);
        return containsKey ? Long.valueOf(v) : null;
    }
    
    @Deprecated
    @Override
    public Long remove(final Object ok) {
        if (ok == null) {
            return null;
        }
        final float k = (float)ok;
        final boolean containsKey = this.containsKey(k);
        final long v = this.remove(k);
        return containsKey ? Long.valueOf(v) : null;
    }
}
