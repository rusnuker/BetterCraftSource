// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import java.io.Serializable;

public abstract class AbstractDouble2BooleanFunction implements Double2BooleanFunction, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected boolean defRetValue;
    
    protected AbstractDouble2BooleanFunction() {
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
    public boolean put(final double key, final boolean value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean remove(final double key) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean containsKey(final Object ok) {
        return ok != null && this.containsKey((double)ok);
    }
    
    @Deprecated
    @Override
    public Boolean get(final Object ok) {
        if (ok == null) {
            return null;
        }
        final double k = (double)ok;
        return this.containsKey(k) ? Boolean.valueOf(this.get(k)) : null;
    }
    
    @Deprecated
    @Override
    public Boolean put(final Double ok, final Boolean ov) {
        final double k = ok;
        final boolean containsKey = this.containsKey(k);
        final boolean v = this.put(k, (boolean)ov);
        return containsKey ? Boolean.valueOf(v) : null;
    }
    
    @Deprecated
    @Override
    public Boolean remove(final Object ok) {
        if (ok == null) {
            return null;
        }
        final double k = (double)ok;
        final boolean containsKey = this.containsKey(k);
        final boolean v = this.remove(k);
        return containsKey ? Boolean.valueOf(v) : null;
    }
}
