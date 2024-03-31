// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.chars;

import java.io.Serializable;

public class CharComparators
{
    public static final CharComparator NATURAL_COMPARATOR;
    public static final CharComparator OPPOSITE_COMPARATOR;
    
    private CharComparators() {
    }
    
    public static CharComparator oppositeComparator(final CharComparator c) {
        return new OppositeComparator(c);
    }
    
    static {
        NATURAL_COMPARATOR = new NaturalImplicitComparator();
        OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
    }
    
    protected static class NaturalImplicitComparator extends AbstractCharComparator implements Serializable
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public final int compare(final char a, final char b) {
            return Character.compare(a, b);
        }
        
        private Object readResolve() {
            return CharComparators.NATURAL_COMPARATOR;
        }
    }
    
    protected static class OppositeImplicitComparator extends AbstractCharComparator implements Serializable
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public final int compare(final char a, final char b) {
            return -Character.compare(a, b);
        }
        
        private Object readResolve() {
            return CharComparators.OPPOSITE_COMPARATOR;
        }
    }
    
    protected static class OppositeComparator extends AbstractCharComparator implements Serializable
    {
        private static final long serialVersionUID = 1L;
        private final CharComparator comparator;
        
        protected OppositeComparator(final CharComparator c) {
            this.comparator = c;
        }
        
        @Override
        public final int compare(final char a, final char b) {
            return this.comparator.compare(b, a);
        }
    }
}
