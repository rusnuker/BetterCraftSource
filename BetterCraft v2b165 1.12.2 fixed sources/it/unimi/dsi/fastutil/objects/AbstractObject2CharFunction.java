// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public abstract class AbstractObject2CharFunction<K> implements Object2CharFunction<K>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected char defRetValue;
    
    protected AbstractObject2CharFunction() {
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
    public char put(final K key, final char value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public char removeChar(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public Character get(final Object ok) {
        final Object k = ok;
        return this.containsKey(k) ? Character.valueOf(this.getChar(k)) : null;
    }
    
    @Deprecated
    @Override
    public Character put(final K ok, final Character ov) {
        final K k = ok;
        final boolean containsKey = this.containsKey(k);
        final char v = this.put(k, (char)ov);
        return containsKey ? Character.valueOf(v) : null;
    }
    
    @Deprecated
    @Override
    public Character remove(final Object ok) {
        final Object k = ok;
        final boolean containsKey = this.containsKey(k);
        final char v = this.removeChar(k);
        return containsKey ? Character.valueOf(v) : null;
    }
}
