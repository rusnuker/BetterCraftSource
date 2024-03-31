// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.format;

import java.util.Set;
import java.util.Iterator;
import java.util.Objects;
import java.util.Map;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface MutableStyleSetter<T extends MutableStyleSetter<?>> extends StyleSetter<T>
{
    @Contract("_ -> this")
    @NotNull
    default T decorate(@NotNull final TextDecoration... decorations) {
        for (int i = 0, length = decorations.length; i < length; ++i) {
            this.decorate(decorations[i]);
        }
        return (T)this;
    }
    
    @Contract("_ -> this")
    @NotNull
    default T decorations(@NotNull final Map<TextDecoration, TextDecoration.State> decorations) {
        Objects.requireNonNull(decorations, "decorations");
        for (final Map.Entry<TextDecoration, TextDecoration.State> entry : decorations.entrySet()) {
            this.decoration(entry.getKey(), entry.getValue());
        }
        return (T)this;
    }
    
    @Contract("_, _ -> this")
    @NotNull
    default T decorations(@NotNull final Set<TextDecoration> decorations, final boolean flag) {
        final TextDecoration.State state = TextDecoration.State.byBoolean(flag);
        decorations.forEach(decoration -> this.decoration(decoration, state));
        return (T)this;
    }
}
