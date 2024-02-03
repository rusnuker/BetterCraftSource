// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.key;

import org.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public interface KeyedValue<T> extends Keyed
{
    @NotNull
    default <T> KeyedValue<T> keyedValue(@NotNull final Key key, @NotNull final T value) {
        return new KeyedValueImpl<T>(key, Objects.requireNonNull(value, "value"));
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    @NotNull
    default <T> KeyedValue<T> of(@NotNull final Key key, @NotNull final T value) {
        return new KeyedValueImpl<T>(key, Objects.requireNonNull(value, "value"));
    }
    
    @NotNull
    T value();
}
