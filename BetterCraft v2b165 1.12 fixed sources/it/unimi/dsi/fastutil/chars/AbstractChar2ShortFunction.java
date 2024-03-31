// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.io.Serializable;

public abstract class AbstractChar2ShortFunction implements Char2ShortFunction, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected short defRetValue;
    
    protected AbstractChar2ShortFunction() {
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
    public short put(final char key, final short value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public short remove(final char key) {
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
    public Short get(final Object ok) {
        if (ok == null) {
            return null;
        }
        final char k = (char)ok;
        return this.containsKey(k) ? Short.valueOf(this.get(k)) : null;
    }
    
    @Deprecated
    @Override
    public Short put(final Character ok, final Short ov) {
        final char k = ok;
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
        final char k = (char)ok;
        final boolean containsKey = this.containsKey(k);
        final short v = this.remove(k);
        return containsKey ? Short.valueOf(v) : null;
    }
}
