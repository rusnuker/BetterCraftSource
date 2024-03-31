// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.io.Serializable;

public abstract class AbstractShortComparator implements ShortComparator, Serializable
{
    private static final long serialVersionUID = 0L;
    
    protected AbstractShortComparator() {
    }
    
    @Override
    public int compare(final Short ok1, final Short ok2) {
        return this.compare((short)ok1, (short)ok2);
    }
    
    @Override
    public abstract int compare(final short p0, final short p1);
}
