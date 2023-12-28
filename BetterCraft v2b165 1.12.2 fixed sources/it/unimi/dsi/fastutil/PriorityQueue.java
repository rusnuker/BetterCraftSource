// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil;

import java.util.Comparator;

public interface PriorityQueue<K>
{
    void enqueue(final K p0);
    
    K dequeue();
    
    boolean isEmpty();
    
    int size();
    
    void clear();
    
    K first();
    
    K last();
    
    void changed();
    
    Comparator<? super K> comparator();
}
