// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.doubles;

import java.io.Serializable;

public class DoubleComparators
{
    public static final DoubleComparator NATURAL_COMPARATOR;
    public static final DoubleComparator OPPOSITE_COMPARATOR;
    
    private DoubleComparators() {
    }
    
    public static DoubleComparator oppositeComparator(final DoubleComparator c) {
        return new OppositeComparator(c);
    }
    
    static {
        NATURAL_COMPARATOR = new NaturalImplicitComparator();
        OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
    }
    
    protected static class NaturalImplicitComparator extends AbstractDoubleComparator implements Serializable
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public final int compare(final double a, final double b) {
            return Double.compare(a, b);
        }
        
        private Object readResolve() {
            return DoubleComparators.NATURAL_COMPARATOR;
        }
    }
    
    protected static class OppositeImplicitComparator extends AbstractDoubleComparator implements Serializable
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public final int compare(final double a, final double b) {
            return -Double.compare(a, b);
        }
        
        private Object readResolve() {
            return DoubleComparators.OPPOSITE_COMPARATOR;
        }
    }
    
    protected static class OppositeComparator extends AbstractDoubleComparator implements Serializable
    {
        private static final long serialVersionUID = 1L;
        private final DoubleComparator comparator;
        
        protected OppositeComparator(final DoubleComparator c) {
            this.comparator = c;
        }
        
        @Override
        public final int compare(final double a, final double b) {
            return this.comparator.compare(b, a);
        }
    }
}
