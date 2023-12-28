// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.io.Serializable;

public abstract class AbstractIntComparator implements IntComparator, Serializable
{
    private static final long serialVersionUID = 0L;
    
    protected AbstractIntComparator() {
    }
    
    @Override
    public int compare(final Integer ok1, final Integer ok2) {
        return this.compare((int)ok1, (int)ok2);
    }
    
    @Override
    public abstract int compare(final int p0, final int p1);
}
