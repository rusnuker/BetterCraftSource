// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.io.Serializable;

public abstract class AbstractFloatComparator implements FloatComparator, Serializable
{
    private static final long serialVersionUID = 0L;
    
    protected AbstractFloatComparator() {
    }
    
    @Override
    public int compare(final Float ok1, final Float ok2) {
        return this.compare((float)ok1, (float)ok2);
    }
    
    @Override
    public abstract int compare(final float p0, final float p1);
}
