// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.ints;

import java.io.Serializable;

public class IntComparators
{
    public static final IntComparator NATURAL_COMPARATOR;
    public static final IntComparator OPPOSITE_COMPARATOR;
    
    private IntComparators() {
    }
    
    public static IntComparator oppositeComparator(final IntComparator c) {
        return new OppositeComparator(c);
    }
    
    static {
        NATURAL_COMPARATOR = new NaturalImplicitComparator();
        OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
    }
    
    protected static class NaturalImplicitComparator extends AbstractIntComparator implements Serializable
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public final int compare(final int a, final int b) {
            return Integer.compare(a, b);
        }
        
        private Object readResolve() {
            return IntComparators.NATURAL_COMPARATOR;
        }
    }
    
    protected static class OppositeImplicitComparator extends AbstractIntComparator implements Serializable
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public final int compare(final int a, final int b) {
            return -Integer.compare(a, b);
        }
        
        private Object readResolve() {
            return IntComparators.OPPOSITE_COMPARATOR;
        }
    }
    
    protected static class OppositeComparator extends AbstractIntComparator implements Serializable
    {
        private static final long serialVersionUID = 1L;
        private final IntComparator comparator;
        
        protected OppositeComparator(final IntComparator c) {
            this.comparator = c;
        }
        
        @Override
        public final int compare(final int a, final int b) {
            return this.comparator.compare(b, a);
        }
    }
}
