// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

public abstract class AbstractObjectListIterator<K> extends AbstractObjectBidirectionalIterator<K> implements ObjectListIterator<K>
{
    protected AbstractObjectListIterator() {
    }
    
    @Override
    public void set(final K k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void add(final K k) {
        throw new UnsupportedOperationException();
    }
}
