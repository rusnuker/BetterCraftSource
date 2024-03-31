// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.BigListIterator;

public interface ObjectBigListIterator<K> extends ObjectBidirectionalIterator<K>, BigListIterator<K>
{
    void set(final K p0);
    
    void add(final K p0);
}
