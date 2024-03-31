// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.io.Serializable;

public abstract class AbstractByte2DoubleFunction implements Byte2DoubleFunction, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected double defRetValue;
    
    protected AbstractByte2DoubleFunction() {
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
    public double put(final byte key, final double value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public double remove(final byte key) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean containsKey(final Object ok) {
        return ok != null && this.containsKey((byte)ok);
    }
    
    @Deprecated
    @Override
    public Double get(final Object ok) {
        if (ok == null) {
            return null;
        }
        final byte k = (byte)ok;
        return this.containsKey(k) ? Double.valueOf(this.get(k)) : null;
    }
    
    @Deprecated
    @Override
    public Double put(final Byte ok, final Double ov) {
        final byte k = ok;
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
        final byte k = (byte)ok;
        final boolean containsKey = this.containsKey(k);
        final double v = this.remove(k);
        return containsKey ? Double.valueOf(v) : null;
    }
}
