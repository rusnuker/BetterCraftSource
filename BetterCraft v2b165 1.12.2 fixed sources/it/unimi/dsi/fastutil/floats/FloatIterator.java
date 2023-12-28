// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.util.Iterator;

public interface FloatIterator extends Iterator<Float>
{
    float nextFloat();
    
    int skip(final int p0);
}
