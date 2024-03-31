// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;

public abstract class AbstractByteSet extends AbstractByteCollection implements Cloneable, ByteSet
{
    protected AbstractByteSet() {
    }
    
    @Override
    public abstract ByteIterator iterator();
    
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
        final ByteIterator i = this.iterator();
        while (n-- != 0) {
            final byte k = i.nextByte();
            h += k;
        }
        return h;
    }
    
    @Override
    public boolean remove(final byte k) {
        return this.rem(k);
    }
}
