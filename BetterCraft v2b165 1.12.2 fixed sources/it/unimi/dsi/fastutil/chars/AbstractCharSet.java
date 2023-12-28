// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;

public abstract class AbstractCharSet extends AbstractCharCollection implements Cloneable, CharSet
{
    protected AbstractCharSet() {
    }
    
    @Override
    public abstract CharIterator iterator();
    
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
        final CharIterator i = this.iterator();
        while (n-- != 0) {
            final char k = i.nextChar();
            h += k;
        }
        return h;
    }
    
    @Override
    public boolean remove(final char k) {
        return this.rem(k);
    }
}
