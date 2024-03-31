// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.base;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@FunctionalInterface
@GwtCompatible
public interface Predicate<T> extends java.util.function.Predicate<T>
{
    @CanIgnoreReturnValue
    boolean apply(@Nullable final T p0);
    
    boolean equals(@Nullable final Object p0);
    
    default boolean test(@Nullable final T input) {
        return this.apply(input);
    }
}
