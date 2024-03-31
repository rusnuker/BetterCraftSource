// 
// Decompiled by Procyon v0.6.0
// 

package org.objectweb.asm.tree.analysis;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Iterator;
import java.util.AbstractSet;

class SmallSet extends AbstractSet implements Iterator
{
    Object e1;
    Object e2;
    
    static final Set emptySet() {
        return new SmallSet(null, null);
    }
    
    SmallSet(final Object e1, final Object e2) {
        this.e1 = e1;
        this.e2 = e2;
    }
    
    public Iterator iterator() {
        return new SmallSet(this.e1, this.e2);
    }
    
    public int size() {
        return (this.e1 == null) ? 0 : ((this.e2 == null) ? 1 : 2);
    }
    
    public boolean hasNext() {
        return this.e1 != null;
    }
    
    public Object next() {
        if (this.e1 == null) {
            throw new NoSuchElementException();
        }
        final Object e1 = this.e1;
        this.e1 = this.e2;
        this.e2 = null;
        return e1;
    }
    
    public void remove() {
    }
    
    Set union(final SmallSet set) {
        if ((set.e1 == this.e1 && set.e2 == this.e2) || (set.e1 == this.e2 && set.e2 == this.e1)) {
            return this;
        }
        if (set.e1 == null) {
            return this;
        }
        if (this.e1 == null) {
            return set;
        }
        if (set.e2 == null) {
            if (this.e2 == null) {
                return new SmallSet(this.e1, set.e1);
            }
            if (set.e1 == this.e1 || set.e1 == this.e2) {
                return this;
            }
        }
        if (this.e2 == null && (this.e1 == set.e1 || this.e1 == set.e2)) {
            return set;
        }
        final HashSet set2 = new HashSet(4);
        set2.add(this.e1);
        if (this.e2 != null) {
            set2.add(this.e2);
        }
        set2.add(set.e1);
        if (set.e2 != null) {
            set2.add(set.e2);
        }
        return set2;
    }
}
