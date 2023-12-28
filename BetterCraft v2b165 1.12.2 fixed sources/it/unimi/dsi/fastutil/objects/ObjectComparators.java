// 
// Decompiled by Procyon v0.6.0
// 

package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;
import java.util.Comparator;

public class ObjectComparators
{
    public static final Comparator NATURAL_COMPARATOR;
    public static final Comparator OPPOSITE_COMPARATOR;
    
    private ObjectComparators() {
    }
    
    public static <K> Comparator<K> oppositeComparator(final Comparator<K> c) {
        return new OppositeComparator<K>(c);
    }
    
    static {
        NATURAL_COMPARATOR = new NaturalImplicitComparator();
        OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
    }
    
    protected static class NaturalImplicitComparator implements Comparator, Serializable
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public final int compare(final Object a, final Object b) {
            return ((Comparable)a).compareTo(b);
        }
        
        private Object readResolve() {
            return ObjectComparators.NATURAL_COMPARATOR;
        }
    }
    
    protected static class OppositeImplicitComparator implements Comparator, Serializable
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public final int compare(final Object a, final Object b) {
            return ((Comparable)b).compareTo(a);
        }
        
        private Object readResolve() {
            return ObjectComparators.OPPOSITE_COMPARATOR;
        }
    }
    
    protected static class OppositeComparator<K> implements Comparator<K>, Serializable
    {
        private static final long serialVersionUID = 1L;
        private final Comparator<K> comparator;
        
        protected OppositeComparator(final Comparator<K> c) {
            this.comparator = c;
        }
        
        @Override
        public final int compare(final K a, final K b) {
            return this.comparator.compare(b, a);
        }
    }
}
