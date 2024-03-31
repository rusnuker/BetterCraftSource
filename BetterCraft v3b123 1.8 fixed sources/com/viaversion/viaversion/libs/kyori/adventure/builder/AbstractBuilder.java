// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.builder;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface AbstractBuilder<R>
{
    @Contract(mutates = "param1")
    @NotNull
    default <R, B extends AbstractBuilder<R>> R configureAndBuild(@NotNull final B builder, @Nullable final Consumer<? super B> consumer) {
        if (consumer != null) {
            consumer.accept(builder);
        }
        return builder.build();
    }
    
    @Contract(value = "-> new", pure = true)
    @NotNull
    R build();
}
