// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.util.Iterator;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Collection;
import java.util.Set;

public abstract class AbstractFloatSet extends AbstractFloatCollection implements Cloneable, FloatSet
{
    protected AbstractFloatSet() {
    }
    
    @Override
    public abstract FloatIterator iterator();
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Set)) {
            return false;
        }
        final Set<?> s = (Set<?>)o;
        return s.size() == this.size() && this.containsAll(s);
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final FloatIterator i = this.iterator();
        while (n-- != 0) {
            final float k = i.nextFloat();
            h += HashCommon.float2int(k);
        }
        return h;
    }
    
    @Override
    public boolean remove(final float k) {
        return this.rem(k);
    }
}
