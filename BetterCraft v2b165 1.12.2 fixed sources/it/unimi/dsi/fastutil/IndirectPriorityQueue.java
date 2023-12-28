// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil;

import java.util.Comparator;

public interface IndirectPriorityQueue<K>
{
    void enqueue(final int p0);
    
    int dequeue();
    
    boolean isEmpty();
    
    int size();
    
    void clear();
    
    int first();
    
    int last();
    
    void changed();
    
    Comparator<? super K> comparator();
    
    void changed(final int p0);
    
    void allChanged();
    
    boolean contains(final int p0);
    
    boolean remove(final int p0);
    
    int front(final int[] p0);
}
