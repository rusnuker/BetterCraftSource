// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public abstract class AbstractReference2LongFunction<K> implements Reference2LongFunction<K>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected long defRetValue;
    
    protected AbstractReference2LongFunction() {
    }
    
    @Override
    public void defaultReturnValue(final long rv) {
        this.defRetValue = rv;
    }
    
    @Override
    public long defaultReturnValue() {
        return this.defRetValue;
    }
    
    @Override
    public long put(final K key, final long value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public long removeLong(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public Long get(final Object ok) {
        final Object k = ok;
        return this.containsKey(k) ? Long.valueOf(this.getLong(k)) : null;
    }
    
    @Deprecated
    @Override
    public Long put(final K ok, final Long ov) {
        final K k = ok;
        final boolean containsKey = this.containsKey(k);
        final long v = this.put(k, (long)ov);
        return containsKey ? Long.valueOf(v) : null;
    }
    
    @Deprecated
    @Override
    public Long remove(final Object ok) {
        final Object k = ok;
        final boolean containsKey = this.containsKey(k);
        final long v = this.removeLong(k);
        return containsKey ? Long.valueOf(v) : null;
    }
}
