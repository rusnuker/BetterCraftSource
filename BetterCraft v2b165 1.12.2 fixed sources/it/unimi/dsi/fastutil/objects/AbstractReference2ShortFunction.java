// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public abstract class AbstractReference2ShortFunction<K> implements Reference2ShortFunction<K>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected short defRetValue;
    
    protected AbstractReference2ShortFunction() {
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
    public short put(final K key, final short value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public short removeShort(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public Short get(final Object ok) {
        final Object k = ok;
        return this.containsKey(k) ? Short.valueOf(this.getShort(k)) : null;
    }
    
    @Deprecated
    @Override
    public Short put(final K ok, final Short ov) {
        final K k = ok;
        final boolean containsKey = this.containsKey(k);
        final short v = this.put(k, (short)ov);
        return containsKey ? Short.valueOf(v) : null;
    }
    
    @Deprecated
    @Override
    public Short remove(final Object ok) {
        final Object k = ok;
        final boolean containsKey = this.containsKey(k);
        final short v = this.removeShort(k);
        return containsKey ? Short.valueOf(v) : null;
    }
}
