// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.io.Serializable;

public abstract class AbstractByte2BooleanFunction implements Byte2BooleanFunction, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected boolean defRetValue;
    
    protected AbstractByte2BooleanFunction() {
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
    public boolean put(final byte key, final boolean value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean remove(final byte key) {
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
    public Boolean get(final Object ok) {
        if (ok == null) {
            return null;
        }
        final byte k = (byte)ok;
        return this.containsKey(k) ? Boolean.valueOf(this.get(k)) : null;
    }
    
    @Deprecated
    @Override
    public Boolean put(final Byte ok, final Boolean ov) {
        final byte k = ok;
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
        final byte k = (byte)ok;
        final boolean containsKey = this.containsKey(k);
        final boolean v = this.remove(k);
        return containsKey ? Boolean.valueOf(v) : null;
    }
}
