// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.io.Serializable;

public abstract class AbstractShort2DoubleFunction implements Short2DoubleFunction, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected double defRetValue;
    
    protected AbstractShort2DoubleFunction() {
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
    public double put(final short key, final double value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public double remove(final short key) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean containsKey(final Object ok) {
        return ok != null && this.containsKey((short)ok);
    }
    
    @Deprecated
    @Override
    public Double get(final Object ok) {
        if (ok == null) {
            return null;
        }
        final short k = (short)ok;
        return this.containsKey(k) ? Double.valueOf(this.get(k)) : null;
    }
    
    @Deprecated
    @Override
    public Double put(final Short ok, final Double ov) {
        final short k = ok;
        final boolean containsKey = this.containsKey(k);
        final double v = this.put(k, (double)ov);
        return containsKey ? Double.valueOf(v) : null;
    }
    
    @Deprecated
    @Override
    public Double remove(final Object ok) {
        if (ok == null) {
            return null;
        }
        final short k = (short)ok;
        final boolean containsKey = this.containsKey(k);
        final double v = this.remove(k);
        return containsKey ? Double.valueOf(v) : null;
    }
}
