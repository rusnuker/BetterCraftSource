// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.booleans;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;

public abstract class AbstractBooleanSet extends AbstractBooleanCollection implements Cloneable, BooleanSet
{
    protected AbstractBooleanSet() {
    }
    
    @Override
    public abstract BooleanIterator iterator();
    
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
        final BooleanIterator i = this.iterator();
        while (n-- != 0) {
            final boolean k = i.nextBoolean();
            h += (k ? 1231 : 1237);
        }
        return h;
    }
    
    @Override
    public boolean remove(final boolean k) {
        return this.rem(k);
    }
}
