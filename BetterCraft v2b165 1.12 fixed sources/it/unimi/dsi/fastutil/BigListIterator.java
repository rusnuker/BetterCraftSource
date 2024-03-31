// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil;

public interface BigListIterator<K> extends BidirectionalIterator<K>
{
    long nextIndex();
    
    long previousIndex();
    
    long skip(final long p0);
}
