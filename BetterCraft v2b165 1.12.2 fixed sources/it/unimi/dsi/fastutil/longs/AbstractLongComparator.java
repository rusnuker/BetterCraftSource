// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.io.Serializable;

public abstract class AbstractLongComparator implements LongComparator, Serializable
{
    private static final long serialVersionUID = 0L;
    
    protected AbstractLongComparator() {
    }
    
    @Override
    public int compare(final Long ok1, final Long ok2) {
        return this.compare((long)ok1, (long)ok2);
    }
    
    @Override
    public abstract int compare(final long p0, final long p1);
}
