// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.io.Serializable;

public abstract class AbstractByte2ReferenceFunction<V> implements Byte2ReferenceFunction<V>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected V defRetValue;
    
    protected AbstractByte2ReferenceFunction() {
    }
    
    @Override
    public void defaultReturnValue(final V rv) {
        this.defRetValue = rv;
    }
    
    @Override
    public V defaultReturnValue() {
        return this.defRetValue;
    }
    
    @Override
    public V put(final byte key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public V remove(final byte key) {
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
    
    @Override
    public V get(final Object ok) {
        if (ok == null) {
            return null;
        }
        final byte k = (byte)ok;
        return this.containsKey(k) ? this.get(k) : null;
    }
    
    @Deprecated
    @Override
    public V put(final Byte ok, final V ov) {
        final byte k = ok;
        final boolean containsKey = this.containsKey(k);
        final V v = this.put(k, ov);
        return containsKey ? v : null;
    }
    
    @Override
    public V remove(final Object ok) {
        if (ok == null) {
            return null;
        }
        final byte k = (byte)ok;
        final boolean containsKey = this.containsKey(k);
        final V v = this.remove(k);
        return containsKey ? v : null;
    }
}
