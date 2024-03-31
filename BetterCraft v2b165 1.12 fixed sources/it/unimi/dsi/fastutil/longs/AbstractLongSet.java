// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.util.Iterator;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Collection;
import java.util.Set;

public abstract class AbstractLongSet extends AbstractLongCollection implements Cloneable, LongSet
{
    protected AbstractLongSet() {
    }
    
    @Override
    public abstract LongIterator iterator();
    
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
        final LongIterator i = this.iterator();
        while (n-- != 0) {
            final long k = i.nextLong();
            h += HashCommon.long2int(k);
        }
        return h;
    }
    
    @Override
    public boolean remove(final long k) {
        return this.rem(k);
    }
}
