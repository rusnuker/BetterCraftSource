// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.io.Serializable;

public abstract class AbstractInt2CharFunction implements Int2CharFunction, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected char defRetValue;
    
    protected AbstractInt2CharFunction() {
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
    public char put(final int key, final char value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public char remove(final int key) {
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
    public Character get(final Object ok) {
        if (ok == null) {
            return null;
        }
        final int k = (int)ok;
        return this.containsKey(k) ? Character.valueOf(this.get(k)) : null;
    }
    
    @Deprecated
    @Override
    public Character put(final Integer ok, final Character ov) {
        final int k = ok;
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
        final int k = (int)ok;
        final boolean containsKey = this.containsKey(k);
        final char v = this.remove(k);
        return containsKey ? Character.valueOf(v) : null;
    }
}
