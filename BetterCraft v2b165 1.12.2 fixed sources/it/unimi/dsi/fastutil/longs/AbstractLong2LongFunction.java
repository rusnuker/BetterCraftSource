// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.io.Serializable;

public abstract class AbstractLong2LongFunction implements Long2LongFunction, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected long defRetValue;
    
    protected AbstractLong2LongFunction() {
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
    public long put(final long key, final long value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public long remove(final long key) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean containsKey(final Object ok) {
        return ok != null && this.containsKey((long)ok);
    }
    
    @Deprecated
    @Override
    public Long get(final Object ok) {
        if (ok == null) {
            return null;
        }
        final long k = (long)ok;
        return this.containsKey(k) ? Long.valueOf(this.get(k)) : null;
    }
    
    @Deprecated
    @Override
    public Long put(final Long ok, final Long ov) {
        final long k = ok;
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
        final long k = (long)ok;
        final boolean containsKey = this.containsKey(k);
        final long v = this.remove(k);
        return containsKey ? Long.valueOf(v) : null;
    }
}
