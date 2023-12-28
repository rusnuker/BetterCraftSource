// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.io.Serializable;

public abstract class AbstractCharComparator implements CharComparator, Serializable
{
    private static final long serialVersionUID = 0L;
    
    protected AbstractCharComparator() {
    }
    
    @Override
    public int compare(final Character ok1, final Character ok2) {
        return this.compare((char)ok1, (char)ok2);
    }
    
    @Override
    public abstract int compare(final char p0, final char p1);
}
