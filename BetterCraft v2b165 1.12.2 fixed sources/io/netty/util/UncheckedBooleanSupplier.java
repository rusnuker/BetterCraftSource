// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util;

public interface UncheckedBooleanSupplier extends BooleanSupplier
{
    public static final UncheckedBooleanSupplier FALSE_SUPPLIER = new UncheckedBooleanSupplier() {
        @Override
        public boolean get() {
            return false;
        }
    };
    public static final UncheckedBooleanSupplier TRUE_SUPPLIER = new UncheckedBooleanSupplier() {
        @Override
        public boolean get() {
            return true;
        }
    };
    
    boolean get();
}
