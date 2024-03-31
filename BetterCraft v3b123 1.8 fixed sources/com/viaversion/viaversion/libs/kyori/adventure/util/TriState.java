// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.util;

import org.jetbrains.annotations.NotNull;
import java.util.function.BooleanSupplier;
import org.jetbrains.annotations.Nullable;

public enum TriState
{
    NOT_SET, 
    FALSE, 
    TRUE;
    
    @Nullable
    public Boolean toBoolean() {
        switch (this) {
            case TRUE: {
                return Boolean.TRUE;
            }
            case FALSE: {
                return Boolean.FALSE;
            }
            default: {
                return null;
            }
        }
    }
    
    public boolean toBooleanOrElse(final boolean other) {
        switch (this) {
            case TRUE: {
                return true;
            }
            case FALSE: {
                return false;
            }
            default: {
                return other;
            }
        }
    }
    
    public boolean toBooleanOrElseGet(@NotNull final BooleanSupplier supplier) {
        switch (this) {
            case TRUE: {
                return true;
            }
            case FALSE: {
                return false;
            }
            default: {
                return supplier.getAsBoolean();
            }
        }
    }
    
    @NotNull
    public static TriState byBoolean(final boolean value) {
        return value ? TriState.TRUE : TriState.FALSE;
    }
    
    @NotNull
    public static TriState byBoolean(@Nullable final Boolean value) {
        return (value == null) ? TriState.NOT_SET : byBoolean((boolean)value);
    }
}
