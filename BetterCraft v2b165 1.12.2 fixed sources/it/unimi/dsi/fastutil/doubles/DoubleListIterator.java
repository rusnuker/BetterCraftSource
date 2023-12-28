// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import java.util.ListIterator;

public interface DoubleListIterator extends ListIterator<Double>, DoubleBidirectionalIterator
{
    void set(final double p0);
    
    void add(final double p0);
}
