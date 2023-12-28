// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.shorts;

import java.io.Serializable;

public class ShortComparators
{
    public static final ShortComparator NATURAL_COMPARATOR;
    public static final ShortComparator OPPOSITE_COMPARATOR;
    
    private ShortComparators() {
    }
    
    public static ShortComparator oppositeComparator(final ShortComparator c) {
        return new OppositeComparator(c);
    }
    
    static {
        NATURAL_COMPARATOR = new NaturalImplicitComparator();
        OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
    }
    
    protected static class NaturalImplicitComparator extends AbstractShortComparator implements Serializable
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public final int compare(final short a, final short b) {
            return Short.compare(a, b);
        }
        
        private Object readResolve() {
            return ShortComparators.NATURAL_COMPARATOR;
        }
    }
    
    protected static class OppositeImplicitComparator extends AbstractShortComparator implements Serializable
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public final int compare(final short a, final short b) {
            return -Short.compare(a, b);
        }
        
        private Object readResolve() {
            return ShortComparators.OPPOSITE_COMPARATOR;
        }
    }
    
    protected static class OppositeComparator extends AbstractShortComparator implements Serializable
    {
        private static final long serialVersionUID = 1L;
        private final ShortComparator comparator;
        
        protected OppositeComparator(final ShortComparator c) {
            this.comparator = c;
        }
        
        @Override
        public final int compare(final short a, final short b) {
            return this.comparator.compare(b, a);
        }
    }
}
