// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil;

public abstract class AbstractIndirectPriorityQueue<K> implements IndirectPriorityQueue<K>
{
    @Override
    public int last() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void changed() {
        this.changed(this.first());
    }
    
    @Override
    public void changed(final int index) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void allChanged() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean remove(final int index) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean contains(final int index) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
}
