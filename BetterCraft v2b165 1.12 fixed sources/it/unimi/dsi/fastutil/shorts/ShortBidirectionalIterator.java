// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public interface ShortBidirectionalIterator extends ShortIterator, ObjectBidirectionalIterator<Short>
{
    short previousShort();
    
    int back(final int p0);
}
