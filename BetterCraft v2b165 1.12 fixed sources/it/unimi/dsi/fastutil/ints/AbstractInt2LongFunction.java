// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.io.Serializable;

public abstract class AbstractInt2LongFunction implements Int2LongFunction, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected long defRetValue;
    
    protected AbstractInt2LongFunction() {
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
    public long put(final int key, final long value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public long remove(final int key) {
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
    public Long get(final Object ok) {
        if (ok == null) {
            return null;
        }
        final int k = (int)ok;
        return this.containsKey(k) ? Long.valueOf(this.get(k)) : null;
    }
    
    @Deprecated
    @Override
    public Long put(final Integer ok, final Long ov) {
        final int k = ok;
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
        final int k = (int)ok;
        final boolean containsKey = this.containsKey(k);
        final long v = this.remove(k);
        return containsKey ? Long.valueOf(v) : null;
    }
}
