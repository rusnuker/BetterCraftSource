// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.longs;

import java.io.Serializable;

public class LongComparators
{
    public static final LongComparator NATURAL_COMPARATOR;
    public static final LongComparator OPPOSITE_COMPARATOR;
    
    private LongComparators() {
    }
    
    public static LongComparator oppositeComparator(final LongComparator c) {
        return new OppositeComparator(c);
    }
    
    static {
        NATURAL_COMPARATOR = new NaturalImplicitComparator();
        OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
    }
    
    protected static class NaturalImplicitComparator extends AbstractLongComparator implements Serializable
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public final int compare(final long a, final long b) {
            return Long.compare(a, b);
        }
        
        private Object readResolve() {
            return LongComparators.NATURAL_COMPARATOR;
        }
    }
    
    protected static class OppositeImplicitComparator extends AbstractLongComparator implements Serializable
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public final int compare(final long a, final long b) {
            return -Long.compare(a, b);
        }
        
        private Object readResolve() {
            return LongComparators.OPPOSITE_COMPARATOR;
        }
    }
    
    protected static class OppositeComparator extends AbstractLongComparator implements Serializable
    {
        private static final long serialVersionUID = 1L;
        private final LongComparator comparator;
        
        protected OppositeComparator(final LongComparator c) {
            this.comparator = c;
        }
        
        @Override
        public final int compare(final long a, final long b) {
            return this.comparator.compare(b, a);
        }
    }
}
