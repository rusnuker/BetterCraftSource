// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.io.Serializable;

public abstract class AbstractInt2BooleanFunction implements Int2BooleanFunction, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected boolean defRetValue;
    
    protected AbstractInt2BooleanFunction() {
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
    public boolean put(final int key, final boolean value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean remove(final int key) {
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
    public Boolean get(final Object ok) {
        if (ok == null) {
            return null;
        }
        final int k = (int)ok;
        return this.containsKey(k) ? Boolean.valueOf(this.get(k)) : null;
    }
    
    @Deprecated
    @Override
    public Boolean put(final Integer ok, final Boolean ov) {
        final int k = ok;
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
        final int k = (int)ok;
        final boolean containsKey = this.containsKey(k);
        final boolean v = this.remove(k);
        return containsKey ? Boolean.valueOf(v) : null;
    }
}
