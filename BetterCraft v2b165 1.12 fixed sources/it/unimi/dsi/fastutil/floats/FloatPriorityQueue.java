// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.PriorityQueue;

public interface FloatPriorityQueue extends PriorityQueue<Float>
{
    void enqueue(final float p0);
    
    float dequeueFloat();
    
    float firstFloat();
    
    float lastFloat();
    
    FloatComparator comparator();
}
