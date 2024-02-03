// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.util;

import org.jetbrains.annotations.Contract;
import com.viaversion.viaversion.libs.kyori.adventure.builder.AbstractBuilder;
import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public interface Buildable<R, B extends Builder<R>>
{
    @Deprecated
    @Contract(mutates = "param1")
    @NotNull
    default <R extends Buildable<R, B>, B extends Builder<R>> R configureAndBuild(@NotNull final B builder, @Nullable final Consumer<? super B> consumer) {
        return AbstractBuilder.configureAndBuild(builder, consumer);
    }
    
    @Contract(value = "-> new", pure = true)
    @NotNull
    B toBuilder();
    
    @Deprecated
    public interface Builder<R> extends AbstractBuilder<R>
    {
        @Contract(value = "-> new", pure = true)
        @NotNull
        R build();
    }
}
