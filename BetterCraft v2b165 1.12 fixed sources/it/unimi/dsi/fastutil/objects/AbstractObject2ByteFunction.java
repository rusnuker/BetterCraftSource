// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public abstract class AbstractObject2ByteFunction<K> implements Object2ByteFunction<K>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected byte defRetValue;
    
    protected AbstractObject2ByteFunction() {
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
    public byte put(final K key, final byte value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public byte removeByte(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public Byte get(final Object ok) {
        final Object k = ok;
        return this.containsKey(k) ? Byte.valueOf(this.getByte(k)) : null;
    }
    
    @Deprecated
    @Override
    public Byte put(final K ok, final Byte ov) {
        final K k = ok;
        final boolean containsKey = this.containsKey(k);
        final byte v = this.put(k, (byte)ov);
        return containsKey ? Byte.valueOf(v) : null;
    }
    
    @Deprecated
    @Override
    public Byte remove(final Object ok) {
        final Object k = ok;
        final boolean containsKey = this.containsKey(k);
        final byte v = this.removeByte(k);
        return containsKey ? Byte.valueOf(v) : null;
    }
}
