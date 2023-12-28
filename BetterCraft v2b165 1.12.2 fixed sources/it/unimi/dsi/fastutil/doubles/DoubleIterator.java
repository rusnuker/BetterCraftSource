// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import java.util.Iterator;

public interface DoubleIterator extends Iterator<Double>
{
    double nextDouble();
    
    int skip(final int p0);
}
