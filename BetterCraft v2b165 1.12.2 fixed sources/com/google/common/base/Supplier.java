// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.base;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.GwtCompatible;

@FunctionalInterface
@GwtCompatible
public interface Supplier<T> extends java.util.function.Supplier<T>
{
    @CanIgnoreReturnValue
    T get();
}
