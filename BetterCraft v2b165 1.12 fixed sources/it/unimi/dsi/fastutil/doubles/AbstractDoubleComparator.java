// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import java.io.Serializable;

public abstract class AbstractDoubleComparator implements DoubleComparator, Serializable
{
    private static final long serialVersionUID = 0L;
    
    protected AbstractDoubleComparator() {
    }
    
    @Override
    public int compare(final Double ok1, final Double ok2) {
        return this.compare((double)ok1, (double)ok2);
    }
    
    @Override
    public abstract int compare(final double p0, final double p1);
}
