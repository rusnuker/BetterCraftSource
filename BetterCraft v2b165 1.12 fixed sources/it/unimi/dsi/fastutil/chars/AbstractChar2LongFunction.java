// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.io.Serializable;

public abstract class AbstractChar2LongFunction implements Char2LongFunction, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected long defRetValue;
    
    protected AbstractChar2LongFunction() {
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
    public long put(final char key, final long value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public long remove(final char key) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean containsKey(final Object ok) {
        return ok != null && this.containsKey((char)ok);
    }
    
    @Deprecated
    @Override
    public Long get(final Object ok) {
        if (ok == null) {
            return null;
        }
        final char k = (char)ok;
        return this.containsKey(k) ? Long.valueOf(this.get(k)) : null;
    }
    
    @Deprecated
    @Override
    public Long put(final Character ok, final Long ov) {
        final char k = ok;
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
        final char k = (char)ok;
        final boolean containsKey = this.containsKey(k);
        final long v = this.remove(k);
        return containsKey ? Long.valueOf(v) : null;
    }
}
