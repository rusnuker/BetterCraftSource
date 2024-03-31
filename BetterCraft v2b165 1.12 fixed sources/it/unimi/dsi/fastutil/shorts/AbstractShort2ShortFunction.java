// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.io.Serializable;

public abstract class AbstractShort2ShortFunction implements Short2ShortFunction, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected short defRetValue;
    
    protected AbstractShort2ShortFunction() {
    }
    
    @Override
    public void defaultReturnValue(final short rv) {
        this.defRetValue = rv;
    }
    
    @Override
    public short defaultReturnValue() {
        return this.defRetValue;
    }
    
    @Override
    public short put(final short key, final short value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public short remove(final short key) {
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
    public Short get(final Object ok) {
        if (ok == null) {
            return null;
        }
        final short k = (short)ok;
        return this.containsKey(k) ? Short.valueOf(this.get(k)) : null;
    }
    
    @Deprecated
    @Override
    public Short put(final Short ok, final Short ov) {
        final short k = ok;
        final boolean containsKey = this.containsKey(k);
        final short v = this.put(k, (short)ov);
        return containsKey ? Short.valueOf(v) : null;
    }
    
    @Deprecated
    @Override
    public Short remove(final Object ok) {
        if (ok == null) {
            return null;
        }
        final short k = (short)ok;
        final boolean containsKey = this.containsKey(k);
        final short v = this.remove(k);
        return containsKey ? Short.valueOf(v) : null;
    }
}
