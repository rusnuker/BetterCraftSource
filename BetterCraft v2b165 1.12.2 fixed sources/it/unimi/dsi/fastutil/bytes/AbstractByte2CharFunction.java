// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.io.Serializable;

public abstract class AbstractByte2CharFunction implements Byte2CharFunction, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected char defRetValue;
    
    protected AbstractByte2CharFunction() {
    }
    
    @Override
    public void defaultReturnValue(final char rv) {
        this.defRetValue = rv;
    }
    
    @Override
    public char defaultReturnValue() {
        return this.defRetValue;
    }
    
    @Override
    public char put(final byte key, final char value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public char remove(final byte key) {
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
    public Character get(final Object ok) {
        if (ok == null) {
            return null;
        }
        final byte k = (byte)ok;
        return this.containsKey(k) ? Character.valueOf(this.get(k)) : null;
    }
    
    @Deprecated
    @Override
    public Character put(final Byte ok, final Character ov) {
        final byte k = ok;
        final boolean containsKey = this.containsKey(k);
        final char v = this.put(k, (char)ov);
        return containsKey ? Character.valueOf(v) : null;
    }
    
    @Deprecated
    @Override
    public Character remove(final Object ok) {
        if (ok == null) {
            return null;
        }
        final byte k = (byte)ok;
        final boolean containsKey = this.containsKey(k);
        final char v = this.remove(k);
        return containsKey ? Character.valueOf(v) : null;
    }
}
