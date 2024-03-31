// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public interface BooleanBidirectionalIterator extends BooleanIterator, ObjectBidirectionalIterator<Boolean>
{
    boolean previousBoolean();
    
    int back(final int p0);
}
