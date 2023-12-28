// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.PriorityQueue;

public interface DoublePriorityQueue extends PriorityQueue<Double>
{
    void enqueue(final double p0);
    
    double dequeueDouble();
    
    double firstDouble();
    
    double lastDouble();
    
    DoubleComparator comparator();
}
