// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.bytes;

import java.io.Serializable;

public class ByteComparators
{
    public static final ByteComparator NATURAL_COMPARATOR;
    public static final ByteComparator OPPOSITE_COMPARATOR;
    
    private ByteComparators() {
    }
    
    public static ByteComparator oppositeComparator(final ByteComparator c) {
        return new OppositeComparator(c);
    }
    
    static {
        NATURAL_COMPARATOR = new NaturalImplicitComparator();
        OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
    }
    
    protected static class NaturalImplicitComparator extends AbstractByteComparator implements Serializable
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public final int compare(final byte a, final byte b) {
            return Byte.compare(a, b);
        }
        
        private Object readResolve() {
            return ByteComparators.NATURAL_COMPARATOR;
        }
    }
    
    protected static class OppositeImplicitComparator extends AbstractByteComparator implements Serializable
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public final int compare(final byte a, final byte b) {
            return -Byte.compare(a, b);
        }
        
        private Object readResolve() {
            return ByteComparators.OPPOSITE_COMPARATOR;
        }
    }
    
    protected static class OppositeComparator extends AbstractByteComparator implements Serializable
    {
        private static final long serialVersionUID = 1L;
        private final ByteComparator comparator;
        
        protected OppositeComparator(final ByteComparator c) {
            this.comparator = c;
        }
        
        @Override
        public final int compare(final byte a, final byte b) {
            return this.comparator.compare(b, a);
        }
    }
}
