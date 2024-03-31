// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.io.Serializable;

public abstract class AbstractChar2ByteFunction implements Char2ByteFunction, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected byte defRetValue;
    
    protected AbstractChar2ByteFunction() {
    }
    
    @Override
    public void defaultReturnValue(final byte rv) {
        this.defRetValue = rv;
    }
    
    @Override
    public byte defaultReturnValue() {
        return this.defRetValue;
    }
    
    @Override
    public byte put(final char key, final byte value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public byte remove(final char key) {
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
    public Byte get(final Object ok) {
        if (ok == null) {
            return null;
        }
        final char k = (char)ok;
        return this.containsKey(k) ? Byte.valueOf(this.get(k)) : null;
    }
    
    @Deprecated
    @Override
    public Byte put(final Character ok, final Byte ov) {
        final char k = ok;
        final boolean containsKey = this.containsKey(k);
        final byte v = this.put(k, (byte)ov);
        return containsKey ? Byte.valueOf(v) : null;
    }
    
    @Deprecated
    @Override
    public Byte remove(final Object ok) {
        if (ok == null) {
            return null;
        }
        final char k = (char)ok;
        final boolean containsKey = this.containsKey(k);
        final byte v = this.remove(k);
        return containsKey ? Byte.valueOf(v) : null;
    }
}
