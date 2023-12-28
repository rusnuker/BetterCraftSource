// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.floats;

import java.io.Serializable;

public class FloatComparators
{
    public static final FloatComparator NATURAL_COMPARATOR;
    public static final FloatComparator OPPOSITE_COMPARATOR;
    
    private FloatComparators() {
    }
    
    public static FloatComparator oppositeComparator(final FloatComparator c) {
        return new OppositeComparator(c);
    }
    
    static {
        NATURAL_COMPARATOR = new NaturalImplicitComparator();
        OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
    }
    
    protected static class NaturalImplicitComparator extends AbstractFloatComparator implements Serializable
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public final int compare(final float a, final float b) {
            return Float.compare(a, b);
        }
        
        private Object readResolve() {
            return FloatComparators.NATURAL_COMPARATOR;
        }
    }
    
    protected static class OppositeImplicitComparator extends AbstractFloatComparator implements Serializable
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public final int compare(final float a, final float b) {
            return -Float.compare(a, b);
        }
        
        private Object readResolve() {
            return FloatComparators.OPPOSITE_COMPARATOR;
        }
    }
    
    protected static class OppositeComparator extends AbstractFloatComparator implements Serializable
    {
        private static final long serialVersionUID = 1L;
        private final FloatComparator comparator;
        
        protected OppositeComparator(final FloatComparator c) {
            this.comparator = c;
        }
        
        @Override
        public final int compare(final float a, final float b) {
            return this.comparator.compare(b, a);
        }
    }
}
