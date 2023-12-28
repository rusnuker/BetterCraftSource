// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util;

public interface HashingStrategy<T>
{
    public static final HashingStrategy JAVA_HASHER = new HashingStrategy() {
        @Override
        public int hashCode(final Object obj) {
            return (obj != null) ? obj.hashCode() : 0;
        }
        
        @Override
        public boolean equals(final Object a, final Object b) {
            return a == b || (a != null && a.equals(b));
        }
    };
    
    int hashCode(final T p0);
    
    boolean equals(final T p0, final T p1);
}
