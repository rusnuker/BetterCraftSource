// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.format;

import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEventSource;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.ClickEvent;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Set;
import java.util.Map;
import java.util.EnumMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface StyleSetter<T extends StyleSetter<?>>
{
    @NotNull
    T font(@Nullable final Key font);
    
    @NotNull
    T color(@Nullable final TextColor color);
    
    @NotNull
    T colorIfAbsent(@Nullable final TextColor color);
    
    @NotNull
    default T decorate(@NotNull final TextDecoration decoration) {
        return this.decoration(decoration, TextDecoration.State.TRUE);
    }
    
    @NotNull
    default T decorate(@NotNull final TextDecoration... decorations) {
        final Map<TextDecoration, TextDecoration.State> map = new EnumMap<TextDecoration, TextDecoration.State>(TextDecoration.class);
        for (int i = 0, length = decorations.length; i < length; ++i) {
            map.put(decorations[i], TextDecoration.State.TRUE);
        }
        return this.decorations(map);
    }
    
    @NotNull
    default T decoration(@NotNull final TextDecoration decoration, final boolean flag) {
        return this.decoration(decoration, TextDecoration.State.byBoolean(flag));
    }
    
    @NotNull
    T decoration(@NotNull final TextDecoration decoration, final TextDecoration.State state);
    
    @NotNull
    T decorationIfAbsent(@NotNull final TextDecoration decoration, final TextDecoration.State state);
    
    @NotNull
    T decorations(@NotNull final Map<TextDecoration, TextDecoration.State> decorations);
    
    @NotNull
    default T decorations(@NotNull final Set<TextDecoration> decorations, final boolean flag) {
        return this.decorations(decorations.stream().collect(Collectors.toMap((Function<? super Object, ? extends TextDecoration>)Function.identity(), decoration -> TextDecoration.State.byBoolean(flag))));
    }
    
    @NotNull
    T clickEvent(@Nullable final ClickEvent event);
    
    @NotNull
    T hoverEvent(@Nullable final HoverEventSource<?> source);
    
    @NotNull
    T insertion(@Nullable final String insertion);
}
