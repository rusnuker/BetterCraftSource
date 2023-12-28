// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;

public abstract class AbstractShortSet extends AbstractShortCollection implements Cloneable, ShortSet
{
    protected AbstractShortSet() {
    }
    
    @Override
    public abstract ShortIterator iterator();
    
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
        final ShortIterator i = this.iterator();
        while (n-- != 0) {
            final short k = i.nextShort();
            h += k;
        }
        return h;
    }
    
    @Override
    public boolean remove(final short k) {
        return this.rem(k);
    }
}
