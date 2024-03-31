// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public abstract class AbstractObject2ReferenceFunction<K, V> implements Object2ReferenceFunction<K, V>, Serializable
{
    private static final long serialVersionUID = -4940583368468432370L;
    protected V defRetValue;
    
    protected AbstractObject2ReferenceFunction() {
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
    public V put(final K key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public V remove(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
